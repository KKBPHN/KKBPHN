package com.android.camera;

import android.content.Context;
import com.android.camera.log.Log;

public class ApplicationInit {
    private static final String TAG = "ApplicationInit";

    public static void init(Context context) {
        long currentTimeMillis = System.currentTimeMillis();
        Display.init(context);
        Util.initialize(context);
        StringBuilder sb = new StringBuilder();
        sb.append("<application init> consume time:");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        Log.d(TAG, sb.toString());
    }
}
