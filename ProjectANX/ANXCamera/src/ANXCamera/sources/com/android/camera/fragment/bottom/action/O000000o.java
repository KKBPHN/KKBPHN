package com.android.camera.fragment.bottom.action;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import com.android.camera.ui.AnimationView;

/* compiled from: lambda */
public final /* synthetic */ class O000000o implements AnimatorUpdateListener {
    private final /* synthetic */ AnimationView O0OOoO0;

    public /* synthetic */ O000000o(AnimationView animationView) {
        this.O0OOoO0 = animationView;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        FragmentBottomAction.O000000o(this.O0OOoO0, valueAnimator);
    }
}
