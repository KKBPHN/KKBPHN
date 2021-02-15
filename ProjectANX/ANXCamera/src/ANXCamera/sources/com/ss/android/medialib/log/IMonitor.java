package com.ss.android.medialib.log;

import androidx.annotation.Keep;
import org.json.JSONObject;

@Keep
public interface IMonitor {
    void monitorLog(String str, JSONObject jSONObject);
}
