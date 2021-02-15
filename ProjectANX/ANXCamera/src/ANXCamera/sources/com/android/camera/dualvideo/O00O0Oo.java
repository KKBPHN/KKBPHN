package com.android.camera.dualvideo;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/* compiled from: lambda */
public final /* synthetic */ class O00O0Oo implements SingleOnSubscribe {
    private final /* synthetic */ DualVideoRecordModule O0OOoO0;

    public /* synthetic */ O00O0Oo(DualVideoRecordModule dualVideoRecordModule) {
        this.O0OOoO0 = dualVideoRecordModule;
    }

    public final void subscribe(SingleEmitter singleEmitter) {
        this.O0OOoO0.O00000Oo(singleEmitter);
    }
}
