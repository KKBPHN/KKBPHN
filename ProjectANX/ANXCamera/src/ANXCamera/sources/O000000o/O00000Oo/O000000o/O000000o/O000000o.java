package O000000o.O00000Oo.O000000o.O000000o;

import java.util.ArrayList;

public final class O000000o {
    public static final int IS_32BIT = 2;
    public static final int IS_64BIT = 1;
    public static final int UNKNOWN = 0;

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("UNKNOWN");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("IS_64BIT");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("IS_32BIT");
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
            return "UNKNOWN";
        }
        if (i == 1) {
            return "IS_64BIT";
        }
        if (i == 2) {
            return "IS_32BIT";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        sb.append(Integer.toHexString(i));
        return sb.toString();
    }
}
