package miui.util.cache;

import android.app.ActivityManager;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;
import miui.util.AppConstants;

public class LruCache implements Cache {
    private static final int BASE_MEMORY_SIZE = ((((ActivityManager) AppConstants.getCurrentApplication().getSystemService("activity")).getMemoryClass() * 1024) * 1024);
    private final LinkedHashMap mCacheItems;
    private final ReentrantLock mLock;
    private long mMaxSize;
    private long mTotalSize;

    class CacheItem {
        public Object cacheItem;
        public Object key;
        public int size;
        public SoftReference softCacheItem;

        private CacheItem() {
        }
    }

    public LruCache(int i) {
        if (i < 0) {
            i = BASE_MEMORY_SIZE / 8;
        }
        this.mLock = new ReentrantLock();
        this.mMaxSize = (long) i;
        this.mTotalSize = 0;
        this.mCacheItems = new LinkedHashMap(0, 0.75f, true);
    }

    private void trim() {
        Iterator it = this.mCacheItems.entrySet().iterator();
        while (it.hasNext()) {
            CacheItem cacheItem = (CacheItem) ((Entry) it.next()).getValue();
            if (cacheItem.cacheItem == null) {
                SoftReference softReference = cacheItem.softCacheItem;
                if (softReference == null || softReference.get() == null) {
                    it.remove();
                }
            }
        }
        Iterator it2 = this.mCacheItems.entrySet().iterator();
        while (this.mTotalSize > this.mMaxSize && it2.hasNext()) {
            CacheItem cacheItem2 = (CacheItem) ((Entry) it2.next()).getValue();
            if (cacheItem2 != null) {
                if (cacheItem2.softCacheItem == null) {
                    cacheItem2.softCacheItem = new SoftReference(cacheItem2.cacheItem);
                }
                this.mTotalSize -= (long) cacheItem2.size;
                cacheItem2.cacheItem = null;
            }
        }
    }

    public void clear() {
        this.mLock.lock();
        try {
            for (Entry value : this.mCacheItems.entrySet()) {
                CacheItem cacheItem = (CacheItem) value.getValue();
                if (cacheItem.softCacheItem != null) {
                    cacheItem.softCacheItem.clear();
                }
            }
            this.mCacheItems.clear();
        } finally {
            this.mLock.unlock();
        }
    }

    public Object get(Object obj) {
        this.mLock.lock();
        try {
            trim();
            CacheItem cacheItem = (CacheItem) this.mCacheItems.get(obj);
            if (cacheItem != null) {
                if (cacheItem.cacheItem == null) {
                    if (cacheItem.softCacheItem != null) {
                        cacheItem.cacheItem = cacheItem.softCacheItem.get();
                        if (cacheItem.cacheItem != null) {
                            this.mTotalSize += (long) cacheItem.size;
                            if (this.mTotalSize > this.mMaxSize) {
                                trim();
                            }
                        }
                    }
                    this.mCacheItems.remove(obj);
                }
                Object obj2 = cacheItem.cacheItem;
                this.mLock.unlock();
                return obj2;
            }
            return null;
        } finally {
            this.mLock.unlock();
        }
    }

    public int getCount() {
        this.mLock.lock();
        try {
            trim();
            int size = this.mCacheItems.size();
            return size;
        } finally {
            this.mLock.unlock();
        }
    }

    public void put(Object obj, Object obj2, int i) {
        if (obj != null && obj2 != null) {
            CacheItem cacheItem = new CacheItem();
            cacheItem.key = obj;
            cacheItem.cacheItem = obj2;
            if (i < 0) {
                i = 0;
            }
            cacheItem.size = i;
            this.mLock.lock();
            try {
                this.mCacheItems.put(obj, cacheItem);
                this.mTotalSize += (long) cacheItem.size;
                trim();
            } finally {
                this.mLock.unlock();
            }
        }
    }

    public void setMaxSize(int i) {
        if (i < 0) {
            i = BASE_MEMORY_SIZE / 8;
        }
        this.mMaxSize = (long) i;
        try {
            this.mLock.lock();
            if (this.mTotalSize < this.mMaxSize) {
                trim();
            }
        } finally {
            this.mLock.unlock();
        }
    }
}
