package com.android.camera.resource;

import com.android.camera.Util;
import java.io.IOException;

public class SimpleNativeDecompressRequest extends BaseObservableRequest {
    private String mArchivePath;
    private String mTargetPath;

    public SimpleNativeDecompressRequest(String str, String str2) {
        this.mArchivePath = str;
        this.mTargetPath = str2;
    }

    /* access modifiers changed from: protected */
    public void scheduleRequest(ResponseListener responseListener, BaseResourceItem baseResourceItem) {
        try {
            Util.verifySdcardZip(this.mArchivePath, this.mTargetPath, 32768);
            baseResourceItem.onDecompressFinished(this.mTargetPath, true);
        } catch (IOException e) {
            e.printStackTrace();
            responseListener.onResponseError(3, e.getMessage(), null);
        }
        responseListener.onResponse(baseResourceItem, false);
    }
}
