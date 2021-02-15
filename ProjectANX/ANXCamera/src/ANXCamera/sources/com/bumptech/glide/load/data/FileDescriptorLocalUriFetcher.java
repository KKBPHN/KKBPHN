package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import androidx.annotation.NonNull;
import java.io.FileNotFoundException;

public class FileDescriptorLocalUriFetcher extends LocalUriFetcher {
    public FileDescriptorLocalUriFetcher(ContentResolver contentResolver, Uri uri) {
        super(contentResolver, uri);
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
    public ParcelFileDescriptor loadResource(Uri uri, ContentResolver contentResolver) {
        AssetFileDescriptor openAssetFileDescriptor = contentResolver.openAssetFileDescriptor(uri, "r");
        if (openAssetFileDescriptor != null) {
            return openAssetFileDescriptor.getParcelFileDescriptor();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("FileDescriptor is null for: ");
        sb.append(uri);
        throw new FileNotFoundException(sb.toString());
    }
}
