package com.android.camera.tts;

import com.android.camera.tts.TTSHelper.Listener;

/* compiled from: lambda */
public final /* synthetic */ class O0000O0o implements Runnable {
    private final /* synthetic */ Listener O0OOoO0;
    private final /* synthetic */ boolean O0OOoOO;

    public /* synthetic */ O0000O0o(Listener listener, boolean z) {
        this.O0OOoO0 = listener;
        this.O0OOoOO = z;
    }

    public final void run() {
        this.O0OOoO0.onTTSStopped(this.O0OOoOO);
    }
}
