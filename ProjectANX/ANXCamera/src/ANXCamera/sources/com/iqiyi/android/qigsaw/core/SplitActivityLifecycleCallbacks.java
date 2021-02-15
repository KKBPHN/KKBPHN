package com.iqiyi.android.qigsaw.core;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.util.LruCache;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.iqiyi.android.qigsaw.core.extension.AABExtension;
import com.iqiyi.android.qigsaw.core.splitreport.SplitBriefInfo;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfoManager;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfoManagerService;

public abstract class SplitActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {
    private static final String SPLIT_NAME_BASE = "base";
    private static final String TAG = "SplitActivityLifecycleCallbacks";
    private final LruCache splitActivityNameCache = new LruCache(20);
    private final LruCache splitBriefInfoCache = new LruCache(10);

    @Nullable
    private SplitBriefInfo getSplitBriefInfoForActivity(Activity activity) {
        String splitNameForActivityName = getSplitNameForActivityName(activity);
        if (SPLIT_NAME_BASE.equals(splitNameForActivityName)) {
            return null;
        }
        SplitBriefInfo splitBriefInfo = (SplitBriefInfo) this.splitBriefInfoCache.get(splitNameForActivityName);
        if (splitBriefInfo == null) {
            SplitInfoManager instance = SplitInfoManagerService.getInstance();
            if (instance != null) {
                SplitInfo splitInfo = instance.getSplitInfo(activity, splitNameForActivityName);
                if (splitInfo != null) {
                    splitBriefInfo = new SplitBriefInfo(splitInfo.getSplitName(), splitInfo.getSplitVersion(), splitInfo.isBuiltIn());
                    this.splitBriefInfoCache.put(splitNameForActivityName, splitBriefInfo);
                }
            }
        }
        return splitBriefInfo;
    }

    private String getSplitNameForActivityName(Activity activity) {
        String name = activity.getClass().getName();
        String str = (String) this.splitActivityNameCache.get(name);
        if (str == null) {
            str = AABExtension.getInstance().getSplitNameForActivityName(name);
            if (str == null) {
                str = SPLIT_NAME_BASE;
            }
            this.splitActivityNameCache.put(name, str);
        }
        return str;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public final void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        SplitBriefInfo splitBriefInfoForActivity = getSplitBriefInfoForActivity(activity);
        if (splitBriefInfoForActivity != null) {
            onSplitActivityCreated(splitBriefInfoForActivity, activity, bundle);
            SplitLog.i(TAG, "Activity %s of split %s is created.", activity.getClass().getName(), splitBriefInfoForActivity.toString());
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void onActivityDestroyed(@NonNull Activity activity) {
        SplitBriefInfo splitBriefInfoForActivity = getSplitBriefInfoForActivity(activity);
        if (splitBriefInfoForActivity != null) {
            onSplitActivityDestroyed(splitBriefInfoForActivity, activity);
            SplitLog.i(TAG, "Activity %s of split %s is destroyed.", activity.getClass().getName(), splitBriefInfoForActivity.toString());
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void onActivityPaused(@NonNull Activity activity) {
        SplitBriefInfo splitBriefInfoForActivity = getSplitBriefInfoForActivity(activity);
        if (splitBriefInfoForActivity != null) {
            onSplitActivityPaused(splitBriefInfoForActivity, activity);
            SplitLog.i(TAG, "Activity %s of split %s is paused.", activity.getClass().getName(), splitBriefInfoForActivity.toString());
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void onActivityResumed(@NonNull Activity activity) {
        SplitBriefInfo splitBriefInfoForActivity = getSplitBriefInfoForActivity(activity);
        if (splitBriefInfoForActivity != null) {
            onSplitActivityResumed(splitBriefInfoForActivity, activity);
            SplitLog.i(TAG, "Activity %s of split %s is resumed.", activity.getClass().getName(), splitBriefInfoForActivity.toString());
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
        SplitBriefInfo splitBriefInfoForActivity = getSplitBriefInfoForActivity(activity);
        if (splitBriefInfoForActivity != null) {
            onSplitActivitySaveInstanceState(splitBriefInfoForActivity, activity, bundle);
            SplitLog.i(TAG, "Activity %s of split %s is saving state.", activity.getClass().getName(), splitBriefInfoForActivity.toString());
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void onActivityStarted(@NonNull Activity activity) {
        SplitBriefInfo splitBriefInfoForActivity = getSplitBriefInfoForActivity(activity);
        if (splitBriefInfoForActivity != null) {
            onSplitActivityStarted(splitBriefInfoForActivity, activity);
            SplitLog.i(TAG, "Activity %s of split %s is started.", activity.getClass().getName(), splitBriefInfoForActivity.toString());
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void onActivityStopped(@NonNull Activity activity) {
        SplitBriefInfo splitBriefInfoForActivity = getSplitBriefInfoForActivity(activity);
        if (splitBriefInfoForActivity != null) {
            onSplitActivityStopped(splitBriefInfoForActivity, activity);
            SplitLog.i(TAG, "Activity %s of split %s is stopped.", activity.getClass().getName(), splitBriefInfoForActivity.toString());
        }
    }

    public abstract void onSplitActivityCreated(@NonNull SplitBriefInfo splitBriefInfo, @NonNull Activity activity, @Nullable Bundle bundle);

    public abstract void onSplitActivityDestroyed(@NonNull SplitBriefInfo splitBriefInfo, @NonNull Activity activity);

    public abstract void onSplitActivityPaused(@NonNull SplitBriefInfo splitBriefInfo, @NonNull Activity activity);

    public abstract void onSplitActivityResumed(@NonNull SplitBriefInfo splitBriefInfo, @NonNull Activity activity);

    public abstract void onSplitActivitySaveInstanceState(@NonNull SplitBriefInfo splitBriefInfo, @NonNull Activity activity, @NonNull Bundle bundle);

    public abstract void onSplitActivityStarted(@NonNull SplitBriefInfo splitBriefInfo, @NonNull Activity activity);

    public abstract void onSplitActivityStopped(@NonNull SplitBriefInfo splitBriefInfo, @NonNull Activity activity);
}
