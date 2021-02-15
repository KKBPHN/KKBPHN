package com.bumptech.glide.request.transition;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.bumptech.glide.load.DataSource;

public class ViewAnimationFactory implements TransitionFactory {
    private Transition transition;
    private final ViewTransitionAnimationFactory viewTransitionAnimationFactory;

    class ConcreteViewTransitionAnimationFactory implements ViewTransitionAnimationFactory {
        private final Animation animation;

        ConcreteViewTransitionAnimationFactory(Animation animation2) {
            this.animation = animation2;
        }

        public Animation build(Context context) {
            return this.animation;
        }
    }

    class ResourceViewTransitionAnimationFactory implements ViewTransitionAnimationFactory {
        private final int animationId;

        ResourceViewTransitionAnimationFactory(int i) {
            this.animationId = i;
        }

        public Animation build(Context context) {
            return AnimationUtils.loadAnimation(context, this.animationId);
        }
    }

    public ViewAnimationFactory(int i) {
        this((ViewTransitionAnimationFactory) new ResourceViewTransitionAnimationFactory(i));
    }

    public ViewAnimationFactory(Animation animation) {
        this((ViewTransitionAnimationFactory) new ConcreteViewTransitionAnimationFactory(animation));
    }

    ViewAnimationFactory(ViewTransitionAnimationFactory viewTransitionAnimationFactory2) {
        this.viewTransitionAnimationFactory = viewTransitionAnimationFactory2;
    }

    public Transition build(DataSource dataSource, boolean z) {
        if (dataSource == DataSource.MEMORY_CACHE || !z) {
            return NoTransition.get();
        }
        if (this.transition == null) {
            this.transition = new ViewTransition(this.viewTransitionAnimationFactory);
        }
        return this.transition;
    }
}
