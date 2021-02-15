package vendor.xiaomi.hardware.campostproc.V1_0;

import java.util.ArrayList;

public final class NotifyType {
    public static final int METADATA_RESULT_TYPE = 3;
    public static final int MIFLAW_RESULT_TYPE = 2;
    public static final int RELEASE_INPUT_BUFFER_TYPE = 0;
    public static final int RELEASE_OUTPUT_BUFFER_TYPE = 1;

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("RELEASE_INPUT_BUFFER_TYPE");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("RELEASE_OUTPUT_BUFFER_TYPE");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("MIFLAW_RESULT_TYPE");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("METADATA_RESULT_TYPE");
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
            return "RELEASE_INPUT_BUFFER_TYPE";
        }
        if (i == 1) {
            return "RELEASE_OUTPUT_BUFFER_TYPE";
        }
        if (i == 2) {
            return "MIFLAW_RESULT_TYPE";
        }
        if (i == 3) {
            return "METADATA_RESULT_TYPE";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        sb.append(Integer.toHexString(i));
        return sb.toString();
    }
}
