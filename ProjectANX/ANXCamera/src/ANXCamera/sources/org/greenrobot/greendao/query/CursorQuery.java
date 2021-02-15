package org.greenrobot.greendao.query;

import android.database.Cursor;
import java.util.Date;
import org.greenrobot.greendao.AbstractDao;

public class CursorQuery extends AbstractQueryWithLimit {
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
        public CursorQuery createQuery() {
            CursorQuery cursorQuery = new CursorQuery(this, this.dao, this.sql, (String[]) this.initialValues.clone(), this.limitPosition, this.offsetPosition);
            return cursorQuery;
        }
    }

    private CursorQuery(QueryData queryData2, AbstractDao abstractDao, String str, String[] strArr, int i, int i2) {
        super(abstractDao, str, strArr, i, i2);
        this.queryData = queryData2;
    }

    static CursorQuery create(AbstractDao abstractDao, String str, Object[] objArr, int i, int i2) {
        QueryData queryData2 = new QueryData(abstractDao, str, AbstractQuery.toStringArray(objArr), i, i2);
        return (CursorQuery) queryData2.forCurrentThread();
    }

    public static CursorQuery internalCreate(AbstractDao abstractDao, String str, Object[] objArr) {
        return create(abstractDao, str, objArr, -1, -1);
    }

    public CursorQuery forCurrentThread() {
        return (CursorQuery) this.queryData.forCurrentThread(this);
    }

    public Cursor query() {
        checkThread();
        return this.dao.getDatabase().rawQuery(this.sql, this.parameters);
    }

    public /* bridge */ /* synthetic */ void setLimit(int i) {
        super.setLimit(i);
    }

    public /* bridge */ /* synthetic */ void setOffset(int i) {
        super.setOffset(i);
    }

    public CursorQuery setParameter(int i, Boolean bool) {
        return (CursorQuery) super.setParameter(i, bool);
    }

    public CursorQuery setParameter(int i, Object obj) {
        return (CursorQuery) super.setParameter(i, obj);
    }

    public CursorQuery setParameter(int i, Date date) {
        return (CursorQuery) super.setParameter(i, date);
    }
}
