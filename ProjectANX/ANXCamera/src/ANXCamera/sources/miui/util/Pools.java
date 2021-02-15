package miui.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import miui.util.concurrent.ConcurrentRingQueue;

public final class Pools {
    /* access modifiers changed from: private */
    public static final HashMap mInstanceHolderMap = new HashMap();
    /* access modifiers changed from: private */
    public static final HashMap mSoftReferenceInstanceHolderMap = new HashMap();
    private static final Pool mStringBuilderPool = createSoftReferencePool(new Manager() {
        public StringBuilder createInstance() {
            return new StringBuilder();
        }

        public void onRelease(StringBuilder sb) {
            sb.setLength(0);
        }
    }, 4);

    abstract class BasePool implements Pool {
        private final Object mFinalizeGuardian = new Object() {
            /* access modifiers changed from: protected */
            public void finalize() {
                try {
                    BasePool.this.close();
                } finally {
                    super.finalize();
                }
            }
        };
        private IInstanceHolder mInstanceHolder;
        private final Manager mManager;
        private final int mSize;

        public BasePool(Manager manager, int i) {
            if (manager == null || i < 1) {
                this.mSize = this.mFinalizeGuardian.hashCode();
                throw new IllegalArgumentException("manager cannot be null and size cannot less then 1");
            }
            this.mManager = manager;
            this.mSize = i;
            Object createInstance = this.mManager.createInstance();
            if (createInstance != null) {
                this.mInstanceHolder = createInstanceHolder(createInstance.getClass(), i);
                doRelease(createInstance);
                return;
            }
            throw new IllegalStateException("manager create instance cannot return null");
        }

        public Object acquire() {
            return doAcquire();
        }

        public void close() {
            IInstanceHolder iInstanceHolder = this.mInstanceHolder;
            if (iInstanceHolder != null) {
                destroyInstanceHolder(iInstanceHolder, this.mSize);
                this.mInstanceHolder = null;
            }
        }

        public abstract IInstanceHolder createInstanceHolder(Class cls, int i);

        public abstract void destroyInstanceHolder(IInstanceHolder iInstanceHolder, int i);

        /* access modifiers changed from: protected */
        public final Object doAcquire() {
            IInstanceHolder iInstanceHolder = this.mInstanceHolder;
            if (iInstanceHolder != null) {
                Object obj = iInstanceHolder.get();
                if (obj == null) {
                    obj = this.mManager.createInstance();
                    if (obj == null) {
                        throw new IllegalStateException("manager create instance cannot return null");
                    }
                }
                this.mManager.onAcquire(obj);
                return obj;
            }
            throw new IllegalStateException("Cannot acquire object after close()");
        }

        /* access modifiers changed from: protected */
        public final void doRelease(Object obj) {
            if (this.mInstanceHolder == null) {
                throw new IllegalStateException("Cannot release object after close()");
            } else if (obj != null) {
                this.mManager.onRelease(obj);
                if (!this.mInstanceHolder.put(obj)) {
                    this.mManager.onDestroy(obj);
                }
            }
        }

        public int getSize() {
            if (this.mInstanceHolder == null) {
                return 0;
            }
            return this.mSize;
        }

        public void release(Object obj) {
            doRelease(obj);
        }
    }

    interface IInstanceHolder {
        Object get();

        Class getElementClass();

        int getSize();

        boolean put(Object obj);

        void resize(int i);
    }

    class InstanceHolder implements IInstanceHolder {
        private final Class mClazz;
        private final ConcurrentRingQueue mQueue;

        InstanceHolder(Class cls, int i) {
            this.mClazz = cls;
            this.mQueue = new ConcurrentRingQueue(i, false, true);
        }

        public Object get() {
            return this.mQueue.get();
        }

        public Class getElementClass() {
            return this.mClazz;
        }

        public int getSize() {
            return this.mQueue.getCapacity();
        }

        public boolean put(Object obj) {
            return this.mQueue.put(obj);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:20:0x002f, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public synchronized void resize(int i) {
            int capacity = i + this.mQueue.getCapacity();
            if (capacity <= 0) {
                synchronized (Pools.mInstanceHolderMap) {
                    Pools.mInstanceHolderMap.remove(getElementClass());
                }
            } else if (capacity > 0) {
                this.mQueue.increaseCapacity(capacity);
            } else {
                this.mQueue.decreaseCapacity(-capacity);
            }
        }
    }

    public abstract class Manager {
        public abstract Object createInstance();

        public void onAcquire(Object obj) {
        }

        public void onDestroy(Object obj) {
        }

        public void onRelease(Object obj) {
        }
    }

    public interface Pool {
        Object acquire();

        void close();

        int getSize();

        void release(Object obj);
    }

    public class SimplePool extends BasePool {
        SimplePool(Manager manager, int i) {
            super(manager, i);
        }

        public /* bridge */ /* synthetic */ Object acquire() {
            return super.acquire();
        }

        public /* bridge */ /* synthetic */ void close() {
            super.close();
        }

        /* access modifiers changed from: 0000 */
        public final IInstanceHolder createInstanceHolder(Class cls, int i) {
            return Pools.onPoolCreate(cls, i);
        }

        /* access modifiers changed from: 0000 */
        public final void destroyInstanceHolder(IInstanceHolder iInstanceHolder, int i) {
            Pools.onPoolClose((InstanceHolder) iInstanceHolder, i);
        }

