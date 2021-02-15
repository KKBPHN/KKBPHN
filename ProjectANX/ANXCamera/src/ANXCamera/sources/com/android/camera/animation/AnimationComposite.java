package com.android.camera.animation;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.LinearInterpolator;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import com.android.camera.Display;
import com.android.camera.animation.AnimationDelegate.AnimationResource;
import com.android.camera.data.DataRepository;
import com.android.camera.log.Log;
import com.android.camera.module.loader.StartControl;
import com.ss.android.vesdk.VEResult;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class AnimationComposite implements Consumer {
    private static final String TAG = "AnimationComposite";
    private AtomicBoolean mActive = new AtomicBoolean(true);
    private Disposable mAfterFrameArrivedDisposable = Observable.create(new ObservableOnSubscribe() {
        public void subscribe(ObservableEmitter observableEmitter) {
            AnimationComposite.this.mAfterFrameArrivedEmitter = observableEmitter;
        }
    }).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer) this);
    /* access modifiers changed from: private */
    public ObservableEmitter mAfterFrameArrivedEmitter;
    private Disposable mAnimationDisposable;
    /* access modifiers changed from: private */
    public int mCurrentDegree = 0;
    private int mOrientation = -1;
    private SparseArray mResourceSparseArray = new SparseArray();
    private ValueAnimator mRotationAnimator;
    private int mStartDegree = 0;
    private int mTargetDegree = 0;

    public void accept(@NonNull Integer num) {
        if (!this.mActive.get()) {
            Log.e(TAG, "not active, skip notifyAfterFrameAvailable");
            return;
        }
        DataRepository.dataItemGlobal().setRetriedIfCameraError(false);
        for (int i = 1; i < this.mResourceSparseArray.size(); i++) {
            AnimationResource animationResource = (AnimationResource) this.mResourceSparseArray.valueAt(i);
            if (animationResource.canProvide()) {
                if (!animationResource.isEnableClick()) {
                    animationResource.setClickEnable(true);
                }
                animationResource.notifyAfterFrameAvailable(num.intValue());
            }
        }
        ((AnimationResource) this.mResourceSparseArray.valueAt(0)).notifyAfterFrameAvailable(num.intValue());
    }

    public void destroy() {
        SparseArray sparseArray = this.mResourceSparseArray;
        if (sparseArray != null) {
            sparseArray.clear();
            this.mResourceSparseArray = null;
        }
        Disposable disposable = this.mAfterFrameArrivedDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mAfterFrameArrivedEmitter.onComplete();
            this.mAfterFrameArrivedDisposable.dispose();
            this.mAfterFrameArrivedDisposable = null;
        }
    }

    public Disposable dispose(@Nullable Completable completable, @Nullable Action action, StartControl startControl) {
        int i;
        ArrayList arrayList = new ArrayList();
        if (completable != null) {
            arrayList.add(completable);
        }
        int i2 = startControl.mTargetMode;
        int i3 = startControl.mResetType;
        int i4 = startControl.mViewConfigType;
        if (i4 != -1) {
            int i5 = 0;
            if (i4 == 1) {
                while (i < this.mResourceSparseArray.size()) {
                    AnimationResource animationResource = (AnimationResource) this.mResourceSparseArray.valueAt(i);
                    if (animationResource.canProvide()) {
                        animationResource.provideAnimateElement(i2, null, i3);
                    }
                    i++;
                }
            } else if (i4 == 2) {
                while (i5 < this.mResourceSparseArray.size()) {
                    AnimationResource animationResource2 = (AnimationResource) this.mResourceSparseArray.valueAt(i5);
                    if (animationResource2.canProvide()) {
                        animationResource2.provideAnimateElement(i2, arrayList, i3);
                    }
                    i5++;
                }
            } else if (i4 == 3) {
                while (i5 < this.mResourceSparseArray.size()) {
                    AnimationResource animationResource3 = (AnimationResource) this.mResourceSparseArray.valueAt(i5);
                    if (animationResource3.canProvide() && animationResource3.needViewClear()) {
                        animationResource3.provideAnimateElement(i2, null, i3);
                    }
                    i5++;
                }
            }
        }
        Disposable disposable = this.mAnimationDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mAnimationDisposable.dispose();
        }
        Completable merge = Completable.merge((Iterable) arrayList);
        this.mAnimationDisposable = action != null ? merge.subscribe(action) : merge.subscribe();
        return this.mAnimationDisposable;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:48:0x009c, code lost:
        if (r1 == 0) goto L_0x00a9;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void disposeRotation(int i, boolean z) {
        int i2;
        int i3 = m.cQ;
        int i4 = i >= 0 ? i % m.cQ : (i % m.cQ) + m.cQ;
        int i5 = this.mOrientation;
        if (i5 != i4) {
            boolean z2 = i5 != -1;
            int i6 = i4 - this.mOrientation;
            if (i6 < 0) {
                i6 += m.cQ;
            }
            if (i6 > 180) {
                i6 += VEResult.TER_EGL_BAD_MATCH;
            }
            boolean z3 = i6 <= 0;
            this.mOrientation = i4;
            if (this.mOrientation != 0 || this.mCurrentDegree != 0) {
                this.mTargetDegree = (360 - i4) % m.cQ;
                final ArrayList<View> arrayList = new ArrayList<>();
                for (int i7 = 0; i7 < this.mResourceSparseArray.size(); i7++) {
                    AnimationResource animationResource = (AnimationResource) this.mResourceSparseArray.valueAt(i7);
                    if (animationResource.canProvide()) {
                        if (z) {
                            animationResource.provideOrientationChanged(Display.getScreenOrientation(), arrayList, this.mTargetDegree);
                        } else {
                            animationResource.provideRotateItem(arrayList, this.mTargetDegree);
                        }
                    }
                }
                if (!z2) {
                    this.mCurrentDegree = this.mTargetDegree;
                    for (View rotation : arrayList) {
                        ViewCompat.setRotation(rotation, (float) this.mTargetDegree);
                    }
                    return;
                }
                ValueAnimator valueAnimator = this.mRotationAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                this.mStartDegree = this.mCurrentDegree;
                int i8 = this.mStartDegree;
                if (z3) {
                    if (i8 == 360) {
                        i8 = 0;
                    }
                    i2 = this.mTargetDegree;
                } else {
                    if (i8 == 0) {
                        i8 = 360;
                    }
                    i2 = this.mTargetDegree;
                    if (i2 == 360) {
                        i3 = 0;
                        this.mRotationAnimator = ValueAnimator.ofInt(new int[]{i8, i3});
                        this.mRotationAnimator.setInterpolator(new LinearInterpolator());
                        this.mRotationAnimator.removeAllUpdateListeners();
                        this.mRotationAnimator.addUpdateListener(new AnimatorUpdateListener() {
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                AnimationComposite.this.mCurrentDegree = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                                for (View view : arrayList) {
                                    if (view != null) {
                                        ViewCompat.setRotation(view, (float) AnimationComposite.this.mCurrentDegree);
                                    }
                                }
                            }
                        });
                        this.mRotationAnimator.start();
                    }
                }
                i3 = i2;
                this.mRotationAnimator = ValueAnimator.ofInt(new int[]{i8, i3});
                this.mRotationAnimator.setInterpolator(new LinearInterpolator());
                this.mRotationAnimator.removeAllUpdateListeners();
                this.mRotationAnimator.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        AnimationComposite.this.mCurrentDegree = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                        for (View view : arrayList) {
                            if (view != null) {
                                ViewCompat.setRotation(view, (float) AnimationComposite.this.mCurrentDegree);
                            }
                        }
                    }
                });
                this.mRotationAnimator.start();
            }
        }
    }

    public int getTargetDegree() {
        return this.mTargetDegree;
    }

    public void notifyAfterFirstFrameArrived(int i) {
        Disposable disposable = this.mAfterFrameArrivedDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mAfterFrameArrivedEmitter.onNext(Integer.valueOf(i));
        }
    }

    public void notifyDataChanged(int i, int i2) {
        for (int i3 = 0; i3 < this.mResourceSparseArray.size(); i3++) {
            AnimationResource animationResource = (AnimationResource) this.mResourceSparseArray.valueAt(i3);
            if (animationResource.canProvide()) {
                animationResource.notifyDataChanged(i, i2);
            }
        }
    }

    public void onStart() {
        this.mActive.set(true);
    }

    public void onStop() {
        this.mActive.set(false);
    }

    public void put(int i, AnimationResource animationResource) {
        this.mResourceSparseArray.put(i, animationResource);
    }

    public void remove(int i) {
        this.mResourceSparseArray.remove(i);
    }

    public void setClickEnable(boolean z) {
        for (int i = 0; i < this.mResourceSparseArray.size(); i++) {
            AnimationResource animationResource = (AnimationResource) this.mResourceSparseArray.valueAt(i);
            if (animationResource.canProvide() && animationResource.isEnableClick() != z) {
                animationResource.setClickEnable(z);
            }
        }
    }
}
