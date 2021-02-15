package okio;

import com.ss.android.ttve.utils.UIUtils;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import javax.annotation.Nullable;

final class RealBufferedSource implements BufferedSource {
    public final Buffer buffer = new Buffer();
    boolean closed;
    public final Source source;

    RealBufferedSource(Source source2) {
        if (source2 != null) {
            this.source = source2;
            return;
        }
        throw new NullPointerException("source == null");
    }

    public Buffer buffer() {
        return this.buffer;
    }

    public void close() {
        if (!this.closed) {
            this.closed = true;
            this.source.close();
            this.buffer.clear();
        }
    }

    public boolean exhausted() {
        if (!this.closed) {
            return this.buffer.exhausted() && this.source.read(this.buffer, 8192) == -1;
        }
        throw new IllegalStateException("closed");
    }

    public Buffer getBuffer() {
        return this.buffer;
    }

    public long indexOf(byte b) {
        return indexOf(b, 0, Long.MAX_VALUE);
    }

    public long indexOf(byte b, long j) {
        return indexOf(b, j, Long.MAX_VALUE);
    }

    public long indexOf(byte b, long j, long j2) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        } else if (j < 0 || j2 < j) {
            throw new IllegalArgumentException(String.format("fromIndex=%s toIndex=%s", new Object[]{Long.valueOf(j), Long.valueOf(j2)}));
        } else {
            while (j < j2) {
                long indexOf = this.buffer.indexOf(b, j, j2);
                if (indexOf == -1) {
                    Buffer buffer2 = this.buffer;
                    long j3 = buffer2.size;
                    if (j3 >= j2 || this.source.read(buffer2, 8192) == -1) {
                        break;
                    }
                    j = Math.max(j, j3);
                } else {
                    return indexOf;
                }
            }
            return -1;
        }
    }

    public long indexOf(ByteString byteString) {
        return indexOf(byteString, 0);
    }

    public long indexOf(ByteString byteString, long j) {
        if (!this.closed) {
            while (true) {
                long indexOf = this.buffer.indexOf(byteString, j);
                if (indexOf != -1) {
                    return indexOf;
                }
                Buffer buffer2 = this.buffer;
                long j2 = buffer2.size;
                if (this.source.read(buffer2, 8192) == -1) {
                    return -1;
                }
                j = Math.max(j, (j2 - ((long) byteString.size())) + 1);
            }
        } else {
            throw new IllegalStateException("closed");
        }
    }

    public long indexOfElement(ByteString byteString) {
        return indexOfElement(byteString, 0);
    }

    public long indexOfElement(ByteString byteString, long j) {
        if (!this.closed) {
            while (true) {
                long indexOfElement = this.buffer.indexOfElement(byteString, j);
                if (indexOfElement != -1) {
                    return indexOfElement;
                }
                Buffer buffer2 = this.buffer;
                long j2 = buffer2.size;
                if (this.source.read(buffer2, 8192) == -1) {
                    return -1;
                }
                j = Math.max(j, j2);
            }
        } else {
            throw new IllegalStateException("closed");
        }
    }

    public InputStream inputStream() {
        return new InputStream() {
            public int available() {
                RealBufferedSource realBufferedSource = RealBufferedSource.this;
                if (!realBufferedSource.closed) {
                    return (int) Math.min(realBufferedSource.buffer.size, 2147483647L);
                }
                throw new IOException("closed");
            }

            public void close() {
                RealBufferedSource.this.close();
            }

            public int read() {
                RealBufferedSource realBufferedSource = RealBufferedSource.this;
                if (!realBufferedSource.closed) {
                    Buffer buffer = realBufferedSource.buffer;
                    if (buffer.size == 0 && realBufferedSource.source.read(buffer, 8192) == -1) {
                        return -1;
                    }
                    return RealBufferedSource.this.buffer.readByte() & -1;
                }
                throw new IOException("closed");
            }

            public int read(byte[] bArr, int i, int i2) {
                if (!RealBufferedSource.this.closed) {
                    Util.checkOffsetAndCount((long) bArr.length, (long) i, (long) i2);
                    RealBufferedSource realBufferedSource = RealBufferedSource.this;
                    Buffer buffer = realBufferedSource.buffer;
                    if (buffer.size == 0 && realBufferedSource.source.read(buffer, 8192) == -1) {
                        return -1;
                    }
                    return RealBufferedSource.this.buffer.read(bArr, i, i2);
                }
                throw new IOException("closed");
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append(RealBufferedSource.this);
                sb.append(".inputStream()");
                return sb.toString();
            }
        };
    }

    public boolean isOpen() {
        return !this.closed;
    }

    public BufferedSource peek() {
        return Okio.buffer((Source) new PeekSource(this));
    }

    public boolean rangeEquals(long j, ByteString byteString) {
        return rangeEquals(j, byteString, 0, byteString.size());
    }

    public boolean rangeEquals(long j, ByteString byteString, int i, int i2) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        } else if (j < 0 || i < 0 || i2 < 0 || byteString.size() - i < i2) {
            return false;
        } else {
            int i3 = 0;
            while (i3 < i2) {
                long j2 = ((long) i3) + j;
                if (!request(1 + j2) || this.buffer.getByte(j2) != byteString.getByte(i + i3)) {
                    return false;
                }
                i3++;
            }
            return true;
        }
    }

    public int read(ByteBuffer byteBuffer) {
        Buffer buffer2 = this.buffer;
        if (buffer2.size == 0 && this.source.read(buffer2, 8192) == -1) {
            return -1;
        }
        return this.buffer.read(byteBuffer);
    }

    public int read(byte[] bArr) {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) {
        long j = (long) i2;
        Util.checkOffsetAndCount((long) bArr.length, (long) i, j);
        Buffer buffer2 = this.buffer;
        if (buffer2.size == 0 && this.source.read(buffer2, 8192) == -1) {
            return -1;
        }
        return this.buffer.read(bArr, i, (int) Math.min(j, this.buffer.size));
    }

    public long read(Buffer buffer2, long j) {
        if (buffer2 == null) {
            throw new IllegalArgumentException("sink == null");
        } else if (j < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("byteCount < 0: ");
            sb.append(j);
            throw new IllegalArgumentException(sb.toString());
        } else if (!this.closed) {
            Buffer buffer3 = this.buffer;
            if (buffer3.size == 0 && this.source.read(buffer3, 8192) == -1) {
                return -1;
            }
            return this.buffer.read(buffer2, Math.min(j, this.buffer.size));
        } else {
            throw new IllegalStateException("closed");
        }
    }

    public long readAll(Sink sink) {
        if (sink != null) {
            long j = 0;
            while (this.source.read(this.buffer, 8192) != -1) {
                long completeSegmentByteCount = this.buffer.completeSegmentByteCount();
                if (completeSegmentByteCount > 0) {
                    j += completeSegmentByteCount;
                    sink.write(this.buffer, completeSegmentByteCount);
                }
            }
            if (this.buffer.size() <= 0) {
                return j;
            }
            long size = j + this.buffer.size();
            Buffer buffer2 = this.buffer;
            sink.write(buffer2, buffer2.size());
            return size;
        }
        throw new IllegalArgumentException("sink == null");
    }

    public byte readByte() {
        require(1);
        return this.buffer.readByte();
    }

    public byte[] readByteArray() {
        this.buffer.writeAll(this.source);
        return this.buffer.readByteArray();
    }

    public byte[] readByteArray(long j) {
        require(j);
        return this.buffer.readByteArray(j);
    }

    public ByteString readByteString() {
        this.buffer.writeAll(this.source);
        return this.buffer.readByteString();
    }

    public ByteString readByteString(long j) {
        require(j);
        return this.buffer.readByteString(j);
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x002b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long readDecimalLong() {
        require(1);
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (!request((long) i2)) {
                break;
            }
            byte b = this.buffer.getByte((long) i);
            if ((b >= 48 && b <= 57) || (i == 0 && b == 45)) {
                i = i2;
            } else if (i == 0) {
                throw new NumberFormatException(String.format("Expected leading [0-9] or '-' character but was %#x", new Object[]{Byte.valueOf(b)}));
            }
        }
        if (i == 0) {
        }
        return this.buffer.readDecimalLong();
    }

    public void readFully(Buffer buffer2, long j) {
        try {
            require(j);
            this.buffer.readFully(buffer2, j);
        } catch (EOFException e) {
            buffer2.writeAll(this.buffer);
            throw e;
        }
    }

    public void readFully(byte[] bArr) {
        try {
            require((long) bArr.length);
            this.buffer.readFully(bArr);
        } catch (EOFException e) {
            int i = 0;
            while (true) {
                Buffer buffer2 = this.buffer;
                long j = buffer2.size;
                if (j > 0) {
                    int read = buffer2.read(bArr, i, (int) j);
                    if (read != -1) {
                        i += read;
                    } else {
                        throw new AssertionError();
                    }
                } else {
                    throw e;
                }
            }
        }
    }

    public long readHexadecimalUnsignedLong() {
        require(1);
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (!request((long) i2)) {
                break;
            }
            byte b = this.buffer.getByte((long) i);
            if ((b >= 48 && b <= 57) || ((b >= 97 && b <= 102) || (b >= 65 && b <= 70))) {
                i = i2;
            } else if (i == 0) {
                throw new NumberFormatException(String.format("Expected leading [0-9a-fA-F] character but was %#x", new Object[]{Byte.valueOf(b)}));
            }
        }
        return this.buffer.readHexadecimalUnsignedLong();
    }

    public int readInt() {
        require(4);
        return this.buffer.readInt();
    }

    public int readIntLe() {
        require(4);
        return this.buffer.readIntLe();
    }

    public long readLong() {
        require(8);
        return this.buffer.readLong();
    }

    public long readLongLe() {
        require(8);
        return this.buffer.readLongLe();
    }

    public short readShort() {
        require(2);
        return this.buffer.readShort();
    }

    public short readShortLe() {
        require(2);
        return this.buffer.readShortLe();
    }

    public String readString(long j, Charset charset) {
        require(j);
        if (charset != null) {
            return this.buffer.readString(j, charset);
        }
        throw new IllegalArgumentException("charset == null");
    }

    public String readString(Charset charset) {
        if (charset != null) {
            this.buffer.writeAll(this.source);
            return this.buffer.readString(charset);
        }
        throw new IllegalArgumentException("charset == null");
    }

    public String readUtf8() {
        this.buffer.writeAll(this.source);
        return this.buffer.readUtf8();
    }

    public String readUtf8(long j) {
        require(j);
        return this.buffer.readUtf8(j);
    }

    public int readUtf8CodePoint() {
        long j;
        require(1);
        byte b = this.buffer.getByte(0);
        if ((b & -32) == -64) {
            j = 2;
        } else if ((b & -16) == -32) {
            j = 3;
        } else {
            if ((b & -8) == -16) {
                j = 4;
            }
            return this.buffer.readUtf8CodePoint();
        }
        require(j);
        return this.buffer.readUtf8CodePoint();
    }

    @Nullable
    public String readUtf8Line() {
        long indexOf = indexOf(10);
        if (indexOf != -1) {
            return this.buffer.readUtf8Line(indexOf);
        }
        long j = this.buffer.size;
        return j != 0 ? readUtf8(j) : null;
    }

    public String readUtf8LineStrict() {
        return readUtf8LineStrict(Long.MAX_VALUE);
    }

    public String readUtf8LineStrict(long j) {
        if (j >= 0) {
            long j2 = j == Long.MAX_VALUE ? Long.MAX_VALUE : j + 1;
            long indexOf = indexOf(10, 0, j2);
            if (indexOf != -1) {
                return this.buffer.readUtf8Line(indexOf);
            }
            if (j2 < Long.MAX_VALUE && request(j2) && this.buffer.getByte(j2 - 1) == 13 && request(1 + j2) && this.buffer.getByte(j2) == 10) {
                return this.buffer.readUtf8Line(j2);
            }
            Buffer buffer2 = new Buffer();
            Buffer buffer3 = this.buffer;
            buffer3.copyTo(buffer2, 0, Math.min(32, buffer3.size()));
            StringBuilder sb = new StringBuilder();
            sb.append("\\n not found: limit=");
            sb.append(Math.min(this.buffer.size(), j));
            sb.append(" content=");
            sb.append(buffer2.readByteString().hex());
            sb.append(UIUtils.ELLIPSIS_CHAR);
            throw new EOFException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("limit < 0: ");
        sb2.append(j);
        throw new IllegalArgumentException(sb2.toString());
    }

    public boolean request(long j) {
        Buffer buffer2;
        if (j < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("byteCount < 0: ");
            sb.append(j);
            throw new IllegalArgumentException(sb.toString());
        } else if (!this.closed) {
            do {
                buffer2 = this.buffer;
                if (buffer2.size >= j) {
                    return true;
                }
            } while (this.source.read(buffer2, 8192) != -1);
            return false;
        } else {
            throw new IllegalStateException("closed");
        }
    }

    public void require(long j) {
        if (!request(j)) {
            throw new EOFException();
        }
    }

    public int select(Options options) {
        if (!this.closed) {
            do {
                int selectPrefix = this.buffer.selectPrefix(options, true);
                if (selectPrefix == -1) {
                    return -1;
                }
                if (selectPrefix != -2) {
                    this.buffer.skip((long) options.byteStrings[selectPrefix].size());
                    return selectPrefix;
                }
            } while (this.source.read(this.buffer, 8192) != -1);
            return -1;
        }
        throw new IllegalStateException("closed");
    }

    public void skip(long j) {
        if (!this.closed) {
            while (j > 0) {
                Buffer buffer2 = this.buffer;
                if (buffer2.size == 0 && this.source.read(buffer2, 8192) == -1) {
                    throw new EOFException();
                }
                long min = Math.min(j, this.buffer.size());
                this.buffer.skip(min);
                j -= min;
            }
            return;
        }
        throw new IllegalStateException("closed");
    }

    public Timeout timeout() {
        return this.source.timeout();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("buffer(");
        sb.append(this.source);
        sb.append(")");
        return sb.toString();
    }
}
