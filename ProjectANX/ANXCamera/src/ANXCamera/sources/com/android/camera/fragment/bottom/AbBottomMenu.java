package com.android.camera.fragment.bottom;

import android.content.Context;
import android.util.SparseArray;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import com.android.camera.ui.ColorActivateTextView;

public abstract class AbBottomMenu {
    private static final int CLICK_INTERVAL = 300;
    protected BeautyMenuAnimator mBeautyMenuAnimator;
    protected LinearLayout mContainerView;
    protected Context mContext;
    protected ColorActivateTextView mCurrentBeautyTextView;
    private long mLastClickTime;

    public AbBottomMenu(Context context, LinearLayout linearLayout, BeautyMenuAnimator beautyMenuAnimator) {
        this.mContext = context;
        this.mContainerView = linearLayout;
        this.mBeautyMenuAnimator = beautyMenuAnimator;
    }

    public abstract void addAllView();

    public abstract SparseArray getChildMenuViewList();

    public abstract int getDefaultType();

    public abstract SparseArray getMenuData();

    /* access modifiers changed from: protected */
    public void hideAdvance() {
        this.mContainerView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                BeautyMenuAnimator beautyMenuAnimator = AbBottomMenu.this.mBeautyMenuAnimator;
                if (beautyMenuAnimator != null) {
                    beautyMenuAnimator.shrinkImmediately();
                    AbBottomMenu.this.mContainerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean isClickEnable() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.mLastClickTime < 300) {
            return false;
        }
        this.mLastClickTime = currentTimeMillis;
        return true;
    }

    public abstract boolean isRefreshUI();

    /* access modifiers changed from: 0000 */
    public void selectBeautyType(int i) {
        SparseArray childMenuViewList = getChildMenuViewList();
        boolean z = childMenuViewList.size() > 1;
        ColorActivateTextView colorActivateTextView = this.mCurrentBeautyTextView;
        if (colorActivateTextView != null) {
            colorActivateTextView.setActivated(false);
        }
        if (childMenuViewList != null) {
            ColorActivateTextView colorActivateTextView2 = (ColorActivateTextView) childMenuViewList.get(i);
            if (colorActivateTextView2 != null) {
                if (z) {
                    colorActivateTextView2.setActivated(true);
                }
                this.mCurrentBeautyTextView = colorActivateTextView2;
            }
        }
    }

    public abstract void switchMenu();
}
