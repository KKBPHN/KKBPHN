package com.bumptech.glide.load.resource.bitmap;

import android.graphics.ImageDecoder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import java.nio.ByteBuffer;

@RequiresApi(api = 28)
public final class ByteBufferBitmapImageDecoderResourceDecoder implements ResourceDecoder {
    private final BitmapImageDecoderResourceDecoder wrapped = new BitmapImageDecoderResourceDecoder();

    @Nullable
    public Resource decode(@NonNull ByteBuffer byteBuffer, int i, int i2, @NonNull Options options) {
        return this.wrapped.decode(ImageDecoder.createSource(byteBuffer), i, i2, options);
    }

    public boolean handles(@NonNull ByteBuffer byteBuffer, @NonNull Options options) {
        return true;
    }
}
