package com.android.camera.module.impl.component;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.widget.Toast;
import androidx.annotation.MainThread;
import com.android.camera.ActivityBase;
import com.android.camera.R;
import com.android.camera.RotateDialogController;
import com.android.camera.data.DataRepository;
import com.android.camera.data.observeable.VMFeature;
import com.android.camera.fragment.CtaNoticeFragment;
import com.android.camera.fragment.CtaNoticeFragment.OnCtaNoticeClickListener;
import com.android.camera.log.Log;
import com.android.camera.multi.SampleDownloader;
import com.android.camera.multi.reporter.SampleLogger;
import com.android.camera.multi.reporter.SampleSplitInstallReporter;
import com.android.camera.multi.reporter.SampleSplitLoadReporter;
import com.android.camera.multi.reporter.SampleSplitUninstallReporter;
import com.android.camera.multi.reporter.SampleSplitUpdateReporter;
import com.android.camera.network.NetworkDependencies;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.MultiFeatureManager;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.FeatureAttr;
import com.google.android.play.core.splitinstall.SplitInstallHelper;
import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;
import com.google.android.play.core.splitinstall.SplitInstallRequest.Builder;
import com.google.android.play.core.splitinstall.SplitInstallSessionState;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;
import com.google.android.play.core.tasks.OnFailureListener;
import com.iqiyi.android.qigsaw.core.Qigsaw;
import com.iqiyi.android.qigsaw.core.SplitConfiguration;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfoManagerService;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class MultiFeatureManagerImpl implements MultiFeatureManager, OnCtaNoticeClickListener {
    private static final int SESSION_ID_PENDING_CANCEL = -1;
    private static final String TAG = "FML";
    private Activity mActivity;
    private Context mContext;
    /* access modifiers changed from: private */
    public SplitInstallStateUpdatedListener mExternalListener;
    private HashMap mFeatureSize;
    private HashMap mFeatureVersion;
    private SplitInstallManager mInstallManager;
    /* access modifiers changed from: private */
    public HashMap mInstallSession;
    private List mInstalledFeature;
    private List mLoadedLibrary;
    /* access modifiers changed from: private */
    public VMFeature mVMFeature;
    private SplitInstallStateUpdatedListener myListener = new SplitInstallStateUpdatedListener() {
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x007b, code lost:
            r6 = com.android.camera.module.impl.component.MultiFeatureManagerImpl.access$200(r6.this$0);
            r7 = java.lang.Integer.valueOf(18);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x013d, code lost:
            com.android.camera.log.Log.d(r3, r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0152, code lost:
            r6.updateState(r0, r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0155, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onStateUpdate(SplitInstallSessionState splitInstallSessionState) {
            Integer num;
            VMFeature vMFeature;
            String str;
            if (MultiFeatureManagerImpl.this.mExternalListener != null) {
                MultiFeatureManagerImpl.this.mExternalListener.onStateUpdate(splitInstallSessionState);
            }
            String str2 = (String) splitInstallSessionState.moduleNames().get(0);
            Integer num2 = (Integer) MultiFeatureManagerImpl.this.mInstallSession.get(str2);
            if (num2 == null || num2.intValue() != -1) {
                MultiFeatureManagerImpl.this.mInstallSession.put(str2, Integer.valueOf(splitInstallSessionState.sessionId()));
                int status = splitInstallSessionState.status();
                String str3 = MultiFeatureManagerImpl.TAG;
                switch (status) {
                    case 1:
                        Log.d(str3, "PENDING");
                        vMFeature = MultiFeatureManagerImpl.this.mVMFeature;
                        num = Integer.valueOf(17);
                        break;
                    case 2:
                        int round = Math.round((((float) splitInstallSessionState.bytesDownloaded()) * 100.0f) / ((float) MultiFeatureManagerImpl.this.getFeatureSize(str2)));
                        MultiFeatureManagerImpl.this.mVMFeature.updateState(str2, Integer.valueOf(VMFeature.wrapDownloadingProgress(round)));
                        StringBuilder sb = new StringBuilder();
                        sb.append("DOWNLOADING: ");
                        sb.append(round);
                        str = sb.toString();
                        break;
                    case 3:
                        MultiFeatureManagerImpl.this.mVMFeature.updateState(str2, Integer.valueOf(19));
                        str = "DOWNLOADED";
                        break;
                    case 4:
                        str = "INSTALLING";
                        break;
                    case 5:
                        CameraStatUtils.trackFeatureInstallResult(str2, MultiFeatureManagerImpl.this.getFeatureVersion(str2), 5);
                        MultiFeatureManagerImpl.this.onInstalled(str2);
                        MultiFeatureManagerImpl.this.mVMFeature.updateState(str2, Integer.valueOf(21));
                        MultiFeatureManagerImpl.this.mVMFeature.removeFeature(str2);
                        break;
                    case 6:
                        int errorCode = splitInstallSessionState.errorCode();
                        CameraStatUtils.trackFeatureInstallResult(str2, MultiFeatureManagerImpl.this.getFeatureVersion(str2), errorCode);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("FAILED :");
                        sb2.append(errorCode);
                        Log.e(str3, sb2.toString());
                        int i = 257;
                        if (!(errorCode == -10 || errorCode == -6)) {
                            if (errorCode != 257 && errorCode != 258) {
                                switch (errorCode) {
                                    case -100:
                                    case SplitInstallInternalErrorCode.BUILTIN_SPLIT_APK_COPIED_FAILED /*-99*/:
                                    case SplitInstallInternalErrorCode.UNINSTALLATION_UNSUPPORTED /*-98*/:
                                        i = 258;
                                        break;
                                }
                            } else {
                                i = errorCode;
                            }
                        }
                        vMFeature = MultiFeatureManagerImpl.this.mVMFeature;
                        num = Integer.valueOf(i);
                        break;
                    case 7:
                        Log.d(str3, "CANCELED");
                        CameraStatUtils.trackFeatureInstallResult(str2, MultiFeatureManagerImpl.this.getFeatureVersion(str2), 7);
                        break;
                    case 9:
                        Log.d(str3, "CANCELING");
                        break;
                }
            } else {
                MultiFeatureManagerImpl.this.mInstallSession.put(str2, Integer.valueOf(splitInstallSessionState.sessionId()));
                MultiFeatureManagerImpl.this.cancelInstallFeature(str2);
            }
        }
    };

    public MultiFeatureManagerImpl(ActivityBase activityBase) {
        this.mActivity = activityBase;
        this.mContext = activityBase.getApplicationContext();
    }

    public static MultiFeatureManagerImpl create(ActivityBase activityBase) {
        return new MultiFeatureManagerImpl(activityBase);
    }

    /* JADX WARNING: type inference failed for: r2v1, types: [android.content.Context] */
    /* JADX WARNING: type inference failed for: r2v5 */
    /* JADX WARNING: type inference failed for: r2v6 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static AssetManager getAssetManager(Context context) {
        try {
            r2 = context;
            r2 = context.createPackageContext(context.getPackageName(), 0).getAssets();
            r2 = r2;
            return r2;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return r2.getResources().getAssets();
        }
    }

    /* access modifiers changed from: private */
    public String getFeatureVersion(String str) {
        return (String) this.mFeatureVersion.get(str);
    }

    private void onFailed(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("onFailed: ");
        sb.append(str);
        Log.d(TAG, sb.toString());
        this.mInstallSession.remove(str);
    }

    /* access modifiers changed from: private */
    public void onInstalled(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("onInstalled: ");
        sb.append(str);
        Log.d(TAG, sb.toString());
        this.mInstallSession.remove(str);
        this.mInstalledFeature.add(str);
        if (!this.mInstallManager.getInstalledModules().contains(str)) {
            this.mInstallManager.startInstall(SplitInstallRequest.newBuilder().addModule(str).build()).addOnFailureListener(new OnFailureListener() {
                public void onFailure(Exception exc) {
                    Log.d("onInstallFailed:", exc.getMessage());
                }
            });
        }
    }

    private void test() {
    }

    public void cancelInstallFeature(String str) {
        Integer num = (Integer) this.mInstallSession.get(str);
        if (num == null) {
            this.mInstallSession.put(str, Integer.valueOf(-1));
            return;
        }
        this.mInstallManager.cancelInstall(num.intValue());
        onFailed(str);
    }

    public AlertDialog checkAndShowDownloadConfirmDialog(final String str, Context context, boolean z, Runnable runnable) {
        String str2 = str;
        Context context2 = context;
        StringBuilder sb = new StringBuilder();
        sb.append("check :");
        sb.append(str);
        String sb2 = sb.toString();
        String str3 = TAG;
        Log.d(str3, sb2);
        if (!CtaNoticeFragment.checkCta(this.mActivity.getFragmentManager(), this, 5)) {
            Log.d(str3, "check cta");
            return null;
        } else if (!NetworkDependencies.isConnected(context)) {
            Log.d(str3, "check networkError");
            Toast.makeText(context, R.string.live_music_network_exception, 0).show();
            return null;
        } else {
            boolean isConnectedWIFI = NetworkDependencies.isConnectedWIFI(context);
            long featureSize = getFeatureSize(str);
            CameraStatUtils.trackFeatureInstallStartClick(str, getFeatureVersion(str), z ^ true ? 1 : 0, isConnectedWIFI);
            if (isConnectedWIFI) {
                Log.d(str3, "check connected");
                runnable.run();
                startInstallFeature(str, null);
                return null;
            }
            String format = new DecimalFormat("###.00").format(((double) featureSize) / 1048576.0d);
            final Runnable runnable2 = runnable;
            AlertDialog showSystemAlertDialog = RotateDialogController.showSystemAlertDialog(context, context.getResources().getString(R.string.download_title), context.getResources().getString(R.string.download_hint, new Object[]{format}), context.getString(R.string.download_confirm), new Runnable() {
                public void run() {
                    CameraStatUtils.trackFeatureInstallOperation(FeatureAttr.KEY_FEATURE_INSTALL_CELLULAR_CONFIRM);
                    StringBuilder sb = new StringBuilder();
                    sb.append("check confirm:");
                    sb.append(str);
                    sb.append("_");
                    sb.append(MultiFeatureManagerImpl.this.getFeatureVersion(str));
                    Log.d(MultiFeatureManagerImpl.TAG, sb.toString());
                    runnable2.run();
                    MultiFeatureManagerImpl.this.startInstallFeature(str, null);
                }
            }, null, null, context.getString(R.string.snap_cancel), null);
            CameraStatUtils.trackFeatureInstallOperation(FeatureAttr.KEY_FEATURE_INSTALL_CELLULAR_SHOW);
            return showSystemAlertDialog;
        }
    }

    public long getFeatureSize(String str) {
        init();
        if (this.mFeatureSize == null) {
            this.mFeatureSize = new HashMap();
        }
        if (this.mFeatureVersion == null) {
            this.mFeatureVersion = new HashMap();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("getFeatureSize featureName :");
        sb.append(str);
        Log.d(TAG, sb.toString());
        if (this.mFeatureSize.get(str) == null) {
            SplitInfo splitInfo = SplitInfoManagerService.getInstance().getSplitInfo(this.mContext, str);
            this.mFeatureSize.put(str, Long.valueOf(splitInfo.getSize()));
            this.mFeatureVersion.put(str, splitInfo.getSplitVersion());
        }
        return ((Long) this.mFeatureSize.get(str)).longValue();
    }

    public List getInstalledFeatures() {
        return this.mInstalledFeature;
    }

    @MainThread
    public boolean hasFeatureInstalled(String str) {
        init();
        return this.mInstallManager.getInstalledModules().contains(str);
    }

    @MainThread
    public void init() {
        if (this.mInstallManager == null) {
            long currentTimeMillis = System.currentTimeMillis();
            Qigsaw.install(this.mContext, new SampleDownloader(), SplitConfiguration.newBuilder().splitLoadMode(2).logger(new SampleLogger()).verifySignature(false).loadReporter(new SampleSplitLoadReporter(this.mContext)).installReporter(new SampleSplitInstallReporter(this.mContext)).uninstallReporter(new SampleSplitUninstallReporter(this.mContext)).updateReporter(new SampleSplitUpdateReporter(this.mContext)).build());
            StringBuilder sb = new StringBuilder();
            sb.append(System.currentTimeMillis() - currentTimeMillis);
            sb.append("");
            Log.d("split attach cost ", sb.toString());
            Qigsaw.onApplicationCreated();
            this.mInstalledFeature = new ArrayList();
            this.mLoadedLibrary = new ArrayList();
            this.mInstallManager = SplitInstallManagerFactory.create(this.mContext);
            this.mInstallManager.registerListener(this.myListener);
            Set<String> installedModules = this.mInstallManager.getInstalledModules();
            this.mVMFeature = (VMFeature) DataRepository.dataItemObservable().get(VMFeature.class);
            for (String add : installedModules) {
                this.mInstalledFeature.add(add);
            }
        }
    }

    public void loadLibrary(Context context, String str) {
        if (!this.mLoadedLibrary.contains(str)) {
            SplitInstallHelper.loadLibrary(this.mContext, str);
            this.mLoadedLibrary.add(str);
        }
    }

    public void onNegativeClick(DialogInterface dialogInterface, int i) {
    }

    public void onPositiveClick(DialogInterface dialogInterface, int i) {
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(929, this);
    }

    public AlertDialog showDownloadCancelDialog(final String str, Context context, boolean z, final Runnable runnable) {
        StringBuilder sb = new StringBuilder();
        sb.append("cancel :");
        sb.append(str);
        Log.d(TAG, sb.toString());
        String geItemStringName = DataRepository.dataItemGlobal().getComponentModuleList().geItemStringName(VMFeature.getLocalModeByFeatureName(str), z);
        Context context2 = context;
        AlertDialog showSystemAlertDialog = RotateDialogController.showSystemAlertDialog(context2, null, context.getResources().getString(R.string.download_cancel_hint, new Object[]{geItemStringName}), context.getString(R.string.mimoji_confirm), new Runnable() {
            public void run() {
                CameraStatUtils.trackFeatureInstallOperation(FeatureAttr.KEY_FEATURE_INSTALL_CANCEL_CONFIRM);
                StringBuilder sb = new StringBuilder();
                sb.append("cancel confirm:");
                sb.append(str);
                Log.d(MultiFeatureManagerImpl.TAG, sb.toString());
                runnable.run();
                MultiFeatureManagerImpl.this.cancelInstallFeature(str);
            }
        }, null, null, context.getString(R.string.snap_cancel), null);
        CameraStatUtils.trackFeatureInstallOperation(FeatureAttr.KEY_FEATURE_INSTALL_CANCEL_SHOW);
        return showSystemAlertDialog;
    }

    @MainThread
    public void startInstallFeature(String str, SplitInstallStateUpdatedListener splitInstallStateUpdatedListener) {
        if (!hasFeatureInstalled(str)) {
            if (this.mInstallSession == null) {
                this.mInstallSession = new HashMap();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("startInstall :");
            sb.append(str);
            Log.d(TAG, sb.toString());
            init();
            this.mExternalListener = splitInstallStateUpdatedListener;
            Builder newBuilder = SplitInstallRequest.newBuilder();
            newBuilder.addModule(str);
            this.mInstallManager.startInstall(newBuilder.build());
        }
    }

    public void unRegisterProtocol() {
        this.mContext = null;
        this.mActivity = null;
        if (this.mInstallManager != null) {
            HashMap hashMap = this.mInstallSession;
            if (hashMap != null) {
                for (Entry key : hashMap.entrySet()) {
                    cancelInstallFeature((String) key.getKey());
                }
            }
            this.mInstalledFeature.clear();
            this.mLoadedLibrary.clear();
            this.mInstallManager.unregisterListener(this.myListener);
        }
        ModeCoordinatorImpl.getInstance().detachProtocol(929, this);
    }
}
