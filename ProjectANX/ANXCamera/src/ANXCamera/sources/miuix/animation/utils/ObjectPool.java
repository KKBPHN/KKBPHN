package miuix.animation.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectPool {
    private static final long DELAY = 5000;
    private static final int MAX_POOL_SIZE = 5;
    private static final ConcurrentHashMap sCacheMap = new ConcurrentHashMap();
    /* access modifiers changed from: private */
    public static final Handler sMainHandler = new Handler(Looper.getMainLooper());

    class Cache {
        final LinkedList pool;
        final Runnable shrinkTask;

        private Cache() {
            this.pool = new LinkedList();
            this.shrinkTask = new Runnable() {
                public void run() {
                    Cache.this.shrink();
                }
            };
        }

        /* access modifiers changed from: 0000 */
        public synchronized Object acquireObject(Class cls, Object... objArr) {
            Object poll;
            poll = this.pool.poll();
            if (poll == null) {
                poll = ObjectPool.createObject(cls, objArr);
            }
            return poll;
        }

        /* access modifiers changed from: 0000 */
        public synchronized void releaseObject(Object obj) {
            this.pool.add(obj);
            ObjectPool.sMainHandler.removeCallbacks(this.shrinkTask);
            if (this.pool.size() > 5) {
                ObjectPool.sMainHandler.postDelayed(this.shrinkTask, ObjectPool.DELAY);
            }
        }

        /* access modifiers changed from: 0000 */
        public synchronized void shrink() {
            while (this.pool.size() > 5) {
                this.pool.poll();
            }
        }
    }

    public interface IPoolObject {
        void clear();
    }

    private ObjectPool() {
    }

    public static Object acquire(Class cls, Object... objArr) {
        return getObjectCache(cls, true).acquireObject(cls, objArr);
    }

    /* access modifiers changed from: private */
    public static Object createObject(Class cls, Object... objArr) {
        try {
            Constructor[] declaredConstructors = cls.getDeclaredConstructors();
            int length = declaredConstructors.length;
            int i = 0;
            while (i < length) {
                Constructor constructor = declaredConstructors[i];
                if (constructor.getParameterTypes().length != objArr.length) {
                    i++;
                } else {
                    constructor.setAccessible(true);
                    return constructor.newInstance(objArr);
                }
            }
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("ObjectPool.createObject failed, clz = ");
            sb.append(cls);
            Log.w(CommonUtils.TAG, sb.toString(), e);
        }
        return null;
    }

    private static Cache getObjectCache(Class cls, boolean z) {
        Cache cache = (Cache) sCacheMap.get(cls);
        if (cache != null || !z) {
            return cache;
        }
        Cache cache2 = new Cache();
        Cache cache3 = (Cache) sCacheMap.putIfAbsent(cls, cache2);
        return cache3 != null ? cache3 : cache2;
    }

    public static void release(Object obj) {
        if (obj != null) {
            Class cls = obj.getClass();
            if (obj instanceof IPoolObject) {
                ((IPoolObject) obj).clear();
            } else if (obj instanceof Collection) {
                ((Collection) obj).clear();
            } else if (obj instanceof Map) {
                ((Map) obj).clear();
            }
            Cache objectCache = getObjectCache(cls, false);
            if (objectCache != null) {
                objectCache.releaseObject(obj);
            }
        }
    }
}
