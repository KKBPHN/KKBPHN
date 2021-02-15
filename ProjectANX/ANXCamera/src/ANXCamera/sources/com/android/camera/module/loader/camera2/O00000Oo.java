package com.android.camera.module.loader.camera2;

import io.reactivex.functions.Function;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: lambda */
public final /* synthetic */ class O00000Oo implements Function {
    private final /* synthetic */ ConcurrentHashMap O0OOoO0;

    public /* synthetic */ O00000Oo(ConcurrentHashMap concurrentHashMap) {
        this.O0OOoO0 = concurrentHashMap;
    }

    public final Object apply(Object obj) {
        return Camera2OpenManager.O000000o(this.O0OOoO0, (Camera2Result) obj);
    }
}
