package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataFetcher.DataCallback;
import com.bumptech.glide.load.data.mediastore.MediaStoreUtil;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

@RequiresApi(29)
public final class QMediaStoreUriLoader implements ModelLoader {
    private final Context context;
    private final Class dataClass;
    private final ModelLoader fileDelegate;
    private final ModelLoader uriDelegate;

    abstract class Factory implements ModelLoaderFactory {
        private final Context context;
        private final Class dataClass;

        Factory(Context context2, Class cls) {
            this.context = context2;
            this.dataClass = cls;
        }

        @NonNull
        public final ModelLoader build(@NonNull MultiModelLoaderFactory multiModelLoaderFactory) {
            return new QMediaStoreUriLoader(this.context, multiModelLoaderFactory.build(File.class, this.dataClass), multiModelLoaderFactory.build(Uri.class, this.dataClass), this.dataClass);
        }

        public final void teardown() {
        }
    }

    @RequiresApi(29)
    public final class FileDescriptorFactory extends Factory {
        public FileDescriptorFactory(Context context) {
            super(context, ParcelFileDescriptor.class);
        }
    }

    @RequiresApi(29)
    public final class InputStreamFactory extends Factory {
        public InputStreamFactory(Context context) {
            super(context, InputStream.class);
        }
    }

    final class QMediaStoreUriFetcher implements DataFetcher {
        private static final String[] PROJECTION = {"_data"};
        private final Context context;
        private final Class dataClass;
        @Nullable
        private volatile DataFetcher delegate;
        private final ModelLoader fileDelegate;
        private final int height;
        private volatile boolean isCancelled;
        private final Options options;
        private final Uri uri;
        private final ModelLoader uriDelegate;
        private final int width;

        QMediaStoreUriFetcher(Context context2, ModelLoader modelLoader, ModelLoader modelLoader2, Uri uri2, int i, int i2, Options options2, Class cls) {
            this.context = context2.getApplicationContext();
            this.fileDelegate = modelLoader;
            this.uriDelegate = modelLoader2;
            this.uri = uri2;
            this.width = i;
            this.height = i2;
            this.options = options2;
            this.dataClass = cls;
        }

        @Nullable
        private LoadData buildDelegateData() {
            if (Environment.isExternalStorageLegacy()) {
                return this.fileDelegate.buildLoadData(queryForFilePath(this.uri), this.width, this.height, this.options);
            }
            return this.uriDelegate.buildLoadData(isAccessMediaLocationGranted() ? MediaStore.setRequireOriginal(this.uri) : this.uri, this.width, this.height, this.options);
        }

        @Nullable
        private DataFetcher buildDelegateFetcher() {
            LoadData buildDelegateData = buildDelegateData();
            if (buildDelegateData != null) {
                return buildDelegateData.fetcher;
            }
            return null;
        }

        private boolean isAccessMediaLocationGranted() {
            return this.context.checkSelfPermission("android.permission.ACCESS_MEDIA_LOCATION") == 0;
        }

        /* JADX INFO: finally extract failed */
        @NonNull
        private File queryForFilePath(Uri uri2) {
            Cursor cursor = null;
            try {
                cursor = this.context.getContentResolver().query(uri2, PROJECTION, null, null, null);
                if (cursor == null || !cursor.moveToFirst()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Failed to media store entry for: ");
                    sb.append(uri2);
                    throw new FileNotFoundException(sb.toString());
                }
                String string = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                if (!TextUtils.isEmpty(string)) {
                    File file = new File(string);
                    if (cursor != null) {
                        cursor.close();
                    }
                    return file;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("File path was empty in media store for: ");
                sb2.append(uri2);
                throw new FileNotFoundException(sb2.toString());
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        }

        public void cancel() {
            this.isCancelled = true;
            DataFetcher dataFetcher = this.delegate;
            if (dataFetcher != null) {
                dataFetcher.cancel();
            }
        }

        public void cleanup() {
            DataFetcher dataFetcher = this.delegate;
            if (dataFetcher != null) {
                dataFetcher.cleanup();
            }
        }

        @NonNull
        public Class getDataClass() {
            return this.dataClass;
        }

        @NonNull
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        public void loadData(@NonNull Priority priority, @NonNull DataCallback dataCallback) {
            try {
                DataFetcher buildDelegateFetcher = buildDelegateFetcher();
                if (buildDelegateFetcher == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Failed to build fetcher for: ");
                    sb.append(this.uri);
                    dataCallback.onLoadFailed(new IllegalArgumentException(sb.toString()));
                    return;
                }
                this.delegate = buildDelegateFetcher;
                if (this.isCancelled) {
                    cancel();
                } else {
                    buildDelegateFetcher.loadData(priority, dataCallback);
                }
            } catch (FileNotFoundException e) {
                dataCallback.onLoadFailed(e);
            }
        }
    }

    QMediaStoreUriLoader(Context context2, ModelLoader modelLoader, ModelLoader modelLoader2, Class cls) {
        this.context = context2.getApplicationContext();
        this.fileDelegate = modelLoader;
        this.uriDelegate = modelLoader2;
        this.dataClass = cls;
    }

    public LoadData buildLoadData(@NonNull Uri uri, int i, int i2, @NonNull Options options) {
        Uri uri2 = uri;
        ObjectKey objectKey = new ObjectKey(uri);
        QMediaStoreUriFetcher qMediaStoreUriFetcher = new QMediaStoreUriFetcher(this.context, this.fileDelegate, this.uriDelegate, uri2, i, i2, options, this.dataClass);
        return new LoadData(objectKey, qMediaStoreUriFetcher);
    }

    public boolean handles(@NonNull Uri uri) {
        return VERSION.SDK_INT >= 29 && MediaStoreUtil.isMediaStoreUri(uri);
    }
}
