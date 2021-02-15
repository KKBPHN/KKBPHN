package com.android.camera;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.text.TextUtils;
import com.android.camera.log.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.WeakHashMap;

public class CameraIntentManager {
    public static final String ACTION_EDIT_DOCOCUMENT_IMAGE = "com.miui.extraphoto.action.EDIT_DOCUMENT_PHOTO";
    public static final String ACTION_EDIT_IDCARD_IMAGE = "com.miui.extraphoto.action.EDIT_IDCARD_PHOTO";
    public static final String ACTION_IDPHOTO_IMAGE = "com.android.camera.action.IDPHOTO";
    public static final String ACTION_QR_CODE_CAPTURE = "com.android.camera.action.QR_CODE_CAPTURE";
    public static final String ACTION_QR_CODE_ZXING = "com.google.zxing.client.android.SCAN";
    public static final String ACTION_SPEECH_SHUTTER = "com.android.camera.action.SPEECH_SHUTTER";
    public static final String ACTION_VOICE_CONTROL = "android.media.action.VOICE_COMMAND";
    private static final String CALLER_AMAZON = "com.amazon.dee.app";
    private static final String CALLER_CTS = "android.providerui.cts";
    private static final String CALLER_FROM_HOME = "com.miui.home";
    private static final String CALLER_GOOGLE_ASSISTANT = "com.google.android.googlequicksearchbox";
    public static final String CALLER_MIUI_CAMERA = "com.android.camera";
    private static final String CALLER_XIAO_AI = "com.miui.voiceassist";
    private static final String CALLER_XIAO_AI_DEBUG_UTIL = "com.xiaomi.voiceassistant";
    private static final String CALLER_XIAO_SHOP = "com.xiaomi.shop";
    public static final String DOCUMENT_IMAGE_EFFECT = "com.miui.extraphoto.extra.DOCUMENT_PHOTO_EFFECT";
    public static final String EXTRAS_CAMERA_FACING = "android.intent.extras.CAMERA_FACING";
    public static final String EXTRAS_CAMERA_PORTRAIT = "android.intent.extras.PORTRAIT";
    private static final String EXTRAS_QUICK_CAPTURE = "android.intent.extra.quickCapture";
    public static final String EXTRAS_SCREEN_SLIDE = "android.intent.extras.SCREEN_SLIDE";
    public static final String EXTRA_ASSISTANT_HASH = "com.android.camera.ASSISTANT_HASH";
    public static final String EXTRA_CTA_WEBVIEW_LINK = "cta_url";
    public static final String EXTRA_FROM_VOICE_ROOT = "com.android.camera.FROM_VOICE_ROOT";
    public static final String EXTRA_LAUNCH_SOURCE = "com.android.systemui.camera_launch_source";
    public static final String EXTRA_LAUNCH_SOURCE_DOUBLE_POWER = "power_double_tap";
    public static final String EXTRA_LAUNCH_SOURCE_DOUBLE_VOLUME = "double_click_volume_down";
    public static final String EXTRA_LAUNCH_SOURCE_KEY_SHORT = "key_short";
    public static final String EXTRA_LAUNCH_SOURCE_LOCKSCREEN_AFFORDANCE = "lockscreen_affordance";
    public static final String EXTRA_LAUNCH_SOURCE_MIWATCH = "miwatch";
    public static final String EXTRA_LAUNCH_SOURCE_STABILIZER = "stabilizer";
    public static final String EXTRA_SHOW_WHEN_LOCKED = "ShowCameraWhenLocked";
    private static final String TAG = "CameraIntentManager";
    public static final int TIMER_DURATION_NONE = -1;
    private static WeakHashMap sMap = new WeakHashMap();
    private Intent mIntent;
    private Uri mReferer;

