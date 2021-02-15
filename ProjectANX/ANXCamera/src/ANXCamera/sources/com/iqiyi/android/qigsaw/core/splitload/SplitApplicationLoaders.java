package com.iqiyi.android.qigsaw.core.splitload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

final class SplitApplicationLoaders {
    private static final AtomicReference sInstance = new AtomicReference();
    private final Set splitDexClassLoaders = Collections.newSetFromMap(new ConcurrentHashMap());

    SplitApplicationLoaders() {
    }

    public static SplitApplicationLoaders getInstance() {
        if (sInstance.get() == null) {
            sInstance.set(new SplitApplicationLoaders());
        }
        return (SplitApplicationLoaders) sInstance.get();
    }

    /* access modifiers changed from: 0000 */
    public void addClassLoader(@NonNull SplitDexClassLoader splitDexClassLoader) {
        this.splitDexClassLoaders.add(splitDexClassLoader);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public SplitDexClassLoader getClassLoader(String str) {
        for (SplitDexClassLoader splitDexClassLoader : this.splitDexClassLoaders) {
            if (splitDexClassLoader.moduleName().equals(str)) {
                return splitDexClassLoader;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public Set getClassLoaders() {
        return this.splitDexClassLoaders;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Set getClassLoaders(@Nullable List list) {
        if (list == null) {
            return null;
        }
        HashSet hashSet = new HashSet(list.size());
        for (SplitDexClassLoader splitDexClassLoader : this.splitDexClassLoaders) {
            if (list.contains(splitDexClassLoader.moduleName())) {
                hashSet.add(splitDexClassLoader);
            }
        }
        return hashSet;
    }

    /* access modifiers changed from: 0000 */
    public boolean removeClassLoader(@NonNull SplitDexClassLoader splitDexClassLoader) {
        return this.splitDexClassLoaders.remove(splitDexClassLoader);
    }
}
