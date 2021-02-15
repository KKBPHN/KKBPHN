package org.greenrobot.greendao;

import android.database.CrossProcessCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.identityscope.IdentityScope;
import org.greenrobot.greendao.identityscope.IdentityScopeLong;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.internal.FastCursor;
import org.greenrobot.greendao.internal.TableStatements;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

public abstract class AbstractDao {
    protected final DaoConfig config;
    protected final Database db;
    protected final IdentityScope identityScope;
    protected final IdentityScopeLong identityScopeLong;
    protected final boolean isStandardSQLite;
    protected final int pkOrdinal;
    protected final AbstractDaoSession session;
    protected final TableStatements statements;

    public AbstractDao(DaoConfig daoConfig) {
        this(daoConfig, null);
    }

    public AbstractDao(DaoConfig daoConfig, AbstractDaoSession abstractDaoSession) {
        this.config = daoConfig;
        this.session = abstractDaoSession;
        this.db = daoConfig.db;
        this.isStandardSQLite = this.db.getRawDatabase() instanceof SQLiteDatabase;
        this.identityScope = daoConfig.getIdentityScope();
        IdentityScope identityScope2 = this.identityScope;
        this.identityScopeLong = identityScope2 instanceof IdentityScopeLong ? (IdentityScopeLong) identityScope2 : null;
        this.statements = daoConfig.statements;
        Property property = daoConfig.pkProperty;
        this.pkOrdinal = property != null ? property.ordinal : -1;
    }

