package com.android.camera.fragment.fastmotion;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import com.android.camera.ActivityBase;
import com.android.camera.Camera;
import com.android.camera.CameraApplicationDelegate;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.constant.FastMotionConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.config.ComponentManuallyET;
import com.android.camera.data.data.runing.ComponentRunningFastMotion;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.BaseFragmentPagerAdapter;
import com.android.camera.fragment.manually.ManuallyListener;
import com.android.camera.lib.compatibility.related.vibrator.ViberatorContext;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BottomMenuProtocol;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.DualController;
import com.android.camera.protocol.ModeProtocol.FastMotionProtocol;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.ui.NoScrollViewPager;
import java.util.ArrayList;
import java.util.List;

public class FragmentFastMotion extends BaseFragment implements FastMotionProtocol, HandleBackTrace, ManuallyListener {
    public static final int FRAGMENT_INFO = 16777201;
    private static final String TAG = "FragmentFastMotion";
    private ComponentRunningFastMotion mComponentFastMotion;
    private int mCurrentState = -1;
    private BaseFragmentPagerAdapter mPagerAdapter;
    private View mRootView;
    private NoScrollViewPager mViewPager;

    static /* synthetic */ boolean O00000o0(View view, MotionEvent motionEvent) {
        return true;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    private String getExtraSpeedStr(String str) {
        char c;
        Resources resources;
        int i;
        switch (str.hashCode()) {
            case 48687:
                if (str.equals("120")) {
                    c = 0;
                    break;
                }
            case 50547:
                if (str.equals(FastMotionConstant.FAST_MOTION_SPEED_10X)) {
                    c = 1;
                    break;
                }
            case 52469:
                if (str.equals("500")) {
                    c = 2;
                    break;
                }
            case 1507423:
                if (str.equals(FastMotionConstant.FAST_MOTION_SPEED_30X)) {
                    c = 3;
                    break;
                }
            case 1537214:
                if (str.equals(FastMotionConstant.FAST_MOTION_SPEED_60X)) {
                    c = 4;
                    break;
                }
            case 1567005:
                if (str.equals(FastMotionConstant.FAST_MOTION_SPEED_90X)) {
                    c = 5;
                    break;
                }
            case 1596796:
                if (str.equals(FastMotionConstant.FAST_MOTION_SPEED_120X)) {
                    c = 6;
                    break;
                }
            case 1626587:
                if (str.equals(FastMotionConstant.FAST_MOTION_SPEED_150X)) {
                    c = 7;
                    break;
                }
            case 46730161:
                if (str.equals(FastMotionConstant.FAST_MOTION_SPEED_300X)) {
                    c = 8;
                    break;
                }
            case 46879116:
                if (str.equals(FastMotionConstant.FAST_MOTION_SPEED_450X)) {
                    c = 9;
                    break;
                }
            case 47653682:
                if (str.equals(FastMotionConstant.FAST_MOTION_SPEED_600X)) {
                    c = 10;
                    break;
                }
            case 48577203:
                if (str.equals(FastMotionConstant.FAST_MOTION_SPEED_900X)) {
                    c = 11;
                    break;
                }
            case 51347766:
                if (str.equals(FastMotionConstant.FAST_MOTION_SPEED_1800X)) {
                    c = 12;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
                resources = getResources();
                i = R.string.pref_camera_fastmotion_speed_30x;
                break;
            case 4:
            case 5:
                resources = getResources();
                i = R.string.pref_camera_fastmotion_speed_90x;
                break;
            case 6:
            case 7:
                resources = getResources();
                i = R.string.pref_camera_fastmotion_speed_150x;
                break;
            case 8:
            case 9:
            case 10:
                resources = getResources();
                i = R.string.pref_camera_fastmotion_speed_750x;
                break;
            case 11:
            case 12:
                resources = getResources();
                i = R.string.pref_camera_fastmotion_speed_1800x;
                break;
            default:
                return "";
        }
        return resources.getString(i);
    }

    private String getSaveTime() {
        String componentValue = DataRepository.dataItemRunning().getComponentRunningFastMotionSpeed().getComponentValue(169);
        String componentValue2 = DataRepository.dataItemRunning().getComponentRunningFastMotionDuration().getComponentValue(169);
        if ("0".equals(componentValue2)) {
            return getString(R.string.fastmotion_pro_save_time_infinity);
        }
        double parseDouble = (((Double.parseDouble(componentValue2) * 60.0d) / (Double.parseDouble(componentValue) / 1000.0d)) / 30.0d) / 60.0d;
        int i = (int) parseDouble;
        int round = (int) Math.round((parseDouble - ((double) i)) * 60.0d);
        if (parseDouble < 1.0d) {
            Object[] objArr = new Object[1];
            Resources resources = getResources();
            if (round <= 0) {
                objArr[0] = resources.getQuantityString(R.plurals.fastmotion_pro_sec_unit, 1, new Object[]{Integer.valueOf(1)});
                return getString(R.string.fastmotion_pro_save_time_min_or_sec, objArr);
            }
            objArr[0] = resources.getQuantityString(R.plurals.fastmotion_pro_sec_unit, round, new Object[]{Integer.valueOf(round)});
            return getString(R.string.fastmotion_pro_save_time_min_or_sec, objArr);
        } else if (round == 0) {
            return getString(R.string.fastmotion_pro_save_time_min_or_sec, getResources().getQuantityString(R.plurals.fastmotion_pro_min_unit, i, new Object[]{Integer.valueOf(i)}));
        } else {
            return getString(R.string.fastmotion_pro_save_time_min_and_sec, getResources().getQuantityString(R.plurals.fastmotion_pro_min_unit, i, new Object[]{Integer.valueOf(i)}), getResources().getQuantityString(R.plurals.fastmotion_pro_sec_unit, round, new Object[]{Integer.valueOf(round)}));
        }
    }

    private void initFastMotionType() {
        this.mComponentFastMotion = DataRepository.dataItemRunning().getComponentRunningFastMotion();
        this.mCurrentState = 1;
        initViewPager();
        String currentType = this.mComponentFastMotion.getCurrentType();
        initFastMotionType(currentType, true);
        setViewPagerPageByType(currentType);
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges != null) {
            configChanges.reCheckFastMotion(true);
        }
    }

    private void initFastMotionType(String str, boolean z) {
        if (!TextUtils.isEmpty(str)) {
            this.mComponentFastMotion.setCurrentType(str);
        }
    }

    private void initViewPager() {
        ComponentData componentData;
        FragmentFastMotionExtra fragmentFastMotionExtra;
        ArrayList arrayList = new ArrayList();
        for (ComponentDataItem componentDataItem : this.mComponentFastMotion.getItems()) {
            int intValue = Integer.valueOf(componentDataItem.mValue).intValue();
            if (intValue == 1) {
                fragmentFastMotionExtra = new FragmentFastMotionExtra();
                fragmentFastMotionExtra.setDegree(this.mDegree);
                componentData = DataRepository.dataItemRunning().getComponentRunningFastMotionSpeed();
            } else if (intValue == 2) {
                fragmentFastMotionExtra = new FragmentFastMotionExtra();
                fragmentFastMotionExtra.setDegree(this.mDegree);
                componentData = DataRepository.dataItemRunning().getComponentRunningFastMotionDuration();
            }
            fragmentFastMotionExtra.setComponentData(componentData, this.mCurrentMode, false, this);
            arrayList.add(fragmentFastMotionExtra);
        }
        this.mPagerAdapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), arrayList);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setFocusable(false);
        this.mViewPager.setOnTouchListener(O000000o.INSTANCE);
    }

    private void onDismissFinished(int i) {
        resetFragment();
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction != null && !cameraAction.isDoingAction() && !cameraAction.isRecording() && i != 3) {
            resetTips();
        }
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges == null) {
            return;
        }
        if (i == 2) {
            configChanges.reCheckFastMotion(false);
            ((TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172)).clearFastmotionValue();
        } else if (i != 3) {
            configChanges.reCheckFastMotion(true);
        }
    }

    private void resetFragment() {
        this.mViewPager.setAdapter(null);
        BaseFragmentPagerAdapter baseFragmentPagerAdapter = this.mPagerAdapter;
        if (baseFragmentPagerAdapter != null) {
            baseFragmentPagerAdapter.recycleFragmentList(getChildFragmentManager());
            this.mPagerAdapter = null;
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

    private void setViewPagerPageByType(String str) {
        List items = this.mComponentFastMotion.getItems();
        for (int i = 0; i < items.size(); i++) {
            if (((ComponentDataItem) items.get(i)).mValue.equals(str)) {
                this.mViewPager.setCurrentItem(i, false);
                return;
            }
        }
    }

    public boolean dismiss(int i, int i2) {
        View view = getView();
        if (this.mCurrentState == -1 || view == null) {
            return false;
        }
        this.mCurrentState = -1;
        if (i == 2 || i == 3) {
            ((BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197)).onRestoreCameraActionMenu(i2);
        }
        FolmeUtils.animateDeparture(this.mRootView);
        onDismissFinished(i2);
        return true;
    }

    public int getFragmentInto() {
        return 16777201;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_fastmotion;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mRootView = view;
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.bottomMargin = Display.getBottomHeight() - getResources().getDimensionPixelSize(R.dimen.fastmotion_fragment_layout_margin_compensation);
        marginLayoutParams.setMarginStart(Display.getStartMargin());
        marginLayoutParams.setMarginEnd(Display.getEndMargin());
        this.mViewPager = (NoScrollViewPager) view.findViewById(R.id.fast_motion_viewPager);
        initFastMotionType();
    }

    public boolean isShowing() {
        return this.mCurrentState == 1;
    }

    public boolean onBackEvent(int i) {
        int i2 = 3;
        if (i != 3) {
            i2 = i != 4 ? 2 : 1;
        }
        this.mComponentFastMotion.setClosed(true);
        return dismiss(i2, i);
    }

    public void onDestroyView() {
        super.onDestroyView();
        ComponentRunningFastMotion componentRunningFastMotion = this.mComponentFastMotion;
        if (componentRunningFastMotion != null) {
            componentRunningFastMotion.setClosed(true);
        }
    }

    public void onManuallyDataChanged(ComponentData componentData, String str, String str2, boolean z, int i) {
        String str3;
        boolean z2;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        boolean z3;
        String str9;
        String str10;
        String str11;
        if (isEnableClick() && i == this.mCurrentMode) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                boolean isLocaleChinese = Util.isLocaleChinese();
                int displayTitleString = componentData.getDisplayTitleString();
                if (displayTitleString > 0) {
                    String str12 = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append(CameraApplicationDelegate.getAndroidContext().getString(displayTitleString));
                    sb.append(" onManuallyDataChanged: newValue=");
                    sb.append(str2);
                    sb.append(", isCustomValue=");
                    sb.append(z);
                    Log.u(str12, sb.toString());
                }
                String str13 = FastMotionConstant.FAST_MOTION_SPEED_150X;
                String str14 = FastMotionConstant.FAST_MOTION_SPEED_120X;
                if (displayTitleString == R.string.pref_camera_fastmotion_duration) {
                    String componentValue = DataRepository.dataItemRunning().getComponentRunningFastMotionSpeed().getComponentValue(169);
                    String str15 = "0";
                    if (C0122O00000o.instance().OOO00Oo()) {
                        if (!Util.isLocaleChinese() || (!str14.equals(componentValue) && !str13.equals(componentValue))) {
                            if (str15.equals(str2)) {
                                str7 = getString(R.string.pref_camera_fastmotion_duration_infinity);
                            } else {
                                str7 = getResources().getQuantityString(R.plurals.pref_camera_fastmotion_duration_unit, Integer.parseInt(str2), new Object[]{str2});
                            }
                            str6 = str7;
                            str5 = getExtraSpeedStr(componentValue);
                            str4 = getSaveTime();
                            z2 = false;
                        } else {
                            if (str15.equals(str2)) {
                                str8 = getString(R.string.pref_camera_fastmotion_duration_infinity);
                            } else {
                                str8 = getResources().getQuantityString(R.plurals.pref_camera_fastmotion_duration_unit, Integer.parseInt(str2), new Object[]{str2});
                            }
                            str6 = str8;
                            str5 = getExtraSpeedStr(componentValue);
                            str4 = getSaveTime();
                            z2 = true;
                        }
                        topAlert.alertFastmotionProValue(str6, str5, str4, isLocaleChinese, z2);
                    } else {
                        String str16 = "";
                        if (Util.isLocaleChinese() || str15.equals(str2)) {
                            if (str15.equals(str2)) {
                                str3 = getString(R.string.pref_camera_fastmotion_duration_infinity);
                            } else {
                                str3 = getResources().getQuantityString(R.plurals.pref_camera_fastmotion_duration_unit, Integer.parseInt(str2), new Object[]{str2});
                            }
                            topAlert.alertFastmotionValue(str3, str16);
                        } else {
                            topAlert.alertFastmotionValue(str2, getResources().getQuantityString(R.plurals.pref_camera_fastmotion_duration_unit, 10, new Object[]{str16}));
                        }
                    }
                } else if (displayTitleString == R.string.pref_camera_fastmotion_speed) {
                    if (C0122O00000o.instance().OOO00Oo()) {
                        if (!Util.isLocaleChinese() || (!str14.equals(str2) && !str13.equals(str2))) {
                            str11 = componentData.getValueDisplayStringNotFromResource(this.mCurrentMode);
                            str10 = getExtraSpeedStr(str2);
                            str9 = getSaveTime();
                            z3 = false;
                        } else {
                            str11 = componentData.getValueDisplayStringNotFromResource(this.mCurrentMode);
                            str10 = getExtraSpeedStr(str2);
                            str9 = getSaveTime();
                            z3 = true;
                        }
                        topAlert.alertFastmotionProValue(str11, str10, str9, isLocaleChinese, z3);
                    } else {
                        topAlert.alertFastmotionValue(componentData.getValueDisplayStringNotFromResource(this.mCurrentMode), getExtraSpeedStr(str2));
                    }
                    if (C0122O00000o.instance().OOO00Oo()) {
                        ComponentManuallyET componentManuallyET = DataRepository.dataItemConfig().getmComponentManuallyET();
                        double parseLong = ((double) (Long.parseLong(componentManuallyET.getComponentValue(169)) / 1000)) / 1000.0d;
                        double parseDouble = Double.parseDouble(str2);
                        if (parseDouble < parseLong) {
                            componentManuallyET.reset(169);
                            ((BaseModule) ((Camera) getActivity()).getCurrentModule()).updatePreferenceInWorkThread(16);
                            String str17 = TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("speedValue ");
                            sb2.append(parseDouble);
                            sb2.append(" etValue ");
                            sb2.append(parseLong);
                            Log.d(str17, sb2.toString());
                        }
                    }
                }
                ((ActivityBase) getContext()).playCameraSound(6);
                ViberatorContext.getInstance(getContext().getApplicationContext()).performSlideScaleNormal();
            }
        }
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        if (this.mCurrentState != -1) {
            onBackEvent(4);
        }
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        BaseFragmentPagerAdapter baseFragmentPagerAdapter = this.mPagerAdapter;
        if (baseFragmentPagerAdapter != null && baseFragmentPagerAdapter.getFragmentList() != null) {
            List fragmentList = this.mPagerAdapter.getFragmentList();
            for (int i2 = 0; i2 < fragmentList.size(); i2++) {
                ((BaseFragment) fragmentList.get(i2)).provideRotateItem(null, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        registerBackStack(modeCoordinator, this);
        modeCoordinator.attachProtocol(674, this);
    }

    public void show() {
        if (this.mCurrentState != 1) {
            initFastMotionType();
            FolmeUtils.animateEntrance(this.mRootView);
        }
    }

    public void switchType(String str, boolean z) {
        initFastMotionType(str, z);
        setViewPagerPageByType(str);
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        unRegisterBackStack(modeCoordinator, this);
        modeCoordinator.detachProtocol(674, this);
    }
}
