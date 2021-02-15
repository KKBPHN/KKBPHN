package com.bumptech.glide.load;

import android.content.Context;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.engine.Resource;

public interface Transformation extends Key {
    @NonNull
    Resource transform(@NonNull Context context, @NonNull Resource resource, int i, int i2);
}
