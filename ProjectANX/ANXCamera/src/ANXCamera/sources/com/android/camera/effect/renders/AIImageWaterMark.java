package com.android.camera.effect.renders;

import android.graphics.Bitmap;
import com.android.gallery3d.ui.BasicTexture;
import com.android.gallery3d.ui.BitmapTexture;

public class AIImageWaterMark extends WaterMark {
    private int mCenterX = 0;
    private int mCenterY = 0;
    private int mHeight = 0;
    private BitmapTexture mImageTexture;
    private int mPaddingX = 0;
    private int mPaddingY = 0;
    private int[] mRange = null;
    private int mWidth = 0;

    public AIImageWaterMark(Bitmap bitmap, int i, int i2, int i3, int[] iArr, float f) {
        super(i, i2, i3);
        this.mRange = iArr;
        this.mWidth = ((int) Math.ceil((double) (((float) bitmap.getWidth()) * f))) & -2;
        this.mHeight = ((int) Math.ceil((double) (((float) bitmap.getHeight()) * f))) & -2;
        this.mImageTexture = new BitmapTexture(bitmap);
        this.mImageTexture.setOpaque(false);
        calcCenterAxis();
    }

    private void calcCenterAxis() {
        int[] iArr = this.mRange;
        this.mCenterX = iArr[0] + (iArr[2] / 2);
        this.mCenterY = iArr[1] + (iArr[3] / 2);
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
