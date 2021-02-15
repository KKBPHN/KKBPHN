package com.android.camera.multi;

import com.iqiyi.android.qigsaw.core.splitdownload.DownloadCallback;
import io.reactivex.functions.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.multi.O00000oO reason: case insensitive filesystem */
public final /* synthetic */ class C0412O00000oO implements Consumer {
    private final /* synthetic */ SampleDownloader O0OOoO0;
    private final /* synthetic */ DownloadCallback O0OOoOO;
    private final /* synthetic */ int O0OOoOo;

    public /* synthetic */ C0412O00000oO(SampleDownloader sampleDownloader, DownloadCallback downloadCallback, int i) {
        this.O0OOoO0 = sampleDownloader;
        this.O0OOoOO = downloadCallback;
        this.O0OOoOo = i;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.O000000o(this.O0OOoOO, this.O0OOoOo, (Throwable) obj);
    }
}
