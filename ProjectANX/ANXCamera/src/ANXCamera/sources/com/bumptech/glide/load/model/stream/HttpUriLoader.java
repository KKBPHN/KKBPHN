package com.bumptech.glide.load.model.stream;

import android.net.Uri;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HttpUriLoader implements ModelLoader {
    private static final Set SCHEMES = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[]{"http", "https"})));
    private final ModelLoader urlLoader;

    public class Factory implements ModelLoaderFactory {
        @NonNull
        public ModelLoader build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new HttpUriLoader(multiModelLoaderFactory.build(GlideUrl.class, InputStream.class));
        }

        public void teardown() {
        }
    }

    public HttpUriLoader(ModelLoader modelLoader) {
        this.urlLoader = modelLoader;
    }

    public LoadData buildLoadData(@NonNull Uri uri, int i, int i2, @NonNull Options options) {
        return this.urlLoader.buildLoadData(new GlideUrl(uri.toString()), i, i2, options);
    }

    public boolean handles(@NonNull Uri uri) {
        return SCHEMES.contains(uri.getScheme());
    }
}
