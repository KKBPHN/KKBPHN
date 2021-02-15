package com.xiaomi.camera.rx;

import com.android.camera.constant.ExceptionConstant;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2OpenManager;
import com.android.camera.module.loader.camera2.Camera2Result;
import com.xiaomi.camera.device.CameraService;
import com.xiaomi.camera.device.callable.CameraListener;
import com.xiaomi.camera.device.callable.OpenCameraListener;
import io.reactivex.Single;
import io.reactivex.SingleObserver;

public class CameraOpenObservable extends Single {
    private static final String TAG = "CameraOpenObservable";
    private final String mCameraId;
    private final String[] mExclusions;

    final class Listener extends SimpleDisposable implements CameraListener, OpenCameraListener {
        private final String mCameraId;
        private final SingleObserver mObserver;

        private Listener(String str, SingleObserver singleObserver) {
            super(CameraService.getCameraCallableHandler());
            this.mCameraId = str;
            this.mObserver = singleObserver;
        }

        public void onClosed(String str) {
            StringBuilder sb = new StringBuilder();
            sb.append("onClosed: cid = ");
            sb.append(this.mCameraId);
            sb.append(", listener = ");
            sb.append(hashCode());
            Log.d(CameraOpenObservable.TAG, sb.toString());
            Camera2OpenManager.getInstance().onClosed(str);
        }

        public void onDisconnected(String str) {
            StringBuilder sb = new StringBuilder();
            sb.append("onDisconnected: cid = ");
            sb.append(this.mCameraId);
            sb.append(", listener = ");
            sb.append(hashCode());
            Log.d(CameraOpenObservable.TAG, sb.toString());
            Camera2OpenManager.getInstance().onDisconnected(str);
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            StringBuilder sb = new StringBuilder();
            sb.append("onDispose: listener: ");
            sb.append(hashCode());
            Log.d(CameraOpenObservable.TAG, sb.toString());
        }

        public void onError(String str, int i) {
            StringBuilder sb = new StringBuilder();
            sb.append("onError: cid = ");
            sb.append(this.mCameraId);
            sb.append(", listener = ");
            sb.append(hashCode());
            Log.d(CameraOpenObservable.TAG, sb.toString());
            Camera2OpenManager.getInstance().onError(str, i);
            if (!isDisposed()) {
                this.mObserver.onSuccess(Camera2Result.create(3).setCameraError(ExceptionConstant.transFromCamera2Error(i)));
            }
        }

        public void onFailure(Exception exc) {
            this.mObserver.onSuccess(Camera2Result.create(3).setCameraError(231));
        }

        public void onOpened(String str) {
            StringBuilder sb = new StringBuilder();
            sb.append("onOpened: cid = ");
            sb.append(this.mCameraId);
            sb.append(", listener = ");
            sb.append(hashCode());
            Log.d(CameraOpenObservable.TAG, sb.toString());
            Camera2OpenManager.getInstance().onOpened(str);
            if (!isDisposed()) {
                this.mObserver.onSuccess(Camera2Result.create(2));
            }
        }

        public void onSuccess(Void voidR) {
        }
    }

    private CameraOpenObservable(String str, String... strArr) {
        this.mCameraId = str;
        this.mExclusions = strArr;
    }

    public static CameraOpenObservable create(String str, String... strArr) {
        return new CameraOpenObservable(str, strArr);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        Listener listener = new Listener(this.mCameraId, singleObserver);
        singleObserver.onSubscribe(listener);
        StringBuilder sb = new StringBuilder();
        sb.append("subscribeActual: openCamera: cid = ");
        sb.append(this.mCameraId);
        sb.append(", listener = ");
        sb.append(listener.hashCode());
        Log.d(TAG, sb.toString());
        CameraService.openCamera(this.mCameraId, listener, listener, CameraService.getCameraCallableHandler(), this.mExclusions);
    }
}
