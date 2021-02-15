package com.miui.internal.hybrid;

import java.util.TreeSet;

public class ConfigUtils {
    private static final String KEY_FEATURES = "features";
    private static final String KEY_NAME = "name";
    private static final String KEY_ORIGIN = "origin";
    private static final String KEY_PARAMS = "params";
    private static final String KEY_PERMISSIONS = "permissions";
    private static final String KEY_SUBDOMAINS = "subdomains";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_VALUE = "value";
    private static final String KEY_VENDOR = "vendor";

    private ConfigUtils() {
    }

    private static String buildFeature(Config config) {
        StringBuilder sb = new StringBuilder();
        TreeSet<String> treeSet = new TreeSet<>(config.getFeatures().keySet());
        if (treeSet.isEmpty()) {
            return "";
        }
        for (String str : treeSet) {
            sb.append("{");
            sb.append(KEY_NAME);
            String str2 = ":";
            sb.append(str2);
            String str3 = "\"";
            sb.append(str3);
            sb.append(str);
            sb.append(str3);
            String str4 = ",";
            sb.append(str4);
            sb.append(KEY_PARAMS);
            sb.append(str2);
            sb.append("[");
            sb.append(buildParam(config.getFeature(str)));
            sb.append("]");
            sb.append("}");
            sb.append(str4);
        }
        return sb.substring(0, sb.length() - 1);
    }

    private static String buildFeatures(Config config) {
        StringBuilder sb = new StringBuilder();
        sb.append(KEY_FEATURES);
        sb.append(":");
        sb.append("[");
        sb.append(buildFeature(config));
        sb.append("]");
        return sb.toString();
    }

    private static String buildParam(Feature feature) {
        StringBuilder sb = new StringBuilder();
        TreeSet<String> treeSet = new TreeSet<>(feature.getParams().keySet());
        if (treeSet.isEmpty()) {
            return "";
        }
        for (String str : treeSet) {
            sb.append("{");
            sb.append(KEY_NAME);
            String str2 = ":";
            sb.append(str2);
            String str3 = "\"";
            sb.append(str3);
            sb.append(str);
            sb.append(str3);
            String str4 = ",";
            sb.append(str4);
            sb.append("value");
            sb.append(str2);
            sb.append(str3);
            sb.append(feature.getParam(str));
            sb.append(str3);
            sb.append("}");
            sb.append(str4);
        }
        return sb.substring(0, sb.length() - 1);
    }

    private static Object buildPermission(Config config) {
        StringBuilder sb = new StringBuilder();
        TreeSet<String> treeSet = new TreeSet<>(config.getPermissions().keySet());
        if (treeSet.isEmpty()) {
            return "";
        }
        for (String str : treeSet) {
            sb.append("{");
            sb.append("origin");
            String str2 = ":";
            sb.append(str2);
            String str3 = "\"";
            sb.append(str3);
            sb.append(str);
            sb.append(str3);
            String str4 = ",";
            sb.append(str4);
            sb.append(KEY_SUBDOMAINS);
            sb.append(str2);
            sb.append(config.getPermission(str).isApplySubdomains());
            sb.append("}");
            sb.append(str4);
        }
        return sb.substring(0, sb.length() - 1);
    }

    private static String buildPermissions(Config config) {
        StringBuilder sb = new StringBuilder();
        sb.append(KEY_PERMISSIONS);
        sb.append(":");
        sb.append("[");
        sb.append(buildPermission(config));
        sb.append("]");
        return sb.toString();
    }

    public static String getRawConfig(Config config) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(KEY_TIMESTAMP);
        String str = ":";
        sb.append(str);
        sb.append(config.getSecurity().getTimestamp());
        String str2 = ",";
        sb.append(str2);
        sb.append("vendor");
        sb.append(str);
        String str3 = "\"";
        sb.append(str3);
        sb.append(config.getVendor());
        sb.append(str3);
        sb.append(str2);
        sb.append(buildFeatures(config));
        sb.append(str2);
        sb.append(buildPermissions(config));
        sb.append("}");
        return sb.toString();
    }
}
