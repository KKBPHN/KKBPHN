package com.bumptech.glide.load.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pools.Pool;
import com.bumptech.glide.Registry.NoModelLoaderAvailableException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelLoaderRegistry {
    private final ModelLoaderCache cache;
    private final MultiModelLoaderFactory multiModelLoaderFactory;

    class ModelLoaderCache {
        private final Map cachedModelLoaders = new HashMap();

        class Entry {
            final List loaders;

            public Entry(List list) {
                this.loaders = list;
            }
        }

        ModelLoaderCache() {
        }

        public void clear() {
            this.cachedModelLoaders.clear();
        }

        @Nullable
        public List get(Class cls) {
            Entry entry = (Entry) this.cachedModelLoaders.get(cls);
            if (entry == null) {
                return null;
            }
            return entry.loaders;
        }

        public void put(Class cls, List list) {
            if (((Entry) this.cachedModelLoaders.put(cls, new Entry(list))) != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Already cached loaders for model: ");
                sb.append(cls);
                throw new IllegalStateException(sb.toString());
            }
        }
    }

    public ModelLoaderRegistry(@NonNull Pool pool) {
        this(new MultiModelLoaderFactory(pool));
    }

    private ModelLoaderRegistry(@NonNull MultiModelLoaderFactory multiModelLoaderFactory2) {
        this.cache = new ModelLoaderCache();
        this.multiModelLoaderFactory = multiModelLoaderFactory2;
    }

    @NonNull
    private static Class getClass(@NonNull Object obj) {
        return obj.getClass();
    }

    @NonNull
    private synchronized List getModelLoadersForClass(@NonNull Class cls) {
        List list;
        list = this.cache.get(cls);
        if (list == null) {
            list = Collections.unmodifiableList(this.multiModelLoaderFactory.build(cls));
            this.cache.put(cls, list);
        }
        return list;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.bumptech.glide.load.model.ModelLoaderFactory>, for r1v0, types: [java.util.List, java.util.List<com.bumptech.glide.load.model.ModelLoaderFactory>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void tearDown(@NonNull List<ModelLoaderFactory> list) {
        for (ModelLoaderFactory teardown : list) {
            teardown.teardown();
        }
    }

    public synchronized void append(@NonNull Class cls, @NonNull Class cls2, @NonNull ModelLoaderFactory modelLoaderFactory) {
        this.multiModelLoaderFactory.append(cls, cls2, modelLoaderFactory);
        this.cache.clear();
    }

    public synchronized ModelLoader build(@NonNull Class cls, @NonNull Class cls2) {
        return this.multiModelLoaderFactory.build(cls, cls2);
    }

    @NonNull
    public synchronized List getDataClasses(@NonNull Class cls) {
        return this.multiModelLoaderFactory.getDataClasses(cls);
    }

    @NonNull
    public List getModelLoaders(@NonNull Object obj) {
        List modelLoadersForClass = getModelLoadersForClass(getClass(obj));
        if (!modelLoadersForClass.isEmpty()) {
            int size = modelLoadersForClass.size();
            boolean z = true;
            List emptyList = Collections.emptyList();
            for (int i = 0; i < size; i++) {
                ModelLoader modelLoader = (ModelLoader) modelLoadersForClass.get(i);
                if (modelLoader.handles(obj)) {
                    if (z) {
                        emptyList = new ArrayList(size - i);
                        z = false;
                    }
                    emptyList.add(modelLoader);
                }
            }
            if (!emptyList.isEmpty()) {
                return emptyList;
            }
            throw new NoModelLoaderAvailableException(obj, modelLoadersForClass);
        }
        throw new NoModelLoaderAvailableException(obj);
    }

    public synchronized void prepend(@NonNull Class cls, @NonNull Class cls2, @NonNull ModelLoaderFactory modelLoaderFactory) {
        this.multiModelLoaderFactory.prepend(cls, cls2, modelLoaderFactory);
        this.cache.clear();
    }

    public synchronized void remove(@NonNull Class cls, @NonNull Class cls2) {
        tearDown(this.multiModelLoaderFactory.remove(cls, cls2));
        this.cache.clear();
    }

    public synchronized void replace(@NonNull Class cls, @NonNull Class cls2, @NonNull ModelLoaderFactory modelLoaderFactory) {
        tearDown(this.multiModelLoaderFactory.replace(cls, cls2, modelLoaderFactory));
        this.cache.clear();
    }
}
