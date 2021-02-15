package com.android.gallery3d.exif;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.location.Location;
import android.media.MediaDataSource;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.text.TextUtils;
import com.android.camera.Util;
import com.android.camera.log.Log;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.TimeZone;

public class ExifHelper {
    private static final Charset ASCII = Charset.forName("US-ASCII");
    private static final int BYTES_COPY_BUFFER_LENGTH = 2048;
    private static final byte[] HEIF_BRAND_HEIC = {104, 101, 105, 99};
    private static final byte[] HEIF_BRAND_MIF1 = {109, 105, 102, 49};
    private static final byte[] HEIF_TYPE_FTYP = {102, 116, 121, 112};
    private static final byte[] IDENTIFIER_EXIF_APP1 = "Exif\u0000\u0000".getBytes(ASCII);
    public static final int IMAGE_TYPE_ARW = 1;
    public static final int IMAGE_TYPE_CR2 = 2;
    public static final int IMAGE_TYPE_DNG = 3;
    public static final int IMAGE_TYPE_HEIF = 12;
    public static final int IMAGE_TYPE_JPEG = 4;
    public static final int IMAGE_TYPE_NEF = 5;
    public static final int IMAGE_TYPE_NRW = 6;
    public static final int IMAGE_TYPE_ORF = 7;
    public static final int IMAGE_TYPE_PEF = 8;
    public static final int IMAGE_TYPE_RAF = 9;
    public static final int IMAGE_TYPE_RW2 = 10;
    public static final int IMAGE_TYPE_SRW = 11;
    public static final int IMAGE_TYPE_UNKNOWN = 0;
    private static final byte[] JPEG_SIGNATURE = {-1, -40, -1};
    public static final int SIGNATURE_CHECK_SIZE = 5000;
    private static final String TAG = "ExifHelper";

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

    private static void closeQuietly(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException unused) {
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:1|2|3|4|6) */
    /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0005 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void closeQuietly(OutputStream outputStream) {
        if (outputStream != null) {
            outputStream.flush();
            outputStream.close();
        }
    }

