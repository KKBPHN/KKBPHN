package miui.media;

import java.nio.MappedByteBuffer;
import miui.media.Recorder.ErrorCode;

class RecorderUtils {
    RecorderUtils() {
    }

    public static int convertErrorCode(int i, boolean z) {
        if (!z) {
            switch (i) {
                case 1:
                    return 1001;
                case 2:
                    return 1002;
                case 3:
                    return 1003;
                case 4:
                    return 1004;
                case 5:
                    return 1005;
                case 6:
                    return 1006;
                case 7:
                    return 1007;
                case 8:
                    return 1009;
            }
        } else if (i == 100) {
            return 1010;
        } else {
            if (i == 800) {
                return 1008;
            }
            if (i == 801) {
                return 1007;
            }
        }
        return ErrorCode.UNKNOWN;
    }

    public static byte[] getBytes(long j) {
        return new byte[]{(byte) ((int) (j & 255)), (byte) ((int) ((j >> 8) & 255)), (byte) ((int) ((j >> 16) & 255)), (byte) ((int) ((j >> 24) & 255))};
    }

    public static byte[] getWaveHeader(int i, int i2) {
        int i3 = i == 16 ? 1 : 2;
        long j = (long) (((i2 * i3) * 16) / 8);
        return new byte[]{82, 73, 70, 70, 0, 0, 0, 0, 87, 65, 86, 69, 102, 109, 116, 32, 16, 0, 0, 0, 1, 0, (byte) i3, 0, (byte) (i2 & 255), (byte) ((i2 >> 8) & 255), (byte) ((i2 >> 16) & 255), (byte) ((i2 >> 24) & 255), (byte) ((int) (j & 255)), (byte) ((int) ((j >> 8) & 255)), (byte) ((int) ((j >> 16) & 255)), (byte) ((int) ((j >> 24) & 255)), (byte) ((i3 * 16) / 8), 0, 16, 0, 100, 97, 116, 97, 0, 0, 0, 0};
    }

    public static byte[] short2byte(short[] sArr, int i) {
        byte[] bArr = new byte[(i * 2)];
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = i2 * 2;
            bArr[i3] = (byte) (sArr[i2] & 255);
            bArr[i3 + 1] = (byte) (sArr[i2] >> 8);
            sArr[i2] = 0;
        }
        return bArr;
    }

    public static void updateBytes(MappedByteBuffer mappedByteBuffer, int i, byte[] bArr) {
        for (int i2 = 0; i2 < 4; i2++) {
            mappedByteBuffer.put(i + i2, bArr[i2]);
        }
    }
}
