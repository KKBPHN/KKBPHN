package com.android.camera.dualvideo.render;

import com.android.camera.dualvideo.util.SelectIndex;
import com.android.camera.dualvideo.util.UserSelectData;
import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O00oOooO reason: case insensitive filesystem */
public final /* synthetic */ class C0235O00oOooO implements Predicate {
    private final /* synthetic */ SelectIndex O0OOoO0;

    public /* synthetic */ C0235O00oOooO(SelectIndex selectIndex) {
        this.O0OOoO0 = selectIndex;
    }

    public final boolean test(Object obj) {
        return CameraItemManager.O000000o(this.O0OOoO0, (UserSelectData) obj);
    }
}
