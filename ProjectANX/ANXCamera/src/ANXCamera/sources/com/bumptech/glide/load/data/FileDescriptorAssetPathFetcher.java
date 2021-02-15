package com.bumptech.glide.load.data;

import android.content.res.AssetManager;
import android.os.ParcelFileDescriptor;
import androidx.annotation.NonNull;

public class FileDescriptorAssetPathFetcher extends AssetPathFetcher {
    public FileDescriptorAssetPathFetcher(AssetManager assetManager, String str) {
        super(assetManager, str);
    }

    /* access modifiers changed from: protected */
    public void close(ParcelFileDescriptor parcelFileDescriptor) {
        parcelFileDescriptor.close();
    }

    @NonNull
    public Class getDataClass() {
        return ParcelFileDescriptor.class;
    }

    /* access modifiers changed from: protected */
    public ParcelFileDescriptor loadResource(AssetManager assetManager, String str) {
        return assetManager.openFd(str).getParcelFileDescriptor();
    }
}
