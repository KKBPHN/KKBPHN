package com.android.camera.dualvideo.recorder;

import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class O0000Oo0 implements Predicate {
    private final /* synthetic */ String O0OOoO0;

    public /* synthetic */ O0000Oo0(String str) {
        this.O0OOoO0 = str;
    }

    public final boolean test(Object obj) {
        return ((RecordType) obj).getName().equals(this.O0OOoO0);
    }
}
