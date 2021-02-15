package com.iqiyi.android.qigsaw.core.splitload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.iqiyi.android.qigsaw.core.splitload.listener.OnSplitLoadListener;
import java.io.File;
import java.util.List;

final class SplitLoadTaskImpl extends SplitLoadTask {
    SplitLoadTaskImpl(@NonNull SplitLoadManager splitLoadManager, @NonNull List list, @Nullable OnSplitLoadListener onSplitLoadListener) {
        super(splitLoadManager, list, onSplitLoadListener);
    }

    /* access modifiers changed from: 0000 */
    public SplitLoader createSplitLoader() {
        return new SplitLoaderImpl(this.appContext);
    }

    /* access modifiers changed from: 0000 */
    public ClassLoader loadCode(SplitLoader splitLoader, String str, List list, File file, File file2, List list2) {
        SplitDexClassLoader classLoader = SplitApplicationLoaders.getInstance().getClassLoader(str);
        if (classLoader != null) {
            return classLoader;
        }
        SplitDexClassLoader loadCode = splitLoader.loadCode(str, list, file, file2, list2);
        SplitApplicationLoaders.getInstance().addClassLoader(loadCode);
        return loadCode;
    }

    /* access modifiers changed from: 0000 */
    public void onSplitActivateFailed(ClassLoader classLoader) {
        if (classLoader instanceof SplitDexClassLoader) {
            SplitApplicationLoaders.getInstance().removeClassLoader((SplitDexClassLoader) classLoader);
        }
    }
}
