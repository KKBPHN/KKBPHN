package vendor.xiaomi.hardware.campostproc.V1_0;

import java.util.ArrayList;

public final class PostProcType {
    public static final int PROCESS_CLEARSHOT = 2;
    public static final int PROCESS_HHT = 3;
    public static final int PROCESS_JPEG = 4;
    public static final int PROCESS_NORMAL = 0;
    public static final int PROCESS_SR = 1;

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("PROCESS_NORMAL");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("PROCESS_SR");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("PROCESS_CLEARSHOT");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("PROCESS_HHT");
            i2 |= 3;
        }
        if ((i & 4) == 4) {
            arrayList.add("PROCESS_JPEG");
            i2 |= 4;
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
            return "PROCESS_NORMAL";
        }
        if (i == 1) {
            return "PROCESS_SR";
        }
        if (i == 2) {
            return "PROCESS_CLEARSHOT";
        }
        if (i == 3) {
            return "PROCESS_HHT";
        }
        if (i == 4) {
            return "PROCESS_JPEG";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        sb.append(Integer.toHexString(i));
        return sb.toString();
    }
}
