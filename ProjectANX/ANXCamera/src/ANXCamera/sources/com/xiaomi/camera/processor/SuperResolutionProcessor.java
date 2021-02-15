package com.xiaomi.camera.processor;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.media.Image;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.android.camera.log.Log;
import com.android.camera2.vendortag.CaptureRequestVendorTags;
import com.xiaomi.camera.base.PerformanceTracker;
import com.xiaomi.camera.core.CaptureData;
import com.xiaomi.camera.core.CaptureData.CaptureDataBean;
import com.xiaomi.camera.core.CaptureDataListener;
import com.xiaomi.camera.imagecodec.ImagePool;
import com.xiaomi.camera.imagecodec.ImagePool.ImageFormat;
import com.xiaomi.engine.FrameData;
import com.xiaomi.engine.TaskSession;
import com.xiaomi.protocol.ICustomCaptureResult;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SuperResolutionProcessor implements AlgoProcessor {
    private static final String TAG = "SRProcessor";
    private int mOutputHeight;
    private int mOutputWidth;

    private void onImageAvailable(CaptureData captureData, CaptureDataBean captureDataBean, ProcessResultListener processResultListener) {
        captureData.setMultiFrameProcessResult(captureDataBean);
        processResultListener.onProcessFinished(captureData, !captureData.isMoonMode());
    }

    private void onImageAvailable(CaptureData captureData, List list, ProcessResultListener processResultListener) {
        captureData.setMultiFrameProcessResult((CaptureDataBean) list.get(0));
        captureData.setHDRSRResult(list);
        processResultListener.onProcessFinished(captureData, !captureData.isMoonMode());
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x00a9  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00ca  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void doProcess(@NonNull CaptureData captureData, ProcessResultListener processResultListener, TaskSession taskSession) {
        List<CaptureDataBean> list;
        boolean z;
        int i;
        Iterator it;
        FrameData frameData;
        CaptureData captureData2 = captureData;
        ProcessResultListener processResultListener2 = processResultListener;
        TaskSession taskSession2 = taskSession;
        String str = TAG;
        Log.d(str, "doProcess: E");
        List<CaptureDataBean> captureDataBeanList = captureData.getCaptureDataBeanList();
        if (captureDataBeanList == null || captureDataBeanList.isEmpty()) {
            throw new IllegalArgumentException("taskBeanList is not allow to be empty!");
        }
        CaptureDataBean captureDataBean = new CaptureDataBean(captureData.getStreamNum(), captureData.isRequireTuningData(), captureData.isSatFusionShot());
        StringBuilder sb = new StringBuilder();
        String str2 = "doProcess: dataNum = ";
        sb.append(str2);
        sb.append(captureDataBeanList.size());
        Log.d(str, sb.toString());
        String str3 = "[SR]";
        PerformanceTracker.trackAlgorithmProcess(str3, 0);
        ArrayList arrayList = new ArrayList();
        boolean isHdrSR = captureData.isHdrSR();
        ArrayList arrayList2 = null;
        if (isHdrSR) {
            arrayList2 = new ArrayList();
        }
        ArrayList arrayList3 = arrayList2;
        Iterator it2 = captureDataBeanList.iterator();
        while (it2.hasNext()) {
            CaptureDataBean captureDataBean2 = (CaptureDataBean) it2.next();
            Parcelable results = captureDataBean2.getResult().getResults();
            try {
                it = it2;
                try {
                    ((CameraMetadataNative) results).set(CaptureRequestVendorTags.CONTROL_SAT_FUSION_IMAGE_TYPE, Byte.valueOf(0));
                    Log.d(str, "update metadata with image flag: 0");
                } catch (Exception e) {
                    e = e;
                }
            } catch (Exception e2) {
                e = e2;
                it = it2;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("doProcess: Exception： ");
                sb2.append(e.getMessage());
                Log.e(str, sb2.toString());
                if (isHdrSR) {
                }
                arrayList.add(frameData);
                CaptureData captureData3 = captureData;
                it2 = it;
            }
            if (isHdrSR) {
                frameData = new FrameData(0, captureDataBean2.getResult().getSequenceId(), captureDataBean2.getResult().getFrameNumber(), results, captureDataBean2.getMainImage());
            } else {
                Integer num = (Integer) captureDataBean2.getResult().getRequest().get(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION);
                if (num == null || num.intValue() == 0) {
                    frameData = new FrameData(0, captureDataBean2.getResult().getSequenceId(), captureDataBean2.getResult().getFrameNumber(), results, captureDataBean2.getMainImage());
                } else {
                    arrayList3.add(captureDataBean2);
                    CaptureData captureData32 = captureData;
                    it2 = it;
                }
            }
            arrayList.add(frameData);
            CaptureData captureData322 = captureData;
            it2 = it;
        }
        Image mainImage = ((CaptureDataBean) captureDataBeanList.get(0)).getMainImage();
        int i2 = this.mOutputWidth;
        if (i2 <= 0) {
            i2 = mainImage.getWidth();
        }
        int i3 = this.mOutputHeight;
        if (i3 <= 0) {
            i3 = mainImage.getHeight();
        }
        Image anEmptyImage = ImagePool.getInstance().getAnEmptyImage(new ImageFormat(i2, i3, mainImage.getFormat()));
        int processFrameWithSync = taskSession2.processFrameWithSync(arrayList, anEmptyImage, 0);
        String str4 = "doProcess: returned a error baseIndex: ";
        if (processFrameWithSync > arrayList.size() || processFrameWithSync < 0) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str4);
            sb3.append(processFrameWithSync);
            Log.w(str, sb3.toString());
            processFrameWithSync = 0;
        }
        PerformanceTracker.trackAlgorithmProcess(str3, 1);
        StringBuilder sb4 = new StringBuilder();
        String str5 = "doProcess: SR done. baseIndex = ";
        sb4.append(str5);
        sb4.append(processFrameWithSync);
        Log.d(str, sb4.toString());
        CaptureDataBean captureDataBean3 = (CaptureDataBean) captureDataBeanList.get(processFrameWithSync);
        ICustomCaptureResult result = captureDataBean3.getResult();
        boolean z2 = isHdrSR;
        ArrayList arrayList4 = arrayList3;
        long timeStamp = result.getTimeStamp();
        if (captureData.isSatFusionShot()) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append(str2);
            sb5.append(captureDataBeanList.size());
            Log.d(str, sb5.toString());
            PerformanceTracker.trackAlgorithmProcess(str3, 0);
            ArrayList arrayList5 = new ArrayList();
            for (CaptureDataBean captureDataBean4 : captureDataBeanList) {
                Parcelable results2 = captureDataBean4.getResult().getResults();
                try {
                    ((CameraMetadataNative) results2).set(CaptureRequestVendorTags.CONTROL_SAT_FUSION_IMAGE_TYPE, Byte.valueOf(1));
                    Log.d(str, "update metadata with image flag: 1");
                } catch (Exception e3) {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("doProcess: sat fusion exception: ");
                    sb6.append(e3.getMessage());
                    Log.e(str, sb6.toString());
                }
                FrameData frameData2 = new FrameData(1, captureDataBean4.getResult().getSequenceId(), captureDataBean4.getResult().getFrameNumber(), results2, captureDataBean4.getSubImage());
                arrayList5.add(frameData2);
                ProcessResultListener processResultListener3 = processResultListener;
            }
            Image subImage = ((CaptureDataBean) captureDataBeanList.get(0)).getSubImage();
            list = captureDataBeanList;
            Image anEmptyImage2 = ImagePool.getInstance().getAnEmptyImage(new ImageFormat(subImage.getWidth(), subImage.getHeight(), subImage.getFormat()));
            int processFrameWithSync2 = taskSession2.processFrameWithSync(arrayList5, anEmptyImage2, 0);
            if (processFrameWithSync2 > arrayList5.size() || processFrameWithSync2 < 0) {
                StringBuilder sb7 = new StringBuilder();
                sb7.append(str4);
                sb7.append(processFrameWithSync2);
                Log.w(str, sb7.toString());
                i = 1;
                processFrameWithSync2 = 0;
            } else {
                i = 1;
            }
            PerformanceTracker.trackAlgorithmProcess(str3, i);
            StringBuilder sb8 = new StringBuilder();
            sb8.append(str5);
            sb8.append(processFrameWithSync2);
            Log.d(str, sb8.toString());
            anEmptyImage2.setTimestamp(timeStamp);
            ImagePool.getInstance().queueImage(anEmptyImage2);
            Image image = ImagePool.getInstance().getImage(timeStamp);
            z = true;
            captureDataBean.setImage(image, 1, true);
            ImagePool.getInstance().holdImage(image);
        } else {
            list = captureDataBeanList;
            z = true;
        }
        captureDataBean.setCaptureResult(result, z);
        if (captureData.isRequireTuningData()) {
            captureDataBean.setImage(captureDataBean3.getTuningImage(), 2, captureDataBean3.isTuningImageFromPool());
        }
        anEmptyImage.setTimestamp(timeStamp);
        ImagePool.getInstance().queueImage(anEmptyImage);
        Image image2 = ImagePool.getInstance().getImage(timeStamp);
        captureDataBean.setImage(image2, 0, true);
        ImagePool.getInstance().holdImage(image2);
        CaptureDataListener captureDataListener = captureData.getCaptureDataListener();
        for (CaptureDataBean captureDataBean5 : list) {
            ArrayList arrayList6 = arrayList4;
            if (captureDataBean5 != captureDataBean3 && (arrayList4 == null || !arrayList6.contains(captureDataBean5))) {
                Image mainImage2 = captureDataBean5.getMainImage();
                mainImage2.close();
                captureDataListener.onOriginalImageClosed(mainImage2);
                Image subImage2 = captureDataBean5.getSubImage();
                if (subImage2 != null) {
                    subImage2.close();
                    captureDataListener.onOriginalImageClosed(subImage2);
                }
                Image tuningImage = captureDataBean5.getTuningImage();
                if (tuningImage != null) {
                    tuningImage.close();
                    captureDataListener.onOriginalImageClosed(tuningImage);
                }
            }
            arrayList4 = arrayList6;
        }
        ArrayList arrayList7 = arrayList4;
        list.clear();
        Image mainImage3 = captureDataBean3.getMainImage();
        mainImage3.close();
        captureDataListener.onOriginalImageClosed(mainImage3);
        Image subImage3 = captureDataBean3.getSubImage();
        if (subImage3 != null) {
            if (captureData.isSatFusionShot()) {
                subImage3.close();
                captureDataListener.onOriginalImageClosed(subImage3);
            } else {
                long timestamp = subImage3.getTimestamp();
                ImagePool.getInstance().queueImage(subImage3);
                Image image3 = ImagePool.getInstance().getImage(timestamp);
                captureDataBean.setImage(image3, 1, true);
                captureDataListener.onOriginalImageClosed(subImage3);
                if (timestamp != timeStamp) {
                    image3.setTimestamp(timeStamp);
                }
                ImagePool.getInstance().holdImage(image3);
            }
        }
        if (captureDataBean.isDataReady()) {
            if (z2) {
                arrayList7.add(0, captureDataBean);
                onImageAvailable(captureData, (List) arrayList7, processResultListener);
            } else {
                onImageAvailable(captureData, captureDataBean, processResultListener);
            }
        }
        Log.d(str, "doProcess: X");
    }

    public void setOutputSize(int i, int i2) {
        if (i > 0 && i2 > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("setOutputSize: ");
            sb.append(i);
            sb.append("x");
            sb.append(i2);
            Log.d(TAG, sb.toString());
            this.mOutputWidth = i;
            this.mOutputHeight = i2;
        }
    }
}
