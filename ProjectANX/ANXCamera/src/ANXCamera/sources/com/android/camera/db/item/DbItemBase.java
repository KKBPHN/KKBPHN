package com.android.camera.db.item;

import com.android.camera.db.DbContainer;
import com.android.camera.db.greendao.DaoMaster;
import com.android.camera.db.greendao.DaoSession;
import com.android.camera.db.provider.DbProvider.providerDb;
import java.util.List;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.Query;

public abstract class DbItemBase implements providerDb {
    protected final Object mLock = new Object();

    public abstract Object createItem(long j);

    public abstract Object endItem(Object obj, long j);

    public long endItemAndInsert(Object obj, long j) {
        long insertOrReplace;
        synchronized (this.mLock) {
            insertOrReplace = insertOrReplace(endItem(obj, j));
        }
        return insertOrReplace;
    }

    public Object generateItem(long j) {
        Object createItem;
        synchronized (this.mLock) {
            createItem = createItem(j);
        }
        return createItem;
    }

    public List getAllItems() {
        List loadAll;
        synchronized (this.mLock) {
            loadAll = getDao().loadAll();
        }
        return loadAll;
    }

    public long getCount() {
        long count;
        synchronized (this.mLock) {
            count = getDao().queryBuilder().buildCount().count();
        }
        return count;
    }

    public abstract AbstractDao getDao();

    /* access modifiers changed from: protected */
    public final DaoMaster getDaoMaser() {
        return DbContainer.getInstance().getDaoMaster();
    }

    /* access modifiers changed from: protected */
    public final DaoSession getDaoSession() {
        return DbContainer.getInstance().getDaoSession();
    }

    public Object getItemByMediaId(Long l) {
        throw new RuntimeException("todo");
    }

    public Object getItemByPath(String str) {
        throw new RuntimeException("todo");
    }

    public Object getItemWithExistedQuery(Query query, String str) {
        throw new RuntimeException("todo");
    }

    public Object getLastItem() {
        synchronized (this.mLock) {
            List list = getDao().queryBuilder().limit(1).orderDesc(getOrderProperty()).list();
            if (list == null || list.isEmpty()) {
                return null;
            }
            Object obj = list.get(0);
            return obj;
        }
    }

    public abstract Property getOrderProperty();

    /* access modifiers changed from: protected */
    public final long insertOrReplace(Object obj) {
        return getDaoSession().insertOrReplace(obj);
    }

    public abstract String provideTableName();

    public void removeItem(Object obj) {
        synchronized (this.mLock) {
            getDaoSession().delete(obj);
        }
    }

    public void updateItem(Object obj) {
        synchronized (this.mLock) {
            updateItemThroughDb(obj);
        }
    }

    /* access modifiers changed from: protected */
    public final void updateItemThroughDb(Object obj) {
        getDaoSession().update(obj);
    }
}
