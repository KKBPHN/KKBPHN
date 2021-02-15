package com.bumptech.glide.load.resource.bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RecyclableBufferedInputStream extends FilterInputStream {
    private volatile byte[] buf;
    private final ArrayPool byteArrayPool;
    private int count;
    private int marklimit;
    private int markpos;
    private int pos;

    class InvalidMarkException extends IOException {
        private static final long serialVersionUID = -4338378848813561757L;

        InvalidMarkException(String str) {
            super(str);
        }
    }

    public RecyclableBufferedInputStream(@NonNull InputStream inputStream, @NonNull ArrayPool arrayPool) {
        this(inputStream, arrayPool, 65536);
    }

    @VisibleForTesting
    RecyclableBufferedInputStream(@NonNull InputStream inputStream, @NonNull ArrayPool arrayPool, int i) {
        super(inputStream);
        this.markpos = -1;
        this.byteArrayPool = arrayPool;
        this.buf = (byte[]) arrayPool.get(i, byte[].class);
    }

    private int fillbuf(InputStream inputStream, byte[] bArr) {
        int i = this.markpos;
        if (i != -1) {
            int i2 = this.pos - i;
            int i3 = this.marklimit;
            if (i2 < i3) {
                if (i == 0 && i3 > bArr.length && this.count == bArr.length) {
                    int length = bArr.length * 2;
                    if (length > i3) {
                        length = i3;
                    }
                    byte[] bArr2 = (byte[]) this.byteArrayPool.get(length, byte[].class);
                    System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
                    this.buf = bArr2;
                    this.byteArrayPool.put(bArr);
                    bArr = bArr2;
                } else {
                    int i4 = this.markpos;
                    if (i4 > 0) {
                        System.arraycopy(bArr, i4, bArr, 0, bArr.length - i4);
                    }
                }
                this.pos -= this.markpos;
                this.markpos = 0;
                this.count = 0;
                int i5 = this.pos;
                int read = inputStream.read(bArr, i5, bArr.length - i5);
                int i6 = this.pos;
                if (read > 0) {
                    i6 += read;
                }
                this.count = i6;
                return read;
            }
        }
        int read2 = inputStream.read(bArr);
        if (read2 > 0) {
            this.markpos = -1;
            this.pos = 0;
            this.count = read2;
        }
        return read2;
    }

    private static IOException streamClosed() {
        throw new IOException("BufferedInputStream is closed");
    }

    public synchronized int available() {
        InputStream inputStream;
        inputStream = this.in;
        if (this.buf == null || inputStream == null) {
            streamClosed();
            throw null;
        }
        return (this.count - this.pos) + inputStream.available();
    }

    public void close() {
        if (this.buf != null) {
            this.byteArrayPool.put(this.buf);
            this.buf = null;
        }
        InputStream inputStream = this.in;
        this.in = null;
        if (inputStream != null) {
            inputStream.close();
        }
    }

    public synchronized void fixMarkLimit() {
        this.marklimit = this.buf.length;
    }

    public synchronized void mark(int i) {
        this.marklimit = Math.max(this.marklimit, i);
        this.markpos = this.pos;
    }

    public boolean markSupported() {
        return true;
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:19:0x0026=Splitter:B:19:0x0026, B:11:0x0019=Splitter:B:11:0x0019, B:28:0x003b=Splitter:B:28:0x003b} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int read() {
        byte[] bArr = this.buf;
        InputStream inputStream = this.in;
        if (bArr == null || inputStream == null) {
            streamClosed();
            throw null;
        } else if (this.pos >= this.count && fillbuf(inputStream, bArr) == -1) {
            return -1;
        } else {
            if (bArr != this.buf) {
                bArr = this.buf;
                if (bArr == null) {
                    streamClosed();
                    throw null;
                }
            }
            if (this.count - this.pos <= 0) {
                return -1;
            }
            int i = this.pos;
            this.pos = i + 1;
            return bArr[i] & -1;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x003b, code lost:
        return r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0051, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x005e, code lost:
        return r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int read(@NonNull byte[] bArr, int i, int i2) {
        int i3;
        int i4;
        byte[] bArr2 = this.buf;
        if (bArr2 == null) {
            streamClosed();
            throw null;
        } else if (i2 == 0) {
            return 0;
        } else {
            InputStream inputStream = this.in;
            if (inputStream != null) {
                if (this.pos < this.count) {
                    int i5 = this.count - this.pos >= i2 ? i2 : this.count - this.pos;
                    System.arraycopy(bArr2, this.pos, bArr, i, i5);
                    this.pos += i5;
                    if (i5 != i2 && inputStream.available() != 0) {
                        i += i5;
                        i3 = i2 - i5;
                    }
                } else {
                    i3 = i2;
                }
                while (true) {
                    int i6 = -1;
                    if (this.markpos == -1 && i3 >= bArr2.length) {
                        i4 = inputStream.read(bArr, i, i3);
                        if (i4 == -1) {
                            if (i3 != i2) {
                                i6 = i2 - i3;
                            }
                        }
                    } else if (fillbuf(inputStream, bArr2) != -1) {
                        if (bArr2 != this.buf) {
                            bArr2 = this.buf;
                            if (bArr2 == null) {
                                streamClosed();
                                throw null;
                            }
                        }
                        i4 = this.count - this.pos >= i3 ? i3 : this.count - this.pos;
                        System.arraycopy(bArr2, this.pos, bArr, i, i4);
                        this.pos += i4;
                    } else if (i3 != i2) {
                        i6 = i2 - i3;
                    }
                    i3 -= i4;
                    if (i3 == 0) {
                        return i2;
                    }
                    if (inputStream.available() == 0) {
                        return i2 - i3;
                    }
                    i += i4;
                }
            } else {
                streamClosed();
                throw null;
            }
        }
    }

    public synchronized void release() {
        if (this.buf != null) {
            this.byteArrayPool.put(this.buf);
            this.buf = null;
        }
    }

    public synchronized void reset() {
        if (this.buf == null) {
            throw new IOException("Stream is closed");
        } else if (-1 != this.markpos) {
            this.pos = this.markpos;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Mark has been invalidated, pos: ");
            sb.append(this.pos);
            sb.append(" markLimit: ");
            sb.append(this.marklimit);
            throw new InvalidMarkException(sb.toString());
        }
    }

    public synchronized long skip(long j) {
        if (j < 1) {
            return 0;
        }
        byte[] bArr = this.buf;
        if (bArr != null) {
            InputStream inputStream = this.in;
            if (inputStream == null) {
                streamClosed();
                throw null;
            } else if (((long) (this.count - this.pos)) >= j) {
                this.pos = (int) (((long) this.pos) + j);
                return j;
            } else {
                long j2 = ((long) this.count) - ((long) this.pos);
                this.pos = this.count;
                if (this.markpos == -1 || j > ((long) this.marklimit)) {
                    return j2 + inputStream.skip(j - j2);
                } else if (fillbuf(inputStream, bArr) == -1) {
                    return j2;
                } else {
                    if (((long) (this.count - this.pos)) >= j - j2) {
                        this.pos = (int) ((((long) this.pos) + j) - j2);
                        return j;
                    }
                    long j3 = (j2 + ((long) this.count)) - ((long) this.pos);
                    this.pos = this.count;
                    return j3;
                }
            }
        } else {
            streamClosed();
            throw null;
        }
    }
}
