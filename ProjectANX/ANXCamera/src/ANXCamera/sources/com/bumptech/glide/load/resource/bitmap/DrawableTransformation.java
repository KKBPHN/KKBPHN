package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import java.security.MessageDigest;

public class DrawableTransformation implements Transformation {
    private final boolean isRequired;
    private final Transformation wrapped;

    public DrawableTransformation(Transformation transformation, boolean z) {
        this.wrapped = transformation;
        this.isRequired = z;
    }

    private Resource newDrawableResource(Context context, Resource resource) {
        return LazyBitmapDrawableResource.obtain(context.getResources(), resource);
    }

    public Transformation asBitmapDrawable() {
        return this;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DrawableTransformation)) {
            return false;
        }
        return this.wrapped.equals(((DrawableTransformation) obj).wrapped);
    }

    public int hashCode() {
        return this.wrapped.hashCode();
    }

    @NonNull
    public Resource transform(@NonNull Context context, @NonNull Resource resource, int i, int i2) {
        Drawable drawable = (Drawable) resource.get();
        Resource convert = DrawableToBitmapConverter.convert(Glide.get(context).getBitmapPool(), drawable, i, i2);
        if (convert != null) {
            Resource transform = this.wrapped.transform(context, convert, i, i2);
            if (!transform.equals(convert)) {
                return newDrawableResource(context, transform);
            }
            transform.recycle();
            return resource;
        } else if (!this.isRequired) {
            return resource;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to convert ");
            sb.append(drawable);
            sb.append(" to a Bitmap");
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        this.wrapped.updateDiskCacheKey(messageDigest);
    }
}
