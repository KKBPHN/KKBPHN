package com.miui.internal.util;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import java.io.File;
import miui.core.Manifest;
import miui.core.ManifestParser;
import miui.util.AppConstants;

public class PackageConstants extends AppConstants {
    public static final int CURRENT_SDK_LEVEL = retrieveSdkLevel();
    public static final String LOG_TAG = "miuisdk";
    public static final String ORIGINAL_PACKAGE_NAME = "miui";
    public static final String PACKAGE_NAME = "com.miui.core";
    public static final String RESOURCE_PATH = PackageHelper.getApkPath(null, "com.miui.core", "miui");
    private static final String SDK_BASE_FOLDER_NAME = "miuisdk";
    public static final int SDK_STATUS_INITIALIZED = 2;
    public static final int SDK_STATUS_LOADED = 1;
    public static final int SDK_STATUS_NOT_EXISTS = 0;
    public static final int SDK_STATUS_STARTED = 3;
    public static volatile Application sApplication;
    public static Application sInitializingApplication;
    public static int sSdkStatus;

    private PackageConstants() {
    }

    public static File getSdkBaseFolder(Context context) {
        return new File(context.getFilesDir(), "miuisdk");
    }

    private static int retrieveSdkLevel() {
        int i = 0;
        Manifest parse = ManifestParser.createFromResources(ResourcesUtils.createResources(RESOURCE_PATH), "miui", null).parse(null);
        if (parse != null) {
            i = parse.getModule().getLevel();
            StringBuilder sb = new StringBuilder();
            sb.append("current sdk level is ");
            sb.append(i);
            Log.d("miuisdk", sb.toString());
        }
        if (i != 0) {
            return i;
        }
        throw new IllegalStateException("cannot retrieve sdk level");
    }
}
