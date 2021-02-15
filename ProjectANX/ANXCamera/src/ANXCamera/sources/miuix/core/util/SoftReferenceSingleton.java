package miuix.core.util;

import java.lang.ref.SoftReference;

public abstract class SoftReferenceSingleton {
    private SoftReference mInstance = null;

    /* access modifiers changed from: protected */
    public Object createInstance() {
        return null;
    }

    /* access modifiers changed from: protected */
    public Object createInstance(Object obj) {
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000b, code lost:
        if (r0 == null) goto L_0x000d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final Object get() {
        Object obj;
        synchronized (this) {
            if (this.mInstance != null) {
                obj = this.mInstance.get();
            }
            obj = createInstance();
            this.mInstance = new SoftReference(obj);
        }
        return obj;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000b, code lost:
        if (r0 == null) goto L_0x000d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final Object get(Object obj) {
        Object obj2;
        synchronized (this) {
            if (this.mInstance != null) {
                obj2 = this.mInstance.get();
            }
            obj2 = createInstance(obj);
            this.mInstance = new SoftReference(obj2);
        }
        return obj2;
    }
}
