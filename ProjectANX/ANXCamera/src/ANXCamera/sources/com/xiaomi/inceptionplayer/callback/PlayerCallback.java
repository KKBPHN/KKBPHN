package com.xiaomi.inceptionplayer.callback;

import com.xiaomi.inceptionplayer.datastruct.VideoSize;
import com.xiaomi.inceptionplayer.enums.AVErrorState;

public interface PlayerCallback {
    void onAudioRenderingStart();

    void onFirstPacketRecved();

    void onOpenStreamFailed(AVErrorState aVErrorState);

    void onPlayerPaused();

    void onPlayerResumed();

    void onPlayerStarted();

    void onPlayerStoped();

    void onSeekCompleted();

    void onStartBuffering();

    void onStartPlaying();

    void onStartWithTimeInvalid(long j);

    void onStreamEOF();

    void onVideoRenderingStart();

    void onVideoSizeChanged(VideoSize videoSize);
}
