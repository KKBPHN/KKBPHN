package com.android.camera.data.observeable;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import java.util.HashMap;

public class DataItemObservable {
    private static final String DEFAULT_KEY = "com.android.camera.ViewModelProvider.DefaultKey";
    private final HashMap mMap = new HashMap();

    @MainThread
    @NonNull
    private VMBase get(@NonNull String str, @NonNull Class cls) {
        VMBase vMBase = get(str);
        if (cls.isInstance(vMBase)) {
            return vMBase;
        }
        VMBase create = create(cls);
        put(str, create);
        return create;
    }

    public final void clear() {
        for (VMBase onCleared : this.mMap.values()) {
            onCleared.onCleared();
        }
        this.mMap.clear();
    }

    @NonNull
    public VMBase create(@NonNull Class cls) {
        String str = "Cannot create an instance of ";
        try {
            return (VMBase) cls.newInstance();
        } catch (InstantiationException e) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(cls);
            throw new RuntimeException(sb.toString(), e);
        } catch (IllegalAccessException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(cls);
            throw new RuntimeException(sb2.toString(), e2);
        }
    }

    @MainThread
    @NonNull
    public VMBase get(@NonNull Class cls) {
        String canonicalName = cls.getCanonicalName();
        if (canonicalName != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("com.android.camera.ViewModelProvider.DefaultKey:");
            sb.append(canonicalName);
            return get(sb.toString(), cls);
        }
        throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
    }

    /* access modifiers changed from: 0000 */
    public final VMBase get(String str) {
        return (VMBase) this.mMap.get(str);
    }

    /* access modifiers changed from: 0000 */
    public final void put(String str, VMBase vMBase) {
        VMBase vMBase2 = (VMBase) this.mMap.put(str, vMBase);
        if (vMBase2 != null) {
            vMBase2.onCleared();
        }
    }
}
