package com.android.camera.dualvideo.util;

import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import java.util.Comparator;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.util.O0000OoO reason: case insensitive filesystem */
public final /* synthetic */ class C0239O0000OoO implements Comparator {
    public static final /* synthetic */ C0239O0000OoO INSTANCE = new C0239O0000OoO();

    private /* synthetic */ C0239O0000OoO() {
    }

    public final int compare(Object obj, Object obj2) {
        return Float.compare(((ConfigItem) obj).mPresentZoom, ((ConfigItem) obj2).mPresentZoom);
    }
}
