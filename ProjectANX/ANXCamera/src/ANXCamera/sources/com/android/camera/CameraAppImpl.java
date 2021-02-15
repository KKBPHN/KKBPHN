package com.android.camera;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.app.Activity;
import android.content.Context;
import android.os.Process;
import com.android.camera.aftersales.AftersalesManager;
import com.android.camera.external.mivi.MIVIHelper;
import com.android.camera.log.FileLogger;
import com.android.camera.log.FileLogger.Config;
import com.android.camera.log.Log;
import com.android.camera.network.util.NetworkUtils;
import com.android.camera.parallel.AlgoConnector;
import com.android.camera.upgrade.UpgradeManager;
import com.android.camera2.vendortag.CameraCharacteristicsVendorTags;
import com.android.camera2.vendortag.CaptureRequestVendorTags;
import com.android.camera2.vendortag.CaptureResultVendorTags;
import com.android.camera2.vendortag.struct.MarshalQueryableRegister;
import com.miui.filtersdk.BeautificationSDK;
import com.xiaomi.camera.imagecodec.ImagePool;
import java.io.File;
import miui.external.Application;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

public class CameraAppImpl extends Application {
    private static CameraApplicationDelegate sApplicationDelegate;
    private final String TAG = CameraAppImpl.class.getSimpleName();
    private boolean isMimojiNeedUpdate = true;
    private boolean sLaunched = true;

    public static Context getAndroidContext() {
        return CameraApplicationDelegate.getAndroidContext();
    }

    public void addActivity(Activity activity) {
        sApplicationDelegate.addActivity(activity);
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    public void closeAllActivitiesBut(Activity activity) {
        sApplicationDelegate.closeAllActivitiesBut(activity);
    }

    public boolean containsResumedCameraInStack() {
        return sApplicationDelegate.containsResumedCameraInStack();
    }

    public Activity getActivity(int i) {
        return sApplicationDelegate.getActivity(i);
    }

    public int getActivityCount() {
        return sApplicationDelegate.getActivityCount();
    }

    public boolean isApplicationFirstLaunched() {
        boolean z = this.sLaunched;
        if (!z) {
            return z;
        }
        this.sLaunched = !z;
        return !this.sLaunched;
    }

    public boolean isMainIntentActivityLaunched() {
        return sApplicationDelegate.isMainIntentActivityLaunched();
    }

    public boolean isMimojiNeedUpdate() {
        if (!this.isMimojiNeedUpdate) {
            return false;
        }
        this.isMimojiNeedUpdate = false;
        return true;
    }

    public boolean isNeedRestore() {
        return sApplicationDelegate.getSettingsFlag();
    }

    public CameraApplicationDelegate onCreateApplicationDelegate() {
        if (sApplicationDelegate == null) {
            sApplicationDelegate = new CameraApplicationDelegate(this);
        }
        String str = "camera.db";
        File databasePath = getDatabasePath(str);
        if (databasePath != null && databasePath.exists()) {
            deleteDatabase(str);
        }
        System.setProperty("rx2.purge-period-seconds", "3600");
        AftersalesManager.checkSelf(this);
        if ("avenger".equals(C0124O00000oO.O0Ooo0o)) {
            getPackageManager().setApplicationEnabledSetting(CameraIntentManager.CALLER_MIUI_CAMERA, 2, 1);
        }
        MarshalQueryableRegister.preload();
        CameraCharacteristicsVendorTags.preload();
        CaptureRequestVendorTags.preload();
        CaptureResultVendorTags.preload();
        if (C0122O00000o.instance().OOo00O()) {
            int ooO00O0 = C0122O00000o.instance().ooO00O0();
            int OOOoo0 = C0122O00000o.instance().OOOoo0();
            long totalMemory = Process.getTotalMemory() / IjkMediaMeta.AV_CH_STEREO_RIGHT;
            if (totalMemory > 6) {
                ooO00O0 = C0122O00000o.instance().OOOoo00();
                OOOoo0 = C0122O00000o.instance().o00O0oO0();
            }
            String str2 = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("totalMemory:");
            sb.append(totalMemory);
            sb.append("G, maxAcquireCount = ");
            sb.append(ooO00O0);
            sb.append(", maxDequeueCount:");
            sb.append(OOOoo0);
            Log.d(str2, sb.toString());
            ImagePool.init(ooO00O0, OOOoo0);
            AlgoConnector.getInstance().startService(this);
        }
        CrashHandler.getInstance().init(this);
        NetworkUtils.bind(this);
        BeautificationSDK.init(this);
        UpgradeManager.migrateForUpgrade(this);
        MIVIHelper.requestCloudDataAsync();
        FileLogger.init(this, Config.newBuild().setMaxBackUpCount(4).setMaxFileSize(3145728).build());
        return sApplicationDelegate;
    }

    public void removeActivity(Activity activity) {
        sApplicationDelegate.removeActivity(activity);
    }

    public void resetRestoreFlag() {
        sApplicationDelegate.resetRestoreFlag();
    }
}
