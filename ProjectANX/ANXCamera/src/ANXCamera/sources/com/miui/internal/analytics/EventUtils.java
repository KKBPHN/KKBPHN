package com.miui.internal.analytics;

import android.content.Context;
import com.miui.internal.util.DeviceHelper;
import miui.provider.ExtraSettings.Secure;

public class EventUtils {
    public static final String COLUMN_ID = "event_id";
    public static final String COLUMN_PACKAGE_NAME = "package";
    public static final String COLUMN_PARAM = "param";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_VALUE = "value";
    public static final int DEFAULT_MAXSAVE = 7;
    public static final int TYPE_EVENT = 2;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_TRACKPAGEVIEW = 3;

    private EventUtils() {
    }

    public static boolean enableWrite(Context context) {
        return Secure.getInt(context.getContentResolver(), "upload_log_pref", DeviceHelper.IS_DEVELOPMENT_VERSION ? 1 : 0) != 0;
    }

    public static int getDay(long j) {
        return (int) (j / 86400000);
    }
}
