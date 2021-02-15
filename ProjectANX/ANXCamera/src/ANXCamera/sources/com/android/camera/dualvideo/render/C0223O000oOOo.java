package com.android.camera.dualvideo.render;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000oOOo reason: case insensitive filesystem */
public final /* synthetic */ class C0223O000oOOo implements ObservableOnSubscribe {
    private final /* synthetic */ RenderTrigger O0OOoO0;

    public /* synthetic */ C0223O000oOOo(RenderTrigger renderTrigger) {
        this.O0OOoO0 = renderTrigger;
    }

    public final void subscribe(ObservableEmitter observableEmitter) {
        this.O0OOoO0.O00000o(observableEmitter);
    }
}
