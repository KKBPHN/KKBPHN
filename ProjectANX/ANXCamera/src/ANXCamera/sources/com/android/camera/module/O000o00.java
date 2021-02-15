package com.android.camera.module;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/* compiled from: lambda */
public final /* synthetic */ class O000o00 implements FlowableOnSubscribe {
    private final /* synthetic */ MiLiveModule O0OOoO0;

    public /* synthetic */ O000o00(MiLiveModule miLiveModule) {
        this.O0OOoO0 = miLiveModule;
    }

    public final void subscribe(FlowableEmitter flowableEmitter) {
        this.O0OOoO0.O000000o(flowableEmitter);
    }
}
