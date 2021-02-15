package miui.app.transition;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import miui.R;

class ExitFade extends PhysicalFade {
    private static final float DEFAULT_EXIT_END_ALPHA = 0.0f;
    private static final float DEFAULT_ZOOMLESS_SCALE = 1.0f;

    public Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        viewGroup.getOverlay().remove(view);
        ((FrameLayout) viewGroup.getRootView().findViewById(R.id.fragment_bottom)).addView(view);
        ValueAnimator createTransitionAlphaAnim = ViewCompat.createTransitionAlphaAnim(view, 1.0f, 0.0f);
        createTransitionAlphaAnim.setDuration(getDuration());
        createTransitionAlphaAnim.setInterpolator(getInterpolator());
        createTransitionAlphaAnim.start();
        return null;
    }
}
