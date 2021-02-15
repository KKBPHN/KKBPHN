package com.miui.internal.component.module;

import java.io.File;

public class ModuleFolder {
    private File apkFolder;
    private File optFolder;

    public ModuleFolder() {
    }

    public ModuleFolder(File file, File file2) {
        this.apkFolder = file;
        this.optFolder = file2;
    }

    public File getApkFolder() {
        return this.apkFolder;
    }

    public File getOptFolder() {
        return this.optFolder;
    }

    public void setApkFolder(File file) {
        this.apkFolder = file;
    }

    public void setOptFolder(File file) {
        this.optFolder = file;
    }
}
