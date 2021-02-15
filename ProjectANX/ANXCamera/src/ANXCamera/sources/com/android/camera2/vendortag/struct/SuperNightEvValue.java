package com.android.camera2.vendortag.struct;

import java.util.Arrays;

public class SuperNightEvValue {
    private int mSequenceNum;
    private int[] mValue;

    private SuperNightEvValue(int i, int[] iArr) {
        this.mSequenceNum = i;
        this.mValue = iArr;
    }

    public static int getTotalExposureTime(byte[] bArr) {
        int i = 0;
        if (bArr == null) {
            return 0;
        }
        if (bArr.length > 68) {
            i = (int) (((long) ((int) (((long) ((int) (((long) ((int) (((long) 0) + (Byte.toUnsignedLong(bArr[bArr.length - 1]) << 24)))) + (Byte.toUnsignedLong(bArr[bArr.length - 2]) << 16)))) + (Byte.toUnsignedLong(bArr[bArr.length - 3]) << 8)))) + Byte.toUnsignedLong(bArr[bArr.length - 4]));
        }
        return i;
    }

    /* JADX WARNING: type inference failed for: r1v0 */
    /* JADX WARNING: type inference failed for: r1v4 */
    /* JADX WARNING: type inference failed for: r1v5 */
    /* JADX WARNING: type inference failed for: r1v6 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static SuperNightEvValue parseSuperNightEvValue(byte[] bArr, String str, boolean z) {
        int[] iArr;
        int i = 8;
        if (str != null && str.length() > 0) {
            String[] split = str.split(",");
            int length = split.length;
            int[] iArr2 = new int[length];
            for (int i2 = 0; i2 < split.length; i2++) {
                try {
                    iArr2[i2] = Integer.parseInt(split[i2]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            i = length;
            iArr = iArr2;
        } else if (bArr == 0 || bArr.length <= 0) {
            iArr = new int[]{-18, -12, -6, 0, 6, 6, 6, 6};
            if (z) {
                // fill-array-data instruction
                iArr[0] = -18;
                iArr[1] = -12;
                iArr[2] = -6;
                iArr[3] = 0;
                iArr[4] = 0;
                iArr[5] = 0;
                iArr[6] = 0;
                iArr[7] = 0;
            } else {
                // fill-array-data instruction
                iArr[0] = -18;
                iArr[1] = -12;
                iArr[2] = -6;
                iArr[3] = 0;
                iArr[4] = 6;
                iArr[5] = 6;
                iArr[6] = 6;
                iArr[7] = 6;
                i = i;
                iArr = iArr;
            }
        } else {
            byte b = bArr[0];
            int[] iArr3 = new int[b];
            for (int i3 = 1; i3 < b + 1; i3++) {
                iArr3[i3 - 1] = bArr[i3 * 4];
            }
            i = b;
            iArr = iArr3;
        }
        return new SuperNightEvValue(i, iArr);
    }

    public int getSequenceNum() {
        return this.mSequenceNum;
    }

    public int[] getValue() {
        return this.mValue;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SuperNightEvValue{mSequenceNum=");
        sb.append(this.mSequenceNum);
        sb.append(", mValue=");
        sb.append(Arrays.toString(this.mValue));
        sb.append('}');
        return sb.toString();
    }
}
