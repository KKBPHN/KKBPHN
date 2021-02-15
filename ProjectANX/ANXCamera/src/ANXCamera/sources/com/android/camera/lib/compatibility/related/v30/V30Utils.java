package com.android.camera.lib.compatibility.related.v30;

import android.annotation.TargetApi;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest.Builder;

@TargetApi(30)
public class V30Utils {
    private static final String TAG = "V30Utils";

    public static void applyExtendSceneMode(Builder builder, int i) {
        throw new RuntimeException("could not find control_extended_scene_mode");
    }

    public static void applySessionKey(Builder builder, String str, Object obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("could not find tag ");
        sb.append(str);
        throw new RuntimeException(sb.toString());
    }

    public static void applyZoomRatio(Builder builder, float f) {
        throw new RuntimeException("could not apply control_zoom_ratio below Android R");
    }

    public static void setCameraAudioRestriction(CameraDevice cameraDevice, int i) {
    }
}
