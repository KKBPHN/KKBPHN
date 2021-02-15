package com.xiaomi.camera.core;

import android.location.Location;
import android.text.TextUtils;
import android.util.Size;
import androidx.annotation.NonNull;
import com.android.camera.aiwatermark.data.WatermarkItem;
import com.android.camera.effect.renders.DeviceWatermarkParam;
import com.android.zxing.PreviewImage;
import java.util.List;

public class ParallelTaskDataParameter {
    /* access modifiers changed from: private */
    public boolean anchorPreview;
    /* access modifiers changed from: private */
    public boolean mAgeGenderAndMagicMirrorWater;
    /* access modifiers changed from: private */
    public String mAlgorithmName;
    /* access modifiers changed from: private */
    public boolean mBokehFrontCamera;
    /* access modifiers changed from: private */
    public int mCameraPreferredMode;
    /* access modifiers changed from: private */
    public DeviceWatermarkParam mDeviceWatermarkParam;
    /* access modifiers changed from: private */
    public float[] mDocumentPoints;
    /* access modifiers changed from: private */
    public PreviewImage mDocumentPreview;
    /* access modifiers changed from: private */
    public List mFaceWaterMarkList;
    /* access modifiers changed from: private */
    public int mFilterId;
    /* access modifiers changed from: private */
    public boolean mHasDualWaterMark;
    /* access modifiers changed from: private */
    public boolean mIsDocumentShot;
    /* access modifiers changed from: private */
    public boolean mIsFrontCamera;
    /* access modifiers changed from: private */
    public boolean mIsMiMovieOpen;
    /* access modifiers changed from: private */
    public boolean mIsMoonMode;
    /* access modifiers changed from: private */
    public boolean mIsSupportMiDualBokeh;
    /* access modifiers changed from: private */
    public boolean mIsSupportZeroDegreeOrientationImage;
    /* access modifiers changed from: private */
    public int mJpegQuality;
    /* access modifiers changed from: private */
    public int mJpegRotation;
    /* access modifiers changed from: private */
    public int mLightingPattern;
    /* access modifiers changed from: private */
    public Location mLocation;
    /* access modifiers changed from: private */
    public WatermarkItem mMajorAIWatermarkItem;
    /* access modifiers changed from: private */
    public WatermarkItem mMinorAIWatermarkItem;
    /* access modifiers changed from: private */
    public boolean mMirror;
    /* access modifiers changed from: private */
    public int mOrientation;
    private int mOutputFormat;
    private Size mOutputSize;
    /* access modifiers changed from: private */
    public PictureInfo mPictureInfo;
    private Size mPictureSize;
    /* access modifiers changed from: private */
    public int mPortraitLightingVersion;
    /* access modifiers changed from: private */
    public String mPrefix;
    private Size mPreviewSize;
    /* access modifiers changed from: private */
    public Size mRawSize;
    /* access modifiers changed from: private */
    public boolean mReprocessBurstShotPicture;
    /* access modifiers changed from: private */
    public boolean mSaveGroupshotPrimitive;
    /* access modifiers changed from: private */
    public int mSensorOrientation;
    /* access modifiers changed from: private */
    public int mShootOrientation;
    /* access modifiers changed from: private */
    public float mShootRotation;
    /* access modifiers changed from: private */
    public String mSuffix;
    /* access modifiers changed from: private */
    public String mTiltShiftMode;
    /* access modifiers changed from: private */
    public String mTimeWaterMarkString;
    /* access modifiers changed from: private */
    public boolean mVendorWaterMark;

    public final class Builder {
        private final ParallelTaskDataParameter mParameter;

        public Builder(Size size, Size size2, Size size3, int i) {
            ParallelTaskDataParameter parallelTaskDataParameter = new ParallelTaskDataParameter(size, size2, size3, i);
            this.mParameter = parallelTaskDataParameter;
        }

        public Builder(ParallelTaskDataParameter parallelTaskDataParameter) {
            this.mParameter = new ParallelTaskDataParameter();
        }

        @NonNull
        public ParallelTaskDataParameter build() {
            return this.mParameter;
        }

        public Builder setAgeGenderAndMagicMirrorWater(boolean z) {
            this.mParameter.mAgeGenderAndMagicMirrorWater = z;
            return this;
        }

        public Builder setAlgorithmName(String str) {
            this.mParameter.mAlgorithmName = str;
            return this;
        }

