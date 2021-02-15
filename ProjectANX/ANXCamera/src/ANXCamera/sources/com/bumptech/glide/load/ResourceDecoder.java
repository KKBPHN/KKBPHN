package com.bumptech.glide.load;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.engine.Resource;

public interface ResourceDecoder {
    @Nullable
    Resource decode(@NonNull Object obj, int i, int i2, @NonNull Options options);

    boolean handles(@NonNull Object obj, @NonNull Options options);
}
