package com.bumptech.glide.load;

import androidx.annotation.NonNull;
import java.io.File;

public interface Encoder {
    boolean encode(@NonNull Object obj, @NonNull File file, @NonNull Options options);
}
