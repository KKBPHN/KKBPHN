package com.android.camera.module.impl.component;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import com.android.camera.CameraAppImpl;
import com.android.camera.log.Log;
import com.android.camera.storage.Storage;
import com.ss.android.ugc.effectmanager.effect.model.ComposerHelper;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

public class FileUtils {
    public static final String BEAUTY_12_DIR;
    public static final String BEAUTY_12_FILENAME = "Beauty_12.zip";
    public static final String CACHE;
    public static final String CONCAT_VIDEO_DIR;
    public static final String DOC;
    public static final String FACE_RESHAPE = "FaceReshape_V2.zip";
    public static final String FILTER;
    public static final String FILTER_DIR;
    public static final String FILTER_FILE_SUFFIX = ".png";
    public static final String MI_LIVE_TMP;
    public static final String MODELS_DIR;
    public static final String MUSIC;
    public static final String MUSIC_EFFECT_DIR = RESOURCE_DIR;
    public static final String MUSIC_LOCAL;
    public static final String MUSIC_ONLINE;
    public static final String PHONEPARAM = "phoneParams.txt";
    public static final String RESHAPE_DIR_NAME;
    public static final String RESOURCE_DIR;
    public static List RESOURCE_LIST_CN = new ArrayList();
    public static List RESOURCE_LIST_GLOBAL = new ArrayList();
    public static final String ROOT_DIR;
    public static final int SIZETYPE_B = 1;
    public static final int SIZETYPE_GB = 4;
    public static final int SIZETYPE_KB = 2;
    public static final int SIZETYPE_MB = 3;
    public static final String STICKER_RESOURCE_DIR;
    public static final String SUFFIX = ".zip";
    public static final String TAG = "FileUtils";
    public static final String VIDEO_DUMP;
    public static final String VIDEO_TMP;
    public static final List musicList = new ArrayList();

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(CameraAppImpl.getAndroidContext().getNoBackupFilesDir().getPath());
        sb.append("/");
        ROOT_DIR = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(ROOT_DIR);
        sb2.append("resource/");
        RESOURCE_DIR = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(RESOURCE_DIR);
        sb3.append("stickers/");
        STICKER_RESOURCE_DIR = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(ROOT_DIR);
        sb4.append("models/");
        MODELS_DIR = sb4.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(ROOT_DIR);
        sb5.append("tmp/");
        VIDEO_TMP = sb5.toString();
        StringBuilder sb6 = new StringBuilder();
        sb6.append(ROOT_DIR);
        sb6.append(".milive/");
        MI_LIVE_TMP = sb6.toString();
        StringBuilder sb7 = new StringBuilder();
        sb7.append(ROOT_DIR);
        sb7.append("dump/");
        VIDEO_DUMP = sb7.toString();
        StringBuilder sb8 = new StringBuilder();
        sb8.append(ROOT_DIR);
        sb8.append("concat/");
        CONCAT_VIDEO_DIR = sb8.toString();
        StringBuilder sb9 = new StringBuilder();
        sb9.append(ROOT_DIR);
        sb9.append("cache/");
        CACHE = sb9.toString();
        StringBuilder sb10 = new StringBuilder();
        sb10.append(ROOT_DIR);
        sb10.append("music/");
        MUSIC = sb10.toString();
        StringBuilder sb11 = new StringBuilder();
        sb11.append(ROOT_DIR);
        sb11.append("doc/");
        DOC = sb11.toString();
        StringBuilder sb12 = new StringBuilder();
        sb12.append(RESOURCE_DIR);
        sb12.append("filter/");
        FILTER_DIR = sb12.toString();
        StringBuilder sb13 = new StringBuilder();
        sb13.append(FILTER_DIR);
        sb13.append("Filter_02/");
        FILTER = sb13.toString();
        StringBuilder sb14 = new StringBuilder();
        sb14.append(RESOURCE_DIR);
        sb14.append("Beauty_12/");
        BEAUTY_12_DIR = sb14.toString();
        StringBuilder sb15 = new StringBuilder();
        sb15.append(RESOURCE_DIR);
        sb15.append("FaceReshape_V2/");
        RESHAPE_DIR_NAME = sb15.toString();
        StringBuilder sb16 = new StringBuilder();
        sb16.append(MUSIC);
        sb16.append("local/");
        MUSIC_LOCAL = sb16.toString();
        StringBuilder sb17 = new StringBuilder();
        sb17.append(MUSIC);
        sb17.append("online/");
        MUSIC_ONLINE = sb17.toString();
        RESOURCE_LIST_CN.add("0eb0e0214f7bc7f7bbfb4e9f4dba7f99");
        RESOURCE_LIST_CN.add("a75682e81788cc12f68682b9c9067f70");
        String str = "24991e783f23920397ac8aeed15994c2";
        RESOURCE_LIST_CN.add(str);
        RESOURCE_LIST_GLOBAL.add(str);
        RESOURCE_LIST_GLOBAL.add("9b74385fe7e8cb81e1b88ce3b293bdf2");
        RESOURCE_LIST_GLOBAL.add("0a673064d64fce91ee41b405c6f74dca");
        musicList.add("music00001.mp3");
        musicList.add("music00002.mp3");
        musicList.add("music00003.mp3");
        musicList.add("music00004.mp3");
    }

    private static String FormetFileSize(long j) {
        StringBuilder sb;
        String str;
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        if (j == 0) {
            return "0B";
        }
        if (j < 1024) {
            sb = new StringBuilder();
            sb.append(decimalFormat.format((double) j));
            str = "B";
        } else if (j < PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) {
            sb = new StringBuilder();
            sb.append(decimalFormat.format(((double) j) / 1024.0d));
            str = "KB";
        } else if (j < IjkMediaMeta.AV_CH_STEREO_RIGHT) {
            sb = new StringBuilder();
            sb.append(decimalFormat.format(((double) j) / 1048576.0d));
            str = "MB";
        } else {
            sb = new StringBuilder();
            sb.append(decimalFormat.format(((double) j) / 1.073741824E9d));
            str = "GB";
        }
        sb.append(str);
        return sb.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:8:0x0053  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void UnZipAssetFolder(Context context, String str, String str2) {
        File file;
        StringBuilder sb = new StringBuilder();
        sb.append("live/");
        sb.append(str);
        String sb2 = sb.toString();
        InputStream open = context.getAssets().open(sb2);
        File file2 = new File(str2);
        if (file2.exists()) {
            if (file2.isFile()) {
                file2.delete();
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str2);
            sb3.append(File.separator);
            sb3.append(getFileName(sb2));
            file = new File(sb3.toString());
            if (file.exists()) {
                deleteDir(file);
            }
            file.mkdirs();
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str2);
            sb4.append(getFileName(sb2));
            String sb5 = sb4.toString();
            open.close();
            UnZipFolder(context.getAssets().open(sb2), sb5);
        }
        file2.mkdirs();
        StringBuilder sb32 = new StringBuilder();
        sb32.append(str2);
        sb32.append(File.separator);
        sb32.append(getFileName(sb2));
        file = new File(sb32.toString());
        if (file.exists()) {
        }
        file.mkdirs();
        StringBuilder sb42 = new StringBuilder();
        sb42.append(str2);
        sb42.append(getFileName(sb2));
        String sb52 = sb42.toString();
        open.close();
        UnZipFolder(context.getAssets().open(sb2), sb52);
    }

    /* JADX WARNING: Removed duplicated region for block: B:8:0x0044  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int UnZipFileFolder(String str, String str2) {
        File file;
        FileInputStream fileInputStream = new FileInputStream(new File(str));
        File file2 = new File(str2);
        if (file2.exists()) {
            if (file2.isFile()) {
                file2.delete();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(File.separator);
            sb.append(getFileName(str));
            file = new File(sb.toString());
            if (file.exists()) {
                deleteDir(file);
            }
            file.mkdirs();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(File.separator);
            sb2.append(getFileName(str));
            String sb3 = sb2.toString();
            fileInputStream.close();
            UnZipFolder(new FileInputStream(new File(str)), sb3);
            return 0;
        }
        file2.mkdirs();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str2);
        sb4.append(File.separator);
        sb4.append(getFileName(str));
        file = new File(sb4.toString());
        if (file.exists()) {
        }
        file.mkdirs();
        StringBuilder sb22 = new StringBuilder();
        sb22.append(str2);
        sb22.append(File.separator);
        sb22.append(getFileName(str));
        String sb32 = sb22.toString();
        fileInputStream.close();
        UnZipFolder(new FileInputStream(new File(str)), sb32);
        return 0;
    }

    private static void UnZipFolder(InputStream inputStream, String str) {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        while (true) {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry == null) {
                break;
            }
            String name = nextEntry.getName();
            if (nextEntry.isDirectory()) {
                String substring = name.substring(0, name.length() - 1);
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(File.separator);
                sb.append(substring);
                File file = new File(sb.toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(File.separator);
                sb2.append(name);
                File file2 = new File(sb2.toString());
                if (file2.exists()) {
                    break;
                }
                file2.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = zipInputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, read);
                    fileOutputStream.flush();
                }
                fileOutputStream.close();
            }
        }
        zipInputStream.close();
    }

    public static boolean checkFileConsist(String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        File file = new File(str);
        if (file.exists() && !file.isDirectory() && getFileSize(str) > 10) {
            z = true;
        }
        return z;
    }

    public static boolean checkFileDirectoryConsist(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        File file = new File(str);
        return file.exists() && file.isDirectory();
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:10|11|12|13|14|20|15) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0072 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean copyAssetsDirectory(Context context, String str, String str2) {
        String[] list;
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdirs();
        }
        AssetManager assets = context.getAssets();
        try {
            if (str.endsWith("/")) {
                str = str.substring(0, str.length() - 1);
            }
            for (String str3 : assets.list(str)) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(File.separator);
                sb.append(str3);
                String sb2 = sb.toString();
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str2);
                sb3.append(File.separator);
                sb3.append(str3);
                String sb4 = sb3.toString();
                InputStream open = assets.open(sb2);
                StringBuilder sb5 = new StringBuilder();
                sb5.append(str2);
                sb5.append(File.separator);
                copyFileIfNeed(context, sb5.toString(), sb2, str3);
                open.close();
                copyAssetsDirectory(context, sb2, sb4);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void copyFile(File file, File file2) {
        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream fileOutputStream = new FileOutputStream(file2);
        FileChannel channel = fileInputStream.getChannel();
        channel.transferTo(0, channel.size(), fileOutputStream.getChannel());
        fileInputStream.close();
        fileOutputStream.close();
    }

    public static boolean copyFileIfNeed(Context context, String str, String str2) {
        return copyFileIfNeed(context, str, str2, str2);
    }

    public static boolean copyFileIfNeed(Context context, String str, String str2, String str3) {
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str3);
        String sb2 = sb.toString();
        if (!sb2.isEmpty()) {
            File file2 = new File(sb2);
            if (!file2.exists()) {
                try {
                    file2.createNewFile();
                    InputStream open = context.getApplicationContext().getAssets().open(str2);
                    if (open == null) {
                        return false;
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    byte[] bArr = new byte[4096];
                    while (true) {
                        int read = open.read(bArr);
                        if (read <= 0) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, read);
                    }
                    open.close();
                    fileOutputStream.close();
                } catch (IOException unused) {
                    file2.delete();
                    return false;
                }
            }
        }
        return true;
    }

    public static void createNewFile(String str) {
        File file = new File(str);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized String createtFileName(String str, String str2) {
        String sb;
        synchronized (FileUtils.class) {
            String format = new SimpleDateFormat("MMddHHmmssSSS").format(new Date(System.currentTimeMillis()));
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(format);
            sb2.append('.');
            sb2.append(str2);
            sb = sb2.toString();
        }
        return sb;
    }

    public static boolean delDir(String str) {
        boolean z = false;
        if (str.isEmpty()) {
            return false;
        }
        File file = new File(str);
        if (!file.isFile() && file.exists() && deleteDir(file)) {
            z = true;
        }
        return z;
    }

    public static final boolean deleteDir(File file) {
        if (file.isDirectory()) {
            String[] list = file.list();
            if (list != null) {
                for (String file2 : list) {
                    if (!deleteDir(new File(file, file2))) {
                        return false;
                    }
                }
            }
        }
        return file.delete();
    }

    public static boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            return true;
        }
        if (!file.isDirectory()) {
            return file.delete();
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File deleteFile : listFiles) {
                if (!deleteFile(deleteFile)) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    public static boolean deleteFile(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return deleteFile(new File(str));
    }

    public static boolean deleteSubFiles(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        File file = new File(str);
        if (!file.exists() || !file.isDirectory()) {
            return false;
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File delete : listFiles) {
                delete.delete();
            }
        }
        return true;
    }

    private static void doDeleteEmptyDir(String str) {
        String str2;
        StringBuilder sb;
        PrintStream printStream;
        if (new File(str).delete()) {
            printStream = System.out;
            sb = new StringBuilder();
            str2 = "Successfully deleted empty directory: ";
        } else {
            printStream = System.out;
            sb = new StringBuilder();
            str2 = "Failed to delete empty directory: ";
        }
        sb.append(str2);
        sb.append(str);
        printStream.println(sb.toString());
    }

    public static double formatFileSize(long j, int i) {
        double d;
        double d2;
        double d3;
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        if (i != 1) {
            if (i == 2) {
                d2 = (double) j;
                d3 = 1024.0d;
            } else if (i == 3) {
                d2 = (double) j;
                d3 = 1048576.0d;
            } else if (i != 4) {
                return 0.0d;
            } else {
                d2 = (double) j;
                d3 = 1.073741824E9d;
            }
            d = d2 / d3;
        } else {
            d = (double) j;
        }
        return Double.valueOf(decimalFormat.format(d)).doubleValue();
    }

    /* JADX WARNING: Removed duplicated region for block: B:53:0x007c A[SYNTHETIC, Splitter:B:53:0x007c] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0086 A[SYNTHETIC, Splitter:B:58:0x0086] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0092 A[SYNTHETIC, Splitter:B:65:0x0092] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x009c A[SYNTHETIC, Splitter:B:70:0x009c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] getFileBytes(String str) {
        FileInputStream fileInputStream;
        ByteArrayOutputStream byteArrayOutputStream;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        File file = new File(str);
        if (!file.exists() || file.length() <= 0) {
            return null;
        }
        try {
            fileInputStream = new FileInputStream(file);
            try {
                byte[] bArr = new byte[1024];
                byteArrayOutputStream = null;
                while (true) {
                    try {
                        int read = fileInputStream.read(bArr);
                        if (read <= 0) {
                            break;
                        }
                        if (byteArrayOutputStream == null) {
                            byteArrayOutputStream = new ByteArrayOutputStream();
                        }
                        byteArrayOutputStream.write(bArr, 0, read);
                    } catch (Exception e) {
                        e = e;
                        try {
                            e.printStackTrace();
                            if (byteArrayOutputStream != null) {
                            }
                            if (fileInputStream != null) {
                            }
                            return null;
                        } catch (Throwable th) {
                            th = th;
                            if (byteArrayOutputStream != null) {
                            }
                            if (fileInputStream != null) {
                            }
                            throw th;
                        }
                    }
                }
                if (byteArrayOutputStream != null) {
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    try {
                        fileInputStream.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                    return byteArray;
                }
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                try {
                    fileInputStream.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
                return null;
            } catch (Exception e6) {
                e = e6;
                byteArrayOutputStream = null;
                e.printStackTrace();
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e7) {
                        e7.printStackTrace();
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e8) {
                        e8.printStackTrace();
                    }
                }
                return null;
            } catch (Throwable th2) {
                th = th2;
                byteArrayOutputStream = null;
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e9) {
                        e9.printStackTrace();
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e10) {
                        e10.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (Exception e11) {
            e = e11;
            fileInputStream = null;
            byteArrayOutputStream = null;
            e.printStackTrace();
            if (byteArrayOutputStream != null) {
            }
            if (fileInputStream != null) {
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
            byteArrayOutputStream = null;
            if (byteArrayOutputStream != null) {
            }
            if (fileInputStream != null) {
            }
            throw th;
        }
    }

    public static String getFileName(String str) {
        int lastIndexOf = str.lastIndexOf("/");
        int lastIndexOf2 = str.lastIndexOf(".");
        if (lastIndexOf2 != -1) {
            return str.substring(lastIndexOf + 1, lastIndexOf2);
        }
        return null;
    }

    public static long getFileSize(String str) {
        long j = 0;
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        File file = new File(str);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                if (listFiles != null) {
                    for (File path : listFiles) {
                        j += getFileSize(path.getPath());
                    }
                }
            } else {
                j = file.length();
            }
        }
        return j;
    }

    public static File getOutputMediaFile() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        if (file.exists() || file.mkdirs()) {
            String format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
            StringBuilder sb = new StringBuilder();
            sb.append(file.getPath());
            sb.append(File.separator);
            sb.append("IMG_");
            sb.append(format);
            sb.append(Storage.JPEG_SUFFIX);
            return new File(sb.toString());
        }
        Log.e("FileUtil", "failed to create directory");
        return null;
    }

    public static boolean hasDir(String str) {
        File file = new File(str);
        return file.exists() && file.isDirectory();
    }

    private static boolean hasParentDir(InputStream inputStream) {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        boolean z = true;
        while (true) {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry == null) {
                zipInputStream.close();
                return z;
            } else if (nextEntry.getName().equals(ComposerHelper.CONFIG_FILE_NAME)) {
                z = false;
            }
        }
    }

    public static boolean makeDir(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        File file = new File(str);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return false;
    }

    public static boolean makeNoMediaDir(String str) {
        boolean makeDir = makeDir(str);
        makeSureNoMedia(str);
        return makeDir;
    }

    public static boolean makeSureNoMedia(String str) {
        File file = new File(str, Storage.AVOID_SCAN_FILE_NAME);
        if (file.exists()) {
            return true;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* JADX WARNING: type inference failed for: r8v1, types: [java.io.InputStreamReader] */
    /* JADX WARNING: type inference failed for: r4v0, types: [java.io.BufferedReader] */
    /* JADX WARNING: type inference failed for: r8v4 */
    /* JADX WARNING: type inference failed for: r4v1 */
    /* JADX WARNING: type inference failed for: r8v5, types: [java.io.InputStreamReader] */
    /* JADX WARNING: type inference failed for: r4v2, types: [java.io.BufferedReader] */
    /* JADX WARNING: type inference failed for: r4v3 */
    /* JADX WARNING: type inference failed for: r8v8 */
    /* JADX WARNING: type inference failed for: r4v4 */
    /* JADX WARNING: type inference failed for: r8v10, types: [java.io.InputStreamReader] */
    /* JADX WARNING: type inference failed for: r4v5, types: [java.io.BufferedReader] */
    /* JADX WARNING: type inference failed for: r4v6 */
    /* JADX WARNING: type inference failed for: r8v13 */
    /* JADX WARNING: type inference failed for: r4v7 */
    /* JADX WARNING: type inference failed for: r8v15 */
    /* JADX WARNING: type inference failed for: r4v8 */
    /* JADX WARNING: type inference failed for: r4v9 */
    /* JADX WARNING: type inference failed for: r4v10 */
    /* JADX WARNING: type inference failed for: r8v19 */
    /* JADX WARNING: type inference failed for: r4v11 */
    /* JADX WARNING: type inference failed for: r8v20, types: [java.io.Reader, java.io.InputStreamReader] */
    /* JADX WARNING: type inference failed for: r4v13 */
    /* JADX WARNING: type inference failed for: r4v15 */
    /* JADX WARNING: type inference failed for: r4v16 */
    /* JADX WARNING: type inference failed for: r4v17, types: [java.io.BufferedReader] */
    /* JADX WARNING: type inference failed for: r8v22 */
    /* JADX WARNING: type inference failed for: r4v18 */
    /* JADX WARNING: type inference failed for: r8v23 */
    /* JADX WARNING: type inference failed for: r4v19 */
    /* JADX WARNING: type inference failed for: r4v20 */
    /* JADX WARNING: type inference failed for: r8v24 */
    /* JADX WARNING: type inference failed for: r4v21 */
    /* JADX WARNING: type inference failed for: r4v22 */
    /* JADX WARNING: type inference failed for: r8v25 */
    /* JADX WARNING: type inference failed for: r4v23 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r8v4
  assigns: []
  uses: []
  mth insns count: 123
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
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0091 A[SYNTHETIC, Splitter:B:50:0x0091] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x009b A[SYNTHETIC, Splitter:B:55:0x009b] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00a5 A[SYNTHETIC, Splitter:B:60:0x00a5] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x00c8 A[SYNTHETIC, Splitter:B:68:0x00c8] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x00d2 A[SYNTHETIC, Splitter:B:73:0x00d2] */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x00dc A[SYNTHETIC, Splitter:B:78:0x00dc] */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x00e3 A[SYNTHETIC, Splitter:B:83:0x00e3] */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x00ed A[SYNTHETIC, Splitter:B:88:0x00ed] */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x00f7 A[SYNTHETIC, Splitter:B:93:0x00f7] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:47:0x0077=Splitter:B:47:0x0077, B:65:0x00ae=Splitter:B:65:0x00ae} */
    /* JADX WARNING: Unknown variable types count: 9 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String readStringFromFile(File file) {
        ? r8;
        ? r4;
        FileInputStream fileInputStream;
        ? r82;
        ? r42;
        FileNotFoundException e;
        ? r43;
        IOException e2;
        ? r44;
        ? r45;
        ? bufferedReader;
        String str = TAG;
        String str2 = "";
        if (!file.exists()) {
            return str2;
        }
        try {
            fileInputStream = new FileInputStream(file);
            try {
                r82 = new InputStreamReader(fileInputStream);
                try {
                    bufferedReader = new BufferedReader(r82);
                    try {
                        StringBuilder sb = new StringBuilder();
                        while (true) {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                break;
                            }
                            sb.append("\n");
                            sb.append(readLine);
                        }
                        str2 = sb.toString();
                        try {
                            fileInputStream.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                        try {
                            r82.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    } catch (FileNotFoundException e5) {
                        e = e5;
                        r82 = r82;
                        r42 = r42;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("File not found: ");
                        sb2.append(e.toString());
                        Log.e(str, sb2.toString());
                        if (fileInputStream != null) {
                        }
                        if (r82 != 0) {
                        }
                        if (r42 != 0) {
                        }
                        return str2;
                    } catch (IOException e6) {
                        e2 = e6;
                        r42 = bufferedReader;
                        try {
                            r82 = r82;
                            r42 = r42;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("Can not read file: ");
                            sb3.append(e2.toString());
                            Log.e(str, sb3.toString());
                            if (fileInputStream != null) {
                            }
                            if (r82 != 0) {
                            }
                            if (r42 != 0) {
                            }
                            return str2;
                        } catch (Throwable th) {
                            th = th;
                            r8 = r82;
                            r4 = r42;
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (IOException e7) {
                                    e7.printStackTrace();
                                }
                            }
                            if (r8 != 0) {
                                try {
                                    r8.close();
                                } catch (IOException e8) {
                                    e8.printStackTrace();
                                }
                            }
                            if (r4 != 0) {
                                try {
                                    r4.close();
                                } catch (IOException e9) {
                                    e9.printStackTrace();
                                }
                            }
                            throw th;
                        }
                    }
                } catch (FileNotFoundException e10) {
                    FileNotFoundException fileNotFoundException = e10;
                    r44 = 0;
                    e = fileNotFoundException;
                    r82 = r82;
                    r42 = r42;
                    StringBuilder sb22 = new StringBuilder();
                    sb22.append("File not found: ");
                    sb22.append(e.toString());
                    Log.e(str, sb22.toString());
                    if (fileInputStream != null) {
                    }
                    if (r82 != 0) {
                    }
                    if (r42 != 0) {
                    }
                    return str2;
                } catch (IOException e11) {
                    IOException iOException = e11;
                    r42 = 0;
                    e2 = iOException;
                    r82 = r82;
                    r82 = r82;
                    r42 = r42;
                    StringBuilder sb32 = new StringBuilder();
                    sb32.append("Can not read file: ");
                    sb32.append(e2.toString());
                    Log.e(str, sb32.toString());
                    if (fileInputStream != null) {
                    }
                    if (r82 != 0) {
                    }
                    if (r42 != 0) {
                    }
                    return str2;
                } catch (Throwable th2) {
                    th = th2;
                    r4 = 0;
                    if (fileInputStream != null) {
                    }
                    if (r8 != 0) {
                    }
                    if (r4 != 0) {
                    }
                    throw th;
                }
            } catch (FileNotFoundException e12) {
                e = e12;
                r43 = 0;
                e = e;
                ? r83 = r43;
                r44 = r43;
                r82 = r82;
                r42 = r42;
                StringBuilder sb222 = new StringBuilder();
                sb222.append("File not found: ");
                sb222.append(e.toString());
                Log.e(str, sb222.toString());
                if (fileInputStream != null) {
                }
                if (r82 != 0) {
                }
                if (r42 != 0) {
                }
                return str2;
            } catch (IOException e13) {
                e = e13;
                r45 = 0;
                e2 = e;
                r82 = r44;
                r42 = r44;
                r82 = r82;
                r42 = r42;
                StringBuilder sb322 = new StringBuilder();
                sb322.append("Can not read file: ");
                sb322.append(e2.toString());
                Log.e(str, sb322.toString());
                if (fileInputStream != null) {
                }
                if (r82 != 0) {
                }
                if (r42 != 0) {
                }
                return str2;
            } catch (Throwable th3) {
                th = th3;
                r8 = 0;
                r4 = 0;
                if (fileInputStream != null) {
                }
                if (r8 != 0) {
                }
                if (r4 != 0) {
                }
                throw th;
            }
            try {
                bufferedReader.close();
            } catch (IOException e14) {
                e14.printStackTrace();
            }
        } catch (FileNotFoundException e15) {
            e = e15;
            FileInputStream fileInputStream2 = null;
            r43 = 0;
            e = e;
            ? r832 = r43;
            r44 = r43;
            r82 = r82;
            r42 = r42;
            StringBuilder sb2222 = new StringBuilder();
            sb2222.append("File not found: ");
            sb2222.append(e.toString());
            Log.e(str, sb2222.toString());
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e16) {
                    e16.printStackTrace();
                }
            }
            if (r82 != 0) {
                try {
                    r82.close();
                } catch (IOException e17) {
                    e17.printStackTrace();
                }
            }
            if (r42 != 0) {
                r42.close();
            }
            return str2;
        } catch (IOException e18) {
            e = e18;
            fileInputStream = null;
            r45 = 0;
            e2 = e;
            r82 = r44;
            r42 = r44;
            r82 = r82;
            r42 = r42;
            StringBuilder sb3222 = new StringBuilder();
            sb3222.append("Can not read file: ");
            sb3222.append(e2.toString());
            Log.e(str, sb3222.toString());
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e19) {
                    e19.printStackTrace();
                }
            }
            if (r82 != 0) {
                try {
                    r82.close();
                } catch (IOException e20) {
                    e20.printStackTrace();
                }
            }
            if (r42 != 0) {
                r42.close();
            }
            return str2;
        } catch (Throwable th4) {
            th = th4;
            r8 = 0;
            fileInputStream = null;
            r4 = 0;
            if (fileInputStream != null) {
            }
            if (r8 != 0) {
            }
            if (r4 != 0) {
            }
            throw th;
        }
        return str2;
    }

    public static void saveBitmap(Bitmap bitmap, String str) {
        String str2;
        String str3 = TAG;
        try {
            File file = new File(str);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            StringBuilder sb = new StringBuilder();
            sb.append("saveBitmap: ");
            sb.append(file.getAbsolutePath());
            str2 = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            str2 = "saveBitmap: return";
        }
        Log.d(str3, str2);
    }
}
