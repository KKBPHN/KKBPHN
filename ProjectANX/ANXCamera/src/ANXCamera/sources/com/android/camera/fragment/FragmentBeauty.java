package com.android.camera.fragment;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.SeekBar;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import com.android.camera.Camera;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.NoClipChildrenLayout;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.folme.FolmeAlphaOutOnSubscribe;
import com.android.camera.constant.BeautyConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.runing.ComponentRunningShine;
import com.android.camera.data.data.runing.ComponentRunningShine.ShineType;
import com.android.camera.fragment.beauty.BaseBeautyFragment;
import com.android.camera.fragment.beauty.BeautyBodyFragment;
import com.android.camera.fragment.beauty.BeautyLevelFragment;
import com.android.camera.fragment.beauty.BeautySettingManager;
import com.android.camera.fragment.beauty.BeautySmoothLevelFragment;
import com.android.camera.fragment.beauty.BokehSmoothLevelFragment;
import com.android.camera.fragment.beauty.IBeautySettingBusiness;
import com.android.camera.fragment.beauty.LiveBeautyFilterFragment;
import com.android.camera.fragment.beauty.LiveBeautyModeFragment;
import com.android.camera.fragment.beauty.MakeupBeautyFragment;
import com.android.camera.fragment.beauty.MakeupParamsFragment;
import com.android.camera.fragment.beauty.MiLiveParamsFragment;
import com.android.camera.fragment.beauty.MiNightParamsFragment;
import com.android.camera.fragment.beauty.RemodelingParamsFragment;
import com.android.camera.fragment.beauty.VideoBokehColorRetentionFragment;
import com.android.camera.fragment.live.FragmentKaleidoscope;
import com.android.camera.fragment.live.FragmentLiveSpeed;
import com.android.camera.fragment.live.FragmentLiveSticker;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.BottomMenuProtocol;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.CameraModuleSpecial;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.DualController;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.MakeupProtocol;
import com.android.camera.protocol.ModeProtocol.MiBeautyProtocol;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.BeautyAttr;
import com.android.camera.statistic.MistatsConstants.MiLive;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera.ui.NoScrollViewPager;
import com.android.camera.ui.SeekBarCompat;
import com.android.camera.ui.SeekBarCompat.OnSeekBarCompatChangeListener;
import com.xiaomi.camera.rx.CameraSchedulers;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class FragmentBeauty extends BasePanelFragment implements HandleBackTrace, MiBeautyProtocol, MakeupProtocol, Consumer, OnClickListener, OnPageChangeListener {
    public static final int FRAGMENT_INFO = 251;
    private static final String LOG_TAG = "FragmentBeauty";
    private static final int SEEKBAR_PROGRESS_RATIO = 1;
    /* access modifiers changed from: private */
    public int mActiveProgress;
    /* access modifiers changed from: private */
    public SeekBarCompat mAdjustSeekBar;
    private BaseFragmentPagerAdapter mBeautyPagerAdapter;
    private BeautySettingManager mBeautySettingManager;
    private ComponentRunningShine mComponentRunningShine;
    /* access modifiers changed from: private */
    public IBeautySettingBusiness mCurrentSettingBusiness;
    private int mCurrentState = -1;
    /* access modifiers changed from: private */
    public FlowableEmitter mFlowableEmitter;
    private boolean mIsEyeLightShow;
    private boolean mIsRTL;
    private View mRootView;
    private Disposable mSeekBarDisposable;
    private int mSeekBarMaxProgress = 100;
    private NoScrollViewPager mViewPager;

    static /* synthetic */ boolean O00000o0(View view, MotionEvent motionEvent) {
        return true;
    }

    private void extraEnterAnim() {
        this.mViewPager.setTranslationX(0.0f);
        this.mViewPager.setAlpha(1.0f);
        ViewCompat.animate(this.mViewPager).translationX(-100.0f).alpha(0.0f).setDuration(120).setStartDelay(0).setInterpolator(new AccelerateDecelerateInterpolator()).start();
    }

    private void extraExitAnim() {
        this.mViewPager.setTranslationX(-100.0f);
        this.mViewPager.setAlpha(0.0f);
        ViewCompat.animate(this.mViewPager).translationX(0.0f).alpha(1.0f).setDuration(280).setInterpolator(new AccelerateDecelerateInterpolator()).setStartDelay(120).start();
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<androidx.fragment.app.Fragment>, for r3v0, types: [java.util.List, java.util.List<androidx.fragment.app.Fragment>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void feedRotation(List<Fragment> list) {
        for (Fragment fragment : list) {
            if (fragment instanceof BaseBeautyFragment) {
                ((BaseBeautyFragment) fragment).setDegree(this.mDegree);
            }
        }
    }

    private boolean hideBeautyLayout(int i, int i2) {
        if (this.mCurrentState == -1) {
            return false;
        }
        if (3 == i && !isHiddenBeautyPanelOnShutter()) {
            return false;
        }
        this.mCurrentState = -1;
        moveAIWatermark(2);
        ComponentRunningShine componentRunningShine = this.mComponentRunningShine;
        if (componentRunningShine != null) {
            componentRunningShine.setTargetShow(false);
        }
        if (this.mRootView == null) {
            return false;
        }
        if (i2 == 1) {
            resetFragment();
        } else if (i2 == 2 || i2 == 3) {
            ((BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197)).onRestoreCameraActionMenu(i);
        }
        if (!((Camera) getActivity()).getCameraIntentManager().isImageCaptureIntent()) {
            FolmeAlphaOutOnSubscribe.directSetResult(this.mRootView);
        }
        onDismissFinished(i);
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges != null) {
            configChanges.onShineDismiss(i);
        }
        return true;
    }

    private void initAdjustSeekBar() {
        if (this.mSeekBarDisposable == null) {
            this.mSeekBarDisposable = Flowable.create(new FlowableOnSubscribe() {
                public void subscribe(FlowableEmitter flowableEmitter) {
                    FragmentBeauty.this.mFlowableEmitter = flowableEmitter;
                }
            }, BackpressureStrategy.DROP).observeOn(CameraSchedulers.sCameraSetupScheduler).onBackpressureDrop(new Consumer() {
                public void accept(@NonNull Integer num) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("seekbar change too fast :");
                    sb.append(num.toString());
                    Log.e(Log.VIEW_TAG, sb.toString());
                }
            }).subscribe((Consumer) this);
            this.mAdjustSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_style));
            this.mAdjustSeekBar.setOnSeekBarChangeListener(new OnSeekBarCompatChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                    TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                    if (topAlert != null && z) {
                        topAlert.alertUpdateValue(2, FragmentBeauty.this.mCurrentSettingBusiness.getDisplayNameRes(), String.valueOf((FragmentBeauty.this.mAdjustSeekBar.isCenterTwoWayMode() ? i / 2 : i) / 1));
                    }
                    FragmentBeauty.this.mActiveProgress = i;
                    FragmentBeauty.this.mFlowableEmitter.onNext(Integer.valueOf(i / 1));
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x0076  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initShineType() {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mRootView.getLayoutParams();
        this.mRootView.setPaddingRelative(Display.getStartMargin(), 0, Display.getEndMargin(), 0);
        MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mViewPager.getLayoutParams();
        ViewGroup viewGroup = (ViewGroup) getActivity().findViewById(R.id.bottom_beauty);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.beauty_fragment_height);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.beautycamera_makeup_item_margin);
        if (this.mCurrentMode == 165) {
            int i = dimensionPixelSize2 / 2;
            marginLayoutParams.bottomMargin = Display.getBottomHeight() - i;
            marginLayoutParams2.topMargin = i;
            marginLayoutParams2.bottomMargin = 0;
        } else {
            marginLayoutParams.bottomMargin = Display.getBottomHeight();
            marginLayoutParams2.topMargin = 0;
            marginLayoutParams2.bottomMargin = dimensionPixelSize2;
            if (DataRepository.dataItemRunning().getUiStyle() == 0) {
                viewGroup.setClipChildren(true);
                marginLayoutParams2.height = dimensionPixelSize;
                if (this.mBeautySettingManager == null) {
                    this.mBeautySettingManager = new BeautySettingManager();
                }
                this.mCurrentState = 1;
                moveAIWatermark(1);
                this.mComponentRunningShine = DataRepository.dataItemRunning().getComponentRunningShine();
                this.mComponentRunningShine.setTargetShow(false);
                initAdjustSeekBar();
                String currentType = this.mComponentRunningShine.getCurrentType();
                initShineType(currentType, false);
                initViewPager();
                setViewPagerPageByType(currentType);
            }
        }
        viewGroup.setClipChildren(false);
        marginLayoutParams2.height = dimensionPixelSize;
        if (this.mBeautySettingManager == null) {
        }
        this.mCurrentState = 1;
        moveAIWatermark(1);
        this.mComponentRunningShine = DataRepository.dataItemRunning().getComponentRunningShine();
        this.mComponentRunningShine.setTargetShow(false);
        initAdjustSeekBar();
        String currentType2 = this.mComponentRunningShine.getCurrentType();
        initShineType(currentType2, false);
        initViewPager();
        setViewPagerPageByType(currentType2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00a8, code lost:
        r5.mCurrentSettingBusiness = null;
        setAdjustSeekBarVisible(false, true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00b4, code lost:
        if (r5.mComponentRunningShine.supportVideoBokehColorRetention() != false) goto L_0x00b6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00f0, code lost:
        r5.put(r1, r6);
        com.android.camera.statistic.MistatsWrapper.mistatEvent(r0, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0113, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initShineType(String str, boolean z) {
        String str2;
        HashMap hashMap;
        if (!TextUtils.isEmpty(str)) {
            this.mComponentRunningShine.setCurrentType(str);
            char c = 65535;
            int hashCode = str.hashCode();
            if (hashCode != 57) {
                if (hashCode != 1568) {
                    switch (hashCode) {
                        case 49:
                            if (str.equals("1")) {
                                c = 1;
                                break;
                            }
                            break;
                        case 50:
                            if (str.equals("2")) {
                                c = 2;
                                break;
                            }
                            break;
                        case 51:
                            if (str.equals("3")) {
                                c = 4;
                                break;
                            }
                            break;
                        case 52:
                            if (str.equals("4")) {
                                c = 5;
                                break;
                            }
                            break;
                        case 53:
                            if (str.equals("5")) {
                                c = 8;
                                break;
                            }
                            break;
                        case 54:
                            if (str.equals("6")) {
                                c = 9;
                                break;
                            }
                            break;
                        default:
                            switch (hashCode) {
                                case 1571:
                                    if (str.equals("14")) {
                                        c = 11;
                                        break;
                                    }
                                    break;
                                case 1572:
                                    if (str.equals("15")) {
                                        c = 6;
                                        break;
                                    }
                                    break;
                                case 1573:
                                    if (str.equals("16")) {
                                        c = 3;
                                        break;
                                    }
                                    break;
                                case 1574:
                                    if (str.equals("17")) {
                                        c = 7;
                                        break;
                                    }
                                    break;
                            }
                    }
                } else if (str.equals("11")) {
                    c = 10;
                }
            } else if (str.equals("9")) {
                c = 0;
            }
            String str3 = BeautyAttr.KEY_BEAUTY_CLICK;
            String str4 = BaseEvent.OPERATE_STATE;
            switch (c) {
                case 0:
                    throw new RuntimeException("not allowed, see onMakeupItemSelected");
                case 1:
                case 2:
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put(str4, BeautyAttr.VALUE_BEAUTY_V1_BOTTOM_TAB);
                    MistatsWrapper.mistatEvent(str3, hashMap2);
                    break;
                case 3:
                    CameraStatUtils.trackKaleidoscopeClick(MiLive.VALUE_MI_LIVE_CLICK_KALEIDOSCOPE);
                    break;
                case 4:
                case 5:
                case 6:
                case 7:
                    this.mCurrentSettingBusiness = this.mBeautySettingManager.constructAndGetSetting(str, this.mComponentRunningShine.getTypeElementsBeauty());
                    hashMap = new HashMap();
                    str2 = BeautyAttr.VALUE_BEAUTY_BOTTOM_TAB;
                    break;
                case 8:
                    this.mCurrentSettingBusiness = this.mBeautySettingManager.constructAndGetSetting(str, this.mComponentRunningShine.getTypeElementsBeauty());
                    hashMap = new HashMap();
                    str2 = BeautyAttr.VALUE_MAKEUP_BOTTOM_TAB;
                    break;
                case 11:
                    break;
                case 9:
                case 10:
                    this.mCurrentSettingBusiness = this.mBeautySettingManager.constructAndGetSetting(str, this.mComponentRunningShine.getTypeElementsBeauty());
                    break;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00b8, code lost:
        if (r2.equals("3") != false) goto L_0x00d0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initViewPager() {
        Object obj;
        ArrayList arrayList = new ArrayList();
        Iterator it = this.mComponentRunningShine.getItems().iterator();
        while (true) {
            char c = 2;
            if (it.hasNext()) {
                String str = ((ComponentDataItem) it.next()).mValue;
                int hashCode = str.hashCode();
                switch (hashCode) {
                    case 49:
                        if (str.equals("1")) {
                            c = 0;
                            break;
                        }
                    case 50:
                        if (str.equals("2")) {
                            c = 1;
                            break;
                        }
                    case 51:
                        break;
                    case 52:
                        if (str.equals("4")) {
                            c = 3;
                            break;
                        }
                    case 53:
                        if (str.equals("5")) {
                            c = 5;
                            break;
                        }
                    case 54:
                        if (str.equals("6")) {
                            c = 6;
                            break;
                        }
                    case 55:
                        if (str.equals("7")) {
                            c = 7;
                            break;
                        }
                    default:
                        switch (hashCode) {
                            case 1567:
                                if (str.equals("10")) {
                                    c = 8;
                                    break;
                                }
                            case 1568:
                                if (str.equals("11")) {
                                    c = 9;
                                    break;
                                }
                            case 1569:
                                if (str.equals("12")) {
                                    c = 10;
                                    break;
                                }
                            case 1570:
                                if (str.equals("13")) {
                                    c = 11;
                                    break;
                                }
                            case 1571:
                                if (str.equals("14")) {
                                    c = 12;
                                    break;
                                }
                            case 1572:
                                if (str.equals("15")) {
                                    c = 4;
                                    break;
                                }
                            case 1573:
                                if (str.equals("16")) {
                                    c = 13;
                                    break;
                                }
                            case 1574:
                                if (str.equals("17")) {
                                    c = 14;
                                    break;
                                }
                        }
                        c = 65535;
                        break;
                }
                switch (c) {
                    case 0:
                        obj = new BeautyLevelFragment();
                        break;
                    case 1:
                        obj = new BeautySmoothLevelFragment();
                        break;
                    case 2:
                        obj = new MakeupParamsFragment();
                        break;
                    case 3:
                        obj = new RemodelingParamsFragment();
                        break;
                    case 4:
                        obj = new MiLiveParamsFragment();
                        break;
                    case 5:
                        obj = new MakeupBeautyFragment();
                        break;
                    case 6:
                        obj = new BeautyBodyFragment();
                        break;
                    case 7:
                        obj = new FragmentFilter();
                        break;
                    case 8:
                        obj = new LiveBeautyFilterFragment();
                        break;
                    case 9:
                        obj = new LiveBeautyModeFragment();
                        break;
                    case 10:
                        obj = new FragmentLiveSticker();
                        break;
                    case 11:
                        obj = new FragmentLiveSpeed();
                        break;
                    case 12:
                        if (!this.mComponentRunningShine.supportVideoBokehColorRetention()) {
                            obj = new BokehSmoothLevelFragment();
                            break;
                        } else {
                            obj = new VideoBokehColorRetentionFragment();
                            break;
                        }
                    case 13:
                        obj = new FragmentKaleidoscope();
                        break;
                    case 14:
                        obj = new MiNightParamsFragment();
                        break;
                    default:
                        throw new RuntimeException("unknown beauty type");
                }
                arrayList.add(obj);
            } else {
                feedRotation(arrayList);
                this.mBeautyPagerAdapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), arrayList);
                this.mViewPager.clearOnPageChangeListeners();
                this.mViewPager.addOnPageChangeListener(this);
                this.mViewPager.setAdapter(this.mBeautyPagerAdapter);
                this.mViewPager.setOffscreenPageLimit(2);
                this.mViewPager.setFocusable(false);
                this.mViewPager.setOnTouchListener(O0000O0o.INSTANCE);
                if (arrayList.size() == 1 && (arrayList.get(0) instanceof NoClipChildrenLayout)) {
                    ((NoClipChildrenLayout) arrayList.get(0)).setNoClip(true);
                    return;
                }
                return;
            }
        }
    }

    private boolean isHiddenBeautyPanelOnShutter() {
        int i = this.mCurrentMode;
        return i == 162 || i == 161 || i == 174 || i == 183 || i == 176 || i == 180 || i == 169;
    }

    private void moveAIWatermark(int i) {
        if (DataRepository.dataItemRunning().getComponentRunningAIWatermark().needMoveUp(this.mCurrentMode)) {
            MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
            if (mainContentProtocol != null) {
                mainContentProtocol.moveWatermarkLayout(i, getDistanceForWM());
            }
        }
    }

    private void onDismissFinished(int i) {
        resetFragment();
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (!isHiddenBeautyPanelOnShutter() || (cameraAction != null && !cameraAction.isDoingAction() && !cameraAction.isRecording())) {
            int i2 = this.mCurrentMode;
            if ((i2 != 163 && i2 != 165) || cameraAction == null || !cameraAction.isRecording()) {
                if (!(i == 4 || i == 7)) {
                    resetTips();
                }
            } else {
                return;
            }
        }
        View view = this.mRootView;
        if (view != null) {
            FolmeUtils.clean(view);
        }
    }

    private void resetFragment() {
        setAdjustSeekBarVisible(false, false);
        this.mViewPager.setAdapter(null);
        BaseFragmentPagerAdapter baseFragmentPagerAdapter = this.mBeautyPagerAdapter;
        if (baseFragmentPagerAdapter != null) {
            baseFragmentPagerAdapter.recycleFragmentList(getChildFragmentManager());
            this.mBeautyPagerAdapter = null;
        }
    }

    private void resetTips() {
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
            bottomPopupTips.reInitTipImage();
        }
        if (bottomPopupTips != null) {
            bottomPopupTips.updateTipBottomMargin(0, true);
        }
        showZoomTipImage();
        int i = this.mCurrentMode;
        if (i == 163) {
            CameraModuleSpecial cameraModuleSpecial = (CameraModuleSpecial) ModeCoordinatorImpl.getInstance().getAttachProtocol(195);
            if (cameraModuleSpecial != null) {
                cameraModuleSpecial.showOrHideChip(true);
            }
        } else if (i != 165) {
            return;
        }
        if (bottomPopupTips != null) {
            bottomPopupTips.updateLyingDirectHint(false, true);
        }
    }

    private void setAdjustSeekBarVisible(boolean z, boolean z2) {
        animateViews(z ? 1 : -1, z2, (View) this.mAdjustSeekBar);
    }

    private void setSeekBarMode(int i, int i2) {
        boolean z;
        if (this.mCurrentSettingBusiness != null) {
            if (i == 1) {
                this.mSeekBarMaxProgress = 100;
                this.mAdjustSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_style));
            } else if (i == 2) {
                this.mSeekBarMaxProgress = 200;
                this.mAdjustSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.center_seekbar_style));
                i2 = 100;
                z = true;
                this.mAdjustSeekBar.setCenterTwoWayMode(z);
                this.mAdjustSeekBar.setMax(this.mSeekBarMaxProgress);
                this.mAdjustSeekBar.setSeekBarPinProgress(i2);
                this.mAdjustSeekBar.setProgress(this.mCurrentSettingBusiness.getProgressByCurrentItem() * 1, false);
            }
            z = false;
            this.mAdjustSeekBar.setCenterTwoWayMode(z);
            this.mAdjustSeekBar.setMax(this.mSeekBarMaxProgress);
            this.mAdjustSeekBar.setSeekBarPinProgress(i2);
            this.mAdjustSeekBar.setProgress(this.mCurrentSettingBusiness.getProgressByCurrentItem() * 1, false);
        }
    }

    private void setViewPagerPageByType(String str) {
        List items = this.mComponentRunningShine.getItems();
        for (int i = 0; i < items.size(); i++) {
            if (((ComponentDataItem) items.get(i)).mValue.equals(str)) {
                BaseFragmentPagerAdapter baseFragmentPagerAdapter = this.mBeautyPagerAdapter;
                if (baseFragmentPagerAdapter != null) {
                    Fragment item = baseFragmentPagerAdapter.getItem(i);
                    if (item instanceof BaseBeautyFragment) {
                        ((BaseBeautyFragment) item).setDegree(this.mDegree);
                    }
                }
                this.mViewPager.setCurrentItem(i, false);
                return;
            }
        }
    }

    private void showHideExtraLayout(boolean z, Fragment fragment, String str) {
        FragmentManager fragmentManager = getFragmentManager();
        if (z) {
            FragmentUtils.addFragmentWithTag(fragmentManager, (int) R.id.beauty_extra, fragment, str);
        } else {
            FragmentUtils.removeFragmentByTag(fragmentManager, str);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003d, code lost:
        if (com.android.camera.HybridZoomingSystem.IS_3_OR_MORE_SAT == false) goto L_0x0098;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void showZoomTipImage() {
        int i = this.mCurrentMode;
        if (i != 165) {
            if (i != 166) {
                if (i != 169) {
                    if (i != 171) {
                        if (i != 183) {
                            switch (i) {
                                case 161:
                                case 162:
                                    break;
                                case 163:
                                    break;
                                default:
                                    switch (i) {
                                        case 174:
                                            break;
                                        case 173:
                                        case 175:
                                            break;
                                    }
                            }
                        }
                    } else {
                        DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
                        if (dualController != null) {
                            dualController.showBokehButton();
                        }
                    }
                }
            } else if (!C0122O00000o.instance().OOOO0OO()) {
                return;
            }
        }
        DualController dualController2 = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
        if (dualController2 != null && !CameraSettings.isFrontCamera() && !CameraSettings.isUltraWideConfigOpen(this.mCurrentMode) && ((!CameraSettings.isUltraPixelOn() || C0122O00000o.instance().OOOOO00() || C0122O00000o.instance().OOOO0oo()) && !CameraSettings.isMacroModeEnabled(this.mCurrentMode) && !CameraSettings.isAIWatermarkOn(this.mCurrentMode))) {
            dualController2.showZoomButton();
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.clearAlertStatus();
            }
        }
    }

    public void accept(@NonNull Integer num) {
        IBeautySettingBusiness iBeautySettingBusiness = this.mCurrentSettingBusiness;
        if (iBeautySettingBusiness != null) {
            iBeautySettingBusiness.setProgressForCurrentItem(num.intValue());
        }
    }

    public void clearBeauty() {
        IBeautySettingBusiness iBeautySettingBusiness = this.mCurrentSettingBusiness;
        if (iBeautySettingBusiness != null) {
            iBeautySettingBusiness.clearBeauty();
        }
    }

    public void dismiss(int i) {
        hideBeautyLayout(6, i);
    }

    /* access modifiers changed from: protected */
    public int getAnimationType() {
        return 10;
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0052 A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getDistanceForWM() {
        char c;
        String currentType = DataRepository.dataItemRunning().getComponentRunningShine().getCurrentType();
        int hashCode = currentType.hashCode();
        if (hashCode != 49) {
            if (hashCode != 50) {
                if (hashCode != 55) {
                    if (hashCode == 1573 && currentType.equals("16")) {
                        c = 3;
                        return (c != 0 || c == 1 || c == 2 || c == 3) ? getContext().getResources().getDimensionPixelSize(R.dimen.beauty_fragment_height) : getContext().getResources().getDimensionPixelSize(R.dimen.beautycamera_popup_fragment_height) + getContext().getResources().getDimensionPixelSize(R.dimen.beauty_fragment_height);
                    }
                } else if (currentType.equals("7")) {
                    c = 0;
                    if (c != 0) {
                    }
                }
            } else if (currentType.equals("2")) {
                c = 2;
                if (c != 0) {
                }
            }
        } else if (currentType.equals("1")) {
            c = 1;
            if (c != 0) {
            }
        }
        c = 65535;
        if (c != 0) {
        }
    }

    public int getFragmentInto() {
        return 251;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_beauty;
    }

    public List getSupportedBeautyItems(@ShineType String str) {
        return this.mBeautySettingManager.constructAndGetSetting(str, this.mComponentRunningShine.getTypeElementsBeauty()).getSupportedTypeArray(str);
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mRootView = view;
        this.mIsRTL = Util.isLayoutRTL(getContext());
        this.mViewPager = (NoScrollViewPager) view.findViewById(R.id.beauty_viewPager);
        this.mAdjustSeekBar = (SeekBarCompat) view.findViewById(R.id.beauty_adjust_seekbar);
        setAdjustSeekBarVisible(false, false);
        initShineType();
    }

    public boolean isBeautyPanelShow() {
        return this.mCurrentState == 1;
    }

    public boolean isSeekBarVisible() {
        SeekBarCompat seekBarCompat = this.mAdjustSeekBar;
        return seekBarCompat != null && seekBarCompat.getVisibility() == 0;
    }

    public boolean needViewClear() {
        if (CameraSettings.isUltraPixelRearOn()) {
            return true;
        }
        return super.needViewClear();
    }

    public boolean onBackEvent(int i) {
        if (isHidden()) {
            return false;
        }
        int i2 = 3;
        if (i != 3) {
            i2 = i != 4 ? 2 : 1;
        }
        return hideBeautyLayout(i, i2);
    }

    public void onClick(View view) {
    }

    public void onMakeupItemSelected(String str, @StringRes int i, boolean z) {
        IBeautySettingBusiness iBeautySettingBusiness = this.mCurrentSettingBusiness;
        if (iBeautySettingBusiness != null) {
            iBeautySettingBusiness.setCurrentType(str);
            this.mCurrentSettingBusiness.setDisplayNameRes(i);
            if (TextUtils.equals(str, "key_video_bokeh_blur_null")) {
                setAdjustSeekBarVisible(false, true);
                return;
            }
            this.mActiveProgress = this.mCurrentSettingBusiness.getProgressByCurrentItem() * 1;
            int defaultProgressByCurrentItem = this.mCurrentSettingBusiness.getDefaultProgressByCurrentItem() * 1;
            if (BeautyConstant.isSupportTwoWayAdjustable(str)) {
                setSeekBarMode(2, defaultProgressByCurrentItem);
            } else {
                setSeekBarMode(1, defaultProgressByCurrentItem);
            }
            setAdjustSeekBarVisible(true, true);
        }
    }

    public void onPageScrollStateChanged(int i) {
    }

    public void onPageScrolled(int i, float f, int i2) {
    }

    public void onPageSelected(int i) {
        boolean z = false;
        int i2 = -1;
        for (int i3 = 0; i3 < this.mBeautyPagerAdapter.getCount(); i3++) {
            if (this.mBeautyPagerAdapter.getItem(i3) instanceof NoClipChildrenLayout) {
                i2 = i3;
            }
        }
        if (i2 >= 0) {
            NoClipChildrenLayout noClipChildrenLayout = (NoClipChildrenLayout) this.mBeautyPagerAdapter.getItem(i2);
            if (i2 == i) {
                z = true;
            }
            noClipChildrenLayout.setNoClip(z);
        }
        Fragment item = this.mBeautyPagerAdapter.getItem(i);
        if (item.getView() != null) {
            FolmeUtils.animateShow(item.getView());
        }
    }

    public void onPause() {
        super.onPause();
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate != null && baseDelegate.getActiveFragment(R.id.bottom_beauty) == 251) {
            hideBeautyLayout(5, 2);
        }
    }

    public void onStateChanged() {
        IBeautySettingBusiness iBeautySettingBusiness = this.mCurrentSettingBusiness;
        if (iBeautySettingBusiness != null) {
            iBeautySettingBusiness.onStateChanged();
        }
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (this.mCurrentMode == 163) {
            CameraModuleSpecial cameraModuleSpecial = (CameraModuleSpecial) ModeCoordinatorImpl.getInstance().getAttachProtocol(195);
            if (cameraModuleSpecial != null) {
                cameraModuleSpecial.showOrHideChip(false);
            }
        }
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        if (this.mCurrentState != -1) {
            if (i2 != 7 || (i != 180 && i != 169)) {
                onBackEvent(4);
            }
        }
    }

    public void provideRotateItem(List list, int i) {
        Fragment fragment;
        super.provideRotateItem(list, i);
        BaseFragmentPagerAdapter baseFragmentPagerAdapter = this.mBeautyPagerAdapter;
        if (baseFragmentPagerAdapter != null) {
            NoScrollViewPager noScrollViewPager = this.mViewPager;
            if (noScrollViewPager != null) {
                fragment = baseFragmentPagerAdapter.getItem(noScrollViewPager.getCurrentItem());
                if (fragment != null && (fragment instanceof BaseBeautyFragment)) {
                    ((BaseBeautyFragment) fragment).provideRotateItem(list, i);
                    return;
                }
            }
        }
        fragment = null;
        if (fragment != null) {
        }
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        registerBackStack(modeCoordinator, this);
        modeCoordinator.attachProtocol(194, this);
        modeCoordinator.attachProtocol(180, this);
        this.mIsEyeLightShow = false;
    }

    public void resetBeauty() {
        IBeautySettingBusiness iBeautySettingBusiness = this.mCurrentSettingBusiness;
        if (iBeautySettingBusiness != null) {
            iBeautySettingBusiness.resetBeauty();
        }
    }

    public void setClickEnable(boolean z) {
        super.setClickEnable(z);
        BaseFragmentPagerAdapter baseFragmentPagerAdapter = this.mBeautyPagerAdapter;
        if (baseFragmentPagerAdapter != null) {
            List<Fragment> fragmentList = baseFragmentPagerAdapter.getFragmentList();
            if (fragmentList != null) {
                for (Fragment fragment : fragmentList) {
                    if (fragment instanceof BeautyLevelFragment) {
                        ((BeautyLevelFragment) fragment).setEnableClick(z);
                        return;
                    }
                }
            }
        }
    }

    public void show() {
        if (this.mCurrentState != 1) {
            FolmeUtils.clean(this.mRootView);
            if (this.mCurrentMode == 163) {
                CameraModuleSpecial cameraModuleSpecial = (CameraModuleSpecial) ModeCoordinatorImpl.getInstance().getAttachProtocol(195);
                if (cameraModuleSpecial != null) {
                    cameraModuleSpecial.showOrHideChip(false);
                }
            }
            initShineType();
            FolmeUtils.animateEntrance4Filter(this.mRootView);
        }
    }

    public void switchShineType(String str, boolean z) {
        initShineType(str, z);
        setViewPagerPageByType(str);
        moveAIWatermark(1);
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        unRegisterBackStack(modeCoordinator, this);
        modeCoordinator.detachProtocol(194, this);
        modeCoordinator.detachProtocol(180, this);
        Disposable disposable = this.mSeekBarDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mSeekBarDisposable.dispose();
        }
        this.mIsEyeLightShow = false;
    }
}
