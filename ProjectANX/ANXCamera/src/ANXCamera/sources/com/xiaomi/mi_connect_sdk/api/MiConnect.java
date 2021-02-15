package com.xiaomi.mi_connect_sdk.api;

import android.content.Context;

public class MiConnect {
    private static final String TAG = "MiConnect";

    public static void delApp(MiApp miApp, int i) {
        ((InnerMiApp) miApp).destroy(i);
    }

    public static MiApp newApp(Context context, MiAppCallback miAppCallback, int i) {
        return new DefaultMiApp(context, miAppCallback, i);
    }
}
