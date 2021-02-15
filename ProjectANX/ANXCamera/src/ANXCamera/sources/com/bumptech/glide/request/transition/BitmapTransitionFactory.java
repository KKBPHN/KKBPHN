package com.bumptech.glide.request.transition;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;

public class BitmapTransitionFactory extends BitmapContainerTransitionFactory {
    public BitmapTransitionFactory(@NonNull TransitionFactory transitionFactory) {
        super(transitionFactory);
    }

    /* access modifiers changed from: protected */
    @NonNull
    public Bitmap getBitmap(@NonNull Bitmap bitmap) {
        return bitmap;
    }
}
