package com.bumptech.glide.load.resource.bitmap;

import android.annotation.TargetApi;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import androidx.annotation.GuardedBy;
import androidx.annotation.VisibleForTesting;
import java.io.File;

public final class HardwareConfigState {
    private static final File FD_SIZE_LIST = new File("/proc/self/fd");
    private static final int MAXIMUM_FDS_FOR_HARDWARE_CONFIGS_O = 700;
    private static final int MAXIMUM_FDS_FOR_HARDWARE_CONFIGS_P = 20000;
    private static final int MINIMUM_DECODES_BETWEEN_FD_CHECKS = 50;
    @VisibleForTesting
    static final int MIN_HARDWARE_DIMENSION_O = 128;
    private static final int MIN_HARDWARE_DIMENSION_P = 0;
    private static volatile HardwareConfigState instance;
    @GuardedBy("this")
    private int decodesSinceLastFdCheck;
    private final int fdCountLimit;
    @GuardedBy("this")
    private boolean isFdSizeBelowHardwareLimit = true;
    private final boolean isHardwareConfigAllowedByDeviceModel = isHardwareConfigAllowedByDeviceModel();
    private final int minHardwareDimension;

    @VisibleForTesting
    HardwareConfigState() {
        int i;
        if (VERSION.SDK_INT >= 28) {
            this.fdCountLimit = MAXIMUM_FDS_FOR_HARDWARE_CONFIGS_P;
            i = 0;
        } else {
            this.fdCountLimit = 700;
            i = 128;
        }
        this.minHardwareDimension = i;
    }

    public static HardwareConfigState getInstance() {
        if (instance == null) {
            synchronized (HardwareConfigState.class) {
                if (instance == null) {
                    instance = new HardwareConfigState();
                }
            }
        }
        return instance;
    }

    private synchronized boolean isFdSizeBelowHardwareLimit() {
        int i = this.decodesSinceLastFdCheck + 1;
        this.decodesSinceLastFdCheck = i;
        if (i >= 50) {
            boolean z = false;
            this.decodesSinceLastFdCheck = 0;
            int length = FD_SIZE_LIST.list().length;
            if (length < this.fdCountLimit) {
                z = true;
            }
            this.isFdSizeBelowHardwareLimit = z;
            if (!this.isFdSizeBelowHardwareLimit && Log.isLoggable("Downsampler", 5)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Excluding HARDWARE bitmap config because we're over the file descriptor limit, file descriptors ");
                sb.append(length);
                sb.append(", limit ");
                sb.append(this.fdCountLimit);
                Log.w("Downsampler", sb.toString());
            }
        }
        return this.isFdSizeBelowHardwareLimit;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean isHardwareConfigAllowedByDeviceModel() {
        char c;
        String str = Build.MODEL;
        boolean z = true;
        if (str != null && str.length() >= 7) {
            String substring = Build.MODEL.substring(0, 7);
            switch (substring.hashCode()) {
                case -1398613787:
                    if (substring.equals("SM-A520")) {
                        c = 6;
                        break;
                    }
                case -1398431166:
                    if (substring.equals("SM-G930")) {
                        c = 5;
                        break;
                    }
                case -1398431161:
                    if (substring.equals("SM-G935")) {
                        c = 4;
                        break;
                    }
                case -1398431073:
                    if (substring.equals("SM-G960")) {
                        c = 2;
                        break;
                    }
                case -1398431068:
                    if (substring.equals("SM-G965")) {
                        c = 3;
                        break;
                    }
                case -1398343746:
                    if (substring.equals("SM-J720")) {
                        c = 1;
                        break;
                    }
                case -1398222624:
                    if (substring.equals("SM-N935")) {
                        c = 0;
                        break;
                    }
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    if (VERSION.SDK_INT == 26) {
                        z = false;
                        break;
                    }
                    break;
                default:
                    return true;
            }
        }
        return z;
    }

    public boolean isHardwareConfigAllowed(int i, int i2, boolean z, boolean z2) {
        if (!z || !this.isHardwareConfigAllowedByDeviceModel || VERSION.SDK_INT < 26 || z2) {
            return false;
        }
        int i3 = this.minHardwareDimension;
        return i >= i3 && i2 >= i3 && isFdSizeBelowHardwareLimit();
    }

    /* access modifiers changed from: 0000 */
    @TargetApi(26)
    public boolean setHardwareConfigIfAllowed(int i, int i2, Options options, boolean z, boolean z2) {
        boolean isHardwareConfigAllowed = isHardwareConfigAllowed(i, i2, z, z2);
        if (isHardwareConfigAllowed) {
            options.inPreferredConfig = Config.HARDWARE;
            options.inMutable = false;
        }
        return isHardwareConfigAllowed;
    }
}
