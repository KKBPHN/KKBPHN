package com.android.camera.fragment.manually;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;
import com.android.camera.ActivityBase;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.type.AlphaInOnSubscribe;
import com.android.camera.animation.type.AlphaOutOnSubscribe;
import com.android.camera.animation.type.BaseOnSubScribe;
import com.android.camera.animation.type.SlideInOnSubscribe;
import com.android.camera.animation.type.SlideOutOnSubscribe;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.config.ComponentManuallyDualLens;
import com.android.camera.data.data.config.ComponentManuallyET;
import com.android.camera.data.data.config.ComponentManuallyEV;
import com.android.camera.data.data.config.ComponentManuallyFocus;
import com.android.camera.data.data.config.ComponentManuallyISO;
import com.android.camera.data.data.config.ComponentManuallyWB;
import com.android.camera.data.data.config.DataItemConfig;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.fragment.manually.adapter.ExtraRecyclerViewAdapter;
import com.android.camera.fragment.manually.adapter.ManuallyAdapter;
import com.android.camera.fragment.manually.adapter.ManuallySingleAdapter;
import com.android.camera.lib.compatibility.related.vibrator.ViberatorContext;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ManuallyAdjust;
import com.android.camera.protocol.ModeProtocol.ManuallyValueChanged;
import com.android.camera.protocol.ModeProtocol.MasterFilterProtocol;
import com.android.camera.protocol.ModeProtocol.MiBeautyProtocol;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera2.CameraCapabilities;
import io.reactivex.Completable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FragmentManually extends BaseFragment implements OnClickListener, HandleBackTrace, ManuallyAdjust, ManuallyListener {
    private static final String TAG = "FragmentManually";
    private Adapter mAdapter;
    private int mCurrentAdjustType = -1;
    private FragmentManuallyExtra mFragmentManuallyExtra;
    private boolean mIsSuperEISEnabled;
    private boolean mIsVideoRecording;
    private List mManuallyComponents;
    private ViewGroup mManuallyParent;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    private float mRecyclerViewItemWidth;

    public class ItemPadding extends ItemDecoration {
        protected int padding;

        public ItemPadding(Context context) {
            this.padding = context.getResources().getDimensionPixelSize(R.dimen.manual_recyclerview_item_padding);
        }

        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
            int i = this.padding;
            rect.set(i, 0, i, 0);
        }
    }

    private FragmentManuallyExtra getExtraFragment() {
        FragmentManuallyExtra fragmentManuallyExtra = this.mFragmentManuallyExtra;
        if (fragmentManuallyExtra == null || !fragmentManuallyExtra.isAdded()) {
            return null;
        }
        return this.mFragmentManuallyExtra;
    }

    private void hideTips() {
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
            bottomPopupTips.directlyHideTips();
        }
    }

    private void initManually() {
        initManuallyDataList();
        ManuallyAdapter manuallyAdapter = new ManuallyAdapter(this.mCurrentMode, this, this.mManuallyComponents, Math.round(this.mRecyclerViewItemWidth));
        FragmentManuallyExtra extraFragment = getExtraFragment();
        if (extraFragment != null) {
            extraFragment.updateData();
            manuallyAdapter.setSelectedTitle(extraFragment.getCurrentTitle());
        }
        this.mAdapter = manuallyAdapter;
        ((ManuallyAdapter) this.mAdapter).setRotate(this.mDegree);
    }

    private List initManuallyDataList() {
        List list = this.mManuallyComponents;
        if (list == null) {
            this.mManuallyComponents = new ArrayList();
        } else {
            list.clear();
        }
        DataItemConfig dataItemConfig = DataRepository.dataItemConfig();
        this.mManuallyComponents.add(dataItemConfig.getmComponentManuallyWB());
        if (C0124O00000oO.Oo00o00()) {
            ComponentManuallyFocus manuallyFocus = dataItemConfig.getManuallyFocus();
            CameraCapabilities currentCameraCapabilities = Camera2DataContainer.getInstance().getCurrentCameraCapabilities();
            if (currentCameraCapabilities != null) {
                manuallyFocus.setFixedFocusLens(!currentCameraCapabilities.isAFRegionSupported());
            }
            this.mManuallyComponents.add(manuallyFocus);
            this.mManuallyComponents.add(dataItemConfig.getmComponentManuallyET());
        }
        this.mManuallyComponents.add(dataItemConfig.getmComponentManuallyISO());
        this.mManuallyComponents.add(dataItemConfig.getComponentManuallyEV());
        if ((CameraSettings.isSupportedOpticalZoom() || C0122O00000o.instance().isSupportUltraWide()) && !this.mIsVideoRecording) {
            this.mManuallyComponents.add(dataItemConfig.getManuallyDualLens());
        }
        return this.mManuallyComponents;
    }

    private int initRecyclerView(int i, ManuallyListener manuallyListener) {
        this.mCurrentAdjustType = i;
        if (i != 0) {
            if (i == 1) {
                this.mCurrentAdjustType = 1;
                initManually();
            } else if (i == 2) {
                this.mCurrentAdjustType = 2;
                initScene(manuallyListener);
            } else if (i == 3) {
                this.mCurrentAdjustType = 3;
                initTilt(manuallyListener);
            }
            return this.mRecyclerView.getLayoutParams().height;
        }
        this.mCurrentAdjustType = 0;
        this.mManuallyParent.setVisibility(4);
        this.mAdapter = null;
        return 0;
    }

    private void initScene(ManuallyListener manuallyListener) {
        ExtraRecyclerViewAdapter extraRecyclerViewAdapter = new ExtraRecyclerViewAdapter(DataRepository.dataItemRunning().getComponentRunningSceneValue(), this.mCurrentMode, manuallyListener, Math.round(this.mRecyclerViewItemWidth), this.mDegree);
        this.mAdapter = extraRecyclerViewAdapter;
    }

    private void initTilt(ManuallyListener manuallyListener) {
        this.mAdapter = new ManuallySingleAdapter(DataRepository.dataItemRunning().getComponentRunningTiltValue(), this.mCurrentMode, manuallyListener, Math.round(this.mRecyclerViewItemWidth));
    }

    private boolean isReinitNeeded(int i, int i2, int i3) {
        if (this.mCurrentAdjustType != i) {
            return true;
        }
        if (i3 != 167) {
            if (i3 == 180 && i2 == 167) {
                return true;
            }
        } else if (i2 == 180) {
            return true;
        }
        return this.mIsSuperEISEnabled != CameraSettings.isMovieSolidOn();
    }

    private void performFocusValueChangedViberator(String str, String str2) {
        if (!TextUtils.equals(str, str2)) {
            try {
                if (Integer.parseInt(str2) % 100 == 0) {
                    ViberatorContext.getInstance(getContext().getApplicationContext()).performFocusValueLargeChangedInManual();
                } else {
                    ViberatorContext.getInstance(getContext().getApplicationContext()).performFocusValueLightChangedInManual();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0012, code lost:
        if (r1 != 1) goto L_0x0019;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void reInitManuallyLayout(int i, int i2, int i3, List list) {
        if (isReinitNeeded(i, i2, i3)) {
            this.mIsSuperEISEnabled = CameraSettings.isMovieSolidOn();
            this.mCurrentAdjustType = i;
            int i4 = i != 0 ? 1 : 0;
            initRecyclerView(i4, this);
            if (i == 0 && list != null && (i2 == 167 || i2 == 180 || this.mManuallyParent.getVisibility() == 0)) {
                FolmeUtils.clean(this.mManuallyParent);
                FolmeUtils.animateHide(this.mManuallyParent);
            }
        }
    }

    private void removeExtra() {
        if (this.mFragmentManuallyExtra != null) {
            FragmentTransaction beginTransaction = getChildFragmentManager().beginTransaction();
            beginTransaction.remove(this.mFragmentManuallyExtra);
            beginTransaction.commitAllowingStateLoss();
        }
    }

    private void setManuallyLayoutViewVisible(int i) {
        AccelerateDecelerateInterpolator accelerateDecelerateInterpolator;
        BaseOnSubScribe baseOnSubScribe;
        FragmentUtils.removeFragmentByTag(getChildFragmentManager(), String.valueOf(254));
        if (i == 1) {
            baseOnSubScribe = new SlideInOnSubscribe(this.mManuallyParent, 80).setDurationTime(af.bY);
            accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
        } else if (i == 2) {
            this.mCurrentAdjustType = 0;
            baseOnSubScribe = new SlideOutOnSubscribe(this.mManuallyParent, 80).setDurationTime(af.bY);
            accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
        } else if (i == 3) {
            this.mCurrentAdjustType = 0;
            SlideOutOnSubscribe.directSetResult(this.mManuallyParent, 80);
            return;
        } else if (i == 4) {
            this.mCurrentAdjustType = 0;
            BaseOnSubScribe.setAnimateViewVisible(this.mManuallyParent, 4);
            return;
        } else {
            return;
        }
        Completable.create(baseOnSubScribe.setInterpolator(accelerateDecelerateInterpolator)).subscribe();
    }

    private void updateEVState(int i) {
        DataItemConfig dataItemConfig = DataRepository.dataItemConfig();
        ComponentManuallyEV componentManuallyEV = dataItemConfig.getComponentManuallyEV();
        ComponentManuallyET componentManuallyET = dataItemConfig.getmComponentManuallyET();
        ComponentManuallyISO componentManuallyISO = dataItemConfig.getmComponentManuallyISO();
        String componentValue = componentManuallyISO.getComponentValue(i);
        String componentValue2 = componentManuallyET.getComponentValue(i);
        boolean z = true;
        if (Long.parseLong(componentValue2) <= 125000000 && (componentValue2.equals(componentManuallyET.getDefaultValue(i)) || componentValue.equals(componentManuallyISO.getDefaultValue(i)))) {
            z = false;
        }
        componentManuallyEV.setDisabled(z);
        if (z) {
            FragmentManuallyExtra fragmentManuallyExtra = this.mFragmentManuallyExtra;
            if (fragmentManuallyExtra != null && fragmentManuallyExtra.getCurrentTitle() == R.string.pref_camera_manually_exposure_value_abbr) {
                removeExtra();
            }
        }
    }

    public void forceUpdateManualView(boolean z) {
        ViewGroup viewGroup = this.mManuallyParent;
        if (viewGroup != null) {
            Completable.create(z ? new AlphaInOnSubscribe(viewGroup) : new AlphaOutOnSubscribe(viewGroup)).subscribe();
        }
    }

    public int getFragmentInto() {
        return 247;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_manually;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.bottomMargin = Display.getBottomHeight();
        marginLayoutParams.setMarginStart(Display.getStartMargin());
        marginLayoutParams.setMarginEnd(Display.getEndMargin());
        this.mManuallyParent = (ViewGroup) view.findViewById(R.id.manually_adjust_layout);
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.manually_recycler_view);
        this.mRecyclerView.setFocusable(false);
        LinearLayoutManagerWrapper linearLayoutManagerWrapper = new LinearLayoutManagerWrapper(getContext(), "manually_recycler_view");
        linearLayoutManagerWrapper.setOrientation(0);
        this.mRecyclerView.setLayoutManager(linearLayoutManagerWrapper);
        this.mRecyclerView.addItemDecoration(new ItemPadding(getContext()));
        this.mRecyclerViewItemWidth = (float) this.mRecyclerView.getLayoutParams().height;
        provideAnimateElement(this.mCurrentMode, null, 2);
    }

    public void notifyAfterFrameAvailable(int i) {
        super.notifyAfterFrameAvailable(i);
        MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
        boolean z = true;
        boolean z2 = miBeautyProtocol != null && miBeautyProtocol.isBeautyPanelShow();
        MasterFilterProtocol masterFilterProtocol = (MasterFilterProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(934);
        if (masterFilterProtocol == null || !masterFilterProtocol.isShowing()) {
            z = false;
        }
        int i2 = this.mCurrentMode;
        if ((i2 == 167 || i2 == 180) && this.mManuallyParent.getVisibility() != 0 && !z2 && !z) {
            FolmeUtils.clean(this.mManuallyParent);
            FolmeUtils.animateShow(this.mManuallyParent);
        }
    }

    public void notifyDataChanged(int i, int i2) {
        super.notifyDataChanged(i, i2);
        if (this.mCurrentAdjustType == 1 && this.mAdapter != null) {
            initManuallyDataList();
            this.mRecyclerView.setAdapter(this.mAdapter);
            this.mAdapter.notifyDataSetChanged();
        }
        FragmentManuallyExtra extraFragment = getExtraFragment();
        if (extraFragment != null) {
            extraFragment.notifyDataChanged(i, this.mCurrentMode);
        }
    }

    public boolean onBackEvent(int i) {
        ViewGroup viewGroup = this.mManuallyParent;
        if ((viewGroup == null || viewGroup.getVisibility() == 0) && i == 2) {
            FragmentManuallyExtra fragmentManuallyExtra = this.mFragmentManuallyExtra;
            if (!(fragmentManuallyExtra == null || fragmentManuallyExtra.getCurrentTitle() == -1)) {
                MistatsWrapper.moduleUIClickEvent("M_manual_", Manual.MANUAL_EDIT_TAB_HIDE, (Object) Integer.valueOf(1));
                this.mFragmentManuallyExtra.animateOut();
                ManuallyAdapter manuallyAdapter = (ManuallyAdapter) this.mRecyclerView.getAdapter();
                if (manuallyAdapter != null) {
                    manuallyAdapter.setSelectedTitle(-1);
                }
            }
        }
        return false;
    }

    public void onClick(View view) {
        if (isEnableClick()) {
            view.getId();
            ComponentData componentData = (ComponentData) view.getTag();
            int displayTitleString = componentData.getDisplayTitleString();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onClick: ");
            sb.append(view.getContext().getString(displayTitleString));
            Log.u(str, sb.toString());
            this.mFragmentManuallyExtra = getExtraFragment();
            FragmentManuallyExtra fragmentManuallyExtra = this.mFragmentManuallyExtra;
            if (fragmentManuallyExtra == null) {
                Log.u(TAG, "onClick FragmentManuallyExtra show");
                hideTips();
                this.mFragmentManuallyExtra = new FragmentManuallyExtra();
                this.mFragmentManuallyExtra.setDegree(this.mDegree);
                this.mFragmentManuallyExtra.setmFragmentManually(this);
                this.mFragmentManuallyExtra.updateRecordingState(this.mIsVideoRecording);
                this.mFragmentManuallyExtra.setComponentData(componentData, this.mCurrentMode, true, this);
                FragmentManager childFragmentManager = getChildFragmentManager();
                FragmentManuallyExtra fragmentManuallyExtra2 = this.mFragmentManuallyExtra;
                FragmentUtils.addFragmentWithTag(childFragmentManager, (int) R.id.manually_popup, (Fragment) fragmentManuallyExtra2, fragmentManuallyExtra2.getFragmentTag());
            } else if (fragmentManuallyExtra.getCurrentTitle() == displayTitleString) {
                Log.u(TAG, "onClick FragmentManuallyExtra hide");
                this.mFragmentManuallyExtra.animateOut();
                ((ManuallyAdapter) this.mRecyclerView.getAdapter()).setSelectedTitle(-1);
            } else {
                Log.u(TAG, "onClick FragmentManuallyExtra reset");
                hideTips();
                this.mFragmentManuallyExtra.updateRecordingState(this.mIsVideoRecording);
                this.mFragmentManuallyExtra.resetData(componentData);
            }
            ((ManuallyAdapter) this.mRecyclerView.getAdapter()).setSelectedTitle(displayTitleString);
        }
    }

    public Animation onCreateAnimation(int i, boolean z, int i2) {
        return super.onCreateAnimation(i, z, i2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x009b, code lost:
        if (r8.mIsVideoRecording == false) goto L_0x0151;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0120, code lost:
        if (r8.mIsVideoRecording == false) goto L_0x0151;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x014f, code lost:
        if (r8.mIsVideoRecording == false) goto L_0x0151;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0151, code lost:
        ((com.android.camera.ActivityBase) getContext()).playCameraSound(6);
        com.android.camera.lib.compatibility.related.vibrator.ViberatorContext.getInstance(getContext().getApplicationContext()).performSlideScaleNormal();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onManuallyDataChanged(ComponentData componentData, String str, String str2, boolean z, int i) {
        if (isEnableClick() && i == this.mCurrentMode) {
            ManuallyValueChanged manuallyValueChanged = (ManuallyValueChanged) ModeCoordinatorImpl.getInstance().getAttachProtocol(174);
            if (manuallyValueChanged != null) {
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (topAlert != null) {
                    boolean z2 = true;
                    String str3 = "0";
                    switch (componentData.getDisplayTitleString()) {
                        case R.string.pref_camera_iso_title_abbr /*2131756363*/:
                            if (!str2.equals(str3)) {
                                Optional.ofNullable(getExtraFragment()).ifPresent(O00000o.INSTANCE);
                                topAlert.alertUpdateValue(7, 0, getResources().getString(componentData.getValueDisplayString(this.mCurrentMode)));
                            }
                            manuallyValueChanged.onISOValueChanged((ComponentManuallyISO) componentData, str, str2);
                            break;
                        case R.string.pref_camera_manually_exposure_value_abbr /*2131756400*/:
                            if (!str2.equals(String.valueOf(1000))) {
                                Optional.ofNullable(getExtraFragment()).ifPresent(O00000o0.INSTANCE);
                                topAlert.alertUpdateValue(8, 0, componentData.getValueDisplayStringNotFromResource(this.mCurrentMode));
                            }
                            manuallyValueChanged.onEVValueChanged((ComponentManuallyEV) componentData, str2);
                            break;
                        case R.string.pref_camera_whitebalance_title_abbr /*2131756539*/:
                            if (str2.equals("manual")) {
                                Optional.ofNullable(getExtraFragment()).ifPresent(new C0307O00000oO(componentData, C0124O00000oO.o00OO00()));
                            }
                            manuallyValueChanged.onWBValueChanged((ComponentManuallyWB) componentData, str2, z);
                            if (z && !this.mIsVideoRecording) {
                                ((ActivityBase) getContext()).playCameraSound(6);
                                ViberatorContext.getInstance(getContext().getApplicationContext()).performSlideScaleNormal();
                            }
                            if (z) {
                                topAlert.alertUpdateValue(5, 0, String.valueOf(CameraSettings.getCustomWB()));
                                break;
                            }
                            break;
                        case R.string.pref_camera_zoom_mode_title_abbr /*2131756555*/:
                            manuallyValueChanged.onDualLensSwitch((ComponentManuallyDualLens) componentData, this.mCurrentMode);
                            z2 = false;
                            break;
                        case R.string.pref_manual_exposure_title_abbr /*2131756598*/:
                            if (!str2.equals(str3)) {
                                Optional.ofNullable(getExtraFragment()).ifPresent(C0308O00000oo.INSTANCE);
                                topAlert.alertUpdateValue(6, 0, getResources().getString(componentData.getValueDisplayString(this.mCurrentMode)));
                            }
                            manuallyValueChanged.onETValueChanged((ComponentManuallyET) componentData, str, str2);
                            break;
                        case R.string.pref_qc_focus_position_title_abbr /*2131756624*/:
                            if (!str2.equals(String.valueOf(1000))) {
                                Optional.ofNullable(getExtraFragment()).ifPresent(O00000Oo.INSTANCE);
                                topAlert.alertUpdateValue(9, 0, CameraSettings.getManualFocusName(getContext(), Integer.parseInt(str2)));
                            }
                            manuallyValueChanged.onFocusValueChanged((ComponentManuallyFocus) componentData, str, str2);
                            if (!this.mIsVideoRecording) {
                                performFocusValueChangedViberator(str, str2);
                                break;
                            }
                            break;
                    }
                    updateEVState(i);
                    if (z2) {
                        ((ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164)).reCheckParameterResetTip(false);
                    } else {
                        topAlert.refreshExtraMenu();
                    }
                    if (this.mRecyclerView.getAdapter() != null) {
                        this.mRecyclerView.post(new Runnable() {
                            public void run() {
                                FragmentManually.this.mRecyclerView.getAdapter().notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        }
    }

    public void onRecordingPrepare() {
        this.mIsVideoRecording = true;
        FragmentManuallyExtra fragmentManuallyExtra = this.mFragmentManuallyExtra;
        if (fragmentManuallyExtra != null) {
            fragmentManuallyExtra.updateRecordingState(this.mIsVideoRecording);
            if (R.string.pref_camera_zoom_mode_title_abbr == this.mFragmentManuallyExtra.getCurrentTitle()) {
                this.mFragmentManuallyExtra.animateOut();
                ((ManuallyAdapter) this.mRecyclerView.getAdapter()).setSelectedTitle(-1);
            }
        }
        initRecyclerView(1, this);
        notifyDataChanged(1, 180);
    }

    public void onRecordingStop() {
        this.mIsVideoRecording = false;
        FragmentManuallyExtra fragmentManuallyExtra = this.mFragmentManuallyExtra;
        if (fragmentManuallyExtra != null) {
            fragmentManuallyExtra.updateRecordingState(this.mIsVideoRecording);
        }
        initRecyclerView(1, this);
        notifyDataChanged(1, 180);
    }

    public void provideAnimateElement(int i, List list, int i2) {
        int i3 = this.mCurrentMode;
        super.provideAnimateElement(i, list, i2);
        int i4 = (i == 167 || i == 180) ? 1 : 0;
        if (i4 != 0) {
            updateEVState(i);
        }
        reInitManuallyLayout(i4, i3, i, list);
    }

    /* access modifiers changed from: protected */
    public Animation provideEnterAnimation(int i) {
        return null;
    }

    /* access modifiers changed from: protected */
    public Animation provideExitAnimation(int i) {
        return null;
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        if (this.mRecyclerView != null) {
            for (int i2 = 0; i2 < this.mRecyclerView.getChildCount(); i2++) {
                list.add(this.mRecyclerView.getChildAt(i2));
            }
        }
        Adapter adapter = this.mAdapter;
        if (adapter != null && (adapter instanceof ManuallyAdapter)) {
            ((ManuallyAdapter) adapter).setRotate(i);
        }
        if (getExtraFragment() != null) {
            getExtraFragment().provideRotateItem(list, i);
        }
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        modeCoordinator.attachProtocol(181, this);
        registerBackStack(modeCoordinator, this);
    }

    public void resetManually() {
        if (!(this.mManuallyComponents == null || this.mAdapter == null)) {
            FragmentManuallyExtra extraFragment = getExtraFragment();
            int currentTitle = extraFragment != null ? extraFragment.getCurrentTitle() : -1;
            ArrayList arrayList = new ArrayList();
            int i = -1;
            for (int i2 = 0; i2 < this.mManuallyComponents.size(); i2++) {
                ComponentData componentData = (ComponentData) this.mManuallyComponents.get(i2);
                if (!(componentData instanceof ComponentManuallyDualLens)) {
                    if (componentData.isModified(this.mCurrentMode)) {
                        arrayList.add(componentData);
                    }
                    componentData.reset(this.mCurrentMode);
                    if (componentData.getDisplayTitleString() == currentTitle) {
                        i = i2;
                    }
                }
            }
            this.mAdapter.notifyDataSetChanged();
            if (!(currentTitle == -1 || i == -1)) {
                extraFragment.resetData((ComponentData) this.mManuallyComponents.get(i));
                ((ManuallyAdapter) this.mAdapter).setSelectedTitle(currentTitle);
            }
            ManuallyValueChanged manuallyValueChanged = (ManuallyValueChanged) ModeCoordinatorImpl.getInstance().getAttachProtocol(174);
            if (manuallyValueChanged != null) {
                manuallyValueChanged.resetManuallyParameters(arrayList);
                updateEVState(this.mCurrentMode);
            }
        }
    }

    public void setManuallyLayoutVisible(boolean z) {
        int i = 0;
        if (z) {
            int i2 = this.mCurrentMode;
            int i3 = (i2 == 167 || i2 == 180) ? 1 : 0;
            this.mCurrentAdjustType = i3;
            int i4 = this.mCurrentMode;
            if (i4 == 180 || i4 == 167) {
                FolmeUtils.clean(this.mManuallyParent);
                FolmeUtils.animateShow(this.mManuallyParent);
                return;
            }
        } else {
            this.mCurrentAdjustType = 0;
            int i5 = this.mCurrentMode;
            i = 4;
            if (i5 == 180 || i5 == 167) {
                FolmeUtils.clean(this.mManuallyParent);
            }
        }
        this.mManuallyParent.setVisibility(i);
    }

    public int setManuallyVisible(int i, int i2, ManuallyListener manuallyListener) {
        int initRecyclerView = initRecyclerView(i, manuallyListener);
        if (i != 0) {
            Adapter adapter = this.mAdapter;
            if (adapter != null) {
                this.mRecyclerView.setAdapter(adapter);
            }
        }
        setManuallyLayoutViewVisible(i2);
        return initRecyclerView;
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        modeCoordinator.detachProtocol(181, this);
        unRegisterBackStack(modeCoordinator, this);
        removeExtra();
    }

    public int visibleHeight() {
        int i = this.mCurrentAdjustType;
        if (i == 0 || i == -1) {
            return 0;
        }
        return this.mManuallyParent.getHeight() + (getResources().getDimensionPixelSize(R.dimen.tips_margin_bottom_normal) / 2);
    }
}