    private static long copy(InputStream inputStream, OutputStream outputStream) {
        byte[] bArr = new byte[2048];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
                j += (long) read;
            } else {
                outputStream.flush();
                return j;
            }
        }
    }

    static byte[] extractExifFromHeif(final ByteOrderedDataInputStream byteOrderedDataInputStream) {
        String str;
        String str2;
        String str3 = "yes";
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(new MediaDataSource() {
                long mPosition;

                public void close() {
                }

                public long getSize() {
                    return -1;
                }

                public int readAt(long j, byte[] bArr, int i, int i2) {
                    if (i2 == 0) {
                        return 0;
                    }
                    if (j < 0) {
                        return -1;
                    }
                    try {
                        if (this.mPosition != j) {
                            if (this.mPosition >= 0 && j >= this.mPosition + ((long) ByteOrderedDataInputStream.this.available())) {
                                return -1;
                            }
                            ByteOrderedDataInputStream.this.seek(j);
                            this.mPosition = j;
                        }
                        if (i2 > ByteOrderedDataInputStream.this.available()) {
                            i2 = ByteOrderedDataInputStream.this.available();
                        }
                        int read = ByteOrderedDataInputStream.this.read(bArr, i, i2);
                        if (read >= 0) {
                            this.mPosition += (long) read;
                            return read;
                        }
                    } catch (IOException unused) {
                    }
                    this.mPosition = -1;
                    return -1;
                }
            });
            String extractMetadata = mediaMetadataRetriever.extractMetadata(33);
            String extractMetadata2 = mediaMetadataRetriever.extractMetadata(34);
            String extractMetadata3 = mediaMetadataRetriever.extractMetadata(26);
            String extractMetadata4 = mediaMetadataRetriever.extractMetadata(17);
            String str4 = null;
            if (str3.equals(extractMetadata3)) {
                str4 = mediaMetadataRetriever.extractMetadata(29);
                str2 = mediaMetadataRetriever.extractMetadata(30);
                str = mediaMetadataRetriever.extractMetadata(31);
            } else if (str3.equals(extractMetadata4)) {
                str4 = mediaMetadataRetriever.extractMetadata(18);
                str2 = mediaMetadataRetriever.extractMetadata(19);
                str = mediaMetadataRetriever.extractMetadata(24);
            } else {
                str2 = null;
                str = null;
            }
            String str5 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Heif meta: ");
            sb.append(str4);
            sb.append("x");
            sb.append(str2);
            sb.append(", rotation ");
            sb.append(str);
            Log.d(str5, sb.toString());
            if (extractMetadata == null || extractMetadata2 == null) {
                mediaMetadataRetriever.release();
                return new byte[0];
            }
            int parseInt = Integer.parseInt(extractMetadata);
            int parseInt2 = Integer.parseInt(extractMetadata2);
            if (parseInt2 > 6) {
                long j = (long) parseInt;
                byteOrderedDataInputStream.seek(j);
                byte[] bArr = new byte[6];
                if (byteOrderedDataInputStream.read(bArr) != 6) {
                    throw new IOException("Can't read identifier");
                } else if (Arrays.equals(bArr, IDENTIFIER_EXIF_APP1)) {
                    byte[] bArr2 = new byte[(parseInt2 + 6)];
                    bArr2[0] = -1;
                    bArr2[1] = -40;
                    bArr2[2] = -1;
                    bArr2[3] = -31;
                    writeShortValue(bArr2, 4, (char) (parseInt2 + 2), ByteOrder.BIG_ENDIAN);
                    byteOrderedDataInputStream.seek(j);
                    if (byteOrderedDataInputStream.read(bArr2, 6, parseInt2) == parseInt2) {
                        return bArr2;
                    }
                    throw new IOException("Can't read exif");
                } else {
                    throw new IOException("Invalid identifier");
                }
            } else {
                throw new IOException("Invalid exif length");
            }
        } finally {
            mediaMetadataRetriever.release();
        }
    }

    static int getMimeType(BufferedInputStream bufferedInputStream) {
        bufferedInputStream.mark(5000);
        byte[] bArr = new byte[5000];
        bufferedInputStream.read(bArr);
        bufferedInputStream.reset();
        if (isJpegFormat(bArr)) {
            return 4;
        }
        return isHeifFormat(bArr) ? 12 : 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0032, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        $closeResource(r3, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0036, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getOrientation(byte[] bArr) {
        if (bArr == null) {
            return 0;
        }
        try {
            int mimeType = getMimeType(new BufferedInputStream(new ByteArrayInputStream(bArr)));
            if (mimeType == 4) {
                return getOrientationFromExif(bArr);
            }
            if (mimeType == 12) {
                try {
                    ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(bArr);
                    int orientationFromExif = getOrientationFromExif(extractExifFromHeif(byteOrderedDataInputStream));
                    $closeResource(null, byteOrderedDataInputStream);
                    return orientationFromExif;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return 0;
        } catch (IOException e2) {
            e2.printStackTrace();
            return 0;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0063, code lost:
        r1 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0064, code lost:
        r2 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0065, code lost:
        if (r2 <= 8) goto L_0x00d1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0067, code lost:
        r3 = pack(r11, r1, 4, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x006e, code lost:
        if (r3 == 1229531648) goto L_0x007b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0073, code lost:
        if (r3 == 1296891946) goto L_0x007b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0075, code lost:
        com.android.camera.log.Log.e(r5, "Invalid byte order");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x007a, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x007b, code lost:
        if (r3 != 1229531648) goto L_0x007f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x007d, code lost:
        r3 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x007f, code lost:
        r3 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0080, code lost:
        r4 = pack(r11, r1 + 4, 4, r3) + 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0089, code lost:
        if (r4 < 10) goto L_0x00cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x008b, code lost:
        if (r4 <= r2) goto L_0x008e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x008e, code lost:
        r1 = r1 + r4;
        r2 = r2 - r4;
        r4 = pack(r11, r1 - 2, 2, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0096, code lost:
        r9 = r4 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0098, code lost:
        if (r4 <= 0) goto L_0x00d1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x009c, code lost:
        if (r2 < 12) goto L_0x00d1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00a4, code lost:
        if (pack(r11, r1, 2, r3) != 274) goto L_0x00c5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00a6, code lost:
        r11 = pack(r11, r1 + 8, 2, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00ab, code lost:
        if (r11 == 1) goto L_0x00c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00ae, code lost:
        if (r11 == 3) goto L_0x00c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00b1, code lost:
        if (r11 == 6) goto L_0x00be;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00b3, code lost:
        if (r11 == 8) goto L_0x00bb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00b5, code lost:
        com.android.camera.log.Log.i(r5, "Unsupported orientation");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00ba, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00bd, code lost:
        return 270;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00c0, code lost:
        return 90;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x00c3, code lost:
        return 180;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00c4, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x00c5, code lost:
        r1 = r1 + 12;
        r2 = r2 - 12;
        r4 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x00cb, code lost:
        com.android.camera.log.Log.e(r5, "Invalid offset");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x00d0, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x00d1, code lost:
        com.android.camera.log.Log.i(r5, "Orientation not found");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x00d6, code lost:
        return 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int getOrientationFromExif(byte[] bArr) {
        String str;
        int i = 0;
        while (true) {
            int i2 = i + 3;
            int length = bArr.length;
            str = TAG;
            if (i2 >= length) {
                break;
            }
            int i3 = i + 1;
            if ((bArr[i] & -1) != -1) {
                break;
            }
            byte b = bArr[i3] & -1;
            if (b != -1) {
                i3++;
                if (!(b == -40 || b == 1)) {
                    if (b != -39 && b != -38) {
                        int pack = pack(bArr, i3, 2, false);
                        if (pack < 2) {
                            break;
                        }
                        int i4 = i3 + pack;
                        if (i4 <= bArr.length) {
                            if (b == -31 && pack >= 8 && pack(bArr, i3 + 2, 4, false) == 1165519206 && pack(bArr, i3 + 6, 2, false) == 0) {
                                i = i3 + 8;
                                int i5 = pack - 8;
                                break;
                            }
                            i = i4;
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            i = i3;
        }
        Log.e(str, "Invalid length");
        return 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:58:0x008d, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:?, code lost:
        $closeResource(r15, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0091, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean isHeifFormat(byte[] bArr) {
        try {
            ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(bArr);
            byteOrderedDataInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
            long readInt = (long) byteOrderedDataInputStream.readInt();
            byte[] bArr2 = new byte[4];
            byteOrderedDataInputStream.read(bArr2);
            if (!Arrays.equals(bArr2, HEIF_TYPE_FTYP)) {
                $closeResource(null, byteOrderedDataInputStream);
                return false;
            }
            long j = 16;
            if (readInt == 1) {
                readInt = byteOrderedDataInputStream.readLong();
                if (readInt < 16) {
                    $closeResource(null, byteOrderedDataInputStream);
                    return false;
                }
            } else {
                j = 8;
            }
            if (readInt > ((long) bArr.length)) {
                readInt = (long) bArr.length;
            }
            long j2 = readInt - j;
            if (j2 < 8) {
                $closeResource(null, byteOrderedDataInputStream);
                return false;
            }
            byte[] bArr3 = new byte[4];
            boolean z = false;
            boolean z2 = false;
            for (long j3 = 0; j3 < j2 / 4; j3++) {
                if (byteOrderedDataInputStream.read(bArr3) != bArr3.length) {
                    $closeResource(null, byteOrderedDataInputStream);
                    return false;
                }
                if (j3 != 1) {
                    if (Arrays.equals(bArr3, HEIF_BRAND_MIF1)) {
                        z = true;
                    } else if (Arrays.equals(bArr3, HEIF_BRAND_HEIC)) {
                        z2 = true;
                    }
                    if (z && z2) {
                        $closeResource(null, byteOrderedDataInputStream);
                        return true;
                    }
                }
            }
            $closeResource(null, byteOrderedDataInputStream);
            return false;
        } catch (Exception e) {
            Log.d(TAG, "Exception parsing HEIF file type box.", (Throwable) e);
        }
    }

    private static boolean isJpegFormat(byte[] bArr) {
        int i = 0;
        while (true) {
            byte[] bArr2 = JPEG_SIGNATURE;
            if (i >= bArr2.length) {
                return true;
            }
            if (bArr[i] != bArr2[i]) {
                return false;
            }
            i++;
        }
    }

    private static boolean isSeekableFD(FileDescriptor fileDescriptor) {
        try {
            Os.lseek(fileDescriptor, 0, OsConstants.SEEK_CUR);
            return true;
        } catch (ErrnoException unused) {
            return false;
        }
    }

    private static int pack(byte[] bArr, int i, int i2, boolean z) {
        int i3;
        if (z) {
            i += i2 - 1;
            i3 = -1;
        } else {
            i3 = 1;
        }
        byte b = 0;
        while (true) {
            int i4 = i2 - 1;
            if (i2 <= 0) {
                return b;
            }
            b = (bArr[i] & -1) | (b << 8);
            i += i3;
            i2 = i4;
        }
    }

    private static void setTagValue(ExifInterface exifInterface, int i, Object obj) {
        if (!exifInterface.setTagValue(i, obj)) {
            exifInterface.setTag(exifInterface.buildTag(i, obj));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:89:0x0151, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:?, code lost:
        $closeResource(r10, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x0155, code lost:
        throw r11;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void writeExifByFd(FileDescriptor fileDescriptor, int i, Location location, long j) {
        OutputStream outputStream;
        OutputStream outputStream2;
        OutputStream fileOutputStream;
        String str = TAG;
        if (fileDescriptor == null) {
            Log.e(str, "writeExifByFd: the given fd must not be null");
        } else if (!isSeekableFD(fileDescriptor)) {
            Log.e(str, "writeExifByFd: the given fd must be seekable");
        } else {
            ExifInterface exifInterface = new ExifInterface();
            try {
                FileInputStream fileInputStream = new FileInputStream(fileDescriptor);
                exifInterface.readExif((InputStream) fileInputStream);
                InputStream inputStream = null;
                $closeResource(null, fileInputStream);
                try {
                    setTagValue(exifInterface, ExifInterface.TAG_ORIENTATION, Short.valueOf(ExifInterface.getExifOrientationValue(i)));
                    setTagValue(exifInterface, ExifInterface.TAG_MAKE, Build.MANUFACTURER);
                    setTagValue(exifInterface, ExifInterface.TAG_MODEL, C0124O00000oO.MODULE);
                    if (!TextUtils.isEmpty(Util.MARKET_NAME)) {
                        exifInterface.addXiaomiProduct(Util.MARKET_NAME);
                    }
                    if (j > 0) {
                        exifInterface.addDateTimeStampTag(ExifInterface.TAG_DATE_TIME, j, TimeZone.getDefault());
                        exifInterface.addSubTagSecTime(ExifInterface.TAG_SUB_SEC_TIME, j, TimeZone.getDefault());
                    }
                    if (location != null) {
                        exifInterface.addGpsTags(location.getLatitude(), location.getLongitude());
                        exifInterface.addGpsDateTimeStampTag(location.getTime());
                        double altitude = location.getAltitude();
                        if (altitude != 0.0d) {
                            exifInterface.setTag(exifInterface.buildTag(ExifInterface.TAG_GPS_ALTITUDE_REF, Short.valueOf(altitude < 0.0d ? (short) 1 : 0)));
                        }
                    }
                    try {
                        File createTempFile = File.createTempFile("temp", "jpg");
                        Os.lseek(fileDescriptor, 0, OsConstants.SEEK_SET);
                        InputStream fileInputStream2 = new FileInputStream(fileDescriptor);
                        try {
                            fileOutputStream = new FileOutputStream(createTempFile);
                        } catch (ErrnoException | IOException e) {
                            e = e;
                            outputStream2 = null;
                            inputStream = fileInputStream2;
                            try {
                                Log.w(str, "writeExifByFd: failed to backup the original file", e);
                                closeQuietly(inputStream);
                                closeQuietly(outputStream);
                            } catch (Throwable th) {
                                th = th;
                                closeQuietly(inputStream);
                                closeQuietly(outputStream);
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            outputStream = null;
                            inputStream = fileInputStream2;
                            closeQuietly(inputStream);
                            closeQuietly(outputStream);
                            throw th;
                        }
                        try {
                            copy(fileInputStream2, fileOutputStream);
                            closeQuietly(fileInputStream2);
                            closeQuietly(fileOutputStream);
                            try {
                                Os.lseek(fileDescriptor, 0, OsConstants.SEEK_SET);
                                FileInputStream fileInputStream3 = new FileInputStream(createTempFile);
                                try {
                                    FileOutputStream fileOutputStream2 = new FileOutputStream(fileDescriptor);
                                    try {
                                        exifInterface.writeExif((InputStream) fileInputStream3, (OutputStream) fileOutputStream2);
                                        closeQuietly((InputStream) fileInputStream3);
                                        closeQuietly((OutputStream) fileOutputStream2);
                                    } catch (ErrnoException | IOException e2) {
                                        Throwable th3 = e2;
                                        fileOutputStream = fileOutputStream2;
                                        fileInputStream2 = fileInputStream3;
                                        e = th3;
                                        try {
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("writeExifByFd: failed update exif for ");
                                            sb.append(fileDescriptor);
                                            Log.w(str, sb.toString(), e);
                                            closeQuietly(fileInputStream2);
                                            closeQuietly(fileOutputStream);
                                            createTempFile.delete();
                                        } catch (Throwable th4) {
                                            th = th4;
                                            closeQuietly(fileInputStream2);
                                            closeQuietly(fileOutputStream);
                                            createTempFile.delete();
                                            throw th;
                                        }
                                    } catch (Throwable th5) {
                                        th = th5;
                                        fileOutputStream = fileOutputStream2;
                                        fileInputStream2 = fileInputStream3;
                                        closeQuietly(fileInputStream2);
                                        closeQuietly(fileOutputStream);
                                        createTempFile.delete();
                                        throw th;
                                    }
                                } catch (ErrnoException | IOException e3) {
                                    InputStream inputStream2 = fileInputStream3;
                                    e = e3;
                                    fileInputStream2 = inputStream2;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("writeExifByFd: failed update exif for ");
                                    sb2.append(fileDescriptor);
                                    Log.w(str, sb2.toString(), e);
                                    closeQuietly(fileInputStream2);
                                    closeQuietly(fileOutputStream);
                                    createTempFile.delete();
                                } catch (Throwable th6) {
                                    th = th6;
                                    fileInputStream2 = fileInputStream3;
                                    closeQuietly(fileInputStream2);
                                    closeQuietly(fileOutputStream);
                                    createTempFile.delete();
                                    throw th;
                                }
                            } catch (ErrnoException | IOException e4) {
                                e = e4;
                                StringBuilder sb22 = new StringBuilder();
                                sb22.append("writeExifByFd: failed update exif for ");
                                sb22.append(fileDescriptor);
                                Log.w(str, sb22.toString(), e);
                                closeQuietly(fileInputStream2);
                                closeQuietly(fileOutputStream);
                                createTempFile.delete();
                            }
                            createTempFile.delete();
                        } catch (ErrnoException | IOException e5) {
                            e = e5;
                            inputStream = fileInputStream2;
                            Log.w(str, "writeExifByFd: failed to backup the original file", e);
                            closeQuietly(inputStream);
                            closeQuietly(outputStream);
                        } catch (Throwable th7) {
                            th = th7;
                            inputStream = fileInputStream2;
                            closeQuietly(inputStream);
                            closeQuietly(outputStream);
                            throw th;
                        }
                    } catch (ErrnoException | IOException e6) {
                        e = e6;
                        outputStream2 = null;
                        Log.w(str, "writeExifByFd: failed to backup the original file", e);
                        closeQuietly(inputStream);
                        closeQuietly(outputStream);
                    } catch (Throwable th8) {
                        th = th8;
                        outputStream = null;
                        closeQuietly(inputStream);
                        closeQuietly(outputStream);
                        throw th;
                    }
                } catch (Exception e7) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("writeExifByFd: failed to update exif for ");
                    sb3.append(fileDescriptor);
                    Log.w(str, sb3.toString(), (Throwable) e7);
                }
            } catch (IOException e8) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("writeExifByFilePath: failed to extract exif fd ");
                sb4.append(fileDescriptor);
                Log.w(str, sb4.toString(), (Throwable) e8);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00ea, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
        $closeResource(r7, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00ee, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00f1, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:?, code lost:
        $closeResource(r7, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00f5, code lost:
        throw r10;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void writeExifByFilePath(String str, int i, Location location, long j) {
        boolean isEmpty = TextUtils.isEmpty(str);
        String str2 = TAG;
        if (isEmpty || !Util.isPathExist(str) || new File(str).length() == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("writeExifByFilePath: ");
            sb.append(str);
            sb.append(" does not exist or its content is empty");
            Log.e(str2, sb.toString());
            return;
        }
        ExifInterface exifInterface = new ExifInterface();
        try {
            exifInterface.readExif(str);
            try {
                setTagValue(exifInterface, ExifInterface.TAG_ORIENTATION, Short.valueOf(ExifInterface.getExifOrientationValue(i)));
                setTagValue(exifInterface, ExifInterface.TAG_MAKE, Build.MANUFACTURER);
                setTagValue(exifInterface, ExifInterface.TAG_MODEL, C0124O00000oO.MODULE);
                if (!TextUtils.isEmpty(Util.MARKET_NAME)) {
                    exifInterface.addXiaomiProduct(Util.MARKET_NAME);
                }
                if (j > 0) {
                    exifInterface.addDateTimeStampTag(ExifInterface.TAG_DATE_TIME, j, TimeZone.getDefault());
                    exifInterface.addSubTagSecTime(ExifInterface.TAG_SUB_SEC_TIME, j, TimeZone.getDefault());
                }
                if (location != null) {
                    exifInterface.addGpsTags(location.getLatitude(), location.getLongitude());
                    exifInterface.addGpsDateTimeStampTag(location.getTime());
                    double altitude = location.getAltitude();
                    if (altitude != 0.0d) {
                        exifInterface.setTag(exifInterface.buildTag(ExifInterface.TAG_GPS_ALTITUDE_REF, Short.valueOf(altitude < 0.0d ? (short) 1 : 0)));
                    }
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(".tmp");
                File file = new File(sb2.toString());
                if (!new File(str).renameTo(file)) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("writeExifByFilePath: failed rename to ");
                    sb3.append(file.getAbsolutePath());
                    Log.w(str2, sb3.toString());
                    return;
                }
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    FileOutputStream fileOutputStream = new FileOutputStream(str, false);
                    exifInterface.writeExif((InputStream) fileInputStream, (OutputStream) fileOutputStream);
                    $closeResource(null, fileOutputStream);
                    $closeResource(null, fileInputStream);
                } catch (IOException e) {
                    try {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("writeExifByFilePath: failed update exif for ");
                        sb4.append(str);
                        Log.w(str2, sb4.toString(), (Throwable) e);
                    } catch (Throwable th) {
                        file.delete();
                        throw th;
                    }
                }
                file.delete();
            } catch (Exception e2) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append("writeExifByFilePath: failed to update exif info for ");
                sb5.append(str);
                Log.w(str2, sb5.toString(), (Throwable) e2);
            }
        } catch (IOException e3) {
            StringBuilder sb6 = new StringBuilder();
            sb6.append("writeExifByFilePath: failed to extract exif from ");
            sb6.append(str);
            Log.w(str2, sb6.toString(), (Throwable) e3);
        }
    }

    private static void writeShortValue(byte[] bArr, int i, int i2, ByteOrder byteOrder) {
        ByteBuffer.wrap(bArr, i, 2).order(byteOrder == ByteOrder.BIG_ENDIAN ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN).putChar((char) i2);
    }
}
