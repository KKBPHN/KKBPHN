package com.iqiyi.android.qigsaw.core.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.lang.reflect.Field;

@RestrictTo({Scope.LIBRARY_GROUP})
public class SplitBaseInfoProvider {
    private static final String CLASS_QigsawConfig = ".QigsawConfig";
    private static final String DEFAULT_SPLIT_INFO_VERSION = "DEFAULT_SPLIT_INFO_VERSION";
    private static final String DEFAULT_SPLIT_INFO_VERSION_VALUE = "unknown_1.0.0";
    private static final String DEFAULT_VALUE = "unknown";
    private static final String DYNAMIC_FEATURES = "DYNAMIC_FEATURES";
    private static final String QIGSAW_ID = "QIGSAW_ID";
    private static final String QIGSAW_MODE = "QIGSAW_MODE";
    private static final String TAG = "SplitBaseInfoProvider";
    private static final String VERSION_NAME = "VERSION_NAME";
    private static String sPackageName;

    @NonNull
    public static String getDefaultSplitInfoVersion() {
        try {
            Field field = getQigsawConfigClass().getField(DEFAULT_SPLIT_INFO_VERSION);
            field.setAccessible(true);
            return (String) field.get(null);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException unused) {
            return DEFAULT_SPLIT_INFO_VERSION_VALUE;
        }
    }

    @Nullable
    public static String[] getDynamicFeatures() {
        try {
            Field field = getQigsawConfigClass().getField(DYNAMIC_FEATURES);
            field.setAccessible(true);
            return (String[]) field.get(null);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException unused) {
            return null;
        }
    }

    private static Class getQigsawConfigClass() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(sPackageName);
            sb.append(CLASS_QigsawConfig);
            return Class.forName(sb.toString());
        } catch (ClassNotFoundException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Qigsaw Warning: Can't find class ");
            sb2.append(sPackageName);
            sb2.append(".QigsawConfig.class!");
            SplitLog.w(TAG, sb2.toString(), new Object[0]);
            throw e;
        }
    }

    @NonNull
    public static String getQigsawId() {
        return "5.0.0.0";
    }

    @NonNull
    public static String getVersionName() {
        try {
            Field field = getQigsawConfigClass().getField(VERSION_NAME);
            field.setAccessible(true);
            return (String) field.get(null);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException unused) {
            return "unknown";
        }
    }

    public static boolean isQigsawMode() {
        try {
            Field field = getQigsawConfigClass().getField(QIGSAW_MODE);
            field.setAccessible(true);
            return ((Boolean) field.get(null)).booleanValue();
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException unused) {
            return false;
        }
    }

    public static void setPackageName(String str) {
        sPackageName = str;
    }
}
