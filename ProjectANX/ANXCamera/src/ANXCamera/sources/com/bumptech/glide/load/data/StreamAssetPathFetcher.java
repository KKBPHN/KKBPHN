package com.bumptech.glide.load.data;

import android.content.res.AssetManager;
import androidx.annotation.NonNull;
import java.io.InputStream;

public class StreamAssetPathFetcher extends AssetPathFetcher {
    public StreamAssetPathFetcher(AssetManager assetManager, String str) {
        super(assetManager, str);
    }

    /* access modifiers changed from: protected */
    public void close(InputStream inputStream) {
        inputStream.close();
    }

    @NonNull
    public Class getDataClass() {
        return InputStream.class;
    }

    /* access modifiers changed from: protected */
    public InputStream loadResource(AssetManager assetManager, String str) {
        return assetManager.open(str);
    }
}
