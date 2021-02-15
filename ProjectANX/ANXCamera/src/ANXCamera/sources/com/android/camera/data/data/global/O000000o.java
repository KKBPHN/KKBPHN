package com.android.camera.data.data.global;

import java.util.function.ToIntFunction;

/* compiled from: lambda */
public final /* synthetic */ class O000000o implements ToIntFunction {
    public static final /* synthetic */ O000000o INSTANCE = new O000000o();

    private /* synthetic */ O000000o() {
    }

    public final int applyAsInt(Object obj) {
        return Integer.parseInt(((String) obj).trim());
    }
}
