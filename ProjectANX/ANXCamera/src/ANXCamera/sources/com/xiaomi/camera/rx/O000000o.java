package com.xiaomi.camera.rx;

/* compiled from: lambda */
public final /* synthetic */ class O000000o implements Runnable {
    public static final /* synthetic */ O000000o INSTANCE = new O000000o();

    private /* synthetic */ O000000o() {
    }

    public final void run() {
        CameraSchedulers.sIsCameraSetupThread.set(Boolean.TRUE);
    }
}
