package com.bumptech.glide.load.engine.bitmap_recycle;

import com.android.camera.Util;
import java.util.Map.Entry;
import java.util.TreeMap;

class PrettyPrintTreeMap extends TreeMap {
    PrettyPrintTreeMap() {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("( ");
        for (Entry entry : entrySet()) {
            sb.append('{');
            sb.append(entry.getKey());
            sb.append(Util.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            sb.append(entry.getValue());
            sb.append("}, ");
        }
        if (!isEmpty()) {
            sb.replace(sb.length() - 2, sb.length(), "");
        }
        sb.append(" )");
        return sb.toString();
    }
}
