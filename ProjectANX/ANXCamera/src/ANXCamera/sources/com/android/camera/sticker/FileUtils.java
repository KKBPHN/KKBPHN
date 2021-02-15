package com.android.camera.sticker;

import android.content.Context;
import android.text.TextUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    /* JADX WARNING: type inference failed for: r0v3 */
    /* JADX WARNING: type inference failed for: r3v2, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r0v4, types: [java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r5v1 */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r5v2, types: [java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r0v6, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r3v7 */
    /* JADX WARNING: type inference failed for: r5v3 */
    /* JADX WARNING: type inference failed for: r3v11 */
    /* JADX WARNING: type inference failed for: r3v13, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r5v4 */
    /* JADX WARNING: type inference failed for: r0v7 */
    /* JADX WARNING: type inference failed for: r5v5 */
    /* JADX WARNING: type inference failed for: r5v6, types: [java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r0v10 */
    /* JADX WARNING: type inference failed for: r3v17 */
    /* JADX WARNING: type inference failed for: r5v7 */
    /* JADX WARNING: type inference failed for: r3v18 */
    /* JADX WARNING: type inference failed for: r5v8 */
    /* JADX WARNING: type inference failed for: r5v9 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r3v5
  assigns: []
  uses: []
  mth insns count: 79
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
    /* JADX WARNING: Removed duplicated region for block: B:52:0x007b A[SYNTHETIC, Splitter:B:52:0x007b] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0085 A[SYNTHETIC, Splitter:B:57:0x0085] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0094 A[SYNTHETIC, Splitter:B:66:0x0094] */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x009e A[SYNTHETIC, Splitter:B:71:0x009e] */
    /* JADX WARNING: Unknown variable types count: 8 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean copyFileIfNeed(Context context, File file, String str) {
        Throwable th;
        ? r3;
        ? r5;
        ? r32;
        ? r52;
        ? r0;
        ? fileOutputStream;
        if (file != null && !TextUtils.isEmpty(str)) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (!file.exists()) {
                ? r02 = 0;
                try {
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                    r3 = context.getAssets().open(str);
                    if (r3 == 0) {
                        if (r3 != 0) {
                            try {
                                r3.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return false;
                    }
                    try {
                        fileOutputStream = new FileOutputStream(file);
                    } catch (IOException unused) {
                        ? r53 = 0;
                        r0 = r3;
                        r52 = r53;
                        try {
                            file.delete();
                            if (r0 != 0) {
                                try {
                                    r0.close();
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                            }
                            if (r52 != 0) {
                                try {
                                    r52.close();
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                }
                            }
                            return false;
                        } catch (Throwable th2) {
                            th = th2;
                            r32 = r0;
                            r5 = r52;
                            r02 = r5;
                            r3 = r32;
                            if (r3 != 0) {
                            }
                            if (r02 != 0) {
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        if (r3 != 0) {
                            try {
                                r3.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        if (r02 != 0) {
                            try {
                                r02.close();
                            } catch (IOException e5) {
                                e5.printStackTrace();
                            }
                        }
                        throw th;
                    }
                    try {
                        byte[] bArr = new byte[4096];
                        while (true) {
                            int read = r3.read(bArr);
                            if (read <= 0) {
                                break;
                            }
                            fileOutputStream.write(bArr, 0, read);
                        }
                        r3.close();
                        fileOutputStream.close();
                        if (r3 != 0) {
                            try {
                                r3.close();
                            } catch (IOException e6) {
                                e6.printStackTrace();
                            }
                        }
                        try {
                            fileOutputStream.close();
                        } catch (IOException e7) {
                            e7.printStackTrace();
                        }
                    } catch (IOException unused2) {
                        r0 = r3;
                        r52 = r53;
                        file.delete();
                        if (r0 != 0) {
                        }
                        if (r52 != 0) {
                        }
                        return false;
                    } catch (Throwable th4) {
                        th = th4;
                        r32 = r3;
                        r5 = fileOutputStream;
                        r02 = r5;
                        r3 = r32;
                        if (r3 != 0) {
                        }
                        if (r02 != 0) {
                        }
                        throw th;
                    }
                } catch (IOException unused3) {
                    r52 = 0;
                    file.delete();
                    if (r0 != 0) {
                    }
                    if (r52 != 0) {
                    }
                    return false;
                } catch (Throwable th5) {
                    th = th5;
                    r3 = 0;
                    r02 = r02;
                    if (r3 != 0) {
                    }
                    if (r02 != 0) {
                    }
                    throw th;
                }
            }
        }
        return true;
    }
}
