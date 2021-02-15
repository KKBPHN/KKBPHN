package com.xiaomi.camera.rcs;

import android.os.Bundle;

public class RemoteControlExtension {
    public static final String CUSTOM_REQUEST_SET_CAPTURING_MODE = "com.xiaomi.camera.rcs.setCapturingMode";
    private static final String PAYLOAD_KEY_IS_GROUP_OWNER = "com.xiaomi.camera.rcs.isGroupOwner";

    public static boolean isGroupOwner(Bundle bundle) {
        return bundle.getBoolean(PAYLOAD_KEY_IS_GROUP_OWNER, false);
    }

    public static void setIsGroupOwner(Bundle bundle, boolean z) {
        bundle.putBoolean(PAYLOAD_KEY_IS_GROUP_OWNER, z);
    }
}
