package com.android.camera.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.PathInterpolator;
import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.camera.Display;
import com.android.camera.Util;
import com.android.camera.animation.AnimationDelegate.AnimationResource;
import com.android.camera.animation.AnimationDelegate.AnimationResource.ScreenOrientation;
import com.android.camera.animation.folme.FolmeAlphaInOnSubscribe;
import com.android.camera.animation.folme.FolmeAlphaOutOnSubscribe;
import com.android.camera.animation.type.AlphaInOnSubscribe;
import com.android.camera.animation.type.AlphaOutOnSubscribe;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.lifeCircle.BaseLifecycleListener;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.BaseProtocol;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.tts.TTSHelper;
import io.reactivex.Completable;
import io.reactivex.CompletableOnSubscribe;
import java.util.List;

public abstract class BaseFragment extends Fragment implements AnimationResource, BaseProtocol {
    protected static final int LEFT_LANDSCAPE = 90;
    protected static final int RIGHT_LANDSCAPE = 270;
    protected static final int ROTATE_FLIP = 180;
    protected static final int STATE_HIDE = -1;
    protected static final int STATE_SHOW = 1;
    private static final String TAG = "BaseFragment";
    /* access modifiers changed from: protected */
    public int mCurrentMode;
    /* access modifiers changed from: protected */
    public int mDegree;
    private boolean mEnableClick = true;
    private boolean mInModeChanging;
    private boolean mIsFullScreenNavBarHidden;
    private boolean mIsnotchScreenHidden;
    private int mLaseFragmentInfo = 240;
    private BaseLifecycleListener mLifecycleListener;
    private int mNewFragmentInfo = 240;
    private boolean mRegistered;
    protected int mResetType = 2;
    private boolean mSilentRemove = false;
    protected TTSHelper mTTSHelper;

    /* access modifiers changed from: protected */
    public void animateViews(int i, List list, float f, View view) {
        CompletableOnSubscribe completableOnSubscribe;
        if (view.getTag() == null || ((Integer) view.getTag()).intValue() != i) {
            view.setTag(Integer.valueOf(i));
            if (i == 1) {
                if (list == null) {
                    FolmeAlphaInOnSubscribe.directSetResult(view);
                }
                FolmeAlphaInOnSubscribe folmeAlphaInOnSubscribe = new FolmeAlphaInOnSubscribe(view);
                if (f == -1.0f) {
                    f = 0.0f;
                }
                completableOnSubscribe = folmeAlphaInOnSubscribe.setStartAlpha(f);
            } else if (list == null) {
                FolmeAlphaOutOnSubscribe.directSetResult(view);
            } else {
                FolmeAlphaOutOnSubscribe folmeAlphaOutOnSubscribe = new FolmeAlphaOutOnSubscribe(view);
                if (f == -1.0f) {
                    f = 1.0f;
                }
                completableOnSubscribe = folmeAlphaOutOnSubscribe.setStartAlpha(f);
            }
            list.add(Completable.create(completableOnSubscribe));
        }
    }

    /* access modifiers changed from: protected */
    public void animateViews(int i, List list, View view) {
        animateViews(i, list, -1.0f, view);
    }

    /* access modifiers changed from: protected */
    public void animateViews(int i, boolean z, View view) {
        CompletableOnSubscribe completableOnSubscribe;
        if (view.getTag() == null || ((Integer) view.getTag()).intValue() != i) {
            view.setTag(Integer.valueOf(i));
            if (i == 1) {
                if (!z) {
                    AlphaInOnSubscribe.directSetResult(view);
                }
                completableOnSubscribe = new AlphaInOnSubscribe(view);
            } else if (!z) {
                AlphaOutOnSubscribe.directSetResult(view);
            } else {
                completableOnSubscribe = new AlphaOutOnSubscribe(view);
            }
            Completable.create(completableOnSubscribe).subscribe();
        }
    }

    public final boolean canProvide() {
        return isAdded();
    }

    public abstract int getFragmentInto();

    @NonNull
    public final String getFragmentTag() {
        return String.valueOf(getFragmentInto());
    }

    @LayoutRes
    public abstract int getLayoutResourceId();

    public abstract void initView(View view);

    public final boolean isBothLandscapeMode() {
        return this.mCurrentMode == 212;
    }

    public boolean isEnableClick() {
        return this.mEnableClick;
    }

    public final boolean isFlipRotate() {
        return this.mDegree == 180;
    }

    /* access modifiers changed from: protected */
    public boolean isInModeChanging() {
        return this.mInModeChanging;
    }

    public final boolean isLandScape() {
        int i = this.mDegree;
        return i == 90 || i == 270;
    }

    public final boolean isLeftBothLandScape() {
        int i = this.mDegree;
        return i == 0 || i == 90;
    }

    public final boolean isLeftLandScape() {
        return this.mDegree == 90;
    }

    public final boolean isLeftLandscapeMode() {
        int i = this.mCurrentMode;
        return i == 179 || i == 182 || i == 185 || i == 189 || i == 213 || i == 207 || i == 208;
    }

    public final boolean isRightBothLandScape() {
        return !isLeftBothLandScape();
    }

    public final boolean isRightLandScape() {
        return this.mDegree == 270;
    }

    public boolean needViewClear() {
        return false;
    }

    @CallSuper
    public void notifyAfterFrameAvailable(int i) {
    }

    @CallSuper
    public void notifyDataChanged(int i, int i2) {
        this.mInModeChanging = false;
        this.mCurrentMode = i2;
    }

