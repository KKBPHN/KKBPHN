package com.miui.internal.server;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;
import com.android.camera.module.loader.FunctionParseBeautyBodySlimCount;
import com.google.android.apps.photos.api.PhotosOemApi;
import com.miui.internal.server.IDropBoxManagerService.Stub;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.GZIPOutputStream;
import miui.os.DropBoxManager;
import miui.os.DropBoxManager.Entry;
import miui.os.FileUtils;
import miui.util.AppConstants;
import miui.util.SoftReferenceSingleton;

public final class DropBoxManagerService extends Stub {
    private static final int DEFAULT_AGE_SECONDS = 259200;
    private static final int DEFAULT_MAX_FILES = 1000;
    private static final int DEFAULT_QUOTA_KB = 5120;
    private static final int DEFAULT_QUOTA_PERCENT = 10;
    private static final int DEFAULT_RESERVE_PERCENT = 10;
    private static final String DROPBOX_TAG_PREFIX = "dropbox:";
    private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
        /* access modifiers changed from: protected */
        public DropBoxManagerService createInstance() {
            return new DropBoxManagerService();
        }
    };
    private static final int MSG_SEND_BROADCAST = 1;
    private static final boolean PROFILE_DUMP = false;
    private static final int QUOTA_RESCAN_MILLIS = 5000;
    public static final String SERVICE_NAME = "DropBoxManagerService";
    private static final String TAG = "DropBoxManagerService";
    private FileList mAllFiles;
    private int mBlockSize;
    private int mCachedQuotaBlocks;
    private long mCachedQuotaUptimeMillis;
    /* access modifiers changed from: private */
    public Context mContext;
    private File mDropBoxDir;
    private HashMap mFilesByTag;
    private Handler mHandler;
    private StatFs mStatFs;

    final class EntryFile implements Comparable {
        public final int blocks;
        public final File file;
        public final int flags;
        public final String tag;
        public final long timestampMillis;

        public EntryFile(long j) {
            this.tag = null;
            this.timestampMillis = j;
            this.flags = 1;
            this.file = null;
            this.blocks = 0;
        }

        public EntryFile(File file2, int i) {
            int i2;
            int length;
            this.file = file2;
            long j = (long) i;
            this.blocks = (int) (((this.file.length() + j) - 1) / j);
            String name = file2.getName();
            int lastIndexOf = name.lastIndexOf(64);
            long j2 = 0;
            if (lastIndexOf < 0) {
                this.tag = null;
                this.timestampMillis = 0;
                this.flags = 1;
                return;
            }
            int i3 = 0;
            this.tag = Uri.decode(name.substring(0, lastIndexOf));
            if (name.endsWith(".gz")) {
                name = name.substring(0, name.length() - 3);
                i3 = 4;
            }
            if (name.endsWith(".lost")) {
                i3 |= 1;
                i2 = lastIndexOf + 1;
                length = name.length() - 5;
            } else {
                if (name.endsWith(".txt")) {
                    i3 |= 2;
                } else if (!name.endsWith(".dat")) {
                    this.flags = 1;
                    this.timestampMillis = 0;
                    return;
                }
                i2 = lastIndexOf + 1;
                length = name.length() - 4;
            }
            String substring = name.substring(i2, length);
            this.flags = i3;
            try {
                j2 = Long.valueOf(substring).longValue();
            } catch (NumberFormatException unused) {
            }
            this.timestampMillis = j2;
        }

        public EntryFile(File file2, File file3, String str, long j, int i, int i2) {
            if ((i & 1) == 0) {
                this.tag = str;
                this.timestampMillis = j;
                this.flags = i;
                StringBuilder sb = new StringBuilder();
                sb.append(Uri.encode(str));
                sb.append("@");
                sb.append(j);
                sb.append((i & 2) != 0 ? ".txt" : ".dat");
                sb.append((i & 4) != 0 ? ".gz" : "");
                this.file = new File(file3, sb.toString());
                if (file2.renameTo(this.file)) {
                    long j2 = (long) i2;
                    this.blocks = (int) (((this.file.length() + j2) - 1) / j2);
                    return;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Can't rename ");
                sb2.append(file2);
                sb2.append(" to ");
                sb2.append(this.file);
                throw new IOException(sb2.toString());
            }
            throw new IllegalArgumentException();
        }

        public EntryFile(File file2, String str, long j) {
            this.tag = str;
            this.timestampMillis = j;
            this.flags = 1;
            StringBuilder sb = new StringBuilder();
            sb.append(Uri.encode(str));
            sb.append("@");
            sb.append(j);
            sb.append(".lost");
            this.file = new File(file2, sb.toString());
            this.blocks = 0;
            new FileOutputStream(this.file).close();
        }

        public final int compareTo(EntryFile entryFile) {
            long j = this.timestampMillis;
            long j2 = entryFile.timestampMillis;
            if (j < j2) {
                return -1;
            }
            if (j > j2) {
                return 1;
            }
            File file2 = this.file;
            if (file2 != null) {
                File file3 = entryFile.file;
                if (file3 != null) {
                    return file2.compareTo(file3);
                }
            }
            if (entryFile.file != null) {
                return -1;
            }
            if (this.file != null) {
                return 1;
            }
            if (this == entryFile) {
                return 0;
            }
            if (hashCode() < entryFile.hashCode()) {
                return -1;
            }
            return hashCode() > entryFile.hashCode() ? 1 : 0;
        }
    }

    final class FileList implements Comparable {
        public int blocks;
        public final TreeSet contents;

        private FileList() {
            this.blocks = 0;
            this.contents = new TreeSet();
        }

        public final int compareTo(FileList fileList) {
            int i = this.blocks;
            int i2 = fileList.blocks;
            if (i != i2) {
                return i2 - i;
            }
            if (this == fileList) {
                return 0;
            }
            if (hashCode() < fileList.hashCode()) {
                return -1;
            }
            return hashCode() > fileList.hashCode() ? 1 : 0;
        }
    }

    private DropBoxManagerService() {
        this.mAllFiles = null;
        this.mFilesByTag = null;
        this.mStatFs = null;
        this.mBlockSize = 0;
        this.mCachedQuotaBlocks = 0;
        this.mCachedQuotaUptimeMillis = 0;
        this.mContext = AppConstants.getCurrentApplication();
        StringBuilder sb = new StringBuilder();
        sb.append(this.mContext.getFilesDir().getAbsolutePath());
        sb.append(File.separator);
        sb.append("dropbox");
        this.mDropBoxDir = new File(sb.toString());
        this.mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message message) {
                if (message.what == 1) {
                    DropBoxManagerService.this.mContext.sendBroadcast((Intent) message.obj, "miui.permission.READ_LOGS");
                }
            }
        };
        initAndTrimAsync();
    }

    private synchronized long createEntry(File file, String str, int i) {
        long j;
        EntryFile entryFile;
        synchronized (this) {
            long currentTimeMillis = System.currentTimeMillis();
            SortedSet tailSet = this.mAllFiles.contents.tailSet(new EntryFile(FunctionParseBeautyBodySlimCount.TIP_INTERVAL_TIME + currentTimeMillis));
            EntryFile[] entryFileArr = null;
            if (!tailSet.isEmpty()) {
                entryFileArr = (EntryFile[]) tailSet.toArray(new EntryFile[tailSet.size()]);
                tailSet.clear();
            }
            if (!this.mAllFiles.contents.isEmpty()) {
                currentTimeMillis = Math.max(currentTimeMillis, ((EntryFile) this.mAllFiles.contents.last()).timestampMillis + 1);
            }
            if (entryFileArr != null) {
                int length = entryFileArr.length;
                j = currentTimeMillis;
                for (int i2 = 0; i2 < length; i2++) {
                    EntryFile entryFile2 = entryFileArr[i2];
                    this.mAllFiles.blocks -= entryFile2.blocks;
                    FileList fileList = (FileList) this.mFilesByTag.get(entryFile2.tag);
                    if (fileList != null && fileList.contents.remove(entryFile2)) {
                        fileList.blocks -= entryFile2.blocks;
                    }
                    if ((entryFile2.flags & 1) == 0) {
                        long j2 = j + 1;
                        EntryFile entryFile3 = new EntryFile(entryFile2.file, this.mDropBoxDir, entryFile2.tag, j, entryFile2.flags, this.mBlockSize);
                        enrollEntry(entryFile3);
                        j = j2;
                    } else {
                        long j3 = j + 1;
                        enrollEntry(new EntryFile(this.mDropBoxDir, entryFile2.tag, j));
                        j = j3;
                    }
                }
            } else {
                j = currentTimeMillis;
            }
            if (file == null) {
                entryFile = new EntryFile(this.mDropBoxDir, str, j);
            } else {
                String str2 = str;
                entryFile = new EntryFile(file, this.mDropBoxDir, str, j, i, this.mBlockSize);
            }
            enrollEntry(entryFile);
        }
        return j;
    }

    private synchronized void enrollEntry(EntryFile entryFile) {
        this.mAllFiles.contents.add(entryFile);
        this.mAllFiles.blocks += entryFile.blocks;
        if (!(entryFile.tag == null || entryFile.file == null || entryFile.blocks <= 0)) {
            FileList fileList = (FileList) this.mFilesByTag.get(entryFile.tag);
            if (fileList == null) {
                fileList = new FileList();
                this.mFilesByTag.put(entryFile.tag, fileList);
            }
            fileList.contents.add(entryFile);
            fileList.blocks += entryFile.blocks;
        }
    }

    public static DropBoxManagerService getInstance() {
        return (DropBoxManagerService) INSTANCE.get();
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't wrap try/catch for region: R(7:4|(2:6|(1:8)(2:9|10))|11|12|13|14|15) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0045 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void init() {
        if (this.mStatFs == null) {
            if (!this.mDropBoxDir.isDirectory()) {
                if (!this.mDropBoxDir.mkdirs()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Can't mkdir: ");
                    sb.append(this.mDropBoxDir);
                    throw new IOException(sb.toString());
                }
            }
            this.mStatFs = new StatFs(this.mDropBoxDir.getPath());
            this.mBlockSize = this.mStatFs.getBlockSize();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Can't statfs: ");
            sb2.append(this.mDropBoxDir);
            throw new IOException(sb2.toString());
        }
        if (this.mAllFiles == null) {
            File[] listFiles = this.mDropBoxDir.listFiles();
            if (listFiles != null) {
                this.mAllFiles = new FileList();
                this.mFilesByTag = new HashMap();
                for (File file : listFiles) {
                    if (file.getName().endsWith(".tmp")) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Cleaning temp file: ");
                        sb3.append(file);
                        Log.i("DropBoxManagerService", sb3.toString());
                    } else {
                        EntryFile entryFile = new EntryFile(file, this.mBlockSize);
                        if (entryFile.tag == null) {
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("Unrecognized file: ");
                            sb4.append(file);
                            Log.w("DropBoxManagerService", sb4.toString());
                        } else if (entryFile.timestampMillis == 0) {
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("Invalid filename: ");
                            sb5.append(file);
                            Log.w("DropBoxManagerService", sb5.toString());
                        } else {
                            enrollEntry(entryFile);
                        }
                    }
                    file.delete();
                }
            } else {
                StringBuilder sb6 = new StringBuilder();
                sb6.append("Can't list files: ");
                sb6.append(this.mDropBoxDir);
                throw new IOException(sb6.toString());
            }
        }
    }

    private void initAndTrimAsync() {
        this.mCachedQuotaUptimeMillis = 0;
        new Thread() {
            public void run() {
                try {
                    DropBoxManagerService.this.init();
                    DropBoxManagerService.this.trimToFit();
                } catch (IOException e) {
                    Log.e("DropBoxManagerService", "Can't init", e);
                }
            }
        }.start();
    }

    public static void onReceive(Context context, Intent intent) {
        if (Receiver.isActionEquals(intent, "android.intent.action.DEVICE_STORAGE_LOW")) {
            getInstance().initAndTrimAsync();
        }
    }

    /* access modifiers changed from: private */
    public synchronized long trimToFit() {
        long currentTimeMillis = System.currentTimeMillis() - ((long) 259200000);
        while (true) {
            if (this.mAllFiles.contents.isEmpty()) {
                break;
            }
            EntryFile entryFile = (EntryFile) this.mAllFiles.contents.first();
            if (entryFile.timestampMillis > currentTimeMillis && this.mAllFiles.contents.size() < 1000) {
                break;
            }
            FileList fileList = (FileList) this.mFilesByTag.get(entryFile.tag);
            if (fileList != null && fileList.contents.remove(entryFile)) {
                fileList.blocks -= entryFile.blocks;
            }
            if (this.mAllFiles.contents.remove(entryFile)) {
                this.mAllFiles.blocks -= entryFile.blocks;
            }
            if (entryFile.file != null) {
                entryFile.file.delete();
            }
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        int i = 0;
        if (uptimeMillis > this.mCachedQuotaUptimeMillis + 5000) {
            this.mStatFs.restat(this.mDropBoxDir.getPath());
            this.mCachedQuotaBlocks = Math.min(5242880 / this.mBlockSize, Math.max(0, ((this.mStatFs.getAvailableBlocks() - ((this.mStatFs.getBlockCount() * 10) / 100)) * 10) / 100));
            this.mCachedQuotaUptimeMillis = uptimeMillis;
        }
        if (this.mAllFiles.blocks > this.mCachedQuotaBlocks) {
            int i2 = this.mAllFiles.blocks;
            TreeSet treeSet = new TreeSet(this.mFilesByTag.values());
            Iterator it = treeSet.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                FileList fileList2 = (FileList) it.next();
                if (i > 0 && fileList2.blocks <= (this.mCachedQuotaBlocks - i2) / i) {
                    break;
                }
                i2 -= fileList2.blocks;
                i++;
            }
            int i3 = (this.mCachedQuotaBlocks - i2) / i;
            Iterator it2 = treeSet.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                FileList fileList3 = (FileList) it2.next();
                if (this.mAllFiles.blocks < this.mCachedQuotaBlocks) {
                    break;
                }
                while (fileList3.blocks > i3 && !fileList3.contents.isEmpty()) {
                    EntryFile entryFile2 = (EntryFile) fileList3.contents.first();
                    if (fileList3.contents.remove(entryFile2)) {
                        fileList3.blocks -= entryFile2.blocks;
                    }
                    if (this.mAllFiles.contents.remove(entryFile2)) {
                        this.mAllFiles.blocks -= entryFile2.blocks;
                    }
                    try {
                        if (entryFile2.file != null) {
                            entryFile2.file.delete();
                        }
                        enrollEntry(new EntryFile(this.mDropBoxDir, entryFile2.tag, entryFile2.timestampMillis));
                    } catch (IOException e) {
                        Log.e("DropBoxManagerService", "Can't write tombstone file", e);
                    }
                }
            }
        }
        return ((long) this.mCachedQuotaBlocks) * ((long) this.mBlockSize);
    }

    /* JADX WARNING: Removed duplicated region for block: B:103:0x0179 A[SYNTHETIC, Splitter:B:103:0x0179] */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x017e A[SYNTHETIC, Splitter:B:107:0x017e] */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x0186  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0165 A[SYNTHETIC, Splitter:B:90:0x0165] */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x016a A[SYNTHETIC, Splitter:B:94:0x016a] */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x0172  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void add(Entry entry) {
        File file;
        InputStream inputStream;
        FilterOutputStream filterOutputStream;
        FilterOutputStream filterOutputStream2;
        FilterOutputStream filterOutputStream3;
        FilterOutputStream filterOutputStream4;
        FilterOutputStream filterOutputStream5;
        long createEntry;
        String str = "DropBoxManagerService";
        String tag = entry.getTag();
        try {
            int flags = entry.getFlags();
            if ((flags & 1) == 0) {
                init();
                if (!isTagEnabled(tag)) {
                    entry.close();
                    return;
                }
                long trimToFit = trimToFit();
                long currentTimeMillis = System.currentTimeMillis();
                byte[] bArr = new byte[this.mBlockSize];
                inputStream = entry.getInputStream();
                int i = 0;
                while (true) {
                    try {
                        if (i >= bArr.length) {
                            break;
                        }
                        int read = inputStream.read(bArr, i, bArr.length - i);
                        if (read <= 0) {
                            break;
                        }
                        i += read;
                    } catch (IOException e) {
                        e = e;
                        filterOutputStream3 = null;
                        file = null;
                        try {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Can't write: ");
                            sb.append(tag);
                            Log.e(str, sb.toString(), e);
                            if (filterOutputStream != null) {
                            }
                            if (inputStream != null) {
                            }
                            entry.close();
                            if (file != null) {
                            }
                        } catch (Throwable th) {
                            th = th;
                            if (filterOutputStream != null) {
                            }
                            if (inputStream != null) {
                            }
                            entry.close();
                            if (file != null) {
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        filterOutputStream4 = null;
                        file = null;
                        if (filterOutputStream != null) {
                        }
                        if (inputStream != null) {
                        }
                        entry.close();
                        if (file != null) {
                        }
                        throw th;
                    }
                }
                File file2 = this.mDropBoxDir;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("drop");
                int i2 = i;
                sb2.append(Thread.currentThread().getId());
                sb2.append(".tmp");
                File file3 = new File(file2, sb2.toString());
                try {
                    int i3 = this.mBlockSize;
                    if (i3 > 4096) {
                        i3 = 4096;
                    }
                    if (i3 < 512) {
                        i3 = 512;
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file3);
                    FilterOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, i3);
                    try {
                        int i4 = i2;
                        if (i4 == bArr.length && (flags & 4) == 0) {
                            filterOutputStream5 = new GZIPOutputStream(bufferedOutputStream);
                            flags |= 4;
                        } else {
                            filterOutputStream5 = bufferedOutputStream;
                        }
                        while (true) {
                            try {
                                filterOutputStream5.write(bArr, 0, i4);
                                long currentTimeMillis2 = System.currentTimeMillis();
                                if (currentTimeMillis2 - currentTimeMillis > 30000) {
                                    trimToFit = trimToFit();
                                    currentTimeMillis = currentTimeMillis2;
                                }
                                i4 = inputStream.read(bArr);
                                if (i4 <= 0) {
                                    FileUtils.sync(fileOutputStream);
                                    filterOutputStream5.close();
                                    filterOutputStream5 = null;
                                } else {
                                    filterOutputStream5.flush();
                                }
                                if (file3.length() <= trimToFit) {
                                    if (i4 <= 0) {
                                        break;
                                    }
                                } else {
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("Dropping: ");
                                    sb3.append(tag);
                                    sb3.append(" (");
                                    sb3.append(file3.length());
                                    sb3.append(" > ");
                                    sb3.append(trimToFit);
                                    sb3.append(" bytes)");
                                    Log.w(str, sb3.toString());
                                    file3.delete();
                                    file3 = null;
                                    break;
                                }
                            } catch (IOException e2) {
                                e = e2;
                                file = file3;
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("Can't write: ");
                                sb4.append(tag);
                                Log.e(str, sb4.toString(), e);
                                if (filterOutputStream != null) {
                                    try {
                                        filterOutputStream.close();
                                    } catch (IOException unused) {
                                    }
                                }
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (IOException unused2) {
                                    }
                                }
                                entry.close();
                                if (file != null) {
                                    file.delete();
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                file = file3;
                                if (filterOutputStream != null) {
                                    try {
                                        filterOutputStream.close();
                                    } catch (IOException unused3) {
                                    }
                                }
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (IOException unused4) {
                                    }
                                }
                                entry.close();
                                if (file != null) {
                                    file.delete();
                                }
                                throw th;
                            }
                        }
                        createEntry = createEntry(file3, tag, flags);
                    } catch (IOException e3) {
                        e = e3;
                        filterOutputStream2 = bufferedOutputStream;
                        file = file3;
                        StringBuilder sb42 = new StringBuilder();
                        sb42.append("Can't write: ");
                        sb42.append(tag);
                        Log.e(str, sb42.toString(), e);
                        if (filterOutputStream != null) {
                        }
                        if (inputStream != null) {
                        }
                        entry.close();
                        if (file != null) {
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        filterOutputStream = bufferedOutputStream;
                        file = file3;
                        if (filterOutputStream != null) {
                        }
                        if (inputStream != null) {
                        }
                        entry.close();
                        if (file != null) {
                        }
                        throw th;
                    }
                    try {
                        Intent intent = new Intent(DropBoxManager.ACTION_DROPBOX_ENTRY_ADDED);
                        intent.putExtra(DropBoxManager.EXTRA_TAG, tag);
                        intent.putExtra("time", createEntry);
                        this.mHandler.sendMessage(this.mHandler.obtainMessage(1, intent));
                        if (filterOutputStream5 != null) {
                            try {
                                filterOutputStream5.close();
                            } catch (IOException unused5) {
                            }
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException unused6) {
                            }
                        }
                        entry.close();
                    } catch (IOException e4) {
                        e = e4;
                        file = null;
                        StringBuilder sb422 = new StringBuilder();
                        sb422.append("Can't write: ");
                        sb422.append(tag);
                        Log.e(str, sb422.toString(), e);
                        if (filterOutputStream != null) {
                        }
                        if (inputStream != null) {
                        }
                        entry.close();
                        if (file != null) {
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        file = null;
                        if (filterOutputStream != null) {
                        }
                        if (inputStream != null) {
                        }
                        entry.close();
                        if (file != null) {
                        }
                        throw th;
                    }
                } catch (IOException e5) {
                    e = e5;
                    file = file3;
                    filterOutputStream2 = null;
                    StringBuilder sb4222 = new StringBuilder();
                    sb4222.append("Can't write: ");
                    sb4222.append(tag);
                    Log.e(str, sb4222.toString(), e);
                    if (filterOutputStream != null) {
                    }
                    if (inputStream != null) {
                    }
                    entry.close();
                    if (file != null) {
                    }
                } catch (Throwable th6) {
                    th = th6;
                    file = file3;
                    filterOutputStream = null;
                    if (filterOutputStream != null) {
                    }
                    if (inputStream != null) {
                    }
                    entry.close();
                    if (file != null) {
                    }
                    throw th;
                }
            }
            throw new IllegalArgumentException();
        } catch (IOException e6) {
            e = e6;
            filterOutputStream3 = null;
            inputStream = null;
            file = null;
            StringBuilder sb42222 = new StringBuilder();
            sb42222.append("Can't write: ");
            sb42222.append(tag);
            Log.e(str, sb42222.toString(), e);
            if (filterOutputStream != null) {
            }
            if (inputStream != null) {
            }
            entry.close();
            if (file != null) {
            }
        } catch (Throwable th7) {
            th = th7;
            filterOutputStream4 = null;
            inputStream = null;
            file = null;
            if (filterOutputStream != null) {
            }
            if (inputStream != null) {
            }
            entry.close();
            if (file != null) {
            }
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:110:0x01df, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x01e0, code lost:
        r22 = r14;
        r14 = r24;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:129:0x020d, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:133:0x0214, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:134:0x0215, code lost:
        r22 = r14;
        r14 = r24;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:135:0x021b, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:162:0x026c, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:165:0x0272, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:166:0x0273, code lost:
        r14 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:168:0x0277, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:169:0x0278, code lost:
        r23 = r5;
        r21 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:177:?, code lost:
        r14.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:180:?, code lost:
        r22.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:184:?, code lost:
        r14.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:187:?, code lost:
        r22.close();
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:188:0x02bf */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x0214 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:102:0x01ce] */
    /* JADX WARNING: Removed duplicated region for block: B:165:0x0272 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:89:0x01a2] */
    /* JADX WARNING: Removed duplicated region for block: B:176:0x02ab A[SYNTHETIC, Splitter:B:176:0x02ab] */
    /* JADX WARNING: Removed duplicated region for block: B:179:0x02b0 A[SYNTHETIC, Splitter:B:179:0x02b0] */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x02b7 A[SYNTHETIC, Splitter:B:183:0x02b7] */
    /* JADX WARNING: Removed duplicated region for block: B:186:0x02bc A[SYNTHETIC, Splitter:B:186:0x02bc] */
    /* JADX WARNING: Removed duplicated region for block: B:193:0x02c7 A[Catch:{ IOException -> 0x02f4 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        ArrayList arrayList;
        int i;
        InputStreamReader inputStreamReader;
        Entry entry;
        Entry entry2;
        Entry entry3;
        InputStreamReader inputStreamReader2;
        Entry entry4;
        String str;
        PrintWriter printWriter2 = printWriter;
        String[] strArr2 = strArr;
        synchronized (this) {
            if (this.mContext.checkCallingOrSelfPermission("android.permission.DUMP") != 0) {
                printWriter2.println("Permission Denial: Can't dump DropBoxManagerService");
                return;
            }
            try {
                init();
                StringBuilder sb = new StringBuilder();
                ArrayList arrayList2 = new ArrayList();
                int i2 = 0;
                boolean z = false;
                boolean z2 = false;
                while (strArr2 != null && i2 < strArr2.length) {
                    if (!strArr2[i2].equals("-p")) {
                        if (!strArr2[i2].equals("--print")) {
                            if (!strArr2[i2].equals("-f")) {
                                if (!strArr2[i2].equals("--file")) {
                                    if (strArr2[i2].startsWith("-")) {
                                        sb.append("Unknown argument: ");
                                        sb.append(strArr2[i2]);
                                        sb.append("\n");
                                    } else {
                                        arrayList2.add(strArr2[i2]);
                                    }
                                    i2++;
                                }
                            }
                            z2 = true;
                            i2++;
                        }
                    }
                    z = true;
                    i2++;
                }
                sb.append("Drop box contents: ");
                sb.append(this.mAllFiles.contents.size());
                sb.append(" entries\n");
                if (!arrayList2.isEmpty()) {
                    sb.append("Searching for:");
                    Iterator it = arrayList2.iterator();
                    while (it.hasNext()) {
                        String str2 = (String) it.next();
                        sb.append(" ");
                        sb.append(str2);
                    }
                    sb.append("\n");
                }
                int size = arrayList2.size();
                Time time = new Time();
                sb.append("\n");
                Iterator it2 = this.mAllFiles.contents.iterator();
                int i3 = 0;
                while (it2.hasNext()) {
                    EntryFile entryFile = (EntryFile) it2.next();
                    time.set(entryFile.timestampMillis);
                    String format = time.format("%Y-%m-%d %H:%M:%S");
                    boolean z3 = true;
                    for (int i4 = 0; i4 < size && z3; i4++) {
                        String str3 = (String) arrayList2.get(i4);
                        if (!format.contains(str3)) {
                            if (!str3.equals(entryFile.tag)) {
                                z3 = false;
                            }
                        }
                        z3 = true;
                    }
                    if (z3) {
                        int i5 = i3 + 1;
                        if (z) {
                            sb.append("========================================\n");
                        }
                        sb.append(format);
                        sb.append(" ");
                        sb.append(entryFile.tag == null ? "(no tag)" : entryFile.tag);
                        if (entryFile.file == null) {
                            str = " (no file)\n";
                        } else if ((entryFile.flags & 1) != 0) {
                            str = " (contents lost)\n";
                        } else {
                            sb.append(" (");
                            if ((entryFile.flags & 4) != 0) {
                                sb.append("compressed ");
                            }
                            sb.append((entryFile.flags & 2) != 0 ? "text" : PhotosOemApi.PATH_SPECIAL_TYPE_DATA);
                            sb.append(", ");
                            sb.append(entryFile.file.length());
                            sb.append(" bytes)\n");
                            if (z2 || (z && (entryFile.flags & 2) == 0)) {
                                if (!z) {
                                    sb.append("    ");
                                }
                                sb.append(entryFile.file.getPath());
                                sb.append("\n");
                            }
                            if ((entryFile.flags & 2) == 0 || (!z && z2)) {
                                arrayList = arrayList2;
                                i = size;
                            } else {
                                try {
                                    i = size;
                                    arrayList = arrayList2;
                                    Entry entry5 = new Entry(entryFile.tag, entryFile.timestampMillis, entryFile.file, entryFile.flags);
                                    if (z) {
                                        try {
                                            inputStreamReader2 = new InputStreamReader(entry3.getInputStream());
                                            try {
                                                char[] cArr = new char[4096];
                                                boolean z4 = false;
                                                while (true) {
                                                    int read = inputStreamReader2.read(cArr);
                                                    if (read <= 0) {
                                                        break;
                                                    }
                                                    sb.append(cArr, 0, read);
                                                    z4 = cArr[read + -1] == 10;
                                                    if (sb.length() > 65536) {
                                                        printWriter2.write(sb.toString());
                                                        sb.setLength(0);
                                                    }
                                                }
                                                if (!z4) {
                                                    sb.append("\n");
                                                }
                                                entry4 = entry3;
                                            } catch (IOException e) {
                                                e = e;
                                                inputStreamReader = inputStreamReader2;
                                                entry2 = entry3;
                                                try {
                                                    sb.append("*** ");
                                                    sb.append(e.toString());
                                                    sb.append("\n");
                                                    StringBuilder sb2 = new StringBuilder();
                                                    sb2.append("Can't read: ");
                                                    sb2.append(entryFile.file);
                                                    Log.e("DropBoxManagerService", sb2.toString(), e);
                                                    if (entry != null) {
                                                    }
                                                    if (inputStreamReader != null) {
                                                    }
                                                    if (z) {
                                                    }
                                                    i3 = i5;
                                                    size = i;
                                                    arrayList2 = arrayList;
                                                } catch (Throwable th) {
                                                    th = th;
                                                    if (entry != null) {
                                                    }
                                                    if (inputStreamReader != null) {
                                                    }
                                                    throw th;
                                                }
                                            } catch (Throwable th2) {
                                            }
                                        } catch (IOException e2) {
                                            e = e2;
                                            entry2 = entry3;
                                            inputStreamReader = null;
                                            sb.append("*** ");
                                            sb.append(e.toString());
                                            sb.append("\n");
                                            StringBuilder sb22 = new StringBuilder();
                                            sb22.append("Can't read: ");
                                            sb22.append(entryFile.file);
                                            Log.e("DropBoxManagerService", sb22.toString(), e);
                                            if (entry != null) {
                                            }
                                            if (inputStreamReader != null) {
                                            }
                                            if (z) {
                                            }
                                            i3 = i5;
                                            size = i;
                                            arrayList2 = arrayList;
                                        } catch (Throwable th3) {
                                            th = th3;
                                            entry = entry3;
                                            inputStreamReader = null;
                                            if (entry != null) {
                                            }
                                            if (inputStreamReader != null) {
                                            }
                                            throw th;
                                        }
                                    } else {
                                        entry4 = entry3;
                                        try {
                                            String text = entry4.getText(70);
                                            boolean z5 = text.length() == 70;
                                            sb.append("    ");
                                            sb.append(text.trim().replace(10, '/'));
                                            if (z5) {
                                                sb.append(" ...");
                                            }
                                            sb.append("\n");
                                            inputStreamReader2 = null;
                                        } catch (IOException e3) {
                                            e = e3;
                                            entry2 = entry4;
                                            inputStreamReader = null;
                                            sb.append("*** ");
                                            sb.append(e.toString());
                                            sb.append("\n");
                                            StringBuilder sb222 = new StringBuilder();
                                            sb222.append("Can't read: ");
                                            sb222.append(entryFile.file);
                                            Log.e("DropBoxManagerService", sb222.toString(), e);
                                            if (entry != null) {
                                            }
                                            if (inputStreamReader != null) {
                                            }
                                            if (z) {
                                            }
                                            i3 = i5;
                                            size = i;
                                            arrayList2 = arrayList;
                                        } catch (Throwable th4) {
                                            th = th4;
                                            entry = entry4;
                                            inputStreamReader = null;
                                            if (entry != null) {
                                            }
                                            if (inputStreamReader != null) {
                                            }
                                            throw th;
                                        }
                                    }
                                    entry4.close();
                                    if (inputStreamReader2 != null) {
                                        try {
                                            inputStreamReader2.close();
                                        } catch (IOException unused) {
                                        }
                                    }
                                } catch (IOException e4) {
                                    e = e4;
                                    arrayList = arrayList2;
                                    entry2 = null;
                                    inputStreamReader = null;
                                    sb.append("*** ");
                                    sb.append(e.toString());
                                    sb.append("\n");
                                    StringBuilder sb2222 = new StringBuilder();
                                    sb2222.append("Can't read: ");
                                    sb2222.append(entryFile.file);
                                    Log.e("DropBoxManagerService", sb2222.toString(), e);
                                    if (entry != null) {
                                    }
                                    if (inputStreamReader != null) {
                                    }
                                    if (z) {
                                    }
                                    i3 = i5;
                                    size = i;
                                    arrayList2 = arrayList;
                                } catch (Throwable th5) {
                                }
                            }
                            if (z) {
                                sb.append("\n");
                            }
                            i3 = i5;
                            size = i;
                            arrayList2 = arrayList;
                        }
                        sb.append(str);
                        arrayList = arrayList2;
                        i = size;
                        i3 = i5;
                        size = i;
                        arrayList2 = arrayList;
                    }
                }
                if (i3 == 0) {
                    sb.append("(No entries found.)\n");
                }
                if (strArr2 == null || strArr2.length == 0) {
                    if (!z) {
                        sb.append("\n");
                    }
                    sb.append("Usage: dumpsys dropbox [--print|--file] [YYYY-mm-dd] [HH:MM:SS] [tag]\n");
                }
                printWriter2.write(sb.toString());
            } catch (IOException e5) {
                IOException iOException = e5;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Can't initialize: ");
                sb3.append(iOException);
                printWriter2.println(sb3.toString());
                Log.e("DropBoxManagerService", "Can't init", iOException);
            }
        }
    }

    public synchronized Entry getNextEntry(String str, long j) {
        if (this.mContext.checkCallingOrSelfPermission("miui.permission.READ_LOGS") == 0) {
            try {
                init();
                FileList fileList = str == null ? this.mAllFiles : (FileList) this.mFilesByTag.get(str);
                if (fileList == null) {
                    return null;
                }
                for (EntryFile entryFile : fileList.contents.tailSet(new EntryFile(j + 1))) {
                    if (entryFile.tag != null) {
                        if ((entryFile.flags & 1) != 0) {
                            return new Entry(entryFile.tag, entryFile.timestampMillis);
                        }
                        try {
                            Entry entry = new Entry(entryFile.tag, entryFile.timestampMillis, entryFile.file, entryFile.flags);
                            return entry;
                        } catch (IOException e) {
                            String str2 = "DropBoxManagerService";
                            StringBuilder sb = new StringBuilder();
                            sb.append("Can't read: ");
                            sb.append(entryFile.file);
                            Log.e(str2, sb.toString(), e);
                        }
                    }
                }
                return null;
            } catch (IOException e2) {
                Log.e("DropBoxManagerService", "Can't init", e2);
                return null;
            }
        } else {
            throw new SecurityException("READ_LOGS permission required");
        }
    }

    public boolean isTagEnabled(String str) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        String str2 = "disabled";
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(DROPBOX_TAG_PREFIX);
            sb.append(str);
            return !str2.equals(sb.toString());
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }
}
