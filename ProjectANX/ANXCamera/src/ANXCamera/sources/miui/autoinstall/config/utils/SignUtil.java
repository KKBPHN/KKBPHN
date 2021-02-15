package miui.autoinstall.config.utils;

import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

public class SignUtil {
    private SignUtil() {
    }

    private static String bytesToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder("");
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & -1);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0031, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x003a, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        byte[] bArr = new byte[1024];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            MessageDigest instance = MessageDigest.getInstance("MD5");
            while (true) {
                int read = fileInputStream.read(bArr, 0, 1024);
                if (read != -1) {
                    instance.update(bArr, 0, read);
                } else {
                    fileInputStream.close();
                    return bytesToHexString(instance.digest());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } catch (Throwable th) {
            r7.addSuppressed(th);
        }
    }

    public static String getNonceStr() {
        return UUID.randomUUID().toString();
    }

    public static String getSign(TreeMap treeMap, String str) {
        StringBuilder sb = new StringBuilder();
        for (Entry entry : treeMap.entrySet()) {
            String str2 = (String) entry.getKey();
            Object value = entry.getValue();
            sb.append(str2);
            sb.append("&");
            sb.append(value);
        }
        sb.append("#");
        sb.append(str);
        return md5(sb.toString());
    }

    public static String loadPkgSignature(PackageInfo packageInfo) {
        Signature[] signatureArr = packageInfo.signatures;
        int length = signatureArr != null ? signatureArr.length : 0;
        if (length == 0) {
            return "";
        }
        String md5 = md5(packageInfo.signatures[0].toCharsString());
        for (int i = 1; i < length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(md5);
            sb.append(",");
            sb.append(md5(packageInfo.signatures[i].toCharsString()));
            md5 = sb.toString();
        }
        return md5;
    }

    private static String md5(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            String bigInteger = new BigInteger(1, instance.digest()).toString(16);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 32 - bigInteger.length(); i++) {
                sb.append("0");
            }
            sb.append(bigInteger);
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
