package com.bumptech.glide.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.ListPreloader.PreloadSizeProvider;

public class FixedPreloadSizeProvider implements PreloadSizeProvider {
    private final int[] size;

    public FixedPreloadSizeProvider(int i, int i2) {
        this.size = new int[]{i, i2};
    }

    @Nullable
    public int[] getPreloadSize(@NonNull Object obj, int i, int i2) {
        return this.size;
    }
}
