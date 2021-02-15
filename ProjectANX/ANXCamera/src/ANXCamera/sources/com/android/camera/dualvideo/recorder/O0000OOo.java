package com.android.camera.dualvideo.recorder;

import io.reactivex.SingleEmitter;
import io.reactivex.functions.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O0000OOo implements Consumer {
    private final /* synthetic */ MultiRecorderManager O0OOoO0;
    private final /* synthetic */ SingleEmitter O0OOoOO;
    private final /* synthetic */ long O0OOoOo;

    public /* synthetic */ O0000OOo(MultiRecorderManager multiRecorderManager, SingleEmitter singleEmitter, long j) {
        this.O0OOoO0 = multiRecorderManager;
        this.O0OOoOO = singleEmitter;
        this.O0OOoOo = j;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.O000000o(this.O0OOoOO, this.O0OOoOo, (Boolean) obj);
    }
}
