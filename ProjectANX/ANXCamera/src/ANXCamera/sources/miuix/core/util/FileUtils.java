package miuix.core.util;

import com.android.camera.storage.Storage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import miuix.os.Native;

public class FileUtils {
    private static final Pattern SAFE_FILENAME_PATTERN = Pattern.compile("[\\w%+,./=_-]+");
    public static final int S_IRGRP = 32;
    public static final int S_IROTH = 4;
    public static final int S_IRUSR = 256;
    public static final int S_IRWXG = 56;
    public static final int S_IRWXO = 7;
    public static final int S_IRWXU = 448;
    public static final int S_IWGRP = 16;
    public static final int S_IWOTH = 2;
    public static final int S_IWUSR = 128;
    public static final int S_IXGRP = 8;
    public static final int S_IXOTH = 1;
    public static final int S_IXUSR = 64;

    protected FileUtils() {
        throw new InstantiationException("Cannot instantiate utility class");
    }

    public static boolean addNoMedia(String str) {
        File file = new File(str);
        if (file.isDirectory()) {
            try {
                return new File(file, Storage.AVOID_SCAN_FILE_NAME).createNewFile();
            } catch (IOException unused) {
            }
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0029 A[SYNTHETIC, Splitter:B:18:0x0029] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static long checksumCrc32(File file) {
        CRC32 crc32 = new CRC32();
        CheckedInputStream checkedInputStream = null;
        try {
            CheckedInputStream checkedInputStream2 = new CheckedInputStream(new FileInputStream(file), crc32);
            try {
                while (checkedInputStream2.read(new byte[128]) >= 0) {
                }
                long value = crc32.getValue();
                try {
                    checkedInputStream2.close();
                } catch (IOException unused) {
                }
                return value;
            } catch (Throwable th) {
                th = th;
                checkedInputStream = checkedInputStream2;
                if (checkedInputStream != null) {
                    try {
                        checkedInputStream.close();
                    } catch (IOException unused2) {
                    }
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            if (checkedInputStream != null) {
            }
            throw th;
        }
    }

    public static boolean chmod(String str, int i) {
        if (str == null || str.length() == 0) {
            return false;
        }
        if (i < 0) {
            return true;
        }
        return Native.chmod(str, i, 1);
    }

    public static boolean chown(String str, int i, int i2) {
        if (str == null || str.length() == 0) {
            return false;
        }
        if (i < 0 && i2 < 0) {
            return true;
        }
        if (i < 0) {
            i = -1;
        } else if (i2 < 0) {
            i2 = -1;
        }
        return Native.chown(str, i, i2, 1);
    }

    public static boolean copyFile(File file, File file2) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
            boolean copyToFile = copyToFile(fileInputStream, file2);
            fileInputStream.close();
            return copyToFile;
        } catch (IOException unused) {
            return false;
        } catch (Throwable th) {
            fileInputStream.close();
            throw th;
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(11:7|8|9|10|(2:11|(2:13|14)(1:15))|16|17|18|19|20|21) */
    /* JADX WARNING: Can't wrap try/catch for region: R(7:22|23|24|25|26|27|28) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x002b */
    /* JADX WARNING: Missing exception handler attribute for start block: B:26:0x003b */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:19:0x002b=Splitter:B:19:0x002b, B:26:0x003b=Splitter:B:26:0x003b} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean copyToFile(InputStream inputStream, File file) {
        FileOutputStream fileOutputStream;
        try {
            if (file.exists() && !file.delete()) {
                return false;
            }
            fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[4096];
            while (true) {
                int read = inputStream.read(bArr);
                if (read < 0) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
            }
            fileOutputStream.flush();
            fileOutputStream.getFD().sync();
            fileOutputStream.close();
            return true;
        } catch (IOException unused) {
            return false;
        } catch (Throwable th) {
            fileOutputStream.flush();
            fileOutputStream.getFD().sync();
            fileOutputStream.close();
            throw th;
        }
    }

    public static String getExtension(String str) {
        String str2 = "";
        if (!(str == null || str.length() == 0)) {
            int lastIndexOf = str.lastIndexOf(46);
            if (lastIndexOf > -1) {
                return str.substring(lastIndexOf + 1);
            }
        }
        return str2;
    }

    public static String getFileName(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf(File.separatorChar);
        if (lastIndexOf > -1) {
            str = str.substring(lastIndexOf + 1);
        }
        return str;
    }

    public static long getFolderSize(File file) {
        long j = 0;
        if (!file.exists()) {
            return 0;
        }
        if (!file.isDirectory()) {
            return file.length();
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File folderSize : listFiles) {
                j += getFolderSize(folderSize);
            }
        }
        return j;
    }

    public static String getName(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf(File.separatorChar);
        if (lastIndexOf < 0) {
            lastIndexOf = -1;
        }
        int lastIndexOf2 = str.lastIndexOf(".");
        int i = lastIndexOf + 1;
        return lastIndexOf2 < 0 ? str.substring(i) : str.substring(i, lastIndexOf2);
    }

    public static boolean isFilenameSafe(File file) {
        return SAFE_FILENAME_PATTERN.matcher(file.getPath()).matches();
    }

    public static boolean mkdirs(File file, int i, int i2, int i3) {
        if (file.exists()) {
            return file.isDirectory();
        }
        boolean z = false;
        if (!mkdirs(file.getParentFile(), i, i2, i3)) {
            return false;
        }
        if (file.mkdir() && chmod(file.getPath(), i) && chown(file.getPath(), i2, i3)) {
            z = true;
        }
        return z;
    }

    public static String normalizeDirectoryName(String str) {
        if (str.charAt(str.length() - 1) == File.separatorChar) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(File.separator);
        return sb.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0030 A[SYNTHETIC, Splitter:B:20:0x0030] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] readFileAsBytes(String str) {
        RandomAccessFile randomAccessFile;
        try {
            randomAccessFile = new RandomAccessFile(str, "r");
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream((int) randomAccessFile.length());
                byte[] bArr = new byte[8192];
                while (true) {
                    int read = randomAccessFile.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                try {
                    randomAccessFile.close();
                } catch (IOException unused) {
                }
                return byteArray;
            } catch (Throwable th) {
                th = th;
                if (randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (IOException unused2) {
                    }
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            randomAccessFile = null;
            if (randomAccessFile != null) {
            }
            throw th;
        }
    }

    public static String readFileAsString(String str) {
        return new String(readFileAsBytes(str), Charset.forName("UTF-8"));
    }

    public static String readTextFile(File file, int i, String str) {
        int read;
        int read2;
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            long length = file.length();
            String str2 = "";
            if (i > 0 || (length > 0 && i == 0)) {
                if (length > 0 && (i == 0 || length < ((long) i))) {
                    i = (int) length;
                }
                byte[] bArr = new byte[(i + 1)];
                int read3 = fileInputStream.read(bArr);
                if (read3 <= 0) {
                    fileInputStream.close();
                    return str2;
                } else if (read3 <= i) {
                    String str3 = new String(bArr, 0, read3);
                    fileInputStream.close();
                    return str3;
                } else if (str == null) {
                    String str4 = new String(bArr, 0, i);
                    fileInputStream.close();
                    return str4;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(new String(bArr, 0, i));
                    sb.append(str);
                    String sb2 = sb.toString();
                    fileInputStream.close();
                    return sb2;
                }
            } else if (i < 0) {
                byte[] bArr2 = null;
                byte[] bArr3 = null;
                boolean z = false;
                while (true) {
                    if (bArr2 != null) {
                        z = true;
                    }
                    if (bArr2 == null) {
                        bArr2 = new byte[(-i)];
                    }
                    read2 = fileInputStream.read(bArr2);
                    if (read2 != bArr2.length) {
                        break;
                    }
                    byte[] bArr4 = bArr3;
                    bArr3 = bArr2;
                    bArr2 = bArr4;
                }
                if (bArr3 == null && read2 <= 0) {
                    return str2;
                }
                if (bArr3 == null) {
                    String str5 = new String(bArr2, 0, read2);
                    fileInputStream.close();
                    return str5;
                }
                if (read2 > 0) {
                    System.arraycopy(bArr3, read2, bArr3, 0, bArr3.length - read2);
                    System.arraycopy(bArr2, 0, bArr3, bArr3.length - read2, read2);
                    z = true;
                }
                if (str != null) {
                    if (z) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(str);
                        sb3.append(new String(bArr3));
                        String sb4 = sb3.toString();
                        fileInputStream.close();
                        return sb4;
                    }
                }
                String str6 = new String(bArr3);
                fileInputStream.close();
                return str6;
            } else {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bArr5 = new byte[1024];
                do {
                    read = fileInputStream.read(bArr5);
                    if (read > 0) {
                        byteArrayOutputStream.write(bArr5, 0, read);
                    }
                } while (read == bArr5.length);
                String byteArrayOutputStream2 = byteArrayOutputStream.toString();
                fileInputStream.close();
                return byteArrayOutputStream2;
            }
        } finally {
            fileInputStream.close();
        }
    }

    public static boolean rm(String str) {
        return Native.rm(str, 1);
    }

    public static void stringToFile(String str, String str2) {
        FileWriter fileWriter = new FileWriter(str);
        try {
            fileWriter.write(str2);
        } finally {
            fileWriter.close();
        }
    }

    public static boolean sync(FileOutputStream fileOutputStream) {
        if (fileOutputStream != null) {
            try {
                fileOutputStream.getFD().sync();
            } catch (IOException unused) {
                return false;
            }
        }
        return true;
    }

    public static int umask(int i) {
        return Native.umask(i);
    }
}
