package com.bumptech.glide.load.resource.transcode;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.LazyBitmapDrawableResource;
import com.bumptech.glide.util.Preconditions;

public class BitmapDrawableTranscoder implements ResourceTranscoder {
    private final Resources resources;

    public BitmapDrawableTranscoder(@NonNull Context context) {
        this(context.getResources());
    }

    public BitmapDrawableTranscoder(@NonNull Resources resources2) {
        Preconditions.checkNotNull(resources2);
        this.resources = resources2;
    }

    @Deprecated
    public BitmapDrawableTranscoder(@NonNull Resources resources2, BitmapPool bitmapPool) {
        this(resources2);
    }

    @Nullable
    public Resource transcode(@NonNull Resource resource, @NonNull Options options) {
        return LazyBitmapDrawableResource.obtain(this.resources, resource);
    }
}
