package com.android.camera.animation.folme;

import android.view.View;
import io.reactivex.CompletableEmitter;
import miuix.animation.Folme;
import miuix.animation.controller.AnimState;
import miuix.animation.property.ViewProperty;

public class FolmeRotateOnSubscribe extends FolmeBaseOnSubScribe {
    private int mOriginDegree;
    private int mTargetDegree;

    public FolmeRotateOnSubscribe(View view) {
        super(view);
    }

    /* access modifiers changed from: protected */
    public void clean(View view) {
        Folme.useAt(this.mAniView).state().clean();
    }

    public FolmeRotateOnSubscribe setRotateDegree(int i, int i2) {
        this.mOriginDegree = i;
        this.mTargetDegree = i2;
        return this;
    }

    public void subscribe(CompletableEmitter completableEmitter) {
        super.subscribe(completableEmitter);
        AnimState add = new AnimState("rotate from").add(ViewProperty.ROTATION, this.mOriginDegree, new long[0]);
        AnimState add2 = new AnimState("rotate to").add(ViewProperty.ROTATION, this.mTargetDegree, new long[0]);
        Folme.useAt(this.mAniView).state().fromTo(add, add2, getAnimConfig());
    }
}
