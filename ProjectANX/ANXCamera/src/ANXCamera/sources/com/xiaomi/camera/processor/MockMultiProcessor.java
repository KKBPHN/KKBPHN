package com.xiaomi.camera.processor;

import android.media.Image;
import androidx.annotation.NonNull;
import com.xiaomi.camera.core.CaptureData;
import com.xiaomi.camera.core.CaptureData.CaptureDataBean;
import com.xiaomi.camera.core.CaptureDataListener;
import com.xiaomi.engine.TaskSession;

public class MockMultiProcessor implements AlgoProcessor {
    private static final String TAG = "MockMulti";

    private void onImageAvailable(CaptureData captureData, CaptureDataBean captureDataBean, ProcessResultListener processResultListener) {
        captureData.setMultiFrameProcessResult(captureDataBean);
        processResultListener.onProcessFinished(captureData, false);
    }

    public void doProcess(@NonNull CaptureData captureData, ProcessResultListener processResultListener, TaskSession taskSession) {
        CaptureDataBean captureDataBean = (CaptureDataBean) captureData.getCaptureDataBeanList().get(0);
        captureDataBean.setCaptureResult(captureDataBean.getResult(), true);
        CaptureDataListener captureDataListener = captureData.getCaptureDataListener();
        for (int i = 1; i < captureData.getCaptureDataBeanList().size(); i++) {
            CaptureDataBean captureDataBean2 = (CaptureDataBean) captureData.getCaptureDataBeanList().get(i);
            Image mainImage = captureDataBean2.getMainImage();
            Image subImage = captureDataBean2.getSubImage();
            Image tuningImage = captureDataBean2.getTuningImage();
            if (mainImage != null) {
                mainImage.close();
                captureDataListener.onOriginalImageClosed(mainImage);
            }
            if (subImage != null) {
                subImage.close();
                captureDataListener.onOriginalImageClosed(subImage);
            }
            if (tuningImage != null) {
                tuningImage.close();
                captureDataListener.onOriginalImageClosed(tuningImage);
            }
        }
        captureData.getCaptureDataBeanList().clear();
        if (captureDataBean.isDataReady()) {
            onImageAvailable(captureData, captureDataBean, processResultListener);
        }
    }
}
