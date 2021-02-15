package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Initializable;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;

public final class LazyBitmapDrawableResource implements Resource, Initializable {
    private final Resource bitmapResource;
    private final Resources resources;

    private LazyBitmapDrawableResource(@NonNull Resources resources2, @NonNull Resource resource) {
        Preconditions.checkNotNull(resources2);
        this.resources = resources2;
        Preconditions.checkNotNull(resource);
        this.bitmapResource = resource;
    }

    @Nullable
    public static Resource obtain(@NonNull Resources resources2, @Nullable Resource resource) {
        if (resource == null) {
            return null;
        }
        return new LazyBitmapDrawableResource(resources2, resource);
    }

    @Deprecated
    public static LazyBitmapDrawableResource obtain(Context context, Bitmap bitmap) {
        return (LazyBitmapDrawableResource) obtain(context.getResources(), (Resource) BitmapResource.obtain(bitmap, Glide.get(context).getBitmapPool()));
    }

    @Deprecated
    public static LazyBitmapDrawableResource obtain(Resources resources2, BitmapPool bitmapPool, Bitmap bitmap) {
        return (LazyBitmapDrawableResource) obtain(resources2, (Resource) BitmapResource.obtain(bitmap, bitmapPool));
    }

    @NonNull
    public BitmapDrawable get() {
        return new BitmapDrawable(this.resources, (Bitmap) this.bitmapResource.get());
    }

    @NonNull
    public Class getResourceClass() {
        return BitmapDrawable.class;
    }

    public int getSize() {
        return this.bitmapResource.getSize();
    }

    public void initialize() {
        Resource resource = this.bitmapResource;
        if (resource instanceof Initializable) {
            ((Initializable) resource).initialize();
        }
    }

    public void recycle() {
        this.bitmapResource.recycle();
    }
}
