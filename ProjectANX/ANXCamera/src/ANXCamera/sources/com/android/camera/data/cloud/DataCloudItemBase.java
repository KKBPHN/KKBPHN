package com.android.camera.data.cloud;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import androidx.collection.SimpleArrayMap;
import com.android.camera.CameraAppImpl;
import com.android.camera.data.cloud.DataCloud.CloudItem;

public abstract class DataCloudItemBase implements CloudItem {
    private Editor mEditor;
    private SharedPreferences mPreferences;
    private boolean mReady;
    private SimpleArrayMap mValues = new SimpleArrayMap();

    private SharedPreferences getSharedPreferences() {
        if (this.mPreferences == null) {
            initPreferences();
        }
        return this.mPreferences;
    }

    private void initPreferences() {
        this.mPreferences = CameraAppImpl.getAndroidContext().getSharedPreferences(provideKey(), 0);
    }

    public Editor editor() {
        this.mEditor = getSharedPreferences().edit();
        return this.mEditor;
    }

    public boolean getCloudBoolean(String str, boolean z) {
        if (!this.mReady) {
            return z;
        }
        Boolean bool = (Boolean) this.mValues.get(str);
        if (bool != null) {
            z = bool.booleanValue();
        }
        return z;
    }

    public float getCloudFloat(String str, float f) {
        if (!this.mReady) {
            return f;
        }
        Float f2 = (Float) this.mValues.get(str);
        if (f2 != null) {
            f = f2.floatValue();
        }
        return f;
    }

    public int getCloudInt(String str, int i) {
        if (!this.mReady) {
            return i;
        }
        Integer num = (Integer) this.mValues.get(str);
        if (num != null) {
            i = num.intValue();
        }
        return i;
    }

    public long getCloudLong(String str, long j) {
        if (!this.mReady) {
            return j;
        }
        Long l = (Long) this.mValues.get(str);
        if (l != null) {
            j = l.longValue();
        }
        return j;
    }

    public String getCloudString(String str, String str2) {
        if (!this.mReady) {
            return str2;
        }
        String str3 = (String) this.mValues.get(str);
        if (str3 == null) {
            str3 = str2;
        }
        return str3;
    }

    /* access modifiers changed from: protected */
    public SimpleArrayMap getValues() {
        return this.mValues;
    }

    public void setReady(boolean z) {
        this.mReady = z;
    }
}
