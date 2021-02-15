package O000000o.O000000o.O000000o.O000000o.O000000o;

import java.util.ArrayList;

public final class O000000o {
    public static final int O0000oo = 0;
    public static final int O0000ooO = 2;
    public static final int PRESENT = 1;

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("NOT_PRESENT");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("PRESENT");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("ENUMERATING");
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
            return "NOT_PRESENT";
        }
        if (i == 1) {
            return "PRESENT";
        }
        if (i == 2) {
            return "ENUMERATING";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        sb.append(Integer.toHexString(i));
        return sb.toString();
    }
}
