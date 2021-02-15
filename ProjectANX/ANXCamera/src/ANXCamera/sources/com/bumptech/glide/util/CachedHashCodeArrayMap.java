package com.bumptech.glide.util;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;

public final class CachedHashCodeArrayMap extends ArrayMap {
    private int hashCode;

    public void clear() {
        this.hashCode = 0;
        super.clear();
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = super.hashCode();
        }
        return this.hashCode;
    }

    public Object put(Object obj, Object obj2) {
        this.hashCode = 0;
        return super.put(obj, obj2);
    }

    public void putAll(SimpleArrayMap simpleArrayMap) {
        this.hashCode = 0;
        super.putAll(simpleArrayMap);
    }

    public Object removeAt(int i) {
        this.hashCode = 0;
        return super.removeAt(i);
    }

    public Object setValueAt(int i, Object obj) {
        this.hashCode = 0;
        return super.setValueAt(i, obj);
    }
}
