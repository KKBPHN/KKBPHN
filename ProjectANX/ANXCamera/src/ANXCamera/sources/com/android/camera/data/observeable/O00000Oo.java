package com.android.camera.data.observeable;

import com.android.camera.resource.BaseResourceItem;
import com.android.camera.resource.SimpleNativeDecompressRequest;
import io.reactivex.functions.Function;

/* compiled from: lambda */
public final /* synthetic */ class O00000Oo implements Function {
    public static final /* synthetic */ O00000Oo INSTANCE = new O00000Oo();

    private /* synthetic */ O00000Oo() {
    }

    public final Object apply(Object obj) {
        return new SimpleNativeDecompressRequest(((BaseResourceItem) obj).mZipPath, ((BaseResourceItem) obj).baseArchivesFolder).startObservable((Object) (BaseResourceItem) obj);
    }
}
