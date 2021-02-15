package miui.autoinstall.config.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.miui.internal.R;
import java.io.File;
import java.lang.ref.WeakReference;
import java.miui.autoinstall.config.pm.O00000Oo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import miui.autoinstall.config.activity.PromptRestoreDialogActivity;
import miui.autoinstall.config.activity.RestoreFailedDialogActivity;
import miui.autoinstall.config.download.AutoInstallDownLoader;
import miui.autoinstall.config.download.AutoInstallNotification;
import miui.autoinstall.config.entity.LocalAppInfo;
import miui.autoinstall.config.entity.RequestAppInfo;
import miui.autoinstall.config.entity.RequestEntity;
import miui.autoinstall.config.entity.ResponseAppInfo;
import miui.autoinstall.config.entity.ResponseAppInfo.ResponseAppInfoItem;
import miui.autoinstall.config.entity.RestoreRecord;
import miui.autoinstall.config.pm.AutoInstallFileProvider;
import miui.autoinstall.config.pm.PackageManagerCompat;
import miui.autoinstall.config.utils.AutoInstallRequestUtil;
import miui.autoinstall.config.utils.SignUtil;
import org.json.JSONException;

public class AutoInstallService extends Service implements O00000Oo {
    private static final String ACTION_AUTO_INSTALL = "miui.autoinstall.config.action.AUTOINSTALL";
    private static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    public static final String ACTION_DOWNLOAD_BY_PASS = "miui.autoinstall.config.action.DOWNLOADBYPASS";
    private static final String ACTION_PROVISION_COMPLETED = "android.provision.action.PROVISION_COMPLETE";
    private static final String ACTION_START_INSTALL = "miui.autoinstall.config.ACTION_INSTALL";
    public static final String ACTION_START_PROCEDURE = "miui.autoinstall.config.ACTION_START";
    private static final String EXTRA_APK_PATH = "extra_apk_path";
    public static final String EXTRA_IS_MOBILE_DATA_REMIND = "extra_is_mobile_data_remind";
    public static final String EXTRA_MOBILE_DATA_CONSUME = "extra_mobile_data_consume";
    private static final String KEY_APP_INFO = "appInfo";
    private static final String TAG = "AutoInstallService";
    private int mCurrentRestoring = 1;
    private DownloadCompleteReceiver mDownloadCompleteReceiver;
    /* access modifiers changed from: private */
    public Map mDownloadIds = new HashMap();
    /* access modifiers changed from: private */
    public AutoInstallDownLoader mDownloader;
    private boolean mHasError;
    private Queue mInstallQueue = new LinkedList();
    private boolean mIsProvision;
    private boolean mIsRestoring;
    /* access modifiers changed from: private */
    public List mLocalAppInfos;
    private AutoInstallNotification mNotification;
    private PackageManagerCompat mPackageManagerCompat;
    private Map mPackagePath = new HashMap();
    private List mPackageRestoring = new ArrayList();

    final class DownloadCompleteReceiver extends BroadcastReceiver {
        private WeakReference mOuter;

        DownloadCompleteReceiver(AutoInstallService autoInstallService) {
            this.mOuter = new WeakReference(autoInstallService);
        }

        public void onReceive(Context context, Intent intent) {
            AutoInstallService autoInstallService = (AutoInstallService) this.mOuter.get();
            if (autoInstallService != null) {
                long longExtra = intent.getLongExtra("extra_download_id", -1);
                ResponseAppInfoItem responseAppInfoItem = (ResponseAppInfoItem) ((AutoInstallService) this.mOuter.get()).mDownloadIds.get(Long.valueOf(longExtra));
                if (responseAppInfoItem != null) {
                    if (autoInstallService.mDownloader.queryStatus(longExtra) == 8) {
                        String queryFileDir = autoInstallService.mDownloader.queryFileDir(longExtra);
                        StringBuilder sb = new StringBuilder();
                        sb.append("onReceive: ");
                        sb.append(queryFileDir);
                        Log.d(AutoInstallService.TAG, sb.toString());
                        autoInstallService.submitInstall(queryFileDir, responseAppInfoItem.packageName);
                        return;
                    }
                    autoInstallService.showRestoreError();
                }
            }
        }
    }

