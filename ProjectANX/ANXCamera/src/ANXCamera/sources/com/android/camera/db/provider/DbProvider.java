package com.android.camera.db.provider;

import java.util.List;
import org.greenrobot.greendao.query.Query;

public interface DbProvider {

    public interface providerDb {
        long endItemAndInsert(Object obj, long j);

        Object generateItem(long j);

        List getAllItems();

        long getCount();

        Object getItemByMediaId(Long l);

        Object getItemByPath(String str);

        Object getItemWithExistedQuery(Query query, String str);

        Object getLastItem();

        void removeItem(Object obj);

        void updateItem(Object obj);
    }

    providerDb dbItemSaveTask();
}
