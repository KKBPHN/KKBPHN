package com.miui.internal.variable;

import android.os.Build.VERSION;
import java.util.Locale;

public abstract class AbsClassFactory {
    protected static final int CURRENT_SUPPORT_SDK_VERSION = VERSION.SDK_INT;
    protected static final int MIN_SUPPORT_SDK_VERSION = 16;

    /* JADX WARNING: type inference failed for: r4v1, types: [int] */
    /* JADX WARNING: type inference failed for: r4v2, types: [int] */
    /* JADX WARNING: type inference failed for: r4v5 */
    /* JADX WARNING: type inference failed for: r4v6, types: [int] */
    /* JADX WARNING: type inference failed for: r4v7, types: [int] */
    /* JADX WARNING: type inference failed for: r4v8 */
    /* JADX WARNING: type inference failed for: r4v9, types: [java.lang.Object] */
    /* JADX WARNING: type inference failed for: r4v10 */
    /* JADX WARNING: type inference failed for: r4v11 */
    /* JADX WARNING: type inference failed for: r4v12 */
    /* JADX WARNING: type inference failed for: r4v13 */
    /* JADX WARNING: type inference failed for: r4v14 */
    /* JADX WARNING: type inference failed for: r4v15 */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r4v5
  assigns: []
  uses: []
  mth insns count: 33
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
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 5 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Object create(String str) {
        ? r4;
        ? r42;
        ? r43 = CURRENT_SUPPORT_SDK_VERSION;
        while (r43 >= 16) {
            String format = String.format(Locale.US, "com.miui.internal.variable.v%d.%s", new Object[]{Integer.valueOf(r43), str});
            try {
                r42 = r43;
                r43 = Class.forName(format).newInstance();
                r42 = r43;
                return r43;
            } catch (ClassNotFoundException unused) {
                r4 = r43;
            } catch (Exception e) {
                VariableExceptionHandler.getInstance().onThrow(format, e);
                r4 = r42;
            }
        }
        VariableExceptionHandler instance = VariableExceptionHandler.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("Cannot found class ");
        sb.append(str);
        instance.onThrow(str, new ClassNotFoundException(sb.toString()));
        return null;
        r43 = r4 - 1;
    }

    public abstract Object get();
}
