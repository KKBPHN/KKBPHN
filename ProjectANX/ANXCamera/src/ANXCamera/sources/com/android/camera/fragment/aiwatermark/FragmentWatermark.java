package com.android.camera.fragment.aiwatermark;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.aiwatermark.lisenter.IPermissionListener;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.runing.ComponentRunningAIWatermark;
import com.android.camera.fragment.BaseFragmentPagerAdapter;
import com.android.camera.fragment.BasePanelFragment;
import com.android.camera.fragment.beauty.BaseBeautyFragment;
import com.android.camera.log.Log;
import com.android.camera.permission.PermissionManager;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BottomMenuProtocol;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.DualController;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.protocol.ModeProtocol.WatermarkProtocol;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.AIWatermark;
import com.android.camera.ui.NoScrollViewPager;
import java.util.ArrayList;
import java.util.List;
import miuix.view.animation.CubicEaseOutInterpolator;

public class FragmentWatermark extends BasePanelFragment implements WatermarkProtocol, HandleBackTrace {
    public static final int FRAGMENT_INFO = 1048574;
    private static final String TAG = "FragmentWatermark";
    private ComponentRunningAIWatermark mComponentAIWatermark;
    private int mCurrentState = -1;
    private IPermissionListener mListener = null;
    private CubicEaseOutInterpolator mOutInterpolator = new CubicEaseOutInterpolator();
    private BaseFragmentPagerAdapter mPagerAdapter;
    private View mRootView = null;
    private ValueAnimator mValueAnimator = new ValueAnimator();
    private NoScrollViewPager mViewPager;

    static /* synthetic */ boolean O00000o0(View view, MotionEvent motionEvent) {
        return true;
    }

    static /* synthetic */ void O000o0OO() {
    }

    static /* synthetic */ void O000o0o0() {
    }

