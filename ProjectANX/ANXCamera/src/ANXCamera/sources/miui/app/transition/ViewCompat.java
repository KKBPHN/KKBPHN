package miui.app.transition;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;

class ViewCompat {
    private ViewCompat() {
    }

    public static ValueAnimator createTransitionAlphaAnim(final View view, float f, float f2) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{f, f2});
        ofFloat.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setTransitionAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        return ofFloat;
    }
}
