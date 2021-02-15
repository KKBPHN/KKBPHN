package com.android.camera.fragment.beauty;

import com.android.camera.CameraSettings;
import com.android.camera.data.data.TypeItem;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.MakeupProtocol;

public class VideoBokehColorRetentionFragment extends BaseBeautyMakeupFragment {
    private int getPositionByMode(int i) {
        switch (i) {
            case 2:
                return 5;
            case 3:
                return 4;
            case 4:
                return 2;
            case 5:
                return 3;
            case 6:
                return 1;
            default:
                return 0;
        }
    }

    /* access modifiers changed from: protected */
    public void augmentItemList() {
        super.augmentItemList();
        this.mSelectedParam = getPositionByMode(CameraSettings.getVideoBokehColorRetentionMode());
        this.mSelectedPosition = this.mSelectedParam;
    }

    /* access modifiers changed from: protected */
    public String getClassString() {
        return VideoBokehColorRetentionFragment.class.getSimpleName();
    }

    /* access modifiers changed from: protected */
    public String getShineType() {
        return "14";
    }

    /* access modifiers changed from: protected */
    public void initExtraType() {
        this.mAlphaElement = -1;
        this.mBetaElement = -1;
    }

    /* access modifiers changed from: protected */
    public void onAdapterItemClick(TypeItem typeItem) {
        MakeupProtocol makeupProtocol = (MakeupProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(180);
        if (makeupProtocol != null) {
            makeupProtocol.onMakeupItemSelected(typeItem.mKeyOrType, typeItem.mDisplayNameRes, true);
        }
    }

    /* access modifiers changed from: protected */
    public void onClearClick() {
    }

    /* access modifiers changed from: protected */
    public void onResetClick() {
    }

    /* access modifiers changed from: protected */
    public void onViewCreatedAndVisibleToUser(boolean z) {
        this.mSelectedParam = getPositionByMode(CameraSettings.getVideoBokehColorRetentionMode());
        this.mSelectedPosition = this.mSelectedParam;
        this.mMakeupAdapter.setSelectedPosition(this.mSelectedPosition);
        super.onViewCreatedAndVisibleToUser(z);
    }
}
