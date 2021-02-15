package com.android.camera.tts;

import android.util.Pair;
import java.util.function.BiConsumer;

/* compiled from: lambda */
public final /* synthetic */ class O0000OOo implements BiConsumer {
    public static final /* synthetic */ O0000OOo INSTANCE = new O0000OOo();

    private /* synthetic */ O0000OOo() {
    }

    public final void accept(Object obj, Object obj2) {
        ((BatchListener) obj).onError((Pair) obj2);
    }
}
