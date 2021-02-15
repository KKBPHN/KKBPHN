package com.ss.android.vesdk;

import android.os.StatFs;
import android.text.TextUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.ZipFile;

public class VEFileUtils {
    public static boolean canWrite(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(".");
        sb.append(System.currentTimeMillis());
        File file2 = new File(file, sb.toString());
        boolean mkdir = file2.mkdir();
        if (mkdir) {
            mkdir = file2.delete();
        }
        return mkdir;
    }

    public static void clearPath(String str) {
        String[] list;
        StringBuilder sb;
        File file = new File(str);
        if (file.exists() && file.isDirectory()) {
            for (String str2 : file.list()) {
                if (str.endsWith(File.separator)) {
                    sb = new StringBuilder();
                    sb.append(str);
                } else {
                    sb = new StringBuilder();
                    sb.append(str);
                    sb.append(File.separator);
                }
                sb.append(str2);
                String sb2 = sb.toString();
                File file2 = new File(sb2);
                if (file2.isFile()) {
                    file2.delete();
                }
                if (file2.isDirectory()) {
                    deletePath(sb2);
                }
            }
        }
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }

    public static void close(ZipFile zipFile) {
        if (zipFile != null) {
            try {
                zipFile.close();
            } catch (IOException unused) {
            }
        }
    }

