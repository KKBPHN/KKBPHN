package com.miui.internal.hybrid;

public class Permission {
    private boolean applySubdomains;
    private boolean forbidden;
    private String uri;

    public String getUri() {
        return this.uri;
    }

    public boolean isApplySubdomains() {
        return this.applySubdomains;
    }

    public boolean isForbidden() {
        return this.forbidden;
    }

    public void setApplySubdomains(boolean z) {
        this.applySubdomains = z;
    }

    public void setForbidden(boolean z) {
        this.forbidden = z;
    }

    public void setUri(String str) {
        this.uri = str;
    }
}
