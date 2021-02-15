package com.android.zxing;

import android.media.Image;
import com.android.camera.log.Log;
import com.xiaomi.stat.d;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class PreviewImage {
    public static final int PREVIEW_STATUS_NORMAL = 2;
    public static final int PREVIEW_STATUS_START = 1;
    public static final int PREVIEW_STATUS_STOP = 3;
    public static final int PREVIEW_STATUS_UNKNOWN = 0;
    private static final String TAG = "PreviewImage";
    private int mCameraId;
    private byte[] mData;
    private int mFormat;
    private int mHeight;
    private int mOrientation;
    private int mPreviewStatus = 0;
    private long mTimestamp;
    private int mWidth;

    public PreviewImage(int i, int i2) {
        this.mPreviewStatus = i;
        this.mCameraId = i2;
    }

    public PreviewImage(Image image, int i) {
        long currentTimeMillis = System.currentTimeMillis();
        convertYUV420ToNV21(image);
        this.mOrientation = i;
        this.mPreviewStatus = 2;
        StringBuilder sb = new StringBuilder();
        sb.append("PreviewDecodeManager convertYUV420ToNV21: cost = ");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        sb.append(d.H);
        Log.d(TAG, sb.toString());
    }

    private void convertYUV420ToNV21(Image image) {
        int i;
        byte[] bArr;
        if (image != null) {
            try {
                if (image.getFormat() == 35) {
                    this.mTimestamp = image.getTimestamp();
                    this.mWidth = image.getWidth();
                    this.mHeight = image.getHeight();
                    this.mFormat = image.getFormat();
                    int i2 = 0;
                    int rowStride = image.getPlanes()[0].getRowStride();
                    int rowStride2 = image.getPlanes()[2].getRowStride();
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    ByteBuffer buffer2 = image.getPlanes()[2].getBuffer();
                    int limit = buffer.limit();
                    int limit2 = buffer2.limit();
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("convertYUV420888ToNV21: size = ");
                    sb.append(this.mWidth);
                    sb.append("x");
                    sb.append(this.mHeight);
                    sb.append(", yStride = ");
                    sb.append(rowStride);
                    sb.append(", uvStride = ");
                    sb.append(rowStride2);
                    Log.v(str, sb.toString());
                    this.mData = new byte[Math.max(((this.mWidth * this.mHeight) * 3) / 2, limit + limit2)];
                    buffer.get(this.mData, 0, limit);
                    buffer2.get(this.mData, limit, limit2);
                    byte[] bArr2 = new byte[(((this.mWidth * this.mHeight) * 3) / 2)];
                    int i3 = 0;
                    int i4 = 0;
                    int i5 = 0;
                    while (i3 < this.mHeight) {
                        System.arraycopy(this.mData, i4, bArr2, i5, this.mWidth);
                        i4 = i3 == this.mHeight + -1 ? i4 + this.mWidth : i4 + rowStride;
                        i5 += this.mWidth;
                        i3++;
                    }
                    while (true) {
                        int i6 = this.mHeight;
                        if (i2 >= i6 / 2) {
                            break;
                        }
                        if (i2 == (i6 / 2) - 1) {
                            bArr = this.mData;
                            i = this.mWidth - 1;
                        } else {
                            bArr = this.mData;
                            i = this.mWidth;
                        }
                        System.arraycopy(bArr, i4, bArr2, i5, i);
                        i4 += rowStride2;
                        i5 += this.mWidth;
                        i2++;
                    }
                    this.mData = bArr2;
                }
            } catch (Exception unused) {
            }
        }
    }

    public int getCameraId() {
        return this.mCameraId;
    }

    public byte[] getData() {
        return this.mData;
    }

    public int getFormat() {
        return this.mFormat;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getPreviewStatus() {
        return this.mPreviewStatus;
    }

    public long getTimestamp() {
        return this.mTimestamp;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PreviewImage{mData=");
        sb.append(Arrays.toString(this.mData));
        sb.append(", mTimestamp=");
        sb.append(this.mTimestamp);
        sb.append(", mWidth=");
        sb.append(this.mWidth);
        sb.append(", mHeight=");
        sb.append(this.mHeight);
        sb.append(", mFormat=");
        sb.append(this.mFormat);
        sb.append('}');
        return sb.toString();
    }
}