    private long calculateMobileData(String str, String str2, String str3) {
        if (ensureFileNeedDownload(str, str3) == null) {
            return AutoInstallRequestUtil.calculateFileSize(str2);
        }
        return 0;
    }

    private void deleteInstalledApk(String str) {
        if (!TextUtils.isEmpty(str)) {
            File file = new File(str);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    private void doDownload(ResponseAppInfoItem responseAppInfoItem) {
        File ensureFileNeedDownload = ensureFileNeedDownload(responseAppInfoItem.apkName, responseAppInfoItem.apkMd5);
        if (ensureFileNeedDownload != null) {
            submitInstall(ensureFileNeedDownload.getPath(), responseAppInfoItem.apkName);
            return;
        }
        long enqueue = this.mDownloader.enqueue(this, responseAppInfoItem.cdnPath, responseAppInfoItem.apkName);
        StringBuilder sb = new StringBuilder();
        sb.append("doDownload: enqueue：");
        sb.append(responseAppInfoItem.apkName);
        Log.d(TAG, sb.toString());
        if (enqueue != -1) {
            this.mDownloadIds.put(Long.valueOf(enqueue), responseAppInfoItem);
        }
    }

    /* access modifiers changed from: private */
    public void downloadApp(ResponseAppInfo responseAppInfo) {
        if (responseAppInfo != null) {
            this.mInstallQueue.clear();
            this.mPackageRestoring.clear();
            List<ResponseAppInfoItem> list = responseAppInfo.data;
            registerDownloadCompleteReceiver();
            boolean isMobileData = isMobileData();
            long j = 0;
            for (ResponseAppInfoItem responseAppInfoItem : list) {
                if (responseAppInfoItem != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("downloadApp: enqueue:");
                    sb.append(responseAppInfoItem.apkName);
                    Log.d(TAG, sb.toString());
                    if (!isPackageInstalled(responseAppInfoItem.packageName)) {
                        if (isMobileData) {
                            j += calculateMobileData(responseAppInfoItem.apkName, responseAppInfoItem.cdnPath, responseAppInfoItem.apkMd5);
                        }
                        this.mPackageRestoring.add(responseAppInfoItem.packageName);
                        this.mInstallQueue.add(responseAppInfoItem);
                    } else {
                        this.mPackageManagerCompat.updateInstallRecord(this, responseAppInfoItem.packageName, true);
                    }
                }
            }
            downloadIfRemind(j);
        }
    }

    private void downloadByPass() {
        if (!this.mInstallQueue.isEmpty()) {
            showRestoring(this.mPackageRestoring.size());
            ResponseAppInfoItem responseAppInfoItem = (ResponseAppInfoItem) this.mInstallQueue.remove();
            if (responseAppInfoItem != null) {
                doDownload(responseAppInfoItem);
                return;
            }
            return;
        }
        showRestoreError();
    }

    private void downloadIfRemind(long j) {
        if (j > 0) {
            showMobileDataRemind(j);
        } else {
            downloadByPass();
        }
    }

    private File ensureFileNeedDownload(String str, String str2) {
        File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (externalFilesDir == null || !externalFilesDir.exists()) {
            return null;
        }
        File file = new File(externalFilesDir.getPath(), str);
        if (!file.exists()) {
            return null;
        }
        if (TextUtils.equals(SignUtil.getFileMD5(file), str2)) {
            return file;
        }
        file.delete();
        return null;
    }

    private void getNext() {
        if (!this.mInstallQueue.isEmpty()) {
            this.mCurrentRestoring++;
            this.mNotification.updateProgress(this.mCurrentRestoring, this.mPackageRestoring.size());
            ResponseAppInfoItem responseAppInfoItem = (ResponseAppInfoItem) this.mInstallQueue.remove();
            if (responseAppInfoItem != null) {
                doDownload(responseAppInfoItem);
            }
        } else if (hasError()) {
            showRestoreError();
        } else {
            this.mNotification.showSuccessNotification();
            this.mCurrentRestoring = 1;
            stopSelf();
            setRestoring(false);
        }
    }

    private TreeMap getParamsMap(String str) {
        TreeMap treeMap = new TreeMap();
        treeMap.put(KEY_APP_INFO, str);
        return treeMap;
    }

    private boolean hasError() {
        return this.mHasError;
    }

    private void install(Uri uri, String str) {
        this.mPackageManagerCompat.installPackage(uri, str);
    }

    private boolean isMobileData() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 0;
    }

    private boolean isOverProvision() {
        return Secure.getInt(getContentResolver(), "device_provisioned", 0) != 0;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<miui.autoinstall.config.entity.LocalAppInfo>, for r5v0, types: [java.util.List, java.util.List<miui.autoinstall.config.entity.LocalAppInfo>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isPackageAllExisted(List<LocalAppInfo> list) {
        for (LocalAppInfo localAppInfo : list) {
            if (localAppInfo != null && !this.mPackageManagerCompat.packageExists(getPackageManager(), localAppInfo.packageName)) {
                StringBuilder sb = new StringBuilder();
                sb.append("isPackageAllExisted: package:");
                sb.append(localAppInfo.packageName);
                sb.append(" not installed");
                Log.d(TAG, sb.toString());
                return false;
            }
        }
        return true;
    }

    private boolean isPackageAllInstalled(List list) {
        boolean isProvisionAction = isProvisionAction();
        String str = TAG;
        if (isProvisionAction) {
            Log.d(str, "isPackageAllInstalled: isProvision");
            return isPackageAllExisted(list);
        }
        Log.d(str, "isPackageAllInstalled: reboot");
        boolean isRestoreNotNow = this.mPackageManagerCompat.isRestoreNotNow();
        boolean isPackageAllExisted = isPackageAllExisted(list);
        StringBuilder sb = new StringBuilder();
        sb.append("isPackageAllInstalled: isRestoreNotNow:");
        sb.append(isRestoreNotNow);
        sb.append(",allInstalled:");
        sb.append(isPackageAllExisted);
        Log.d(str, sb.toString());
        if (isRestoreNotNow && !isPackageAllExisted) {
            return false;
        }
        boolean hasFailedRecord = this.mPackageManagerCompat.hasFailedRecord(this, list);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("isPackageAllInstalled: hasFailedRecord:");
        sb2.append(hasFailedRecord);
        Log.d(str, sb2.toString());
        return !hasFailedRecord;
    }

    private boolean isPackageAllRecorded(List list) {
        try {
            return this.mPackageManagerCompat.isRecordAllInstalled(this, list);
        } catch (JSONException e) {
            Log.e(TAG, "isPackageAllExistedInRecord: ", e);
            return false;
        }
    }

    private boolean isPackageInstalled(String str) {
        String str2 = TAG;
        if (isProvisionAction()) {
            return this.mPackageManagerCompat.packageExists(getPackageManager(), str);
        }
        boolean z = false;
        try {
            boolean isPackageInstalledRecord = this.mPackageManagerCompat.isPackageInstalledRecord(this, str, null);
            boolean packageExists = this.mPackageManagerCompat.packageExists(getPackageManager(), str);
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(":isPackageRecord:");
            sb.append(isPackageInstalledRecord);
            sb.append(",isPackageInstalled:");
            sb.append(packageExists);
            Log.d(str2, sb.toString());
            if (isPackageInstalledRecord || packageExists) {
                z = true;
            }
            return z;
        } catch (JSONException e) {
            Log.e(str2, "isPackageInstalled: ", e);
            return false;
        }
    }

    private boolean isRestoring() {
        return this.mIsRestoring;
    }

    private void onMiuiHome() {
        if (isRestoring()) {
            showRestoringToast();
        } else {
            onSystemReboot();
        }
    }

    private void onSystemReboot() {
        if (isOverProvision()) {
            start();
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<miui.autoinstall.config.entity.RestoreRecord>, for r1v0, types: [java.util.List<miui.autoinstall.config.entity.RestoreRecord>, java.util.List] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean packageCompleted(List<RestoreRecord> list, String str) {
        for (RestoreRecord restoreRecord : list) {
            if (TextUtils.equals(restoreRecord.packageName, str)) {
                return true;
            }
        }
        return false;
    }

    private void recordNotInstalledPkg() {
        if (this.mLocalAppInfos != null) {
            PackageManager packageManager = getPackageManager();
            for (LocalAppInfo localAppInfo : this.mLocalAppInfos) {
                if (localAppInfo != null) {
                    PackageManagerCompat packageManagerCompat = this.mPackageManagerCompat;
                    String str = localAppInfo.packageName;
                    packageManagerCompat.updateInstallRecord(this, str, packageManagerCompat.packageExists(packageManager, str));
                }
            }
        }
    }

    private void registerDownloadCompleteReceiver() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE");
        this.mDownloadCompleteReceiver = new DownloadCompleteReceiver(this);
        registerReceiver(this.mDownloadCompleteReceiver, intentFilter);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<miui.autoinstall.config.entity.LocalAppInfo>, for r7v0, types: [java.util.List, java.util.List<miui.autoinstall.config.entity.LocalAppInfo>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ResponseAppInfo requestAppInfo(List<LocalAppInfo> list) {
        ArrayList arrayList = new ArrayList();
        List completedRestore = this.mPackageManagerCompat.getCompletedRestore(this);
        boolean z = completedRestore != null && !completedRestore.isEmpty();
        for (LocalAppInfo localAppInfo : list) {
            if (localAppInfo != null) {
                if (!z || !packageCompleted(completedRestore, localAppInfo.packageName)) {
                    RequestAppInfo requestAppInfo = new RequestAppInfo();
                    requestAppInfo.pn = localAppInfo.packageName;
                    requestAppInfo.pvc = String.valueOf(localAppInfo.versionCode);
                    arrayList.add(requestAppInfo);
                }
            }
        }
        RequestEntity requestEntity = new RequestEntity();
        requestEntity.nonceStr = SignUtil.getNonceStr();
        requestEntity.appInfo = arrayList;
        requestEntity.sign = SignUtil.getSign(getParamsMap(RequestEntity.list2Json(arrayList)), requestEntity.nonceStr);
        return AutoInstallRequestUtil.requestAppInfo(requestEntity);
    }

    private void requestDownload() {
        setRestoring(true);
        setHasError(false);
        showRestoringToast();
        recordNotInstalledPkg();
        this.mPackageManagerCompat.bindInstallerServices(this);
        if (this.mDownloader == null) {
            this.mDownloader = new AutoInstallDownLoader(getApplicationContext());
        }
        new Thread(new Runnable() {
            public void run() {
                AutoInstallService autoInstallService = AutoInstallService.this;
                ResponseAppInfo access$100 = autoInstallService.requestAppInfo(autoInstallService.mLocalAppInfos);
                AutoInstallService autoInstallService2 = AutoInstallService.this;
                if (access$100 != null) {
                    autoInstallService2.downloadApp(access$100);
                } else {
                    autoInstallService2.showRestoreError();
                }
            }
        }).start();
    }

    private void setHasError(boolean z) {
        this.mHasError = z;
    }

    private void setRestoring(boolean z) {
        this.mIsRestoring = z;
    }

    private void showMobileDataRemind(long j) {
        Intent intent = new Intent(this, PromptRestoreDialogActivity.class);
        intent.putExtra(EXTRA_MOBILE_DATA_CONSUME, j);
        intent.putExtra(EXTRA_IS_MOBILE_DATA_REMIND, true);
        intent.addFlags(268435456);
        startActivity(intent);
    }

    private void showPromptRestore() {
        Intent intent = new Intent(this, PromptRestoreDialogActivity.class);
        intent.addFlags(268435456);
        startActivity(intent);
    }

    /* access modifiers changed from: private */
    public void showRestoreError() {
        Intent intent = new Intent(this, RestoreFailedDialogActivity.class);
        intent.setFlags(268435456);
        startActivity(intent);
        stopSelf();
        setRestoring(false);
        setHasError(false);
        this.mNotification.clearAll();
    }

    private void showRestoring(int i) {
        this.mNotification.showStartNotification(i);
    }

    private void showRestoringToast() {
        Toast.makeText(this, getResources().getString(R.string.system_app_is_restore), 0).show();
    }

    private void start() {
        List list = this.mLocalAppInfos;
        if (list != null && !list.isEmpty()) {
            if (isPackageAllInstalled(this.mLocalAppInfos)) {
                stopSelf();
                return;
            }
            showPromptRestore();
        }
    }

    /* access modifiers changed from: private */
    public void submitInstall(String str, String str2) {
        if (this.mPackageManagerCompat.packageExists(getPackageManager(), str2)) {
            this.mPackageManagerCompat.updateInstallRecord(this, str2, true);
            deleteInstalledApk(str);
            getNext();
            return;
        }
        this.mPackagePath.put(str2, str);
        Intent intent = new Intent(this, AutoInstallService.class);
        intent.putExtra(EXTRA_APK_PATH, str);
        intent.setAction(ACTION_START_INSTALL);
        File file = new File(str);
        if (!file.exists()) {
            StringBuilder sb = new StringBuilder();
            sb.append("submitInstall: file not exist:");
            sb.append(file.getPath());
            Log.d(TAG, sb.toString());
            setHasError(true);
            getNext();
            return;
        }
        Uri uriForFile = AutoInstallFileProvider.getUriForFile(this, "miui.autoinstall.config.fileprovider", file);
        grantUriPermission("com.xiaomi.market", uriForFile, 3);
        intent.setData(uriForFile);
        startService(intent);
    }

    public boolean isProvisionAction() {
        return this.mIsProvision;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.mPackageManagerCompat = new PackageManagerCompat(this);
        this.mPackageManagerCompat.registerInstallListener(this);
        this.mLocalAppInfos = this.mPackageManagerCompat.loadSystemAppInfoLocal();
        this.mNotification = new AutoInstallNotification(this);
    }

    public void onDestroy() {
        DownloadCompleteReceiver downloadCompleteReceiver = this.mDownloadCompleteReceiver;
        if (downloadCompleteReceiver != null) {
            unregisterReceiver(downloadCompleteReceiver);
        }
        this.mPackageManagerCompat.unbindInstallerService(this);
        super.onDestroy();
    }

    public void onRefuseInstall(String str, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("packageInstallFailed: ");
        sb.append(str);
        Log.d(TAG, sb.toString());
        if (!TextUtils.isEmpty(str)) {
            this.mPackageManagerCompat.updateInstallRecord(this, str, false);
        }
        setHasError(true);
        getNext();
    }

    public void onServiceDead() {
        Log.d(TAG, "onServiceDead");
        this.mCurrentRestoring = 1;
        showRestoreError();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent != null) {
            String action = intent.getAction() != null ? intent.getAction() : "";
            char c = 65535;
            switch (action.hashCode()) {
                case -1589339876:
                    if (action.equals(ACTION_DOWNLOAD_BY_PASS)) {
                        c = 5;
                        break;
                    }
                    break;
                case -782377083:
                    if (action.equals(ACTION_START_PROCEDURE)) {
                        c = 0;
                        break;
                    }
                    break;
                case -760896832:
                    if (action.equals(ACTION_AUTO_INSTALL)) {
                        c = 2;
                        break;
                    }
                    break;
                case -685309954:
                    if (action.equals(ACTION_START_INSTALL)) {
                        c = 1;
                        break;
                    }
                    break;
                case -510741405:
                    if (action.equals(ACTION_PROVISION_COMPLETED)) {
                        c = 4;
                        break;
                    }
                    break;
                case 798292259:
                    if (action.equals(ACTION_BOOT_COMPLETED)) {
                        c = 3;
                        break;
                    }
                    break;
            }
            if (c == 0) {
                requestDownload();
            } else if (c != 1) {
                String str = TAG;
                if (c == 2) {
                    Log.d(str, "onStartCommand: auto install from outside");
                    onMiuiHome();
                } else if (c == 3) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("onStartCommand: system boot completed receiver: isProvisionAction:");
                    sb.append(this.mIsProvision);
                    Log.d(str, sb.toString());
                    onSystemReboot();
                } else if (c == 4) {
                    this.mIsProvision = true;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onStartCommand: provisioned completed receiver:isProvisionAction:");
                    sb2.append(this.mIsProvision);
                    Log.d(str, sb2.toString());
                    start();
                } else if (c == 5) {
                    downloadByPass();
                }
            } else {
                install(intent.getData(), intent.getStringExtra(EXTRA_APK_PATH));
            }
        }
        return super.onStartCommand(intent, i, i2);
    }

    public void packageInstalled(String str, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("packageInstalled: ");
        sb.append(str);
        sb.append(",returnCode:");
        sb.append(i);
        Log.d(TAG, sb.toString());
        boolean z = i == 1 || i == -25;
        if (!z) {
            setHasError(true);
        } else {
            deleteInstalledApk((String) this.mPackagePath.remove(str));
        }
        this.mPackageManagerCompat.updateInstallRecord(this, str, z);
        getNext();
    }
}
