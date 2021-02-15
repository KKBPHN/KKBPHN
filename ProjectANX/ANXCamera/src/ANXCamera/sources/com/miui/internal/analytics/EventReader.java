package com.miui.internal.analytics;

import android.content.Context;
import java.util.Collections;
import java.util.List;

public class EventReader {
    private PersistenceHelper mPersistenceHelper = null;

    public void close() {
        if (this.mPersistenceHelper != null) {
            this.mPersistenceHelper = null;
        }
    }

    public void open(Context context) {
        this.mPersistenceHelper = PersistenceHelper.getInstance();
    }

    public List readEvents(String str, List list) {
        PersistenceHelper persistenceHelper = this.mPersistenceHelper;
        return persistenceHelper != null ? persistenceHelper.readEvents(str, list) : Collections.emptyList();
    }
}
