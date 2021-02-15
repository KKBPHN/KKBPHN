package com.ss.android.ttve.monitor;

import android.content.Context;
import android.util.Log;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import org.json.JSONObject;

@RestrictTo({Scope.LIBRARY})
public class MonitorCompat {
    private static final String TAG = "MonitorCompat";

    public static String getHeaderInfo(String str) {
        Log.d(TAG, "Montiro lib is disabled");
        return null;
    }

    public static void init(Context context, String str, String str2, String str3) {
        Log.d(TAG, "Montiro lib is disabled");
    }

    public static void monitorStatusAndDuration(String str, int i, JSONObject jSONObject) {
        Log.d(TAG, "Montiro lib is disabled");
    }

    public static void monitorStatusAndDuration(String str, int i, JSONObject jSONObject, JSONObject jSONObject2) {
        Log.d(TAG, "Montiro lib is disabled");
    }

    public static void setHeaderInfo(String str, String str2) {
        Log.d(TAG, "Montiro lib is disabled");
    }

    public static void setServerLocation(int i) {
        Log.d(TAG, "Montiro lib is disabled");
    }
}
