package com.iqiyi.android.qigsaw.core.splitrequest.splitinfo;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.common.SplitBaseInfoProvider;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.iqiyi.android.qigsaw.core.splitreport.SplitUpdateReporter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestrictTo({Scope.LIBRARY_GROUP})
public class SplitUpdateService extends IntentService {
    private static final String TAG = "SplitUpdateService";

    public SplitUpdateService() {
        super("qigsaw_split_update");
    }

    private void onUpdateError(String str, String str2, int i) {
        SplitUpdateReporter updateReporter = SplitUpdateReporterManager.getUpdateReporter();
        if (updateReporter != null) {
            updateReporter.onUpdateFailed(str, str2, i);
        }
    }

    private void onUpdateOK(String str, String str2, List list) {
        SplitUpdateReporter updateReporter = SplitUpdateReporterManager.getUpdateReporter();
        if (updateReporter != null) {
            updateReporter.onUpdateOK(str, str2, list);
        }
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(@Nullable Intent intent) {
        String str = TAG;
        if (intent == null) {
            SplitLog.w(str, "SplitUpdateService receiver null intent!", new Object[0]);
            return;
        }
        SplitInfoManager instance = SplitInfoManagerService.getInstance();
        if (instance == null) {
            SplitLog.w(str, "SplitInfoManager has not been created!", new Object[0]);
        } else if (instance.getAllSplitInfo(this) == null) {
            SplitLog.w(str, "Failed to get splits info of current split-info version!", new Object[0]);
        } else {
            String stringExtra = intent.getStringExtra(SplitConstants.NEW_SPLIT_INFO_VERSION);
            String stringExtra2 = intent.getStringExtra(SplitConstants.NEW_SPLIT_INFO_PATH);
            String currentSplitInfoVersion = instance.getCurrentSplitInfoVersion();
            if (TextUtils.isEmpty(stringExtra)) {
                SplitLog.w(str, "New split-info version null", new Object[0]);
                onUpdateError(currentSplitInfoVersion, stringExtra, -31);
            } else if (TextUtils.isEmpty(stringExtra2)) {
                SplitLog.w(str, "New split-info path null", new Object[0]);
                onUpdateError(currentSplitInfoVersion, stringExtra, -32);
            } else {
                File file = new File(stringExtra2);
                if (!file.exists() || !file.canWrite()) {
                    SplitLog.w(str, "New split-info file %s is invalid", stringExtra2);
                    onUpdateError(currentSplitInfoVersion, stringExtra, -33);
                } else if (stringExtra.equals(instance.getCurrentSplitInfoVersion())) {
                    SplitLog.w(str, "New split-info version %s is equals to current version!", stringExtra);
                    onUpdateError(currentSplitInfoVersion, stringExtra, -34);
                } else {
                    SplitDetails createSplitDetailsForJsonFile = instance.createSplitDetailsForJsonFile(stringExtra2);
                    if (createSplitDetailsForJsonFile == null || !createSplitDetailsForJsonFile.verifySplitInfoListing()) {
                        SplitLog.w(str, "Failed to parse SplitDetails for new split info file!", new Object[0]);
                        onUpdateError(currentSplitInfoVersion, stringExtra, -35);
                        return;
                    }
                    String qigsawId = createSplitDetailsForJsonFile.getQigsawId();
                    if (TextUtils.isEmpty(qigsawId) || !qigsawId.equals(SplitBaseInfoProvider.getQigsawId())) {
                        SplitLog.w(str, "New qigsaw-id is not equal to current app, so we could't update splits!", new Object[0]);
                        onUpdateError(currentSplitInfoVersion, stringExtra, -37);
                        return;
                    }
                    ArrayList arrayList = (ArrayList) createSplitDetailsForJsonFile.getUpdateSplits();
                    if (arrayList == null || arrayList.isEmpty()) {
                        SplitLog.w(str, "There are no splits need to be updated!", new Object[0]);
                        onUpdateError(currentSplitInfoVersion, stringExtra, -36);
                        return;
                    }
                    SplitLog.w(str, "Success to check update request, updatedSplitInfoPath: %s, updatedSplitInfoVersion: %s", stringExtra2, stringExtra);
                    if (instance.updateSplitInfoVersion(getApplicationContext(), stringExtra, file)) {
                        onUpdateOK(currentSplitInfoVersion, stringExtra, arrayList);
                    } else {
                        onUpdateError(currentSplitInfoVersion, stringExtra, -38);
                    }
                }
            }
        }
    }
}
