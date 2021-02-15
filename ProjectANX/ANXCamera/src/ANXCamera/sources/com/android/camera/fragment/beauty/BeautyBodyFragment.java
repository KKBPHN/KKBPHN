package com.android.camera.fragment.beauty;

import android.content.res.Resources;
import com.android.camera.R;
import com.android.camera.constant.BeautyConstant;
import com.android.camera.data.data.TypeItem;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.MakeupProtocol;
import com.android.camera.statistic.CameraStatUtils;
import java.util.List;

public class BeautyBodyFragment extends BaseBeautyMakeupFragment {
    private static final String TAG = "BeautyBodyFragment";

    /* access modifiers changed from: protected */
    public String getClassString() {
        return BeautyBodyFragment.class.getSimpleName();
    }

    /* access modifiers changed from: protected */
    public String getShineType() {
        return "6";
    }

    /* access modifiers changed from: protected */
    public void initExtraType() {
        this.mAlphaElement = 1;
        this.mBetaElement = -1;
    }

    /* access modifiers changed from: protected */
    public void onAdapterItemClick(TypeItem typeItem) {
        MakeupProtocol makeupProtocol = (MakeupProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(180);
        if (makeupProtocol != null) {
            makeupProtocol.onMakeupItemSelected(typeItem.mKeyOrType, typeItem.mDisplayNameRes, true);
        }
        CameraStatUtils.trackBeautyClick("6", typeItem.mKeyOrType);
    }

    /* access modifiers changed from: protected */
    public void onClearClick() {
        ShineHelper.clearBeauty();
        selectFirstItem();
    }

    /* access modifiers changed from: protected */
    public void onResetClick() {
        int i;
        Resources resources;
        Log.u(TAG, "onResetClick");
        ShineHelper.resetBeauty();
        selectFirstItem();
        List list = this.mItemList;
        if (list != null && !list.isEmpty()) {
            if ("pref_beautify_skin_smooth_ratio_key".equals(((TypeItem) this.mItemList.get(0)).mKeyOrType)) {
                resources = getResources();
                i = R.string.beauty_reset_toast;
            } else {
                resources = getResources();
                i = R.string.beauty_body_reset_tip;
            }
            toast(resources.getString(i));
        }
        CameraStatUtils.trackBeautyClick("6", BeautyConstant.BEAUTY_RESET);
    }
}
