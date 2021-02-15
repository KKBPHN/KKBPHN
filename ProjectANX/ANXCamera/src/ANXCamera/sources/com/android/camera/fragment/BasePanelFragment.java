package com.android.camera.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import androidx.annotation.NonNull;
import com.android.camera.animation.FolmeUtils;

public abstract class BasePanelFragment extends BaseFragment {
    protected final int ANIMATION_TYPE_ALPHA = 1;
    protected final int ANIMATION_TYPE_NONE = 0;
    protected final int ANIMATION_TYPE_SLIDE_UP = 2;
    protected final int ANIMATION_TYPE_SLIDE_UP_FILTER = 10;

    /* access modifiers changed from: protected */
    public void enterAnim(@NonNull View view) {
        int animationType = getAnimationType();
        if (animationType == 1) {
            FolmeUtils.animateShow(view);
        } else if (animationType == 2) {
            FolmeUtils.animateEntrance(view);
        } else if (animationType == 10) {
            FolmeUtils.animateEntrance4Filter(view);
        }
    }

    /* access modifiers changed from: protected */
    public void exitAnim(@NonNull View view) {
        int animationType = getAnimationType();
        if (animationType == 1) {
            FolmeUtils.animateHide(view);
        } else if (animationType == 2 || animationType == 10) {
            FolmeUtils.animateDeparture(view);
        }
    }

    /* access modifiers changed from: protected */
    public int getAnimationType() {
        return 0;
    }

    public Animation onCreateAnimation(int i, boolean z, int i2) {
        if (getAnimationType() == 0) {
            return super.onCreateAnimation(i, z, i2);
        }
        return null;
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (getView() != null) {
            exitAnim(getView());
        }
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (view != null) {
            enterAnim(view);
        }
    }
}
