package com.miui.internal.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Process;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

public class PermissionUtils {
    private static final String TAG = "PermissionUtils";

    private static boolean checkPermission(Context context, String str) {
        return context.checkPermission(str, Process.myPid(), Process.myUid()) == 0;
    }

    private static String[] getUngrantedPermissions(Context context, String[] strArr) {
        ArrayList arrayList = new ArrayList();
        for (String str : strArr) {
            if (!checkPermission(context, str)) {
                arrayList.add(str);
            }
        }
        if (arrayList.isEmpty()) {
            return new String[0];
        }
        String[] strArr2 = new String[arrayList.size()];
        arrayList.toArray(strArr2);
        return strArr2;
    }

    public static void requestPermissions(Context context, String[] strArr) {
        if (VERSION.SDK_INT >= 23) {
            String[] ungrantedPermissions = getUngrantedPermissions(context, strArr);
            if (ungrantedPermissions.length > 0) {
                startPermissionRequest(context, ungrantedPermissions);
            }
        }
    }

    private static void startPermissionRequest(Context context, String[] strArr) {
        StringBuilder sb;
        String str = "Fail to request permissions: ";
        String str2 = TAG;
        try {
            Intent intent = (Intent) PackageManager.class.getDeclaredMethod("buildRequestPermissionsIntent", new Class[]{String[].class}).invoke(context.getPackageManager(), new Object[]{strArr});
            intent.setFlags(268435456);
            context.startActivity(intent);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Success to request permissions: ");
            sb2.append(Arrays.toString(strArr));
            Log.i(str2, sb2.toString());
            return;
        } catch (NoSuchMethodException e) {
            e = e;
            sb = new StringBuilder();
        } catch (InvocationTargetException e2) {
            e = e2;
            sb = new StringBuilder();
        } catch (IllegalAccessException e3) {
            e = e3;
            sb = new StringBuilder();
        }
        sb.append(str);
        sb.append(Arrays.toString(strArr));
        Log.e(str2, sb.toString(), e);
    }
}
