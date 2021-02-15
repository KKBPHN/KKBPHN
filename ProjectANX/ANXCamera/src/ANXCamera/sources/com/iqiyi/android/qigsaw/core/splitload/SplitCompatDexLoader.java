package com.iqiyi.android.qigsaw.core.splitload;

import android.os.Build.VERSION;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class SplitCompatDexLoader {
    private static final String TAG = "SplitCompatDexLoader";
    private static int sPatchDexCount;

    final class V14 {
        private V14() {
        }

        /* access modifiers changed from: private */
        public static void load(ClassLoader classLoader, List list, File file) {
            Object obj = HiddenApiReflection.findField((Object) classLoader, "pathList").get(classLoader);
            HiddenApiReflection.expandFieldArray(obj, "dexElements", makeDexElements(obj, new ArrayList(list), file));
        }

        private static Object[] makeDexElements(Object obj, ArrayList arrayList, File file) {
            return (Object[]) HiddenApiReflection.findMethod(obj, "makeDexElements", ArrayList.class, File.class).invoke(obj, new Object[]{arrayList, file});
        }
    }

    final class V19 {
        private V19() {
        }

        /* access modifiers changed from: private */
        public static void load(ClassLoader classLoader, List list, File file) {
            Object obj = HiddenApiReflection.findField((Object) classLoader, "pathList").get(classLoader);
            ArrayList arrayList = new ArrayList();
            HiddenApiReflection.expandFieldArray(obj, "dexElements", makeDexElements(obj, new ArrayList(list), file, arrayList));
            if (arrayList.size() > 0) {
                Iterator it = arrayList.iterator();
                if (it.hasNext()) {
                    IOException iOException = (IOException) it.next();
                    SplitLog.e(SplitCompatDexLoader.TAG, "Exception in makeDexElement", (Throwable) iOException);
                    throw iOException;
                }
            }
        }

        /* JADX WARNING: type inference failed for: r0v0, types: [java.lang.String] */
        /* JADX WARNING: type inference failed for: r0v1, types: [java.lang.reflect.Method] */
        /* JADX WARNING: type inference failed for: r0v2, types: [java.lang.String] */
        /* JADX WARNING: type inference failed for: r0v3, types: [java.lang.reflect.Method] */
        /* JADX WARNING: type inference failed for: r0v4, types: [java.lang.reflect.Method] */
        /* JADX WARNING: type inference failed for: r0v5 */
        /* JADX WARNING: type inference failed for: r0v6 */
        /* JADX WARNING: type inference failed for: r0v7 */
        /* access modifiers changed from: private */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v5
  assigns: [java.lang.reflect.Method]
  uses: [java.lang.String, java.lang.reflect.Method]
  mth insns count: 38
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Unknown variable types count: 4 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static Object[] makeDexElements(Object obj, ArrayList arrayList, File file, ArrayList arrayList2) {
            ? r0;
            ? r02;
            ? r03 = "makeDexElements";
            try {
                r02 = r03;
                ? findMethod = HiddenApiReflection.findMethod(obj, (String) r03, ArrayList.class, File.class, ArrayList.class);
                r0 = findMethod;
            } catch (NoSuchMethodException unused) {
                Object[] objArr = new Object[0];
                String str = SplitCompatDexLoader.TAG;
                SplitLog.e(str, "NoSuchMethodException: makeDexElements(ArrayList,File,ArrayList) failure", objArr);
                try {
                    r0 = HiddenApiReflection.findMethod(obj, (String) r02, List.class, File.class, List.class);
                } catch (NoSuchMethodException e) {
                    SplitLog.e(str, "NoSuchMethodException: makeDexElements(List,File,List) failure", new Object[0]);
                    throw e;
                }
            }
            return (Object[]) r0.invoke(obj, new Object[]{arrayList, file, arrayList2});
        }
    }

    final class V23 {
        private V23() {
        }

        /* access modifiers changed from: private */
        public static void load(ClassLoader classLoader, List list, File file) {
            Object obj = HiddenApiReflection.findField((Object) classLoader, "pathList").get(classLoader);
            ArrayList arrayList = new ArrayList();
            HiddenApiReflection.expandFieldArray(obj, "dexElements", makePathElements(obj, new ArrayList(list), file, arrayList));
            if (arrayList.size() > 0) {
                Iterator it = arrayList.iterator();
                if (it.hasNext()) {
                    IOException iOException = (IOException) it.next();
                    SplitLog.e(SplitCompatDexLoader.TAG, "Exception in makePathElement", (Throwable) iOException);
                    throw iOException;
                }
            }
        }

        /* JADX WARNING: type inference failed for: r0v0, types: [java.lang.String] */
        /* JADX WARNING: type inference failed for: r0v1, types: [java.lang.reflect.Method] */
        /* JADX WARNING: type inference failed for: r0v2, types: [java.lang.String] */
        /* JADX WARNING: type inference failed for: r0v5, types: [java.lang.reflect.Method] */
        /* JADX WARNING: type inference failed for: r0v6, types: [java.lang.reflect.Method] */
        /* JADX WARNING: type inference failed for: r0v7 */
        /* JADX WARNING: type inference failed for: r0v8 */
        /* JADX WARNING: type inference failed for: r0v9 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v7
  assigns: [java.lang.reflect.Method]
  uses: [java.lang.String, java.lang.reflect.Method]
  mth insns count: 47
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Unknown variable types count: 4 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private static Object[] makePathElements(Object obj, ArrayList arrayList, File file, ArrayList arrayList2) {
            ? r0;
            ? r02;
            ? r03 = "makePathElements";
            try {
                r02 = r03;
                ? findMethod = HiddenApiReflection.findMethod(obj, (String) r03, List.class, File.class, List.class);
                r0 = findMethod;
            } catch (NoSuchMethodException unused) {
                Object[] objArr = new Object[0];
                String str = SplitCompatDexLoader.TAG;
                SplitLog.e(str, "NoSuchMethodException: makePathElements(List,File,List) failure", objArr);
                try {
                    r0 = HiddenApiReflection.findMethod(obj, (String) r02, ArrayList.class, File.class, ArrayList.class);
                } catch (NoSuchMethodException unused2) {
                    SplitLog.e(str, "NoSuchMethodException: makeDexElements(ArrayList,File,ArrayList) failure", new Object[0]);
                    try {
                        SplitLog.w(str, "NoSuchMethodException: try use v19 instead", new Object[0]);
                        return V19.makeDexElements(obj, arrayList, file, arrayList2);
                    } catch (NoSuchMethodException e) {
                        SplitLog.e(str, "NoSuchMethodException: makeDexElements(List,File,List) failure", new Object[0]);
                        throw e;
                    }
                }
            }
            return (Object[]) r0.invoke(obj, new Object[]{arrayList, file, arrayList2});
        }
    }

    SplitCompatDexLoader() {
    }

    static void load(ClassLoader classLoader, File file, List list) {
        if (!list.isEmpty()) {
            int i = VERSION.SDK_INT;
            if (i >= 23) {
                V23.load(classLoader, list, file);
            } else if (i >= 19) {
                V19.load(classLoader, list, file);
            } else if (i >= 14) {
                V14.load(classLoader, list, file);
            } else {
                throw new UnsupportedOperationException("don't support under SDK version 14!");
            }
            sPatchDexCount = list.size();
        }
    }

    static void unLoad(ClassLoader classLoader) {
        if (sPatchDexCount > 0) {
            if (VERSION.SDK_INT >= 14) {
                HiddenApiReflection.reduceFieldArray(HiddenApiReflection.findField((Object) classLoader, "pathList").get(classLoader), "dexElements", sPatchDexCount);
                return;
            }
            throw new RuntimeException("don't support under SDK version 14!");
        }
    }
}
