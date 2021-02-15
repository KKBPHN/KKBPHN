package com.android.camera.dualvideo.render;

import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class O000OOo implements Predicate {
    private final /* synthetic */ String O0OOoO0;

    public /* synthetic */ O000OOo(String str) {
        this.O0OOoO0 = str;
    }

    public final boolean test(Object obj) {
        return ((MiscRenderItem) obj).getName().equals(this.O0OOoO0);
    }
}
