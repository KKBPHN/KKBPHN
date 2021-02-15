package com.android.camera.data.data.runing;

import com.android.camera.dualvideo.util.UserSelectData;
import java.util.ArrayList;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O00000o implements Consumer {
    private final /* synthetic */ ArrayList O0OOoO0;

    public /* synthetic */ O00000o(ArrayList arrayList) {
        this.O0OOoO0 = arrayList;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.stream().filter(C0130O00000oo.INSTANCE).findFirst().ifPresent(new O0000Oo0((UserSelectData) obj));
    }
}
