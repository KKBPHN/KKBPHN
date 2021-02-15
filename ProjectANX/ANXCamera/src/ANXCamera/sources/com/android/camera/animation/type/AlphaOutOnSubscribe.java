package com.android.camera.animation.type;

import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;

@Deprecated
public class AlphaOutOnSubscribe extends BaseOnSubScribe {
    private float srcAlpha = 1.0f;

    public AlphaOutOnSubscribe(View view) {
        super(view);
    }

    public static void directSetResult(View view) {
        ViewCompat.setAlpha(view, 0.0f);
        BaseOnSubScribe.setAnimateViewVisible(view, 4);
    }

    public static boolean matchState(View view) {
        return ViewCompat.getAlpha(view) == 0.0f && view.getVisibility() != 0;
    }

    /* access modifiers changed from: protected */
    public ViewPropertyAnimatorCompat getAnimation() {
        BaseOnSubScribe.setAnimateViewVisible(this.mAniView, 0);
        ViewCompat.setAlpha(this.mAniView, this.srcAlpha);
        return ViewCompat.animate(this.mAniView).alpha(0.0f);
    }

    /* access modifiers changed from: protected */
    public void onAnimationEnd() {
        super.onAnimationEnd();
        BaseOnSubScribe.setAnimateViewVisible(this.mAniView, this.mTargetGone ? 8 : 4);
    }

    public AlphaOutOnSubscribe setStartAlpha(float f) {
        this.srcAlpha = f;
        return this;
    }
}
