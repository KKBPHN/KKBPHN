package com.bumptech.glide.request.target;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.Transition.ViewAdapter;

public abstract class ImageViewTarget extends ViewTarget implements ViewAdapter {
    @Nullable
    private Animatable animatable;

    public ImageViewTarget(ImageView imageView) {
        super(imageView);
    }

    @Deprecated
    public ImageViewTarget(ImageView imageView, boolean z) {
        super(imageView, z);
    }

    private void maybeUpdateAnimatable(@Nullable Object obj) {
        if (obj instanceof Animatable) {
            this.animatable = (Animatable) obj;
            this.animatable.start();
            return;
        }
        this.animatable = null;
    }

    private void setResourceInternal(@Nullable Object obj) {
        setResource(obj);
        maybeUpdateAnimatable(obj);
    }

    @Nullable
    public Drawable getCurrentDrawable() {
        return ((ImageView) this.view).getDrawable();
    }

    public void onLoadCleared(@Nullable Drawable drawable) {
        super.onLoadCleared(drawable);
        Animatable animatable2 = this.animatable;
        if (animatable2 != null) {
            animatable2.stop();
        }
        setResourceInternal(null);
        setDrawable(drawable);
    }

    public void onLoadFailed(@Nullable Drawable drawable) {
        super.onLoadFailed(drawable);
        setResourceInternal(null);
        setDrawable(drawable);
    }

    public void onLoadStarted(@Nullable Drawable drawable) {
        super.onLoadStarted(drawable);
        setResourceInternal(null);
        setDrawable(drawable);
    }

    public void onResourceReady(@NonNull Object obj, @Nullable Transition transition) {
        if (transition == null || !transition.transition(obj, this)) {
            setResourceInternal(obj);
        } else {
            maybeUpdateAnimatable(obj);
        }
    }

    public void onStart() {
        Animatable animatable2 = this.animatable;
        if (animatable2 != null) {
            animatable2.start();
        }
    }

    public void onStop() {
        Animatable animatable2 = this.animatable;
        if (animatable2 != null) {
            animatable2.stop();
        }
    }

    public void setDrawable(Drawable drawable) {
        ((ImageView) this.view).setImageDrawable(drawable);
    }

    public abstract void setResource(@Nullable Object obj);
}
