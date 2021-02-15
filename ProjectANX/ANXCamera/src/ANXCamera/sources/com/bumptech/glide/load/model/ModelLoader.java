package com.bumptech.glide.load.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.util.Preconditions;
import java.util.Collections;
import java.util.List;

public interface ModelLoader {

    public class LoadData {
        public final List alternateKeys;
        public final DataFetcher fetcher;
        public final Key sourceKey;

        public LoadData(@NonNull Key key, @NonNull DataFetcher dataFetcher) {
            this(key, Collections.emptyList(), dataFetcher);
        }

        public LoadData(@NonNull Key key, @NonNull List list, @NonNull DataFetcher dataFetcher) {
            Preconditions.checkNotNull(key);
            this.sourceKey = key;
            Preconditions.checkNotNull(list);
            this.alternateKeys = list;
            Preconditions.checkNotNull(dataFetcher);
            this.fetcher = dataFetcher;
        }
    }

    @Nullable
    LoadData buildLoadData(@NonNull Object obj, int i, int i2, @NonNull Options options);

    boolean handles(@NonNull Object obj);
}
