package com.android.camera.external.mivi;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MiviInfoContentProvider extends ContentProvider {
    public static final String KEY_MIVI_INFO = "miviInfo";
    public static final String METHOD_GET_MIVI_INFO = "getMiviInfo";

    @Nullable
    public Bundle call(@NonNull String str, @Nullable String str2, @Nullable Bundle bundle) {
        Bundle bundle2 = new Bundle();
        char c = (str.hashCode() == 946797587 && str.equals(METHOD_GET_MIVI_INFO)) ? (char) 0 : 65535;
        if (c == 0) {
            bundle2.putString("miviInfo", MIVIHelper.getCloudDataFilterByPackageName(getCallingPackage()));
        }
        return bundle2;
    }

    public int delete(@NonNull Uri uri, @Nullable String str, @Nullable String[] strArr) {
        return 0;
    }

    @Nullable
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    public boolean onCreate() {
        return false;
    }

    @Nullable
    public Cursor query(@NonNull Uri uri, @Nullable String[] strArr, @Nullable String str, @Nullable String[] strArr2, @Nullable String str2) {
        return null;
    }

    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String str, @Nullable String[] strArr) {
        return 0;
    }
}
