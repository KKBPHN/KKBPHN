package com.android.camera.effect.renders;

import android.graphics.Bitmap;
import com.android.camera.Util;
import com.android.gallery3d.ui.BasicTexture;
import com.android.gallery3d.ui.BitmapTexture;

class MimojiImageWaterMark extends WaterMark {
    private int mCenterX;
    private int mCenterY;
    private int mHeight;
    private BitmapTexture mImageTexture;
    private int mPaddingX;
    private int mPaddingY;
    private int mWidth;

    public MimojiImageWaterMark(Bitmap bitmap, int i, int i2, int i3, float f, float f2, float f3) {
        super(i, i2, i3);
        int[] calcDualCameraWatermarkLocation = Util.calcDualCameraWatermarkLocation(i, i2, bitmap.getWidth(), bitmap.getHeight());
        this.mWidth = calcDualCameraWatermarkLocation[0];
        this.mHeight = calcDualCameraWatermarkLocation[1];
        this.mPaddingX = calcDualCameraWatermarkLocation[2];
        this.mPaddingY = calcDualCameraWatermarkLocation[3];
        this.mImageTexture = new BitmapTexture(bitmap);
        this.mImageTexture.setOpaque(false);
        calcCenterAxis();
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

    public int getCenterX() {
        return this.mCenterX;
    }

    public int getCenterY() {
        return this.mCenterY;
    }

    public int getHeight() {
        return this.mHeight;
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

    public int getWidth() {
        return this.mWidth;
    }
}
