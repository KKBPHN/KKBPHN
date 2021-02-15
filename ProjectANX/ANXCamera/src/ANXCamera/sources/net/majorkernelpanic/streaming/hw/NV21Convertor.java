package net.majorkernelpanic.streaming.hw;

import java.nio.ByteBuffer;

public class NV21Convertor {
    private byte[] mBuffer;
    ByteBuffer mCopy;
    private int mHeight;
    private boolean mPanesReversed = false;
    private boolean mPlanar;
    private int mSize;
    private int mSliceHeight;
    private int mStride;
    private int mWidth;
    private int mYPadding;

    public void convert(byte[] bArr, ByteBuffer byteBuffer) {
        byteBuffer.put(convert(bArr), 0, byteBuffer.capacity() < bArr.length ? byteBuffer.capacity() : bArr.length);
    }

    public byte[] convert(byte[] bArr) {
        byte[] bArr2 = this.mBuffer;
        if (bArr2 == null || bArr2.length != (((this.mSliceHeight * 3) * this.mStride) / 2) + this.mYPadding) {
            this.mBuffer = new byte[((((this.mSliceHeight * 3) * this.mStride) / 2) + this.mYPadding)];
        }
        if (!this.mPlanar) {
            if (this.mSliceHeight == this.mHeight && this.mStride == this.mWidth) {
                if (!this.mPanesReversed) {
                    int i = this.mSize;
                    while (true) {
                        int i2 = this.mSize;
                        if (i >= i2 + (i2 / 2)) {
                            break;
                        }
                        byte[] bArr3 = this.mBuffer;
                        int i3 = i + 1;
                        bArr3[0] = bArr[i3];
                        bArr[i3] = bArr[i];
                        bArr[i] = bArr3[0];
                        i += 2;
                    }
                }
                if (this.mYPadding <= 0) {
                    return bArr;
                }
                System.arraycopy(bArr, 0, this.mBuffer, 0, this.mSize);
                int i4 = this.mSize;
                System.arraycopy(bArr, i4, this.mBuffer, this.mYPadding + i4, i4 / 2);
            }
            return bArr;
        }
        if (this.mSliceHeight == this.mHeight && this.mStride == this.mWidth) {
            if (this.mPanesReversed) {
                int i5 = 0;
                while (true) {
                    int i6 = this.mSize;
                    if (i5 >= i6 / 4) {
                        break;
                    }
                    byte[] bArr4 = this.mBuffer;
                    int i7 = i5 * 2;
                    bArr4[i5] = bArr[i6 + i7];
                    bArr4[(i6 / 4) + i5] = bArr[i6 + i7 + 1];
                    i5++;
                }
            } else {
                int i8 = 0;
                while (true) {
                    int i9 = this.mSize;
                    if (i8 >= i9 / 4) {
                        break;
                    }
                    byte[] bArr5 = this.mBuffer;
                    int i10 = i8 * 2;
                    bArr5[i8] = bArr[i9 + i10 + 1];
                    bArr5[(i9 / 4) + i8] = bArr[i9 + i10];
                    i8++;
                }
            }
            if (this.mYPadding == 0) {
                byte[] bArr6 = this.mBuffer;
                int i11 = this.mSize;
                System.arraycopy(bArr6, 0, bArr, i11, i11 / 2);
                return bArr;
            }
            System.arraycopy(bArr, 0, this.mBuffer, 0, this.mSize);
            byte[] bArr7 = this.mBuffer;
            int i12 = this.mSize;
            System.arraycopy(bArr7, 0, bArr7, this.mYPadding + i12, i12 / 2);
        }
        return bArr;
        return this.mBuffer;
    }

    public int getBufferSize() {
        return (this.mSize * 3) / 2;
    }

    public boolean getPlanar() {
        return this.mPlanar;
    }

    public int getSliceHeigth() {
        return this.mSliceHeight;
    }

    public int getStride() {
        return this.mStride;
    }

    public boolean getUVPanesReversed() {
        return this.mPanesReversed;
    }

    public int getYPadding() {
        return this.mYPadding;
    }

    public void setColorPanesReversed(boolean z) {
        this.mPanesReversed = z;
    }

    public void setEncoderColorFormat(int i) {
        boolean z;
        if (!(i == 39 || i == 2130706688)) {
            switch (i) {
                case 19:
                case 20:
                    z = true;
                    break;
                case 21:
                    break;
                default:
                    return;
            }
        }
        z = false;
        setPlanar(z);
    }

    public void setPlanar(boolean z) {
        this.mPlanar = z;
    }

    public void setSize(int i, int i2) {
        this.mHeight = i2;
        this.mWidth = i;
        this.mSliceHeight = i2;
        this.mStride = i;
        this.mSize = this.mWidth * this.mHeight;
    }

    public void setSliceHeigth(int i) {
        this.mSliceHeight = i;
    }

    public void setStride(int i) {
        this.mStride = i;
    }

    public void setYPadding(int i) {
        this.mYPadding = i;
    }
}
