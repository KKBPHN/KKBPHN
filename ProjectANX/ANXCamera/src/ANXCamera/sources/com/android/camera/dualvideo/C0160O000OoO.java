package com.android.camera.dualvideo;

import com.android.camera.dualvideo.util.UserSelectData;
import java.util.Comparator;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O000OoO reason: case insensitive filesystem */
public final /* synthetic */ class C0160O000OoO implements Comparator {
    public static final /* synthetic */ C0160O000OoO INSTANCE = new C0160O000OoO();

    private /* synthetic */ C0160O000OoO() {
    }

    public final int compare(Object obj, Object obj2) {
        return Integer.compare(((UserSelectData) obj).getmSelectWindowLayoutType().getIndex(), ((UserSelectData) obj2).getmSelectWindowLayoutType().getIndex());
    }
}
