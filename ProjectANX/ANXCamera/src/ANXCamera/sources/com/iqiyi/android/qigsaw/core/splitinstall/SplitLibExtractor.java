package com.iqiyi.android.qigsaw.core.splitinstall;

import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.iqiyi.android.qigsaw.core.common.FileUtil;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo.LibInfo.Lib;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitPathManager;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

final class SplitLibExtractor implements Closeable {
    private static final String LOCK_FILENAME = "SplitLib.lock";
    private static final String TAG = "Split:LibExtractor";
    private final FileLock cacheLock;
    private final File libDir;
    private final FileChannel lockChannel;
    private final RandomAccessFile lockRaf;
    private final File sourceApk;

    SplitLibExtractor(File file, File file2) {
        String str = TAG;
        this.sourceApk = file;
        this.libDir = file2;
        File file3 = new File(file2, LOCK_FILENAME);
        this.lockRaf = new RandomAccessFile(file3, "rw");
        try {
            this.lockChannel = this.lockRaf.getChannel();
            StringBuilder sb = new StringBuilder();
            sb.append("Blocking on lock ");
            sb.append(file3.getPath());
            SplitLog.i(str, sb.toString(), new Object[0]);
            this.cacheLock = this.lockChannel.lock();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(file3.getPath());
            sb2.append(" locked");
            SplitLog.i(str, sb2.toString(), new Object[0]);
        } catch (IOException | Error | RuntimeException e) {
            FileUtil.closeQuietly(this.lockChannel);
            throw e;
        } catch (IOException | Error | RuntimeException e2) {
            FileUtil.closeQuietly(this.lockRaf);
            throw e2;
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo$LibInfo$Lib>, for r3v0, types: [java.util.List<com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo$LibInfo$Lib>, java.util.List] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private Lib findLib(String str, List<Lib> list) {
        for (Lib lib : list) {
            if (lib.getName().equals(str)) {
                return lib;
            }
        }
        return null;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo$LibInfo$Lib>, for r12v0, types: [java.util.List<com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo$LibInfo$Lib>, java.util.List] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private List loadExistingExtractions(List<Lib> list) {
        Object[] objArr = new Object[0];
        String str = TAG;
        SplitLog.i(str, "loading existing lib files", objArr);
        File[] listFiles = this.libDir.listFiles();
        if (listFiles == null || listFiles.length <= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Missing extracted lib file '");
            sb.append(this.libDir.getPath());
            sb.append("'");
            throw new IOException(sb.toString());
        }
        ArrayList arrayList = new ArrayList(listFiles.length);
        for (Lib lib : list) {
            boolean z = false;
            for (File file : listFiles) {
                if (lib.getName().equals(file.getName())) {
                    if (lib.getMd5().equals(FileUtil.getMD5(file))) {
                        arrayList.add(file);
                        z = true;
                    } else {
                        throw new IOException("Invalid extracted lib : file md5 is unmatched!");
                    }
                }
            }
            if (!z) {
                throw new IOException(String.format("Invalid extracted lib: file %s is not existing!", new Object[]{lib.getName()}));
            }
        }
        SplitLog.i(str, "Existing lib files loaded", new Object[0]);
        return arrayList;
    }

    /* JADX WARNING: Removed duplicated region for block: B:65:0x018c  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x018f  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x01c5  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x01db  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x01de  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0206  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private List performExtractions(SplitInfo splitInfo) {
        int i;
        String str;
        String md5;
        Throwable th;
        Throwable th2;
        SplitLibExtractor splitLibExtractor = this;
        ZipFile zipFile = new ZipFile(splitLibExtractor.sourceApk);
        int i2 = 1;
        int i3 = 0;
        String format = String.format("lib/%s/", new Object[]{splitInfo.getLibInfo().getAbi()});
        Enumeration entries = zipFile.entries();
        ArrayList arrayList = new ArrayList();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            String name = zipEntry.getName();
            if (name.charAt(i3) >= 'l') {
                if (name.charAt(i3) <= 'l') {
                    if (name.startsWith("lib/")) {
                        if (!name.endsWith(SplitConstants.DOT_SO)) {
                            int i4 = i2;
                        } else if (name.startsWith(format)) {
                            String substring = name.substring(name.lastIndexOf(47) + i2);
                            Lib findLib = splitLibExtractor.findLib(substring, splitInfo.getLibInfo().getLibs());
                            if (findLib != null) {
                                File file = new File(splitLibExtractor.libDir, substring);
                                boolean exists = file.exists();
                                String str2 = "'";
                                String str3 = TAG;
                                if (exists) {
                                    if (findLib.getMd5().equals(FileUtil.getMD5(file))) {
                                        arrayList.add(file);
                                    } else {
                                        FileUtil.deleteFileSafely(file);
                                        if (file.exists()) {
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("Failed to delete corrupted lib file '");
                                            sb.append(file.getPath());
                                            sb.append(str2);
                                            SplitLog.w(str3, sb.toString(), new Object[i3]);
                                        }
                                    }
                                }
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("Extraction is needed for lib: ");
                                sb2.append(file.getAbsolutePath());
                                SplitLog.i(str3, sb2.toString(), new Object[i3]);
                                File splitTmpDir = SplitPathManager.require().getSplitTmpDir();
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("tmp-");
                                sb3.append(substring);
                                File createTempFile = File.createTempFile(sb3.toString(), "", splitTmpDir);
                                int i5 = i3;
                                int i6 = i5;
                                while (i5 < 3 && i == 0) {
                                    int i7 = i5 + 1;
                                    try {
                                        FileOutputStream fileOutputStream = new FileOutputStream(createTempFile);
                                        try {
                                            FileUtil.copyFile(zipFile.getInputStream(zipEntry), (OutputStream) fileOutputStream);
                                            if (!createTempFile.renameTo(file)) {
                                                StringBuilder sb4 = new StringBuilder();
                                                sb4.append("Failed to rename \"");
                                                sb4.append(createTempFile.getAbsolutePath());
                                                sb4.append("\" to \"");
                                                sb4.append(file.getAbsolutePath());
                                                sb4.append("\"");
                                                str = format;
                                                try {
                                                    SplitLog.w(str3, sb4.toString(), new Object[0]);
                                                } catch (Throwable th3) {
                                                    th = th3;
                                                }
                                            } else {
                                                str = format;
                                                i = 1;
                                            }
                                            try {
                                                fileOutputStream.close();
                                            } catch (IOException unused) {
                                            } catch (Throwable th4) {
                                                th.addSuppressed(th4);
                                            }
                                            int i8 = i;
                                            StringBuilder sb5 = new StringBuilder();
                                            sb5.append("Extraction ");
                                            sb5.append(i8 == 0 ? "succeeded" : BaseEvent.VALUE_FAILED);
                                            sb5.append(" '");
                                            sb5.append(file.getAbsolutePath());
                                            sb5.append("': length ");
                                            int i9 = i7;
                                            sb5.append(file.length());
                                            SplitLog.i(str3, sb5.toString(), new Object[0]);
                                            md5 = FileUtil.getMD5(file);
                                            if (findLib.getMd5().equals(md5)) {
                                                SplitLog.w(str3, "Failed to check %s md5, excepted %s but %s", substring, findLib.getMd5(), md5);
                                                i6 = 0;
                                            } else {
                                                i6 = i8;
                                            }
                                            if (i6 != 0) {
                                                FileUtil.deleteFileSafely(file);
                                                if (file.exists()) {
                                                    StringBuilder sb6 = new StringBuilder();
                                                    sb6.append("Failed to delete extracted lib that has been corrupted'");
                                                    sb6.append(file.getPath());
                                                    sb6.append(str2);
                                                    SplitLog.w(str3, sb6.toString(), new Object[0]);
                                                }
                                            } else {
                                                arrayList.add(file);
                                            }
                                            i5 = i9;
                                            format = str;
                                        } catch (Throwable th5) {
                                            th = th5;
                                            str = format;
                                            th = th;
                                            throw th;
                                        }
                                    } catch (IOException unused2) {
                                        str = format;
                                        StringBuilder sb7 = new StringBuilder();
                                        sb7.append("Failed to extract so :");
                                        sb7.append(substring);
                                        sb7.append(", attempts times : ");
                                        sb7.append(i7);
                                        SplitLog.w(str3, sb7.toString(), new Object[0]);
                                        int i82 = i;
                                        StringBuilder sb52 = new StringBuilder();
                                        sb52.append("Extraction ");
                                        sb52.append(i82 == 0 ? "succeeded" : BaseEvent.VALUE_FAILED);
                                        sb52.append(" '");
                                        sb52.append(file.getAbsolutePath());
                                        sb52.append("': length ");
                                        int i92 = i7;
                                        sb52.append(file.length());
                                        SplitLog.i(str3, sb52.toString(), new Object[0]);
                                        md5 = FileUtil.getMD5(file);
                                        if (findLib.getMd5().equals(md5)) {
                                        }
                                        if (i6 != 0) {
                                        }
                                        i5 = i92;
                                        format = str;
                                    }
                                }
                                String str4 = format;
                                FileUtil.deleteFileSafely(createTempFile);
                                if (i != 0) {
                                    String str5 = str4;
                                    i2 = 1;
                                    i3 = 0;
                                } else {
                                    StringBuilder sb8 = new StringBuilder();
                                    sb8.append("Could not create lib file ");
                                    sb8.append(file.getAbsolutePath());
                                    sb8.append(")");
                                    throw new IOException(sb8.toString());
                                }
                            } else {
                                throw new IOException(String.format("Failed to find %s in split-info", new Object[]{substring}));
                            }
                        }
                        splitLibExtractor = this;
                    }
                }
            }
        }
        FileUtil.closeQuietly(zipFile);
        return arrayList;
        throw th2;
    }

    public void close() {
        this.lockChannel.close();
        this.lockRaf.close();
        this.cacheLock.release();
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [com.iqiyi.android.qigsaw.core.splitinstall.SplitLibExtractor] */
    /* JADX WARNING: type inference failed for: r3v2, types: [java.util.List] */
    /* JADX WARNING: type inference failed for: r3v3, types: [com.iqiyi.android.qigsaw.core.splitinstall.SplitLibExtractor] */
    /* JADX WARNING: type inference failed for: r3v4, types: [java.util.List] */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r3v6, types: [java.util.List] */
    /* JADX WARNING: type inference failed for: r3v7 */
    /* JADX WARNING: type inference failed for: r3v8 */
    /* JADX WARNING: type inference failed for: r3v9 */
    /* JADX WARNING: type inference failed for: r3v10 */
    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r3v5
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
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public List load(SplitInfo splitInfo, boolean z) {
        if (this.cacheLock.isValid()) {
            String str = TAG;
            if (!z) {
                try {
                    this = this;
                    r3 = loadExistingExtractions(splitInfo.getLibInfo().getLibs());
                    r3 = r3;
                } catch (IOException unused) {
                    SplitLog.w(str, "Failed to reload existing extracted lib files, falling back to fresh extraction", new Object[0]);
                    r3 = this;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("load found ");
                sb.append(r3.size());
                sb.append(" lib files");
                SplitLog.i(str, sb.toString(), new Object[0]);
                return r3;
            }
            r3 = r3.performExtractions(splitInfo);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("load found ");
            sb2.append(r3.size());
            sb2.append(" lib files");
            SplitLog.i(str, sb2.toString(), new Object[0]);
            return r3;
        }
        throw new IllegalStateException("SplitLibExtractor was closed");
    }
}
