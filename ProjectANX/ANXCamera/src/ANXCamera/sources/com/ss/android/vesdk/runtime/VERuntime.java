package com.ss.android.vesdk.runtime;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.bef.effectsdk.EffectSDKUtils;
import com.bef.effectsdk.ResourceFinder;
import com.ss.android.medialib.VideoSdkCore;
import com.ss.android.ttve.monitor.DeviceInfoDetector;
import com.ss.android.ttve.monitor.IMonitor;
import com.ss.android.ttve.monitor.MonitorUtils;
import com.ss.android.ttve.monitor.MonitorUtils.IMonitorStatisticsListener;
import com.ss.android.ttve.monitor.TEMonitor;
import com.ss.android.ttve.nativePort.TENativeLibsLoader;
import com.ss.android.vesdk.TEEffectCallback;
import com.ss.android.vesdk.VEAB;
import com.ss.android.vesdk.VEListener.VEMonitorListener;
import com.ss.android.vesdk.VELogUtil;
import com.ss.android.vesdk.VEResult;
import com.ss.android.vesdk.VEVideoEncodeSettings;
import com.ss.android.vesdk.keyvaluepair.VEKeyValue;
import com.ss.android.vesdk.runtime.cloudconfig.PerformanceConfig;
import com.ss.android.vesdk.runtime.oauth.TEOAuth;
import com.ss.android.vesdk.runtime.oauth.TEOAuthResult;
import com.ss.android.vesdk.runtime.persistence.VESP;
import java.io.File;
import java.lang.ref.WeakReference;
import org.json.JSONObject;

public class VERuntime {
    private static final String TAG = "VERuntime";
    private VEAB mAB;
    Context mContext;
    private boolean mEnableGLES3;
    private VEEnv mEnv;
    /* access modifiers changed from: private */
    public WeakReference mExternalMonitorListenerRef;
    private boolean mIsInited;
    private IMonitor mMoniter;
    private VEResManager mResManager;
    private boolean mUseAssetManager;
    /* access modifiers changed from: private */
    public boolean mUseCloudConfig;
    private boolean mUseResourceFinder;
    /* access modifiers changed from: private */
    public WeakReference mVEMonitorListener;

    enum VERuntimeSingleton {
        INSTANCE;
        
        private VERuntime veRuntime;

        public VERuntime getInstance() {
            return this.veRuntime;
        }
    }

    private VERuntime() {
        this.mUseCloudConfig = false;
        this.mUseAssetManager = false;
        this.mUseResourceFinder = false;
        this.mEnableGLES3 = false;
        this.mIsInited = false;
        this.mMoniter = new IMonitor() {
            public void monitorLog(String str, JSONObject jSONObject) {
                if (VERuntime.this.mVEMonitorListener != null && VERuntime.this.mVEMonitorListener.get() != null) {
                    ((VEMonitorListener) VERuntime.this.mVEMonitorListener.get()).monitorLog(str, jSONObject);
                }
            }
        };
        MonitorUtils.setExternalMonitorListener(new IMonitorStatisticsListener() {
            public void onMonitorStatistics(String str, int i, String str2) {
                if (VERuntime.this.mExternalMonitorListenerRef != null) {
                    VEExternalMonitorListener vEExternalMonitorListener = (VEExternalMonitorListener) VERuntime.this.mExternalMonitorListenerRef.get();
                    if (vEExternalMonitorListener != null) {
                        vEExternalMonitorListener.onMonitorInvoked(str, i, str2);
                    }
                }
            }
        });
    }

    public static TEOAuthResult activate(Context context, @NonNull String str, @NonNull String str2, @NonNull String str3) {
        return TEOAuth.activate(context, str, str2, str3);
    }

    public static String getActivationCode() {
        return TEOAuth.getActivationCode();
    }

    @NonNull
    public static VERuntime getInstance() {
        return VERuntimeSingleton.INSTANCE.getInstance();
    }

