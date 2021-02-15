package com.android.camera.videoplayer.player_messages;

import com.android.camera.videoplayer.PlayerMessageState;
import com.android.camera.videoplayer.manager.VideoPlayerManagerCallback;
import com.android.camera.videoplayer.ui.VideoPlayerView;

public class ClearPlayerInstance extends PlayerMessage {
    public ClearPlayerInstance(VideoPlayerView videoPlayerView, VideoPlayerManagerCallback videoPlayerManagerCallback) {
        super(videoPlayerView, videoPlayerManagerCallback);
    }

    /* access modifiers changed from: protected */
    public void performAction(VideoPlayerView videoPlayerView) {
        videoPlayerView.clearPlayerInstance();
    }

    /* access modifiers changed from: protected */
    public PlayerMessageState stateAfter() {
        return PlayerMessageState.PLAYER_INSTANCE_CLEARED;
    }

    /* access modifiers changed from: protected */
    public PlayerMessageState stateBefore() {
        return PlayerMessageState.CLEARING_PLAYER_INSTANCE;
    }
}
