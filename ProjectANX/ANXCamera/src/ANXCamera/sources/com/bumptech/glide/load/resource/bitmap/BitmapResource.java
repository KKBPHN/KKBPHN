package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.engine.Initializable;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;

public class BitmapResource implements Resource, Initializable {
    private final Bitmap bitmap;
    private final BitmapPool bitmapPool;

    public BitmapResource(@NonNull Bitmap bitmap2, @NonNull BitmapPool bitmapPool2) {
        Preconditions.checkNotNull(bitmap2, "Bitmap must not be null");
        this.bitmap = bitmap2;
        Preconditions.checkNotNull(bitmapPool2, "BitmapPool must not be null");
        this.bitmapPool = bitmapPool2;
    }

    @Nullable
    public static BitmapResource obtain(@Nullable Bitmap bitmap2, @NonNull BitmapPool bitmapPool2) {
        if (bitmap2 == null) {
            return null;
        }
        return new BitmapResource(bitmap2, bitmapPool2);
    }

    @NonNull
    public Bitmap get() {
        return this.bitmap;
    }

    @NonNull
    public Class getResourceClass() {
        return Bitmap.class;
    }

    public int getSize() {
        return Util.getBitmapByteSize(this.bitmap);
    }

    public void initialize() {
        this.bitmap.prepareToDraw();
    }

    public void recycle() {
        this.bitmapPool.put(this.bitmap);
    }
}
