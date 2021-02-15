package com.bumptech.glide.load.model;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import androidx.annotation.NonNull;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataFetcher.DataCallback;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.signature.ObjectKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileLoader implements ModelLoader {
    private static final String TAG = "FileLoader";
    private final FileOpener fileOpener;

    public class Factory implements ModelLoaderFactory {
        private final FileOpener opener;

        public Factory(FileOpener fileOpener) {
            this.opener = fileOpener;
        }

        @NonNull
        public final ModelLoader build(@NonNull MultiModelLoaderFactory multiModelLoaderFactory) {
            return new FileLoader(this.opener);
        }

        public final void teardown() {
        }
    }

    public class FileDescriptorFactory extends Factory {
        public FileDescriptorFactory() {
            super(new FileOpener() {
                public void close(ParcelFileDescriptor parcelFileDescriptor) {
                    parcelFileDescriptor.close();
                }

                public Class getDataClass() {
                    return ParcelFileDescriptor.class;
                }

                public ParcelFileDescriptor open(File file) {
                    return ParcelFileDescriptor.open(file, 268435456);
                }
            });
        }
    }

    final class FileFetcher implements DataFetcher {
        private Object data;
        private final File file;
        private final FileOpener opener;

        FileFetcher(File file2, FileOpener fileOpener) {
            this.file = file2;
            this.opener = fileOpener;
        }

        public void cancel() {
        }

        public void cleanup() {
            Object obj = this.data;
            if (obj != null) {
                try {
                    this.opener.close(obj);
                } catch (IOException unused) {
                }
            }
        }

        @NonNull
        public Class getDataClass() {
            return this.opener.getDataClass();
        }

        @NonNull
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        public void loadData(@NonNull Priority priority, @NonNull DataCallback dataCallback) {
            try {
                this.data = this.opener.open(this.file);
                dataCallback.onDataReady(this.data);
            } catch (FileNotFoundException e) {
                String str = FileLoader.TAG;
                if (Log.isLoggable(str, 3)) {
                    Log.d(str, "Failed to open file", e);
                }
                dataCallback.onLoadFailed(e);
            }
        }
    }

    public interface FileOpener {
        void close(Object obj);

        Class getDataClass();

        Object open(File file);
    }

    public class StreamFactory extends Factory {
        public StreamFactory() {
            super(new FileOpener() {
                public void close(InputStream inputStream) {
                    inputStream.close();
                }

                public Class getDataClass() {
                    return InputStream.class;
                }

                public InputStream open(File file) {
                    return new FileInputStream(file);
                }
            });
        }
    }

    public FileLoader(FileOpener fileOpener2) {
        this.fileOpener = fileOpener2;
    }

    public LoadData buildLoadData(@NonNull File file, int i, int i2, @NonNull Options options) {
        return new LoadData(new ObjectKey(file), new FileFetcher(file, this.fileOpener));
    }

    public boolean handles(@NonNull File file) {
        return true;
    }
}
