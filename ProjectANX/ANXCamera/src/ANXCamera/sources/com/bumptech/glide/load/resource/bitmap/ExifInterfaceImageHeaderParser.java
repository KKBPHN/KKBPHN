package com.bumptech.glide.load.resource.bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.exifinterface.media.ExifInterface;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParser.ImageType;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.InputStream;
import java.nio.ByteBuffer;

@RequiresApi(27)
public final class ExifInterfaceImageHeaderParser implements ImageHeaderParser {
    public int getOrientation(@NonNull InputStream inputStream, @NonNull ArrayPool arrayPool) {
        int attributeInt = new ExifInterface(inputStream).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        if (attributeInt == 0) {
            return -1;
        }
        return attributeInt;
    }

    public int getOrientation(@NonNull ByteBuffer byteBuffer, @NonNull ArrayPool arrayPool) {
        return getOrientation(ByteBufferUtil.toStream(byteBuffer), arrayPool);
    }

    @NonNull
    public ImageType getType(@NonNull InputStream inputStream) {
        return ImageType.UNKNOWN;
    }

    @NonNull
    public ImageType getType(@NonNull ByteBuffer byteBuffer) {
        return ImageType.UNKNOWN;
    }
}
