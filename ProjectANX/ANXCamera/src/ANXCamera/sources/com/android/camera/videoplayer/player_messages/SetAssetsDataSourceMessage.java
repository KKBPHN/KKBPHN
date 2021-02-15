package com.android.camera.videoplayer.player_messages;

import android.content.res.AssetFileDescriptor;
import com.android.camera.videoplayer.manager.VideoPlayerManagerCallback;
import com.android.camera.videoplayer.ui.VideoPlayerView;

public class SetAssetsDataSourceMessage extends SetDataSourceMessage {
    private final AssetFileDescriptor mAssetFileDescriptor;

    public SetAssetsDataSourceMessage(VideoPlayerView videoPlayerView, AssetFileDescriptor assetFileDescriptor, VideoPlayerManagerCallback videoPlayerManagerCallback) {
        super(videoPlayerView, videoPlayerManagerCallback);
        this.mAssetFileDescriptor = assetFileDescriptor;
    }

    /* access modifiers changed from: protected */
    public void performAction(VideoPlayerView videoPlayerView) {
        videoPlayerView.setDataSource(this.mAssetFileDescriptor);
    }
}
