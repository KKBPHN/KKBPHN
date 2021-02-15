package com.android.camera.fragment.fastmotion;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
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
import com.android.camera.HybridZoomingSystem;
import com.android.camera.R;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.config.ComponentManuallyDualLens;
import com.android.camera.data.data.config.ComponentManuallyET;
import com.android.camera.data.data.config.ComponentManuallyEV;
import com.android.camera.data.data.config.ComponentManuallyFocus;
import com.android.camera.data.data.config.ComponentManuallyISO;
import com.android.camera.data.data.config.ComponentManuallyWB;
import com.android.camera.data.data.config.DataItemConfig;
import com.android.camera.data.data.runing.ComponentRunningFastMotionPro;
import com.android.camera.data.data.runing.ComponentRunningFastMotionSpeed;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.fragment.manually.ManuallyListener;
import com.android.camera.fragment.manually.adapter.ManuallyAdapter;
import com.android.camera.lib.compatibility.related.vibrator.ViberatorContext;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BottomMenuProtocol;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.DualController;
import com.android.camera.protocol.ModeProtocol.FastmotionProAdjust;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ManuallyValueChanged;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FragmentFastmotionPro extends BaseFragment implements OnClickListener, HandleBackTrace, FastmotionProAdjust, ManuallyListener {
    private static final String TAG = "FragmentFastmotionPro";
    private Adapter mAdapter;
    private ComponentRunningFastMotionPro mComponentRunningFastMotionPro;
    private int mCurrentAdjustType = -1;
    private int mCurrentState = -1;
    private FragmentFastmotionProExtra mFragmentFastmotionProExtra;
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

    private FragmentFastmotionProExtra getExtraFragment() {
        FragmentFastmotionProExtra fragmentFastmotionProExtra = this.mFragmentFastmotionProExtra;
        if (fragmentFastmotionProExtra == null || !fragmentFastmotionProExtra.isAdded()) {
            return null;
        }
        return this.mFragmentFastmotionProExtra;
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
        FragmentFastmotionProExtra extraFragment = getExtraFragment();
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
        return this.mManuallyComponents;
    }

    private void onDismissFinished(int i) {
        setManuallyLayoutVisible(false, i);
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

    private void reCheckFastmotionSpeedValue() {
        String str;
        ComponentRunningFastMotionSpeed componentRunningFastMotionSpeed = DataRepository.dataItemRunning().getComponentRunningFastMotionSpeed();
        double parseLong = ((double) (Long.parseLong(DataRepository.dataItemConfig().getmComponentManuallyET().getComponentValue(169)) / 1000)) / 1000.0d;
        List items = componentRunningFastMotionSpeed.getItems();
        int i = 0;
        while (true) {
            if (i >= items.size()) {
                str = "120";
                break;
            }
            ComponentDataItem componentDataItem = (ComponentDataItem) items.get(i);
            double parseDouble = Double.parseDouble(componentDataItem.mValue);
            if (parseDouble >= parseLong) {
                str = componentDataItem.mValue;
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("reCheckFastmotionSpeedValue speedValue ");
                sb.append(parseDouble);
                sb.append(" etValue ");
                sb.append(parseLong);
                Log.i(str2, sb.toString());
                break;
            }
            i++;
        }
        int max = Math.max(Integer.parseInt(componentRunningFastMotionSpeed.getComponentValue(169)), Integer.parseInt(str));
        componentRunningFastMotionSpeed.setComponentValue(169, String.valueOf(max));
        String str3 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("reCheckFastmotionSpeedValue max ");
        sb2.append(max);
        Log.i(str3, sb2.toString());
    }

    private void removeExtra() {
        if (this.mFragmentFastmotionProExtra != null) {
            FragmentTransaction beginTransaction = getChildFragmentManager().beginTransaction();
            beginTransaction.remove(this.mFragmentFastmotionProExtra);
            beginTransaction.commitAllowingStateLoss();
        }
    }

    private void resetTips() {
        DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
        if (dualController != null && HybridZoomingSystem.IS_3_OR_MORE_SAT && !CameraSettings.isFrontCamera()) {
            dualController.showZoomButton();
        }
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
            bottomPopupTips.reInitTipImage();
        }
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
            FragmentFastmotionProExtra fragmentFastmotionProExtra = this.mFragmentFastmotionProExtra;
            if (fragmentFastmotionProExtra != null && fragmentFastmotionProExtra.getCurrentTitle() == R.string.pref_camera_manually_exposure_value_abbr) {
                removeExtra();
            }
        }
    }

    public boolean dismiss(int i, int i2) {
        reCheckFastmotionSpeedValue();
        View view = getView();
        if (this.mCurrentState == -1 || view == null) {
            return false;
        }
        if (i == 2 || i == 3) {
            ((BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197)).onRestoreCameraActionMenu(i2);
        }
        FolmeUtils.animateDeparture(getView());
        onDismissFinished(i2);
        return true;
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_FAST_MOTION_PRO;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_fastmotion_pro;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        ((MarginLayoutParams) view.getLayoutParams()).bottomMargin = Display.getBottomHeight();
        this.mManuallyParent = (ViewGroup) view.findViewById(R.id.fastmotion_pro_adjust_layout);
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.fastmotion_pro_recycler_view);
        this.mRecyclerView.setFocusable(false);
        LinearLayoutManagerWrapper linearLayoutManagerWrapper = new LinearLayoutManagerWrapper(getContext(), "manually_recycler_view");
        linearLayoutManagerWrapper.setOrientation(0);
        this.mRecyclerView.setLayoutManager(linearLayoutManagerWrapper);
        this.mRecyclerView.addItemDecoration(new ItemPadding(getContext()));
        this.mRecyclerViewItemWidth = (float) this.mRecyclerView.getLayoutParams().height;
        setManuallyLayoutVisible(true, 4);
        this.mComponentRunningFastMotionPro = DataRepository.dataItemRunning().getComponentRunningFastMotionPro();
    }

    public boolean isShowing() {
        return this.mCurrentState == 1;
    }

    public boolean onBackEvent(int i) {
        FragmentFastmotionProExtra fragmentFastmotionProExtra = this.mFragmentFastmotionProExtra;
        if (!(fragmentFastmotionProExtra == null || fragmentFastmotionProExtra.getCurrentTitle() == -1)) {
            this.mFragmentFastmotionProExtra.animateOut();
        }
        if (this.mCurrentState == -1) {
            return false;
        }
        int i2 = 3;
        if (i != 3) {
            i2 = i != 4 ? 2 : 1;
        }
        return dismiss(i2, i);
    }

    public void onClick(View view) {
        if (isEnableClick()) {
            view.getId();
            ComponentData componentData = (ComponentData) view.getTag();
            int displayTitleString = componentData.getDisplayTitleString();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onClick ");
            sb.append(view.getContext().getString(displayTitleString));
            Log.u(str, sb.toString());
            this.mFragmentFastmotionProExtra = getExtraFragment();
            FragmentFastmotionProExtra fragmentFastmotionProExtra = this.mFragmentFastmotionProExtra;
            if (fragmentFastmotionProExtra == null) {
                Log.u(TAG, "onClick FragmentFastmotionProExtra show");
                hideTips();
                this.mFragmentFastmotionProExtra = new FragmentFastmotionProExtra();
                this.mFragmentFastmotionProExtra.setDegree(this.mDegree);
                this.mFragmentFastmotionProExtra.setmFragmentManually(this);
                this.mFragmentFastmotionProExtra.updateRecordingState(this.mIsVideoRecording);
                this.mFragmentFastmotionProExtra.setComponentData(componentData, this.mCurrentMode, true, this);
                FragmentManager childFragmentManager = getChildFragmentManager();
                FragmentFastmotionProExtra fragmentFastmotionProExtra2 = this.mFragmentFastmotionProExtra;
                FragmentUtils.addFragmentWithTag(childFragmentManager, (int) R.id.fastmotion_pro_popup, (Fragment) fragmentFastmotionProExtra2, fragmentFastmotionProExtra2.getFragmentTag());
            } else if (fragmentFastmotionProExtra.getCurrentTitle() == displayTitleString) {
                Log.u(TAG, "onClick FragmentFastmotionProExtra hide");
                this.mFragmentFastmotionProExtra.animateOut();
                ((ManuallyAdapter) this.mRecyclerView.getAdapter()).setSelectedTitle(-1);
            } else {
                hideTips();
                Log.u(TAG, "onClick FragmentFastmotionProExtra reset");
                this.mFragmentFastmotionProExtra.updateRecordingState(this.mIsVideoRecording);
                this.mFragmentFastmotionProExtra.resetData(componentData);
            }
            ((ManuallyAdapter) this.mRecyclerView.getAdapter()).setSelectedTitle(displayTitleString);
        }
    }

    public Animation onCreateAnimation(int i, boolean z, int i2) {
        return super.onCreateAnimation(i, z, i2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00a1, code lost:
        if (r7.mIsVideoRecording == false) goto L_0x014d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x011c, code lost:
        if (r7.mIsVideoRecording == false) goto L_0x014d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x014b, code lost:
        if (r7.mIsVideoRecording == false) goto L_0x014d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x014d, code lost:
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
                    String str3 = "0";
                    switch (componentData.getDisplayTitleString()) {
                        case R.string.pref_camera_iso_title_abbr /*2131756363*/:
                            if (!str2.equals(str3)) {
                                Optional.ofNullable(getExtraFragment()).ifPresent(O0000O0o.INSTANCE);
                                topAlert.alertUpdateValue(7, 0, getResources().getString(componentData.getValueDisplayString(this.mCurrentMode)));
                            }
                            manuallyValueChanged.onISOValueChanged((ComponentManuallyISO) componentData, str, str2);
                            break;
                        case R.string.pref_camera_manually_exposure_value_abbr /*2131756400*/:
                            if (!str2.equals(String.valueOf(1000))) {
                                Optional.ofNullable(getExtraFragment()).ifPresent(O00000o.INSTANCE);
                                topAlert.alertUpdateValue(8, 0, componentData.getValueDisplayStringNotFromResource(this.mCurrentMode));
                            }
                            manuallyValueChanged.onEVValueChanged((ComponentManuallyEV) componentData, str2);
                            break;
                        case R.string.pref_camera_whitebalance_title_abbr /*2131756539*/:
                            if (str2.equals("manual")) {
                                Optional.ofNullable(getExtraFragment()).ifPresent(new C0303O00000oo(componentData, C0124O00000oO.o00OO00()));
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
                        case R.string.pref_manual_exposure_title_abbr /*2131756598*/:
                            DataRepository.dataItemRunning().getComponentRunningFastMotionPro();
                            if (!str2.equals(str3)) {
                                Optional.ofNullable(getExtraFragment()).ifPresent(O0000OOo.INSTANCE);
                                topAlert.alertUpdateValue(6, 0, getResources().getString(componentData.getValueDisplayString(this.mCurrentMode)));
                            }
                            manuallyValueChanged.onETValueChanged((ComponentManuallyET) componentData, str, str2);
                            break;
                        case R.string.pref_qc_focus_position_title_abbr /*2131756624*/:
                            if (!str2.equals(String.valueOf(1000))) {
                                Optional.ofNullable(getExtraFragment()).ifPresent(C0302O00000oO.INSTANCE);
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
                    ((ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164)).reCheckParameterResetTip(false);
                    if (this.mRecyclerView.getAdapter() != null) {
                        this.mRecyclerView.post(new Runnable() {
                            public void run() {
                                FragmentFastmotionPro.this.mRecyclerView.getAdapter().notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        }
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        if (this.mCurrentState != -1) {
            onBackEvent(4);
        }
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
        modeCoordinator.attachProtocol(932, this);
        registerBackStack(modeCoordinator, this);
    }

    public void resetManually() {
        if (!(this.mManuallyComponents == null || this.mAdapter == null)) {
            FragmentFastmotionProExtra extraFragment = getExtraFragment();
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

    public void setManuallyLayoutVisible(boolean z, int i) {
        if (z) {
            updateEVState(this.mCurrentMode);
            initManually();
            this.mRecyclerView.setAdapter(this.mAdapter);
            this.mAdapter.notifyDataSetChanged();
            this.mCurrentState = 1;
        } else {
            this.mCurrentState = -1;
            ComponentRunningFastMotionPro componentRunningFastMotionPro = this.mComponentRunningFastMotionPro;
            if (componentRunningFastMotionPro != null) {
                componentRunningFastMotionPro.setClosed(true);
            }
            CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
            if (cameraAction != null && !cameraAction.isDoingAction() && !cameraAction.isRecording() && i != 3) {
                resetTips();
            }
        }
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges != null) {
            configChanges.reCheckParameterResetTip(false);
            if (i != 3) {
                configChanges.reCheckFastMotion(true);
            }
        }
    }

    public void show() {
        setManuallyLayoutVisible(true, 4);
        FolmeUtils.animateEntrance(getView());
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        modeCoordinator.detachProtocol(932, this);
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
