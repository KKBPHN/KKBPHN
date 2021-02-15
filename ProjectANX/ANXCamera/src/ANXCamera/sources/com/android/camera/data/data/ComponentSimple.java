package com.android.camera.data.data;

import java.util.List;

public abstract class ComponentSimple extends ComponentData {
    private boolean mClosed;

    public ComponentSimple(DataItemBase dataItemBase) {
        super(dataItemBase);
    }

    public boolean asBoolean(Object obj) {
        return obj instanceof Boolean;
    }

    public boolean asFloat(Object obj) {
        return obj instanceof Float;
    }

    public boolean asInteger(Object obj) {
        return obj instanceof Integer;
    }

    public boolean asLong(Object obj) {
        return obj instanceof Long;
    }

    public boolean asString(Object obj) {
        return obj instanceof String;
    }

    @Deprecated
    public String getDefaultValue(int i) {
        return null;
    }

    public abstract Object getDefaultValueGeneric(int i);

    public List getItems() {
        return null;
    }

    public final Object getValue(int i) {
        Object defaultValueGeneric = getDefaultValueGeneric(i);
        if (asString(defaultValueGeneric)) {
            return this.mParentDataItem.getString(getKey(i), (String) defaultValueGeneric);
        }
        if (asBoolean(defaultValueGeneric)) {
            return Boolean.valueOf(this.mParentDataItem.getBoolean(getKey(i), ((Boolean) defaultValueGeneric).booleanValue()));
        }
        if (asInteger(defaultValueGeneric)) {
            return Integer.valueOf(this.mParentDataItem.getInt(getKey(i), ((Integer) defaultValueGeneric).intValue()));
        }
        if (asLong(defaultValueGeneric)) {
            return Long.valueOf(this.mParentDataItem.getLong(getKey(i), ((Long) defaultValueGeneric).longValue()));
        }
        if (asFloat(defaultValueGeneric)) {
            return Float.valueOf(this.mParentDataItem.getFloat(getKey(i), ((Float) defaultValueGeneric).floatValue()));
        }
        return null;
    }

    public final void putValue(int i, Object obj) {
        if (!this.mParentDataItem.isTransient()) {
            this.mParentDataItem.editor();
        }
        if (asString(obj)) {
            this.mParentDataItem.putString(getKey(i), (String) obj);
        } else if (asBoolean(obj)) {
            this.mParentDataItem.putBoolean(getKey(i), ((Boolean) obj).booleanValue());
        } else if (asInteger(obj)) {
            this.mParentDataItem.putInt(getKey(i), ((Integer) obj).intValue());
        } else if (asLong(obj)) {
            this.mParentDataItem.putLong(getKey(i), ((Long) obj).longValue());
        } else if (asFloat(obj)) {
            this.mParentDataItem.putFloat(getKey(i), ((Float) obj).floatValue());
        }
        if (!this.mParentDataItem.isTransient()) {
            this.mParentDataItem.apply();
        }
    }
}
