package com.iqiyi.android.qigsaw.core.splitrequest.splitinfo;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.io.File;
import java.util.Collection;
import java.util.List;

@RestrictTo({Scope.LIBRARY_GROUP})
public interface SplitInfoManager {
    SplitDetails createSplitDetailsForJsonFile(@NonNull String str);

    Collection getAllSplitInfo(Context context);

    String getBaseAppVersionName(Context context);

    String getCurrentSplitInfoVersion();

    String getQigsawId(Context context);

    List getSplitEntryFragments(Context context);

    SplitInfo getSplitInfo(Context context, String str);

    List getSplitInfos(Context context, Collection collection);

    List getUpdateSplits(Context context);

    boolean updateSplitInfoVersion(Context context, String str, File file);
}
