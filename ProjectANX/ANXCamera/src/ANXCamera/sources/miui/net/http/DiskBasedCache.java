package miui.net.http;

import android.util.Log;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import miui.net.http.Cache.Entry;
import miui.security.DigestUtils;
import miui.text.ExtraTextUtils;
import miui.util.AppConstants;
import miui.util.IOUtils;
import miui.util.SoftReferenceSingleton;

public class DiskBasedCache implements Cache {
    private static final int CACHE_MAGIC = 538182184;
    private static final int DEFAULT_DISK_USAGE_BYTES = 10485760;
    private static final float HYSTERESIS_FACTOR = 0.9f;
    private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
        /* access modifiers changed from: protected */
        public DiskBasedCache createInstance() {
            DiskBasedCache diskBasedCache = new DiskBasedCache();
            diskBasedCache.initialize();
            return diskBasedCache;
        }
    };
    private static final String TAG = "DisBasedCache";
    private final Map mEntries;
    private final int mMaxCacheSizeInBytes;
    private final File mRootDirectory;
    private long mTotalSize;

    class CacheFileContentInputStream extends FilterInputStream {
        private DiskCacheEntry mDiskCacheEntry;

        protected CacheFileContentInputStream(DiskCacheEntry diskCacheEntry) {
            super(getInputStream(diskCacheEntry));
            if (this.in != null) {
                this.mDiskCacheEntry = diskCacheEntry;
            }
        }

        private static InputStream getInputStream(DiskCacheEntry diskCacheEntry) {
            diskCacheEntry.acquire();
            try {
                FileInputStream fileInputStream = new FileInputStream(diskCacheEntry.file);
                if (fileInputStream.skip(diskCacheEntry.offset) == diskCacheEntry.offset) {
                    return fileInputStream;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("skip failed for file ");
                sb.append(diskCacheEntry.file.getName());
                throw new IOException(sb.toString());
            } catch (Throwable th) {
                diskCacheEntry.release();
                throw th;
            }
        }

        public void close() {
            this.mDiskCacheEntry.release();
            super.close();
        }
    }

    class DiskCacheEntry {
        public String contentEncoding;
        public String contentType;
        public String etag;
        public File file;
        public String key;
        private volatile boolean markDelete;
        public long offset;
        public Map responseHeaders;
        public long serverDate;
        public long size;
        public long softTtl;
        public long ttl;
        private volatile int using;

        private DiskCacheEntry() {
        }

        public static DiskCacheEntry fromFile(File file2) {
            FileInputStream fileInputStream;
            String str = "";
            try {
                fileInputStream = new FileInputStream(file2);
                try {
                    if (readInt(fileInputStream) != DiskBasedCache.CACHE_MAGIC) {
                        IOUtils.closeQuietly((InputStream) fileInputStream);
                        return null;
                    }
                    DiskCacheEntry diskCacheEntry = new DiskCacheEntry();
                    diskCacheEntry.key = readString(fileInputStream);
                    diskCacheEntry.etag = readString(fileInputStream);
                    if (str.equals(diskCacheEntry.etag)) {
                        diskCacheEntry.etag = null;
                    }
                    diskCacheEntry.contentType = readString(fileInputStream);
                    if (str.equals(diskCacheEntry.contentType)) {
                        diskCacheEntry.contentType = null;
                    }
                    diskCacheEntry.contentEncoding = readString(fileInputStream);
                    if (str.equals(diskCacheEntry.contentEncoding)) {
                        diskCacheEntry.contentEncoding = null;
                    }
                    diskCacheEntry.serverDate = readLong(fileInputStream);
                    diskCacheEntry.ttl = readLong(fileInputStream);
                    diskCacheEntry.softTtl = readLong(fileInputStream);
                    diskCacheEntry.responseHeaders = readHeaders(fileInputStream);
                    diskCacheEntry.offset = fileInputStream.getChannel().position();
                    diskCacheEntry.file = file2;
                    diskCacheEntry.size = file2.length();
                    IOUtils.closeQuietly((InputStream) fileInputStream);
                    return diskCacheEntry;
                } catch (IOException unused) {
                    IOUtils.closeQuietly((InputStream) fileInputStream);
                    return null;
                } catch (Throwable th) {
                    th = th;
                    IOUtils.closeQuietly((InputStream) fileInputStream);
                    throw th;
                }
            } catch (IOException unused2) {
                fileInputStream = null;
                IOUtils.closeQuietly((InputStream) fileInputStream);
                return null;
            } catch (Throwable th2) {
                th = th2;
                fileInputStream = null;
                IOUtils.closeQuietly((InputStream) fileInputStream);
                throw th;
            }
        }

        private static int read(InputStream inputStream) {
            int read = inputStream.read();
            if (read != -1) {
                return read;
            }
            throw new EOFException();
        }

        static Map readHeaders(InputStream inputStream) {
            int readInt = readInt(inputStream);
            HashMap hashMap = new HashMap();
            for (int i = 0; i < readInt; i++) {
                hashMap.put(readString(inputStream).intern(), readString(inputStream).intern());
            }
            return hashMap;
        }

        static int readInt(InputStream inputStream) {
            return (read(inputStream) << 24) | read(inputStream) | 0 | (read(inputStream) << 8) | (read(inputStream) << 16);
        }

        static long readLong(InputStream inputStream) {
            return (((long) read(inputStream)) & 255) | 0 | ((((long) read(inputStream)) & 255) << 8) | ((((long) read(inputStream)) & 255) << 16) | ((((long) read(inputStream)) & 255) << 24) | ((((long) read(inputStream)) & 255) << 32) | ((((long) read(inputStream)) & 255) << 40) | ((((long) read(inputStream)) & 255) << 48) | ((255 & ((long) read(inputStream))) << 56);
        }

        static String readString(InputStream inputStream) {
            int readLong = (int) readLong(inputStream);
            if (readLong < 0 || readLong > 8192) {
                throw new IOException("Malformed data structure encountered");
            }
            byte[] bArr = new byte[readLong];
            int i = 0;
            while (i < readLong) {
                int read = inputStream.read(bArr, i, readLong - i);
                if (read == -1) {
                    break;
                }
                i += read;
            }
            if (i == readLong) {
                return new String(bArr, "UTF-8");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Expected ");
            sb.append(readLong);
            sb.append(" bytes but read ");
            sb.append(i);
            sb.append(" bytes");
            throw new IOException(sb.toString());
        }

        /* JADX WARNING: Removed duplicated region for block: B:40:0x0109  */
        /* JADX WARNING: Removed duplicated region for block: B:44:0x0129  */
        /* JADX WARNING: Removed duplicated region for block: B:48:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static DiskCacheEntry toFile(File file2, String str, Entry entry) {
            FileOutputStream fileOutputStream;
            String str2 = "Cannot delete file ";
            String str3 = DiskBasedCache.TAG;
            InputStream inputStream = entry.data;
            try {
                entry.data = null;
                fileOutputStream = new FileOutputStream(file2);
                try {
                    writeInt(fileOutputStream, DiskBasedCache.CACHE_MAGIC);
                    writeString(fileOutputStream, str);
                    String str4 = "";
                    writeString(fileOutputStream, entry.etag == null ? str4 : entry.etag);
                    writeString(fileOutputStream, entry.contentType == null ? str4 : entry.contentType);
                    if (entry.contentEncoding != null) {
                        str4 = entry.contentEncoding;
                    }
                    writeString(fileOutputStream, str4);
                    writeLong(fileOutputStream, entry.serverDate);
                    writeLong(fileOutputStream, entry.ttl);
                    writeLong(fileOutputStream, entry.softTtl);
                    writeHeaders(entry.responseHeaders, fileOutputStream);
                    fileOutputStream.flush();
                    DiskCacheEntry diskCacheEntry = new DiskCacheEntry();
                    diskCacheEntry.key = str;
                    diskCacheEntry.etag = entry.etag;
                    diskCacheEntry.contentType = entry.contentType;
                    diskCacheEntry.contentEncoding = entry.contentEncoding;
                    diskCacheEntry.serverDate = entry.serverDate;
                    diskCacheEntry.ttl = entry.ttl;
                    diskCacheEntry.softTtl = entry.softTtl;
                    diskCacheEntry.responseHeaders = entry.responseHeaders;
                    diskCacheEntry.file = file2;
                    diskCacheEntry.offset = fileOutputStream.getChannel().position();
                    IOUtils.copy(inputStream, (OutputStream) fileOutputStream);
                    fileOutputStream.flush();
                    diskCacheEntry.size = fileOutputStream.getChannel().position();
                    if (entry.length <= 0) {
                        entry.length = diskCacheEntry.size - diskCacheEntry.offset;
                    } else if (entry.length != diskCacheEntry.size - diskCacheEntry.offset) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("length not match ");
                        sb.append(entry.length);
                        sb.append(":");
                        sb.append(diskCacheEntry.size - diskCacheEntry.offset);
                        throw new IOException(sb.toString());
                    }
                    fileOutputStream.close();
                    entry.data = new CacheFileContentInputStream(diskCacheEntry);
                    IOUtils.closeQuietly(inputStream);
                    IOUtils.closeQuietly((OutputStream) null);
                    return diskCacheEntry;
                } catch (IOException e) {
                    e = e;
                    try {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Cannot save cache to disk file ");
                        sb2.append(file2);
                        Log.w(str3, sb2.toString(), e);
                        IOUtils.closeQuietly(inputStream);
                        IOUtils.closeQuietly((OutputStream) fileOutputStream);
                        if (!file2.delete()) {
                            return null;
                        }
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(str2);
                        sb3.append(file2);
                        Log.e(str3, sb3.toString());
                        return null;
                    } catch (Throwable th) {
                        th = th;
                        IOUtils.closeQuietly(inputStream);
                        IOUtils.closeQuietly((OutputStream) fileOutputStream);
                        if (!file2.delete()) {
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(str2);
                            sb4.append(file2);
                            Log.e(str3, sb4.toString());
                        }
                        throw th;
                    }
                }
            } catch (IOException e2) {
                e = e2;
                fileOutputStream = null;
                StringBuilder sb22 = new StringBuilder();
                sb22.append("Cannot save cache to disk file ");
                sb22.append(file2);
                Log.w(str3, sb22.toString(), e);
                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly((OutputStream) fileOutputStream);
                if (!file2.delete()) {
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = null;
                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly((OutputStream) fileOutputStream);
                if (!file2.delete()) {
                }
                throw th;
            }
        }

        static void writeHeaders(Map map, OutputStream outputStream) {
            if (map == null || map.size() == 0) {
                writeInt(outputStream, 0);
                return;
            }
            writeInt(outputStream, map.size());
            for (Map.Entry entry : map.entrySet()) {
                writeString(outputStream, (String) entry.getKey());
                writeString(outputStream, (String) entry.getValue());
            }
        }

        static void writeInt(OutputStream outputStream, int i) {
            outputStream.write(i & 255);
            outputStream.write((i >> 8) & 255);
            outputStream.write((i >> 16) & 255);
            outputStream.write((i >> 24) & 255);
        }

        static void writeLong(OutputStream outputStream, long j) {
            outputStream.write((byte) ((int) j));
            outputStream.write((byte) ((int) (j >>> 8)));
            outputStream.write((byte) ((int) (j >>> 16)));
            outputStream.write((byte) ((int) (j >>> 24)));
            outputStream.write((byte) ((int) (j >>> 32)));
            outputStream.write((byte) ((int) (j >>> 40)));
            outputStream.write((byte) ((int) (j >>> 48)));
            outputStream.write((byte) ((int) (j >>> 56)));
        }

        static void writeString(OutputStream outputStream, String str) {
            byte[] bytes = str.getBytes("UTF-8");
            writeLong(outputStream, (long) bytes.length);
            outputStream.write(bytes, 0, bytes.length);
        }

        public synchronized void acquire() {
            this.using++;
        }

        public synchronized void delete() {
            if (this.using > 0) {
                this.markDelete = true;
            } else if (!this.file.delete()) {
                String str = DiskBasedCache.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot delete file ");
                sb.append(this.file);
                Log.e(str, sb.toString());
            }
        }

        public synchronized boolean isUsing() {
            return this.using > 0;
        }

        public synchronized void release() {
            this.using--;
            if (this.using <= 0 && this.markDelete && !this.file.delete()) {
                String str = DiskBasedCache.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot delete file ");
                sb.append(this.file);
                Log.e(str, sb.toString());
            }
        }

        public Entry toCacheEntry() {
            try {
                Entry entry = new Entry();
                entry.data = new FileInputStream(this.file);
                if (entry.data.skip(this.offset) != this.offset) {
                    return null;
                }
                entry.length = this.size - this.offset;
                entry.etag = this.etag;
                entry.contentType = this.contentType;
                entry.contentEncoding = this.contentEncoding;
                entry.serverDate = this.serverDate;
                entry.ttl = this.ttl;
                entry.softTtl = this.softTtl;
                entry.responseHeaders = this.responseHeaders;
                return entry;
            } catch (IOException unused) {
                return null;
            }
        }
    }

    public DiskBasedCache() {
        this(null, DEFAULT_DISK_USAGE_BYTES);
    }

    public DiskBasedCache(File file) {
        this(file, DEFAULT_DISK_USAGE_BYTES);
    }

    public DiskBasedCache(File file, int i) {
        this.mEntries = new ConcurrentHashMap(16, 0.75f);
        this.mTotalSize = 0;
        if (file == null) {
            file = new File(AppConstants.getCurrentApplication().getCacheDir(), "miui.net.http");
        }
        this.mRootDirectory = file;
        this.mMaxCacheSizeInBytes = i;
    }

    public static DiskBasedCache getDefault() {
        return (DiskBasedCache) INSTANCE.get();
    }

    private File getFileForKey(String str) {
        return new File(this.mRootDirectory, ExtraTextUtils.toHexReadable(DigestUtils.get((CharSequence) str, "MD5")));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00da, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void pruneIfNeeded(long j) {
        if (this.mTotalSize + j >= ((long) this.mMaxCacheSizeInBytes)) {
            long currentTimeMillis = System.currentTimeMillis();
            synchronized (this.mEntries) {
                if (this.mTotalSize + j >= ((long) this.mMaxCacheSizeInBytes)) {
                    Iterator it = this.mEntries.entrySet().iterator();
                    while (it.hasNext()) {
                        DiskCacheEntry diskCacheEntry = (DiskCacheEntry) ((Map.Entry) it.next()).getValue();
                        if (diskCacheEntry.ttl < currentTimeMillis) {
                            diskCacheEntry.delete();
                            this.mTotalSize -= diskCacheEntry.size;
                            it.remove();
                        }
                    }
                    if (this.mTotalSize + j >= ((long) this.mMaxCacheSizeInBytes)) {
                        Iterator it2 = this.mEntries.entrySet().iterator();
                        while (it2.hasNext()) {
                            DiskCacheEntry diskCacheEntry2 = (DiskCacheEntry) ((Map.Entry) it2.next()).getValue();
                            if (diskCacheEntry2.softTtl < currentTimeMillis) {
                                diskCacheEntry2.delete();
                                this.mTotalSize -= diskCacheEntry2.size;
                                it2.remove();
                                if (((float) (this.mTotalSize + j)) < ((float) this.mMaxCacheSizeInBytes) * 0.9f) {
                                    break;
                                }
                            }
                        }
                        if (this.mTotalSize + j >= ((long) this.mMaxCacheSizeInBytes)) {
                            Iterator it3 = this.mEntries.entrySet().iterator();
                            while (it3.hasNext()) {
                                DiskCacheEntry diskCacheEntry3 = (DiskCacheEntry) ((Map.Entry) it3.next()).getValue();
                                if (!diskCacheEntry3.isUsing()) {
                                    diskCacheEntry3.delete();
                                    it3.remove();
                                    if (((float) (this.mTotalSize + j)) < ((float) this.mMaxCacheSizeInBytes) * 0.9f) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void putEntry(DiskCacheEntry diskCacheEntry) {
        synchronized (this.mEntries) {
            DiskCacheEntry diskCacheEntry2 = (DiskCacheEntry) this.mEntries.get(diskCacheEntry.key);
            if (diskCacheEntry2 == null) {
                this.mTotalSize += diskCacheEntry.size;
            } else {
                this.mTotalSize += diskCacheEntry.size - diskCacheEntry2.size;
            }
            this.mEntries.put(diskCacheEntry.key, diskCacheEntry);
        }
    }

    private void removeEntry(DiskCacheEntry diskCacheEntry) {
        synchronized (this.mEntries) {
            this.mTotalSize -= diskCacheEntry.size;
            this.mEntries.remove(diskCacheEntry.key);
        }
    }

    public void clear() {
        for (Map.Entry value : this.mEntries.entrySet()) {
            ((DiskCacheEntry) value.getValue()).delete();
        }
        this.mEntries.clear();
        this.mTotalSize = 0;
    }

    public Entry get(String str) {
        DiskCacheEntry diskCacheEntry = (DiskCacheEntry) this.mEntries.get(str);
        if (diskCacheEntry == null) {
            return null;
        }
        return diskCacheEntry.toCacheEntry();
    }

    public void initialize() {
        this.mEntries.clear();
        this.mTotalSize = 0;
        boolean exists = this.mRootDirectory.exists();
        String str = TAG;
        if (!exists) {
            if (!this.mRootDirectory.mkdirs()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot create directory ");
                sb.append(this.mRootDirectory);
                Log.e(str, sb.toString());
            }
            return;
        }
        File[] listFiles = this.mRootDirectory.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                DiskCacheEntry fromFile = DiskCacheEntry.fromFile(file);
                if (fromFile != null) {
                    putEntry(fromFile);
                } else if (!file.delete()) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Cannot delete file ");
                    sb2.append(file);
                    Log.e(str, sb2.toString());
                }
            }
        }
    }

    public void invalidate(String str, boolean z) {
        DiskCacheEntry diskCacheEntry = (DiskCacheEntry) this.mEntries.get(str);
        if (diskCacheEntry != null) {
            diskCacheEntry.softTtl = 0;
            if (z) {
                diskCacheEntry.ttl = 0;
            }
        }
    }

    public boolean put(String str, Entry entry) {
        DiskCacheEntry file = DiskCacheEntry.toFile(getFileForKey(str), str, entry);
        if (file == null) {
            return false;
        }
        pruneIfNeeded(file.size);
        putEntry(file);
        return true;
    }

    public void remove(String str) {
        DiskCacheEntry diskCacheEntry = (DiskCacheEntry) this.mEntries.get(str);
        if (diskCacheEntry != null) {
            diskCacheEntry.delete();
            removeEntry(diskCacheEntry);
        }
    }
}
