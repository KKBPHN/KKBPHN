package com.android.camera.fragment;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.camera.CameraApplicationDelegate;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.config.ComponentRunningMasterFilter;
import com.android.camera.fragment.EffectItemAdapter.EffectItemPadding;
import com.android.camera.fragment.EffectItemAdapter.IEffectItemListener;
import com.android.camera.fragment.EffectItemAdapter.ItemChangeData;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.BottomMenuProtocol;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.DualController;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.MasterFilterProtocol;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import java.util.List;
import miui.view.animation.CubicEaseOutInterpolator;

public class FragmentMasterFilter extends BasePanelFragment implements OnClickListener, HandleBackTrace, IEffectItemListener, MasterFilterProtocol {
    public static final int FRAGMENT_INFO = 16777207;
    private static final String TAG = "FragmentMasterFilter";
    private boolean isAnimation = false;
    private ComponentRunningMasterFilter mComponentRunningMasterFilter;
    private CubicEaseOutInterpolator mCubicEaseOut;
    private int mCurrentIndex = 0;
    private int mCurrentState = -1;
    private EffectItemAdapter mEffectItemAdapter;
    private EffectItemPadding mEffectItemPadding;
    private int mHolderHeight;
    private int mHolderWidth;
    private int mLastIndex = 0;
    private LinearLayoutManagerWrapper mLayoutManager;
    private LinearLayout mMasterFilterParent;
    private RecyclerView mRecyclerView;
    private View mRootView;
    private int mTotalWidth;

    private void notifyItemChanged(int i, int i2) {
        ItemChangeData itemChangeData = new ItemChangeData(false, i);
        ItemChangeData itemChangeData2 = new ItemChangeData(true, i2);
        if (i > -1) {
            if (Util.isAccessible()) {
                ComponentRunningMasterFilter componentRunningMasterFilter = this.mComponentRunningMasterFilter;
                if (componentRunningMasterFilter != null) {
                    int i3 = ((ComponentDataItem) componentRunningMasterFilter.getItems().get(i)).mDisplayNameRes;
                    ViewHolder findViewHolderForAdapterPosition = this.mRecyclerView.findViewHolderForAdapterPosition(i);
                    if (findViewHolderForAdapterPosition != null) {
                        View view = findViewHolderForAdapterPosition.itemView;
                        if (i3 <= 0) {
                            i3 = R.string.lighting_pattern_null;
                        }
                        view.setContentDescription(getString(i3));
                    }
                }
            }
            this.mEffectItemAdapter.notifyItemChanged(i, itemChangeData);
        }
        if (i2 > -1) {
            if (Util.isAccessible()) {
                ComponentRunningMasterFilter componentRunningMasterFilter2 = this.mComponentRunningMasterFilter;
                if (componentRunningMasterFilter2 != null) {
                    int i4 = ((ComponentDataItem) componentRunningMasterFilter2.getItems().get(i2)).mDisplayNameRes;
                    ViewHolder findViewHolderForAdapterPosition2 = this.mRecyclerView.findViewHolderForAdapterPosition(i2);
                    if (findViewHolderForAdapterPosition2 != null && isAdded()) {
                        this.mEffectItemAdapter.setAccessible(findViewHolderForAdapterPosition2.itemView, i4, true);
                    }
                }
            }
            this.mEffectItemAdapter.notifyItemChanged(i2, itemChangeData2);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onDismissFinished */
    public void O000o0O() {
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction != null && !cameraAction.isDoingAction() && !cameraAction.isRecording()) {
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            if (bottomPopupTips != null) {
                bottomPopupTips.reInitTipImage();
            }
            if (bottomPopupTips != null) {
                bottomPopupTips.updateTipBottomMargin(0, true);
            }
            showZoomTipImage();
        }
        View view = this.mRootView;
        if (view != null) {
            FolmeUtils.clean(view);
        }
    }

    private void onItemSelected(int i, boolean z) {
        StringBuilder sb;
        String str;
        String str2;
        String str3 = "invalid filter id: ";
        String str4 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onItemSelected: index = ");
        sb2.append(i);
        sb2.append(", fromClick = ");
        sb2.append(z);
        sb2.append(", mCurrentMode = ");
        sb2.append(this.mCurrentMode);
        sb2.append(", DataRepository.dataItemGlobal().getCurrentMode() = ");
        sb2.append(DataRepository.dataItemGlobal().getCurrentMode());
        Log.u(str4, sb2.toString());
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges == null) {
            Log.e(TAG, "onItemSelected: configChanges = null");
            return;
        }
        try {
            String str5 = ((ComponentDataItem) this.mComponentRunningMasterFilter.getItems().get(i)).mValue;
            int i2 = ((ComponentDataItem) this.mComponentRunningMasterFilter.getItems().get(i)).mDisplayNameRes;
            if (i2 > 0) {
                String str6 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("onItemSelected: filterId = ");
                sb3.append(str5);
                sb3.append(" filterName = ");
                sb3.append(CameraApplicationDelegate.getAndroidContext().getString(i2));
                Log.u(str6, sb3.toString());
            }
            int intValue = Integer.valueOf(str5).intValue();
            CameraStatUtils.trackFilterChanged(intValue, z);
            selectItem(i);
            configChanges.setMasterFilter(intValue);
        } catch (NumberFormatException e) {
            str = TAG;
            sb = new StringBuilder();
            sb.append(str3);
            str2 = e.getMessage();
            sb.append(str2);
            Log.e(str, sb.toString());
        } catch (IndexOutOfBoundsException e2) {
            str = TAG;
            sb = new StringBuilder();
            sb.append(str3);
            str2 = e2.getMessage();
            sb.append(str2);
            Log.e(str, sb.toString());
        }
    }

