package com.android.camera.upgrade;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import com.android.camera.CameraSettings;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.mimoji.MimojiHelper;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.FileUtils;
import com.xiaomi.camera.rx.CameraSchedulers;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class UpgradeManager {
    private static final String TAG = "UpgradeManager";

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

    static /* synthetic */ void O00000Oo(long j) {
        migrateMimojiCustom();
        onUpgradeFinish(j);
    }

    public static long currentVersionCode(Context context) {
        try {
            return CompatibilityUtils.extractVersionCode(context.getPackageManager().getPackageInfo(context.getPackageName(), 0));
        } catch (NameNotFoundException unused) {
            return 0;
        }
    }

    public static boolean mIsNeedUpgrade(long j) {
        long j2 = DataRepository.dataItemGlobal().getLong(CameraSettings.KEY_MIUICAMERA_VERSION_CODE, 0);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("migrateForUpgrade previous -> ");
        sb.append(j2);
        sb.append(", currentVersion -> ");
        sb.append(j);
        Log.d(str, sb.toString());
        return j > j2 && j2 < 5;
    }

    public static void migrateForUpgrade(Context context) {
        long currentVersionCode = currentVersionCode(context);
        if (mIsNeedUpgrade(currentVersionCode)) {
            onUpgrade(currentVersionCode);
        }
    }

    private static void migrateMimojiCustom() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getPath());
        sb.append("/MIUI/Camera/");
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append("custom");
        File file = new File(sb3.toString());
        if (file.exists() && file.isDirectory()) {
            long currentTimeMillis = System.currentTimeMillis();
            FileUtils.makeNoMediaDir(MimojiHelper.CUSTOM_DIR);
            moveDirectory(file, new File(MimojiHelper.CUSTOM_DIR));
            long currentTimeMillis2 = System.currentTimeMillis();
            String str = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("migrateMimojiCustom move takes ");
            sb4.append(currentTimeMillis2 - currentTimeMillis);
            Log.d(str, sb4.toString());
            FileUtils.delDir(file.getPath());
            String str2 = TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("migrateMimojiCustom delete old custom takes ");
            sb5.append(System.currentTimeMillis() - currentTimeMillis2);
            Log.d(str2, sb5.toString());
        }
    }

    private static void moveDirectory(File file, File file2) {
        if (file != null && file2 != null) {
            File[] listFiles = file.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file3 : listFiles) {
                    if (file3.isDirectory()) {
                        File file4 = new File(file2, file3.getName());
                        if (!file4.exists()) {
                            file4.mkdirs();
                        }
                        moveDirectory(file3, file4);
                    } else {
                        moveFile(file3, file2);
                    }
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x006f, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        $closeResource(r0, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0073, code lost:
        throw r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0076, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
        $closeResource(r3, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x007a, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void moveFile(File file, File file2) {
        if (!(file == null || file2 == null)) {
            File file3 = new File(file2, file.getName());
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("moveFile ");
            sb.append(file);
            sb.append(" -> ");
            sb.append(file3);
            Log.d(str, sb.toString());
            if (file3.exists()) {
                Log.d(TAG, "moveFile target exists do nothing.");
                return;
            }
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file3));
                byte[] bArr = new byte[8912];
                while (true) {
                    int read = bufferedInputStream.read(bArr);
                    if (read <= 0) {
                        break;
                    }
                    bufferedOutputStream.write(bArr, 0, read);
                }
                Log.d(TAG, "moveFile file moved!");
                $closeResource(null, bufferedOutputStream);
                $closeResource(null, bufferedInputStream);
            } catch (IOException e) {
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("moveFile failed: ");
                sb2.append(e.getMessage());
                Log.d(str2, sb2.toString());
            }
        }
    }

    private static void onUpgrade(long j) {
        new Thread(new O00000Oo(j)).start();
    }

    private static void onUpgradeFinish(long j) {
        CameraSchedulers.sCameraSetupScheduler.scheduleDirect(new O000000o(j));
    }
}
