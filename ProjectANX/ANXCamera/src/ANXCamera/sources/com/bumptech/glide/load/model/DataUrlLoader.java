package com.bumptech.glide.load.model;

import android.util.Base64;
import androidx.annotation.NonNull;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataFetcher.DataCallback;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.signature.ObjectKey;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class DataUrlLoader implements ModelLoader {
    private static final String BASE64_TAG = ";base64";
    private static final String DATA_SCHEME_IMAGE = "data:image";
    private final DataDecoder dataDecoder;

    public interface DataDecoder {
        void close(Object obj);

        Object decode(String str);

        Class getDataClass();
    }

    final class DataUriFetcher implements DataFetcher {
        private Object data;
        private final String dataUri;
        private final DataDecoder reader;

        DataUriFetcher(String str, DataDecoder dataDecoder) {
            this.dataUri = str;
            this.reader = dataDecoder;
        }

        public void cancel() {
        }

        public void cleanup() {
            try {
                this.reader.close(this.data);
            } catch (IOException unused) {
            }
        }

        @NonNull
        public Class getDataClass() {
            return this.reader.getDataClass();
        }

        @NonNull
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        public void loadData(@NonNull Priority priority, @NonNull DataCallback dataCallback) {
            try {
                this.data = this.reader.decode(this.dataUri);
                dataCallback.onDataReady(this.data);
            } catch (IllegalArgumentException e) {
                dataCallback.onLoadFailed(e);
            }
        }
    }

    public final class StreamFactory implements ModelLoaderFactory {
        private final DataDecoder opener = new DataDecoder() {
            public void close(InputStream inputStream) {
                inputStream.close();
            }

            public InputStream decode(String str) {
                if (str.startsWith(DataUrlLoader.DATA_SCHEME_IMAGE)) {
                    int indexOf = str.indexOf(44);
                    if (indexOf == -1) {
                        throw new IllegalArgumentException("Missing comma in data URL.");
                    } else if (str.substring(0, indexOf).endsWith(DataUrlLoader.BASE64_TAG)) {
                        return new ByteArrayInputStream(Base64.decode(str.substring(indexOf + 1), 0));
                    } else {
                        throw new IllegalArgumentException("Not a base64 image data URL.");
                    }
                } else {
                    throw new IllegalArgumentException("Not a valid image data URL.");
                }
            }

            public Class getDataClass() {
                return InputStream.class;
            }
        };

        @NonNull
        public ModelLoader build(@NonNull MultiModelLoaderFactory multiModelLoaderFactory) {
            return new DataUrlLoader(this.opener);
        }

        public void teardown() {
        }
    }

    public DataUrlLoader(DataDecoder dataDecoder2) {
        this.dataDecoder = dataDecoder2;
    }

    public LoadData buildLoadData(@NonNull Object obj, int i, int i2, @NonNull Options options) {
        return new LoadData(new ObjectKey(obj), new DataUriFetcher(obj.toString(), this.dataDecoder));
    }

    public boolean handles(@NonNull Object obj) {
        return obj.toString().startsWith(DATA_SCHEME_IMAGE);
    }
}
