package com.xiaomi.camera.core;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.media.Image;
import android.os.ConditionVariable;
import android.util.Size;
import androidx.annotation.NonNull;
import com.android.camera.CameraAppImpl;
import com.android.camera.Util;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.FilterInfo;
import com.android.camera.effect.draw_mode.DrawYuvAttribute;
import com.android.camera.effect.renders.DeviceWatermarkParam;
import com.android.camera.effect.renders.SnapshotRender;
import com.android.camera.log.Log;
import com.android.camera.storage.Storage;
import com.xiaomi.camera.base.ImageUtil;
import java.io.File;
import java.util.Arrays;

public class FilterProcessor {
    private static final String TAG = "FilterProcessor";
    ConditionVariable mBlockVariable;
    private Size mRenderSize = new Size(0, 0);

    public class YuvAttributeWrapper {
        public DrawYuvAttribute mAttribute;
        public ConditionVariable mBlocker;

        public YuvAttributeWrapper(DrawYuvAttribute drawYuvAttribute, ConditionVariable conditionVariable) {
            this.mAttribute = drawYuvAttribute;
            this.mBlocker = conditionVariable;
        }
    }

    public FilterProcessor(ConditionVariable conditionVariable) {
        this.mBlockVariable = conditionVariable;
    }

    private static DrawYuvAttribute getDrawYuvAttribute(Image image, boolean z, ParallelTaskData parallelTaskData) {
        ParallelTaskDataParameter dataParameter = parallelTaskData.getDataParameter();
        DrawYuvAttribute drawYuvAttribute = new DrawYuvAttribute(image, dataParameter.getPreviewSize(), dataParameter.getPictureSize(), dataParameter.getFilterId(), dataParameter.getOrientation(), dataParameter.getJpegRotation(), dataParameter.getShootRotation(), System.currentTimeMillis(), dataParameter.isMirror(), z, dataParameter.getOutputFormat() == 1212500294, dataParameter.getTiltShiftMode(), dataParameter.getTimeWaterMarkString(), dataParameter.isHasDualWaterMark(), dataParameter.isHasFrontWaterMark(), dataParameter.getDeviceWatermarkParam(), EffectController.getInstance().copyEffectRectAttribute(), dataParameter.getFaceWaterMarkList(), dataParameter.getMajorAIWatermark(), dataParameter.getMinorAIWatermark());
        return drawYuvAttribute;
    }

    private boolean isAIWatermarkEnabled(ParallelTaskData parallelTaskData) {
        ParallelTaskDataParameter dataParameter = parallelTaskData.getDataParameter();
        return (dataParameter.getMajorAIWatermark() == null && dataParameter.getMinorAIWatermark() == null) ? false : true;
    }

    private boolean isWatermarkEnabled(ParallelTaskData parallelTaskData) {
        ParallelTaskDataParameter dataParameter = parallelTaskData.getDataParameter();
        return !dataParameter.getVendorWaterMark() && (dataParameter.isHasDualWaterMark() || dataParameter.isHasFrontWaterMark() || dataParameter.getTimeWaterMarkString() != null || dataParameter.isAgeGenderAndMagicMirrorWater());
    }

    private void prepareEffectProcessor(ParallelTaskDataParameter parallelTaskDataParameter) {
        int filterId = parallelTaskDataParameter.getFilterId();
        Size pictureSize = parallelTaskDataParameter.getPictureSize();
        DeviceWatermarkParam deviceWatermarkParam = parallelTaskDataParameter.getDeviceWatermarkParam();
        if (!this.mRenderSize.equals(pictureSize)) {
            init(pictureSize);
        }
        Log.d(TAG, String.format("prepareEffectProcessor: %x", new Object[]{Integer.valueOf(filterId)}));
        SnapshotRender.getRender().prepareEffectRender(deviceWatermarkParam, filterId);
    }

    private boolean shouldApplyEffect(@NonNull ParallelTaskData parallelTaskData) {
        if (isWatermarkEnabled(parallelTaskData) || isAIWatermarkEnabled(parallelTaskData)) {
            return true;
        }
        ParallelTaskDataParameter dataParameter = parallelTaskData.getDataParameter();
        return (dataParameter == null || (FilterInfo.FILTER_ID_NONE == dataParameter.getFilterId() && dataParameter.getTiltShiftMode() == null && !dataParameter.isCinematicAspectRatio())) ? false : true;
    }

    public Image doFilterSync(@NonNull ParallelTaskData parallelTaskData, @NonNull Image image, int i) {
        ParallelTaskDataParameter dataParameter = parallelTaskData.getDataParameter();
        if (shouldApplyEffect(parallelTaskData)) {
            prepareEffectProcessor(dataParameter);
            boolean z = i == 0 && isWatermarkEnabled(parallelTaskData);
            DrawYuvAttribute drawYuvAttribute = getDrawYuvAttribute(image, z, parallelTaskData);
            drawYuvAttribute.mJpegQuality = dataParameter.getJpegQuality();
            drawYuvAttribute.mOutputSize = dataParameter.getOutputSize();
            YuvAttributeWrapper yuvAttributeWrapper = new YuvAttributeWrapper(drawYuvAttribute, this.mBlockVariable);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("apply filter (id: ");
            sb.append(drawYuvAttribute.mEffectIndex);
            sb.append(") to the captured photo");
            Log.d(str, sb.toString());
            this.mBlockVariable.close();
            SnapshotRender.getRender().processImageSync(yuvAttributeWrapper);
            this.mBlockVariable.block();
            if (!C0124O00000oO.O0o0O00 && drawYuvAttribute.mDataOfTheRegionUnderWatermarks != null) {
                if (Util.WATER_MARK_DUMP) {
                    String savePath = parallelTaskData.getSavePath();
                    String substring = savePath.substring(savePath.lastIndexOf(File.separator));
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("dump_water_mark doFilterSync: path = ");
                    sb2.append(savePath);
                    sb2.append(", name = ");
                    sb2.append(substring);
                    sb2.append(", rect = ");
                    sb2.append(Arrays.toString(drawYuvAttribute.mCoordinatesOfTheRegionUnderWatermarks));
                    Log.d(str2, sb2.toString());
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("_");
                    sb3.append(Arrays.toString(drawYuvAttribute.mCoordinatesOfTheRegionUnderWatermarks));
                    String str3 = Storage.JPEG_SUFFIX;
                    sb3.append(str3);
                    String replace = substring.replace(str3, sb3.toString());
                    byte[] bArr = drawYuvAttribute.mDataOfTheRegionUnderWatermarks;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(CameraAppImpl.getAndroidContext().getExternalCacheDir().getAbsolutePath());
                    sb4.append(replace);
                    Util.saveBlobToFile(bArr, sb4.toString());
                }
                parallelTaskData.setDataOfTheRegionUnderWatermarks(drawYuvAttribute.mDataOfTheRegionUnderWatermarks);
                parallelTaskData.setCoordinatesOfTheRegionUnderWatermarks(drawYuvAttribute.mCoordinatesOfTheRegionUnderWatermarks);
            }
            if (Util.isDumpImageEnabled()) {
                ImageUtil.dumpImage(image, "filter_done");
            }
        }
        return image;
    }

    public void init(Size size) {
        this.mRenderSize = size;
    }
}
