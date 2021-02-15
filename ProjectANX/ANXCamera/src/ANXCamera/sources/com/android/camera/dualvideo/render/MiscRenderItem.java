package com.android.camera.dualvideo.render;

import com.android.gallery3d.ui.BasicTexture;

class MiscRenderItem {
    private final String mName;
    private final BasicTexture mTex;

    public MiscRenderItem(String str, BasicTexture basicTexture) {
        this.mName = str;
        this.mTex = basicTexture;
    }

    public synchronized BasicTexture getBasicTexture() {
        return this.mTex;
    }

    public String getName() {
        return this.mName;
    }
}
