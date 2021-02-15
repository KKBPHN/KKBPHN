package com.bumptech.glide.load.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pools.Pool;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataFetcher.DataCallback;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

class MultiModelLoader implements ModelLoader {
    private final Pool exceptionListPool;
    private final List modelLoaders;

    class MultiFetcher implements DataFetcher, DataCallback {
        private DataCallback callback;
        private int currentIndex = 0;
        @Nullable
        private List exceptions;
        private final List fetchers;
        private boolean isCancelled;
        private Priority priority;
        private final Pool throwableListPool;

        MultiFetcher(@NonNull List list, @NonNull Pool pool) {
            this.throwableListPool = pool;
            Preconditions.checkNotEmpty((Collection) list);
            this.fetchers = list;
        }

        private void startNextOrFail() {
            if (!this.isCancelled) {
                if (this.currentIndex < this.fetchers.size() - 1) {
                    this.currentIndex++;
                    loadData(this.priority, this.callback);
                } else {
                    Preconditions.checkNotNull(this.exceptions);
                    this.callback.onLoadFailed(new GlideException("Fetch failed", (List) new ArrayList(this.exceptions)));
                }
            }
        }

        public void cancel() {
            this.isCancelled = true;
            for (DataFetcher cancel : this.fetchers) {
                cancel.cancel();
            }
        }

        public void cleanup() {
            List list = this.exceptions;
            if (list != null) {
                this.throwableListPool.release(list);
            }
            this.exceptions = null;
            for (DataFetcher cleanup : this.fetchers) {
                cleanup.cleanup();
            }
        }

        @NonNull
        public Class getDataClass() {
            return ((DataFetcher) this.fetchers.get(0)).getDataClass();
        }

        @NonNull
        public DataSource getDataSource() {
            return ((DataFetcher) this.fetchers.get(0)).getDataSource();
        }

        public void loadData(@NonNull Priority priority2, @NonNull DataCallback dataCallback) {
            this.priority = priority2;
            this.callback = dataCallback;
            this.exceptions = (List) this.throwableListPool.acquire();
            ((DataFetcher) this.fetchers.get(this.currentIndex)).loadData(priority2, this);
            if (this.isCancelled) {
                cancel();
            }
        }

        public void onDataReady(@Nullable Object obj) {
            if (obj != null) {
                this.callback.onDataReady(obj);
            } else {
                startNextOrFail();
            }
        }

        public void onLoadFailed(@NonNull Exception exc) {
            List list = this.exceptions;
            Preconditions.checkNotNull(list);
            list.add(exc);
            startNextOrFail();
        }
    }

    MultiModelLoader(@NonNull List list, @NonNull Pool pool) {
        this.modelLoaders = list;
        this.exceptionListPool = pool;
    }

    public LoadData buildLoadData(@NonNull Object obj, int i, int i2, @NonNull Options options) {
        int size = this.modelLoaders.size();
        ArrayList arrayList = new ArrayList(size);
        Key key = null;
        for (int i3 = 0; i3 < size; i3++) {
            ModelLoader modelLoader = (ModelLoader) this.modelLoaders.get(i3);
            if (modelLoader.handles(obj)) {
                LoadData buildLoadData = modelLoader.buildLoadData(obj, i, i2, options);
                if (buildLoadData != null) {
                    key = buildLoadData.sourceKey;
                    arrayList.add(buildLoadData.fetcher);
                }
            }
        }
        if (arrayList.isEmpty() || key == null) {
            return null;
        }
        return new LoadData(key, new MultiFetcher(arrayList, this.exceptionListPool));
    }

    public boolean handles(@NonNull Object obj) {
        for (ModelLoader handles : this.modelLoaders) {
            if (handles.handles(obj)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MultiModelLoader{modelLoaders=");
        sb.append(Arrays.toString(this.modelLoaders.toArray()));
        sb.append('}');
        return sb.toString();
    }
}
