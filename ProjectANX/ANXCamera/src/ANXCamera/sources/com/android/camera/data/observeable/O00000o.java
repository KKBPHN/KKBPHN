package com.android.camera.data.observeable;

import com.android.camera.resource.BaseResourceItem;
import io.reactivex.functions.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O00000o implements Consumer {
    private final /* synthetic */ VMResource O0OOoO0;
    private final /* synthetic */ BaseResourceItem O0OOoOO;

    public /* synthetic */ O00000o(VMResource vMResource, BaseResourceItem baseResourceItem) {
        this.O0OOoO0 = vMResource;
        this.O0OOoOO = baseResourceItem;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.O000000o(this.O0OOoOO, (Throwable) obj);
    }
}
