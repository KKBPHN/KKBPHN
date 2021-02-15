package com.bumptech.glide.load.resource.bitmap;

import androidx.annotation.NonNull;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.ByteBufferUtil;
import java.nio.ByteBuffer;

public class ByteBufferBitmapDecoder implements ResourceDecoder {
    private final Downsampler downsampler;

    public ByteBufferBitmapDecoder(Downsampler downsampler2) {
        this.downsampler = downsampler2;
    }

    public Resource decode(@NonNull ByteBuffer byteBuffer, int i, int i2, @NonNull Options options) {
        return this.downsampler.decode(ByteBufferUtil.toStream(byteBuffer), i, i2, options);
    }

    public boolean handles(@NonNull ByteBuffer byteBuffer, @NonNull Options options) {
        return this.downsampler.handles(byteBuffer);
    }
}
