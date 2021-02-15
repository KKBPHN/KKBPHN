package com.arcsoft.avatar.recoder;

import android.opengl.GLES30;
import com.arcsoft.avatar.gl.GLFramebuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FrameQueue {
    private static final String a = "FrameQueue";
    private FrameItem b = null;
    private FrameItem c = null;
    private List d = new ArrayList();
    private Queue e = new LinkedList();
    private boolean f;

    public void addEmptyFrameForConsumer() {
        FrameItem frameItem = this.c;
        if (frameItem != null) {
            this.d.add(frameItem);
            this.c = null;
        }
    }

    public void addFrameForProducer() {
        FrameItem frameItem = this.b;
        if (frameItem != null) {
            this.e.offer(frameItem);
            this.b = null;
        }
    }

    public void deleteSync(FrameItem frameItem) {
        try {
            if (0 != frameItem.a) {
                GLES30.glDeleteSync(frameItem.a);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        } catch (Throwable th) {
            frameItem.a = 0;
            throw th;
        }
        frameItem.a = 0;
    }

    public FrameItem getFrameForConsumer() {
        FrameItem frameItem = this.c;
        if (frameItem != null) {
            return frameItem;
        }
        if (this.e.isEmpty()) {
            return null;
        }
        this.c = (FrameItem) this.e.poll();
        return this.c;
    }

    public FrameItem getFrameForProducer() {
        Object poll;
        FrameItem frameItem = this.b;
        if (frameItem != null) {
            return frameItem;
        }
        if (!this.d.isEmpty()) {
            poll = this.d.remove(0);
        } else if (this.e.isEmpty()) {
            return null;
        } else {
            poll = this.e.poll();
        }
        this.b = (FrameItem) poll;
        return this.b;
    }

    public void init(int i, int i2, int i3, boolean z) {
        unInit();
        for (int i4 = 0; i4 < i; i4++) {
            FrameItem frameItem = new FrameItem();
            frameItem.mIsEmpty = true;
            frameItem.mIsInited = true;
            frameItem.mFrameIndex = i4;
            frameItem.mFramebuffer = new GLFramebuffer();
            frameItem.mFramebuffer.init(i2, i3, z);
            this.d.add(frameItem);
        }
        this.f = true;
    }

    public boolean isIsInited() {
        return this.f;
    }

    public int queueSize() {
        return this.e.size();
    }

    public void unInit() {
        FrameItem frameItem = this.b;
        if (frameItem != null) {
            GLFramebuffer gLFramebuffer = frameItem.mFramebuffer;
            if (gLFramebuffer != null) {
                gLFramebuffer.unInit();
                deleteSync(this.b);
                this.b.mFramebuffer = null;
                this.b = null;
            }
        }
        FrameItem frameItem2 = this.c;
        if (frameItem2 != null) {
            GLFramebuffer gLFramebuffer2 = frameItem2.mFramebuffer;
            if (gLFramebuffer2 != null) {
                gLFramebuffer2.unInit();
                deleteSync(this.c);
                this.c.mFramebuffer = null;
                this.c = null;
            }
        }
        if (!this.d.isEmpty()) {
            for (FrameItem frameItem3 : this.d) {
                GLFramebuffer gLFramebuffer3 = frameItem3.mFramebuffer;
                if (gLFramebuffer3 != null) {
                    gLFramebuffer3.unInit();
                    deleteSync(frameItem3);
                    frameItem3.mFramebuffer = null;
                }
            }
        }
        this.d.clear();
        while (!this.e.isEmpty()) {
            FrameItem frameItem4 = (FrameItem) this.e.poll();
            if (frameItem4 != null) {
                GLFramebuffer gLFramebuffer4 = frameItem4.mFramebuffer;
                if (gLFramebuffer4 != null) {
                    gLFramebuffer4.unInit();
                    deleteSync(frameItem4);
                    frameItem4.mFramebuffer = null;
                }
            }
        }
        this.e.clear();
        this.f = false;
    }
}
