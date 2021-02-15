package com.android.camera2.vendortag;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.graphics.Rect;
import android.hardware.camera2.CaptureResult.Key;
import com.android.camera.log.Log;
import com.android.camera2.vendortag.struct.AECFrameControl;
import com.android.camera2.vendortag.struct.AFFrameControl;
import com.android.camera2.vendortag.struct.AWBFrameControl;
import com.android.camera2.vendortag.struct.MarshalQueryableASDScene;
import com.android.camera2.vendortag.struct.MarshalQueryableChiRect.ChiRect;
import com.android.camera2.vendortag.struct.MarshalQueryableDxoAsdScene.ASDScene;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class CaptureResultVendorTags {
    public static final VendorTag AEC_FRAME_CONTROL = create(C0677O0Oo0OO.INSTANCE, AECFrameControl.class);
    public static final VendorTag AEC_LUX = create(C0692O0OoOo0.INSTANCE, Float.class);
    public static final VendorTag AF_FRAME_CONTROL = create(O0O0OO0.INSTANCE, AFFrameControl.class);
    public static final VendorTag AISHUT_EXIST_MOTION = create(C0663O0OOo0O.INSTANCE, int[].class);
    public static final VendorTag AI_HDR_DETECTED = create(O0Oo00.INSTANCE, Byte.class);
    public static final VendorTag AI_SCENE_DETECTED = create(C0688O0OoOO0.INSTANCE, Integer.class);
    public static final VendorTag AI_SCENE_ENABLE = create(ooooooo.INSTANCE, Byte.class);
    public static final VendorTag AMBILIGHT_AE_EXPOSURE = create(O0O0O0o.INSTANCE, Long.class);
    public static final VendorTag ANCHOR_FRAME_TIMESTAMP = create(C0634O00oooo.INSTANCE, Long.class);
    public static final VendorTag AUTOZOOM_ACTIVE_OBJECTS = create(C0672O0OOooo.INSTANCE, int[].class);
    public static final VendorTag AUTOZOOM_BOUNDS = create(C0682O0Oo0oo.INSTANCE, float[].class);
    public static final VendorTag AUTOZOOM_DELAYED_TARGET_BOUNDS_STABILIZED = create(O0OO0O.INSTANCE, float[].class);
    public static final VendorTag AUTOZOOM_DELAYED_TARGET_BOUNDS_ZOOMED = create(OoO0o.INSTANCE, float[].class);
    public static final VendorTag AUTOZOOM_OBJECT_BOUNDS_STABILIZED = create(C0626O00ooo.INSTANCE, float[].class);
    public static final VendorTag AUTOZOOM_OBJECT_BOUNDS_ZOOMED = create(C0676O0Oo0O0.INSTANCE, float[].class);
    public static final VendorTag AUTOZOOM_PAUSED_OBJECTS = create(C0679O0Oo0o.INSTANCE, int[].class);
    public static final VendorTag AUTOZOOM_SELECTED_OBJECTS = create(O0O0OO.INSTANCE, int[].class);
    public static final VendorTag AUTOZOOM_STATUS = create(C0698O0Ooo0O.INSTANCE, Integer.class);
    public static final VendorTag AUTOZOOM_TARGET_BOUNDS_STABILIZED = create(C0641O0O0OoO.INSTANCE, float[].class);
    public static final VendorTag AUTOZOOM_TARGET_BOUNDS_ZOOMED = create(C0632O00oooOO.INSTANCE, float[].class);
    public static final VendorTag AWB_FRAME_CONTROL = create(O0OOO00.INSTANCE, AWBFrameControl.class);
    public static final VendorTag BEAUTY_BLUSHER = create(C0665O0OOoO.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_BODY_SLIM = create(C0687O0OoOO.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_BODY_SLIM_COUNT = create(O0OOOO.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_CHIN = create(C0635O00oooo0.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_ENLARGE_EYE = create(C0670O0OOoo0.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_EYEBROW_DYE = create(C0662O0OOo00.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_HAIRLINE = create(C0655O0OO0oO.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_HEAD_SLIM = create(C0642O0O0Ooo.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_JELLY_LIPS = create(C0666O0OOoO0.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_LEG_SLIM = create(C0649O0O0oOo.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_LEVEL = create(C0668O0OOoOo.INSTANCE, String.class);
    public static final VendorTag BEAUTY_LIPS = create(C0656O0OO0oo.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_NECK = create(C0646O0O0oO.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_NOSE = create(C0640O0O0Oo0.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_PUPIL_LINE = create(O0O0o00.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_RISORIUS = create(C0631O00oooO0.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_SHOULDER_SLIM = create(O0Oo0.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_SKIN_COLOR = create(C0638O0O00oO.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_SKIN_SMOOTH = create(C0651O0O0oo0.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_SLIM_FACE = create(C0680O0Oo0o0.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_SLIM_NOSE = create(C0660O0OOOoO.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_SMILE = create(C0699O0Ooo0o.INSTANCE, Integer.class);
    public static final VendorTag BUTT_SLIM = create(C0645O0O0o0o.INSTANCE, Integer.class);
    public static final VendorTag CONTROL_ENABLE_REMOSAIC = create(O0O00OO.INSTANCE, Boolean.class);
    public static final VendorTag CONTROL_ENABLE_SPECSHOT_DETECTED = create(C0647O0O0oO0.INSTANCE, Integer.class);
    public static final VendorTag CONTROL_ENABLE_SPECSHOT_MODE = create(C0644O0O0o0O.INSTANCE, Integer.class);
    public static final VendorTag DEPURPLE = create(C0684O0OoO0.INSTANCE, Byte.class);
    public static final VendorTag DISTORTION_FPC_DATA = create(C0629O00ooo0o.INSTANCE, byte[].class);
    public static final VendorTag DXO_ASD_SCENE = create(C0669O0OOoo.INSTANCE, ASDScene.class);
    public static VendorTag EXIF_INFO_VALUES = create(C0654O0OO00o.INSTANCE, byte[].class);
    public static final VendorTag EYE_LIGHT_STRENGTH = create(O0Oo.INSTANCE, Integer.class);
    public static final VendorTag EYE_LIGHT_TYPE = create(O0O00o.INSTANCE, Integer.class);
    public static final VendorTag FAKE_SAT_ENABLE = create(C0661O0OOOoo.INSTANCE, Integer.class);
    public static final VendorTag FAST_ZOOM_RESULT = create(C0691O0OoOo.INSTANCE, Byte.class);
    public static final VendorTag FRONT_SINGLE_CAMERA_BOKEH = create(C0685O0OoO00.INSTANCE, Boolean.class);
    public static final VendorTag HDR_CHECKER_ADRC = create(C0674O0Oo00o.INSTANCE, Integer.class);
    public static final VendorTag HDR_CHECKER_EV_VALUES = create(C0652O0O0ooO.INSTANCE, byte[].class);
    public static final VendorTag HDR_CHECKER_SCENETYPE = create(O0O0o0.INSTANCE, Integer.class);
    public static final VendorTag HDR_MODE = create(C0650O0O0oo.INSTANCE, Integer.class);
    public static final VendorTag HDR_MOTION_DETECTED = create(C0657O0OOO0o.INSTANCE, Byte.class);
    public static final VendorTag HHT_DISABLED = create(C0675O0Oo0O.INSTANCE, Boolean.class);
    public static final VendorTag HHT_FRAMENUMBER = create(O0OO0o0.INSTANCE, Integer.class);
    public static final VendorTag HISTOGRAM_STATS = create(C0630O00oooO.INSTANCE, int[].class);
    public static final VendorTag HISTOGRAM_STATS_ENABLED = create(C0678O0Oo0Oo.INSTANCE, Byte.class);
    public static final Key ISO_VALUE = new Key("xiaomi.algoup.iso_value", Integer.TYPE);
    public static final VendorTag IS_DEPTH_FOCUS = create(OO0oO.INSTANCE, Integer.class);
    public static final VendorTag IS_HDR_ENABLE = create(C0701o00o00O.INSTANCE, Boolean.class);
    public static final VendorTag IS_LLS_NEEDED = create(O0O0O.INSTANCE, Integer.class);
    public static final VendorTag IS_SR_ENABLE = create(C0659O0OOOo.INSTANCE, Boolean.class);
    public static final VendorTag LENS_DIRTY_DETECTED = create(C0664O0OOo0o.INSTANCE, Integer.class);
    public static final VendorTag MFNR_ENABLED = create(C0686O0OoO0O.INSTANCE, Boolean.class);
    public static final VendorTag MI_STATISTICS_FACE_RECTANGLES = create(C0681O0Oo0oO.INSTANCE, Rect[].class);
    public static final VendorTag MTK_AISHUT_EXPOSURE_TIME = create(O0OOo.INSTANCE, Integer.class);
    public static final VendorTag MTK_AISHUT_ISO = create(C0693O0OoOoO.INSTANCE, Integer.class);
    public static final VendorTag NON_SEMANTIC_SCENE = create(C0653O0O0ooo.INSTANCE, MarshalQueryableASDScene.ASDScene[].class);
    public static final VendorTag P2_KEY_NOTIFICATION_RESULT = create(C0633O00oooOo.INSTANCE, int[].class);
    public static final VendorTag POST_PROCESS_CROP_REGION = create(O0OOOo0.INSTANCE, Rect.class);
    public static final VendorTag REAL_BV = create(C0689O0OoOOO.INSTANCE, Integer.class);
    public static final VendorTag REAR_BOKEH_ENABLE = create(OooOO.INSTANCE, Boolean.class);
    public static final VendorTag REMOSAIC_DETECTED = create(C0694O0OoOoo.INSTANCE, Boolean.class);
    public static final VendorTag SAT_DBG_INFO = create(C0636O00ooooo.INSTANCE, byte[].class);
    public static final VendorTag SAT_FALLBACKROLE = create(O0OOO0O.INSTANCE, Byte.class);
    public static final VendorTag SAT_FALLBACK_DETECTED = create(C0658O0OOOOo.INSTANCE, Boolean.class);
    public static final VendorTag SAT_FUSION_SHOT_PIPELINE_READY = create(O0O00o0.INSTANCE, Byte.class);
    public static final VendorTag SAT_MASTER_PHYSICAL_CAMERA_ID = create(C0673O0Oo00O.INSTANCE, Integer.class);
    public static final VendorTag SAT_MATER_CAMERA_ID = create(C0695O0Ooo.INSTANCE, Integer.class);
    public static final VendorTag SCENE_DETECTION_RESULT = create(O0OOO0.INSTANCE, Integer.class);
    public static final VendorTag SEMANTIC_SCENE = create(C0667O0OOoOO.INSTANCE, MarshalQueryableASDScene.ASDScene[].class);
    public static final VendorTag SENSOR_HDR_ENABLE = create(O0OO0Oo.INSTANCE, Byte.class);
    public static final int SPECSHOT_MODE_AINR = 1;
    public static final int SPECSHOT_MODE_AISAINR = 2;
    public static final VendorTag STATE_SCENE = create(C0648O0O0oOO.INSTANCE, MarshalQueryableASDScene.ASDScene[].class);
    public static final VendorTag STATISTICS_FACE_AGE = create(O0O0OOO.INSTANCE, float[].class);
    public static final VendorTag STATISTICS_FACE_FACESCORE = create(C0643O0O0o.INSTANCE, float[].class);
    public static final VendorTag STATISTICS_FACE_GENDER = create(C0696O0Ooo0.INSTANCE, float[].class);
    public static final VendorTag STATISTICS_FACE_INFO = create(C0683O0OoO.INSTANCE, byte[].class);
    public static final VendorTag STATISTICS_FACE_PROP = create(C0697O0Ooo00.INSTANCE, float[].class);
    public static final VendorTag SUPER_NIGHT_CHECKER_EV = create(C0639O0O0OOo.INSTANCE, byte[].class);
    public static final VendorTag SUPER_NIGHT_EXIF = create(O0OO00O.INSTANCE, byte[].class);
    public static final VendorTag SUPER_NIGHT_SCENE_ENABLED = create(C0671O0OOooO.INSTANCE, Boolean.class);
    public static final VendorTag SW_MFNR_ENABLED = create(C0700O0Oooo.INSTANCE, Boolean.class);
    private static final String TAG = "CaptureResultVendorTags";
    public static final VendorTag ULTRA_WIDE_LENS_DISTORTION_CORRECTION_LEVEL = create(O0OOOOO.INSTANCE, Byte.class);
    public static final VendorTag ULTRA_WIDE_RECOMMENDED_RESULT = create(C0690O0OoOOo.INSTANCE, Integer.class);
    public static final int VALUE_SAT_MATER_CAMERA_ID_TELE = 3;
    public static final int VALUE_SAT_MATER_CAMERA_ID_ULTRA_WIDE = 1;
    public static final int VALUE_SAT_MATER_CAMERA_ID_WIDE = 2;
    public static final int VALUE_VIDEO_RECORD_STATE_IDLE = 2;
    public static final int VALUE_VIDEO_RECORD_STATE_PROCESS = 1;
    public static final VendorTag VIDEO_RECORD_STATE = create(O0O000o.INSTANCE, Integer.class);
    public static final VendorTag WHOLE_BODY_SLIM = create(O0Oo000.INSTANCE, Integer.class);
    public static final VendorTag ZOOM_MAP_RIO = create(C0637O0O00Oo.INSTANCE, ChiRect.class);
    private static Constructor resultConstructor;

    static /* synthetic */ String O00oooo() {
        return "com.vidhance.autozoom.target_bounds_stabilized";
    }

    static /* synthetic */ String O00oooo0() {
        return "com.vidhance.autozoom.bounds";
    }

    static /* synthetic */ String O00ooooo() {
        return "com.vidhance.autozoom.delayed_target_bounds_zoomed";
    }

    static /* synthetic */ String O0O000o() {
        return "xiaomi.beauty.slimFaceRatio";
    }

    static /* synthetic */ String O0O00OO() {
        return "xiaomi.beauty.skinSmoothRatio";
    }

    static /* synthetic */ String O0O00Oo() {
        return "xiaomi.beauty.enlargeEyeRatio";
    }

    static /* synthetic */ String O0O00o() {
        return "xiaomi.beauty.risoriusRatio";
    }

    static /* synthetic */ String O0O00o0() {
        return "xiaomi.beauty.noseRatio";
    }

    static /* synthetic */ String O0O00oO() {
        return "xiaomi.beauty.skinColorRatio";
    }

    static /* synthetic */ String O0O0O() {
        return "xiaomi.beauty.chinRatio";
    }

    static /* synthetic */ String O0O0O0o() {
        return "xiaomi.beauty.lipsRatio";
    }

    static /* synthetic */ String O0O0OO() {
        return "xiaomi.beauty.neckRatio";
    }

    static /* synthetic */ String O0O0OO0() {
        return "com.vidhance.autozoom.target_bounds_zoomed";
    }

    static /* synthetic */ String O0O0OOO() {
        return "xiaomi.beauty.smileRatio";
    }

    static /* synthetic */ String O0O0OOo() {
        return "xiaomi.beauty.slimNoseRatio";
    }

    static /* synthetic */ String O0O0Oo0() {
        return "xiaomi.beauty.hairlineRatio";
    }

    static /* synthetic */ String O0O0OoO() {
        return "xiaomi.beauty.eyeBrowDyeRatio";
    }

    static /* synthetic */ String O0O0Ooo() {
        return "xiaomi.beauty.pupilLineRatio";
    }

    static /* synthetic */ String O0O0o() {
        return "com.vidhance.autozoom.status";
    }

    static /* synthetic */ String O0O0o0() {
        return "xiaomi.beauty.blushRatio";
    }

    static /* synthetic */ String O0O0o00() {
        return "xiaomi.beauty.lipGlossRatio";
    }

    static /* synthetic */ String O0O0o0O() {
        return "xiaomi.beauty.eyeLightType";
    }

    static /* synthetic */ String O0O0o0o() {
        return "xiaomi.beauty.eyeLightStrength";
    }

    static /* synthetic */ String O0O0oO() {
        return "xiaomi.beauty.headSlimRatio";
    }

    static /* synthetic */ String O0O0oO0() {
        return "xiaomi.supernight.enabled";
    }

    static /* synthetic */ String O0O0oOO() {
        return "xiaomi.beauty.bodySlimRatio";
    }

    static /* synthetic */ String O0O0oOo() {
        return "xiaomi.beauty.shoulderSlimRatio";
    }

    static /* synthetic */ String O0O0oo() {
        return "xiaomi.beauty.oneKeySlimRatio";
    }

    static /* synthetic */ String O0O0oo0() {
        return "xiaomi.beauty.legSlimRatio";
    }

    static /* synthetic */ String O0O0ooO() {
        return "xiaomi.beauty.buttPlumpSlimRatio";
    }

    static /* synthetic */ String O0O0ooo() {
        return "xiaomi.bokeh.enabled";
    }

    static /* synthetic */ String O0OO00O() {
        return "com.vidhance.autozoom.active_objects";
    }

    static /* synthetic */ String O0OO00o() {
        return "xiaomi.specshot.mode.enabled";
    }

    static /* synthetic */ String O0OO0O() {
        return "xiaomi.distortion.ultraWideDistortionLevel";
    }

    static /* synthetic */ String O0OO0Oo() {
        return "xiaomi.bokehrear.enabled";
    }

    static /* synthetic */ String O0OO0o0() {
        return "xiaomi.mfnr.enabled";
    }

    static /* synthetic */ String O0OO0oO() {
        return "xiaomi.swmf.enabled";
    }

    static /* synthetic */ String O0OO0oo() {
        return "xiaomi.hht.frameNumber";
    }

    static /* synthetic */ String O0OOO0() {
        return "com.vidhance.autozoom.selected_objects";
    }

    static /* synthetic */ String O0OOO00() {
        return "xiaomi.hht.disabled";
    }

    static /* synthetic */ String O0OOO0O() {
        return "xiaomi.faceAnalyzeResult.age";
    }

    static /* synthetic */ String O0OOO0o() {
        return "xiaomi.faceAnalyzeResult.gender";
    }

    static /* synthetic */ String O0OOOO() {
        return "xiaomi.faceAnalyzeResult.prop";
    }

    static /* synthetic */ String O0OOOOO() {
        return "org.quic.camera2.statsconfigs.AECIsInsensorHDR";
    }

    static /* synthetic */ String O0OOOOo() {
        return "xiaomi.scene.result";
    }

    static /* synthetic */ String O0OOOo() {
        return "xiaomi.sat.real.bv";
    }

    static /* synthetic */ String O0OOOo0() {
        return C0124O00000oO.isMTKPlatform() ? "com.xiaomi.statsconfigs.AecLux" : "com.qti.chi.statsaec.AecLux";
    }

    static /* synthetic */ String O0OOOoO() {
        return "xiaomi.ai.asd.enabled";
    }

    static /* synthetic */ String O0OOOoo() {
        return "xiaomi.ai.asd.sceneDetected";
    }

    static /* synthetic */ String O0OOo() {
        return C0124O00000oO.isMTKPlatform() ? "xiaomi.camera.awb.colorTemperature" : "org.quic.camera2.statsconfigs.AWBFrameControl";
    }

    static /* synthetic */ String O0OOo00() {
        return "com.vidhance.autozoom.paused_objects";
    }

    static /* synthetic */ String O0OOo0O() {
        return "xiaomi.hdr.hdrDetected";
    }

    static /* synthetic */ String O0OOo0o() {
        return "xiaomi.ai.add.lensDirtyDetected";
    }

    static /* synthetic */ String O0OOoO() {
        return "xiaomi.faceAnalyzeResult.result";
    }

    static /* synthetic */ String O0OOoO0() {
        return "org.quic.camera2.statsconfigs.AECFrameControl";
    }

    static /* synthetic */ String O0OOoOO() {
        return "org.quic.camera2.statsconfigs.AFFrameControl";
    }

    static /* synthetic */ String O0OOoOo() {
        return C0124O00000oO.isMTKPlatform() ? "xiaomi.histogram.stats" : "org.codeaurora.qcamera3.histogram.stats";
    }

    static /* synthetic */ String O0OOoo() {
        return "xiaomi.video.recordState";
    }

    static /* synthetic */ String O0OOoo0() {
        return "xiaomi.aishutter.existmotion";
    }

    static /* synthetic */ String O0OOooO() {
        return "com.mediatek.control.capture.next.ready";
    }

    static /* synthetic */ String O0OOooo() {
        return "org.quic.camera.isDepthFocus.isDepthFocus";
    }

    static /* synthetic */ String O0Oo() {
        return "xiaomi.pro.video.histogram.stats.enabled";
    }

    static /* synthetic */ String O0Oo0() {
        return "xiaomi.hdr.hdrChecker.adrc";
    }

    static /* synthetic */ String O0Oo00() {
        return "com.vidhance.autozoom.object_bounds_stabilized";
    }

    static /* synthetic */ String O0Oo000() {
        return "xiaomi.smoothTransition.result";
    }

    static /* synthetic */ String O0Oo00O() {
        return "xiaomi.hdr.hdrChecker";
    }

    static /* synthetic */ String O0Oo00o() {
        return "xiaomi.hdr.hdrChecker.sceneType";
    }

    static /* synthetic */ String O0Oo0O() {
        return "xiaomi.ai.misd.ultraWideRecommended";
    }

    static /* synthetic */ String O0Oo0O0() {
        return "xiaomi.hdr.hdrMode";
    }

    static /* synthetic */ String O0Oo0OO() {
        return "xiaomi.beauty.bodySlimCnt";
    }

    static /* synthetic */ String O0Oo0Oo() {
        return "xiaomi.superResolution.enabled";
    }

    static /* synthetic */ String O0Oo0o() {
        return "xiaomi.superResolution.cropRegionMtk";
    }

    static /* synthetic */ String O0Oo0o0() {
        return "xiaomi.depurple.enabled";
    }

    static /* synthetic */ String O0Oo0oO() {
        return "xiaomi.hdr.enabled";
    }

    static /* synthetic */ String O0Oo0oo() {
        return "com.vidhance.autozoom.object_bounds_zoomed";
    }

    static /* synthetic */ String O0OoO() {
        return "xiaomi.smoothTransition.masterCameraId";
    }

    static /* synthetic */ String O0OoO0() {
        return "xiaomi.ai.misd.NonSemanticScene";
    }

    static /* synthetic */ String O0OoO00() {
        return "xiaomi.ai.misd.SemanticScene";
    }

    static /* synthetic */ String O0OoO0O() {
        return "xiaomi.ai.misd.StateScene";
    }

    static /* synthetic */ String O0OoOO() {
        return "xiaomi.specshot.mode.detected";
    }

    static /* synthetic */ String O0OoOO0() {
        return "xiaomi.smoothTransition.physicalCameraId";
    }

    static /* synthetic */ String O0OoOOO() {
        return "xiaomi.FakeSat.enabled";
    }

    static /* synthetic */ String O0OoOOo() {
        return "com.vidhance.autozoom.delayed_target_bounds_stabilized";
    }

    static /* synthetic */ String O0OoOo() {
        return "xiaomi.remosaic.enabled";
    }

    static /* synthetic */ String O0OoOo0() {
        return "xiaomi.mfnr.anchorTimeStamp";
    }

    static /* synthetic */ String O0OoOoO() {
        return "xiaomi.super.night.exposure";
    }

    static /* synthetic */ String O0OoOoo() {
        return "com.mediatek.3afeature.aishutExposuretime";
    }

    static /* synthetic */ String O0Ooo00() {
        return "com.mediatek.3afeature.aishutISO";
    }

    static /* synthetic */ String O0Oooo() {
        return "xiaomi.debugInfo.info";
    }

    static /* synthetic */ String O0oOO() {
        return "xiaomi.sat.dbg.satDbgInfo";
    }

    static /* synthetic */ String O0oOO0o() {
        return "xiaomi.smoothTransition.detected";
    }

    static /* synthetic */ String O0oOOO0() {
        return "xiaomi.ai.misd.hdrmotionDetected";
    }

    static /* synthetic */ String O0oOOOo() {
        return "xiaomi.ai.misd.SuperNightExif";
    }

    static /* synthetic */ String O0oOOo() {
        return "xiaomi.smoothTransition.fallbackRole";
    }

    static /* synthetic */ String O0oOOo0() {
        return "com.qti.stats_control.is_lls_needed";
    }

    static /* synthetic */ String O0oOOoO() {
        return "xiaomi.smoothTransition.mapROI";
    }

    static /* synthetic */ String O0oOOoo() {
        return "xiaomi.statistics.faceRectangles";
    }

    static /* synthetic */ String O0oOo00() {
        return "xiaomi.capturefusion.isPipelineReady";
    }

    static /* synthetic */ String OO0oO() {
        return "xiaomi.faceAnalyzeResult.score";
    }

    static /* synthetic */ String Oo0OOo() {
        return "xiaomi.supernight.checker";
    }

    static /* synthetic */ String OoO0o() {
        return "xiaomi.distortion.distortioFpcData";
    }

    static /* synthetic */ String OooOO() {
        return "xiaomi.ai.asd.sceneDetectedExt";
    }

    private static VendorTag create(final Supplier supplier, final Class cls) {
        return new VendorTag() {
            /* access modifiers changed from: protected */
            public Key create() {
                return CaptureResultVendorTags.resultKey(getName(), cls);
            }

            public String getName() {
                return (String) supplier.get();
            }
        };
    }

    static /* synthetic */ String o00o00O() {
        return "xiaomi.remosaic.detected";
    }

    static /* synthetic */ String ooooooo() {
        return "xiaomi.beauty.beautyLevelApplied";
    }

    public static void preload() {
        Log.d(TAG, "preloading...");
    }

    /* access modifiers changed from: private */
    public static Key resultKey(String str, Class cls) {
        try {
            if (resultConstructor == null) {
                resultConstructor = Key.class.getConstructor(new Class[]{String.class, cls.getClass()});
                resultConstructor.setAccessible(true);
            }
            return (Key) resultConstructor.newInstance(new Object[]{str, cls});
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot find/call Key constructor: ");
            sb.append(e.getMessage());
            Log.d(TAG, sb.toString());
            return null;
        }
    }
}
