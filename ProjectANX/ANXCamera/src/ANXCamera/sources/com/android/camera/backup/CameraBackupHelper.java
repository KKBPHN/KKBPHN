package com.android.camera.backup;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.XmlResourceParser;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.aiwatermark.util.WatermarkConstant;
import com.android.camera.log.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import miui.cloud.backup.data.DataPackage;
import miui.cloud.backup.data.KeyStringSettingItem;
import miui.cloud.backup.data.PrefsBackupHelper.PrefEntry;
import org.xmlpull.v1.XmlPullParserException;

public class CameraBackupHelper {
    private static final String TAG = "CameraBackupHelper";

    private static String convertAntiBandingMode(String str) {
        if (str == null) {
            return null;
        }
        char c = 65535;
        switch (str.hashCode()) {
            case 109935:
                if (str.equals("off")) {
                    c = 0;
                    break;
                }
                break;
            case 1628397:
                if (str.equals("50hz")) {
                    c = 1;
                    break;
                }
                break;
            case 1658188:
                if (str.equals("60hz")) {
                    c = 2;
                    break;
                }
                break;
            case 3005871:
                if (str.equals("auto")) {
                    c = 3;
                    break;
                }
                break;
        }
        if (c == 0) {
            return String.valueOf(0);
        }
        if (c == 1) {
            return String.valueOf(1);
        }
        if (c == 2) {
            return String.valueOf(2);
        }
        if (c == 3) {
            return String.valueOf(3);
        }
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("unknown antibanding mode ");
        sb.append(str);
        Log.w(str2, sb.toString());
        return null;
    }

    private static String convertContrast(String str) {
        return null;
    }

    private static String convertExposureMode(String str) {
        if (str == null) {
            return null;
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -1364013995:
                if (str.equals("center")) {
                    c = 2;
                    break;
                }
                break;
            case -1200037852:
                if (str.equals("spot-metering")) {
                    c = 5;
                    break;
                }
                break;
            case -631448035:
                if (str.equals("average")) {
                    c = 0;
                    break;
                }
                break;
            case 3537154:
                if (str.equals("spot")) {
                    c = 4;
                    break;
                }
                break;
            case 1302812559:
                if (str.equals("center-weighted")) {
                    c = 3;
                    break;
                }
                break;
            case 2133765565:
                if (str.equals("frame-average")) {
                    c = 1;
                    break;
                }
                break;
        }
        if (c == 0 || c == 1) {
            return "0";
        }
        if (c == 2 || c == 3) {
            return "1";
        }
        if (c == 4 || c == 5) {
            return "2";
        }
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("unknown exposure mode ");
        sb.append(str);
        Log.w(str2, sb.toString());
        return null;
    }

    private static String convertSaturation(String str) {
        return null;
    }

    private static String convertSharpness(String str) {
        return null;
    }

    private static String convertVideoQuality(String str) {
        if (str == null) {
            return null;
        }
        char c = 65535;
        int hashCode = str.hashCode();
        String str2 = "8";
        String str3 = "6";
        String str4 = "5";
        String str5 = "4";
        if (hashCode != 56) {
            if (hashCode != 57) {
                switch (hashCode) {
                    case 52:
                        if (str.equals(str5)) {
                            c = 0;
                            break;
                        }
                        break;
                    case 53:
                        if (str.equals(str4)) {
                            c = 2;
                            break;
                        }
                        break;
                    case 54:
                        if (str.equals(str3)) {
                            c = 4;
                            break;
                        }
                        break;
                    default:
                        switch (hashCode) {
                            case 1567:
                                if (str.equals("10")) {
                                    c = 3;
                                    break;
                                }
                                break;
                            case 1568:
                                if (str.equals("11")) {
                                    c = 5;
                                    break;
                                }
                                break;
                            case 1569:
                                if (str.equals("12")) {
                                    c = 7;
                                    break;
                                }
                                break;
                        }
                }
            } else if (str.equals("9")) {
                c = 1;
            }
        } else if (str.equals(str2)) {
            c = 6;
        }
        switch (c) {
            case 0:
            case 1:
                return str5;
            case 2:
            case 3:
                return str4;
            case 4:
            case 5:
                return str3;
            case 6:
            case 7:
                return str2;
            default:
                String str6 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("unknown video quality ");
                sb.append(str);
                Log.w(str6, sb.toString());
                return null;
        }
    }

    private static String filterValue(String str, int i) {
        if (Util.isSupported((Object) str, (Object[]) CameraAppImpl.getAndroidContext().getResources().getStringArray(i))) {
            return str;
        }
        return null;
    }

