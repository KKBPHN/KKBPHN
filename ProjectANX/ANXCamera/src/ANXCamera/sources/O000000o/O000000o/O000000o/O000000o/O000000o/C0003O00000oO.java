package O000000o.O000000o.O000000o.O000000o.O000000o;

import java.util.ArrayList;

/* renamed from: O000000o.O000000o.O000000o.O000000o.O000000o.O00000oO reason: case insensitive filesystem */
public final class C0003O00000oO {
    public static final int O00oOoOo = 0;
    public static final int VENDOR = Integer.MIN_VALUE;

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("AOSP");
        int i2 = Integer.MIN_VALUE;
        if ((i & Integer.MIN_VALUE) == Integer.MIN_VALUE) {
            arrayList.add("VENDOR");
        } else {
            i2 = 0;
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
            return "AOSP";
        }
        if (i == Integer.MIN_VALUE) {
            return "VENDOR";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        sb.append(Integer.toHexString(i));
        return sb.toString();
    }
}
