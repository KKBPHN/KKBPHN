package com.android.camera.fragment.beauty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import androidx.annotation.Nullable;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.constant.BeautyConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.ui.SeekBarCompat;
import com.android.camera.ui.SeekBarCompat.OnSeekBarCompatChangeListener;
import java.util.List;

public class BeautySmoothLevelFragment extends BaseBeautyFragment {
    private static final int INTERVAL = 5;
    private SeekBarCompat mAdjustSeekBar;
    private boolean mIsRTL;
    private int mSeekBarMaxProgress = 100;

    private void initView(View view) {
        this.mIsRTL = Util.isLayoutRTL(getContext());
        this.mAdjustSeekBar = (SeekBarCompat) view.findViewById(R.id.beauty_level_adjust_seekbar);
        initBeautyItems();
        this.mSeekBarMaxProgress = 100;
        String str = "pref_beautify_skin_smooth_ratio_key";
        int faceBeautyRatio = CameraSettings.getFaceBeautyRatio(str);
        int defaultValueByKey = BeautyConstant.getDefaultValueByKey(str);
        this.mAdjustSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_style));
        this.mAdjustSeekBar.setMax(this.mSeekBarMaxProgress);
        this.mAdjustSeekBar.setSeekBarPinProgress(defaultValueByKey);
        this.mAdjustSeekBar.setProgress(faceBeautyRatio, false);
        this.mAdjustSeekBar.setOnSeekBarChangeListener(new OnSeekBarCompatChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                BeautySmoothLevelFragment.this.onLevelSelected(i, z);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void onLevelSelected(int i, boolean z) {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null && z) {
            topAlert.alertUpdateValue(2, 0, String.valueOf(this.mAdjustSeekBar.isCenterTwoWayMode() ? i / 2 : i));
        }
        DataRepository.dataItemGlobal().getCurrentMode();
        CameraSettings.setFaceBeautyRatio("pref_beautify_skin_smooth_ratio_key", i);
        ShineHelper.onBeautyChanged();
    }

    /* access modifiers changed from: protected */
    public int beautyLevelToPosition(int i, int i2) {
        return Util.clamp(i, 0, i2);
    }

    /* access modifiers changed from: protected */
    public View getAnimateView() {
        return this.mAdjustSeekBar;
    }

    /* access modifiers changed from: protected */
    public List initBeautyItems() {
        return null;
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_beauty_smooth, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    /* access modifiers changed from: protected */
    public void onViewCreatedAndJumpOut() {
        super.onViewCreatedAndJumpOut();
        this.mAdjustSeekBar.setEnabled(false);
    }

    /* access modifiers changed from: protected */
    public void onViewCreatedAndVisibleToUser(boolean z) {
        super.onViewCreatedAndVisibleToUser(z);
        this.mAdjustSeekBar.setEnabled(true);
    }

    /* access modifiers changed from: protected */
    public int provideItemHorizontalMargin() {
        return getResources().getDimensionPixelSize(R.dimen.beautycamera_beauty_level_item_margin);
    }

    public void setEnableClick(boolean z) {
        this.mAdjustSeekBar.setEnabled(z);
    }
}
