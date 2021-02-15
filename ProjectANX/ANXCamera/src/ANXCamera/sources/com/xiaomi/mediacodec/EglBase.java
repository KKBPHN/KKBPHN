package com.xiaomi.mediacodec;

import android.graphics.SurfaceTexture;
import android.view.Surface;

public abstract class EglBase {
    static final int[] CONFIG_PLAIN = {12339, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, 8, 12352, 4, 12344};
    private static final int EGL_OPENGL_ES2_BIT = 4;
    public static final Object lock = new Object();

    class Context {
        Context() {
        }
    }

    public static EglBase create() {
        return create(null, CONFIG_PLAIN);
    }

    public static EglBase create(Context context) {
        return create(context, CONFIG_PLAIN);
    }

    public static EglBase create(Context context, int[] iArr) {
        return (!EglBase14.isEGL14Supported() || (context instanceof com.xiaomi.mediacodec.EglBase10.Context)) ? new EglBase10((com.xiaomi.mediacodec.EglBase10.Context) context, iArr) : new EglBase14((com.xiaomi.mediacodec.EglBase14.Context) context, iArr);
    }

    public static Context getCurrentContext() {
        return EglBase14.isEGL14Supported() ? EglBase14.getCurrentContext14() : EglBase10.getCurrentContext10();
    }

    public abstract void createPbufferSurface(int i, int i2);

    public abstract void createSurface(SurfaceTexture surfaceTexture);

    public abstract void createSurface(Surface surface);

    public abstract void detachCurrent();

    public abstract Context getEglBaseContext();

    public abstract int getSurfaceHeight();

    public abstract int getSurfaceWidth();

    public abstract boolean hasSurface();

    public abstract void makeCurrent();

    public abstract void release();

    public abstract void releaseSuface();

    public abstract void setPresentTime(long j);

    public abstract void swapBuffers();
}
