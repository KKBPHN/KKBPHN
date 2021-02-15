package com.android.camera.fragment.mode;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.folme.FolmeAlphaInOnSubscribe;
import com.android.camera.animation.folme.FolmeAlphaOutOnSubscribe;
import com.android.camera.customization.CustomizationActivity;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.global.DataItemGlobal.MoreModeTabStyle;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ModeListManager;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.MoreMode;
import com.android.camera.statistic.MistatsWrapper;
import java.util.List;

public class FragmentMoreModeNormal extends FragmentMoreModeBase {
    private static final String TAG = "MoreModeNormal";
    private View mEditIcon;
    private IMoreMode mMoreMode;
    private FragmentMoreModeTabV1 mMoreModeV1 = new FragmentMoreModeTabV1();
    private FragmentMoreModeTabV2 mMoreModeV2 = new FragmentMoreModeTabV2();
    private FrameLayout mNewMoreModeRoot;
    private ImageView mSwitchIcon;

    public FragmentMoreModeNormal() {
        this.mMoreMode = DataRepository.dataItemGlobal().useNewMoreTabStyle() ? this.mMoreModeV2 : this.mMoreModeV1;
    }

    private void switchStyle(View view) {
        Adapter adapter;
        int moreModeTabStyle = DataRepository.dataItemGlobal().getMoreModeTabStyle();
        switchType();
        View view2 = this.mRootView;
        if (view2 != null) {
            RecyclerView modeList = getModeList(view2);
            if (modeList.getAdapter() == null) {
                modeList.setVisibility(0);
                modeList.setAlpha(0.0f);
                super.initView(this.mRootView);
            }
            RecyclerView modeList2 = this.mMoreModeV1.getModeList(this.mRootView);
            RecyclerView modeList3 = this.mMoreModeV2.getModeList(this.mRootView);
            ImageView imageView = (ImageView) view;
            if (moreModeTabStyle == 0) {
                imageView.setImageResource(R.drawable.ic_more_mode_style_square);
                imageView.setBackgroundResource(R.drawable.ic_more_mode_style_square_shadow);
                moreModeTabStyle = 1;
            } else if (moreModeTabStyle == 1) {
                imageView.setImageResource(R.drawable.ic_more_mode_style_circle);
                imageView.setBackgroundResource(R.drawable.ic_more_mode_style_circle_shadow);
                moreModeTabStyle = 0;
            }
            updateSwitchIcon(moreModeTabStyle);
            DataRepository.dataItemGlobal().setMoreModeStyle(moreModeTabStyle);
            CameraStatUtils.trackSwitchTabStyle(moreModeTabStyle == 1 ? MoreMode.VALUE_ENTER_MORE_MODE_TAB_NEW : MoreMode.VALUE_ENTER_MORE_MODE_TAB_OLD);
            getModeAdapter().setRotate(this.mDegree);
            if (moreModeTabStyle != 0) {
                if (moreModeTabStyle == 1) {
                    this.mNewMoreModeRoot.setVisibility(0);
                    FolmeAlphaOutOnSubscribe.directSetResult(modeList2);
                    modeList2.scrollToPosition(0);
                    FolmeAlphaInOnSubscribe.directSetResult(modeList3);
                    if (modeList3.getAdapter() instanceof ModeAdapter) {
                        adapter = modeList3.getAdapter();
                    }
                }
                getModeAdapter().setAnimFlags(4);
            }
            FolmeAlphaOutOnSubscribe.directSetResult(modeList3);
            modeList3.scrollToPosition(0);
            this.mNewMoreModeRoot.setVisibility(8);
            FolmeAlphaInOnSubscribe.directSetResult(modeList2);
            if (modeList2.getAdapter() != null) {
                adapter = modeList2.getAdapter();
            }
            getModeAdapter().setAnimFlags(4);
            adapter.notifyDataSetChanged();
            getModeAdapter().setAnimFlags(4);
        }
    }

    private void updateLayout(View view, boolean z) {
        int i;
        LayoutParams layoutParams = (LayoutParams) ((ViewGroup) view.findViewById(R.id.more_mode_header)).getLayoutParams();
        layoutParams.height = Display.getTopBarHeight();
        layoutParams.topMargin = Display.getTopMargin();
        FrameLayout frameLayout = (FrameLayout) this.mRootView.findViewById(R.id.modes_contain);
        frameLayout.setClipChildren(false);
        LayoutParams layoutParams2 = (LayoutParams) frameLayout.getLayoutParams();
        if (Display.DISPLAY_RATIO_456.equals(Display.getDisplayRatio())) {
            Rect displayRect = Util.getDisplayRect();
            layoutParams2.width = -1;
            layoutParams2.height = displayRect.height();
        } else {
            layoutParams2.width = -1;
            layoutParams2.height = -1;
            layoutParams2.bottomMargin = Display.getBottomHeight();
        }
        LayoutParams layoutParams3 = (LayoutParams) frameLayout.getChildAt(0).getLayoutParams();
        if (Display.fitDisplayFull(1.3333333f)) {
            layoutParams2.topMargin = Display.getBottomBarHeight();
            i = 17;
        } else {
            layoutParams2.topMargin = Display.getTopHeight();
            i = 81;
        }
        layoutParams3.gravity = i;
        if (z) {
            view.requestLayout();
        }
    }

