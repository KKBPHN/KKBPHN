package com.bumptech.glide.load.data;

import androidx.annotation.NonNull;
import java.io.FilterInputStream;
import java.io.InputStream;

public final class ExifOrientationStream extends FilterInputStream {
    private static final byte[] EXIF_SEGMENT = {-1, -31, 0, 28, 69, 120, 105, 102, 0, 0, 77, 77, 0, 0, 0, 0, 0, 8, 0, 1, 1, 18, 0, 2, 0, 0, 0, 1, 0};
    private static final int ORIENTATION_POSITION = (SEGMENT_LENGTH + 2);
    private static final int SEGMENT_LENGTH = EXIF_SEGMENT.length;
    private static final int SEGMENT_START_POSITION = 2;
    private final byte orientation;
    private int position;

    public ExifOrientationStream(InputStream inputStream, int i) {
        super(inputStream);
        if (i < -1 || i > 8) {
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot add invalid orientation: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
        this.orientation = (byte) i;
    }

    public void mark(int i) {
        throw new UnsupportedOperationException();
    }

    public boolean markSupported() {
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x001e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int read() {
        int i;
        int i2 = this.position;
        if (i2 >= 2) {
            int i3 = ORIENTATION_POSITION;
            if (i2 <= i3) {
                i = i2 == i3 ? this.orientation : EXIF_SEGMENT[i2 - 2] & -1;
                if (i != -1) {
                    this.position++;
                }
                return i;
            }
        }
        i = super.read();
        if (i != -1) {
        }
        return i;
    }

    public int read(@NonNull byte[] bArr, int i, int i2) {
        int i3;
        int i4 = this.position;
        int i5 = ORIENTATION_POSITION;
        if (i4 > i5) {
            i3 = super.read(bArr, i, i2);
        } else if (i4 == i5) {
            bArr[i] = this.orientation;
            i3 = 1;
        } else if (i4 < 2) {
            i3 = super.read(bArr, i, 2 - i4);
        } else {
            int min = Math.min(i5 - i4, i2);
            System.arraycopy(EXIF_SEGMENT, this.position - 2, bArr, i, min);
            i3 = min;
        }
        if (i3 > 0) {
            this.position += i3;
        }
        return i3;
    }

    public void reset() {
        throw new UnsupportedOperationException();
    }

    public long skip(long j) {
        long skip = super.skip(j);
        if (skip > 0) {
            this.position = (int) (((long) this.position) + skip);
        }
        return skip;
    }
}
