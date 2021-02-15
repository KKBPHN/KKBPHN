package miui.security;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtils {
    public static final String ALGORITHM_MD5 = "MD5";
    public static final String ALGORITHM_SHA_1 = "SHA-1";
    private static final int BUFFER_SIZE = 4096;

    protected DigestUtils() {
        throw new InstantiationException("Cannot instantiate utility class");
    }

    public static byte[] get(InputStream inputStream, String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance(str);
            byte[] bArr = new byte[4096];
            while (true) {
                int read = inputStream.read(bArr);
                if (read <= 0) {
                    return instance.digest();
                }
                instance.update(bArr, 0, read);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        }
    }

    public static byte[] get(CharSequence charSequence, String str) {
        return get(charSequence.toString().getBytes(), str);
    }

    public static byte[] get(byte[] bArr, String str) {
        try {
            return get((InputStream) new ByteArrayInputStream(bArr), str);
        } catch (IOException e) {
            throw new RuntimeException("IO exception happend in ByteArrayInputStream", e);
        }
    }
}
