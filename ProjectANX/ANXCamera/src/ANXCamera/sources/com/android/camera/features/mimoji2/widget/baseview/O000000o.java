package com.android.camera.features.mimoji2.widget.baseview;

import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: lambda */
public final /* synthetic */ class O000000o implements OnClickListener {
    private final /* synthetic */ OnRecyclerItemClickListener O0OOoO0;
    private final /* synthetic */ Object O0OOoOO;
    private final /* synthetic */ int O0OOoOo;
    private final /* synthetic */ View O0OOoo0;

    public /* synthetic */ O000000o(OnRecyclerItemClickListener onRecyclerItemClickListener, Object obj, int i, View view) {
        this.O0OOoO0 = onRecyclerItemClickListener;
        this.O0OOoOO = obj;
        this.O0OOoOo = i;
        this.O0OOoo0 = view;
    }

    public final void onClick(View view) {
        this.O0OOoO0.OnRecyclerItemClickListener(this.O0OOoOO, this.O0OOoOo, this.O0OOoo0);
    }
}