    private void initConfig() {
        new Thread() {
            public void run() {
                String str = VESP.KEY_SENSOR_REPORTED;
                String str2 = VERuntime.TAG;
                try {
                    DeviceInfoDetector.init(VERuntime.this.mContext);
                    if (!((Boolean) VESP.getInstance().get(str, Boolean.valueOf(false))).booleanValue()) {
                        MonitorUtils.sensorReport(VERuntime.this.mContext);
                        VESP.getInstance().put(str, Boolean.valueOf(true));
                    }
                } catch (Exception e) {
                    Log.e(str2, "DeviceInfoDetector init failed", e);
                }
                if (VERuntime.this.mUseCloudConfig) {
                    try {
                        PerformanceConfig.restoreFromCache();
                    } catch (Exception e2) {
                        Log.e(str2, "PerformanceConfig restoreFromCache failed", e2);
                    }
                    PerformanceConfig.fetch();
                }
            }
        }.start();
    }

    public static boolean isValidAuthorization() {
        boolean isPermitted = TEOAuth.isPermitted();
        StringBuilder sb = new StringBuilder();
        sb.append("FAILED. TEOAuth isPermitted = ");
        sb.append(isPermitted);
        VELogUtil.e(TEOAuth.class, sb.toString());
        return isPermitted;
    }

    private native void nativeEnableHDH264HWDecoder(boolean z, int i);

    private native void nativeEnableTT265Decoder(boolean z);

    private native void nativeSetVideoEncodeSetting(VEVideoEncodeSettings vEVideoEncodeSettings);

    public void enableGLES3(boolean z) {
        this.mEnableGLES3 = z;
    }

    public int enableHDH264HWDecoder(boolean z, int i) {
        if (!this.mIsInited) {
            VELogUtil.e(TAG, "runtime not init");
            return VEResult.TER_INVALID_ENV;
        }
        if (i <= 720) {
            i = 730;
        }
        nativeEnableHDH264HWDecoder(z, i);
        return 0;
    }

    public int enableTT265Decoder(boolean z) {
        if (!this.mIsInited) {
            VELogUtil.e(TAG, "runtime not init");
            return VEResult.TER_INVALID_ENV;
        }
        nativeEnableTT265Decoder(z);
        return 0;
    }

    public VEAB getAB() {
        if (this.mAB == null) {
            this.mAB = new VEAB();
        }
        return this.mAB;
    }

    public AssetManager getAssetManager() {
        boolean z = this.mUseAssetManager;
        String str = TAG;
        if (!z) {
            VELogUtil.e(str, "disable use AssetManager!");
        }
        Context context = this.mContext;
        if (context != null) {
            return context.getAssets();
        }
        VELogUtil.e(str, "context is null!");
        return null;
    }

    public Context getContext() {
        return this.mContext;
    }

    public VEEnv getEnv() {
        return this.mEnv;
    }

    public VEResManager getResManager() {
        return this.mResManager;
    }

    @Deprecated
    public void init(@NonNull Context context, @NonNull VEEnv vEEnv) {
        if (!this.mIsInited) {
            this.mContext = context;
            this.mEnv = vEEnv;
            this.mAB = new VEAB();
            TENativeLibsLoader.setContext(context);
            this.mResManager = new VEResManager();
            VESP.getInstance().init(context);
            TEMonitor.init(this.mContext, (String) VESP.getInstance().get(VESP.KEY_DEVICEID, ""));
            VideoSdkCore.init(context);
            initConfig();
        }
    }

    public void init(@NonNull Context context, @NonNull String str) {
        if (!this.mIsInited) {
            this.mIsInited = true;
            this.mContext = context;
            TENativeLibsLoader.setContext(context);
            this.mEnv = new VEEnv();
            this.mEnv.setWorkspace(str);
            this.mAB = new VEAB();
            this.mResManager = new VEResManager();
            VESP.getInstance().init(context);
            TEMonitor.init(this.mContext, (String) VESP.getInstance().get(VESP.KEY_DEVICEID, ""));
            VideoSdkCore.init(context);
            initConfig();
            VEKeyValue vEKeyValue = new VEKeyValue();
            vEKeyValue.add("iesve_vesdk_init_finish_result", BaseEvent.VALUE_SUCCESS);
            vEKeyValue.add("iesve_vesdk_init_finish_reason", "null");
            MonitorUtils.monitorStatistics("iesve_vesdk_init_finish", 1, vEKeyValue);
        }
    }

    public boolean isGLES3Enabled() {
        return this.mEnableGLES3;
    }

