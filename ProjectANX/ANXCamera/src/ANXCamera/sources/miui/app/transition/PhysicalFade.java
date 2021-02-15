package miui.app.transition;

import android.animation.Animator;
import android.transition.Fade;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;

class PhysicalFade extends Fade {
    public PhysicalFade() {
        setDuration(350);
        setInterpolator(FragmentTransitionHelper.PHYSIC_BASED_INTERPOLATOR);
    }

    public Animator onAppear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        Animator onAppear = super.onAppear(viewGroup, view, transitionValues, transitionValues2);
        if (onAppear != null) {
            onAppear.setInterpolator(getInterpolator());
            onAppear.setDuration(getDuration());
        }
        return onAppear;
    }

    public Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        Animator onDisappear = super.onDisappear(viewGroup, view, transitionValues, transitionValues2);
        if (onDisappear != null) {
            onDisappear.setInterpolator(getInterpolator());
            onDisappear.setDuration(getDuration());
        }
        return onDisappear;
    }
}
