package com.bumptech.glide.load.model.stream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.HttpUrlFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

public class HttpGlideUrlLoader implements ModelLoader {
    public static final Option TIMEOUT = Option.memory("com.bumptech.glide.load.model.stream.HttpGlideUrlLoader.Timeout", Integer.valueOf(2500));
    @Nullable
    private final ModelCache modelCache;

    public class Factory implements ModelLoaderFactory {
        private final ModelCache modelCache = new ModelCache(500);

        @NonNull
        public ModelLoader build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new HttpGlideUrlLoader(this.modelCache);
        }

        public void teardown() {
        }
    }

    public HttpGlideUrlLoader() {
        this(null);
    }

    public HttpGlideUrlLoader(@Nullable ModelCache modelCache2) {
        this.modelCache = modelCache2;
    }

    public LoadData buildLoadData(@NonNull GlideUrl glideUrl, int i, int i2, @NonNull Options options) {
        ModelCache modelCache2 = this.modelCache;
        if (modelCache2 != null) {
            GlideUrl glideUrl2 = (GlideUrl) modelCache2.get(glideUrl, 0, 0);
            if (glideUrl2 == null) {
                this.modelCache.put(glideUrl, 0, 0, glideUrl);
            } else {
                glideUrl = glideUrl2;
            }
        }
        return new LoadData(glideUrl, new HttpUrlFetcher(glideUrl, ((Integer) options.get(TIMEOUT)).intValue()));
    }

    public boolean handles(@NonNull GlideUrl glideUrl) {
        return true;
    }
}
