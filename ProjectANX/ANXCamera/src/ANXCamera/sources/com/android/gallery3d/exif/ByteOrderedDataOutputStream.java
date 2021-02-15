package com.android.gallery3d.exif;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.nio.ByteOrder;

class ByteOrderedDataOutputStream extends FilterOutputStream {
    private ByteOrder mByteOrder;
    private final OutputStream mOutputStream;

    public ByteOrderedDataOutputStream(OutputStream outputStream, ByteOrder byteOrder) {
        super(outputStream);
        this.mOutputStream = outputStream;
        this.mByteOrder = byteOrder;
    }

    public void setByteOrder(ByteOrder byteOrder) {
        this.mByteOrder = byteOrder;
    }

    public void write(byte[] bArr) {
        this.mOutputStream.write(bArr);
    }

    public void write(byte[] bArr, int i, int i2) {
        this.mOutputStream.write(bArr, i, i2);
    }

    public void writeByte(int i) {
        this.mOutputStream.write(i);
    }

    public void writeInt(int i) {
        OutputStream outputStream;
        int i2;
        ByteOrder byteOrder = this.mByteOrder;
        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            this.mOutputStream.write((i >>> 0) & 255);
            this.mOutputStream.write((i >>> 8) & 255);
            this.mOutputStream.write((i >>> 16) & 255);
            outputStream = this.mOutputStream;
            i2 = i >>> 24;
        } else if (byteOrder == ByteOrder.BIG_ENDIAN) {
            this.mOutputStream.write((i >>> 24) & 255);
            this.mOutputStream.write((i >>> 16) & 255);
            this.mOutputStream.write((i >>> 8) & 255);
            outputStream = this.mOutputStream;
            i2 = i >>> 0;
        } else {
            return;
        }
        outputStream.write(i2 & 255);
    }

    public void writeShort(short s) {
        OutputStream outputStream;
        int i;
        ByteOrder byteOrder = this.mByteOrder;
        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            this.mOutputStream.write((s >>> 0) & 255);
            outputStream = this.mOutputStream;
            i = s >>> 8;
        } else if (byteOrder == ByteOrder.BIG_ENDIAN) {
            this.mOutputStream.write((s >>> 8) & 255);
            outputStream = this.mOutputStream;
            i = s >>> 0;
        } else {
            return;
        }
        outputStream.write(i & 255);
    }

    public void writeUnsignedInt(long j) {
        writeInt((int) j);
    }

    public void writeUnsignedShort(int i) {
        writeShort((short) i);
    }
}
