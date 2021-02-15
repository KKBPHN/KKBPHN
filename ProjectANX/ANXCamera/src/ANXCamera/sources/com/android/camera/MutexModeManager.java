package com.android.camera;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import androidx.annotation.MainThread;
import com.android.camera.statistic.MistatsConstants.FeatureName;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MutexModeManager {
    public static final int MUTEX_AOHDR = 2;
    public static final int MUTEX_BURST_SHOOT = 6;
    public static final int MUTEX_GROUP = 8;
    public static final int MUTEX_HAND_NIGHT = 3;
    public static final int MUTEX_MORPHO_HDR = 1;
    public static final int MUTEX_NONE = 0;
    public static final int MUTEX_RAW = 4;
    public static final int MUTEX_SCENE_HDR = 5;
    public static final int MUTEX_STEREO = 7;
    public static final int MUTEX_SUPER_RESOLUTION = 9;
    private int mCurrentMutexMode = 0;
    private boolean mIsMandatory;
    private MutexCallBack mMutexCallBack;

    public interface MutexCallBack {
        void enterMutexMode(int i);

        void exitMutexMode(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MutexMode {
    }

    public MutexModeManager(MutexCallBack mutexCallBack) {
        this.mMutexCallBack = mutexCallBack;
    }

    private void enter(int i) {
        if (i != 0) {
            MutexCallBack mutexCallBack = this.mMutexCallBack;
            if (mutexCallBack != null && this.mCurrentMutexMode != i) {
                this.mCurrentMutexMode = i;
                mutexCallBack.enterMutexMode(i);
            }
        }
    }

    private void exit(int i) {
        if (i != 0) {
            MutexCallBack mutexCallBack = this.mMutexCallBack;
            if (mutexCallBack != null) {
                this.mCurrentMutexMode = 0;
                mutexCallBack.exitMutexMode(i);
            }
        }
    }

    private void switchMutexMode(int i, int i2) {
        if (i != i2) {
            exit(i);
            enter(i2);
        }
    }

    public void clearMandatoryFlag() {
        this.mIsMandatory = false;
    }

    public String getAlgorithmName() {
        int i = this.mCurrentMutexMode;
        if (i != 1) {
            if (i == 2) {
                return "AO_HDR";
            }
            if (i == 3) {
                return FeatureName.VALUE_HHT;
            }
            if (i != 5) {
                return "";
            }
        }
        return "HDR";
    }

    public int getMutexMode() {
        return this.mCurrentMutexMode;
    }

    public String getSuffix() {
        if (this.mCurrentMutexMode == 4) {
            return "_RAW";
        }
        String str = "";
        if (!C0124O00000oO.OOoOooo() && !Util.isForceNameSuffix()) {
            return str;
        }
        int i = this.mCurrentMutexMode;
        if (i != 1) {
            if (i == 2) {
                return "_AO_HDR";
            }
            if (i == 3) {
                return "_HHT";
            }
            if (i != 5) {
                return str;
            }
        }
        return "_HDR";
    }

    public boolean inMandatoryMode() {
        return this.mIsMandatory;
    }

    public boolean isAoHdr() {
        return this.mCurrentMutexMode == 2;
    }

    public boolean isBurstShoot() {
        return this.mCurrentMutexMode == 6;
    }

    public boolean isGroupShot() {
        return this.mCurrentMutexMode == 8;
    }

    public boolean isHandNight() {
        return this.mCurrentMutexMode == 3;
    }

    public boolean isHdr() {
        int i = this.mCurrentMutexMode;
        return i == 2 || i == 1 || i == 5;
    }

    public boolean isHdrSupportTorch(boolean z) {
        return C0124O00000oO.Oo0O00() && z && this.mCurrentMutexMode == 1;
    }

    public boolean isMorphoHdr() {
        return this.mCurrentMutexMode == 1;
    }

    public boolean isNeedComposed() {
        int i = this.mCurrentMutexMode;
        return (i == 0 || i == 2 || i == 6) ? false : true;
    }

    public boolean isNormal() {
        return this.mCurrentMutexMode == 0;
    }

    public boolean isRAW() {
        return this.mCurrentMutexMode == 4;
    }

    public boolean isSceneHdr() {
        return this.mCurrentMutexMode == 5;
    }

    public boolean isSuperResolution() {
        return this.mCurrentMutexMode == 9;
    }

    public boolean isSupportedFlashOn() {
        int i = this.mCurrentMutexMode;
        return i == 0 || i == 4 || i == 3;
    }

    public boolean isSupportedTorch() {
        if (C0124O00000oO.Oo0O00()) {
            int i = this.mCurrentMutexMode;
            if (i == 0 || i == 2 || i == 6 || i == 8 || i == 9) {
                return true;
            }
        }
        return false;
    }

    @MainThread
    public void resetMutexMode() {
        this.mIsMandatory = false;
        switchMutexMode(this.mCurrentMutexMode, 0);
    }

    @MainThread
    public void setMutexMode(int i) {
        this.mIsMandatory = false;
        switchMutexMode(this.mCurrentMutexMode, i);
    }

    public void setMutexModeMandatory(int i) {
        this.mIsMandatory = true;
        switchMutexMode(this.mCurrentMutexMode, i);
    }
}
