package com.iqiyi.android.qigsaw.core.splitload;

import android.content.Context;
import androidx.annotation.Nullable;
import java.io.File;
import java.util.List;

abstract class SplitLoader {
    final Context context;

    SplitLoader(Context context2) {
        this.context = context2;
    }

    /* access modifiers changed from: 0000 */
    public SplitDexClassLoader loadCode(String str, @Nullable List list, File file, @Nullable File file2, @Nullable List list2) {
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void loadCode2(@Nullable List list, File file, @Nullable File file2) {
    }

    /* access modifiers changed from: 0000 */
    public final void loadResources(String str) {
        try {
            SplitCompatResourcesLoader.loadResources(this.context, this.context.getResources(), str);
        } catch (Throwable th) {
            throw new SplitLoadException(-21, th);
        }
    }
}
