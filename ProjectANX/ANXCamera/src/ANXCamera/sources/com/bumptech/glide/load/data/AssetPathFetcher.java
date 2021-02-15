package com.bumptech.glide.load.data;

import android.content.res.AssetManager;
import android.util.Log;
import androidx.annotation.NonNull;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher.DataCallback;
import java.io.IOException;

public abstract class AssetPathFetcher implements DataFetcher {
    private static final String TAG = "AssetPathFetcher";
    private final AssetManager assetManager;
    private final String assetPath;
    private Object data;

    public AssetPathFetcher(AssetManager assetManager2, String str) {
        this.assetManager = assetManager2;
        this.assetPath = str;
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

    public void loadData(@NonNull Priority priority, @NonNull DataCallback dataCallback) {
        try {
            this.data = loadResource(this.assetManager, this.assetPath);
            dataCallback.onDataReady(this.data);
        } catch (IOException e) {
            String str = TAG;
            if (Log.isLoggable(str, 3)) {
                Log.d(str, "Failed to load data from asset manager", e);
            }
            dataCallback.onLoadFailed(e);
        }
    }

    public abstract Object loadResource(AssetManager assetManager2, String str);
}
