package com.bumptech.glide.load.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataFetcher.DataCallback;
import com.bumptech.glide.load.data.mediastore.MediaStoreUtil;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.signature.ObjectKey;
import java.io.File;
import java.io.FileNotFoundException;

public final class MediaStoreFileLoader implements ModelLoader {
    private final Context context;

    public final class Factory implements ModelLoaderFactory {
        private final Context context;

        public Factory(Context context2) {
            this.context = context2;
        }

        @NonNull
        public ModelLoader build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new MediaStoreFileLoader(this.context);
        }

        public void teardown() {
        }
    }

    class FilePathFetcher implements DataFetcher {
        private static final String[] PROJECTION = {"_data"};
        private final Context context;
        private final Uri uri;

        FilePathFetcher(Context context2, Uri uri2) {
            this.context = context2;
            this.uri = uri2;
        }

        public void cancel() {
        }

        public void cleanup() {
        }

        @NonNull
        public Class getDataClass() {
            return File.class;
        }

        @NonNull
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        public void loadData(@NonNull Priority priority, @NonNull DataCallback dataCallback) {
            Cursor query = this.context.getContentResolver().query(this.uri, PROJECTION, null, null, null);
            String str = null;
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        str = query.getString(query.getColumnIndexOrThrow("_data"));
                    }
                } finally {
                    query.close();
                }
            }
            if (TextUtils.isEmpty(str)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Failed to find file path for: ");
                sb.append(this.uri);
                dataCallback.onLoadFailed(new FileNotFoundException(sb.toString()));
                return;
            }
            dataCallback.onDataReady(new File(str));
        }
    }

    public MediaStoreFileLoader(Context context2) {
        this.context = context2;
    }

    public LoadData buildLoadData(@NonNull Uri uri, int i, int i2, @NonNull Options options) {
        return new LoadData(new ObjectKey(uri), new FilePathFetcher(this.context, uri));
    }

    public boolean handles(@NonNull Uri uri) {
        return MediaStoreUtil.isMediaStoreUri(uri);
    }
}
