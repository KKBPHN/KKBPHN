package com.android.camera.fragment.top;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.SystemProperties;
import android.view.View;
import android.view.animation.Interpolator;
import com.android.camera.animation.type.AlphaInOnSubscribe;
import com.android.camera.data.data.config.TopConfigItem;
import java.util.ArrayList;
import java.util.List;
import miui.view.animation.CubicEaseOutInterpolator;

public class TopBarAnimationComponent {
    public static final int DEBUG_ANIMATION_TIME_MULTIPLE = SystemProperties.getInt("animation_time_multiple", 1);
    public View mAnchorView;
    private Interpolator mCubiEaseOut = new CubicEaseOutInterpolator();
    private List mHidedViews;
    public int mReverseLeft;
    public boolean mShown;
    public TopExpendView mTopExpendView;
    public int spacesItemWidth;

    private ObjectAnimator addTopBarItemInAnimator(int i, View view, TopConfigItem topConfigItem) {
        if (view == null) {
            return null;
        }
        view.animate().cancel();
        view.clearAnimation();
        long j = (long) (DEBUG_ANIMATION_TIME_MULTIPLE * 300);
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("alpha", new float[]{0.0f, topConfigItem.enable ? 1.0f : 0.4f}), PropertyValuesHolder.ofFloat("translationX", new float[]{(float) i, 0.0f})});
        ofPropertyValuesHolder.setDuration(j);
        ofPropertyValuesHolder.setInterpolator(this.mCubiEaseOut);
        ofPropertyValuesHolder.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
            }
        });
        return ofPropertyValuesHolder;
    }

    private void addTopBarItemOutAnimator(View view) {
        if (view != null) {
            view.clearAnimation();
            view.setAlpha(0.0f);
            view.setEnabled(false);
        }
    }

    public /* synthetic */ void O00ooO00() {
        for (View enabled : this.mHidedViews) {
            enabled.setEnabled(true);
        }
        this.mAnchorView.setAlpha(1.0f);
        this.mAnchorView.setEnabled(true);
        hideExpendView();
        this.mHidedViews.clear();
        this.mTopExpendView = null;
    }

    public void hideExpendView() {
        TopExpendView topExpendView = this.mTopExpendView;
        if (topExpendView != null) {
            topExpendView.setVisibility(8);
            this.mTopExpendView.setOnClickListener(null);
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<android.view.View>, for r4v0, types: [java.util.List, java.util.List<android.view.View>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void hideOtherViews(int i, List<View> list) {
        if (!this.mShown) {
            this.mShown = true;
            List list2 = this.mHidedViews;
            if (list2 == null) {
                this.mHidedViews = new ArrayList();
            } else {
                list2.clear();
            }
            for (View view : list) {
                if (view.getVisibility() == 0) {
                    if (view.getAlpha() != 0.0f) {
                        this.mHidedViews.add(view);
                        addTopBarItemOutAnimator(view);
                    }
                }
            }
        }
    }

    public boolean reverse(boolean z) {
        int i;
        int i2;
        TopExpendView topExpendView = this.mTopExpendView;
        int i3 = 0;
        if ((topExpendView != null && topExpendView.isAnimRuning()) || !this.mShown) {
            return false;
        }
        this.mShown = false;
        if (this.mHidedViews != null) {
            TopConfigItem topConfigItem = (TopConfigItem) this.mAnchorView.getTag();
            int i4 = topConfigItem.index;
            int i5 = this.spacesItemWidth;
            if (i4 > 0) {
                View view = (View) this.mHidedViews.get(i4 - 1);
                i = ((this.mTopExpendView.getStartViewLeft() - i5) - view.getWidth()) - view.getLeft();
            } else {
                i = 0;
            }
            if (i4 < this.mHidedViews.size() - 1) {
                View view2 = (View) this.mHidedViews.get(i4 + 1);
                i2 = ((this.mTopExpendView.getEndViewRight() + i5) + view2.getWidth()) - view2.getRight();
            } else {
                i2 = 0;
            }
            for (View view3 : this.mHidedViews) {
                if (z) {
                    TopConfigItem topConfigItem2 = (TopConfigItem) view3.getTag();
                    int i6 = topConfigItem2.index;
                    if (topConfigItem.configItem != topConfigItem2.configItem) {
                        if (i4 > i6) {
                            i3 = i;
                        } else if (i4 < i6) {
                            i3 = i2;
                        }
                        ObjectAnimator addTopBarItemInAnimator = addTopBarItemInAnimator(i3, view3, topConfigItem2);
                        if (addTopBarItemInAnimator != null) {
                            addTopBarItemInAnimator.start();
                        }
                    }
                } else {
                    AlphaInOnSubscribe.directSetResult(view3);
                }
            }
        }
        this.mTopExpendView.revertExpendView(z, new C0329O0000ooO(this));
        return true;
    }

    public void showExpendView() {
        TopExpendView topExpendView = this.mTopExpendView;
        if (topExpendView != null) {
            topExpendView.setVisibility(0);
        }
    }
}
