package com.iqiyi.android.qigsaw.core.splitinstall.remote;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.common.FileUtil;
import com.iqiyi.android.qigsaw.core.common.ProcessUtil;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.iqiyi.android.qigsaw.core.splitinstall.SplitPendingUninstallManager;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfoManager;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfoManagerService;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitPathManager;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestrictTo({Scope.LIBRARY_GROUP})
public abstract class SplitInstallSupervisor {
    private static final String TAG = "SplitInstallSupervisor";

    public interface Callback {
        void onCancelInstall(int i, Bundle bundle);

        void onDeferredInstall(Bundle bundle);

        void onDeferredUninstall(Bundle bundle);

        void onError(Bundle bundle);

        void onGetSession(int i, Bundle bundle);

        void onGetSessionStates(List list);

        void onStartInstall(int i, Bundle bundle);
    }

    protected static Bundle bundleErrorCode(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("error_code", i);
        return bundle;
    }

    private static int createSessionId(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(sb.toString().getBytes("UTF-8"));
            StringBuilder sb2 = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                byte b2 = b & -1;
                if (b2 < 16) {
                    sb2.append("0");
                }
                sb2.append(Integer.toHexString(b2));
            }
            return sb2.toString().hashCode();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e2) {
            throw new RuntimeException("UnsupportedEncodingException", e2);
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo>, for r5v0, types: [java.util.Collection<com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo>, java.util.Collection] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected static int createSessionId(Collection<SplitInfo> collection) {
        int i = 0;
        for (SplitInfo splitInfo : collection) {
            StringBuilder sb = new StringBuilder();
            sb.append(splitInfo.getSplitName());
            String str = "@";
            sb.append(str);
            sb.append(splitInfo.getAppVersion());
            sb.append(str);
            sb.append(splitInfo.getSplitVersion());
            i += createSessionId(sb.toString());
        }
        return i;
    }

    protected static boolean isMobileAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 0;
    }

    protected static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager != null) {
            NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
            if (allNetworkInfo.length > 0) {
                for (NetworkInfo state : allNetworkInfo) {
                    if (state.getState() == State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<android.os.Bundle>, for r3v0, types: [java.util.Collection, java.util.Collection<android.os.Bundle>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected static List unBundleModuleNames(Collection<Bundle> collection) {
        ArrayList arrayList = new ArrayList(collection.size());
        for (Bundle string : collection) {
            arrayList.add(string.getString("module_name"));
        }
        return arrayList;
    }

    public abstract void cancelInstall(int i, Callback callback);

    public abstract boolean cancelInstallWithoutUserConfirmation(int i);

    public abstract boolean continueInstallWithUserConfirmation(int i);

    public abstract void deferredInstall(List list, Callback callback);

    public abstract void deferredUninstall(List list, Callback callback);

    public abstract void getSessionState(int i, Callback callback);

    public abstract void getSessionStates(Callback callback);

    public abstract void startInstall(List list, Callback callback);

    /* JADX WARNING: Removed duplicated region for block: B:19:0x006f  */
    /* JADX WARNING: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void startUninstall(Context context) {
        ArrayList arrayList;
        SplitInfoManager instance;
        List readPendingUninstallSplits = new SplitPendingUninstallManager().readPendingUninstallSplits();
        SplitInfoManager instance2 = SplitInfoManagerService.getInstance();
        if (!(readPendingUninstallSplits == null || instance2 == null)) {
            List<SplitInfo> splitInfos = instance2.getSplitInfos(context, readPendingUninstallSplits);
            if (splitInfos != null) {
                ProcessUtil.killAllOtherProcess(context);
                arrayList = new ArrayList(splitInfos.size());
                for (SplitInfo splitInfo : splitInfos) {
                    if (FileUtil.deleteFileSafely(SplitPathManager.require().getSplitMarkFile(splitInfo))) {
                        arrayList.add(splitInfo);
                    }
                }
                if (arrayList != null || arrayList.isEmpty()) {
                    SplitLog.d(TAG, "No splits need to uninstall!", new Object[0]);
                } else {
                    SplitInstallService.getHandler(context.getPackageName()).post(new SplitStartUninstallTask(arrayList));
                }
                instance = SplitInfoManagerService.getInstance();
                if (instance == null) {
                    Collection allSplitInfo = instance.getAllSplitInfo(context);
                    if (allSplitInfo != null) {
                        SplitInstallService.getHandler(context.getPackageName()).post(new SplitDeleteRedundantVersionTask(allSplitInfo));
                        return;
                    }
                    return;
                }
                return;
            }
        }
        arrayList = null;
        if (arrayList != null) {
        }
        SplitLog.d(TAG, "No splits need to uninstall!", new Object[0]);
        instance = SplitInfoManagerService.getInstance();
        if (instance == null) {
        }
    }
}
