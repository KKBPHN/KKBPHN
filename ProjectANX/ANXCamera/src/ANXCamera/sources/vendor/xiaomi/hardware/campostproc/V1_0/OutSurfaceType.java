package vendor.xiaomi.hardware.campostproc.V1_0;

import java.util.ArrayList;

public final class OutSurfaceType {
    public static final int PREVIEW_MAIN_TYPE = 3;
    public static final int SNAPSHOT_AUX_TYPE = 1;
    public static final int SNAPSHOT_DEPTH_TYPE = 2;
    public static final int SNAPSHOT_MAIN_TYPE = 0;

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("SNAPSHOT_MAIN_TYPE");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("SNAPSHOT_AUX_TYPE");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("SNAPSHOT_DEPTH_TYPE");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("PREVIEW_MAIN_TYPE");
            i2 |= 3;
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
            return "SNAPSHOT_MAIN_TYPE";
        }
        if (i == 1) {
            return "SNAPSHOT_AUX_TYPE";
        }
        if (i == 2) {
            return "SNAPSHOT_DEPTH_TYPE";
        }
        if (i == 3) {
            return "PREVIEW_MAIN_TYPE";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        sb.append(Integer.toHexString(i));
        return sb.toString();
    }
}
