package com.miui.internal.component.plugin;

public class Outlet {
    private String name;
    private boolean optional;
    private AccessPermission visibility;

    public String getName() {
        return this.name;
    }

    public AccessPermission getVisibility() {
        return this.visibility;
    }

    public boolean isOptional() {
        return this.optional;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setOptional(boolean z) {
        this.optional = z;
    }

    public void setVisibility(AccessPermission accessPermission) {
        this.visibility = accessPermission;
    }
}
