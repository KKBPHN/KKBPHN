package com.ss.android.medialib;

import android.content.Context;
import android.content.res.AssetManager;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.bef.effectsdk.ResourceFinder;
import com.ss.android.medialib.common.LogInvoker;
import com.ss.android.medialib.common.LogUtil;
import com.ss.android.medialib.config.PerformanceConfig;
import com.ss.android.medialib.utils.DeviceInfoDetector;
import com.ss.android.ttve.nativePort.TENativeLibsLoader;
import com.ss.android.vesdk.VELogUtil;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Keep
public final class VideoSdkCore {
    @RestrictTo({Scope.LIBRARY})
    public static Context APPLICATION_CONTEXT = null;
    public static final int PRODUCT_AWEME = 0;
    public static final int PRODUCT_HOTSOON = 1;
    public static final int PRODUCT_OTHER = 2;
    /* access modifiers changed from: private */
    public static final String TAG = "VideoSdkCore";
    private static boolean inited;
    private static ResourceFinder sFinder;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ProductType {
    }

    static {
        TENativeLibsLoader.loadLibrary();
    }

    public static void enableGLES3(boolean z) {
        nativeEnableGLES3(z);
    }

    public static long getNativeFinder(long j) {
        String str;
        String str2;
        if (j == 0) {
            str = TAG;
            str2 = "getNativeFinder effectHandler is null";
        } else {
            ResourceFinder resourceFinder = sFinder;
            if (resourceFinder != null) {
                return resourceFinder.createNativeResourceFinder(j);
            }
            str = TAG;
            str2 = "错误调用finder相关接口";
        }
        VELogUtil.e(str, str2);
        return 0;
    }

    public static String getSdkVersionCode() {
        return nativeGetVersionCode();
    }

    public static String getSdkVersionName() {
        return nativeGetVersionName();
    }

    public static void init(final Context context) {
        if (!inited) {
            synchronized (VideoSdkCore.class) {
                if (!inited) {
                    APPLICATION_CONTEXT = context.getApplicationContext();
                    nativeSetAssertManagerFromJava(context.getAssets());
                    new Thread() {
                        public void run() {
                            try {
                                DeviceInfoDetector.init(context);
                                PerformanceConfig.restoreFromCache();
                            } catch (Exception e) {
                                String access$000 = VideoSdkCore.TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("VideoSdkCore init failed: ");
                                sb.append(e.toString());
                                VELogUtil.e(access$000, sb.toString());
                            }
                        }
                    }.start();
                    inited = true;
                }
            }
        }
    }

    private static native void nativeEnableGLES3(boolean z);

    private static native String nativeGetVersionCode();

    private static native String nativeGetVersionName();

    private static native void nativeSetAmazingShareDir(String str);

    private static native void nativeSetAssertManagerEnable(boolean z);

    private static native void nativeSetAssertManagerFromJava(AssetManager assetManager);

    private static native void nativeSetProduct(int i);

    private static native void nativeSetResourceFinderEnable(boolean z);

    public static void releaseNativeFinder(long j) {
        String str;
        String str2;
        if (j == 0) {
            str = TAG;
            str2 = "getNativeFinder effectHandler is null";
        } else {
            ResourceFinder resourceFinder = sFinder;
            if (resourceFinder == null) {
                str = TAG;
                str2 = "错误调用finder相关接口";
            } else {
                resourceFinder.release(j);
                return;
            }
        }
        VELogUtil.e(str, str2);
    }

    public static void setAmazingShareDir(@NonNull String str) {
        nativeSetAmazingShareDir(str);
    }

    public static void setEnableAssetManager(boolean z) {
        nativeSetAssertManagerEnable(z);
    }

    public static void setLogLevel(int i) {
        LogUtil.setUp((String) null, i);
        LogInvoker.setLogLevel(i);
    }

    public static void setProduct(int i) {
        nativeSetProduct(i);
    }

    public static void setResourceFinder(@NonNull ResourceFinder resourceFinder) {
        sFinder = resourceFinder;
        nativeSetResourceFinderEnable(true);
    }
}
