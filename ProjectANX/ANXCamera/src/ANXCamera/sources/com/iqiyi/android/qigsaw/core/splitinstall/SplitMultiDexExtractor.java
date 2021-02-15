package com.iqiyi.android.qigsaw.core.splitinstall;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

final class SplitMultiDexExtractor implements Closeable {
    private static final String DEX_PREFIX = "classes";
    private static final String EXTRACTED_NAME_EXT = ".classes";
    private static final String KEY_CRC = "crc";
    private static final String KEY_DEX_CRC = "dex.crc.";
    private static final String KEY_DEX_NUMBER = "dex.number";
    private static final String KEY_DEX_TIME = "dex.time.";
    private static final String KEY_TIME_STAMP = "timestamp";
    private static final String LOCK_FILENAME = "SplitMultiDex.lock";
    private static final long NO_VALUE = -1;
    private static final String PREFS_FILE = "split.multidex.version";
    private static final String TAG = "Split:MultiDexExtractor";
    private final FileLock cacheLock;
    private final File dexDir;
    private final FileChannel lockChannel;
    private final RandomAccessFile lockRaf;
    private final File sourceApk;
    private final long sourceCrc;

    class CentralDirectory {
        long offset;
        long size;

        CentralDirectory() {
        }
    }

    class ExtractedDex extends File {
        long crc = 1;

        ExtractedDex(File file, String str) {
            super(file, str);
        }
    }

    final class ZipCrcUtil {
        ZipCrcUtil() {
        }

        private static long computeCrcOfCentralDir(RandomAccessFile randomAccessFile, CentralDirectory centralDirectory) {
            CRC32 crc32 = new CRC32();
            long j = centralDirectory.size;
            randomAccessFile.seek(centralDirectory.offset);
            int min = (int) Math.min(16384, j);
            byte[] bArr = new byte[16384];
            while (true) {
                int read = randomAccessFile.read(bArr, 0, min);
                if (read == -1) {
                    break;
                }
                crc32.update(bArr, 0, read);
                j -= (long) read;
                if (j == 0) {
                    break;
                }
                min = (int) Math.min(16384, j);
            }
            return crc32.getValue();
        }

        private static CentralDirectory findCentralDirectory(RandomAccessFile randomAccessFile) {
            long length = randomAccessFile.length() - 22;
            long j = 0;
            if (length >= 0) {
                long j2 = length - 65536;
                if (j2 >= 0) {
                    j = j2;
                }
                int reverseBytes = Integer.reverseBytes(101010256);
                while (true) {
                    randomAccessFile.seek(length);
                    if (randomAccessFile.readInt() == reverseBytes) {
                        randomAccessFile.skipBytes(2);
                        randomAccessFile.skipBytes(2);
                        randomAccessFile.skipBytes(2);
                        randomAccessFile.skipBytes(2);
                        CentralDirectory centralDirectory = new CentralDirectory();
                        centralDirectory.size = ((long) Integer.reverseBytes(randomAccessFile.readInt())) & 4294967295L;
                        centralDirectory.offset = ((long) Integer.reverseBytes(randomAccessFile.readInt())) & 4294967295L;
                        return centralDirectory;
                    }
                    length--;
                    if (length < j) {
                        throw new ZipException("End Of Central Directory signature not found");
                    }
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("File too short to be a zip file: ");
                sb.append(randomAccessFile.length());
                throw new ZipException(sb.toString());
            }
        }

        static long getZipCrc(File file) {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            try {
                return computeCrcOfCentralDir(randomAccessFile, findCentralDirectory(randomAccessFile));
            } finally {
                randomAccessFile.close();
            }
        }
    }

