package com.iqiyi.android.qigsaw.core.splitdownload;

import java.util.List;

public interface Downloader {
    boolean cancelDownloadSync(int i);

    void deferredDownload(int i, List list, DownloadCallback downloadCallback, boolean z);

    long getDownloadSizeThresholdWhenUsingMobileData();

    boolean isDeferredDownloadOnlyWhenUsingWifiData();

    void startDownload(int i, List list, DownloadCallback downloadCallback);
}
