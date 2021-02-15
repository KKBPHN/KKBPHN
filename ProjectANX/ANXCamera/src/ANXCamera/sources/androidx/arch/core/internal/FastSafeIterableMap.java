package androidx.arch.core.internal;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.util.HashMap;
import java.util.Map.Entry;

@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
public class FastSafeIterableMap extends SafeIterableMap {
    private HashMap mHashMap = new HashMap();

    public Entry ceil(Object obj) {
        if (contains(obj)) {
            return ((Entry) this.mHashMap.get(obj)).mPrevious;
        }
        return null;
    }

    public boolean contains(Object obj) {
        return this.mHashMap.containsKey(obj);
    }

    /* access modifiers changed from: protected */
    public Entry get(Object obj) {
        return (Entry) this.mHashMap.get(obj);
    }

    public Object putIfAbsent(@NonNull Object obj, @NonNull Object obj2) {
        Entry entry = get(obj);
        if (entry != null) {
            return entry.mValue;
        }
        this.mHashMap.put(obj, put(obj, obj2));
        return null;
    }

    public Object remove(@NonNull Object obj) {
        Object remove = super.remove(obj);
        this.mHashMap.remove(obj);
        return remove;
    }
}
