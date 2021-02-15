package com.arcsoft.avatar2.recoder;

import com.arcsoft.avatar2.gl.GLFramebuffer;

public class FrameItem {
    long a;
    public int mFrameIndex;
    public GLFramebuffer mFramebuffer;
    public volatile boolean mIsEmpty;
    public volatile boolean mIsInited;
}
