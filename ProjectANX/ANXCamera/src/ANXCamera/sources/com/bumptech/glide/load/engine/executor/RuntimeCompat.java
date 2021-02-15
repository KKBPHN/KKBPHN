package com.bumptech.glide.load.engine.executor;

import android.os.Build.VERSION;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

final class RuntimeCompat {
    private static final String CPU_LOCATION = "/sys/devices/system/cpu/";
    private static final String CPU_NAME_REGEX = "cpu[0-9]+";
    private static final String TAG = "GlideRuntimeCompat";

    private RuntimeCompat() {
    }

    static int availableProcessors() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        return VERSION.SDK_INT < 17 ? Math.max(getCoreCountPre17(), availableProcessors) : availableProcessors;
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: type inference failed for: r0v0, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r0v1 */
    /* JADX WARNING: type inference failed for: r0v6, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r0v8 */
    /* JADX WARNING: type inference failed for: r0v9, types: [java.io.File[]] */
    /* JADX WARNING: type inference failed for: r0v10 */
    /* JADX WARNING: type inference failed for: r0v11 */
    /* JADX WARNING: type inference failed for: r0v12 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v10
  assigns: [java.io.File[], ?[int, float, boolean, short, byte, char, OBJECT, ARRAY]]
  uses: [java.lang.String, ?[int, boolean, OBJECT, ARRAY, byte, short, char], ?[]]
  mth insns count: 30
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
    private static int getCoreCountPre17() {
        ? r0;
        ? r02;
        ? r03 = TAG;
        ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            r02 = r03;
            File file = new File(CPU_LOCATION);
            final Pattern compile = Pattern.compile(CPU_NAME_REGEX);
            ? listFiles = file.listFiles(new FilenameFilter() {
                public boolean accept(File file, String str) {
                    return compile.matcher(str).matches();
                }
            });
            StrictMode.setThreadPolicy(allowThreadDiskReads);
            r0 = listFiles;
        } catch (Throwable th) {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
            throw th;
        }
        return Math.max(1, r0 != 0 ? r0.length : 0);
    }
}
