package O000000o.O000000o.O000000o.O000000o.O000000o;

import java.util.ArrayList;

public final class O0000O0o {
    public static final int O000O0o = 1;
    public static final int O000O0o0 = 0;
    public static final int O000O0oO = 2;

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("NOT_AVAILABLE");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("AVAILABLE_OFF");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("AVAILABLE_ON");
            i2 |= 2;
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
            return "NOT_AVAILABLE";
        }
        if (i == 1) {
            return "AVAILABLE_OFF";
        }
        if (i == 2) {
            return "AVAILABLE_ON";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        sb.append(Integer.toHexString(i));
        return sb.toString();
    }
}
