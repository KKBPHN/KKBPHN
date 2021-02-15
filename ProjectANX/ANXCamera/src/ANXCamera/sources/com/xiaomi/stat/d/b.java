package com.xiaomi.stat.d;

import android.content.Context;
import android.os.Build.VERSION;
import android.security.KeyPairGeneratorSpec;
import android.security.KeyPairGeneratorSpec.Builder;
import android.util.Base64;
import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.GregorianCalendar;
import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;

public class b {
    private static final String a = "AndroidKeyStoreUtils";
    private static final String b = "AndroidKeyStore";
    private static final String c = "RSA/ECB/PKCS1Padding";
    private static final String d = "RSA_KEY";

    public static synchronized String a(Context context, String str) {
        synchronized (b.class) {
            Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            KeyStore instance2 = KeyStore.getInstance(b);
            instance2.load(null);
            a(context, instance2);
            Certificate certificate = instance2.getCertificate(d);
            if (certificate == null) {
                return null;
            }
            instance.init(1, certificate.getPublicKey());
            String encodeToString = Base64.encodeToString(instance.doFinal(str.getBytes("UTF-8")), 0);
            return encodeToString;
        }
    }

    private static void a() {
        Class cls = Class.forName("android.security.keystore.KeyGenParameterSpec$Builder");
        if (cls != null) {
            Constructor constructor = cls.getConstructor(new Class[]{String.class, Integer.TYPE});
            Class cls2 = Class.forName("android.security.keystore.KeyProperties");
            Object newInstance = constructor.newInstance(new Object[]{d, Integer.valueOf(cls2.getDeclaredField("PURPOSE_ENCRYPT").getInt(null) | cls2.getDeclaredField("PURPOSE_DECRYPT").getInt(null))});
            cls.getMethod("setDigests", new Class[]{String[].class}).invoke(newInstance, new Object[]{new String[]{(String) cls2.getDeclaredField("DIGEST_SHA256").get(null), (String) cls2.getDeclaredField("DIGEST_SHA512").get(null)}});
            cls.getMethod("setEncryptionPaddings", new Class[]{String[].class}).invoke(newInstance, new Object[]{new String[]{(String) cls2.getDeclaredField("ENCRYPTION_PADDING_RSA_PKCS1").get(null)}});
            Object invoke = cls.getMethod("build", new Class[0]).invoke(newInstance, new Object[0]);
            Class cls3 = Class.forName("java.security.KeyPairGenerator");
            if (cls3 != null) {
                KeyPairGenerator keyPairGenerator = (KeyPairGenerator) cls3.getMethod("getInstance", new Class[]{String.class, String.class}).invoke(null, new Object[]{"RSA", b});
                cls3.getMethod("initialize", new Class[]{AlgorithmParameterSpec.class}).invoke(keyPairGenerator, new Object[]{invoke});
                keyPairGenerator.generateKeyPair();
            }
        }
    }

    private static void a(Context context) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        GregorianCalendar gregorianCalendar2 = new GregorianCalendar();
        gregorianCalendar2.add(1, 1);
        KeyPairGeneratorSpec build = new Builder(context).setAlias(d).setSubject(new X500Principal("CN=RSA_KEY")).setSerialNumber(BigInteger.valueOf(1337)).setStartDate(gregorianCalendar.getTime()).setEndDate(gregorianCalendar2.getTime()).build();
        KeyPairGenerator instance = KeyPairGenerator.getInstance("RSA", b);
        instance.initialize(build);
        instance.generateKeyPair();
    }

    private static void a(Context context, KeyStore keyStore) {
        try {
            if (!keyStore.containsAlias(d) && VERSION.SDK_INT >= 18) {
                if (VERSION.SDK_INT < 23) {
                    a(context);
                } else {
                    a();
                }
            }
        } catch (Exception e) {
            k.d(a, "createKey e", e);
        }
    }

    public static synchronized String b(Context context, String str) {
        String str2;
        synchronized (b.class) {
            Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            KeyStore instance2 = KeyStore.getInstance(b);
            instance2.load(null);
            a(context, instance2);
            instance.init(2, (PrivateKey) instance2.getKey(d, null));
            str2 = new String(instance.doFinal(Base64.decode(str, 0)), "UTF-8");
        }
        return str2;
    }
}
