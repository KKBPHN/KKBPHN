package O000000o.O000000o.O000000o.O000000o.O000000o;

import java.util.ArrayList;

public final class O00000o {
    public static final int CAMERA_DISCONNECTED = 6;
    public static final int CAMERA_IN_USE = 2;
    public static final int INTERNAL_ERROR = 7;
    public static final int MAX_CAMERAS_IN_USE = 3;
    public static final int O000O00o = 1;
    public static final int O000O0OO = 4;
    public static final int O000O0Oo = 5;
    public static final int OK = 0;

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("OK");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("ILLEGAL_ARGUMENT");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("CAMERA_IN_USE");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("MAX_CAMERAS_IN_USE");
            i2 |= 3;
        }
        if ((i & 4) == 4) {
            arrayList.add("METHOD_NOT_SUPPORTED");
            i2 |= 4;
        }
        if ((i & 5) == 5) {
            arrayList.add("OPERATION_NOT_SUPPORTED");
            i2 |= 5;
        }
        if ((i & 6) == 6) {
            arrayList.add("CAMERA_DISCONNECTED");
            i2 |= 6;
        }
        if ((i & 7) == 7) {
            arrayList.add("INTERNAL_ERROR");
            i2 |= 7;
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
            return "OK";
        }
        if (i == 1) {
            return "ILLEGAL_ARGUMENT";
        }
        if (i == 2) {
            return "CAMERA_IN_USE";
        }
        if (i == 3) {
            return "MAX_CAMERAS_IN_USE";
        }
        if (i == 4) {
            return "METHOD_NOT_SUPPORTED";
        }
        if (i == 5) {
            return "OPERATION_NOT_SUPPORTED";
        }
        if (i == 6) {
            return "CAMERA_DISCONNECTED";
        }
        if (i == 7) {
            return "INTERNAL_ERROR";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        sb.append(Integer.toHexString(i));
        return sb.toString();
    }
}
