package com.android.camera.fragment.beauty;

import androidx.recyclerview.widget.RecyclerView;
import com.android.camera.R;
import com.android.camera.constant.BeautyConstant;
import com.android.camera.data.data.TypeItem;
import com.android.camera.log.Log;
import com.android.camera.module.ModuleManager;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.MakeupProtocol;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.MiLive;

public class LiveBeautyModeFragment extends BaseBeautyMakeupFragment {
    private static final String TAG = "LiveBeautyModeFragment";

    /* access modifiers changed from: protected */
    public String getClassString() {
        return LiveBeautyModeFragment.class.getSimpleName();
    }

    /* access modifiers changed from: protected */
    public int getListItemMargin() {
        return super.getListItemMargin();
    }

    /* access modifiers changed from: protected */
    public String getShineType() {
        return "11";
    }

    /* access modifiers changed from: protected */
    public void initExtraType() {
        this.mAlphaElement = 1;
        this.mHeaderCustomWidth = getResources().getDimensionPixelSize(R.dimen.live_beauty_list_heard_width);
        this.mBetaElement = -1;
    }

    /* access modifiers changed from: protected */
    public void onAdapterItemClick(TypeItem typeItem) {
        MakeupProtocol makeupProtocol = (MakeupProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(180);
        if (makeupProtocol != null) {
            makeupProtocol.onMakeupItemSelected(typeItem.mKeyOrType, typeItem.mDisplayNameRes, true);
        }
        if (ModuleManager.isLiveModule()) {
            CameraStatUtils.trackLiveBeautyCounter(typeItem.mKeyOrType);
        } else if (ModuleManager.isMiLiveModule()) {
            CameraStatUtils.trackMiLiveBeautyCounter(typeItem.mKeyOrType);
        }
    }

    /* access modifiers changed from: protected */
    public void onClearClick() {
        ShineHelper.clearBeauty();
        selectFirstItem();
    }

    /* access modifiers changed from: protected */
    public void onResetClick() {
        Log.u(TAG, "onResetClick");
        ShineHelper.resetBeauty();
        selectFirstItem();
        toast(getResources().getString(R.string.beauty_reset_toast));
        if (ModuleManager.isLiveModule()) {
            CameraStatUtils.trackLiveBeautyCounter(BeautyConstant.BEAUTY_RESET);
        } else if (ModuleManager.isMiLiveModule()) {
            CameraStatUtils.trackMiLiveClick(MiLive.VALUE_MI_LIVE_CLICK_BEAUTY_RESET);
        }
    }

    /* access modifiers changed from: protected */
    public void setListPadding(RecyclerView recyclerView) {
        super.setListPadding(recyclerView);
        if (recyclerView != null) {
            recyclerView.setPadding(0, 0, 0, 0);
        }
    }
}
