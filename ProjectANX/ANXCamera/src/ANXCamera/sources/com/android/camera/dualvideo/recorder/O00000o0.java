package com.android.camera.dualvideo.recorder;

import io.reactivex.functions.Function;
import java.util.Arrays;

/* compiled from: lambda */
public final /* synthetic */ class O00000o0 implements Function {
    public static final /* synthetic */ O00000o0 INSTANCE = new O00000o0();

    private /* synthetic */ O00000o0() {
    }

    public final Object apply(Object obj) {
        return Boolean.valueOf(Arrays.stream((Object[]) obj).allMatch(C0172O00000oO.INSTANCE));
    }
}
