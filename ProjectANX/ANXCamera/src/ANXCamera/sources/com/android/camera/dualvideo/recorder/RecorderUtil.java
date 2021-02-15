package com.android.camera.dualvideo.recorder;

import android.content.ContentValues;
import android.net.Uri;
import com.android.camera.log.Log;
import com.android.camera.storage.ImageSaver;
import java.util.HashMap;

public class RecorderUtil {
    public static final HashMap HEVC_VIDEO_ENCODER_BITRATE = new HashMap();
    private static final String TAG = "RecorderUtil";

    static {
        HEVC_VIDEO_ENCODER_BITRATE.put("3840x2160:30", Integer.valueOf(38500000));
        HEVC_VIDEO_ENCODER_BITRATE.put("1920x1080:30", Integer.valueOf(15400000));
        HEVC_VIDEO_ENCODER_BITRATE.put("1280x720:30", Integer.valueOf(10780000));
        HEVC_VIDEO_ENCODER_BITRATE.put("720x480:30", Integer.valueOf(1379840));
    }

    public static Uri saveVideo(ImageSaver imageSaver, String str, ContentValues contentValues, boolean z, boolean z2) {
        String str2 = TAG;
        if (imageSaver != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("saveVideo: path=");
            sb.append(str);
            sb.append(" isFinal=");
            sb.append(z);
            Log.w(str2, sb.toString());
            contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
            if (z2) {
                return imageSaver.addVideoSync(str, contentValues, false);
            }
            imageSaver.addVideo(str, contentValues, z);
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("saveVideo: failed to save ");
            sb2.append(str);
            Log.w(str2, sb2.toString());
        }
        return null;
    }
}
