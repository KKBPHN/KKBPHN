package com.android.camera.storage;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.location.Location;
import android.net.Uri;
import android.text.TextUtils;
import com.android.camera.CameraSettings;
import com.android.camera.Util;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.EffectController.EffectRectAttribute;
import com.android.camera.effect.FilterInfo;
import com.android.camera.effect.draw_mode.DrawJPEGAttribute;
import com.android.camera.effect.renders.DeviceWatermarkParam;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.camera2.PortraitDepthMap;
import com.android.gallery3d.exif.ExifHelper;
import com.android.gallery3d.exif.ExifInterface;
import com.xiaomi.camera.base.PerformanceTracker;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.core.ParallelTaskDataParameter;
import com.xiaomi.camera.core.PictureInfo;
import com.xiaomi.camera.liveshot.CircularMediaRecorder.VideoClipSavingCallback;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

public abstract class AbstractSaveRequest implements SaveRequest {
    private static final String TAG = "AbstractSaveRequest";
    protected long date;
    public int height;
    protected ParallelTaskData mParallelTaskData;
    private WeakReference mSaverCallbackRef;
    public int orientation;
    public int width;

    private DrawJPEGAttribute getDrawJPEGAttribute(byte[] bArr, int i, int i2, int i3, boolean z, int i4, int i5, Location location, String str, int i6, int i7, float f, String str2, boolean z2, boolean z3, String str3, boolean z4, boolean z5, DeviceWatermarkParam deviceWatermarkParam, List list, boolean z6, PictureInfo pictureInfo, int i8, int i9, boolean z7) {
        int i10 = i4;
        int i11 = i5;
        Location location2 = location;
        int max = i10 > i11 ? Math.max(i, i2) : Math.min(i, i2);
        int max2 = i11 > i10 ? Math.max(i, i2) : Math.min(i, i2);
        EffectRectAttribute copyEffectRectAttribute = EffectController.getInstance().copyEffectRectAttribute();
        Location location3 = location2 == null ? null : new Location(location2);
        long currentTimeMillis = System.currentTimeMillis();
        boolean isFrontMirror = pictureInfo.isFrontMirror();
        boolean z8 = CameraSettings.isDualCameraWaterMarkOpen() || CameraSettings.isFrontCameraWaterMarkOpen();
        DrawJPEGAttribute drawJPEGAttribute = new DrawJPEGAttribute(bArr, z, max, max2, i4, i5, i3, copyEffectRectAttribute, location3, str, currentTimeMillis, i6, i7, f, isFrontMirror, str2, z2, pictureInfo, list, z8, z3, CameraSettings.isTimeWaterMarkOpen() ? str3 : null, z4, z5, deviceWatermarkParam, z6, i8, i9, z7);
        return drawJPEGAttribute;
    }

    private SaverCallback getSaverCallback() {
        WeakReference weakReference = this.mSaverCallbackRef;
        if (weakReference != null) {
            return (SaverCallback) weakReference.get();
        }
        return null;
    }

