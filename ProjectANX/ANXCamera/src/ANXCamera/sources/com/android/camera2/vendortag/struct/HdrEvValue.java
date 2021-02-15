package com.android.camera2.vendortag.struct;

import com.android.camera.log.Log;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.Serializable;
import java.util.Arrays;

public class HdrEvValue implements Serializable {
    public static final int HDR_TYPE_HDR = 0;
    public static final int HDR_TYPE_LLHDR = 1;
    private static final byte[] NEW_HDR_TYPE = {104, 100, 114, 32};
    public static final String TAG = "HdrEvValue";
    private static final long serialVersionUID = 1;
    private int[] mHdrCheckerEvValue;
    private int mHdrType;
    private int mSequenceNum;

    public HdrEvValue(byte[] bArr) {
        this(bArr, false);
    }

    public HdrEvValue(byte[] bArr, boolean z) {
        int[] iArr;
        int i = 0;
        this.mHdrType = 0;
        if (bArr == 0 || bArr.length < 1 || bArr[0] == 0) {
            if (!z) {
                this.mSequenceNum = 3;
                iArr = new int[]{-6, 0, 6};
            } else {
                this.mSequenceNum = 12;
                iArr = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -6, -12};
            }
            this.mHdrCheckerEvValue = iArr;
            return;
        }
        int i2 = 8;
        if (bArr.length <= 8 || !isNewHdrFormat(bArr)) {
            i2 = 0;
        } else {
            Log.d(TAG, "HdrEvValue is new version");
            if (bArr[4] == 1) {
                this.mHdrType = 1;
            }
        }
        this.mSequenceNum = bArr[i2];
        this.mHdrCheckerEvValue = new int[this.mSequenceNum];
        while (i < this.mSequenceNum) {
            int i3 = i + 1;
            this.mHdrCheckerEvValue[i] = bArr[(i3 * 4) + i2];
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("HdrEvValue: evValue[");
            sb.append(i);
            sb.append("]=");
            sb.append(this.mHdrCheckerEvValue[i]);
            Log.d(str, sb.toString());
            i = i3;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0023, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002c, code lost:
        throw r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean isNewHdrFormat(byte[] bArr) {
        try {
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
            byte[] bArr2 = new byte[4];
            dataInputStream.read(bArr2);
            if (Arrays.equals(bArr2, NEW_HDR_TYPE)) {
                dataInputStream.close();
                return true;
            }
            dataInputStream.close();
            return false;
        } catch (Exception e) {
            Log.d(TAG, "Exception parsing hdrCheckerValues type box.", (Throwable) e);
        } catch (Throwable th) {
            r2.addSuppressed(th);
        }
    }

    public int[] getHdrCheckerEvValue() {
        return this.mHdrCheckerEvValue;
    }

    public int getHdrType() {
        return this.mHdrType;
    }

    public int getSequenceNum() {
        return this.mSequenceNum;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(16);
        int[] iArr = this.mHdrCheckerEvValue;
        if (iArr != null && iArr.length > 0) {
            sb.append("[");
            int i = 0;
            while (true) {
                int[] iArr2 = this.mHdrCheckerEvValue;
                if (i >= iArr2.length) {
                    break;
                }
                sb.append(iArr2[i]);
                if (i != this.mHdrCheckerEvValue.length - 1) {
                    sb.append(",");
                }
                i++;
            }
            sb.append("]");
        }
        return sb.toString();
    }
}
