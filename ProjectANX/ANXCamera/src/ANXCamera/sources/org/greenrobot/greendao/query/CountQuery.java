package org.greenrobot.greendao.query;

import android.database.Cursor;
import java.util.Date;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;

public class CountQuery extends AbstractQuery {
    private final QueryData queryData;

    final class QueryData extends AbstractQueryData {
        private QueryData(AbstractDao abstractDao, String str, String[] strArr) {
            super(abstractDao, str, strArr);
        }

        /* access modifiers changed from: protected */
        public CountQuery createQuery() {
            CountQuery countQuery = new CountQuery(this, this.dao, this.sql, (String[]) this.initialValues.clone());
            return countQuery;
        }
    }

    private CountQuery(QueryData queryData2, AbstractDao abstractDao, String str, String[] strArr) {
        super(abstractDao, str, strArr);
        this.queryData = queryData2;
    }

    static CountQuery create(AbstractDao abstractDao, String str, Object[] objArr) {
        return (CountQuery) new QueryData(abstractDao, str, AbstractQuery.toStringArray(objArr)).forCurrentThread();
    }

    public long count() {
        checkThread();
        Cursor rawQuery = this.dao.getDatabase().rawQuery(this.sql, this.parameters);
        try {
            if (!rawQuery.moveToNext()) {
                throw new DaoException("No result for count");
            } else if (!rawQuery.isLast()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected row count: ");
                sb.append(rawQuery.getCount());
                throw new DaoException(sb.toString());
            } else if (rawQuery.getColumnCount() == 1) {
                return rawQuery.getLong(0);
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Unexpected column count: ");
                sb2.append(rawQuery.getColumnCount());
                throw new DaoException(sb2.toString());
            }
        } finally {
            rawQuery.close();
        }
    }

    public CountQuery forCurrentThread() {
        return (CountQuery) this.queryData.forCurrentThread(this);
    }

    public CountQuery setParameter(int i, Boolean bool) {
        return (CountQuery) super.setParameter(i, bool);
    }

    public CountQuery setParameter(int i, Object obj) {
        super.setParameter(i, obj);
        return this;
    }

    public CountQuery setParameter(int i, Date date) {
        return (CountQuery) super.setParameter(i, date);
    }
}
