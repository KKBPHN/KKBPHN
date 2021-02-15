package com.ss.android.vesdk.runtime.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import androidx.annotation.NonNull;
import java.util.Map;

public class VESP {
    public static final String KEY_DEVICEID = "KEY_DEVICEID";
    public static final String KEY_MODELS_DIR_SP_KEY = "vesdk_models_dir_sp_key";
    public static final String KEY_SENSOR_REPORTED = "sensor_reported";
    private Editor editor;
    private boolean mInited;
    private SharedPreferences sharedPreferences;

    enum VESPSingleton {
        INSTANCE;
        
        private VESP mInstance;

        public VESP getInstance() {
            return this.mInstance;
        }
    }

    private VESP() {
        this.mInited = false;
    }

    public static VESP getInstance() {
        return VESPSingleton.INSTANCE.getInstance();
    }

    private synchronized void initEditorIfNeed() {
        if (this.editor == null) {
            this.editor = this.sharedPreferences.edit();
        }
    }

    public void clear() {
        initEditorIfNeed();
        this.editor.clear();
        this.editor.commit();
    }

    public boolean contain(@NonNull String str) {
        return this.sharedPreferences.contains(str);
    }

    public Object get(@NonNull String str, @NonNull Object obj) {
        if (obj instanceof String) {
            return this.sharedPreferences.getString(str, (String) obj);
        }
        if (obj instanceof Integer) {
            return Integer.valueOf(this.sharedPreferences.getInt(str, ((Integer) obj).intValue()));
        }
        if (obj instanceof Boolean) {
            return Boolean.valueOf(this.sharedPreferences.getBoolean(str, ((Boolean) obj).booleanValue()));
        }
        if (obj instanceof Float) {
            return Float.valueOf(this.sharedPreferences.getFloat(str, ((Float) obj).floatValue()));
        }
        boolean z = obj instanceof Long;
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        return z ? Long.valueOf(sharedPreferences2.getLong(str, ((Long) obj).longValue())) : sharedPreferences2.getString(str, null);
    }

    public Map getAll() {
        return this.sharedPreferences.getAll();
    }

    public void init(Context context) {
        synchronized (this) {
            if (!this.mInited) {
                this.sharedPreferences = context.getSharedPreferences(context.getPackageName(), 0);
                this.mInited = true;
            }
        }
    }

    public void put(@NonNull String str, @NonNull Object obj) {
        put(str, obj, false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x005e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void put(@NonNull String str, @NonNull Object obj, boolean z) {
        Editor editor2;
        String obj2;
        initEditorIfNeed();
        if (obj instanceof String) {
            editor2 = this.editor;
            obj2 = (String) obj;
        } else {
            if (obj instanceof Integer) {
                this.editor.putInt(str, ((Integer) obj).intValue());
            } else if (obj instanceof Boolean) {
                this.editor.putBoolean(str, ((Boolean) obj).booleanValue());
            } else if (obj instanceof Float) {
                this.editor.putFloat(str, ((Float) obj).floatValue());
            } else if (obj instanceof Long) {
                this.editor.putLong(str, ((Long) obj).longValue());
            } else {
                editor2 = this.editor;
                obj2 = obj.toString();
            }
            Editor editor3 = this.editor;
            if (!z) {
                editor3.apply();
                return;
            } else {
                editor3.commit();
                return;
            }
        }
        editor2.putString(str, obj2);
        Editor editor32 = this.editor;
        if (!z) {
        }
    }

    public void remove(@NonNull String str) {
        initEditorIfNeed();
        this.editor.remove(str);
        this.editor.commit();
    }
}
