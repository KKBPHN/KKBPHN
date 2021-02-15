package com.xiaomi.idm.account;

import com.xiaomi.mi_connect_sdk.util.LogUtil;
import com.xiaomi.stat.d;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceTokenInfo extends TokenInfo {
    public static final String KEY_ID = "ServiceTokenInfo";
    private static final String TAG = "ServiceTokenInfo";
    String cUserId;
    String domain;
    String sid;
    String ssecurity;
    long timeDiff;
    String userId;

    public static ServiceTokenInfo buildFromJson(JSONObject jSONObject) {
        try {
            ServiceTokenInfo serviceTokenInfo = new ServiceTokenInfo();
            serviceTokenInfo.token = jSONObject.getString("token");
            serviceTokenInfo.userId = jSONObject.getString("userId");
            serviceTokenInfo.sid = jSONObject.getString(d.g);
            serviceTokenInfo.cUserId = jSONObject.getString("cUserId");
            serviceTokenInfo.ssecurity = jSONObject.getString("ssecurity");
            serviceTokenInfo.domain = jSONObject.getString("domain");
            serviceTokenInfo.timeDiff = jSONObject.getLong("timeDiff");
            return serviceTokenInfo;
        } catch (JSONException e) {
            LogUtil.e("ServiceTokenInfo", e.getMessage(), (Throwable) e);
            return null;
        }
    }

    private void setJsonValue(JSONObject jSONObject, String str, Object obj) {
        if (obj != null) {
            jSONObject.put(str, obj);
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof ServiceTokenInfo) {
            String str = this.token;
            if (str != null) {
                ServiceTokenInfo serviceTokenInfo = (ServiceTokenInfo) obj;
                if (str.equals(serviceTokenInfo.token)) {
                    String str2 = this.userId;
                    if (str2 != null && str2.equals(serviceTokenInfo.userId)) {
                        String str3 = this.sid;
                        if (str3 != null && str3.equals(serviceTokenInfo.sid)) {
                            String str4 = this.cUserId;
                            if (str4 != null && str4.equals(serviceTokenInfo.cUserId)) {
                                String str5 = this.ssecurity;
                                if (str5 != null && str5.equals(serviceTokenInfo.ssecurity)) {
                                    String str6 = this.domain;
                                    if (str6 != null && str6.equals(serviceTokenInfo.domain)) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public String getKeyId() {
        return "ServiceTokenInfo";
    }

    public JSONObject toJsonSub() {
        JSONObject jSONObject = new JSONObject();
        try {
            setJsonValue(jSONObject, "token", this.token);
            setJsonValue(jSONObject, d.g, this.sid);
            setJsonValue(jSONObject, "userId", this.userId);
            setJsonValue(jSONObject, "cUserId", this.cUserId);
            setJsonValue(jSONObject, "ssecurity", this.ssecurity);
            setJsonValue(jSONObject, "domain", this.domain);
            setJsonValue(jSONObject, "timeDiff", Long.valueOf(this.timeDiff));
        } catch (JSONException e) {
            LogUtil.e("ServiceTokenInfo", e.getMessage(), (Throwable) e);
        }
        return jSONObject;
    }
}
