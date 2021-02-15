package com.bumptech.glide;

import androidx.annotation.NonNull;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.request.transition.ViewPropertyTransition.Animator;

public final class GenericTransitionOptions extends TransitionOptions {
    @NonNull
    public static GenericTransitionOptions with(int i) {
        return (GenericTransitionOptions) new GenericTransitionOptions().transition(i);
    }

    @NonNull
    public static GenericTransitionOptions with(@NonNull TransitionFactory transitionFactory) {
        return (GenericTransitionOptions) new GenericTransitionOptions().transition(transitionFactory);
    }

    @NonNull
    public static GenericTransitionOptions with(@NonNull Animator animator) {
        return (GenericTransitionOptions) new GenericTransitionOptions().transition(animator);
    }

    @NonNull
    public static GenericTransitionOptions withNoTransition() {
        return (GenericTransitionOptions) new GenericTransitionOptions().dontTransition();
    }
}
