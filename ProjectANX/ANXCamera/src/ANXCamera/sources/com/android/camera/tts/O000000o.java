package com.android.camera.tts;

import android.util.Pair;
import java.util.function.BiConsumer;

/* compiled from: lambda */
public final /* synthetic */ class O000000o implements BiConsumer {
    public static final /* synthetic */ O000000o INSTANCE = new O000000o();

    private /* synthetic */ O000000o() {
    }

    public final void accept(Object obj, Object obj2) {
        ((BatchListener) obj).onStop((Pair) obj2);
    }
}
