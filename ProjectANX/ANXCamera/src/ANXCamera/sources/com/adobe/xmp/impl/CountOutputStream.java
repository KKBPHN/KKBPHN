package com.adobe.xmp.impl;

import java.io.OutputStream;

public final class CountOutputStream extends OutputStream {
    private int bytesWritten = 0;
    private final OutputStream out;

    CountOutputStream(OutputStream outputStream) {
        this.out = outputStream;
    }

    public int getBytesWritten() {
        return this.bytesWritten;
    }

    public void write(int i) {
        this.out.write(i);
        this.bytesWritten++;
    }

    public void write(byte[] bArr) {
        this.out.write(bArr);
        this.bytesWritten += bArr.length;
    }

    public void write(byte[] bArr, int i, int i2) {
        this.out.write(bArr, i, i2);
        this.bytesWritten += i2;
    }
}
