package com.android.gallery3d.exif;

import com.android.gallery3d.exif.ExifInterface.ColorSpace;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

class CountedDataInputStream extends FilterInputStream {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final byte[] mByteArray = new byte[8];
    private final ByteBuffer mByteBuffer = ByteBuffer.wrap(this.mByteArray);
    private int mCount = 0;

    protected CountedDataInputStream(InputStream inputStream) {
        super(inputStream);
    }

    public ByteOrder getByteOrder() {
        return this.mByteBuffer.order();
    }

    public int getReadByteCount() {
        return this.mCount;
    }

    public int read() {
        int read = this.in.read();
        this.mCount += read >= 0 ? 1 : 0;
        return read;
    }

    public int read(byte[] bArr) {
        int read = this.in.read(bArr);
        this.mCount += read >= 0 ? read : 0;
        return read;
    }

    public int read(byte[] bArr, int i, int i2) {
        int read = this.in.read(bArr, i, i2);
        this.mCount += read >= 0 ? read : 0;
        return read;
    }

    public int readInt() {
        readOrThrow(this.mByteArray, 0, 4);
        this.mByteBuffer.rewind();
        return this.mByteBuffer.getInt();
    }

    public long readLong() {
        readOrThrow(this.mByteArray, 0, 8);
        this.mByteBuffer.rewind();
        return this.mByteBuffer.getLong();
    }

    public void readOrThrow(byte[] bArr) {
        readOrThrow(bArr, 0, bArr.length);
    }

    public void readOrThrow(byte[] bArr, int i, int i2) {
        if (read(bArr, i, i2) != i2) {
            throw new EOFException();
        }
    }

    public short readShort() {
        readOrThrow(this.mByteArray, 0, 2);
        this.mByteBuffer.rewind();
        return this.mByteBuffer.getShort();
    }

    public String readString(int i) {
        byte[] bArr = new byte[i];
        readOrThrow(bArr);
        return new String(bArr, "UTF8");
    }

    public String readString(int i, Charset charset) {
        byte[] bArr = new byte[i];
        readOrThrow(bArr);
        return new String(bArr, charset);
    }

    public long readUnsignedInt() {
        return ((long) readInt()) & 4294967295L;
    }

    public int readUnsignedShort() {
        return readShort() & ColorSpace.UNCALIBRATED;
    }

    public void setByteOrder(ByteOrder byteOrder) {
        this.mByteBuffer.order(byteOrder);
    }

    public long skip(long j) {
        long skip = this.in.skip(j);
        this.mCount = (int) (((long) this.mCount) + skip);
        return skip;
    }

    public void skipOrThrow(long j) {
        if (skip(j) != j) {
            throw new EOFException();
        }
    }

    public void skipTo(long j) {
        skipOrThrow(j - ((long) this.mCount));
    }
}
