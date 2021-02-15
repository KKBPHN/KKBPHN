package com.miui.internal.hybrid;

import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonConfigParser implements ConfigParser {
    private static final String KEY_CONTENT = "content";
    private static final String KEY_FEATURES = "features";
    private static final String KEY_NAME = "name";
    private static final String KEY_ORIGIN = "origin";
    private static final String KEY_PARAMS = "params";
    private static final String KEY_PERMISSIONS = "permissions";
    private static final String KEY_SIGNATURE = "signature";
    private static final String KEY_SUBDOMAINS = "subdomains";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_VALUE = "value";
    private static final String KEY_VENDOR = "vendor";
    private JSONObject mJson;

    private JsonConfigParser(JSONObject jSONObject) {
        this.mJson = jSONObject;
    }

    private Config buildCompleteConfig(Config config, Map map) {
        return config;
    }

    public static JsonConfigParser createFromJSONObject(JSONObject jSONObject) {
        return new JsonConfigParser(jSONObject);
    }

    public static JsonConfigParser createFromString(String str) {
        try {
            return createFromJSONObject(new JSONObject(str));
        } catch (JSONException e) {
            throw new HybridException(201, e.getMessage());
        }
    }

    private void parseFeatures(Config config, JSONObject jSONObject) {
        JSONArray optJSONArray = jSONObject.optJSONArray(KEY_FEATURES);
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                Feature feature = new Feature();
                String str = KEY_NAME;
                feature.setName(jSONObject2.getString(str));
                JSONArray optJSONArray2 = jSONObject2.optJSONArray(KEY_PARAMS);
                if (optJSONArray2 != null) {
                    for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                        JSONObject jSONObject3 = optJSONArray2.getJSONObject(i2);
                        feature.setParam(jSONObject3.getString(str), jSONObject3.getString("value"));
                    }
                }
                config.addFeature(feature);
            }
        }
    }

    private void parsePermissions(Config config, JSONObject jSONObject) {
        JSONArray optJSONArray = jSONObject.optJSONArray(KEY_PERMISSIONS);
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                Permission permission = new Permission();
                permission.setUri(jSONObject2.getString("origin"));
                permission.setApplySubdomains(jSONObject2.optBoolean(KEY_SUBDOMAINS));
                config.addPermission(permission);
            }
        }
    }

    public Config parse(Map map) {
        Config config = new Config();
        try {
            JSONObject jSONObject = this.mJson;
            Security security = new Security();
            security.setSignature(jSONObject.getString(KEY_SIGNATURE));
            security.setTimestamp(jSONObject.getLong(KEY_TIMESTAMP));
            config.setSecurity(security);
            config.setVendor(jSONObject.getString("vendor"));
            config.setContent(jSONObject.optString("content"));
            parseFeatures(config, jSONObject);
            parsePermissions(config, jSONObject);
            buildCompleteConfig(config, map);
            return config;
        } catch (JSONException e) {
            throw new HybridException(201, e.getMessage());
        }
    }
}
