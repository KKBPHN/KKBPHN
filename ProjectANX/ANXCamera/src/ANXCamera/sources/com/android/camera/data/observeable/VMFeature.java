package com.android.camera.data.observeable;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import androidx.annotation.IntRange;
import androidx.annotation.MainThread;
import androidx.lifecycle.LifecycleOwner;
import io.reactivex.functions.Consumer;
import java.util.HashMap;

public class VMFeature extends VMBase {
    public static final int ERROR_CODE_INSTALL = 258;
    public static final int ERROR_CODE_NETWORK = 257;
    public static final int SCOPE_DOWNLOADING_PROGRESS = 4096;
    public static final int SCOPE_ERROR = 256;
    public static final int SCOPE_NORMAL_STATE = 16;
    public static final int STATE_CANCELLED = 18;
    public static final int STATE_INSTALL_ALL_READY = 22;
    public static final int STATE_INSTALL_OK = 21;
    public static final int STATE_PENDING_DOWNLOAD = 17;
    public static final int STATE_PENDING_INSTALL = 19;
    public static final int STATE_START_INSTALL = 20;
    public RxData mRxLoadingState = new RxData(new HashMap());

    public @interface FeatureModule {
        public static final String MODULE_AMBILIGHT = "ambilight";
        public static final String MODULE_CLONE = "clone";
        public static final String MODULE_DOC2 = "document";
        public static final String MODULE_MILIVE = "milive";
        public static final String MODULE_MIMOJI2 = "mimoji2";
        public static final String MODULE_PANORAMA = "panorama";
        public static final String MODULE_VLOG2 = "vlog2";
    }

    @IntRange(from = 1, to = 100)
    public static final int getDownloadingProgress(int i) {
        return i - 4096;
    }

    @FeatureModule
    public static final String getFeatureNameByLocalMode(int i) {
        if (i != 166 && i != 176) {
            if (i != 179 && i != 209) {
                switch (i) {
                    case 183:
                        if (C0122O00000o.instance().OOO00()) {
                            return FeatureModule.MODULE_MILIVE;
                        }
                        break;
                    case 184:
                        if (C0122O00000o.instance().OOO00()) {
                            return FeatureModule.MODULE_MIMOJI2;
                        }
                        break;
                    case 185:
                        if (C0122O00000o.instance().OOO00()) {
                            return FeatureModule.MODULE_CLONE;
                        }
                        break;
                    case 186:
                        if (C0122O00000o.instance().OOO00() && C0122O00000o.instance().OO0ooo()) {
                            return FeatureModule.MODULE_DOC2;
                        }
                    case 187:
                        if (C0122O00000o.instance().OOO00()) {
                            return "ambilight";
                        }
                        break;
                }
            } else {
                return FeatureModule.MODULE_VLOG2;
            }
        } else if (C0122O00000o.instance().OOO00()) {
            return FeatureModule.MODULE_PANORAMA;
        }
        return null;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final int getLocalModeByFeatureName(@FeatureModule String str) {
        char c;
        switch (str.hashCode()) {
            case -1074098040:
                if (str.equals(FeatureModule.MODULE_MILIVE)) {
                    c = 2;
                    break;
                }
            case 94756189:
                if (str.equals(FeatureModule.MODULE_CLONE)) {
                    c = 4;
                    break;
                }
            case 112302820:
                if (str.equals(FeatureModule.MODULE_VLOG2)) {
                    c = 0;
                    break;
                }
            case 861720859:
                if (str.equals(FeatureModule.MODULE_DOC2)) {
                    c = 3;
                    break;
                }
            case 1063790037:
                if (str.equals(FeatureModule.MODULE_MIMOJI2)) {
                    c = 5;
                    break;
                }
            case 1069983349:
                if (str.equals(FeatureModule.MODULE_PANORAMA)) {
                    c = 1;
                    break;
                }
            case 1648420739:
                if (str.equals("ambilight")) {
                    c = 6;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return 209;
            case 1:
                if (C0122O00000o.instance().OOO00()) {
                    return 166;
                }
                break;
            case 2:
                if (C0122O00000o.instance().OOO00()) {
                    return 183;
                }
                break;
            case 3:
                if (C0122O00000o.instance().OOO00() && C0122O00000o.instance().OO0ooo()) {
                    return 186;
                }
            case 4:
                if (C0122O00000o.instance().OOO00()) {
                    return 185;
                }
                break;
            case 5:
                if (C0122O00000o.instance().OOO00()) {
                    return 184;
                }
                break;
            case 6:
                if (C0122O00000o.instance().OOO00()) {
                    return 187;
                }
                break;
        }
        return -1;
    }

    public static final int getScope(int i) {
        if ((i & 4096) != 0) {
            return 4096;
        }
        return (i & 256) != 0 ? 256 : 16;
    }

    public static final int wrapDownloadingProgress(@IntRange(from = 1, to = 100) int i) {
        return i + 4096;
    }

    /* access modifiers changed from: protected */
    public boolean achieveEndOfCycle() {
        return false;
    }

    public HashMap getState() {
        return (HashMap) this.mRxLoadingState.get();
    }

    public void removeFeature(@FeatureModule String str) {
        ((HashMap) this.mRxLoadingState.get()).remove(str);
    }

    /* access modifiers changed from: protected */
    public void rollbackData() {
    }

    public void startObservable(LifecycleOwner lifecycleOwner, Consumer consumer) {
        this.mRxLoadingState.observable(lifecycleOwner).subscribe(consumer);
    }

    @MainThread
    public void updateState(@FeatureModule String str, Integer num) {
        ((HashMap) this.mRxLoadingState.get()).put(str, num);
        this.mRxLoadingState.notifyChanged();
        judge();
    }
}
