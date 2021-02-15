package vendor.xiaomi.hardware.campostproc.V1_0;

import com.android.camera.CameraIntentManager.ControlActions;
import java.util.ArrayList;

public final class Error {
    public static final int ABORT = 9;
    public static final int BAD_STREAMID = 1;
    public static final int DEVICE_BAD_STATE = 7;
    public static final int INVALID_CALLBACK_PTR = 8;
    public static final int INVALID_HANDLE = 3;
    public static final int MALLOC_FAIL = 5;
    public static final int MAX_SESSIONS = 2;
    public static final int NONE = 0;
    public static final int POSTPROC_FAIL = 6;
    public static final int SESSION_NOT_INIT = 4;
    public static final int UNSUPPORTED_RESOLUTION = 10;

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(ControlActions.CONTROL_ACTION_UNKNOWN);
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("BAD_STREAMID");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("MAX_SESSIONS");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("INVALID_HANDLE");
            i2 |= 3;
        }
        if ((i & 4) == 4) {
            arrayList.add("SESSION_NOT_INIT");
            i2 |= 4;
        }
        if ((i & 5) == 5) {
            arrayList.add("MALLOC_FAIL");
            i2 |= 5;
        }
        if ((i & 6) == 6) {
            arrayList.add("POSTPROC_FAIL");
            i2 |= 6;
        }
        if ((i & 7) == 7) {
            arrayList.add("DEVICE_BAD_STATE");
            i2 |= 7;
        }
        if ((i & 8) == 8) {
            arrayList.add("INVALID_CALLBACK_PTR");
            i2 |= 8;
        }
        if ((i & 9) == 9) {
            arrayList.add("ABORT");
            i2 |= 9;
        }
        if ((i & 10) == 10) {
            arrayList.add("UNSUPPORTED_RESOLUTION");
            i2 |= 10;
        }
        if (i != i2) {
            StringBuilder sb = new StringBuilder();
            sb.append("0x");
            sb.append(Integer.toHexString(i & (~i2)));
            arrayList.add(sb.toString());
        }
        return String.join(" | ", arrayList);
    }

    public static final String toString(int i) {
        if (i == 0) {
            return ControlActions.CONTROL_ACTION_UNKNOWN;
        }
        if (i == 1) {
            return "BAD_STREAMID";
        }
        if (i == 2) {
            return "MAX_SESSIONS";
        }
        if (i == 3) {
            return "INVALID_HANDLE";
        }
        if (i == 4) {
            return "SESSION_NOT_INIT";
        }
        if (i == 5) {
            return "MALLOC_FAIL";
        }
        if (i == 6) {
            return "POSTPROC_FAIL";
        }
        if (i == 7) {
            return "DEVICE_BAD_STATE";
        }
        if (i == 8) {
            return "INVALID_CALLBACK_PTR";
        }
        if (i == 9) {
            return "ABORT";
        }
        if (i == 10) {
            return "UNSUPPORTED_RESOLUTION";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        sb.append(Integer.toHexString(i));
        return sb.toString();
    }
}