    private void deleteByKeyInsideSynchronized(Object obj, DatabaseStatement databaseStatement) {
        if (obj instanceof Long) {
            databaseStatement.bindLong(1, ((Long) obj).longValue());
        } else if (obj != null) {
            databaseStatement.bindString(1, obj.toString());
        } else {
            throw new DaoException("Cannot delete entity, key is null");
        }
        databaseStatement.execute();
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.lang.Iterable, code=java.lang.Iterable<java.lang.Object>, for r4v0, types: [java.lang.Iterable<java.lang.Object>, java.lang.Iterable] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void deleteInTxInternal(Iterable<Object> iterable, Iterable iterable2) {
        ArrayList arrayList;
        assertSinglePk();
        DatabaseStatement deleteStatement = this.statements.getDeleteStatement();
        this.db.beginTransaction();
        try {
            synchronized (deleteStatement) {
                if (this.identityScope != null) {
                    this.identityScope.lock();
                    arrayList = new ArrayList();
                } else {
                    arrayList = null;
                }
                if (iterable != null) {
                    try {
                        for (Object keyVerified : iterable) {
                            Object keyVerified2 = getKeyVerified(keyVerified);
                            deleteByKeyInsideSynchronized(keyVerified2, deleteStatement);
                            if (arrayList != null) {
                                arrayList.add(keyVerified2);
                            }
                        }
                    } finally {
                        if (this.identityScope != null) {
                            this.identityScope.unlock();
                        }
                    }
                }
                if (iterable2 != null) {
                    for (Object next : iterable2) {
                        deleteByKeyInsideSynchronized(next, deleteStatement);
                        if (arrayList != null) {
                            arrayList.add(next);
                        }
                    }
                }
                if (this.identityScope != null) {
                    this.identityScope.unlock();
                }
            }
            this.db.setTransactionSuccessful();
            if (!(arrayList == null || this.identityScope == null)) {
                this.identityScope.remove((Iterable) arrayList);
            }
        } finally {
            this.db.endTransaction();
        }
    }

    private long executeInsert(Object obj, DatabaseStatement databaseStatement, boolean z) {
        long j;
        if (this.db.isDbLockedByCurrentThread()) {
            j = insertInsideTx(obj, databaseStatement);
        } else {
            this.db.beginTransaction();
            try {
                j = insertInsideTx(obj, databaseStatement);
                this.db.setTransactionSuccessful();
            } finally {
                this.db.endTransaction();
            }
        }
        if (z) {
            updateKeyAfterInsertAndAttach(obj, j, true);
        }
        return j;
    }

    private void executeInsertInTx(DatabaseStatement databaseStatement, Iterable iterable, boolean z) {
        this.db.beginTransaction();
        try {
            synchronized (databaseStatement) {
                if (this.identityScope != null) {
                    this.identityScope.lock();
                }
                try {
                    if (this.isStandardSQLite) {
                        SQLiteStatement sQLiteStatement = (SQLiteStatement) databaseStatement.getRawStatement();
                        for (Object next : iterable) {
                            bindValues(sQLiteStatement, next);
                            if (z) {
                                updateKeyAfterInsertAndAttach(next, sQLiteStatement.executeInsert(), false);
                            } else {
                                sQLiteStatement.execute();
                            }
                        }
                    } else {
                        for (Object next2 : iterable) {
                            bindValues(databaseStatement, next2);
                            if (z) {
                                updateKeyAfterInsertAndAttach(next2, databaseStatement.executeInsert(), false);
                            } else {
                                databaseStatement.execute();
                            }
                        }
                    }
                } finally {
                    if (this.identityScope != null) {
                        this.identityScope.unlock();
                    }
                }
            }
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    private long insertInsideTx(Object obj, DatabaseStatement databaseStatement) {
        synchronized (databaseStatement) {
            if (this.isStandardSQLite) {
                SQLiteStatement sQLiteStatement = (SQLiteStatement) databaseStatement.getRawStatement();
                bindValues(sQLiteStatement, obj);
                long executeInsert = sQLiteStatement.executeInsert();
                return executeInsert;
            }
            bindValues(databaseStatement, obj);
            long executeInsert2 = databaseStatement.executeInsert();
            return executeInsert2;
        }
    }

    private void loadAllUnlockOnWindowBounds(Cursor cursor, CursorWindow cursorWindow, List list) {
        int startPosition = cursorWindow.getStartPosition() + cursorWindow.getNumRows();
        int i = 0;
        while (true) {
            list.add(loadCurrent(cursor, 0, false));
            int i2 = i + 1;
            if (i2 >= startPosition) {
                CursorWindow moveToNextUnlocked = moveToNextUnlocked(cursor);
                if (moveToNextUnlocked != null) {
                    startPosition = moveToNextUnlocked.getStartPosition() + moveToNextUnlocked.getNumRows();
                } else {
                    return;
                }
            } else if (!cursor.moveToNext()) {
                return;
            }
            i = i2 + 1;
        }
    }

    private CursorWindow moveToNextUnlocked(Cursor cursor) {
        this.identityScope.unlock();
        try {
            return cursor.moveToNext() ? ((CrossProcessCursor) cursor).getWindow() : null;
        } finally {
            this.identityScope.lock();
        }
    }

    /* access modifiers changed from: protected */
    public void assertSinglePk() {
        if (this.config.pkColumns.length != 1) {
            StringBuilder sb = new StringBuilder();
            sb.append(this);
            sb.append(" (");
            sb.append(this.config.tablename);
            sb.append(") does not have a single-column primary key");
            throw new DaoException(sb.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void attachEntity(Object obj) {
    }

    /* access modifiers changed from: protected */
    public final void attachEntity(Object obj, Object obj2, boolean z) {
        attachEntity(obj2);
        IdentityScope identityScope2 = this.identityScope;
        if (identityScope2 != null && obj != null) {
            if (z) {
                identityScope2.put(obj, obj2);
            } else {
                identityScope2.putNoLock(obj, obj2);
            }
        }
    }

    public abstract void bindValues(SQLiteStatement sQLiteStatement, Object obj);

    public abstract void bindValues(DatabaseStatement databaseStatement, Object obj);

    public long count() {
        return this.statements.getCountStatement().simpleQueryForLong();
    }

    public void delete(Object obj) {
        assertSinglePk();
        deleteByKey(getKeyVerified(obj));
    }

    public void deleteAll() {
        Database database = this.db;
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM '");
        sb.append(this.config.tablename);
        sb.append("'");
        database.execSQL(sb.toString());
        IdentityScope identityScope2 = this.identityScope;
        if (identityScope2 != null) {
            identityScope2.clear();
        }
    }

    public void deleteByKey(Object obj) {
        assertSinglePk();
        DatabaseStatement deleteStatement = this.statements.getDeleteStatement();
        if (this.db.isDbLockedByCurrentThread()) {
            synchronized (deleteStatement) {
                deleteByKeyInsideSynchronized(obj, deleteStatement);
            }
        } else {
            this.db.beginTransaction();
            try {
                synchronized (deleteStatement) {
                    deleteByKeyInsideSynchronized(obj, deleteStatement);
                }
                this.db.setTransactionSuccessful();
                this.db.endTransaction();
            } catch (Throwable th) {
                this.db.endTransaction();
                throw th;
            }
        }
        IdentityScope identityScope2 = this.identityScope;
        if (identityScope2 != null) {
            identityScope2.remove(obj);
        }
    }

    public void deleteByKeyInTx(Iterable iterable) {
        deleteInTxInternal(null, iterable);
    }

    public void deleteByKeyInTx(Object... objArr) {
        deleteInTxInternal(null, Arrays.asList(objArr));
    }

    public void deleteInTx(Iterable iterable) {
        deleteInTxInternal(iterable, null);
    }

    public void deleteInTx(Object... objArr) {
        deleteInTxInternal(Arrays.asList(objArr), null);
    }

    public boolean detach(Object obj) {
        if (this.identityScope == null) {
            return false;
        }
        return this.identityScope.detach(getKeyVerified(obj), obj);
    }

    public void detachAll() {
        IdentityScope identityScope2 = this.identityScope;
        if (identityScope2 != null) {
            identityScope2.clear();
        }
    }

    public String[] getAllColumns() {
        return this.config.allColumns;
    }

    public Database getDatabase() {
        return this.db;
    }

    public abstract Object getKey(Object obj);

    /* access modifiers changed from: protected */
    public Object getKeyVerified(Object obj) {
        Object key = getKey(obj);
        if (key != null) {
            return key;
        }
        if (obj == null) {
            throw new NullPointerException("Entity may not be null");
        }
        throw new DaoException("Entity has no key");
    }

    public String[] getNonPkColumns() {
        return this.config.nonPkColumns;
    }

    public String[] getPkColumns() {
        return this.config.pkColumns;
    }

    public Property getPkProperty() {
        return this.config.pkProperty;
    }

    public Property[] getProperties() {
        return this.config.properties;
    }

    public AbstractDaoSession getSession() {
        return this.session;
    }

    /* access modifiers changed from: 0000 */
    public TableStatements getStatements() {
        return this.config.statements;
    }

    public String getTablename() {
        return this.config.tablename;
    }

    public abstract boolean hasKey(Object obj);

    public long insert(Object obj) {
        return executeInsert(obj, this.statements.getInsertStatement(), true);
    }

    public void insertInTx(Iterable iterable) {
        insertInTx(iterable, isEntityUpdateable());
    }

    public void insertInTx(Iterable iterable, boolean z) {
        executeInsertInTx(this.statements.getInsertStatement(), iterable, z);
    }

    public void insertInTx(Object... objArr) {
        insertInTx(Arrays.asList(objArr), isEntityUpdateable());
    }

    public long insertOrReplace(Object obj) {
        return executeInsert(obj, this.statements.getInsertOrReplaceStatement(), true);
    }

    public void insertOrReplaceInTx(Iterable iterable) {
        insertOrReplaceInTx(iterable, isEntityUpdateable());
    }

    public void insertOrReplaceInTx(Iterable iterable, boolean z) {
        executeInsertInTx(this.statements.getInsertOrReplaceStatement(), iterable, z);
    }

    public void insertOrReplaceInTx(Object... objArr) {
        insertOrReplaceInTx(Arrays.asList(objArr), isEntityUpdateable());
    }

    public long insertWithoutSettingPk(Object obj) {
        return executeInsert(obj, this.statements.getInsertOrReplaceStatement(), false);
    }

    public abstract boolean isEntityUpdateable();

    public Object load(Object obj) {
        assertSinglePk();
        if (obj == null) {
            return null;
        }
        IdentityScope identityScope2 = this.identityScope;
        if (identityScope2 != null) {
            Object obj2 = identityScope2.get(obj);
            if (obj2 != null) {
                return obj2;
            }
        }
        return loadUniqueAndCloseCursor(this.db.rawQuery(this.statements.getSelectByKey(), new String[]{obj.toString()}));
    }

    public List loadAll() {
        return loadAllAndCloseCursor(this.db.rawQuery(this.statements.getSelectAll(), null));
    }

    /* access modifiers changed from: protected */
    public List loadAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0054  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x007d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public List loadAllFromCursor(Cursor cursor) {
        boolean z;
        IdentityScope identityScope2;
        int count = cursor.getCount();
        if (count == 0) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList(count);
        CursorWindow cursorWindow = null;
        if (cursor instanceof CrossProcessCursor) {
            cursorWindow = ((CrossProcessCursor) cursor).getWindow();
            if (cursorWindow != null) {
                if (cursorWindow.getNumRows() == count) {
                    cursor = new FastCursor(cursorWindow);
                    z = true;
                    if (cursor.moveToFirst()) {
                        IdentityScope identityScope3 = this.identityScope;
                        if (identityScope3 != null) {
                            identityScope3.lock();
                            this.identityScope.reserveRoom(count);
                        }
                        if (!z && cursorWindow != null) {
                            try {
                                if (this.identityScope != null) {
                                    loadAllUnlockOnWindowBounds(cursor, cursorWindow, arrayList);
                                    identityScope2 = this.identityScope;
                                    if (identityScope2 != null) {
                                        identityScope2.unlock();
                                    }
                                }
                            } catch (Throwable th) {
                                IdentityScope identityScope4 = this.identityScope;
                                if (identityScope4 != null) {
                                    identityScope4.unlock();
                                }
                                throw th;
                            }
                        }
                        do {
                            arrayList.add(loadCurrent(cursor, 0, false));
                        } while (cursor.moveToNext());
                        identityScope2 = this.identityScope;
                        if (identityScope2 != null) {
                        }
                    }
                    return arrayList;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Window vs. result size: ");
                sb.append(cursorWindow.getNumRows());
                sb.append("/");
                sb.append(count);
                DaoLog.d(sb.toString());
            }
        }
        z = false;
        if (cursor.moveToFirst()) {
        }
        return arrayList;
    }

    public Object loadByRowId(long j) {
        return loadUniqueAndCloseCursor(this.db.rawQuery(this.statements.getSelectByRowId(), new String[]{Long.toString(j)}));
    }

    /* access modifiers changed from: protected */
    public final Object loadCurrent(Cursor cursor, int i, boolean z) {
        if (this.identityScopeLong != null) {
            if (i != 0 && cursor.isNull(this.pkOrdinal + i)) {
                return null;
            }
            long j = cursor.getLong(this.pkOrdinal + i);
            IdentityScopeLong identityScopeLong2 = this.identityScopeLong;
            Object r2 = z ? identityScopeLong2.get2(j) : identityScopeLong2.get2NoLock(j);
            if (r2 != null) {
                return r2;
            }
            Object readEntity = readEntity(cursor, i);
            attachEntity(readEntity);
            IdentityScopeLong identityScopeLong3 = this.identityScopeLong;
            if (z) {
                identityScopeLong3.put2(j, readEntity);
            } else {
                identityScopeLong3.put2NoLock(j, readEntity);
            }
            return readEntity;
        } else if (this.identityScope != null) {
            Object readKey = readKey(cursor, i);
            if (i != 0 && readKey == null) {
                return null;
            }
            IdentityScope identityScope2 = this.identityScope;
            Object noLock = z ? identityScope2.get(readKey) : identityScope2.getNoLock(readKey);
            if (noLock != null) {
                return noLock;
            }
            Object readEntity2 = readEntity(cursor, i);
            attachEntity(readKey, readEntity2, z);
            return readEntity2;
        } else if (i != 0 && readKey(cursor, i) == null) {
            return null;
        } else {
            Object readEntity3 = readEntity(cursor, i);
            attachEntity(readEntity3);
            return readEntity3;
        }
    }

    /* access modifiers changed from: protected */
    public final Object loadCurrentOther(AbstractDao abstractDao, Cursor cursor, int i) {
        return abstractDao.loadCurrent(cursor, i, true);
    }

    /* access modifiers changed from: protected */
    public Object loadUnique(Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return null;
        }
        if (cursor.isLast()) {
            return loadCurrent(cursor, 0, true);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Expected unique result, but count was ");
        sb.append(cursor.getCount());
        throw new DaoException(sb.toString());
    }

    /* access modifiers changed from: protected */
    public Object loadUniqueAndCloseCursor(Cursor cursor) {
        try {
            return loadUnique(cursor);
        } finally {
            cursor.close();
        }
    }

    public QueryBuilder queryBuilder() {
        return QueryBuilder.internalCreate(this);
    }

    public List queryRaw(String str, String... strArr) {
        Database database = this.db;
        StringBuilder sb = new StringBuilder();
        sb.append(this.statements.getSelectAll());
        sb.append(str);
        return loadAllAndCloseCursor(database.rawQuery(sb.toString(), strArr));
    }

    public Query queryRawCreate(String str, Object... objArr) {
        return queryRawCreateListArgs(str, Arrays.asList(objArr));
    }

    public Query queryRawCreateListArgs(String str, Collection collection) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.statements.getSelectAll());
        sb.append(str);
        return Query.internalCreate(this, sb.toString(), collection.toArray());
    }

    public abstract Object readEntity(Cursor cursor, int i);

    public abstract void readEntity(Cursor cursor, Object obj, int i);

    public abstract Object readKey(Cursor cursor, int i);

    public void refresh(Object obj) {
        assertSinglePk();
        Object keyVerified = getKeyVerified(obj);
        Cursor rawQuery = this.db.rawQuery(this.statements.getSelectByKey(), new String[]{keyVerified.toString()});
        try {
            if (!rawQuery.moveToFirst()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Entity does not exist in the database anymore: ");
                sb.append(obj.getClass());
                sb.append(" with key ");
                sb.append(keyVerified);
                throw new DaoException(sb.toString());
            } else if (rawQuery.isLast()) {
                readEntity(rawQuery, obj, 0);
                attachEntity(keyVerified, obj, true);
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Expected unique result, but count was ");
                sb2.append(rawQuery.getCount());
                throw new DaoException(sb2.toString());
            }
        } finally {
            rawQuery.close();
        }
    }

    public void save(Object obj) {
        if (hasKey(obj)) {
            update(obj);
        } else {
            insert(obj);
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.lang.Iterable, code=java.lang.Iterable<java.lang.Object>, for r5v0, types: [java.lang.Iterable<java.lang.Object>, java.lang.Iterable] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void saveInTx(Iterable<Object> iterable) {
        int i = 0;
        int i2 = 0;
        for (Object hasKey : iterable) {
            if (hasKey(hasKey)) {
                i++;
            } else {
                i2++;
            }
        }
        if (i > 0 && i2 > 0) {
            ArrayList arrayList = new ArrayList(i);
            ArrayList arrayList2 = new ArrayList(i2);
            for (Object next : iterable) {
                if (hasKey(next)) {
                    arrayList.add(next);
                } else {
                    arrayList2.add(next);
                }
            }
            this.db.beginTransaction();
            try {
                updateInTx((Iterable) arrayList);
                insertInTx((Iterable) arrayList2);
                this.db.setTransactionSuccessful();
            } finally {
                this.db.endTransaction();
            }
        } else if (i2 > 0) {
            insertInTx((Iterable) iterable);
        } else if (i > 0) {
            updateInTx((Iterable) iterable);
        }
    }

    public void saveInTx(Object... objArr) {
        saveInTx((Iterable) Arrays.asList(objArr));
    }

    public void update(Object obj) {
        assertSinglePk();
        DatabaseStatement updateStatement = this.statements.getUpdateStatement();
        if (this.db.isDbLockedByCurrentThread()) {
            synchronized (updateStatement) {
                if (this.isStandardSQLite) {
                    updateInsideSynchronized(obj, (SQLiteStatement) updateStatement.getRawStatement(), true);
                } else {
                    updateInsideSynchronized(obj, updateStatement, true);
                }
            }
            return;
        }
        this.db.beginTransaction();
        try {
            synchronized (updateStatement) {
                updateInsideSynchronized(obj, updateStatement, true);
            }
            this.db.setTransactionSuccessful();
            this.db.endTransaction();
        } catch (Throwable th) {
            this.db.endTransaction();
            throw th;
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.lang.Iterable, code=java.lang.Iterable<java.lang.Object>, for r5v0, types: [java.lang.Iterable<java.lang.Object>, java.lang.Iterable] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateInTx(Iterable<Object> iterable) {
        Throwable th;
        DatabaseStatement updateStatement = this.statements.getUpdateStatement();
        this.db.beginTransaction();
        try {
            synchronized (updateStatement) {
                if (this.identityScope != null) {
                    this.identityScope.lock();
                }
                try {
                    if (this.isStandardSQLite) {
                        SQLiteStatement sQLiteStatement = (SQLiteStatement) updateStatement.getRawStatement();
                        for (Object updateInsideSynchronized : iterable) {
                            updateInsideSynchronized(updateInsideSynchronized, sQLiteStatement, false);
                        }
                    } else {
                        for (Object updateInsideSynchronized2 : iterable) {
                            updateInsideSynchronized(updateInsideSynchronized2, updateStatement, false);
                        }
                    }
                } finally {
                    if (this.identityScope != null) {
                        this.identityScope.unlock();
                    }
                }
            }
            this.db.setTransactionSuccessful();
            try {
                th = null;
                if (th != null) {
                    throw th;
                }
            } catch (RuntimeException e) {
                throw e;
            }
        } catch (RuntimeException e2) {
            try {
                th = e2;
            } catch (RuntimeException e3) {
                DaoLog.w("Could not end transaction (rethrowing initial exception)", e3);
                throw e2;
            }
        } finally {
            try {
                this.db.endTransaction();
            } catch (RuntimeException e4) {
                throw e4;
            }
        }
    }

    public void updateInTx(Object... objArr) {
        updateInTx((Iterable) Arrays.asList(objArr));
    }

    /* access modifiers changed from: protected */
    public void updateInsideSynchronized(Object obj, SQLiteStatement sQLiteStatement, boolean z) {
        bindValues(sQLiteStatement, obj);
        int length = this.config.allColumns.length + 1;
        Object key = getKey(obj);
        if (key instanceof Long) {
            sQLiteStatement.bindLong(length, ((Long) key).longValue());
        } else if (key != null) {
            sQLiteStatement.bindString(length, key.toString());
        } else {
            throw new DaoException("Cannot update entity without key - was it inserted before?");
        }
        sQLiteStatement.execute();
        attachEntity(key, obj, z);
    }

    /* access modifiers changed from: protected */
    public void updateInsideSynchronized(Object obj, DatabaseStatement databaseStatement, boolean z) {
        bindValues(databaseStatement, obj);
        int length = this.config.allColumns.length + 1;
        Object key = getKey(obj);
        if (key instanceof Long) {
            databaseStatement.bindLong(length, ((Long) key).longValue());
        } else if (key != null) {
            databaseStatement.bindString(length, key.toString());
        } else {
            throw new DaoException("Cannot update entity without key - was it inserted before?");
        }
        databaseStatement.execute();
        attachEntity(key, obj, z);
    }

    public abstract Object updateKeyAfterInsert(Object obj, long j);

    /* access modifiers changed from: protected */
    public void updateKeyAfterInsertAndAttach(Object obj, long j, boolean z) {
        if (j != -1) {
            attachEntity(updateKeyAfterInsert(obj, j), obj, z);
        } else {
            DaoLog.w("Could not insert row (executeInsert returned -1)");
        }
    }
}
