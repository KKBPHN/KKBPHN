package com.android.camera.animation.folme;

import android.view.View;
import io.reactivex.CompletableEmitter;
import miuix.animation.Folme;
import miuix.animation.controller.AnimState;
import miuix.animation.property.ViewProperty;

public class FolmeScaleOnSubScribe extends FolmeBaseOnSubScribe {
    private float mOriginScale;
    private float mTargetScale;

    public FolmeScaleOnSubScribe(View view, float f, float f2) {
        super(view);
        this.mOriginScale = f;
        this.mTargetScale = f2;
    }

    /* access modifiers changed from: protected */
    public void clean(View view) {
        Folme.useAt(this.mAniView).state().clean();
    }

    public void subscribe(CompletableEmitter completableEmitter) {
        super.subscribe(completableEmitter);
        AnimState add = new AnimState("Scale from").add(ViewProperty.SCALE_X, this.mOriginScale, new long[0]).add(ViewProperty.SCALE_Y, this.mOriginScale, new long[0]);
        AnimState add2 = new AnimState("Scale to").add(ViewProperty.SCALE_X, this.mTargetScale, new long[0]).add(ViewProperty.SCALE_Y, this.mTargetScale, new long[0]);
        Folme.useAt(this.mAniView).state().fromTo(add, add2, getAnimConfig());
    }
}
