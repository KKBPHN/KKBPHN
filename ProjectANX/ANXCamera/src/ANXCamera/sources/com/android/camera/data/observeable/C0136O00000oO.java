package com.android.camera.data.observeable;

import com.android.camera.multi.PluginInfo;
import com.android.camera.resource.SimpleNetworkDownloadRequest;
import io.reactivex.functions.Function;

/* compiled from: lambda */
/* renamed from: com.android.camera.data.observeable.O00000oO reason: case insensitive filesystem */
public final /* synthetic */ class C0136O00000oO implements Function {
    public static final /* synthetic */ C0136O00000oO INSTANCE = new C0136O00000oO();

    private /* synthetic */ C0136O00000oO() {
    }

    public final Object apply(Object obj) {
        return new SimpleNetworkDownloadRequest(((PluginInfo) obj).getDownloadUrl(), ((PluginInfo) obj).resourceItem.mZipPath).startObservable((Object) ((PluginInfo) obj).resourceItem);
    }
}
