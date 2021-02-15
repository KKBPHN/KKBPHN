package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import androidx.annotation.NonNull;
import java.io.FileNotFoundException;

public final class AssetFileDescriptorLocalUriFetcher extends LocalUriFetcher {
    public AssetFileDescriptorLocalUriFetcher(ContentResolver contentResolver, Uri uri) {
        super(contentResolver, uri);
    }

    /* access modifiers changed from: protected */
    public void close(AssetFileDescriptor assetFileDescriptor) {
        assetFileDescriptor.close();
    }

    @NonNull
    public Class getDataClass() {
        return AssetFileDescriptor.class;
    }

    /* access modifiers changed from: protected */
    public AssetFileDescriptor loadResource(Uri uri, ContentResolver contentResolver) {
        AssetFileDescriptor openAssetFileDescriptor = contentResolver.openAssetFileDescriptor(uri, "r");
        if (openAssetFileDescriptor != null) {
            return openAssetFileDescriptor;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("FileDescriptor is null for: ");
        sb.append(uri);
        throw new FileNotFoundException(sb.toString());
    }
}
