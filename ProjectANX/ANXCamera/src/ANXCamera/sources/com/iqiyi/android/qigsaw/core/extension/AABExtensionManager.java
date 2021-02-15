package com.iqiyi.android.qigsaw.core.extension;

import android.app.Application;
import android.content.Context;
import java.util.Map;

interface AABExtensionManager {
    void activeApplication(Application application, Context context);

    Application createApplication(ClassLoader classLoader, String str);

    Map getSplitActivitiesMap();

    boolean isSplitActivity(String str);

    boolean isSplitReceiver(String str);

    boolean isSplitService(String str);
}
