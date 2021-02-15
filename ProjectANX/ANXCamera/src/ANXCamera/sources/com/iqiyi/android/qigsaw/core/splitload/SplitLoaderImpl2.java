package com.iqiyi.android.qigsaw.core.splitload;

import android.content.Context;
import androidx.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

final class SplitLoaderImpl2 extends SplitLoader {
    SplitLoaderImpl2(Context context) {
        super(context);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<java.lang.String>, for r5v0, types: [java.util.List, java.util.List<java.lang.String>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void loadDex(ClassLoader classLoader, List<String> list, File file) {
        if (list != null) {
            ArrayList arrayList = new ArrayList(list.size());
            for (String file2 : list) {
                arrayList.add(new File(file2));
            }
            try {
                SplitCompatDexLoader.load(classLoader, file, arrayList);
                SplitUnKnownFileTypeDexLoader.loadDex(classLoader, list, file);
            } catch (Throwable th) {
                throw new SplitLoadException(-23, th);
            }
        }
    }

    private void loadLibrary(ClassLoader classLoader, File file) {
        if (file != null) {
            try {
                SplitCompatLibraryLoader.load(classLoader, file);
            } catch (Throwable th) {
                throw new SplitLoadException(-22, th);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void loadCode2(@Nullable List list, File file, @Nullable File file2) {
        ClassLoader classLoader = SplitLoader.class.getClassLoader();
        loadLibrary(classLoader, file2);
        loadDex(classLoader, list, file);
    }
}
