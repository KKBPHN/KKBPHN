package com.ss.android.medialib.log;

import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.ss.android.medialib.common.LogUtil;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class BaseMonitor {
    private static final String TAG = "BaseMonitor";
    public static IMonitor sMonitor;

    public static boolean monitorVELog(IMonitor iMonitor, String str, String str2, Map map) {
        String str3;
        String str4;
        if (iMonitor == null) {
            str3 = TAG;
            str4 = "No monitor callback, return";
        } else {
            JSONObject jSONObject = new JSONObject();
            try {
                for (String str5 : map.keySet()) {
                    jSONObject.put(str5, map.get(str5));
                    if (!TextUtils.isEmpty(str2)) {
                        jSONObject.put(NotificationCompat.CATEGORY_SERVICE, str2);
                    }
                }
                if (iMonitor != null) {
                    iMonitor.monitorLog(str, jSONObject);
                }
                return true;
            } catch (JSONException unused) {
                str3 = TAG;
                str4 = "No monitor callback, skip";
            }
        }
        LogUtil.d(str3, str4);
        return false;
    }

    public static boolean monitorVELog(String str, String str2, float f) {
        return monitorVELog(str, str2, String.valueOf(f));
    }

    public static boolean monitorVELog(String str, String str2, long j) {
        return monitorVELog(str, str2, String.valueOf(j));
    }

    public static boolean monitorVELog(String str, String str2, String str3) {
        HashMap hashMap = new HashMap();
        hashMap.put(str2, str3);
        return monitorVELog(str, str2, (Map) hashMap);
    }

    public static boolean monitorVELog(String str, String str2, Map map) {
        return monitorVELog(sMonitor, str, str2, map);
    }

    public static void register(IMonitor iMonitor) {
        sMonitor = iMonitor;
    }

    public static void unRegister() {
        sMonitor = null;
    }
}
