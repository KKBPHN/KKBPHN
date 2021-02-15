package miui.autoinstall.config.utils;

import android.text.TextUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class FileUtil {
    private static /* synthetic */ void $closeResource(Throwable th, AutoCloseable autoCloseable) {
        if (th != null) {
            try {
                autoCloseable.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        } else {
            autoCloseable.close();
        }
    }

    private FileUtil() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002c, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        $closeResource(r4, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0030, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String getFileContent(String str) {
        File file = new File(str);
        if (!file.exists()) {
            return null;
        }
        byte[] bArr = new byte[((int) file.length())];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bArr);
            fileInputStream.close();
            String str2 = new String(bArr, StandardCharsets.UTF_8);
            $closeResource(null, fileInputStream);
            return str2;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFileNameFromUrl(String str) {
        if (!TextUtils.isEmpty(str)) {
            int lastIndexOf = str.lastIndexOf(35);
            if (lastIndexOf > 0) {
                str = str.substring(0, lastIndexOf);
            }
            int lastIndexOf2 = str.lastIndexOf(63);
            if (lastIndexOf2 > 0) {
                str = str.substring(0, lastIndexOf2);
            }
            int lastIndexOf3 = str.lastIndexOf(47);
            if (lastIndexOf3 >= 0) {
                str = str.substring(lastIndexOf3 + 1);
            }
            if (!TextUtils.isEmpty(str)) {
                try {
                    return URLDecoder.decode(str, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    private static void makeFilePath(String str, String str2) {
        makeRootDirectory(str);
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(str2);
            File file = new File(sb.toString());
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void makeRootDirectory(String str) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004d, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        $closeResource(r3, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0051, code lost:
        throw r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void writeContentToFile(String str, String str2, String str3) {
        makeFilePath(str2, str3);
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(File.separator);
        sb.append(str3);
        try {
            File file = new File(sb.toString());
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            } else {
                file.delete();
            }
            file.createNewFile();
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.seek(file.length());
            randomAccessFile.write(str.getBytes());
            $closeResource(null, randomAccessFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
