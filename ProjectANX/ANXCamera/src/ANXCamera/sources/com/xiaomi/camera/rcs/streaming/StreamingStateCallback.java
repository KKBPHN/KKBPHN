package com.xiaomi.camera.rcs.streaming;

import android.os.Bundle;

public interface StreamingStateCallback {
    void onStreamingServerStateChanged(int i, Bundle bundle);

    void onStreamingSessionStateChanged(int i, Bundle bundle);
}
