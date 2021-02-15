package com.miui.internal.hybrid;

import java.util.HashMap;
import java.util.Map;

public class Config {
    private String content;
    private Map features = new HashMap();
    private Map permissions = new HashMap();
    private Map preferences = new HashMap();
    private Security security;

    /* renamed from: vendor reason: collision with root package name */
    private String f9vendor;

    public void addFeature(Feature feature) {
        this.features.put(feature.getName(), feature);
    }

    public void addPermission(Permission permission) {
        this.permissions.put(permission.getUri(), permission);
    }

    public void clearFeatures() {
        this.features.clear();
    }

    public void clearPermissions() {
        this.permissions.clear();
    }

    public void clearPreferences() {
        this.preferences.clear();
    }

    public String getContent() {
        return this.content;
    }

    public Feature getFeature(String str) {
        return (Feature) this.features.get(str);
    }

    public Map getFeatures() {
        return this.features;
    }

    public Permission getPermission(String str) {
        return (Permission) this.permissions.get(str);
    }

    public Map getPermissions() {
        return this.permissions;
    }

    public String getPreference(String str) {
        return (String) this.preferences.get(str);
    }

    public Map getPreferences() {
        return this.preferences;
    }

    public Security getSecurity() {
        return this.security;
    }

    public String getVendor() {
        return this.f9vendor;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public void setFeatures(Map map) {
        this.features = map;
    }

    public void setPermissions(Map map) {
        this.permissions = map;
    }

    public void setPreference(String str, String str2) {
        this.preferences.put(str, str2);
    }

    public void setPreferences(Map map) {
        this.preferences = map;
    }

    public void setSecurity(Security security2) {
        this.security = security2;
    }

    public void setVendor(String str) {
        this.f9vendor = str;
    }
}
