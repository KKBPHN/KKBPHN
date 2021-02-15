package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher.DataCallback;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class LocalUriFetcher implements DataFetcher {
    private static final String TAG = "LocalUriFetcher";
    private final ContentResolver contentResolver;
    private Object data;
    private final Uri uri;

    public LocalUriFetcher(ContentResolver contentResolver2, Uri uri2) {
        this.contentResolver = contentResolver2;
        this.uri = uri2;
    }

    public void cancel() {
    }

    public void cleanup() {
        Object obj = this.data;
        if (obj != null) {
            try {
                close(obj);
            } catch (IOException unused) {
            }
        }
    }

    public abstract void close(Object obj);

    @NonNull
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }

    public final void loadData(@NonNull Priority priority, @NonNull DataCallback dataCallback) {
        try {
            this.data = loadResource(this.uri, this.contentResolver);
            dataCallback.onDataReady(this.data);
        } catch (FileNotFoundException e) {
            String str = TAG;
            if (Log.isLoggable(str, 3)) {
                Log.d(str, "Failed to open Uri", e);
            }
            dataCallback.onLoadFailed(e);
        }
    }

    public abstract Object loadResource(Uri uri2, ContentResolver contentResolver2);
}
