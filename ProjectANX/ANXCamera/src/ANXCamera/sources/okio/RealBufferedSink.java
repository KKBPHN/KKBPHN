package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

final class RealBufferedSink implements BufferedSink {
    public final Buffer buffer = new Buffer();
    boolean closed;
    public final Sink sink;

    RealBufferedSink(Sink sink2) {
        if (sink2 != null) {
            this.sink = sink2;
            return;
        }
        throw new NullPointerException("sink == null");
    }

    public Buffer buffer() {
        return this.buffer;
    }

    public void close() {
        if (!this.closed) {
            try {
                if (this.buffer.size > 0) {
                    this.sink.write(this.buffer, this.buffer.size);
                }
                th = null;
            } catch (Throwable th) {
                th = th;
            }
            try {
                this.sink.close();
            } catch (Throwable th2) {
                if (th == null) {
                    th = th2;
                }
            }
            this.closed = true;
            if (th != null) {
                Util.sneakyRethrow(th);
                throw null;
            }
        }
    }

    public BufferedSink emit() {
        if (!this.closed) {
            long size = this.buffer.size();
            if (size > 0) {
                this.sink.write(this.buffer, size);
            }
            return this;
        }
        throw new IllegalStateException("closed");
    }

    public BufferedSink emitCompleteSegments() {
        if (!this.closed) {
            long completeSegmentByteCount = this.buffer.completeSegmentByteCount();
            if (completeSegmentByteCount > 0) {
                this.sink.write(this.buffer, completeSegmentByteCount);
            }
            return this;
        }
        throw new IllegalStateException("closed");
    }

    public void flush() {
        if (!this.closed) {
            Buffer buffer2 = this.buffer;
            long j = buffer2.size;
            if (j > 0) {
                this.sink.write(buffer2, j);
            }
            this.sink.flush();
            return;
        }
        throw new IllegalStateException("closed");
    }

    public boolean isOpen() {
        return !this.closed;
    }

    public OutputStream outputStream() {
        return new OutputStream() {
            public void close() {
                RealBufferedSink.this.close();
            }

            public void flush() {
                RealBufferedSink realBufferedSink = RealBufferedSink.this;
                if (!realBufferedSink.closed) {
                    realBufferedSink.flush();
                }
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append(RealBufferedSink.this);
                sb.append(".outputStream()");
                return sb.toString();
            }

            public void write(int i) {
                RealBufferedSink realBufferedSink = RealBufferedSink.this;
                if (!realBufferedSink.closed) {
                    realBufferedSink.buffer.writeByte((int) (byte) i);
                    RealBufferedSink.this.emitCompleteSegments();
                    return;
                }
                throw new IOException("closed");
            }

            public void write(byte[] bArr, int i, int i2) {
                RealBufferedSink realBufferedSink = RealBufferedSink.this;
                if (!realBufferedSink.closed) {
                    realBufferedSink.buffer.write(bArr, i, i2);
                    RealBufferedSink.this.emitCompleteSegments();
                    return;
                }
                throw new IOException("closed");
            }
        };
    }

    public Timeout timeout() {
        return this.sink.timeout();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("buffer(");
        sb.append(this.sink);
        sb.append(")");
        return sb.toString();
    }

    public int write(ByteBuffer byteBuffer) {
        if (!this.closed) {
            int write = this.buffer.write(byteBuffer);
            emitCompleteSegments();
            return write;
        }
        throw new IllegalStateException("closed");
    }

    public BufferedSink write(ByteString byteString) {
        if (!this.closed) {
            this.buffer.write(byteString);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    public BufferedSink write(Source source, long j) {
        while (j > 0) {
            long read = source.read(this.buffer, j);
            if (read != -1) {
                j -= read;
                emitCompleteSegments();
            } else {
                throw new EOFException();
            }
        }
        return this;
    }

    public BufferedSink write(byte[] bArr) {
        if (!this.closed) {
            this.buffer.write(bArr);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    public BufferedSink write(byte[] bArr, int i, int i2) {
        if (!this.closed) {
            this.buffer.write(bArr, i, i2);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    public void write(Buffer buffer2, long j) {
        if (!this.closed) {
            this.buffer.write(buffer2, j);
            emitCompleteSegments();
            return;
        }
        throw new IllegalStateException("closed");
    }

    public long writeAll(Source source) {
        if (source != null) {
            long j = 0;
            while (true) {
                long read = source.read(this.buffer, 8192);
                if (read == -1) {
                    return j;
                }
                j += read;
                emitCompleteSegments();
            }
        } else {
            throw new IllegalArgumentException("source == null");
        }
    }

    public BufferedSink writeByte(int i) {
        if (!this.closed) {
            this.buffer.writeByte(i);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    public BufferedSink writeDecimalLong(long j) {
        if (!this.closed) {
            this.buffer.writeDecimalLong(j);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    public BufferedSink writeHexadecimalUnsignedLong(long j) {
        if (!this.closed) {
            this.buffer.writeHexadecimalUnsignedLong(j);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    public BufferedSink writeInt(int i) {
        if (!this.closed) {
            this.buffer.writeInt(i);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    public BufferedSink writeIntLe(int i) {
        if (!this.closed) {
            this.buffer.writeIntLe(i);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    public BufferedSink writeLong(long j) {
        if (!this.closed) {
            this.buffer.writeLong(j);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    public BufferedSink writeLongLe(long j) {
        if (!this.closed) {
            this.buffer.writeLongLe(j);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    public BufferedSink writeShort(int i) {
        if (!this.closed) {
            this.buffer.writeShort(i);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    public BufferedSink writeShortLe(int i) {
        if (!this.closed) {
            this.buffer.writeShortLe(i);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    public BufferedSink writeString(String str, int i, int i2, Charset charset) {
        if (!this.closed) {
            this.buffer.writeString(str, i, i2, charset);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    public BufferedSink writeString(String str, Charset charset) {
        if (!this.closed) {
            this.buffer.writeString(str, charset);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    public BufferedSink writeUtf8(String str) {
        if (!this.closed) {
            this.buffer.writeUtf8(str);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    public BufferedSink writeUtf8(String str, int i, int i2) {
        if (!this.closed) {
            this.buffer.writeUtf8(str, i, i2);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    public BufferedSink writeUtf8CodePoint(int i) {
        if (!this.closed) {
            this.buffer.writeUtf8CodePoint(i);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }
}
