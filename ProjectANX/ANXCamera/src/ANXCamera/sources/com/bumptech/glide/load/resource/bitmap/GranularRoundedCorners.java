package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public final class GranularRoundedCorners extends BitmapTransformation {
    private static final String ID = "com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners";
    private static final byte[] ID_BYTES = ID.getBytes(Key.CHARSET);
    private final float bottomLeft;
    private final float bottomRight;
    private final float topLeft;
    private final float topRight;

    public GranularRoundedCorners(float f, float f2, float f3, float f4) {
        this.topLeft = f;
        this.topRight = f2;
        this.bottomRight = f3;
        this.bottomLeft = f4;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof GranularRoundedCorners)) {
            return false;
        }
        GranularRoundedCorners granularRoundedCorners = (GranularRoundedCorners) obj;
        return this.topLeft == granularRoundedCorners.topLeft && this.topRight == granularRoundedCorners.topRight && this.bottomRight == granularRoundedCorners.bottomRight && this.bottomLeft == granularRoundedCorners.bottomLeft;
    }

    public int hashCode() {
        return Util.hashCode(this.bottomLeft, Util.hashCode(this.bottomRight, Util.hashCode(this.topRight, Util.hashCode(ID.hashCode(), Util.hashCode(this.topLeft)))));
    }

    /* access modifiers changed from: protected */
    public Bitmap transform(@NonNull BitmapPool bitmapPool, @NonNull Bitmap bitmap, int i, int i2) {
        return TransformationUtils.roundedCorners(bitmapPool, bitmap, this.topLeft, this.topRight, this.bottomRight, this.bottomLeft);
    }

    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
        messageDigest.update(ByteBuffer.allocate(16).putFloat(this.topLeft).putFloat(this.topRight).putFloat(this.bottomRight).putFloat(this.bottomLeft).array());
    }
}
