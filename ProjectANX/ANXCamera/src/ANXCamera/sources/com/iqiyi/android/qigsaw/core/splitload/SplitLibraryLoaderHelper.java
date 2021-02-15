package com.iqiyi.android.qigsaw.core.splitload;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo.LibInfo.Lib;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfoManagerService;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitPathManager;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;

@RestrictTo({Scope.LIBRARY_GROUP})
public class SplitLibraryLoaderHelper {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    @SuppressLint({"UnsafeDynamicallyLoadedCode"})
    public static boolean loadSplitLibrary(Context context, String str) {
        if (!SplitLoadManagerService.hasInstance() || SplitLoadManagerService.getInstance().splitLoadMode() != 1) {
            return false;
        }
        Collection<SplitInfo> allSplitInfo = SplitInfoManagerService.getInstance().getAllSplitInfo(context);
        if (allSplitInfo == null) {
            return false;
        }
        for (SplitInfo splitInfo : allSplitInfo) {
            if (splitInfo.hasLibs()) {
                Iterator it = splitInfo.getLibInfo().getLibs().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        continue;
                        break;
                    }
                    Lib lib = (Lib) it.next();
                    if (lib.getName().equals(System.mapLibraryName(str))) {
                        if (context instanceof Application) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(SplitPathManager.require().getSplitLibDir(splitInfo).getAbsolutePath());
                            sb.append(File.separator);
                            sb.append(lib.getName());
                            try {
                                System.load(sb.toString());
                                return true;
                            } catch (UnsatisfiedLinkError unused) {
                                return false;
                            }
                        } else {
                            SplitDexClassLoader classLoader = SplitApplicationLoaders.getInstance().getClassLoader(splitInfo.getSplitName());
                            if (classLoader != null) {
                                return loadSplitLibrary0(classLoader, splitInfo.getSplitName(), str);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean loadSplitLibrary0(ClassLoader classLoader, String str, String str2) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("com.iqiyi.android.qigsaw.core.splitlib.");
            sb.append(str);
            sb.append("SplitLibraryLoader");
            Class loadClass = classLoader.loadClass(sb.toString());
            Object newInstance = loadClass.newInstance();
            HiddenApiReflection.findMethod(loadClass, "loadSplitLibrary", String.class).invoke(newInstance, new Object[]{str2});
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }
}
