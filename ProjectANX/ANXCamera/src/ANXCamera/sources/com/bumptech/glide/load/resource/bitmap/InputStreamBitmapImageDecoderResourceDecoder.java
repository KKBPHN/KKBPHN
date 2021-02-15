package com.bumptech.glide.load.resource.bitmap;

import android.graphics.ImageDecoder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.InputStream;

@RequiresApi(api = 28)
public final class InputStreamBitmapImageDecoderResourceDecoder implements ResourceDecoder {
    private final BitmapImageDecoderResourceDecoder wrapped = new BitmapImageDecoderResourceDecoder();

    @Nullable
    public Resource decode(@NonNull InputStream inputStream, int i, int i2, @NonNull Options options) {
        return this.wrapped.decode(ImageDecoder.createSource(ByteBufferUtil.fromStream(inputStream)), i, i2, options);
    }

    public boolean handles(@NonNull InputStream inputStream, @NonNull Options options) {
        return true;
    }
}