        public Builder setAnchorPreview(boolean z) {
            this.mParameter.anchorPreview = z;
            return this;
        }

        public Builder setBokehFrontCamera(boolean z) {
            this.mParameter.mBokehFrontCamera = z;
            return this;
        }

        public Builder setCameraPreferredMode(int i) {
            this.mParameter.mCameraPreferredMode = i;
            return this;
        }

        public Builder setDeviceWatermarkParam(DeviceWatermarkParam deviceWatermarkParam) {
            this.mParameter.mDeviceWatermarkParam = deviceWatermarkParam;
            return this;
        }

        public Builder setDocumentPoints(float[] fArr) {
            this.mParameter.mDocumentPoints = fArr;
            return this;
        }

        public Builder setDocumentPreview(PreviewImage previewImage) {
            this.mParameter.mDocumentPreview = previewImage;
            return this;
        }

        public Builder setDocumentShot(boolean z) {
            this.mParameter.mIsDocumentShot = z;
            return this;
        }

        public Builder setFaceWaterMarkList(List list) {
            this.mParameter.mFaceWaterMarkList = list;
            return this;
        }

        public Builder setFilterId(int i) {
            this.mParameter.mFilterId = i;
            return this;
        }

        public Builder setFrontCamera(boolean z) {
            this.mParameter.mIsFrontCamera = z;
            return this;
        }

        public Builder setHasDualWaterMark(boolean z) {
            this.mParameter.mHasDualWaterMark = z;
            return this;
        }

        public Builder setIsSupportMiDualBokeh(boolean z) {
            this.mParameter.mIsSupportMiDualBokeh = z;
            return this;
        }

        public Builder setJpegQuality(int i) {
            this.mParameter.mJpegQuality = i;
            return this;
        }

        public Builder setJpegRotation(int i) {
            this.mParameter.mJpegRotation = i;
            return this;
        }

        public Builder setLightingPattern(int i) {
            this.mParameter.mLightingPattern = i;
            return this;
        }

        public Builder setLocation(Location location) {
            this.mParameter.mLocation = location;
            return this;
        }

        public Builder setMajorAIWatermark(WatermarkItem watermarkItem) {
            this.mParameter.mMajorAIWatermarkItem = watermarkItem;
            return this;
        }

        public Builder setMiMovieOpen(boolean z) {
            this.mParameter.mIsMiMovieOpen = z;
            return this;
        }

        public Builder setMinorAIWatermark(WatermarkItem watermarkItem) {
            this.mParameter.mMinorAIWatermarkItem = watermarkItem;
            return this;
        }

        public Builder setMirror(boolean z) {
            this.mParameter.mMirror = z;
            return this;
        }

        public Builder setMoonMode(boolean z) {
            this.mParameter.mIsMoonMode = z;
            return this;
        }

        public Builder setOrientation(int i) {
            this.mParameter.mOrientation = i;
            return this;
        }

        public Builder setPictureInfo(PictureInfo pictureInfo) {
            this.mParameter.mPictureInfo = pictureInfo;
            return this;
        }

        public Builder setPortraitLightingVersion(int i) {
            this.mParameter.mPortraitLightingVersion = i;
            return this;
        }

        public Builder setPrefix(String str) {
            this.mParameter.mPrefix = str;
            return this;
        }

        public Builder setRawSize(int i, int i2) {
            this.mParameter.mRawSize = new Size(i, i2);
            return this;
        }

        public Builder setReprocessBurstShotPicture(boolean z) {
            this.mParameter.mReprocessBurstShotPicture = z;
            return this;
        }

        public Builder setSaveGroupshotPrimitive(boolean z) {
            this.mParameter.mSaveGroupshotPrimitive = z;
            return this;
        }

        public Builder setSensorOrientation(int i) {
            this.mParameter.mSensorOrientation = i;
            return this;
        }

        public Builder setShootOrientation(int i) {
            this.mParameter.mShootOrientation = i;
            return this;
        }

        public Builder setShootRotation(float f) {
            this.mParameter.mShootRotation = f;
            return this;
        }

        public Builder setSuffix(String str) {
            this.mParameter.mSuffix = str;
            return this;
        }

        public Builder setSupportZeroDegreeOrientationImage(boolean z) {
            this.mParameter.mIsSupportZeroDegreeOrientationImage = z;
            return this;
        }

