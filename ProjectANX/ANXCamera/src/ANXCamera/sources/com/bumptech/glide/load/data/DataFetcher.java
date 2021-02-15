package com.bumptech.glide.load.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;

public interface DataFetcher {

    public interface DataCallback {
        void onDataReady(@Nullable Object obj);

        void onLoadFailed(@NonNull Exception exc);
    }

    void cancel();

    void cleanup();

    @NonNull
    Class getDataClass();

    @NonNull
    DataSource getDataSource();

    void loadData(@NonNull Priority priority, @NonNull DataCallback dataCallback);
}
