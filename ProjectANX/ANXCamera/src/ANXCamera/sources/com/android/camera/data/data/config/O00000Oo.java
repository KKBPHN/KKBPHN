package com.android.camera.data.data.config;

import com.android.camera.data.data.ComponentDataItem;
import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class O00000Oo implements Predicate {
    private final /* synthetic */ String O0OOoO0;

    public /* synthetic */ O00000Oo(String str) {
        this.O0OOoO0 = str;
    }

    public final boolean test(Object obj) {
        return ((ComponentDataItem) obj).mValue.equals(this.O0OOoO0);
    }
}
