package com.android.camera.animation.type;

import android.view.View;
import android.view.animation.Interpolator;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import com.android.camera.animation.AnimationDelegate;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

@Deprecated
public abstract class BaseOnSubScribe implements CompletableOnSubscribe {
    protected View mAniView;
    protected int mDurationTime = 200;
    private Interpolator mInterpolator;
    protected int mStartDelayTime;
    protected boolean mTargetGone;

    public BaseOnSubScribe(View view) {
        this.mAniView = view;
    }

    public static final void setAnimateViewVisible(View view, int i) {
        ViewCompat.animate(view).cancel();
        if (view != null && view.getVisibility() != i) {
            view.setVisibility(i);
        }
    }

    public abstract ViewPropertyAnimatorCompat getAnimation();

    /* access modifiers changed from: protected */
    public void onAnimationEnd() {
    }

    public BaseOnSubScribe setDurationTime(int i) {
        this.mDurationTime = i;
        return this;
    }

    public BaseOnSubScribe setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
        return this;
    }

    public BaseOnSubScribe setStartDelayTime(int i) {
        this.mStartDelayTime = i;
        return this;
    }

    public final void startAnimationImmediately() {
        Completable.create(this).subscribe();
    }

    public void subscribe(final CompletableEmitter completableEmitter) {
        ViewPropertyAnimatorCompat duration = getAnimation().setStartDelay((long) this.mStartDelayTime).setDuration((long) this.mDurationTime);
        Interpolator interpolator = this.mInterpolator;
        if (interpolator == null) {
            interpolator = AnimationDelegate.DEFAULT_INTERPOLATOR;
        }
        duration.setInterpolator(interpolator).withEndAction(new Runnable() {
            public void run() {
                BaseOnSubScribe.this.onAnimationEnd();
                completableEmitter.onComplete();
            }
        }).start();
    }

    public BaseOnSubScribe targetGone() {
        this.mTargetGone = true;
        return this;
    }
}
