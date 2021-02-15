package com.android.camera.permission;

import android.app.Activity;
import android.app.Fragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.android.camera.CameraAppImpl;
import com.android.camera.R;
import com.android.camera.log.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import miui.os.Build;

public class PermissionManager {
    public static final int CAM_REQUEST_CODE_ASK_LAUNCH_PERMISSIONS = 102;
    public static final int CAM_REQUEST_CODE_ASK_LOCATION_PERMISSIONS = 101;
    public static final int CAM_REQUEST_CODE_ASK_RUNTIME_PERMISSIONS = 100;
    private static final String TAG = "PermissionManager";
    private static List mLaunchPermissionList = new ArrayList();
    private static List sLocationPermissionList = new ArrayList();
    private static List sRuntimePermissions = new ArrayList();
    private static List sStoragePermissionList = new ArrayList();

    static {
        sLocationPermissionList.add("android.permission.ACCESS_FINE_LOCATION");
        sLocationPermissionList.add("android.permission.ACCESS_COARSE_LOCATION");
        sStoragePermissionList.add("android.permission.READ_EXTERNAL_STORAGE");
        sStoragePermissionList.add("android.permission.WRITE_EXTERNAL_STORAGE");
        mLaunchPermissionList.add("android.permission.CAMERA");
        mLaunchPermissionList.add("android.permission.RECORD_AUDIO");
        mLaunchPermissionList.addAll(sStoragePermissionList);
        sRuntimePermissions.addAll(mLaunchPermissionList);
        sRuntimePermissions.addAll(sLocationPermissionList);
    }

    public static boolean checkCameraLaunchPermissions() {
        if (getNeedCheckPermissionList(mLaunchPermissionList).size() > 0) {
            return false;
        }
        Log.i(TAG, "CheckCameraPermissions(), all on");
        return true;
    }

    public static boolean checkCameraLocationPermissions() {
        if (getNeedCheckPermissionList(sLocationPermissionList).size() > 0) {
            return false;
        }
        Log.i(TAG, "checkCameraLocationPermissions(), all on");
        return true;
    }

    public static boolean checkStoragePermissions() {
        if (getNeedCheckPermissionList(sStoragePermissionList).size() > 0) {
            return false;
        }
        Log.i(TAG, "checkStoragePermissions on");
        return true;
    }

    private static List getNeedCheckPermissionList(List list) {
        if (list.size() <= 0) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (true) {
            boolean hasNext = it.hasNext();
            String str = TAG;
            if (hasNext) {
                String str2 = (String) it.next();
                if (ContextCompat.checkSelfPermission(CameraAppImpl.getAndroidContext(), str2) != 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("getNeedCheckPermissionList() permission =");
                    sb.append(str2);
                    Log.i(str, sb.toString());
                    arrayList.add(str2);
                    if (!Build.IS_INTERNATIONAL_BUILD && (str2.equals("android.permission.ACCESS_COARSE_LOCATION") || str2.equals("android.permission.ACCESS_FINE_LOCATION"))) {
                        arrayList.add(CameraAppImpl.getAndroidContext().getString(R.string.location_permission_des));
                    }
                }
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("getNeedCheckPermissionList() listSize =");
                sb2.append(arrayList.size());
                Log.i(str, sb2.toString());
                return arrayList;
            }
        }
    }

    public static boolean isCameraLaunchPermissionsResultReady(String[] strArr, int[] iArr) {
        HashMap hashMap = new HashMap();
        Integer valueOf = Integer.valueOf(0);
        String str = "android.permission.CAMERA";
        hashMap.put(str, valueOf);
        String str2 = "android.permission.RECORD_AUDIO";
        hashMap.put(str2, valueOf);
        String str3 = "android.permission.WRITE_EXTERNAL_STORAGE";
        hashMap.put(str3, valueOf);
        for (int i = 0; i < strArr.length; i++) {
            hashMap.put(strArr[i], Integer.valueOf(iArr[i]));
        }
        return ((Integer) hashMap.get(str)).intValue() == 0 && ((Integer) hashMap.get(str2)).intValue() == 0 && ((Integer) hashMap.get(str3)).intValue() == 0;
    }

    public static boolean isCameraLocationPermissionsResultReady(String[] strArr, int[] iArr) {
        HashMap hashMap = new HashMap();
        Integer valueOf = Integer.valueOf(0);
        String str = "android.permission.ACCESS_COARSE_LOCATION";
        hashMap.put(str, valueOf);
        String str2 = "android.permission.ACCESS_FINE_LOCATION";
        hashMap.put(str2, valueOf);
        for (int i = 0; i < strArr.length; i++) {
            hashMap.put(strArr[i], Integer.valueOf(iArr[i]));
        }
        return ((Integer) hashMap.get(str)).intValue() == 0 && ((Integer) hashMap.get(str2)).intValue() == 0;
    }

    public static boolean isContainLocationPermissions(String[] strArr) {
        if (strArr != null && strArr.length >= 1) {
            for (String contains : strArr) {
                if (sLocationPermissionList.contains(contains)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean requestCameraLocationPermissionsByFragment(Fragment fragment) {
        List needCheckPermissionList = getNeedCheckPermissionList(sLocationPermissionList);
        int size = needCheckPermissionList.size();
        String str = TAG;
        if (size > 0) {
            Log.i(str, "requestCameraLocationPermissions(), user check");
            fragment.requestPermissions((String[]) needCheckPermissionList.toArray(new String[needCheckPermissionList.size()]), 101);
            return false;
        }
        Log.i(str, "requestCameraLocationPermissions(), all on");
        return true;
    }

    public static boolean requestCameraLocationPermissionsByFragment(androidx.fragment.app.Fragment fragment) {
        List needCheckPermissionList = getNeedCheckPermissionList(sLocationPermissionList);
        int size = needCheckPermissionList.size();
        String str = TAG;
        if (size > 0) {
            Log.i(str, "requestCameraLocationPermissions(), user check");
            fragment.requestPermissions((String[]) needCheckPermissionList.toArray(new String[needCheckPermissionList.size()]), 101);
            return false;
        }
        Log.i(str, "requestCameraLocationPermissions(), all on");
        return true;
    }

    public static boolean requestCameraPermissions(Activity activity, int i) {
        List list;
        List list2;
        switch (i) {
            case 100:
                list2 = sRuntimePermissions;
                break;
            case 101:
                list2 = sLocationPermissionList;
                break;
            case 102:
                list2 = mLaunchPermissionList;
                break;
            default:
                list = null;
                break;
        }
        list = getNeedCheckPermissionList(list2);
        String str = TAG;
        if (list == null || list.size() <= 0) {
            Log.i(str, "requestCameraPermissions(), all on");
            return true;
        }
        Log.i(str, "requestCameraPermissions(), user check");
        String[] strArr = (String[]) list.toArray(new String[list.size()]);
        int i2 = 101;
        if (i != 101) {
            i2 = 100;
        }
        ActivityCompat.requestPermissions(activity, strArr, i2);
        return false;
    }
}