        public Builder setTiltShiftMode(String str) {
            this.mParameter.mTiltShiftMode = str;
            return this;
        }

        public Builder setTimeWaterMarkString(String str) {
            this.mParameter.mTimeWaterMarkString = str;
            return this;
        }

        public Builder setVendorWaterMark(boolean z) {
            this.mParameter.mVendorWaterMark = z;
            return this;
        }
    }

    private ParallelTaskDataParameter(Size size, Size size2, Size size3, int i) {
        this.mSensorOrientation = 90;
        this.mPreviewSize = size;
        this.mPictureSize = size2;
        this.mOutputSize = size3;
        this.mOutputFormat = i;
    }

    private ParallelTaskDataParameter(ParallelTaskDataParameter parallelTaskDataParameter) {
        this.mSensorOrientation = 90;
        this.mHasDualWaterMark = parallelTaskDataParameter.mHasDualWaterMark;
        this.mMirror = parallelTaskDataParameter.mMirror;
        this.mLightingPattern = parallelTaskDataParameter.mLightingPattern;
        Size size = parallelTaskDataParameter.mPreviewSize;
        if (size != null) {
            this.mPreviewSize = new Size(size.getWidth(), parallelTaskDataParameter.mPreviewSize.getHeight());
        }
        Size size2 = parallelTaskDataParameter.mPictureSize;
        if (size2 != null) {
            this.mPictureSize = new Size(size2.getWidth(), parallelTaskDataParameter.mPictureSize.getHeight());
        }
        Size size3 = parallelTaskDataParameter.mRawSize;
        if (size3 != null) {
            this.mRawSize = new Size(size3.getWidth(), parallelTaskDataParameter.mRawSize.getHeight());
        }
        this.mFilterId = parallelTaskDataParameter.mFilterId;
        this.mOrientation = parallelTaskDataParameter.mOrientation;
        this.mJpegRotation = parallelTaskDataParameter.mJpegRotation;
        this.mShootRotation = parallelTaskDataParameter.mShootRotation;
        this.mShootOrientation = parallelTaskDataParameter.mShootOrientation;
        this.mIsSupportZeroDegreeOrientationImage = parallelTaskDataParameter.mIsSupportZeroDegreeOrientationImage;
        Location location = parallelTaskDataParameter.mLocation;
        if (location != null) {
            this.mLocation = new Location(location);
        }
        this.mTimeWaterMarkString = parallelTaskDataParameter.mTimeWaterMarkString;
        this.mFaceWaterMarkList = parallelTaskDataParameter.mFaceWaterMarkList;
        this.mAgeGenderAndMagicMirrorWater = parallelTaskDataParameter.mAgeGenderAndMagicMirrorWater;
        this.mIsFrontCamera = parallelTaskDataParameter.mIsFrontCamera;
        Size size4 = parallelTaskDataParameter.mOutputSize;
        if (size4 != null) {
            this.mOutputSize = new Size(size4.getWidth(), parallelTaskDataParameter.mOutputSize.getHeight());
        }
        this.mOutputFormat = parallelTaskDataParameter.mOutputFormat;
        this.mBokehFrontCamera = parallelTaskDataParameter.mBokehFrontCamera;
        this.mAlgorithmName = parallelTaskDataParameter.mAlgorithmName;
        this.mPictureInfo = parallelTaskDataParameter.mPictureInfo;
        this.mSuffix = parallelTaskDataParameter.mSuffix;
        this.mTiltShiftMode = parallelTaskDataParameter.mTiltShiftMode;
        this.mDeviceWatermarkParam = parallelTaskDataParameter.mDeviceWatermarkParam;
        this.mJpegQuality = parallelTaskDataParameter.mJpegQuality;
        this.mIsDocumentShot = parallelTaskDataParameter.mIsDocumentShot;
        this.mDocumentPreview = parallelTaskDataParameter.mDocumentPreview;
        this.mDocumentPoints = parallelTaskDataParameter.mDocumentPoints;
        this.mSensorOrientation = parallelTaskDataParameter.mSensorOrientation;
        this.anchorPreview = parallelTaskDataParameter.anchorPreview;
    }

    public String getAlgorithmName() {
        return this.mAlgorithmName;
    }

    public int getCameraPreferredMode() {
        return this.mCameraPreferredMode;
    }

