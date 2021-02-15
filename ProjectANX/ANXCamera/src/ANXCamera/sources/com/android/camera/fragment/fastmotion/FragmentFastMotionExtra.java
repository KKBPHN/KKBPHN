package com.android.camera.fragment.fastmotion;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Property;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.manually.ManuallyListener;
import com.android.camera.fragment.manually.adapter.ExtraHorizontalListAdapter;
import com.android.camera.ui.HorizontalZoomView;
import java.util.ArrayList;
import java.util.List;
import miui.view.animation.CubicEaseOutInterpolator;

public class FragmentFastMotionExtra extends BaseFragment {
    public static final int FRAGMENT_INFO = 16777202;
    private int mCurrentTitle = -1;
    private ComponentData mData;
    private HorizontalZoomView mHorizontalView;
    private RelativeLayout mHorizontalViewLayout;
    private ManuallyListener mManuallyListener;
    private boolean mNeedAnimation;
    private View mTargetView;

    public class ItemPadding extends ItemDecoration {
        protected int mPadding;

        public ItemPadding(int i) {
            this.mPadding = i;
        }

        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
            int i = this.mPadding;
            rect.set(i, 0, i, 0);
        }
    }

    private void animateParentInOrOut(View view, boolean z, Runnable runnable) {
        if (z) {
            FolmeUtils.animateEntrance(view);
        } else {
            FolmeUtils.animateDeparture(view, runnable);
        }
    }

    private void animateShowView(View view) {
        view.setVisibility(0);
        if (!this.mNeedAnimation) {
            FolmeUtils.animateShow(view);
        }
    }

    private void hideView() {
        View view = this.mTargetView;
        if (view != null) {
            view.clearAnimation();
            FolmeUtils.clean(this.mTargetView);
            this.mTargetView.setVisibility(8);
        }
    }

    private void initAdapter(ComponentData componentData) {
        initHorizontalListView(componentData, C0124O00000oO.o00OO00());
    }

    private void initHorizontalListView(ComponentData componentData, boolean z) {
        hideView();
        this.mTargetView = this.mHorizontalViewLayout;
        String componentValue = componentData.getComponentValue(this.mCurrentMode);
        ExtraHorizontalListAdapter extraHorizontalListAdapter = new ExtraHorizontalListAdapter(getContext(), componentData, this.mCurrentMode, this.mManuallyListener);
        this.mHorizontalView.setDrawAdapter(extraHorizontalListAdapter, this.mDegree, false);
        this.mHorizontalView.getLayoutParams().width = Display.getWindowWidth();
        this.mHorizontalView.setSelection((int) extraHorizontalListAdapter.mapValueToPosition(componentValue), true);
        this.mHorizontalView.setListener(extraHorizontalListAdapter, null);
        animateShowView(this.mHorizontalViewLayout);
    }

    private void toShowOrHideView(final View view, final View view2, boolean z) {
        Property property;
        float[] fArr;
        this.mTargetView = view2;
        ArrayList arrayList = new ArrayList();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.manually_recycler_view_height);
        arrayList.add(ObjectAnimator.ofFloat(view2, View.ALPHA, new float[]{0.0f, 1.0f}));
        if (z) {
            property = View.TRANSLATION_X;
            fArr = new float[]{(float) dimensionPixelSize, 0.0f};
        } else {
            property = View.TRANSLATION_X;
            fArr = new float[]{(float) (-dimensionPixelSize), 0.0f};
        }
        arrayList.add(ObjectAnimator.ofFloat(view2, property, fArr));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(arrayList);
        animatorSet.setInterpolator(new CubicEaseOutInterpolator());
        animatorSet.setDuration(400).addListener(new AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
                view.setVisibility(8);
                view2.setVisibility(0);
                ViewCompat.setAlpha(view2, 1.0f);
                ViewCompat.setTranslationX(view2, 0.0f);
            }

            public void onAnimationEnd(Animator animator) {
                view.setVisibility(8);
                view2.setVisibility(0);
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
                view.setVisibility(8);
                view2.setVisibility(0);
            }
        });
        animatorSet.start();
    }

    public /* synthetic */ void O000oOo0() {
        FragmentUtils.removeFragmentByTag(getFragmentManager(), getFragmentTag());
        this.mCurrentTitle = -1;
    }

    public void animateOut() {
        View view = getView();
        if (view != null) {
            animateParentInOrOut(view, false, new O00000o0(this));
        }
    }

    public int getCurrentTitle() {
        return this.mCurrentTitle;
    }

    public int getFragmentInto() {
        return 16777202;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_fastmotion_extra;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mHorizontalView = (HorizontalZoomView) view.findViewById(R.id.manually_extra_horizontal_view);
        this.mHorizontalViewLayout = (RelativeLayout) view.findViewById(R.id.manually_extra_horizontal_layout);
        ComponentData componentData = this.mData;
        if (componentData != null) {
            initAdapter(componentData);
            this.mCurrentTitle = this.mData.getDisplayTitleString();
        }
    }

    public void notifyDataChanged(int i, int i2) {
        super.notifyDataChanged(i, i2);
    }

    public void onPause() {
        super.onPause();
        View view = this.mTargetView;
        if (view != null) {
            FolmeUtils.clean(view);
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (this.mNeedAnimation) {
            this.mNeedAnimation = false;
            animateParentInOrOut(view, true, null);
        }
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        HorizontalZoomView horizontalZoomView = this.mHorizontalView;
        if (horizontalZoomView != null) {
            horizontalZoomView.setRotate(i);
        }
    }

    public void resetData(ComponentData componentData) {
        this.mData = componentData;
        initAdapter(this.mData);
        this.mCurrentTitle = this.mData.getDisplayTitleString();
    }

    public void setComponentData(ComponentData componentData, int i, boolean z, ManuallyListener manuallyListener) {
        this.mData = componentData;
        this.mCurrentMode = i;
        this.mNeedAnimation = z;
        this.mManuallyListener = manuallyListener;
    }

    public void updateData() {
        this.mCurrentMode = DataRepository.dataItemGlobal().getCurrentMode();
        initAdapter(this.mData);
    }
}
