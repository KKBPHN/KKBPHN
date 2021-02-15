package com.android.camera.network.resource;

import com.android.camera.network.download.Request;

public interface LiveDownloadHelper {
    Request createDownloadRequest(LiveResource liveResource);

    boolean isDownloaded(LiveResource liveResource);
}
