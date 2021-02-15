package com.android.camera.dualvideo.render;

import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O000o implements Consumer {
    public static final /* synthetic */ O000o INSTANCE = new O000o();

    private /* synthetic */ O000o() {
    }

    public final void accept(Object obj) {
        ((ActionProcessing) obj).setBackgroundColor(1);
    }
}
