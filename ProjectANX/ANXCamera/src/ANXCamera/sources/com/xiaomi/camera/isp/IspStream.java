package com.xiaomi.camera.isp;

public class IspStream {
    public int format;
    public int height;
    public long streamId;
    public int[] stride;
    public int width;

    public IspStream(long j, int i, int i2, int[] iArr, int i3) {
        this.streamId = j;
        this.width = i;
        this.height = i2;
        this.stride = iArr;
        this.format = i3;
    }
}
