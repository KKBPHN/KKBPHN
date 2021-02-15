package com.android.camera.fragment.bottom;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;

@Deprecated
public class BeautyMenuGroupManager {
    private BeautyMenuAnimator mBeautyMenuAnimator;
    private SparseArray mBeautyMenuList;
    private AbBottomMenu mBottomMenu;
    private LinearLayout mContainerView;
    private Context mContext;
    private int mCurrentMenuGroupType = 160;
    private int mPortraitLightingVersion;

    public BeautyMenuGroupManager(Context context, LinearLayout linearLayout, int i) {
        this.mContainerView = linearLayout;
        this.mContext = context;
        this.mBeautyMenuAnimator = BeautyMenuAnimator.animator(this.mContainerView);
        this.mPortraitLightingVersion = i;
        initView();
    }

    private void initView() {
        updateCurrentMenu(getCurrentBeautyMenuType());
        this.mBottomMenu.addAllView();
    }

    private void updateCurrentMenu(int i) {
        AbBottomMenu frontBeautyMenu;
        SparseArray sparseArray = this.mBeautyMenuList;
        if (sparseArray != null) {
            AbBottomMenu abBottomMenu = (AbBottomMenu) sparseArray.get(i);
            if (abBottomMenu != null) {
                this.mBottomMenu = abBottomMenu;
                return;
            }
        }
        this.mBeautyMenuList = new SparseArray();
        switch (i) {
            case 161:
                frontBeautyMenu = new FrontBeautyMenu(this.mContext, this.mContainerView, this.mBeautyMenuAnimator);
                break;
            case 162:
                frontBeautyMenu = new BackBeautyMenu(this.mContext, this.mContainerView, this.mBeautyMenuAnimator);
                break;
            case 163:
                frontBeautyMenu = new LiveBeautyMenu(this.mContext, this.mContainerView, this.mBeautyMenuAnimator);
                break;
            case 164:
                frontBeautyMenu = new LiveStickerMenu(this.mContext, this.mContainerView, this.mBeautyMenuAnimator);
                break;
            case 165:
                frontBeautyMenu = new LiveSpeedMenu(this.mContext, this.mContainerView, this.mBeautyMenuAnimator);
                break;
            case 166:
                frontBeautyMenu = new KaleidoscopeMenu(this.mContext, this.mContainerView, this.mBeautyMenuAnimator);
                break;
            case 167:
                frontBeautyMenu = new PortraitLightingMenu(this.mContext, this.mContainerView, this.mBeautyMenuAnimator, this.mPortraitLightingVersion);
                break;
            case 168:
                frontBeautyMenu = new MimojiMenu(this.mContext, this.mContainerView, this.mBeautyMenuAnimator);
                break;
            default:
                frontBeautyMenu = new FrontBeautyMenu(this.mContext, this.mContainerView, this.mBeautyMenuAnimator);
                break;
        }
        this.mBottomMenu = frontBeautyMenu;
        this.mBeautyMenuList.put(i, frontBeautyMenu);
    }

    public void animateExpanding(boolean z) {
        BeautyMenuAnimator beautyMenuAnimator = this.mBeautyMenuAnimator;
        if (z) {
            beautyMenuAnimator.expandAnimate();
        } else {
            beautyMenuAnimator.shrinkAnimate();
        }
    }

    public AbBottomMenu getBottomMenu() {
        return this.mBottomMenu;
    }

    public int getCurrentBeautyMenuType() {
        return this.mCurrentMenuGroupType;
    }

    public View getView() {
        return this.mContainerView;
    }

    public boolean isVisible() {
        LinearLayout linearLayout = this.mContainerView;
        return linearLayout != null && linearLayout.getVisibility() == 0;
    }

    public void setCurrentBeautyMenuType(int i) {
        this.mCurrentMenuGroupType = i;
        updateCurrentMenu(i);
    }

    public void setVisibility(int i) {
        LinearLayout linearLayout = this.mContainerView;
        if (linearLayout != null) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) linearLayout.getLayoutParams();
            marginLayoutParams.setMarginStart(0);
            marginLayoutParams.setMarginEnd(0);
            this.mContainerView.setLayoutParams(marginLayoutParams);
            this.mContainerView.setVisibility(i);
        }
    }

    public void switchMenu() {
        this.mBottomMenu.switchMenu();
    }
}
