package com.xiaomi.camera.core;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.LongSparseArray;
import androidx.annotation.NonNull;
import com.android.camera.log.Log;
import com.xiaomi.camera.core.CaptureData.CaptureDataBean;
import com.xiaomi.protocol.ICustomCaptureResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import miui.os.Build;

public class ParallelDataZipper {
    /* access modifiers changed from: private */
    public static final String TAG = "ParallelDataZipper";
    /* access modifiers changed from: private */
    @SuppressLint({"UseSparseArrays"})
    public final Map mCaptureDataArray;
    /* access modifiers changed from: private */
    public LongSparseArray mCaptureDataBeanArray;
    private Handler mHandler;
    private final LongSparseArray mUnTrackCaptureDataBeanMap;
    private HandlerThread mWorkThread;

    public interface DataListener {
        void onParallelDataAbandoned(CaptureData captureData);

        void onParallelDataAvailable(CaptureData captureData);
    }

    class InstanceHolder {
        static ParallelDataZipper INSTANCE = new ParallelDataZipper();

        InstanceHolder() {
        }
    }

    private ParallelDataZipper() {
        this.mCaptureDataBeanArray = new LongSparseArray(4);
        this.mCaptureDataArray = new HashMap(4);
        this.mUnTrackCaptureDataBeanMap = new LongSparseArray(4);
        this.mWorkThread = new HandlerThread("ParallelDataZipperThread");
        this.mWorkThread.start();
        this.mHandler = new Handler(this.mWorkThread.getLooper());
    }

    /* access modifiers changed from: private */
    public long getFirstFrameTimestamp(long j) {
        if (this.mCaptureDataArray.containsKey(Long.valueOf(j))) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("getFirstFrameTimestamp: return current timestamp: ");
            sb.append(j);
            Log.d(str, sb.toString());
            return j;
        }
        int i = 0;
        Long[] lArr = (Long[]) this.mCaptureDataArray.keySet().toArray(new Long[0]);
        if (!(lArr == null || lArr.length == 0)) {
            Arrays.sort(lArr);
            if (lArr.length == 1) {
                return lArr[0].longValue();
            }
            while (i <= lArr.length - 2) {
                if (j > lArr[i].longValue() && j < lArr[i + 1].longValue()) {
                    return lArr[i].longValue();
                }
                i++;
            }
            if (j > lArr[lArr.length - 1].longValue()) {
                return lArr[lArr.length - 1].longValue();
            }
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getFirstFrameTimestamp: return an error result with timestamp: ");
        sb2.append(j);
        Log.e(str2, sb2.toString());
        return 0;
    }

