package org.greenrobot.greendao;

import android.database.Cursor;
import java.util.List;
import org.greenrobot.greendao.internal.TableStatements;

public final class InternalQueryDaoAccess {
    private final AbstractDao dao;

    public InternalQueryDaoAccess(AbstractDao abstractDao) {
        this.dao = abstractDao;
    }

    public static TableStatements getStatements(AbstractDao abstractDao) {
        return abstractDao.getStatements();
    }

    public TableStatements getStatements() {
        return this.dao.getStatements();
    }

    public List loadAllAndCloseCursor(Cursor cursor) {
        return this.dao.loadAllAndCloseCursor(cursor);
    }

    public Object loadCurrent(Cursor cursor, int i, boolean z) {
        return this.dao.loadCurrent(cursor, i, z);
    }

    public Object loadUniqueAndCloseCursor(Cursor cursor) {
        return this.dao.loadUniqueAndCloseCursor(cursor);
    }
}
