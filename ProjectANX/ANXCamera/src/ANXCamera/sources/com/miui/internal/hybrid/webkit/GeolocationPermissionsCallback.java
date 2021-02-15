package com.miui.internal.hybrid.webkit;

import android.webkit.GeolocationPermissions;
import miui.hybrid.GeolocationPermissions.Callback;

public class GeolocationPermissionsCallback implements Callback {
    private GeolocationPermissions.Callback mCallback;

    public GeolocationPermissionsCallback(GeolocationPermissions.Callback callback) {
        this.mCallback = callback;
    }

    public void invoke(String str, boolean z, boolean z2) {
        this.mCallback.invoke(str, z, z2);
    }
}