    private void checkAIWatermark() {
        if (DataRepository.dataItemRunning().getComponentRunningAIWatermark().needActive()) {
            resetFragment();
        }
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

    private void initViewPager() {
        Object obj;
        ArrayList arrayList = new ArrayList();
        for (ComponentDataItem componentDataItem : this.mComponentAIWatermark.getItems()) {
            int intValue = Integer.valueOf(componentDataItem.mValue).intValue();
            if (intValue == 0) {
                obj = new FragmentGenWatermark();
            } else if (intValue == 1) {
                obj = new FragmentSpotsWatermark();
            } else if (intValue == 2) {
                obj = new FragmentFestivalWatermark();
            } else if (intValue == 3) {
                obj = new FragmentASDWatermark();
            } else if (intValue == 4) {
                obj = new FragmentCityWatermark();
            } else if (intValue == 11) {
                obj = new FragmentSuperMoonSilhouetteWatermark();
            } else if (intValue == 12) {
                obj = new FragmentSuperMoonTextWatermark();
            }
            arrayList.add(obj);
        }
        feedRotation(arrayList);
        this.mPagerAdapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), arrayList);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOffscreenPageLimit(2);
        this.mViewPager.setFocusable(false);
        this.mViewPager.setOnTouchListener(O00000Oo.INSTANCE);
    }

    private void initWatermarkType() {
        this.mComponentAIWatermark = DataRepository.dataItemRunning().getComponentRunningAIWatermark();
        this.mCurrentState = 1;
        moveAIWatermark(1);
        initViewPager();
        String currentType = this.mComponentAIWatermark.getCurrentType();
        initWatermarkType(currentType, true);
        setViewPagerPageByType(currentType);
    }

    private void initWatermarkType(String str, boolean z) {
        if (!TextUtils.isEmpty(str)) {
            this.mComponentAIWatermark.setCurrentType(str);
        }
    }

    private void moveAIWatermark(int i) {
        if (DataRepository.dataItemRunning().getComponentRunningAIWatermark().needMoveUp(this.mCurrentMode)) {
            MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
            if (mainContentProtocol != null) {
                mainContentProtocol.moveWatermarkLayout(i, getDistanceForWM());
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onDismissFinished */
    public void O000o0Oo() {
        resetFragment();
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction != null && !cameraAction.isDoingAction() && !cameraAction.isRecording()) {
            resetTips();
        }
    }

    private void resetFragment() {
        this.mViewPager.setAdapter(null);
        BaseFragmentPagerAdapter baseFragmentPagerAdapter = this.mPagerAdapter;
        if (baseFragmentPagerAdapter != null) {
            baseFragmentPagerAdapter.recycleFragmentList(getChildFragmentManager());
            this.mPagerAdapter = null;
        }
        this.mCurrentState = -1;
    }

    private void resetTips() {
        if (this.mCurrentMode == 188) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.clearAlertStatus();
            }
        }
    }

    private void restoreCameraActionMenu(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("restoreCameraActionMenu callingFrom=");
        sb.append(i);
        Log.d(str, sb.toString());
        BottomMenuProtocol bottomMenuProtocol = (BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197);
        if (bottomMenuProtocol != null) {
            bottomMenuProtocol.onRestoreCameraActionMenu(i);
        }
    }

    private void setViewPagerPageByType(String str) {
        List items = this.mComponentAIWatermark.getItems();
        for (int i = 0; i < items.size(); i++) {
            if (((ComponentDataItem) items.get(i)).mValue.equals(str)) {
                this.mViewPager.setCurrentItem(i, false);
                return;
            }
        }
    }

    private void setViewPagerSelect(int i) {
        this.mComponentAIWatermark.getItems();
        this.mViewPager.setCurrentItem(i, false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0072  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean dismiss(int i, int i2) {
        Runnable runnable;
        View view = getView();
        if (this.mCurrentState == -1 || view == null) {
            return false;
        }
        if (i != 1) {
            if (i == 2) {
                FolmeUtils.animateDeparture(this.mRootView, new O00000o0(this));
                restoreCameraActionMenu(i2);
                DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
                ((BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175)).directShowOrHideLeftTipImage(true);
                if (this.mCurrentMode != 188) {
                    CameraStatUtils.trackAIWatermarkClick(AIWatermark.AI_WATERMARK_LIST_HIDE);
                } else if (dualController != null && !CameraSettings.isFrontCamera()) {
                    dualController.showZoomButton();
                }
            } else if (i == 3) {
                restoreCameraActionMenu(i2);
                runnable = O000000o.INSTANCE;
            }
            if (CameraSettings.isFrontCamera()) {
                BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
                if (bottomPopupTips != null) {
                    bottomPopupTips.updateTipImage();
                }
            }
            moveAIWatermark(2);
            return true;
        }
        resetFragment();
        view.setVisibility(4);
        restoreCameraActionMenu(i2);
        runnable = O00000o.INSTANCE;
        FolmeUtils.animateDeparture(view, runnable);
        if (CameraSettings.isFrontCamera()) {
        }
        moveAIWatermark(2);
        return true;
    }

    /* access modifiers changed from: protected */
    public int getAnimationType() {
        return 2;
    }

    public int getDistanceForWM() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.wm_item_width) + getContext().getResources().getDimensionPixelSize(R.dimen.beautycamera_makeup_item_margin);
    }

    public int getFragmentInto() {
        return 1048574;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_aiwatermark;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mRootView = view;
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.setMarginStart(Display.getStartMargin());
        marginLayoutParams.setMarginEnd(Display.getEndMargin());
        Util.alignPopupBottom(view);
        this.mViewPager = (NoScrollViewPager) view.findViewById(R.id.water_viewPager);
        initWatermarkType();
    }

    public boolean isWatermarkPanelShow() {
        return this.mCurrentState == 1;
    }

    public boolean onBackEvent(int i) {
        if (i == 3) {
            return false;
        }
        return dismiss(i != 4 ? 2 : 1, i);
    }

    public void onPause() {
        super.onPause();
        if (this.mCurrentMode == 205) {
            onBackEvent(4);
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 101 && PermissionManager.isContainLocationPermissions(strArr) && this.mListener != null) {
            boolean isCameraLocationPermissionsResultReady = PermissionManager.isCameraLocationPermissionsResultReady(strArr, iArr);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onRequestPermissionsResult: is location granted = ");
            sb.append(isCameraLocationPermissionsResultReady);
            Log.u(str, sb.toString());
            this.mListener.onPermissionResult(isCameraLocationPermissionsResultReady);
        }
    }

    public void onResume() {
        super.onResume();
        if (this.mCurrentMode == 205) {
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            if (bottomPopupTips != null) {
                bottomPopupTips.directShowOrHideLeftTipImage(!isWatermarkPanelShow());
            }
        }
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        if (this.mCurrentState != -1) {
            if (i2 == 5) {
                checkAIWatermark();
            }
            onBackEvent(4);
        }
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        if (this.mPagerAdapter != null && this.mViewPager != null) {
            for (int i2 = 0; i2 < this.mPagerAdapter.getCount(); i2++) {
                Fragment item = this.mPagerAdapter.getItem(i2);
                if (item != null && (item instanceof BaseBeautyFragment)) {
                    ((BaseBeautyFragment) item).provideRotateItem(list, i);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        registerBackStack(modeCoordinator, this);
        modeCoordinator.attachProtocol(253, this);
    }

    public void requestLocationPermission(IPermissionListener iPermissionListener) {
        this.mListener = iPermissionListener;
        PermissionManager.requestCameraLocationPermissionsByFragment((Fragment) this);
    }

    public void show() {
        if (this.mCurrentState != 1) {
            initWatermarkType();
            View view = getView();
            if (view != null) {
                FolmeUtils.animateEntrance(view);
            }
        }
    }

    public void startDottedLineAnimation(View view) {
        final Drawable drawable = view.getResources().getDrawable(R.drawable.aiwatermark_text_board);
        drawable.setAlpha(0);
        view.setBackground(drawable);
        this.mValueAnimator.setIntValues(new int[]{0, 255, 0});
        this.mValueAnimator.setInterpolator(this.mOutInterpolator);
        this.mValueAnimator.setDuration(1200);
        this.mValueAnimator.setStartDelay(500);
        this.mValueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                drawable.setAlpha(((Integer) valueAnimator.getAnimatedValue()).intValue());
            }
        });
        this.mValueAnimator.start();
    }

    public void switchType(String str, boolean z) {
        initWatermarkType(str, z);
        setViewPagerPageByType(str);
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        unRegisterBackStack(modeCoordinator, this);
        modeCoordinator.detachProtocol(253, this);
    }
}
