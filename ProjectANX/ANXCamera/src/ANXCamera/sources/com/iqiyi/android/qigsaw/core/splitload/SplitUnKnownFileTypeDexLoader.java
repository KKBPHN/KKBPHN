package com.iqiyi.android.qigsaw.core.splitload;

import android.os.Build.VERSION;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;
import dalvik.system.DexFile;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

final class SplitUnKnownFileTypeDexLoader {
    private static final String TAG = "SplitUnKnownFileTypeDexLoader";

    SplitUnKnownFileTypeDexLoader() {
    }

    /* JADX WARNING: type inference failed for: r3v6, types: [java.io.File] */
    /* JADX WARNING: type inference failed for: r7v0, types: [java.lang.Object[]] */
    /* JADX WARNING: type inference failed for: r3v8, types: [java.lang.Object] */
    /* JADX WARNING: type inference failed for: r3v9 */
    /* JADX WARNING: type inference failed for: r3v10, types: [java.io.File] */
    /* JADX WARNING: type inference failed for: r3v11, types: [java.io.File] */
    /* JADX WARNING: type inference failed for: r9v1, types: [java.lang.Object[]] */
    /* JADX WARNING: type inference failed for: r3v14, types: [java.lang.Object] */
    /* JADX WARNING: type inference failed for: r10v6, types: [java.lang.Object[]] */
    /* JADX WARNING: type inference failed for: r3v15, types: [java.lang.Object] */
    /* JADX WARNING: type inference failed for: r8v7, types: [java.lang.Object[]] */
    /* JADX WARNING: type inference failed for: r3v16, types: [java.lang.Object] */
    /* JADX WARNING: type inference failed for: r3v17 */
    /* JADX WARNING: type inference failed for: r3v18 */
    /* JADX WARNING: type inference failed for: r3v19 */
    /* JADX WARNING: type inference failed for: r3v20 */
    /* JADX WARNING: type inference failed for: r3v21 */
    /* JADX WARNING: type inference failed for: r3v22 */
    /* JADX WARNING: Can't wrap try/catch for region: R(9:15|16|17|18|19|20|21|26|44) */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00cd, code lost:
        r7 = com.iqiyi.android.qigsaw.core.splitload.HiddenApiReflection.findConstructor(r7, java.io.File.class, java.util.zip.ZipFile.class, dalvik.system.DexFile.class);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00df, code lost:
        r3 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        r3 = r7.newInstance(new java.lang.Object[]{r3, new java.util.zip.ZipFile(r3), r6});
        r3 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00f5, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00f6, code lost:
        r7 = new java.lang.StringBuilder();
        r7.append("Unable to open zip file: ");
        r7.append(r3.getAbsolutePath());
        com.iqiyi.android.qigsaw.core.common.SplitLog.printErrStackTrace(TAG, r6, r7.toString(), new java.lang.Object[0]);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<java.lang.String>, for r13v0, types: [java.util.List, java.util.List<java.lang.String>] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x00ae */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r3v17
  assigns: [java.lang.Object]
  uses: [?[OBJECT, ARRAY], java.io.File, java.lang.Object]
  mth insns count: 124
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
    /* JADX WARNING: Unknown variable types count: 10 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void loadDex(ClassLoader classLoader, List<String> list, File file) {
        if (VERSION.SDK_INT < 21 && list != null) {
            ArrayList<?> arrayList = new ArrayList<>();
            for (String str : list) {
                if (str.endsWith(SplitConstants.DOT_SO)) {
                    arrayList.add(new File(str));
                }
            }
            if (!arrayList.isEmpty()) {
                Object obj = HiddenApiReflection.findField((Object) classLoader, "pathList").get(classLoader);
                Method findMethod = HiddenApiReflection.findMethod(Class.forName("dalvik.system.DexPathList"), "loadDexFile", File.class, File.class);
                ArrayList arrayList2 = new ArrayList(arrayList.size());
                for (? r3 : arrayList) {
                    try {
                        DexFile dexFile = (DexFile) findMethod.invoke(null, new Object[]{r3, file});
                        Class cls = Class.forName("dalvik.system.DexPathList$Element");
                        ? r32 = r3;
                        ? newInstance = HiddenApiReflection.findConstructor(cls, File.class, Boolean.TYPE, File.class, DexFile.class).newInstance(new Object[]{r3, Boolean.valueOf(false), r3, dexFile});
                        ? r33 = newInstance;
                        ? r34 = r32;
                        r34 = HiddenApiReflection.findConstructor(cls, File.class, File.class, DexFile.class).newInstance(new Object[]{r32, r32, dexFile});
                        r33 = r34;
                        arrayList2.add(r33);
                    } catch (Throwable th) {
                        if (!(th instanceof IOException)) {
                            throw th;
                        }
                    }
                }
                if (!arrayList2.isEmpty()) {
                    HiddenApiReflection.expandFieldArray(obj, "dexElements", arrayList2.toArray());
                }
            }
        }
    }
}
