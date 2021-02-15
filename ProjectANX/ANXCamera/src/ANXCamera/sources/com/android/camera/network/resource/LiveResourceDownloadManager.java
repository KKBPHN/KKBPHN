package com.android.camera.network.resource;

import com.android.camera.log.Log;
import com.android.camera.network.download.GalleryDownloadManager;
import com.android.camera.network.download.GalleryDownloadManager.OnCompleteListener;
import com.android.camera.network.download.Request;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveResourceDownloadManager {
    private static final String TAG = "LiveResourceDownloadManager";
    /* access modifiers changed from: private */
    public Map mDownloadState;
    private List mListeners;
    private OnCompleteListener mOnCompleteListener;
    /* access modifiers changed from: private */
    public Object object;

    class Instance {
        public static LiveResourceDownloadManager mInstance = new LiveResourceDownloadManager();

        private Instance() {
        }
    }

    private LiveResourceDownloadManager() {
        this.mDownloadState = new HashMap();
        this.object = new Object();
        this.mListeners = new ArrayList();
        this.mOnCompleteListener = new OnCompleteListener() {
            public void onRequestComplete(Request request, int i) {
                StringBuilder sb = new StringBuilder();
                sb.append("download finish ");
                sb.append(i);
                Log.v(LiveResourceDownloadManager.TAG, sb.toString());
                String tag = request.getTag();
                synchronized (LiveResourceDownloadManager.this.object) {
                    LiveResourceDownloadManager.this.mDownloadState.remove(tag);
                }
                LiveResourceDownloadManager.this.dispatchListener(tag, i == 0 ? 3 : 4);
            }
        };
    }

    /* access modifiers changed from: private */
    public void dispatchListener(String str, int i) {
        for (OnLiveDownloadListener onFinish : this.mListeners) {
            onFinish.onFinish(str, i);
        }
    }

    public static LiveResourceDownloadManager getInstance() {
        return Instance.mInstance;
    }

    public void addDownloadListener(OnLiveDownloadListener onLiveDownloadListener) {
        this.mListeners.add(onLiveDownloadListener);
    }

    public void download(LiveResource liveResource, LiveDownloadHelper liveDownloadHelper) {
        String str = liveResource.id;
        StringBuilder sb = new StringBuilder();
        sb.append("downloading ");
        sb.append(str);
        String sb2 = sb.toString();
        String str2 = TAG;
        Log.v(str2, sb2);
        if (liveDownloadHelper.isDownloaded(liveResource)) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("file downloaded ");
            sb3.append(str);
            Log.v(str2, sb3.toString());
            dispatchListener(str, 1);
            return;
        }
        Request createDownloadRequest = liveDownloadHelper.createDownloadRequest(liveResource);
        createDownloadRequest.setAllowedOverMetered(true);
        GalleryDownloadManager.INSTANCE.enqueue(createDownloadRequest, this.mOnCompleteListener);
    }

    public int getDownloadState(String str) {
        if (this.mDownloadState.containsKey(str)) {
            return ((Integer) this.mDownloadState.get(str)).intValue();
        }
        return 0;
    }

    public void removeDownloadListener(OnLiveDownloadListener onLiveDownloadListener) {
        this.mListeners.remove(onLiveDownloadListener);
    }
}
