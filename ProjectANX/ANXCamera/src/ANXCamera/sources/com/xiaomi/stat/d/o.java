package com.xiaomi.stat.d;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

public class o {
    private static final String a = "RsaUtils";
    private static final String b = "RSA/ECB/PKCS1Padding";
    private static final String c = "BC";
    private static final String d = "RSA";

    private static RSAPublicKey a(byte[] bArr) {
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bArr));
    }

    public static byte[] a(byte[] bArr, byte[] bArr2) {
        try {
            RSAPublicKey a2 = a(bArr);
            Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            instance.init(1, a2);
            return instance.doFinal(bArr2);
        } catch (Exception e) {
            k.d(a, "RsaUtils encrypt exception:", e);
            return null;
        }
    }
}
