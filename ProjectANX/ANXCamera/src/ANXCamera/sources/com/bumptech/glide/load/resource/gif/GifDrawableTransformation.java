package com.bumptech.glide.load.resource.gif;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

public class GifDrawableTransformation implements Transformation {
    private final Transformation wrapped;

    public GifDrawableTransformation(Transformation transformation) {
        Preconditions.checkNotNull(transformation);
        this.wrapped = transformation;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof GifDrawableTransformation)) {
            return false;
        }
        return this.wrapped.equals(((GifDrawableTransformation) obj).wrapped);
    }

    public int hashCode() {
        return this.wrapped.hashCode();
    }

    @NonNull
    public Resource transform(@NonNull Context context, @NonNull Resource resource, int i, int i2) {
        GifDrawable gifDrawable = (GifDrawable) resource.get();
        BitmapResource bitmapResource = new BitmapResource(gifDrawable.getFirstFrame(), Glide.get(context).getBitmapPool());
        Resource transform = this.wrapped.transform(context, bitmapResource, i, i2);
        if (!bitmapResource.equals(transform)) {
            bitmapResource.recycle();
        }
        gifDrawable.setFrameTransformation(this.wrapped, (Bitmap) transform.get());
        return resource;
    }

    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        this.wrapped.updateDiskCacheKey(messageDigest);
    }
}