    private static List getSettingsKeys() {
        ArrayList arrayList = new ArrayList();
        XmlResourceParser xml = CameraAppImpl.getAndroidContext().getResources().getXml(R.xml.camera_other_preferences_new);
        try {
            int eventType = xml.getEventType();
            while (eventType != 1) {
                if ((eventType == 2 || eventType == 3) && xml.getDepth() >= 3) {
                    String attributeValue = xml.getAttributeValue("http://schemas.android.com/apk/res/android", WatermarkConstant.ITEM_KEY);
                    if (attributeValue != null) {
                        arrayList.add(attributeValue);
                    }
                }
                eventType = xml.next();
            }
        } catch (IOException | XmlPullParserException e) {
            Log.e(TAG, e.getMessage(), (Throwable) e);
        }
        xml.close();
        return arrayList;
    }

    public static void restoreSettings(SharedPreferences sharedPreferences, DataPackage dataPackage, PrefEntry[] prefEntryArr, boolean z) {
        PrefEntry[] prefEntryArr2 = prefEntryArr;
        List settingsKeys = getSettingsKeys();
        Editor edit = sharedPreferences.edit();
        int length = prefEntryArr2.length;
        for (int i = 0; i < length; i++) {
            PrefEntry prefEntry = prefEntryArr2[i];
            if (settingsKeys.contains(prefEntry.getLocalKey()) && ((!z || !CameraSettings.isCameraSpecific(prefEntry.getLocalKey())) && (z || CameraSettings.isCameraSpecific(prefEntry.getLocalKey())))) {
                try {
                    try {
                        KeyStringSettingItem keyStringSettingItem = dataPackage.get(prefEntry.getCloudKey());
                        if (keyStringSettingItem != null) {
                            String localKey = prefEntry.getLocalKey();
                            char c = 65535;
                            switch (localKey.hashCode()) {
                                case -302378757:
                                    if (localKey.equals(CameraSettings.KEY_QC_SATURATION)) {
                                        c = 3;
                                        break;
                                    }
                                    break;
                                case -33912691:
                                    if (localKey.equals(CameraSettings.KEY_ANTIBANDING)) {
                                        c = 0;
                                        break;
                                    }
                                    break;
                                case 17536442:
                                    if (localKey.equals(CameraSettings.KEY_FRONT_MIRROR)) {
                                        c = 5;
                                        break;
                                    }
                                    break;
                                case 549001748:
                                    if (localKey.equals(CameraSettings.KEY_AUTOEXPOSURE)) {
                                        c = 1;
                                        break;
                                    }
                                    break;
                                case 936502456:
                                    if (localKey.equals(CameraSettings.KEY_QC_SHARPNESS)) {
                                        c = 4;
                                        break;
                                    }
                                    break;
                                case 1907727979:
                                    if (localKey.equals(CameraSettings.KEY_QC_CONTRAST)) {
                                        c = 2;
                                        break;
                                    }
                                    break;
                            }
                            String str = c != 0 ? c != 1 ? c != 2 ? c != 3 ? c != 4 ? c != 5 ? (String) keyStringSettingItem.getValue() : filterValue((String) keyStringSettingItem.getValue(), R.array.pref_front_mirror_entryvalues) : convertSharpness((String) keyStringSettingItem.getValue()) : convertSaturation((String) keyStringSettingItem.getValue()) : convertContrast((String) keyStringSettingItem.getValue()) : convertExposureMode((String) keyStringSettingItem.getValue()) : convertAntiBandingMode((String) keyStringSettingItem.getValue());
                            if (str != null) {
                                if (prefEntry.getValueClass() == Integer.class) {
                                    edit.putInt(prefEntry.getLocalKey(), Integer.parseInt(str));
                                } else if (prefEntry.getValueClass() == Long.class) {
                                    edit.putLong(prefEntry.getLocalKey(), Long.parseLong(str));
                                } else if (prefEntry.getValueClass() == Boolean.class) {
                                    edit.putBoolean(prefEntry.getLocalKey(), Boolean.parseBoolean(str));
                                } else if (prefEntry.getValueClass() == String.class) {
                                    edit.putString(prefEntry.getLocalKey(), str);
                                }
                            }
                        }
                    } catch (ClassCastException unused) {
                        String str2 = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("entry ");
                        sb.append(prefEntry.getCloudKey());
                        sb.append(" is not KeyStringSettingItem");
                        Log.e(str2, sb.toString());
                    }
                } catch (ClassCastException unused2) {
                    DataPackage dataPackage2 = dataPackage;
                    String str22 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("entry ");
                    sb2.append(prefEntry.getCloudKey());
                    sb2.append(" is not KeyStringSettingItem");
                    Log.e(str22, sb2.toString());
                }
            } else {
                DataPackage dataPackage3 = dataPackage;
            }
        }
        edit.commit();
    }
}
