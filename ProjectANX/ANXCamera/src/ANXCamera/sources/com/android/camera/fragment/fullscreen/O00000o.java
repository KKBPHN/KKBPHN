package com.android.camera.fragment.fullscreen;

import com.android.camera.protocol.ModeProtocol.LiveVideoEditor;

/* compiled from: lambda */
public final /* synthetic */ class O00000o implements OnFrameUpdatedCallback {
    private final /* synthetic */ FragmentFullScreen O0OOoO0;
    private final /* synthetic */ LiveVideoEditor O0OOoOO;

    public /* synthetic */ O00000o(FragmentFullScreen fragmentFullScreen, LiveVideoEditor liveVideoEditor) {
        this.O0OOoO0 = fragmentFullScreen;
        this.O0OOoOO = liveVideoEditor;
    }

    public final void onUpdate() {
        this.O0OOoO0.O000000o(this.O0OOoOO);
    }
}
