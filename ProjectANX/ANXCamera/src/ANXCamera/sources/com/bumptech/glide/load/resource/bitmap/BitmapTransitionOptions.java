package com.bumptech.glide.load.resource.bitmap;

import androidx.annotation.NonNull;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.request.transition.BitmapTransitionFactory;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory.Builder;
import com.bumptech.glide.request.transition.TransitionFactory;

public final class BitmapTransitionOptions extends TransitionOptions {
    @NonNull
    public static BitmapTransitionOptions with(@NonNull TransitionFactory transitionFactory) {
        return (BitmapTransitionOptions) new BitmapTransitionOptions().transition(transitionFactory);
    }

    @NonNull
    public static BitmapTransitionOptions withCrossFade() {
        return new BitmapTransitionOptions().crossFade();
    }

    @NonNull
    public static BitmapTransitionOptions withCrossFade(int i) {
        return new BitmapTransitionOptions().crossFade(i);
    }

    @NonNull
    public static BitmapTransitionOptions withCrossFade(@NonNull Builder builder) {
        return new BitmapTransitionOptions().crossFade(builder);
    }

    @NonNull
    public static BitmapTransitionOptions withCrossFade(@NonNull DrawableCrossFadeFactory drawableCrossFadeFactory) {
        return new BitmapTransitionOptions().crossFade(drawableCrossFadeFactory);
    }

    @NonNull
    public static BitmapTransitionOptions withWrapped(@NonNull TransitionFactory transitionFactory) {
        return new BitmapTransitionOptions().transitionUsing(transitionFactory);
    }

    @NonNull
    public BitmapTransitionOptions crossFade() {
        return crossFade(new Builder());
    }

    @NonNull
    public BitmapTransitionOptions crossFade(int i) {
        return crossFade(new Builder(i));
    }

    @NonNull
    public BitmapTransitionOptions crossFade(@NonNull Builder builder) {
        return transitionUsing(builder.build());
    }

    @NonNull
    public BitmapTransitionOptions crossFade(@NonNull DrawableCrossFadeFactory drawableCrossFadeFactory) {
        return transitionUsing(drawableCrossFadeFactory);
    }

    @NonNull
    public BitmapTransitionOptions transitionUsing(@NonNull TransitionFactory transitionFactory) {
        return (BitmapTransitionOptions) transition((TransitionFactory) new BitmapTransitionFactory(transitionFactory));
    }
}
