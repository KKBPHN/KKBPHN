package com.bumptech.glide.load.model;

import androidx.annotation.NonNull;

public interface ModelLoaderFactory {
    @NonNull
    ModelLoader build(@NonNull MultiModelLoaderFactory multiModelLoaderFactory);

    void teardown();
}
