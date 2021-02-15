package com.xiaomi.camera.rcs;

import android.os.Bundle;
import android.util.Size;
import android.view.Surface;
import org.json.JSONException;
import org.json.JSONObject;

public class RemoteControlContract {
    public static final String CUSTOM_CALLBACK_CAPTURING_MODE_CHANGED = "com.xiaomi.wearable.onCapturingModeChanged";
    public static final String CUSTOM_CALLBACK_GROUP_OWNER_DIED = "com.xiaomi.wearable.onGroupOwnerDied";
    public static final int DEFAULT_STREAMING_SERVER_PORT = 8086;
    private static final String PAYLOAD_KEY_AUTHORISATION_REQUIRED = "authorisation_required";
    private static final String PAYLOAD_KEY_CODEC_INPUT_SURFACE = "codec_surface";
    private static final String PAYLOAD_KEY_CURRENT_CAPTURING_MODE = "curr_mode";
    private static final String PAYLOAD_KEY_ERROR_CODE = "errno";
    private static final String PAYLOAD_KEY_PREVIOUS_CAPTURING_MODE = "prev_mode";
    private static final String PAYLOAD_KEY_STREAMING_SERVER_PORT = "rtsp_port";
    private static final String PAYLOAD_KEY_STREAMING_SESSION_ID = "streaming_session_id";
    private static final String PAYLOAD_KEY_STREAMING_VIDEO_RESOLUTION_X = "video_res_x";
    private static final String PAYLOAD_KEY_STREAMING_VIDEO_RESOLUTION_Y = "video_res_y";
    public static final int UNSPECIFIED_CAPTURING_MODE = 160;

    private RemoteControlContract() {
    }

    public static Surface getCodecInputSurface(Bundle bundle) {
        return (Surface) bundle.getParcelable(PAYLOAD_KEY_CODEC_INPUT_SURFACE);
    }

    public static int getCurrentCapturingMode(Bundle bundle) {
        return bundle.getInt(PAYLOAD_KEY_CURRENT_CAPTURING_MODE, 160);
    }

    public static int getErrorCode(Bundle bundle) {
        return bundle.getInt(PAYLOAD_KEY_ERROR_CODE, 100);
    }

    public static int getPreviousCapturingMode(Bundle bundle) {
        return bundle.getInt(PAYLOAD_KEY_PREVIOUS_CAPTURING_MODE, 160);
    }

    public static int getStreamingServerPort(Bundle bundle) {
        return bundle.getInt("rtsp_port", 8086);
    }

    public static String getStreamingSessionId(Bundle bundle) {
        return bundle.getString(PAYLOAD_KEY_STREAMING_SESSION_ID, null);
    }

    public static Size getVideoStreamSize(Bundle bundle) {
        return new Size(bundle.getInt(PAYLOAD_KEY_STREAMING_VIDEO_RESOLUTION_X, -1), bundle.getInt(PAYLOAD_KEY_STREAMING_VIDEO_RESOLUTION_Y, -1));
    }

    public static boolean isAuthorisationRequired(Bundle bundle) {
        return bundle.getBoolean(PAYLOAD_KEY_AUTHORISATION_REQUIRED, false);
    }

    public static String jsonify(Bundle bundle) {
        if (bundle == null) {
            return "null";
        }
        JSONObject jSONObject = new JSONObject();
        for (String str : bundle.keySet()) {
            try {
                jSONObject.put(str, JSONObject.wrap(bundle.get(str)));
            } catch (JSONException unused) {
            }
        }
        return jSONObject.toString();
    }

    public static void setAuthorisationRequired(Bundle bundle, boolean z) {
        bundle.putBoolean(PAYLOAD_KEY_AUTHORISATION_REQUIRED, z);
    }

    public static void setCodecInputSurface(Bundle bundle, Surface surface) {
        bundle.putParcelable(PAYLOAD_KEY_CODEC_INPUT_SURFACE, surface);
    }

    public static void setCurrentCapturingMode(Bundle bundle, int i) {
        bundle.putInt(PAYLOAD_KEY_CURRENT_CAPTURING_MODE, i);
    }

    public static void setErrorCode(Bundle bundle, int i) {
        bundle.putInt(PAYLOAD_KEY_ERROR_CODE, i);
    }

    public static void setPreviousCapturingMode(Bundle bundle, int i) {
        bundle.putInt(PAYLOAD_KEY_PREVIOUS_CAPTURING_MODE, i);
    }

    public static void setStreamingServerPort(Bundle bundle, int i) {
        bundle.putInt("rtsp_port", i);
    }

    public static void setStreamingSessionId(Bundle bundle, String str) {
        bundle.putString(PAYLOAD_KEY_STREAMING_SESSION_ID, str);
    }

    public static void setVideoStreamSize(Bundle bundle, int i, int i2) {
        bundle.putInt(PAYLOAD_KEY_STREAMING_VIDEO_RESOLUTION_X, i);
        bundle.putInt(PAYLOAD_KEY_STREAMING_VIDEO_RESOLUTION_Y, i2);
    }
}
