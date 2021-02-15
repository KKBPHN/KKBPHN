package com.iqiyi.android.qigsaw.core.splitload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.iqiyi.android.qigsaw.core.splitload.listener.OnSplitLoadListener;
import java.io.File;
import java.util.List;

final class SplitLoadTaskImpl2 extends SplitLoadTask {
    SplitLoadTaskImpl2(@NonNull SplitLoadManager splitLoadManager, @NonNull List list, @Nullable OnSplitLoadListener onSplitLoadListener) {
        super(splitLoadManager, list, onSplitLoadListener);
    }

    /* access modifiers changed from: 0000 */
    public SplitLoader createSplitLoader() {
        return new SplitLoaderImpl2(this.appContext);
    }

    /* access modifiers changed from: 0000 */
    public ClassLoader loadCode(SplitLoader splitLoader, String str, List list, File file, File file2, List list2) {
        splitLoader.loadCode2(list, file, file2);
        return SplitLoadTask.class.getClassLoader();
    }

    /* access modifiers changed from: 0000 */
    public void onSplitActivateFailed(ClassLoader classLoader) {
        try {
            SplitCompatDexLoader.unLoad(classLoader);
        } catch (Throwable unused) {
        }
    }
}
