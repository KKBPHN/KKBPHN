package com.bumptech.glide.load.model;

import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import java.io.File;
import java.io.InputStream;

public class StringLoader implements ModelLoader {
    private final ModelLoader uriLoader;

    public final class AssetFileDescriptorFactory implements ModelLoaderFactory {
        public ModelLoader build(@NonNull MultiModelLoaderFactory multiModelLoaderFactory) {
            return new StringLoader(multiModelLoaderFactory.build(Uri.class, AssetFileDescriptor.class));
        }

        public void teardown() {
        }
    }

    public class FileDescriptorFactory implements ModelLoaderFactory {
        @NonNull
        public ModelLoader build(@NonNull MultiModelLoaderFactory multiModelLoaderFactory) {
            return new StringLoader(multiModelLoaderFactory.build(Uri.class, ParcelFileDescriptor.class));
        }

        public void teardown() {
        }
    }

    public class StreamFactory implements ModelLoaderFactory {
        @NonNull
        public ModelLoader build(@NonNull MultiModelLoaderFactory multiModelLoaderFactory) {
            return new StringLoader(multiModelLoaderFactory.build(Uri.class, InputStream.class));
        }

        public void teardown() {
        }
    }

    public StringLoader(ModelLoader modelLoader) {
        this.uriLoader = modelLoader;
    }

    @Nullable
    private static Uri parseUri(String str) {
        Uri uri;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.charAt(0) != '/') {
            Uri parse = Uri.parse(str);
            if (parse.getScheme() != null) {
                uri = parse;
                return uri;
            }
        }
        uri = toFileUri(str);
        return uri;
    }

    private static Uri toFileUri(String str) {
        return Uri.fromFile(new File(str));
    }

    public LoadData buildLoadData(@NonNull String str, int i, int i2, @NonNull Options options) {
        Uri parseUri = parseUri(str);
        if (parseUri == null || !this.uriLoader.handles(parseUri)) {
            return null;
        }
        return this.uriLoader.buildLoadData(parseUri, i, i2, options);
    }

    public boolean handles(@NonNull String str) {
        return true;
    }
}