    public boolean isUseAssetManager() {
        return this.mUseAssetManager;
    }

    public boolean isUseCloudConfig() {
        return this.mUseCloudConfig;
    }

    public int needUpdateEffectModelFiles() {
        VEEnv vEEnv = this.mEnv;
        if (vEEnv == null || TextUtils.isEmpty(vEEnv.getWorkspace())) {
            return VEResult.TER_INVALID_ENV;
        }
        boolean z = this.mUseResourceFinder;
        String str = TAG;
        if (z) {
            VELogUtil.w(str, "Use resource finder. Do not need update effect model files!");
            return -1;
        } else if (this.mUseAssetManager) {
            VELogUtil.w(str, "Enable assetManager. Do not need update effect model files!");
            return -1;
        } else if (EffectSDKUtils.needUpdate(this.mContext, this.mEnv.getEffectModelResourceDirPath())) {
            return 0;
        } else {
            VEEffectConfig.configEffect(this.mEnv.getEffectModelResourceDirPath(), "nexus");
            return -1;
        }
    }

    public void registerMonitor(VEMonitorListener vEMonitorListener) {
        this.mVEMonitorListener = new WeakReference(vEMonitorListener);
        TEMonitor.register(this.mMoniter);
    }

    public void setAB(VEAB veab) {
        this.mAB = veab;
    }

    public boolean setAssetManagerEnable(boolean z) {
        this.mUseAssetManager = z;
        VideoSdkCore.setEnableAssetManager(z);
        if (this.mUseAssetManager) {
            Context context = this.mContext;
            if (context != null) {
                VEEffectConfig.enableAssetManager(context.getAssets());
                VEEffectConfig.configEffect(null, "nexus");
            } else {
                VELogUtil.d(TAG, "mContext is null!!! need init");
                return false;
            }
        }
        return true;
    }

    public void setCloudConfigEnable(boolean z) {
        this.mUseCloudConfig = z;
    }

    public boolean setEffectAmazingShareDir(String str) {
        VideoSdkCore.setAmazingShareDir(str);
        VEEffectConfig.setShareDir(str);
        return true;
    }

    public boolean setEffectCallback(TEEffectCallback tEEffectCallback) {
        VEEffectConfig.setEffectCallback(tEEffectCallback);
        return true;
    }

    public int setEffectModelsPath(@NonNull String str) {
        VEEnv vEEnv = this.mEnv;
        if (vEEnv == null) {
            return VEResult.TER_INVALID_ENV;
        }
        vEEnv.setDetectModelsDir(str);
        VEEffectConfig.configEffect(str, "nexus");
        return 0;
    }

    public boolean setEffectResourceFinder(@NonNull ResourceFinder resourceFinder) {
        VideoSdkCore.setResourceFinder(resourceFinder);
        VEEffectConfig.setResourceFinder(resourceFinder);
        this.mUseAssetManager = false;
        this.mUseResourceFinder = true;
        VEEffectConfig.configEffect("", "nexus");
        return true;
    }

    public void setEnv(@NonNull VEEnv vEEnv) {
        this.mEnv = vEEnv;
    }

    public void setExternalMonitorListener(VEExternalMonitorListener vEExternalMonitorListener) {
        this.mExternalMonitorListenerRef = new WeakReference(vEExternalMonitorListener);
    }

    public int updateEffectModelFiles() {
        VEEnv vEEnv = this.mEnv;
        if (vEEnv == null || TextUtils.isEmpty(vEEnv.getWorkspace())) {
            return VEResult.TER_INVALID_ENV;
        }
        File file = new File(this.mEnv.getWorkspace(), "models");
        if (!file.exists()) {
            file.mkdirs();
        }
        int i = 0;
        try {
            String absolutePath = file.getAbsolutePath();
            EffectSDKUtils.flushAlgorithmModelFiles(this.mContext, absolutePath);
            this.mEnv.setDetectModelsDir(absolutePath);
            VEEffectConfig.configEffect(absolutePath, "nexus");
        } catch (Throwable unused) {
            i = -1;
        }
        return i;
    }

    public void updateVideoEncodeSettings(VEVideoEncodeSettings vEVideoEncodeSettings) {
        nativeSetVideoEncodeSetting(vEVideoEncodeSettings);
    }
}
