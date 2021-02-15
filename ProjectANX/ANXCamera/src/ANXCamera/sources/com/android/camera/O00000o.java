package com.android.camera;

import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.fragment.dialog.BaseDialogFragment.DismissCallback;

/* compiled from: lambda */
public final /* synthetic */ class O00000o implements DismissCallback {
    private final /* synthetic */ Camera O0OOoO0;
    private final /* synthetic */ DataItemGlobal O0OOoOO;

    public /* synthetic */ O00000o(Camera camera, DataItemGlobal dataItemGlobal) {
        this.O0OOoO0 = camera;
        this.O0OOoOO = dataItemGlobal;
    }

    public final void onDismiss() {
        this.O0OOoO0.O000000o(this.O0OOoOO);
    }
}