    SplitMultiDexExtractor(File file, File file2) {
        StringBuilder sb = new StringBuilder();
        sb.append("SplitMultiDexExtractor(");
        sb.append(file.getPath());
        sb.append(", ");
        sb.append(file2.getPath());
        sb.append(")");
        String sb2 = sb.toString();
        Object[] objArr = new Object[0];
        String str = TAG;
        SplitLog.i(str, sb2, objArr);
        this.sourceApk = file;
        this.dexDir = file2;
        this.sourceCrc = getZipCrc(file);
        File file3 = new File(file2, LOCK_FILENAME);
        this.lockRaf = new RandomAccessFile(file3, "rw");
        try {
            this.lockChannel = this.lockRaf.getChannel();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Blocking on lock ");
            sb3.append(file3.getPath());
            SplitLog.i(str, sb3.toString(), new Object[0]);
            this.cacheLock = this.lockChannel.lock();
            StringBuilder sb4 = new StringBuilder();
            sb4.append(file3.getPath());
            sb4.append(" locked");
            SplitLog.i(str, sb4.toString(), new Object[0]);
        } catch (IOException | Error | RuntimeException e) {
            closeQuietly(this.lockChannel);
            throw e;
        } catch (IOException | Error | RuntimeException e2) {
            closeQuietly(this.lockRaf);
            throw e2;
        }
    }

