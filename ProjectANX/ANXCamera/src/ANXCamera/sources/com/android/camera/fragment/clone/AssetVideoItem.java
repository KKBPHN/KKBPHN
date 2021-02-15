package com.android.camera.fragment.clone;

import android.content.res.AssetFileDescriptor;
import com.android.camera.videoplayer.manager.VideoPlayerManager;
import com.android.camera.videoplayer.meta.MetaData;
import com.android.camera.videoplayer.ui.VideoPlayerView;

public class AssetVideoItem extends BaseVideoItem {
    private static final boolean SHOW_LOGS = false;
    private static final String TAG = "AssetVideoItem";
    private final AssetFileDescriptor mAssetFileDescriptor;
    protected final int mCoverResource;

    public AssetVideoItem(AssetFileDescriptor assetFileDescriptor, VideoPlayerManager videoPlayerManager, int i) {
        super(videoPlayerManager);
        this.mAssetFileDescriptor = assetFileDescriptor;
        this.mCoverResource = i;
    }

    public String getContentDescription() {
        return "";
    }

    public void playNewVideo(MetaData metaData, VideoPlayerView videoPlayerView, VideoPlayerManager videoPlayerManager) {
        AssetFileDescriptor assetFileDescriptor = this.mAssetFileDescriptor;
        if (assetFileDescriptor != null) {
            videoPlayerManager.playNewVideo(metaData, videoPlayerView, assetFileDescriptor);
        } else {
            videoPlayerManager.stopAnyPlayback();
        }
    }

    public void stopPlayback(VideoPlayerManager videoPlayerManager) {
        videoPlayerManager.stopAnyPlayback();
    }

    public void update(int i, VideoViewHolder videoViewHolder, VideoPlayerManager videoPlayerManager) {
        videoViewHolder.mCover.setVisibility(0);
    }
}
