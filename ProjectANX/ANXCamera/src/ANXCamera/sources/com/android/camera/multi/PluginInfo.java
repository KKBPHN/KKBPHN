package com.android.camera.multi;

import com.android.camera.resource.BaseResourceDownloadable;
import com.android.camera.resource.BaseResourceItem;

public class PluginInfo implements BaseResourceDownloadable {
    public BaseResourceItem resourceItem;
    public String sha1Base16;
    public String url;

    public PluginInfo() {
    }

    public PluginInfo(String str) {
        this.url = str;
    }

    public int getCurrentState() {
        return 0;
    }

    public String getDownloadUrl() {
        return this.url;
    }

    public void setState(int i) {
    }
}
