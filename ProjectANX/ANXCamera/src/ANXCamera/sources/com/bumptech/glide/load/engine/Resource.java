package com.bumptech.glide.load.engine;

import androidx.annotation.NonNull;

public interface Resource {
    @NonNull
    Object get();

    @NonNull
    Class getResourceClass();

    int getSize();

    void recycle();
}
