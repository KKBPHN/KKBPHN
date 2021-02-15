package com.android.camera.animation.folme;

import android.view.View;
import io.reactivex.CompletableEmitter;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.property.ViewProperty;

public class FolmeAlphaToOnSubscribe extends FolmeBaseOnSubScribe {
    private float mEndAlpha;
    private float mStartAlpha;

    public FolmeAlphaToOnSubscribe(View view, float f, float f2) {
        super(view);
        this.mStartAlpha = f;
        this.mEndAlpha = f2;
    }

    /* access modifiers changed from: protected */
    public void clean(View view) {
        Folme.useAt(view).state().clean();
    }

    public void subscribe(CompletableEmitter completableEmitter) {
        super.subscribe(completableEmitter);
        clean(this.mAniView);
        this.mAniView.setVisibility(0);
        AnimState add = new AnimState("start alpha").add(ViewProperty.ALPHA, this.mStartAlpha, new long[0]);
        AnimState add2 = new AnimState("end alpha").add(ViewProperty.ALPHA, this.mEndAlpha, new long[0]);
        Folme.useAt(this.mAniView).state().setTo((Object) add).to(add2, new AnimConfig(getAnimConfig()).setEase(16, 300.0f));
    }
}
