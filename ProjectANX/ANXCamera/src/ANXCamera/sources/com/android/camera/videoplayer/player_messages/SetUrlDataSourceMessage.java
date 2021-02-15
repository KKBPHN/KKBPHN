package com.android.camera.videoplayer.player_messages;

import com.android.camera.videoplayer.manager.VideoPlayerManagerCallback;
import com.android.camera.videoplayer.ui.VideoPlayerView;

public class SetUrlDataSourceMessage extends SetDataSourceMessage {
    private final String mVideoUrl;

    public SetUrlDataSourceMessage(VideoPlayerView videoPlayerView, String str, VideoPlayerManagerCallback videoPlayerManagerCallback) {
        super(videoPlayerView, videoPlayerManagerCallback);
        this.mVideoUrl = str;
    }

    /* access modifiers changed from: protected */
    public void performAction(VideoPlayerView videoPlayerView) {
        videoPlayerView.setDataSource(this.mVideoUrl);
    }
}
