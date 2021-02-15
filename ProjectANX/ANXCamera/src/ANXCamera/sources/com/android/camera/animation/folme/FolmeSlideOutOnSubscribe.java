package com.android.camera.animation.folme;

import android.view.View;
import io.reactivex.CompletableEmitter;
import miuix.animation.Folme;
import miuix.animation.controller.AnimState;
import miuix.animation.property.ViewProperty;

class FolmeSlideOutOnSubscribe extends FolmeBaseOnSubScribe {
    private int mGravity;

    public FolmeSlideOutOnSubscribe(View view, int i) {
        super(view);
        this.mGravity = i;
    }

    public static void directSetResult(View view, int i) {
        int i2;
        Folme.useAt(view).state().clean();
        int max = Math.max(view.getWidth(), view.getLayoutParams().width);
        int max2 = Math.max(view.getHeight(), view.getLayoutParams().height);
        if (i == 3) {
            i2 = -max;
        } else if (i != 5) {
            if (i == 48) {
                max2 = -max2;
            } else if (i != 80) {
                i2 = 0;
                max2 = 0;
                view.setTranslationX((float) i2);
                view.setTranslationY((float) max2);
                view.setAlpha(1.0f);
                view.setVisibility(0);
            }
            i2 = 0;
            view.setTranslationX((float) i2);
            view.setTranslationY((float) max2);
            view.setAlpha(1.0f);
            view.setVisibility(0);
        } else {
            i2 = max;
        }
        max2 = 0;
        view.setTranslationX((float) i2);
        view.setTranslationY((float) max2);
        view.setAlpha(1.0f);
        view.setVisibility(0);
    }

    /* access modifiers changed from: protected */
    public void clean(View view) {
        Folme.useAt(this.mAniView).state().clean();
    }

    /* access modifiers changed from: protected */
    public void onAnimationEnd() {
        super.onAnimationEnd();
        this.mAniView.setVisibility(this.mTargetGone ? 8 : 4);
    }

    public void subscribe(CompletableEmitter completableEmitter) {
        super.subscribe(completableEmitter);
        this.mAniView.setVisibility(0);
        this.mAniView.setAlpha(1.0f);
        int max = Math.max(this.mAniView.getWidth(), this.mAniView.getLayoutParams().width);
        int max2 = Math.max(this.mAniView.getHeight(), this.mAniView.getLayoutParams().height);
        int i = this.mGravity;
        if (i == 3) {
            max = -max;
        } else if (i != 5) {
            if (i == 48) {
                max2 = -max2;
            } else if (i != 80) {
                max = 0;
                max2 = 0;
                AnimState add = new AnimState("Slide out from").add(ViewProperty.TRANSLATION_X, 0, new long[0]).add(ViewProperty.TRANSLATION_Y, 0, new long[0]);
                AnimState add2 = new AnimState("Slide out to").add(ViewProperty.TRANSLATION_X, max, new long[0]).add(ViewProperty.TRANSLATION_Y, max2, new long[0]);
                Folme.useAt(this.mAniView).state().fromTo(add, add2, getAnimConfig());
            }
            max = 0;
            AnimState add3 = new AnimState("Slide out from").add(ViewProperty.TRANSLATION_X, 0, new long[0]).add(ViewProperty.TRANSLATION_Y, 0, new long[0]);
            AnimState add22 = new AnimState("Slide out to").add(ViewProperty.TRANSLATION_X, max, new long[0]).add(ViewProperty.TRANSLATION_Y, max2, new long[0]);
            Folme.useAt(this.mAniView).state().fromTo(add3, add22, getAnimConfig());
        }
        max2 = 0;
        AnimState add32 = new AnimState("Slide out from").add(ViewProperty.TRANSLATION_X, 0, new long[0]).add(ViewProperty.TRANSLATION_Y, 0, new long[0]);
        AnimState add222 = new AnimState("Slide out to").add(ViewProperty.TRANSLATION_X, max, new long[0]).add(ViewProperty.TRANSLATION_Y, max2, new long[0]);
        Folme.useAt(this.mAniView).state().fromTo(add32, add222, getAnimConfig());
    }
}
