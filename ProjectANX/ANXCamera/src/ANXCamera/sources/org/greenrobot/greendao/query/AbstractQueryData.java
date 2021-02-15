package org.greenrobot.greendao.query;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.greenrobot.greendao.AbstractDao;

abstract class AbstractQueryData {
    final AbstractDao dao;
    final String[] initialValues;
    final Map queriesForThreads = new HashMap();
    final String sql;

    AbstractQueryData(AbstractDao abstractDao, String str, String[] strArr) {
        this.dao = abstractDao;
        this.sql = str;
        this.initialValues = strArr;
    }

    public abstract AbstractQuery createQuery();

    /* access modifiers changed from: 0000 */
    public AbstractQuery forCurrentThread() {
        AbstractQuery abstractQuery;
        long id = Thread.currentThread().getId();
        synchronized (this.queriesForThreads) {
            WeakReference weakReference = (WeakReference) this.queriesForThreads.get(Long.valueOf(id));
            abstractQuery = weakReference != null ? (AbstractQuery) weakReference.get() : null;
            if (abstractQuery == null) {
                gc();
                abstractQuery = createQuery();
                this.queriesForThreads.put(Long.valueOf(id), new WeakReference(abstractQuery));
            } else {
                System.arraycopy(this.initialValues, 0, abstractQuery.parameters, 0, this.initialValues.length);
            }
        }
        return abstractQuery;
    }

    /* access modifiers changed from: 0000 */
    public AbstractQuery forCurrentThread(AbstractQuery abstractQuery) {
        if (Thread.currentThread() != abstractQuery.ownerThread) {
            return forCurrentThread();
        }
        String[] strArr = this.initialValues;
        System.arraycopy(strArr, 0, abstractQuery.parameters, 0, strArr.length);
        return abstractQuery;
    }

    /* access modifiers changed from: 0000 */
    public void gc() {
        synchronized (this.queriesForThreads) {
            Iterator it = this.queriesForThreads.entrySet().iterator();
            while (it.hasNext()) {
                if (((WeakReference) ((Entry) it.next()).getValue()).get() == null) {
                    it.remove();
                }
            }
        }
    }
}
