package com.xiaomi.asr.engine.utils;

public class CircleBuffer {
    public static final int DEFAULT_SIZE = 4096;
    private boolean isFull = false;
    private boolean isWrite = false;
    private byte[] mCacheData;
    private int mCacheSize = 0;
    private int mIndex = 0;

    public CircleBuffer(int i) {
        this.mCacheData = new byte[i];
        this.mCacheSize = i;
    }

    public int capacity() {
        return this.mCacheSize;
    }

    public boolean isFull() {
        return this.isFull;
    }

    public boolean isHasData() {
        return this.isWrite;
    }

    public byte[] readBuffer() {
        if (this.isFull) {
            int i = this.mCacheSize;
            byte[] bArr = new byte[i];
            byte[] bArr2 = this.mCacheData;
            int i2 = this.mIndex;
            System.arraycopy(bArr2, i2, bArr, 0, i - i2);
            byte[] bArr3 = this.mCacheData;
            int i3 = this.mCacheSize;
            int i4 = this.mIndex;
            System.arraycopy(bArr3, 0, bArr, i3 - i4, i4);
            return bArr;
        }
        int i5 = this.mIndex;
        byte[] bArr4 = new byte[i5];
        System.arraycopy(this.mCacheData, 0, bArr4, 0, i5);
        return bArr4;
    }

    public void reset() {
        this.isFull = false;
        this.isWrite = false;
        this.mIndex = 0;
    }

    public int size() {
        return this.isFull ? this.mCacheSize : this.mIndex;
    }

    public void writeBuffer(byte[] bArr) {
        if (bArr != null && bArr.length > 0) {
            for (byte b : bArr) {
                byte[] bArr2 = this.mCacheData;
                int i = this.mIndex;
                bArr2[i] = b;
                this.mIndex = i + 1;
                if (this.mIndex >= this.mCacheSize) {
                    this.mIndex = 0;
                    this.isFull = true;
                }
            }
            this.isWrite = true;
        }
    }
}
