package com.android.camera;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.android.camera.log.Log;
import java.util.Locale;

public class ActivityLauncher {
    public static final String TAG = "ActivityLauncher";
    private static String URL_MIUI_PRIVACY_POLICY = "https://privacy.mi.com/all/";

    private ActivityLauncher() {
    }

    public static void launchPopupCameraSetting(Context context) {
        try {
            context.startActivity(new Intent("android.settings.POPUP_CAMERA_SETTINGS"));
        } catch (Exception unused) {
            Log.w(TAG, "launchPopupCameraSetting error");
        }
    }

    public static void launchPrivacyPolicyWebpage(Activity activity) {
        String str;
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        String country = locale.getCountry();
        if (TextUtils.isEmpty(language) || TextUtils.isEmpty(country)) {
            str = URL_MIUI_PRIVACY_POLICY;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(URL_MIUI_PRIVACY_POLICY);
            sb.append("%s_%s");
            str = String.format(sb.toString(), new Object[]{language, country});
        }
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            activity.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            Intent intent2 = new Intent(activity, WebViewActivity.class);
            intent2.putExtra(CameraIntentManager.EXTRA_CTA_WEBVIEW_LINK, str);
            activity.startActivity(intent2);
        }
    }
}
