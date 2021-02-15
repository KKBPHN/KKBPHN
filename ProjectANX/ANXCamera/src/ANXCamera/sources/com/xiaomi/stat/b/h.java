package com.xiaomi.stat.b;

import com.xiaomi.stat.d.k;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class h {
    static final String a = "key_update_time";
    static final String b = "get_all_config";
    static final String c = "mistats/v3";
    static final String d = "key_get";
    static final String e = "http://";
    static final String f = "https://";
    static final String g = "/";
    private static final String h = "RegionManagerHelper";

    /* access modifiers changed from: 0000 */
    public HashMap a(String str, JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject(str);
        String str2 = h;
        if (optJSONObject != null) {
            HashMap hashMap = new HashMap();
            StringBuilder sb = new StringBuilder();
            sb.append("parse the map contains key:");
            sb.append(str);
            k.b(str2, sb.toString());
            Iterator keys = optJSONObject.keys();
            while (keys.hasNext()) {
                try {
                    String str3 = (String) keys.next();
                    String string = optJSONObject.getString(str3);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("[region]:");
                    sb2.append(str3);
                    sb2.append("\n[domain]:");
                    sb2.append(string);
                    k.b(str2, sb2.toString());
                    hashMap.put(str3, string);
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
            return hashMap;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("can not find the specific key");
        sb3.append(str);
        k.d(str2, sb3.toString());
        return null;
    }

    /* access modifiers changed from: 0000 */
    public HashMap a(HashMap hashMap, HashMap hashMap2) {
        HashMap hashMap3 = new HashMap();
        if (hashMap2 != null) {
            hashMap3.putAll(hashMap2);
        }
        Set<String> keySet = hashMap.keySet();
        Set keySet2 = hashMap3.keySet();
        for (String str : keySet) {
            if (!keySet2.contains(str)) {
                hashMap3.put(str, hashMap.get(str));
            }
        }
        return hashMap3;
    }
}
