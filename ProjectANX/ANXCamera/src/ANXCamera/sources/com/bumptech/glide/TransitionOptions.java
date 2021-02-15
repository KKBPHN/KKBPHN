package com.bumptech.glide;

import androidx.annotation.NonNull;
import com.bumptech.glide.request.transition.NoTransition;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.request.transition.ViewAnimationFactory;
import com.bumptech.glide.request.transition.ViewPropertyAnimationFactory;
import com.bumptech.glide.request.transition.ViewPropertyTransition.Animator;
import com.bumptech.glide.util.Preconditions;

public abstract class TransitionOptions implements Cloneable {
    private TransitionFactory transitionFactory = NoTransition.getFactory();

    private TransitionOptions self() {
        return this;
    }

    public final TransitionOptions clone() {
        try {
            return (TransitionOptions) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    public final TransitionOptions dontTransition() {
        return transition(NoTransition.getFactory());
    }

    /* access modifiers changed from: 0000 */
    public final TransitionFactory getTransitionFactory() {
        return this.transitionFactory;
    }

    @NonNull
    public final TransitionOptions transition(int i) {
        return transition((TransitionFactory) new ViewAnimationFactory(i));
    }

    @NonNull
    public final TransitionOptions transition(@NonNull TransitionFactory transitionFactory2) {
        Preconditions.checkNotNull(transitionFactory2);
        this.transitionFactory = transitionFactory2;
        self();
        return this;
    }

    @NonNull
    public final TransitionOptions transition(@NonNull Animator animator) {
        return transition((TransitionFactory) new ViewPropertyAnimationFactory(animator));
    }
}
