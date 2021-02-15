package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

@Deprecated
public class BitmapDrawableTransformation implements Transformation {
    private final Transformation wrapped;

    public BitmapDrawableTransformation(Transformation transformation) {
        DrawableTransformation drawableTransformation = new DrawableTransformation(transformation, false);
        Preconditions.checkNotNull(drawableTransformation);
        this.wrapped = drawableTransformation;
    }

    private static Resource convertToBitmapDrawableResource(Resource resource) {
        if (resource.get() instanceof BitmapDrawable) {
            return resource;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Wrapped transformation unexpectedly returned a non BitmapDrawable resource: ");
        sb.append(resource.get());
        throw new IllegalArgumentException(sb.toString());
    }

    private static Resource convertToDrawableResource(Resource resource) {
        return resource;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BitmapDrawableTransformation)) {
            return false;
        }
        return this.wrapped.equals(((BitmapDrawableTransformation) obj).wrapped);
    }

    public int hashCode() {
        return this.wrapped.hashCode();
    }

    @NonNull
    public Resource transform(@NonNull Context context, @NonNull Resource resource, int i, int i2) {
        Resource transform = this.wrapped.transform(context, resource, i, i2);
        convertToBitmapDrawableResource(transform);
        return transform;
    }

    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        this.wrapped.updateDiskCacheKey(messageDigest);
    }
}
