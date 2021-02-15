package com.miui.internal.variable.v16;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

public class Android_App_PreferenceActivity_class extends com.miui.internal.variable.Android_App_PreferenceActivity_class {
    private static final String ID_HEADERS = "headers";
    private static final String ID_PREFS_FRAME = "prefs_frame";
    private static final String PACKAGE_ANDROID = "android";
    private static final String TYPE_ID = "id";

    public void onCreate(Activity activity, Bundle bundle) {
        Resources resources = activity.getResources();
        String str = PACKAGE_ANDROID;
        String str2 = "id";
        int identifier = resources.getIdentifier(ID_HEADERS, str2, str);
        if (identifier > 0) {
            View findViewById = activity.findViewById(identifier);
            if (findViewById != null) {
                findViewById.setBackground(null);
                findViewById.setPadding(0, 0, 0, 0);
            }
        }
        int identifier2 = activity.getResources().getIdentifier(ID_PREFS_FRAME, str2, str);
        if (identifier2 > 0) {
            View findViewById2 = activity.findViewById(identifier2);
            if (findViewById2 != null) {
                findViewById2.setBackground(null);
                findViewById2.setPadding(0, 0, 0, 0);
            }
        }
    }
}
