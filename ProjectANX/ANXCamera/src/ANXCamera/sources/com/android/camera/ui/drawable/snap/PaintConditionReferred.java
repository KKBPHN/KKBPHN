package com.android.camera.ui.drawable.snap;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.features.mimoji2.widget.helper.MimojiStatusManager2;

public class PaintConditionReferred {
    public boolean forceVideoPattern;
    public boolean isFPS960;
    public boolean needFocusBack;
    public boolean needSnapButtonAnimation;
    public float targetFrameRatio;
    public int targetMode;

    public PaintConditionReferred(int i) {
        this.targetMode = i;
        configVariables();
    }

    public PaintConditionReferred(int i, String str, boolean z) {
    }

    public static PaintConditionReferred create(int i) {
        return new PaintConditionReferred(i);
    }

    public static PaintConditionReferred createModeChange(int i, boolean z, boolean z2) {
        return new PaintConditionReferred(i).setNeedSnapButtonAnimation(true).setIsFPS960(z).setNeedSnapButtonAnimation(z2);
    }

    public boolean bottomHalfScreen() {
        float f = this.targetFrameRatio;
        boolean z = true;
        if (f == 1.3333333f) {
            return !Display.fitDisplayFull(1.3333333f);
        }
        if (f != 1.0f) {
            z = false;
        }
        return z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:41:0x009e, code lost:
        if (com.android.camera.CameraSettings.getDualVideoConfig().ismDrawSelectWindow() != false) goto L_0x00a0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void configVariables() {
        float f;
        int i = this.targetMode;
        if (!(i == 161 || i == 162 || i == 168 || i == 169 || i == 172 || i == 174 || i == 189)) {
            if (i != 204) {
                if (i != 254) {
                    if (!(i == 179 || i == 180)) {
                        switch (i) {
                            case 183:
                            case 185:
                                break;
                            case 184:
                                this.targetFrameRatio = CameraSettings.isGifOn() ? 1.0f : Util.getRatio(DataRepository.dataItemConfig().getComponentConfigRatio().getPictureSizeRatioString(this.targetMode));
                                MimojiStatusManager2 mimojiStatusManager2 = DataRepository.dataItemLive().getMimojiStatusManager2();
                                if (mimojiStatusManager2.getMimojiRecordState() == 1 && !mimojiStatusManager2.isInMimojiCreate()) {
                                    this.forceVideoPattern = true;
                                    return;
                                }
                                return;
                            default:
                                switch (i) {
                                    case 207:
                                    case 208:
                                    case 209:
                                    case 210:
                                    case 211:
                                    case 212:
                                    case 213:
                                    case 214:
                                        break;
                                    default:
                                        f = Util.getRatio(DataRepository.dataItemConfig().getComponentConfigRatio().getPictureSizeRatioString(this.targetMode));
                                        break;
                                }
                        }
                    }
                } else {
                    int uiStyle = DataRepository.dataItemRunning().getUiStyle();
                    if (uiStyle != 1) {
                        if (uiStyle == 3) {
                            f = 2.2222223f;
                        }
                    }
                }
                this.targetFrameRatio = f;
                return;
            }
            this.targetFrameRatio = 1.3333333f;
            return;
        }
        this.targetFrameRatio = 1.7777777f;
    }

    public int getBottomMaskTargetHeight(int i) {
        if (i == 165 || this.targetFrameRatio == 1.0f) {
            return Display.getBottomHeight() + Display.getSquareBottomCoverHeight();
        } else if (bottomHalfScreen()) {
            return Display.getBottomHeight();
        } else {
            if (this.targetFrameRatio != 1.7777777f || Display.fitDisplayFull(1.3333333f)) {
                return 0;
            }
            return Display.getBottomMargin();
        }
    }

    public int getLeftMaskWidth(int i) {
        if (!C0122O00000o.instance().OOOOoO() || Display.getDisplayRatio() != Display.DISPLAY_RATIO_123) {
        }
        return 0;
    }

    public int getRightMaskWidth(int i) {
        if (!C0122O00000o.instance().OOOOoO() || Display.getDisplayRatio() != Display.DISPLAY_RATIO_123) {
        }
        return 0;
    }

    public int getTopMaskTargetHeight(int i) {
        if (i != 165) {
            float f = this.targetFrameRatio;
            if (f != 1.0f) {
                if (f == 1.7777777f) {
                    if (Display.fitDisplayFull(1.3333333f)) {
                        return 0;
                    }
                } else if (f != 1.3333333f || Display.fitDisplayFull(1.3333333f)) {
                    return 0;
                }
                return Display.getTopMargin() + Display.getTopBarHeight();
            }
        }
        return Display.getTopCoverHeight();
    }

    public PaintConditionReferred setIsFPS960(boolean z) {
        this.isFPS960 = z;
        return this;
    }

    public PaintConditionReferred setNeedSnapButtonAnimation(boolean z) {
        this.needSnapButtonAnimation = z;
        return this;
    }

    public PaintConditionReferred setTargetFrameRatio(float f) {
        this.targetFrameRatio = f;
        return this;
    }
}
