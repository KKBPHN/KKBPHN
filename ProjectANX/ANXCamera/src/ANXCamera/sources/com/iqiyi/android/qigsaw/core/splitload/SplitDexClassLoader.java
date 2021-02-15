package com.iqiyi.android.qigsaw.core.splitload;

import android.text.TextUtils;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import dalvik.system.BaseDexClassLoader;
import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

final class SplitDexClassLoader extends BaseDexClassLoader {
    private static final String TAG = "SplitDexClassLoader";
    private Set dependenciesLoaders;
    private final String moduleName;

    private SplitDexClassLoader(String str, List list, File file, String str2, List list2, ClassLoader classLoader) {
        super(list == null ? "" : TextUtils.join(File.pathSeparator, list), file, str2, classLoader);
        this.moduleName = str;
        this.dependenciesLoaders = SplitApplicationLoaders.getInstance().getClassLoaders(list2);
        SplitUnKnownFileTypeDexLoader.loadDex(this, list, file);
    }

    static SplitDexClassLoader create(String str, List list, File file, File file2, List list2) {
        long currentTimeMillis = System.currentTimeMillis();
        SplitDexClassLoader splitDexClassLoader = new SplitDexClassLoader(str, list, file, file2 == null ? null : file2.getAbsolutePath(), list2, SplitDexClassLoader.class.getClassLoader());
        SplitLog.d(TAG, "Cost %d ms to load %s code", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), str);
        return splitDexClassLoader;
    }

    /* JADX WARNING: type inference failed for: r4v0, types: [com.iqiyi.android.qigsaw.core.splitload.SplitDexClassLoader, dalvik.system.BaseDexClassLoader] */
    /* JADX WARNING: type inference failed for: r4v1, types: [com.iqiyi.android.qigsaw.core.splitload.SplitDexClassLoader] */
    /* JADX WARNING: type inference failed for: r4v3, types: [java.util.Iterator] */
    /* JADX WARNING: type inference failed for: r4v4, types: [java.util.Iterator] */
    /* JADX WARNING: type inference failed for: r4v5 */
    /* JADX WARNING: type inference failed for: r4v6, types: [java.lang.Class] */
    /* JADX WARNING: type inference failed for: r4v7, types: [java.lang.Class] */
    /* JADX WARNING: type inference failed for: r4v8 */
    /* JADX WARNING: type inference failed for: r4v9 */
    /* JADX WARNING: type inference failed for: r4v10 */
    /* JADX WARNING: type inference failed for: r4v11 */
    /* JADX WARNING: type inference failed for: r4v12 */
    /* JADX WARNING: type inference failed for: r4v13 */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r4v5
  assigns: []
  uses: []
  mth insns count: 28
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 4 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Class findClass(String str) {
        ? r4;
        try {
            this = this;
            this = super.findClass(str);
            r4 = this;
            return this;
        } catch (ClassNotFoundException e) {
            Set set = r4.dependenciesLoaders;
            if (set != null) {
                ? r42 = set.iterator();
                while (r42.hasNext()) {
                    SplitDexClassLoader splitDexClassLoader = (SplitDexClassLoader) r42.next();
                    try {
                        r4 = r42;
                        ? loadClassItself = splitDexClassLoader.loadClassItself(str);
                        r4 = loadClassItself;
                        return loadClassItself;
                    } catch (ClassNotFoundException unused) {
                        SplitLog.w(TAG, "SplitDexClassLoader: Class %s is not found in %s ClassLoader", str, splitDexClassLoader.moduleName());
                        r42 = r4;
                    }
                }
            }
            throw e;
        }
    }

    public String findLibrary(String str) {
        String findLibrary = super.findLibrary(str);
        if (findLibrary == null) {
            Set<SplitDexClassLoader> set = this.dependenciesLoaders;
            if (set != null) {
                for (SplitDexClassLoader findLibrary2 : set) {
                    findLibrary = findLibrary2.findLibrary(str);
                    if (findLibrary != null) {
                        break;
                    }
                }
            }
        }
        return (findLibrary != null || !(getParent() instanceof BaseDexClassLoader)) ? findLibrary : ((BaseDexClassLoader) getParent()).findLibrary(str);
    }

    /* access modifiers changed from: 0000 */
    public String findLibraryItself(String str) {
        return super.findLibrary(str);
    }

    /* access modifiers changed from: protected */
    public URL findResource(String str) {
        URL findResource = super.findResource(str);
        if (findResource == null) {
            Set<SplitDexClassLoader> set = this.dependenciesLoaders;
            if (set != null) {
                for (SplitDexClassLoader findResourceItself : set) {
                    findResource = findResourceItself.findResourceItself(str);
                    if (findResource != null) {
                        break;
                    }
                }
            }
        }
        return findResource;
    }

    /* access modifiers changed from: 0000 */
    public URL findResourceItself(String str) {
        return super.findResource(str);
    }

    /* access modifiers changed from: protected */
    public Enumeration findResources(String str) {
        Enumeration findResources = super.findResources(str);
        if (findResources == null) {
            Set<SplitDexClassLoader> set = this.dependenciesLoaders;
            if (set != null) {
                for (SplitDexClassLoader findResourcesItself : set) {
                    findResources = findResourcesItself.findResourcesItself(str);
                    if (findResources != null) {
                        break;
                    }
                }
            }
        }
        return findResources;
    }

    /* access modifiers changed from: 0000 */
    public Enumeration findResourcesItself(String str) {
        return super.findResources(str);
    }

    /* access modifiers changed from: 0000 */
    public Class loadClassItself(String str) {
        Class findLoadedClass = findLoadedClass(str);
        return findLoadedClass != null ? findLoadedClass : super.findClass(str);
    }

    /* access modifiers changed from: 0000 */
    public String moduleName() {
        return this.moduleName;
    }
}
