package com.android.camera.module.loader.camera2;

import com.android.camera.data.DataRepository;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.log.Log;
import com.android.camera.module.ModuleManager;
import com.android.camera.module.loader.SurfaceCreatedCallback;
import com.android.camera.module.loader.SurfaceStateListener;
import com.android.camera.snap.SnapTrigger;
import io.reactivex.Observer;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;

public class Camera2OpenOnSubscribe implements SingleOnSubscribe, SurfaceCreatedCallback, Observer {
    private static final String TAG = "Camera2OpenOnSubScribe";
    private Camera2Result mCamera2Result;
    private SingleEmitter mSingleEmitter;
    private final SurfaceStateListener mSurfaceStateListener;

    public Camera2OpenOnSubscribe(SurfaceStateListener surfaceStateListener) {
        this.mSurfaceStateListener = surfaceStateListener;
    }

    private void submitResult(Camera2Result camera2Result) {
        SingleEmitter singleEmitter = this.mSingleEmitter;
        if (singleEmitter != null) {
            singleEmitter.onSuccess(camera2Result);
        }
    }

    public void onComplete() {
        Log.d(TAG, "onComplete");
    }

    public void onError(Throwable th) {
        StringBuilder sb = new StringBuilder();
        sb.append("onError: hasSurface = ");
        sb.append(this.mSurfaceStateListener.hasSurface());
        Log.d(TAG, sb.toString());
        this.mCamera2Result = Camera2Result.create(3);
        submitResult(this.mCamera2Result);
    }

    public void onGlSurfaceCreated() {
        StringBuilder sb = new StringBuilder();
        sb.append("onGlSurfaceCreated: mSingleEmitter = ");
        sb.append(this.mSingleEmitter);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        SingleEmitter singleEmitter = this.mSingleEmitter;
        if (singleEmitter == null || singleEmitter.isDisposed()) {
            Log.d(str, "onGlSurfaceCreated: mSingleEmitter already disposed");
            return;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("onGlSurfaceCreated: mCamera2Result = ");
        sb3.append(this.mCamera2Result);
        Log.d(str, sb3.toString());
        Camera2Result camera2Result = this.mCamera2Result;
        if (camera2Result != null) {
            submitResult(camera2Result);
        }
    }

    public void onNext(Camera2Result camera2Result) {
        boolean isCapture = ModuleManager.isCapture();
        boolean hasSurface = this.mSurfaceStateListener.hasSurface();
        StringBuilder sb = new StringBuilder();
        sb.append("onNext: hasSurface = ");
        sb.append(hasSurface);
        sb.append(", isCapture = ");
        sb.append(isCapture);
        Log.d(TAG, sb.toString());
        this.mCamera2Result = camera2Result;
        if (isCapture || hasSurface) {
            submitResult(camera2Result);
        }
    }

    public void onSubscribe(Disposable disposable) {
        Log.d(TAG, "onSubscribe");
    }

    public void subscribe(SingleEmitter singleEmitter) {
        this.mCamera2Result = null;
        this.mSingleEmitter = singleEmitter;
        if (SnapTrigger.getInstance().isRunning()) {
            SnapTrigger.getInstance();
            SnapTrigger.destroy();
        }
        DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
        int currentCameraId = dataItemGlobal.getCurrentCameraId();
        int currentMode = dataItemGlobal.getCurrentMode();
        StringBuilder sb = new StringBuilder();
        sb.append("subscribe: request to open ");
        sb.append(currentCameraId);
        Log.d(TAG, sb.toString());
        Camera2OpenManager.getInstance().openCamera(currentCameraId, currentMode, this, false);
    }
}
