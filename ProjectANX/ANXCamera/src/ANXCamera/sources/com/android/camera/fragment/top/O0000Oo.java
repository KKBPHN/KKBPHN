package com.android.camera.fragment.top;

import com.android.camera.statistic.CameraStatUtils;

/* compiled from: lambda */
public final /* synthetic */ class O0000Oo implements Runnable {
    public static final /* synthetic */ O0000Oo INSTANCE = new O0000Oo();

    private /* synthetic */ O0000Oo() {
    }

    public final void run() {
        CameraStatUtils.trackLyingDirectShow(0);
    }
}
