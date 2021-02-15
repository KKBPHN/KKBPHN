package org.greenrobot.greendao.query;

import java.util.Date;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;

public class DeleteQuery extends AbstractQuery {
    private final QueryData queryData;

    final class QueryData extends AbstractQueryData {
        private QueryData(AbstractDao abstractDao, String str, String[] strArr) {
            super(abstractDao, str, strArr);
        }

        /* access modifiers changed from: protected */
        public DeleteQuery createQuery() {
            DeleteQuery deleteQuery = new DeleteQuery(this, this.dao, this.sql, (String[]) this.initialValues.clone());
            return deleteQuery;
        }
    }

    private DeleteQuery(QueryData queryData2, AbstractDao abstractDao, String str, String[] strArr) {
        super(abstractDao, str, strArr);
        this.queryData = queryData2;
    }

    static DeleteQuery create(AbstractDao abstractDao, String str, Object[] objArr) {
        return (DeleteQuery) new QueryData(abstractDao, str, AbstractQuery.toStringArray(objArr)).forCurrentThread();
    }

    public void executeDeleteWithoutDetachingEntities() {
        checkThread();
        Database database = this.dao.getDatabase();
        if (database.isDbLockedByCurrentThread()) {
            this.dao.getDatabase().execSQL(this.sql, this.parameters);
            return;
        }
        database.beginTransaction();
        try {
            this.dao.getDatabase().execSQL(this.sql, this.parameters);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public DeleteQuery forCurrentThread() {
        return (DeleteQuery) this.queryData.forCurrentThread(this);
    }

    public DeleteQuery setParameter(int i, Boolean bool) {
        return (DeleteQuery) super.setParameter(i, bool);
    }

    public DeleteQuery setParameter(int i, Object obj) {
        super.setParameter(i, obj);
        return this;
    }

    public DeleteQuery setParameter(int i, Date date) {
        return (DeleteQuery) super.setParameter(i, date);
    }
}
