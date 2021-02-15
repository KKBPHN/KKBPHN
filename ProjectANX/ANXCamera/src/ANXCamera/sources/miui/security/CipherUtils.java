package miui.security;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CipherUtils {
    public static final String ALOGIRHTM_RSA = "RSA/ECB/PKCS1Padding";
    public static final String KEY_ALOGIRHTM_RSA = "RSA";

    class CipherStream extends InputStream {
        final Cipher mCipher;
        byte[] mInputBuffer;
        final InputStream mInputDataStream;
        boolean mIsFinal;
        byte[] mOutputBuffer;
        int mOutputBufferOffset;

        CipherStream(Cipher cipher, InputStream inputStream) {
            this.mCipher = cipher;
            this.mInputDataStream = inputStream;
            this.mInputBuffer = new byte[cipher.getBlockSize()];
            update();
        }

        private void update() {
            int read = this.mInputDataStream.read(this.mInputBuffer);
            if (read > 0) {
                this.mOutputBuffer = this.mCipher.update(this.mInputBuffer, 0, read);
                this.mOutputBufferOffset = 0;
                return;
            }
            this.mIsFinal = true;
            try {
                this.mOutputBuffer = this.mCipher.doFinal();
                this.mOutputBufferOffset = 0;
            } catch (IllegalBlockSizeException e) {
                throw new RuntimeException("IllegalBlockSizeException", e);
            } catch (BadPaddingException e2) {
                throw new RuntimeException("BadPaddingException", e2);
            }
        }

        public int available() {
            byte[] bArr = this.mOutputBuffer;
            if (bArr != null) {
                int length = bArr.length;
                int i = this.mOutputBufferOffset;
                if (length > i) {
                    return (bArr.length - i) + this.mInputDataStream.available();
                }
            }
            return 0;
        }

        public int read() {
            byte[] bArr = this.mOutputBuffer;
            if (bArr == null) {
                return -1;
            }
            int i = this.mOutputBufferOffset;
            if (i < bArr.length) {
                this.mOutputBufferOffset = i + 1;
                return bArr[i];
            }
            this.mOutputBuffer = null;
            if (this.mIsFinal) {
                return -1;
            }
            update();
            return read();
        }
    }

    protected CipherUtils() {
        throw new InstantiationException("Cannot instantiate utility class");
    }

    public static InputStream decrypt(InputStream inputStream, Key key, AlgorithmParameterSpec algorithmParameterSpec, String str) {
        String str2 = "NoSuchAlgorithmException catched for algorithm ";
        try {
            Cipher instance = Cipher.getInstance(str);
            instance.init(2, key, algorithmParameterSpec);
            return new CipherStream(instance, inputStream);
        } catch (NoSuchAlgorithmException e) {
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(str);
            throw new RuntimeException(sb.toString(), e);
        } catch (NoSuchPaddingException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(str);
            throw new RuntimeException(sb2.toString(), e2);
        } catch (InvalidKeyException e3) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("InvalidKeyException catched for key ");
            sb3.append(key);
            throw new RuntimeException(sb3.toString(), e3);
        } catch (InvalidAlgorithmParameterException e4) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("InvalidAlgorithmParameterException catched for algorithm parameter ");
            sb4.append(algorithmParameterSpec);
            throw new RuntimeException(sb4.toString(), e4);
        }
    }

    public static byte[] decrypt(byte[] bArr, Key key, AlgorithmParameterSpec algorithmParameterSpec, String str) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        try {
            InputStream decrypt = decrypt((InputStream) new ByteArrayInputStream(bArr), key, algorithmParameterSpec, str);
            byte[] bArr2 = new byte[(bArr.length * 2)];
            byte[] bArr3 = new byte[1024];
            int i = 0;
            while (true) {
                int read = decrypt.read(bArr3);
                if (read == 0) {
                    return Arrays.copyOfRange(bArr2, 0, i);
                }
                int i2 = read + i;
                if (i2 > bArr2.length) {
                    byte[] bArr4 = new byte[((bArr2.length + read) * 2)];
                    System.arraycopy(bArr2, 0, bArr4, 0, i);
                    bArr2 = bArr4;
                }
                System.arraycopy(bArr3, 0, bArr2, i, read);
                i = i2;
            }
        } catch (IOException e) {
            throw new RuntimeException("IOException catched when using ByteArrayInputStream", e);
        }
    }

    public static InputStream encrypt(InputStream inputStream, Key key, AlgorithmParameterSpec algorithmParameterSpec, String str) {
        String str2 = "NoSuchAlgorithmException catched for algorithm ";
        try {
            Cipher instance = Cipher.getInstance(str);
            instance.init(1, key, algorithmParameterSpec);
            return new CipherStream(instance, inputStream);
        } catch (NoSuchAlgorithmException e) {
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(str);
            throw new RuntimeException(sb.toString(), e);
        } catch (NoSuchPaddingException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(str);
            throw new RuntimeException(sb2.toString(), e2);
        } catch (InvalidKeyException e3) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("InvalidKeyException catched for key ");
            sb3.append(key);
            throw new RuntimeException(sb3.toString(), e3);
        } catch (InvalidAlgorithmParameterException e4) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("InvalidAlgorithmParameterException catched for algorithm parameter ");
            sb4.append(algorithmParameterSpec);
            throw new RuntimeException(sb4.toString(), e4);
        }
    }

    public static byte[] encrypt(byte[] bArr, Key key, AlgorithmParameterSpec algorithmParameterSpec, String str) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        try {
            InputStream encrypt = encrypt((InputStream) new ByteArrayInputStream(bArr), key, algorithmParameterSpec, str);
            byte[] bArr2 = new byte[(bArr.length * 2)];
            byte[] bArr3 = new byte[1024];
            int i = 0;
            while (true) {
                int read = encrypt.read(bArr3);
                if (read == 0) {
                    return Arrays.copyOfRange(bArr2, 0, i);
                }
                int i2 = read + i;
                if (i2 > bArr2.length) {
                    byte[] bArr4 = new byte[((bArr2.length + read) * 2)];
                    System.arraycopy(bArr2, 0, bArr4, 0, i);
                    bArr2 = bArr4;
                }
                System.arraycopy(bArr3, 0, bArr2, i, read);
                i = i2;
            }
        } catch (IOException e) {
            throw new RuntimeException("IOException catched when using ByteArrayInputStream", e);
        }
    }

    public static PrivateKey getPrivateKey(KeySpec keySpec, String str) {
        try {
            r3 = keySpec;
            r3 = KeyFactory.getInstance(str).generatePrivate(keySpec);
            r3 = r3;
            return r3;
        } catch (NoSuchAlgorithmException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("NoSuchAlgorithmException catched for algorithm ");
            sb.append(str);
            throw new RuntimeException(sb.toString(), e);
        } catch (InvalidKeySpecException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("InvalidKeySpecException catched for keySpec ");
            sb2.append(r3.getClass().getName());
            sb2.append(":");
            sb2.append(r3);
            throw new RuntimeException(sb2.toString(), e2);
        }
    }

    public static PublicKey getPublicKey(KeySpec keySpec, String str) {
        try {
            r3 = keySpec;
            r3 = KeyFactory.getInstance(str).generatePublic(keySpec);
            r3 = r3;
            return r3;
        } catch (NoSuchAlgorithmException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("NoSuchAlgorithmException catched for algorithm ");
            sb.append(str);
            throw new RuntimeException(sb.toString(), e);
        } catch (InvalidKeySpecException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("InvalidKeySpecException catched for keySpec ");
            sb2.append(r3.getClass().getName());
            sb2.append(":");
            sb2.append(r3);
            throw new RuntimeException(sb2.toString(), e2);
        }
    }

    public static PrivateKey getRsaPrivateKey(String str, String str2, int i) {
        return getPrivateKey(new RSAPrivateKeySpec(new BigInteger(str, i), new BigInteger(str2, i)), "RSA");
    }

    public static PublicKey getRsaPublicKey(String str, String str2, int i) {
        return getPublicKey(new RSAPublicKeySpec(new BigInteger(str, i), new BigInteger(str2, i)), "RSA");
    }
}
