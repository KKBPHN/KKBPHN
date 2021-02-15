package com.bumptech.glide.load.model.stream;

import androidx.annotation.NonNull;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.InputStream;
import java.net.URL;

public class UrlLoader implements ModelLoader {
    private final ModelLoader glideUrlLoader;

    public class StreamFactory implements ModelLoaderFactory {
        @NonNull
        public ModelLoader build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new UrlLoader(multiModelLoaderFactory.build(GlideUrl.class, InputStream.class));
        }

        public void teardown() {
        }
    }

    public UrlLoader(ModelLoader modelLoader) {
        this.glideUrlLoader = modelLoader;
    }

    public LoadData buildLoadData(@NonNull URL url, int i, int i2, @NonNull Options options) {
        return this.glideUrlLoader.buildLoadData(new GlideUrl(url), i, i2, options);
    }

    public boolean handles(@NonNull URL url) {
        return true;
    }
}
