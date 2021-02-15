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
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.customization.TintColor;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.config.ComponentManuallyFocus;
import com.android.camera.data.data.config.ComponentManuallyWB;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.fragment.manually.ManuallyListener;
import com.android.camera.fragment.manually.adapter.ExtraCustomWBListAdapter;
import com.android.camera.fragment.manually.adapter.ExtraFocusAdapter;
import com.android.camera.fragment.manually.adapter.ExtraHorizontalListAdapter;
import com.android.camera.fragment.manually.adapter.ExtraRecyclerViewAdapter;
import com.android.camera.ui.ColorImageView;
import com.android.camera.ui.HorizontalZoomView;
import java.util.ArrayList;
import java.util.List;
import miui.view.animation.CubicEaseOutInterpolator;

public class FragmentFastmotionProExtra extends BaseFragment implements OnClickListener {
    public static final int FRAGMENT_INFO = 254;
    private ColorImageView mAutoButton;
    private int mCurrentTitle = -1;
    private ComponentData mData;
    private RecyclerView mExtraList;
    private FragmentFastmotionPro mFragmentFastmotionPro;
    private HorizontalZoomView mHorizontalView;
    private RelativeLayout mHorizontalViewLayout;
    private boolean mIsAnimateIng;
    private boolean mIsRecording;
    private ManuallyListener mManuallyListener;
    private boolean mNeedAnimation;
    private int mTargetKey;
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
        switch (componentData.getDisplayTitleString()) {
            case R.string.pref_camera_iso_title_abbr /*2131756363*/:
            case R.string.pref_camera_manually_exposure_value_abbr /*2131756400*/:
            case R.string.pref_manual_exposure_title_abbr /*2131756598*/:
                initHorizontalListView(componentData, C0124O00000oO.o00OO00());
                return;
            case R.string.pref_camera_whitebalance_title_abbr /*2131756539*/:
                initWBRecyclerView(componentData);
                return;
            case R.string.pref_camera_zoom_mode_title_abbr /*2131756555*/:
                initLensRecyclerView(componentData);
                return;
            case R.string.pref_qc_focus_position_title_abbr /*2131756624*/:
                initSlideFocusView(componentData);
                return;
            default:
                return;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x003a, code lost:
        if (r7 != com.android.camera.R.string.pref_manual_exposure_title_abbr) goto L_0x00ba;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initHorizontalListView(ComponentData componentData, boolean z) {
        HorizontalZoomView horizontalZoomView;
        int i;
        hideView();
        this.mTargetView = this.mHorizontalViewLayout;
        this.mTargetKey = componentData.getDisplayTitleString();
        int displayTitleString = componentData.getDisplayTitleString();
        String componentValue = componentData.getComponentValue(this.mCurrentMode);
        ExtraHorizontalListAdapter extraHorizontalListAdapter = new ExtraHorizontalListAdapter(getContext(), componentData, this.mCurrentMode, this.mManuallyListener);
        this.mHorizontalView.setDrawAdapter(extraHorizontalListAdapter, this.mDegree, false);
        if (displayTitleString != R.string.pref_camera_iso_title_abbr) {
            if (displayTitleString == R.string.pref_camera_manually_exposure_value_abbr) {
                ((MarginLayoutParams) this.mHorizontalView.getLayoutParams()).setMarginStart(0);
                this.mAutoButton.setVisibility(8);
                horizontalZoomView = this.mHorizontalView;
                i = (int) extraHorizontalListAdapter.mapValueToPosition(componentValue);
                horizontalZoomView.setSelection(i, true);
                this.mHorizontalView.setListener(extraHorizontalListAdapter, null);
                animateShowView(this.mHorizontalViewLayout);
            }
        }
        ((MarginLayoutParams) this.mHorizontalView.getLayoutParams()).setMarginStart(getResources().getDimensionPixelSize(R.dimen.manual_extra_horizontal_view_left));
        this.mAutoButton.setVisibility(0);
        this.mAutoButton.setRotation((float) this.mDegree);
        this.mAutoButton.setImageResource(R.drawable.ic_manu_auto_normal);
        this.mAutoButton.setBackgroundResource(R.drawable.ic_manu_auto_normal_shadow);
        this.mAutoButton.setContentDescription(getString(R.string.pref_video_focusmode_entryvalue_auto));
        if (componentValue.equals("0")) {
            this.mAutoButton.setColorAndRefresh(TintColor.tintColor());
            horizontalZoomView = this.mHorizontalView;
            i = -1;
            horizontalZoomView.setSelection(i, true);
            this.mHorizontalView.setListener(extraHorizontalListAdapter, null);
            animateShowView(this.mHorizontalViewLayout);
        }
        this.mAutoButton.setColor(0);
        this.mAutoButton.setIsNeedTransparent(true, true);
        horizontalZoomView = this.mHorizontalView;
        i = (int) extraHorizontalListAdapter.mapValueToPosition(componentValue);
        horizontalZoomView.setSelection(i, true);
        this.mHorizontalView.setListener(extraHorizontalListAdapter, null);
        animateShowView(this.mHorizontalViewLayout);
    }

    private void initLensRecyclerView(ComponentData componentData) {
        hideView();
        RecyclerView recyclerView = this.mExtraList;
        this.mTargetView = recyclerView;
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) recyclerView.getLayoutParams();
        marginLayoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.manual_extra_recyclerview_lens_margin_bottom);
        marginLayoutParams.height = getResources().getDimensionPixelSize(R.dimen.manual_extra_recyclerview_lens_height);
        AnonymousClass2 r2 = new ExtraRecyclerViewAdapter(this.mFragmentFastmotionPro, componentData, this.mCurrentMode, this.mManuallyListener, marginLayoutParams.height, this.mDegree) {
            /* access modifiers changed from: protected */
            public boolean couldNewValueTakeEffect(String str) {
                if (str == null || !str.equals("manual")) {
                    return super.couldNewValueTakeEffect(str);
                }
                return true;
            }
        };
        LinearLayoutManagerWrapper linearLayoutManagerWrapper = new LinearLayoutManagerWrapper(getContext(), "manually_extra_list");
        linearLayoutManagerWrapper.setOrientation(0);
        this.mExtraList.setLayoutManager(linearLayoutManagerWrapper);
        if (this.mExtraList.getItemDecorationCount() != 0) {
            this.mExtraList.removeItemDecorationAt(0);
        }
        this.mExtraList.addItemDecoration(new ItemPadding(getResources().getDimensionPixelSize(R.dimen.manual_extra_recyclerview_lens_item_padding)));
        this.mExtraList.setAdapter(r2);
        animateShowView(this.mExtraList);
    }

    private void initSlideFocusView(ComponentData componentData) {
        hideView();
        this.mTargetView = this.mHorizontalViewLayout;
        this.mTargetKey = componentData.getDisplayTitleString();
        ExtraFocusAdapter extraFocusAdapter = new ExtraFocusAdapter(getContext(), componentData, this.mCurrentMode, this.mManuallyListener);
        this.mHorizontalView.setDrawAdapter(extraFocusAdapter, this.mDegree, false);
        int intValue = Integer.valueOf(componentData.getComponentValue(this.mCurrentMode)).intValue();
        this.mAutoButton.setImageResource(R.drawable.ic_manu_auto_normal);
        this.mAutoButton.setBackgroundResource(R.drawable.ic_manu_auto_normal_shadow);
        this.mAutoButton.setContentDescription(getString(R.string.pref_video_focusmode_entryvalue_auto));
        this.mAutoButton.setVisibility(0);
        this.mAutoButton.setRotation((float) this.mDegree);
        if (intValue == 1000) {
            this.mAutoButton.setColorAndRefresh(TintColor.tintColor());
            this.mHorizontalView.setSelection(-1, true);
        } else {
            this.mAutoButton.setColor(0);
            this.mAutoButton.setIsNeedTransparent(true, true);
            this.mHorizontalView.setSelection((int) extraFocusAdapter.mapValueToPosition(Integer.valueOf(intValue)), true);
        }
        ((MarginLayoutParams) this.mHorizontalView.getLayoutParams()).setMarginStart(getResources().getDimensionPixelSize(R.dimen.manual_extra_horizontal_view_left));
        this.mHorizontalView.setListener(extraFocusAdapter, null);
        animateShowView(this.mHorizontalViewLayout);
    }

    private void initWBRecyclerView(ComponentData componentData) {
        hideView();
        RecyclerView recyclerView = this.mExtraList;
        this.mTargetView = recyclerView;
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) recyclerView.getLayoutParams();
        marginLayoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.manual_extra_recyclerview_margin_bottom);
        marginLayoutParams.height = getResources().getDimensionPixelSize(R.dimen.manual_extra_recyclerview_height);
        AnonymousClass1 r2 = new ExtraRecyclerViewAdapter(componentData, this.mCurrentMode, this.mManuallyListener, marginLayoutParams.height, this.mDegree) {
            /* access modifiers changed from: protected */
            public boolean couldNewValueTakeEffect(String str) {
                if (str == null || !str.equals("manual")) {
                    return super.couldNewValueTakeEffect(str);
                }
                return true;
            }
        };
        LinearLayoutManagerWrapper linearLayoutManagerWrapper = new LinearLayoutManagerWrapper(getContext(), "manually_extra_list");
        linearLayoutManagerWrapper.setOrientation(0);
        this.mExtraList.setLayoutManager(linearLayoutManagerWrapper);
        if (this.mExtraList.getItemDecorationCount() != 0) {
            this.mExtraList.removeItemDecorationAt(0);
        }
        this.mExtraList.addItemDecoration(new ItemPadding(getResources().getDimensionPixelSize(R.dimen.manual_extra_recyclerview_wb_item_padding)));
        this.mExtraList.setAdapter(r2);
        animateShowView(this.mExtraList);
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

    public /* synthetic */ void O000oOo() {
        FragmentUtils.removeFragmentByTag(getFragmentManager(), getFragmentTag());
        this.mCurrentTitle = -1;
    }

    public void animateOut() {
        View view = getView();
        if (view != null) {
            animateParentInOrOut(view, false, new O0000Oo0(this));
        }
    }

    public int getCurrentTitle() {
        return this.mCurrentTitle;
    }

    public int getFragmentInto() {
        return 254;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_manually_extra;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.setMarginStart(Display.getStartMargin());
        marginLayoutParams.setMarginEnd(Display.getEndMargin());
        this.mExtraList = (RecyclerView) view.findViewById(R.id.manually_extra_list);
        this.mExtraList.setFocusable(false);
        this.mHorizontalView = (HorizontalZoomView) view.findViewById(R.id.manually_extra_horizontal_view);
        this.mHorizontalViewLayout = (RelativeLayout) view.findViewById(R.id.manually_extra_horizontal_layout);
        this.mAutoButton = (ColorImageView) view.findViewById(R.id.manually_extra_auto_button);
        this.mAutoButton.setOnClickListener(this);
        ComponentData componentData = this.mData;
        if (componentData != null) {
            initAdapter(componentData);
            this.mCurrentTitle = this.mData.getDisplayTitleString();
        }
    }

    public void notifyDataChanged(int i, int i2) {
        super.notifyDataChanged(i, i2);
        if (this.mExtraList.getAdapter() != null) {
            this.mExtraList.getAdapter().notifyDataSetChanged();
        }
    }

    /* JADX WARNING: type inference failed for: r5v0, types: [com.android.camera.data.data.ComponentData] */
    /* JADX WARNING: type inference failed for: r5v1, types: [com.android.camera.data.data.ComponentData, com.android.camera.data.data.config.ComponentManuallyISO] */
    /* JADX WARNING: type inference failed for: r5v2, types: [com.android.camera.data.data.config.ComponentManuallyET, com.android.camera.data.data.ComponentData] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r5v1, types: [com.android.camera.data.data.ComponentData, com.android.camera.data.data.config.ComponentManuallyISO]
  assigns: [com.android.camera.data.data.config.ComponentManuallyISO, com.android.camera.data.data.config.ComponentManuallyET]
  uses: [com.android.camera.data.data.config.ComponentManuallyISO, com.android.camera.data.data.ComponentData, com.android.camera.data.data.config.ComponentManuallyET]
  mth insns count: 70
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClick(View view) {
        String str;
        ? r5;
        ManuallyListener manuallyListener;
        String str2 = "0";
        switch (this.mTargetKey) {
            case R.string.pref_camera_iso_title_abbr /*2131756363*/:
                ((ColorImageView) view).setColorAndRefresh(TintColor.tintColor());
                this.mHorizontalView.setSelection(-1, true);
                r5 = DataRepository.dataItemConfig().getmComponentManuallyISO();
                str = r5.getComponentValue(this.mCurrentMode);
                r5.setComponentValue(this.mCurrentMode, str2);
                this.mHorizontalView.getDrawAdapter().setCurrentValue(str2);
                manuallyListener = this.mManuallyListener;
                if (manuallyListener == null) {
                    return;
                }
                break;
            case R.string.pref_camera_whitebalance_title_abbr /*2131756539*/:
                toShowOrHideView(this.mHorizontalViewLayout, this.mExtraList, false);
                return;
            case R.string.pref_manual_exposure_title_abbr /*2131756598*/:
                ((ColorImageView) view).setColorAndRefresh(TintColor.tintColor());
                this.mHorizontalView.setSelection(-1, true);
                r5 = DataRepository.dataItemConfig().getmComponentManuallyET();
                str = r5.getComponentValue(this.mCurrentMode);
                r5.setComponentValue(this.mCurrentMode, str2);
                this.mHorizontalView.getDrawAdapter().setCurrentValue(str2);
                manuallyListener = this.mManuallyListener;
                if (manuallyListener == null) {
                    return;
                }
                break;
            case R.string.pref_qc_focus_position_title_abbr /*2131756624*/:
                ((ColorImageView) view).setColorAndRefresh(TintColor.tintColor());
                this.mHorizontalView.setSelection(-1, true);
                ComponentManuallyFocus manuallyFocus = DataRepository.dataItemConfig().getManuallyFocus();
                String componentValue = manuallyFocus.getComponentValue(this.mCurrentMode);
                manuallyFocus.setComponentValue(this.mCurrentMode, String.valueOf(1000));
                this.mHorizontalView.getDrawAdapter().setCurrentValue(String.valueOf(1000));
                ManuallyListener manuallyListener2 = this.mManuallyListener;
                if (manuallyListener2 != null) {
                    manuallyListener2.onManuallyDataChanged(manuallyFocus, componentValue, String.valueOf(1000), false, this.mCurrentMode);
                    return;
                }
                return;
            default:
                return;
        }
        manuallyListener.onManuallyDataChanged(r5, str, "0", false, this.mCurrentMode);
    }

    public void onPause() {
        super.onPause();
        ColorImageView colorImageView = this.mAutoButton;
        if (colorImageView != null) {
            FolmeUtils.clean(colorImageView);
        }
        View view = this.mTargetView;
        if (view != null) {
            FolmeUtils.clean(view);
        }
        if (getView() != null) {
            FolmeUtils.clean(getView());
        }
    }

    public void onResume() {
        super.onResume();
        ColorImageView colorImageView = this.mAutoButton;
        if (colorImageView != null) {
            FolmeUtils.touchTint((View) colorImageView);
        }
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
        RecyclerView recyclerView = this.mExtraList;
        if (recyclerView != null) {
            Adapter adapter = recyclerView.getAdapter();
            if (adapter != null && (adapter instanceof ExtraRecyclerViewAdapter)) {
                ((ExtraRecyclerViewAdapter) adapter).setDegree(i);
            }
            for (int i2 = 0; i2 < this.mExtraList.getChildCount(); i2++) {
                list.add(this.mExtraList.getChildAt(i2));
            }
        }
        HorizontalZoomView horizontalZoomView = this.mHorizontalView;
        if (horizontalZoomView != null) {
            horizontalZoomView.setRotate(i);
        }
        ColorImageView colorImageView = this.mAutoButton;
        if (colorImageView != null) {
            if (this.mTargetKey == R.string.pref_camera_whitebalance_title_abbr) {
                int i3 = this.mDegree;
                if (i3 == 0 || i3 == 90) {
                    this.mAutoButton.setRotation(0.0f);
                } else {
                    colorImageView.setRotation(180.0f);
                }
                return;
            }
            list.add(colorImageView);
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

    public void setmFragmentManually(FragmentFastmotionPro fragmentFastmotionPro) {
        this.mFragmentFastmotionPro = fragmentFastmotionPro;
    }

    public void showCustomWB(ComponentManuallyWB componentManuallyWB, boolean z) {
        float f;
        ColorImageView colorImageView;
        this.mTargetView = this.mHorizontalViewLayout;
        this.mTargetKey = componentManuallyWB.getDisplayTitleString();
        ExtraCustomWBListAdapter extraCustomWBListAdapter = new ExtraCustomWBListAdapter(getContext(), componentManuallyWB, this.mCurrentMode, z, this.mManuallyListener);
        this.mHorizontalView.setDrawAdapter(extraCustomWBListAdapter, this.mDegree, false);
        int mapValueToPosition = (int) extraCustomWBListAdapter.mapValueToPosition(Integer.valueOf(componentManuallyWB.getCustomWB(this.mCurrentMode)));
        this.mHorizontalView.setListener(extraCustomWBListAdapter, null);
        ((MarginLayoutParams) this.mHorizontalView.getLayoutParams()).setMarginStart(getResources().getDimensionPixelSize(R.dimen.manual_extra_horizontal_view_left));
        this.mHorizontalView.setSelection(mapValueToPosition, true);
        this.mAutoButton.setColor(0);
        this.mAutoButton.setIsNeedTransparent(true, true);
        this.mAutoButton.setImageResource(R.drawable.ic_manually_extra_back_button);
        this.mAutoButton.setBackgroundResource(R.drawable.ic_manually_extra_back_button_shadow);
        this.mAutoButton.setContentDescription(getString(R.string.mimoji_back));
        this.mAutoButton.setVisibility(0);
        int i = this.mDegree;
        if (i == 0 || i == 90) {
            colorImageView = this.mAutoButton;
            f = 0.0f;
        } else {
            colorImageView = this.mAutoButton;
            f = 180.0f;
        }
        colorImageView.setRotation(f);
        toShowOrHideView(this.mExtraList, this.mHorizontalViewLayout, true);
    }

    public void toUpdateAutoButton() {
        ColorImageView colorImageView = this.mAutoButton;
        if (colorImageView != null) {
            colorImageView.setColor(0);
            this.mAutoButton.setIsNeedTransparent(true, true);
        }
    }

    public void updateData() {
        this.mCurrentMode = DataRepository.dataItemGlobal().getCurrentMode();
        initAdapter(this.mData);
    }

    public void updateRecordingState(boolean z) {
        this.mIsRecording = z;
        RecyclerView recyclerView = this.mExtraList;
    }
}
