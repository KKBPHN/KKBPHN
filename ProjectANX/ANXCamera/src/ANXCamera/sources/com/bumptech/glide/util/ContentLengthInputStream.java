package com.bumptech.glide.util;

import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ContentLengthInputStream extends FilterInputStream {
    private static final String TAG = "ContentLengthStream";
    private static final int UNKNOWN = -1;
    private final long contentLength;
    private int readSoFar;

    private ContentLengthInputStream(@NonNull InputStream inputStream, long j) {
        super(inputStream);
        this.contentLength = j;
    }

    private int checkReadSoFarOrThrow(int i) {
        if (i >= 0) {
            this.readSoFar += i;
        } else if (this.contentLength - ((long) this.readSoFar) > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to read all expected data, expected: ");
            sb.append(this.contentLength);
            sb.append(", but read: ");
            sb.append(this.readSoFar);
            throw new IOException(sb.toString());
        }
        return i;
    }

    @NonNull
    public static InputStream obtain(@NonNull InputStream inputStream, long j) {
        return new ContentLengthInputStream(inputStream, j);
    }

    @NonNull
    public static InputStream obtain(@NonNull InputStream inputStream, @Nullable String str) {
        return obtain(inputStream, (long) parseContentLength(str));
    }

    private static int parseContentLength(@Nullable String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                r4 = str;
                r4 = Integer.parseInt(str);
                r4 = r4;
                return r4;
            } catch (NumberFormatException e) {
                String str2 = TAG;
                if (Log.isLoggable(str2, 3)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("failed to parse content length header: ");
                    sb.append(r4);
                    Log.d(str2, sb.toString(), e);
                }
            }
        }
        return -1;
    }

    public synchronized int available() {
        return (int) Math.max(this.contentLength - ((long) this.readSoFar), (long) this.in.available());
    }

    public synchronized int read() {
        int read;
        read = super.read();
        checkReadSoFarOrThrow(read >= 0 ? 1 : -1);
        return read;
    }

    public int read(byte[] bArr) {
        return read(bArr, 0, bArr.length);
    }

    public synchronized int read(byte[] bArr, int i, int i2) {
        int read;
        read = super.read(bArr, i, i2);
        checkReadSoFarOrThrow(read);
        return read;
    }
}
