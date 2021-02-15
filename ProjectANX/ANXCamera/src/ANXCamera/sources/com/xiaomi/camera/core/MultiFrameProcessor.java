package com.xiaomi.camera.core;

import android.media.Image;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Size;
import com.android.camera.LocalParallelService;
import com.android.camera.log.Log;
import com.xiaomi.camera.core.CaptureData.CaptureDataBean;
import com.xiaomi.camera.imagecodec.ReprocessData;
import com.xiaomi.camera.imagecodec.ReprocessData.OnDataAvailableListener;
import com.xiaomi.camera.processor.ClearShotProcessor;
import com.xiaomi.camera.processor.GroupShotProcessor;
import com.xiaomi.camera.processor.MockMultiProcessor;
import com.xiaomi.camera.processor.ProcessResultListener;
import com.xiaomi.camera.processor.SuperResolutionProcessor;
import com.xiaomi.engine.TaskSession;
import java.io.File;
import java.util.List;
import miui.os.Build;

public class MultiFrameProcessor {
    private static final int REPROCESS_TIMEOUT_MS = 8000;
    /* access modifiers changed from: private */
    public static final String TAG = "MultiFrameProcessor";
    private final int MSG_PROCESS_DATA;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public final Object mObjLock;
    private ProcessResultListener mProcessResultListener;
    /* access modifiers changed from: private */
    public long mReprocessStartTime;
    /* access modifiers changed from: private */
    public boolean mReprocessing;
    private HandlerThread mWorkThread;

    class MultiFrameProcessorHolder {
        static MultiFrameProcessor INSTANCE = new MultiFrameProcessor();

        MultiFrameProcessorHolder() {
        }
    }

    class ProcessDataAndTaskSession {
        CaptureData data;
        TaskSession taskSession;

        public ProcessDataAndTaskSession(CaptureData captureData, TaskSession taskSession2) {
            this.data = captureData;
            this.taskSession = taskSession2;
        }
    }

