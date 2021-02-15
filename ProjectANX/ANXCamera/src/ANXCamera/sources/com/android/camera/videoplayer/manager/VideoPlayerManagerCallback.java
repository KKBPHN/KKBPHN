package com.android.camera.videoplayer.manager;

import com.android.camera.videoplayer.PlayerMessageState;
import com.android.camera.videoplayer.meta.MetaData;
import com.android.camera.videoplayer.ui.VideoPlayerView;

public interface VideoPlayerManagerCallback {
    PlayerMessageState getCurrentPlayerState();

    void setCurrentItem(MetaData metaData, VideoPlayerView videoPlayerView);

    void setVideoPlayerState(VideoPlayerView videoPlayerView, PlayerMessageState playerMessageState);
}