    @Retention(RetentionPolicy.SOURCE)
    public @interface BroadcastControlExtras {
        public static final String EXTRAS_VOICE_CONTROL_ACTION = "android.intent.extras.VOICE_CONTROL_ACTION";
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CameraExtras {
        public static final String CAMERA_FILTER_MODE = "android.intent.extra.CAMERA_FILTER_MODE";
        public static final String CAMERA_FLASH_MODE = "android.intent.extra.CAMERA_FLASH_MODE";
        public static final String CAMERA_MODE = "android.intent.extra.CAMERA_MODE";
        public static final String CAMERA_OPEN_ONLY = "android.intent.extra.CAMERA_OPEN_ONLY";
        public static final String CAMERA_SUB_MODE = "android.intent.extra.CAMERA_SUB_MODE";
        public static final String EXTRAS_CAMERA_HDR_MODE = "android.intent.extra.CAMERA_HDR_MODE";
        public static final String EXTRAS_NO_UI_QUERY = "NoUiQuery";
        public static final String TIMER_DURATION_SECONDS = "android.intent.extra.TIMER_DURATION_SECONDS";
        public static final String USE_FRONT_CAMERA = "android.intent.extra.USE_FRONT_CAMERA";
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CameraMode {
        public static final String CAPTURE = "CAPTURE";
        public static final String DOC = "DOC";
        public static final String FAST_MOTION = "FAST_MOTION";
        public static final String FUN_AR = "FUN_AR";
        public static final String FUN_AR2 = "FUN_AR2";
        public static final String GOOGLE_MANUAL = "MANUAL_MODE";
        public static final String GOOGLE_PANORAMA = "PANORAMIC";
        public static final String MANUAL = "MANUAL";
        public static final String PANORAMA = "PANORAMA";
        public static final String PORTRAIT = "PORTRAIT";
        public static final String SHORT_VIDEO = "SHORT_VIDEO";
        public static final String SLOW_MOTION = "SLOW_MOTION";
        public static final String SQUARE = "SQUARE";
        public static final String SUPER_NIGHT = "SUPER_NIGHT";
        public static final String ULTRA_PIXEL = "ULTRA_PIXEL";
        public static final String UNSPECIFIED = "UNSPECIFIED";
        public static final String VIDEO = "VIDEO";
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ControlActions {
        public static final String CONTROL_ACTION_CAPTURE = "CAPTURE";
        public static final String CONTROL_ACTION_QUERY_CAMERA_STATUS = "QUERY_CAMERA_STATUS";
        public static final String CONTROL_ACTION_UNKNOWN = "NONE";
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FlashMode {
        public static final String AUTO = "auto";
        public static final String OFF = "off";
        public static final String ON = "on";
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface HDRMode {
        public static final String AUTO = "auto";
        public static final String OFF = "off";
        public static final String ON = "on";
    }

    private CameraIntentManager(Intent intent) {
        this.mIntent = intent;
        this.mReferer = parseReferer(intent);
    }

    public static CameraIntentManager getInstance(Intent intent) {
        CameraIntentManager cameraIntentManager = (CameraIntentManager) sMap.get(intent);
        if (cameraIntentManager != null) {
            return cameraIntentManager;
        }
        CameraIntentManager cameraIntentManager2 = new CameraIntentManager(intent);
        sMap.put(intent, cameraIntentManager2);
        Log.dumpIntent(TAG, intent);
        return cameraIntentManager2;
    }

    private static boolean isBackCameraIntent(int i) {
        return i == 0;
    }

    private boolean isFromVoice(Activity activity) {
        if (KeyKeeper.getInstance().getAssistantHash() == this.mIntent.getIntExtra(EXTRA_ASSISTANT_HASH, -1)) {
            return this.mIntent.getBooleanExtra(EXTRA_FROM_VOICE_ROOT, false);
        }
        return false;
    }

    private static boolean isFrontCameraIntent(int i) {
        return i == 1;
    }

    public static boolean isLaunchByMiWatch(Intent intent) {
        String stringExtra = intent == null ? null : intent.getStringExtra(EXTRA_LAUNCH_SOURCE);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("camera_launch_source = ");
        sb.append(stringExtra);
        Log.d(str, sb.toString());
        return EXTRA_LAUNCH_SOURCE_MIWATCH.equalsIgnoreCase(stringExtra);
    }

    private Uri parseReferer(Intent intent) {
        if (intent == null) {
            return null;
        }
        try {
            Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.REFERRER");
            if (uri != null) {
                return uri;
            }
            String stringExtra = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
            if (stringExtra != null) {
                return Uri.parse(stringExtra);
            }
            return null;
        } catch (BadParcelableException unused) {
            Log.w(TAG, "Cannot read referrer from intent; intent extras contain unknown custom Parcelable objects");
        }
    }

    public static void removeAllInstance() {
        sMap.clear();
    }

    public static void removeInstance(Intent intent) {
        sMap.remove(intent);
    }

    public boolean checkCallerLegality() {
        String caller = getCaller();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("The caller:");
        sb.append(caller);
        Log.d(str, sb.toString());
        if (caller == null) {
            return false;
        }
        char c = 65535;
        switch (caller.hashCode()) {
            case -1958346218:
                if (caller.equals(CALLER_GOOGLE_ASSISTANT)) {
                    c = 0;
                    break;
                }
                break;
            case -664904870:
                if (caller.equals(CALLER_AMAZON)) {
                    c = 5;
                    break;
                }
                break;
            case 249834385:
                if (caller.equals(CALLER_MIUI_CAMERA)) {
                    c = 6;
                    break;
                }
                break;
            case 277863260:
                if (caller.equals(CALLER_XIAO_SHOP)) {
                    c = 4;
                    break;
                }
                break;
            case 881132242:
                if (caller.equals(CALLER_XIAO_AI_DEBUG_UTIL)) {
                    c = 1;
                    break;
                }
                break;
            case 1535655722:
                if (caller.equals(CALLER_XIAO_AI)) {
                    c = 2;
                    break;
                }
                break;
            case 2095214256:
                if (caller.equals(CALLER_FROM_HOME)) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return true;
            default:
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("checkCallerLegality: Unknown caller: ");
                sb2.append(caller);
                Log.e(str2, sb2.toString());
                return false;
        }
    }

    public boolean checkIntentLocationPermission(Activity activity) {
        if (!isImageCaptureIntent() && !isVideoCaptureIntent()) {
            return true;
        }
        String callingPackage = activity.getCallingPackage();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("intent calling package is ");
        sb.append(callingPackage);
        Log.d(str, sb.toString());
        boolean z = false;
        if (TextUtils.isEmpty(callingPackage)) {
            return false;
        }
        if (CameraAppImpl.getAndroidContext().getPackageManager().checkPermission("android.permission.ACCESS_FINE_LOCATION", callingPackage) == 0) {
            z = true;
        }
        return z;
    }

    public void destroy() {
        sMap.remove(this.mIntent);
        this.mReferer = null;
    }

    public String getCaller() {
        if (this.mReferer == null) {
            this.mReferer = parseReferer(this.mIntent);
        }
        Uri uri = this.mReferer;
        if (uri == null) {
            return null;
        }
        return uri.getHost();
    }

    public int getCameraFacing() {
        return this.mIntent.getIntExtra(EXTRAS_CAMERA_FACING, -1);
    }

    public String getCameraMode() {
        Intent intent = this.mIntent;
        String str = CameraExtras.CAMERA_MODE;
        boolean hasExtra = intent.hasExtra(str);
        String str2 = CameraMode.UNSPECIFIED;
        if (!hasExtra) {
            if ("android.media.action.VIDEO_CAMERA".equals(this.mIntent.getAction())) {
                return CameraMode.VIDEO;
            }
            return "android.media.action.STILL_IMAGE_CAMERA".equals(this.mIntent.getAction()) ? "CAPTURE" : str2;
        }
        String stringExtra = this.mIntent.getStringExtra(str);
        return (stringExtra == null || stringExtra.isEmpty()) ? str2 : stringExtra;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getCameraModeId() {
        char c;
        String cameraMode = getCameraMode();
        switch (cameraMode.hashCode()) {
            case -2028086330:
                if (cameraMode.equals(CameraMode.MANUAL)) {
                    c = 2;
                    break;
                }
            case -1841345251:
                if (cameraMode.equals(CameraMode.SQUARE)) {
                    c = 6;
                    break;
                }
            case -143850509:
                if (cameraMode.equals(CameraMode.ULTRA_PIXEL)) {
                    c = 12;
                    break;
                }
            case 67864:
                if (cameraMode.equals(CameraMode.DOC)) {
                    c = 15;
                    break;
                }
            case 65911732:
                if (cameraMode.equals(CameraMode.SLOW_MOTION)) {
                    c = 10;
                    break;
                }
            case 76699320:
                if (cameraMode.equals(CameraMode.SHORT_VIDEO)) {
                    c = 7;
                    break;
                }
            case 81665115:
                if (cameraMode.equals(CameraMode.VIDEO)) {
                    c = 8;
                    break;
                }
            case 209155905:
                if (cameraMode.equals(CameraMode.FUN_AR2)) {
                    c = 14;
                    break;
                }
            case 244777209:
                if (cameraMode.equals(CameraMode.FAST_MOTION)) {
                    c = 9;
                    break;
                }
            case 1270567718:
                if (cameraMode.equals("CAPTURE")) {
                    c = 0;
                    break;
                }
            case 1340413460:
                if (cameraMode.equals(CameraMode.SUPER_NIGHT)) {
                    c = 11;
                    break;
                }
            case 1511893915:
                if (cameraMode.equals(CameraMode.PORTRAIT)) {
                    c = 5;
                    break;
                }
            case 1596340582:
                if (cameraMode.equals(CameraMode.GOOGLE_PANORAMA)) {
                    c = 3;
                    break;
                }
            case 1852610165:
                if (cameraMode.equals(CameraMode.PANORAMA)) {
                    c = 4;
                    break;
                }
            case 1871805052:
                if (cameraMode.equals(CameraMode.GOOGLE_MANUAL)) {
                    c = 1;
                    break;
                }
            case 2084956945:
                if (cameraMode.equals(CameraMode.FUN_AR)) {
                    c = 13;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return 163;
            case 1:
            case 2:
                return 167;
            case 3:
            case 4:
                return 166;
            case 5:
                return 171;
            case 6:
                return 165;
            case 7:
                if (C0122O00000o.instance().OOO0o0O()) {
                    return 174;
                }
                return C0122O00000o.instance().OOO0ooO() ? 183 : 161;
            case 8:
                return 162;
            case 9:
                return 169;
            case 10:
                return C0122O00000o.instance().OOoOoo0() ? 172 : 160;
            case 11:
                return 173;
            case 12:
                return 175;
            case 13:
                return 177;
            case 14:
                return 184;
            case 15:
                return 186;
            default:
                return 160;
        }
    }

    public String getCameraSubMode() {
        return this.mIntent.getStringExtra(CameraExtras.CAMERA_SUB_MODE);
    }

    public String getExtraCropValue() {
        Bundle extras = this.mIntent.getExtras();
        if (extras != null) {
            return extras.getString("crop");
        }
        return null;
    }

    public Uri getExtraSavedUri() {
        Bundle extras = this.mIntent.getExtras();
        if (extras != null) {
            return (Uri) extras.getParcelable("output");
        }
        return null;
    }

    public Boolean getExtraShouldSaveCapture() {
        Bundle extras = this.mIntent.getExtras();
        return extras != null ? Boolean.valueOf(extras.getBoolean("save-image", false)) : Boolean.valueOf(false);
    }

    public int getFilterMode() {
        return this.mIntent.getIntExtra(CameraExtras.CAMERA_FILTER_MODE, 0);
    }

    public String getFlashMode() {
        return this.mIntent.getStringExtra(CameraExtras.CAMERA_FLASH_MODE);
    }

    public String getHdrMode() {
        return this.mIntent.getStringExtra(CameraExtras.EXTRAS_CAMERA_HDR_MODE);
    }

    public long getRequestSize() {
        Bundle extras = this.mIntent.getExtras();
        if (extras != null) {
            return extras.getLong("android.intent.extra.sizeLimit");
        }
        return 0;
    }

    public int getTimerDurationSeconds() {
        return this.mIntent.getIntExtra(CameraExtras.TIMER_DURATION_SECONDS, -1);
    }

    public int getVideoDurationTime() {
        String str = "android.intent.extra.durationLimit";
        if (this.mIntent.hasExtra(str)) {
            return this.mIntent.getIntExtra(str, 0);
        }
        throw new RuntimeException("EXTRA_DURATION_LIMIT has not been defined");
    }

    public int getVideoQuality() {
        String str = "android.intent.extra.videoQuality";
        if (this.mIntent.hasExtra(str)) {
            return this.mIntent.getIntExtra(str, 0);
        }
        return -1;
    }

    public String getVoiceControlAction() {
        Intent intent = this.mIntent;
        String str = BroadcastControlExtras.EXTRAS_VOICE_CONTROL_ACTION;
        boolean hasExtra = intent.hasExtra(str);
        String str2 = ControlActions.CONTROL_ACTION_UNKNOWN;
        if (!hasExtra) {
            return str2;
        }
        String stringExtra = this.mIntent.getStringExtra(str);
        return stringExtra == null ? str2 : stringExtra;
    }

    public boolean isCtsCall() {
        return TextUtils.equals(CALLER_CTS, getCaller());
    }

    public Boolean isFromScreenSlide() {
        boolean z = false;
        if (this.mIntent.getBooleanExtra(EXTRAS_SCREEN_SLIDE, false) || isFrontCameraIntent(getCameraFacing())) {
            z = true;
        }
        return Boolean.valueOf(z);
    }

    public Boolean isFromVolumeKey() {
        String stringExtra = this.mIntent.getStringExtra(EXTRA_LAUNCH_SOURCE);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("camera_launch_source = ");
        sb.append(stringExtra);
        Log.d(str, sb.toString());
        boolean OO00ooO = C0122O00000o.instance().OO00ooO();
        String str2 = EXTRA_LAUNCH_SOURCE_DOUBLE_POWER;
        if (OO00ooO) {
            return Boolean.valueOf(TextUtils.equals(stringExtra, str2));
        }
        boolean z = (this.mIntent.getFlags() & 8388608) == 0 || TextUtils.equals(stringExtra, str2) || TextUtils.equals(stringExtra, EXTRA_LAUNCH_SOURCE_DOUBLE_VOLUME) || TextUtils.equals(stringExtra, EXTRA_LAUNCH_SOURCE_STABILIZER) || TextUtils.equals(stringExtra, EXTRA_LAUNCH_SOURCE_MIWATCH);
        return Boolean.valueOf(z);
    }

    public boolean isImageCaptureIntent() {
        String action = this.mIntent.getAction();
        return "android.media.action.IMAGE_CAPTURE".equals(action) || "android.media.action.IMAGE_CAPTURE_SECURE".equals(action) || ACTION_IDPHOTO_IMAGE.equals(action);
    }

    public boolean isOnlyForceOpenMainBackCamera() {
        return this.mIntent.getBooleanExtra(CameraExtras.EXTRAS_NO_UI_QUERY, false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0067, code lost:
        if ("android.media.action.STILL_IMAGE_CAMERA_SECURE".equals(r0) != false) goto L_0x0012;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x009b, code lost:
        if (android.text.TextUtils.equals("com.android.camera.VoiceCamera", r8.getClassName()) == false) goto L_0x00a7;
     */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0074  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0082  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isOpenOnly(Activity activity) {
        String action = this.mIntent.getAction();
        String str = ACTION_VOICE_CONTROL;
        boolean equals = str.equals(action);
        String str2 = CALLER_GOOGLE_ASSISTANT;
        boolean z = false;
        if (!equals && !"android.intent.action.MAIN".equals(action)) {
            if ("android.media.action.STILL_IMAGE_CAMERA".equals(action)) {
                if (str2.equals(getCaller())) {
                    if (this.mIntent.getCategories() != null && this.mIntent.getCategories().contains("android.intent.category.VOICE") && isFromVoice(activity)) {
                        Log.d(TAG, "from voice root, not open only");
                    } else if (isOnlyForceOpenMainBackCamera()) {
                        Log.d(TAG, "not from voice root, just open");
                    }
                }
                if (!str2.equals(getCaller())) {
                    if (!isFromVoice(activity)) {
                        Log.d(TAG, "just open only");
                        return true;
                    }
                } else if (!TextUtils.equals(str, action)) {
                    return true;
                } else {
                    ComponentName component = this.mIntent.getComponent();
                    if (component != null) {
                    }
                    return true;
                }
                return this.mIntent.getBooleanExtra(CameraExtras.CAMERA_OPEN_ONLY, z);
            }
        }
        z = true;
        if (!str2.equals(getCaller())) {
        }
        return this.mIntent.getBooleanExtra(CameraExtras.CAMERA_OPEN_ONLY, z);
    }

    public Boolean isQuickCapture() {
        return Boolean.valueOf(this.mIntent.getBooleanExtra(EXTRAS_QUICK_CAPTURE, false));
    }

    public boolean isQuickLaunch() {
        boolean z = false;
        if (!C0122O00000o.instance().OO00ooO()) {
            return this.mIntent.getBooleanExtra("StartActivityWhenLocked", false);
        }
        String action = this.mIntent.getAction();
        if (!TextUtils.equals(action, "android.media.action.STILL_IMAGE_CAMERA") && !TextUtils.equals(action, "android.media.action.STILL_IMAGE_CAMERA_SECURE")) {
            return false;
        }
        String stringExtra = this.mIntent.getStringExtra(EXTRA_LAUNCH_SOURCE);
        if (TextUtils.equals(stringExtra, EXTRA_LAUNCH_SOURCE_LOCKSCREEN_AFFORDANCE) || TextUtils.equals(stringExtra, EXTRA_LAUNCH_SOURCE_DOUBLE_POWER)) {
            z = true;
        }
        return z;
    }

    public boolean isScanQRCodeIntent() {
        String action = this.mIntent.getAction();
        return ACTION_QR_CODE_CAPTURE.equals(action) || ACTION_QR_CODE_ZXING.equals(action);
    }

    public boolean isUseFrontCamera() {
        Intent intent = this.mIntent;
        String str = CameraExtras.USE_FRONT_CAMERA;
        if (intent.hasExtra(str)) {
            return this.mIntent.getBooleanExtra(str, false);
        }
        throw new Exception("USE_FRONT_CAMERA extras has not been defined!");
    }

    public boolean isVideoCaptureIntent() {
        return "android.media.action.VIDEO_CAPTURE".equals(this.mIntent.getAction());
    }

    public void setReferer(Activity activity) {
        if (activity != null) {
            this.mReferer = activity.getReferrer();
        }
    }
}