    class WorkerHandler extends Handler {
        public WorkerHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            if (message.what != 1) {
                String access$100 = MultiFrameProcessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("unexpected message ");
                sb.append(message.what);
                Log.w(access$100, sb.toString());
                return;
            }
            ProcessDataAndTaskSession processDataAndTaskSession = (ProcessDataAndTaskSession) message.obj;
            MultiFrameProcessor.this.doProcess(processDataAndTaskSession.data, processDataAndTaskSession.taskSession);
        }
    }

    private MultiFrameProcessor() {
        this.mObjLock = new Object();
        this.mProcessResultListener = new ProcessResultListener() {
            public void onProcessFinished(CaptureData captureData, boolean z) {
                String access$100 = MultiFrameProcessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onProcessFinished: doReprocess = ");
                sb.append(z);
                Log.d(access$100, sb.toString());
                CaptureDataListener captureDataListener = captureData.getCaptureDataListener();
                if (captureDataListener == null) {
                    Log.w(MultiFrameProcessor.TAG, "onProcessFinished: null CaptureDataListener!");
                    if (captureData.isHdrSR()) {
                        List<CaptureDataBean> hDRSRResult = captureData.getHDRSRResult();
                        if (hDRSRResult != null) {
                            for (CaptureDataBean close : hDRSRResult) {
                                close.close();
                            }
                            hDRSRResult.clear();
                        }
                    } else {
                        CaptureDataBean multiFrameProcessResult = captureData.getMultiFrameProcessResult();
                        if (multiFrameProcessResult != null) {
                            multiFrameProcessResult.close();
                        }
                    }
                    for (CaptureDataBean captureDataBean : captureData.getCaptureDataBeanList()) {
                        if (captureDataBean != null) {
                            captureDataBean.close();
                        }
                    }
                    return;
                }
                if (z) {
                    CaptureDataBean multiFrameProcessResult2 = captureData.getMultiFrameProcessResult();
                    MultiFrameProcessor.this.reprocessImage(multiFrameProcessResult2, 0, captureData.isCapturedByFrontCamera());
                    if (multiFrameProcessResult2.isSatFusionShot()) {
                        MultiFrameProcessor.this.reprocessImage(multiFrameProcessResult2, 1, captureData.isCapturedByFrontCamera());
                    }
                }
                Log.d(MultiFrameProcessor.TAG, "onProcessFinished: dispatch image to algorithm engine");
                captureDataListener.onCaptureDataAvailable(captureData);
            }
        };
        this.MSG_PROCESS_DATA = 1;
        this.mWorkThread = new HandlerThread("MultiFrameProcessorThread");
        this.mWorkThread.start();
        this.mHandler = new WorkerHandler(this.mWorkThread.getLooper());
    }

    /* access modifiers changed from: private */
    public void doProcess(CaptureData captureData, TaskSession taskSession) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("doProcess: start process task: ");
        sb.append(captureData.getCaptureTimestamp());
        Log.d(str, sb.toString());
        if (PostProcessor.SKIP_MULTI) {
            Log.d(TAG, "prop skip multi");
            new MockMultiProcessor().doProcess(captureData, this.mProcessResultListener, null);
            return;
        }
        int algoType = captureData.getAlgoType();
        if (2 == algoType) {
            new ClearShotProcessor().doProcess(captureData, this.mProcessResultListener, null);
        } else if (5 == algoType) {
            new GroupShotProcessor().doProcess(captureData, this.mProcessResultListener, null);
        } else if (3 == algoType) {
            SuperResolutionProcessor superResolutionProcessor = new SuperResolutionProcessor();
            Size algoSize = captureData.getAlgoSize();
            if (algoSize != null) {
                superResolutionProcessor.setOutputSize(algoSize.getWidth(), algoSize.getHeight());
            }
            superResolutionProcessor.doProcess(captureData, this.mProcessResultListener, taskSession);
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("unknown multi-frame process algorithm type: ");
            sb2.append(algoType);
            throw new RuntimeException(sb2.toString());
        }
    }

    public static MultiFrameProcessor getInstance() {
        return MultiFrameProcessorHolder.INSTANCE;
    }

    /* access modifiers changed from: private */
    public void reprocessImage(CaptureDataBean captureDataBean, int i, boolean z) {
        Image subImage;
        final int i2 = i;
        final CaptureDataBean captureDataBean2 = captureDataBean;
        AnonymousClass2 r11 = new OnDataAvailableListener() {
            public void onError(String str, String str2) {
                synchronized (MultiFrameProcessor.this.mObjLock) {
                    String access$100 = MultiFrameProcessor.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onError>>tag=");
                    sb.append(str2);
                    sb.append(" reason=");
                    sb.append(str);
                    Log.v(access$100, sb.toString());
                    if (!Build.IS_DEBUGGABLE) {
                        MultiFrameProcessor.this.mReprocessing = false;
                        MultiFrameProcessor.this.mObjLock.notify();
                        String access$1002 = MultiFrameProcessor.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("onError<<cost=");
                        sb2.append(System.currentTimeMillis() - MultiFrameProcessor.this.mReprocessStartTime);
                        Log.v(access$1002, sb2.toString());
                    } else {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("reprocessImage failed image = ");
                        sb3.append(str2);
                        sb3.append(" reason = ");
                        sb3.append(str);
                        throw new RuntimeException(sb3.toString());
                    }
                }
            }

            public void onJpegAvailable(byte[] bArr, String str) {
                String access$100 = MultiFrameProcessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onJpegAvailable: unexpected. imageTag = ");
                sb.append(str);
                Log.w(access$100, sb.toString());
            }

            public void onJpegImageAvailable(Image image, String str, boolean z) {
                String access$100 = MultiFrameProcessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onJpegImageAvailable: unexpected. imageTag = ");
                sb.append(str);
                Log.w(access$100, sb.toString());
            }

            public void onTuningImageAvailable(Image image, String str, boolean z) {
            }

            public void onYuvAvailable(Image image, String str, boolean z) {
                synchronized (MultiFrameProcessor.this.mObjLock) {
                    String access$100 = MultiFrameProcessor.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onYuvAvailable>>tag=");
                    sb.append(str);
                    Log.v(access$100, sb.toString());
                    MultiFrameProcessor.this.mReprocessing = false;
                    captureDataBean2.setImage(image, i2, z);
                    MultiFrameProcessor.this.mObjLock.notify();
                    String access$1002 = MultiFrameProcessor.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onYuvAvailable<<cost=");
                    sb2.append(System.currentTimeMillis() - MultiFrameProcessor.this.mReprocessStartTime);
                    Log.v(access$1002, sb2.toString());
                }
            }
        };
        if (i2 == 0) {
            subImage = captureDataBean.getMainImage();
        } else if (i2 == 1) {
            subImage = captureDataBean.getSubImage();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("ImageType(");
            sb.append(i2);
            sb.append(") not supported.");
            throw new UnsupportedOperationException(sb.toString());
        }
        Image image = subImage;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(captureDataBean.getResult().getTimeStamp());
        sb2.append(File.separator);
        sb2.append(i2);
        String sb3 = sb2.toString();
        int width = image.getWidth();
        int height = image.getHeight();
        long timestamp = image.getTimestamp();
        String str = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("E: reprocessImage: timestamp = ");
        sb4.append(timestamp);
        sb4.append(", imageType = ");
        sb4.append(i2);
        Log.d(str, sb4.toString());
        synchronized (this.mObjLock) {
            this.mReprocessStartTime = System.currentTimeMillis();
            this.mReprocessing = true;
            ReprocessData reprocessData = r3;
            ReprocessData reprocessData2 = new ReprocessData(image, sb3, captureDataBean.getResult(), z, width, height, 35, (OnDataAvailableListener) r11);
            reprocessData.setImageFromPool(true);
            long currentTimeMillis = System.currentTimeMillis();
            try {
                LocalParallelService.getReprocessor().submit(reprocessData);
                while (this.mReprocessing) {
                    this.mObjLock.wait(8000);
                    this.mReprocessing = false;
                }
            } catch (IllegalArgumentException | IllegalStateException | InterruptedException e) {
                this.mReprocessing = false;
                Log.e(TAG, e.getMessage(), (Throwable) e);
            }
            if (System.currentTimeMillis() - currentTimeMillis >= 8000) {
                Log.e(TAG, "reprocessImage: frame %d is timeout", Long.valueOf(timestamp));
            }
        }
        String str2 = TAG;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("X: reprocessImage: timestamp = ");
        sb5.append(timestamp);
        sb5.append(", imageType = ");
        sb5.append(i2);
        Log.d(str2, sb5.toString());
    }

    public void processData(CaptureData captureData, TaskSession taskSession) {
        if (captureData.getBurstNum() != captureData.getCaptureDataBeanList().size()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Loss some capture data, burstNum is: ");
            sb.append(captureData.getBurstNum());
            sb.append("; but data bean list size is: ");
            sb.append(captureData.getCaptureDataBeanList().size());
            throw new RuntimeException(sb.toString());
        } else if (!this.mWorkThread.isAlive() || this.mHandler == null) {
            Log.w(TAG, "processData: sync mode");
            doProcess(captureData, taskSession);
        } else {
            String str = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("processData: queue task: ");
            sb2.append(captureData.getCaptureTimestamp());
            Log.v(str, sb2.toString());
            this.mHandler.obtainMessage(1, new ProcessDataAndTaskSession(captureData, taskSession)).sendToTarget();
        }
    }
}