    private void updateSwitchIcon(@MoreModeTabStyle int i) {
        int i2;
        ImageView imageView;
        if (i == 0) {
            this.mSwitchIcon.setImageResource(R.drawable.ic_more_mode_style_circle);
            imageView = this.mSwitchIcon;
            i2 = R.drawable.ic_more_mode_style_circle_shadow;
        } else if (i == 1) {
            this.mSwitchIcon.setImageResource(R.drawable.ic_more_mode_style_square);
            imageView = this.mSwitchIcon;
            i2 = R.drawable.ic_more_mode_style_square_shadow;
        } else {
            return;
        }
        imageView.setBackgroundResource(i2);
    }

    public LayoutManager createLayoutManager(Context context) {
        return this.mMoreMode.createLayoutManager(context);
    }

    public ModeItemDecoration createModeItemDecoration(Context context, IMoreMode iMoreMode) {
        return this.mMoreMode.createModeItemDecoration(context, iMoreMode);
    }

    public int getCountPerLine() {
        return this.mMoreMode.getCountPerLine();
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_MODES_MORE_NORMAL;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_module_more;
    }

    public RecyclerView getModeList(View view) {
        return this.mMoreMode.getModeList(view);
    }

    public int getType() {
        return this.mMoreMode.getType();
    }

    /* access modifiers changed from: protected */
    public boolean hide() {
        ModeListManager modeListManager = (ModeListManager) ModeCoordinatorImpl.getInstance().getAttachProtocol(2560);
        if (modeListManager == null || !modeListManager.isMoreModeShowing(false)) {
            return false;
        }
        modeListManager.hideMoreMode(false);
        return true;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        int i = 8;
        this.mMoreModeV1.getModeList(view).setVisibility(DataRepository.dataItemGlobal().useNewMoreTabStyle() ? 8 : 0);
        this.mMoreModeV2.getModeList(view).setVisibility(DataRepository.dataItemGlobal().useNewMoreTabStyle() ? 0 : 8);
        super.initView(view);
        Log.d(TAG, "initView");
        updateLayout(view, false);
        ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.more_mode_header);
        if (!C0122O00000o.instance().OOo0oO()) {
            viewGroup.setVisibility(8);
            return;
        }
        viewGroup.setVisibility(0);
        this.mEditIcon = view.findViewById(R.id.more_mode_edit);
        this.mEditIcon.setOnClickListener(this);
        FolmeUtils.touchTint(this.mEditIcon);
        this.mSwitchIcon = (ImageView) view.findViewById(R.id.more_tab_switch);
        this.mSwitchIcon.setOnClickListener(this);
        FolmeUtils.touchTint((View) this.mSwitchIcon);
        this.mNewMoreModeRoot = (FrameLayout) view.findViewById(R.id.new_mode_root);
        FrameLayout frameLayout = this.mNewMoreModeRoot;
        if (DataRepository.dataItemGlobal().useNewMoreTabStyle()) {
            i = 0;
        }
        frameLayout.setVisibility(i);
        updateSwitchIcon(DataRepository.dataItemGlobal().getMoreModeTabStyle());
        MoreModeListAnimation.getInstance().initSwitchAnimation(this.mMoreModeV1, this.mMoreModeV2, getComponentModuleList().getMoreItems().size());
    }

    public void notifyAfterFrameAvailable(int i) {
        super.notifyAfterFrameAvailable(i);
        if (this.mCurrentMode != 254) {
            hide();
        }
    }

    public void onClick(View view) {
        super.onClick(view);
        int id = view.getId();
        if (id == R.id.more_mode_edit) {
            Intent intent = new Intent(getActivity(), CustomizationActivity.class);
            intent.putExtra("from_where", 254);
            intent.putExtra(":miui:starting_window_label", getResources().getString(R.string.pref_customization_title));
            String str = "StartActivityWhenLocked";
            if (getActivity().getIntent().getBooleanExtra(str, false)) {
                intent.putExtra(str, true);
            }
            startActivity(intent);
            String str2 = "attr_custom_camera";
            MistatsWrapper.settingClickEvent(str2, null);
            MistatsWrapper.customizeCameraSettingClick(str2);
        } else if (id == R.id.more_tab_switch) {
            switchStyle(view);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        MoreModeListAnimation.getInstance().releaseSwitchAnimation();
        if (getView() != null) {
            FolmeUtils.animateHide(getView());
        }
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (getModeAdapter() != null) {
            getModeAdapter().notifyDataSetChanged();
        }
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (view != null) {
            FolmeUtils.animationSlide(view, 1, 300);
        }
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
        if (C0122O00000o.instance().OOo0oO()) {
            list.add(this.mSwitchIcon);
            list.add(this.mEditIcon);
        }
        if (getModeAdapter() != null) {
            getModeAdapter().setRotate(i);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("provideRotateItem type ");
        sb.append(getType());
        Log.d(TAG, sb.toString());
        View view = this.mRootView;
        if (view != null) {
            RecyclerView modeList = getModeList(view);
            if (modeList != null) {
                int i2 = 0;
                if (getType() == 0) {
                    while (i2 < modeList.getChildCount()) {
                        list.add(modeList.getChildAt(i2));
                        i2++;
                    }
                } else if (getType() == 3) {
                    while (i2 < modeList.getChildCount()) {
                        if (modeList.getChildAt(i2) instanceof ViewGroup) {
                            ViewGroup viewGroup = (ViewGroup) modeList.getChildAt(i2);
                            if (viewGroup.getChildCount() >= 1) {
                                list.add(viewGroup.getChildAt(viewGroup.getChildCount() - 1));
                            }
                        }
                        i2++;
                    }
                }
            }
        }
    }

    public void switchType() {
        this.mMoreMode = getType() == 0 ? this.mMoreModeV2 : this.mMoreModeV1;
        StringBuilder sb = new StringBuilder();
        sb.append("switchType ");
        sb.append(getType());
        Log.d(TAG, sb.toString());
    }
}
