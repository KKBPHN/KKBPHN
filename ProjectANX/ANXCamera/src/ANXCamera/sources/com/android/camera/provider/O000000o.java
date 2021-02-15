package com.android.camera.provider;

import android.content.Context;
import java.io.File;

/* compiled from: lambda */
public final /* synthetic */ class O000000o implements Runnable {
    private final /* synthetic */ SplashProvider O0OOoO0;
    private final /* synthetic */ Context O0OOoOO;
    private final /* synthetic */ File O0OOoOo;

    public /* synthetic */ O000000o(SplashProvider splashProvider, Context context, File file) {
        this.O0OOoO0 = splashProvider;
        this.O0OOoOO = context;
        this.O0OOoOo = file;
    }

    public final void run() {
        this.O0OOoO0.O000000o(this.O0OOoOO, this.O0OOoOo);
    }
}
