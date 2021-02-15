package com.xiaomi.idm.account;

import com.xiaomi.mi_connect_sdk.util.LogUtil;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class TokenInfo {
    private static final String TAG = "TokenInfo";
    String token;

    public static TokenInfo buildFromJson(String str) {
        try {
            return buildFromJson(new JSONObject(str));
        } catch (JSONException e) {
            LogUtil.e(TAG, e.getMessage(), (Throwable) e);
            return null;
        }
    }

    public static TokenInfo buildFromJson(JSONObject jSONObject) {
        String str = ServiceTokenInfo.KEY_ID;
        try {
            if (jSONObject.has(str)) {
                return ServiceTokenInfo.buildFromJson(jSONObject.getJSONObject(str));
            }
        } catch (JSONException e) {
            LogUtil.e(TAG, e.getMessage(), (Throwable) e);
        }
        return null;
    }

    public abstract String getKeyId();

    public final String getToken() {
        return this.token;
    }

    public JSONObject toJson() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(getKeyId(), toJsonSub());
        } catch (JSONException e) {
            LogUtil.e(TAG, e.getMessage(), (Throwable) e);
        }
        return jSONObject;
    }

    public abstract JSONObject toJsonSub();
}