    public Animation onCreateAnimation(int i, boolean z, int i2) {
        if (z) {
            return provideEnterAnimation(this.mLaseFragmentInfo);
        }
        if (!this.mSilentRemove) {
            return provideExitAnimation(this.mNewFragmentInfo);
        }
        return null;
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        this.mCurrentMode = DataRepository.dataItemGlobal().getCurrentMode();
        this.mIsFullScreenNavBarHidden = Display.isFullScreenNavBarHidden();
        this.mIsnotchScreenHidden = Display.isNotchScreenHidden();
        View inflate = layoutInflater.inflate(getLayoutResourceId(), viewGroup, false);
        initView(inflate);
        return inflate;
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.mRegistered) {
            this.mRegistered = false;
            unRegisterProtocol();
            BaseLifecycleListener baseLifecycleListener = this.mLifecycleListener;
            if (baseLifecycleListener != null) {
                baseLifecycleListener.onLifeDestroy(getFragmentTag());
                this.mLifecycleListener = null;
            }
        }
    }

    public void onResume() {
        super.onResume();
        if (!(this.mIsnotchScreenHidden == Display.isNotchScreenHidden() && this.mIsFullScreenNavBarHidden == Display.isFullScreenNavBarHidden())) {
            initView(getView());
        }
        this.mIsnotchScreenHidden = Display.isNotchScreenHidden();
        this.mIsFullScreenNavBarHidden = Display.isFullScreenNavBarHidden();
        if (Util.isAccessible() && this.mTTSHelper == null) {
            this.mTTSHelper = new TTSHelper(getContext());
        }
    }

    public void onStart() {
        super.onStart();
        BaseLifecycleListener baseLifecycleListener = this.mLifecycleListener;
        if (baseLifecycleListener != null) {
            baseLifecycleListener.onLifeStart(getFragmentTag());
        }
    }

    public void onStop() {
        super.onStop();
        BaseLifecycleListener baseLifecycleListener = this.mLifecycleListener;
        if (baseLifecycleListener != null) {
            baseLifecycleListener.onLifeStop(getFragmentTag());
        }
        TTSHelper tTSHelper = this.mTTSHelper;
        if (tTSHelper != null) {
            tTSHelper.cleanup();
            this.mTTSHelper = null;
        }
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public void pendingGone(boolean z) {
        setClickEnable(false);
        this.mSilentRemove = z;
    }

    public void pendingShow() {
        setClickEnable(true);
    }

    @CallSuper
    public void provideAnimateElement(int i, List list, int i2) {
        if (list != null) {
            this.mInModeChanging = true;
        }
        this.mCurrentMode = i;
        this.mResetType = i2;
    }

    /* access modifiers changed from: protected */
    public Animation provideEnterAnimation(int i) {
        return null;
    }

    /* access modifiers changed from: protected */
    public Animation provideExitAnimation(int i) {
        return null;
    }

    public void provideOrientationChanged(@ScreenOrientation int i, List list, int i2) {
    }

    @CallSuper
    public void provideRotateItem(List list, int i) {
        setDegree(i);
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void register(ModeCoordinator modeCoordinator) {
    }

    /* access modifiers changed from: protected */
    public final void registerBackStack(ModeCoordinator modeCoordinator, HandleBackTrace handleBackTrace) {
        BackStack backStack = (BackStack) modeCoordinator.getAttachProtocol(171);
        if (backStack != null) {
            backStack.addInBackStack(handleBackTrace);
        }
    }

    public final void registerProtocol() {
        this.mRegistered = true;
        register(ModeCoordinatorImpl.getInstance());
    }

    @CallSuper
    public void setClickEnable(boolean z) {
        String simpleName = getClass().getSimpleName();
        StringBuilder sb = new StringBuilder();
        sb.append("setClickEnable: ");
        sb.append(z);
        Log.d(simpleName, sb.toString());
        this.mEnableClick = z;
    }

    public final void setDegree(int i) {
        this.mDegree = i;
    }

    public void setEnableClickInitValue(boolean z) {
        this.mEnableClick = z;
    }

    public void setLastFragmentInfo(int i) {
        this.mLaseFragmentInfo = i;
    }

    public void setLifecycleListener(BaseLifecycleListener baseLifecycleListener) {
        this.mLifecycleListener = baseLifecycleListener;
    }

    public void setNewFragmentInfo(int i) {
        this.mNewFragmentInfo = i;
    }

    /* access modifiers changed from: protected */
    public void startAnimateViewGone(View view, boolean z) {
        if (view.getVisibility() == 0) {
            if (z) {
                Completable.create(new AlphaOutOnSubscribe(view).setDurationTime(260).setInterpolator(new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f))).subscribe();
            } else {
                AlphaOutOnSubscribe.directSetResult(view);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void startAnimateViewVisible(View view, boolean z) {
        if (view.getVisibility() != 0) {
            if (z) {
                Completable.create(new AlphaInOnSubscribe(view).setStartDelayTime(240).setDurationTime(300).setInterpolator(new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f))).subscribe();
            } else {
                AlphaInOnSubscribe.directSetResult(view);
            }
        }
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void unRegister(ModeCoordinator modeCoordinator) {
    }

    /* access modifiers changed from: protected */
    public final void unRegisterBackStack(ModeCoordinator modeCoordinator, HandleBackTrace handleBackTrace) {
        BackStack backStack = (BackStack) modeCoordinator.getAttachProtocol(171);
        if (backStack != null) {
            backStack.removeBackStack(handleBackTrace);
        }
    }

    public final void unRegisterProtocol() {
        unRegister(ModeCoordinatorImpl.getInstance());
    }
}
