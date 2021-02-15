package com.android.camera.multi;

import com.iqiyi.android.qigsaw.core.splitdownload.DownloadCallback;
import io.reactivex.functions.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O00000o implements Consumer {
    private final /* synthetic */ DownloadCallback O0OOoO0;

    public /* synthetic */ O00000o(DownloadCallback downloadCallback) {
        this.O0OOoO0 = downloadCallback;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.onProgress(((Long) obj).longValue());
    }
}
