package com.android.camera.module.loader;

import android.graphics.Rect;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.params.Face;
import com.android.camera.effect.FaceAnalyzeInfo;
import com.android.camera.log.Log;
import com.android.camera2.Camera2Proxy.FaceDetectionCallback;
import com.android.camera2.CameraHardwareFace;
import com.android.camera2.vendortag.CaptureResultVendorTags;
import com.android.camera2.vendortag.VendorTagHelper;
import io.reactivex.functions.Function;
import java.lang.ref.WeakReference;

public class FunctionFaceDetect implements Function {
    private static final String TAG = "FunctionFaceDetect";
    private WeakReference mFaceCallbackReference;
    private FaceAnalyzeInfo mFaceInfo;
    private boolean mNeedFaceInfo;

    public FunctionFaceDetect(FaceDetectionCallback faceDetectionCallback, boolean z) {
        this.mFaceCallbackReference = new WeakReference(faceDetectionCallback);
        this.mNeedFaceInfo = z;
    }

    public CaptureResult apply(CaptureResult captureResult) {
        CameraHardwareFace[] cameraHardwareFaceArr;
        FaceDetectionCallback faceDetectionCallback = (FaceDetectionCallback) this.mFaceCallbackReference.get();
        if (faceDetectionCallback == null || !faceDetectionCallback.isFaceDetectStarted()) {
            return captureResult;
        }
        Face[] faceArr = (Face[]) captureResult.get(CaptureResult.STATISTICS_FACES);
        if (faceArr == null) {
            return captureResult;
        }
        boolean isUseFaceInfo = faceDetectionCallback.isUseFaceInfo();
        if (this.mNeedFaceInfo && isUseFaceInfo) {
            if (this.mFaceInfo == null) {
                this.mFaceInfo = new FaceAnalyzeInfo();
            }
            this.mFaceInfo.mAge = (float[]) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.STATISTICS_FACE_AGE);
            this.mFaceInfo.mGender = (float[]) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.STATISTICS_FACE_GENDER);
            this.mFaceInfo.mFaceScore = (float[]) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.STATISTICS_FACE_FACESCORE);
            this.mFaceInfo.mProp = (float[]) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.STATISTICS_FACE_PROP);
        }
        if (this.mNeedFaceInfo && isUseFaceInfo && faceArr.length > 0) {
            FaceAnalyzeInfo faceAnalyzeInfo = this.mFaceInfo;
            if (faceAnalyzeInfo.mAge != null) {
                cameraHardwareFaceArr = CameraHardwareFace.convertExCameraHardwareFace(faceArr, faceAnalyzeInfo);
                faceDetectionCallback.onFaceDetected(cameraHardwareFaceArr, this.mFaceInfo, (Rect) captureResult.getRequest().get(CaptureRequest.SCALER_CROP_REGION));
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("camera faces size:");
                sb.append(cameraHardwareFaceArr.length);
                Log.c(str, sb.toString());
                return captureResult;
            }
        }
        cameraHardwareFaceArr = CameraHardwareFace.convertCameraHardwareFace(faceArr);
        faceDetectionCallback.onFaceDetected(cameraHardwareFaceArr, this.mFaceInfo, (Rect) captureResult.getRequest().get(CaptureRequest.SCALER_CROP_REGION));
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("camera faces size:");
        sb2.append(cameraHardwareFaceArr.length);
        Log.c(str2, sb2.toString());
        return captureResult;
    }
}
