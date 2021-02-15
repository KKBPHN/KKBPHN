package com.ss.android.ttve.mediacodec;

import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

class MediaCodecUtils {
    MediaCodecUtils() {
    }

    @RequiresApi(api = 18)
    @Nullable
    static CodecProfileLevel findBestMatchedProfile(@NonNull CodecCapabilities codecCapabilities, int i) {
        CodecProfileLevel[] codecProfileLevelArr;
        CodecProfileLevel codecProfileLevel = null;
        for (CodecProfileLevel codecProfileLevel2 : codecCapabilities.profileLevels) {
            int i2 = codecProfileLevel2.profile;
            if (i2 == i) {
                return codecProfileLevel2;
            }
            if (codecProfileLevel == null || codecProfileLevel.profile < i2) {
                codecProfileLevel = codecProfileLevel2;
            }
        }
        return codecProfileLevel;
    }
}
