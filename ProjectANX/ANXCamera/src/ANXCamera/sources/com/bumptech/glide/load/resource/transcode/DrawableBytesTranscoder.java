package com.bumptech.glide.load.resource.transcode;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.gif.GifDrawable;

public final class DrawableBytesTranscoder implements ResourceTranscoder {
    private final ResourceTranscoder bitmapBytesTranscoder;
    private final BitmapPool bitmapPool;
    private final ResourceTranscoder gifDrawableBytesTranscoder;

    public DrawableBytesTranscoder(@NonNull BitmapPool bitmapPool2, @NonNull ResourceTranscoder resourceTranscoder, @NonNull ResourceTranscoder resourceTranscoder2) {
        this.bitmapPool = bitmapPool2;
        this.bitmapBytesTranscoder = resourceTranscoder;
        this.gifDrawableBytesTranscoder = resourceTranscoder2;
    }

    @NonNull
    private static Resource toGifDrawableResource(@NonNull Resource resource) {
        return resource;
    }

    @Nullable
    public Resource transcode(@NonNull Resource resource, @NonNull Options options) {
        Drawable drawable = (Drawable) resource.get();
        if (drawable instanceof BitmapDrawable) {
            return this.bitmapBytesTranscoder.transcode(BitmapResource.obtain(((BitmapDrawable) drawable).getBitmap(), this.bitmapPool), options);
        }
        if (drawable instanceof GifDrawable) {
            return this.gifDrawableBytesTranscoder.transcode(resource, options);
        }
        return null;
    }
}
