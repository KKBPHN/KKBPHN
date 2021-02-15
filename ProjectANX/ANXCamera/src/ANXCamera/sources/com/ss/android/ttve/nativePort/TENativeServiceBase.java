package com.ss.android.ttve.nativePort;

import androidx.annotation.Keep;
import com.ss.android.ttve.common.TECommonCallback;
import com.ss.android.ttve.nativePort.NativeCallbacks.IEncoderDataCallback;
import com.ss.android.ttve.nativePort.NativeCallbacks.IGetImageCallback;
import com.ss.android.ttve.nativePort.NativeCallbacks.IOpenGLCallback;

@Keep
public class TENativeServiceBase {
    protected IEncoderDataCallback mEncoderDataCallback = null;
    protected IGetImageCallback mGetImageCallback = null;
    protected TECommonCallback mOnErrorListener = null;
    protected TECommonCallback mOnInfoListener = null;
    protected IOpenGLCallback mOpenGLCallback = null;

    public IEncoderDataCallback getEncoderDataListener() {
        return this.mEncoderDataCallback;
    }

    public TECommonCallback getErrorListener() {
        return this.mOnErrorListener;
    }

    public TECommonCallback getInfoListener() {
        return this.mOnInfoListener;
    }

    public IOpenGLCallback getOpenGLListeners() {
        return this.mOpenGLCallback;
    }

    public void nativeCallback_onCompressBuffer(byte[] bArr, int i, int i2, boolean z) {
        IEncoderDataCallback iEncoderDataCallback = this.mEncoderDataCallback;
        if (iEncoderDataCallback != null) {
            iEncoderDataCallback.onCompressBuffer(bArr, i, i2, z);
        }
    }

    public void nativeCallback_onErrorListener(int i, int i2, float f, String str) {
        TECommonCallback tECommonCallback = this.mOnErrorListener;
        if (tECommonCallback != null) {
            tECommonCallback.onCallback(i, i2, f, str);
        }
    }

    public int nativeCallback_onImageData(byte[] bArr, int i, int i2, int i3) {
        IGetImageCallback iGetImageCallback = this.mGetImageCallback;
        if (iGetImageCallback != null) {
            return iGetImageCallback.onImageData(bArr, i, i2, i3);
        }
        return 0;
    }

    public void nativeCallback_onInfoListener(int i, int i2, float f) {
        TECommonCallback tECommonCallback = this.mOnInfoListener;
        if (tECommonCallback != null) {
            tECommonCallback.onCallback(i, i2, f, null);
        }
    }

    public void nativeCallback_onOpenGLCreate(int i) {
        IOpenGLCallback iOpenGLCallback = this.mOpenGLCallback;
        if (iOpenGLCallback != null) {
            iOpenGLCallback.onOpenGLCreate(i);
        }
    }

    public void nativeCallback_onOpenGLDestroy(int i) {
        IOpenGLCallback iOpenGLCallback = this.mOpenGLCallback;
        if (iOpenGLCallback != null) {
            iOpenGLCallback.onOpenGLDestroy(i);
        }
    }

    public void nativeCallback_onOpenGLDrawAfter(int i, double d) {
        IOpenGLCallback iOpenGLCallback = this.mOpenGLCallback;
        if (iOpenGLCallback != null) {
            iOpenGLCallback.onOpenGLDrawAfter(i, d);
        }
    }

    public void nativeCallback_onOpenGLDrawBefore(int i, double d) {
        IOpenGLCallback iOpenGLCallback = this.mOpenGLCallback;
        if (iOpenGLCallback != null) {
            iOpenGLCallback.onOpenGLDrawBefore(i, d);
        }
    }

    public void nativeCallback_onPreviewSurface(int i) {
        IOpenGLCallback iOpenGLCallback = this.mOpenGLCallback;
        if (iOpenGLCallback != null) {
            iOpenGLCallback.onPreviewSurface(i);
        }
    }

    public void setEncoderDataListener(IEncoderDataCallback iEncoderDataCallback) {
        this.mEncoderDataCallback = iEncoderDataCallback;
    }

    public void setErrorListener(TECommonCallback tECommonCallback) {
        this.mOnErrorListener = tECommonCallback;
    }

    public void setGetImageCallback(IGetImageCallback iGetImageCallback) {
        this.mGetImageCallback = iGetImageCallback;
    }

    public void setInfoListener(TECommonCallback tECommonCallback) {
        this.mOnInfoListener = tECommonCallback;
    }

    public void setOpenGLListeners(IOpenGLCallback iOpenGLCallback) {
        this.mOpenGLCallback = iOpenGLCallback;
    }
}
