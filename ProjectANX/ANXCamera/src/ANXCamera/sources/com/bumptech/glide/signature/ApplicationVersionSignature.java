package com.bumptech.glide.signature;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import com.bumptech.glide.load.Key;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class ApplicationVersionSignature {
    private static final ConcurrentMap PACKAGE_NAME_TO_KEY = new ConcurrentHashMap();
    private static final String TAG = "AppVersionSignature";

    private ApplicationVersionSignature() {
    }

    /* JADX WARNING: type inference failed for: r3v1, types: [android.content.Context] */
    /* JADX WARNING: type inference failed for: r3v6 */
    /* JADX WARNING: type inference failed for: r3v7 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @Nullable
    private static PackageInfo getPackageInfo(@NonNull Context context) {
        try {
            r3 = context;
            r3 = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            r3 = r3;
            return r3;
        } catch (NameNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot resolve info for");
            sb.append(r3.getPackageName());
            Log.e(TAG, sb.toString(), e);
            return null;
        }
    }

    @NonNull
    private static String getVersionCode(@Nullable PackageInfo packageInfo) {
        return packageInfo != null ? String.valueOf(packageInfo.versionCode) : UUID.randomUUID().toString();
    }

    @NonNull
    public static Key obtain(@NonNull Context context) {
        String packageName = context.getPackageName();
        Key key = (Key) PACKAGE_NAME_TO_KEY.get(packageName);
        if (key != null) {
            return key;
        }
        Key obtainVersionSignature = obtainVersionSignature(context);
        Key key2 = (Key) PACKAGE_NAME_TO_KEY.putIfAbsent(packageName, obtainVersionSignature);
        return key2 == null ? obtainVersionSignature : key2;
    }

    @NonNull
    private static Key obtainVersionSignature(@NonNull Context context) {
        return new ObjectKey(getVersionCode(getPackageInfo(context)));
    }

    @VisibleForTesting
    static void reset() {
        PACKAGE_NAME_TO_KEY.clear();
    }
}
