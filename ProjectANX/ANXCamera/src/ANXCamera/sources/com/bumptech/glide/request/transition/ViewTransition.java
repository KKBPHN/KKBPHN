package com.bumptech.glide.request.transition;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import com.bumptech.glide.request.transition.Transition.ViewAdapter;

public class ViewTransition implements Transition {
    private final ViewTransitionAnimationFactory viewTransitionAnimationFactory;

    interface ViewTransitionAnimationFactory {
        Animation build(Context context);
    }

    ViewTransition(ViewTransitionAnimationFactory viewTransitionAnimationFactory2) {
        this.viewTransitionAnimationFactory = viewTransitionAnimationFactory2;
    }

    public boolean transition(Object obj, ViewAdapter viewAdapter) {
        View view = viewAdapter.getView();
        if (view != null) {
            view.clearAnimation();
            view.startAnimation(this.viewTransitionAnimationFactory.build(view.getContext()));
        }
        return false;
    }
}
