package org.greenrobot.greendao.query;

import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;

public class Query extends AbstractQueryWithLimit {
    private final QueryData queryData;

    final class QueryData extends AbstractQueryData {
        private final int limitPosition;
        private final int offsetPosition;

        QueryData(AbstractDao abstractDao, String str, String[] strArr, int i, int i2) {
            super(abstractDao, str, strArr);
            this.limitPosition = i;
            this.offsetPosition = i2;
        }

        /* access modifiers changed from: protected */
        public Query createQuery() {
            Query query = new Query(this, this.dao, this.sql, (String[]) this.initialValues.clone(), this.limitPosition, this.offsetPosition);
            return query;
        }
    }

    private Query(QueryData queryData2, AbstractDao abstractDao, String str, String[] strArr, int i, int i2) {
        super(abstractDao, str, strArr, i, i2);
        this.queryData = queryData2;
    }

    static Query create(AbstractDao abstractDao, String str, Object[] objArr, int i, int i2) {
        QueryData queryData2 = new QueryData(abstractDao, str, AbstractQuery.toStringArray(objArr), i, i2);
        return (Query) queryData2.forCurrentThread();
    }

    public static Query internalCreate(AbstractDao abstractDao, String str, Object[] objArr) {
        return create(abstractDao, str, objArr, -1, -1);
    }

    public Query forCurrentThread() {
        return (Query) this.queryData.forCurrentThread(this);
    }

    public List list() {
        checkThread();
        return this.daoAccess.loadAllAndCloseCursor(this.dao.getDatabase().rawQuery(this.sql, this.parameters));
    }

    public CloseableListIterator listIterator() {
        return listLazyUncached().listIteratorAutoClose();
    }

    public LazyList listLazy() {
        checkThread();
        return new LazyList(this.daoAccess, this.dao.getDatabase().rawQuery(this.sql, this.parameters), true);
    }

    public LazyList listLazyUncached() {
        checkThread();
        return new LazyList(this.daoAccess, this.dao.getDatabase().rawQuery(this.sql, this.parameters), false);
    }

    public /* bridge */ /* synthetic */ void setLimit(int i) {
        super.setLimit(i);
    }

    public /* bridge */ /* synthetic */ void setOffset(int i) {
        super.setOffset(i);
    }

    public Query setParameter(int i, Boolean bool) {
        return (Query) super.setParameter(i, bool);
    }

    public Query setParameter(int i, Object obj) {
        return (Query) super.setParameter(i, obj);
    }

    public Query setParameter(int i, Date date) {
        return (Query) super.setParameter(i, date);
    }

    public Object unique() {
        checkThread();
        return this.daoAccess.loadUniqueAndCloseCursor(this.dao.getDatabase().rawQuery(this.sql, this.parameters));
    }

    public Object uniqueOrThrow() {
        Object unique = unique();
        if (unique != null) {
            return unique;
        }
        throw new DaoException("No entity found for query");
    }
}
