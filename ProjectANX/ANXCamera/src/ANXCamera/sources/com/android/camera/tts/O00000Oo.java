package com.android.camera.tts;

import android.util.Pair;
import java.util.function.BiConsumer;

/* compiled from: lambda */
public final /* synthetic */ class O00000Oo implements BiConsumer {
    public static final /* synthetic */ O00000Oo INSTANCE = new O00000Oo();

    private /* synthetic */ O00000Oo() {
    }

    public final void accept(Object obj, Object obj2) {
        ((BatchListener) obj).onDone((Pair) obj2);
    }
}
