package com.arcsoft.camera.utils;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.text.format.DateFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class O000000o {
    private O000000o() {
    }

    public static String O000000o(String str, String str2, String str3, int i, int i2) {
        if (str == null) {
            return null;
        }
        if (!str.endsWith(File.separator)) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(File.separator);
            str = sb.toString();
        }
        String charSequence = DateFormat.format("yyyyMMdd", System.currentTimeMillis()).toString();
        StringBuilder sb2 = new StringBuilder();
        String str4 = "_";
        sb2.append(str4);
        sb2.append(String.format("%03d", new Object[]{Integer.valueOf(i2)}));
        sb2.append(str4);
        sb2.append(i);
        String sb3 = sb2.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str);
        sb4.append(str2);
        sb4.append(charSequence);
        sb4.append(sb3);
        sb4.append(str3);
        String sb5 = sb4.toString();
        File file = new File(sb5);
        if (file.exists()) {
            file.delete();
        }
        return sb5;
    }

    public static boolean O000000o(byte[] bArr, int i, int i2, int i3, String str) {
        if (str != null) {
            File file = new File(str);
            int i4 = 0;
            while (file.exists()) {
                i4++;
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(i4);
                file = new File(sb.toString());
            }
            file.getParentFile().mkdirs();
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(str);
                YuvImage yuvImage = new YuvImage(bArr, 17, i, i2, null);
                yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), i3, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return false;
    }

    public static boolean O00000o(Context context, String str, String str2) {
        boolean z = false;
        if (context == null || str == null || str2 == null) {
            return false;
        }
        try {
            z = O00000o(context.getAssets().open(str), str2);
            return z;
        } catch (IOException e) {
            e.printStackTrace();
            return z;
        }
    }

    public static boolean O00000o(InputStream inputStream, String str) {
        if (inputStream == null || str == null) {
            return false;
        }
        File file = new File(str);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(str);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
            }
            inputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String O0000Ooo(String str, String str2) {
        if (str == null || str2 == null) {
            return null;
        }
        if (!str.isEmpty() && !str.endsWith(File.separator)) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(File.separator);
            str = sb.toString();
        }
        if (!str2.isEmpty() && str2.startsWith(File.separator)) {
            str2.substring(str2.indexOf(File.separator) + 1);
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(str2);
        return sb2.toString();
    }

    public static boolean O000OOOo(String str) {
        if (str == null) {
            return false;
        }
        return new File(str).exists();
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0039 A[SYNTHETIC, Splitter:B:25:0x0039] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0049 A[SYNTHETIC, Splitter:B:34:0x0049] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x004f A[SYNTHETIC, Splitter:B:37:0x004f] */
    /* JADX WARNING: Removed duplicated region for block: B:44:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:46:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:22:0x0034=Splitter:B:22:0x0034, B:31:0x0044=Splitter:B:31:0x0044} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] O000OOo0(String str) {
        byte[] bArr;
        byte[] bArr2;
        File file = new File(str);
        FileInputStream fileInputStream = null;
        try {
            FileInputStream fileInputStream2 = new FileInputStream(file);
            try {
                byte[] bArr3 = new byte[fileInputStream2.available()];
                while (fileInputStream2.read(bArr3) > 0) {
                }
                try {
                    fileInputStream2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bArr3;
            } catch (FileNotFoundException e2) {
                e = e2;
                FileInputStream fileInputStream3 = fileInputStream2;
                bArr2 = null;
                fileInputStream = fileInputStream3;
                e.printStackTrace();
                if (fileInputStream != null) {
                }
            } catch (IOException e3) {
                e = e3;
                FileInputStream fileInputStream4 = fileInputStream2;
                bArr = null;
                fileInputStream = fileInputStream4;
                try {
                    e.printStackTrace();
                    if (fileInputStream != null) {
                    }
                } catch (Throwable th) {
                    th = th;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileInputStream = fileInputStream2;
                if (fileInputStream != null) {
                }
                throw th;
            }
        } catch (FileNotFoundException e5) {
            e = e5;
            bArr2 = null;
            e.printStackTrace();
            if (fileInputStream != null) {
                return bArr;
            }
            fileInputStream.close();
            return bArr;
        } catch (IOException e6) {
            e = e6;
            bArr = null;
            e.printStackTrace();
            if (fileInputStream != null) {
                return bArr;
            }
            try {
                fileInputStream.close();
                return bArr;
            } catch (IOException e7) {
                e7.printStackTrace();
                return bArr;
            }
        }
    }

    public static String getExtension(String str) {
        if (str != null && !str.isEmpty()) {
            int lastIndexOf = str.lastIndexOf(46);
            if (lastIndexOf >= 0 && lastIndexOf < str.length() - 1) {
                return str.substring(lastIndexOf + 1);
            }
        }
        return null;
    }

    public static boolean isVideoFile(String str) {
        if (str == null) {
            return false;
        }
        String substring = str.substring(str.lastIndexOf(46));
        return ".3gp".equals(substring) || ".mp4".equals(substring);
    }
}