    private void clearDexDir() {
        File[] listFiles = this.dexDir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return !file.getName().equals(SplitMultiDexExtractor.LOCK_FILENAME);
            }
        });
        String str = TAG;
        if (listFiles == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to list secondary dex dir content (");
            sb.append(this.dexDir.getPath());
            sb.append(").");
            SplitLog.w(str, sb.toString(), new Object[0]);
            return;
        }
        for (File file : listFiles) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Trying to delete old file ");
            sb2.append(file.getPath());
            sb2.append(" of size ");
            sb2.append(file.length());
            SplitLog.i(str, sb2.toString(), new Object[0]);
            if (!file.delete()) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Failed to delete old file ");
                sb3.append(file.getPath());
                SplitLog.w(str, sb3.toString(), new Object[0]);
            } else {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Deleted old file ");
                sb4.append(file.getPath());
                SplitLog.i(str, sb4.toString(), new Object[0]);
            }
        }
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            SplitLog.w(TAG, "Failed to close resource", (Throwable) e);
        }
    }

    private static void extract(ZipFile zipFile, ZipEntry zipEntry, File file, String str) {
        ZipOutputStream zipOutputStream;
        InputStream inputStream = zipFile.getInputStream(zipEntry);
        StringBuilder sb = new StringBuilder();
        sb.append("tmp-");
        sb.append(str);
        File createTempFile = File.createTempFile(sb.toString(), ".zip", file.getParentFile());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Extracting ");
        sb2.append(createTempFile.getPath());
        String sb3 = sb2.toString();
        Object[] objArr = new Object[0];
        String str2 = TAG;
        SplitLog.i(str2, sb3, objArr);
        try {
            zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(createTempFile)));
            ZipEntry zipEntry2 = new ZipEntry("classes.dex");
            zipEntry2.setTime(zipEntry.getTime());
            zipOutputStream.putNextEntry(zipEntry2);
            byte[] bArr = new byte[16384];
            while (true) {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                zipOutputStream.write(bArr, 0, read);
            }
            zipOutputStream.closeEntry();
            closeQuietly(zipOutputStream);
            if (createTempFile.setReadOnly()) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Renaming to ");
                sb4.append(file.getPath());
                SplitLog.i(str2, sb4.toString(), new Object[0]);
                if (createTempFile.renameTo(file)) {
                    closeQuietly(inputStream);
                    createTempFile.delete();
                    return;
                }
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Failed to rename \"");
                sb5.append(createTempFile.getAbsolutePath());
                sb5.append("\" to \"");
                sb5.append(file.getAbsolutePath());
                sb5.append("\"");
                throw new IOException(sb5.toString());
            }
            StringBuilder sb6 = new StringBuilder();
            sb6.append("Failed to mark readonly \"");
            sb6.append(createTempFile.getAbsolutePath());
            sb6.append("\" (tmp of \"");
            sb6.append(file.getAbsolutePath());
            sb6.append("\")");
            throw new IOException(sb6.toString());
        } catch (Throwable th) {
            closeQuietly(inputStream);
            createTempFile.delete();
            throw th;
        }
    }

    private static SharedPreferences getMultiDexPreferences(Context context) {
        return context.getSharedPreferences(PREFS_FILE, 4);
    }

    private static long getTimeStamp(File file) {
        long lastModified = file.lastModified();
        return lastModified == 1 ? lastModified - 1 : lastModified;
    }

    private static long getZipCrc(File file) {
        long zipCrc = ZipCrcUtil.getZipCrc(file);
        return zipCrc == 1 ? zipCrc - 1 : zipCrc;
    }

    private static boolean isModified(Context context, File file, long j, String str) {
        SharedPreferences multiDexPreferences = getMultiDexPreferences(context);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(KEY_TIME_STAMP);
        if (multiDexPreferences.getLong(sb.toString(), 1) == getTimeStamp(file)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(KEY_CRC);
            if (multiDexPreferences.getLong(sb2.toString(), 1) == j) {
                return false;
            }
        }
        return true;
    }

    private List loadExistingExtractions(Context context, String str) {
        String str2 = str;
        int i = 0;
        Object[] objArr = new Object[0];
        String str3 = TAG;
        SplitLog.i(str3, "loading existing secondary dex files", objArr);
        StringBuilder sb = new StringBuilder();
        sb.append(this.sourceApk.getName());
        sb.append(EXTRACTED_NAME_EXT);
        String sb2 = sb.toString();
        SharedPreferences multiDexPreferences = getMultiDexPreferences(context);
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str2);
        sb3.append(KEY_DEX_NUMBER);
        int i2 = multiDexPreferences.getInt(sb3.toString(), 1);
        ArrayList arrayList = new ArrayList(i2 - 1);
        int i3 = 2;
        while (i3 <= i2) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(sb2);
            sb4.append(i3);
            sb4.append(".zip");
            ExtractedDex extractedDex = new ExtractedDex(this.dexDir, sb4.toString());
            if (extractedDex.isFile()) {
                extractedDex.crc = getZipCrc(extractedDex);
                StringBuilder sb5 = new StringBuilder();
                sb5.append(str2);
                sb5.append(KEY_DEX_CRC);
                sb5.append(i3);
                long j = multiDexPreferences.getLong(sb5.toString(), -1);
                StringBuilder sb6 = new StringBuilder();
                sb6.append(str2);
                sb6.append(KEY_DEX_TIME);
                sb6.append(i3);
                long j2 = multiDexPreferences.getLong(sb6.toString(), -1);
                String str4 = sb2;
                long lastModified = extractedDex.lastModified();
                if (j2 == lastModified) {
                    SharedPreferences sharedPreferences = multiDexPreferences;
                    int i4 = i2;
                    if (j == extractedDex.crc) {
                        arrayList.add(extractedDex);
                        i3++;
                        multiDexPreferences = sharedPreferences;
                        sb2 = str4;
                        i2 = i4;
                        i = 0;
                    }
                }
                StringBuilder sb7 = new StringBuilder();
                sb7.append("Invalid extracted dex: ");
                sb7.append(extractedDex);
                sb7.append(" (key \"");
                sb7.append(str2);
                sb7.append("\"), expected modification time: ");
                sb7.append(j2);
                sb7.append(", modification time: ");
                sb7.append(lastModified);
                sb7.append(", expected crc: ");
                sb7.append(j);
                sb7.append(", file crc: ");
                sb7.append(extractedDex.crc);
                throw new IOException(sb7.toString());
            }
            StringBuilder sb8 = new StringBuilder();
            sb8.append("Missing extracted secondary dex file '");
            sb8.append(extractedDex.getPath());
            sb8.append("'");
            throw new IOException(sb8.toString());
        }
        SplitLog.i(str3, "Existing secondary dex files loaded", new Object[i]);
        return arrayList;
    }

    private List performExtractions() {
        boolean z;
        ExtractedDex extractedDex;
        boolean z2;
        int i;
        boolean z3;
        String str = SplitConstants.DOT_DEX;
        String str2 = "Failed to close resource";
        String str3 = DEX_PREFIX;
        String str4 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append(this.sourceApk.getName());
        sb.append(EXTRACTED_NAME_EXT);
        String sb2 = sb.toString();
        clearDexDir();
        ArrayList arrayList = new ArrayList();
        ZipFile zipFile = new ZipFile(this.sourceApk);
        try {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str3);
            sb3.append(2);
            sb3.append(str);
            ZipEntry entry = zipFile.getEntry(sb3.toString());
            int i2 = 2;
            while (entry != null) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(sb2);
                sb4.append(i2);
                sb4.append(".zip");
                extractedDex = new ExtractedDex(this.dexDir, sb4.toString());
                arrayList.add(extractedDex);
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Extraction is needed for file ");
                sb5.append(extractedDex);
                boolean z4 = false;
                SplitLog.i(str4, sb5.toString(), new Object[0]);
                th = 0;
                while (i < 3 && !z2) {
                    int i3 = i + 1;
                    extract(zipFile, entry, extractedDex, sb2);
                    extractedDex.crc = getZipCrc(extractedDex);
                    boolean z5 = true;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("Extraction ");
                    sb6.append(z5 ? "succeeded" : BaseEvent.VALUE_FAILED);
                    sb6.append(" '");
                    sb6.append(extractedDex.getAbsolutePath());
                    sb6.append("': length ");
                    int i4 = i3;
                    sb6.append(extractedDex.length());
                    sb6.append(" - crc: ");
                    sb6.append(extractedDex.crc);
                    SplitLog.i(str4, sb6.toString(), new Object[0]);
                    if (!z5) {
                        extractedDex.delete();
                        if (extractedDex.exists()) {
                            StringBuilder sb7 = new StringBuilder();
                            sb7.append("Failed to delete corrupted secondary dex '");
                            sb7.append(extractedDex.getPath());
                            sb7.append("'");
                            z3 = false;
                            SplitLog.w(str4, sb7.toString(), new Object[0]);
                            z4 = z3;
                            z2 = z5;
                            i = i4;
                        }
                    }
                    z3 = false;
                    z4 = z3;
                    z2 = z5;
                    i = i4;
                }
                if (z2) {
                    i2++;
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(str3);
                    sb8.append(i2);
                    sb8.append(str);
                    entry = zipFile.getEntry(sb8.toString());
                } else {
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append("Could not create zip file ");
                    sb9.append(extractedDex.getAbsolutePath());
                    sb9.append(" for secondary dex (");
                    sb9.append(i2);
                    sb9.append(")");
                    throw new IOException(sb9.toString());
                }
            }
            try {
                zipFile.close();
            } catch (IOException e) {
                SplitLog.w(str4, str2, (Throwable) e);
            }
            return arrayList;
        } catch (IOException e2) {
            StringBuilder sb10 = new StringBuilder();
            sb10.append("Failed to read crc from ");
            sb10.append(extractedDex.getAbsolutePath());
            SplitLog.w(str4, sb10.toString(), (Throwable) e2);
        } finally {
            z = th;
            try {
                zipFile.close();
            } catch (IOException e3) {
                SplitLog.w(str4, str2, (Throwable) e3);
            }
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.iqiyi.android.qigsaw.core.splitinstall.SplitMultiDexExtractor$ExtractedDex>, for r8v0, types: [java.util.List, java.util.List<com.iqiyi.android.qigsaw.core.splitinstall.SplitMultiDexExtractor$ExtractedDex>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void putStoredApkInfo(Context context, String str, long j, long j2, List<ExtractedDex> list) {
        Editor edit = getMultiDexPreferences(context).edit();
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(KEY_TIME_STAMP);
        edit.putLong(sb.toString(), j);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(KEY_CRC);
        edit.putLong(sb2.toString(), j2);
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str);
        sb3.append(KEY_DEX_NUMBER);
        edit.putInt(sb3.toString(), list.size() + 1);
        int i = 2;
        for (ExtractedDex extractedDex : list) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str);
            sb4.append(KEY_DEX_CRC);
            sb4.append(i);
            edit.putLong(sb4.toString(), extractedDex.crc);
            StringBuilder sb5 = new StringBuilder();
            sb5.append(str);
            sb5.append(KEY_DEX_TIME);
            sb5.append(i);
            edit.putLong(sb5.toString(), extractedDex.lastModified());
            i++;
        }
        edit.apply();
    }

    public void close() {
        this.cacheLock.release();
        this.lockChannel.close();
        this.lockRaf.close();
    }

    /* JADX WARNING: type inference failed for: r11v0, types: [com.iqiyi.android.qigsaw.core.splitinstall.SplitMultiDexExtractor] */
    /* JADX WARNING: type inference failed for: r14v1, types: [java.util.List] */
    /* JADX WARNING: type inference failed for: r11v4, types: [com.iqiyi.android.qigsaw.core.splitinstall.SplitMultiDexExtractor] */
    /* JADX WARNING: type inference failed for: r14v2, types: [java.util.List] */
    /* JADX WARNING: type inference failed for: r10v0, types: [java.util.List] */
    /* JADX WARNING: type inference failed for: r11v5 */
    /* JADX WARNING: type inference failed for: r11v6, types: [java.util.List] */
    /* JADX WARNING: type inference failed for: r14v7 */
    /* JADX WARNING: type inference failed for: r11v7 */
    /* JADX WARNING: type inference failed for: r14v8 */
    /* JADX WARNING: type inference failed for: r11v8 */
    /* JADX WARNING: type inference failed for: r11v9 */
    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r11v5
  assigns: []
  uses: []
  mth insns count: 63
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
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public List load(Context context, String str, boolean z) {
        ? r14;
        Object[] objArr;
        String str2;
        StringBuilder sb = new StringBuilder();
        sb.append("SplitMultiDexExtractor.load(");
        sb.append(this.sourceApk.getPath());
        String str3 = ", ";
        sb.append(str3);
        sb.append(z);
        sb.append(str3);
        sb.append(str);
        sb.append(")");
        String sb2 = sb.toString();
        Object[] objArr2 = new Object[0];
        String str4 = TAG;
        SplitLog.i(str4, sb2, objArr2);
        if (this.cacheLock.isValid()) {
            if (z || isModified(context, this.sourceApk, this.sourceCrc, str)) {
                if (z) {
                    objArr = new Object[0];
                    str2 = "Forced extraction must be performed.";
                } else {
                    objArr = new Object[0];
                    str2 = "Detected that extraction must be performed.";
                }
                SplitLog.i(str4, str2, objArr);
            } else {
                try {
                    this = this;
                    this = loadExistingExtractions(context, str);
                    r11 = this;
                    r14 = this;
                } catch (IOException e) {
                    SplitLog.w(str4, "Failed to reload existing extracted secondary dex files, falling back to fresh extraction", (Throwable) e);
                    r11 = r11;
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append("load found ");
                sb3.append(r14.size());
                sb3.append(" secondary dex files");
                SplitLog.i(str4, sb3.toString(), new Object[0]);
                return r14;
            }
            ? performExtractions = r11.performExtractions();
            putStoredApkInfo(context, str, getTimeStamp(r11.sourceApk), r11.sourceCrc, performExtractions);
            r14 = performExtractions;
            StringBuilder sb32 = new StringBuilder();
            sb32.append("load found ");
            sb32.append(r14.size());
            sb32.append(" secondary dex files");
            SplitLog.i(str4, sb32.toString(), new Object[0]);
            return r14;
        }
        throw new IllegalStateException("SplitMultiDexExtractor was closed");
    }
}
