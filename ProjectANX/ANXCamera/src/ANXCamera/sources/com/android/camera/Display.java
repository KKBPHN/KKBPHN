package com.android.camera;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.provider.MiuiSettings.Global;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import android.view.WindowManager;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import java.util.Locale;

public class Display {
    public static final String DISPLAY_RATIO_123 = "4:3";
    public static final String DISPLAY_RATIO_169 = "16:9";
    public static final String DISPLAY_RATIO_456 = "3:1";
    private static final String TAG = "Display";
    private static boolean isNotchDevice = false;
    private static IDisplayRect mDisplayRectCompat = null;
    private static boolean mIsInitialized = false;
    private static boolean sHasNavigationBar = false;
    private static boolean sIsFullScreenNavBarHidden = false;
    private static boolean sIsnotchScreenHidden = false;
    private static int sNavigationBarHeight = 0;
    private static float sPixelDensity = 1.0f;
    private static float sPixelDensityScale = 1.0f;
    private static int sScreenOriention = 0;
    private static int sStatusBarHeight = 0;
    private static int sWindowHeight = 1080;
    private static IWindowManager sWindowManager = null;
    private static int sWindowWidth = 720;

    static {
        if (!mIsInitialized) {
            init(CameraAppImpl.getAndroidContext());
        }
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        if (sWindowManager == null) {
            sWindowManager = Stub.asInterface(ServiceManager.getService("window"));
            try {
                sHasNavigationBar = CompatibilityUtils.hasNavigationBar(context, sWindowManager);
            } catch (Exception unused) {
                Log.e(TAG, "checkDeviceHasNavigationBar exception");
            }
        }
        return sHasNavigationBar;
    }

    public static boolean fitDisplayFull(float f) {
        return f == 1.3333333f && getDisplayRatio() == DISPLAY_RATIO_123;
    }

    public static int getBottomBarHeight() {
        return getDisplayAdapter().getBottomBarHeight();
    }

    public static int getBottomHeight() {
        return getDisplayAdapter().getBottomHeight();
    }

    public static int getBottomMargin() {
        return getDisplayAdapter().getBottomMargin();
    }

    public static int getCenterDisplayHeight() {
        return getDisplayAdapter().getCenterDisplayHeight();
    }

    private static IDisplayRect getDisplayAdapter() {
        if (mDisplayRectCompat == null) {
            init(CameraAppImpl.getAndroidContext());
        }
        return mDisplayRectCompat;
    }

    public static String getDisplayRatio() {
        return getDisplayAdapter().getDisplayRatio();
    }

    public static Rect getDisplayRect(int i) {
        return getDisplayAdapter().getDisplayRect(i);
    }

    public static int getDragDistanceFix() {
        return getDisplayAdapter().getDragDistanceFix();
    }

    public static int getDragLayoutTopMargin() {
        return getDisplayAdapter().getDragLayoutTopMargin();
    }

    public static int getEndMargin() {
        return getDisplayAdapter().getEndMargin();
    }

    public static int getGestureLineHeight(Context context) {
        if (!isEnableGestureLine(context)) {
            return 0;
        }
        return getNavigationBarHeight(context);
    }

    public static Rect getMaxViewFinderRect() {
        return getDisplayAdapter().getMaxViewFinderRect();
    }

    public static int[] getMoreModePrefVideo(boolean z) {
        return getDisplayAdapter().getMoreModePrefVideo(z);
    }

    public static int getMoreModeTabCol(int i, boolean z) {
        return getDisplayAdapter().getMoreModeTabCol(i, z);
    }

    public static int getMoreModeTabMarginVer(int i, boolean z) {
        return getDisplayAdapter().getMoreModeTabMarginVer(i, z);
    }

    public static int getMoreModeTabRow(int i, boolean z) {
        return getDisplayAdapter().getMoreModeTabRow(i, z);
    }

    public static int getNavigationBarHeight() {
        return sNavigationBarHeight;
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("navBarHeight=");
        sb.append(dimensionPixelSize);
        Log.v(str, sb.toString());
        return dimensionPixelSize;
    }

    public static float getPixelDensity() {
        return sPixelDensity;
    }

    public static int getScreenOrientation() {
        return sScreenOriention;
    }

    private static int getScreenOrientation(int i) {
        if (i != 90) {
            return i != 270 ? 0 : 1;
        }
        return 2;
    }

    public static int getSquareBottomCoverHeight() {
        return getDisplayAdapter().getSquareBottomCoverHeight();
    }

    public static int getStartMargin() {
        return getDisplayAdapter().getStartMargin();
    }

    public static int getStatusBarHeight() {
        return sStatusBarHeight;
    }

    public static int getStatusBarHeight(Context context) {
        int i;
        int identifier;
        Resources resources;
        if (C0122O00000o.instance().OO00ooO()) {
            resources = context.getResources();
            identifier = R.dimen.camera_status_bar_height;
        } else {
            identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (identifier > 0) {
                resources = context.getResources();
            } else {
                i = 0;
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("StatusBarHeight=");
                sb.append(i);
                Log.v(str, sb.toString());
                return i;
            }
        }
        i = resources.getDimensionPixelSize(identifier);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("StatusBarHeight=");
        sb2.append(i);
        Log.v(str2, sb2.toString());
        return i;
    }

    public static int getTipsMarginTop() {
        return getDisplayAdapter().getTipsMarginTop();
    }

