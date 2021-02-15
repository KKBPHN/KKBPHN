package org.greenrobot.greendao.identityscope;

public interface IdentityScope {
    void clear();

    boolean detach(Object obj, Object obj2);

    Object get(Object obj);

    Object getNoLock(Object obj);

    void lock();

    void put(Object obj, Object obj2);

    void putNoLock(Object obj, Object obj2);

    void remove(Iterable iterable);

    void remove(Object obj);

    void reserveRoom(int i);

    void unlock();
}
