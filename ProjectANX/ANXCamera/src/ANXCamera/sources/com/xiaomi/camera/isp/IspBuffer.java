package com.xiaomi.camera.isp;

import android.media.Image;

public class IspBuffer {
    public Image bufferImage;
    public long streamId;
    public Image tuningImage;

    public IspBuffer(long j, Image image, Image image2) {
        this.streamId = j;
        this.bufferImage = image;
        this.tuningImage = image2;
    }
}
