package com.android.camera.fragment.top;

import android.widget.ImageView;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.fragment.top.O0000oO0 reason: case insensitive filesystem */
public final /* synthetic */ class C0324O0000oO0 implements Consumer {
    private final /* synthetic */ int O0OOoO0;
    private final /* synthetic */ List O0OOoOO;

    public /* synthetic */ C0324O0000oO0(int i, List list) {
        this.O0OOoO0 = i;
        this.O0OOoOO = list;
    }

    public final void accept(Object obj) {
        FragmentTopConfig.O000000o(this.O0OOoO0, this.O0OOoOO, (ImageView) obj);
    }
}
