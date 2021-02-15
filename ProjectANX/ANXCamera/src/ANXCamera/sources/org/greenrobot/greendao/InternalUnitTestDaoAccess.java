package org.greenrobot.greendao;

import android.database.Cursor;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScope;
import org.greenrobot.greendao.internal.DaoConfig;

public class InternalUnitTestDaoAccess {
    private final AbstractDao dao;

    public InternalUnitTestDaoAccess(Database database, Class cls, IdentityScope identityScope) {
        DaoConfig daoConfig = new DaoConfig(database, cls);
        daoConfig.setIdentityScope(identityScope);
        this.dao = (AbstractDao) cls.getConstructor(new Class[]{DaoConfig.class}).newInstance(new Object[]{daoConfig});
    }

    public AbstractDao getDao() {
        return this.dao;
    }

    public Object getKey(Object obj) {
        return this.dao.getKey(obj);
    }

    public Property[] getProperties() {
        return this.dao.getProperties();
    }

    public boolean isEntityUpdateable() {
        return this.dao.isEntityUpdateable();
    }

    public Object readEntity(Cursor cursor, int i) {
        return this.dao.readEntity(cursor, i);
    }

    public Object readKey(Cursor cursor, int i) {
        return this.dao.readKey(cursor, i);
    }
}
