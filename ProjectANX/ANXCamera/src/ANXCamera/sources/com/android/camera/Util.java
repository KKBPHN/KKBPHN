package com.android.camera;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import O00000Oo.O00000oO.O000000o.O0000Oo0;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.YuvImage;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.utils.SurfaceUtils;
import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.location.Country;
import android.location.CountryDetector;
import android.location.Location;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.Image;
import android.media.Image.Plane;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.Build.VERSION;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.MiuiSettings.Global;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextUtils.SimpleStringSplitter;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Size;
import android.util.SparseArray;
import android.util.Xml;
import android.view.IWindowManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AlphaAnimation;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.core.app.FrameMetricsAggregator;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.ComponentConfigRatio;
import com.android.camera.effect.FilterInfo;
import com.android.camera.effect.MiYuvImage;
import com.android.camera.effect.draw_mode.DrawFillRectAttribute;
import com.android.camera.effect.renders.CustomTextWaterMark;
import com.android.camera.effect.renders.DeviceWatermarkParam;
import com.android.camera.effect.renders.ImageWaterMark;
import com.android.camera.effect.renders.NewStyleTextWaterMark;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.lib.compatibility.util.CompatibilityUtils.PackageInstallerListener;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.FileUtils;
import com.android.camera.module.impl.component.MultiFeatureManagerImpl;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.network.download.Verifier;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.storage.Storage;
import com.android.camera.watermark.WaterMarkUtil;
import com.android.camera2.CameraCapabilities;
import com.android.camera2.CaptureResultParser;
import com.android.camera2.PortraitDepthMap;
import com.android.camera2.PortraitDepthMapExif.Builder;
import com.android.camera2.vendortag.CaptureResultVendorTags;
import com.android.camera2.vendortag.struct.AECFrameControl;
import com.android.camera2.vendortag.struct.AFFrameControl;
import com.android.camera2.vendortag.struct.SatFusionCalibrationData;
import com.android.gallery3d.exif.ExifInterface;
import com.android.gallery3d.exif.Rational;
import com.android.gallery3d.ui.BaseGLCanvas;
import com.android.gallery3d.ui.StringTexture;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.core.PictureInfo;
import com.xiaomi.camera.liveshot.CircularMediaRecorder.VideoClipSavingCallback;
import com.xiaomi.camera.liveshot.LivePhotoResult;
import com.xiaomi.stat.a;
import com.xiaomi.stat.a.j;
import com.xiaomi.stat.b;
import com.xiaomi.stat.d;
import dalvik.system.VMRuntime;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import miui.hardware.display.DisplayFeatureManager;
import miui.os.Build;
import miui.reflect.Field;
import miui.reflect.NoSuchClassException;
import miui.reflect.NoSuchFieldException;
import miui.reflect.NoSuchMethodException;
import miui.telephony.phonenumber.TelocationProvider.Contract.Params;
import miui.text.ExtraTextUtils;
import miui.util.IOUtils;
import miui.view.animation.CubicEaseOutInterpolator;
import miui.view.animation.SineEaseInOutInterpolator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer.OnNativeInvokeListener;
import tv.danmaku.ijk.media.player.misc.IMediaFormat;

public final class Util {
    public static final String ACTION_BIND_GALLERY_SERVICE = "com.miui.gallery.action.BIND_SERVICE";
    public static final String ACTION_DISMISS_KEY_GUARD = "xiaomi.intent.action.SHOW_SECURE_KEYGUARD";
    public static final String ACTION_KILL_CAMERA_SERVICE = "com.android.camera.action.KILL_CAMERA_SERVICE";
    public static final String ACTION_RESET_CAMERA_PREF = "miui.intent.action.RESET_CAMERA_PREF";
    public static final String ALGORITHM_NAME_DUAL_PORTRAIT = "portrait";
    public static final String ALGORITHM_NAME_MEGVII_DUAL_PORTRAIT = "megvii_portrait";
    public static final String ALGORITHM_NAME_MIMOJI_CAPTURE = "mimoji";
    public static final String ALGORITHM_NAME_MI_DUAL_PORTRAIT = "mi_portrait";
    public static final String ALGORITHM_NAME_MI_SOFT_PORTRAIT = "soft-portrait";
    public static final String ALGORITHM_NAME_MI_SOFT_PORTRAIT_ENCRYPTED = "soft-portrait-enc";
    public static final String ANDROID_ONE_EXTRA_IS_SECURE_MODE = "com.google.android.apps.photos.api.secure_mode";
    public static final String ANDROID_ONE_EXTRA_SECURE_MODE_MEDIA_STORE_IDS = "com.google.android.apps.photos.api.secure_mode_ids";
    public static final String ANDROID_ONE_REVIEW_ACTIVITY_PACKAGE = "com.google.android.apps.photos";
    private static HashSet ANTIBANDING_60_COUNTRY = new HashSet(Arrays.asList(new String[]{"TW", "KR", "SA", "US", "CA", "BR", "CO", "MX", "PH"}));
    private static final Long APERTURE_VALUE_PRECISION = Long.valueOf(100);
    public static final double ASPECT_TOLERANCE = 0.02d;
    private static final int BACK_LIGHT_SENSOR_TYPE = 33171055;
    public static final int BLUR_DURATION = 100;
    private static final int BYTES_COPY_BUFFER_LENGTH = 1024;
    public static final String CAMERA_CHANNEL_ID = "MiuiCamera";
    private static final List COLOR_TEMPERATURE_LIST = new ArrayList();
    private static final List COLOR_TEMPERATURE_MAP = new ArrayList();
    public static final String CUSTOM_DEFAULT_WATERMARK_PREFIX = "1";
    public static final boolean DEBUG = (!Build.IS_STABLE_VERSION);
    public static final boolean DEBUG_FPS = SystemProperties.getBoolean("cam.app.debug.fps", false);
    public static int DUMP_BUG_REPORT_THRHOLD = 15000;
    public static final char ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR = ':';
    public static final String EXTRAS_SKIP_LOCK = "skip_interception";
    private static final String EXTRAS_START_WITH_EFFECT_RENDER = "android.intent.extras.START_WITH_EFFECT_RENDER";
    public static final String EXTRA_HEIGHT_FROM_CAMERA = "photo_height";
    public static final String EXTRA_MIME_TYPE_FROM_CAMERA = "photo_mime_type";
    public static final String EXTRA_PHOTO_ENTER_TRANSIT_HEIGHT = "anchor_height";
    public static final String EXTRA_PHOTO_ENTER_TRANSIT_POS_X = "anchor_pivot_x";
    public static final String EXTRA_PHOTO_ENTER_TRANSIT_POS_Y = "anchor_pivot_y";
    public static final String EXTRA_PHOTO_ENTER_TRANSIT_WIDTH = "anchor_width";
    public static final String EXTRA_WIDTH_FROM_CAMERA = "photo_width";
    public static final int FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS = Integer.MIN_VALUE;
    public static final int FLING_DOWN = -1;
    public static final int FLING_ERROR = 0;
    public static final int FLING_UP = 1;
    private static final Long FOCAL_LENGTH_PRECISION = Long.valueOf(100);
    public static final String FONT_MIPRO_MEDIUM_NAME = "mipro-medium";
    public static final String FONT_MIPRO_REGULAR_NAME = "mipro-regular";
    private static final String FORCE_CAMERA_0_FILE = "force_camera_0";
    private static final String FORCE_NAME_SUFFIX_FILE = "force_name_suffix";
    private static final Long F_NUMBER_PRECISION = Long.valueOf(100);
    public static final int GOING_TO_CROP = 5;
    public static final int GOING_TO_DETAIL = 3;
    public static final int GOING_TO_GALLERY = 1;
    public static final int GOING_TO_MIUI_EXTRA_PHOTO = 6;
    public static final int GOING_TO_MODE_EDIT = 7;
    public static final int GOING_TO_MUSIC = 8;
    public static final int GOING_TO_PLAYBACK = 4;
    public static final int GOING_TO_SETTING = 2;
    public static final float GYROSCOPE_MAX_X = 0.7f;
    public static final float GYROSCOPE_MAX_Y = 5.0f;
    public static final float GYROSCOPE_MAX_Z = 0.7f;
    public static final String INTENT_MIUI_GALLERY_REVIEW = "com.miui.camera.action.REVIEW";
    private static final File INTERNAL_STORAGE_DIRECTORY = new File("/data/sdcard");
    public static final int KEYCODE_SLIDE_OFF = 701;
    public static final int KEYCODE_SLIDE_ON = 700;
    public static final String KEY_CAMERA_BRIGHTNESS = "camera-brightness";
    public static final String KEY_CAMERA_BRIGHTNESS_AUTO = "camera-brightness-auto";
    public static final String KEY_CAMERA_BRIGHTNESS_MANUAL = "camera-brightness-manual";
    public static final String KEY_KILLED_MODULE_INDEX = "killed-moduleIndex";
    public static final String KEY_REVIEW_FROM_MIUICAMERA = "from_MiuiCamera";
    public static final String KEY_SECURE_ITEMS = "SecureUri";
    private static final String LAB_OPTIONS_VISIBLE_FILE = "lab_options_visible";
    public static final String LAST_FRAME_GAUSSIAN_FILE_NAME = "blur.jpg";
    public static final int LIMIT_SURFACE_WIDTH = 720;
    private static final double LOG_2 = Math.log(2.0d);
    public static final String MARKET_NAME = SystemProperties.get("ro.product.marketname", "");
    public static final int MAX_SECURE_SIZE = 100;
    private static final Long MS_TO_S = Long.valueOf(ExtraTextUtils.MB);
    private static final String NONUI_MODE_PROPERTY = "sys.power.nonui";
    private static final Long NS_TO_S = Long.valueOf(ExtraTextUtils.GB);
    public static final int ORIENTATION_HYSTERESIS = 5;
    public static final String QRCODE_RECEIVER_ACTION = "com.xiaomi.scanner.receiver.senderbarcodescanner";
    public static final float RATIO_16_9 = 1.7777777f;
    public static final float RATIO_18_7_5_9 = 2.0833333f;
    public static final float RATIO_18_9 = 2.0f;
    public static final float RATIO_19P5_9 = 2.1666667f;
    public static final float RATIO_19_9 = 2.1111112f;
    public static final float RATIO_1_1 = 1.0f;
    public static final float RATIO_20_9 = 2.2222223f;
    public static final float RATIO_4_3 = 1.3333333f;
    public static final int REQUEST_CODE_OPEN_MIUI_EXTRA_PHOTO = 35893;
    public static final String REVIEW_ACTION = "com.android.camera.action.REVIEW";
    public static final String REVIEW_ACTIVITY_PACKAGE = "com.miui.gallery";
    public static final String REVIEW_SCAN_RESULT_PACKAGE = "com.xiaomi.scanner";
    public static final int ROTATION_ANIMATION_SCALE_DOWN = 5;
    public static final int ROTATION_ANIMATION_SCALE_UP = 4;
    private static final String SCHEME_PHOTO = "photo";
    public static final int SCREEN_EFFECT_CAMERA_STATE = 14;
    public static final Uri SCREEN_SLIDE_STATUS_SETTING_URI = System.getUriFor("sc_status");
    private static String SCREEN_VENDOR = null;
    public static final int SHADOW_COLOR = Integer.MIN_VALUE;
    public static final float SHADOW_RADIUS = 1.0f;
    private static final Long SHUTTER_SPEED_VALUE_PRECISION = Long.valueOf(100);
    public static final float STANDARD_WIDTH = 3000.0f;
    private static final String TAG = "CameraUtil";
    private static final String TEMP_SUFFIX = ".tmp";
    public static final long TOTAL_MEMORY_GB = getPhoneMemorySize();
    public static final String VOICEACCESS_A11y_SERVICE = "com.miui.accessibility/com.miui.accessibility.voiceaccess.VoiceAccessAccessibilityService";
    public static final String WATERMARK_CINEMATIC_RATIO_FILE_NAME;
    public static final String WATERMARK_DEFAULT_FILE_NAME = "dualcamera.png";
    public static final String WATERMARK_FRONT_FILE_NAME;
    public static String WATERMARK_SPACE = null;
    public static final String WATERMARK_STORAGE_DIRECTORY = "/mnt/vendor/persist/camera/";
    public static final boolean WATER_MARK_DUMP = SystemProperties.getBoolean("dump_water_mark", false);
    private static final String ZOOM_ANIMATION_PROPERTY = "camera_zoom_animation";
    private static boolean isHasBackLightSensor = false;
    private static String mCountryIso = null;
    private static int mLockedOrientation = -1;
    public static String sAAID = null;
    public static final double sCinematicAspectRatio = 2.39d;
    public static final float sCinematicAspectWaterMarkRatio = 0.86f;
    private static boolean sClearMemoryLimit;
    private static Float sDumpCropFrontZoomRatio;
    private static ImageFileNamer sImageFileNamer;
    private static boolean sIsAccessibilityEnable;
    public static boolean sIsDumpBugReport;
    private static Boolean sIsDumpImageEnabled;
    public static boolean sIsDumpLog;
    public static boolean sIsDumpOrigJpg;
    private static Boolean sIsForceNameSuffix;
    public static boolean sIsKillCameraService;
    private static Boolean sIsLabOptionsVisible;
    private static boolean sIsVoiceAccessibilityEnable;
    public static long sLastDumpBugReportTime;
    public static String sRegion;
    public static boolean sSuperNightDefaultModeEnable;
    private static HashMap sTypefaces = new HashMap();
    private static IWindowManager sWindowManager;

    class ImageFileNamer {
        private SimpleDateFormat mFormat;
        private long mLastDate;
        private int mSameSecondCount;

        public ImageFileNamer(String str) {
            this.mFormat = new SimpleDateFormat(str);
        }

        public String generateName(long j) {
            String format = this.mFormat.format(new Date(j));
            if (j / 1000 == this.mLastDate / 1000) {
                this.mSameSecondCount++;
                StringBuilder sb = new StringBuilder();
                sb.append(format);
                sb.append("_");
                sb.append(this.mSameSecondCount);
                return sb.toString();
            }
            this.mLastDate = j;
            this.mSameSecondCount = 0;
            return format;
        }
    }

    private static /* synthetic */ void $closeResource(Throwable th, AutoCloseable autoCloseable) {
        if (th != null) {
            try {
                autoCloseable.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        } else {
            autoCloseable.close();
        }
    }

    static {
        SCREEN_VENDOR = SystemProperties.get("sys.panel.display");
        WATERMARK_SPACE = "other_space";
        if (UserHandle.myUserId() == 0) {
            WATERMARK_SPACE = "main_space";
        }
        if (TextUtils.isEmpty(SCREEN_VENDOR)) {
            SCREEN_VENDOR = SystemProperties.get("vendor.panel.display");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(android.os.Build.DEVICE);
        sb.append("_front_watermark.png");
        WATERMARK_FRONT_FILE_NAME = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(android.os.Build.DEVICE);
        sb2.append("_");
        sb2.append(WATERMARK_SPACE);
        sb2.append("_cinematic_ratio_custom_watermark.png");
        WATERMARK_CINEMATIC_RATIO_FILE_NAME = sb2.toString();
    }

    private Util() {
    }

    public static void Assert(boolean z) {
        if (!z) {
            throw new AssertionError();
        }
    }

    public static byte[] RGBA2RGB(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            return null;
        }
        int i3 = i * i2;
        byte[] bArr2 = new byte[(i3 * 3)];
        int i4 = 0;
        int i5 = 0;
        while (i4 < i3) {
            int i6 = i5 + 1;
            int i7 = i4 * 4;
            bArr2[i5] = bArr[i7];
            int i8 = i6 + 1;
            bArr2[i6] = bArr[i7 + 1];
            int i9 = i8 + 1;
            bArr2[i8] = bArr[i7 + 2];
            i4++;
            i5 = i9;
        }
        return bArr2;
    }

    private static String addDebugInfo(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\t ");
        sb.append(str);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append("\n");
        return sb3.toString();
    }

    private static String addProperties(String str) {
        if (SystemProperties.get(str) == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\t ");
        sb.append(SystemProperties.get(str));
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append("\n");
        return sb3.toString();
    }

    public static void adjBoost() {
        String str = TAG;
        try {
            Log.d(str, "boostParallelServiceAdj");
            Method declaredMethod = Class.forName("miui.process.ProcessManager").getDeclaredMethod("adjBoost", new Class[]{String.class, Integer.TYPE, Long.TYPE, Integer.TYPE});
            declaredMethod.invoke(null, new Object[]{CameraIntentManager.CALLER_MIUI_CAMERA, Integer.valueOf(0), Long.valueOf(60000), Integer.valueOf(((Integer) Class.forName("android.os.UserHandle").getMethod("myUserId", new Class[0]).invoke(null, new Object[0])).intValue())});
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            StringBuilder sb = new StringBuilder();
            sb.append(e.getClass().getSimpleName());
            sb.append(":");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), (Throwable) e);
        }
    }

