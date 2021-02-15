package com.bumptech.glide.load.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.util.Pools.Pool;
import com.bumptech.glide.Registry.NoModelLoaderAvailableException;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MultiModelLoaderFactory {
    private static final Factory DEFAULT_FACTORY = new Factory();
    private static final ModelLoader EMPTY_MODEL_LOADER = new EmptyModelLoader();
    private final Set alreadyUsedEntries;
    private final List entries;
    private final Factory factory;
    private final Pool throwableListPool;

    class EmptyModelLoader implements ModelLoader {
        EmptyModelLoader() {
        }

        @Nullable
        public LoadData buildLoadData(@NonNull Object obj, int i, int i2, @NonNull Options options) {
            return null;
        }

        public boolean handles(@NonNull Object obj) {
            return false;
        }
    }

    class Entry {
        final Class dataClass;
        final ModelLoaderFactory factory;
        private final Class modelClass;

        public Entry(@NonNull Class cls, @NonNull Class cls2, @NonNull ModelLoaderFactory modelLoaderFactory) {
            this.modelClass = cls;
            this.dataClass = cls2;
            this.factory = modelLoaderFactory;
        }

        public boolean handles(@NonNull Class cls) {
            return this.modelClass.isAssignableFrom(cls);
        }

        public boolean handles(@NonNull Class cls, @NonNull Class cls2) {
            return handles(cls) && this.dataClass.isAssignableFrom(cls2);
        }
    }

    class Factory {
        Factory() {
        }

        @NonNull
        public MultiModelLoader build(@NonNull List list, @NonNull Pool pool) {
            return new MultiModelLoader(list, pool);
        }
    }

    public MultiModelLoaderFactory(@NonNull Pool pool) {
        this(pool, DEFAULT_FACTORY);
    }

    @VisibleForTesting
    MultiModelLoaderFactory(@NonNull Pool pool, @NonNull Factory factory2) {
        this.entries = new ArrayList();
        this.alreadyUsedEntries = new HashSet();
        this.throwableListPool = pool;
        this.factory = factory2;
    }

    private void add(@NonNull Class cls, @NonNull Class cls2, @NonNull ModelLoaderFactory modelLoaderFactory, boolean z) {
        Entry entry = new Entry(cls, cls2, modelLoaderFactory);
        List list = this.entries;
        list.add(z ? list.size() : 0, entry);
    }

    @NonNull
    private ModelLoader build(@NonNull Entry entry) {
        ModelLoader build = entry.factory.build(this);
        Preconditions.checkNotNull(build);
        return build;
    }

    @NonNull
    private static ModelLoader emptyModelLoader() {
        return EMPTY_MODEL_LOADER;
    }

    @NonNull
    private ModelLoaderFactory getFactory(@NonNull Entry entry) {
        return entry.factory;
    }

    /* access modifiers changed from: 0000 */
    public synchronized void append(@NonNull Class cls, @NonNull Class cls2, @NonNull ModelLoaderFactory modelLoaderFactory) {
        add(cls, cls2, modelLoaderFactory, true);
    }

    @NonNull
    public synchronized ModelLoader build(@NonNull Class cls, @NonNull Class cls2) {
        try {
            ArrayList arrayList = new ArrayList();
            boolean z = false;
            for (Entry entry : this.entries) {
                if (this.alreadyUsedEntries.contains(entry)) {
                    z = true;
                } else if (entry.handles(cls, cls2)) {
                    this.alreadyUsedEntries.add(entry);
                    arrayList.add(build(entry));
                    this.alreadyUsedEntries.remove(entry);
                }
            }
            if (arrayList.size() > 1) {
                return this.factory.build(arrayList, this.throwableListPool);
            } else if (arrayList.size() == 1) {
                return (ModelLoader) arrayList.get(0);
            } else if (z) {
                return emptyModelLoader();
            } else {
                throw new NoModelLoaderAvailableException(cls, cls2);
            }
        } catch (Throwable th) {
            this.alreadyUsedEntries.clear();
            throw th;
        }
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public synchronized List build(@NonNull Class cls) {
        ArrayList arrayList;
        try {
            arrayList = new ArrayList();
            for (Entry entry : this.entries) {
                if (!this.alreadyUsedEntries.contains(entry)) {
                    if (entry.handles(cls)) {
                        this.alreadyUsedEntries.add(entry);
                        arrayList.add(build(entry));
                        this.alreadyUsedEntries.remove(entry);
                    }
                }
            }
        } catch (Throwable th) {
            this.alreadyUsedEntries.clear();
            throw th;
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public synchronized List getDataClasses(@NonNull Class cls) {
        ArrayList arrayList;
        arrayList = new ArrayList();
        for (Entry entry : this.entries) {
            if (!arrayList.contains(entry.dataClass) && entry.handles(cls)) {
                arrayList.add(entry.dataClass);
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    public synchronized void prepend(@NonNull Class cls, @NonNull Class cls2, @NonNull ModelLoaderFactory modelLoaderFactory) {
        add(cls, cls2, modelLoaderFactory, false);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public synchronized List remove(@NonNull Class cls, @NonNull Class cls2) {
        ArrayList arrayList;
        arrayList = new ArrayList();
        Iterator it = this.entries.iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            if (entry.handles(cls, cls2)) {
                it.remove();
                arrayList.add(getFactory(entry));
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public synchronized List replace(@NonNull Class cls, @NonNull Class cls2, @NonNull ModelLoaderFactory modelLoaderFactory) {
        List remove;
        remove = remove(cls, cls2);
        append(cls, cls2, modelLoaderFactory);
        return remove;
    }
}
