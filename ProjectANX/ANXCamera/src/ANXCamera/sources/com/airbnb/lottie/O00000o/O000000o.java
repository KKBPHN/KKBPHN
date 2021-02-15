package com.airbnb.lottie.O00000o;

import android.animation.Animator.AnimatorListener;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.os.Build.VERSION;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class O000000o extends ValueAnimator {

    /* renamed from: O000000o reason: collision with root package name */
    private final Set f5O000000o = new CopyOnWriteArraySet();
    private final Set listeners = new CopyOnWriteArraySet();

    /* access modifiers changed from: 0000 */
    public void O000000o(boolean z) {
        for (AnimatorListener animatorListener : this.listeners) {
            if (VERSION.SDK_INT >= 26) {
                animatorListener.onAnimationEnd(this, z);
            } else {
                animatorListener.onAnimationEnd(this);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void O00000Oo(boolean z) {
        for (AnimatorListener animatorListener : this.listeners) {
            if (VERSION.SDK_INT >= 26) {
                animatorListener.onAnimationStart(this, z);
            } else {
                animatorListener.onAnimationStart(this);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void O0000OoO() {
        for (AnimatorListener onAnimationCancel : this.listeners) {
            onAnimationCancel.onAnimationCancel(this);
        }
    }

    /* access modifiers changed from: 0000 */
    public void O0000Ooo() {
        for (AnimatorListener onAnimationRepeat : this.listeners) {
            onAnimationRepeat.onAnimationRepeat(this);
        }
    }

    /* access modifiers changed from: 0000 */
    public void O0000o00() {
        for (AnimatorUpdateListener onAnimationUpdate : this.f5O000000o) {
            onAnimationUpdate.onAnimationUpdate(this);
        }
    }

    public void addListener(AnimatorListener animatorListener) {
        this.listeners.add(animatorListener);
    }

    public void addUpdateListener(AnimatorUpdateListener animatorUpdateListener) {
        this.f5O000000o.add(animatorUpdateListener);
    }

    public long getStartDelay() {
        throw new UnsupportedOperationException("LottieAnimator does not support getStartDelay.");
    }

    public void removeAllListeners() {
        this.listeners.clear();
    }

    public void removeAllUpdateListeners() {
        this.f5O000000o.clear();
    }

    public void removeListener(AnimatorListener animatorListener) {
        this.listeners.remove(animatorListener);
    }

    public void removeUpdateListener(AnimatorUpdateListener animatorUpdateListener) {
        this.f5O000000o.remove(animatorUpdateListener);
    }

    public ValueAnimator setDuration(long j) {
        throw new UnsupportedOperationException("LottieAnimator does not support setDuration.");
    }

    public void setInterpolator(TimeInterpolator timeInterpolator) {
        throw new UnsupportedOperationException("LottieAnimator does not support setInterpolator.");
    }

    public void setStartDelay(long j) {
        throw new UnsupportedOperationException("LottieAnimator does not support setStartDelay.");
    }
}