    private void parserMimojiCaptureTask(ParallelTaskData parallelTaskData) {
        int i;
        int i2;
        ParallelTaskDataParameter parallelTaskDataParameter;
        ParallelTaskDataParameter dataParameter = parallelTaskData.getDataParameter();
        byte[] jpegImageData = parallelTaskData.getJpegImageData();
        int width2 = dataParameter.getOutputSize().getWidth();
        int height2 = dataParameter.getOutputSize().getHeight();
        int jpegRotation = dataParameter.getJpegRotation();
        int filterId = dataParameter.getFilterId();
        ExifInterface exif = ExifInterface.getExif(jpegImageData);
        int orientation2 = ExifInterface.getOrientation(exif);
        boolean z = EffectController.getInstance().hasEffect(false) || filterId != FilterInfo.FILTER_ID_NONE;
        String createJpegName = Util.createJpegName(System.currentTimeMillis());
        if (parallelTaskData.isAdaptiveSnapshotSize()) {
            int imageWidth = ExifInterface.getImageWidth(exif);
            i = ExifInterface.getImageHeight(exif);
            i2 = imageWidth;
        } else if ((jpegRotation + orientation2) % 180 == 0) {
            i2 = width2;
            i = height2;
        } else {
            i = width2;
            i2 = height2;
        }
        if (z || dataParameter.isHasWaterMark()) {
            SaverCallback saverCallback = getSaverCallback();
            if (saverCallback != null) {
                parallelTaskDataParameter = dataParameter;
                SaverCallback saverCallback2 = saverCallback;
                DrawJPEGAttribute drawJPEGAttribute = getDrawJPEGAttribute(jpegImageData, dataParameter.getPreviewSize().getWidth(), dataParameter.getPreviewSize().getHeight(), filterId, parallelTaskData.isNeedThumbnail(), i2, i, dataParameter.getLocation(), createJpegName, dataParameter.getShootOrientation(), jpegRotation, dataParameter.getShootRotation(), dataParameter.getAlgorithmName(), dataParameter.isHasWaterMark(), dataParameter.isUltraPixelWaterMark(), dataParameter.getTimeWaterMarkString(), dataParameter.isHasDualWaterMark(), dataParameter.isHasFrontWaterMark(), dataParameter.getDeviceWatermarkParam(), dataParameter.getFaceWaterMarkList(), false, dataParameter.getPictureInfo(), parallelTaskData.getCurrentModuleIndex(), parallelTaskData.getPreviewThumbnailHash(), dataParameter.isMiMovieOpen());
                saverCallback2.processorJpegSync(false, drawJPEGAttribute);
                jpegImageData = drawJPEGAttribute.mData;
            } else {
                parallelTaskDataParameter = dataParameter;
                Log.d(TAG, "parserMimojiCaptureTask(): saverCallback is null");
            }
        } else {
            parallelTaskDataParameter = dataParameter;
        }
        reFillSaveRequest(jpegImageData, parallelTaskData.isNeedThumbnail(), createJpegName, null, parallelTaskData.getDateTakenTime(), null, parallelTaskDataParameter.getLocation(), width2, height2, null, jpegRotation, false, false, true, false, false, Util.ALGORITHM_NAME_MIMOJI_CAPTURE, parallelTaskDataParameter.getPictureInfo(), parallelTaskData.getPreviewThumbnailHash());
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x01a8  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x01fa  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0249  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0252  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parserNormalDualTask(ParallelTaskData parallelTaskData) {
        int i;
        int i2;
        String str;
        byte[] bArr;
        int[] iArr;
        byte[] bArr2;
        String str2;
        boolean isDepthMapData = PortraitDepthMap.isDepthMapData(parallelTaskData.getPortraitDepthData());
        byte[] jpegImageData = parallelTaskData.getJpegImageData();
        byte[] portraitRawData = parallelTaskData.getPortraitRawData();
        byte[] portraitDepthData = parallelTaskData.getPortraitDepthData();
        ParallelTaskDataParameter dataParameter = parallelTaskData.getDataParameter();
        int filterId = dataParameter.getFilterId();
        boolean z = EffectController.getInstance().hasEffect(false) || filterId != FilterInfo.FILTER_ID_NONE;
        int width2 = dataParameter.getOutputSize().getWidth();
        int height2 = dataParameter.getOutputSize().getHeight();
        ExifInterface exif = ExifInterface.getExif(jpegImageData);
        int orientation2 = ExifInterface.getOrientation(exif);
        int jpegRotation = dataParameter.getJpegRotation();
        if (parallelTaskData.isAdaptiveSnapshotSize()) {
            width2 = ExifInterface.getImageWidth(exif);
            height2 = ExifInterface.getImageHeight(exif);
        } else if ((jpegRotation + orientation2) % 180 != 0) {
            i = width2;
            i2 = height2;
            if (!parallelTaskData.isShot2Gallery() || parallelTaskData.isInTimerBurstShotting()) {
                str = Util.getFileTitleFromPath(parallelTaskData.getSavePath());
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(Util.createJpegName(System.currentTimeMillis()));
                sb.append(dataParameter.getSuffix());
                str = sb.toString();
            }
            String str3 = str;
            String str4 = TAG;
            byte[] bArr3 = null;
            if (z) {
                SaverCallback saverCallback = getSaverCallback();
                if (saverCallback != null) {
                    SaverCallback saverCallback2 = saverCallback;
                    String str5 = str4;
                    int i3 = filterId;
                    DrawJPEGAttribute drawJPEGAttribute = getDrawJPEGAttribute(jpegImageData, dataParameter.getPreviewSize().getWidth(), dataParameter.getPreviewSize().getHeight(), filterId, parallelTaskData.isNeedThumbnail(), i2, i, dataParameter.getLocation(), str3, dataParameter.getShootOrientation(), orientation2, dataParameter.getShootRotation(), dataParameter.getAlgorithmName(), dataParameter.isHasWaterMark(), false, dataParameter.getTimeWaterMarkString(), dataParameter.isHasDualWaterMark(), dataParameter.isHasFrontWaterMark(), dataParameter.getDeviceWatermarkParam(), dataParameter.getFaceWaterMarkList(), false, dataParameter.getPictureInfo(), parallelTaskData.getCurrentModuleIndex(), -1, dataParameter.isMiMovieOpen());
                    DrawJPEGAttribute drawJPEGAttribute2 = isDepthMapData ? getDrawJPEGAttribute(portraitRawData, dataParameter.getPreviewSize().getWidth(), dataParameter.getPreviewSize().getHeight(), i3, parallelTaskData.isNeedThumbnail(), i2, i, dataParameter.getLocation(), str3, dataParameter.getShootOrientation(), orientation2, dataParameter.getShootRotation(), dataParameter.getAlgorithmName(), false, false, dataParameter.getTimeWaterMarkString(), dataParameter.isHasDualWaterMark(), dataParameter.isHasFrontWaterMark(), dataParameter.getDeviceWatermarkParam(), dataParameter.getFaceWaterMarkList(), true, dataParameter.getPictureInfo(), parallelTaskData.getCurrentModuleIndex(), -1, dataParameter.isMiMovieOpen()) : null;
                    saverCallback2.processorJpegSync(false, drawJPEGAttribute, drawJPEGAttribute2);
                    byte[] bArr4 = drawJPEGAttribute.mData;
                    byte[] bArr5 = drawJPEGAttribute.mDataOfTheRegionUnderWatermarks;
                    iArr = drawJPEGAttribute.mCoordinatesOfTheRegionUnderWatermarks;
                    if (C0124O00000oO.O0o0O00) {
                        iArr = null;
                    } else {
                        bArr3 = bArr5;
                    }
                    if (isDepthMapData) {
                        portraitRawData = drawJPEGAttribute2.mData;
                    }
                    jpegImageData = bArr4;
                    bArr = portraitRawData;
                    str4 = str5;
                    if (isDepthMapData) {
                        str2 = str4;
                        bArr2 = Util.composeDepthMapPicture(jpegImageData, portraitDepthData, bArr, bArr3, iArr, -1, dataParameter.getSupportZeroDegreeOrientationImage(), dataParameter.isHasDualWaterMark(), dataParameter.isHasFrontWaterMark(), dataParameter.getLightingPattern(), dataParameter.getTimeWaterMarkString(), dataParameter.getOutputSize().getWidth(), dataParameter.getOutputSize().getHeight(), dataParameter.isMirror(), dataParameter.isBokehFrontCamera(), dataParameter.getJpegRotation(), dataParameter.getDeviceWatermarkParam(), dataParameter.getPictureInfo(), dataParameter.getPortraitLightingVersion(), parallelTaskData.getTimestamp(), dataParameter.getCameraPreferredMode());
                    } else {
                        str2 = str4;
                        bArr2 = Util.composeMainSubPicture(jpegImageData, bArr3, iArr, dataParameter.getOutputSize().getWidth(), dataParameter.getOutputSize().getHeight(), dataParameter.isHasDualWaterMark(), dataParameter.isHasFrontWaterMark(), dataParameter.getTimeWaterMarkString(), dataParameter.getJpegRotation(), dataParameter.getDeviceWatermarkParam(), parallelTaskData, -1);
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("insertNormalDualTask: isShot2Gallery = ");
                    sb2.append(parallelTaskData.isShot2Gallery());
                    Log.d(str2, sb2.toString());
                    if (parallelTaskData.isShot2Gallery()) {
                        parallelTaskData.refillJpegData(bArr2);
                        parserParallelDualTask(parallelTaskData);
                        return;
                    }
                    ParallelTaskData parallelTaskData2 = parallelTaskData;
                    reFillSaveRequest(bArr2, parallelTaskData.isNeedThumbnail(), str3, null, parallelTaskData.getDateTakenTime(), null, dataParameter.getLocation(), i2, i, null, orientation2, false, false, true, false, false, dataParameter.getAlgorithmName(), dataParameter.getPictureInfo(), -1);
                    return;
                }
                String str6 = str4;
                Log.d(str4, "parserNormalDualTask(): saverCallback is null");
            }
            bArr = portraitRawData;
            iArr = null;
            if (isDepthMapData) {
            }
            StringBuilder sb22 = new StringBuilder();
            sb22.append("insertNormalDualTask: isShot2Gallery = ");
            sb22.append(parallelTaskData.isShot2Gallery());
            Log.d(str2, sb22.toString());
            if (parallelTaskData.isShot2Gallery()) {
            }
        }
        i2 = width2;
        i = height2;
        if (!parallelTaskData.isShot2Gallery()) {
        }
        str = Util.getFileTitleFromPath(parallelTaskData.getSavePath());
        String str32 = str;
        String str42 = TAG;
        byte[] bArr32 = null;
        if (z) {
        }
        bArr = portraitRawData;
        iArr = null;
        if (isDepthMapData) {
        }
        StringBuilder sb222 = new StringBuilder();
        sb222.append("insertNormalDualTask: isShot2Gallery = ");
        sb222.append(parallelTaskData.isShot2Gallery());
        Log.d(str2, sb222.toString());
        if (parallelTaskData.isShot2Gallery()) {
        }
    }

    private void parserParallelBurstTask(ParallelTaskData parallelTaskData) {
        int i;
        ParallelTaskDataParameter dataParameter = parallelTaskData.getDataParameter();
        StringBuilder sb = new StringBuilder();
        sb.append("insertParallelBurstTask: path=");
        sb.append(parallelTaskData.getSavePath());
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        byte[] populateExif = populateExif(parallelTaskData.getJpegImageData(), parallelTaskData);
        byte[] dataOfTheRegionUnderWatermarks = parallelTaskData.getDataOfTheRegionUnderWatermarks();
        int[] coordinatesOfTheRegionUnderWatermarks = parallelTaskData.getCoordinatesOfTheRegionUnderWatermarks();
        int width2 = dataParameter.getPictureSize().getWidth();
        int height2 = dataParameter.getPictureSize().getHeight();
        int orientation2 = ExifHelper.getOrientation(populateExif);
        int jpegRotation = dataParameter.getJpegRotation();
        if (orientation2 == jpegRotation) {
            jpegRotation = -1;
        }
        int i2 = jpegRotation;
        int jpegRotation2 = dataParameter.getJpegRotation();
        Log.d(str, String.format(Locale.ENGLISH, "insertParallelBurstTask: %d x %d, %d : %d", new Object[]{Integer.valueOf(width2), Integer.valueOf(height2), Integer.valueOf(jpegRotation2), Integer.valueOf(orientation2)}));
        if ((jpegRotation2 + orientation2) % 180 == 0) {
            i = height2;
        } else {
            i = width2;
            width2 = height2;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("insertParallelBurstTask: result = ");
        sb3.append(width2);
        sb3.append("x");
        sb3.append(i);
        Log.d(str, sb3.toString());
        String fileTitleFromPath = Util.getFileTitleFromPath(parallelTaskData.getSavePath());
        String str2 = fileTitleFromPath;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("insertParallelBurstTask: ");
        sb4.append(fileTitleFromPath);
        Log.d(str, sb4.toString());
        int i3 = orientation2;
        int i4 = width2;
        int i5 = i;
        reFillSaveRequest(Util.composeMainSubPicture(populateExif, dataOfTheRegionUnderWatermarks, coordinatesOfTheRegionUnderWatermarks, dataParameter.getOutputSize().getWidth(), dataParameter.getOutputSize().getHeight(), dataParameter.isHasDualWaterMark(), dataParameter.isHasFrontWaterMark(), dataParameter.getTimeWaterMarkString(), dataParameter.getJpegRotation(), dataParameter.getDeviceWatermarkParam(), parallelTaskData, i2), parallelTaskData.isNeedThumbnail(), str2, null, parallelTaskData.getDateTakenTime(), null, dataParameter.getLocation(), i4, i5, null, i3, false, false, parallelTaskData.isNeedThumbnail(), false, true, dataParameter.getAlgorithmName(), dataParameter.getPictureInfo(), -1);
    }

    private void parserParallelDualTask(ParallelTaskData parallelTaskData) {
        int i;
        byte[] bArr;
        ParallelTaskData parallelTaskData2 = parallelTaskData;
        ParallelTaskDataParameter dataParameter = parallelTaskData.getDataParameter();
        StringBuilder sb = new StringBuilder();
        sb.append("addParallel: path=");
        sb.append(parallelTaskData.getSavePath());
        Log.d(TAG, sb.toString());
        boolean isMemDebug = parallelTaskData.isMemDebug();
        byte[] populateExif = populateExif(parallelTaskData.getJpegImageData(), parallelTaskData2);
        byte[] dataOfTheRegionUnderWatermarks = parallelTaskData.getDataOfTheRegionUnderWatermarks();
        int[] coordinatesOfTheRegionUnderWatermarks = parallelTaskData.getCoordinatesOfTheRegionUnderWatermarks();
        if (isMemDebug) {
            reFillSaveRequest(populateExif, parallelTaskData.getTimestamp(), parallelTaskData.getDateTakenTime(), dataParameter.getLocation(), dataParameter.getJpegRotation(), parallelTaskData.getSavePath(), dataParameter.getOutputSize().getWidth(), dataParameter.getOutputSize().getHeight(), parallelTaskData.isNeedThumbnail(), dataParameter.getAlgorithmName(), dataParameter.getPictureInfo());
            return;
        }
        int orientation2 = ExifHelper.getOrientation(populateExif);
        int jpegRotation = dataParameter.getJpegRotation();
        int i2 = orientation2 == jpegRotation ? -1 : jpegRotation;
        if ((6 == parallelTaskData.getParallelType() || 11 == parallelTaskData.getParallelType() || 8 == parallelTaskData.getParallelType() || 7 == parallelTaskData.getParallelType() || -6 == parallelTaskData.getParallelType() || -7 == parallelTaskData.getParallelType()) && PortraitDepthMap.isDepthMapData(parallelTaskData.getPortraitDepthData())) {
            bArr = Util.composeDepthMapPicture(populateExif, parallelTaskData.getPortraitDepthData(), parallelTaskData.getPortraitRawData(), dataOfTheRegionUnderWatermarks, coordinatesOfTheRegionUnderWatermarks, i2, dataParameter.getSupportZeroDegreeOrientationImage(), dataParameter.isHasDualWaterMark(), dataParameter.isHasFrontWaterMark(), dataParameter.getLightingPattern(), dataParameter.getTimeWaterMarkString(), dataParameter.getOutputSize().getWidth(), dataParameter.getOutputSize().getHeight(), dataParameter.isMirror(), dataParameter.isBokehFrontCamera(), dataParameter.getJpegRotation(), dataParameter.getDeviceWatermarkParam(), dataParameter.getPictureInfo(), dataParameter.getPortraitLightingVersion(), parallelTaskData.getTimestamp(), dataParameter.getCameraPreferredMode());
        } else if (parallelTaskData.isLiveShotTask()) {
            String microVideoPath = parallelTaskData.getMicroVideoPath();
            byte[] composeLiveShotPicture = Util.composeLiveShotPicture(populateExif, dataParameter.getOutputSize().getWidth(), dataParameter.getOutputSize().getHeight(), microVideoPath, parallelTaskData.getCoverFrameTimestamp(), dataParameter.isHasDualWaterMark(), dataParameter.isHasFrontWaterMark(), dataParameter.getTimeWaterMarkString(), dataParameter.getJpegRotation(), dataParameter.getDeviceWatermarkParam(), dataOfTheRegionUnderWatermarks, coordinatesOfTheRegionUnderWatermarks, i2);
            if (microVideoPath != null && !VideoClipSavingCallback.EMPTY_VIDEO_PATH.equals(microVideoPath) && !Util.keepLiveShotMicroVideoInCache()) {
                Util.deleteFile(microVideoPath);
            }
            bArr = composeLiveShotPicture;
        } else {
            byte[] bArr2 = populateExif;
            i = -7;
            bArr = Util.composeMainSubPicture(bArr2, dataOfTheRegionUnderWatermarks, coordinatesOfTheRegionUnderWatermarks, dataParameter.getOutputSize().getWidth(), dataParameter.getOutputSize().getHeight(), dataParameter.isHasDualWaterMark(), dataParameter.isHasFrontWaterMark(), dataParameter.getTimeWaterMarkString(), dataParameter.getJpegRotation(), dataParameter.getDeviceWatermarkParam(), parallelTaskData, i2);
            if (parallelTaskData.getParallelType() != i || parallelTaskData.getParallelType() == -6 || parallelTaskData.getParallelType() == -5) {
                ExifInterface exif = ExifInterface.getExif(bArr);
                this.orientation = ExifInterface.getOrientation(exif);
                this.width = ExifInterface.getImageWidth(exif);
                parallelTaskData2.refillJpegData(bArr);
            } else {
                reFillSaveRequest(bArr, parallelTaskData.getTimestamp(), parallelTaskData.getDateTakenTime(), dataParameter.getLocation(), dataParameter.getJpegRotation(), parallelTaskData.getSavePath(), dataParameter.getOutputSize().getWidth(), dataParameter.getOutputSize().getHeight(), parallelTaskData.isNeedThumbnail(), dataParameter.getAlgorithmName(), dataParameter.getPictureInfo());
            }
        }
        i = -7;
        if (parallelTaskData.getParallelType() != i) {
        }
        ExifInterface exif2 = ExifInterface.getExif(bArr);
        this.orientation = ExifInterface.getOrientation(exif2);
        this.width = ExifInterface.getImageWidth(exif2);
        parallelTaskData2.refillJpegData(bArr);
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0146  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0149  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parserPreviewShotTask(ParallelTaskData parallelTaskData) {
        PictureInfo pictureInfo;
        String str;
        Location location;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        boolean z;
        byte[] bArr;
        String str2;
        byte[] jpegImageData = parallelTaskData.getJpegImageData();
        ParallelTaskDataParameter dataParameter = parallelTaskData.getDataParameter();
        int filterId = dataParameter.getFilterId();
        boolean isAnchorPreview = dataParameter.isAnchorPreview();
        int jpegRotation = dataParameter.getJpegRotation();
        boolean z2 = filterId != FilterInfo.FILTER_ID_NONE && isAnchorPreview;
        if (dataParameter != null) {
            int width2 = dataParameter.getOutputSize().getWidth();
            int height2 = dataParameter.getOutputSize().getHeight();
            i2 = width2;
            i = height2;
            location = dataParameter.getLocation();
            str = dataParameter.getAlgorithmName();
            pictureInfo = dataParameter.getPictureInfo();
            i3 = dataParameter.getOrientation();
        } else {
            location = null;
            str = null;
            pictureInfo = null;
            i3 = 0;
            i2 = 0;
            i = 0;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("preview orientation is ");
        sb.append(i3);
        sb.append(" jpegorientation ");
        sb.append(jpegRotation);
        sb.append(" anchorPreview ");
        sb.append(isAnchorPreview);
        String sb2 = sb.toString();
        String str3 = TAG;
        Log.d(str3, sb2);
        PerformanceTracker.trackImageSaver(jpegImageData, 0);
        String fileTitleFromPath = Util.getFileTitleFromPath(parallelTaskData.getSavePath());
        if (!z2 || TextUtils.isEmpty(fileTitleFromPath)) {
            i4 = jpegRotation;
            z = isAnchorPreview;
            str2 = str3;
            i5 = i3;
        } else {
            SaverCallback saverCallback = getSaverCallback();
            if (saverCallback != null) {
                SaverCallback saverCallback2 = saverCallback;
                String str4 = str3;
                i5 = i3;
                i4 = jpegRotation;
                String str5 = fileTitleFromPath;
                z = isAnchorPreview;
                DrawJPEGAttribute drawJPEGAttribute = getDrawJPEGAttribute(jpegImageData, dataParameter.getPreviewSize().getWidth(), dataParameter.getPreviewSize().getHeight(), filterId, parallelTaskData.isNeedThumbnail(), i2, i, dataParameter.getLocation(), str5, dataParameter.getShootOrientation(), i5, dataParameter.getShootRotation(), dataParameter.getAlgorithmName(), dataParameter.isHasWaterMark(), dataParameter.isUltraPixelWaterMark(), dataParameter.getTimeWaterMarkString(), dataParameter.isHasDualWaterMark(), dataParameter.isHasFrontWaterMark(), dataParameter.getDeviceWatermarkParam(), dataParameter.getFaceWaterMarkList(), false, dataParameter.getPictureInfo(), parallelTaskData.getCurrentModuleIndex(), parallelTaskData.getPreviewThumbnailHash(), dataParameter.isMiMovieOpen());
                saverCallback2.processorJpegSync(false, drawJPEGAttribute);
                bArr = drawJPEGAttribute.mData;
                str2 = str4;
                Log.d(str2, "reFill preview image");
                reFillSaveRequest(bArr, parallelTaskData.isNeedThumbnail(), parallelTaskData.getSavePath(), parallelTaskData.getDateTakenTime(), location, i2, i, !z ? i4 : i5, true, true, str, pictureInfo);
            }
            i4 = jpegRotation;
            z = isAnchorPreview;
            i5 = i3;
            str2 = str3;
            Log.d(str2, "parserSingleTask(): saverCallback is null");
        }
        bArr = jpegImageData;
        Log.d(str2, "reFill preview image");
        reFillSaveRequest(bArr, parallelTaskData.isNeedThumbnail(), parallelTaskData.getSavePath(), parallelTaskData.getDateTakenTime(), location, i2, i, !z ? i4 : i5, true, true, str, pictureInfo);
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x015f  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x01c4  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x023c  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x02ac  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parserSingleTask(ParallelTaskData parallelTaskData) {
        int i;
        int i2;
        String str;
        int i3;
        String str2;
        int[] iArr;
        byte[] bArr;
        String str3;
        byte[] bArr2;
        int i4;
        int i5;
        int i6;
        int i7;
        byte[] bArr3;
        String str4;
        int i8;
        int i9;
        AbstractSaveRequest abstractSaveRequest;
        ParallelTaskData parallelTaskData2;
        int[] iArr2;
        byte[] bArr4;
        ParallelTaskData parallelTaskData3 = parallelTaskData;
        ParallelTaskDataParameter dataParameter = parallelTaskData.getDataParameter();
        int filterId = dataParameter.getFilterId();
        boolean z = EffectController.getInstance().hasEffect(false) || filterId != FilterInfo.FILTER_ID_NONE;
        byte[] jpegImageData = parallelTaskData.getJpegImageData();
        int width2 = dataParameter.getOutputSize().getWidth();
        int height2 = dataParameter.getOutputSize().getHeight();
        ExifInterface exif = ExifInterface.getExif(jpegImageData);
        int orientation2 = ExifInterface.getOrientation(exif);
        int jpegRotation = dataParameter.getJpegRotation();
        if (parallelTaskData.isAdaptiveSnapshotSize()) {
            int imageWidth = ExifInterface.getImageWidth(exif);
            i = ExifInterface.getImageHeight(exif);
            i2 = imageWidth;
        } else if ((jpegRotation + orientation2) % 180 == 0) {
            i2 = width2;
            i = height2;
        } else {
            i = width2;
            i2 = height2;
        }
        if (parallelTaskData.isShot2Gallery() || parallelTaskData.isSaveToHiddenFolder() || parallelTaskData.isInTimerBurstShotting()) {
            str = Util.getFileTitleFromPath(parallelTaskData.getSavePath());
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(Util.createJpegName(System.currentTimeMillis()));
            sb.append(dataParameter.getSuffix());
            str = sb.toString();
        }
        String str5 = str;
        String str6 = TAG;
        byte[] bArr5 = null;
        if (z) {
            SaverCallback saverCallback = getSaverCallback();
            if (saverCallback != null) {
                String str7 = str6;
                str2 = str5;
                i3 = orientation2;
                SaverCallback saverCallback2 = saverCallback;
                DrawJPEGAttribute drawJPEGAttribute = getDrawJPEGAttribute(jpegImageData, dataParameter.getPreviewSize().getWidth(), dataParameter.getPreviewSize().getHeight(), filterId, parallelTaskData.isNeedThumbnail(), i2, i, dataParameter.getLocation(), str2, dataParameter.getShootOrientation(), i3, dataParameter.getShootRotation(), dataParameter.getAlgorithmName(), dataParameter.isHasWaterMark(), dataParameter.isUltraPixelWaterMark(), dataParameter.getTimeWaterMarkString(), dataParameter.isHasDualWaterMark(), dataParameter.isHasFrontWaterMark(), dataParameter.getDeviceWatermarkParam(), dataParameter.getFaceWaterMarkList(), false, dataParameter.getPictureInfo(), parallelTaskData.getCurrentModuleIndex(), parallelTaskData.getPreviewThumbnailHash(), dataParameter.isMiMovieOpen());
                saverCallback2.processorJpegSync(false, drawJPEGAttribute);
                byte[] bArr6 = drawJPEGAttribute.mData;
                int i10 = drawJPEGAttribute.mWidth;
                int i11 = drawJPEGAttribute.mHeight;
                byte[] bArr7 = drawJPEGAttribute.mDataOfTheRegionUnderWatermarks;
                int[] iArr3 = drawJPEGAttribute.mCoordinatesOfTheRegionUnderWatermarks;
                if (C0124O00000oO.O0o0O00) {
                    iArr3 = null;
                } else {
                    bArr5 = bArr7;
                }
                iArr = iArr3;
                bArr2 = bArr6;
                i5 = i10;
                i4 = i11;
                bArr = bArr5;
                str3 = str7;
                if (parallelTaskData.isLiveShotTask()) {
                    if (bArr == null) {
                        byte[] dataOfTheRegionUnderWatermarks = parallelTaskData.getDataOfTheRegionUnderWatermarks();
                        iArr2 = parallelTaskData.getCoordinatesOfTheRegionUnderWatermarks();
                        bArr4 = dataOfTheRegionUnderWatermarks;
                    } else {
                        bArr4 = bArr;
                        iArr2 = iArr;
                    }
                    byte[] composeMainSubPicture = Util.composeMainSubPicture(bArr2, bArr4, iArr2, dataParameter.getOutputSize().getWidth(), dataParameter.getOutputSize().getHeight(), dataParameter.isHasDualWaterMark(), dataParameter.isHasFrontWaterMark(), dataParameter.getTimeWaterMarkString(), dataParameter.getJpegRotation(), dataParameter.getDeviceWatermarkParam(), parallelTaskData, -1);
                    if (composeMainSubPicture == null || composeMainSubPicture.length < bArr2.length) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Failed to compose main sub photos: ");
                        sb2.append(str2);
                        Log.e(str3, sb2.toString());
                        i6 = i5;
                        i7 = i4;
                        bArr3 = bArr2;
                    } else {
                        bArr3 = composeMainSubPicture;
                        i6 = i5;
                        i7 = i4;
                    }
                    str4 = str3;
                } else {
                    String str8 = str2;
                    String microVideoPath = parallelTaskData.getMicroVideoPath();
                    long coverFrameTimestamp = parallelTaskData.getCoverFrameTimestamp();
                    i6 = i5;
                    i7 = i4;
                    long j = coverFrameTimestamp;
                    byte[] bArr8 = bArr2;
                    boolean isHasDualWaterMark = dataParameter.isHasDualWaterMark();
                    str4 = str3;
                    byte[] composeLiveShotPicture = Util.composeLiveShotPicture(bArr2, width2, height2, microVideoPath, j, isHasDualWaterMark, dataParameter.isHasFrontWaterMark(), dataParameter.getTimeWaterMarkString(), dataParameter.getJpegRotation(), dataParameter.getDeviceWatermarkParam(), bArr, iArr, -1);
                    if (composeLiveShotPicture == null || composeLiveShotPicture.length < bArr8.length) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Failed to compose LiveShot photo: ");
                        sb3.append(str8);
                        Log.e(str4, sb3.toString());
                        composeLiveShotPicture = bArr8;
                    } else {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(dataParameter.getPrefix());
                        sb4.append(str8);
                        str8 = sb4.toString();
                    }
                    if (microVideoPath != null && !VideoClipSavingCallback.EMPTY_VIDEO_PATH.equals(microVideoPath)) {
                        Util.deleteFile(microVideoPath);
                    }
                    str2 = str8;
                    bArr3 = composeLiveShotPicture;
                }
                if (parallelTaskData.getParallelType() != -2) {
                    parallelTaskData2 = parallelTaskData;
                    i8 = i7;
                    i9 = i6;
                    abstractSaveRequest = this;
                } else if (parallelTaskData.getParallelType() == -3) {
                    abstractSaveRequest = this;
                    parallelTaskData2 = parallelTaskData;
                    i8 = i7;
                    i9 = i6;
                } else {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("insertSingleTask: isShot2Gallery = ");
                    sb5.append(parallelTaskData.isShot2Gallery());
                    Log.d(str4, sb5.toString());
                    if (parallelTaskData.isShot2Gallery()) {
                        parallelTaskData.refillJpegData(bArr3);
                        parallelTaskData.getDataParameter().updateOutputSize(i6, i7);
                        parserParallelDualTask(parallelTaskData);
                        return;
                    }
                    ParallelTaskData parallelTaskData4 = parallelTaskData;
                    byte[] bArr9 = bArr3;
                    String str9 = str2;
                    reFillSaveRequest(bArr9, parallelTaskData.isNeedThumbnail(), str9, null, parallelTaskData.getDateTakenTime(), null, dataParameter.getLocation(), i6, i7, null, i3, false, false, true, false, false, dataParameter.getAlgorithmName(), dataParameter.getPictureInfo(), parallelTaskData.getPreviewThumbnailHash());
                    return;
                }
                abstractSaveRequest.width = i9;
                abstractSaveRequest.height = i8;
                abstractSaveRequest.orientation = i3;
                parallelTaskData2.refillJpegData(bArr3);
            }
            str2 = str5;
            i3 = orientation2;
            str3 = str6;
            Log.d(str3, "parserSingleTask(): saverCallback is null");
        } else {
            str2 = str5;
            i3 = orientation2;
            str3 = str6;
        }
        bArr2 = jpegImageData;
        i5 = i2;
        i4 = i;
        bArr = null;
        iArr = null;
        if (parallelTaskData.isLiveShotTask()) {
        }
        if (parallelTaskData.getParallelType() != -2) {
        }
        abstractSaveRequest.width = i9;
        abstractSaveRequest.height = i8;
        abstractSaveRequest.orientation = i3;
        parallelTaskData2.refillJpegData(bArr3);
    }

    private static byte[] populateExif(byte[] bArr, ParallelTaskData parallelTaskData) {
        if (parallelTaskData == null || parallelTaskData.getCaptureResult() == null) {
            return bArr;
        }
        return Util.appendCaptureResultToExif(bArr, parallelTaskData.getDataParameter().getOutputSize().getWidth(), parallelTaskData.getDataParameter().getOutputSize().getHeight(), parallelTaskData.getDataParameter().getJpegRotation(), parallelTaskData.getDateTakenTime(), parallelTaskData.getDataParameter().getLocation(), (CameraMetadataNative) parallelTaskData.getCaptureResult().getResults());
    }

    /* access modifiers changed from: protected */
    public int calculateMemoryUsed() {
        ParallelTaskData parallelTaskData = this.mParallelTaskData;
        int i = 0;
        if (parallelTaskData == null) {
            return 0;
        }
        byte[] jpegImageData = parallelTaskData.getJpegImageData();
        int length = jpegImageData == null ? 0 : jpegImageData.length;
        byte[] portraitRawData = this.mParallelTaskData.getPortraitRawData();
        int length2 = length + (portraitRawData == null ? 0 : portraitRawData.length);
        byte[] portraitDepthData = this.mParallelTaskData.getPortraitDepthData();
        if (portraitDepthData != null) {
            i = portraitDepthData.length;
        }
        return i + length2;
    }

    /* access modifiers changed from: protected */
    public boolean isHeicSavingRequest() {
        ParallelTaskData parallelTaskData = this.mParallelTaskData;
        return (parallelTaskData == null || parallelTaskData.getDataParameter() == null || !CompatibilityUtils.isHeicImageFormat(this.mParallelTaskData.getDataParameter().getOutputFormat())) ? false : true;
    }

    /* access modifiers changed from: protected */
    public void parserParallelTaskData() {
        ParallelTaskData parallelTaskData = this.mParallelTaskData;
        if (parallelTaskData == null) {
            Log.v(TAG, "mParallelTaskData is null, ignore");
            return;
        }
        switch (parallelTaskData.getParallelType()) {
            case -7:
            case -6:
            case -5:
            case 5:
            case 6:
            case 7:
            case 8:
            case 11:
                parserParallelDualTask(this.mParallelTaskData);
                break;
            case -4:
                parserMimojiCaptureTask(this.mParallelTaskData);
                break;
            case -3:
            case -2:
            case 0:
            case 1:
            case 10:
                parserSingleTask(this.mParallelTaskData);
                break;
            case -1:
                parserPreviewShotTask(this.mParallelTaskData);
                break;
            case 2:
                parserNormalDualTask(this.mParallelTaskData);
                break;
            case 9:
                parserParallelBurstTask(this.mParallelTaskData);
                break;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown shot type: ");
                sb.append(this.mParallelTaskData.getParallelType());
                throw new RuntimeException(sb.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void reFillSaveRequest(byte[] bArr, long j, long j2, Location location, int i, String str, int i2, int i3, boolean z, String str2, PictureInfo pictureInfo) {
    }

    /* access modifiers changed from: protected */
    public void reFillSaveRequest(byte[] bArr, boolean z, String str, long j, Location location, int i, int i2, int i3, boolean z2, boolean z3, String str2, PictureInfo pictureInfo) {
    }

    /* access modifiers changed from: protected */
    public void reFillSaveRequest(byte[] bArr, boolean z, String str, String str2, long j, Uri uri, Location location, int i, int i2, ExifInterface exifInterface, int i3, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, String str3, PictureInfo pictureInfo, int i4) {
    }

    public void setParallelTaskData(ParallelTaskData parallelTaskData) {
        this.mParallelTaskData = parallelTaskData;
    }

    public void setSaverCallback(SaverCallback saverCallback) {
        this.mSaverCallbackRef = new WeakReference(saverCallback);
    }
}
