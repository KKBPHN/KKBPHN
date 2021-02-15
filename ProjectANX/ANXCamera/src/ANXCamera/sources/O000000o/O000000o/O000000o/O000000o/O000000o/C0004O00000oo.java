package O000000o.O000000o.O000000o.O000000o.O000000o;

import java.util.ArrayList;

/* renamed from: O000000o.O000000o.O000000o.O000000o.O000000o.O00000oo reason: case insensitive filesystem */
public final class C0004O00000oo {
    public static final int OFF = 0;
    public static final int ON = 1;

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("OFF");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("ON");
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
            return "OFF";
        }
        if (i == 1) {
            return "ON";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        sb.append(Integer.toHexString(i));
        return sb.toString();
    }
}
