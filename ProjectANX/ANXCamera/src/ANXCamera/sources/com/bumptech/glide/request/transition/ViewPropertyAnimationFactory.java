package com.bumptech.glide.request.transition;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.transition.ViewPropertyTransition.Animator;

public class ViewPropertyAnimationFactory implements TransitionFactory {
    private ViewPropertyTransition animation;
    private final Animator animator;

    public ViewPropertyAnimationFactory(Animator animator2) {
        this.animator = animator2;
    }

    public Transition build(DataSource dataSource, boolean z) {
        if (dataSource == DataSource.MEMORY_CACHE || !z) {
            return NoTransition.get();
        }
        if (this.animation == null) {
            this.animation = new ViewPropertyTransition(this.animator);
        }
        return this.animation;
    }
}
