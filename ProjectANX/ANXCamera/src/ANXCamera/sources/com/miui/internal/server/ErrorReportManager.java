package com.miui.internal.server;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import com.miui.internal.util.DeviceHelper;
import miui.net.ConnectivityHelper;
import miui.os.DropBoxManager.Entry;
import miui.util.AppConstants;
import miui.util.ErrorReport;
import org.json.JSONException;
import org.json.JSONObject;

public class ErrorReportManager {
    private static final int LOG_MAX_SIZE = 512000;
    private static final String PREF_KEY_LAST_UPLOAD_TIME = "error_report_last_update_time";
    private static final String TAG = "ErrorReportManager";
    private Context mContext;

    class Holder {
        static final ErrorReportManager INSTANCE = new ErrorReportManager(AppConstants.getCurrentApplication());

        private Holder() {
        }
    }

    private ErrorReportManager(Context context) {
        this.mContext = context;
    }

    public static ErrorReportManager getInstance() {
        return Holder.INSTANCE;
    }

    private long getLastUploadTime() {
        long j = PreferenceManager.getDefaultSharedPreferences(this.mContext).getLong(PREF_KEY_LAST_UPLOAD_TIME, 0);
        long currentTimeMillis = System.currentTimeMillis();
        return Math.max(j, currentTimeMillis - (currentTimeMillis % 86400000));
    }

    private void saveLastUploadTime(long j) {
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putLong(PREF_KEY_LAST_UPLOAD_TIME, j).apply();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007d, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x007f, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void upload() {
        if (DeviceHelper.isDeviceProvisioned(this.mContext)) {
            if (ConnectivityHelper.getInstance().isUnmeteredNetworkConnected()) {
                DropBoxManagerService instance = DropBoxManagerService.getInstance();
                Entry nextEntry = instance.getNextEntry(ErrorReport.DROPBOX_TAG, getLastUploadTime());
                while (true) {
                    if (nextEntry == null || !ErrorReport.isUserAllowed(this.mContext)) {
                        break;
                    }
                    String text = nextEntry.getText(LOG_MAX_SIZE);
                    JSONObject jSONObject = null;
                    if (!TextUtils.isEmpty(text)) {
                        try {
                            jSONObject = new JSONObject(text);
                        } catch (JSONException e) {
                            Log.w(TAG, "Fail to parse json", e);
                        }
                    }
                    if (jSONObject != null && !ErrorReport.sendReportRequest(this.mContext, jSONObject, 3) && !ConnectivityHelper.getInstance().isUnmeteredNetworkConnected()) {
                        Log.w(TAG, "Stop uploading error report for free network disconnect");
                        break;
                    }
                    long timeMillis = nextEntry.getTimeMillis();
                    saveLastUploadTime(timeMillis);
                    nextEntry.close();
                    nextEntry = instance.getNextEntry(ErrorReport.DROPBOX_TAG, timeMillis);
                }
                if (nextEntry != null) {
                    nextEntry.close();
                }
            }
        }
    }
}
