package com.bumptech.glide.load.engine;

import androidx.annotation.NonNull;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.cache.DiskCache.Writer;
import java.io.File;

class DataCacheWriter implements Writer {
    private final Object data;
    private final Encoder encoder;
    private final Options options;

    DataCacheWriter(Encoder encoder2, Object obj, Options options2) {
        this.encoder = encoder2;
        this.data = obj;
        this.options = options2;
    }

    public boolean write(@NonNull File file) {
        return this.encoder.encode(this.data, file, this.options);
    }
}
