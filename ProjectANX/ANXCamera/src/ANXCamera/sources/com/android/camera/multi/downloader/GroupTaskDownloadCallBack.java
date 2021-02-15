package com.android.camera.multi.downloader;

public interface GroupTaskDownloadCallBack {
    void onCanceled();

    void onCompleted();

    void onError(int i);

    void onProgress(long j);

    void onStarted();
}
