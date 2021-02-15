package com.android.camera.animation.folme;

import android.view.View;
import io.reactivex.CompletableEmitter;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.property.ViewProperty;

public class FolmeAlphaOutOnSubscribe extends FolmeBaseOnSubScribe {
    private float srcAlpha = 1.0f;

    public FolmeAlphaOutOnSubscribe(View view) {
        super(view);
    }

    public static void directSetGone(View view) {
        Folme.clean(view);
        view.setVisibility(8);
    }

    public static void directSetResult(View view) {
        Folme.useAt(view).state().end(new Object[0]);
        Folme.clean(view);
        view.setAlpha(0.0f);
        view.setVisibility(4);
    }

    /* access modifiers changed from: protected */
    public void clean(View view) {
        Folme.useAt(view).visible().clean();
    }

    /* access modifiers changed from: protected */
    public void onAnimationEnd() {
        super.onAnimationEnd();
        this.mAniView.setVisibility(this.mTargetGone ? 8 : 4);
    }

    public FolmeAlphaOutOnSubscribe setStartAlpha(float f) {
        this.srcAlpha = f;
        return this;
    }

    public void subscribe(CompletableEmitter completableEmitter) {
        super.subscribe(completableEmitter);
        clean(this.mAniView);
        if (this.srcAlpha != 1.0f) {
            this.mAniView.setVisibility(0);
            AnimState add = new AnimState("start alpha").add(ViewProperty.ALPHA, this.srcAlpha, new long[0]);
            AnimState add2 = new AnimState("end alpha").add(ViewProperty.ALPHA, 0.0f, new long[0]);
            Folme.useAt(this.mAniView).state().setTo((Object) add).to(add2, new AnimConfig(getAnimConfig()).setEase(16, 300.0f));
        }
        Folme.useAt(this.mAniView).visible().setShow().hide(getAnimConfig());
    }
}
