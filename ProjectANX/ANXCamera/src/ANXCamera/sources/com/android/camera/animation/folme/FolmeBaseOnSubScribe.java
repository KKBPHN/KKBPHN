package com.android.camera.animation.folme;

import android.view.View;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import miuix.animation.base.AnimConfig;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;

public abstract class FolmeBaseOnSubScribe implements CompletableOnSubscribe {
    protected View mAniView;
    /* access modifiers changed from: private */
    public CompletableEmitter mEmitter;
    protected Runnable mOnAnimationEnd;
    protected int mStartDelayTime;
    protected boolean mTargetGone;

    public FolmeBaseOnSubScribe(View view) {
        this.mAniView = view;
    }

    public abstract void clean(View view);

    /* access modifiers changed from: protected */
    public AnimConfig getAnimConfig() {
        return new AnimConfig().setDelay((long) this.mStartDelayTime).addListeners(new TransitionListener() {
            public void onComplete(Object obj, UpdateInfo updateInfo) {
                super.onComplete(obj, updateInfo);
                FolmeBaseOnSubScribe.this.onAnimationEnd();
                if (FolmeBaseOnSubScribe.this.mEmitter != null) {
                    FolmeBaseOnSubScribe.this.mEmitter.onComplete();
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onAnimationEnd() {
        clean(this.mAniView);
        Runnable runnable = this.mOnAnimationEnd;
        if (runnable != null) {
            runnable.run();
        }
    }

    public FolmeBaseOnSubScribe setOnAnimationEnd(Runnable runnable) {
        this.mOnAnimationEnd = runnable;
        return this;
    }

    public FolmeBaseOnSubScribe setStartDelayTime(int i) {
        this.mStartDelayTime = i;
        return this;
    }

    public FolmeBaseOnSubScribe setTargetGone(boolean z) {
        this.mTargetGone = z;
        return this;
    }

    public void subscribe(CompletableEmitter completableEmitter) {
        this.mEmitter = completableEmitter;
    }

    public FolmeBaseOnSubScribe targetGone() {
        this.mTargetGone = true;
        return this;
    }
}
