package com.bumptech.glide.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

public class ExceptionCatchingInputStream extends InputStream {
    private static final Queue QUEUE = Util.createQueue(0);
    private IOException exception;
    private InputStream wrapped;

    ExceptionCatchingInputStream() {
    }

    static void clearQueue() {
        while (!QUEUE.isEmpty()) {
            QUEUE.remove();
        }
    }

    @NonNull
    public static ExceptionCatchingInputStream obtain(@NonNull InputStream inputStream) {
        ExceptionCatchingInputStream exceptionCatchingInputStream;
        synchronized (QUEUE) {
            exceptionCatchingInputStream = (ExceptionCatchingInputStream) QUEUE.poll();
        }
        if (exceptionCatchingInputStream == null) {
            exceptionCatchingInputStream = new ExceptionCatchingInputStream();
        }
        exceptionCatchingInputStream.setInputStream(inputStream);
        return exceptionCatchingInputStream;
    }

    public int available() {
        return this.wrapped.available();
    }

    public void close() {
        this.wrapped.close();
    }

    @Nullable
    public IOException getException() {
        return this.exception;
    }

    public void mark(int i) {
        this.wrapped.mark(i);
    }

    public boolean markSupported() {
        return this.wrapped.markSupported();
    }

    public int read() {
        try {
            this = this;
            this = this.wrapped.read();
            r1 = this;
            return this;
        } catch (IOException e) {
            r1.exception = e;
            return -1;
        }
    }

    public int read(byte[] bArr) {
        try {
            this = this;
            this = this.wrapped.read(bArr);
            r1 = this;
            return this;
        } catch (IOException e) {
            r1.exception = e;
            return -1;
        }
    }

    public int read(byte[] bArr, int i, int i2) {
        try {
            this = this;
            this = this.wrapped.read(bArr, i, i2);
            r1 = this;
            return this;
        } catch (IOException e) {
            r1.exception = e;
            return -1;
        }
    }

    public void release() {
        this.exception = null;
        this.wrapped = null;
        synchronized (QUEUE) {
            QUEUE.offer(this);
        }
    }

    public synchronized void reset() {
        this.wrapped.reset();
    }

    /* access modifiers changed from: 0000 */
    public void setInputStream(@NonNull InputStream inputStream) {
        this.wrapped = inputStream;
    }

    public long skip(long j) {
        try {
            this = this;
            this = this.wrapped.skip(j);
            r1 = this;
            return this;
        } catch (IOException e) {
            r1.exception = e;
            return 0;
        }
    }
}
