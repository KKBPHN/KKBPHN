package com.miui.internal.component.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Plugin {
    private Map dependencies = new HashMap();
    private Map extensions = new HashMap();
    private File file;
    private String group;
    private int level;
    private int minCapatibleLevel;
    private String name;
    private Map outlets = new HashMap();
    private AccessPermission resources;
    private int versionCode;
    private String versionName;

    public void addDependency(Dependency dependency) {
        this.dependencies.put(dependency.getName(), dependency);
    }

    public void addExtension(Extension extension) {
        this.extensions.put(extension.getName(), extension);
    }

    public void addOutlet(Outlet outlet) {
        this.outlets.put(outlet.getName(), outlet);
    }

    public void clearDependencies() {
        this.dependencies.clear();
    }

    public void clearExtensions() {
        this.extensions.clear();
    }

    public void clearOutlets() {
        this.outlets.clear();
    }

    public Map getDependencies() {
        return this.dependencies;
    }

    public Dependency getDependency(String str) {
        return (Dependency) this.dependencies.get(str);
    }

    public Extension getExtension(String str) {
        return (Extension) this.extensions.get(str);
    }

    public Map getExtensions() {
        return this.extensions;
    }

    public File getFile() {
        return this.file;
    }

    public String getGroup() {
        return this.group;
    }

    public int getLevel() {
        return this.level;
    }

    public int getMinCapatibleLevel() {
        return this.minCapatibleLevel;
    }

    public String getName() {
        return this.name;
    }

    public Outlet getOutlet(String str) {
        return (Outlet) this.outlets.get(str);
    }

    public Map getOutlets() {
        return this.outlets;
    }

    public AccessPermission getResources() {
        return this.resources;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public void setDependencies(Map map) {
        this.dependencies = map;
    }

    public void setExtensions(Map map) {
        this.extensions = map;
    }

    public void setFile(File file2) {
        this.file = file2;
    }

    public void setGroup(String str) {
        this.group = str;
    }

    public void setLevel(int i) {
        this.level = i;
    }

    public void setMinCapatibleLevel(int i) {
        this.minCapatibleLevel = i;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setOutlets(Map map) {
        this.outlets = map;
    }

    public void setResources(AccessPermission accessPermission) {
        this.resources = accessPermission;
    }

    public void setVersionCode(int i) {
        this.versionCode = i;
    }

    public void setVersionName(String str) {
        this.versionName = str;
    }
}
