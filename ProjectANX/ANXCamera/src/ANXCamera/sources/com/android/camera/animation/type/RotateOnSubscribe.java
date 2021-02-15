package com.android.camera.animation.type;

import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;

@Deprecated
public class RotateOnSubscribe extends BaseOnSubScribe {
    private int mOriginDegree;
    private int mTargetDegree;

    public RotateOnSubscribe(View view) {
        super(view);
    }

    /* access modifiers changed from: protected */
    public ViewPropertyAnimatorCompat getAnimation() {
        setDurationTime(300);
        this.mAniView.setRotation((float) this.mOriginDegree);
        return ViewCompat.animate(this.mAniView).rotation((float) this.mTargetDegree);
    }

    public RotateOnSubscribe setRotateDegree(int i, int i2) {
        this.mOriginDegree = i;
        this.mTargetDegree = i2;
        return this;
    }
}
