package com.bumptech.glide.load.data;

import androidx.annotation.NonNull;

public interface DataRewinder {

    public interface Factory {
        @NonNull
        DataRewinder build(@NonNull Object obj);

        @NonNull
        Class getDataClass();
    }

    void cleanup();

    @NonNull
    Object rewindAndGet();
}
