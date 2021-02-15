package com.android.camera.videoplayer.player_messages;

public interface Message {
    void messageFinished();

    void polledFromQueue();

    void runMessage();
}