    /* JADX WARNING: type inference failed for: r2v0 */
    /* JADX WARNING: type inference failed for: r3v0, types: [java.io.BufferedInputStream] */
    /* JADX WARNING: type inference failed for: r2v1, types: [java.io.BufferedOutputStream] */
    /* JADX WARNING: type inference failed for: r5v3 */
    /* JADX WARNING: type inference failed for: r3v1 */
    /* JADX WARNING: type inference failed for: r2v2 */
    /* JADX WARNING: type inference failed for: r5v4, types: [java.io.BufferedOutputStream] */
    /* JADX WARNING: type inference failed for: r2v3, types: [java.io.BufferedInputStream] */
    /* JADX WARNING: type inference failed for: r3v2 */
    /* JADX WARNING: type inference failed for: r5v6 */
    /* JADX WARNING: type inference failed for: r3v3 */
    /* JADX WARNING: type inference failed for: r3v4, types: [java.io.BufferedInputStream] */
    /* JADX WARNING: type inference failed for: r5v7 */
    /* JADX WARNING: type inference failed for: r2v4 */
    /* JADX WARNING: type inference failed for: r5v8 */
    /* JADX WARNING: type inference failed for: r5v9, types: [java.io.BufferedOutputStream] */
    /* JADX WARNING: type inference failed for: r2v5 */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r5v11 */
    /* JADX WARNING: type inference failed for: r3v6 */
    /* JADX WARNING: type inference failed for: r5v12 */
    /* JADX WARNING: type inference failed for: r5v13 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r3v1
  assigns: []
  uses: []
  mth insns count: 66
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
    /* JADX WARNING: Removed duplicated region for block: B:45:0x006a A[SYNTHETIC, Splitter:B:45:0x006a] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0074 A[SYNTHETIC, Splitter:B:50:0x0074] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0082 A[SYNTHETIC, Splitter:B:58:0x0082] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x008c A[SYNTHETIC, Splitter:B:63:0x008c] */
    /* JADX WARNING: Unknown variable types count: 8 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean copyFile(File file, File file2) {
        ? r3;
        ? r5;
        ? r32;
        ? r52;
        ? r2;
        if (!file.exists() || !file.isFile() || file2.isDirectory()) {
            return false;
        }
        if (file2.exists()) {
            file2.delete();
        }
        ? r22 = 0;
        try {
            byte[] bArr = new byte[2048];
            r3 = new BufferedInputStream(new FileInputStream(file));
            try {
                ? bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
                while (true) {
                    try {
                        int read = r3.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        bufferedOutputStream.write(bArr, 0, read);
                    } catch (IOException e) {
                        e = e;
                        r2 = r3;
                        r52 = r5;
                        try {
                            e.printStackTrace();
                            if (r2 != 0) {
                            }
                            if (r52 != 0) {
                            }
                            return false;
                        } catch (Throwable th) {
                            th = th;
                            r32 = r2;
                            r5 = r52;
                            r22 = r5;
                            r3 = r32;
                            if (r3 != 0) {
                            }
                            if (r22 != 0) {
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        r32 = r3;
                        r5 = bufferedOutputStream;
                        r22 = r5;
                        r3 = r32;
                        if (r3 != 0) {
                        }
                        if (r22 != 0) {
                        }
                        throw th;
                    }
                }
                bufferedOutputStream.flush();
                try {
                    r3.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                try {
                    bufferedOutputStream.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
                return true;
            } catch (IOException e4) {
                e = e4;
                ? r53 = 0;
                r2 = r3;
                r52 = r53;
                e.printStackTrace();
                if (r2 != 0) {
                    try {
                        r2.close();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                }
                if (r52 != 0) {
                    try {
                        r52.close();
                    } catch (IOException e6) {
                        e6.printStackTrace();
                    }
                }
                return false;
            } catch (Throwable th3) {
                th = th3;
                if (r3 != 0) {
                    try {
                        r3.close();
                    } catch (IOException e7) {
                        e7.printStackTrace();
                    }
                }
                if (r22 != 0) {
                    try {
                        r22.close();
                    } catch (IOException e8) {
                        e8.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (IOException e9) {
            e = e9;
            r52 = 0;
            e.printStackTrace();
            if (r2 != 0) {
            }
            if (r52 != 0) {
            }
            return false;
        } catch (Throwable th4) {
            th = th4;
            r3 = 0;
            r22 = r22;
            if (r3 != 0) {
            }
            if (r22 != 0) {
            }
            throw th;
        }
    }

    public static boolean copyFile(String str, String str2) {
        return copyFile(new File(str), new File(str2));
    }

    public static void deleteFile(String str) {
        if (!TextUtils.isEmpty(str)) {
            File file = new File(str);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
    }

    public static void deletePath(String str) {
        StringBuilder sb;
        File file = new File(str);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
                return;
            }
            String[] list = file.list();
            if (list != null) {
                for (String str2 : list) {
                    if (str2 != null) {
                        if (str.endsWith(File.separator)) {
                            sb = new StringBuilder();
                            sb.append(str);
                        } else {
                            sb = new StringBuilder();
                            sb.append(str);
                            sb.append(File.separator);
                        }
                        sb.append(str2);
                        String sb2 = sb.toString();
                        File file2 = new File(sb2);
                        if (file2.isFile()) {
                            file2.delete();
                        }
                        if (file2.isDirectory()) {
                            deletePath(sb2);
                        }
                    }
                }
                file.delete();
            }
        }
    }

    public static boolean exists(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return new File(str).exists();
    }

    public static long getAllBytes(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        try {
            if (!TextUtils.isEmpty(str) && new File(str).exists()) {
                StatFs statFs = new StatFs(str);
                return ((long) statFs.getBlockSize()) * ((long) statFs.getBlockCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getAvailableBytes(String str) {
        try {
            if (!TextUtils.isEmpty(str) && new File(str).exists()) {
                StatFs statFs = new StatFs(str);
                return ((long) statFs.getBlockSize()) * ((long) statFs.getAvailableBlocks());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getFileExtension(String str) {
        int lastIndexOf = str.lastIndexOf(".");
        return (lastIndexOf < 0 || lastIndexOf >= str.length() - 1) ? "" : str.substring(lastIndexOf + 1).toUpperCase();
    }

    public static String getFileName(String str) {
        String str2 = "/";
        if (str.endsWith(str2)) {
            str = str.substring(0, str.length() - 1);
        }
        int lastIndexOf = str.lastIndexOf(str2);
        return (lastIndexOf <= 0 || lastIndexOf >= str.length() - 1) ? str : str.substring(lastIndexOf + 1);
    }

    public static String getFileNameWithoutExtension(String str) {
        String fileName = getFileName(str);
        if (fileName == null || fileName.length() <= 0) {
            return fileName;
        }
        int lastIndexOf = fileName.lastIndexOf(46);
        return lastIndexOf > 0 ? fileName.substring(0, lastIndexOf) : fileName;
    }

    public static long getFileSize(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        File file = new File(str);
        if (!file.exists()) {
            return 0;
        }
        long length = file.length();
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                int i = 0;
                while (i < listFiles.length) {
                    try {
                        length += getFileSize(listFiles[i].getAbsolutePath());
                        i++;
                    } catch (StackOverflowError e) {
                        e.printStackTrace();
                        return 0;
                    } catch (OutOfMemoryError e2) {
                        e2.printStackTrace();
                        return 0;
                    }
                }
            }
        }
        return length;
    }

    public static String getParentFilePath(String str) {
        String str2 = "/";
        if (str.endsWith(str2)) {
            str = str.substring(0, str.length() - 1);
        }
        int lastIndexOf = str.lastIndexOf(str2);
        if (lastIndexOf >= 0) {
            return str.substring(0, lastIndexOf);
        }
        return null;
    }

    public static boolean mkdir(String str) {
        return new File(str).mkdirs();
    }

    /* JADX INFO: unreachable blocks removed: 1, instructions: 1 */
    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:25:0x002f in {} preds:[B:12:0x001c, B:15:0x0021]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.calcImmediateDominators(BlockProcessor.java:303)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:255)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:55)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:45)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
        */
    public static java.lang.String readFileFirstLine(java.lang.String r3) {
        /*
        r0 = 0
        java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0022 }
        java.io.FileReader r2 = new java.io.FileReader     // Catch:{ Exception -> 0x0022 }
        r2.<init>(r3)     // Catch:{ Exception -> 0x0022 }
        r1.<init>(r2)     // Catch:{ Exception -> 0x0022 }
        java.lang.String r3 = r1.readLine()     // Catch:{ Exception -> 0x001e, all -> 0x001b }
        r1.close()     // Catch:{ Exception -> 0x001e, all -> 0x001b }
        r1.close()     // Catch:{ IOException -> 0x0016 }
        goto L_0x001a
    L_0x0016:
        r0 = move-exception
        r0.printStackTrace()
    L_0x001a:
        return r3
    L_0x001b:
        r3 = move-exception
        r0 = r1
        goto L_0x002f
    L_0x001e:
        r0 = r1
        goto L_0x0022
    L_0x0020:
        r3 = move-exception
        goto L_0x002f
    L_0x0022:
        java.lang.String r3 = ""
        if (r0 == 0) goto L_0x002e
        r0.close()     // Catch:{ IOException -> 0x002a }
        goto L_0x002e
    L_0x002a:
        r0 = move-exception
        r0.printStackTrace()
    L_0x002e:
        return r3
    L_0x002f:
        if (r0 == 0) goto L_0x0039
        r0.close()     // Catch:{ IOException -> 0x0035 }
        goto L_0x0039
    L_0x0035:
        r0 = move-exception
        r0.printStackTrace()
    L_0x0039:
        throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ss.android.vesdk.VEFileUtils.readFileFirstLine(java.lang.String):java.lang.String");
    }

    public static boolean renameFile(String str, String str2) {
        File file = new File(str);
        File file2 = new File(str2);
        if (!file.exists()) {
            return false;
        }
        return file.renameTo(file2);
    }

    public static void setPermissions(String str, int i) {
        Integer valueOf = Integer.valueOf(-1);
        VEJavaCalls.callStaticMethod("android.os.FileUtils", "setPermissions", str, Integer.valueOf(i), valueOf, valueOf);
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:21:0x0033=Splitter:B:21:0x0033, B:15:0x0029=Splitter:B:15:0x0029} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void write(String str, String str2, boolean z) {
        FileWriter fileWriter;
        FileWriter fileWriter2 = null;
        try {
            File file = new File(str);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileWriter fileWriter3 = new FileWriter(str, z);
            try {
                fileWriter3.write(str2);
                try {
                    fileWriter3.close();
                } catch (Exception unused) {
                }
            } catch (IOException e) {
                e = e;
                fileWriter2 = fileWriter3;
                e.printStackTrace();
                if (fileWriter2 == null) {
                }
                fileWriter2.close();
            } catch (Throwable th) {
                th = th;
                fileWriter2 = fileWriter3;
                try {
                    th.printStackTrace();
                    fileWriter2.close();
                } finally {
                    if (fileWriter != null) {
                        try {
                            fileWriter.close();
                        } catch (Exception unused2) {
                        }
                    }
                }
            }
        } catch (IOException e2) {
            e = e2;
            e.printStackTrace();
            if (fileWriter2 == null) {
                return;
            }
            fileWriter2.close();
        } catch (Throwable th2) {
            th = th2;
            th.printStackTrace();
            if (fileWriter == null) {
                return;
            }
            fileWriter2.close();
        }
    }
}
