package com.android.camera.fragment.bottom.action;

import android.view.View;
import com.android.camera.ui.AdjustAnimationView;
import com.android.camera.ui.AnimationView;

/* compiled from: lambda */
public final /* synthetic */ class O00000o0 implements Runnable {
    private final /* synthetic */ AdjustAnimationView O0OOoO0;
    private final /* synthetic */ AnimationView O0OOoOO;
    private final /* synthetic */ View O0OOoOo;
    private final /* synthetic */ float O0OOoo0;
    private final /* synthetic */ float O0OOooO;

    public /* synthetic */ O00000o0(AdjustAnimationView adjustAnimationView, AnimationView animationView, View view, float f, float f2) {
        this.O0OOoO0 = adjustAnimationView;
        this.O0OOoOO = animationView;
        this.O0OOoOo = view;
        this.O0OOoo0 = f;
        this.O0OOooO = f2;
    }

    public final void run() {
        FragmentBottomAction.O000000o(this.O0OOoO0, this.O0OOoOO, this.O0OOoOo, this.O0OOoo0, this.O0OOooO);
    }
}