    private void scrollIfNeed(int i) {
        if (i == this.mLayoutManager.findFirstVisibleItemPosition() || i == this.mLayoutManager.findFirstCompletelyVisibleItemPosition()) {
            int i2 = this.mEffectItemPadding.padding;
            View findViewByPosition = this.mLayoutManager.findViewByPosition(i);
            if (i > 0 && findViewByPosition != null) {
                i2 = (this.mEffectItemPadding.padding * 2) + findViewByPosition.getWidth();
            }
            this.mLayoutManager.scrollToPositionWithOffset(Math.max(0, i), i2);
        } else if (i == this.mLayoutManager.findLastVisibleItemPosition() || i == this.mLayoutManager.findLastCompletelyVisibleItemPosition()) {
            this.mLayoutManager.scrollToPosition(Math.min(i + 1, this.mEffectItemAdapter.getItemCount() - 1));
        }
    }

    private void selectItem(int i) {
        if (i != -1) {
            this.mLastIndex = this.mCurrentIndex;
            this.mCurrentIndex = i;
            scrollIfNeed(i);
            notifyItemChanged(this.mLastIndex, this.mCurrentIndex);
        }
    }

    /* access modifiers changed from: private */
    public void setIsAnimation(boolean z) {
        this.isAnimation = z;
    }

