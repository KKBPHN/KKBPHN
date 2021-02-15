package com.android.camera.animation.folme;

import android.view.View;
import androidx.core.view.ViewCompat;
import io.reactivex.CompletableEmitter;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.property.ViewProperty;

public class FolmeAlphaInOnSubscribe extends FolmeBaseOnSubScribe {
    private float srcAlpha = 0.0f;

    public FolmeAlphaInOnSubscribe(View view) {
        super(view);
    }

    public static void directSetResult(View view) {
        Folme.useAt(view).state().end(new Object[0]);
        Folme.clean(view);
        ViewCompat.setAlpha(view, 1.0f);
        view.setVisibility(0);
    }

    /* access modifiers changed from: protected */
    public void clean(View view) {
        Folme.useAt(view).visible().clean();
    }

    public FolmeAlphaInOnSubscribe setStartAlpha(float f) {
        this.srcAlpha = f;
        return this;
    }

    public void subscribe(CompletableEmitter completableEmitter) {
        super.subscribe(completableEmitter);
        clean(this.mAniView);
        this.mAniView.setVisibility(0);
        if (this.srcAlpha != 0.0f) {
            AnimState add = new AnimState("start alpha").add(ViewProperty.ALPHA, this.srcAlpha, new long[0]);
            AnimState add2 = new AnimState("end alpha").add(ViewProperty.ALPHA, 1.0f, new long[0]);
            Folme.useAt(this.mAniView).state().setTo((Object) add).to(add2, new AnimConfig(getAnimConfig()).setEase(16, 300.0f));
            return;
        }
        Folme.useAt(this.mAniView).visible().show(getAnimConfig());
    }
}
