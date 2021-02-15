package com.android.camera.resource;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

@Deprecated
public class SimpleNetworkArchiveRequest extends SimpleNetworkDownloadRequest {
    protected ObservableEmitter mEmitter;
    private Observable mOutPutObservable;

    public SimpleNetworkArchiveRequest(String str, String str2) {
        super(str, str2);
    }

    public Observable startObservable(@NonNull Object obj) {
        return super.startObservable(obj);
    }
}
