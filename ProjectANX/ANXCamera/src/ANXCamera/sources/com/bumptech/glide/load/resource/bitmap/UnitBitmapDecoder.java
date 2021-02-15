package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Util;

public final class UnitBitmapDecoder implements ResourceDecoder {

    final class NonOwnedBitmapResource implements Resource {
        private final Bitmap bitmap;

        NonOwnedBitmapResource(@NonNull Bitmap bitmap2) {
            this.bitmap = bitmap2;
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

        public void recycle() {
        }
    }

    public Resource decode(@NonNull Bitmap bitmap, int i, int i2, @NonNull Options options) {
        return new NonOwnedBitmapResource(bitmap);
    }

    public boolean handles(@NonNull Bitmap bitmap, @NonNull Options options) {
        return true;
    }
}
