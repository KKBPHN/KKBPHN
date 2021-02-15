package com.bumptech.glide.load.model;

import android.net.Uri;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UrlUriLoader implements ModelLoader {
    private static final Set SCHEMES = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[]{"http", "https"})));
    private final ModelLoader urlLoader;

    public class StreamFactory implements ModelLoaderFactory {
        @NonNull
        public ModelLoader build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new UrlUriLoader(multiModelLoaderFactory.build(GlideUrl.class, InputStream.class));
        }

        public void teardown() {
        }
    }

    public UrlUriLoader(ModelLoader modelLoader) {
        this.urlLoader = modelLoader;
    }

    public LoadData buildLoadData(@NonNull Uri uri, int i, int i2, @NonNull Options options) {
        return this.urlLoader.buildLoadData(new GlideUrl(uri.toString()), i, i2, options);
    }

    public boolean handles(@NonNull Uri uri) {
        return SCHEMES.contains(uri.getScheme());
    }
}