    public static Bitmap adjustPhotoRotation(Bitmap bitmap, int i) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate((float) i);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        boolean equals = createBitmap.equals(bitmap);
        return createBitmap;
    }

    public static void alignPopupBottom(View view) {
        ((MarginLayoutParams) view.getLayoutParams()).bottomMargin = Display.getBottomHeight();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00a3, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00a4, code lost:
        r3 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
        $closeResource(r2, r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00a8, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00a9, code lost:
        r0 = e;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:23:0x008f, B:38:0x00a2] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void appendCaptureResultToExif(String str, int i, int i2, int i3, long j, Location location, long j2, CaptureResult captureResult) {
        String str2;
        Throwable th;
        String str3 = str;
        boolean isEmpty = TextUtils.isEmpty(str);
        String str4 = TAG;
        if (isEmpty || !isPathExist(str) || new File(str3).length() == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("writeExifByFilePath: ");
            sb.append(str3);
            sb.append(" does not exist or its content is empty");
            Log.e(str4, sb.toString());
            return;
        }
        ExifInterface exifInterface = new ExifInterface();
        try {
            exifInterface.readExif(str3);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str3);
            sb2.append(".tmp.jpg");
            File file = new File(sb2.toString());
            if (!new File(str3).renameTo(file)) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("writeExifByFilePath: failed rename to ");
                sb3.append(file.getAbsolutePath());
                Log.w(str4, sb3.toString());
                return;
            }
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                try {
                    int i4 = i;
                    int i5 = i2;
                    int i6 = i3;
                    long j3 = j;
                    Location location2 = location;
                    CaptureResult captureResult2 = captureResult;
                    long j4 = j2;
                    ExifInterface exifInterface2 = exifInterface;
                    str2 = str4;
                    FileOutputStream fileOutputStream = new FileOutputStream(str3, false);
                    try {
                        appendExifInfo(i4, i5, i6, j3, location2, captureResult2, j4, exifInterface2, true);
                        exifInterface.writeExif((InputStream) fileInputStream, (OutputStream) fileOutputStream);
                    } catch (Throwable th2) {
                        Throwable th3 = th2;
                        $closeResource(th, fileOutputStream);
                        throw th3;
                    }
                    try {
                        $closeResource(null, fileOutputStream);
                        $closeResource(null, fileInputStream);
                        file.delete();
                    } catch (Throwable th4) {
                        th = th4;
                        Throwable th5 = th;
                        throw th5;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    str2 = str4;
                    Throwable th52 = th;
                    throw th52;
                }
            } catch (IOException e) {
                e = e;
                str2 = str4;
                try {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("writeExifByFilePath: failed update exif for ");
                    sb4.append(str3);
                    Log.w(str2, sb4.toString(), (Throwable) e);
                    file.delete();
                } catch (Throwable th7) {
                    file.delete();
                    throw th7;
                }
            }
        } catch (IOException e2) {
            IOException iOException = e2;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("writeExifByFilePath: failed to extract exif from ");
            sb5.append(str3);
            Log.w(str4, sb5.toString(), (Throwable) iOException);
        }
    }

    public static byte[] appendCaptureResultToExif(byte[] bArr, int i, int i2, int i3, long j, Location location, CameraMetadataNative cameraMetadataNative) {
        byte[] bArr2;
        Throwable th;
        byte[] bArr3 = bArr;
        if ((!C0124O00000oO.isMTKPlatform() && !C0122O00000o.instance().OOo000o()) || ((C0124O00000oO.isMTKPlatform() && C0122O00000o.instance().OOo0oOO()) || cameraMetadataNative == null)) {
            return bArr3;
        }
        ExifInterface exif = ExifInterface.getExif(bArr);
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                appendExifInfo(i, i2, i3, j, location, cameraMetadataNative, exif);
                exif.writeExif(bArr, (OutputStream) byteArrayOutputStream);
                bArr2 = byteArrayOutputStream.toByteArray();
                try {
                    $closeResource(null, byteArrayOutputStream);
                } catch (IOException | RuntimeException unused) {
                }
                return (bArr2 != null || bArr2.length < bArr3.length) ? bArr3 : bArr2;
            } catch (Throwable th2) {
                Throwable th3 = th2;
                $closeResource(th, byteArrayOutputStream);
                throw th3;
            }
        } catch (IOException | RuntimeException unused2) {
            bArr2 = null;
            Log.d(TAG, "appendExif(): Failed to append exif metadata");
            if (bArr2 != null) {
            }
        }
    }

    public static void appendExifInfo(int i, int i2, int i3, long j, Location location, CaptureResult captureResult, long j2, ExifInterface exifInterface, boolean z) {
        Integer num;
        String sb;
        Short sh;
        int i4;
        Rational rational;
        int i5;
        setTagValue(exifInterface, ExifInterface.TAG_ORIENTATION, Short.valueOf(ExifInterface.getExifOrientationValue(i3)));
        setTagValue(exifInterface, ExifInterface.TAG_PIXEL_X_DIMENSION, Integer.valueOf(i));
        setTagValue(exifInterface, ExifInterface.TAG_PIXEL_Y_DIMENSION, Integer.valueOf(i2));
        setTagValue(exifInterface, ExifInterface.TAG_IMAGE_WIDTH, Integer.valueOf(i));
        setTagValue(exifInterface, ExifInterface.TAG_IMAGE_LENGTH, Integer.valueOf(i2));
        setTagValue(exifInterface, ExifInterface.TAG_MODEL, android.os.Build.MODEL);
        if (!TextUtils.isEmpty(MARKET_NAME)) {
            exifInterface.addXiaomiProduct(MARKET_NAME);
        }
        setTagValue(exifInterface, ExifInterface.TAG_MAKE, android.os.Build.MANUFACTURER);
        if (j > 0) {
            exifInterface.addDateTimeStampTag(ExifInterface.TAG_DATE_TIME, j, TimeZone.getDefault());
            exifInterface.addSubTagSecTime(ExifInterface.TAG_SUB_SEC_TIME, j, TimeZone.getDefault());
        }
        Float f = (Float) captureResult.get(CaptureResult.LENS_FOCAL_LENGTH);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("LENS_FOCAL_LENGTH: ");
        sb2.append(f);
        String sb3 = sb2.toString();
        String str = TAG;
        Log.d(str, sb3);
        if (f != null) {
            setTagValue(exifInterface, ExifInterface.TAG_FOCAL_LENGTH, doubleToRational((double) f.floatValue(), FOCAL_LENGTH_PRECISION.longValue()));
        }
        Float f2 = (Float) captureResult.get(CaptureResult.LENS_APERTURE);
        StringBuilder sb4 = new StringBuilder();
        sb4.append("LENS_APERTURE: ");
        sb4.append(f2);
        Log.d(str, sb4.toString());
        if (f2 != null) {
            setTagValue(exifInterface, ExifInterface.TAG_F_NUMBER, doubleToRational((double) f2.floatValue(), F_NUMBER_PRECISION.longValue()));
            setTagValue(exifInterface, ExifInterface.TAG_APERTURE_VALUE, doubleToRational(log2((double) f2.floatValue()) * 2.0d, APERTURE_VALUE_PRECISION.longValue()));
        }
        if (C0122O00000o.instance().OOo000o()) {
            num = (Integer) captureResult.get(CaptureResultVendorTags.ISO_VALUE);
            StringBuilder sb5 = new StringBuilder();
            sb5.append("[ALGOUP|MMCAMERA] SENSOR_SENSITIVITY: ");
            sb5.append(num);
            sb = sb5.toString();
        } else {
            num = (Integer) captureResult.get(CaptureResult.SENSOR_SENSITIVITY);
            String str2 = "SENSOR_SENSITIVITY: ";
            if (z) {
                Integer num2 = (Integer) captureResult.get(CaptureResult.CONTROL_POST_RAW_SENSITIVITY_BOOST);
                StringBuilder sb6 = new StringBuilder();
                sb6.append(str2);
                sb6.append(num);
                sb6.append(" CONTROL_POST_RAW_SENSITIVITY_BOOST:");
                sb6.append(num2);
                Log.d(str, sb6.toString());
                if (!(num2 == null || num == null)) {
                    num = Integer.valueOf(num.intValue() * (num2.intValue() / 100));
                }
            }
            StringBuilder sb7 = new StringBuilder();
            sb7.append(str2);
            sb7.append(num);
            sb = sb7.toString();
        }
        Log.d(str, sb);
        if (num != null) {
            setTagValue(exifInterface, ExifInterface.TAG_ISO_SPEED_RATINGS, num);
        }
        Long l = (Long) captureResult.get(CaptureResult.SENSOR_EXPOSURE_TIME);
        StringBuilder sb8 = new StringBuilder();
        sb8.append("SENSOR_EXPOSURE_TIME: ");
        sb8.append(l);
        Log.d(str, sb8.toString());
        if (j2 > 0) {
            double d = ((double) j2) / 1000.0d;
            setTagValue(exifInterface, ExifInterface.TAG_EXPOSURE_TIME, new Rational(d < 0.5d ? (long) (d * 1000.0d) : Math.round(d) * 1000, 1000));
        } else if (l != null) {
            if (l.longValue() <= 4000000000L) {
                i5 = ExifInterface.TAG_EXPOSURE_TIME;
                rational = new Rational(l.longValue(), NS_TO_S.longValue());
            } else {
                i5 = ExifInterface.TAG_EXPOSURE_TIME;
                rational = new Rational(l.longValue() / 1000, MS_TO_S.longValue());
            }
            setTagValue(exifInterface, i5, rational);
            setTagValue(exifInterface, ExifInterface.TAG_SHUTTER_SPEED_VALUE, doubleToRational(log2(((double) l.longValue()) / ((double) NS_TO_S.longValue())), SHUTTER_SPEED_VALUE_PRECISION.longValue()));
        }
        Location location2 = (Location) captureResult.get(CaptureResult.JPEG_GPS_LOCATION);
        if (location2 == null) {
            location2 = location;
        }
        StringBuilder sb9 = new StringBuilder();
        sb9.append("JPEG_GPS_LOCATION: ");
        sb9.append(location2);
        Log.d(str, sb9.toString());
        if (location2 != null) {
            exifInterface.addGpsTags(location2.getLatitude(), location2.getLongitude());
            exifInterface.addGpsDateTimeStampTag(location2.getTime());
            double altitude = location2.getAltitude();
            if (altitude != 0.0d) {
                exifInterface.setTag(exifInterface.buildTag(ExifInterface.TAG_GPS_ALTITUDE_REF, Short.valueOf(altitude < 0.0d ? (short) 1 : 0)));
                exifInterface.addGpsTags(location2.getLatitude(), location2.getLongitude());
            }
        }
        Integer num3 = (Integer) captureResult.get(CaptureResult.FLASH_STATE);
        StringBuilder sb10 = new StringBuilder();
        sb10.append("FLASH_STATE: ");
        sb10.append(num3);
        Log.d(str, sb10.toString());
        if (num3 == null || num3.intValue() != 3) {
            i4 = ExifInterface.TAG_FLASH;
            sh = Short.valueOf(0);
        } else {
            i4 = ExifInterface.TAG_FLASH;
            sh = Short.valueOf(1);
        }
        setTagValue(exifInterface, i4, sh);
    }

    private static void appendExifInfo(int i, int i2, int i3, long j, Location location, CameraMetadataNative cameraMetadataNative, ExifInterface exifInterface) {
        String str;
        StringBuilder sb;
        Integer num;
        Short sh;
        int i4;
        Rational rational;
        int i5;
        setTagValue(exifInterface, ExifInterface.TAG_ORIENTATION, Short.valueOf(ExifInterface.getExifOrientationValue(i3)));
        setTagValue(exifInterface, ExifInterface.TAG_PIXEL_X_DIMENSION, Integer.valueOf(i));
        setTagValue(exifInterface, ExifInterface.TAG_PIXEL_Y_DIMENSION, Integer.valueOf(i2));
        setTagValue(exifInterface, ExifInterface.TAG_IMAGE_WIDTH, Integer.valueOf(i));
        setTagValue(exifInterface, ExifInterface.TAG_IMAGE_LENGTH, Integer.valueOf(i2));
        setTagValue(exifInterface, ExifInterface.TAG_MODEL, android.os.Build.MODEL);
        setTagValue(exifInterface, ExifInterface.TAG_MAKE, android.os.Build.MANUFACTURER);
        if (j > 0) {
            exifInterface.addDateTimeStampTag(ExifInterface.TAG_DATE_TIME, j, TimeZone.getDefault());
            exifInterface.addSubTagSecTime(ExifInterface.TAG_SUB_SEC_TIME, j, TimeZone.getDefault());
        }
        Float f = (Float) cameraMetadataNative.get(CaptureResult.LENS_FOCAL_LENGTH);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("LENS_FOCAL_LENGTH: ");
        sb2.append(f);
        String sb3 = sb2.toString();
        String str2 = TAG;
        Log.d(str2, sb3);
        if (f != null) {
            setTagValue(exifInterface, ExifInterface.TAG_FOCAL_LENGTH, doubleToRational((double) f.floatValue(), FOCAL_LENGTH_PRECISION.longValue()));
        }
        Float f2 = (Float) cameraMetadataNative.get(CaptureResult.LENS_APERTURE);
        StringBuilder sb4 = new StringBuilder();
        sb4.append("LENS_APERTURE: ");
        sb4.append(f2);
        Log.d(str2, sb4.toString());
        if (f2 != null) {
            setTagValue(exifInterface, ExifInterface.TAG_F_NUMBER, doubleToRational((double) f2.floatValue(), F_NUMBER_PRECISION.longValue()));
            setTagValue(exifInterface, ExifInterface.TAG_APERTURE_VALUE, doubleToRational(log2((double) f2.floatValue()) * 2.0d, APERTURE_VALUE_PRECISION.longValue()));
        }
        if (C0122O00000o.instance().OOo000o()) {
            num = (Integer) cameraMetadataNative.get(CaptureResultVendorTags.ISO_VALUE);
            sb = new StringBuilder();
            str = "[ALGOUP|MMCAMERA] SENSOR_SENSITIVITY: ";
        } else {
            num = (Integer) cameraMetadataNative.get(CaptureResult.SENSOR_SENSITIVITY);
            sb = new StringBuilder();
            str = "SENSOR_SENSITIVITY: ";
        }
        sb.append(str);
        sb.append(num);
        Log.d(str2, sb.toString());
        if (num != null) {
            setTagValue(exifInterface, ExifInterface.TAG_ISO_SPEED_RATINGS, num);
        }
        Long l = (Long) cameraMetadataNative.get(CaptureResult.SENSOR_EXPOSURE_TIME);
        StringBuilder sb5 = new StringBuilder();
        sb5.append("SENSOR_EXPOSURE_TIME: ");
        sb5.append(l);
        Log.d(str2, sb5.toString());
        if (l != null) {
            if (l.longValue() <= 4000000000L) {
                i5 = ExifInterface.TAG_EXPOSURE_TIME;
                rational = new Rational(l.longValue(), NS_TO_S.longValue());
            } else {
                i5 = ExifInterface.TAG_EXPOSURE_TIME;
                rational = new Rational(l.longValue() / 1000, MS_TO_S.longValue());
            }
            setTagValue(exifInterface, i5, rational);
            setTagValue(exifInterface, ExifInterface.TAG_SHUTTER_SPEED_VALUE, doubleToRational(log2(((double) l.longValue()) / ((double) NS_TO_S.longValue())), SHUTTER_SPEED_VALUE_PRECISION.longValue()));
        }
        Location location2 = (Location) cameraMetadataNative.get(CaptureResult.JPEG_GPS_LOCATION);
        if (location2 == null) {
            location2 = location;
        }
        StringBuilder sb6 = new StringBuilder();
        sb6.append("JPEG_GPS_LOCATION: ");
        sb6.append(location2);
        Log.d(str2, sb6.toString());
        if (location2 != null) {
            exifInterface.addGpsTags(location2.getLatitude(), location2.getLongitude());
            exifInterface.addGpsDateTimeStampTag(location2.getTime());
            double altitude = location2.getAltitude();
            if (altitude != 0.0d) {
                exifInterface.setTag(exifInterface.buildTag(ExifInterface.TAG_GPS_ALTITUDE_REF, Short.valueOf(altitude < 0.0d ? (short) 1 : 0)));
                exifInterface.addGpsTags(location2.getLatitude(), location2.getLongitude());
            }
        }
        Integer num2 = (Integer) cameraMetadataNative.get(CaptureResult.FLASH_STATE);
        StringBuilder sb7 = new StringBuilder();
        sb7.append("FLASH_STATE: ");
        sb7.append(num2);
        Log.d(str2, sb7.toString());
        if (num2 == null || num2.intValue() != 3) {
            i4 = ExifInterface.TAG_FLASH;
            sh = Short.valueOf(0);
        } else {
            i4 = ExifInterface.TAG_FLASH;
            sh = Short.valueOf(1);
        }
        setTagValue(exifInterface, i4, sh);
    }

    public static void appendExifToBitmap(Bitmap bitmap, OutputStream outputStream, int i, CaptureResult captureResult) {
        short s;
        int i2;
        Rational rational;
        int i3;
        ExifInterface exifInterface = new ExifInterface();
        ArrayList arrayList = new ArrayList();
        arrayList.add(exifInterface.buildTag(ExifInterface.TAG_PIXEL_X_DIMENSION, Integer.valueOf(bitmap.getWidth())));
        arrayList.add(exifInterface.buildTag(ExifInterface.TAG_PIXEL_Y_DIMENSION, Integer.valueOf(bitmap.getHeight())));
        arrayList.add(exifInterface.buildTag(ExifInterface.TAG_IMAGE_WIDTH, Integer.valueOf(bitmap.getWidth())));
        arrayList.add(exifInterface.buildTag(ExifInterface.TAG_IMAGE_LENGTH, Integer.valueOf(bitmap.getHeight())));
        arrayList.add(exifInterface.buildTag(ExifInterface.TAG_MODEL, C0124O00000oO.MODULE));
        if (!TextUtils.isEmpty(MARKET_NAME)) {
            exifInterface.addXiaomiProduct(MARKET_NAME);
        }
        arrayList.add(exifInterface.buildTag(ExifInterface.TAG_MAKE, android.os.Build.MANUFACTURER));
        arrayList.add(exifInterface.buildTag(ExifInterface.TAG_ORIENTATION, Short.valueOf(ExifInterface.getExifOrientationValue(i))));
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis > 0) {
            exifInterface.addDateTimeStampTag(ExifInterface.TAG_DATE_TIME, currentTimeMillis, TimeZone.getDefault());
            exifInterface.addSubTagSecTime(ExifInterface.TAG_SUB_SEC_TIME, currentTimeMillis, TimeZone.getDefault());
        }
        Location currentLocation = LocationManager.instance().getCurrentLocation();
        if (currentLocation != null) {
            double latitude = currentLocation.getLatitude();
            double longitude = currentLocation.getLongitude();
            arrayList.add(exifInterface.buildTag(ExifInterface.TAG_GPS_LATITUDE, ExifInterface.toExifLatLong(latitude)));
            arrayList.add(exifInterface.buildTag(ExifInterface.TAG_GPS_LONGITUDE, ExifInterface.toExifLatLong(longitude)));
            arrayList.add(exifInterface.buildTag(ExifInterface.TAG_GPS_LATITUDE_REF, latitude >= 0.0d ? "N" : "S"));
            arrayList.add(exifInterface.buildTag(ExifInterface.TAG_GPS_LONGITUDE_REF, longitude >= 0.0d ? "E" : "W"));
        }
        if (captureResult != null) {
            Float f = (Float) captureResult.get(CaptureResult.LENS_FOCAL_LENGTH);
            if (f != null) {
                arrayList.add(exifInterface.buildTag(ExifInterface.TAG_FOCAL_LENGTH, doubleToRational((double) f.floatValue(), FOCAL_LENGTH_PRECISION.longValue())));
            }
            Float f2 = (Float) captureResult.get(CaptureResult.LENS_APERTURE);
            StringBuilder sb = new StringBuilder();
            sb.append("LENS_APERTURE: ");
            sb.append(f2);
            String sb2 = sb.toString();
            String str = TAG;
            Log.d(str, sb2);
            if (f2 != null) {
                arrayList.add(exifInterface.buildTag(ExifInterface.TAG_F_NUMBER, doubleToRational((double) f2.floatValue(), F_NUMBER_PRECISION.longValue())));
                arrayList.add(exifInterface.buildTag(ExifInterface.TAG_APERTURE_VALUE, doubleToRational(log2((double) f2.floatValue()) * 2.0d, APERTURE_VALUE_PRECISION.longValue())));
            }
            Integer num = (Integer) captureResult.get(CaptureResult.SENSOR_SENSITIVITY);
            if (num != null) {
                arrayList.add(exifInterface.buildTag(ExifInterface.TAG_ISO_SPEED_RATINGS, num));
            }
            Long l = (Long) captureResult.get(CaptureResult.SENSOR_EXPOSURE_TIME);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("SENSOR_EXPOSURE_TIME: ");
            sb3.append(l);
            Log.d(str, sb3.toString());
            if (l != null) {
                if (l.longValue() <= 4000000000L) {
                    i3 = ExifInterface.TAG_EXPOSURE_TIME;
                    rational = new Rational(l.longValue(), NS_TO_S.longValue());
                } else {
                    i3 = ExifInterface.TAG_EXPOSURE_TIME;
                    rational = new Rational(l.longValue() / 1000, MS_TO_S.longValue());
                }
                arrayList.add(exifInterface.buildTag(i3, rational));
                arrayList.add(exifInterface.buildTag(ExifInterface.TAG_SHUTTER_SPEED_VALUE, doubleToRational(log2(((double) l.longValue()) / ((double) NS_TO_S.longValue())), SHUTTER_SPEED_VALUE_PRECISION.longValue())));
            }
            Integer num2 = (Integer) captureResult.get(CaptureResult.FLASH_STATE);
            StringBuilder sb4 = new StringBuilder();
            sb4.append("FLASH_STATE: ");
            sb4.append(num2);
            Log.d(str, sb4.toString());
            if (num2 == null || num2.intValue() != 3) {
                i2 = ExifInterface.TAG_FLASH;
                s = 0;
            } else {
                i2 = ExifInterface.TAG_FLASH;
                s = 1;
            }
            arrayList.add(exifInterface.buildTag(i2, Short.valueOf(s)));
        }
        exifInterface.setExif(arrayList);
        try {
            exifInterface.writeExif(bitmap, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SpannableStringBuilder appendInApi26(SpannableStringBuilder spannableStringBuilder, CharSequence charSequence, Object obj, int i) {
        int length = spannableStringBuilder.length();
        spannableStringBuilder.append(charSequence);
        spannableStringBuilder.setSpan(obj, length, spannableStringBuilder.length(), i);
        return spannableStringBuilder;
    }

    public static int binarySearchRightMost(List list, Object obj) {
        int size = list.size() - 1;
        int i = 0;
        while (i <= size) {
            int i2 = (i + size) / 2;
            if (((Comparable) list.get(i2)).compareTo(obj) >= 0) {
                size = i2 - 1;
            } else {
                i = i2 + 1;
            }
        }
        return i;
    }

    public static void broadcastKillService(Context context, boolean z) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        CameraSettings.setBroadcastKillServiceTime(elapsedRealtime);
        Intent intent = new Intent(ACTION_KILL_CAMERA_SERVICE);
        intent.putExtra("time", elapsedRealtime + 2000);
        intent.putExtra("dump_backtrace", z);
        intent.putExtra("process_name", new String[]{"android.hardware.camera.provider@2.4-service", "android.hardware.camera.provider@2.4-service_64"});
        context.sendBroadcast(intent);
        CameraStatUtils.trackBroadcastKillService();
    }

    public static void broadcastNewPicture(Context context, Uri uri) {
        Intent intent;
        int i = VERSION.SDK_INT;
        String str = "android.hardware.action.NEW_PICTURE";
        if (i < 24) {
            context.sendBroadcast(new Intent(str, uri));
            intent = new Intent("com.android.camera.NEW_PICTURE", uri);
        } else if (i >= 29) {
            intent = new Intent(str, uri);
        } else {
            return;
        }
        context.sendBroadcast(intent);
    }

    public static int[] calcDualCameraWatermarkLocation(int i, int i2, int i3, int i4) {
        float watermarkRatio = getWatermarkRatio(i, i2);
        return new int[]{Math.round(((float) i3) * watermarkRatio) & -2, Math.round(((float) i4) * watermarkRatio) & -2, Math.round(getWaterMarkPaddingX() * watermarkRatio) & -2, Math.round(getWaterMarkPaddingY() * watermarkRatio) & -2};
    }

    public static int[] calcDualCameraWatermarkLocationByCinema(int i, int i2, int i3, int i4, int i5) {
        float watermarkRatio = getWatermarkRatio(i, i2);
        int round = Math.round(((float) i3) * watermarkRatio * getCinematicAspectWaterMarkRatio()) & -2;
        int round2 = Math.round(((float) i4) * watermarkRatio * getCinematicAspectWaterMarkRatio()) & -2;
        int round3 = Math.round(getWaterMarkPaddingX() * watermarkRatio * getCinematicAspectWaterMarkRatio()) & -2;
        int round4 = Math.round(getWaterMarkPaddingY() * watermarkRatio * getCinematicAspectWaterMarkRatio()) & -2;
        int watermarkCinematicAspectMargin = getWatermarkCinematicAspectMargin(i, i2);
        if ((i5 == 90 || i5 == 270) && i > i2) {
            int i6 = i2;
            i2 = i;
            i = i6;
        }
        if (i < i2) {
            round3 += watermarkCinematicAspectMargin;
        } else {
            round4 += watermarkCinematicAspectMargin;
        }
        return new int[]{round, round2, round3, round4};
    }

    public static final int calculateDefaultPreviewEdgeSlop(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float f = ((float) displayMetrics.widthPixels) / displayMetrics.xdpi;
        float f2 = ((float) displayMetrics.heightPixels) / displayMetrics.ydpi;
        return context.getResources().getDimensionPixelSize(((float) Math.sqrt((double) ((f * f) + (f2 * f2)))) < 5.0f ? R.dimen.preview_edge_touch_slop_small_screen : R.dimen.preview_edge_touch_slop);
    }

    public static void checkHasBackLightSensot(Context context) {
        Sensor defaultSensor = ((SensorManager) context.getSystemService("sensor")).getDefaultSensor(BACK_LIGHT_SENSOR_TYPE);
        if (defaultSensor != null && BACK_LIGHT_SENSOR_TYPE == defaultSensor.getType()) {
            isHasBackLightSensor = true;
        }
    }

    public static void checkLockedOrientation(Activity activity) {
        try {
            if (System.getInt(activity.getContentResolver(), "accelerometer_rotation") == 0) {
                mLockedOrientation = System.getInt(activity.getContentResolver(), "user_rotation");
            } else {
                mLockedOrientation = -1;
            }
        } catch (SettingNotFoundException unused) {
            Log.e(TAG, "user rotation cannot found");
        }
    }

    public static Object checkNotNull(Object obj) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException();
    }

    public static float clamp(float f, float f2, float f3) {
        return f > f3 ? f3 : f < f2 ? f2 : f;
    }

    public static int clamp(int i, int i2, int i3) {
        return i > i3 ? i3 : i < i2 ? i2 : i;
    }

    public static long clamp(long j, long j2, long j3) {
        return j > j3 ? j3 : j < j2 ? j2 : j;
    }

    public static void clearMemoryLimit() {
        if (!sClearMemoryLimit) {
            long currentTimeMillis = System.currentTimeMillis();
            VMRuntime.getRuntime().clearGrowthLimit();
            sClearMemoryLimit = true;
            long currentTimeMillis2 = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder();
            sb.append("clearMemoryLimit() consume:");
            sb.append(currentTimeMillis2 - currentTimeMillis);
            Log.v(TAG, sb.toString());
        }
    }

    public static void clearSeamlessRotation(Activity activity) {
        LayoutParams attributes = activity.getWindow().getAttributes();
        activity.getWindow().addFlags(1024);
        attributes.rotationAnimation = 0;
        Log.d(TAG, "clearRotationAnimation");
        activity.getWindow().setAttributes(attributes);
    }

    public static void closeSafely(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception unused) {
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x0034  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] composeDepthMapPicture(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, int[] iArr, int i, boolean z, boolean z2, boolean z3, int i2, String str, int i3, int i4, boolean z4, boolean z5, int i5, DeviceWatermarkParam deviceWatermarkParam, PictureInfo pictureInfo, int i6, long j, int i7) {
        byte[] bArr5;
        byte[] frontCameraWatermarkData;
        byte[] bArr6 = bArr3;
        int i8 = i3;
        int i9 = i4;
        int i10 = i5;
        DeviceWatermarkParam deviceWatermarkParam2 = deviceWatermarkParam;
        String str2 = TAG;
        Log.d(str2, "composeDepthMapPicture: process in portrait depth map picture");
        long currentTimeMillis = System.currentTimeMillis();
        PortraitDepthMap portraitDepthMap = new PortraitDepthMap(bArr2);
        int[] iArr2 = new int[4];
        byte[] bArr7 = null;
        if (z2) {
            frontCameraWatermarkData = getDualCameraWatermarkData(i8, i9, iArr2, i10, deviceWatermarkParam2);
        } else if (z3) {
            frontCameraWatermarkData = getFrontCameraWatermarkData(i8, i9, iArr2, i10, deviceWatermarkParam2);
        } else {
            bArr5 = null;
            int[] iArr3 = new int[4];
            if (str != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("generate a TimeWaterMarkData with :");
                sb.append(i8);
                sb.append("x");
                sb.append(i9);
                Log.d(str2, sb.toString());
                bArr7 = getTimeWaterMarkData(i3, i4, str, iArr3, i5, deviceWatermarkParam);
            }
            byte[] depthMapData = portraitDepthMap.getDepthMapData();
            byte[] bArr8 = bArr;
            byte[] writePortraitExif = portraitDepthMap.writePortraitExif(new Builder().jpeg(bArr).dulWaterMark(bArr5).dulCameraWaterMarkLocation(iArr2).timeWaterMark(bArr7).timeWaterMarkLocation(iArr3).jpegDataOfTheRegionUnderWatermarks(bArr4).coordinatesOfOfTheRegionUnderWatermarks(iArr).rotation(i).lightingPattern(i2).isFrontMirror(z4).isBokehFrontCamera(z5).isCinematicAspectRatio(deviceWatermarkParam.isCinematicAspectRatio()).pictureInfo(pictureInfo).rawLength(bArr6.length).depthLength(depthMapData.length).portraitLightingVersioin(i6).timeStamp(j).isSupportZeroDegreeOrientationImage(z).cameraPreferredMode(i7).build());
            byte[] bArr9 = new byte[(writePortraitExif.length + bArr6.length + depthMapData.length)];
            System.arraycopy(writePortraitExif, 0, bArr9, 0, writePortraitExif.length);
            System.arraycopy(bArr6, 0, bArr9, writePortraitExif.length, bArr6.length);
            System.arraycopy(depthMapData, 0, bArr9, writePortraitExif.length + bArr6.length, depthMapData.length);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("composeDepthMapPicture: compose portrait picture cost: ");
            sb2.append(System.currentTimeMillis() - currentTimeMillis);
            Log.d(str2, sb2.toString());
            return bArr9;
        }
        bArr5 = frontCameraWatermarkData;
        int[] iArr32 = new int[4];
        if (str != null) {
        }
        byte[] depthMapData2 = portraitDepthMap.getDepthMapData();
        byte[] bArr82 = bArr;
        byte[] writePortraitExif2 = portraitDepthMap.writePortraitExif(new Builder().jpeg(bArr).dulWaterMark(bArr5).dulCameraWaterMarkLocation(iArr2).timeWaterMark(bArr7).timeWaterMarkLocation(iArr32).jpegDataOfTheRegionUnderWatermarks(bArr4).coordinatesOfOfTheRegionUnderWatermarks(iArr).rotation(i).lightingPattern(i2).isFrontMirror(z4).isBokehFrontCamera(z5).isCinematicAspectRatio(deviceWatermarkParam.isCinematicAspectRatio()).pictureInfo(pictureInfo).rawLength(bArr6.length).depthLength(depthMapData2.length).portraitLightingVersioin(i6).timeStamp(j).isSupportZeroDegreeOrientationImage(z).cameraPreferredMode(i7).build());
        byte[] bArr92 = new byte[(writePortraitExif2.length + bArr6.length + depthMapData2.length)];
        System.arraycopy(writePortraitExif2, 0, bArr92, 0, writePortraitExif2.length);
        System.arraycopy(bArr6, 0, bArr92, writePortraitExif2.length, bArr6.length);
        System.arraycopy(depthMapData2, 0, bArr92, writePortraitExif2.length + bArr6.length, depthMapData2.length);
        StringBuilder sb22 = new StringBuilder();
        sb22.append("composeDepthMapPicture: compose portrait picture cost: ");
        sb22.append(System.currentTimeMillis() - currentTimeMillis);
        Log.d(str2, sb22.toString());
        return bArr92;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:143:0x0286, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:144:0x0287, code lost:
        r5 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:146:?, code lost:
        $closeResource(r2, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:147:0x028b, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0083, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        throw r14;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:122:0x026a, B:141:0x0285] */
    /* JADX WARNING: Removed duplicated region for block: B:155:0x0298  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0095  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x009b  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x0204  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x020a A[SYNTHETIC, Splitter:B:88:0x020a] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] composeLiveShotPicture(byte[] bArr, int i, int i2, String str, long j, boolean z, boolean z2, String str2, int i3, DeviceWatermarkParam deviceWatermarkParam, byte[] bArr2, int[] iArr, int i4) {
        byte[] bArr3;
        String str3;
        int[] iArr2;
        byte[] bArr4;
        int i5;
        byte[] bArr5;
        String str4;
        String str5;
        byte[] bArr6;
        int i6;
        byte[] bArr7;
        Throwable th;
        Throwable th2;
        Throwable th3;
        StringWriter stringWriter;
        String str6;
        int i7;
        Throwable th4;
        byte[] bArr8 = bArr;
        int i8 = i;
        int i9 = i2;
        String str7 = str;
        int i10 = i3;
        DeviceWatermarkParam deviceWatermarkParam2 = deviceWatermarkParam;
        byte[] bArr9 = bArr2;
        int[] iArr3 = iArr;
        String str8 = "timewatermark";
        String str9 = XmpHelper.GOOGLE_MICROVIDEO_NAMESPACE;
        String str10 = TAG;
        Log.d(str10, "composeLiveShotPicture(): E");
        if (bArr8 == null || bArr8.length == 0) {
            Log.w(str10, "composeLiveShotPicture(): The primary photo of LiveShot is empty");
            return new byte[0];
        } else if (str7 == null || VideoClipSavingCallback.EMPTY_VIDEO_PATH.equals(str7)) {
            Log.w(str10, "composeLiveShotPicture(): The corresponding movie of LiveShot is empty");
            return bArr;
        } else {
            File file = new File(str7);
            if (!file.exists()) {
                StringBuilder sb = new StringBuilder();
                sb.append("composeLiveShotPicture(): not found LiveShot movie file ");
                sb.append(str7);
                Log.w(str10, sb.toString());
                return bArr8;
            }
            int length = (int) file.length();
            if (length == 0) {
                Log.w(str10, "composeLiveShotPicture(): The corresponding movie of LiveShot length is 0");
                return bArr8;
            }
            int[] iArr4 = new int[4];
            int[] iArr5 = new int[4];
            File file2 = file;
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    ExifInterface exifInterface = new ExifInterface();
                    exifInterface.readExif(bArr8);
                    exifInterface.addFileTypeLiveShot(true);
                    exifInterface.writeExif(bArr8, (OutputStream) byteArrayOutputStream);
                    th = byteArrayOutputStream.toByteArray();
                    try {
                        $closeResource(null, byteArrayOutputStream);
                    } catch (IOException unused) {
                        bArr3 = th;
                        Log.d(str10, "composeLiveShotPicture(): Failed to insert xiaomi specific metadata");
                        if (bArr3 == null) {
                        }
                    }
                } finally {
                    $closeResource(th4, byteArrayOutputStream);
                }
            } catch (IOException unused2) {
                bArr3 = null;
                Log.d(str10, "composeLiveShotPicture(): Failed to insert xiaomi specific metadata");
                if (bArr3 == null) {
                }
            }
            if (bArr3 == null) {
                Log.d(str10, "composeLiveShotPicture(): #1: return original jpeg");
                return bArr8;
            }
            byte[] bArr10 = z ? getDualCameraWatermarkData(i8, i9, iArr4, i10, deviceWatermarkParam2) : z2 ? getFrontCameraWatermarkData(i8, i9, iArr4, i10, deviceWatermarkParam2) : null;
            if (str2 == null || str2.isEmpty()) {
                iArr2 = iArr4;
                str3 = str9;
                bArr4 = null;
            } else {
                iArr2 = iArr4;
                str3 = str9;
                bArr4 = getTimeWaterMarkData(i, i2, str2, iArr5, i3, deviceWatermarkParam);
            }
            try {
                XmlSerializer newSerializer = Xml.newSerializer();
                StringWriter stringWriter2 = new StringWriter();
                newSerializer.setOutput(stringWriter2);
                newSerializer.startDocument("UTF-8", Boolean.valueOf(true));
                String str11 = "paddingy";
                String str12 = "paddingx";
                String str13 = "height";
                String str14 = "width";
                bArr5 = bArr3;
                String str15 = Params.LENGTH;
                String str16 = OnNativeInvokeListener.ARG_OFFSET;
                if (bArr4 != null) {
                    str6 = str10;
                    try {
                        if (bArr4.length > 0) {
                            i7 = bArr4.length + length;
                            i5 = length;
                            try {
                                newSerializer.startTag(null, str8);
                                stringWriter = stringWriter2;
                                newSerializer.attribute(null, str16, String.valueOf(i7));
                                newSerializer.attribute(null, str15, String.valueOf(bArr4.length));
                                newSerializer.attribute(null, str14, String.valueOf(iArr5[0]));
                                newSerializer.attribute(null, str13, String.valueOf(iArr5[1]));
                                newSerializer.attribute(null, str12, String.valueOf(iArr5[2]));
                                newSerializer.attribute(null, str11, String.valueOf(iArr5[3]));
                                newSerializer.endTag(null, str8);
                                if (bArr10 != null && bArr10.length > 0) {
                                    i7 += bArr10.length;
                                    newSerializer.startTag(null, "lenswatermark");
                                    newSerializer.attribute(null, str16, String.valueOf(i7));
                                    newSerializer.attribute(null, str15, String.valueOf(bArr10.length));
                                    newSerializer.attribute(null, str14, String.valueOf(iArr2[0]));
                                    newSerializer.attribute(null, str13, String.valueOf(iArr2[1]));
                                    newSerializer.attribute(null, str12, String.valueOf(iArr2[2]));
                                    newSerializer.attribute(null, str11, String.valueOf(iArr2[3]));
                                    newSerializer.endTag(null, "lenswatermark");
                                }
                                if (bArr9 != null && bArr9.length > 0 && iArr3 != null && iArr3.length >= 4) {
                                    int length2 = i7 + bArr9.length;
                                    newSerializer.startTag(null, "subimage");
                                    newSerializer.attribute(null, str16, String.valueOf(length2));
                                    newSerializer.attribute(null, str15, String.valueOf(bArr9.length));
                                    newSerializer.attribute(null, str12, String.valueOf(iArr3[0]));
                                    newSerializer.attribute(null, str11, String.valueOf(iArr3[1]));
                                    newSerializer.attribute(null, str14, String.valueOf(iArr3[2]));
                                    newSerializer.attribute(null, str13, String.valueOf(iArr3[3]));
                                    newSerializer.attribute(null, "rotation", String.valueOf(i4));
                                    newSerializer.endTag(null, "subimage");
                                }
                                newSerializer.endDocument();
                                str4 = stringWriter.toString();
                                str5 = str6;
                            } catch (IOException unused3) {
                                str5 = r21;
                                Log.d(str5, "composeLiveShotPicture(): Failed to generate xiaomi xmp metadata");
                                str4 = null;
                                if (str4 == null) {
                                }
                            }
                            if (str4 == null) {
                                Log.d(str5, "composeLiveShotPicture(): #2: return original jpeg");
                                return bArr;
                            }
                            try {
                                bArr7 = bArr5;
                                try {
                                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr7);
                                    try {
                                        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                                        try {
                                            XMPMeta createXMPMeta = XmpHelper.createXMPMeta();
                                            String str17 = str3;
                                            createXMPMeta.setPropertyInteger(str17, XmpHelper.MICROVIDEO_VERSION, 1);
                                            createXMPMeta.setPropertyInteger(str17, XmpHelper.MICROVIDEO_TYPE, 1);
                                            i6 = i5;
                                            try {
                                                createXMPMeta.setPropertyInteger(str17, XmpHelper.MICROVIDEO_OFFSET, i6);
                                                createXMPMeta.setPropertyLong(str17, XmpHelper.MICROVIDEO_PRESENTATION_TIMESTAMP, j);
                                                if (str4 != null) {
                                                    createXMPMeta.setProperty(XmpHelper.XIAOMI_XMP_METADATA_NAMESPACE, XmpHelper.XIAOMI_XMP_METADATA_PROPERTY_NAME, str4);
                                                }
                                                XmpHelper.writeXMPMeta(byteArrayInputStream, byteArrayOutputStream2, createXMPMeta);
                                                if (bArr9 != null && bArr9.length > 0 && iArr3 != null && iArr3.length >= 4) {
                                                    byteArrayOutputStream2.write(bArr9);
                                                }
                                                if (bArr10 != null && bArr10.length > 0) {
                                                    byteArrayOutputStream2.write(bArr10);
                                                }
                                                if (bArr4 != null && bArr4.length > 0) {
                                                    byteArrayOutputStream2.write(bArr4);
                                                }
                                                byteArrayOutputStream2.flush();
                                                bArr6 = byteArrayOutputStream2.toByteArray();
                                            } catch (Throwable th5) {
                                                th = th5;
                                                th3 = th;
                                                try {
                                                    throw th3;
                                                } catch (Throwable th6) {
                                                    th = th6;
                                                    th2 = th;
                                                    bArr6 = null;
                                                    throw th2;
                                                }
                                            }
                                        } catch (Throwable th7) {
                                            th = th7;
                                            i6 = i5;
                                            th3 = th;
                                            throw th3;
                                        }
                                        try {
                                            $closeResource(null, byteArrayOutputStream2);
                                            $closeResource(null, byteArrayInputStream);
                                            if (bArr6 != null || bArr6.length <= bArr7.length) {
                                                Log.d(str5, "composeLiveShotPicture(): #3: return original jpeg");
                                                return bArr;
                                            }
                                            int length3 = bArr6.length + i6;
                                            StringBuilder sb2 = new StringBuilder();
                                            sb2.append("composeLiveShotPicture(): fileSize = ");
                                            sb2.append(length3);
                                            sb2.append(" videoLength = ");
                                            sb2.append(i6);
                                            Log.d(str5, sb2.toString());
                                            byte[] bArr11 = new byte[length3];
                                            System.arraycopy(bArr6, 0, bArr11, 0, bArr6.length);
                                            try {
                                                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file2));
                                                try {
                                                    bufferedInputStream.read(bArr11, bArr6.length, i6);
                                                    $closeResource(null, bufferedInputStream);
                                                    Log.d(str5, "composeLiveShotPicture(): X");
                                                    return bArr11;
                                                } catch (Throwable th8) {
                                                    Throwable th9 = th8;
                                                    $closeResource(th, bufferedInputStream);
                                                    throw th9;
                                                }
                                            } catch (IOException e) {
                                                StringBuilder sb3 = new StringBuilder();
                                                sb3.append("composeLiveShotPicture(): failed to load the mp4 file content into memory: ");
                                                sb3.append(e);
                                                Log.d(str5, sb3.toString());
                                                return bArr;
                                            }
                                        } catch (Throwable th10) {
                                            th2 = th10;
                                            throw th2;
                                        }
                                    } catch (Throwable th11) {
                                        th = th11;
                                        i6 = i5;
                                        th2 = th;
                                        bArr6 = null;
                                        throw th2;
                                    }
                                } catch (Exception unused4) {
                                    i6 = i5;
                                    bArr6 = null;
                                    Log.d(str5, "composeLiveShotPicture(): failed to insert xmp metadata");
                                    if (bArr6 != null) {
                                    }
                                    Log.d(str5, "composeLiveShotPicture(): #3: return original jpeg");
                                    return bArr;
                                }
                            } catch (Exception unused5) {
                                bArr7 = bArr5;
                                i6 = i5;
                                bArr6 = null;
                                Log.d(str5, "composeLiveShotPicture(): failed to insert xmp metadata");
                                if (bArr6 != null) {
                                }
                                Log.d(str5, "composeLiveShotPicture(): #3: return original jpeg");
                                return bArr;
                            }
                        } else {
                            stringWriter = stringWriter2;
                        }
                    } catch (IOException unused6) {
                        i5 = length;
                        str5 = r21;
                        Log.d(str5, "composeLiveShotPicture(): Failed to generate xiaomi xmp metadata");
                        str4 = null;
                        if (str4 == null) {
                        }
                    }
                } else {
                    stringWriter = stringWriter2;
                    str6 = str10;
                }
                i5 = length;
                i7 = i5;
                i7 += bArr10.length;
                newSerializer.startTag(null, "lenswatermark");
                newSerializer.attribute(null, str16, String.valueOf(i7));
                newSerializer.attribute(null, str15, String.valueOf(bArr10.length));
                newSerializer.attribute(null, str14, String.valueOf(iArr2[0]));
                newSerializer.attribute(null, str13, String.valueOf(iArr2[1]));
                newSerializer.attribute(null, str12, String.valueOf(iArr2[2]));
                newSerializer.attribute(null, str11, String.valueOf(iArr2[3]));
                newSerializer.endTag(null, "lenswatermark");
                int length22 = i7 + bArr9.length;
                newSerializer.startTag(null, "subimage");
                newSerializer.attribute(null, str16, String.valueOf(length22));
                newSerializer.attribute(null, str15, String.valueOf(bArr9.length));
                newSerializer.attribute(null, str12, String.valueOf(iArr3[0]));
                newSerializer.attribute(null, str11, String.valueOf(iArr3[1]));
                newSerializer.attribute(null, str14, String.valueOf(iArr3[2]));
                newSerializer.attribute(null, str13, String.valueOf(iArr3[3]));
                newSerializer.attribute(null, "rotation", String.valueOf(i4));
                newSerializer.endTag(null, "subimage");
                newSerializer.endDocument();
                str4 = stringWriter.toString();
                str5 = str6;
            } catch (IOException unused7) {
                String str18 = str10;
                bArr5 = bArr3;
                i5 = length;
                str5 = str18;
                Log.d(str5, "composeLiveShotPicture(): Failed to generate xiaomi xmp metadata");
                str4 = null;
                if (str4 == null) {
                }
            }
            if (str4 == null) {
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:145:0x039e, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:146:0x039f, code lost:
        r4 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:148:?, code lost:
        $closeResource(r2, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:149:0x03a3, code lost:
        throw r4;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:126:0x0387, B:143:0x039d] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00f9 A[SYNTHETIC, Splitter:B:44:0x00f9] */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x01c7 A[Catch:{ IOException -> 0x030d }] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x01d4 A[Catch:{ IOException -> 0x030d }] */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0314 A[Catch:{ IOException -> 0x0329 }] */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x0333 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x0334 A[SYNTHETIC, Splitter:B:99:0x0334] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] composeMainSubPicture(byte[] bArr, byte[] bArr2, int[] iArr, int i, int i2, boolean z, boolean z2, String str, int i3, DeviceWatermarkParam deviceWatermarkParam, ParallelTaskData parallelTaskData, int i4) {
        String str2;
        byte[] bArr3;
        int[] iArr2;
        int[] iArr3;
        byte[] bArr4;
        File file;
        File file2;
        byte[] bArr5;
        String str3;
        byte[] bArr6;
        byte[] bArr7;
        byte[] bArr8;
        Throwable th;
        byte[] bArr9;
        StringWriter stringWriter;
        String str4;
        XmlSerializer xmlSerializer;
        int header2Int;
        int header2Int2;
        long length;
        int header2Int3;
        int header2Int4;
        long length2;
        long j;
        byte[] frontCameraWatermarkData;
        byte[] bArr10 = bArr;
        byte[] bArr11 = bArr2;
        int[] iArr4 = iArr;
        int i5 = i;
        int i6 = i2;
        int i7 = i3;
        DeviceWatermarkParam deviceWatermarkParam2 = deviceWatermarkParam;
        String str5 = "paddingx";
        String str6 = TAG;
        long timestamp = parallelTaskData.getTimestamp();
        StringBuilder sb = new StringBuilder();
        sb.append("sdcard/DCIM/Camera/fusionMainImage");
        sb.append(timestamp);
        String str7 = ".yuv";
        sb.append(str7);
        File file3 = new File(sb.toString());
        String str8 = ", length = ";
        StringBuilder sb2 = new StringBuilder();
        String str9 = "paddingy";
        sb2.append("sdcard/DCIM/Camera/fusionSubImage");
        sb2.append(timestamp);
        sb2.append(str7);
        File file4 = new File(sb2.toString());
        boolean z3 = (bArr11 == null || iArr4 == null || iArr4.length != 4) ? false : true;
        boolean z4 = CameraSettings.isHighQualityPreferred() && C0122O00000o.instance().OOo00OO() && parallelTaskData.isSatFusionShot() && file3.exists() && file4.exists();
        if (!z3 && !z4) {
            return bArr10;
        }
        try {
            ExifInterface exifInterface = new ExifInterface();
            exifInterface.readExif(bArr10);
            if (exifInterface.getMimeType() != 4) {
                str2 = "composeMainSubPicture(): HEIC does not support watermark removal";
            } else {
                int[] iArr5 = new int[4];
                int[] iArr6 = new int[4];
                if (z) {
                    frontCameraWatermarkData = getDualCameraWatermarkData(i5, i6, iArr5, i7, deviceWatermarkParam2);
                } else if (z2) {
                    frontCameraWatermarkData = getFrontCameraWatermarkData(i5, i6, iArr5, i7, deviceWatermarkParam2);
                } else {
                    bArr3 = null;
                    if (str != null || str.isEmpty()) {
                        iArr3 = iArr6;
                        iArr2 = iArr5;
                        bArr4 = null;
                    } else {
                        iArr3 = iArr6;
                        iArr2 = iArr5;
                        bArr4 = getTimeWaterMarkData(i, i2, str, iArr3, i3, deviceWatermarkParam);
                    }
                    XmlSerializer newSerializer = Xml.newSerializer();
                    StringWriter stringWriter2 = new StringWriter();
                    newSerializer.setOutput(stringWriter2);
                    newSerializer.startDocument("UTF-8", Boolean.valueOf(true));
                    String str10 = "height";
                    String str11 = "width";
                    String str12 = Params.LENGTH;
                    String str13 = OnNativeInvokeListener.ARG_OFFSET;
                    if (!z4) {
                        try {
                            header2Int = getHeader2Int(file3, 0);
                            header2Int2 = getHeader2Int(file3, 4);
                            length = file3.length() - 8;
                            file = file3;
                        } catch (IOException unused) {
                            file = file3;
                            file2 = file4;
                            bArr5 = bArr4;
                            bArr6 = bArr3;
                            Log.e(str6, "composeMainSubPicture(): Failed to generate xiaomi specific xmp metadata");
                            str3 = null;
                            bArr10 = bArr;
                            if (str3 == null) {
                            }
                        }
                        try {
                            header2Int3 = getHeader2Int(file4, 0);
                            header2Int4 = getHeader2Int(file4, 4);
                            length2 = file4.length() - 8;
                            file2 = file4;
                        } catch (IOException unused2) {
                            file2 = file4;
                            bArr5 = bArr4;
                            bArr6 = bArr3;
                            Log.e(str6, "composeMainSubPicture(): Failed to generate xiaomi specific xmp metadata");
                            str3 = null;
                            bArr10 = bArr;
                            if (str3 == null) {
                            }
                        }
                        try {
                            StringBuilder sb3 = new StringBuilder();
                            stringWriter = stringWriter2;
                            sb3.append("fusion main image size: ");
                            sb3.append(header2Int);
                            sb3.append("x");
                            sb3.append(header2Int2);
                            sb3.append(", fusion sub image size: ");
                            sb3.append(header2Int3);
                            sb3.append("x");
                            sb3.append(header2Int4);
                            Log.d(str6, sb3.toString());
                            int length3 = !z3 ? 0 : bArr11.length;
                            bArr9 = bArr4;
                            try {
                                newSerializer.startTag(null, "fusion");
                                newSerializer.startTag(null, a.d);
                                j = ((long) length3) + length2;
                                xmlSerializer = newSerializer;
                                str4 = str6;
                            } catch (IOException unused3) {
                                bArr5 = bArr9;
                                bArr6 = bArr3;
                                Log.e(str6, "composeMainSubPicture(): Failed to generate xiaomi specific xmp metadata");
                                str3 = null;
                                bArr10 = bArr;
                                if (str3 == null) {
                                }
                            }
                            try {
                                xmlSerializer.attribute(null, str13, String.valueOf(j + length));
                                xmlSerializer.attribute(null, str12, String.valueOf(length));
                                xmlSerializer.attribute(null, str11, String.valueOf(header2Int));
                                xmlSerializer.attribute(null, str10, String.valueOf(header2Int2));
                                xmlSerializer.endTag(null, a.d);
                                xmlSerializer.startTag(null, j.i);
                                xmlSerializer.attribute(null, str13, String.valueOf(j));
                                xmlSerializer.attribute(null, str12, String.valueOf(length2));
                                xmlSerializer.attribute(null, str11, String.valueOf(header2Int3));
                                xmlSerializer.attribute(null, str10, String.valueOf(header2Int4));
                                xmlSerializer.endTag(null, j.i);
                                xmlSerializer.endTag(null, "fusion");
                            } catch (IOException unused4) {
                                bArr5 = bArr9;
                                bArr6 = bArr3;
                                str6 = str4;
                                Log.e(str6, "composeMainSubPicture(): Failed to generate xiaomi specific xmp metadata");
                                str3 = null;
                                bArr10 = bArr;
                                if (str3 == null) {
                                }
                            }
                        } catch (IOException unused5) {
                            bArr5 = bArr4;
                            bArr6 = bArr3;
                            Log.e(str6, "composeMainSubPicture(): Failed to generate xiaomi specific xmp metadata");
                            str3 = null;
                            bArr10 = bArr;
                            if (str3 == null) {
                            }
                        }
                    } else {
                        bArr9 = bArr4;
                        stringWriter = stringWriter2;
                        str4 = str6;
                        file = file3;
                        file2 = file4;
                        xmlSerializer = newSerializer;
                    }
                    if (!z3) {
                        int length4 = bArr11.length;
                        xmlSerializer.startTag(null, "subimage");
                        xmlSerializer.attribute(null, str13, String.valueOf(length4));
                        xmlSerializer.attribute(null, str12, String.valueOf(bArr11.length));
                        xmlSerializer.attribute(null, str5, String.valueOf(iArr4[0]));
                        String str14 = str9;
                        xmlSerializer.attribute(null, str14, String.valueOf(iArr4[1]));
                        xmlSerializer.attribute(null, str11, String.valueOf(iArr4[2]));
                        xmlSerializer.attribute(null, str10, String.valueOf(iArr4[3]));
                        xmlSerializer.attribute(null, "rotation", String.valueOf(i4));
                        xmlSerializer.endTag(null, "subimage");
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("composeMainSubPicture: 1 offset = ");
                        sb4.append(length4);
                        String str15 = str8;
                        sb4.append(str15);
                        sb4.append(bArr11.length);
                        str6 = str4;
                        Log.d(str6, sb4.toString());
                        bArr6 = bArr3;
                        if (bArr6 != null) {
                            try {
                                length4 += bArr6.length;
                                xmlSerializer.startTag(null, "lenswatermark");
                                xmlSerializer.attribute(null, str13, String.valueOf(length4));
                                xmlSerializer.attribute(null, str12, String.valueOf(bArr6.length));
                                xmlSerializer.attribute(null, str11, String.valueOf(iArr2[0]));
                                xmlSerializer.attribute(null, str10, String.valueOf(iArr2[1]));
                                xmlSerializer.attribute(null, str5, String.valueOf(iArr2[2]));
                                xmlSerializer.attribute(null, str14, String.valueOf(iArr2[3]));
                                xmlSerializer.endTag(null, "lenswatermark");
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("composeMainSubPicture: 2 offset = ");
                                sb5.append(length4);
                                sb5.append(str15);
                                sb5.append(bArr6.length);
                                Log.d(str6, sb5.toString());
                            } catch (IOException unused6) {
                                bArr5 = bArr9;
                            }
                        }
                        if (bArr9 != null) {
                            bArr5 = bArr9;
                            try {
                                int length5 = length4 + bArr5.length;
                                xmlSerializer.startTag(null, "timewatermark");
                                xmlSerializer.attribute(null, str13, String.valueOf(length5));
                                xmlSerializer.attribute(null, str12, String.valueOf(bArr5.length));
                                xmlSerializer.attribute(null, str11, String.valueOf(iArr3[0]));
                                xmlSerializer.attribute(null, str10, String.valueOf(iArr3[1]));
                                xmlSerializer.attribute(null, str5, String.valueOf(iArr3[2]));
                                xmlSerializer.attribute(null, str14, String.valueOf(iArr3[3]));
                                xmlSerializer.endTag(null, "timewatermark");
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append("composeMainSubPicture: 3 offset = ");
                                sb6.append(length5);
                                sb6.append(str15);
                                sb6.append(bArr5.length);
                                Log.d(str6, sb6.toString());
                            } catch (IOException unused7) {
                                Log.e(str6, "composeMainSubPicture(): Failed to generate xiaomi specific xmp metadata");
                                str3 = null;
                                bArr10 = bArr;
                                if (str3 == null) {
                                }
                            }
                        } else {
                            bArr5 = bArr9;
                        }
                    } else {
                        bArr5 = bArr9;
                        bArr6 = bArr3;
                        String str16 = str4;
                    }
                    xmlSerializer.endDocument();
                    str3 = stringWriter.toString();
                    bArr10 = bArr;
                    if (str3 == null) {
                        return bArr10;
                    }
                    try {
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr10);
                        try {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            try {
                                XMPMeta createXMPMeta = XmpHelper.createXMPMeta();
                                createXMPMeta.setProperty(XmpHelper.XIAOMI_XMP_METADATA_NAMESPACE, XmpHelper.XIAOMI_XMP_METADATA_PROPERTY_NAME, str3);
                                XmpHelper.writeXMPMeta(byteArrayInputStream, byteArrayOutputStream, createXMPMeta);
                                if (z4) {
                                    File file5 = file;
                                    try {
                                        writeFile2Stream(file5, byteArrayOutputStream, 8);
                                        file5.delete();
                                        File file6 = file2;
                                        writeFile2Stream(file6, byteArrayOutputStream, 8);
                                        file6.delete();
                                    } catch (Throwable th2) {
                                        th = th2;
                                        bArr8 = null;
                                        try {
                                            throw th;
                                        } catch (Throwable th3) {
                                            th = th3;
                                            bArr7 = bArr8;
                                            Throwable th4 = th;
                                            throw th4;
                                        }
                                    }
                                }
                                if (z3) {
                                    if (bArr5 != null && bArr5.length > 0) {
                                        byteArrayOutputStream.write(bArr5);
                                    }
                                    if (bArr6 != null && bArr6.length > 0) {
                                        byteArrayOutputStream.write(bArr6);
                                    }
                                    byteArrayOutputStream.write(bArr11);
                                }
                                byteArrayOutputStream.flush();
                                bArr7 = byteArrayOutputStream.toByteArray();
                            } catch (Throwable th5) {
                                bArr8 = null;
                                th = th5;
                                throw th;
                            }
                            try {
                                $closeResource(null, byteArrayOutputStream);
                                $closeResource(null, byteArrayInputStream);
                                if (bArr7 == null && bArr7.length >= bArr10.length) {
                                    return bArr7;
                                }
                                str2 = "composeMainSubPicture(): Failed to append sub image, return original jpeg";
                            } catch (Throwable th6) {
                                th = th6;
                                Throwable th42 = th;
                                throw th42;
                            }
                        } catch (Throwable th7) {
                            th = th7;
                            bArr8 = null;
                            bArr7 = bArr8;
                            Throwable th422 = th;
                            throw th422;
                        }
                    } catch (XMPException | IOException unused8) {
                        bArr7 = null;
                        Log.d(str6, "composeMainSubPicture(): Failed to insert xiaomi specific xmp metadata");
                        if (bArr7 == null) {
                        }
                        str2 = "composeMainSubPicture(): Failed to append sub image, return original jpeg";
                        Log.e(str6, str2);
                        return bArr10;
                    }
                }
                bArr3 = frontCameraWatermarkData;
                if (str != null) {
                }
                iArr3 = iArr6;
                iArr2 = iArr5;
                bArr4 = null;
                try {
                    XmlSerializer newSerializer2 = Xml.newSerializer();
                    StringWriter stringWriter22 = new StringWriter();
                    newSerializer2.setOutput(stringWriter22);
                    newSerializer2.startDocument("UTF-8", Boolean.valueOf(true));
                    String str102 = "height";
                    String str112 = "width";
                    String str122 = Params.LENGTH;
                    String str132 = OnNativeInvokeListener.ARG_OFFSET;
                    if (!z4) {
                    }
                    if (!z3) {
                    }
                    xmlSerializer.endDocument();
                    str3 = stringWriter.toString();
                } catch (IOException unused9) {
                    file = file3;
                    file2 = file4;
                    bArr6 = bArr3;
                    bArr5 = bArr4;
                    Log.e(str6, "composeMainSubPicture(): Failed to generate xiaomi specific xmp metadata");
                    str3 = null;
                    bArr10 = bArr;
                    if (str3 == null) {
                    }
                }
                bArr10 = bArr;
                if (str3 == null) {
                }
            }
        } catch (IOException unused10) {
            str2 = "composeMainSubPicture(): Failed to check file type";
        }
        Log.e(str6, str2);
        return bArr10;
    }

    private static int computeInitialSampleSize(Options options, int i, int i2) {
        int i3;
        double d = (double) options.outWidth;
        double d2 = (double) options.outHeight;
        int ceil = i2 < 0 ? 1 : (int) Math.ceil(Math.sqrt((d * d2) / ((double) i2)));
        if (i < 0) {
            i3 = 128;
        } else {
            double d3 = (double) i;
            i3 = (int) Math.min(Math.floor(d / d3), Math.floor(d2 / d3));
        }
        if (i3 < ceil) {
            return ceil;
        }
        if (i2 >= 0 || i >= 0) {
            return i < 0 ? ceil : i3;
        }
        return 1;
    }

    public static int computeSampleSize(Options options, int i, int i2) {
        int computeInitialSampleSize = computeInitialSampleSize(options, i, i2);
        if (computeInitialSampleSize > 8) {
            return 8 * ((computeInitialSampleSize + 7) / 8);
        }
        int i3 = 1;
        while (i3 < computeInitialSampleSize) {
            i3 <<= 1;
        }
        return i3;
    }

    private static float computeScale(int i, int i2, float f) {
        double atan = Math.atan(((double) i) / ((double) i2));
        return (float) ((Math.sin(Math.toRadians(((double) normalizeDegree(f)) + Math.toDegrees(atan))) / Math.sin(atan)) + ((double) (10.0f / ((float) i))));
    }

    public static String controlAEStateToString(Integer num) {
        if (num == null) {
            return "null";
        }
        int intValue = num.intValue();
        if (intValue == 0) {
            return "inactive";
        }
        if (intValue == 1) {
            return "searching";
        }
        if (intValue == 2) {
            return "converged";
        }
        if (intValue == 3) {
            return "locked";
        }
        if (intValue == 4) {
            return "flash_required";
        }
        if (intValue == 5) {
            return "precapture";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("unknown: ");
        sb.append(num);
        return sb.toString();
    }

    public static String controlAFStateToString(Integer num) {
        if (num == null) {
            return "null";
        }
        switch (num.intValue()) {
            case 0:
                return "inactive";
            case 1:
                return "passive_scan";
            case 2:
                return "passive_focused";
            case 3:
                return "active_scan";
            case 4:
                return "focused_locked";
            case 5:
                return "not_focus_locked";
            case 6:
                return "passive_unfocused";
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("unknown: ");
                sb.append(num);
                return sb.toString();
        }
    }

    public static String controlAWBStateToString(Integer num) {
        if (num == null) {
            return "null";
        }
        int intValue = num.intValue();
        return intValue != 0 ? intValue != 1 ? intValue != 2 ? intValue != 3 ? "unknown" : "locked" : "converged" : "searching" : "inactive";
    }

    public static String convertOutputFormatToFileExt(int i) {
        return i == 2 ? ".mp4" : ".3gp";
    }

    public static final String convertOutputFormatToMimeType(int i) {
        return i == 2 ? "video/mp4" : "video/3gpp";
    }

    public static Bitmap convertResToBitmap(int i) {
        return BitmapFactory.decodeResource(CameraAppImpl.getAndroidContext().getResources(), i);
    }

    public static int convertSizeToQuality(CameraSize cameraSize) {
        int i = cameraSize.width;
        int i2 = cameraSize.height;
        if (i >= i2) {
            int i3 = i;
            i = i2;
            i2 = i3;
        }
        if (i2 == 1920 && i == 1080) {
            return 6;
        }
        if (i2 == 3840 && i == 2160) {
            return 8;
        }
        if (i2 == 1280 && i == 720) {
            return 5;
        }
        return (i2 < 640 || i != 480) ? -1 : 4;
    }

    public static void correctionSelectView(View view, boolean z) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof MarginLayoutParams) {
            ((MarginLayoutParams) layoutParams).setMargins(z ? 1 : 0, z, z, z);
        }
    }

    public static void coverSubYuvImage(byte[] bArr, int i, int i2, int i3, int i4, byte[] bArr2, int[] iArr) {
        int i5 = (iArr[1] * i3) + iArr[0];
        int i6 = 0;
        for (int i7 = 0; i7 < iArr[3]; i7++) {
            System.arraycopy(bArr2, i6, bArr, i5, iArr[2]);
            i6 += iArr[2];
            i5 += i3;
        }
        int i8 = (i3 * (i2 - 1)) + i + ((iArr[1] / 2) * i4) + iArr[0];
        for (int i9 = 0; i9 < iArr[3] / 2; i9++) {
            System.arraycopy(bArr2, i6, bArr, i8, iArr[2]);
            i8 += i4;
            i6 += iArr[2];
        }
    }

    public static boolean createFile(File file) {
        if (file.exists()) {
            return false;
        }
        String parent = file.getParent();
        if (parent != null) {
            mkdirs(new File(parent), FrameMetricsAggregator.EVERY_DURATION, -1, -1);
        }
        try {
            file.createNewFile();
        } catch (IOException unused) {
        }
        return true;
    }

    public static String createJpegName(long j) {
        String generateName;
        synchronized (sImageFileNamer) {
            generateName = sImageFileNamer.generateName(j);
        }
        return generateName;
    }

    public static MeteringRectangle createMeteringRectangleFrom(int i, int i2, int i3, int i4, int i5) {
        try {
            MeteringRectangle meteringRectangle = new MeteringRectangle(0, 0, 0, 0, 0);
            int i6 = i;
            try {
                modify(meteringRectangle, "mX", i);
                int i7 = i2;
            } catch (Exception unused) {
                int i8 = i2;
                int i9 = i3;
                int i10 = i4;
                int i11 = i5;
                MeteringRectangle meteringRectangle2 = new MeteringRectangle(i, i2, i3, i4, i5);
                return meteringRectangle2;
            }
            try {
                modify(meteringRectangle, "mY", i2);
                int i12 = i3;
            } catch (Exception unused2) {
                int i92 = i3;
                int i102 = i4;
                int i112 = i5;
                MeteringRectangle meteringRectangle22 = new MeteringRectangle(i, i2, i3, i4, i5);
                return meteringRectangle22;
            }
            try {
                modify(meteringRectangle, "mWidth", i3);
            } catch (Exception unused3) {
                int i1022 = i4;
                int i1122 = i5;
                MeteringRectangle meteringRectangle222 = new MeteringRectangle(i, i2, i3, i4, i5);
                return meteringRectangle222;
            }
            try {
                modify(meteringRectangle, "mHeight", i4);
            } catch (Exception unused4) {
                int i11222 = i5;
                MeteringRectangle meteringRectangle2222 = new MeteringRectangle(i, i2, i3, i4, i5);
                return meteringRectangle2222;
            }
            try {
                modify(meteringRectangle, "mWeight", i5);
                return meteringRectangle;
            } catch (Exception unused5) {
                MeteringRectangle meteringRectangle22222 = new MeteringRectangle(i, i2, i3, i4, i5);
                return meteringRectangle22222;
            }
        } catch (Exception unused6) {
            int i13 = i;
            int i82 = i2;
            int i922 = i3;
            int i10222 = i4;
            int i112222 = i5;
            MeteringRectangle meteringRectangle222222 = new MeteringRectangle(i, i2, i3, i4, i5);
            return meteringRectangle222222;
        }
    }

    public static MeteringRectangle createMeteringRectangleFrom(Rect rect, int i) {
        try {
            r4 = rect;
            r4 = createMeteringRectangleFrom(rect.left, rect.top, rect.width(), rect.height(), i);
            r4 = r4;
            return r4;
        } catch (Exception unused) {
            return new MeteringRectangle(r4, i);
        }
    }

    public static Bitmap cropBitmap(Bitmap bitmap, float f, boolean z, float f2, boolean z2, boolean z3) {
        int i;
        int i2;
        Bitmap bitmap2 = bitmap;
        String str = TAG;
        if (z || z2 || z3) {
            Bitmap bitmap3 = null;
            if (bitmap2 == null || bitmap.isRecycled()) {
                Log.w(str, "cropBitmap: bitmap is invalid!");
                return null;
            }
            Matrix matrix = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if (z) {
                int i3 = (f2 == 90.0f || f2 == 270.0f) ? 1 : -1;
                matrix.postScale(((float) i3) * 1.0f, ((float) (i3 * -1)) * 1.0f, ((float) width) / 2.0f, ((float) height) / 2.0f);
            }
            if (z2) {
                i2 = Math.min(width, height);
                matrix.postTranslate(((float) (i2 - width)) / 2.0f, ((float) (i2 - height)) / 2.0f);
                i = i2;
            } else {
                i = width;
                i2 = height;
            }
            if (z3) {
                i = (int) (((double) i2) / getCinematicAspectRatio());
                matrix.postTranslate(((float) (i - width)) / 2.0f, 0.0f);
            }
            try {
                bitmap3 = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap3);
                canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setFilterBitmap(true);
                canvas.drawBitmap(bitmap, matrix, paint);
                bitmap.recycle();
                if (z3) {
                    Bitmap createBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
                    Canvas canvas2 = new Canvas(createBitmap);
                    canvas2.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
                    Paint paint2 = new Paint();
                    paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
                    paint2.setAntiAlias(true);
                    paint2.setFilterBitmap(true);
                    canvas2.drawBitmap(bitmap3, ((float) (width - i)) / 2.0f, 0.0f, paint2);
                    bitmap3.recycle();
                    return createBitmap;
                }
            } catch (Exception | OutOfMemoryError e) {
                Log.w(str, "Failed to adjust bitmap", e);
            }
            return bitmap3;
        }
        Log.w(str, "cropBitmap: no effect!");
        return bitmap2;
    }

    public static void deleteFile(String str) {
        if (str != null) {
            if (!(Storage.isUseDocumentMode() ? FileCompat.deleteFile(str) : new File(str).delete())) {
                StringBuilder sb = new StringBuilder();
                sb.append("Failed to delete file: ");
                sb.append(str);
                Log.w(TAG, sb.toString());
            }
        }
    }

    public static float distance(float f, float f2, float f3, float f4) {
        float f5 = f - f3;
        float f6 = f2 - f4;
        return (float) Math.sqrt((double) ((f5 * f5) + (f6 * f6)));
    }

    private static Rational doubleToRational(double d, long j) {
        return new Rational((long) ((d * ((double) j)) + 0.5d), j);
    }

    public static int dpToPixel(float f) {
        return Math.round(Display.getPixelDensity() * f);
    }

    public static void drawMiMovieBlackBridge(Image image) {
        int height = (int) ((((double) image.getHeight()) - (((double) image.getWidth()) / getCinematicAspectRatio())) / 2.0d);
        Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        ByteBuffer buffer2 = planes[2].getBuffer();
        int[] iArr = {planes[0].getRowStride(), planes[2].getRowStride()};
        int limit = buffer.limit();
        int limit2 = buffer2.limit();
        for (int i = 0; i < planes[0].getRowStride() * height; i++) {
            buffer.put(i, 0);
        }
        for (int rowStride = limit - (planes[0].getRowStride() * height); rowStride < limit; rowStride++) {
            buffer.put(rowStride, 0);
        }
        for (int i2 = 0; i2 < (planes[1].getRowStride() * height) / 2; i2++) {
            buffer2.put(i2, Byte.MIN_VALUE);
        }
        for (int rowStride2 = limit2 - ((height * planes[1].getRowStride()) / 2); rowStride2 < limit2; rowStride2++) {
            buffer2.put(rowStride2, Byte.MIN_VALUE);
        }
    }

    public static void drawMiMovieBlackBridgeEGL(BaseGLCanvas baseGLCanvas, int i, int i2) {
        DrawFillRectAttribute drawFillRectAttribute;
        baseGLCanvas.getState().pushState();
        if (i < i2) {
            int cinematicAspectRatio = (int) ((((double) i) - (((double) i2) / getCinematicAspectRatio())) / 2.0d);
            int i3 = cinematicAspectRatio;
            int i4 = i2;
            DrawFillRectAttribute drawFillRectAttribute2 = new DrawFillRectAttribute(0, 0, i3, i4, ViewCompat.MEASURED_STATE_MASK);
            baseGLCanvas.draw(drawFillRectAttribute2);
            drawFillRectAttribute = new DrawFillRectAttribute(i - cinematicAspectRatio, 0, i3, i4, ViewCompat.MEASURED_STATE_MASK);
        } else {
            int cinematicAspectRatio2 = (int) ((((double) i2) - (((double) i) / getCinematicAspectRatio())) / 2.0d);
            int i5 = i;
            int i6 = cinematicAspectRatio2;
            DrawFillRectAttribute drawFillRectAttribute3 = new DrawFillRectAttribute(0, 0, i5, i6, ViewCompat.MEASURED_STATE_MASK);
            baseGLCanvas.draw(drawFillRectAttribute3);
            drawFillRectAttribute = new DrawFillRectAttribute(0, i2 - cinematicAspectRatio2, i5, i6, ViewCompat.MEASURED_STATE_MASK);
        }
        baseGLCanvas.draw(drawFillRectAttribute);
        baseGLCanvas.getState().popState();
    }

    public static void dumpBackTrace(String str) {
        StackTraceElement[] stackTrace;
        RuntimeException runtimeException = new RuntimeException();
        StringBuilder sb = new StringBuilder();
        String str2 = "[";
        sb.append(str2);
        sb.append(str);
        String str3 = "]\n";
        sb.append(str3);
        String sb2 = sb.toString();
        String str4 = TAG;
        Log.d(str4, sb2);
        Log.d(str4, "**********print backtrace start *************");
        for (StackTraceElement stackTraceElement : runtimeException.getStackTrace()) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str2);
            sb3.append(str);
            sb3.append("]:backtrace: ");
            sb3.append(stackTraceElement.getClassName());
            String str5 = " ";
            sb3.append(str5);
            sb3.append(stackTraceElement.getMethodName());
            sb3.append(str5);
            sb3.append(stackTraceElement.getLineNumber());
            Log.d(str4, sb3.toString());
        }
        Log.d(str4, "**********print backtrace end *************");
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str2);
        sb4.append(str);
        sb4.append(str3);
        Log.d(str4, sb4.toString());
    }

    public static void dumpBugReportLog() {
        if (sIsDumpBugReport) {
            long currentTimeMillis = System.currentTimeMillis();
            int i = ((currentTimeMillis - sLastDumpBugReportTime) > ((long) DUMP_BUG_REPORT_THRHOLD) ? 1 : ((currentTimeMillis - sLastDumpBugReportTime) == ((long) DUMP_BUG_REPORT_THRHOLD) ? 0 : -1));
            String str = TAG;
            if (i < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("dumpBugReportLog duration is smaller than ");
                sb.append(DUMP_BUG_REPORT_THRHOLD);
                Log.w(str, sb.toString());
                return;
            }
            sLastDumpBugReportTime = currentTimeMillis;
            Log.d(str, "dumpBugReportLog: start");
            Intent intent = new Intent();
            intent.setPackage("com.miui.bugreport");
            intent.setAction("com.miui.bugreport.service.action.DUMPLOG");
            CameraAppImpl.getAndroidContext().sendBroadcastAsUser(intent, Process.myUserHandle());
        }
    }

    public static void dumpImageInfo(String str, Image image) {
        StringBuilder sb = new StringBuilder();
        Plane[] planes = image.getPlanes();
        for (int i = 0; i < planes.length; i++) {
            Plane plane = planes[i];
            sb.append("plane_");
            sb.append(i);
            sb.append(": ");
            sb.append(plane.getPixelStride());
            String str2 = "|";
            sb.append(str2);
            sb.append(plane.getRowStride());
            sb.append(str2);
            sb.append(plane.getBuffer().remaining());
            sb.append("\n");
        }
        Log.d(str, sb.toString());
    }

    public static String dumpMatrix(float[] fArr) {
        int length = fArr.length;
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%f", new Object[]{Float.valueOf(fArr[i])}));
            if (i != length - 1) {
                sb.append(" ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void dumpRect(RectF rectF, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("=(");
        sb.append(rectF.left);
        String str2 = ",";
        sb.append(str2);
        sb.append(rectF.top);
        sb.append(str2);
        sb.append(rectF.right);
        sb.append(str2);
        sb.append(rectF.bottom);
        sb.append(")");
        Log.v(TAG, sb.toString());
    }

    public static ByteBuffer dumpToBitmap(int i, int i2, int i3, int i4, String str) {
        ByteBuffer allocate = ByteBuffer.allocate(i3 * i4 * 4);
        GLES20.glReadPixels(i, i2, i3, i4, 6408, 5121, allocate);
        String generateFilepath = Storage.generateFilepath(str, Storage.JPEG_SUFFIX);
        saveBitmap(allocate, i3, i4, Config.ARGB_8888, generateFilepath);
        StringBuilder sb = new StringBuilder();
        sb.append("dump to ");
        sb.append(generateFilepath);
        Log.d(TAG, sb.toString());
        allocate.rewind();
        return allocate;
    }

    public static void enableSeamlessRotation(Activity activity, boolean z) {
        LayoutParams attributes = activity.getWindow().getAttributes();
        attributes.rotationAnimation = z ? 4 : 5;
        StringBuilder sb = new StringBuilder();
        sb.append("setRotationAnimation: ");
        sb.append(attributes.rotationAnimation);
        Log.d(TAG, sb.toString());
        activity.getWindow().setAttributes(attributes);
    }

    public static void enterLightsOutMode(Window window) {
        LayoutParams attributes = window.getAttributes();
        attributes.systemUiVisibility |= 1;
        window.setAttributes(attributes);
    }

    public static boolean equals(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static String execCommand(String str, boolean z) {
        String str2 = TAG;
        String[] strArr = {"sh", "-c", str};
        long currentTimeMillis = System.currentTimeMillis();
        String str3 = "";
        try {
            Process exec = Runtime.getRuntime().exec(strArr);
            if (exec.waitFor() != 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("exit value = ");
                sb.append(exec.exitValue());
                Log.e(str2, sb.toString());
                return str3;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            if (!z) {
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    stringBuffer.append(readLine);
                }
            } else {
                while (true) {
                    String readLine2 = bufferedReader.readLine();
                    if (readLine2 == null) {
                        break;
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(readLine2);
                    sb2.append("\r\n");
                    stringBuffer.append(sb2.toString());
                }
            }
            bufferedReader.close();
            str3 = stringBuffer.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("execCommand value=");
            sb3.append(str3);
            sb3.append(" cost=");
            sb3.append(System.currentTimeMillis() - currentTimeMillis);
            Log.v(str2, sb3.toString());
            return str3;
        } catch (InterruptedException e) {
            Log.e(str2, "execCommand InterruptedException");
            e.printStackTrace();
        } catch (IOException e2) {
            Log.e(str2, "execCommand IOException");
            e2.printStackTrace();
        }
    }

    public static void expandViewTouchDelegate(View view) {
        if (view.isShown()) {
            Rect rect = new Rect();
            view.getHitRect(rect);
            int dpToPixel = dpToPixel(10.0f);
            rect.top -= dpToPixel;
            rect.bottom += dpToPixel;
            rect.left -= dpToPixel;
            rect.right += dpToPixel;
            TouchDelegate touchDelegate = new TouchDelegate(rect, view);
            if (View.class.isInstance(view.getParent())) {
                ((View) view.getParent()).setTouchDelegate(touchDelegate);
            }
        } else if (View.class.isInstance(view.getParent())) {
            ((View) view.getParent()).setTouchDelegate(null);
        }
    }

    public static void fadeIn(View view) {
        fadeIn(view, 400);
    }

    public static void fadeIn(View view, int i) {
        if (view != null && view.getVisibility() != 0) {
            view.setVisibility(0);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration((long) i);
            view.clearAnimation();
            view.startAnimation(alphaAnimation);
        }
    }

    public static void fadeOut(View view) {
        fadeOut(view, 400);
    }

    public static void fadeOut(View view, int i) {
        if (view != null && view.getVisibility() == 0) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration((long) i);
            view.clearAnimation();
            view.startAnimation(alphaAnimation);
            view.setVisibility(8);
        }
    }

    public static Bitmap flipBitmap(@NonNull Bitmap bitmap, int i) {
        Bitmap bitmap2 = null;
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        StringBuilder sb = new StringBuilder();
        sb.append("flipBitmap: ");
        sb.append(width);
        sb.append(" x ");
        sb.append(height);
        Log.d(TAG, sb.toString());
        Matrix matrix = new Matrix();
        if (i == 1) {
            matrix.postScale(1.0f, -1.0f, (float) (width / 2), (float) (height / 2));
        } else {
            matrix.postScale(-1.0f, 1.0f, (float) (width / 2), (float) (height / 2));
        }
        try {
            bitmap2 = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        } catch (NullPointerException | OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (bitmap2 == null) {
            return bitmap;
        }
        Canvas canvas = new Canvas(bitmap2);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, matrix, paint);
        bitmap.recycle();
        return bitmap2;
    }

    public static int fromByteArray(byte[] bArr) {
        return (bArr[3] & -1) | (bArr[0] << 24) | ((bArr[1] & -1) << 16) | ((bArr[2] & -1) << 8);
    }

    public static ContentValues genContentValues(int i, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("genContentValues: path=");
        sb.append(str);
        Log.v(TAG, sb.toString());
        ContentValues contentValues = new ContentValues(8);
        String fileName = FileUtils.getFileName(str);
        String convertOutputFormatToMimeType = convertOutputFormatToMimeType(i);
        contentValues.put("title", fileName);
        contentValues.put("_display_name", fileName);
        contentValues.put("mime_type", convertOutputFormatToMimeType);
        contentValues.put("_data", str);
        Location currentLocation = LocationManager.instance().getCurrentLocation();
        if (!(currentLocation == null || (currentLocation.getLatitude() == 0.0d && currentLocation.getLongitude() == 0.0d))) {
            contentValues.put("latitude", Double.valueOf(currentLocation.getLatitude()));
            contentValues.put("longitude", Double.valueOf(currentLocation.getLongitude()));
        }
        return contentValues;
    }

    public static ContentValues genContentValues(int i, String str, int i2, int i3) {
        String fileName = FileUtils.getFileName(str);
        String convertOutputFormatToMimeType = convertOutputFormatToMimeType(i);
        StringBuilder sb = new StringBuilder();
        sb.append("genContentValues: path=");
        sb.append(str);
        Log.v(TAG, sb.toString());
        ContentValues contentValues = new ContentValues(8);
        contentValues.put("title", fileName);
        contentValues.put("_display_name", fileName);
        contentValues.put("mime_type", convertOutputFormatToMimeType);
        contentValues.put("_data", str);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(i2);
        sb2.append("x");
        sb2.append(i3);
        contentValues.put("resolution", sb2.toString());
        Location currentLocation = LocationManager.instance().getCurrentLocation();
        if (!(currentLocation == null || (currentLocation.getLatitude() == 0.0d && currentLocation.getLongitude() == 0.0d))) {
            contentValues.put("latitude", Double.valueOf(currentLocation.getLatitude()));
            contentValues.put("longitude", Double.valueOf(currentLocation.getLongitude()));
        }
        return contentValues;
    }

    public static String genVideoPath(int i, String str) {
        String format = new SimpleDateFormat(str, Locale.ENGLISH).format(new Date(System.currentTimeMillis()));
        StringBuilder sb = new StringBuilder();
        sb.append(format);
        sb.append(convertOutputFormatToFileExt(i));
        return Storage.generateFilepath(sb.toString());
    }

    public static Bitmap generateCinematicRatioWatermark2File() {
        Options options = new Options();
        options.inScaled = false;
        options.inPurgeable = true;
        options.inPremultiplied = true;
        Bitmap drawBackWaterMark = CustomTextWaterMark.drawBackWaterMark(loadWatermarkIcon(CameraAppImpl.getAndroidContext(), C0122O00000o.instance().OO0oo() ? WaterMarkUtil.getBackIconNameGen2() : WaterMarkUtil.getBackIconName(), options), WaterMarkUtil.getTitle(), CameraSettings.getCustomWatermark(WaterMarkUtil.getDescription()), true);
        saveCustomWatermark2File(drawBackWaterMark, WATERMARK_CINEMATIC_RATIO_FILE_NAME);
        return drawBackWaterMark;
    }

    public static Bitmap generateFrontWatermark2File() {
        Bitmap loadFrontCameraWatermark = loadFrontCameraWatermark();
        saveCustomWatermark2File(loadFrontCameraWatermark, WATERMARK_FRONT_FILE_NAME);
        return loadFrontCameraWatermark;
    }

    public static Bitmap generateMainWatermark2File() {
        long currentTimeMillis = System.currentTimeMillis();
        Options options = new Options();
        options.inScaled = false;
        options.inPurgeable = true;
        options.inPremultiplied = true;
        if (!C0122O00000o.instance().OOo0ooO() && !C0122O00000o.instance().OOoOo0O()) {
            return null;
        }
        Bitmap drawBackWaterMark = CustomTextWaterMark.drawBackWaterMark(loadWatermarkIcon(CameraAppImpl.getAndroidContext(), C0122O00000o.instance().OO0oo() ? WaterMarkUtil.getBackIconNameGen2() : WaterMarkUtil.getBackIconName(), options), WaterMarkUtil.getTitle(), CameraSettings.getCustomWatermark(WaterMarkUtil.getDescription()), false);
        saveCustomWatermark2File(drawBackWaterMark, getDefaultWatermarkFileName());
        DataRepository.dataItemGlobal().updateCustomWatermarkVersion();
        StringBuilder sb = new StringBuilder();
        sb.append("generateWatermark2File cost time = ");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        sb.append(d.H);
        Log.d(TAG, sb.toString());
        return drawBackWaterMark;
    }

    public static void generateWatermark2File() {
        generateMainWatermark2File();
        generateCinematicRatioWatermark2File();
    }

    public static boolean get24HourMode(Context context) {
        return DateFormat.is24HourFormat(context);
    }

    public static int[] getAIWatermarkRange(int i, int i2, int[] iArr, float f, Rect rect) {
        int[] iArr2 = new int[4];
        if (Math.abs(i - ((i2 + 270) % m.cQ)) != 180) {
            iArr2[0] = (int) Math.ceil((double) (((float) iArr[1]) * f));
            iArr2[1] = (int) Math.ceil((double) (((float) ((rect.right - rect.left) - iArr[2])) * f));
            iArr2[2] = (int) Math.ceil((double) (((float) (iArr[3] - iArr[1])) * f));
            iArr2[3] = (int) Math.ceil((double) (((float) (iArr[2] - iArr[0])) * f));
        } else {
            iArr2[0] = (int) Math.ceil((double) (((float) ((rect.bottom - rect.top) - iArr[3])) * f));
            iArr2[1] = (int) Math.ceil((double) (((float) iArr[0]) * f));
            iArr2[2] = (int) Math.ceil((double) (((float) (iArr[3] - iArr[1])) * f));
            iArr2[3] = (int) Math.ceil((double) (((float) (iArr[2] - iArr[0])) * f));
        }
        iArr2[0] = (iArr2[0] / 2) * 2;
        iArr2[1] = (iArr2[1] / 2) * 2;
        iArr2[2] = (iArr2[2] / 4) * 4;
        iArr2[3] = (iArr2[3] / 4) * 4;
        return iArr2;
    }

    public static CameraSize getAlgorithmPreviewSize(List list, double d, CameraSize cameraSize) {
        if (cameraSize != null) {
            String str = TAG;
            if (list == null || list.isEmpty()) {
                Log.w(str, "null preview size list");
                return cameraSize;
            }
            int max = Math.max(SystemProperties.getInt("algorithm_limit_height", cameraSize.height), 500);
            Iterator it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                CameraSize cameraSize2 = (CameraSize) it.next();
                if (Math.abs((((double) cameraSize2.width) / ((double) cameraSize2.height)) - d) <= 0.02d) {
                    if (cameraSize2.height < max) {
                        cameraSize = cameraSize2;
                        break;
                    }
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("getAlgorithmPreviewSize: algorithmSize = ");
            sb.append(cameraSize);
            Log.d(str, sb.toString());
            return cameraSize;
        }
        throw new IllegalArgumentException("limitSize can not be null!");
    }

    public static int getArrayIndex(int[] iArr, int i) {
        if (iArr == null) {
            return -1;
        }
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (iArr[i2] == i) {
                return i2;
            }
        }
        return -1;
    }

    public static int getArrayIndex(Object[] objArr, Object obj) {
        if (objArr == null) {
            return -1;
        }
        int i = 0;
        for (Object equals : objArr) {
            if (Objects.equals(equals, obj)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private static int getAttributeIntValue(XmlPullParser xmlPullParser, String str, int i) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        if (TextUtils.isEmpty(attributeValue)) {
            return i;
        }
        try {
            return Integer.parseInt(attributeValue);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("get attribute ");
            sb.append(str);
            sb.append(" failed");
            Log.w(TAG, sb.toString(), (Throwable) e);
            return i;
        }
    }

    public static byte[] getBitmapData(Bitmap bitmap, int i) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, i, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Size getBitmapSize(int i) {
        Bitmap decodeResource = BitmapFactory.decodeResource(CameraAppImpl.getAndroidContext().getResources(), i);
        if (decodeResource == null) {
            return new Size(0, 0);
        }
        int width = decodeResource.getWidth();
        int height = decodeResource.getHeight();
        decodeResource.recycle();
        return new Size(width, height);
    }

    private static String getCaller(StackTraceElement[] stackTraceElementArr, int i) {
        int i2 = i + 4;
        if (i2 >= stackTraceElementArr.length) {
            return "<bottom of call stack>";
        }
        StackTraceElement stackTraceElement = stackTraceElementArr[i2];
        StringBuilder sb = new StringBuilder();
        sb.append(stackTraceElement.getClassName());
        sb.append(".");
        sb.append(stackTraceElement.getMethodName());
        sb.append(":");
        sb.append(stackTraceElement.getLineNumber());
        return sb.toString();
    }

    public static String getCallers(int i) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < i; i2++) {
            stringBuffer.append(getCaller(stackTrace, i2));
            stringBuffer.append(" ");
        }
        return stringBuffer.toString();
    }

    public static int getCenterFocusDepthIndex(byte[] bArr, int i, int i2) {
        if (bArr == null || bArr.length < 25) {
            return 1;
        }
        int length = bArr.length - 25;
        int i3 = length + 1;
        if (bArr[length] != 0) {
            return 1;
        }
        int i4 = i3 + 1;
        int i5 = i4 + 1;
        byte b = ((bArr[i4] & -1) << 16) | ((bArr[i3] & -1) << 24);
        int i6 = i5 + 1;
        byte b2 = b | ((bArr[i5] & -1) << 8);
        int i7 = i6 + 1;
        byte b3 = b2 | (bArr[i6] & -1);
        int i8 = i7 + 1;
        int i9 = i8 + 1;
        byte b4 = ((bArr[i8] & -1) << 16) | ((bArr[i7] & -1) << 24) | ((bArr[i9] & -1) << 8) | (bArr[i9 + 1] & -1);
        Resources resources = CameraAppImpl.getAndroidContext().getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.focus_area_width);
        int windowWidth = (dimensionPixelSize * b3) / Display.getWindowWidth();
        int dimensionPixelSize2 = (int) (((float) (resources.getDimensionPixelSize(R.dimen.focus_area_height) * b4)) / ((((float) Display.getWindowWidth()) * ((float) i2)) / ((float) i)));
        int[] iArr = new int[5];
        int i10 = 0;
        int i11 = (b4 - dimensionPixelSize2) / 2;
        int i12 = 0;
        while (i12 < dimensionPixelSize2) {
            int i13 = i11 + 1;
            int i14 = (i11 * b3) + ((b3 - windowWidth) / 2);
            int i15 = 0;
            while (i15 < windowWidth) {
                int i16 = i14 + 1;
                byte b5 = bArr[i14];
                iArr[b5] = iArr[b5] + 1;
                i15++;
                i14 = i16;
            }
            i12++;
            i11 = i13;
        }
        for (int i17 = 1; i17 < 5; i17++) {
            if (iArr[i10] < iArr[i17]) {
                i10 = i17;
            }
        }
        return i10;
    }

    public static int getChildMeasureWidth(View view) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        int i = marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
        int measuredWidth = view.getMeasuredWidth();
        if (measuredWidth > 0) {
            return measuredWidth + i;
        }
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
        view.measure(makeMeasureSpec, makeMeasureSpec);
        return view.getMeasuredWidth() + i;
    }

    public static double getCinematicAspectRatio() {
        return 2.39d;
    }

    public static int getCinematicAspectRatioMargin() {
        return (int) ((((double) Display.getWindowWidth()) - (((double) ((Display.getWindowWidth() * 16) / 9)) / 2.39d)) / 2.0d);
    }

    public static float getCinematicAspectWaterMarkRatio() {
        return C0122O00000o.instance().OO0oo() ? 1.0f : 0.86f;
    }

    private static File getColorMapXmlMapFile() {
        int i = VERSION.SDK_INT;
        String str = TAG;
        if (i >= 26) {
            File file = (!C0124O00000oO.O0o0 || !SystemProperties.get("ro.boot.hwc").equalsIgnoreCase("India")) ? new File("/vendor/etc/screen_light.xml") : new File("/vendor/etc/screen_light_ind.xml");
            if (file.exists()) {
                return file;
            }
            Log.e(str, "screen_light.xml do not found under /vendor/etc, roll back to /system/etc");
        }
        File file2 = new File("/system/etc/screen_light.xml");
        if (file2.exists()) {
            return file2;
        }
        Log.e(str, "screen_light.xml do not found under /system/etc");
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x003d A[SYNTHETIC, Splitter:B:25:0x003d] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0047 A[SYNTHETIC, Splitter:B:31:0x0047] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0056 A[SYNTHETIC, Splitter:B:38:0x0056] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:28:0x0042=Splitter:B:28:0x0042, B:22:0x0038=Splitter:B:22:0x0038} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String getContentFromFile(File file) {
        BufferedReader bufferedReader = null;
        if (file == null || !file.exists()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while (true) {
                try {
                    String readLine = bufferedReader2.readLine();
                    if (readLine != null) {
                        sb.append(readLine);
                    } else {
                        try {
                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (FileNotFoundException e2) {
                    e = e2;
                    bufferedReader = bufferedReader2;
                    e.printStackTrace();
                    if (bufferedReader != null) {
                    }
                    return sb.toString();
                } catch (IOException e3) {
                    e = e3;
                    bufferedReader = bufferedReader2;
                    try {
                        e.printStackTrace();
                        if (bufferedReader != null) {
                        }
                        return sb.toString();
                    } catch (Throwable th) {
                        th = th;
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = bufferedReader2;
                    if (bufferedReader != null) {
                    }
                    throw th;
                }
            }
            bufferedReader2.close();
        } catch (FileNotFoundException e5) {
            e = e5;
            e.printStackTrace();
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return sb.toString();
        } catch (IOException e6) {
            e = e6;
            e.printStackTrace();
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return sb.toString();
        }
        return sb.toString();
    }

    public static String getDebugInfo() {
        StringBuilder sb = new StringBuilder();
        String str = "1";
        if (str.equals(SystemProperties.get("persist.camera.debug.show_af")) || str.equals(SystemProperties.get("persist.camera.debug.enable"))) {
            sb.append(addProperties("persist.camera.debug.param0"));
            sb.append(addProperties("persist.camera.debug.param1"));
            sb.append(addProperties("persist.camera.debug.param2"));
            sb.append(addProperties("persist.camera.debug.param3"));
            sb.append(addProperties("persist.camera.debug.param4"));
            sb.append(addProperties("persist.camera.debug.param5"));
            sb.append(addProperties("persist.camera.debug.param6"));
            sb.append(addProperties("persist.camera.debug.param7"));
            sb.append(addProperties("persist.camera.debug.param8"));
            sb.append(addProperties("persist.camera.debug.param9"));
        }
        if (str.equals(SystemProperties.get("persist.camera.debug.show_awb"))) {
            sb.append(addProperties("persist.camera.debug.param10"));
            sb.append(addProperties("persist.camera.debug.param11"));
            sb.append(addProperties("persist.camera.debug.param12"));
            sb.append(addProperties("persist.camera.debug.param13"));
            sb.append(addProperties("persist.camera.debug.param14"));
            sb.append(addProperties("persist.camera.debug.param15"));
            sb.append(addProperties("persist.camera.debug.param16"));
            sb.append(addProperties("persist.camera.debug.param17"));
            sb.append(addProperties("persist.camera.debug.param18"));
            sb.append(addProperties("persist.camera.debug.param19"));
        }
        if (str.equals(SystemProperties.get("persist.camera.debug.show_aec"))) {
            sb.append(addProperties("persist.camera.debug.param20"));
            sb.append(addProperties("persist.camera.debug.param21"));
            sb.append(addProperties("persist.camera.debug.param22"));
            sb.append(addProperties("persist.camera.debug.param23"));
            sb.append(addProperties("persist.camera.debug.param24"));
            sb.append(addProperties("persist.camera.debug.param25"));
            sb.append(addProperties("persist.camera.debug.param26"));
            sb.append(addProperties("persist.camera.debug.param27"));
            sb.append(addProperties("persist.camera.debug.param28"));
            sb.append(addProperties("persist.camera.debug.param29"));
        }
        sb.append(addProperties("persist.camera.debug.checkerf"));
        sb.append(addProperties("persist.camera.debug.fc"));
        if (str.equals(SystemProperties.get("persist.camera.debug.hht"))) {
            sb.append(addProperties("camera.debug.hht.luma"));
        }
        if (str.equals(SystemProperties.get("persist.camera.debug.autoscene"))) {
            sb.append(addProperties("camera.debug.hht.iso"));
        }
        return sb.toString();
    }

    public static String getDebugInformation(CaptureResult captureResult, String str) {
        StringBuilder sb = new StringBuilder();
        AECFrameControl aECFrameControl = CaptureResultParser.getAECFrameControl(captureResult);
        AFFrameControl aFFrameControl = CaptureResultParser.getAFFrameControl(captureResult);
        String str2 = "1";
        if (!(!str2.equals(SystemProperties.get("camera.preview.debug.show_shortGain")) || aECFrameControl == null || aECFrameControl.getAecExposureDatas() == null)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("short gain : ");
            sb2.append(aECFrameControl.getAecExposureDatas()[0].getLinearGain());
            sb.append(addDebugInfo(sb2.toString()));
        }
        if (!(!str2.equals(SystemProperties.get("camera.preview.debug.show_adrcGain")) || aECFrameControl == null || aECFrameControl.getAecExposureDatas() == null)) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("adrc gain : ");
            sb3.append(aECFrameControl.getAecExposureDatas()[2].getSensitivity() / aECFrameControl.getAecExposureDatas()[0].getSensitivity());
            sb.append(addDebugInfo(sb3.toString()));
        }
        if (str2.equals(SystemProperties.get("camera.preview.debug.show_afRegion"))) {
            MeteringRectangle[] meteringRectangleArr = (MeteringRectangle[]) captureResult.get(CaptureResult.CONTROL_AF_REGIONS);
            if (meteringRectangleArr != null) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("af region : ");
                sb4.append(meteringRectangleArr[0].getRect().toString());
                sb.append(addDebugInfo(sb4.toString()));
            }
        }
        if (str2.equals(SystemProperties.get("camera.preview.debug.show_afMode"))) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("af mode : ");
            sb5.append(captureResult.get(CaptureResult.CONTROL_AF_MODE));
            sb.append(addDebugInfo(sb5.toString()));
        }
        if (str2.equals(SystemProperties.get("camera.preview.debug.show_afStatus"))) {
            StringBuilder sb6 = new StringBuilder();
            sb6.append("af state : ");
            sb6.append(captureResult.get(CaptureResult.CONTROL_AF_STATE));
            sb.append(addDebugInfo(sb6.toString()));
        }
        if (str2.equals(SystemProperties.get("camera.preview.debug.show_afLensPosition")) && aFFrameControl != null) {
            String str3 = "";
            if (aFFrameControl.getUseDACValue() == 0) {
                StringBuilder sb7 = new StringBuilder();
                sb7.append(aFFrameControl.getTargetLensPosition());
                sb7.append(str3);
                str3 = sb7.toString();
            }
            StringBuilder sb8 = new StringBuilder();
            sb8.append("af lens position : ");
            sb8.append(str3);
            sb.append(addDebugInfo(sb8.toString()));
        }
        if (str2.equals(SystemProperties.get("camera.preview.debug.show_distance")) && captureResult.get(CaptureResult.LENS_FOCUS_DISTANCE) != null) {
            float floatValue = ((Float) captureResult.get(CaptureResult.LENS_FOCUS_DISTANCE)).floatValue();
            StringBuilder sb9 = new StringBuilder();
            sb9.append("distance : ");
            sb9.append(floatValue);
            sb.append(addDebugInfo(sb9.toString()));
            StringBuilder sb10 = new StringBuilder();
            sb10.append("distance(m) : ");
            sb10.append(1.0f / floatValue);
            sb.append(addDebugInfo(sb10.toString()));
        }
        if (str2.equals(SystemProperties.get("camera.preview.debug.show_gyro")) && aFFrameControl != null) {
            for (int i = 0; i < aFFrameControl.getAFGyroData().getSampleCount(); i++) {
                StringBuilder sb11 = new StringBuilder();
                sb11.append("gyro : x: ");
                sb11.append(aFFrameControl.getAFGyroData().getpAngularVelocityX()[i]);
                sb11.append(", y: ");
                sb11.append(aFFrameControl.getAFGyroData().getpAngularVelocityY()[i]);
                sb11.append(", z: ");
                sb11.append(aFFrameControl.getAFGyroData().getpAngularVelocityZ()[i]);
                sb.append(addDebugInfo(sb11.toString()));
            }
        }
        if (str2.equals(SystemProperties.get("camera.preview.debug.sat_info"))) {
            byte[] satDbgInfo = CaptureResultParser.getSatDbgInfo(captureResult);
            if (satDbgInfo != null) {
                sb.append(addDebugInfo(new String(satDbgInfo)));
            }
        }
        if (str2.equals(SystemProperties.get("camera.preview.debug.xp_content"))) {
            byte[] exifValues = CaptureResultParser.getExifValues(captureResult);
            String str4 = "exifInfoString";
            if (exifValues != null && exifValues.length > 0) {
                String str5 = new String(exifValues);
                StringBuilder sb12 = new StringBuilder();
                sb12.append("exifString:");
                sb12.append(str5);
                Log.i(str4, sb12.toString());
                sb.append(str5);
            }
            if (str != null) {
                StringBuilder sb13 = new StringBuilder();
                sb13.append("exifInfoString:");
                sb13.append(str);
                Log.i(str4, sb13.toString());
                sb.append(str);
            }
        }
        return sb.toString();
    }

    public static String getDefaultWatermarkFileName() {
        if (!C0122O00000o.instance().OO0oo0o() || !C0122O00000o.instance().OOoOo0O()) {
            return WATERMARK_DEFAULT_FILE_NAME;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(android.os.Build.DEVICE);
        sb.append("_");
        sb.append(WATERMARK_SPACE);
        sb.append("_custom_watermark.png");
        return sb.toString();
    }

    public static int getDisplayOrientation(int i, int i2) {
        CameraCapabilities capabilities = Camera2DataContainer.getInstance().getCapabilities(i2);
        if (capabilities == null) {
            return 90;
        }
        int sensorOrientation = capabilities.getSensorOrientation();
        return capabilities.getFacing() == 0 ? (360 - ((sensorOrientation + i) % m.cQ)) % m.cQ : ((sensorOrientation - i) + m.cQ) % m.cQ;
    }

    public static Rect getDisplayRect() {
        return getDisplayRect(DataRepository.dataItemRunning().getUiStyle());
    }

    public static Rect getDisplayRect(int i) {
        return Display.getDisplayRect(i);
    }

    public static int getDisplayRotation(Activity activity) {
        int i;
        if (!C0124O00000oO.OOooO0o() || !CameraSettings.isFrontCamera() || activity.getRequestedOrientation() != 7) {
            int i2 = mLockedOrientation;
            i = (i2 == 0 || i2 == 2) ? mLockedOrientation : 0;
        } else {
            i = activity.getWindowManager().getDefaultDisplay().getRotation();
        }
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return 90;
        }
        if (i != 2) {
            return i != 3 ? 0 : 270;
        }
        return 180;
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x007c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] getDualCameraWatermarkData(int i, int i2, int[] iArr, int i3, DeviceWatermarkParam deviceWatermarkParam) {
        String str;
        byte[] bArr;
        Bitmap decodeByteArray;
        Throwable th;
        int[] iArr2 = iArr;
        if (C0122O00000o.instance().OOo0ooO() || C0122O00000o.instance().OOoOo0O()) {
            boolean isCinematicAspectRatio = deviceWatermarkParam.isCinematicAspectRatio();
            String path = new File(CameraAppImpl.getAndroidContext().getFilesDir(), getWatermarkFileName(isCinematicAspectRatio)).getPath();
            if (!new File(path).exists()) {
                if (isCinematicAspectRatio) {
                    generateCinematicRatioWatermark2File();
                } else {
                    generateMainWatermark2File();
                }
            }
            str = path;
        } else {
            str = CameraSettings.getDualCameraWaterMarkFilePathVendor();
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            try {
                bArr = IOUtils.toByteArray((InputStream) fileInputStream);
                try {
                    $closeResource(null, fileInputStream);
                } catch (IOException e) {
                    e = e;
                }
                if (!(bArr == null || iArr2 == null || iArr2.length < 4)) {
                    decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
                    if (decodeByteArray != null) {
                        ImageWaterMark imageWaterMark = new ImageWaterMark(decodeByteArray, i, i2, i3, deviceWatermarkParam.getSize(), deviceWatermarkParam.getPaddingX(), deviceWatermarkParam.getPaddingY(), deviceWatermarkParam.isCinematicAspectRatio());
                        iArr2[0] = imageWaterMark.getWidth();
                        iArr2[1] = imageWaterMark.getHeight();
                        iArr2[2] = imageWaterMark.getPaddingX();
                        iArr2[3] = imageWaterMark.getPaddingY();
                    }
                }
                return bArr;
            } catch (Throwable th2) {
                Throwable th3 = th2;
                $closeResource(th, fileInputStream);
                throw th3;
            }
        } catch (IOException e2) {
            e = e2;
            bArr = null;
            Log.d(TAG, "Failed to load dual camera water mark", (Throwable) e);
            decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
            if (decodeByteArray != null) {
            }
            return bArr;
        }
    }

    public static Float getDumpCropFrontZoomRatio() {
        if (sDumpCropFrontZoomRatio == null) {
            sDumpCropFrontZoomRatio = Float.valueOf(com.xiaomi.camera.util.SystemProperties.getFloat("camera.debug.cropFrontZoomRatio", 0.0f));
        }
        return sDumpCropFrontZoomRatio;
    }

    /* JADX INFO: finally extract failed */
    public static long getDuration(FileDescriptor fileDescriptor) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(fileDescriptor);
            long parseLong = Long.parseLong(mediaMetadataRetriever.extractMetadata(9));
            mediaMetadataRetriever.release();
            return parseLong;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "IllegalArgumentException", (Throwable) e);
            mediaMetadataRetriever.release();
            return 0;
        } catch (Throwable th) {
            mediaMetadataRetriever.release();
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    public static long getDuration(String str) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(str);
            long parseLong = Long.parseLong(mediaMetadataRetriever.extractMetadata(9));
            mediaMetadataRetriever.release();
            return parseLong;
        } catch (Exception e) {
            Log.e(TAG, "getDuration Exception", (Throwable) e);
            mediaMetadataRetriever.release();
            return 0;
        } catch (Throwable th) {
            mediaMetadataRetriever.release();
            throw th;
        }
    }

    public static Set getEnabledServicesFromSettings(Context context) {
        return getEnabledServicesFromSettings(context, UserHandle.myUserId());
    }

    public static Set getEnabledServicesFromSettings(Context context, int i) {
        String stringForUser = Secure.getStringForUser(context.getContentResolver(), "enabled_accessibility_services", i);
        if (TextUtils.isEmpty(stringForUser)) {
            return Collections.emptySet();
        }
        HashSet hashSet = new HashSet();
        SimpleStringSplitter<String> simpleStringSplitter = new SimpleStringSplitter<>(ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        simpleStringSplitter.setString(stringForUser);
        for (String unflattenFromString : simpleStringSplitter) {
            ComponentName unflattenFromString2 = ComponentName.unflattenFromString(unflattenFromString);
            if (unflattenFromString2 != null) {
                hashSet.add(unflattenFromString2);
            }
        }
        return hashSet;
    }

    public static DecimalFormat getEnglishDecimalFormat() {
        DecimalFormat decimalFormat = new DecimalFormat(b.m);
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        return decimalFormat;
    }

    public static long getEnterDuration() {
        return SystemProperties.getLong("enter.duration", 500);
    }

    public static ExifInterface getExif(String str) {
        ExifInterface exifInterface = new ExifInterface();
        try {
            exifInterface.readExif(str);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
        return exifInterface;
    }

    public static ExifInterface getExif(byte[] bArr) {
        ExifInterface exifInterface = new ExifInterface();
        try {
            exifInterface.readExif(bArr);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
        return exifInterface;
    }

    public static long getExitDuration() {
        return SystemProperties.getLong("exit.duration", 500);
    }

    public static Typeface getFZMiaoWuJWTypeface(Context context) {
        return getTypefaceFromFile(context, "vendor/camera/fonts/FZMiaoWuJW.ttf");
    }

    public static String getFileTitleFromPath(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int lastIndexOf = str.lastIndexOf("/");
        if (lastIndexOf < 0 || lastIndexOf >= str.length() - 1) {
            return null;
        }
        String substring = str.substring(lastIndexOf + 1);
        if (TextUtils.isEmpty(substring)) {
            return null;
        }
        int indexOf = substring.indexOf(".");
        if (indexOf >= 0) {
            substring = substring.substring(0, indexOf);
        }
        return substring;
    }

    public static String getFilesState(String str) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("ExternalStorageState:");
        sb2.append(Environment.getExternalStorageState());
        String str2 = ";";
        sb2.append(str2);
        sb.append(sb2.toString());
        while (!TextUtils.isEmpty(str) && !str.equals("/")) {
            File file = new File(str);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("path:");
            sb3.append(file.getPath());
            sb3.append(str2);
            sb.append(sb3.toString());
            StringBuilder sb4 = new StringBuilder();
            sb4.append("exists:");
            sb4.append(file.exists());
            sb4.append(str2);
            sb.append(sb4.toString());
            StringBuilder sb5 = new StringBuilder();
            sb5.append("canWrite:");
            sb5.append(file.canWrite());
            sb5.append(str2);
            sb.append(sb5.toString());
            StringBuilder sb6 = new StringBuilder();
            sb6.append("canRead:");
            sb6.append(file.canRead());
            sb6.append(str2);
            sb.append(sb6.toString());
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                str = parentFile.getPath();
            }
        }
        return sb.toString();
    }

    public static byte[] getFirstPlane(Image image) {
        Plane[] planes = image.getPlanes();
        if (planes.length <= 0) {
            return null;
        }
        ByteBuffer buffer = planes[0].getBuffer();
        byte[] bArr = new byte[buffer.remaining()];
        buffer.get(bArr);
        return bArr;
    }

    public static long getFlickerDuration() {
        return SystemProperties.getLong("flicker.duration", 400);
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0043  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] getFrontCameraWatermarkData(int i, int i2, int[] iArr, int i3, DeviceWatermarkParam deviceWatermarkParam) {
        byte[] bArr;
        Bitmap decodeByteArray;
        Throwable th;
        int[] iArr2 = iArr;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(CameraAppImpl.getAndroidContext().getFilesDir(), WATERMARK_FRONT_FILE_NAME));
            try {
                bArr = IOUtils.toByteArray((InputStream) fileInputStream);
                try {
                    $closeResource(null, fileInputStream);
                } catch (IOException e) {
                    e = e;
                }
            } catch (Throwable th2) {
                Throwable th3 = th2;
                $closeResource(th, fileInputStream);
                throw th3;
            }
        } catch (IOException e2) {
            e = e2;
            bArr = null;
            Log.d(TAG, "Failed to load front camera water mark", (Throwable) e);
            decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
            if (decodeByteArray != null) {
            }
            return bArr;
        }
        if (!(bArr == null || iArr2 == null || iArr2.length < 4)) {
            decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
            if (decodeByteArray != null) {
                ImageWaterMark imageWaterMark = new ImageWaterMark(decodeByteArray, i, i2, i3, deviceWatermarkParam.getSize(), deviceWatermarkParam.getPaddingX(), deviceWatermarkParam.getPaddingY(), deviceWatermarkParam.isCinematicAspectRatio());
                iArr2[0] = imageWaterMark.getWidth();
                iArr2[1] = imageWaterMark.getHeight();
                iArr2[2] = imageWaterMark.getPaddingX();
                iArr2[3] = imageWaterMark.getPaddingY();
            }
        }
        return bArr;
    }

    public static int getHeader2Int(File file, int i) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bArr = new byte[4];
            fileInputStream.skip((long) i);
            fileInputStream.read(bArr);
            fileInputStream.close();
            return fromByteArray(bArr);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getIntField(String str, Object obj, String str2, String str3) {
        String str4 = TAG;
        try {
            r1 = str;
            r1 = Field.of(str, str2, str3).getInt(obj);
            r1 = r1;
            return r1;
        } catch (NoSuchClassException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("no class ");
            sb.append(r1);
            Log.e(str4, sb.toString(), (Throwable) e);
            return Integer.MIN_VALUE;
        } catch (NoSuchFieldException e2) {
            Log.e(str4, "no field ", (Throwable) e2);
            return Integer.MIN_VALUE;
        }
    }

    public static int getJpegRotation(int i, int i2) {
        CameraCapabilities capabilities = Camera2DataContainer.getInstance().getCapabilities(i);
        if (capabilities == null) {
            return 90;
        }
        int sensorOrientation = capabilities.getSensorOrientation();
        if (i2 != -1) {
            return (capabilities.getFacing() == 0 ? (sensorOrientation - i2) + m.cQ : sensorOrientation + i2) % m.cQ;
        }
        Log.w(TAG, "getJpegRotation: orientation UNKNOWN!!! return sensorOrientation...");
        return sensorOrientation;
    }

    public static int getLength(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        return str.length() * 2;
    }

    public static RectF getLocationOnScreen(View view) {
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        int i = iArr[0];
        int i2 = iArr[1];
        return new RectF((float) i, (float) i2, (float) (view.getWidth() + i), (float) (view.getHeight() + i2));
    }

    public static miui.reflect.Method getMethod(Class[] clsArr, String str, String str2) {
        miui.reflect.Method method = null;
        if (clsArr != null) {
            try {
                if (clsArr.length == 1) {
                    method = miui.reflect.Method.of(clsArr[0], str, str2);
                }
            } catch (NoSuchMethodException unused) {
                if (clsArr[0].getSuperclass() != null) {
                    clsArr[0] = clsArr[0].getSuperclass();
                    method = getMethod(clsArr, str, str2);
                }
            }
        }
        if (method == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("getMethod fail, ");
            sb.append(str);
            sb.append("[");
            sb.append(str2);
            sb.append("]");
            Log.e(TAG, sb.toString());
        }
        return method;
    }

    public static final String getMimeType(File file) {
        int lastIndexOf = file.getName().lastIndexOf(46);
        if (lastIndexOf < 0) {
            return null;
        }
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.getName().substring(lastIndexOf + 1));
    }

    public static Typeface getMiuiTypeface() {
        return Typeface.create("mipro-regular", 0);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.camera.CameraSize>, for r14v0, types: [java.util.List, java.util.List<com.android.camera.CameraSize>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static CameraSize getOptimalJpegThumbnailSize(List<CameraSize> list, double d) {
        CameraSize cameraSize;
        String str = TAG;
        CameraSize cameraSize2 = null;
        if (list == null) {
            Log.w(str, "null thumbnail size list");
            return null;
        }
        double d2 = 0.0d;
        for (CameraSize cameraSize3 : list) {
            if (cameraSize3.getWidth() != 0) {
                if (cameraSize3.getHeight() != 0) {
                    double width = ((double) cameraSize3.getWidth()) / ((double) cameraSize3.getHeight());
                    double abs = Math.abs(width - d);
                    double d3 = d2 - d;
                    if (abs <= Math.abs(d3) || abs <= 0.001d) {
                        if (cameraSize2 == null || abs < Math.abs(d3) || cameraSize3.getWidth() > cameraSize2.getWidth()) {
                            cameraSize2 = cameraSize3;
                            d2 = width;
                        }
                    }
                }
            }
        }
        if (cameraSize2 == null) {
            Log.w(str, "No thumbnail size match the aspect ratio");
            for (CameraSize cameraSize4 : list) {
                if (cameraSize2 == null || cameraSize4.getWidth() > cameraSize2.getWidth()) {
                    cameraSize2 = cameraSize4;
                }
            }
        }
        return cameraSize;
    }

    public static CameraSize getOptimalPreviewSize(boolean z, int i, List list, double d) {
        return getOptimalPreviewSize(z, i, list, d, null);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.camera.CameraSize>, for r22v0, types: [java.util.List, java.util.List<com.android.camera.CameraSize>] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x004b  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x007a  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0080  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x008a  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00c4  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0193  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x0196  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x01e1  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x018e A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static CameraSize getOptimalPreviewSize(boolean z, int i, List<CameraSize> list, double d, CameraSize cameraSize) {
        boolean z2;
        Point point;
        int i2;
        Iterator it;
        Point point2;
        CameraSize cameraSize2;
        CameraSize cameraSize3;
        CameraSize cameraSize4 = cameraSize;
        CameraSize cameraSize5 = null;
        String str = TAG;
        if (list == null) {
            Log.w(str, "null preview size list");
            return null;
        }
        int Oo0O0o0 = C0124O00000oO.Oo0O0o0();
        int i3 = 1080;
        if (Oo0O0o0 != 0) {
            boolean z3 = i == Camera2DataContainer.getInstance().getFrontCameraId();
            if (Display.getWindowWidth() < 1080) {
                Oo0O0o0 &= -15;
            }
            if ((Oo0O0o0 & (((z3 ? 2 : 1) << (!z ? 0 : 2)) | 0)) != 0) {
                z2 = true;
                int windowWidth = Display.getWindowWidth();
                int windowHeight = Display.getWindowHeight();
                if (z2) {
                    windowHeight = Math.min(windowHeight, 1920);
                }
                point = new Point(windowWidth, windowHeight);
                if (C0122O00000o.instance().isPad() && Math.abs(1.7777777910232544d - d) <= 0.02d) {
                    i3 = 1860;
                }
                if (C0124O00000oO.Oo0O0()) {
                    i3 = LIMIT_SURFACE_WIDTH;
                }
                i2 = point.x;
                if (i2 > i3) {
                    point.y = (point.y * i3) / i2;
                    point.x = i3;
                }
                if (cameraSize4 != null) {
                    if (point.x > cameraSize4.height || point.y > cameraSize4.width) {
                        double d2 = ((double) point.y) / ((double) point.x);
                        int i4 = cameraSize4.width;
                        int i5 = cameraSize4.height;
                        if (i4 <= i5) {
                            i5 = i4;
                        }
                        point.x = i5;
                        point.y = (int) (d2 * ((double) point.x));
                    }
                    z2 = false;
                }
                it = list.iterator();
                CameraSize cameraSize6 = null;
                double d3 = Double.MAX_VALUE;
                double d4 = Double.MAX_VALUE;
                while (true) {
                    if (it.hasNext()) {
                        point2 = point;
                        cameraSize2 = cameraSize6;
                        break;
                    }
                    cameraSize2 = (CameraSize) it.next();
                    Point point3 = point;
                    double d5 = ((double) cameraSize2.width) / ((double) cameraSize2.height);
                    StringBuilder sb = new StringBuilder();
                    sb.append("getOptimalPreviewSize: height:");
                    sb.append(cameraSize2.height);
                    sb.append(" width: ");
                    sb.append(cameraSize2.width);
                    sb.append(" ratio:");
                    sb.append(d5);
                    Log.e(str, sb.toString());
                    if (((cameraSize4 == null || Math.min(cameraSize4.width, cameraSize4.height) > 500) && Math.min(cameraSize2.width, cameraSize2.height) < 500) || Math.abs(d5 - d) > 0.02d) {
                        point2 = point3;
                    } else {
                        point2 = point3;
                        if (!z2 || (point2.x > cameraSize2.height && point2.y > cameraSize2.width)) {
                            int abs = Math.abs(point2.x - cameraSize2.height) + Math.abs(point2.y - cameraSize2.width);
                            if (abs == 0) {
                                cameraSize5 = cameraSize2;
                                break;
                            }
                            if (cameraSize2.height <= point2.x && cameraSize2.width <= point2.y) {
                                double d6 = (double) abs;
                                if (d6 < d3) {
                                    d3 = d6;
                                    cameraSize6 = cameraSize2;
                                }
                            }
                            double d7 = (double) abs;
                            if (d7 < d4) {
                                d4 = d7;
                                cameraSize5 = cameraSize2;
                            }
                        } else {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("getOptimalPreviewSize: ");
                            sb2.append(cameraSize2.toString());
                            sb2.append(" | ");
                            sb2.append(point2.toString());
                            Log.e(str, sb2.toString());
                        }
                    }
                    point = point2;
                }
                if (cameraSize2 == null) {
                    cameraSize2 = cameraSize5;
                }
                if (cameraSize2 == null) {
                    Log.w(str, String.format(Locale.ENGLISH, "no preview size match the aspect ratio: %.2f", new Object[]{Double.valueOf(d)}));
                    double d8 = Double.MAX_VALUE;
                    for (CameraSize cameraSize7 : list) {
                        double abs2 = (double) (Math.abs(point2.x - cameraSize7.getHeight()) + Math.abs(point2.y - cameraSize7.getWidth()));
                        if (abs2 < d8) {
                            cameraSize2 = cameraSize7;
                            d8 = abs2;
                        }
                    }
                }
                if (cameraSize3 != null) {
                    Log.i(str, String.format(Locale.ENGLISH, "best preview size: %dx%d", new Object[]{Integer.valueOf(cameraSize3.getWidth()), Integer.valueOf(cameraSize3.getHeight())}));
                }
                return cameraSize3;
            }
        }
        z2 = false;
        int windowWidth2 = Display.getWindowWidth();
        int windowHeight2 = Display.getWindowHeight();
        if (z2) {
        }
        point = new Point(windowWidth2, windowHeight2);
        i3 = 1860;
        if (C0124O00000oO.Oo0O0()) {
        }
        i2 = point.x;
        if (i2 > i3) {
        }
        if (cameraSize4 != null) {
        }
        it = list.iterator();
        CameraSize cameraSize62 = null;
        double d32 = Double.MAX_VALUE;
        double d42 = Double.MAX_VALUE;
        while (true) {
            if (it.hasNext()) {
            }
            point = point2;
        }
        if (cameraSize2 == null) {
        }
        if (cameraSize2 == null) {
        }
        if (cameraSize3 != null) {
        }
        return cameraSize3;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.camera.CameraSize>, for r8v0, types: [java.util.List, java.util.List<com.android.camera.CameraSize>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static CameraSize getOptimalVideoSnapshotPictureSize(List<CameraSize> list, double d, int i, int i2) {
        CameraSize cameraSize;
        String str = TAG;
        CameraSize cameraSize2 = null;
        if (list == null) {
            Log.e(str, "null size list");
            return null;
        }
        for (CameraSize cameraSize3 : list) {
            if (Math.abs((((double) cameraSize3.getWidth()) / ((double) cameraSize3.getHeight())) - d) <= 0.02d) {
                if ((cameraSize2 == null || cameraSize3.getWidth() > cameraSize2.getWidth()) && cameraSize3.getWidth() <= i && cameraSize3.getHeight() <= i2) {
                    cameraSize2 = cameraSize3;
                }
            }
        }
        if (cameraSize2 == null) {
            Log.w(str, "No picture size match the aspect ratio");
            for (CameraSize cameraSize4 : list) {
                if (cameraSize2 == null || cameraSize4.getWidth() > cameraSize2.getWidth()) {
                    cameraSize2 = cameraSize4;
                }
            }
        }
        return cameraSize;
    }

    public static long getPhoneMemorySize() {
        return Process.getTotalMemory() >> 30;
    }

    public static byte[] getPixels(byte[] bArr, int i, int i2, int[] iArr) {
        if (bArr == null) {
            return null;
        }
        byte[] bArr2 = new byte[(iArr[2] * iArr[3] * i2)];
        int i3 = ((iArr[1] * i) + iArr[0]) * i2;
        int i4 = 0;
        for (int i5 = 0; i5 < iArr[3]; i5++) {
            System.arraycopy(bArr, i3, bArr2, i4, iArr[2] * i2);
            i3 += i * i2;
            i4 += iArr[2] * i2;
        }
        return bArr2;
    }

    public static Rect getPreviewRect(Context context) {
        int uiStyle = DataRepository.dataItemRunning().getUiStyle();
        Rect displayRect = getDisplayRect(uiStyle);
        if (uiStyle == 3 && Display.isNotchDevice() && (C0122O00000o.instance().OOOooOO() || C0122O00000o.instance().OOOooo0())) {
            displayRect.top = 0;
        }
        return displayRect;
    }

    public static int[] getRange(int i, int i2, int i3) {
        int[] iArr = {0, 0, (int) (((double) i) * 0.3d), i2};
        iArr[0] = (iArr[0] / 2) * 2;
        iArr[1] = (iArr[1] / 2) * 2;
        iArr[2] = (iArr[2] / 4) * 4;
        iArr[3] = (iArr[3] / 4) * 4;
        return iArr;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static float getRatio(String str) {
        char c;
        switch (str.hashCode()) {
            case -2109552250:
                if (str.equals(ComponentConfigRatio.RATIO_FULL_18_7_5X9)) {
                    c = 6;
                    break;
                }
            case 50858:
                if (str.equals(ComponentConfigRatio.RATIO_1X1)) {
                    c = 2;
                    break;
                }
            case 53743:
                if (str.equals(ComponentConfigRatio.RATIO_4X3)) {
                    c = 0;
                    break;
                }
            case 1515430:
                if (str.equals(ComponentConfigRatio.RATIO_16X9)) {
                    c = 1;
                    break;
                }
            case 1517352:
                if (str.equals(ComponentConfigRatio.RATIO_FULL_18X9)) {
                    c = 3;
                    break;
                }
            case 1518313:
                if (str.equals(ComponentConfigRatio.RATIO_FULL_19X9)) {
                    c = 4;
                    break;
                }
            case 1539455:
                if (str.equals(ComponentConfigRatio.RATIO_FULL_20X9)) {
                    c = 7;
                    break;
                }
            case 1456894192:
                if (str.equals(ComponentConfigRatio.RATIO_FULL_195X9)) {
                    c = 5;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return 1.3333333f;
            case 1:
                return 1.7777777f;
            case 2:
                return 1.0f;
            case 3:
                return 2.0f;
            case 4:
                return 2.1111112f;
            case 5:
                return 2.1666667f;
            case 6:
                return 2.0833333f;
            case 7:
                return 2.2222223f;
            default:
                return 1.3333333f;
        }
    }

    public static int[] getRelativeLocation(View view, View view2) {
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        int i = iArr[0];
        int i2 = iArr[1];
        view2.getLocationInWindow(iArr);
        iArr[0] = iArr[0] - i;
        iArr[1] = iArr[1] - i2;
        return iArr;
    }

    public static CameraSize getResolution(ContentValues contentValues) {
        String asString = contentValues.getAsString("resolution");
        if (!TextUtils.isEmpty(asString)) {
            String str = "x";
            if (asString.contains(str)) {
                String[] split = asString.split(str);
                return new CameraSize(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            }
        }
        return null;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float f) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRoundRect(rectF, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return createBitmap;
    }

    public static long getSampleTime() {
        return SystemProperties.getLong("sample.time", 400);
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public static double getScreenInches(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        double sqrt = Math.sqrt(Math.pow((double) (((float) Display.getWindowWidth()) / displayMetrics.xdpi), 2.0d) + Math.pow((double) (((float) Display.getWindowHeight()) / displayMetrics.ydpi), 2.0d));
        StringBuilder sb = new StringBuilder();
        sb.append("getScreenInches=");
        sb.append(sqrt);
        Log.d(TAG, sb.toString());
        return sqrt;
    }

    public static int getScreenLightColor(int i) {
        initScreenLightColorMap();
        int size = COLOR_TEMPERATURE_LIST.size();
        String str = TAG;
        if (size == 0 || COLOR_TEMPERATURE_MAP.size() == 0) {
            Log.e(str, "color temperature list empty!");
            return -1;
        }
        int binarySearchRightMost = binarySearchRightMost(COLOR_TEMPERATURE_LIST, Integer.valueOf(i));
        if (binarySearchRightMost >= COLOR_TEMPERATURE_LIST.size()) {
            binarySearchRightMost = COLOR_TEMPERATURE_LIST.size() - 1;
        } else if (binarySearchRightMost > 0) {
            int i2 = binarySearchRightMost - 1;
            if (((Integer) COLOR_TEMPERATURE_LIST.get(binarySearchRightMost)).intValue() - i > i - ((Integer) COLOR_TEMPERATURE_LIST.get(i2)).intValue()) {
                binarySearchRightMost = i2;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("getScreenLightColor ");
        sb.append(i);
        sb.append("K -> ");
        sb.append(COLOR_TEMPERATURE_LIST.get(binarySearchRightMost));
        sb.append("K");
        Log.d(str, sb.toString());
        return ((Integer) COLOR_TEMPERATURE_MAP.get(binarySearchRightMost)).intValue();
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public static int getSensorOrientation(int i) {
        return Camera2DataContainer.getInstance().getCapabilities(i).getSensorOrientation();
    }

    public static Intent getShareMediaIntent(Context context, String str, boolean z) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(context, "com.android.camera.fileProvider", new File(str)));
        intent.setType(z ? convertOutputFormatToMimeType(2) : "image/*");
        intent.addFlags(1);
        return intent;
    }

    public static int getShootOrientation(Activity activity, int i) {
        return ((i - getDisplayRotation(activity)) + m.cQ) % m.cQ;
    }

    public static float getShootRotation(Activity activity, float f) {
        float displayRotation = f - ((float) getDisplayRotation(activity));
        while (displayRotation < 0.0f) {
            displayRotation += 360.0f;
        }
        while (displayRotation > 360.0f) {
            displayRotation -= 360.0f;
        }
        return displayRotation;
    }

    private static Object getStaticObjectField(Class cls, String str) {
        java.lang.reflect.Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        return declaredField.get(str);
    }

    public static File getStorageDirectory() {
        return isExternalStorageMounted() ? Environment.getExternalStorageDirectory() : INTERNAL_STORAGE_DIRECTORY;
    }

    public static MiYuvImage getSubYuvImage(byte[] bArr, int i, int i2, int i3, int i4, int[] iArr) {
        byte[] bArr2 = new byte[(((iArr[2] * iArr[3]) * 3) / 2)];
        int i5 = (iArr[1] * i3) + iArr[0];
        int i6 = 0;
        for (int i7 = 0; i7 < iArr[3]; i7++) {
            System.arraycopy(bArr, i5, bArr2, i6, iArr[2]);
            i5 += i3;
            i6 += iArr[2];
        }
        int i8 = (i3 * (i2 - 1)) + i + ((iArr[1] / 2) * i4) + iArr[0];
        for (int i9 = 0; i9 < iArr[3] / 2; i9++) {
            System.arraycopy(bArr, i8, bArr2, i6, iArr[2]);
            i8 += i4;
            i6 += iArr[2];
        }
        return new MiYuvImage(bArr2, iArr[2], iArr[3], 35);
    }

    public static byte[] getSubYuvImage(Image image, int[] iArr) {
        int i;
        StringBuilder sb = new StringBuilder();
        sb.append("getSubYuvImage: range = ");
        sb.append(Arrays.toString(iArr));
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        long currentTimeMillis = System.currentTimeMillis();
        byte[] bArr = new byte[(((iArr[2] * iArr[3]) * 3) / 2)];
        Plane[] planes = image.getPlanes();
        int rowStride = planes[0].getRowStride();
        int rowStride2 = planes[2].getRowStride();
        int i2 = (iArr[1] * rowStride) + iArr[0];
        ByteBuffer buffer = planes[0].getBuffer();
        int i3 = 0;
        int i4 = i2;
        for (int i5 = 0; i5 < iArr[3]; i5++) {
            buffer.position(i4);
            buffer.get(bArr, i3, iArr[2]);
            i4 += rowStride;
            i3 += iArr[2];
        }
        int i6 = ((iArr[1] / 2) * rowStride2) + iArr[0];
        Log.d(str, "getSubYuvImage: srcCursor = %d, dstCursor = %d", Integer.valueOf(i6), Integer.valueOf(i3));
        ByteBuffer buffer2 = planes[2].getBuffer();
        int i7 = iArr[3] / 2;
        int i8 = i6;
        for (int i9 = 0; i9 < i7; i9++) {
            buffer2.position(i8);
            if (i9 == i7 - 1) {
                i = Math.min(iArr[2], buffer2.remaining());
                StringBuilder sb3 = new StringBuilder();
                sb3.append("getSubYuvImage: length = ");
                sb3.append(i);
                sb3.append("|");
                sb3.append(buffer2.remaining());
                Log.d(str, sb3.toString());
            } else {
                i = iArr[2];
            }
            buffer2.get(bArr, i3, i);
            i8 += rowStride2;
            i3 += iArr[2];
        }
        Log.d(str, "getSubYuvImage: cost %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        return bArr;
    }

    public static long getSuspendDuration() {
        return SystemProperties.getLong("suspend.duration", 500);
    }

    public static Rect getTapableRectWithEdgeSlop(boolean z, Rect rect, int i, Context context) {
        Rect rect2 = new Rect(rect);
        int uiStyle = DataRepository.dataItemRunning().getUiStyle();
        if (z) {
            rect2 = getDisplayRect(0);
            if (uiStyle == 0 && i == 165) {
                int height = (rect2.height() - rect2.width()) / 2;
                rect2.top += height;
                rect2.bottom -= height;
            }
        }
        int calculateDefaultPreviewEdgeSlop = calculateDefaultPreviewEdgeSlop(context);
        int i2 = SystemProperties.getInt("camera_touch_edge_slop", -1);
        if (i2 != -1) {
            calculateDefaultPreviewEdgeSlop = dpToPixel((float) i2);
        }
        rect2.left += calculateDefaultPreviewEdgeSlop;
        rect2.right -= calculateDefaultPreviewEdgeSlop;
        StringBuilder sb = new StringBuilder();
        sb.append("getTapableRectWithEdgeSlop: after rect = ");
        sb.append(rect2);
        sb.append(", edgeSlop = ");
        sb.append(calculateDefaultPreviewEdgeSlop);
        Log.d(TAG, sb.toString());
        return rect2;
    }

    public static byte[] getTimeWaterMarkData(int i, int i2, String str, int[] iArr, int i3, DeviceWatermarkParam deviceWatermarkParam) {
        NewStyleTextWaterMark newStyleTextWaterMark = new NewStyleTextWaterMark(str, i, i2, i3, deviceWatermarkParam.isCinematicAspectRatio());
        if (iArr != null && iArr.length >= 4) {
            iArr[0] = newStyleTextWaterMark.getWidth();
            iArr[1] = newStyleTextWaterMark.getHeight();
            iArr[2] = newStyleTextWaterMark.getPaddingX();
            iArr[3] = newStyleTextWaterMark.getPaddingY();
        }
        return ((StringTexture) newStyleTextWaterMark.getTexture()).getBitmapData(CompressFormat.PNG);
    }

    public static String getTimeWatermark(Activity activity) {
        StringBuilder sb = new StringBuilder();
        sb.append(new SimpleDateFormat(activity.getResources().getString(R.string.time_stamp_date_format), Locale.ENGLISH).format(new Date()).toCharArray());
        sb.append(" ");
        Time time = new Time();
        time.set(System.currentTimeMillis());
        String str = "%02d";
        sb.append(String.format(Locale.ENGLISH, str, new Object[]{Integer.valueOf(time.hour)}));
        sb.append(":");
        sb.append(String.format(Locale.ENGLISH, str, new Object[]{Integer.valueOf(time.minute)}));
        return sb.toString();
    }

    public static long getTotalMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem;
    }

    private static synchronized Typeface getTypefaceFromAssets(Context context, String str) {
        Typeface typeface;
        synchronized (Util.class) {
            if (!sTypefaces.containsKey(str)) {
                sTypefaces.put(str, Typeface.createFromAsset(context.getAssets(), str));
            }
            typeface = (Typeface) sTypefaces.get(str);
        }
        return typeface;
    }

    private static synchronized Typeface getTypefaceFromFile(Context context, String str) {
        Typeface typeface;
        synchronized (Util.class) {
            if (!sTypefaces.containsKey(str)) {
                sTypefaces.put(str, Typeface.createFromFile(new File(str)));
            }
            typeface = (Typeface) sTypefaces.get(str);
        }
        return typeface;
    }

    public static int getValidValue(int i, int i2, int i3) {
        return Math.min(Math.max(i2, i), i3);
    }

    public static int[] getVendorWatermarkRange(int i, int i2, int i3) {
        int[] iArr = new int[4];
        if (i3 == 0) {
            iArr[0] = 0;
            int i4 = (int) (((float) i2) * 0.2f);
            iArr[1] = i2 - i4;
            iArr[2] = i;
            iArr[3] = i4;
        } else if (i3 == 90) {
            int i5 = (int) (((float) i) * 0.2f);
            iArr[0] = i - i5;
            iArr[1] = 0;
            iArr[2] = i5;
            iArr[3] = i2;
        } else if (i3 == 180) {
            iArr[0] = 0;
            iArr[1] = 0;
            iArr[2] = i;
            iArr[3] = (int) (((float) i2) * 0.2f);
        } else if (i3 == 270) {
            iArr[0] = 0;
            iArr[1] = 0;
            iArr[2] = (int) (((float) i) * 0.2f);
            iArr[3] = i2;
        }
        iArr[0] = (iArr[0] / 2) * 2;
        iArr[1] = (iArr[1] / 2) * 2;
        iArr[2] = (iArr[2] / 4) * 4;
        iArr[3] = (iArr[3] / 4) * 4;
        return iArr;
    }

    public static float getWaterMarkPaddingX() {
        boolean OO0oo = C0122O00000o.instance().OO0oo();
        return 130.0f;
    }

    public static float getWaterMarkPaddingY() {
        return C0122O00000o.instance().OO0oo() ? 96.0f : 108.0f;
    }

    public static int getWatermarkCinematicAspectMargin(int i, int i2) {
        return (int) (((double) (((float) Math.min(i, i2)) - (((float) (Math.max(i, i2) * 9)) / 21.5f))) * 0.5d);
    }

    public static String getWatermarkFileName(boolean z) {
        return CameraSettings.isFrontCameraWaterMarkOpen() ? WATERMARK_FRONT_FILE_NAME : z ? WATERMARK_CINEMATIC_RATIO_FILE_NAME : getDefaultWatermarkFileName();
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x007b  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x018e  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0072  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int[] getWatermarkRange(int i, int i2, int i3, boolean z, boolean z2, String str, DeviceWatermarkParam deviceWatermarkParam) {
        int[] iArr;
        int[] iArr2;
        int[] iArr3;
        int[] iArr4;
        StringBuilder sb;
        String str2;
        int i4 = i;
        int i5 = i2;
        int i6 = i3;
        DeviceWatermarkParam deviceWatermarkParam2 = deviceWatermarkParam;
        String str3 = ", watermarkRange = ";
        String str4 = TAG;
        if (z) {
            iArr4 = new int[4];
            getDualCameraWatermarkData(i, i2, iArr4, i6, deviceWatermarkParam2);
            sb = new StringBuilder();
            str2 = "back deviceWaterMarkLocation: rotation = ";
        } else if (z2) {
            iArr4 = new int[4];
            getFrontCameraWatermarkData(i, i2, iArr4, i6, deviceWatermarkParam2);
            sb = new StringBuilder();
            str2 = "front deviceWaterMarkLocation: rotation = ";
        } else {
            iArr = null;
            if (str == null) {
                iArr2 = new int[4];
                getTimeWaterMarkData(i, i2, str, iArr2, i3, deviceWatermarkParam);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("timeWaterMarkLocation: rotation = ");
                sb2.append(i6);
                sb2.append(str3);
                sb2.append(Arrays.toString(iArr2));
                Log.d(str4, sb2.toString());
            } else {
                iArr2 = null;
            }
            iArr3 = new int[4];
            if (i6 == 0) {
                if (i6 != 90) {
                    if (i6 != 180) {
                        if (i6 == 270) {
                            if (iArr != null && iArr2 != null) {
                                iArr3[0] = 0;
                                iArr3[1] = iArr[2];
                                iArr3[2] = Math.max(iArr[1] + iArr[3], iArr2[1] + iArr2[3]);
                                iArr3[3] = (i5 - iArr[2]) - iArr2[2];
                            } else if (iArr != null) {
                                iArr3[0] = iArr[3];
                                iArr3[1] = iArr[2];
                                iArr3[2] = iArr[1];
                                iArr3[3] = iArr[0];
                            } else if (iArr2 != null) {
                                iArr3[0] = iArr2[3];
                                iArr3[1] = (i5 - iArr2[0]) - iArr2[2];
                                iArr3[2] = iArr2[1];
                                iArr3[3] = iArr2[0];
                            }
                        }
                    } else if (iArr != null && iArr2 != null) {
                        iArr3[0] = iArr2[2];
                        iArr3[1] = 0;
                        iArr3[2] = (i4 - iArr[2]) - iArr2[2];
                        iArr3[3] = Math.max(iArr[1] + iArr[3], iArr2[1] + iArr2[3]);
                    } else if (iArr != null) {
                        iArr3[0] = (i4 - iArr[0]) - iArr[2];
                        iArr3[1] = iArr[3];
                        iArr3[2] = iArr[0];
                        iArr3[3] = iArr[1];
                    } else if (iArr2 != null) {
                        iArr3[0] = iArr2[2];
                        iArr3[1] = iArr2[3];
                        iArr3[2] = iArr2[0];
                        iArr3[3] = iArr2[1];
                    }
                } else if (iArr != null && iArr2 != null) {
                    iArr3[0] = i4 - Math.max(iArr[1] + iArr[3], iArr2[1] + iArr2[3]);
                    iArr3[1] = iArr2[2];
                    iArr3[2] = i4 - iArr3[0];
                    iArr3[3] = (i5 - iArr[2]) - iArr2[2];
                } else if (iArr != null) {
                    iArr3[0] = (i4 - iArr[1]) - iArr[3];
                    iArr3[1] = (i5 - iArr[0]) - iArr[2];
                    iArr3[2] = iArr[1];
                    iArr3[3] = iArr[0];
                } else if (iArr2 != null) {
                    iArr3[0] = (i4 - iArr2[1]) - iArr2[3];
                    iArr3[1] = iArr2[2];
                    iArr3[2] = iArr2[1];
                    iArr3[3] = iArr2[0];
                }
            } else if (iArr != null && iArr2 != null) {
                iArr3[0] = iArr[2];
                iArr3[1] = i5 - Math.max(iArr[1] + iArr[3], iArr2[1] + iArr2[3]);
                iArr3[2] = (i4 - iArr[2]) - iArr2[2];
                iArr3[3] = i5 - iArr3[1];
            } else if (iArr != null) {
                iArr3[0] = iArr[2];
                iArr3[1] = (i5 - iArr[1]) - iArr[3];
                iArr3[2] = iArr[0];
                iArr3[3] = iArr[1];
            } else if (iArr2 != null) {
                iArr3[0] = (i4 - iArr2[0]) - iArr2[2];
                iArr3[1] = (i5 - iArr2[1]) - iArr2[3];
                iArr3[2] = iArr2[0];
                iArr3[3] = iArr2[1];
            }
            iArr3[0] = (iArr3[0] / 2) * 2;
            iArr3[1] = (iArr3[1] / 2) * 2;
            iArr3[2] = (iArr3[2] / 4) * 4;
            iArr3[3] = (iArr3[3] / 4) * 4;
            if ((iArr != null && (iArr3[2] < iArr[0] || iArr3[3] < iArr[1])) || (iArr2 != null && (iArr3[2] < iArr2[0] || iArr3[3] < iArr2[1]))) {
                iArr3[2] = iArr3[2] + 4;
                iArr3[3] = iArr3[3] + 4;
            }
            return iArr3;
        }
        sb.append(str2);
        sb.append(i6);
        sb.append(str3);
        sb.append(Arrays.toString(iArr4));
        Log.d(str4, sb.toString());
        iArr = iArr4;
        if (str == null) {
        }
        iArr3 = new int[4];
        if (i6 == 0) {
        }
        iArr3[0] = (iArr3[0] / 2) * 2;
        iArr3[1] = (iArr3[1] / 2) * 2;
        iArr3[2] = (iArr3[2] / 4) * 4;
        iArr3[3] = (iArr3[3] / 4) * 4;
        iArr3[2] = iArr3[2] + 4;
        iArr3[3] = iArr3[3] + 4;
        return iArr3;
    }

    public static float getWatermarkRatio(int i, int i2) {
        return ((float) Math.min(i, i2)) / 3000.0f;
    }

    public static int getWeekDay(Calendar calendar) {
        int[] iArr = {2, 3, 4, 5, 6, 7, 1};
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        int i = 6;
        if (1 == firstDayOfWeek) {
            int i2 = iArr[6];
            while (i > 0) {
                iArr[i] = iArr[i - 1];
                i--;
            }
            iArr[0] = i2;
        } else if (7 == firstDayOfWeek) {
            int i3 = iArr[6];
            int i4 = iArr[5];
            while (i > 1) {
                iArr[i] = iArr[i - 2];
                i--;
            }
            iArr[1] = i3;
            iArr[0] = i4;
        }
        int i5 = calendar.get(7) - 1;
        if (i5 < 0) {
            i5 = 0;
        }
        return iArr[i5];
    }

    public static String getZoomRatioText(float f) {
        StringBuilder sb = new StringBuilder();
        float decimal = HybridZoomingSystem.toDecimal(f);
        int i = (int) decimal;
        sb.append(((int) ((10.0f * decimal) - ((float) (i * 10)))) == 0 ? String.valueOf(i) : String.valueOf(decimal));
        sb.append("X");
        return sb.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0105, code lost:
        if ((r0 instanceof android.content.res.XmlResourceParser) == false) goto L_0x012a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x011b, code lost:
        if ((r0 instanceof android.content.res.XmlResourceParser) == false) goto L_0x012a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0123, code lost:
        if ((r0 instanceof android.content.res.XmlResourceParser) == false) goto L_0x012a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0125, code lost:
        ((android.content.res.XmlResourceParser) r0).close();
     */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0040  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0079 A[Catch:{ XmlPullParserException -> 0x011e, IOException -> 0x0116, all -> 0x0108 }] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0100 A[EDGE_INSN: B:64:0x0100->B:48:0x0100 ?: BREAK  
EDGE_INSN: B:64:0x0100->B:48:0x0100 ?: BREAK  
EDGE_INSN: B:64:0x0100->B:48:0x0100 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0100 A[EDGE_INSN: B:64:0x0100->B:48:0x0100 ?: BREAK  
EDGE_INSN: B:64:0x0100->B:48:0x0100 ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void initScreenLightColorMap() {
        FileReader fileReader;
        XmlPullParser xmlPullParser;
        if (COLOR_TEMPERATURE_LIST.size() <= 0 && COLOR_TEMPERATURE_MAP.size() <= 0) {
            File colorMapXmlMapFile = getColorMapXmlMapFile();
            if (colorMapXmlMapFile != null) {
                try {
                    fileReader = new FileReader(colorMapXmlMapFile);
                    try {
                        XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
                        newInstance.setNamespaceAware(false);
                        xmlPullParser = newInstance.newPullParser();
                        try {
                            xmlPullParser.setInput(fileReader);
                        } catch (FileNotFoundException | XmlPullParserException e) {
                            e = e;
                        }
                    } catch (FileNotFoundException | XmlPullParserException e2) {
                        e = e2;
                        xmlPullParser = null;
                        e.printStackTrace();
                        String str = TAG;
                        if (xmlPullParser == null) {
                        }
                        while (true) {
                            try {
                                if (xmlPullParser.next() == 3) {
                                }
                            } catch (XmlPullParserException unused) {
                                closeSafely(fileReader);
                            } catch (IOException unused2) {
                                closeSafely(fileReader);
                            } catch (Throwable th) {
                                closeSafely(fileReader);
                                if (xmlPullParser instanceof XmlResourceParser) {
                                    ((XmlResourceParser) xmlPullParser).close();
                                }
                                throw th;
                            }
                        }
                        closeSafely(fileReader);
                    }
                } catch (FileNotFoundException | XmlPullParserException e3) {
                    e = e3;
                    xmlPullParser = null;
                    fileReader = null;
                    e.printStackTrace();
                    String str2 = TAG;
                    if (xmlPullParser == null) {
                    }
                    while (true) {
                        if (xmlPullParser.next() == 3) {
                        }
                    }
                    closeSafely(fileReader);
                }
            } else {
                xmlPullParser = null;
                fileReader = null;
            }
            String str22 = TAG;
            if (xmlPullParser == null) {
                Log.d(str22, "Cannot find screen color map in system, try local resource.");
                int identifier = CameraAppImpl.getAndroidContext().getResources().getIdentifier("screen_light", "xml", CameraAppImpl.getAndroidContext().getPackageName());
                if (identifier <= 0) {
                    Log.e(str22, "res/xml/screen_light.xml not found!");
                    return;
                }
                xmlPullParser = CameraAppImpl.getAndroidContext().getResources().getXml(identifier);
            }
            while (true) {
                if (xmlPullParser.next() == 3) {
                    break;
                } else if (xmlPullParser.getEventType() == 2) {
                    if (!"screen".equals(xmlPullParser.getName())) {
                        continue;
                    } else if (!SCREEN_VENDOR.equals(xmlPullParser.getAttributeValue(null, O0000Oo0.VENDOR))) {
                        skip(xmlPullParser);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("load screen light parameters for ");
                        sb.append(SCREEN_VENDOR);
                        Log.d(str22, sb.toString());
                        while (true) {
                            if (xmlPullParser.next() == 1) {
                                break;
                            } else if (xmlPullParser.getEventType() == 2) {
                                if (!"light".equals(xmlPullParser.getName())) {
                                    break;
                                }
                                int attributeIntValue = getAttributeIntValue(xmlPullParser, "CCT", 0);
                                int attributeIntValue2 = getAttributeIntValue(xmlPullParser, "R", 0);
                                int attributeIntValue3 = getAttributeIntValue(xmlPullParser, "G", 0);
                                int attributeIntValue4 = getAttributeIntValue(xmlPullParser, "B", 0);
                                COLOR_TEMPERATURE_LIST.add(Integer.valueOf(attributeIntValue));
                                COLOR_TEMPERATURE_MAP.add(Integer.valueOf(Color.rgb(attributeIntValue2, attributeIntValue3, attributeIntValue4)));
                            }
                        }
                    }
                }
            }
            closeSafely(fileReader);
        }
    }

    public static void initialize(Context context) {
        updateDeviceConfig(context);
        sImageFileNamer = new ImageFileNamer(context.getString(R.string.image_file_name_format));
        checkHasBackLightSensot(context);
    }

    public static void installPackage(Context context, String str, PackageInstallerListener packageInstallerListener, boolean z, boolean z2) {
        String str2 = TAG;
        if (context == null || TextUtils.isEmpty(str)) {
            StringBuilder sb = new StringBuilder();
            sb.append("invalid params. pkgName=");
            sb.append(str);
            Log.w(str2, sb.toString());
            return;
        }
        try {
            Object packageInstallObserver = CompatibilityUtils.getPackageInstallObserver(packageInstallerListener);
            Class cls = Class.forName("miui.content.pm.PreloadedAppPolicy");
            miui.reflect.Method of = miui.reflect.Method.of(cls, "installPreloadedDataApp", CompatibilityUtils.getInstallMethodDescription());
            int i = z ? 1 : z2 ? 2 : 0;
            boolean invokeBoolean = of.invokeBoolean(cls, null, context, str, packageInstallObserver, Integer.valueOf(i));
            StringBuilder sb2 = new StringBuilder();
            sb2.append("installPackage: result=");
            sb2.append(invokeBoolean);
            Log.d(str2, sb2.toString());
        } catch (Exception e) {
            Log.e(str2, e.getMessage(), (Throwable) e);
            if (packageInstallerListener != null) {
                packageInstallerListener.onPackageInstalled(str, false);
            }
        }
    }

    public static boolean isAEStable(int i) {
        return i == 2 || i == 3 || i == 4;
    }

    public static boolean isAWBStable(int i) {
        return i == 2 || i == 3;
    }

    public static boolean isAccessible() {
        return sIsAccessibilityEnable;
    }

    public static boolean isActivityInvert(Activity activity) {
        return getDisplayRotation(activity) == 180;
    }

    public static boolean isAntibanding60() {
        return ANTIBANDING_60_COUNTRY.contains(mCountryIso);
    }

    public static final boolean isAppLocked(Context context, String str) {
        return GeneralUtils.isAppLocked(context, str);
    }

    public static boolean isBringupDebug() {
        return C0122O00000o.instance().isPad();
    }

    public static boolean isContains(RectF rectF, RectF rectF2) {
        if (rectF == null || rectF2 == null) {
            return false;
        }
        float f = rectF.left;
        float f2 = rectF.right;
        if (f >= f2) {
            return false;
        }
        float f3 = rectF.top;
        float f4 = rectF.bottom;
        return f3 < f4 && f <= rectF2.left && f3 <= rectF2.top && f2 >= rectF2.right && f4 >= rectF2.bottom;
    }

    public static boolean isDebugOsBuild() {
        if (!"userdebug".equals(android.os.Build.TYPE)) {
            if (!"eng".equals(android.os.Build.TYPE) && !sIsDumpLog) {
                return false;
            }
        }
        return true;
    }

    private static boolean isDevices(String str) {
        try {
            Class cls = Class.forName("miui.os.Build");
            if (cls != null) {
                Object staticObjectField = getStaticObjectField(cls, str);
                if (staticObjectField == null) {
                    return false;
                }
                return Boolean.parseBoolean(staticObjectField.toString());
            }
        } catch (Exception e) {
            Log.e(TAG, "getClass error", (Throwable) e);
        }
        return false;
    }

    public static boolean isDumpImageEnabled() {
        if (sIsDumpImageEnabled == null) {
            sIsDumpImageEnabled = Boolean.valueOf(new File(Storage.generatePrimaryFilepath("algoup_dump_images")).exists());
        }
        return sIsDumpImageEnabled.booleanValue();
    }

    public static boolean isEnglishOrNum(String str) {
        return str.matches("[a-zA-Z0-9]+");
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0064 A[SYNTHETIC, Splitter:B:29:0x0064] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0073 A[SYNTHETIC, Splitter:B:35:0x0073] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean isEqual(byte[] bArr, File file) {
        String str = TAG;
        if (bArr == null || bArr.length == 0 || !file.exists()) {
            return false;
        }
        FileInputStream fileInputStream = null;
        byte[] bArr2 = new byte[512];
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            FileInputStream fileInputStream2 = new FileInputStream(file);
            while (true) {
                try {
                    int read = fileInputStream2.read(bArr2, 0, 512);
                    if (read == -1) {
                        break;
                    }
                    instance.update(bArr2, 0, read);
                } catch (IOException | NoSuchAlgorithmException e) {
                    e = e;
                    fileInputStream = fileInputStream2;
                    try {
                        Log.e(str, e.getMessage(), (Throwable) e);
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e2) {
                                Log.e(str, e2.getMessage(), (Throwable) e2);
                            }
                        }
                        return false;
                    } catch (Throwable th) {
                        th = th;
                        fileInputStream2 = fileInputStream;
                        if (fileInputStream2 != null) {
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fileInputStream2 != null) {
                        try {
                            fileInputStream2.close();
                        } catch (IOException e3) {
                            Log.e(str, e3.getMessage(), (Throwable) e3);
                        }
                    }
                    throw th;
                }
            }
            String str2 = new String(instance.digest());
            instance.reset();
            boolean equals = str2.equals(new String(instance.digest(bArr)));
            try {
                fileInputStream2.close();
            } catch (IOException e4) {
                Log.e(str, e4.getMessage(), (Throwable) e4);
            }
            return equals;
        } catch (IOException | NoSuchAlgorithmException e5) {
            e = e5;
            Log.e(str, e.getMessage(), (Throwable) e);
            if (fileInputStream != null) {
            }
            return false;
        }
    }

    public static boolean isEqualsZero(double d) {
        return Math.abs(d) < 1.0E-8d;
    }

    public static boolean isExternalStorageMounted() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static boolean isFingerPrintKeyEvent(KeyEvent keyEvent) {
        return keyEvent != null && 27 == keyEvent.getKeyCode() && keyEvent.getDevice() != null && C0124O00000oO.OOoo00o().contains(keyEvent.getDevice().getName());
    }

    public static int isFlingV(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        if (motionEvent == null || motionEvent2 == null) {
            return 0;
        }
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int x2 = (int) motionEvent2.getX();
        int y2 = (int) motionEvent2.getY();
        int abs = Math.abs(x2 - x);
        int abs2 = Math.abs(y2 - y);
        if (abs2 <= i) {
            return 0;
        }
        boolean z = abs == 0 || ((float) abs2) / ((float) abs) >= 2.0f;
        if (z) {
            return y > y2 ? 1 : -1;
        }
        return 0;
    }

    public static boolean isForceNameSuffix() {
        if (sIsForceNameSuffix == null) {
            sIsForceNameSuffix = Boolean.valueOf(new File(Storage.generatePrimaryFilepath(FORCE_NAME_SUFFIX_FILE)).exists());
        }
        return sIsForceNameSuffix.booleanValue();
    }

    public static boolean isFullScreenNavBarHidden(Context context) {
        return Global.getBoolean(context.getContentResolver(), "force_fsg_nav_bar");
    }

    public static boolean isGlobalVersion() {
        return SystemProperties.get("ro.product.mod_device", "").contains("_global") || C0122O00000o.instance().OO00ooO();
    }

    private static boolean isGyroscopeStable(float[] fArr) {
        return fArr != null && fArr.length == 3 && Math.abs(fArr[0]) < 0.7f && Math.abs(fArr[1]) < 5.0f && Math.abs(fArr[2]) < 0.7f;
    }

    public static boolean isGyroscopeStable(float[] fArr, float[] fArr2) {
        if (fArr == null) {
            return true;
        }
        boolean isGyroscopeStable = isGyroscopeStable(fArr);
        if (isGyroscopeStable) {
            if (fArr2 == null) {
                return true;
            }
            if (isGyroscopeStable(fArr2)) {
                return isGyroscopeStable;
            }
        }
        return false;
    }

    public static boolean isHDR10Video(String str) {
        boolean z;
        MediaExtractor mediaExtractor = new MediaExtractor();
        try {
            mediaExtractor.setDataSource(str);
            int trackCount = mediaExtractor.getTrackCount();
            int i = 0;
            z = false;
            while (i < trackCount) {
                try {
                    MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
                    String string = trackFormat.getString(IMediaFormat.KEY_MIME);
                    if (!TextUtils.isEmpty(string) && string.startsWith("video/") && trackFormat.containsKey("frame-rate") && trackFormat.containsKey("width") && trackFormat.containsKey("height")) {
                        z = trackFormat.getInteger("profile") == 4096;
                    }
                    i++;
                } catch (Exception e) {
                    e = e;
                    try {
                        e.printStackTrace();
                        mediaExtractor.release();
                        return z;
                    } catch (Throwable th) {
                        mediaExtractor.release();
                        throw th;
                    }
                }
            }
        } catch (Exception e2) {
            e = e2;
            z = false;
            e.printStackTrace();
            mediaExtractor.release();
            return z;
        }
        mediaExtractor.release();
        return z;
    }

    public static boolean isHasBackLightSensor() {
        return isHasBackLightSensor;
    }

    public static boolean isInViewRegion(View view, int i, int i2) {
        if (view == null) {
            return false;
        }
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return rect.contains(i, i2);
    }

    public static boolean isInternationalBuild() {
        return SystemProperties.get("ro.product.mod_device", "").endsWith("_global");
    }

    public static boolean isLabOptionsVisible() {
        if (sIsLabOptionsVisible == null) {
            sIsLabOptionsVisible = Boolean.valueOf(new File(Storage.generatePrimaryFilepath(LAB_OPTIONS_VISIBLE_FILE)).exists());
        }
        return sIsLabOptionsVisible.booleanValue();
    }

    public static boolean isLayoutRTL(Context context) {
        boolean z = false;
        if (context == null) {
            return false;
        }
        if (context.getResources().getConfiguration().getLayoutDirection() == 1) {
            z = true;
        }
        return z;
    }

    public static boolean isLivePhotoStable(LivePhotoResult livePhotoResult, int i) {
        boolean z = true;
        if (livePhotoResult == null) {
            return true;
        }
        if (!isAEStable(livePhotoResult.getAEState()) || !isAWBStable(livePhotoResult.getAWBState()) || !livePhotoResult.isGyroScopeStable() || livePhotoResult.getFilterId() != i) {
            z = false;
        }
        return z;
    }

    public static boolean isLocaleChinese() {
        return Locale.CHINESE.getLanguage().equals(Locale.getDefault().getLanguage());
    }

    public static boolean isMemoryRich(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem > 419430400;
    }

    public static boolean isNightUiMode(Context context) {
        return 32 == (context.getResources().getConfiguration().uiMode & 48);
    }

    public static boolean isNonUI() {
        return SystemProperties.getBoolean(NONUI_MODE_PROPERTY, false);
    }

    public static boolean isNonUIEnabled() {
        return !SystemProperties.get(NONUI_MODE_PROPERTY).equals("");
    }

    public static boolean isPackageAvailable(Context context, String str) {
        String str2;
        String str3 = TAG;
        if (context == null || str == null || str.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("invalid params. packageName=");
            sb.append(str);
            Log.w(str3, sb.toString());
            return false;
        }
        try {
            int applicationEnabledSetting = context.getPackageManager().getApplicationEnabledSetting(str);
            boolean z = true;
            if (!(applicationEnabledSetting == 0 || applicationEnabledSetting == 1)) {
                z = false;
            }
            return z;
        } catch (IllegalArgumentException e) {
            str2 = e.getMessage();
            Log.e(str3, str2);
            return false;
        } catch (NullPointerException e2) {
            str2 = e2.getMessage();
            Log.e(str3, str2);
            return false;
        }
    }

    public static boolean isPathExist(String str) {
        return !TextUtils.isEmpty(str) && new File(str).exists();
    }

    public static boolean isProduceFocusInfoSuccess(byte[] bArr) {
        return bArr != null && 25 < bArr.length && bArr[bArr.length - 25] == 0;
    }

    public static boolean isQuotaExceeded(Exception exc) {
        if (exc != null && (exc instanceof FileNotFoundException)) {
            String message = exc.getMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("isQuotaExceeded: msg=");
            sb.append(message);
            Log.e(TAG, sb.toString());
            if (message != null) {
                return message.toLowerCase(Locale.ENGLISH).contains("quota exceeded");
            }
        }
        return false;
    }

    public static boolean isSaveDocPreview() {
        return SystemProperties.getBoolean("doc.save_preview", false);
    }

    public static boolean isSaveToHidenFolder(int i) {
        return CameraSettings.isDocumentModeOn(i) || i == 182;
    }

    public static boolean isScreenSlideOff(Context context) {
        return System.getInt(context.getContentResolver(), "sc_status", -1) == 1;
    }

    public static boolean isShowAfRegionView() {
        return "1".equals(SystemProperties.get("camera.preview.debug.afRegion_view"));
    }

    public static boolean isShowDebugInfo() {
        String str = "1";
        return str.equals(SystemProperties.get("persist.camera.enable.log")) || str.equals(SystemProperties.get("persist.camera.debug.show_af")) || str.equals(SystemProperties.get("persist.camera.debug.show_awb")) || str.equals(SystemProperties.get("persist.camera.debug.show_aec")) || str.equals(SystemProperties.get("persist.camera.debug.autoscene")) || str.equals(SystemProperties.get("persist.camera.debug.hht"));
    }

    public static boolean isShowDebugInfoView() {
        return "1".equals(SystemProperties.get("camera.preview.debug.debugInfo_view"));
    }

    public static boolean isShowPreviewDebugInfo() {
        return "1".equals(SystemProperties.get("camera.preview.enable.log"));
    }

    public static boolean isStringValueContained(Object obj, int i) {
        return isStringValueContained(obj, (CharSequence[]) CameraAppImpl.getAndroidContext().getResources().getStringArray(i));
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<java.lang.CharSequence>, for r3v0, types: [java.util.List, java.util.List<java.lang.CharSequence>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isStringValueContained(Object obj, List<CharSequence> list) {
        if (!(list == null || obj == null)) {
            for (CharSequence equals : list) {
                if (equals.equals(obj)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isStringValueContained(Object obj, CharSequence[] charSequenceArr) {
        if (!(charSequenceArr == null || obj == null)) {
            for (CharSequence equals : charSequenceArr) {
                if (equals.equals(obj)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isSupported(int i, int[] iArr) {
        return getArrayIndex(iArr, i) != -1;
    }

    public static boolean isSupported(Object obj, Object[] objArr) {
        return getArrayIndex(objArr, obj) != -1;
    }

    public static boolean isSupported(String str, List list) {
        return list != null && list.indexOf(str) >= 0;
    }

    public static boolean isTimeout(long j, long j2, long j3) {
        return j < j2 || j - j2 > j3;
    }

    public static boolean isUriValid(Uri uri, ContentResolver contentResolver) {
        StringBuilder sb;
        String message;
        String str = TAG;
        if (uri == null) {
            return false;
        }
        try {
            ParcelFileDescriptor openFileDescriptor = contentResolver.openFileDescriptor(uri, "r");
            if (openFileDescriptor == null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Fail to open URI. URI=");
                sb2.append(uri);
                Log.e(str, sb2.toString());
                return false;
            }
            openFileDescriptor.close();
            return true;
        } catch (IOException e) {
            sb = new StringBuilder();
            sb.append("IOException occurs when opening URI: ");
            message = e.getMessage();
            r3 = e;
            sb.append(message);
            Log.e(str, sb.toString(), r3);
            return false;
        } catch (IllegalArgumentException e2) {
            sb = new StringBuilder();
            sb.append("IllegalArgumentException occurs when Volume not found: ");
            message = e2.getMessage();
            r3 = e2;
            sb.append(message);
            Log.e(str, sb.toString(), r3);
            return false;
        } catch (IllegalStateException e3) {
            sb = new StringBuilder();
            sb.append("IllegalStateException occurs when fail to read EXIF info by MediaProvider#getRedactionRanges");
            message = e3.getMessage();
            r3 = e3;
            sb.append(message);
            Log.e(str, sb.toString(), r3);
            return false;
        } catch (SecurityException e4) {
            sb = new StringBuilder();
            sb.append("SecurityException: no access because uri not exist ");
            message = e4.getMessage();
            r3 = e4;
            sb.append(message);
            Log.e(str, sb.toString(), r3);
            return false;
        }
    }

    public static boolean isUserUnlocked(Context context) {
        UserManager userManager = (UserManager) context.getSystemService("user");
        return userManager != null && userManager.isUserUnlocked();
    }

    public static boolean isValidValue(String str) {
        return !TextUtils.isEmpty(str) && str.matches("^[0-9]+$");
    }

    public static boolean isViewIntersectWindow(View view) {
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        return iArr[0] < Display.getWindowWidth() && iArr[0] + view.getWidth() >= 0 && iArr[1] < Display.getWindowHeight() && iArr[1] + view.getHeight() >= 0;
    }

    public static boolean isVoiceAccessible() {
        return sIsVoiceAccessibilityEnable;
    }

    public static boolean isWiredAudioHeadset(AudioDeviceInfo audioDeviceInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("isWiredAudioHeadset.type: ");
        sb.append(audioDeviceInfo.getType());
        Log.d(TAG, sb.toString());
        int type = audioDeviceInfo.getType();
        return type == 3 || type == 4 || type == 22;
    }

    public static boolean isWiredHeadsetOn() {
        PackageManager packageManager = CameraAppImpl.getAndroidContext().getPackageManager();
        AudioManager audioManager = (AudioManager) CameraAppImpl.getAndroidContext().getSystemService("audio");
        if (VERSION.SDK_INT >= 23 && packageManager.hasSystemFeature("android.hardware.audio.output")) {
            for (AudioDeviceInfo isWiredAudioHeadset : audioManager.getDevices(2)) {
                if (isWiredAudioHeadset(isWiredAudioHeadset)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isZoomAnimationEnabled() {
        return SystemProperties.getBoolean(ZOOM_ANIMATION_PROPERTY, !C0122O00000o.instance().OOOo0O());
    }

    public static String join(String str, List list) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                stringBuffer.append((String) list.get(i));
            } else {
                stringBuffer.append((String) list.get(i));
                stringBuffer.append(str);
            }
        }
        return stringBuffer.toString();
    }

    public static boolean keepLiveShotMicroVideoInCache() {
        return android.util.Log.isLoggable("liveshotsmv", 3);
    }

    private static Bitmap loadFrontCameraWatermark() {
        Options options = new Options();
        options.inScaled = false;
        options.inPurgeable = true;
        options.inPremultiplied = true;
        return CustomTextWaterMark.drawFrontWaterMark(loadWatermarkIcon(CameraAppImpl.getAndroidContext(), WaterMarkUtil.getFrontIconName(), options), WaterMarkUtil.getFrontTitle());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002c, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002d, code lost:
        if (r3 != null) goto L_0x002f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        $closeResource(r4, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0032, code lost:
        throw r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static Bitmap loadWatermarkIcon(Context context, String str, Options options) {
        AssetManager assets = context.getAssets();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("watermarks");
            sb.append(File.separator);
            sb.append(str);
            InputStream open = assets.open(sb.toString());
            Bitmap decodeStream = BitmapFactory.decodeStream(open, null, options);
            if (open != null) {
                $closeResource(null, open);
            }
            return decodeStream;
        } catch (Exception e) {
            Log.d(TAG, "Failed to load app camera watermark ", (Throwable) e);
            return null;
        }
    }

    private static double log2(double d) {
        return Math.log(d) / LOG_2;
    }

    public static Bitmap makeBitmap(byte[] bArr, int i) {
        try {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
            if (!options.mCancel && options.outWidth != -1) {
                if (options.outHeight != -1) {
                    options.inSampleSize = computeSampleSize(options, -1, i);
                    options.inJustDecodeBounds = false;
                    options.inDither = false;
                    options.inPreferredConfig = Config.ARGB_8888;
                    return BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
                }
            }
            return null;
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "Got oom exception ", (Throwable) e);
            return null;
        }
    }

    public static boolean makeSureNoMedia(String str) {
        File file = new File(str, Storage.AVOID_SCAN_FILE_NAME);
        if (file.exists()) {
            return true;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String md5(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes("UTF8"));
            byte[] digest = instance.digest();
            String str2 = "";
            for (byte b : digest) {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(Integer.toHexString((b & -1) | 0).substring(6));
                str2 = sb.toString();
            }
            return str2;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final String millisecondToTimeString(long j, boolean z) {
        return millisecondToTimeString(j, z, false);
    }

    public static final String millisecondToTimeString(long j, boolean z, boolean z2) {
        return millisecondToTimeString(j, z, z2, false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x003d  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x005c  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0064  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final String millisecondToTimeString(long j, boolean z, boolean z2, boolean z3) {
        long j2 = j;
        long j3 = j2 / 1000;
        long j4 = j3 / 60;
        long j5 = j4 / 60;
        long j6 = j4 - (j5 * 60);
        long j7 = j4 * 60;
        long j8 = j3 - j7;
        StringBuilder sb = new StringBuilder();
        if (j5 > 0) {
            if (j5 < 10) {
                sb.append('0');
            }
            sb.append(j5);
        } else {
            if (z3) {
                sb.append("00");
            }
            if (j6 < 10) {
                sb.append('0');
            }
            sb.append(j6);
            sb.append(ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            if (!z && z2) {
                j8 = (long) Math.ceil((((double) j2) / 1000.0d) - ((double) j7));
            }
            if (j8 < 10) {
                sb.append('0');
            }
            sb.append(j8);
            if (z) {
                sb.append('.');
                long j9 = (j2 - (j3 * 1000)) / 10;
                if (j9 < 10) {
                    sb.append('0');
                }
                sb.append(j9);
            }
            return sb.toString();
        }
        sb.append(ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        if (j6 < 10) {
        }
        sb.append(j6);
        sb.append(ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        j8 = (long) Math.ceil((((double) j2) / 1000.0d) - ((double) j7));
        if (j8 < 10) {
        }
        sb.append(j8);
        if (z) {
        }
        return sb.toString();
    }

    public static boolean mkdirs(File file, int i, int i2, int i3) {
        if (file.exists()) {
            return false;
        }
        String parent = file.getParent();
        if (parent != null) {
            mkdirs(new File(parent), i, i2, i3);
        }
        return file.mkdir();
    }

    private static void modify(Object obj, String str, int i) {
        java.lang.reflect.Field declaredField = obj.getClass().getDeclaredField(str);
        declaredField.setAccessible(true);
        declaredField.setInt(obj, i);
    }

    public static int nextPowerOf2(int i) {
        int i2 = i - 1;
        int i3 = i2 | (i2 >>> 16);
        int i4 = i3 | (i3 >>> 8);
        int i5 = i4 | (i4 >>> 4);
        int i6 = i5 | (i5 >>> 2);
        return (i6 | (i6 >>> 1)) + 1;
    }

    private static float normalizeDegree(float f) {
        if (f < 0.0f) {
            f += 360.0f;
        } else if (f > 360.0f) {
            f %= 360.0f;
        }
        return f <= 45.0f ? f : f <= 90.0f ? 90.0f - f : f <= 135.0f ? f - 90.0f : f <= 180.0f ? 180.0f - f : f <= 225.0f ? f - 180.0f : f <= 270.0f ? 270.0f - f : f <= 315.0f ? f - 270.0f : 360.0f - f;
    }

    public static int parseInt(String str, int i) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            Log.e(TAG, e.getMessage(), (Throwable) e);
            if (!isDebugOsBuild()) {
                return i;
            }
            throw e;
        }
    }

    public static Uri photoUri(String str) {
        return new Uri.Builder().scheme("photo").path(str).build();
    }

    public static float pixelToXxhdp(float f) {
        return (f * Display.getsPixelDensityScale()) / 3.0f;
    }

    public static boolean pointInView(float f, float f2, View view) {
        boolean z = false;
        if (view == null) {
            return false;
        }
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        if (f >= ((float) iArr[0]) && f < ((float) (iArr[0] + view.getWidth())) && f2 >= ((float) iArr[1]) && f2 < ((float) (iArr[1] + view.getHeight()))) {
            z = true;
        }
        return z;
    }

    public static void prepareMatrix(Matrix matrix, boolean z, int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        float f;
        float f2;
        float f3;
        matrix.setScale(z ? -1.0f : 1.0f, 1.0f);
        matrix.postRotate((float) i);
        if (i == 90 || i == 270) {
            f3 = ((float) ((i3 * i7) / i6)) / ((float) i7);
            f2 = (float) i3;
            f = (float) i6;
        } else {
            f3 = ((float) ((i3 * i6) / i7)) / ((float) i6);
            f2 = (float) i3;
            f = (float) i7;
        }
        matrix.postScale(f3, f2 / f);
        matrix.postTranslate((float) i4, (float) i5);
    }

    public static void printLog(String str, Object... objArr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < objArr.length; i += 2) {
            sb.append(objArr[i].toString());
            sb.append(" = ");
            sb.append(objArr[i + 1].toString());
            sb.append(" ");
        }
        Log.d(str, sb.toString());
    }

    public static void rectFToRect(RectF rectF, Rect rect) {
        rect.left = Math.round(rectF.left);
        rect.top = Math.round(rectF.top);
        rect.right = Math.round(rectF.right);
        rect.bottom = Math.round(rectF.bottom);
    }

    public static void removeCustomWatermark() {
        if (C0122O00000o.instance().OOoOo0O()) {
            WatermarkMiSysUtils.eraseFile(getDefaultWatermarkFileName());
            WatermarkMiSysUtils.eraseFile(WATERMARK_FRONT_FILE_NAME);
            WatermarkMiSysUtils.eraseFile(WATERMARK_CINEMATIC_RATIO_FILE_NAME);
        }
        File filesDir = CameraAppImpl.getAndroidContext().getFilesDir();
        File file = new File(filesDir, getDefaultWatermarkFileName());
        File file2 = new File(filesDir, WATERMARK_FRONT_FILE_NAME);
        File file3 = new File(filesDir, WATERMARK_CINEMATIC_RATIO_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
        if (file2.exists()) {
            file2.delete();
        }
        if (file3.exists()) {
            file3.delete();
        }
    }

    public static int replaceStartEffectRender(Activity activity) {
        if (C0124O00000oO.Oo00oo()) {
            String stringExtra = activity.getIntent().getStringExtra(EXTRAS_START_WITH_EFFECT_RENDER);
            if (stringExtra != null) {
                int identifier = activity.getResources().getIdentifier(stringExtra, "integer", activity.getPackageName());
                if (identifier != 0) {
                    int integer = activity.getResources().getInteger(identifier);
                    CameraSettings.setShaderEffect(integer);
                    return integer;
                }
            }
        }
        return FilterInfo.FILTER_ID_NONE;
    }

    public static void reverseAnimatorSet(AnimatorSet animatorSet) {
        Iterator it = animatorSet.getChildAnimations().iterator();
        while (it.hasNext()) {
            Animator animator = (Animator) it.next();
            if (animator instanceof ValueAnimator) {
                ((ValueAnimator) animator).reverse();
            } else if (animator instanceof AnimatorSet) {
                reverseAnimatorSet((AnimatorSet) animator);
            }
        }
    }

    public static Bitmap rotate(Bitmap bitmap, int i) {
        return rotateAndMirror(bitmap, i, false);
    }

    public static Bitmap rotateAndMirror(Bitmap bitmap, int i, boolean z) {
        int i2;
        if ((i == 0 && !z) || bitmap == null) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        if (z) {
            matrix.postScale(-1.0f, 1.0f);
            i = (i + m.cQ) % m.cQ;
            if (i == 0 || i == 180) {
                i2 = bitmap.getWidth();
            } else if (i == 90 || i == 270) {
                i2 = bitmap.getHeight();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid degrees=");
                sb.append(i);
                throw new IllegalArgumentException(sb.toString());
            }
            matrix.postTranslate((float) i2, 0.0f);
        }
        if (i != 0) {
            matrix.postRotate((float) i, ((float) bitmap.getWidth()) / 2.0f, ((float) bitmap.getHeight()) / 2.0f);
        }
        try {
            Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (bitmap == createBitmap) {
                return bitmap;
            }
            bitmap.recycle();
            return createBitmap;
        } catch (OutOfMemoryError unused) {
            return bitmap;
        }
    }

    public static int roundOrientation(int i, int i2) {
        boolean z = true;
        if (i2 != -1) {
            int abs = Math.abs(i - i2);
            if (Math.min(abs, 360 - abs) < 75) {
                z = false;
            }
        }
        if (!z) {
            return i2;
        }
        int i3 = (((i + 70) / 90) * 90) % m.cQ;
        StringBuilder sb = new StringBuilder();
        sb.append("onOrientationChanged: orientation = ");
        sb.append(i3);
        Log.d(TAG, sb.toString());
        return i3;
    }

    public static int safeDelete(Uri uri, String str, String[] strArr) {
        int i = -1;
        try {
            i = CameraAppImpl.getAndroidContext().getContentResolver().delete(uri, str, strArr);
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("safeDelete url=");
            sb.append(uri);
            sb.append(" where=");
            sb.append(str);
            sb.append(" selectionArgs=");
            sb.append(strArr);
            sb.append(" result=");
            sb.append(i);
            Log.v(str2, sb.toString());
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return i;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0041 A[SYNTHETIC, Splitter:B:22:0x0041] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0052 A[SYNTHETIC, Splitter:B:28:0x0052] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean saveBitmap(Buffer buffer, int i, int i2, Config config, String str) {
        if (buffer != null) {
            Bitmap createBitmap = Bitmap.createBitmap(i, i2, config);
            createBitmap.copyPixelsFromBuffer(buffer);
            FileOutputStream fileOutputStream = null;
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(new File(str));
                try {
                    createBitmap.compress(CompressFormat.JPEG, 100, fileOutputStream2);
                    try {
                        fileOutputStream2.flush();
                        fileOutputStream2.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    createBitmap.recycle();
                    return true;
                } catch (FileNotFoundException e2) {
                    FileOutputStream fileOutputStream3 = fileOutputStream2;
                    e = e2;
                    fileOutputStream = fileOutputStream3;
                    try {
                        Log.e(TAG, "saveBitmap failed!", (Throwable) e);
                        if (fileOutputStream != null) {
                        }
                        createBitmap.recycle();
                        return false;
                    } catch (Throwable th) {
                        th = th;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.flush();
                                fileOutputStream.close();
                            } catch (Exception e3) {
                                e3.printStackTrace();
                            }
                        }
                        createBitmap.recycle();
                        throw th;
                    }
                } catch (Throwable th2) {
                    FileOutputStream fileOutputStream4 = fileOutputStream2;
                    th = th2;
                    fileOutputStream = fileOutputStream4;
                    if (fileOutputStream != null) {
                    }
                    createBitmap.recycle();
                    throw th;
                }
            } catch (FileNotFoundException e4) {
                e = e4;
                Log.e(TAG, "saveBitmap failed!", (Throwable) e);
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                }
                createBitmap.recycle();
                return false;
            }
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002d, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        $closeResource(r4, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0031, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void saveBlobToFile(byte[] bArr, String str) {
        String str2 = TAG;
        Objects.requireNonNull(str, "The target filepath must not be null");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(str);
            fileOutputStream.write(bArr);
            fileOutputStream.flush();
            StringBuilder sb = new StringBuilder();
            sb.append("successfully write blob into file: ");
            sb.append(str);
            Log.d(str2, sb.toString());
            $closeResource(null, fileOutputStream);
        } catch (IOException unused) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("failed to write blob into file: ");
            sb2.append(str);
            Log.d(str2, sb2.toString());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x004e  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0056  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void saveCameraCalibrationToFile(SatFusionCalibrationData[] satFusionCalibrationDataArr) {
        String str;
        boolean saveCameraCalibrationToFile;
        String str2;
        StringBuilder sb;
        if (satFusionCalibrationDataArr != null) {
            for (SatFusionCalibrationData satFusionCalibrationData : satFusionCalibrationDataArr) {
                int i = satFusionCalibrationData.type;
                if (i != 20) {
                    if (i != 21) {
                        if (i != 1310720) {
                            if (i != 1310743) {
                                if (i != 1376256) {
                                    if (i != 1507348) {
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append("back_dual_camera_caldata_");
                                        sb2.append(i);
                                        str = sb2.toString();
                                        saveCameraCalibrationToFile = saveCameraCalibrationToFile(satFusionCalibrationData.data, 0, satFusionCalibrationData.size, str);
                                        String str3 = TAG;
                                        String str4 = "@";
                                        if (!saveCameraCalibrationToFile) {
                                            sb = new StringBuilder();
                                            str2 = "Sat fusion calibration data successfully saved: ";
                                        } else {
                                            sb = new StringBuilder();
                                            str2 = "Sat fusion calibration data not saved: ";
                                        }
                                        sb.append(str2);
                                        sb.append(i);
                                        sb.append(str4);
                                        sb.append(str);
                                        sb.append(str4);
                                        sb.append(satFusionCalibrationData.size);
                                        Log.d(str3, sb.toString());
                                    }
                                }
                            }
                            str = "back_dual_camera_caldata_tut.bin";
                            saveCameraCalibrationToFile = saveCameraCalibrationToFile(satFusionCalibrationData.data, 0, satFusionCalibrationData.size, str);
                            String str32 = TAG;
                            String str42 = "@";
                            if (!saveCameraCalibrationToFile) {
                            }
                            sb.append(str2);
                            sb.append(i);
                            sb.append(str42);
                            sb.append(str);
                            sb.append(str42);
                            sb.append(satFusionCalibrationData.size);
                            Log.d(str32, sb.toString());
                        }
                    }
                    str = "back_dual_camera_caldata_wu.bin";
                    saveCameraCalibrationToFile = saveCameraCalibrationToFile(satFusionCalibrationData.data, 0, satFusionCalibrationData.size, str);
                    String str322 = TAG;
                    String str422 = "@";
                    if (!saveCameraCalibrationToFile) {
                    }
                    sb.append(str2);
                    sb.append(i);
                    sb.append(str422);
                    sb.append(str);
                    sb.append(str422);
                    sb.append(satFusionCalibrationData.size);
                    Log.d(str322, sb.toString());
                }
                str = "back_dual_camera_caldata.bin";
                saveCameraCalibrationToFile = saveCameraCalibrationToFile(satFusionCalibrationData.data, 0, satFusionCalibrationData.size, str);
                String str3222 = TAG;
                String str4222 = "@";
                if (!saveCameraCalibrationToFile) {
                }
                sb.append(str2);
                sb.append(i);
                sb.append(str4222);
                sb.append(str);
                sb.append(str4222);
                sb.append(satFusionCalibrationData.size);
                Log.d(str3222, sb.toString());
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002c, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002d, code lost:
        if (r8 != null) goto L_0x002f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        $closeResource(r5, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0032, code lost:
        throw r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean saveCameraCalibrationToFile(byte[] bArr, int i, int i2, String str) {
        String str2;
        String str3 = TAG;
        Context androidContext = CameraAppImpl.getAndroidContext();
        if (bArr == null || androidContext == null) {
            return false;
        }
        boolean z = true;
        if (isEqual(bArr, androidContext.getFileStreamPath(str))) {
            return true;
        }
        try {
            FileOutputStream openFileOutput = androidContext.openFileOutput(str, 0);
            openFileOutput.write(bArr, i, i2);
            if (openFileOutput != null) {
                try {
                    $closeResource(null, openFileOutput);
                } catch (FileNotFoundException e) {
                    e = e;
                } catch (IOException e2) {
                    e = e2;
                    str2 = "saveCameraCalibrationToFile: IOException";
                    Log.e(str3, str2, (Throwable) e);
                    return z;
                }
            }
        } catch (FileNotFoundException e3) {
            e = e3;
            boolean z2 = false;
            str2 = "saveCameraCalibrationToFile: FileNotFoundException";
            Log.e(str3, str2, (Throwable) e);
            return z;
        } catch (IOException e4) {
            e = e4;
            z = false;
            str2 = "saveCameraCalibrationToFile: IOException";
            Log.e(str3, str2, (Throwable) e);
            return z;
        }
        return z;
    }

    public static boolean saveCameraCalibrationToFile(byte[] bArr, String str) {
        if (bArr == null) {
            return false;
        }
        return saveCameraCalibrationToFile(bArr, 0, bArr.length, str);
    }

    protected static void saveCustomWatermark2File(Bitmap bitmap, String str) {
        boolean z;
        StringBuilder sb = new StringBuilder();
        sb.append("saveCustomWatermark2File: start... watermarkBitmap = ");
        sb.append(bitmap);
        String sb2 = sb.toString();
        String str2 = TAG;
        Log.d(str2, sb2);
        long currentTimeMillis = System.currentTimeMillis();
        if (bitmap != null && !bitmap.isRecycled()) {
            boolean z2 = true;
            if (C0122O00000o.instance().OOoOo0O()) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(CompressFormat.PNG, 90, byteArrayOutputStream);
                z2 = WatermarkMiSysUtils.writeFileToPersist(byteArrayOutputStream.toByteArray(), str);
            }
            if (z2) {
                FileOutputStream fileOutputStream = null;
                try {
                    File filesDir = CameraAppImpl.getAndroidContext().getFilesDir();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str);
                    sb3.append(TEMP_SUFFIX);
                    File file = new File(filesDir, sb3.toString());
                    FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                    try {
                        bitmap.compress(CompressFormat.PNG, 90, fileOutputStream2);
                        fileOutputStream2.flush();
                        z = file.renameTo(new File(filesDir, str));
                        closeSilently(fileOutputStream2);
                    } catch (IOException e) {
                        e = e;
                        fileOutputStream = fileOutputStream2;
                        try {
                            Log.e(str2, "saveCustomWatermark2File Failed to write image", (Throwable) e);
                            closeSilently(fileOutputStream);
                            z = false;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("saveCustomWatermark2File: watermarkBitmap = ");
                            sb4.append(bitmap);
                            sb4.append(", save result = ");
                            sb4.append(z);
                            sb4.append(", cost time = ");
                            sb4.append(System.currentTimeMillis() - currentTimeMillis);
                            sb4.append(d.H);
                            Log.d(str2, sb4.toString());
                        } catch (Throwable th) {
                            th = th;
                            closeSilently(fileOutputStream);
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        fileOutputStream = fileOutputStream2;
                        closeSilently(fileOutputStream);
                        throw th;
                    }
                } catch (IOException e2) {
                    e = e2;
                    Log.e(str2, "saveCustomWatermark2File Failed to write image", (Throwable) e);
                    closeSilently(fileOutputStream);
                    z = false;
                    StringBuilder sb42 = new StringBuilder();
                    sb42.append("saveCustomWatermark2File: watermarkBitmap = ");
                    sb42.append(bitmap);
                    sb42.append(", save result = ");
                    sb42.append(z);
                    sb42.append(", cost time = ");
                    sb42.append(System.currentTimeMillis() - currentTimeMillis);
                    sb42.append(d.H);
                    Log.d(str2, sb42.toString());
                }
                StringBuilder sb422 = new StringBuilder();
                sb422.append("saveCustomWatermark2File: watermarkBitmap = ");
                sb422.append(bitmap);
                sb422.append(", save result = ");
                sb422.append(z);
                sb422.append(", cost time = ");
                sb422.append(System.currentTimeMillis() - currentTimeMillis);
                sb422.append(d.H);
                Log.d(str2, sb422.toString());
            }
        }
        z = false;
        StringBuilder sb4222 = new StringBuilder();
        sb4222.append("saveCustomWatermark2File: watermarkBitmap = ");
        sb4222.append(bitmap);
        sb4222.append(", save result = ");
        sb4222.append(z);
        sb4222.append(", cost time = ");
        sb4222.append(System.currentTimeMillis() - currentTimeMillis);
        sb4222.append(d.H);
        Log.d(str2, sb4222.toString());
    }

    public static void saveImageToJpeg(Image image) {
        Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        ByteBuffer buffer2 = planes[2].getBuffer();
        int[] iArr = {planes[0].getRowStride(), planes[2].getRowStride()};
        int limit = buffer.limit();
        int limit2 = buffer2.limit();
        byte[] bArr = new byte[(limit + limit2)];
        buffer.rewind();
        buffer2.rewind();
        buffer.get(bArr, 0, limit);
        buffer2.get(bArr, limit, limit2);
        ImageHelper.saveYuvToJpg(bArr, image.getWidth(), image.getHeight(), iArr, String.format(Locale.ENGLISH, "%d_image_stride%d_%dx%d", new Object[]{Long.valueOf(image.getTimestamp()), Integer.valueOf(planes[0].getRowStride()), Integer.valueOf(image.getWidth()), Integer.valueOf(image.getHeight())}));
        StringBuilder sb = new StringBuilder();
        sb.append("saveImageToJpeg: ");
        sb.append(buffer.remaining());
        sb.append("|");
        sb.append(buffer2.remaining());
        Log.d(TAG, sb.toString());
    }

    public static void saveLastFrameGaussian2File(Bitmap bitmap) {
        boolean z;
        FileOutputStream fileOutputStream;
        IOException e;
        File filesDir;
        File file;
        StringBuilder sb = new StringBuilder();
        sb.append("saveLastFrameGaussian2File: start... blurBitmap = ");
        sb.append(bitmap);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        long currentTimeMillis = System.currentTimeMillis();
        if (bitmap != null && !bitmap.isRecycled()) {
            try {
                filesDir = CameraAppImpl.getAndroidContext().getFilesDir();
                file = new File(filesDir, "blur.jpg.tmp");
                fileOutputStream = new FileOutputStream(file);
            } catch (IOException e2) {
                fileOutputStream = null;
                e = e2;
                try {
                    Log.e(str, "saveLastFrameGaussian2File Failed to write image", (Throwable) e);
                    closeSilently(fileOutputStream);
                    z = false;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("saveLastFrameGaussian2File: blurBitmap = ");
                    sb3.append(bitmap);
                    sb3.append(", save result = ");
                    sb3.append(z);
                    sb3.append(", cost time = ");
                    sb3.append(System.currentTimeMillis() - currentTimeMillis);
                    sb3.append(d.H);
                    Log.d(str, sb3.toString());
                } catch (Throwable th) {
                    th = th;
                    closeSilently(fileOutputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = null;
                closeSilently(fileOutputStream);
                throw th;
            }
            try {
                bitmap.compress(CompressFormat.JPEG, 90, fileOutputStream);
                fileOutputStream.flush();
                z = file.renameTo(new File(filesDir, LAST_FRAME_GAUSSIAN_FILE_NAME));
                closeSilently(fileOutputStream);
            } catch (IOException e3) {
                e = e3;
                Log.e(str, "saveLastFrameGaussian2File Failed to write image", (Throwable) e);
                closeSilently(fileOutputStream);
                z = false;
                StringBuilder sb32 = new StringBuilder();
                sb32.append("saveLastFrameGaussian2File: blurBitmap = ");
                sb32.append(bitmap);
                sb32.append(", save result = ");
                sb32.append(z);
                sb32.append(", cost time = ");
                sb32.append(System.currentTimeMillis() - currentTimeMillis);
                sb32.append(d.H);
                Log.d(str, sb32.toString());
            }
            StringBuilder sb322 = new StringBuilder();
            sb322.append("saveLastFrameGaussian2File: blurBitmap = ");
            sb322.append(bitmap);
            sb322.append(", save result = ");
            sb322.append(z);
            sb322.append(", cost time = ");
            sb322.append(System.currentTimeMillis() - currentTimeMillis);
            sb322.append(d.H);
            Log.d(str, sb322.toString());
        }
        z = false;
        StringBuilder sb3222 = new StringBuilder();
        sb3222.append("saveLastFrameGaussian2File: blurBitmap = ");
        sb3222.append(bitmap);
        sb3222.append(", save result = ");
        sb3222.append(z);
        sb3222.append(", cost time = ");
        sb3222.append(System.currentTimeMillis() - currentTimeMillis);
        sb3222.append(d.H);
        Log.d(str, sb3222.toString());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0027, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0028, code lost:
        $closeResource(r2, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002b, code lost:
        throw r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean saveToFile(Bitmap bitmap, String str, int i, CompressFormat compressFormat) {
        if (bitmap == null || bitmap.isRecycled()) {
            return false;
        }
        File file = new File(str);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(str);
        bitmap.compress(compressFormat, i, fileOutputStream);
        $closeResource(null, fileOutputStream);
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x002d A[SYNTHETIC, Splitter:B:22:0x002d] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0037 A[SYNTHETIC, Splitter:B:28:0x0037] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0043 A[SYNTHETIC, Splitter:B:35:0x0043] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:25:0x0032=Splitter:B:25:0x0032, B:19:0x0028=Splitter:B:19:0x0028} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean saveToFile(String str, File file) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        FileOutputStream fileOutputStream = null;
        try {
            FileOutputStream fileOutputStream2 = new FileOutputStream(file);
            try {
                fileOutputStream2.write(str.getBytes());
                fileOutputStream2.close();
            } catch (FileNotFoundException e) {
                e = e;
                fileOutputStream = fileOutputStream2;
                e.printStackTrace();
                if (fileOutputStream != null) {
                }
                return true;
            } catch (IOException e2) {
                e = e2;
                fileOutputStream = fileOutputStream2;
                try {
                    e.printStackTrace();
                    if (fileOutputStream != null) {
                    }
                    return true;
                } catch (Throwable th) {
                    th = th;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = fileOutputStream2;
                if (fileOutputStream != null) {
                }
                throw th;
            }
            try {
                fileOutputStream2.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        } catch (FileNotFoundException e5) {
            e = e5;
            e.printStackTrace();
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            return true;
        } catch (IOException e6) {
            e = e6;
            e.printStackTrace();
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            return true;
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x004e A[SYNTHETIC, Splitter:B:17:0x004e] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0059 A[SYNTHETIC, Splitter:B:22:0x0059] */
    /* JADX WARNING: Removed duplicated region for block: B:28:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void saveYuv(byte[] bArr, String str) {
        String str2 = "Failed to flush/close stream";
        String str3 = TAG;
        FileOutputStream fileOutputStream = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("sdcard/DCIM/Camera/");
            sb.append(str);
            sb.append(".yuv");
            String sb2 = sb.toString();
            FileOutputStream fileOutputStream2 = new FileOutputStream(sb2);
            try {
                fileOutputStream2.write(bArr);
                fileOutputStream2.flush();
                StringBuilder sb3 = new StringBuilder();
                sb3.append("saveYuv: ");
                sb3.append(sb2);
                Log.v(str3, sb3.toString());
                try {
                    fileOutputStream2.close();
                } catch (Exception e) {
                    Log.e(str3, str2, (Throwable) e);
                }
            } catch (Exception e2) {
                e = e2;
                fileOutputStream = fileOutputStream2;
                try {
                    Log.e(str3, "Failed to write image", (Throwable) e);
                    if (fileOutputStream == null) {
                        fileOutputStream.close();
                    }
                } catch (Throwable th) {
                    th = th;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e3) {
                            Log.e(str3, str2, (Throwable) e3);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = fileOutputStream2;
                if (fileOutputStream != null) {
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            Log.e(str3, "Failed to write image", (Throwable) e);
            if (fileOutputStream == null) {
            }
        }
    }

    public static void saveYuvToJpg(byte[] bArr, int i, int i2, int[] iArr, String str) {
        String str2 = TAG;
        if (bArr == null) {
            Log.w(str2, "saveYuvToJpg: null data");
            return;
        }
        YuvImage yuvImage = new YuvImage(bArr, 17, i, i2, iArr);
        StringBuilder sb = new StringBuilder();
        sb.append("sdcard/DCIM/Camera/");
        sb.append(str);
        sb.append(Storage.JPEG_SUFFIX);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("saveYuvToJpg: ");
        sb3.append(sb2);
        Log.v(str2, sb3.toString());
        try {
            yuvImage.compressToJpeg(new Rect(0, 0, i, i2), 100, new FileOutputStream(sb2));
        } catch (FileNotFoundException e) {
            Log.e(str2, e.getMessage(), (Throwable) e);
        }
    }

    public static void scaleCamera2Matrix(Matrix matrix, Rect rect, float f) {
        matrix.postScale(f, f);
        matrix.preTranslate(((float) (-rect.width())) / 2.0f, ((float) (-rect.height())) / 2.0f);
    }

    public static void setBrightnessRampRate(int i) {
    }

    public static void setPixels(byte[] bArr, int i, int i2, byte[] bArr2, int[] iArr) {
        if (bArr != null && bArr2 != null) {
            int i3 = ((iArr[1] * i) + iArr[0]) * i2;
            int i4 = 0;
            for (int i5 = 0; i5 < iArr[3]; i5++) {
                System.arraycopy(bArr2, i4, bArr, i3, iArr[2] * i2);
                i4 += iArr[2] * i2;
                i3 += i * i2;
            }
        }
    }

    public static void setScreenEffect(boolean z) {
        if (C0124O00000oO.Oo00Oo()) {
            try {
                DisplayFeatureManager.getInstance().setScreenEffect(14, z ? 1 : 0);
            } catch (Exception e) {
                Log.d(TAG, "Meet Exception when calling DisplayFeatureManager#setScreenEffect()", (Throwable) e);
            }
        }
    }

    private static void setTagValue(ExifInterface exifInterface, int i, Object obj) {
        if (!exifInterface.setTagValue(i, obj)) {
            exifInterface.setTag(exifInterface.buildTag(i, obj));
        }
    }

    public static void setViewAndChildrenEnabled(View view, boolean z) {
        view.setEnabled(z);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                setViewAndChildrenEnabled(viewGroup.getChildAt(i), z);
            }
        }
    }

    public static boolean shareMediaToMore(Context context, String str, CharSequence charSequence, boolean z) {
        try {
            context.startActivity(Intent.createChooser(getShareMediaIntent(context, str, z), charSequence));
            return true;
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "failed to share video shareMore ", (Throwable) e);
            return false;
        }
    }

    public static void showErrorAndFinish(final Activity activity, int i, boolean z) {
        if (!activity.isFinishing()) {
            final boolean z2 = i == R.string.cannot_connect_camera_twice || i == R.string.cannot_connect_camera_once;
            StringBuilder sb = new StringBuilder();
            sb.append("showErrorAndFinish: ");
            sb.append(activity.getResources().getString(i));
            String sb2 = sb.toString();
            String str = TAG;
            Log.k(6, str, sb2);
            AlertDialog show = new AlertDialog.Builder(activity).setCancelable(false).setIconAttribute(16843605).setTitle(R.string.camera_error_title).setMessage(i).setNeutralButton(R.string.dialog_ok, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (z2) {
                        Util.dumpBugReportLog();
                    }
                    Camera2DataContainer.getInstance().reset();
                    activity.finish();
                }
            }).show();
            if (z2) {
                CameraStatUtils.trackCameraErrorDialogShow();
            }
            if (sIsKillCameraService && VERSION.SDK_INT >= 26 && C0124O00000oO.Oo000OO() && z2 && !sIsDumpBugReport) {
                if (SystemClock.elapsedRealtime() - CameraSettings.getBroadcastKillServiceTime() > 60000) {
                    Log.k(6, str, "Error dialog: broadcastKillService");
                    broadcastKillService(activity, z);
                }
                final Button button = show.getButton(-3);
                button.setTextAppearance(GeneralUtils.miuiWidgetButtonDialog());
                button.setEnabled(false);
                final Activity activity2 = activity;
                AnonymousClass2 r2 = new CountDownTimer(5000, 1000) {
                    public void onFinish() {
                        if (!((ActivityBase) activity2).isActivityPaused()) {
                            button.setEnabled(true);
                            button.setText(activity2.getResources().getString(R.string.dialog_ok));
                        }
                    }

                    public void onTick(long j) {
                        if (!((ActivityBase) activity2).isActivityPaused()) {
                            button.setText(activity2.getResources().getString(R.string.dialog_ok_time, new Object[]{Long.valueOf(j / 1000)}));
                        }
                    }
                };
                final CountDownTimer start = r2.start();
                show.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        CountDownTimer countDownTimer = start;
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                    }
                });
            }
            ((ActivityBase) activity).setErrorDialog(show);
        }
    }

    public static void showSurfaceInfo(Surface surface) {
        boolean isValid = surface.isValid();
        boolean isSurfaceForHwVideoEncoder = SurfaceUtils.isSurfaceForHwVideoEncoder(surface);
        Size surfaceSize = SurfaceUtils.getSurfaceSize(surface);
        int surfaceFormat = SurfaceUtils.getSurfaceFormat(surface);
        StringBuilder sb = new StringBuilder();
        sb.append("showSurfaceInfo: ");
        sb.append(surface);
        String str = "|";
        sb.append(str);
        sb.append(isValid);
        sb.append(str);
        sb.append(surfaceSize);
        sb.append(str);
        sb.append(surfaceFormat);
        sb.append(str);
        sb.append(isSurfaceForHwVideoEncoder);
        Log.d(TAG, sb.toString());
    }

    private static void skip(XmlPullParser xmlPullParser) {
        if (xmlPullParser.getEventType() == 2) {
            int i = 1;
            while (i != 0) {
                int next = xmlPullParser.next();
                if (next == 2) {
                    i++;
                } else if (next == 3) {
                    i--;
                }
            }
            return;
        }
        throw new IllegalStateException();
    }

    public static boolean startActivityForResultCatchException(Activity activity, Intent intent, int i) {
        try {
            activity.startActivityForResult(intent, i);
            return true;
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "failed to start activity", (Throwable) e);
            return false;
        }
    }

    public static void startScreenSlideAlphaInAnimation(View view) {
        ViewCompat.setAlpha(view, 0.0f);
        ViewCompat.animate(view).alpha(1.0f).setDuration(350).setStartDelay(400).setInterpolator(new SineEaseInOutInterpolator()).start();
    }

    public static boolean startShareMedia(String str, String str2, Context context, String str3, boolean z) {
        Intent shareMediaIntent = getShareMediaIntent(context, str3, z);
        shareMediaIntent.setComponent(new ComponentName(str, str2));
        try {
            context.startActivity(shareMediaIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("failed to share video ");
            sb.append(str3);
            Log.e(TAG, sb.toString(), (Throwable) e);
            return false;
        }
    }

    public static int stringSparseArraysIndexOf(SparseArray sparseArray, String str) {
        if (str != null) {
            for (int i = 0; i < sparseArray.size(); i++) {
                if (str.equals(sparseArray.valueAt(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static String toHumanString(long j) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(j));
    }

    public static void updateAccessibility(Context context) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService("accessibility");
        boolean z = accessibilityManager.isEnabled() && accessibilityManager.isTouchExplorationEnabled();
        sIsAccessibilityEnable = z;
        sIsVoiceAccessibilityEnable = getEnabledServicesFromSettings(context).contains(ComponentName.unflattenFromString(VOICEACCESS_A11y_SERVICE));
    }

    public static void updateDeviceConfig(Context context) {
        sRegion = SystemProperties.get("ro.miui.region");
        String str = ((TelephonyManager) context.getSystemService("phone")).getSimState() != 5 ? sRegion : null;
        if (TextUtils.isEmpty(str)) {
            Country detectCountry = ((CountryDetector) context.getSystemService("country_detector")).detectCountry();
            if (detectCountry != null) {
                str = detectCountry.getCountryIso();
            }
        }
        mCountryIso = str;
        StringBuilder sb = new StringBuilder();
        sb.append("antiBanding mCountryIso=");
        sb.append(mCountryIso);
        sb.append(" sRegion=");
        sb.append(sRegion);
        Log.d(TAG, sb.toString());
        sIsDumpLog = SystemProperties.getBoolean("camera_dump_parameters", DEBUG);
        sIsDumpBugReport = SystemProperties.getBoolean("camera_dump_bug_report", false);
        sIsDumpOrigJpg = SystemProperties.getBoolean("camera_dump_orig_jpg", false);
        sIsKillCameraService = SystemProperties.getBoolean("kill_camera_service_enable", C0124O00000oO.O0o0ooO);
        sSuperNightDefaultModeEnable = SystemProperties.getBoolean("super_night_default_mode_enable", false);
    }

    public static void updateOrientationLayoutRotation(View view, int i, int i2) {
        int i3;
        if (view != null) {
            int i4 = (360 - i2) % m.cQ;
            if (i != i2) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                int screenOrientation = Display.getScreenOrientation();
                if (screenOrientation != 0) {
                    if (screenOrientation == 1 || screenOrientation == 2) {
                        layoutParams.width = Display.getWindowWidth();
                        layoutParams.height = Display.getWindowWidth();
                        i3 = 16;
                    }
                    view.setRotation((float) i4);
                    view.setLayoutParams(layoutParams);
                } else {
                    layoutParams.width = Display.getWindowWidth();
                    layoutParams.height = Display.getWindowHeight();
                    i3 = 0;
                }
                layoutParams.gravity = i3;
                view.setRotation((float) i4);
                view.setLayoutParams(layoutParams);
            }
        }
    }

    public static void updateSelectIndicator(View view, final boolean z, boolean z2) {
        float f = 0.0f;
        if (z2) {
            view.setAlpha(z ? 0.0f : 1.0f);
            ViewPropertyAnimatorCompat duration = ViewCompat.animate(view).setDuration(200);
            if (z) {
                f = 1.0f;
            }
            duration.alpha(f).setInterpolator(new CubicEaseOutInterpolator()).setListener(new ViewPropertyAnimatorListenerAdapter() {
                public void onAnimationEnd(View view) {
                    super.onAnimationEnd(view);
                    if (!z) {
                        view.setVisibility(8);
                    }
                }

                public void onAnimationStart(View view) {
                    if (z) {
                        view.setVisibility(0);
                    }
                }
            }).start();
            return;
        }
        view.setVisibility(z ? 0 : 8);
        if (z) {
            f = 1.0f;
        }
        view.setAlpha(f);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0017, code lost:
        $closeResource(r1, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001a, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0014, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0015, code lost:
        if (r0 != null) goto L_0x0017;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void verifyAssetZip(Context context, String str, String str2, int i) {
        InputStream open = MultiFeatureManagerImpl.getAssetManager(context).open(str);
        verifyZip(open, str2, i);
        if (open != null) {
            $closeResource(null, open);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002f, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x002b, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002c, code lost:
        $closeResource(r1, r2);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void verifyFileZip(Context context, String str, String str2, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("verifyAssetZip ");
        sb.append(str);
        Log.d(TAG, sb.toString());
        FileInputStream fileInputStream = new FileInputStream(new File(str));
        verifyZip((InputStream) fileInputStream, str2, i);
        $closeResource(null, fileInputStream);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0013, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x000f, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0010, code lost:
        $closeResource(r1, r0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void verifySdcardZip(String str, String str2, int i) {
        FileInputStream fileInputStream = new FileInputStream(str);
        verifyZip((InputStream) fileInputStream, str2, i);
        $closeResource(null, fileInputStream);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00bd, code lost:
        r12 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
        $closeResource(r11, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00c1, code lost:
        throw r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00c8, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:?, code lost:
        $closeResource(r10, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00cc, code lost:
        throw r11;
     */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x008b  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0013 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void verifyZip(InputStream inputStream, String str, int i) {
        try {
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            File file = new File(str);
            if (!file.exists()) {
                file.mkdirs();
            }
            while (true) {
                ZipEntry nextEntry = zipInputStream.getNextEntry();
                if (nextEntry != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append("/");
                    sb.append(nextEntry.getName());
                    File file2 = new File(sb.toString());
                    boolean z = true;
                    if (file2.exists()) {
                        if (!nextEntry.isDirectory()) {
                            if (!file2.isFile()) {
                                file2.delete();
                                file2.createNewFile();
                            }
                            if (Verifier.crc32(file2, i) != nextEntry.getCrc()) {
                                if (!z) {
                                }
                            }
                        } else if (!file2.isDirectory()) {
                            file2.delete();
                        }
                        z = false;
                        if (!z) {
                        }
                    } else if (!nextEntry.isDirectory()) {
                        File file3 = new File(file2.getParent());
                        if (!file3.exists()) {
                            file3.mkdirs();
                        }
                        file2.createNewFile();
                        if (!z) {
                            String str2 = TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("corrupted ");
                            sb2.append(nextEntry.getName());
                            Log.w(str2, sb2.toString());
                            FileOutputStream fileOutputStream = new FileOutputStream(file2);
                            byte[] bArr = new byte[i];
                            while (true) {
                                int read = zipInputStream.read(bArr);
                                if (read <= 0) {
                                    break;
                                }
                                fileOutputStream.write(bArr, 0, read);
                            }
                            $closeResource(null, fileOutputStream);
                        }
                    }
                    file2.mkdirs();
                    z = false;
                    if (!z) {
                    }
                } else {
                    $closeResource(null, zipInputStream);
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00d0, code lost:
        r12 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
        $closeResource(r11, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00d4, code lost:
        throw r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00d7, code lost:
        r12 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00d8, code lost:
        if (r1 != null) goto L_0x00da;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:?, code lost:
        $closeResource(r11, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00dd, code lost:
        throw r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00e4, code lost:
        r12 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x00e5, code lost:
        $closeResource(r11, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00e8, code lost:
        throw r12;
     */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0095  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0017 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void verifyZip(String str, String str2, int i) {
        ZipFile zipFile = new ZipFile(str);
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdirs();
        }
        Enumeration entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append("/");
            sb.append(zipEntry.getName());
            File file2 = new File(sb.toString());
            boolean z = true;
            if (file2.exists()) {
                if (!zipEntry.isDirectory()) {
                    if (!file2.isFile()) {
                        file2.delete();
                        file2.createNewFile();
                    }
                    if (Verifier.crc32(file2, i) != zipEntry.getCrc()) {
                        if (!z) {
                        }
                    }
                } else if (!file2.isDirectory()) {
                    file2.delete();
                }
                z = false;
                if (!z) {
                }
            } else if (!zipEntry.isDirectory()) {
                File file3 = new File(file2.getParent());
                if (!file3.exists()) {
                    file3.mkdirs();
                }
                file2.createNewFile();
                if (!z) {
                    String str3 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("corrupted ");
                    sb2.append(zipEntry.getName());
                    Log.w(str3, sb2.toString());
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    byte[] bArr = new byte[i];
                    while (true) {
                        int read = inputStream.read(bArr);
                        if (read <= 0) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, read);
                    }
                    $closeResource(null, fileOutputStream);
                    if (inputStream != null) {
                        $closeResource(null, inputStream);
                    } else {
                        continue;
                    }
                }
            }
            file2.mkdirs();
            z = false;
            if (!z) {
            }
        }
        $closeResource(null, zipFile);
    }

    /* JADX INFO: used method not loaded: com.android.camera.log.Log.e(java.lang.String, java.lang.String, java.lang.Throwable):null, types can be incorrect */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:4|5|6|7|10) */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0037, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0038, code lost:
        r0 = new java.lang.StringBuilder();
        r0.append("review image fail. uri=");
        r0.append(r3);
        com.android.camera.log.Log.e(r1, r0.toString(), (java.lang.Throwable) r4);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x002c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void viewUri(Uri uri, Context context) {
        boolean isUriValid = isUriValid(uri, context.getContentResolver());
        String str = TAG;
        if (!isUriValid) {
            StringBuilder sb = new StringBuilder();
            sb.append("Uri invalid. uri=");
            sb.append(uri);
            Log.e(str, sb.toString());
            return;
        }
        context.startActivity(new Intent(REVIEW_ACTION, uri));
        context.startActivity(new Intent("android.intent.action.VIEW", uri));
    }

    public static String viewVisibilityToString(int i) {
        return i != 0 ? i != 4 ? i != 8 ? "UNKNOWN" : "GONE" : "INVISIBLE" : "VISIBLE";
    }

    public static boolean workaroundForJ1() {
        return C0124O00000oO.O0Ooo0o.equals("cmi");
    }

    public static void writeFile2Stream(File file, ByteArrayOutputStream byteArrayOutputStream, int i) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bArr = new byte[1024];
            fileInputStream.skip((long) i);
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read < 0) {
                    fileInputStream.close();
                    return;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
