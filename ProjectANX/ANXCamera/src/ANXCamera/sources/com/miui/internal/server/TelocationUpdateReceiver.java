package com.miui.internal.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import miui.telephony.phonenumber.ChineseTelocationConverter;

public class TelocationUpdateReceiver extends BroadcastReceiver {
    private static final String SERVICE_NAME = "com.miui.telocation";
    private static final String TAG = "TelocationUpdateReceiver";

    public static void onReceiveIntent(Context context, Intent intent) {
        Intent intent2;
        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        StringBuilder sb = new StringBuilder();
        sb.append("onReceiveIntent: action=");
        sb.append(action);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        if (DataUpdateManager.DATA_UPDATE_RECEIVE.equals(action)) {
            long longExtra = intent.getLongExtra(DataUpdateManager.EXTRA_WATER_MARK, 0);
            String string = extras != null ? extras.getString(DataUpdateManager.EXTRA_SERVICE_NAME) : null;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("current water mark is ");
            sb3.append(longExtra);
            sb3.append(", service: ");
            sb3.append(string);
            Log.d(str, sb3.toString());
            if (TextUtils.equals(SERVICE_NAME, string) && longExtra != ((long) ChineseTelocationConverter.getInstance().getVersion())) {
                intent2 = new Intent(TelocationUpdateService.ACTION_UPDATE_TELOCATION);
            } else {
                return;
            }
        } else if ("android.intent.action.DOWNLOAD_COMPLETE".equals(action)) {
            Log.d(str, "telocation download completed.");
            intent2 = new Intent(TelocationUpdateService.ACTION_TELOCATION_DOWNLOAD_COMPLETED);
            if (extras != null) {
                intent2.putExtras(extras);
            }
        } else {
            return;
        }
        intent2.setClass(context, TelocationUpdateService.class);
        context.startService(intent2);
    }

    public void onReceive(Context context, Intent intent) {
        onReceiveIntent(context, intent);
    }
}
