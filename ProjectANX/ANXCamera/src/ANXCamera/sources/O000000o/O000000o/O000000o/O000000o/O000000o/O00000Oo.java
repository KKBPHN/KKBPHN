package O000000o.O000000o.O000000o.O000000o.O000000o;

import java.util.ArrayList;

public final class O00000Oo {
    public static final int BYTE = 0;
    public static final int DOUBLE = 4;
    public static final int FLOAT = 2;
    public static final int INT32 = 1;
    public static final int INT64 = 3;
    public static final int O0000ooo = 5;

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("BYTE");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("INT32");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("FLOAT");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("INT64");
            i2 |= 3;
        }
        if ((i & 4) == 4) {
            arrayList.add("DOUBLE");
            i2 |= 4;
        }
        if ((i & 5) == 5) {
            arrayList.add("RATIONAL");
            i2 |= 5;
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
            return "BYTE";
        }
        if (i == 1) {
            return "INT32";
        }
        if (i == 2) {
            return "FLOAT";
        }
        if (i == 3) {
            return "INT64";
        }
        if (i == 4) {
            return "DOUBLE";
        }
        if (i == 5) {
            return "RATIONAL";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        sb.append(Integer.toHexString(i));
        return sb.toString();
    }
}
