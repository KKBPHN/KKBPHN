package com.android.camera.fragment.bottom;

import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.constant.DurationConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.AmbilightProtocol;
import com.android.camera.protocol.ModeProtocol.LiveSpeedChanges;
import com.android.camera.timerburst.TimerBurstController;
import miui.text.ExtraTextUtils;

public class BottomAnimationConfig {
    public int mCurrentMode;
    public int mDuration;
    public boolean mIsFPS960;
    public boolean mIsInMimojiCreate;
    public boolean mIsLongExpose;
    public boolean mIsPostProcessing;
    public boolean mIsRecordingCircle;
    public boolean mIsRoundingCircle;
    public boolean mIsStart;
    public boolean mIsTimerBurstCircle;
    public boolean mIsVertical;
    public boolean mIsVideoBokeh;
    public boolean mNeedFinishRecord;
    public boolean mSecondPaintHintEnable = true;
    public boolean mShouldRepeat;
    public boolean mStopButtonEnabled;

    private BottomAnimationConfig(boolean z, int i, boolean z2, boolean z3, boolean z4) {
        this.mIsPostProcessing = z;
        this.mCurrentMode = i;
        this.mIsStart = z2;
        this.mIsFPS960 = z3;
        this.mIsVideoBokeh = z4;
        this.mIsLongExpose = isLongExpose();
        this.mStopButtonEnabled = true;
    }

    public static BottomAnimationConfig generate(boolean z, int i, boolean z2, boolean z3, boolean z4) {
        BottomAnimationConfig bottomAnimationConfig = new BottomAnimationConfig(z, i, z2, z3, z4);
        return bottomAnimationConfig;
    }

    private boolean isLongExpose() {
        if (this.mCurrentMode != 167) {
            return false;
        }
        return DataRepository.dataItemConfig().getmComponentManuallyET().isLongExpose(this.mCurrentMode);
    }

