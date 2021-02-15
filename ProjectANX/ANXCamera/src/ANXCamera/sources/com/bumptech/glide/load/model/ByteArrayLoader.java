package com.bumptech.glide.load.model;

import androidx.annotation.NonNull;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataFetcher.DataCallback;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.signature.ObjectKey;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteArrayLoader implements ModelLoader {
    private final Converter converter;

    public class ByteBufferFactory implements ModelLoaderFactory {
        @NonNull
        public ModelLoader build(@NonNull MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ByteArrayLoader(new Converter() {
                public ByteBuffer convert(byte[] bArr) {
                    return ByteBuffer.wrap(bArr);
                }

                public Class getDataClass() {
                    return ByteBuffer.class;
                }
            });
        }

        public void teardown() {
        }
    }

    public interface Converter {
        Object convert(byte[] bArr);

        Class getDataClass();
    }

    class Fetcher implements DataFetcher {
        private final Converter converter;
        private final byte[] model;

        Fetcher(byte[] bArr, Converter converter2) {
            this.model = bArr;
            this.converter = converter2;
        }

        public void cancel() {
        }

        public void cleanup() {
        }

        @NonNull
        public Class getDataClass() {
            return this.converter.getDataClass();
        }

        @NonNull
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        public void loadData(@NonNull Priority priority, @NonNull DataCallback dataCallback) {
            dataCallback.onDataReady(this.converter.convert(this.model));
        }
    }

    public class StreamFactory implements ModelLoaderFactory {
        @NonNull
        public ModelLoader build(@NonNull MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ByteArrayLoader(new Converter() {
                public InputStream convert(byte[] bArr) {
                    return new ByteArrayInputStream(bArr);
                }

                public Class getDataClass() {
                    return InputStream.class;
                }
            });
        }

        public void teardown() {
        }
    }

    public ByteArrayLoader(Converter converter2) {
        this.converter = converter2;
    }

    public LoadData buildLoadData(@NonNull byte[] bArr, int i, int i2, @NonNull Options options) {
        return new LoadData(new ObjectKey(bArr), new Fetcher(bArr, this.converter));
    }

    public boolean handles(@NonNull byte[] bArr) {
        return true;
    }
}
