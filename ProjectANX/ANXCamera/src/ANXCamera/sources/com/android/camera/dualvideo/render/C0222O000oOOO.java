package com.android.camera.dualvideo.render;

import io.reactivex.functions.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000oOOO reason: case insensitive filesystem */
public final /* synthetic */ class C0222O000oOOO implements Consumer {
    private final /* synthetic */ RenderTrigger O0OOoO0;

    public /* synthetic */ C0222O000oOOO(RenderTrigger renderTrigger) {
        this.O0OOoO0 = renderTrigger;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.process((Integer) obj);
    }
}
