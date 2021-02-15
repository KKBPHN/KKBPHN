package com.bumptech.glide.load.model;

import android.content.res.AssetManager;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.FileDescriptorAssetPathFetcher;
import com.bumptech.glide.load.data.StreamAssetPathFetcher;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.signature.ObjectKey;
import com.ss.android.ugc.effectmanager.effect.model.ComposerHelper;

public class AssetUriLoader implements ModelLoader {
    private static final String ASSET_PATH_SEGMENT = "android_asset";
    private static final String ASSET_PREFIX = "file:///android_asset/";
    private static final int ASSET_PREFIX_LENGTH = 22;
    private final AssetManager assetManager;
    private final AssetFetcherFactory factory;

    public interface AssetFetcherFactory {
        DataFetcher buildFetcher(AssetManager assetManager, String str);
    }

    public class FileDescriptorFactory implements ModelLoaderFactory, AssetFetcherFactory {
        private final AssetManager assetManager;

        public FileDescriptorFactory(AssetManager assetManager2) {
            this.assetManager = assetManager2;
        }

        @NonNull
        public ModelLoader build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new AssetUriLoader(this.assetManager, this);
        }

        public DataFetcher buildFetcher(AssetManager assetManager2, String str) {
            return new FileDescriptorAssetPathFetcher(assetManager2, str);
        }

        public void teardown() {
        }
    }

    public class StreamFactory implements ModelLoaderFactory, AssetFetcherFactory {
        private final AssetManager assetManager;

        public StreamFactory(AssetManager assetManager2) {
            this.assetManager = assetManager2;
        }

        @NonNull
        public ModelLoader build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new AssetUriLoader(this.assetManager, this);
        }

        public DataFetcher buildFetcher(AssetManager assetManager2, String str) {
            return new StreamAssetPathFetcher(assetManager2, str);
        }

        public void teardown() {
        }
    }

    public AssetUriLoader(AssetManager assetManager2, AssetFetcherFactory assetFetcherFactory) {
        this.assetManager = assetManager2;
        this.factory = assetFetcherFactory;
    }

    public LoadData buildLoadData(@NonNull Uri uri, int i, int i2, @NonNull Options options) {
        return new LoadData(new ObjectKey(uri), this.factory.buildFetcher(this.assetManager, uri.toString().substring(ASSET_PREFIX_LENGTH)));
    }

    public boolean handles(@NonNull Uri uri) {
        if (!ComposerHelper.COMPOSER_PATH.equals(uri.getScheme()) || uri.getPathSegments().isEmpty()) {
            return false;
        }
        return ASSET_PATH_SEGMENT.equals(uri.getPathSegments().get(0));
    }
}
