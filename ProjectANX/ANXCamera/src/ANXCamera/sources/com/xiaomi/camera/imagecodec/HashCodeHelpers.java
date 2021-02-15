package com.xiaomi.camera.imagecodec;

public final class HashCodeHelpers {
    public static int hashCode(int... iArr) {
        if (iArr == null) {
            return 0;
        }
        int i = 1;
        for (int i2 : iArr) {
            i = ((i << 5) - i) ^ i2;
        }
        return i;
    }
}
