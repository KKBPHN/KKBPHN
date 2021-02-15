package com.android.camera.multi;

import android.util.SparseArray;
import com.android.camera.resource.SimpleNetworkDownloadRequest;
import com.android.camera.statistic.CameraStatUtils;
import com.iqiyi.android.qigsaw.core.splitdownload.DownloadCallback;
import com.iqiyi.android.qigsaw.core.splitdownload.DownloadRequest;
import com.iqiyi.android.qigsaw.core.splitdownload.Downloader;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.io.File;
import java.util.List;

public class SampleDownloader implements Downloader {
    private static final int HIGH_PRIORITY = 10;
    private static final int LOW_PRIORITY = 0;
    private static final String TAG = "Split:SampleDownloader";
    private SparseArray mCallbackList = new SparseArray();
    private final SparseArray mDisposableList = new SparseArray();

    static /* synthetic */ ObservableSource O000000o(String str, DownloadCallback downloadCallback, PluginInfo pluginInfo) {
        SimpleNetworkDownloadRequest simpleNetworkDownloadRequest = new SimpleNetworkDownloadRequest(pluginInfo.getDownloadUrl(), str);
        simpleNetworkDownloadRequest.observableProgress(120).subscribe((Consumer) new O00000o(downloadCallback));
        return simpleNetworkDownloadRequest.startObservable((Object) pluginInfo);
    }

    private void onFinish(int i) {
        this.mCallbackList.remove(i);
        Disposable disposable = (Disposable) this.mDisposableList.get(i);
        if (disposable != null) {
            disposable.dispose();
            this.mDisposableList.remove(i);
        }
    }

    public /* synthetic */ void O000000o(DownloadCallback downloadCallback, int i, PluginInfo pluginInfo) {
        downloadCallback.onCompleted();
        onFinish(i);
    }

    public /* synthetic */ void O000000o(DownloadCallback downloadCallback, int i, Throwable th) {
        th.printStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(th.toString());
        CameraStatUtils.trackFeatureInstallError(sb.toString());
        downloadCallback.onError(257);
        onFinish(i);
    }

    public boolean cancelDownloadSync(int i) {
        DownloadCallback downloadCallback = (DownloadCallback) this.mCallbackList.get(i);
        if (downloadCallback == null) {
            return false;
        }
        downloadCallback.onCanceled();
        onFinish(i);
        return true;
    }

    public void deferredDownload(int i, List list, DownloadCallback downloadCallback, boolean z) {
        throw new RuntimeException("not supported yet");
    }

    public long getDownloadSizeThresholdWhenUsingMobileData() {
        return 1048576000;
    }

    public boolean isDeferredDownloadOnlyWhenUsingWifiData() {
        return false;
    }

    public void startDownload(int i, List list, DownloadCallback downloadCallback) {
        Observable observable;
        downloadCallback.onStart();
        DownloadRequest downloadRequest = (DownloadRequest) list.get(0);
        String url = downloadRequest.getUrl();
        StringBuilder sb = new StringBuilder();
        sb.append(downloadRequest.getFileDir());
        sb.append(File.separator);
        sb.append(downloadRequest.getFileName());
        String sb2 = sb.toString();
        downloadRequest.getModuleName();
        this.mCallbackList.put(i, downloadCallback);
        if (url.startsWith("http")) {
            SimpleNetworkDownloadRequest simpleNetworkDownloadRequest = new SimpleNetworkDownloadRequest(url, sb2);
            simpleNetworkDownloadRequest.observableProgress(120).subscribe((Consumer) new O000000o(downloadCallback));
            observable = simpleNetworkDownloadRequest.startObservable((Object) new PluginInfo(url));
        } else {
            observable = new PluginInfoRequest(url).startObservable(PluginInfo.class).flatMap(new O00000Oo(sb2, downloadCallback));
        }
        this.mDisposableList.append(i, observable.subscribe(new O00000o0(this, downloadCallback, i), new C0412O00000oO(this, downloadCallback, i)));
    }
}
