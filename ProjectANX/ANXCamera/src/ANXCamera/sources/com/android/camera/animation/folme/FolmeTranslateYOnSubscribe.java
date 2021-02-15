package com.android.camera.animation.folme;

import android.view.View;
import io.reactivex.CompletableEmitter;
import miuix.animation.Folme;
import miuix.animation.controller.AnimState;
import miuix.animation.property.ViewProperty;

public class FolmeTranslateYOnSubscribe extends FolmeBaseOnSubScribe {
    private int mDistanceY;

    public FolmeTranslateYOnSubscribe(View view, int i) {
        super(view);
        this.mDistanceY = i;
    }

    public static void directSetResult(View view, int i) {
        Folme.useAt(view).state().clean();
        view.setVisibility(0);
        view.setTranslationY((float) i);
    }

    /* access modifiers changed from: protected */
    public void clean(View view) {
        Folme.useAt(this.mAniView).state().clean();
    }

    public void subscribe(CompletableEmitter completableEmitter) {
        super.subscribe(completableEmitter);
        this.mAniView.setVisibility(0);
        AnimState add = new AnimState("TransY to").add(ViewProperty.TRANSLATION_Y, this.mDistanceY, new long[0]);
        Folme.useAt(this.mAniView).state().to(add, getAnimConfig());
    }
}
