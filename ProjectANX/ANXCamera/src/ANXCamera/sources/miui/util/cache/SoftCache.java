package miui.util.cache;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class SoftCache implements Cache {
    private ConcurrentHashMap mCaches = new ConcurrentHashMap();

    private void trim() {
        Iterator it = this.mCaches.entrySet().iterator();
        while (it.hasNext()) {
            if (((SoftReference) ((Entry) it.next()).getValue()).get() == null) {
                it.remove();
            }
        }
    }

    public void clear() {
        for (Entry value : this.mCaches.entrySet()) {
            ((SoftReference) value.getValue()).clear();
        }
        this.mCaches.clear();
    }

    public Object get(Object obj) {
        trim();
        SoftReference softReference = (SoftReference) this.mCaches.get(obj);
        if (softReference == null) {
            return null;
        }
        return softReference.get();
    }

    public int getCount() {
        trim();
        return this.mCaches.size();
    }

    public void put(Object obj, Object obj2) {
        put(obj, obj2, 0);
    }

    public void put(Object obj, Object obj2, int i) {
        trim();
        this.mCaches.put(obj, new SoftReference(obj2));
    }

    public void setMaxSize(int i) {
    }
}
