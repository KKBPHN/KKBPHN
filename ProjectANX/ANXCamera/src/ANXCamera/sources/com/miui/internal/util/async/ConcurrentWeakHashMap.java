package com.miui.internal.util.async;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentWeakHashMap {
    private final ConcurrentHashMap mMap = new ConcurrentHashMap();
    private final ReferenceQueue mRefQueue = new ReferenceQueue();

    class WeakKey extends WeakReference {
        private int mHashCode;

        public WeakKey(Object obj) {
            super(obj);
            setHashCode(obj);
        }

        public WeakKey(Object obj, ReferenceQueue referenceQueue) {
            super(obj, referenceQueue);
            setHashCode(obj);
        }

        private void setHashCode(Object obj) {
            this.mHashCode = obj != null ? obj.hashCode() : 0;
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof WeakKey)) {
                return false;
            }
            WeakKey weakKey = (WeakKey) obj;
            Object obj2 = get();
            Object obj3 = weakKey.get();
            if (obj2 == null || !obj2.equals(obj3)) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return this.mHashCode;
        }
    }

    private void clean() {
        while (true) {
            Reference poll = this.mRefQueue.poll();
            if (poll != null) {
                this.mMap.remove((WeakKey) poll);
            } else {
                return;
            }
        }
    }

    public boolean containsKey(Object obj) {
        if (obj == null) {
            return false;
        }
        clean();
        return this.mMap.containsKey(new WeakKey(obj));
    }

    public Object get(Object obj) {
        clean();
        return this.mMap.get(new WeakKey(obj));
    }

    public Collection getKeys(Collection collection) {
        clean();
        for (WeakKey weakKey : this.mMap.keySet()) {
            Object obj = weakKey.get();
            if (obj != null) {
                collection.add(obj);
            }
        }
        return collection;
    }

    public Object put(Object obj, Object obj2) {
        if (obj == null || obj2 == null) {
            return null;
        }
        clean();
        return this.mMap.put(new WeakKey(obj, this.mRefQueue), obj2);
    }

    public Object remove(Object obj) {
        if (obj == null) {
            return null;
        }
        clean();
        return this.mMap.remove(new WeakKey(obj));
    }
}
