package com.android.camera.db.item;

import android.content.Context;
import com.android.camera.CameraAppImpl;
import com.android.camera.db.element.SaveTask;
import com.android.camera.db.greendao.SaveTaskDao;
import com.android.camera.db.greendao.SaveTaskDao.Properties;
import com.android.camera.log.Log;
import com.xiaomi.camera.parallelservice.util.ParallelUtil;
import java.util.List;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.WhereCondition;

public class DbItemSaveTask extends DbItemBase {
    private static final int PROCESS_MAX_TIME_OUT = 10000;
    private static final String TAG = "DbItemSaveTask";

    /* access modifiers changed from: protected */
    public SaveTask createItem(long j) {
        SaveTask saveTask;
        synchronized (this.mLock) {
            saveTask = new SaveTask();
            saveTask.setStartTime(Long.valueOf(j));
        }
        return saveTask;
    }

    /* access modifiers changed from: protected */
    public SaveTask endItem(SaveTask saveTask, long j) {
        return saveTask;
    }

    /* access modifiers changed from: protected */
    public AbstractDao getDao() {
        return getDaoSession().getSaveTaskDao();
    }

    public SaveTask getItemByMediaId(Long l) {
        SaveTask saveTask;
        synchronized (this.mLock) {
            saveTask = (SaveTask) getDao().queryBuilder().where(Properties.MediaStoreId.eq(l), new WhereCondition[0]).build().unique();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("getItemByMediaId: ");
            sb.append(saveTask);
            Log.d(str, sb.toString());
        }
        return saveTask;
    }

    public SaveTask getItemByPath(String str) {
        SaveTask saveTask;
        synchronized (this.mLock) {
            saveTask = (SaveTask) getDao().queryBuilder().where(Properties.Path.eq(str), new WhereCondition[0]).limit(1).build().unique();
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("getItemByPath: ");
            sb.append(saveTask);
            Log.d(str2, sb.toString());
        }
        return saveTask;
    }

    public SaveTask getItemWithExistedQuery(Query query, String str) {
        SaveTask saveTask;
        synchronized (this.mLock) {
            query.setParameter(0, (Object) str);
            saveTask = (SaveTask) getDao().queryBuilder().where(Properties.Path.eq(str), new WhereCondition[0]).limit(1).build().unique();
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("getItemWithExistedQuery: ");
            sb.append(saveTask);
            Log.d(str2, sb.toString());
        }
        return saveTask;
    }

    /* access modifiers changed from: protected */
    public Property getOrderProperty() {
        return Properties.Id;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0074, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void markAllDepartedTask() {
        synchronized (this.mLock) {
            List<SaveTask> allItems = getAllItems();
            if (allItems != null) {
                if (allItems.size() != 0) {
                    Context androidContext = CameraAppImpl.getAndroidContext();
                    long currentTimeMillis = System.currentTimeMillis();
                    for (SaveTask saveTask : allItems) {
                        if (saveTask.isDeparted(currentTimeMillis)) {
                            if (!saveTask.isValid()) {
                                String str = TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("not valid, remove:");
                                sb.append(saveTask.getPath());
                                Log.e(str, sb.toString());
                                removeItem(saveTask);
                            } else {
                                String str2 = TAG;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("mark departed:");
                                sb2.append(saveTask.getPath());
                                Log.e(str2, sb2.toString());
                                ParallelUtil.markTaskFinish(androidContext, saveTask, false);
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public String provideTableName() {
        return SaveTaskDao.TABLENAME;
    }
}
