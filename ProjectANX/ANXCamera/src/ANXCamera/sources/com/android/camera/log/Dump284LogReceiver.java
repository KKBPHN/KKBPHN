package com.android.camera.log;

import android.content.Context;
import android.content.Intent;
import com.miui.internal.log.receiver.DumpReceiver;
import com.miui.internal.log.util.Config;

public class Dump284LogReceiver extends DumpReceiver {
    private static final String TAG = "Dump284LogReceiver";

    public Dump284LogReceiver() {
        super(Config.getDefaultCacheLogDir(), FileLogger.MIUI_284_LOG_DIR_PATH);
    }

    public void onReceive(Context context, Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("onReceive: ");
        sb.append(intent.getAction());
        Log.d(TAG, sb.toString());
        if ("android.provider.Telephony.SECRET_CODE".equals(intent.getAction())) {
            if ("284".equals(intent.getData().getHost())) {
                super.onReceive(context, intent);
            }
        }
    }
}
