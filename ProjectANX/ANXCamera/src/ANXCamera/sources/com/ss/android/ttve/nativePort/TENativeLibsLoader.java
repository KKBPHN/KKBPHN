package com.ss.android.ttve.nativePort;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.ss.android.vesdk.runtime.VESafelyLibsLoader;
import java.util.ArrayList;
import java.util.List;

public class TENativeLibsLoader {
    /* access modifiers changed from: private */
    public static final String TAG = "TENativeLibsLoader";
    private static boolean isLibraryLoadedOpt = false;
    private static ILibraryLoader mLibraryLoader;
    /* access modifiers changed from: private */
    public static Context sContext;
    private static ILibraryLoader sDefaultLibraryLoader = new DefaultLibraryLoader();
    private static boolean sLibraryLoaded;
    private static boolean sLoadOptLibrary = false;

    public class DefaultLibraryLoader implements ILibraryLoader {
        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<java.lang.String>, for r4v0, types: [java.util.List, java.util.List<java.lang.String>] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onLoadNativeLibs(List<String> list) {
            for (String str : list) {
                if (!VESafelyLibsLoader.loadLibrary(str, TENativeLibsLoader.sContext)) {
                    String access$100 = TENativeLibsLoader.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("loadLibrary ");
                    sb.append(str);
                    sb.append(" failed");
                    Log.e(access$100, sb.toString());
                }
            }
        }
    }

    public interface ILibraryLoader {
        void onLoadNativeLibs(List list);
    }

    public static void enableLoadOptLibrary(boolean z) {
        sLoadOptLibrary = z;
    }

    public static int getLibraryLoadedVersion() {
        if (!sLibraryLoaded) {
            return -1;
        }
        return isLibraryLoadedOpt ? 1 : 0;
    }

    public static synchronized void loadLibrary() {
        synchronized (TENativeLibsLoader.class) {
            if (!sLibraryLoaded) {
                ArrayList arrayList = new ArrayList();
                arrayList.add("ttffmpeg");
                arrayList.add("ttyuv");
                arrayList.add("tt_effect");
                arrayList.add("ttvebase");
                arrayList.add("ttvideorecorder");
                if (sLoadOptLibrary) {
                    arrayList.add("ttvideoeditor-import");
                    isLibraryLoadedOpt = true;
                } else {
                    arrayList.add("ttvideoeditor");
                    isLibraryLoadedOpt = false;
                }
                if (mLibraryLoader != null) {
                    mLibraryLoader.onLoadNativeLibs(arrayList);
                    sLibraryLoaded = true;
                    return;
                }
                sDefaultLibraryLoader.onLoadNativeLibs(arrayList);
                sLibraryLoaded = true;
            }
        }
    }

    public static synchronized void setContext(@NonNull Context context) {
        synchronized (TENativeLibsLoader.class) {
            sContext = context;
        }
    }

    public static void setLibraryLoad(ILibraryLoader iLibraryLoader) {
        mLibraryLoader = iLibraryLoader;
    }
}
