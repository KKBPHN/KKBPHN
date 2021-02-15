package net.majorkernelpanic.streaming.video;

import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.Log;
import java.util.Iterator;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

public class VideoQuality {
    public static final VideoQuality DEFAULT_VIDEO_QUALITY = new VideoQuality(176, IjkMediaMeta.FF_PROFILE_H264_HIGH_444, 20, 500000);
    public static final String TAG = "VideoQuality";
    public int bitrate = 0;
    public int framerate = 0;
    public int resX = 0;
    public int resY = 0;

    public VideoQuality() {
    }

    public VideoQuality(int i, int i2) {
        this.resX = i;
        this.resY = i2;
    }

    public VideoQuality(int i, int i2, int i3, int i4) {
        this.framerate = i3;
        this.bitrate = i4;
        this.resX = i;
        this.resY = i2;
    }

    public static VideoQuality copyOf(VideoQuality videoQuality) {
        return new VideoQuality(videoQuality.resX, videoQuality.resY, videoQuality.framerate, videoQuality.bitrate);
    }

    public static VideoQuality determineClosestSupportedResolution(Parameters parameters, VideoQuality videoQuality) {
        String str;
        VideoQuality copyOf = copyOf(videoQuality);
        Iterator it = parameters.getSupportedPreviewSizes().iterator();
        int i = Integer.MAX_VALUE;
        String str2 = "Supported resolutions: ";
        while (true) {
            str = "x";
            if (!it.hasNext()) {
                break;
            }
            Size size = (Size) it.next();
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(size.width);
            sb.append(str);
            sb.append(size.height);
            sb.append(it.hasNext() ? ", " : "");
            str2 = sb.toString();
            int abs = Math.abs(videoQuality.resX - size.width);
            if (abs < i) {
                copyOf.resX = size.width;
                copyOf.resY = size.height;
                i = abs;
            }
        }
        String str3 = TAG;
        Log.v(str3, str2);
        if (!(videoQuality.resX == copyOf.resX && videoQuality.resY == copyOf.resY)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Resolution modified: ");
            sb2.append(videoQuality.resX);
            sb2.append(str);
            sb2.append(videoQuality.resY);
            sb2.append("->");
            sb2.append(copyOf.resX);
            sb2.append(str);
            sb2.append(copyOf.resY);
            Log.v(str3, sb2.toString());
        }
        return copyOf;
    }

    public static int[] determineMaximumSupportedFramerate(Parameters parameters) {
        int[] iArr = {0, 0};
        Iterator it = parameters.getSupportedPreviewFpsRange().iterator();
        String str = "Supported frame rates: ";
        while (it.hasNext()) {
            int[] iArr2 = (int[]) it.next();
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(iArr2[0] / 1000);
            sb.append("-");
            sb.append(iArr2[1] / 1000);
            sb.append("fps");
            sb.append(it.hasNext() ? ", " : "");
            String sb2 = sb.toString();
            if (iArr2[1] > iArr[1] || (iArr2[0] > iArr[0] && iArr2[1] == iArr[1])) {
                iArr = iArr2;
            }
            str = sb2;
        }
        Log.v(TAG, str);
        return iArr;
    }

    public static VideoQuality parseQuality(String str) {
        VideoQuality copyOf = copyOf(DEFAULT_VIDEO_QUALITY);
        if (str != null) {
            String[] split = str.split("-");
            try {
                copyOf.bitrate = Integer.parseInt(split[0]) * 1000;
                copyOf.framerate = Integer.parseInt(split[1]);
                copyOf.resX = Integer.parseInt(split[2]);
                copyOf.resY = Integer.parseInt(split[3]);
            } catch (IndexOutOfBoundsException unused) {
            }
        }
        return copyOf;
    }

    public boolean equals(VideoQuality videoQuality) {
        boolean z = false;
        if (videoQuality == null) {
            return false;
        }
        if (videoQuality.resX == this.resX && videoQuality.resY == this.resY && videoQuality.framerate == this.framerate && videoQuality.bitrate == this.bitrate) {
            z = true;
        }
        return z;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.resX);
        sb.append("x");
        sb.append(this.resY);
        sb.append(" px, ");
        sb.append(this.framerate);
        sb.append(" fps, ");
        sb.append(this.bitrate / 1000);
        sb.append(" kbps");
        return sb.toString();
    }
}
