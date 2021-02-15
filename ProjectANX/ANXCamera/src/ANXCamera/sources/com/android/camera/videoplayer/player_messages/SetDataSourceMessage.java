package com.android.camera.videoplayer.player_messages;

import com.android.camera.videoplayer.PlayerMessageState;
import com.android.camera.videoplayer.manager.VideoPlayerManagerCallback;
import com.android.camera.videoplayer.ui.VideoPlayerView;

public abstract class SetDataSourceMessage extends PlayerMessage {
    public SetDataSourceMessage(VideoPlayerView videoPlayerView, VideoPlayerManagerCallback videoPlayerManagerCallback) {
        super(videoPlayerView, videoPlayerManagerCallback);
    }

    /* access modifiers changed from: protected */
    public PlayerMessageState stateAfter() {
        return PlayerMessageState.DATA_SOURCE_SET;
    }

    /* access modifiers changed from: protected */
    public PlayerMessageState stateBefore() {
        return PlayerMessageState.SETTING_DATA_SOURCE;
    }
}
