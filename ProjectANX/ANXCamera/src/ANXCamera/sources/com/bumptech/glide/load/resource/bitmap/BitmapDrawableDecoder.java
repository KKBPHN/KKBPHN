package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;

public class BitmapDrawableDecoder implements ResourceDecoder {
    private final ResourceDecoder decoder;
    private final Resources resources;

    public BitmapDrawableDecoder(Context context, ResourceDecoder resourceDecoder) {
        this(context.getResources(), resourceDecoder);
    }

    public BitmapDrawableDecoder(@NonNull Resources resources2, @NonNull ResourceDecoder resourceDecoder) {
        Preconditions.checkNotNull(resources2);
        this.resources = resources2;
        Preconditions.checkNotNull(resourceDecoder);
        this.decoder = resourceDecoder;
    }

    @Deprecated
    public BitmapDrawableDecoder(Resources resources2, BitmapPool bitmapPool, ResourceDecoder resourceDecoder) {
        this(resources2, resourceDecoder);
    }

    public Resource decode(@NonNull Object obj, int i, int i2, @NonNull Options options) {
        return LazyBitmapDrawableResource.obtain(this.resources, this.decoder.decode(obj, i, i2, options));
    }

    public boolean handles(@NonNull Object obj, @NonNull Options options) {
        return this.decoder.handles(obj, options);
    }
}
