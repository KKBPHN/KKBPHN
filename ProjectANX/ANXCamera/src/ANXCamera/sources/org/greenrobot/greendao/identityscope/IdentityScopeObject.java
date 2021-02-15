package org.greenrobot.greendao.identityscope;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class IdentityScopeObject implements IdentityScope {
    private final ReentrantLock lock = new ReentrantLock();
    private final HashMap map = new HashMap();

    public void clear() {
        this.lock.lock();
        try {
            this.map.clear();
        } finally {
            this.lock.unlock();
        }
    }

    public boolean detach(Object obj, Object obj2) {
        boolean z;
        this.lock.lock();
        try {
            if (get(obj) != obj2 || obj2 == null) {
                z = false;
            } else {
                remove(obj);
                z = true;
            }
            return z;
        } finally {
            this.lock.unlock();
        }
    }

    public Object get(Object obj) {
        this.lock.lock();
        try {
            Reference reference = (Reference) this.map.get(obj);
            if (reference != null) {
                return reference.get();
            }
            return null;
        } finally {
            this.lock.unlock();
        }
    }

    public Object getNoLock(Object obj) {
        Reference reference = (Reference) this.map.get(obj);
        if (reference != null) {
            return reference.get();
        }
        return null;
    }

    public void lock() {
        this.lock.lock();
    }

    public void put(Object obj, Object obj2) {
        this.lock.lock();
        try {
            this.map.put(obj, new WeakReference(obj2));
        } finally {
            this.lock.unlock();
        }
    }

    public void putNoLock(Object obj, Object obj2) {
        this.map.put(obj, new WeakReference(obj2));
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.lang.Iterable, code=java.lang.Iterable<java.lang.Object>, for r3v0, types: [java.lang.Iterable<java.lang.Object>, java.lang.Iterable] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void remove(Iterable<Object> iterable) {
        this.lock.lock();
        try {
            for (Object remove : iterable) {
                this.map.remove(remove);
            }
        } finally {
            this.lock.unlock();
        }
    }

    public void remove(Object obj) {
        this.lock.lock();
        try {
            this.map.remove(obj);
        } finally {
            this.lock.unlock();
        }
    }

    public void reserveRoom(int i) {
    }

    public void unlock() {
        this.lock.unlock();
    }
}
