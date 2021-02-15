package com.xiaomi.camera.rcs.streaming;

import net.majorkernelpanic.streaming.Session;

public class StreamingSession extends Session {
    private String mSessionId;

    public String getSessionId() {
        return this.mSessionId;
    }

    public void setSessionId(String str) {
        this.mSessionId = str;
    }
}
