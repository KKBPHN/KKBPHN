package com.iqiyi.android.qigsaw.core.splitload;

import android.content.Context;
import androidx.annotation.Nullable;
import java.io.File;
import java.util.List;

final class SplitLoaderImpl extends SplitLoader {
    SplitLoaderImpl(Context context) {
        super(context);
    }

    /* access modifiers changed from: 0000 */
    public SplitDexClassLoader loadCode(String str, @Nullable List list, File file, @Nullable File file2, @Nullable List list2) {
        try {
            return SplitDexClassLoader.create(str, list, file, file2, list2);
        } catch (Throwable th) {
            throw new SplitLoadException(-27, th);
        }
    }
}
