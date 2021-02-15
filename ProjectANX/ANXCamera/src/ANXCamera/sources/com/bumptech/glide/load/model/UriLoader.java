package com.bumptech.glide.load.model;

import android.content.ContentResolver;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.AssetFileDescriptorLocalUriFetcher;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.FileDescriptorLocalUriFetcher;
import com.bumptech.glide.load.data.StreamLocalUriFetcher;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.signature.ObjectKey;
import com.ss.android.ugc.effectmanager.effect.model.ComposerHelper;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UriLoader implements ModelLoader {
    private static final Set SCHEMES = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[]{ComposerHelper.COMPOSER_PATH, "android.resource", "content"})));
    private final LocalUriFetcherFactory factory;

    public final class AssetFileDescriptorFactory implements ModelLoaderFactory, LocalUriFetcherFactory {
        private final ContentResolver contentResolver;

        public AssetFileDescriptorFactory(ContentResolver contentResolver2) {
            this.contentResolver = contentResolver2;
        }

        public DataFetcher build(Uri uri) {
            return new AssetFileDescriptorLocalUriFetcher(this.contentResolver, uri);
        }

        public ModelLoader build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new UriLoader(this);
        }

        public void teardown() {
        }
    }

    public class FileDescriptorFactory implements ModelLoaderFactory, LocalUriFetcherFactory {
        private final ContentResolver contentResolver;

        public FileDescriptorFactory(ContentResolver contentResolver2) {
            this.contentResolver = contentResolver2;
        }

        public DataFetcher build(Uri uri) {
            return new FileDescriptorLocalUriFetcher(this.contentResolver, uri);
        }

        @NonNull
        public ModelLoader build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new UriLoader(this);
        }

        public void teardown() {
        }
    }

    public interface LocalUriFetcherFactory {
        DataFetcher build(Uri uri);
    }

    public class StreamFactory implements ModelLoaderFactory, LocalUriFetcherFactory {
        private final ContentResolver contentResolver;

        public StreamFactory(ContentResolver contentResolver2) {
            this.contentResolver = contentResolver2;
        }

        public DataFetcher build(Uri uri) {
            return new StreamLocalUriFetcher(this.contentResolver, uri);
        }

        @NonNull
        public ModelLoader build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new UriLoader(this);
        }

        public void teardown() {
        }
    }

    public UriLoader(LocalUriFetcherFactory localUriFetcherFactory) {
        this.factory = localUriFetcherFactory;
    }

    public LoadData buildLoadData(@NonNull Uri uri, int i, int i2, @NonNull Options options) {
        return new LoadData(new ObjectKey(uri), this.factory.build(uri));
    }

    public boolean handles(@NonNull Uri uri) {
        return SCHEMES.contains(uri.getScheme());
    }
}
