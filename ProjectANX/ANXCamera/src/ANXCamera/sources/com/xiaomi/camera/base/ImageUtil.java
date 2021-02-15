package com.xiaomi.camera.base;

import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.media.Image;
import android.media.Image.Plane;
import android.util.Size;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.xiaomi.camera.imagecodec.ImagePool;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Locale;
import libcore.io.Memory;

public final class ImageUtil {
    private static final String TAG = "ImageUtil";

    private ImageUtil() {
    }

    private static void directByteBufferCopy(ByteBuffer byteBuffer, int i, ByteBuffer byteBuffer2, int i2, int i3) {
        Memory.memmove(byteBuffer2, i2, byteBuffer, i, (long) i3);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:122:0x0451, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:123:0x0452, code lost:
        r1 = r0;
        r18 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:124:0x0456, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:125:0x0457, code lost:
        r11 = false;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x0451 A[ExcHandler: all (r0v13 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:111:0x03d6] */
    /* JADX WARNING: Removed duplicated region for block: B:144:0x047d A[SYNTHETIC, Splitter:B:144:0x047d] */
    /* JADX WARNING: Removed duplicated region for block: B:152:0x0493 A[SYNTHETIC, Splitter:B:152:0x0493] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x01f2 A[SYNTHETIC, Splitter:B:34:0x01f2] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0202 A[SYNTHETIC, Splitter:B:41:0x0202] */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0322 A[SYNTHETIC, Splitter:B:76:0x0322] */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x032d A[SYNTHETIC, Splitter:B:81:0x032d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean dumpImage(Image image, String str) {
        boolean z;
        FileOutputStream fileOutputStream;
        String str2;
        boolean z2;
        Throwable th;
        boolean z3;
        ByteBuffer buffer;
        ByteBuffer buffer2;
        int width;
        int height;
        int rowStride;
        Locale locale;
        String str3;
        Object[] objArr;
        String sb;
        Throwable th2;
        Throwable th3;
        IOException iOException;
        String sb2;
        FileOutputStream fileOutputStream2;
        IOException iOException2;
        Throwable th4;
        Throwable th5;
        int height2;
        int rowStride2;
        int rowStride3;
        int rowStride4;
        String sb3;
        FileOutputStream fileOutputStream3;
        String str4 = str;
        String str5 = " ";
        Log.d(TAG, "dumpImage: E");
        int format = image.getFormat();
        String str6 = "dumpImage: fileName=";
        String str7 = "_";
        String str8 = "";
        String str9 = "x";
        String str10 = "dumpImage: size=";
        String str11 = "Failed to write image";
        String str12 = "Failed to flush/close stream";
        FileOutputStream fileOutputStream4 = null;
        if (format != 17) {
            if (format == 32) {
                String str13 = str8;
                try {
                    int width2 = image.getWidth();
                    int height3 = image.getHeight();
                    String str14 = TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(str10);
                    sb4.append(width2);
                    sb4.append(str9);
                    sb4.append(height3);
                    Log.d(str14, sb4.toString());
                    int rowStride5 = image.getPlanes()[0].getRowStride();
                    Plane[] planes = image.getPlanes();
                    for (int i = 0; i < planes.length; i++) {
                        String str15 = TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("dumpImage: planes[");
                        sb5.append(i);
                        sb5.append("]=");
                        sb5.append(planes[i].getRowStride());
                        sb5.append("|");
                        sb5.append(planes[i].getPixelStride());
                        Log.d(str15, sb5.toString());
                    }
                    Locale locale2 = Locale.ENGLISH;
                    String str16 = "sdcard/DCIM/Camera/%d_stride%d_width%d_height%d_f%x%s.raw";
                    Object[] objArr2 = new Object[6];
                    objArr2[0] = Long.valueOf(image.getTimestamp());
                    objArr2[1] = Integer.valueOf(rowStride5);
                    objArr2[2] = Integer.valueOf(width2);
                    objArr2[3] = Integer.valueOf(height3);
                    objArr2[4] = Integer.valueOf(image.getFormat());
                    if (str4 == null) {
                        sb2 = str13;
                    } else {
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append(str7);
                        sb6.append(str4);
                        sb2 = sb6.toString();
                    }
                    objArr2[5] = sb2;
                    String format2 = String.format(locale2, str16, objArr2);
                    String str17 = TAG;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(str6);
                    sb7.append(format2);
                    Log.v(str17, sb7.toString());
                    fileOutputStream2 = new FileOutputStream(format2, true);
                } catch (IOException e) {
                    th3 = e;
                    try {
                        Log.e(TAG, str11, th3);
                        if (fileOutputStream4 != null) {
                            try {
                                fileOutputStream4.close();
                            } catch (IOException e2) {
                                iOException = e2;
                            }
                        }
                        z = false;
                        Log.d(TAG, "dumpImage: X");
                        return z;
                    } catch (Throwable th6) {
                        th2 = th6;
                        if (fileOutputStream4 != null) {
                        }
                        throw th2;
                    }
                }
                try {
                    ByteBuffer buffer3 = image.getPlanes()[0].getBuffer();
                    int limit = buffer3.limit();
                    byte[] bArr = new byte[limit];
                    buffer3.get(bArr, 0, limit);
                    fileOutputStream2.write(bArr);
                    buffer3.rewind();
                    fileOutputStream2.flush();
                    try {
                        fileOutputStream2.close();
                    } catch (IOException e3) {
                        iOException2 = e3;
                    }
                } catch (IOException e4) {
                    th3 = e4;
                    fileOutputStream4 = fileOutputStream2;
                    Log.e(TAG, str11, th3);
                    if (fileOutputStream4 != null) {
                    }
                    z = false;
                    Log.d(TAG, "dumpImage: X");
                    return z;
                } catch (Throwable th7) {
                    th2 = th7;
                    fileOutputStream4 = fileOutputStream2;
                    if (fileOutputStream4 != null) {
                        try {
                            fileOutputStream4.close();
                        } catch (IOException e5) {
                            Log.e(TAG, str12, (Throwable) e5);
                        }
                    }
                    throw th2;
                }
            } else if (format != 35) {
                if (format == 842094169) {
                    try {
                        int width3 = image.getWidth();
                        height2 = image.getHeight();
                        rowStride2 = image.getPlanes()[0].getRowStride();
                        rowStride3 = image.getPlanes()[1].getRowStride();
                        rowStride4 = image.getPlanes()[2].getRowStride();
                        String str18 = TAG;
                        String str19 = str8;
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append(str10);
                        sb8.append(width3);
                        sb8.append(str9);
                        sb8.append(height2);
                        sb8.append(" rowStride=[");
                        sb8.append(rowStride2);
                        sb8.append(str5);
                        sb8.append(rowStride3);
                        sb8.append(str5);
                        sb8.append(rowStride4);
                        sb8.append("] pixelStride=");
                        sb8.append(image.getPlanes()[0].getPixelStride());
                        sb8.append(str5);
                        sb8.append(image.getPlanes()[1].getPixelStride());
                        sb8.append(str5);
                        sb8.append(image.getPlanes()[2].getPixelStride());
                        Log.d(str18, sb8.toString());
                        Locale locale3 = Locale.ENGLISH;
                        String str20 = "sdcard/DCIM/Camera/%d_stride%d_width%d_height%d_f%x%s.yuv";
                        Object[] objArr3 = new Object[6];
                        objArr3[0] = Long.valueOf(image.getTimestamp());
                        objArr3[1] = Integer.valueOf(rowStride2);
                        objArr3[2] = Integer.valueOf(width3);
                        objArr3[3] = Integer.valueOf(height2);
                        objArr3[4] = Integer.valueOf(image.getFormat());
                        if (str4 == null) {
                            sb3 = str19;
                        } else {
                            StringBuilder sb9 = new StringBuilder();
                            sb9.append(str7);
                            sb9.append(str4);
                            sb3 = sb9.toString();
                        }
                        objArr3[5] = sb3;
                        String format3 = String.format(locale3, str20, objArr3);
                        String str21 = TAG;
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append(str6);
                        sb10.append(format3);
                        Log.v(str21, sb10.toString());
                        fileOutputStream3 = new FileOutputStream(format3, true);
                    } catch (IOException e6) {
                        th5 = e6;
                        try {
                            Log.e(TAG, str11, th5);
                            if (fileOutputStream4 != null) {
                            }
                            z = false;
                            Log.d(TAG, "dumpImage: X");
                            return z;
                        } catch (Throwable th8) {
                            th4 = th8;
                            if (fileOutputStream4 != null) {
                                try {
                                    fileOutputStream4.close();
                                } catch (IOException e7) {
                                    Log.e(TAG, str12, (Throwable) e7);
                                }
                            }
                            throw th4;
                        }
                    }
                    try {
                        ByteBuffer buffer4 = image.getPlanes()[0].getBuffer();
                        ByteBuffer buffer5 = image.getPlanes()[1].getBuffer();
                        ByteBuffer buffer6 = image.getPlanes()[2].getBuffer();
                        int i2 = rowStride2 * height2;
                        int i3 = (rowStride3 * height2) >> 1;
                        int i4 = (rowStride4 * height2) >> 1;
                        int limit2 = buffer4.limit();
                        int limit3 = buffer5.limit();
                        int limit4 = buffer6.limit();
                        String str22 = TAG;
                        StringBuilder sb11 = new StringBuilder();
                        sb11.append("dumpImage: ysize=");
                        sb11.append(i2);
                        sb11.append(" usize=");
                        sb11.append(i3);
                        sb11.append(" vsize=");
                        sb11.append(i4);
                        Log.d(str22, sb11.toString());
                        String str23 = TAG;
                        StringBuilder sb12 = new StringBuilder();
                        sb12.append("dumpImage: yBufferSize=");
                        sb12.append(limit2);
                        sb12.append(" uBufferSize=");
                        sb12.append(limit3);
                        sb12.append(" vBufferSize=");
                        sb12.append(limit4);
                        Log.d(str23, sb12.toString());
                        int i5 = i3 + i2;
                        byte[] bArr2 = new byte[(i4 + i5)];
                        buffer4.rewind();
                        buffer5.rewind();
                        buffer6.rewind();
                        buffer4.get(bArr2, 0, limit2);
                        buffer5.get(bArr2, i2, limit3);
                        buffer6.get(bArr2, i5, limit4);
                        fileOutputStream3.write(bArr2);
                        buffer4.rewind();
                        buffer5.rewind();
                        buffer6.rewind();
                        fileOutputStream3.flush();
                        try {
                            fileOutputStream3.close();
                        } catch (IOException e8) {
                            iOException2 = e8;
                        }
                    } catch (IOException e9) {
                        th5 = e9;
                        fileOutputStream4 = fileOutputStream3;
                        Log.e(TAG, str11, th5);
                        if (fileOutputStream4 != null) {
                            try {
                                fileOutputStream4.close();
                            } catch (IOException e10) {
                                iOException = e10;
                            }
                        }
                        z = false;
                        Log.d(TAG, "dumpImage: X");
                        return z;
                    } catch (Throwable th9) {
                        th4 = th9;
                        fileOutputStream4 = fileOutputStream3;
                        if (fileOutputStream4 != null) {
                        }
                        throw th4;
                    }
                }
                z = false;
                Log.d(TAG, "dumpImage: X");
                return z;
            }
            z = true;
            Log.d(TAG, "dumpImage: X");
            return z;
        }
        String str24 = str8;
        try {
            try {
                buffer = image.getPlanes()[0].getBuffer();
                buffer2 = image.getPlanes()[2].getBuffer();
                width = image.getWidth();
                height = image.getHeight();
            } catch (IOException e11) {
                e = e11;
                z3 = false;
                str2 = str11;
                th = e;
                try {
                    Log.e(TAG, str2, th);
                    if (fileOutputStream != null) {
                    }
                    z = z2;
                    Log.d(TAG, "dumpImage: X");
                    return z;
                } catch (Throwable th10) {
                    Throwable th11 = th10;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e12) {
                            Log.e(TAG, str12, (Throwable) e12);
                        }
                    }
                    throw th11;
                }
            }
            try {
                rowStride = image.getPlanes()[0].getRowStride();
                locale = Locale.ENGLISH;
                str3 = "sdcard/DCIM/Camera/%d_stride%d_width%d_height%d_f%x%s.yuv";
                objArr = new Object[6];
            } catch (IOException e13) {
                e = e13;
                z3 = false;
                str2 = str11;
                th = e;
                Log.e(TAG, str2, th);
                if (fileOutputStream != null) {
                }
                z = z2;
                Log.d(TAG, "dumpImage: X");
                return z;
            }
            try {
                objArr[0] = Long.valueOf(image.getTimestamp());
                objArr[1] = Integer.valueOf(rowStride);
                objArr[2] = Integer.valueOf(width);
                objArr[3] = Integer.valueOf(height);
                objArr[4] = Integer.valueOf(image.getFormat());
                if (str4 == null) {
                    str2 = str11;
                    sb = str24;
                } else {
                    str2 = str11;
                    try {
                        StringBuilder sb13 = new StringBuilder();
                        sb13.append(str7);
                        sb13.append(str4);
                        sb = sb13.toString();
                    } catch (IOException e14) {
                        e = e14;
                        z3 = false;
                        th = e;
                        Log.e(TAG, str2, th);
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e15) {
                                Log.e(TAG, str12, (Throwable) e15);
                            }
                        }
                        z = z2;
                        Log.d(TAG, "dumpImage: X");
                        return z;
                    }
                }
                objArr[5] = sb;
                String format4 = String.format(locale, str3, objArr);
                String str25 = TAG;
                StringBuilder sb14 = new StringBuilder();
                sb14.append(str6);
                sb14.append(format4);
                Log.v(str25, sb14.toString());
                FileOutputStream fileOutputStream5 = new FileOutputStream(format4, true);
                int i6 = height * rowStride;
                int i7 = i6 >> 1;
                try {
                    int limit5 = buffer.limit();
                    int limit6 = buffer2.limit();
                    buffer.rewind();
                    buffer2.rewind();
                    String str26 = TAG;
                    StringBuilder sb15 = new StringBuilder();
                    sb15.append(str10);
                    sb15.append(image.getWidth());
                    sb15.append(str9);
                    sb15.append(image.getHeight());
                    sb15.append(" stride=");
                    sb15.append(image.getPlanes()[2].getRowStride());
                    Log.d(str26, sb15.toString());
                    byte[] bArr3 = new byte[(i7 + i6)];
                    buffer.rewind();
                    buffer2.rewind();
                    z2 = false;
                    buffer.get(bArr3, 0, limit5);
                    buffer2.get(bArr3, i6, limit6);
                    int i8 = rowStride - width;
                    bArr3[(bArr3.length - 1) - i8] = bArr3[(bArr3.length - 3) - i8];
                    fileOutputStream5.write(bArr3);
                    buffer.rewind();
                    buffer2.rewind();
                    fileOutputStream5.flush();
                    try {
                        fileOutputStream5.close();
                    } catch (IOException e16) {
                        Log.e(TAG, str12, (Throwable) e16);
                    }
                    z = true;
                } catch (IOException e17) {
                    e = e17;
                    th = e;
                    fileOutputStream = fileOutputStream5;
                    Log.e(TAG, str2, th);
                    if (fileOutputStream != null) {
                    }
                    z = z2;
                    Log.d(TAG, "dumpImage: X");
                    return z;
                } catch (Throwable th12) {
                }
            } catch (IOException e18) {
                e = e18;
                str2 = str11;
                z3 = false;
                th = e;
                Log.e(TAG, str2, th);
                if (fileOutputStream != null) {
                }
                z = z2;
                Log.d(TAG, "dumpImage: X");
                return z;
            }
        } catch (IOException e19) {
            e = e19;
            str2 = str11;
            z3 = false;
            th = e;
            Log.e(TAG, str2, th);
            if (fileOutputStream != null) {
            }
            z = z2;
            Log.d(TAG, "dumpImage: X");
            return z;
        }
        Log.d(TAG, "dumpImage: X");
        return z;
        Log.e(TAG, str12, (Throwable) iOException2);
        z = true;
        Log.d(TAG, "dumpImage: X");
        return z;
        Log.e(TAG, str12, (Throwable) iOException);
        z = false;
        Log.d(TAG, "dumpImage: X");
        return z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0119 A[SYNTHETIC, Splitter:B:24:0x0119] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0127 A[SYNTHETIC, Splitter:B:29:0x0127] */
    /* JADX WARNING: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean dumpYuvImageAppendWH(Image image, String str) {
        String str2 = str;
        String str3 = "Failed to flush/close stream";
        String str4 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("dumpYuvImageAppendWH start :");
        sb.append(str2);
        Log.d(str4, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("sdcard/DCIM/Camera/");
        sb2.append(str2);
        String sb3 = sb2.toString();
        int format = image.getFormat();
        if (format != 17 && format != 35) {
            return false;
        }
        FileOutputStream fileOutputStream = null;
        try {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(sb3);
            sb4.append(".yuv");
            FileOutputStream fileOutputStream2 = new FileOutputStream(sb4.toString(), true);
            try {
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                ByteBuffer buffer2 = image.getPlanes()[2].getBuffer();
                int width = image.getWidth();
                int height = image.getHeight();
                int rowStride = image.getPlanes()[0].getRowStride();
                int i = rowStride * height;
                int i2 = i >> 1;
                int limit = buffer.limit();
                int limit2 = buffer2.limit();
                buffer.rewind();
                buffer2.rewind();
                String str5 = TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("dumpYuvImageAppendWH: size=");
                sb5.append(image.getWidth());
                sb5.append("x");
                sb5.append(image.getHeight());
                sb5.append(" stride=");
                sb5.append(image.getPlanes()[2].getRowStride());
                Log.d(str5, sb5.toString());
                byte[] bArr = new byte[(i2 + i)];
                buffer.rewind();
                buffer2.rewind();
                buffer.get(bArr, 0, limit);
                buffer2.get(bArr, i, limit2);
                int i3 = rowStride - width;
                bArr[(bArr.length - 1) - i3] = bArr[(bArr.length - 3) - i3];
                fileOutputStream2.write(toBytes(width));
                fileOutputStream2.write(toBytes(height));
                fileOutputStream2.write(bArr);
                buffer.rewind();
                buffer2.rewind();
                fileOutputStream2.flush();
                try {
                    fileOutputStream2.close();
                } catch (Exception e) {
                    Log.e(TAG, str3, (Throwable) e);
                }
                return true;
            } catch (Exception e2) {
                e = e2;
                fileOutputStream = fileOutputStream2;
                try {
                    Log.e(TAG, "Failed to write image", (Throwable) e);
                    if (fileOutputStream != null) {
                    }
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream2 = fileOutputStream;
                    Throwable th2 = th;
                    if (fileOutputStream2 != null) {
                        try {
                            fileOutputStream2.close();
                        } catch (Exception e3) {
                            Log.e(TAG, str3, (Throwable) e3);
                        }
                    }
                    throw th2;
                }
            } catch (Throwable th3) {
                th = th3;
                Throwable th22 = th;
                if (fileOutputStream2 != null) {
                }
                throw th22;
            }
        } catch (Exception e4) {
            e = e4;
            Log.e(TAG, "Failed to write image", (Throwable) e);
            if (fileOutputStream != null) {
                return false;
            }
            try {
                fileOutputStream.close();
                return false;
            } catch (Exception e5) {
                Log.e(TAG, str3, (Throwable) e5);
                return false;
            }
        }
    }

    private static Size getEffectivePlaneSizeForImage(Image image, int i) {
        int format = image.getFormat();
        if (!(format == 1 || format == 2 || format == 3 || format == 4)) {
            if (format == 16) {
                return i == 0 ? new Size(image.getWidth(), image.getHeight()) : new Size(image.getWidth(), image.getHeight() / 2);
            }
            if (format != 17) {
                if (!(format == 20 || format == 32 || format == 256)) {
                    if (format != 842094169) {
                        if (format == 34) {
                            return new Size(0, 0);
                        }
                        if (format != 35) {
                            if (!(format == 37 || format == 38)) {
                                throw new UnsupportedOperationException(String.format("Invalid image format %d", new Object[]{Integer.valueOf(image.getFormat())}));
                            }
                        }
                    }
                }
            }
            return i == 0 ? new Size(image.getWidth(), image.getHeight()) : new Size(image.getWidth() / 2, image.getHeight() / 2);
        }
        return new Size(image.getWidth(), image.getHeight());
    }

    public static byte[] getFirstPlane(Image image) {
        Plane[] planes = image.getPlanes();
        if (planes.length <= 0) {
            return null;
        }
        ByteBuffer buffer = planes[0].getBuffer();
        byte[] bArr = new byte[buffer.remaining()];
        buffer.get(bArr);
        return bArr;
    }

    public static byte[] getYUVFromPreviewImage(Image image) {
        int i;
        int i2;
        if (isPreviewFormatSupported(image)) {
            Rect cropRect = image.getCropRect();
            int format = image.getFormat();
            int width = cropRect.width();
            int height = cropRect.height();
            Plane[] planes = image.getPlanes();
            int i3 = width * height;
            byte[] bArr = new byte[((ImageFormat.getBitsPerPixel(format) * i3) / 8)];
            int i4 = 0;
            byte[] bArr2 = new byte[planes[0].getRowStride()];
            int i5 = 1;
            int i6 = 0;
            int i7 = 0;
            int i8 = 1;
            while (i6 < planes.length) {
                if (i6 != 0) {
                    if (i6 == i5) {
                        i7 = i3 + 1;
                    } else if (i6 == 2) {
                        i7 = i3;
                    }
                    i8 = 2;
                } else {
                    i7 = i4;
                    i8 = i5;
                }
                ByteBuffer buffer = planes[i6].getBuffer();
                int rowStride = planes[i6].getRowStride();
                int pixelStride = planes[i6].getPixelStride();
                int i9 = i6 == 0 ? i4 : i5;
                int i10 = width >> i9;
                int i11 = height >> i9;
                int i12 = width;
                int i13 = height;
                buffer.position(((cropRect.top >> i9) * rowStride) + ((cropRect.left >> i9) * pixelStride));
                for (int i14 = 0; i14 < i11; i14++) {
                    if (pixelStride == 1 && i8 == 1) {
                        buffer.get(bArr, i7, i10);
                        i2 = i7 + i10;
                        i = i10;
                    } else {
                        i = ((i10 - 1) * pixelStride) + 1;
                        buffer.get(bArr2, 0, i);
                        int i15 = i7;
                        for (int i16 = 0; i16 < i10; i16++) {
                            bArr[i15] = bArr2[i16 * pixelStride];
                            i15 += i8;
                        }
                        i2 = i15;
                    }
                    if (i14 < i11 - 1) {
                        buffer.position((buffer.position() + rowStride) - i);
                    }
                }
                i6++;
                width = i12;
                height = i13;
                i4 = 0;
                i5 = 1;
            }
            return bArr;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("can't convert Image to byte array, format ");
        sb.append(image.getFormat());
        throw new IllegalStateException(sb.toString());
    }

    public static byte[] getYuvData(Image image) {
        if (image == null || 35 != image.getFormat()) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("getYuvData: ");
            sb.append(image);
            Log.e(str, sb.toString());
            return null;
        }
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        ByteBuffer buffer2 = image.getPlanes()[2].getBuffer();
        int limit = buffer.limit();
        int limit2 = buffer2.limit();
        byte[] bArr = new byte[(limit + limit2 + 1)];
        buffer.get(bArr, 0, limit);
        buffer2.get(bArr, limit, limit2);
        bArr[bArr.length - 1] = bArr[bArr.length - 3];
        return bArr;
    }

    public static void imageCopy(Image image, Image image2) {
        if (image == null || image2 == null) {
            throw new IllegalArgumentException("Images should be non-null");
        } else if (image.getFormat() == 34 || image2.getFormat() == 34) {
            throw new IllegalArgumentException("PRIVATE format images are not copyable");
        } else if (image.getFormat() != 36) {
            Size size = new Size(image.getWidth(), image.getHeight());
            Size size2 = new Size(image2.getWidth(), image2.getHeight());
            if (size.equals(size2)) {
                Plane[] planes = image.getPlanes();
                Plane[] planes2 = image2.getPlanes();
                int i = 0;
                while (i < planes.length) {
                    int rowStride = planes[i].getRowStride();
                    int rowStride2 = planes2[i].getRowStride();
                    ByteBuffer buffer = planes[i].getBuffer();
                    ByteBuffer buffer2 = planes2[i].getBuffer();
                    if (!buffer.isDirect() || !buffer2.isDirect()) {
                        throw new IllegalArgumentException("Source and destination ByteBuffers must be direct byteBuffer!");
                    } else if (planes[i].getPixelStride() == planes2[i].getPixelStride()) {
                        int position = buffer.position();
                        buffer.rewind();
                        buffer2.rewind();
                        if (rowStride == rowStride2) {
                            buffer2.put(buffer);
                        } else {
                            int position2 = buffer.position();
                            int position3 = buffer2.position();
                            Size effectivePlaneSizeForImage = getEffectivePlaneSizeForImage(image, i);
                            int width = effectivePlaneSizeForImage.getWidth() * planes[i].getPixelStride();
                            int i2 = position3;
                            int i3 = position2;
                            for (int i4 = 0; i4 < effectivePlaneSizeForImage.getHeight(); i4++) {
                                if (i4 == effectivePlaneSizeForImage.getHeight() - 1) {
                                    int remaining = buffer.remaining() - i3;
                                    if (width > remaining) {
                                        width = remaining;
                                    }
                                }
                                directByteBufferCopy(buffer, i3, buffer2, i2, width);
                                i3 += rowStride;
                                i2 += rowStride2;
                            }
                        }
                        buffer.position(position);
                        buffer2.rewind();
                        i++;
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Source plane image pixel stride ");
                        sb.append(planes[i].getPixelStride());
                        sb.append(" must be same as destination image pixel stride ");
                        sb.append(planes2[i].getPixelStride());
                        throw new IllegalArgumentException(sb.toString());
                    }
                }
                return;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("source image size ");
            sb2.append(size);
            sb2.append(" is different with destination image size ");
            sb2.append(size2);
            throw new IllegalArgumentException(sb2.toString());
        } else {
            throw new IllegalArgumentException("Copy of RAW_OPAQUE format has not been implemented");
        }
    }

    private static boolean isPreviewFormatSupported(Image image) {
        int format = image.getFormat();
        return format == 17 || format == 35 || format == 842094169;
    }

    public static Image queueImageToPool(ImagePool imagePool, Image image, int i) {
        if (i < 0) {
            i = 0;
        }
        ImagePool.ImageFormat imageQueueKey = imagePool.toImageQueueKey(image);
        if (imagePool.isImageQueueFull(imageQueueKey, i)) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("queueImageToPool: wait E. ");
            sb.append(Util.getCallers(3));
            Log.w(str, sb.toString());
            imagePool.waitIfImageQueueFull(imageQueueKey, i, 0);
            Log.w(TAG, "queueImageToPool: wait X");
        }
        long timestamp = image.getTimestamp();
        imagePool.queueImage(image);
        Image image2 = imagePool.getImage(timestamp);
        imagePool.holdImage(image2);
        return image2;
    }

    public static ByteBuffer removePadding(Plane plane, int i, int i2) {
        long currentTimeMillis = System.currentTimeMillis();
        ByteBuffer buffer = plane.getBuffer();
        int rowStride = plane.getRowStride();
        int pixelStride = plane.getPixelStride();
        Log.d(TAG, String.format(Locale.ENGLISH, "removePadding: rowStride=%d pixelStride=%d size=%dx%d", new Object[]{Integer.valueOf(rowStride), Integer.valueOf(pixelStride), Integer.valueOf(i), Integer.valueOf(i2)}));
        int i3 = i * pixelStride;
        if (rowStride == i3) {
            return buffer;
        }
        int i4 = i3 * i2;
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(i4);
        int position = buffer.position();
        int position2 = allocateDirect.position();
        for (int i5 = 0; i5 < i2; i5++) {
            if (i5 == i2 - 1) {
                int remaining = buffer.remaining() - position;
                if (i3 > remaining) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("removePadding: ");
                    sb.append(remaining);
                    sb.append("/");
                    sb.append(i3);
                    Log.d(str, sb.toString());
                    i3 = remaining;
                }
            }
            directByteBufferCopy(buffer, position, allocateDirect, position2, i3);
            position += rowStride;
            position2 += i3;
        }
        if (position2 < i4) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("removePadding: add data: ");
            sb2.append(position2);
            sb2.append("|");
            sb2.append(i4);
            Log.d(str2, sb2.toString());
            while (position2 < i4) {
                allocateDirect.put(position2, allocateDirect.get(position2 - 2));
                position2++;
            }
        }
        String str3 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("removePadding: cost=");
        sb3.append(System.currentTimeMillis() - currentTimeMillis);
        Log.v(str3, sb3.toString());
        return allocateDirect;
    }

    private static byte[] toBytes(int i) {
        return new byte[]{(byte) (i >> 24), (byte) (i >> 16), (byte) (i >> 8), (byte) i};
    }

    private static void updateImagePlane(Plane plane, int i, int i2, byte[] bArr, boolean z, int i3) {
        int min;
        int i4 = i2;
        byte[] bArr2 = bArr;
        int i5 = i3;
        ByteBuffer buffer = plane.getBuffer();
        buffer.rewind();
        int rowStride = plane.getRowStride();
        int pixelStride = plane.getPixelStride();
        int i6 = i * pixelStride;
        Log.d(TAG, String.format(Locale.ENGLISH, "updateImagePlane: size=%dx%d offset=%d length=%d rowStride=%d pixelStride=%d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(bArr2.length), Integer.valueOf(rowStride), Integer.valueOf(pixelStride)}));
        int i7 = i6 * i4;
        int length = bArr2.length - i5;
        if (length >= i7) {
            String str = "updateImagePlane: ";
            if (rowStride == i6) {
                min = Math.min(buffer.remaining(), i7);
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(min);
                Log.d(str2, sb.toString());
            } else if (z) {
                int i8 = (rowStride * (i4 - 1)) + i;
                min = Math.min(buffer.remaining(), i8);
                Log.d(TAG, "updateImagePlane: sub range length: %d/%d", Integer.valueOf(min), Integer.valueOf(i8));
            } else {
                int position = buffer.position();
                int i9 = i6;
                for (int i10 = 0; i10 < i4; i10++) {
                    buffer.position(position);
                    if (i10 == i4 - 1) {
                        i9 = Math.min(buffer.remaining(), i6);
                        if (i9 < i6) {
                            String str3 = TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(str);
                            sb2.append(i9);
                            sb2.append("/");
                            sb2.append(i6);
                            Log.d(str3, sb2.toString());
                            i9 = buffer.remaining();
                        }
                    }
                    buffer.put(bArr2, i5, i9);
                    i5 += i6;
                    position += rowStride;
                }
                buffer.rewind();
                return;
            }
            buffer.put(bArr2, i5, min);
            buffer.rewind();
            return;
        }
        throw new RuntimeException(String.format(Locale.ENGLISH, "buffer size should be at least %d but actual size is %d", new Object[]{Integer.valueOf(i7), Integer.valueOf(length)}));
    }

    public static void updateYuvImage(Image image, byte[] bArr, boolean z) {
        if (image == null || 35 != image.getFormat()) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("updateYuvImage: ");
            sb.append(image);
            Log.e(str, sb.toString());
            return;
        }
        Plane[] planes = image.getPlanes();
        updateImagePlane(planes[0], image.getWidth(), image.getHeight(), bArr, z, 0);
        int width = image.getWidth() * image.getHeight();
        if (z) {
            width = (planes[2].getRowStride() * (image.getHeight() - 1)) + image.getWidth();
        }
        updateImagePlane(planes[2], image.getWidth() / 2, image.getHeight() / 2, bArr, z, width);
    }
}
