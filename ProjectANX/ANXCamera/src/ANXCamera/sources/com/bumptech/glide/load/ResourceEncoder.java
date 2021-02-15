package com.bumptech.glide.load;

import androidx.annotation.NonNull;

public interface ResourceEncoder extends Encoder {
    @NonNull
    EncodeStrategy getEncodeStrategy(@NonNull Options options);
}
