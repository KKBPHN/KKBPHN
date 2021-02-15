package com.android.camera.fragment.mode;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import androidx.annotation.CallSuper;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.MoreModePopupController;
import com.android.camera.ui.DragLayout;
import com.android.camera.ui.DragLayout.OnDragListener;
import java.util.List;

public class FragmentMoreModePopup extends FragmentMoreModeBase implements HandleBackTrace, OnDragListener {
    private static final String TAG = "MoreModePopup";
    private GradientDrawable mBgDrawable;
    private FrameLayout mContainView;
    private boolean mIsSupportOrientation;
    private Rect mListArea = new Rect();
    private boolean mNeedBgAlphaAnimation = false;
    private boolean mOnDragging;
    private float[] mRadiusArrays;
    private int mTargetAlpha = 255;

    private boolean isFullScreen() {
        boolean z = true;
        if (Display.fitDisplayFull(1.3333333f)) {
            return true;
        }
        if (DataRepository.dataItemRunning().getUiStyle() != 3) {
            z = false;
        }
        return z;
    }

    private boolean needBgAlphaAnimation() {
        return Display.needAlphaAnimation4PopMore() && (DataRepository.dataItemRunning().getUiStyle() == 3 || DataRepository.dataItemRunning().getUiStyle() == 1);
    }

    private boolean needChangeCornerRadii() {
        return !Display.fitDisplayFull(1.3333333f);
    }

