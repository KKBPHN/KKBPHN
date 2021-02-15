package com.android.camera.videoplayer.manager;

import android.content.res.AssetFileDescriptor;
import com.android.camera.videoplayer.meta.MetaData;
import com.android.camera.videoplayer.ui.VideoPlayerView;

public interface VideoPlayerManager {
    void playNewVideo(MetaData metaData, VideoPlayerView videoPlayerView, AssetFileDescriptor assetFileDescriptor);

    void playNewVideo(MetaData metaData, VideoPlayerView videoPlayerView, String str);

    void resetMediaPlayer();

    void resumeMediaPlayer();

    void stopAnyPlayback();

    void terminate();
}