        public /* bridge */ /* synthetic */ int getSize() {
            return super.getSize();
        }

        public /* bridge */ /* synthetic */ void release(Object obj) {
            super.release(obj);
        }
    }

    class SoftReferenceInstanceHolder implements IInstanceHolder {
        private final Class mClazz;
        private volatile SoftReference[] mElements;
        private volatile int mIndex = 0;
        private volatile int mSize;

        SoftReferenceInstanceHolder(Class cls, int i) {
            this.mClazz = cls;
            this.mSize = i;
            this.mElements = new SoftReference[i];
        }

        public synchronized Object get() {
            int i = this.mIndex;
            SoftReference[] softReferenceArr = this.mElements;
            while (i != 0) {
                i--;
                if (softReferenceArr[i] != null) {
                    Object obj = softReferenceArr[i].get();
                    softReferenceArr[i] = null;
                    if (obj != null) {
                        this.mIndex = i;
                        return obj;
                    }
                }
            }
            return null;
        }

        public Class getElementClass() {
            return this.mClazz;
        }

        public int getSize() {
            return this.mSize;
        }

        public synchronized boolean put(Object obj) {
            int i = this.mIndex;
            SoftReference[] softReferenceArr = this.mElements;
            if (i >= this.mSize) {
                int i2 = 0;
                while (i2 < i) {
                    if (softReferenceArr[i2] != null) {
                        if (softReferenceArr[i2].get() != null) {
                            i2++;
                        }
                    }
                    softReferenceArr[i2] = new SoftReference(obj);
                    return true;
                }
                return false;
            }
            softReferenceArr[i] = new SoftReference(obj);
            this.mIndex = i + 1;
            return true;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:20:0x002e, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public synchronized void resize(int i) {
            int i2 = i + this.mSize;
            if (i2 <= 0) {
                synchronized (Pools.mSoftReferenceInstanceHolderMap) {
                    Pools.mSoftReferenceInstanceHolderMap.remove(getElementClass());
                }
                return;
            }
            this.mSize = i2;
            SoftReference[] softReferenceArr = this.mElements;
            int i3 = this.mIndex;
            if (i2 > softReferenceArr.length) {
                SoftReference[] softReferenceArr2 = new SoftReference[i2];
                System.arraycopy(softReferenceArr, 0, softReferenceArr2, 0, i3);
                this.mElements = softReferenceArr2;
            }
        }
    }

    public class SoftReferencePool extends BasePool {
        SoftReferencePool(Manager manager, int i) {
            super(manager, i);
        }

        public /* bridge */ /* synthetic */ Object acquire() {
            return super.acquire();
        }

        public /* bridge */ /* synthetic */ void close() {
            super.close();
        }

        /* access modifiers changed from: 0000 */
        public final IInstanceHolder createInstanceHolder(Class cls, int i) {
            return Pools.onSoftReferencePoolCreate(cls, i);
        }

        /* access modifiers changed from: 0000 */
        public final void destroyInstanceHolder(IInstanceHolder iInstanceHolder, int i) {
            Pools.onSoftReferencePoolClose((SoftReferenceInstanceHolder) iInstanceHolder, i);
        }

        public /* bridge */ /* synthetic */ int getSize() {
            return super.getSize();
        }

        public /* bridge */ /* synthetic */ void release(Object obj) {
            super.release(obj);
        }
    }

    public static SimplePool createSimplePool(Manager manager, int i) {
        return new SimplePool(manager, i);
    }

    public static SoftReferencePool createSoftReferencePool(Manager manager, int i) {
        return new SoftReferencePool(manager, i);
    }

    public static Pool getStringBuilderPool() {
        return mStringBuilderPool;
    }

    static void onPoolClose(InstanceHolder instanceHolder, int i) {
        synchronized (mInstanceHolderMap) {
            instanceHolder.resize(-i);
        }
    }

    static InstanceHolder onPoolCreate(Class cls, int i) {
        InstanceHolder instanceHolder;
        synchronized (mInstanceHolderMap) {
            instanceHolder = (InstanceHolder) mInstanceHolderMap.get(cls);
            if (instanceHolder == null) {
                instanceHolder = new InstanceHolder(cls, i);
                mInstanceHolderMap.put(cls, instanceHolder);
            } else {
                instanceHolder.resize(i);
            }
        }
        return instanceHolder;
    }

    static void onSoftReferencePoolClose(SoftReferenceInstanceHolder softReferenceInstanceHolder, int i) {
        synchronized (mSoftReferenceInstanceHolderMap) {
            softReferenceInstanceHolder.resize(-i);
        }
    }

    static SoftReferenceInstanceHolder onSoftReferencePoolCreate(Class cls, int i) {
        SoftReferenceInstanceHolder softReferenceInstanceHolder;
        synchronized (mSoftReferenceInstanceHolderMap) {
            softReferenceInstanceHolder = (SoftReferenceInstanceHolder) mSoftReferenceInstanceHolderMap.get(cls);
            if (softReferenceInstanceHolder == null) {
                softReferenceInstanceHolder = new SoftReferenceInstanceHolder(cls, i);
                mSoftReferenceInstanceHolderMap.put(cls, softReferenceInstanceHolder);
            } else {
                softReferenceInstanceHolder.resize(i);
            }
        }
        return softReferenceInstanceHolder;
    }
}