    public static ParallelDataZipper getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private int getStreamNumberByTimestamp(long j) {
        CaptureData captureData = (CaptureData) this.mCaptureDataArray.get(Long.valueOf(getFirstFrameTimestamp(j)));
        if (captureData != null) {
            return captureData.getStreamNum();
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getStreamNumberByTimestamp: returned an error result with timestamp: ");
        sb.append(j);
        Log.e(str, sb.toString());
        return 0;
    }

    /* access modifiers changed from: private */
    public void handleData(CaptureDataBean captureDataBean, long j, long j2) {
        if (captureDataBean.isDataReady()) {
            List list = (List) this.mUnTrackCaptureDataBeanMap.get(j);
            if (list != null) {
                list.remove(Long.valueOf(j2));
                if (list.size() == 0) {
                    this.mUnTrackCaptureDataBeanMap.remove(j);
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("handleData: removed. untrack size: ");
                    sb.append(this.mUnTrackCaptureDataBeanMap.size());
                    Log.d(str, sb.toString());
                }
            }
            this.mCaptureDataBeanArray.remove(j2);
            tryToCallback(captureDataBean);
        } else {
            List list2 = (List) this.mUnTrackCaptureDataBeanMap.get(j);
            if (list2 == null) {
                list2 = new ArrayList();
            }
            if (!list2.contains(Long.valueOf(j2))) {
                list2.add(Long.valueOf(j2));
                this.mUnTrackCaptureDataBeanMap.put(j, list2);
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("handleData: added. firstTimestamp: ");
                sb2.append(j);
                sb2.append(", timestamp ");
                sb2.append(j2);
                sb2.append("'s child size: ");
                sb2.append(list2.size());
                Log.d(str2, sb2.toString());
            }
        }
    }

    private void removeUselessUnTrackCaptureDataBean(long j) {
        if (this.mUnTrackCaptureDataBeanMap.size() > 0) {
            List list = (List) this.mUnTrackCaptureDataBeanMap.get(0);
            if (list != null && list.contains(Long.valueOf(j))) {
                list.remove(Long.valueOf(j));
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("removeUselessUnTrackCaptureDataBean: timestamp > ");
                sb.append(j);
                Log.d(str, sb.toString());
            }
        }
    }

    private void tryToCallback(CaptureDataBean captureDataBean) {
        Integer valueOf = Integer.valueOf(captureDataBean.getResult().getSequenceId());
        long timeStamp = captureDataBean.getResult().getTimeStamp();
        long firstFrameTimestamp = getFirstFrameTimestamp(timeStamp);
        CaptureData captureData = (CaptureData) this.mCaptureDataArray.get(Long.valueOf(firstFrameTimestamp));
        if (captureData != null) {
            captureData.putCaptureDataBean(captureDataBean);
            if (captureData.isDataReady()) {
                DataListener dataListener = captureData.getDataListener();
                if (dataListener != null) {
                    if (!captureData.isAbandoned()) {
                        dataListener.onParallelDataAvailable(captureData);
                    } else {
                        dataListener.onParallelDataAbandoned(captureData);
                    }
                }
                this.mCaptureDataArray.remove(Long.valueOf(firstFrameTimestamp));
                return;
            }
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("No task found with sequenceId: ");
        sb.append(valueOf);
        sb.append(" timestamp: ");
        sb.append(timeStamp);
        sb.append("|");
        sb.append(firstFrameTimestamp);
        String sb2 = sb.toString();
        Log.e(TAG, sb2, (Throwable) new RuntimeException(sb2));
        removeUselessUnTrackCaptureDataBean(timeStamp);
        captureDataBean.close();
        if (Build.IS_DEBUGGABLE) {
            throw new RuntimeException(sb2);
        }
    }

    public /* synthetic */ void O00000o(long j) {
        long firstFrameTimestamp = getFirstFrameTimestamp(j);
        List<Long> list = (List) this.mUnTrackCaptureDataBeanMap.get(firstFrameTimestamp);
        if (list != null) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("releaseData: unTrackDatas size ");
            sb.append(list.size());
            Log.d(str, sb.toString());
            for (Long l : list) {
                CaptureDataBean captureDataBean = (CaptureDataBean) this.mCaptureDataBeanArray.get(l.longValue());
                if (captureDataBean != null) {
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("releaseData: close untrack image : ");
                    sb2.append(l);
                    Log.d(str2, sb2.toString());
                    captureDataBean.close();
                    this.mCaptureDataBeanArray.remove(l.longValue());
                }
            }
            this.mUnTrackCaptureDataBeanMap.remove(firstFrameTimestamp);
        }
        CaptureData captureData = (CaptureData) this.mCaptureDataArray.get(Long.valueOf(firstFrameTimestamp));
        if (captureData != null) {
            if (captureData.getCaptureDataBeanList() != null) {
                for (CaptureDataBean timestamp : captureData.getCaptureDataBeanList()) {
                    long timestamp2 = timestamp.getTimestamp();
                    this.mCaptureDataBeanArray.remove(timestamp2);
                    String str3 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("releaseData: CaptureDataBean.timestamp > ");
                    sb3.append(timestamp2);
                    Log.d(str3, sb3.toString());
                }
            }
            DataListener dataListener = captureData.getDataListener();
            if (dataListener != null) {
                dataListener.onParallelDataAbandoned(captureData);
            }
            this.mCaptureDataArray.remove(Long.valueOf(firstFrameTimestamp));
        } else {
            Log.w(TAG, "releaseData: CaptureData is null");
            CaptureDataBean captureDataBean2 = (CaptureDataBean) this.mCaptureDataBeanArray.get(j);
            if (captureDataBean2 != null) {
                captureDataBean2.close();
                this.mCaptureDataBeanArray.remove(j);
            }
        }
        String str4 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("clearTimeStampData: X | mCaptureDataBeanArray.size > ");
        sb4.append(this.mCaptureDataBeanArray.size());
        sb4.append("|mCaptureDataArray.size > ");
        sb4.append(this.mCaptureDataArray.size());
        Log.d(str4, sb4.toString());
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public boolean isAnyFrontProcessingByProcessor(ImageProcessor imageProcessor) {
        for (CaptureData captureData : this.mCaptureDataArray.values()) {
            ImageProcessor imageProcessor2 = captureData.getImageProcessor();
            if (imageProcessor2 == null) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("WARNING: isAnyFrontProcessingByProcessor: ImageProcessor is null! captureData = ");
                sb.append(captureData);
                Log.w(str, sb.toString());
            } else if (!captureData.isAbandoned() && Objects.equals(imageProcessor2, imageProcessor)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void join(Image image, int i, int i2, boolean z) {
        if (this.mWorkThread.isAlive() && this.mHandler != null) {
            Handler handler = this.mHandler;
            final Image image2 = image;
            final int i3 = i2;
            final boolean z2 = z;
            final int i4 = i;
            AnonymousClass3 r1 = new Runnable() {
                public void run() {
                    boolean z;
                    int i;
                    boolean z2;
                    Image image;
                    long timestamp = image2.getTimestamp();
                    CaptureDataBean captureDataBean = (CaptureDataBean) ParallelDataZipper.this.mCaptureDataBeanArray.get(timestamp);
                    long access$300 = ParallelDataZipper.this.getFirstFrameTimestamp(timestamp);
                    CaptureData captureData = (CaptureData) ParallelDataZipper.this.mCaptureDataArray.get(Long.valueOf(access$300));
                    int i2 = 0;
                    if (captureData == null) {
                        String access$100 = ParallelDataZipper.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("setImage: no capture data with timestamp: ");
                        sb.append(access$300);
                        Log.e(access$100, sb.toString());
                        z2 = false;
                        i = 0;
                        z = false;
                    } else {
                        i = captureData.getStreamNum();
                        z = captureData.isRequireTuningData();
                        z2 = captureData.isSatFusionShot();
                    }
                    if (captureDataBean == null) {
                        captureDataBean = new CaptureDataBean(i, z, z2);
                        ParallelDataZipper.this.mCaptureDataBeanArray.append(timestamp, captureDataBean);
                    }
                    if (!(captureDataBean.getStreamNum() == i || i == 0)) {
                        String access$1002 = ParallelDataZipper.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("setImage: update stream number with: ");
                        sb2.append(i);
                        Log.d(access$1002, sb2.toString());
                        captureDataBean.setStreamNum(i);
                    }
                    if (captureDataBean.isRequireTuningData() != z) {
                        captureDataBean.setRequireTuningData(z);
                    }
                    if (captureDataBean.isSatFusionShot() != z2) {
                        captureDataBean.setIsSatFusionShot(z2);
                    }
                    String access$1003 = ParallelDataZipper.TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("setImage: timestamp=");
                    sb3.append(timestamp);
                    sb3.append(", streamNum=");
                    sb3.append(captureDataBean.getStreamNum());
                    sb3.append(", fusion = ");
                    sb3.append(z2);
                    Log.d(access$1003, sb3.toString());
                    if (!z2) {
                        image = image2;
                        i2 = i4;
                    } else if (i3 == 6) {
                        image = image2;
                        i2 = 1;
                    } else {
                        image = image2;
                    }
                    captureDataBean.setImage(image, i2, z2);
                    ParallelDataZipper.this.handleData(captureDataBean, access$300, timestamp);
                }
            };
            handler.post(r1);
        }
    }

    public synchronized void join(@NonNull final ICustomCaptureResult iCustomCaptureResult, final boolean z) {
        if (!this.mWorkThread.isAlive() || this.mHandler == null) {
            throw new RuntimeException("Thread already die!");
        }
        this.mHandler.post(new Runnable() {
            public void run() {
                boolean z;
                boolean z2;
                long timeStamp = iCustomCaptureResult.getTimeStamp();
                int sequenceId = iCustomCaptureResult.getSequenceId();
                long access$300 = ParallelDataZipper.this.getFirstFrameTimestamp(timeStamp);
                CaptureData captureData = (CaptureData) ParallelDataZipper.this.mCaptureDataArray.get(Long.valueOf(access$300));
                int i = 0;
                if (captureData == null) {
                    String access$100 = ParallelDataZipper.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("setResult: no capture data with timestamp: ");
                    sb.append(access$300);
                    Log.e(access$100, sb.toString());
                    z2 = false;
                    z = false;
                } else {
                    i = captureData.getStreamNum();
                    z = captureData.isRequireTuningData();
                    z2 = captureData.isSatFusionShot();
                }
                CaptureDataBean captureDataBean = (CaptureDataBean) ParallelDataZipper.this.mCaptureDataBeanArray.get(timeStamp);
                if (captureDataBean == null) {
                    captureDataBean = new CaptureDataBean(i, z, z2);
                    ParallelDataZipper.this.mCaptureDataBeanArray.append(timeStamp, captureDataBean);
                }
                if (!(captureDataBean.getStreamNum() == i || i == 0)) {
                    String access$1002 = ParallelDataZipper.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("setResult: update stream number with: ");
                    sb2.append(i);
                    Log.d(access$1002, sb2.toString());
                    captureDataBean.setStreamNum(i);
                }
                if (captureDataBean.isRequireTuningData() != z) {
                    captureDataBean.setRequireTuningData(z);
                }
                if (captureDataBean.isSatFusionShot() != z2) {
                    captureDataBean.setIsSatFusionShot(z2);
                }
                captureDataBean.setCaptureResult(iCustomCaptureResult, z);
                String access$1003 = ParallelDataZipper.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("setResult: timestamp=");
                sb3.append(timeStamp);
                sb3.append(" sequenceId=");
                sb3.append(sequenceId);
                sb3.append(" streamNum=");
                sb3.append(captureDataBean.getStreamNum());
                sb3.append(" isFirst=");
                sb3.append(z);
                sb3.append(" fusion = ");
                sb3.append(z2);
                Log.d(access$1003, sb3.toString());
                ParallelDataZipper.this.handleData(captureDataBean, access$300, timeStamp);
            }
        });
    }

    public void releaseData(long j) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("releaseData: E. timestamp: ");
        sb.append(j);
        Log.d(str, sb.toString());
        if (this.mWorkThread.isAlive()) {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.post(new O000000o(this, j));
            }
        }
    }

    public synchronized void startTask(@NonNull final CaptureData captureData) {
        if (!this.mWorkThread.isAlive() || this.mHandler == null) {
            throw new RuntimeException("Thread already die!");
        }
        this.mHandler.post(new Runnable() {
            public void run() {
                String access$100 = ParallelDataZipper.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("startTask: ");
                sb.append(captureData);
                Log.k(3, access$100, sb.toString());
                ParallelDataZipper.this.mCaptureDataArray.put(Long.valueOf(captureData.getCaptureTimestamp()), captureData);
                ParallelDataZipper.this.updateUnTrackCaptureDataBeanMap(captureData.getCaptureTimestamp());
            }
        });
    }

    public void updateUnTrackCaptureDataBeanMap(long j) {
        if (this.mUnTrackCaptureDataBeanMap.size() > 0 && this.mUnTrackCaptureDataBeanMap.indexOfKey(j) < 0) {
            List list = (List) this.mUnTrackCaptureDataBeanMap.get(0);
            if (list != null) {
                this.mUnTrackCaptureDataBeanMap.remove(0);
                this.mUnTrackCaptureDataBeanMap.put(j, list);
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("updateUnTrackCaptureDataBeanMap: update 0 to firstTimestamp  > ");
                sb.append(j);
                Log.i(str, sb.toString());
            }
        }
    }
}
