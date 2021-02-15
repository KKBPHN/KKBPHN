package com.android.camera.ui;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;

/* compiled from: lambda */
public final /* synthetic */ class O00000o implements AnimatorUpdateListener {
    private final /* synthetic */ View O0OOoO0;

    public /* synthetic */ O00000o(View view) {
        this.O0OOoO0 = view;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.O0OOoO0.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }
}
