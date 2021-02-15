package com.android.camera.videoplayer.manager;

import com.android.camera.videoplayer.meta.MetaData;
import com.android.camera.videoplayer.ui.VideoPlayerView;

public interface VideoItem {
    void playNewVideo(MetaData metaData, VideoPlayerView videoPlayerView, VideoPlayerManager videoPlayerManager);

    void stopPlayback(VideoPlayerManager videoPlayerManager);
}
