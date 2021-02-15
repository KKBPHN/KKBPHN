package com.miui.internal.analytics;

import android.content.Context;
import android.database.Cursor;

interface Storable {
    void close();

    void create(Context context);

    Cursor readDataset(String str);

    void writeData(String str, Integer num, String str2, String str3, String str4, String str5);
}
