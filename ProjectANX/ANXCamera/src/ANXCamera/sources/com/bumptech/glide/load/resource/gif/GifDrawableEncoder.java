package com.bumptech.glide.load.resource.gif;

import android.util.Log;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.File;
import java.io.IOException;

public class GifDrawableEncoder implements ResourceEncoder {
    private static final String TAG = "GifEncoder";

    public boolean encode(@NonNull Resource resource, @NonNull File file, @NonNull Options options) {
        try {
            ByteBufferUtil.toFile(((GifDrawable) resource.get()).getBuffer(), file);
            return true;
        } catch (IOException e) {
            String str = TAG;
            if (Log.isLoggable(str, 5)) {
                Log.w(str, "Failed to encode GIF drawable data", e);
            }
            return false;
        }
    }

    @NonNull
    public EncodeStrategy getEncodeStrategy(@NonNull Options options) {
        return EncodeStrategy.SOURCE;
    }
}