    private void updateLayout(View view, boolean z) {
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.mode_more_top_margin);
        if (Display.fitDisplayFull(1.3333333f)) {
            LayoutParams layoutParams = (LayoutParams) this.mContainView.getLayoutParams();
            layoutParams.leftMargin = Display.getStartMargin() + getResources().getDimensionPixelOffset(R.dimen.mode_list_hor_padding_popup);
            layoutParams.rightMargin = Display.getEndMargin() + getResources().getDimensionPixelOffset(R.dimen.mode_list_hor_padding_popup);
            layoutParams.height = ((getResources().getDimensionPixelOffset(R.dimen.mode_item_height) * MoreModeHelper.getRow4PopupStyle(getComponentModuleList().getMoreItems().size())) - (getResources().getDimensionPixelOffset(R.dimen.mode_item_height) - getResources().getDimensionPixelOffset(R.dimen.mode_item_width))) + (dimensionPixelOffset * 2);
            LayoutParams layoutParams2 = (LayoutParams) this.mContainView.getChildAt(0).getLayoutParams();
            layoutParams2.topMargin = dimensionPixelOffset;
            layoutParams2.bottomMargin = dimensionPixelOffset;
            layoutParams2.gravity = 49;
        } else {
            LayoutParams layoutParams3 = (LayoutParams) this.mContainView.getChildAt(0).getLayoutParams();
            layoutParams3.topMargin = dimensionPixelOffset;
            layoutParams3.bottomMargin = Display.getNavigationBarHeight(getContext()) + ((int) DragLayout.getAnimationConfig().getSpringDistance());
            layoutParams3.gravity = 1;
        }
        if (z) {
            view.requestLayout();
        }
    }

    public boolean catchDrag(int i, int i2) {
        View view = this.mRootView;
        if (view == null) {
            return false;
        }
        RecyclerView modeList = getModeList(view);
        if (modeList == null || modeList.getVisibility() != 0) {
            return false;
        }
        modeList.getGlobalVisibleRect(this.mListArea);
        boolean contains = this.mListArea.contains(i, i2);
        StringBuilder sb = new StringBuilder();
        sb.append("catchDrag = ");
        sb.append(contains);
        Log.d(TAG, sb.toString());
        return contains;
    }

    public LayoutManager createLayoutManager(Context context) {
        return new GridLayoutManager(context, getCountPerLine(), 1, false);
    }

    public ModeItemDecoration createModeItemDecoration(Context context, IMoreMode iMoreMode) {
        return new ModeItemDecoration(context, iMoreMode, getType());
    }

    public int getCountPerLine() {
        return Display.getMoreModeTabCol(DataRepository.dataItemRunning().getUiStyle(), false);
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_MODES_MORE_POPUP;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_module_more_pop;
    }

    public RecyclerView getModeList(View view) {
        return (RecyclerView) view.findViewById(R.id.modes_content);
    }

    public int getType() {
        return 1;
    }

    /* access modifiers changed from: protected */
    public boolean hide() {
        return onBackEvent(5);
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        super.initView(view);
        this.mContainView = (FrameLayout) this.mRootView.findViewById(R.id.modes_contain);
        updateLayout(view, false);
        getModeList(this.mRootView).setVisibility(8);
    }

    public boolean onBackEvent(int i) {
        if (i != 1 || TextUtils.isEmpty(this.mDownloadingFeature)) {
            MoreModePopupController moreModePopupController = (MoreModePopupController) ModeCoordinatorImpl.getInstance().getAttachProtocol(2561);
            if (moreModePopupController == null || !moreModePopupController.isExpanded()) {
                return false;
            }
            return moreModePopupController.shrink(true);
        }
        showDownloadCancelDialog();
        return true;
    }

    public void onClick(View view) {
        if (view.getId() == R.id.mode_item && !this.mOnDragging) {
            View view2 = this.mRootView;
            if (view2 != null && getModeList(view2).getVisibility() == 0 && getModeList(this.mRootView).getAlpha() == 1.0f) {
                super.onClick(view);
            }
        }
    }

    public void onDragDone(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("onDragDone up=");
        sb.append(z);
        String sb2 = sb.toString();
        String str = TAG;
        Log.u(str, sb2);
        View view = this.mRootView;
        if (view != null && this.mContainView != null) {
            RecyclerView modeList = getModeList(view);
            if (modeList != null) {
                if (z) {
                    modeList.setAlpha(1.0f);
                } else {
                    modeList.setAlpha(0.0f);
                    modeList.setVisibility(8);
                    this.mContainView.setBackground(null);
                }
            }
            if (!this.mOnDragging) {
                Log.w(str, "damn, check this flag.");
            } else {
                this.mOnDragging = false;
            }
        }
    }

    public void onDragProgress(int i, boolean z) {
        if (this.mOnDragging) {
            if (z) {
                if (((float) i) < (-DragLayout.getAnimationConfig().getTotalDragDistance())) {
                    this.mOnDragging = false;
                    i = (int) (-DragLayout.getAnimationConfig().getTotalDragDistance());
                }
            } else if (i >= 0) {
                this.mOnDragging = false;
                i = 0;
            }
            if (this.mBgDrawable != null) {
                if (this.mRadiusArrays != null) {
                    int totalDragDistance = (int) (DragLayout.getAnimationConfig().getTotalDragDistance() / 2.0f);
                    if (Math.abs(i) > totalDragDistance) {
                        int floatValue = (int) (((Float) DragLayout.getAnimationConfig().getCornerRadiusRange().getUpper()).floatValue() * Math.min(1.0f, ((float) Math.abs(i + totalDragDistance)) / ((float) totalDragDistance)));
                        float[] fArr = this.mRadiusArrays;
                        float f = (float) floatValue;
                        fArr[3] = f;
                        fArr[2] = f;
                        fArr[1] = f;
                        fArr[0] = f;
                    }
                    this.mBgDrawable.setCornerRadii(this.mRadiusArrays);
                }
                if (this.mNeedBgAlphaAnimation) {
                    this.mBgDrawable.setAlpha((int) (((float) this.mTargetAlpha) * Math.min(((float) Math.abs(i)) / ((float) ((Float) DragLayout.getAnimationConfig().getDisplayRange().getUpper()).intValue()), 1.0f)));
                }
            }
            float min = Math.min(1.0f, ((float) ((int) (((float) Math.abs(i)) - DragLayout.getAnimationConfig().getDisappearDistance()))) / DragLayout.getAnimationConfig().getDisplayDistance());
            View view = this.mRootView;
            if (view != null) {
                getModeList(view).setAlpha(Math.min(1.0f, min + ((Float) DragLayout.getAnimationConfig().getDisplayAlphaRange().getLower()).floatValue()));
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x009a  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x009d  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00a9  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00c0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onDragStart(boolean z) {
        RecyclerView modeList;
        int i;
        GradientDrawable gradientDrawable;
        if (this.mContainView != null && this.mRootView != null) {
            if (this.mBgDrawable == null && getContext() != null) {
                if (needChangeCornerRadii()) {
                    this.mBgDrawable = (GradientDrawable) getContext().getDrawable(R.drawable.bg_more_modes);
                    this.mRadiusArrays = this.mBgDrawable.getCornerRadii();
                } else {
                    this.mBgDrawable = (GradientDrawable) getContext().getDrawable(R.drawable.bg_more_modes_pad);
                }
            }
            this.mContainView.setBackground(this.mBgDrawable);
            this.mNeedBgAlphaAnimation = needBgAlphaAnimation();
            if (isFullScreen()) {
                this.mTargetAlpha = 153;
                gradientDrawable = this.mBgDrawable;
                if (z) {
                    i = 0;
                    gradientDrawable.setAlpha(i);
                    modeList = getModeList(this.mRootView);
                    BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
                    if (!(baseDelegate == null || this.mDegree == baseDelegate.getAnimationComposite().getTargetDegree())) {
                        if (this.mIsSupportOrientation) {
                            this.mDegree = 0;
                        } else {
                            this.mDegree = baseDelegate.getAnimationComposite().getTargetDegree();
                        }
                        if (modeList != null) {
                            for (int i2 = 0; i2 < modeList.getChildCount(); i2++) {
                                modeList.getChildAt(i2).setRotation((float) this.mDegree);
                            }
                        }
                    }
                    if (modeList != null) {
                        if (z) {
                            float[] fArr = this.mRadiusArrays;
                            if (fArr != null) {
                                fArr[3] = 0.0f;
                                fArr[2] = 0.0f;
                                fArr[1] = 0.0f;
                                fArr[0] = 0.0f;
                            }
                            modeList.setVisibility(0);
                            modeList.scrollToPosition(0);
                            modeList.setAlpha(0.0f);
                        } else {
                            modeList.setAlpha(1.0f);
                        }
                    }
                    this.mOnDragging = true;
                }
            } else {
                this.mTargetAlpha = 255;
                if (!this.mNeedBgAlphaAnimation || !z) {
                    gradientDrawable = this.mBgDrawable;
                } else {
                    this.mBgDrawable.setAlpha(0);
                    modeList = getModeList(this.mRootView);
                    BaseDelegate baseDelegate2 = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
                    if (this.mIsSupportOrientation) {
                    }
                    if (modeList != null) {
                    }
                    if (modeList != null) {
                    }
                    this.mOnDragging = true;
                }
            }
            i = this.mTargetAlpha;
            gradientDrawable.setAlpha(i);
            modeList = getModeList(this.mRootView);
            BaseDelegate baseDelegate22 = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
            if (this.mIsSupportOrientation) {
            }
            if (modeList != null) {
            }
            if (modeList != null) {
            }
            this.mOnDragging = true;
        }
    }

    public boolean onInterceptDrag() {
        if (this.mContainView != null) {
            View view = this.mRootView;
            if (view != null && !this.mOnDragging && getModeList(view) != null && getModeList(this.mRootView).getVisibility() == 0) {
                boolean canScrollVertically = getModeList(this.mRootView).canScrollVertically(-1);
                StringBuilder sb = new StringBuilder();
                sb.append("canScrollDown = ");
                sb.append(canScrollVertically);
                Log.d(TAG, sb.toString());
                return canScrollVertically;
            }
        }
        return false;
    }

    public void onPause() {
        super.onPause();
        this.mOnDragging = false;
    }

    public void onResume() {
        super.onResume();
    }

    public void provideOrientationChanged(int i, List list, int i2) {
        super.provideOrientationChanged(i, list, i2);
        if (getView() == null) {
            return;
        }
        if (i == 0 || i == 1 || i == 2) {
            updateLayout(getView(), true);
        }
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        View view = this.mRootView;
        if (view != null) {
            RecyclerView modeList = getModeList(view);
            if (modeList != null) {
                for (int i2 = 0; i2 < modeList.getChildCount(); i2++) {
                    list.add(modeList.getChildAt(i2));
                }
            }
        }
        if (getModeAdapter() != null) {
            getModeAdapter().setRotate(i);
        }
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        Log.d(TAG, "register");
        registerBackStack(modeCoordinator, this);
    }

    public void setSupportOrientation(boolean z) {
        this.mIsSupportOrientation = z;
    }

    public boolean showDragAnimation(int i, int i2) {
        View view = this.mRootView;
        boolean z = false;
        if (view == null) {
            return false;
        }
        RecyclerView modeList = getModeList(view);
        if (modeList == null || Util.isInViewRegion(modeList, i, i2)) {
            return false;
        }
        if (modeList.getVisibility() == 0) {
            z = true;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        Log.d(TAG, "unRegister");
        unRegisterBackStack(modeCoordinator, this);
    }
}
