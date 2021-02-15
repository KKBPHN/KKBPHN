package com.android.camera.multi;

import com.iqiyi.android.qigsaw.core.splitdownload.DownloadCallback;
import io.reactivex.functions.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O00000o0 implements Consumer {
    private final /* synthetic */ SampleDownloader O0OOoO0;
    private final /* synthetic */ DownloadCallback O0OOoOO;
    private final /* synthetic */ int O0OOoOo;

    public /* synthetic */ O00000o0(SampleDownloader sampleDownloader, DownloadCallback downloadCallback, int i) {
        this.O0OOoO0 = sampleDownloader;
        this.O0OOoOO = downloadCallback;
        this.O0OOoOo = i;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.O000000o(this.O0OOoOO, this.O0OOoOo, (PluginInfo) obj);
    }
}
