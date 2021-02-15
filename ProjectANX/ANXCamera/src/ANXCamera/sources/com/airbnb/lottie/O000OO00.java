package com.airbnb.lottie;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;

class O000OO00 implements AnimatorUpdateListener {
    final /* synthetic */ C0083O000OoO0 this$0;

    O000OO00(C0083O000OoO0 o000OoO0) {
        this.this$0 = o000OoO0;
    }

    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        if (this.this$0.O0000oO != null) {
            this.this$0.O0000oO.setProgress(this.this$0.animator.O0000o0O());
        }
    }
}
