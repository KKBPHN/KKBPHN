package com.android.camera.effect.draw_mode;

import android.media.Image;
import android.util.Size;
import com.android.camera.aiwatermark.data.WatermarkItem;
import com.android.camera.effect.EffectController.EffectRectAttribute;
import com.android.camera.effect.MiYuvImage;
import com.android.camera.effect.renders.DeviceWatermarkParam;
import java.util.List;

public class DrawYuvAttribute extends DrawAttribute {
    public boolean mApplyWaterMark;
    public EffectRectAttribute mAttribute;
    public int mBlockHeight;
    public int mBlockWidth;
    public int[] mCoordinatesOfTheRegionUnderWatermarks;
    public byte[] mDataOfTheRegionUnderWatermarks;
    public long mDate;
    public DeviceWatermarkParam mDeviceWatermarkParam;
    public int mEffectIndex;
    public boolean mHasDualWaterMark;
    public boolean mHasFrontWaterMark;
    public Image mImage;
    public boolean mIsHeif;
    public int mJpegQuality;
    public int mJpegRotation;
    public WatermarkItem mMajorAIWatermarkItem;
    public WatermarkItem mMinorAIWatermarkItem;
    public boolean mMirror;
    public int mOffsetUV;
    public int mOffsetY;
    public int mOrientation;
    public Size mOriginalSize;
    public Size mOutputSize;
    public Size mPictureSize;
    public Size mPreviewSize;
    public float mShootRotation;
    public String mTiltShiftMode;
    public String mTimeWatermark;
    public float[] mTransform;
    public List mWaterInfos;
    public int mX;
    public int mY;
    public MiYuvImage mYuvImage;

    public DrawYuvAttribute() {
    }

    public DrawYuvAttribute(Image image, Size size, Size size2, int i, int i2, int i3, float f, long j, boolean z, boolean z2, boolean z3, String str, String str2, boolean z4, boolean z5, DeviceWatermarkParam deviceWatermarkParam, EffectRectAttribute effectRectAttribute, List list, WatermarkItem watermarkItem, WatermarkItem watermarkItem2) {
        this.mImage = image;
        this.mPreviewSize = size;
        this.mPictureSize = size2;
        this.mEffectIndex = i;
        this.mOrientation = i2;
        this.mJpegRotation = i3;
        this.mShootRotation = f;
        this.mDate = j;
        this.mMirror = z;
        this.mApplyWaterMark = z2;
        this.mIsHeif = z3;
        this.mTiltShiftMode = str;
        this.mTimeWatermark = str2;
        this.mHasDualWaterMark = z4;
        this.mHasFrontWaterMark = z5;
        this.mDeviceWatermarkParam = deviceWatermarkParam;
        this.mAttribute = effectRectAttribute;
        this.mWaterInfos = list;
        this.mDataOfTheRegionUnderWatermarks = null;
        this.mCoordinatesOfTheRegionUnderWatermarks = null;
        this.mJpegQuality = 97;
        this.mMajorAIWatermarkItem = watermarkItem;
        this.mMinorAIWatermarkItem = watermarkItem2;
    }

    public DrawYuvAttribute init(MiYuvImage miYuvImage, Size size) {
        this.mYuvImage = miYuvImage;
        this.mPreviewSize = size;
        this.mPictureSize = size;
        return this;
    }

    public boolean isOutputSquare() {
        Size size = this.mOutputSize;
        return size != null && size.getWidth() == this.mOutputSize.getHeight();
    }

    public void updatePosition(int i, int i2) {
        this.mX = i;
        this.mY = i2;
    }

    public void updateZoom(float[] fArr) {
        this.mTransform = fArr;
    }
}
