package org.greenrobot.greendao;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

public class AbstractDaoSession {
    private final Database db;
    private final Map entityToDao = new HashMap();

    public AbstractDaoSession(Database database) {
        this.db = database;
    }

    public Object callInTx(Callable callable) {
        this.db.beginTransaction();
        try {
            Object call = callable.call();
            this.db.setTransactionSuccessful();
            return call;
        } finally {
            this.db.endTransaction();
        }
    }

    public Object callInTxNoException(Callable callable) {
        this.db.beginTransaction();
        try {
            Object call = callable.call();
            this.db.setTransactionSuccessful();
            this.db.endTransaction();
            return call;
        } catch (Exception e) {
            throw new DaoException("Callable failed", e);
        } catch (Throwable th) {
            this.db.endTransaction();
            throw th;
        }
    }

    public void delete(Object obj) {
        getDao(obj.getClass()).delete(obj);
    }

    public void deleteAll(Class cls) {
        getDao(cls).deleteAll();
    }

    public Collection getAllDaos() {
        return Collections.unmodifiableCollection(this.entityToDao.values());
    }

    public AbstractDao getDao(Class cls) {
        AbstractDao abstractDao = (AbstractDao) this.entityToDao.get(cls);
        if (abstractDao != null) {
            return abstractDao;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("No DAO registered for ");
        sb.append(cls);
        throw new DaoException(sb.toString());
    }

    public Database getDatabase() {
        return this.db;
    }

    public long insert(Object obj) {
        return getDao(obj.getClass()).insert(obj);
    }

    public long insertOrReplace(Object obj) {
        return getDao(obj.getClass()).insertOrReplace(obj);
    }

    public Object load(Class cls, Object obj) {
        return getDao(cls).load(obj);
    }

    public List loadAll(Class cls) {
        return getDao(cls).loadAll();
    }

    public QueryBuilder queryBuilder(Class cls) {
        return getDao(cls).queryBuilder();
    }

    public List queryRaw(Class cls, String str, String... strArr) {
        return getDao(cls).queryRaw(str, strArr);
    }

    public void refresh(Object obj) {
        getDao(obj.getClass()).refresh(obj);
    }

    /* access modifiers changed from: protected */
    public void registerDao(Class cls, AbstractDao abstractDao) {
        this.entityToDao.put(cls, abstractDao);
    }

    public void runInTx(Runnable runnable) {
        this.db.beginTransaction();
        try {
            runnable.run();
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public AsyncSession startAsyncSession() {
        return new AsyncSession(this);
    }

    public void update(Object obj) {
        getDao(obj.getClass()).update(obj);
    }
}
