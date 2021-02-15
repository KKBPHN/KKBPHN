package com.ss.android.medialib;

import android.os.Environment;
import android.text.TextUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    private static final String DEFAULT_FOLDER_NAME = "BDMedia";
    protected static String msFolderPath;

    public static boolean checkFileExists(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return new File(str).exists();
    }

    public static File createFile(String str, boolean z) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        File file = new File(str);
        if (!file.exists()) {
            if (!z) {
                file.mkdirs();
            } else {
                try {
                    File parentFile = file.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /* JADX WARNING: type inference failed for: r0v1 */
    /* JADX WARNING: type inference failed for: r11v1, types: [java.nio.channels.FileChannel] */
    /* JADX WARNING: type inference failed for: r2v0, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r0v2, types: [java.nio.channels.FileChannel] */
    /* JADX WARNING: type inference failed for: r11v3 */
    /* JADX WARNING: type inference failed for: r2v2 */
    /* JADX WARNING: type inference failed for: r0v3 */
    /* JADX WARNING: type inference failed for: r9v0 */
    /* JADX WARNING: type inference failed for: r2v3 */
    /* JADX WARNING: type inference failed for: r0v4 */
    /* JADX WARNING: type inference failed for: r11v4, types: [java.nio.channels.FileChannel] */
    /* JADX WARNING: type inference failed for: r2v4, types: [java.nio.channels.FileChannel] */
    /* JADX WARNING: type inference failed for: r0v5, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r11v6 */
    /* JADX WARNING: type inference failed for: r0v7 */
    /* JADX WARNING: type inference failed for: r2v5 */
    /* JADX WARNING: type inference failed for: r11v7 */
    /* JADX WARNING: type inference failed for: r11v8, types: [java.nio.channels.FileChannel] */
    /* JADX WARNING: type inference failed for: r2v6, types: [java.nio.channels.FileChannel] */
    /* JADX WARNING: type inference failed for: r0v8, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r11v10 */
    /* JADX WARNING: type inference failed for: r0v10 */
    /* JADX WARNING: type inference failed for: r2v7 */
    /* JADX WARNING: type inference failed for: r11v11 */
    /* JADX WARNING: type inference failed for: r11v12 */
    /* JADX WARNING: type inference failed for: r2v8 */
    /* JADX WARNING: type inference failed for: r2v9, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r11v13 */
    /* JADX WARNING: type inference failed for: r0v11 */
    /* JADX WARNING: type inference failed for: r11v14 */
    /* JADX WARNING: type inference failed for: r11v15 */
    /* JADX WARNING: type inference failed for: r0v12 */
    /* JADX WARNING: type inference failed for: r11v16 */
    /* JADX WARNING: type inference failed for: r11v17 */
    /* JADX WARNING: type inference failed for: r11v18 */
    /* JADX WARNING: type inference failed for: r11v19 */
    /* JADX WARNING: type inference failed for: r11v20 */
    /* JADX WARNING: type inference failed for: r11v21, types: [java.nio.channels.FileChannel] */
    /* JADX WARNING: type inference failed for: r0v13 */
    /* JADX WARNING: type inference failed for: r9v1 */
    /* JADX WARNING: type inference failed for: r2v10 */
    /* JADX WARNING: type inference failed for: r0v14 */
    /* JADX WARNING: type inference failed for: r0v15 */
    /* JADX WARNING: type inference failed for: r9v2 */
    /* JADX WARNING: type inference failed for: r2v11 */
    /* JADX WARNING: type inference failed for: r0v16 */
    /* JADX WARNING: type inference failed for: r0v17 */
    /* JADX WARNING: type inference failed for: r0v18, types: [java.nio.channels.FileChannel] */
    /* JADX WARNING: type inference failed for: r3v14, types: [java.nio.channels.FileChannel] */
    /* JADX WARNING: type inference failed for: r8v0, types: [java.nio.channels.WritableByteChannel] */
    /* JADX WARNING: type inference failed for: r0v19 */
    /* JADX WARNING: type inference failed for: r11v23 */
    /* JADX WARNING: type inference failed for: r11v24 */
    /* JADX WARNING: type inference failed for: r2v12 */
    /* JADX WARNING: type inference failed for: r0v20 */
    /* JADX WARNING: type inference failed for: r11v25 */
    /* JADX WARNING: type inference failed for: r0v21 */
    /* JADX WARNING: type inference failed for: r11v26 */
    /* JADX WARNING: type inference failed for: r2v13 */
    /* JADX WARNING: type inference failed for: r0v22 */
    /* JADX WARNING: type inference failed for: r11v27 */
    /* JADX WARNING: type inference failed for: r0v23 */
    /* JADX WARNING: type inference failed for: r2v14 */
    /* JADX WARNING: type inference failed for: r11v28 */
    /* JADX WARNING: type inference failed for: r11v29 */
    /* JADX WARNING: type inference failed for: r11v30 */
    /* JADX WARNING: type inference failed for: r0v24 */
    /* JADX WARNING: type inference failed for: r0v25 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r11v3
  assigns: []
  uses: []
  mth insns count: 143
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
    /* JADX WARNING: Removed duplicated region for block: B:100:0x00d0 A[SYNTHETIC, Splitter:B:100:0x00d0] */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x00df A[SYNTHETIC, Splitter:B:108:0x00df] */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x00e9 A[SYNTHETIC, Splitter:B:113:0x00e9] */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x00f3 A[SYNTHETIC, Splitter:B:118:0x00f3] */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x00fd A[SYNTHETIC, Splitter:B:123:0x00fd] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0082 A[SYNTHETIC, Splitter:B:59:0x0082] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x008c A[SYNTHETIC, Splitter:B:64:0x008c] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0096 A[SYNTHETIC, Splitter:B:69:0x0096] */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x00a0 A[SYNTHETIC, Splitter:B:74:0x00a0] */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x00b2 A[SYNTHETIC, Splitter:B:85:0x00b2] */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x00bc A[SYNTHETIC, Splitter:B:90:0x00bc] */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x00c6 A[SYNTHETIC, Splitter:B:95:0x00c6] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:56:0x007d=Splitter:B:56:0x007d, B:82:0x00ad=Splitter:B:82:0x00ad} */
    /* JADX WARNING: Unknown variable types count: 23 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean fileChannelCopy(String str, String str2) {
        ? r11;
        FileOutputStream fileOutputStream;
        ? r2;
        ? r112;
        ? r22;
        ? r0;
        ? r113;
        FileOutputStream fileOutputStream2;
        ? r23;
        ? r114;
        ? r24;
        ? r115;
        ? r116;
        ? r02;
        ? r03;
        ? r04;
        if (!isSdcardWritable()) {
            return false;
        }
        ? r05 = 0;
        try {
            r2 = new FileInputStream(str);
            try {
                fileOutputStream = new FileOutputStream(str2);
                try {
                    r112 = r2.getChannel();
                    try {
                        r04 = r05;
                        r05 = fileOutputStream.getChannel();
                        r112.transferTo(0, r112.size(), r05);
                        r04 = r05;
                        try {
                            r2.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (r112 != 0) {
                            try {
                                r112.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        try {
                            fileOutputStream.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                        if (r05 != 0) {
                            try {
                                r05.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        return true;
                    } catch (FileNotFoundException e5) {
                        e = e5;
                        ? r9 = r2;
                        r23 = r02;
                        r03 = r9;
                        r112 = r112;
                        r22 = r22;
                        r0 = r0;
                        e.printStackTrace();
                        if (r0 != 0) {
                        }
                        if (r112 != 0) {
                        }
                        if (fileOutputStream != null) {
                        }
                        if (r22 != 0) {
                        }
                        return false;
                    } catch (IOException e6) {
                        e = e6;
                        ? r92 = r2;
                        r24 = r05;
                        r0 = r92;
                        try {
                            r112 = r112;
                            r22 = r22;
                            r0 = r0;
                            e.printStackTrace();
                            if (r0 != 0) {
                            }
                            if (r112 != 0) {
                            }
                            if (fileOutputStream != null) {
                            }
                            if (r22 != 0) {
                            }
                            return false;
                        } catch (Throwable th) {
                            th = th;
                            ? r93 = r22;
                            r2 = r0;
                            r05 = r93;
                            r11 = r112;
                            if (r2 != 0) {
                            }
                            if (r11 != 0) {
                            }
                            if (fileOutputStream != null) {
                            }
                            if (r05 != 0) {
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        r11 = r112;
                        r05 = r04;
                        if (r2 != 0) {
                        }
                        if (r11 != 0) {
                        }
                        if (fileOutputStream != null) {
                        }
                        if (r05 != 0) {
                        }
                        throw th;
                    }
                } catch (FileNotFoundException e7) {
                    e = e7;
                    r115 = 0;
                    ? r06 = r2;
                    r113 = r115;
                    r23 = r113;
                    ? r117 = r113;
                    r03 = r06;
                    r112 = r112;
                    r22 = r22;
                    r0 = r0;
                    e.printStackTrace();
                    if (r0 != 0) {
                    }
                    if (r112 != 0) {
                    }
                    if (fileOutputStream != null) {
                    }
                    if (r22 != 0) {
                    }
                    return false;
                } catch (IOException e8) {
                    e = e8;
                    r116 = 0;
                    ? r07 = r2;
                    r114 = r116;
                    r24 = r114;
                    r112 = r114;
                    r0 = r07;
                    r112 = r112;
                    r22 = r22;
                    r0 = r0;
                    e.printStackTrace();
                    if (r0 != 0) {
                    }
                    if (r112 != 0) {
                    }
                    if (fileOutputStream != null) {
                    }
                    if (r22 != 0) {
                    }
                    return false;
                } catch (Throwable th3) {
                    th = th3;
                    r11 = 0;
                    if (r2 != 0) {
                    }
                    if (r11 != 0) {
                    }
                    if (fileOutputStream != null) {
                    }
                    if (r05 != 0) {
                    }
                    throw th;
                }
            } catch (FileNotFoundException e9) {
                e = e9;
                fileOutputStream2 = null;
                r115 = 0;
                ? r062 = r2;
                r113 = r115;
                r23 = r113;
                ? r1172 = r113;
                r03 = r062;
                r112 = r112;
                r22 = r22;
                r0 = r0;
                e.printStackTrace();
                if (r0 != 0) {
                    try {
                        r0.close();
                    } catch (IOException e10) {
                        e10.printStackTrace();
                    }
                }
                if (r112 != 0) {
                    try {
                        r112.close();
                    } catch (IOException e11) {
                        e11.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e12) {
                        e12.printStackTrace();
                    }
                }
                if (r22 != 0) {
                    try {
                        r22.close();
                    } catch (IOException e13) {
                        e13.printStackTrace();
                    }
                }
                return false;
            } catch (IOException e14) {
                e = e14;
                fileOutputStream = null;
                r116 = 0;
                ? r072 = r2;
                r114 = r116;
                r24 = r114;
                r112 = r114;
                r0 = r072;
                r112 = r112;
                r22 = r22;
                r0 = r0;
                e.printStackTrace();
                if (r0 != 0) {
                    try {
                        r0.close();
                    } catch (IOException e15) {
                        e15.printStackTrace();
                    }
                }
                if (r112 != 0) {
                    try {
                        r112.close();
                    } catch (IOException e16) {
                        e16.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e17) {
                        e17.printStackTrace();
                    }
                }
                if (r22 != 0) {
                    try {
                        r22.close();
                    } catch (IOException e18) {
                        e18.printStackTrace();
                    }
                }
                return false;
            } catch (Throwable th4) {
                th = th4;
                fileOutputStream = null;
                r11 = 0;
                r2 = r2;
                if (r2 != 0) {
                    try {
                        r2.close();
                    } catch (IOException e19) {
                        e19.printStackTrace();
                    }
                }
                if (r11 != 0) {
                    try {
                        r11.close();
                    } catch (IOException e20) {
                        e20.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e21) {
                        e21.printStackTrace();
                    }
                }
                if (r05 != 0) {
                    try {
                        r05.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException e23) {
            e = e23;
            fileOutputStream2 = null;
            r113 = 0;
            r23 = r113;
            ? r11722 = r113;
            r03 = r062;
            r112 = r112;
            r22 = r22;
            r0 = r0;
            e.printStackTrace();
            if (r0 != 0) {
            }
            if (r112 != 0) {
            }
            if (fileOutputStream != null) {
            }
            if (r22 != 0) {
            }
            return false;
        } catch (IOException e24) {
            e = e24;
            fileOutputStream = null;
            r114 = 0;
            r24 = r114;
            r112 = r114;
            r0 = r072;
            r112 = r112;
            r22 = r22;
            r0 = r0;
            e.printStackTrace();
            if (r0 != 0) {
            }
            if (r112 != 0) {
            }
            if (fileOutputStream != null) {
            }
            if (r22 != 0) {
            }
            return false;
        } catch (Throwable th5) {
            th = th5;
            fileOutputStream = null;
            r11 = 0;
            r2 = 0;
            if (r2 != 0) {
            }
            if (r11 != 0) {
            }
            if (fileOutputStream != null) {
            }
            if (r05 != 0) {
            }
            throw th;
        }
    }

    public static String getPath() {
        if (msFolderPath == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
            sb.append(File.separator);
            sb.append(DEFAULT_FOLDER_NAME);
            msFolderPath = sb.toString();
            File file = new File(msFolderPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return msFolderPath;
    }

    public static boolean isSdcardWritable() {
        try {
            return "mounted".equals(Environment.getExternalStorageState());
        } catch (Exception unused) {
            return false;
        }
    }
}
