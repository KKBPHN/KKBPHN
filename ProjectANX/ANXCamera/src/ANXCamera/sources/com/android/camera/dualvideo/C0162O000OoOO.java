package com.android.camera.dualvideo;

import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O000OoOO reason: case insensitive filesystem */
public final /* synthetic */ class C0162O000OoOO implements OnClickListener {
    private final /* synthetic */ DualVideoSelectModule O0OOoO0;

    public /* synthetic */ C0162O000OoOO(DualVideoSelectModule dualVideoSelectModule) {
        this.O0OOoO0 = dualVideoSelectModule;
    }

    public final void onClick(View view) {
        this.O0OOoO0.onConfirmClicked(view);
    }
}