    /* JADX WARNING: Removed duplicated region for block: B:54:0x00ca  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00dd  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x00fe  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x0113  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x013c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public BottomAnimationConfig configVariables() {
        boolean z;
        int i;
        int i2;
        int i3 = 2000;
        if (!this.mIsFPS960) {
            int i4 = this.mCurrentMode;
            if (i4 == 173) {
                if (DataRepository.dataItemRunning().isSuperNightCaptureWithKnownDuration()) {
                    this.mSecondPaintHintEnable = !this.mIsStart;
                    i2 = DataRepository.dataItemRunning().getMultiFrameTotalCaptureDuration();
                }
            } else if (i4 != 162 || !this.mIsVideoBokeh) {
                int i5 = this.mCurrentMode;
                i3 = 15000;
                if (i5 == 161) {
                    this.mDuration = 15000;
                    LiveSpeedChanges liveSpeedChanges = (LiveSpeedChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(201);
                    if (liveSpeedChanges != null) {
                        i2 = (int) (((float) this.mDuration) / liveSpeedChanges.getRecordSpeed());
                    }
                    int i6 = this.mCurrentMode;
                    if (!(i6 == 163 || i6 == 161 || i6 == 177 || i6 == 184 || (i6 == 162 && this.mIsVideoBokeh))) {
                        int i7 = this.mCurrentMode;
                        if (!(i7 == 173 || this.mIsFPS960 || i7 == 189 || i7 == 212)) {
                            z = true;
                            this.mShouldRepeat = z;
                            this.mIsRecordingCircle = this.mCurrentMode != 173;
                            boolean z2 = !CameraSettings.isTimerBurstEnable() && TimerBurstController.isSupportTimerBurst(this.mCurrentMode) && DataRepository.dataItemLive().getTimerBurstController().isInTimerBurstShotting();
                            this.mIsTimerBurstCircle = z2;
                            if (this.mIsTimerBurstCircle || this.mIsLongExpose) {
                                this.mShouldRepeat = false;
                            }
                            boolean z3 = (this.mCurrentMode != 173 || this.mIsFPS960) && !this.mIsPostProcessing;
                            this.mNeedFinishRecord = z3;
                            this.mStopButtonEnabled = true;
                            i = this.mCurrentMode;
                            if (i != 187) {
                                AmbilightProtocol ambilightProtocol = (AmbilightProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(420);
                                if (ambilightProtocol != null) {
                                    this.mDuration = ambilightProtocol.getDuration();
                                    this.mStopButtonEnabled = !ambilightProtocol.shouldDisableStopButton();
                                    this.mShouldRepeat = !ambilightProtocol.getAutoFinish();
                                    this.mIsRecordingCircle = ambilightProtocol.getAutoFinish();
                                }
                            } else if (i == 208) {
                                this.mStopButtonEnabled = false;
                            }
                            this.mIsVertical = Display.fitDisplayFull(1.3333333f);
                            return this;
                        }
                    }
                    z = false;
                    this.mShouldRepeat = z;
                    this.mIsRecordingCircle = this.mCurrentMode != 173;
                    if (!CameraSettings.isTimerBurstEnable()) {
                    }
                    this.mIsTimerBurstCircle = z2;
                    this.mShouldRepeat = false;
                    if (this.mCurrentMode != 173) {
                    }
                    this.mNeedFinishRecord = z3;
                    this.mStopButtonEnabled = true;
                    i = this.mCurrentMode;
                    if (i != 187) {
                    }
                    this.mIsVertical = Display.fitDisplayFull(1.3333333f);
                    return this;
                } else if (i5 == 184) {
                    if (CameraSettings.isGifOn()) {
                        i2 = 5000;
                    }
                } else if (i5 == 174) {
                    i2 = DurationConstant.DURATION_LIVE_RECORD;
                } else if (!(i5 == 183 || i5 == 163 || i5 == 177)) {
                    if (i5 != 167 || !this.mIsLongExpose) {
                        int i8 = this.mCurrentMode;
                        i3 = 10000;
                    } else {
                        i2 = (int) (Long.parseLong(DataRepository.dataItemConfig().getmComponentManuallyET().getComponentValue(this.mCurrentMode)) / ExtraTextUtils.MB);
                    }
                }
            } else {
                i2 = DurationConstant.DURATION_VIDEO_BOKEH_RECORDING;
            }
            this.mDuration = i2;
            int i62 = this.mCurrentMode;
            int i72 = this.mCurrentMode;
            z = true;
            this.mShouldRepeat = z;
            this.mIsRecordingCircle = this.mCurrentMode != 173;
            if (!CameraSettings.isTimerBurstEnable()) {
            }
            this.mIsTimerBurstCircle = z2;
            this.mShouldRepeat = false;
            if (this.mCurrentMode != 173) {
            }
            this.mNeedFinishRecord = z3;
            this.mStopButtonEnabled = true;
            i = this.mCurrentMode;
            if (i != 187) {
            }
            this.mIsVertical = Display.fitDisplayFull(1.3333333f);
            return this;
        }
        this.mDuration = i3;
        int i622 = this.mCurrentMode;
        int i722 = this.mCurrentMode;
        z = true;
        this.mShouldRepeat = z;
        this.mIsRecordingCircle = this.mCurrentMode != 173;
        if (!CameraSettings.isTimerBurstEnable()) {
        }
        this.mIsTimerBurstCircle = z2;
        this.mShouldRepeat = false;
        if (this.mCurrentMode != 173) {
        }
        this.mNeedFinishRecord = z3;
        this.mStopButtonEnabled = true;
        i = this.mCurrentMode;
        if (i != 187) {
        }
        this.mIsVertical = Display.fitDisplayFull(1.3333333f);
        return this;
    }

    public void setSpecifiedDuration(int i) {
        this.mDuration = i;
        this.mShouldRepeat = false;
    }
}
