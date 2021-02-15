package com.android.camera2;

import android.hardware.camera2.params.MeteringRectangle;
import android.location.Location;
import android.util.Range;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.Util;
import com.android.camera.aiwatermark.util.WatermarkConstant;
import com.android.camera.fragment.beauty.BeautyValues;
import com.android.camera.log.Log;
import com.android.camera.storage.Storage;
import com.android.camera2.Camera2Proxy.HDRStatus;
import com.android.camera2.vendortag.struct.MarshalQueryableASDScene;
import com.android.camera2.vendortag.struct.MarshalQueryableDxoAsdScene.ASDScene;
import com.xiaomi.camera.imagecodec.FeatureSetting;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CameraConfigs {
    private static final int MAX_JPEG_COMPRESSION_QUALITY = 100;
    private static final int MIN_JPEG_COMPRESSION_QUALITY = 1;
    private static final String TAG = "CameraConfigs";
    private int flashCurrent;
    private int hdrCheckerAdrc;
    private int[] hdrCheckerEvValue;
    private int hdrCheckerSceneType;
    private int hdrMode;
    private boolean isFaceExist;
    private boolean isFlashAutoDetectionEnabled = true;
    private boolean isSuperNightEnabled;
    private boolean isUltraPixelPortraitEnabled;
    private boolean mAELocked;
    private MeteringRectangle[] mAERegions;
    private MeteringRectangle[] mAFRegions;
    private boolean mASDEnable;
    private int mASDScene = -2;
    private boolean mAWBLocked;
    private int mActivityHashCode;
    private boolean mAiAIIEPreviewEnabled;
    private boolean mAiASDEnabled;
    private boolean mAiMoonEffectEnabled;
    private int mAiSceneDetectPeriod;
    private boolean mAiShutterEnable;
    private boolean mAiShutterExistMotion;
    private int mAlgorithmPreviewFormat = 35;
    private CameraSize mAlgorithmPreviewSize;
    private int mAmbilightAeTarget;
    private int mAmbilightMode = 0;
    private int mAntiBanding = -1;
    private boolean mAsdDirtyEnable;
    private int mAutoZoomMode;
    private float mAutoZoomScaleOffset;
    private int mAwbCustomValue;
    private int mAwbMode = 1;
    private int mBackVideoBokehColorRetentionMode;
    private BeautyValues mBeautyValues;
    private CameraSize mBinningPhotoSize;
    private CameraSize mBokehDepthSize;
    private boolean mCameraAi30Enabled;
    private long mCaptureTime;
    private int[] mCaptureTriggerFlow;
    private boolean mCinematicPhotoEnabled;
    private boolean mCinematicVideoEnabled;
    private int mColorEffect = -1;
    private boolean mColorEnhanceEnabled;
    private int mContrastLevel = -1;
    private int mDeviceOrientation = -1;
    private boolean mDoRemosaic;
    private boolean mDodepurpleEnabled;
    private boolean mDualCamWaterMarkEnabled = false;
    private ASDScene mDxoAsdScene;
    private boolean mEISEnabled;
    private int mExposureCompensationIndex;
    private int mExposureMeteringMode = -1;
    private long mExposureTime;
    private int mExtendSceneMode;
    private int mEyeLightType = Integer.parseInt("-1");
    private String mFNumber;
    private boolean mFaceAgeAnalyzeEnabled;
    private boolean mFaceDetectionEnabled;
    private boolean mFaceScoreEnabled;
    private boolean mFaceWaterMarkEnabled = false;
    private String mFaceWaterMarkFormat = null;
    private CameraSize mFakeSatOutputSize;
    private CameraSize mFakeTelePhotoSize;
    private FeatureSetting mFeatureSetting;
    private int mFlashMode = 0;
    private boolean mFlawDetectEnable;
    private float mFocusDistance = -1.0f;
    private boolean mFocusLockEnable;
    private int mFocusMode = -1;
    private boolean mFrontMirror;
    private int mFrontVideoBokehColorRetentionMode;
    private Location mGpsLocation;
    private int mHDR10VideoMode;
    private boolean mHDRCheckerEnabled;
    private int mHDRCheckerStatus;
    private HDRStatus mHDRStatus = new HDRStatus(false, false, false);
    private boolean mHFRDeflicker;
    private boolean mHHTEnabled;
    private boolean mHistogramStatsEnabled;
    private boolean mIsHighQualityPreferredEnable;
    private boolean mIsInTimerBurstShotting;
    private boolean mIsQcfaEnabled = false;
    private boolean mIsShot2Gallery;
    private boolean mIsVideoBokehEnabled;
    private boolean mIsVideoLogEnabled;
    private int mIso;
    private int mJpegQuality = -1;
    private int mJpegRotation = -1;
    private boolean mLLSEnabled;
    private boolean mLensDirtyDetectEnabled;
    private CameraSize mLockedAlgoSize;
    private boolean mMFAfAeLock;
    private boolean mMacroMode;
    private CameraSize mMacroPhotoSize;
    private boolean mMfnrEnabled;
    private boolean mModuleAnchorFrame;
    private int[] mMtkPipDevices;
    private boolean mMultiSnapStopRequest;
    private boolean mNeedFlash;
    private boolean mNeedPausePreview = false;
    private boolean mNeedSequence;
    private boolean mNormalWideLDCEnabled;
    private boolean mOISEnabled;
    private MarshalQueryableASDScene.ASDScene[] mOnTripodScene;
    private CameraSize mOutputSize;
    private int mParallelPerformanceHDREnabled = 0;
    private int mParallelPerformanceLLSEnabled = 0;
    private int mParallelPerformanceMFNREnabled = 0;
    private int mParallelPerformanceSREnabled = 0;
    private int mParallelPerformanceSuperNightEnabled = 0;
    private int mParallelPerformanceZSLEnabled = 0;
    private int mParallelQualityHDREnabled = 0;
    private int mParallelQualityLLSEnabled = 0;
    private int mParallelQualityMFNREnabled = 0;
    private int mParallelQualitySREnabled = 0;
    private int mParallelQualitySuperNightEnabled = 0;
    private int mParallelQualityZSLEnabled = 0;
    private int mPhotoFormat = 256;
    private int mPhotoMaxImages = 2;
    private CameraSize mPhotoSize;
    private int mPortraitLightingPattern;
    private Range mPreviewFpsRange;
    private int mPreviewMaxImages = 1;
    private CameraSize mPreviewSize;
    private boolean mQuickShotAnimation;
    private CameraSize mRawSizeOfMacro;
    private CameraSize mRawSizeOfTele;
    private CameraSize mRawSizeOfTuningBuffer;
    private CameraSize mRawSizeOfUltraTele;
    private CameraSize mRawSizeOfUltraWide;
    private CameraSize mRawSizeOfWide;
    private boolean mRearBokehEnabled;
    private CameraSize mSATRemosicSize;
    private int mSaturationLevel = -1;
    private int mSceneMode = -1;
    private CameraSize mSensorRawImageSize;
    private int mSharpnessLevel = -1;
    private ArrayDeque mShotPath = null;
    private ArrayDeque mShotPathThumbnail = null;
    private int mShotType = 0;
    private boolean mSingleBokehEnabled;
    private boolean mSpecshotModeEnable;
    private CameraSize mSubPhotoSize;
    private boolean mSuperResolutionEnabled;
    private boolean mSwMfnrEnabled;
    private float mTargetZoom;
    private CameraSize mTelePhotoSize;
    private int mThermalLevel = 0;
    private CameraSize mThumbnailSize;
    private boolean mTimeWaterMarkEnabled = false;
    private String mTimeWatermarkValue = null;
    private CameraSize mTuningBufferSize;
    private byte mTuningMode;
    private CameraSize mUltraTelePhotoSize;
    private boolean mUltraWideLDCEnabled;
    private CameraSize mUltraWidePhotoSize;
    private boolean mUseLegacyFlashMode;
    private int mVideoBokehLevelBack = -1;
    private float mVideoBokehLevelFront = -1.0f;
    private boolean mVideoFilterColorRetentionBack;
    private boolean mVideoFilterColorRetentionFront;
    private int mVideoFilterId = -1;
    private Range mVideoFpsRange;
    private boolean mVideoHdr;
    private boolean mVideoLogEnable;
    private CameraSize mVideoSize;
    private CameraSize mVideoSnapshotSize;
    private List mWaterMarkAppliedList = new ArrayList();
    private CameraSize mWidePhotoSize;
    private float mZoomRatio = 1.0f;
    private boolean mZslEnabled;
    private boolean satFallbackDisable;
    private boolean satIsZooming;

    private boolean isSupportParallelLLSEnable() {
        boolean z = false;
        if (CameraSettings.isHighQualityPreferred()) {
            if (this.mParallelQualityLLSEnabled == 1 && isLLSEnabled() && !isHDREnabled()) {
                z = true;
            }
            return z;
        }
        if (this.mParallelPerformanceLLSEnabled == 1 && isMfnrEnabled() && !isHDREnabled()) {
            z = true;
        }
        return z;
    }

    private boolean isSupportParallelMFNREnable() {
        boolean z = false;
        if (CameraSettings.isHighQualityPreferred()) {
            if (this.mParallelQualityMFNREnabled == 1 && isMfnrEnabled() && !isLLSEnabled() && !isSuperNightEnabled()) {
                z = true;
            }
            return z;
        }
        if (this.mParallelPerformanceMFNREnabled == 1 && isMfnrEnabled() && !isLLSEnabled() && !isSuperNightEnabled()) {
            z = true;
        }
        return z;
    }

    private boolean isSupportParallelSREnable() {
        boolean z = false;
        if (CameraSettings.isHighQualityPreferred()) {
            if (this.mParallelQualitySREnabled == 1 && isSuperResolutionEnabled()) {
                z = true;
            }
            return z;
        }
        if (this.mParallelPerformanceSREnabled == 1 && isSuperResolutionEnabled()) {
            z = true;
        }
        return z;
    }

    private boolean isSupportParallelSuperNightEnable() {
        boolean z = false;
        if (CameraSettings.isHighQualityPreferred()) {
            if (this.mParallelQualitySuperNightEnabled == 1 && isSuperNightEnabled()) {
                z = true;
            }
            return z;
        }
        if (this.mParallelPerformanceSuperNightEnabled == 1 && isSuperNightEnabled()) {
            z = true;
        }
        return z;
    }

    private boolean isSupportParallelZSLEnable() {
        boolean z = false;
        if (CameraSettings.isHighQualityPreferred()) {
            if (this.mParallelQualityZSLEnabled == 1 && isZslEnabled()) {
                z = true;
            }
            return z;
        }
        if (this.mParallelPerformanceZSLEnabled == 1 && isZslEnabled()) {
            z = true;
        }
        return z;
    }

    public MeteringRectangle[] getAERegions() {
        return this.mAERegions;
    }

    public MeteringRectangle[] getAFRegions() {
        return this.mAFRegions;
    }

    public int getASDScene() {
        return this.mASDScene;
    }

    public int getAWBMode() {
        return this.mAwbMode;
    }

    public int getActivityHashCode() {
        return this.mActivityHashCode;
    }

    public int getAiSceneDetectPeriod() {
        return this.mAiSceneDetectPeriod;
    }

    public int getAlgorithmPreviewFormat() {
        return this.mAlgorithmPreviewFormat;
    }

    public CameraSize getAlgorithmPreviewSize() {
        return this.mAlgorithmPreviewSize;
    }

    public int getAmbilightMode() {
        return this.mAmbilightMode;
    }

    public int getAntiBanding() {
        return this.mAntiBanding;
    }

    public int getAutoZoomMode() {
        return this.mAutoZoomMode;
    }

    public float getAutoZoomScaleOffset() {
        return this.mAutoZoomScaleOffset;
    }

    public int getAwbCustomValue() {
        return this.mAwbCustomValue;
    }

    public BeautyValues getBeautyValues() {
        return this.mBeautyValues;
    }

    public CameraSize getBinningPhotoSize() {
        return this.mBinningPhotoSize;
    }

    public CameraSize getBokehDepthSize() {
        return this.mBokehDepthSize;
    }

    public long getCaptureTime() {
        return this.mCaptureTime;
    }

    public int[] getCaptureTriggerFlow() {
        return this.mCaptureTriggerFlow;
    }

    public int getColorEffect() {
        return this.mColorEffect;
    }

    public boolean getColorEnhanceEnabled() {
        return this.mColorEnhanceEnabled;
    }

    public int getContrastLevel() {
        return this.mContrastLevel;
    }

    public int getDeviceOrientation() {
        return this.mDeviceOrientation;
    }

    public boolean getDoRemosaic() {
        return this.mDoRemosaic;
    }

    public ASDScene getDxoAsdScene() {
        return this.mDxoAsdScene;
    }

    public int getExposureCompensationIndex() {
        return this.mExposureCompensationIndex;
    }

    public int getExposureMeteringMode() {
        return this.mExposureMeteringMode;
    }

    public long getExposureTime() {
        return this.mExposureTime;
    }

    public int getExtendSceneMode() {
        return this.mExtendSceneMode;
    }

    public int getEyeLightType() {
        return this.mEyeLightType;
    }

    public String getFNumber() {
        return this.mFNumber;
    }

    public String getFaceWaterMarkFormat() {
        return this.mFaceWaterMarkFormat;
    }

    public CameraSize getFakeSatOutputSize() {
        return this.mFakeSatOutputSize;
    }

    public CameraSize getFakeTelePhotoSize() {
        return this.mFakeTelePhotoSize;
    }

    public FeatureSetting getFeatureSetting() {
        return this.mFeatureSetting;
    }

    public int getFlashCurrent() {
        return this.flashCurrent;
    }

    public int getFlashMode() {
        return this.mFlashMode;
    }

    public float getFocusDistance() {
        return this.mFocusDistance;
    }

    public int getFocusMode() {
        return this.mFocusMode;
    }

    public Location getGpsLocation() {
        return this.mGpsLocation;
    }

    public int getHDR10Video() {
        return this.mHDR10VideoMode;
    }

    public int getHDRCheckerStatus() {
        return this.mHDRCheckerStatus;
    }

    public int getHDRMode() {
        return this.hdrMode;
    }

    public HDRStatus getHDRStatus() {
        return this.mHDRStatus;
    }

    public int getHdrCheckerAdrc() {
        return this.hdrCheckerAdrc;
    }

    public int[] getHdrCheckerEvValue() {
        return this.hdrCheckerEvValue;
    }

    public int getHdrCheckerSceneType() {
        return this.hdrCheckerSceneType;
    }

    public int getISO() {
        return this.mIso;
    }

    public int getJpegQuality() {
        return this.mJpegQuality;
    }

    public int getJpegRotation() {
        return this.mJpegRotation;
    }

    public CameraSize getLockedAlgoSize() {
        return this.mLockedAlgoSize;
    }

    public CameraSize getMacroPhotoSize() {
        return this.mMacroPhotoSize;
    }

    public boolean getNormalWideLDCEnabled() {
        return this.mNormalWideLDCEnabled;
    }

    public MarshalQueryableASDScene.ASDScene[] getOnTripodScenes() {
        return this.mOnTripodScene;
    }

    public CameraSize getOutputSize() {
        return this.mOutputSize;
    }

    public int getPhotoFormat() {
        return this.mPhotoFormat;
    }

    public int getPhotoMaxImages() {
        return this.mPhotoMaxImages;
    }

    public CameraSize getPhotoSize() {
        return this.mPhotoSize;
    }

    public int getPortraitLightingPattern() {
        return this.mPortraitLightingPattern;
    }

    public Range getPreviewFpsRange() {
        return this.mPreviewFpsRange;
    }

    public int getPreviewMaxImages() {
        return this.mPreviewMaxImages;
    }

    public CameraSize getPreviewSize() {
        return this.mPreviewSize;
    }

    public CameraSize getRawSizeOfMacro() {
        return this.mRawSizeOfMacro;
    }

    public CameraSize getRawSizeOfTele() {
        return this.mRawSizeOfTele;
    }

    public CameraSize getRawSizeOfTuningBuffer() {
        return this.mRawSizeOfTuningBuffer;
    }

    public CameraSize getRawSizeOfUltraTele() {
        return this.mRawSizeOfUltraTele;
    }

    public CameraSize getRawSizeOfUltraWide() {
        return this.mRawSizeOfUltraWide;
    }

    public CameraSize getRawSizeOfWide() {
        return this.mRawSizeOfWide;
    }

    public CameraSize getSATRemosicPhotoSize() {
        return this.mSATRemosicSize;
    }

    public int getSaturationLevel() {
        return this.mSaturationLevel;
    }

    public int getSceneMode() {
        return this.mSceneMode;
    }

    public CameraSize getSensorRawImageSize() {
        return this.mSensorRawImageSize;
    }

    public int getSharpnessLevel() {
        return this.mSharpnessLevel;
    }

    public String getShotPath() {
        ArrayDeque arrayDeque = this.mShotPath;
        if (arrayDeque == null) {
            return null;
        }
        String str = (String) arrayDeque.poll();
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getShotPath: ");
        sb.append(str);
        sb.append(", size:");
        sb.append(this.mShotPath.size());
        Log.d(str2, sb.toString());
        return str;
    }

    public int getShotType() {
        return this.mShotType;
    }

    public CameraSize getStandalonePhotoSize() {
        return this.mUltraTelePhotoSize;
    }

    public CameraSize getSubPhotoSize() {
        return this.mSubPhotoSize;
    }

    public float getTargetZoom() {
        return this.mTargetZoom;
    }

    public CameraSize getTelePhotoSize() {
        return this.mTelePhotoSize;
    }

    public int getThermalLevel() {
        return this.mThermalLevel;
    }

    public String getThumbnailShotPath() {
        ArrayDeque arrayDeque = this.mShotPathThumbnail;
        if (arrayDeque == null) {
            return null;
        }
        String str = (String) arrayDeque.poll();
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getThumbnailShotPath: ");
        sb.append(str);
        sb.append(", size:");
        sb.append(this.mShotPathThumbnail.size());
        Log.d(str2, sb.toString());
        return str;
    }

    public CameraSize getThumbnailSize() {
        return this.mThumbnailSize;
    }

    public String getTimeWaterMarkValue() {
        return this.mTimeWatermarkValue;
    }

    public CameraSize getTuningBufferSize() {
        return this.mTuningBufferSize;
    }

    public byte getTuringMode() {
        return this.mTuningMode;
    }

    public CameraSize getUltraWidePhotoSize() {
        return this.mUltraWidePhotoSize;
    }

    public int getVideoBokehColorRetentionMode(boolean z) {
        return z ? this.mFrontVideoBokehColorRetentionMode : this.mBackVideoBokehColorRetentionMode;
    }

    public int getVideoBokehLevelBack() {
        return this.mVideoBokehLevelBack;
    }

    public float getVideoBokehLevelFront() {
        return this.mVideoBokehLevelFront;
    }

    public boolean getVideoFilterColorRetentionBack() {
        return this.mVideoFilterColorRetentionBack;
    }

    public boolean getVideoFilterColorRetentionFront() {
        return this.mVideoFilterColorRetentionFront;
    }

    public int getVideoFilterId() {
        return this.mVideoFilterId;
    }

    public Range getVideoFpsRange() {
        return this.mVideoFpsRange;
    }

    public CameraSize getVideoSize() {
        return this.mVideoSize;
    }

    public CameraSize getVideoSnapshotSize() {
        return this.mVideoSnapshotSize;
    }

    public List getWaterMarkAppliedList() {
        return this.mWaterMarkAppliedList;
    }

    public CameraSize getWidePhotoSize() {
        return this.mWidePhotoSize;
    }

    public float getZoomRatio() {
        return this.mZoomRatio;
    }

    public int getmAmbilightAeTarget() {
        return this.mAmbilightAeTarget;
    }

    public int[] getmMtkPipDevices() {
        return this.mMtkPipDevices;
    }

    public boolean isAELocked() {
        return this.mAELocked;
    }

    public boolean isAIIEPreviewEnabled() {
        return this.mAiAIIEPreviewEnabled;
    }

    public boolean isASDEnabled() {
        return this.mASDEnable;
    }

    public boolean isAWBLocked() {
        return this.mAWBLocked;
    }

    public boolean isAiASDEnabled() {
        return this.mAiASDEnabled;
    }

    public boolean isAiMoonEffectEnabled() {
        return this.mAiMoonEffectEnabled;
    }

    public boolean isAiShutterEnable() {
        return this.mAiShutterEnable;
    }

    public boolean isAiShutterExistMotion() {
        return this.mAiShutterExistMotion;
    }

    public boolean isAsdDirtyEnable() {
        return this.mAsdDirtyEnable;
    }

    public boolean isCameraAi30Enabled() {
        return this.mCameraAi30Enabled;
    }

    public boolean isCinematicPhotoEnabled() {
        return this.mCinematicPhotoEnabled;
    }

    public boolean isCinematicVideoEnabled() {
        return this.mCinematicVideoEnabled;
    }

    public boolean isDodepurpleEnabled() {
        return this.mDodepurpleEnabled;
    }

    public boolean isDualBokehEnabled() {
        return this.mRearBokehEnabled;
    }

    public boolean isEISEnabled() {
        return this.mEISEnabled;
    }

    public boolean isEnableRecordControl() {
        return this.mEISEnabled || this.mIsVideoBokehEnabled;
    }

    public boolean isFaceAgeAnalyzeEnabled() {
        return this.mFaceAgeAnalyzeEnabled;
    }

    public boolean isFaceDetectionEnabled() {
        return this.mFaceDetectionEnabled;
    }

    public boolean isFaceExist() {
        return this.isFaceExist;
    }

    public boolean isFaceScoreEnabled() {
        return this.mFaceScoreEnabled;
    }

    public boolean isFlashAutoDetectionEnabled() {
        return this.isFlashAutoDetectionEnabled;
    }

    public boolean isFlawDetectEnable() {
        return this.mFlawDetectEnable;
    }

    public boolean isFrontMirror() {
        return this.mFrontMirror;
    }

    public boolean isHDRCheckerEnabled() {
        return this.mHDRCheckerEnabled;
    }

    public boolean isHDREnabled() {
        return this.mHDRStatus.isEnable();
    }

    public boolean isHFRDeflicker() {
        return this.mHFRDeflicker;
    }

    public boolean isHHTEnabled() {
        return this.mHHTEnabled;
    }

    public boolean isHighQualityPreferred() {
        return this.mIsHighQualityPreferredEnable;
    }

    public boolean isHistogramStatsEnabled() {
        return this.mHistogramStatsEnabled;
    }

    public boolean isInTimerBurstShotting() {
        return this.mIsInTimerBurstShotting;
    }

    public boolean isLLSEnabled() {
        return this.mLLSEnabled;
    }

    public boolean isLensDirtyDetectEnabled() {
        return this.mLensDirtyDetectEnabled;
    }

    public boolean isMFAfAeLock() {
        return this.mMFAfAeLock;
    }

    public boolean isMacroMode() {
        return this.mMacroMode;
    }

    public boolean isMfnrEnabled() {
        return this.mMfnrEnabled;
    }

    public boolean isModuleAnchorFrame() {
        return this.mModuleAnchorFrame;
    }

    public boolean isMultiSnapStopRequest() {
        return this.mMultiSnapStopRequest;
    }

    public boolean isNeedFlash() {
        return this.mNeedFlash;
    }

    public boolean isNeedPausePreview() {
        return this.mNeedPausePreview;
    }

    public boolean isNeedSequence() {
        return this.mNeedSequence;
    }

    public boolean isOISEnabled() {
        return this.mOISEnabled;
    }

    public boolean isParallelDualShotType() {
        return getShotType() == 6 || getShotType() == 11 || getShotType() == -7;
    }

    public boolean isParallelShotType() {
        return getShotType() == 6 || getShotType() == 11 || getShotType() == 5 || getShotType() == 8 || getShotType() == 7;
    }

    public boolean isParallelSupportedCaptureMode() {
        return isSupportParallelHDREnable() || isSupportParallelLLSEnable() || isSupportParallelSuperNightEnable() || isSupportParallelZSLEnable() || isSupportParallelSREnable() || isSupportParallelMFNREnable();
    }

    public boolean isQcfaEnable() {
        return this.mIsQcfaEnabled;
    }

    public boolean isQuickShotAnimation() {
        return this.mQuickShotAnimation;
    }

    public boolean isSatFallbackDisable() {
        return this.satFallbackDisable;
    }

    public boolean isSatIsZooming() {
        return this.satIsZooming;
    }

    public boolean isShot2Gallery() {
        return this.mIsShot2Gallery;
    }

    public boolean isSingleBokehEnabled() {
        return this.mSingleBokehEnabled;
    }

    public boolean isSpecshotModeEnable() {
        return this.mSpecshotModeEnable;
    }

    public boolean isSuperNightEnabled() {
        return this.isSuperNightEnabled;
    }

    public boolean isSuperResolutionEnabled() {
        return this.mSuperResolutionEnabled;
    }

    public boolean isSupportParallelHDREnable() {
        boolean z = false;
        if (CameraSettings.isHighQualityPreferred()) {
            if (this.mParallelQualityHDREnabled == 1 && isHDREnabled()) {
                z = true;
            }
            return z;
        }
        if (this.mParallelPerformanceHDREnabled == 1 && isHDREnabled()) {
            z = true;
        }
        return z;
    }

    public boolean isSwMfnrEnabled() {
        return this.mSwMfnrEnabled;
    }

    public boolean isUltraPixelPortraitEnabled() {
        return this.isUltraPixelPortraitEnabled;
    }

    public boolean isUltraWideLDCEnabled() {
        return this.mUltraWideLDCEnabled;
    }

    public boolean isUseLegacyFlashMode() {
        return this.mUseLegacyFlashMode;
    }

    public boolean isVideoHdrEnable() {
        return this.mVideoHdr;
    }

    public boolean isVideoLogEnable() {
        return this.mVideoLogEnable;
    }

    public boolean isVideoLogEnabled() {
        return this.mIsVideoLogEnabled;
    }

    public boolean isZslEnabled() {
        return this.mZslEnabled;
    }

    public boolean setAELock(boolean z) {
        if (this.mAELocked == z) {
            return false;
        }
        this.mAELocked = z;
        return true;
    }

    public boolean setAERegions(MeteringRectangle[] meteringRectangleArr) {
        if (Arrays.equals(this.mAERegions, meteringRectangleArr)) {
            return false;
        }
        this.mAERegions = meteringRectangleArr;
        return true;
    }

    public boolean setAFRegions(MeteringRectangle[] meteringRectangleArr) {
        if (Arrays.equals(this.mAFRegions, meteringRectangleArr)) {
            return false;
        }
        this.mAFRegions = meteringRectangleArr;
        return true;
    }

    public boolean setASDEnable(boolean z) {
        if (this.mASDEnable == z) {
            return false;
        }
        this.mASDEnable = z;
        return true;
    }

    public boolean setASDScene(int i) {
        if (this.mASDScene == i) {
            return false;
        }
        this.mASDScene = i;
        return true;
    }

    public boolean setAWBLock(boolean z) {
        if (this.mAWBLocked == z) {
            return false;
        }
        this.mAWBLocked = z;
        return true;
    }

    public boolean setAWBMode(int i) {
        if (this.mAwbMode == i) {
            return false;
        }
        this.mAwbMode = i;
        return true;
    }

    public void setActivityHashCode(int i) {
        this.mActivityHashCode = i;
    }

    public boolean setAiAIIEPreviewEnable(boolean z) {
        if (this.mAiAIIEPreviewEnabled == z) {
            return false;
        }
        this.mAiAIIEPreviewEnabled = z;
        return true;
    }

    public boolean setAiASDEnable(boolean z) {
        if (this.mAiASDEnabled == z) {
            return false;
        }
        this.mAiASDEnabled = z;
        return true;
    }

    public boolean setAiMoonEffectEnable(boolean z) {
        if (this.mAiMoonEffectEnabled == z) {
            return false;
        }
        this.mAiMoonEffectEnabled = z;
        return true;
    }

    public boolean setAiSceneDetectPeriod(int i) {
        if (this.mAiSceneDetectPeriod == i) {
            return false;
        }
        this.mAiSceneDetectPeriod = i;
        return true;
    }

    public boolean setAiShutterEnable(boolean z) {
        if (z == this.mAiShutterEnable) {
            return false;
        }
        this.mAiShutterEnable = z;
        return true;
    }

    public boolean setAiShutterExistMotion(boolean z) {
        if (z == this.mAiShutterExistMotion) {
            return false;
        }
        this.mAiShutterExistMotion = z;
        return true;
    }

    public boolean setAlgorithmPreviewFormat(int i) {
        if (this.mAlgorithmPreviewFormat == i) {
            return false;
        }
        this.mAlgorithmPreviewFormat = i;
        return true;
    }

    public boolean setAlgorithmPreviewSize(CameraSize cameraSize) {
        if (Objects.equals(this.mAlgorithmPreviewSize, cameraSize)) {
            return false;
        }
        this.mAlgorithmPreviewSize = cameraSize;
        return true;
    }

    public void setAmbilightAeTarget(int i) {
        this.mAmbilightAeTarget = i;
    }

    public void setAmbilightMode(int i) {
        this.mAmbilightMode = i;
    }

    public boolean setAntiBanding(int i) {
        if (this.mAntiBanding == i) {
            return false;
        }
        this.mAntiBanding = i;
        return true;
    }

    public boolean setAsdDirtyEnable(boolean z) {
        if (this.mAsdDirtyEnable == z) {
            return false;
        }
        this.mAsdDirtyEnable = z;
        return true;
    }

    public void setAutoZoomMode(int i) {
        this.mAutoZoomMode = i;
    }

    public void setAutoZoomScaleOffset(float f) {
        this.mAutoZoomScaleOffset = f;
    }

    public void setBeautyValues(BeautyValues beautyValues) {
        this.mBeautyValues = beautyValues;
    }

    public boolean setBinningPhotoSize(CameraSize cameraSize) {
        if (Objects.equals(this.mBinningPhotoSize, cameraSize)) {
            return false;
        }
        this.mBinningPhotoSize = cameraSize;
        return true;
    }

    public boolean setBokehDepthSize(CameraSize cameraSize) {
        if (Objects.equals(this.mBokehDepthSize, cameraSize)) {
            return false;
        }
        this.mBokehDepthSize = cameraSize;
        return true;
    }

    public boolean setCameraAi30Enable(boolean z) {
        if (this.mCameraAi30Enabled == z) {
            return false;
        }
        this.mCameraAi30Enabled = z;
        return true;
    }

    public void setCaptureTime(long j) {
        this.mCaptureTime = j;
    }

    public void setCaptureTriggerFlow(int[] iArr) {
        this.mCaptureTriggerFlow = iArr;
    }

    public void setCinematicPhotoEnabled(boolean z) {
        this.mCinematicPhotoEnabled = z;
    }

    public void setCinematicVideoEnabled(boolean z) {
        this.mCinematicVideoEnabled = z;
    }

    public boolean setColorEffect(int i) {
        if (this.mColorEffect == i) {
            return false;
        }
        this.mColorEffect = i;
        return true;
    }

    public boolean setColorEnhanceEnabled(boolean z) {
        if (this.mColorEnhanceEnabled == z) {
            return false;
        }
        this.mColorEnhanceEnabled = z;
        return true;
    }

    public boolean setContrastLevel(int i) {
        if (this.mContrastLevel == i) {
            return false;
        }
        this.mContrastLevel = i;
        return true;
    }

    public boolean setCustomAWB(int i) {
        if (this.mAwbCustomValue == i) {
            return false;
        }
        this.mAwbCustomValue = i;
        return true;
    }

    public boolean setDeviceOrientation(int i) {
        if (this.mDeviceOrientation == i) {
            return false;
        }
        this.mDeviceOrientation = i;
        return true;
    }

    public void setDoRemosaic(boolean z) {
        this.mDoRemosaic = z;
    }

    public boolean setDodepurpleEnabled(boolean z) {
        if (this.mDodepurpleEnabled == z) {
            return false;
        }
        this.mDodepurpleEnabled = z;
        return true;
    }

    public boolean setDualBokehEnable(boolean z) {
        if (this.mRearBokehEnabled == z) {
            return false;
        }
        this.mRearBokehEnabled = z;
        return true;
    }

    public boolean setDualCamWaterMarkEnable(boolean z) {
        String str = "device";
        boolean isStringValueContained = Util.isStringValueContained((Object) str, this.mWaterMarkAppliedList);
        if (z) {
            if (!isStringValueContained) {
                this.mWaterMarkAppliedList.add(str);
            }
        } else if (isStringValueContained) {
            this.mWaterMarkAppliedList.remove(str);
        }
        if (this.mDualCamWaterMarkEnabled == z) {
            return false;
        }
        this.mDualCamWaterMarkEnabled = z;
        return true;
    }

    public void setDxoAsdScene(ASDScene aSDScene) {
        this.mDxoAsdScene = aSDScene;
    }

    public boolean setEnableEIS(boolean z) {
        if (this.mEISEnabled == z) {
            return false;
        }
        this.mEISEnabled = z;
        return true;
    }

    public void setEnableOIS(boolean z) {
        this.mOISEnabled = z;
    }

    public void setEnableZsl(boolean z) {
        this.mZslEnabled = z;
    }

    public boolean setExposureCompensationIndex(int i) {
        if (this.mExposureCompensationIndex == i) {
            return false;
        }
        this.mExposureCompensationIndex = i;
        return true;
    }

    public boolean setExposureMeteringMode(int i) {
        if (this.mExposureMeteringMode == i) {
            return false;
        }
        this.mExposureMeteringMode = i;
        return true;
    }

    public boolean setExposureTime(long j) {
        if (this.mExposureTime == j) {
            return false;
        }
        this.mExposureTime = j;
        return true;
    }

    public boolean setExtendSceneMode(int i) {
        if (this.mExtendSceneMode == i) {
            return false;
        }
        this.mExtendSceneMode = i;
        return true;
    }

    public boolean setEyeLight(int i) {
        if (this.mEyeLightType == i) {
            return false;
        }
        this.mEyeLightType = i;
        return true;
    }

    public void setFNumber(String str) {
        this.mFNumber = str;
    }

    public boolean setFaceAgeAnalyzeEnabled(boolean z) {
        if (this.mFaceAgeAnalyzeEnabled == z) {
            return false;
        }
        this.mFaceAgeAnalyzeEnabled = z;
        return true;
    }

    public boolean setFaceDetectionEnabled(boolean z) {
        if (this.mFaceDetectionEnabled == z) {
            return false;
        }
        this.mFaceDetectionEnabled = z;
        return true;
    }

    public boolean setFaceScoreEnabled(boolean z) {
        if (this.mFaceScoreEnabled == z) {
            return false;
        }
        this.mFaceScoreEnabled = z;
        return true;
    }

    public boolean setFaceWaterMarkEnable(boolean z) {
        String str = "beautify";
        boolean isStringValueContained = Util.isStringValueContained((Object) str, this.mWaterMarkAppliedList);
        if (z) {
            if (!isStringValueContained) {
                this.mWaterMarkAppliedList.add(str);
            }
        } else if (isStringValueContained) {
            this.mWaterMarkAppliedList.remove(str);
        }
        if (this.mFaceWaterMarkEnabled == z) {
            return false;
        }
        this.mFaceWaterMarkEnabled = z;
        return true;
    }

    public void setFaceWaterMarkFormat(String str) {
        this.mFaceWaterMarkFormat = str;
    }

    public boolean setFakeSatOutputSize(CameraSize cameraSize) {
        if (Objects.equals(this.mFakeSatOutputSize, cameraSize)) {
            return false;
        }
        this.mFakeSatOutputSize = cameraSize;
        return true;
    }

    public boolean setFakeTelePhotoSize(CameraSize cameraSize) {
        if (Objects.equals(this.mFakeTelePhotoSize, cameraSize)) {
            return false;
        }
        this.mFakeTelePhotoSize = cameraSize;
        return true;
    }

    public void setFeatureSetting(FeatureSetting featureSetting) {
        this.mFeatureSetting = featureSetting;
    }

    public void setFlashCurrent(int i) {
        this.flashCurrent = i;
    }

    public void setFlashMode(int i) {
        this.mFlashMode = i;
    }

    public boolean setFlawDetectEnable(boolean z) {
        if (this.mFlawDetectEnable == z) {
            return false;
        }
        this.mFlawDetectEnable = z;
        return true;
    }

    public boolean setFocusDistance(float f) {
        if (this.mFocusDistance == f) {
            return false;
        }
        this.mFocusDistance = f;
        return true;
    }

    public boolean setFocusMode(int i) {
        if (this.mFocusMode == i) {
            return false;
        }
        this.mFocusMode = i;
        return true;
    }

    public void setFrontMirror(boolean z) {
        this.mFrontMirror = z;
    }

    public void setGlobalWatermark() {
        String str = "global";
        if (!Util.isStringValueContained((Object) str, this.mWaterMarkAppliedList)) {
            this.mWaterMarkAppliedList.add(str);
        }
    }

    public void setGpsLocation(Location location) {
        this.mGpsLocation = location;
    }

    public boolean setHDR10Video(int i) {
        if (this.mHDR10VideoMode == i) {
            return false;
        }
        this.mHDR10VideoMode = i;
        return true;
    }

    public boolean setHDRCheckerEnabled(boolean z) {
        if (this.mHDRCheckerEnabled == z) {
            return false;
        }
        this.mHDRCheckerEnabled = z;
        return true;
    }

    public boolean setHDRCheckerStatus(int i) {
        if (this.mHDRCheckerStatus == i) {
            return false;
        }
        this.mHDRCheckerStatus = i;
        return true;
    }

    public boolean setHDRMode(int i) {
        if (this.hdrMode == i) {
            return false;
        }
        this.hdrMode = i;
        return true;
    }

    public boolean setHDRStatus(HDRStatus hDRStatus) {
        if (this.mHDRStatus == hDRStatus) {
            return false;
        }
        this.mHDRStatus = hDRStatus;
        return true;
    }

    public boolean setHFRDeflickerEnable(boolean z) {
        if (z == this.mHFRDeflicker) {
            return false;
        }
        this.mHFRDeflicker = z;
        return true;
    }

    public boolean setHHTEnabled(boolean z) {
        if (this.mHHTEnabled == z) {
            return false;
        }
        this.mHHTEnabled = z;
        return true;
    }

    public void setHdrCheckerAdrc(int i) {
        this.hdrCheckerAdrc = i;
    }

    public void setHdrCheckerEvValue(int[] iArr) {
        this.hdrCheckerEvValue = iArr;
    }

    public void setHdrCheckerSceneType(int i) {
        this.hdrCheckerSceneType = i;
    }

    public boolean setHighQualityPreferred(boolean z) {
        if (z == this.mIsHighQualityPreferredEnable) {
            return false;
        }
        this.mIsHighQualityPreferredEnable = z;
        return true;
    }

    public void setHistogramStatsEnabled(boolean z) {
        this.mHistogramStatsEnabled = z;
    }

    public boolean setISO(int i) {
        if (this.mIso == i) {
            return false;
        }
        this.mIso = i;
        return true;
    }

    public boolean setInTimerBurstShotting(boolean z) {
        if (this.mIsInTimerBurstShotting == z) {
            return false;
        }
        this.mIsInTimerBurstShotting = z;
        return true;
    }

    public void setIsFaceExist(boolean z) {
        this.isFaceExist = z;
    }

    public boolean setIsVideoLogEnabled(boolean z) {
        if (this.mIsVideoLogEnabled == z) {
            return false;
        }
        this.mIsVideoLogEnabled = z;
        return true;
    }

    public boolean setJpegQuality(int i) {
        if (i < 1 || i > 100) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("setJpegQuality: invalid jpeg quality ");
            sb.append(i);
            Log.w(str, sb.toString());
            return false;
        } else if (this.mJpegQuality == i) {
            return false;
        } else {
            this.mJpegQuality = i;
            return true;
        }
    }

    public boolean setJpegRotation(int i) {
        if (this.mJpegRotation == i) {
            return false;
        }
        this.mJpegRotation = i;
        return true;
    }

    public boolean setLLSEnabled(boolean z) {
        if (this.mLLSEnabled == z) {
            return false;
        }
        this.mLLSEnabled = z;
        return true;
    }

    public boolean setLensDirtyDetectEnabled(boolean z) {
        if (this.mLensDirtyDetectEnabled == z) {
            return false;
        }
        this.mLensDirtyDetectEnabled = z;
        return true;
    }

    public void setLockedAlgoSize(CameraSize cameraSize) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setLockedAlgoSize: ");
        sb.append(cameraSize);
        Log.d(str, sb.toString());
        this.mLockedAlgoSize = cameraSize;
    }

    public void setMFAfAeLock(boolean z) {
        this.mMFAfAeLock = z;
    }

    public boolean setMacroMode(boolean z) {
        if (z == this.mMacroMode) {
            return false;
        }
        this.mMacroMode = z;
        return true;
    }

    public boolean setMacroPhotoSize(CameraSize cameraSize) {
        if (Objects.equals(this.mMacroPhotoSize, cameraSize)) {
            return false;
        }
        this.mMacroPhotoSize = cameraSize;
        return true;
    }

    public boolean setMfnrEnabled(boolean z) {
        if (this.mMfnrEnabled == z) {
            return false;
        }
        this.mMfnrEnabled = z;
        return true;
    }

    public void setModuleAnchorFrame(boolean z) {
        this.mModuleAnchorFrame = z;
    }

    public void setMtkPipDevices(int[] iArr) {
        this.mMtkPipDevices = iArr;
    }

    public void setMultiSnapStopRequest(boolean z) {
        this.mMultiSnapStopRequest = z;
    }

    public boolean setNeedFlash(boolean z) {
        if (this.mNeedFlash == z) {
            return false;
        }
        this.mNeedFlash = z;
        return true;
    }

    public void setNeedSequence(boolean z) {
        this.mNeedSequence = z;
    }

    public void setNewWatermark(boolean z) {
        String str = "new";
        boolean isStringValueContained = Util.isStringValueContained((Object) str, this.mWaterMarkAppliedList);
        if (z) {
            if (!isStringValueContained) {
                this.mWaterMarkAppliedList.add(str);
            }
        } else if (isStringValueContained) {
            this.mWaterMarkAppliedList.remove(str);
        }
    }

    public boolean setNormalWideLDCEnabled(boolean z) {
        if (this.mNormalWideLDCEnabled == z) {
            return false;
        }
        this.mNormalWideLDCEnabled = z;
        return true;
    }

    public void setOnTripodScenes(MarshalQueryableASDScene.ASDScene[] aSDSceneArr) {
        this.mOnTripodScene = aSDSceneArr;
    }

    public void setOutputSize(CameraSize cameraSize) {
        if (cameraSize != null) {
            this.mOutputSize = new CameraSize(cameraSize.getWidth(), cameraSize.getHeight());
        } else {
            this.mOutputSize = null;
        }
    }

    public void setParallelSettings(int i, int i2) {
        this.mParallelPerformanceMFNREnabled = i & 1;
        this.mParallelPerformanceHDREnabled = (i & 2) >> 1;
        this.mParallelPerformanceSuperNightEnabled = (i & 4) >> 2;
        this.mParallelPerformanceLLSEnabled = (i & 8) >> 3;
        this.mParallelPerformanceSREnabled = (i & 16) >> 4;
        this.mParallelPerformanceZSLEnabled = (i & 32) >> 5;
        this.mParallelQualityMFNREnabled = i2 & 1;
        this.mParallelQualityHDREnabled = (i2 & 2) >> 1;
        this.mParallelQualitySuperNightEnabled = (i2 & 4) >> 2;
        this.mParallelQualityLLSEnabled = (i2 & 8) >> 3;
        this.mParallelQualitySREnabled = (i2 & 16) >> 4;
        this.mParallelQualityZSLEnabled = (i2 & 32) >> 5;
    }

    public boolean setPausePreview(boolean z) {
        if (this.mNeedPausePreview == z) {
            return false;
        }
        this.mNeedPausePreview = z;
        return true;
    }

    public boolean setPhotoFormat(int i) {
        if (this.mPhotoFormat == i) {
            return false;
        }
        this.mPhotoFormat = i;
        return true;
    }

    public void setPhotoMaxImages(int i) {
        this.mPhotoMaxImages = i;
    }

    public boolean setPhotoSize(CameraSize cameraSize) {
        if (Objects.equals(this.mPhotoSize, cameraSize)) {
            return false;
        }
        this.mPhotoSize = cameraSize;
        return true;
    }

    public boolean setPortraitLightingPattern(int i) {
        if (this.mPortraitLightingPattern == i) {
            return false;
        }
        this.mPortraitLightingPattern = i;
        return true;
    }

    public boolean setPreviewFpsRange(Range range) {
        if (Objects.equals(this.mPreviewFpsRange, range)) {
            return false;
        }
        this.mPreviewFpsRange = range;
        return true;
    }

    public void setPreviewMaxImages(int i) {
        this.mPreviewMaxImages = i;
    }

    public boolean setPreviewSize(CameraSize cameraSize) {
        if (Objects.equals(this.mPreviewSize, cameraSize)) {
            return false;
        }
        this.mPreviewSize = cameraSize;
        return true;
    }

    public void setQcfaEnable(boolean z) {
        this.mIsQcfaEnabled = z;
    }

    public void setQuickShotAnimation(boolean z) {
        this.mQuickShotAnimation = z;
    }

    public boolean setRawSizeOfMacro(CameraSize cameraSize) {
        if (Objects.equals(this.mRawSizeOfMacro, cameraSize)) {
            return false;
        }
        this.mRawSizeOfMacro = cameraSize;
        return true;
    }

    public boolean setRawSizeOfTele(CameraSize cameraSize) {
        if (Objects.equals(this.mRawSizeOfTele, cameraSize)) {
            return false;
        }
        this.mRawSizeOfTele = cameraSize;
        return true;
    }

    public boolean setRawSizeOfTuningBuffer(CameraSize cameraSize) {
        if (Objects.equals(this.mRawSizeOfTuningBuffer, cameraSize)) {
            return false;
        }
        this.mRawSizeOfTuningBuffer = cameraSize;
        return true;
    }

    public boolean setRawSizeOfUltraTele(CameraSize cameraSize) {
        if (Objects.equals(this.mRawSizeOfUltraTele, cameraSize)) {
            return false;
        }
        this.mRawSizeOfUltraTele = cameraSize;
        return true;
    }

    public boolean setRawSizeOfUltraWide(CameraSize cameraSize) {
        if (Objects.equals(this.mRawSizeOfUltraWide, cameraSize)) {
            return false;
        }
        this.mRawSizeOfUltraWide = cameraSize;
        return true;
    }

    public boolean setRawSizeOfWide(CameraSize cameraSize) {
        if (Objects.equals(this.mRawSizeOfWide, cameraSize)) {
            return false;
        }
        this.mRawSizeOfWide = cameraSize;
        return true;
    }

    public boolean setSATRemosicPhotoSize(CameraSize cameraSize) {
        if (Objects.equals(this.mSATRemosicSize, cameraSize)) {
            return false;
        }
        this.mSATRemosicSize = cameraSize;
        return true;
    }

    public void setSatFallbackDisable(boolean z) {
        this.satFallbackDisable = z;
    }

    public void setSatIsZooming(boolean z) {
        this.satIsZooming = z;
    }

    public boolean setSaturationLevel(int i) {
        if (this.mSaturationLevel == i) {
            return false;
        }
        this.mSaturationLevel = i;
        return true;
    }

    public boolean setSceneMode(int i) {
        if (this.mSceneMode == i) {
            return false;
        }
        this.mSceneMode = i;
        return true;
    }

    public boolean setSensorRawImageSize(CameraSize cameraSize) {
        if (Objects.equals(this.mSensorRawImageSize, cameraSize)) {
            return false;
        }
        this.mSensorRawImageSize = cameraSize;
        return true;
    }

    public boolean setSharpnessLevel(int i) {
        if (this.mSharpnessLevel == i) {
            return false;
        }
        this.mSharpnessLevel = i;
        return true;
    }

    public boolean setShot2Gallery(boolean z) {
        if (this.mIsShot2Gallery == z) {
            return false;
        }
        this.mIsShot2Gallery = z;
        return true;
    }

    public void setShotPath(String str, boolean z) {
        boolean isSaveToHidenFolder = Storage.isSaveToHidenFolder(Util.getFileTitleFromPath(str));
        if (z || this.mShotType == 8 || isSaveToHidenFolder) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("setShotPath: ");
            sb.append(str);
            Log.d(str2, sb.toString());
            if (this.mShotPath == null) {
                this.mShotPath = new ArrayDeque(5);
            }
            this.mShotPath.offer(str);
            if (z) {
                if (this.mShotPathThumbnail == null) {
                    this.mShotPathThumbnail = new ArrayDeque(5);
                }
                this.mShotPathThumbnail.offer(str);
                return;
            }
            return;
        }
        String str3 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("setShotPath: skip. shotType=");
        sb2.append(this.mShotType);
        Log.d(str3, sb2.toString());
    }

    public boolean setShotType(int i) {
        if (this.mShotType == i) {
            return false;
        }
        this.mShotType = i;
        return true;
    }

    public boolean setSingleBokehEnabled(boolean z) {
        if (this.mSingleBokehEnabled == z) {
            return false;
        }
        this.mSingleBokehEnabled = z;
        return true;
    }

    public boolean setSpecshotModeEnable(boolean z) {
        if (this.mSpecshotModeEnable == z) {
            return false;
        }
        this.mSpecshotModeEnable = z;
        return true;
    }

    public boolean setSubPhotoSize(CameraSize cameraSize) {
        if (Objects.equals(this.mSubPhotoSize, cameraSize)) {
            return false;
        }
        this.mSubPhotoSize = cameraSize;
        return true;
    }

    public void setSuperNightEnabled(boolean z) {
        this.isSuperNightEnabled = z;
    }

    public boolean setSuperResolutionEnabled(boolean z) {
        if (this.mSuperResolutionEnabled == z) {
            return false;
        }
        this.mSuperResolutionEnabled = z;
        return true;
    }

    public boolean setSwMfnrEnabled(boolean z) {
        if (this.mSwMfnrEnabled == z) {
            return false;
        }
        this.mSwMfnrEnabled = z;
        return true;
    }

    public boolean setTargetZoom(float f) {
        if (f == this.mTargetZoom) {
            return false;
        }
        this.mTargetZoom = f;
        return true;
    }

    public boolean setTelePhotoSize(CameraSize cameraSize) {
        if (Objects.equals(this.mTelePhotoSize, cameraSize)) {
            return false;
        }
        this.mTelePhotoSize = cameraSize;
        return true;
    }

    public void setThermalLevel(int i) {
        this.mThermalLevel = i;
    }

    public boolean setThumbnailSize(CameraSize cameraSize) {
        if (Objects.equals(this.mThumbnailSize, cameraSize)) {
            return false;
        }
        this.mThumbnailSize = cameraSize;
        return true;
    }

    public boolean setTimeWaterMarkEnable(boolean z) {
        List list = this.mWaterMarkAppliedList;
        String str = WatermarkConstant.ITEM_TAG;
        boolean isStringValueContained = Util.isStringValueContained((Object) str, list);
        if (z) {
            if (!isStringValueContained) {
                this.mWaterMarkAppliedList.add(str);
            }
        } else if (isStringValueContained) {
            this.mWaterMarkAppliedList.remove(str);
        }
        if (this.mTimeWaterMarkEnabled == z) {
            return false;
        }
        this.mTimeWaterMarkEnabled = z;
        return true;
    }

    public void setTimeWaterMarkValue(String str) {
        this.mTimeWatermarkValue = str;
    }

    public boolean setTuningBufferSize(CameraSize cameraSize) {
        if (Objects.equals(this.mTuningBufferSize, cameraSize)) {
            return false;
        }
        this.mTuningBufferSize = cameraSize;
        return true;
    }

    public boolean setTuningMode(byte b) {
        if (b == this.mTuningMode) {
            return false;
        }
        this.mTuningMode = b;
        return true;
    }

    public boolean setUltraPixelPortraitEnabled(boolean z) {
        if (this.isUltraPixelPortraitEnabled == z) {
            return false;
        }
        this.isUltraPixelPortraitEnabled = z;
        return true;
    }

    public boolean setUltraTelePhotoSize(CameraSize cameraSize) {
        if (Objects.equals(this.mUltraTelePhotoSize, cameraSize)) {
            return false;
        }
        this.mUltraTelePhotoSize = cameraSize;
        return true;
    }

    public boolean setUltraWideLDCEnabled(boolean z) {
        if (this.mUltraWideLDCEnabled == z) {
            return false;
        }
        this.mUltraWideLDCEnabled = z;
        return true;
    }

    public boolean setUltraWidePhotoSize(CameraSize cameraSize) {
        if (Objects.equals(this.mUltraWidePhotoSize, cameraSize)) {
            return false;
        }
        this.mUltraWidePhotoSize = cameraSize;
        return true;
    }

    public void setUseLegacyFlashMode(boolean z) {
        this.mUseLegacyFlashMode = z;
    }

    public boolean setVideoBokehColorRetentionMode(int i, boolean z) {
        if (z) {
            if (this.mFrontVideoBokehColorRetentionMode != i) {
                this.mFrontVideoBokehColorRetentionMode = i;
                return true;
            }
        } else if (this.mBackVideoBokehColorRetentionMode != i) {
            this.mBackVideoBokehColorRetentionMode = i;
            return true;
        }
        return false;
    }

    public boolean setVideoBokehEnabled(boolean z) {
        if (this.mIsVideoBokehEnabled == z) {
            return false;
        }
        this.mIsVideoBokehEnabled = z;
        return true;
    }

    public void setVideoBokehLevelBack(int i) {
        this.mVideoBokehLevelBack = i;
    }

    public void setVideoBokehLevelFront(float f) {
        this.mVideoBokehLevelFront = f;
    }

    public boolean setVideoFilterColorRetentionBack(boolean z) {
        if (this.mVideoFilterColorRetentionBack == z) {
            return false;
        }
        this.mVideoFilterColorRetentionBack = z;
        return true;
    }

    public boolean setVideoFilterColorRetentionFront(boolean z) {
        if (this.mVideoFilterColorRetentionFront == z) {
            return false;
        }
        this.mVideoFilterColorRetentionFront = z;
        return true;
    }

    public void setVideoFilterId(int i) {
        this.mVideoFilterId = i;
    }

    public boolean setVideoFpsRange(Range range) {
        if (Objects.equals(this.mVideoFpsRange, range)) {
            return false;
        }
        this.mVideoFpsRange = range;
        return true;
    }

    public boolean setVideoHdrEnable(boolean z) {
        if (z == this.mVideoHdr) {
            return false;
        }
        this.mVideoHdr = z;
        return true;
    }

    public boolean setVideoLogEnable(boolean z) {
        if (this.mVideoLogEnable == z) {
            return false;
        }
        this.mVideoLogEnable = z;
        return true;
    }

    public void setVideoSize(CameraSize cameraSize) {
        this.mVideoSize = cameraSize;
    }

    public void setVideoSnapshotSize(CameraSize cameraSize) {
        this.mVideoSnapshotSize = cameraSize;
    }

    public boolean setWidePhotoSize(CameraSize cameraSize) {
        if (Objects.equals(this.mWidePhotoSize, cameraSize)) {
            return false;
        }
        this.mWidePhotoSize = cameraSize;
        return true;
    }

    public boolean setZoomRatio(float f) {
        float abs = Math.abs(this.mZoomRatio - f);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("zoom ratio diff: ");
        sb.append(abs);
        Log.d(str, sb.toString());
        this.mZoomRatio = f;
        return true;
    }

    public void updateFlashAutoDetectionEnabled(boolean z) {
        this.isFlashAutoDetectionEnabled = z;
    }
}
