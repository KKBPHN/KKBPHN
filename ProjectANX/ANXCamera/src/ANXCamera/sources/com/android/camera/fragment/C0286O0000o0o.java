package com.android.camera.fragment;

import android.view.View;
import com.android.camera.aiwatermark.data.WatermarkItem;

/* compiled from: lambda */
/* renamed from: com.android.camera.fragment.O0000o0o reason: case insensitive filesystem */
public final /* synthetic */ class C0286O0000o0o implements Runnable {
    private final /* synthetic */ WatermarkItem O0OOoO0;
    private final /* synthetic */ boolean O0OOoOO;
    private final /* synthetic */ View O0OOoOo;

    public /* synthetic */ C0286O0000o0o(WatermarkItem watermarkItem, boolean z, View view) {
        this.O0OOoO0 = watermarkItem;
        this.O0OOoOO = z;
        this.O0OOoOo = view;
    }

    public final void run() {
        FragmentMainContent.O000000o(this.O0OOoO0, this.O0OOoOO, this.O0OOoOo);
    }
}
