package com.xiaomi.inceptionplayer.datastruct;

public class VideoSize {
    public float video_height;
    public float video_width;

    public VideoSize(float f, float f2) {
        this.video_height = f;
        this.video_width = f2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("video_height=");
        sb.append(this.video_height);
        sb.append(",");
        sb.append("video_width=");
        sb.append(this.video_width);
        return sb.toString();
    }
}
