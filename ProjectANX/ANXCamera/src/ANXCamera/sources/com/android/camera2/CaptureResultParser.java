package com.android.camera2;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.annotation.TargetApi;
import android.hardware.camera2.CaptureResult;
import androidx.annotation.NonNull;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera2.vendortag.CaptureResultVendorTags;
import com.android.camera2.vendortag.VendorTagHelper;
import com.android.camera2.vendortag.struct.AECFrameControl;
import com.android.camera2.vendortag.struct.AFFrameControl;
import com.android.camera2.vendortag.struct.AWBFrameControl;
import com.android.camera2.vendortag.struct.MarshalQueryableChiRect.ChiRect;
import com.android.camera2.vendortag.struct.MarshalQueryableDxoAsdScene.ASDScene;
import com.android.camera2.vendortag.struct.MarshalQueryableSuperNightExif;
import com.android.camera2.vendortag.struct.MarshalQueryableSuperNightExif.SuperNightExif;
import java.util.Locale;

@TargetApi(21)
public class CaptureResultParser {
    private static final float AECGAIN_THRESHOLD = 2.0f;
    private static final String TAG = "CaptureResultParser";

    public static AECFrameControl getAECFrameControl(CaptureResult captureResult) {
        return (AECFrameControl) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.AEC_FRAME_CONTROL);
    }

    public static AFFrameControl getAFFrameControl(CaptureResult captureResult) {
        return (AFFrameControl) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.AF_FRAME_CONTROL);
    }

    public static AWBFrameControl getAWBFrameControl(CaptureResult captureResult) {
        return (AWBFrameControl) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.AWB_FRAME_CONTROL);
    }

    public static float getAecLux(CaptureResult captureResult) {
        Float f = (Float) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.AEC_LUX);
        if (f == null) {
            return 0.0f;
        }
        return f.floatValue();
    }

    public static int getAsdDetectedModes(CaptureResult captureResult) {
        Integer num = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.AI_SCENE_DETECTED);
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }

    public static int getBeautyBodySlimCountResult(CaptureResult captureResult) {
        Integer num = (Integer) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.BEAUTY_BODY_SLIM_COUNT);
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }

    public static ASDScene getDxoAsdScene(CameraCapabilities cameraCapabilities, CaptureResult captureResult) {
        if (captureResult == null || cameraCapabilities == null) {
            Log.c(TAG, "getDxoAsdScene, capture result is null, or capability is null.");
            return new ASDScene();
        } else if (!cameraCapabilities.isTagDefined(CaptureResultVendorTags.DXO_ASD_SCENE.getName())) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("getDxoAsdScene, tag not define:");
            sb.append(CaptureResultVendorTags.DXO_ASD_SCENE.getName());
            Log.c(str, sb.toString());
            return new ASDScene();
        } else {
            ASDScene aSDScene = (ASDScene) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.DXO_ASD_SCENE);
            if (aSDScene == null) {
                Log.c(TAG, "getDxoAsdScene, scene is null");
                aSDScene = new ASDScene();
            }
            return aSDScene;
        }
    }

    public static byte[] getExifValues(CaptureResult captureResult) {
        return (byte[]) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.EXIF_INFO_VALUES);
    }

    public static int getFallbackRoleId(CameraCapabilities cameraCapabilities, CaptureResult captureResult) {
        if (captureResult == null) {
            return 0;
        }
        if (cameraCapabilities != null && !cameraCapabilities.isTagDefined(CaptureResultVendorTags.SAT_FALLBACKROLE.getName())) {
            return 0;
        }
        Byte b = (Byte) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.SAT_FALLBACKROLE);
        if (b == null) {
            return 0;
        }
        return b.byteValue();
    }

    public static boolean getFastZoomResult(CaptureResult captureResult) {
        Byte b = (Byte) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.FAST_ZOOM_RESULT);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("FAST_ZOOM_RESULT = ");
        sb.append(b);
        Log.d(str, sb.toString());
        return b != null && b.byteValue() == 1;
    }

    public static int getHHTFrameNumber(CameraCapabilities cameraCapabilities, CaptureResult captureResult) {
        String str;
        String str2;
        if (captureResult == null || cameraCapabilities == null) {
            str = TAG;
            str2 = "getHHTFrameNumber, capture result is null";
        } else if (!cameraCapabilities.isTagDefined(CaptureResultVendorTags.HHT_FRAMENUMBER.getName())) {
            str = TAG;
            str2 = "getHHTFrameNumber, tag not define";
        } else {
            Integer num = (Integer) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.HHT_FRAMENUMBER);
            if (num != null) {
                return num.intValue();
            }
            return 0;
        }
        Log.w(str, str2);
        return 0;
    }

    public static int getHdrCheckerAdrc(CaptureResult captureResult) {
        Integer num = (Integer) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.HDR_CHECKER_ADRC);
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }

    public static int getHdrCheckerSceneType(CaptureResult captureResult) {
        Integer num = (Integer) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.HDR_CHECKER_SCENETYPE);
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }

    public static byte[] getHdrCheckerValues(CaptureResult captureResult) {
        return (byte[]) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.HDR_CHECKER_EV_VALUES);
    }

    public static int getHdrDetectedScene(CaptureResult captureResult) {
        Byte b = (Byte) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.AI_HDR_DETECTED);
        if (b != null) {
            return b.byteValue();
        }
        return 0;
    }

    public static int getHdrMode(CaptureResult captureResult) {
        Integer num = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.HDR_MODE);
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }

    public static int[] getHistogramStats(CaptureResult captureResult) {
        return (int[]) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.HISTOGRAM_STATS);
    }

    public static Integer getRealBV(CaptureResult captureResult) {
        Integer num = (Integer) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.REAL_BV);
        return Integer.valueOf(num == null ? 0 : num.intValue());
    }

    public static byte[] getSatDbgInfo(CaptureResult captureResult) {
        return (byte[]) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.SAT_DBG_INFO);
    }

    public static int getSatMasterCameraId(CaptureResult captureResult) {
        Integer num;
        int i;
        Integer valueOf = Integer.valueOf(2);
        if (captureResult != null) {
            if (C0122O00000o.instance().OO0o00O()) {
                Integer num2 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.SAT_MASTER_PHYSICAL_CAMERA_ID);
                if (num2 != null) {
                    int roleIdByActualId = Camera2DataContainer.getInstance().getRoleIdByActualId(num2.intValue());
                    Log.d(TAG, String.format(Locale.US, "getSatMasterPhysicalCameraId: %d -> role(%d)", new Object[]{num2, Integer.valueOf(roleIdByActualId)}));
                    if (roleIdByActualId == 21) {
                        num = Integer.valueOf(1);
                    } else if (roleIdByActualId == 0) {
                        num = valueOf;
                    } else {
                        if (roleIdByActualId == 20) {
                            i = 3;
                        } else if (roleIdByActualId == 23) {
                            i = 4;
                        } else {
                            num = num2;
                        }
                        num = Integer.valueOf(i);
                    }
                }
            }
            num = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.SAT_MATER_CAMERA_ID);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("getSatMasterCameraId: ");
            sb.append(num);
            Log.d(str, sb.toString());
        } else {
            num = null;
        }
        if (num == null) {
            Log.w(TAG, "getSatMasterCameraId: not found, default to WIDE");
            num = valueOf;
        }
        return num.intValue();
    }

    public static byte[] getSuperNightCheckerEv(CaptureResult captureResult) {
        return (byte[]) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.SUPER_NIGHT_CHECKER_EV);
    }

    public static SuperNightExif getSuperNightInfo(CaptureResult captureResult, boolean z) {
        return MarshalQueryableSuperNightExif.getSuperNightExif(captureResult, z);
    }

    public static int getUltraWideDetectedResult(CaptureResult captureResult) {
        Integer num = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.ULTRA_WIDE_RECOMMENDED_RESULT);
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }

    public static ChiRect getZoomMapRIO(CameraCapabilities cameraCapabilities, CaptureResult captureResult) {
        if (captureResult == null || cameraCapabilities == null) {
            Log.w(TAG, "getZoomMapRIO, capture result is null");
            return new ChiRect(0, 0, 0, 0);
        } else if (!cameraCapabilities.isTagDefined(CaptureResultVendorTags.ZOOM_MAP_RIO.getName())) {
            Log.w(TAG, "getZoomMapRIO, tag not define");
            return new ChiRect(0, 0, 0, 0);
        } else {
            ChiRect chiRect = (ChiRect) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.ZOOM_MAP_RIO);
            if (chiRect == null) {
                chiRect = new ChiRect(0, 0, 0, 0);
            }
            return chiRect;
        }
    }

    public static boolean isASDEnable(CaptureResult captureResult) {
        Byte b = (Byte) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.AI_SCENE_ENABLE);
        if (b == null) {
            return false;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isASDEnable: ");
        sb.append(b);
        Log.d(str, sb.toString());
        return b.byteValue() == 1;
    }

    public static boolean isAishutExistMotion(CaptureResult captureResult) {
        int[] iArr = (int[]) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.AISHUT_EXIST_MOTION);
        return iArr != null && iArr[0] == 1;
    }

    public static boolean isDepthFocus(CaptureResult captureResult, CameraCapabilities cameraCapabilities) {
        if (captureResult == null) {
            return false;
        }
        if (cameraCapabilities != null && !cameraCapabilities.isTagDefined(CaptureResultVendorTags.IS_DEPTH_FOCUS.getName())) {
            return false;
        }
        Integer num = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.IS_DEPTH_FOCUS);
        if (num == null) {
            return false;
        }
        boolean z = true;
        if (num.intValue() != 1) {
            z = false;
        }
        return z;
    }

    public static boolean isFakeSatEnable(CaptureResult captureResult) {
        if (captureResult == null) {
            return false;
        }
        Integer num = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.FAKE_SAT_ENABLE);
        return num != null && num.intValue() == 1;
    }

    public static boolean isHHTDisabled(CameraCapabilities cameraCapabilities, CaptureResult captureResult) {
        String str;
        String str2;
        if (captureResult == null || cameraCapabilities == null) {
            str = TAG;
            str2 = "isHHTDisabled, capture result is null";
        } else if (!cameraCapabilities.isTagDefined(CaptureResultVendorTags.HHT_DISABLED.getName())) {
            str = TAG;
            str2 = "isHHTDisabled, tag not define";
        } else {
            Boolean bool = (Boolean) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.HHT_DISABLED);
            if (bool != null) {
                return bool.booleanValue();
            }
            return false;
        }
        Log.w(str, str2);
        return false;
    }

    public static boolean isHdrMotionDetected(CaptureResult captureResult) {
        Byte b = (Byte) VendorTagHelper.getValue(captureResult, CaptureResultVendorTags.HDR_MOTION_DETECTED);
        return (b == null || b.byteValue() == 0) ? false : true;
    }

    public static boolean isHistogramStatsEnabled(CameraCapabilities cameraCapabilities, CaptureResult captureResult) {
        if (captureResult == null || cameraCapabilities == null || !cameraCapabilities.isTagDefined(CaptureResultVendorTags.HISTOGRAM_STATS_ENABLED.getName())) {
            return false;
        }
        Byte b = (Byte) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.HISTOGRAM_STATS_ENABLED);
        boolean z = true;
        if (b == null || b.byteValue() != 1) {
            z = false;
        }
        return z;
    }

    public static boolean isLLSNeeded(CaptureResult captureResult) {
        Integer num = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.IS_LLS_NEEDED);
        return num != null && num.intValue() == 1;
    }

    public static boolean isLensDirtyDetected(CaptureResult captureResult) {
        Integer num = (Integer) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.LENS_DIRTY_DETECTED);
        return num != null && num.intValue() == 1;
    }

    public static boolean isP2doneReady(CaptureResult captureResult) {
        int[] iArr = (int[]) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.P2_KEY_NOTIFICATION_RESULT);
        return iArr != null && iArr.length > 0 && iArr[0] == 1;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0061  */
    /* JADX WARNING: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isQuadCfaRunning(CaptureResult captureResult) {
        float f;
        boolean OOOOO = C0122O00000o.instance().OOOOO();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isQuadCfaRunning: support=");
        sb.append(OOOOO);
        Log.d(str, sb.toString());
        if (OOOOO) {
            AECFrameControl aECFrameControl = (AECFrameControl) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.AEC_FRAME_CONTROL);
            if (!(aECFrameControl == null || aECFrameControl.getAecExposureDatas() == null || aECFrameControl.getAecExposureDatas().length <= 0)) {
                f = aECFrameControl.getAecExposureDatas()[0].getLinearGain();
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("isQuadCfaRunning: gain=");
                sb2.append(f);
                Log.d(str2, sb2.toString());
                return f >= 2.0f;
            }
        }
        f = 3.0f;
        String str22 = TAG;
        StringBuilder sb22 = new StringBuilder();
        sb22.append("isQuadCfaRunning: gain=");
        sb22.append(f);
        Log.d(str22, sb22.toString());
        if (f >= 2.0f) {
        }
    }

    public static boolean isRemosaicDetected(CaptureResult captureResult) {
        Boolean bool = (Boolean) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.REMOSAIC_DETECTED);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isRemosaicDetected: ");
        sb.append(bool);
        Log.d(str, sb.toString());
        return bool != null && bool.booleanValue();
    }

    public static boolean isSREnable(@NonNull CaptureResult captureResult) {
        Boolean bool = (Boolean) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.IS_SR_ENABLE);
        return bool != null && bool.booleanValue();
    }

    public static boolean isSatFallbackDetected(CaptureResult captureResult) {
        boolean z = false;
        if (captureResult == null) {
            return false;
        }
        Boolean bool = (Boolean) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.SAT_FALLBACK_DETECTED);
        if (bool != null) {
            z = bool.booleanValue();
        }
        return z;
    }

    public static Integer isSpecshotDetected(CaptureResult captureResult) {
        Integer num = (Integer) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.CONTROL_ENABLE_SPECSHOT_DETECTED);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isSpecshotDetected: ");
        sb.append(num);
        Log.d(str, sb.toString());
        return num;
    }
}
