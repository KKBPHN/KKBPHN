package com.android.camera.external;

import android.content.Context;
import android.content.Intent;
import com.android.camera.log.Log;

public class SendBroadcastNotifyExternal implements INotifyExternal {
    private static final String CAMERA_STATUS_ACTION = "com.android.camera.action.camera_status";
    private static final String CAMERA_STATUS_RECEIVER_PERMISSION = "com.android.camera.permission.CAMERA_STATUS";
    private static final String EXTRA_CAMERA_LENS = "lens";
    private static final String EXTRA_CAMERA_MODULE = "module";
    private static final String EXTRA_CAMERA_STATUS = "status";
    private static final String TAG = "SendBroadcastNotifyExternal";
    private static final String VALUE_CAMERA_STATUS_START_FOREGROUND = "start_foreground";
    private Context mContext;

    public SendBroadcastNotifyExternal(Context context) {
        this.mContext = context;
    }

    public void notifyCameraResume() {
        if (this.mContext != null) {
            Log.d(TAG, "notify external(status: start_foreground)");
            Intent intent = new Intent(CAMERA_STATUS_ACTION);
            intent.putExtra("status", VALUE_CAMERA_STATUS_START_FOREGROUND);
            this.mContext.sendBroadcast(intent, "com.android.camera.permission.CAMERA_STATUS");
        }
    }

    public void notifyModeAndFacing(int i, int i2) {
        if (this.mContext != null) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("notify external(mode:");
            sb.append(i);
            sb.append(",facing:");
            sb.append(i2);
            sb.append(")");
            Log.d(str, sb.toString());
            Intent intent = new Intent(CAMERA_STATUS_ACTION);
            intent.putExtra(EXTRA_CAMERA_MODULE, String.valueOf(i));
            intent.putExtra("lens", String.valueOf(i2));
            this.mContext.sendBroadcast(intent, "com.android.camera.permission.CAMERA_STATUS");
        }
    }
}
