package com.android.camera.effect.renders;

import android.graphics.Bitmap;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.gallery3d.ui.BasicTexture;
import com.android.gallery3d.ui.BitmapTexture;
import java.util.Arrays;

public class ImageWaterMark extends WaterMark {
    private static final String TAG = "ImageWaterMark";
    private int mCenterX;
    private int mCenterY;
    private int mHeight;
    private BitmapTexture mImageTexture;
    private boolean mIsCinematicAspectRatio;
    private int mPaddingX;
    private int mPaddingY;
    private int mWidth;

    public ImageWaterMark(Bitmap bitmap, int i, int i2, int i3, float f, float f2, float f3, boolean z) {
        super(i, i2, i3);
        this.mIsCinematicAspectRatio = z;
        int[] calcDualCameraWatermarkLocationByCinema = this.mIsCinematicAspectRatio ? Util.calcDualCameraWatermarkLocationByCinema(i, i2, bitmap.getWidth(), bitmap.getHeight(), i3) : Util.calcDualCameraWatermarkLocation(i, i2, bitmap.getWidth(), bitmap.getHeight());
        StringBuilder sb = new StringBuilder();
        sb.append("ImageWaterMark: ");
        sb.append(Arrays.toString(calcDualCameraWatermarkLocationByCinema));
        Log.d(TAG, sb.toString());
        this.mWidth = calcDualCameraWatermarkLocationByCinema[0];
        this.mHeight = calcDualCameraWatermarkLocationByCinema[1];
        this.mPaddingX = calcDualCameraWatermarkLocationByCinema[2];
        this.mPaddingY = calcDualCameraWatermarkLocationByCinema[3];
        this.mImageTexture = new BitmapTexture(bitmap);
        this.mImageTexture.setOpaque(false);
        calcCenterAxis();
        if (Util.sIsDumpLog) {
            print();
        }
    }

    private void calcCenterAxis() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6 = this.mOrientation;
        if (i6 == 0) {
            this.mCenterX = this.mPaddingX + (getWidth() / 2);
            i3 = this.mPictureHeight - this.mPaddingY;
            i2 = getHeight();
        } else if (i6 != 90) {
            if (i6 == 180) {
                this.mCenterX = (this.mPictureWidth - this.mPaddingX) - (getWidth() / 2);
                i5 = this.mPaddingY;
                i4 = getHeight();
            } else if (i6 == 270) {
                this.mCenterX = this.mPaddingY + (getHeight() / 2);
                i5 = this.mPaddingX;
                i4 = getWidth();
            } else {
                return;
            }
            i = i5 + (i4 / 2);
            this.mCenterY = i;
        } else {
            this.mCenterX = (this.mPictureWidth - this.mPaddingY) - (getHeight() / 2);
            i3 = this.mPictureHeight - this.mPaddingX;
            i2 = getWidth();
        }
        i = i3 - (i2 / 2);
        this.mCenterY = i;
    }

    private void print() {
        StringBuilder sb = new StringBuilder();
        sb.append("WaterMark pictureWidth=");
        sb.append(this.mPictureWidth);
        sb.append(" pictureHeight=");
        sb.append(this.mPictureHeight);
        sb.append(" centerX=");
        sb.append(this.mCenterX);
        sb.append(" centerY=");
        sb.append(this.mCenterY);
        sb.append(" waterWidth=");
        sb.append(this.mWidth);
        sb.append(" waterHeight=");
        sb.append(this.mHeight);
        sb.append(" paddingX=");
        sb.append(this.mPaddingX);
        sb.append(" paddingY=");
        sb.append(this.mPaddingY);
        Log.v(TAG, sb.toString());
    }

    public int getCenterX() {
        return this.mCenterX;
    }

    public int getCenterY() {
        return this.mCenterY;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public /* bridge */ /* synthetic */ int getLeft() {
        return super.getLeft();
    }

    public int getPaddingX() {
        return this.mPaddingX;
    }

    public int getPaddingY() {
        return this.mPaddingY;
    }

    public BasicTexture getTexture() {
        return this.mImageTexture;
    }

    public /* bridge */ /* synthetic */ int getTop() {
        return super.getTop();
    }

    public int getWidth() {
        return this.mWidth;
    }
}
