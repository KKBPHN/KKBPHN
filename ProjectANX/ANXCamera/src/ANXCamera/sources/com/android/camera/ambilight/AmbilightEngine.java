package com.android.camera.ambilight;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import com.android.camera.log.Log;
import java.nio.ByteBuffer;

public class AmbilightEngine {
    public static final int FLIP_HORIZONTAL = 1;
    public static final int FLIP_VERTICAL = 2;
    public static final int SCENE_MODE_CROWD_MOVING = 4;
    public static final int SCENE_MODE_LIGHT_TRACK = 2;
    public static final int SCENE_MODE_MAGIC_STAR = 5;
    public static final int SCENE_MODE_SILKY_WATER = 1;
    public static final int SCENE_MODE_STAR_TRACK = 3;
    public static final int SCENE_MODE_TRAFFIC_LIGHT = 0;
    private static final String TAG = "AmbilightEngine";

    public @interface AmbilightFlipMode {
    }

    public @interface AmbilightSceneMode {
    }

    static {
        try {
            String str = C0124O00000oO.isMTKPlatform() ? "_mtk" : "_qcom";
            StringBuilder sb = new StringBuilder();
            sb.append("camera_ambilight_utils");
            sb.append(str);
            System.loadLibrary(sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("camera_ambilight_ambt");
            sb2.append(str);
            System.loadLibrary(sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append("camera_mialgo_ambilight");
            sb3.append(str);
            System.loadLibrary(sb3.toString());
        } catch (UnsatisfiedLinkError e) {
            String str2 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("can't loadLibrary, ");
            sb4.append(e.getMessage());
            Log.e(str2, sb4.toString());
        }
    }

    public final native void destroy();

    public final native int frameProc();

    public final native int getEngineStatus();

    public final native int getPreviewHeight();

    public final native int getPreviewWidth();

    public final native void init(@AmbilightSceneMode int i, int i2, int i3, int i4);

    public final native void initData(int i);

    public final native int postProc();

    public final native void prepare(ByteBuffer byteBuffer, int i);

    public final native int prepareData(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3);

    public final native void setFlipMode(@AmbilightFlipMode int i);

    public final native void setZoomRoi(int i, int i2, int i3, int i4);

    public final native void updatePreview(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3, AmbilightRoi ambilightRoi);
}
