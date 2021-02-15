package com.android.camera.resource;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/* compiled from: lambda */
public final /* synthetic */ class O00000o0 implements ObservableOnSubscribe {
    private final /* synthetic */ BaseObservableRequest O0OOoO0;
    private final /* synthetic */ Class O0OOoOO;

    public /* synthetic */ O00000o0(BaseObservableRequest baseObservableRequest, Class cls) {
        this.O0OOoO0 = baseObservableRequest;
        this.O0OOoOO = cls;
    }

    public final void subscribe(ObservableEmitter observableEmitter) {
        this.O0OOoO0.O000000o(this.O0OOoOO, observableEmitter);
    }
}
