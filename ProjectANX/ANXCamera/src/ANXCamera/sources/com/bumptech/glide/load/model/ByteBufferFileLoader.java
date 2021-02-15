package com.bumptech.glide.load.model;

import android.util.Log;
import androidx.annotation.NonNull;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataFetcher.DataCallback;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteBufferFileLoader implements ModelLoader {
    private static final String TAG = "ByteBufferFileLoader";

    final class ByteBufferFetcher implements DataFetcher {
        private final File file;

        ByteBufferFetcher(File file2) {
            this.file = file2;
        }

        public void cancel() {
        }

        public void cleanup() {
        }

        @NonNull
        public Class getDataClass() {
            return ByteBuffer.class;
        }

        @NonNull
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        public void loadData(@NonNull Priority priority, @NonNull DataCallback dataCallback) {
            try {
                dataCallback.onDataReady(ByteBufferUtil.fromFile(this.file));
            } catch (IOException e) {
                String str = ByteBufferFileLoader.TAG;
                if (Log.isLoggable(str, 3)) {
                    Log.d(str, "Failed to obtain ByteBuffer for file", e);
                }
                dataCallback.onLoadFailed(e);
            }
        }
    }

    public class Factory implements ModelLoaderFactory {
        @NonNull
        public ModelLoader build(@NonNull MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ByteBufferFileLoader();
        }

        public void teardown() {
        }
    }

    public LoadData buildLoadData(@NonNull File file, int i, int i2, @NonNull Options options) {
        return new LoadData(new ObjectKey(file), new ByteBufferFetcher(file));
    }

    public boolean handles(@NonNull File file) {
        return true;
    }
}
