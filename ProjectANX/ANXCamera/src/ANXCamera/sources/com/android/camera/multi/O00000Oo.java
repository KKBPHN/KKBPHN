package com.android.camera.multi;

import com.iqiyi.android.qigsaw.core.splitdownload.DownloadCallback;
import io.reactivex.functions.Function;

/* compiled from: lambda */
public final /* synthetic */ class O00000Oo implements Function {
    private final /* synthetic */ String O0OOoO0;
    private final /* synthetic */ DownloadCallback O0OOoOO;

    public /* synthetic */ O00000Oo(String str, DownloadCallback downloadCallback) {
        this.O0OOoO0 = str;
        this.O0OOoOO = downloadCallback;
    }

    public final Object apply(Object obj) {
        return SampleDownloader.O000000o(this.O0OOoO0, this.O0OOoOO, (PluginInfo) obj);
    }
}