    public DeviceWatermarkParam getDeviceWatermarkParam() {
        return this.mDeviceWatermarkParam;
    }

    public float[] getDocumentPoints() {
        return this.mDocumentPoints;
    }

    public PreviewImage getDocumentPreview() {
        return this.mDocumentPreview;
    }

    public List getFaceWaterMarkList() {
        return this.mFaceWaterMarkList;
    }

    public int getFilterId() {
        return this.mFilterId;
    }

    public int getJpegQuality() {
        return this.mJpegQuality;
    }

    public int getJpegRotation() {
        return this.mJpegRotation;
    }

    public int getLightingPattern() {
        return this.mLightingPattern;
    }

    public Location getLocation() {
        return this.mLocation;
    }

    public WatermarkItem getMajorAIWatermark() {
        return this.mMajorAIWatermarkItem;
    }

    public WatermarkItem getMinorAIWatermark() {
        return this.mMinorAIWatermarkItem;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getOutputFormat() {
        return this.mOutputFormat;
    }

    public Size getOutputSize() {
        return this.mOutputSize;
    }

    public PictureInfo getPictureInfo() {
        return this.mPictureInfo;
    }

    public Size getPictureSize() {
        return this.mPictureSize;
    }

    public int getPortraitLightingVersion() {
        return this.mPortraitLightingVersion;
    }

    public String getPrefix() {
        return this.mPrefix;
    }

    public Size getPreviewSize() {
        return this.mPreviewSize;
    }

    public Size getRawSize() {
        return this.mRawSize;
    }

    public int getSensorOrientation() {
        return this.mSensorOrientation;
    }

    public int getShootOrientation() {
        return this.mShootOrientation;
    }

    public float getShootRotation() {
        return this.mShootRotation;
    }

    public String getSuffix() {
        return this.mSuffix;
    }

    public boolean getSupportZeroDegreeOrientationImage() {
        return this.mIsSupportZeroDegreeOrientationImage;
    }

    public String getTiltShiftMode() {
        return this.mTiltShiftMode;
    }

    public String getTimeWaterMarkString() {
        return this.mTimeWaterMarkString;
    }

    public boolean getVendorWaterMark() {
        return this.mVendorWaterMark;
    }

    public boolean isAgeGenderAndMagicMirrorWater() {
        return this.mAgeGenderAndMagicMirrorWater;
    }

    public boolean isAnchorPreview() {
        return this.anchorPreview;
    }

    public boolean isBokehFrontCamera() {
        return this.mBokehFrontCamera;
    }

    public boolean isCinematicAspectRatio() {
        DeviceWatermarkParam deviceWatermarkParam = this.mDeviceWatermarkParam;
        return deviceWatermarkParam != null && deviceWatermarkParam.isCinematicAspectRatio();
    }

    public boolean isDocumentShot() {
        return this.mIsDocumentShot;
    }

    public boolean isFrontCamera() {
        return this.mIsFrontCamera;
    }

    public boolean isHasDualWaterMark() {
        return this.mHasDualWaterMark;
    }

    public boolean isHasFrontWaterMark() {
        DeviceWatermarkParam deviceWatermarkParam = this.mDeviceWatermarkParam;
        return deviceWatermarkParam != null && deviceWatermarkParam.isFrontWatermarkEnable();
    }

    public boolean isHasWaterMark() {
        return isHasDualWaterMark() || isHasFrontWaterMark() || !TextUtils.isEmpty(this.mTimeWaterMarkString);
    }

    public boolean isMiMovieOpen() {
        return this.mIsMiMovieOpen;
    }

    public boolean isMirror() {
        return this.mMirror;
    }

    public boolean isMoonMode() {
        return this.mIsMoonMode;
    }

    public boolean isSaveGroupshotPrimitive() {
        return this.mSaveGroupshotPrimitive;
    }

    public boolean isSupportMiDualBokeh() {
        return this.mIsSupportMiDualBokeh;
    }

    public boolean isUltraPixelWaterMark() {
        DeviceWatermarkParam deviceWatermarkParam = this.mDeviceWatermarkParam;
        return deviceWatermarkParam != null && deviceWatermarkParam.isUltraWatermarkEnable();
    }

    public boolean shouldReprocessBurstShotPicture() {
        return this.mReprocessBurstShotPicture;
    }

    public void updateOutputSize(int i, int i2) {
        this.mOutputSize = new Size(i, i2);
    }
}
