package com.android.camera.resource;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/* compiled from: lambda */
public final /* synthetic */ class O00000Oo implements ObservableOnSubscribe {
    private final /* synthetic */ BaseObservableRequest O0OOoO0;
    private final /* synthetic */ Object O0OOoOO;

    public /* synthetic */ O00000Oo(BaseObservableRequest baseObservableRequest, Object obj) {
        this.O0OOoO0 = baseObservableRequest;
        this.O0OOoOO = obj;
    }

    public final void subscribe(ObservableEmitter observableEmitter) {
        this.O0OOoO0.O000000o(this.O0OOoOO, observableEmitter);
    }
}
