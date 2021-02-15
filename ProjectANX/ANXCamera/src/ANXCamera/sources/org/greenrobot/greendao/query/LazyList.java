package org.greenrobot.greendao.query;

import android.database.Cursor;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantLock;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.InternalQueryDaoAccess;

public class LazyList implements List, Closeable {
    private final Cursor cursor;
    private final InternalQueryDaoAccess daoAccess;
    private final List entities;
    private volatile int loadedCount;
    private final ReentrantLock lock;
    /* access modifiers changed from: private */
    public final int size;

    public class LazyIterator implements CloseableListIterator {
        private final boolean closeWhenDone;
        private int index;

        public LazyIterator(int i, boolean z) {
            this.index = i;
            this.closeWhenDone = z;
        }

        public void add(Object obj) {
            throw new UnsupportedOperationException();
        }

        public void close() {
            LazyList.this.close();
        }

        public boolean hasNext() {
            return this.index < LazyList.this.size;
        }

        public boolean hasPrevious() {
            return this.index > 0;
        }

        public Object next() {
            if (this.index < LazyList.this.size) {
                Object obj = LazyList.this.get(this.index);
                this.index++;
                if (this.index == LazyList.this.size && this.closeWhenDone) {
                    close();
                }
                return obj;
            }
            throw new NoSuchElementException();
        }

        public int nextIndex() {
            return this.index;
        }

        public Object previous() {
            int i = this.index;
            if (i > 0) {
                this.index = i - 1;
                return LazyList.this.get(this.index);
            }
            throw new NoSuchElementException();
        }

        public int previousIndex() {
            return this.index - 1;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void set(Object obj) {
            throw new UnsupportedOperationException();
        }
    }

    LazyList(InternalQueryDaoAccess internalQueryDaoAccess, Cursor cursor2, boolean z) {
        this.cursor = cursor2;
        this.daoAccess = internalQueryDaoAccess;
        this.size = cursor2.getCount();
        if (z) {
            this.entities = new ArrayList(this.size);
            for (int i = 0; i < this.size; i++) {
                this.entities.add(null);
            }
        } else {
            this.entities = null;
        }
        if (this.size == 0) {
            cursor2.close();
        }
        this.lock = new ReentrantLock();
    }

    public void add(int i, Object obj) {
        throw new UnsupportedOperationException();
    }

    public boolean add(Object obj) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int i, Collection collection) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection collection) {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public void checkCached() {
        if (this.entities == null) {
            throw new DaoException("This operation only works with cached lazy lists");
        }
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public void close() {
        this.cursor.close();
    }

    public boolean contains(Object obj) {
        loadRemaining();
        return this.entities.contains(obj);
    }

    public boolean containsAll(Collection collection) {
        loadRemaining();
        return this.entities.containsAll(collection);
    }

    public Object get(int i) {
        List list = this.entities;
        if (list != null) {
            Object obj = list.get(i);
            if (obj == null) {
                this.lock.lock();
                try {
                    obj = this.entities.get(i);
                    if (obj == null) {
                        obj = loadEntity(i);
                        this.entities.set(i, obj);
                        this.loadedCount++;
                        if (this.loadedCount == this.size) {
                            this.cursor.close();
                        }
                    }
                } finally {
                    this.lock.unlock();
                }
            }
            return obj;
        }
        this.lock.lock();
        try {
            Object loadEntity = loadEntity(i);
            return loadEntity;
        } finally {
            this.lock.unlock();
        }
    }

    public int getLoadedCount() {
        return this.loadedCount;
    }

    public int indexOf(Object obj) {
        loadRemaining();
        return this.entities.indexOf(obj);
    }

    public boolean isClosed() {
        return this.cursor.isClosed();
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean isLoadedCompletely() {
        return this.loadedCount == this.size;
    }

    public Iterator iterator() {
        return new LazyIterator(0, false);
    }

    public int lastIndexOf(Object obj) {
        loadRemaining();
        return this.entities.lastIndexOf(obj);
    }

    public ListIterator listIterator(int i) {
        return new LazyIterator(i, false);
    }

    public CloseableListIterator listIterator() {
        return new LazyIterator(0, false);
    }

    public CloseableListIterator listIteratorAutoClose() {
        return new LazyIterator(0, true);
    }

    /* access modifiers changed from: protected */
    public Object loadEntity(int i) {
        if (this.cursor.moveToPosition(i)) {
            Object loadCurrent = this.daoAccess.loadCurrent(this.cursor, 0, true);
            if (loadCurrent != null) {
                return loadCurrent;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Loading of entity failed (null) at position ");
            sb.append(i);
            throw new DaoException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Could not move to cursor location ");
        sb2.append(i);
        throw new DaoException(sb2.toString());
    }

    public void loadRemaining() {
        checkCached();
        int size2 = this.entities.size();
        for (int i = 0; i < size2; i++) {
            get(i);
        }
    }

    public Object peek(int i) {
        List list = this.entities;
        if (list != null) {
            return list.get(i);
        }
        return null;
    }

    public Object remove(int i) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection collection) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection collection) {
        throw new UnsupportedOperationException();
    }

    public Object set(int i, Object obj) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return this.size;
    }

    public List subList(int i, int i2) {
        checkCached();
        for (int i3 = i; i3 < i2; i3++) {
            get(i3);
        }
        return this.entities.subList(i, i2);
    }

    public Object[] toArray() {
        loadRemaining();
        return this.entities.toArray();
    }

    public Object[] toArray(Object[] objArr) {
        loadRemaining();
        return this.entities.toArray(objArr);
    }
}
