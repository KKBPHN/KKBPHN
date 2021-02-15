package com.android.camera.videoplayer;

import com.android.camera.videoplayer.manager.VideoPlayerManagerCallback;
import com.android.camera.videoplayer.meta.MetaData;
import com.android.camera.videoplayer.player_messages.PlayerMessage;
import com.android.camera.videoplayer.ui.VideoPlayerView;

public class SetNewViewForPlayback extends PlayerMessage {
    private final VideoPlayerManagerCallback mCallback;
    private final MetaData mCurrentItemMetaData;
    private final VideoPlayerView mCurrentPlayer;

    public SetNewViewForPlayback(MetaData metaData, VideoPlayerView videoPlayerView, VideoPlayerManagerCallback videoPlayerManagerCallback) {
        super(videoPlayerView, videoPlayerManagerCallback);
        this.mCurrentItemMetaData = metaData;
        this.mCurrentPlayer = videoPlayerView;
        this.mCallback = videoPlayerManagerCallback;
    }

    /* access modifiers changed from: protected */
    public void performAction(VideoPlayerView videoPlayerView) {
        this.mCallback.setCurrentItem(this.mCurrentItemMetaData, this.mCurrentPlayer);
    }

    /* access modifiers changed from: protected */
    public PlayerMessageState stateAfter() {
        return PlayerMessageState.IDLE;
    }

    /* access modifiers changed from: protected */
    public PlayerMessageState stateBefore() {
        return PlayerMessageState.SETTING_NEW_PLAYER;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SetNewViewForPlayback.class.getSimpleName());
        sb.append(", mCurrentPlayer ");
        sb.append(this.mCurrentPlayer);
        return sb.toString();
    }
}
