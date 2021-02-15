package org.greenrobot.greendao.identityscope;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.concurrent.locks.ReentrantLock;
import org.greenrobot.greendao.internal.LongHashMap;

public class IdentityScopeLong implements IdentityScope {
    private final ReentrantLock lock = new ReentrantLock();
    private final LongHashMap map = new LongHashMap();

    public void clear() {
        this.lock.lock();
        try {
            this.map.clear();
        } finally {
            this.lock.unlock();
        }
    }

    public boolean detach(Long l, Object obj) {
        boolean z;
        this.lock.lock();
        try {
            if (get(l) != obj || obj == null) {
                z = false;
            } else {
                remove(l);
                z = true;
            }
            return z;
        } finally {
            this.lock.unlock();
        }
    }

    public Object get(Long l) {
        return get2(l.longValue());
    }

    public Object get2(long j) {
        this.lock.lock();
        try {
            Reference reference = (Reference) this.map.get(j);
            if (reference != null) {
                return reference.get();
            }
            return null;
        } finally {
            this.lock.unlock();
        }
    }

    public Object get2NoLock(long j) {
        Reference reference = (Reference) this.map.get(j);
        if (reference != null) {
            return reference.get();
        }
        return null;
    }

    public Object getNoLock(Long l) {
        return get2NoLock(l.longValue());
    }

    public void lock() {
        this.lock.lock();
    }

    public void put(Long l, Object obj) {
        put2(l.longValue(), obj);
    }

    public void put2(long j, Object obj) {
        this.lock.lock();
        try {
            this.map.put(j, new WeakReference(obj));
        } finally {
            this.lock.unlock();
        }
    }

    public void put2NoLock(long j, Object obj) {
        this.map.put(j, new WeakReference(obj));
    }

    public void putNoLock(Long l, Object obj) {
        put2NoLock(l.longValue(), obj);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.lang.Iterable, code=java.lang.Iterable<java.lang.Long>, for r5v0, types: [java.lang.Iterable<java.lang.Long>, java.lang.Iterable] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void remove(Iterable<Long> iterable) {
        this.lock.lock();
        try {
            for (Long longValue : iterable) {
                this.map.remove(longValue.longValue());
            }
        } finally {
            this.lock.unlock();
        }
    }

    public void remove(Long l) {
        this.lock.lock();
        try {
            this.map.remove(l.longValue());
        } finally {
            this.lock.unlock();
        }
    }

    public void reserveRoom(int i) {
        this.map.reserveRoom(i);
    }

    public void unlock() {
        this.lock.unlock();
    }
}
