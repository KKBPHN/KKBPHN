package com.android.camera.videoplayer.player_messages;

import com.android.camera.videoplayer.PlayerMessageState;
import com.android.camera.videoplayer.manager.VideoPlayerManagerCallback;
import com.android.camera.videoplayer.ui.VideoPlayerView;

public abstract class PlayerMessage implements Message {
    private static final boolean SHOW_LOGS = false;
    private static final String TAG = "PlayerMessage";
    private final VideoPlayerManagerCallback mCallback;
    private final VideoPlayerView mCurrentPlayer;

    public PlayerMessage(VideoPlayerView videoPlayerView, VideoPlayerManagerCallback videoPlayerManagerCallback) {
        this.mCurrentPlayer = videoPlayerView;
        this.mCallback = videoPlayerManagerCallback;
    }

    /* access modifiers changed from: protected */
    public final PlayerMessageState getCurrentState() {
        return this.mCallback.getCurrentPlayerState();
    }

    public final void messageFinished() {
        this.mCallback.setVideoPlayerState(this.mCurrentPlayer, stateAfter());
    }

    public abstract void performAction(VideoPlayerView videoPlayerView);

    public final void polledFromQueue() {
        this.mCallback.setVideoPlayerState(this.mCurrentPlayer, stateBefore());
    }

    public final void runMessage() {
        performAction(this.mCurrentPlayer);
    }

    public abstract PlayerMessageState stateAfter();

    public abstract PlayerMessageState stateBefore();

    public String toString() {
        return getClass().getSimpleName();
    }
}