    private void setItemInCenter(int i) {
        this.mCurrentIndex = i;
        int i2 = (this.mTotalWidth / 2) - (this.mHolderWidth / 2);
        this.mEffectItemAdapter.notifyDataSetChanged();
        this.mLayoutManager.scrollToPositionWithOffset(i, i2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0026, code lost:
        if (com.android.camera.HybridZoomingSystem.IS_3_OR_MORE_SAT == false) goto L_0x0083;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void showZoomTipImage() {
        int i = this.mCurrentMode;
        if (i != 165) {
            if (i != 166) {
                if (!(i == 169 || i == 183)) {
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
            } else if (!C0122O00000o.instance().OOOO0OO()) {
                return;
            }
        }
        DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
        if (dualController != null && !CameraSettings.isFrontCamera() && !CameraSettings.isUltraWideConfigOpen(this.mCurrentMode) && ((!CameraSettings.isUltraPixelOn() || C0122O00000o.instance().OOOOO00() || C0122O00000o.instance().OOOO0oo()) && !CameraSettings.isMacroModeEnabled(this.mCurrentMode) && !CameraSettings.isAIWatermarkOn(this.mCurrentMode))) {
            dualController.showZoomButton();
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.clearAlertStatus();
            }
        }
    }

    private void updateCurrentIndex() {
        CameraSettings.getVideoMasterFilter();
        String componentValue = this.mComponentRunningMasterFilter.getComponentValue(DataRepository.dataItemGlobal().getCurrentMode());
        int findIndexOfValue = this.mComponentRunningMasterFilter.findIndexOfValue(componentValue);
        if (findIndexOfValue == -1) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("invalid filter ");
            sb.append(componentValue);
            Log.w(str, sb.toString());
            findIndexOfValue = 0;
        }
        setItemInCenter(findIndexOfValue);
    }

    public boolean dismiss(int i, int i2) {
        if (this.mCurrentState == -1 || this.mRootView == null) {
            return false;
        }
        this.mCurrentState = -1;
        if (i == 2 || i == 3) {
            ((BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197)).onRestoreCameraActionMenu(i2);
        }
        FolmeUtils.animateDeparture(this.mRootView, new C0290O0000oOo(this));
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges != null) {
            configChanges.onShineDismiss(i2);
            configChanges.reCheckParameterDescriptionTip();
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public int getAnimationType() {
        return 10;
    }

    public int getCurrentIndex() {
        return this.mCurrentIndex;
    }

    public int getFragmentInto() {
        return 16777207;
    }

    public int getHolderHeight() {
        return this.mHolderHeight;
    }

    public int getHolderWidth() {
        return this.mHolderWidth;
    }

    public int getLastIndex() {
        return this.mLastIndex;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_master_filter;
    }

    public int getTextureHeight() {
        return 0;
    }

    public int getTextureOffsetX() {
        return 0;
    }

    public int getTextureOffsetY() {
        return 0;
    }

    public int getTextureWidth() {
        return 0;
    }

    public int getTotalWidth() {
        return this.mTotalWidth;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        Util.alignPopupBottom(view);
        this.mRootView = view;
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.master_filter_list);
        this.mRecyclerView.setFocusable(false);
        this.mMasterFilterParent = (LinearLayout) view.findViewById(R.id.master_filter_parent);
        this.mMasterFilterParent.setClipChildren(false);
        Context context = getContext();
        this.mTotalWidth = context.getResources().getDisplayMetrics().widthPixels;
        this.mHolderWidth = context.getResources().getDimensionPixelSize(R.dimen.beautycamera_makeup_item_width);
        this.mHolderHeight = this.mHolderWidth;
        this.mComponentRunningMasterFilter = DataRepository.dataItemRunning().getComponentRunningMasterFilter();
        this.mEffectItemAdapter = new EffectItemAdapter(getContext(), this.mComponentRunningMasterFilter, false);
        this.mEffectItemAdapter.setOnClickListener(this);
        this.mEffectItemAdapter.setOnEffectItemListener(this);
        this.mEffectItemAdapter.setRotation(this.mDegree);
        this.mLayoutManager = new LinearLayoutManagerWrapper(getContext(), 0, false, "master_filter_list");
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        if (this.mEffectItemPadding == null) {
            this.mEffectItemPadding = new EffectItemPadding(getContext());
            this.mRecyclerView.addItemDecoration(this.mEffectItemPadding);
        }
        this.mRecyclerView.setAdapter(this.mEffectItemAdapter);
        this.mRecyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                FragmentMasterFilter.this.setIsAnimation(false);
            }
        });
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setChangeDuration(150);
        defaultItemAnimator.setMoveDuration(150);
        defaultItemAnimator.setAddDuration(150);
        this.mRecyclerView.setItemAnimator(defaultItemAnimator);
        this.mCubicEaseOut = new CubicEaseOutInterpolator();
        show();
    }

    public boolean isAnimation() {
        return this.isAnimation;
    }

    public boolean isShowing() {
        return this.mCurrentState == 1;
    }

    public boolean onBackEvent(int i) {
        if (isHidden()) {
            return false;
        }
        int i2 = 3;
        if (i != 3) {
            i2 = i != 4 ? 2 : 1;
        }
        return dismiss(i2, i);
    }

    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
        if (this.mRecyclerView.isEnabled()) {
            ComponentRunningMasterFilter componentRunningMasterFilter = this.mComponentRunningMasterFilter;
            if (!(componentRunningMasterFilter == null || componentRunningMasterFilter.getItems() == null)) {
                CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
                if (cameraAction == null || !cameraAction.isDoingAction()) {
                    int intValue = ((Integer) view.getTag()).intValue();
                    if (this.mCurrentIndex == intValue) {
                        if (Util.isAccessible() && isAdded()) {
                            view.sendAccessibilityEvent(32768);
                        }
                        return;
                    }
                    setIsAnimation(false);
                    onItemSelected(intValue, true);
                }
            }
        }
    }

    public void onPause() {
        super.onPause();
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate != null && baseDelegate.getActiveFragment(R.id.bottom_beauty) == 16777207) {
            onBackEvent(5);
        }
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        updateCurrentIndex();
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        if (this.mCurrentState != -1 && i2 != 7) {
            onBackEvent(4);
        }
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        if (this.mRecyclerView != null) {
            for (int i2 = 0; i2 < this.mRecyclerView.getChildCount(); i2++) {
                list.add(this.mRecyclerView.getChildAt(i2));
            }
        }
        EffectItemAdapter effectItemAdapter = this.mEffectItemAdapter;
        if (effectItemAdapter != null) {
            effectItemAdapter.setRotation(i);
            int findFirstVisibleItemPosition = this.mLayoutManager.findFirstVisibleItemPosition();
            int findLastVisibleItemPosition = this.mLayoutManager.findLastVisibleItemPosition();
            for (int i3 = 0; i3 < findFirstVisibleItemPosition; i3++) {
                this.mEffectItemAdapter.notifyItemChanged(i3);
            }
            while (true) {
                findLastVisibleItemPosition++;
                if (findLastVisibleItemPosition < this.mEffectItemAdapter.getItemCount()) {
                    this.mEffectItemAdapter.notifyItemChanged(findLastVisibleItemPosition);
                } else {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        registerBackStack(modeCoordinator, this);
        modeCoordinator.attachProtocol(934, this);
    }

    public void show() {
        if (this.mCurrentState != 1) {
            this.mCurrentState = 1;
            FolmeUtils.animateEntrance(this.mRootView);
            EffectItemAdapter effectItemAdapter = this.mEffectItemAdapter;
            if (effectItemAdapter != null) {
                effectItemAdapter.setRotation(this.mDegree);
                this.mEffectItemAdapter.notifyDataSetChanged();
            }
            updateCurrentIndex();
            ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
            if (configChanges != null) {
                configChanges.reCheckParameterDescriptionTip();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        unRegisterBackStack(modeCoordinator, this);
        modeCoordinator.detachProtocol(934, this);
    }
}
