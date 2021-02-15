package com.android.camera.features.mimoji2.bean;

import com.android.camera.features.mimoji2.widget.autoselectview.SelectItemBean;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarConfigType;

public class MimojiTypeBean2 extends SelectItemBean {
    private ASAvatarConfigType mASAvatarConfigType;

    public ASAvatarConfigType getASAvatarConfigType() {
        return this.mASAvatarConfigType;
    }

    public void setASAvatarConfigType(ASAvatarConfigType aSAvatarConfigType) {
        this.mASAvatarConfigType = aSAvatarConfigType;
    }
}
