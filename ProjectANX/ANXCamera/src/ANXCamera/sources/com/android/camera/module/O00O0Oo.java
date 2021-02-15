package com.android.camera.module;

import com.android.camera.data.observeable.RxData.DataWrap;
import io.reactivex.functions.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O00O0Oo implements Consumer {
    private final /* synthetic */ FilmDreamModule O0OOoO0;

    public /* synthetic */ O00O0Oo(FilmDreamModule filmDreamModule) {
        this.O0OOoO0 = filmDreamModule;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.O0000OoO((DataWrap) obj);
    }
}
