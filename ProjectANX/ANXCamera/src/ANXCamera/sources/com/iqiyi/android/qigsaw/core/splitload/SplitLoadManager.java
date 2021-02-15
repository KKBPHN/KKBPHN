package com.iqiyi.android.qigsaw.core.splitload;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.iqiyi.android.qigsaw.core.splitload.listener.OnSplitLoadListener;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestrictTo({Scope.LIBRARY_GROUP})
public abstract class SplitLoadManager {
    protected static final String TAG = "SplitLoadManager";
    private final Context context;
    final String currentProcessName;
    private final Set loadedSplitApkPaths = new HashSet(0);
    private final Set loadedSplitNames = new HashSet(0);
    private final Set loadedSplits = new HashSet(0);
    final int splitLoadMode;

    SplitLoadManager(Context context2, String str, int i) {
        this.context = context2;
        this.currentProcessName = str;
        this.splitLoadMode = i;
    }

    public abstract Runnable createSplitLoadTask(List list, @Nullable OnSplitLoadListener onSplitLoadListener);

    /* access modifiers changed from: 0000 */
    public Context getContext() {
        return this.context;
    }

    /* access modifiers changed from: 0000 */
    public Set getLoadedSplitApkPaths() {
        HashSet hashSet;
        synchronized (this) {
            hashSet = new HashSet(this.loadedSplitApkPaths.size());
            for (String str : this.loadedSplitApkPaths) {
                File file = new File(str);
                if (!file.exists() || !file.isFile()) {
                    SplitLog.w(TAG, "Split has been loaded, but its file %s is not exist!", str);
                } else {
                    hashSet.add(str);
                }
            }
        }
        return hashSet;
    }

    public Set getLoadedSplitNames() {
        Set set;
        synchronized (this) {
            set = this.loadedSplitNames;
        }
        return set;
    }

    /* access modifiers changed from: 0000 */
    public final Set getLoadedSplits() {
        Set set;
        synchronized (this) {
            set = this.loadedSplits;
        }
        return set;
    }

    public abstract void getResources(Resources resources);

    public abstract void injectPathClassloader();

    public abstract void loadInstalledSplits();

    public abstract void loadInstalledSplitsWhenAppLaunches();

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<com.iqiyi.android.qigsaw.core.splitload.Split>, for r4v0, types: [java.util.Collection<com.iqiyi.android.qigsaw.core.splitload.Split>, java.util.Collection] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void putSplits(Collection<Split> collection) {
        synchronized (this) {
            this.loadedSplits.addAll(collection);
            for (Split split : collection) {
                this.loadedSplitNames.add(split.splitName);
                this.loadedSplitApkPaths.add(split.splitApkPath);
            }
        }
    }

    public int splitLoadMode() {
        return this.splitLoadMode;
    }
}
