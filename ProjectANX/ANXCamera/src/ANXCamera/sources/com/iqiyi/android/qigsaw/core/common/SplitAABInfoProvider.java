package com.iqiyi.android.qigsaw.core.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Build.VERSION;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RestrictTo({Scope.LIBRARY_GROUP})
public final class SplitAABInfoProvider {
    private static final String TAG = "SplitAABInfoProvider";
    private Context context;
    private final String packageName;

    public SplitAABInfoProvider(Context context2) {
        this.packageName = context2.getPackageName();
        this.context = context2;
    }

    private String cutSplitName(String str) {
        return str.split("\\.config\\.")[0];
    }

    private Set getFusedModules() {
        String str = TAG;
        HashSet hashSet = new HashSet();
        try {
            ApplicationInfo applicationInfo = this.context.getPackageManager().getApplicationInfo(this.packageName, 128);
            if (applicationInfo == null || applicationInfo.metaData == null) {
                SplitLog.d(str, "App has no applicationInfo or metaData", new Object[0]);
                return hashSet;
            }
            String string = applicationInfo.metaData.getString("shadow.bundletool.com.android.dynamic.apk.fused.modules");
            if (string == null || string.isEmpty()) {
                SplitLog.d(str, "App has no fused modules.", new Object[0]);
                return hashSet;
            }
            Collections.addAll(hashSet, string.split(",", -1));
            hashSet.remove("");
            return hashSet;
        } catch (Throwable th) {
            SplitLog.printErrStackTrace(str, th, "App is not found in PackageManager", new Object[0]);
            return hashSet;
        }
    }

    @RequiresApi(api = 21)
    private String[] getSplitInstallInfo() {
        String[] strArr = null;
        try {
            PackageInfo packageInfo = this.context.getPackageManager().getPackageInfo(this.packageName, 0);
            if (packageInfo != null) {
                strArr = packageInfo.splitNames;
            }
            return strArr;
        } catch (Throwable th) {
            SplitLog.printErrStackTrace(TAG, th, "App is not found in PackageManager", new Object[0]);
            return null;
        }
    }

    public Set getInstalledSplitsForAAB() {
        Set fusedModules = getFusedModules();
        if (VERSION.SDK_INT < 21) {
            return fusedModules;
        }
        String[] splitInstallInfo = getSplitInstallInfo();
        String str = TAG;
        if (splitInstallInfo == null) {
            SplitLog.d(str, "No splits are found or app cannot be found in package manager.", new Object[0]);
            return fusedModules;
        }
        String str2 = "Split names are: ";
        String arrays = Arrays.toString(splitInstallInfo);
        if (arrays.length() != 0) {
            str2 = str2.concat(arrays);
        }
        SplitLog.d(str, str2, new Object[0]);
        for (String str3 : splitInstallInfo) {
            if (!str3.startsWith("config.")) {
                fusedModules.add(cutSplitName(str3));
            }
        }
        return fusedModules;
    }
}
