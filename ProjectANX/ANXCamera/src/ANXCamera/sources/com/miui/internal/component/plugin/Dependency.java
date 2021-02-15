package com.miui.internal.component.plugin;

public class Dependency {
    private int minLevel;
    private String name;
    private boolean optional;
    private boolean resourcesRequired;
    private int targetLevel;

    public int getMinLevel() {
        return this.minLevel;
    }

    public String getName() {
        return this.name;
    }

    public int getTargetLevel() {
        return this.targetLevel;
    }

    public boolean isOptional() {
        return this.optional;
    }

    public boolean isResourcesRequired() {
        return this.resourcesRequired;
    }

    public void setMinLevel(int i) {
        this.minLevel = i;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setOptional(boolean z) {
        this.optional = z;
    }

    public void setResourcesRequired(boolean z) {
        this.resourcesRequired = z;
    }

    public void setTargetLevel(int i) {
        this.targetLevel = i;
    }
}
