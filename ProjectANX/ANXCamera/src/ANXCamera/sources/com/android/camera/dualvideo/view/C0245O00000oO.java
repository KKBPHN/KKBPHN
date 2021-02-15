package com.android.camera.dualvideo.view;

import android.graphics.Rect;
import java.util.ArrayList;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.view.O00000oO reason: case insensitive filesystem */
public final /* synthetic */ class C0245O00000oO implements Consumer {
    private final /* synthetic */ TouchHelper O0OOoO0;
    private final /* synthetic */ Rect O0OOoOO;

    public /* synthetic */ C0245O00000oO(TouchHelper touchHelper, Rect rect) {
        this.O0OOoO0 = touchHelper;
        this.O0OOoOO = rect;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.O000000o(this.O0OOoOO, (ArrayList) obj);
    }
}