    public static int getTopBarHeight() {
        return getDisplayAdapter().getTopBarHeight();
    }

    public static int getTopBarWidth() {
        return getDisplayAdapter().getTopBarWidth();
    }

    public static int getTopCoverHeight() {
        return getDisplayAdapter().getTopCoverHeight();
    }

    public static int getTopHeight() {
        return getDisplayAdapter().getTopMargin() + getDisplayAdapter().getTopBarHeight();
    }

    public static int getTopMargin() {
        return getDisplayAdapter().getTopMargin();
    }

    public static int getWindowHeight() {
        return sWindowHeight;
    }

    public static IWindowManager getWindowManager() {
        if (sWindowManager == null) {
            sWindowManager = Stub.asInterface(ServiceManager.getService("window"));
        }
        return sWindowManager;
    }

    public static int getWindowWidth() {
        return sWindowWidth;
    }

    public static float getsPixelDensityScale() {
        return sPixelDensityScale;
    }

    public static void init(Context context) {
        if (context == null) {
            Log.w(TAG, "initialize: context is null");
            return;
        }
        sIsnotchScreenHidden = isNotchScreenHidden(context);
        boolean z = SystemProperties.getInt("ro.miui.notch", 0) == 1 && !sIsnotchScreenHidden;
        isNotchDevice = z;
        if (Build.DEVICE.equalsIgnoreCase("laurel_sprout")) {
            isNotchDevice = !sIsnotchScreenHidden;
        }
        sIsFullScreenNavBarHidden = isFullScreenNavBarHidden(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        sPixelDensity = displayMetrics.density;
        Point point = new Point();
        windowManager.getDefaultDisplay().getRealSize(point);
        int i = point.x;
        int i2 = point.y;
        if (i < i2) {
            sWindowWidth = i;
            sWindowHeight = i2;
        } else {
            sWindowWidth = i2;
            sWindowHeight = i;
        }
        if ("hercules".equals(Build.DEVICE)) {
            sWindowHeight = 2244;
        }
        sPixelDensity = displayMetrics.density;
        sPixelDensityScale = (((float) Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels)) / sPixelDensity) / 360.0f;
        mIsInitialized = true;
        initDisplayCompat(context);
        CameraSettings.BOTTOM_CONTROL_HEIGHT = getBottomHeight();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("initialize: sCenterDisplayHeight = ");
        sb.append(getCenterDisplayHeight());
        sb.append(", sTopMargin = ");
        sb.append(getTopMargin());
        sb.append(", sTopBarHeight = ");
        sb.append(getTopBarHeight());
        sb.append(", sBottomMargin = ");
        sb.append(getBottomMargin());
        sb.append(", sBottomBarHeight = ");
        sb.append(getBottomBarHeight());
        sb.append(", windowSize = ");
        sb.append(getWindowWidth());
        sb.append("x");
        sb.append(getWindowHeight());
        sb.append(", isNotchDevice = ");
        sb.append(isNotchDevice);
        sb.append(", mIsInitialized = ");
        sb.append(mIsInitialized);
        Log.w(str, sb.toString());
        Log.i(TAG, String.format(Locale.ENGLISH, "windowSize=%dx%d density=%.4f", new Object[]{Integer.valueOf(getWindowWidth()), Integer.valueOf(getWindowHeight()), Float.valueOf(sPixelDensity)}));
    }

    private static void initDisplayCompat(Context context) {
        DisplayParameter displayParameter = new DisplayParameter();
        displayParameter.windowHeight = getWindowHeight();
        displayParameter.windowWidth = getWindowWidth();
        displayParameter.isNotchDevice = isNotchDevice();
        displayParameter.statusBarHeight = getStatusBarHeight(context);
        mDisplayRectCompat = new DisplayAdapter(context, displayParameter);
    }

    public static void initStatusBarHeight(Context context) {
        sNavigationBarHeight = checkDeviceHasNavigationBar(context) ? getNavigationBarHeight(context) : 0;
        sStatusBarHeight = getStatusBarHeight(context);
    }

    public static boolean isContentViewExtendToTopEdges() {
        return isNotchDevice;
    }

    public static boolean isEnableGestureLine(Context context) {
        if (!Global.getBoolean(context.getContentResolver(), "hide_gesture_line")) {
            return true;
        }
        boolean z = sHasNavigationBar;
        return false;
    }

    public static boolean isFullScreenNavBarHidden() {
        return sIsFullScreenNavBarHidden;
    }

    private static boolean isFullScreenNavBarHidden(Context context) {
        return Global.getBoolean(context.getContentResolver(), "force_fsg_nav_bar");
    }

    public static boolean isNotchDevice() {
        return isNotchDevice;
    }

    public static boolean isNotchScreenHidden() {
        return sIsnotchScreenHidden;
    }

    public static boolean isNotchScreenHidden(Context context) {
        if (VERSION.SDK_INT < 28) {
            return false;
        }
        boolean z = true;
        if (Settings.Global.getInt(context.getContentResolver(), "force_black_v2", 0) != 1) {
            z = false;
        }
        return z;
    }

    public static boolean needAlphaAnimation4PopMore() {
        return getDisplayAdapter().needAlphaAnimation4PopMore();
    }

    public static void updateOrientation(int i) {
        sScreenOriention = getScreenOrientation(i);
    }
}
