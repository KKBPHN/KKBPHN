package com.android.camera.module;

import java.util.List;

/* compiled from: lambda */
/* renamed from: com.android.camera.module.O000oO0O reason: case insensitive filesystem */
public final /* synthetic */ class C0384O000oO0O implements OnTagsListener {
    private final /* synthetic */ VideoModule O0OOoO0;
    private final /* synthetic */ OnTagsListener O0OOoOO;

    public /* synthetic */ C0384O000oO0O(VideoModule videoModule, OnTagsListener onTagsListener) {
        this.O0OOoO0 = videoModule;
        this.O0OOoOO = onTagsListener;
    }

    public final void onTagsReady(List list) {
        this.O0OOoO0.O000000o(this.O0OOoOO, list);
    }
}
