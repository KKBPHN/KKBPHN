package com.android.camera.ui;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;

/* compiled from: lambda */
public final /* synthetic */ class O0000O0o implements AnimatorUpdateListener {
    private final /* synthetic */ MutiStateButton O0OOoO0;
    private final /* synthetic */ float O0OOoOO;
    private final /* synthetic */ float O0OOoOo;
    private final /* synthetic */ float O0OOoo0;
    private final /* synthetic */ float O0OOooO;

    public /* synthetic */ O0000O0o(MutiStateButton mutiStateButton, float f, float f2, float f3, float f4) {
        this.O0OOoO0 = mutiStateButton;
        this.O0OOoOO = f;
        this.O0OOoOo = f2;
        this.O0OOoo0 = f3;
        this.O0OOooO = f4;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.O0OOoO0.O000000o(this.O0OOoOO, this.O0OOoOo, this.O0OOoo0, this.O0OOooO, valueAnimator);
    }
}
