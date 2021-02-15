package com.android.camera.videoplayer.ui;

import android.util.Pair;

public class ReadyForPlaybackIndicator {
    private static final boolean SHOW_LOGS = false;
    private static final String TAG = "ReadyForPlaybackIndicator";
    private boolean mFailedToPrepareUiForPlayback = false;
    private boolean mSurfaceTextureAvailable;
    private Pair mVideoSize;

    public boolean isFailedToPrepareUiForPlayback() {
        return this.mFailedToPrepareUiForPlayback;
    }

    public boolean isReadyForPlayback() {
        return isVideoSizeAvailable() && isSurfaceTextureAvailable();
    }

    public boolean isSurfaceTextureAvailable() {
        return this.mSurfaceTextureAvailable;
    }

    public boolean isVideoSizeAvailable() {
        Pair pair = this.mVideoSize;
        return (pair.first == null || pair.second == null) ? false : true;
    }

    public void setFailedToPrepareUiForPlayback(boolean z) {
        this.mFailedToPrepareUiForPlayback = z;
    }

    public void setSurfaceTextureAvailable(boolean z) {
        this.mSurfaceTextureAvailable = z;
    }

    public void setVideoSize(Integer num, Integer num2) {
        this.mVideoSize = new Pair(num, num2);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ReadyForPlaybackIndicator.class.getSimpleName());
        sb.append(isReadyForPlayback());
        return sb.toString();
    }
}
