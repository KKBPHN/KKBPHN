package com.android.camera.fragment.vv;

import com.android.camera.multi.PluginInfo;
import com.android.camera.resource.BaseResourceRaw;
import com.android.camera.resource.SimpleNetworkRawRequest;
import io.reactivex.functions.Function;

/* compiled from: lambda */
/* renamed from: com.android.camera.fragment.vv.O0000OoO reason: case insensitive filesystem */
public final /* synthetic */ class C0333O0000OoO implements Function {
    public static final /* synthetic */ C0333O0000OoO INSTANCE = new C0333O0000OoO();

    private /* synthetic */ C0333O0000OoO() {
    }

    public final Object apply(Object obj) {
        return new SimpleNetworkRawRequest(((PluginInfo) obj).getDownloadUrl()).startObservable(BaseResourceRaw.class);
    }
}
