package com.android.camera.dualvideo.recorder;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/* compiled from: lambda */
public final /* synthetic */ class O0000O0o implements ObservableOnSubscribe {
    private final /* synthetic */ MultiRecorderManager O0OOoO0;
    private final /* synthetic */ MiRecorder O0OOoOO;

    public /* synthetic */ O0000O0o(MultiRecorderManager multiRecorderManager, MiRecorder miRecorder) {
        this.O0OOoO0 = multiRecorderManager;
        this.O0OOoOO = miRecorder;
    }

    public final void subscribe(ObservableEmitter observableEmitter) {
        this.O0OOoO0.O000000o(this.O0OOoOO, observableEmitter);
    }
}
