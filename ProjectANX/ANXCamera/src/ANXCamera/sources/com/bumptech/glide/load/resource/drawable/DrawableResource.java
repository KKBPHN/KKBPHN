package com.bumptech.glide.load.resource.drawable;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.engine.Initializable;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.util.Preconditions;

public abstract class DrawableResource implements Resource, Initializable {
    protected final Drawable drawable;

    public DrawableResource(Drawable drawable2) {
        Preconditions.checkNotNull(drawable2);
        this.drawable = drawable2;
    }

    @NonNull
    public final Drawable get() {
        ConstantState constantState = this.drawable.getConstantState();
        return constantState == null ? this.drawable : constantState.newDrawable();
    }

    public void initialize() {
        Bitmap firstFrame;
        Drawable drawable2 = this.drawable;
        if (drawable2 instanceof BitmapDrawable) {
            firstFrame = ((BitmapDrawable) drawable2).getBitmap();
        } else if (drawable2 instanceof GifDrawable) {
            firstFrame = ((GifDrawable) drawable2).getFirstFrame();
        } else {
            return;
        }
        firstFrame.prepareToDraw();
    }
}
