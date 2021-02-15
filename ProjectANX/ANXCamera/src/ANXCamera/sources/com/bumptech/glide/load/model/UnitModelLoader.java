package com.bumptech.glide.load.model;

import androidx.annotation.NonNull;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataFetcher.DataCallback;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.signature.ObjectKey;

public class UnitModelLoader implements ModelLoader {
    private static final UnitModelLoader INSTANCE = new UnitModelLoader();

    public class Factory implements ModelLoaderFactory {
        private static final Factory FACTORY = new Factory();

        public static Factory getInstance() {
            return FACTORY;
        }

        @NonNull
        public ModelLoader build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return UnitModelLoader.getInstance();
        }

        public void teardown() {
        }
    }

    class UnitFetcher implements DataFetcher {
        private final Object resource;

        UnitFetcher(Object obj) {
            this.resource = obj;
        }

        public void cancel() {
        }

        public void cleanup() {
        }

        @NonNull
        public Class getDataClass() {
            return this.resource.getClass();
        }

        @NonNull
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        public void loadData(@NonNull Priority priority, @NonNull DataCallback dataCallback) {
            dataCallback.onDataReady(this.resource);
        }
    }

    public static UnitModelLoader getInstance() {
        return INSTANCE;
    }

    public LoadData buildLoadData(@NonNull Object obj, int i, int i2, @NonNull Options options) {
        return new LoadData(new ObjectKey(obj), new UnitFetcher(obj));
    }

    public boolean handles(@NonNull Object obj) {
        return true;
    }
}
