package com.split.signature;

import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Pair;
import com.android.gallery3d.exif.ExifInterface.ColorSpace;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.security.DigestException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class O00000o implements O000000o {
    private final ByteBuffer a;

    private O00000o(ByteBuffer byteBuffer) {
        this.a = byteBuffer.slice();
    }

    private static long O000000o(ByteBuffer byteBuffer, int i) {
        return ((long) byteBuffer.getInt(i)) & 4294967295L;
    }

    private static long O000000o(ByteBuffer byteBuffer, long j) {
        O00000Oo(byteBuffer);
        long O000000o2 = O000000o(byteBuffer, byteBuffer.position() + 16);
        if (O000000o2 < j) {
            O00000Oo(byteBuffer);
            if (O000000o(byteBuffer, byteBuffer.position() + 12) + O000000o2 == j) {
                return O000000o2;
            }
            throw new D("ZIP Central Directory is not immediately followed by End of Central Directory");
        }
        StringBuilder sb = new StringBuilder(122);
        sb.append("ZIP Central Directory offset out of range: ");
        sb.append(O000000o2);
        sb.append(". ZIP End of Central Directory offset: ");
        sb.append(j);
        throw new D(sb.toString());
    }

    private static Pair O000000o(RandomAccessFile randomAccessFile, int i) {
        int i2;
        if (i < 0 || i > 65535) {
            StringBuilder sb = new StringBuilder(27);
            sb.append("maxCommentSize: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
        long length = randomAccessFile.length();
        if (length < 22) {
            return null;
        }
        ByteBuffer allocate = ByteBuffer.allocate(((int) Math.min((long) i, length - 22)) + 22);
        allocate.order(ByteOrder.LITTLE_ENDIAN);
        long capacity = length - ((long) allocate.capacity());
        randomAccessFile.seek(capacity);
        randomAccessFile.readFully(allocate.array(), allocate.arrayOffset(), allocate.capacity());
        O00000Oo(allocate);
        int capacity2 = allocate.capacity();
        if (capacity2 >= 22) {
            int i3 = capacity2 - 22;
            int min = Math.min(i3, 65535);
            int i4 = 0;
            while (true) {
                if (i4 >= min) {
                    break;
                }
                i2 = i3 - i4;
                if (allocate.getInt(i2) == 101010256 && (allocate.getShort(i2 + 20) & ColorSpace.UNCALIBRATED) == i4) {
                    break;
                }
                i4++;
            }
        }
        i2 = -1;
        if (i2 == -1) {
            return null;
        }
        allocate.position(i2);
        ByteBuffer slice = allocate.slice();
        slice.order(ByteOrder.LITTLE_ENDIAN);
        return Pair.create(slice, Long.valueOf(capacity + ((long) i2)));
    }

    private static Pair O000000o(RandomAccessFile randomAccessFile, long j) {
        if (j >= 32) {
            ByteBuffer allocate = ByteBuffer.allocate(24);
            allocate.order(ByteOrder.LITTLE_ENDIAN);
            randomAccessFile.seek(j - ((long) allocate.capacity()));
            randomAccessFile.readFully(allocate.array(), allocate.arrayOffset(), allocate.capacity());
            if (allocate.getLong(8) == 2334950737559900225L && allocate.getLong(16) == 3617552046287187010L) {
                long j2 = allocate.getLong(0);
                if (j2 < ((long) allocate.capacity()) || j2 > 2147483639) {
                    StringBuilder sb = new StringBuilder(57);
                    sb.append("APK Signing Block size out of range: ");
                    sb.append(j2);
                    throw new D(sb.toString());
                }
                int i = (int) (8 + j2);
                long j3 = j - ((long) i);
                if (j3 >= 0) {
                    ByteBuffer allocate2 = ByteBuffer.allocate(i);
                    allocate2.order(ByteOrder.LITTLE_ENDIAN);
                    randomAccessFile.seek(j3);
                    randomAccessFile.readFully(allocate2.array(), allocate2.arrayOffset(), allocate2.capacity());
                    long j4 = allocate2.getLong(0);
                    if (j4 == j2) {
                        return Pair.create(allocate2, Long.valueOf(j3));
                    }
                    StringBuilder sb2 = new StringBuilder(103);
                    sb2.append("APK Signing Block sizes in header and footer do not match: ");
                    sb2.append(j4);
                    sb2.append(" vs ");
                    sb2.append(j2);
                    throw new D(sb2.toString());
                }
                StringBuilder sb3 = new StringBuilder(59);
                sb3.append("APK Signing Block offset out of range: ");
                sb3.append(j3);
                throw new D(sb3.toString());
            }
            throw new D("No APK Signing Block before ZIP Central Directory");
        }
        StringBuilder sb4 = new StringBuilder(87);
        sb4.append("APK too small for APK Signing Block. ZIP Central Directory offset: ");
        sb4.append(j);
        throw new D(sb4.toString());
    }

    private static void O000000o(int i, byte[] bArr, int i2) {
        bArr[1] = (byte) i;
        bArr[2] = (byte) (i >>> 8);
        bArr[3] = (byte) (i >>> 16);
        bArr[4] = (byte) (i >>> 24);
    }

    private static void O000000o(Map map, FileChannel fileChannel, long j, long j2, long j3, ByteBuffer byteBuffer) {
        long j4 = j;
        if (!map.isEmpty()) {
            O00000Oo o00000Oo = new O00000Oo(fileChannel, 0, j);
            O00000Oo o00000Oo2 = new O00000Oo(fileChannel, j2, j3 - j2);
            ByteBuffer duplicate = byteBuffer.duplicate();
            duplicate.order(ByteOrder.LITTLE_ENDIAN);
            O00000Oo(duplicate);
            int position = duplicate.position() + 16;
            if (j4 < 0 || j4 > 4294967295L) {
                StringBuilder sb = new StringBuilder(47);
                sb.append("uint32 value of out range: ");
                sb.append(j4);
                throw new IllegalArgumentException(sb.toString());
            }
            duplicate.putInt(duplicate.position() + position, (int) j4);
            O00000o o00000o = new O00000o(duplicate);
            int[] iArr = new int[map.size()];
            int i = 0;
            int i2 = 0;
            for (Integer intValue : map.keySet()) {
                iArr[i2] = intValue.intValue();
                i2++;
            }
            try {
                byte[][] O000000o2 = O000000o(iArr, new O000000o[]{o00000Oo, o00000Oo2, o00000o});
                while (i < iArr.length) {
                    int i3 = iArr[i];
                    Map map2 = map;
                    if (MessageDigest.isEqual((byte[]) map.get(Integer.valueOf(i3)), O000000o2[i])) {
                        i++;
                    } else {
                        throw new SecurityException(c(i3).concat(" digest of contents did not verify"));
                    }
                }
            } catch (DigestException e) {
                throw new SecurityException("Failed to compute digest(s) of contents", e);
            }
        } else {
            throw new SecurityException("No digests provided");
        }
    }

    private static X509Certificate[] O000000o(ByteBuffer byteBuffer, Map map, CertificateFactory certificateFactory) {
        SecurityException securityException;
        ByteBuffer O00000o02 = O00000o0(byteBuffer);
        ByteBuffer O00000o03 = O00000o0(byteBuffer);
        byte[] O00000o2 = O00000o(byteBuffer);
        ArrayList arrayList = new ArrayList();
        byte[] bArr = null;
        int i = -1;
        int i2 = 0;
        while (O00000o03.hasRemaining()) {
            i2++;
            try {
                ByteBuffer O00000o04 = O00000o0(O00000o03);
                if (O00000o04.remaining() >= 8) {
                    int i3 = O00000o04.getInt();
                    arrayList.add(Integer.valueOf(i3));
                    if (a(i3) && (i == -1 || a(i3, i) > 0)) {
                        bArr = O00000o(O00000o04);
                        i = i3;
                    }
                } else {
                    throw new SecurityException("Signature record too short");
                }
            } catch (IOException | BufferUnderflowException e) {
                StringBuilder sb = new StringBuilder(45);
                sb.append("Failed to parse signature record #");
                sb.append(i2);
                throw new SecurityException(sb.toString(), e);
            }
        }
        if (i != -1) {
            String e2 = e(i);
            Pair O000O0OO = O000O0OO(i);
            String str = (String) O000O0OO.first;
            AlgorithmParameterSpec algorithmParameterSpec = (AlgorithmParameterSpec) O000O0OO.second;
            try {
                PublicKey generatePublic = KeyFactory.getInstance(e2).generatePublic(new X509EncodedKeySpec(O00000o2));
                Signature instance = Signature.getInstance(str);
                instance.initVerify(generatePublic);
                if (algorithmParameterSpec != null) {
                    instance.setParameter(algorithmParameterSpec);
                }
                instance.update(O00000o02);
                if (instance.verify(bArr)) {
                    O00000o02.clear();
                    ByteBuffer O00000o05 = O00000o0(O00000o02);
                    ArrayList arrayList2 = new ArrayList();
                    byte[] bArr2 = null;
                    int i4 = 0;
                    while (O00000o05.hasRemaining()) {
                        i4++;
                        try {
                            ByteBuffer O00000o06 = O00000o0(O00000o05);
                            if (O00000o06.remaining() >= 8) {
                                int i5 = O00000o06.getInt();
                                arrayList2.add(Integer.valueOf(i5));
                                if (i5 == i) {
                                    bArr2 = O00000o(O00000o06);
                                }
                            } else {
                                throw new IOException("Record too short");
                            }
                        } catch (IOException | BufferUnderflowException e3) {
                            StringBuilder sb2 = new StringBuilder(42);
                            sb2.append("Failed to parse digest record #");
                            sb2.append(i4);
                            throw new IOException(sb2.toString(), e3);
                        }
                    }
                    if (arrayList.equals(arrayList2)) {
                        int b = b(i);
                        byte[] bArr3 = (byte[]) map.put(Integer.valueOf(b), bArr2);
                        if (bArr3 == null || MessageDigest.isEqual(bArr3, bArr2)) {
                            ByteBuffer O00000o07 = O00000o0(O00000o02);
                            ArrayList arrayList3 = new ArrayList();
                            int i6 = 0;
                            while (O00000o07.hasRemaining()) {
                                i6++;
                                byte[] O00000o3 = O00000o(O00000o07);
                                try {
                                    arrayList3.add(new X509CertificateEx((X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(O00000o3)), O00000o3));
                                } catch (CertificateException e4) {
                                    StringBuilder sb3 = new StringBuilder(41);
                                    sb3.append("Failed to decode certificate #");
                                    sb3.append(i6);
                                    throw new SecurityException(sb3.toString(), e4);
                                }
                            }
                            if (arrayList3.isEmpty()) {
                                throw new SecurityException("No certificates listed");
                            } else if (Arrays.equals(O00000o2, ((X509Certificate) arrayList3.get(0)).getPublicKey().getEncoded())) {
                                return (X509Certificate[]) arrayList3.toArray(new X509Certificate[arrayList3.size()]);
                            } else {
                                throw new SecurityException("Public key mismatch between certificate and signature record");
                            }
                        } else {
                            throw new SecurityException(c(b).concat(" contents digest does not match the digest specified by a preceding signer"));
                        }
                    } else {
                        throw new SecurityException("Signature algorithms don't match between digests and signatures records");
                    }
                } else {
                    throw new SecurityException(String.valueOf(str).concat(" signature did not verify"));
                }
            } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException e5) {
                StringBuilder sb4 = new StringBuilder(String.valueOf(str).length() + 27);
                sb4.append("Failed to verify ");
                sb4.append(str);
                sb4.append(" signature");
                throw new SecurityException(sb4.toString(), e5);
            }
        } else if (i2 == 0) {
            securityException = new SecurityException("No signatures found");
            throw securityException;
        } else {
            securityException = new SecurityException("No supported signatures found");
            throw securityException;
        }
    }

    private static byte[][] O000000o(int[] iArr, O000000o[] o000000oArr) {
        char c;
        int i;
        String str;
        String str2;
        int[] iArr2 = iArr;
        O000000o[] o000000oArr2 = o000000oArr;
        long j = 0;
        long j2 = 0;
        for (O000000o a2 : o000000oArr2) {
            j2 += a(a2.a());
        }
        if (j2 < 2097151) {
            int i2 = (int) j2;
            byte[][] bArr = new byte[iArr2.length][];
            int i3 = 0;
            while (true) {
                c = 5;
                i = 1;
                if (i3 >= iArr2.length) {
                    break;
                }
                byte[] bArr2 = new byte[((d(iArr2[i3]) * i2) + 5)];
                bArr2[0] = 90;
                O000000o(i2, bArr2, 1);
                bArr[i3] = bArr2;
                i3++;
            }
            byte[] bArr3 = new byte[5];
            bArr3[0] = -91;
            MessageDigest[] messageDigestArr = new MessageDigest[iArr2.length];
            int i4 = 0;
            while (true) {
                str = " digest not supported";
                if (i4 >= iArr2.length) {
                    break;
                }
                String c2 = c(iArr2[i4]);
                try {
                    messageDigestArr[i4] = MessageDigest.getInstance(c2);
                    i4++;
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(c2.concat(str), e);
                }
            }
            int length = o000000oArr2.length;
            int i5 = 0;
            int i6 = 0;
            int i7 = 0;
            while (i5 < length) {
                O000000o o000000o = o000000oArr2[i5];
                long j3 = j;
                byte[][] bArr4 = bArr;
                long a3 = o000000o.a();
                while (a3 > j) {
                    int min = (int) Math.min(a3, PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED);
                    O000000o(min, bArr3, i);
                    for (MessageDigest update : messageDigestArr) {
                        update.update(bArr3);
                    }
                    long j4 = j3;
                    try {
                        o000000o.O000000o(messageDigestArr, j4, min);
                        int i8 = 0;
                        while (i8 < iArr2.length) {
                            int i9 = iArr2[i8];
                            byte[] bArr5 = bArr3;
                            byte[] bArr6 = bArr4[i8];
                            int d = d(i9);
                            int i10 = length;
                            MessageDigest messageDigest = messageDigestArr[i8];
                            MessageDigest[] messageDigestArr2 = messageDigestArr;
                            int digest = messageDigest.digest(bArr6, (i6 * d) + 5, d);
                            if (digest == d) {
                                i8++;
                                bArr3 = bArr5;
                                length = i10;
                                messageDigestArr = messageDigestArr2;
                            } else {
                                String algorithm = messageDigest.getAlgorithm();
                                StringBuilder sb = new StringBuilder(String.valueOf(algorithm).length() + 46);
                                sb.append("Unexpected output size of ");
                                sb.append(algorithm);
                                sb.append(" digest: ");
                                sb.append(digest);
                                throw new RuntimeException(sb.toString());
                            }
                        }
                        MessageDigest[] messageDigestArr3 = messageDigestArr;
                        int i11 = length;
                        long j5 = (long) min;
                        long j6 = j4 + j5;
                        a3 -= j5;
                        i6++;
                        O000000o[] o000000oArr3 = o000000oArr;
                        bArr3 = bArr3;
                        c = 5;
                        i = 1;
                        j3 = j6;
                        j = 0;
                    } catch (IOException e2) {
                        IOException iOException = e2;
                        StringBuilder sb2 = new StringBuilder(59);
                        sb2.append("Failed to digest chunk #");
                        sb2.append(i6);
                        sb2.append(" of section #");
                        sb2.append(i7);
                        throw new DigestException(sb2.toString(), iOException);
                    }
                }
                byte[] bArr7 = bArr3;
                MessageDigest[] messageDigestArr4 = messageDigestArr;
                int i12 = length;
                char c3 = c;
                i7++;
                i5++;
                o000000oArr2 = o000000oArr;
                bArr = bArr4;
                j = 0;
                i = 1;
            }
            byte[][] bArr8 = bArr;
            byte[][] bArr9 = new byte[iArr2.length][];
            int i13 = 0;
            while (i13 < iArr2.length) {
                int i14 = iArr2[i13];
                byte[] bArr10 = bArr8[i13];
                String c4 = c(i14);
                try {
                    str2 = c4;
                    MessageDigest instance = MessageDigest.getInstance(c4);
                    str2 = instance;
                    bArr9[i13] = instance.digest(bArr10);
                    i13++;
                } catch (NoSuchAlgorithmException e3) {
                    throw new RuntimeException(str2.concat(str), e3);
                }
            }
            return bArr9;
        }
        StringBuilder sb3 = new StringBuilder(37);
        sb3.append("Too many chunks: ");
        sb3.append(j2);
        throw new DigestException(sb3.toString());
    }

    private static X509Certificate[][] O000000o(RandomAccessFile randomAccessFile) {
        return O000000o(randomAccessFile.getChannel(), O00000Oo(randomAccessFile));
    }

    private static X509Certificate[][] O000000o(FileChannel fileChannel, O00000o0 o00000o0) {
        HashMap hashMap = new HashMap();
        ArrayList arrayList = new ArrayList();
        try {
            CertificateFactory instance = CertificateFactory.getInstance("X.509");
            ByteBuffer O00000o02 = O00000o0(o00000o0.a);
            int i = 0;
            while (O00000o02.hasRemaining()) {
                i++;
                try {
                    arrayList.add(O000000o(O00000o0(O00000o02), (Map) hashMap, instance));
                } catch (IOException | SecurityException | BufferUnderflowException unused) {
                }
            }
            if (i <= 0) {
                throw new SecurityException("No signers found");
            } else if (!hashMap.isEmpty()) {
                O000000o(hashMap, fileChannel, o00000o0.b, o00000o0.c, o00000o0.d, o00000o0.e);
                return (X509Certificate[][]) arrayList.toArray(new X509Certificate[arrayList.size()][]);
            } else {
                throw new SecurityException("No content digests found");
            }
        } catch (Throwable th) {
            throw new RuntimeException("Failed to obtain X.509 CertificateFactory", th);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x002c  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x004b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static O00000o0 O00000Oo(RandomAccessFile randomAccessFile) {
        boolean z;
        Pair O00000o02 = O00000o0(randomAccessFile);
        ByteBuffer byteBuffer = (ByteBuffer) O00000o02.first;
        long longValue = ((Long) O00000o02.second).longValue();
        long j = longValue - 20;
        if (j >= 0) {
            randomAccessFile.seek(j);
            if (randomAccessFile.readInt() == 1347094023) {
                z = true;
                if (z) {
                    long O000000o2 = O000000o(byteBuffer, longValue);
                    Pair O000000o3 = O000000o(randomAccessFile, O000000o2);
                    ByteBuffer byteBuffer2 = (ByteBuffer) O000000o3.first;
                    O00000o0 o00000o0 = new O00000o0(O00000oO(byteBuffer2), ((Long) O000000o3.second).longValue(), O000000o2, longValue, byteBuffer);
                    return o00000o0;
                }
                throw new D("ZIP64 APK not supported");
            }
        }
        z = false;
        if (z) {
        }
    }

    private static ByteBuffer O00000Oo(ByteBuffer byteBuffer, int i) {
        if (i >= 0) {
            int limit = byteBuffer.limit();
            int position = byteBuffer.position();
            int i2 = i + position;
            if (i2 < position || i2 > limit) {
                throw new BufferUnderflowException();
            }
            byteBuffer.limit(i2);
            try {
                ByteBuffer slice = byteBuffer.slice();
                slice.order(byteBuffer.order());
                byteBuffer.position(i2);
                return slice;
            } finally {
                byteBuffer.limit(limit);
            }
        } else {
            StringBuilder sb = new StringBuilder(17);
            sb.append("size: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    private static ByteBuffer O00000Oo(ByteBuffer byteBuffer, int i, int i2) {
        if (i2 >= 8) {
            int capacity = byteBuffer.capacity();
            if (i2 <= byteBuffer.capacity()) {
                int limit = byteBuffer.limit();
                int position = byteBuffer.position();
                try {
                    byteBuffer.position(0);
                    byteBuffer.limit(i2);
                    byteBuffer.position(8);
                    ByteBuffer slice = byteBuffer.slice();
                    slice.order(byteBuffer.order());
                    return slice;
                } finally {
                    byteBuffer.position(0);
                    byteBuffer.limit(limit);
                    byteBuffer.position(position);
                }
            } else {
                StringBuilder sb = new StringBuilder(41);
                sb.append("end > capacity: ");
                sb.append(i2);
                sb.append(" > ");
                sb.append(capacity);
                throw new IllegalArgumentException(sb.toString());
            }
        } else {
            StringBuilder sb2 = new StringBuilder(38);
            sb2.append("end < start: ");
            sb2.append(i2);
            sb2.append(" < 8");
            throw new IllegalArgumentException(sb2.toString());
        }
    }

    private static void O00000Oo(ByteBuffer byteBuffer) {
        if (byteBuffer.order() != ByteOrder.LITTLE_ENDIAN) {
            throw new IllegalArgumentException("ByteBuffer byte order must be little endian");
        }
    }

    private static byte[] O00000o(ByteBuffer byteBuffer) {
        int i = byteBuffer.getInt();
        if (i < 0) {
            throw new IOException("Negative length");
        } else if (i <= byteBuffer.remaining()) {
            byte[] bArr = new byte[i];
            byteBuffer.get(bArr);
            return bArr;
        } else {
            int remaining = byteBuffer.remaining();
            StringBuilder sb = new StringBuilder(90);
            sb.append("Underflow while reading length-prefixed value. Length: ");
            sb.append(i);
            sb.append(", available: ");
            sb.append(remaining);
            throw new IOException(sb.toString());
        }
    }

    private static Pair O00000o0(RandomAccessFile randomAccessFile) {
        Pair pair;
        if (randomAccessFile.length() < 22) {
            pair = null;
        } else {
            pair = O000000o(randomAccessFile, 0);
            if (pair == null) {
                pair = O000000o(randomAccessFile, 65535);
            }
        }
        if (pair != null) {
            return pair;
        }
        long length = randomAccessFile.length();
        StringBuilder sb = new StringBuilder(102);
        sb.append("Not an APK file: ZIP End of Central Directory record not found in file with ");
        sb.append(length);
        sb.append(" bytes");
        throw new D(sb.toString());
    }

    private static ByteBuffer O00000o0(ByteBuffer byteBuffer) {
        if (byteBuffer.remaining() >= 4) {
            int i = byteBuffer.getInt();
            if (i < 0) {
                throw new IllegalArgumentException("Negative length");
            } else if (i <= byteBuffer.remaining()) {
                return O00000Oo(byteBuffer, i);
            } else {
                int remaining = byteBuffer.remaining();
                StringBuilder sb = new StringBuilder(101);
                sb.append("Length-prefixed field longer than remaining buffer. Field length: ");
                sb.append(i);
                sb.append(", remaining: ");
                sb.append(remaining);
                throw new IOException(sb.toString());
            }
        } else {
            int remaining2 = byteBuffer.remaining();
            StringBuilder sb2 = new StringBuilder(93);
            sb2.append("Remaining buffer too short to contain length of length-prefixed field. Remaining: ");
            sb2.append(remaining2);
            throw new IOException(sb2.toString());
        }
    }

    private static int O00000oO(int i, int i2) {
        String str = "Unknown digestAlgorithm2: ";
        if (i != 1) {
            if (i != 2) {
                StringBuilder sb = new StringBuilder(37);
                sb.append("Unknown digestAlgorithm1: ");
                sb.append(i);
                throw new IllegalArgumentException(sb.toString());
            } else if (i2 == 1) {
                return 1;
            } else {
                if (i2 == 2) {
                    return 0;
                }
                StringBuilder sb2 = new StringBuilder(37);
                sb2.append(str);
                sb2.append(i2);
                throw new IllegalArgumentException(sb2.toString());
            }
        } else if (i2 == 1) {
            return 0;
        } else {
            if (i2 == 2) {
                return -1;
            }
            StringBuilder sb3 = new StringBuilder(37);
            sb3.append(str);
            sb3.append(i2);
            throw new IllegalArgumentException(sb3.toString());
        }
    }

    private static ByteBuffer O00000oO(ByteBuffer byteBuffer) {
        O00000oo(byteBuffer);
        ByteBuffer O00000Oo2 = O00000Oo(byteBuffer, 8, byteBuffer.capacity() - 24);
        int i = 0;
        while (O00000Oo2.hasRemaining()) {
            i++;
            if (O00000Oo2.remaining() >= 8) {
                long j = O00000Oo2.getLong();
                String str = " size out of range: ";
                String str2 = "APK Signing Block entry #";
                if (j < 4 || j > 2147483647L) {
                    StringBuilder sb = new StringBuilder(76);
                    sb.append(str2);
                    sb.append(i);
                    sb.append(str);
                    sb.append(j);
                    throw new D(sb.toString());
                }
                int i2 = (int) j;
                int position = O00000Oo2.position() + i2;
                if (i2 > O00000Oo2.remaining()) {
                    int remaining = O00000Oo2.remaining();
                    StringBuilder sb2 = new StringBuilder(91);
                    sb2.append(str2);
                    sb2.append(i);
                    sb2.append(str);
                    sb2.append(i2);
                    sb2.append(", available: ");
                    sb2.append(remaining);
                    throw new D(sb2.toString());
                } else if (O00000Oo2.getInt() == 1896449818) {
                    return O00000Oo(O00000Oo2, i2 - 4);
                } else {
                    O00000Oo2.position(position);
                }
            } else {
                StringBuilder sb3 = new StringBuilder(70);
                sb3.append("Insufficient data to read size of APK Signing Block entry #");
                sb3.append(i);
                throw new D(sb3.toString());
            }
        }
        throw new D("No APK Signature Scheme v2 block in APK Signing Block");
    }

    private static void O00000oo(ByteBuffer byteBuffer) {
        if (byteBuffer.order() != ByteOrder.LITTLE_ENDIAN) {
            throw new IllegalArgumentException("ByteBuffer byte order must be little endian");
        }
    }

    private static Pair O000O0OO(int i) {
        if (i == 513) {
            return Pair.create("SHA256withECDSA", null);
        }
        if (i == 514) {
            return Pair.create("SHA512withECDSA", null);
        }
        if (i == 769) {
            return Pair.create("SHA256withDSA", null);
        }
        switch (i) {
            case 257:
                PSSParameterSpec pSSParameterSpec = new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1);
                return Pair.create("SHA256withRSA/PSS", pSSParameterSpec);
            case 258:
                PSSParameterSpec pSSParameterSpec2 = new PSSParameterSpec("SHA-512", "MGF1", MGF1ParameterSpec.SHA512, 64, 1);
                return Pair.create("SHA512withRSA/PSS", pSSParameterSpec2);
            case 259:
                return Pair.create("SHA256withRSA", null);
            case 260:
                return Pair.create("SHA512withRSA", null);
            default:
                String str = "Unknown signature algorithm: 0x";
                String valueOf = String.valueOf(Long.toHexString((long) i));
                throw new IllegalArgumentException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        }
    }

    private static int a(int i, int i2) {
        return O00000oO(b(i), b(i2));
    }

    private static long a(long j) {
        return ((j + PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) - 1) / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED;
    }

    private static boolean a(int i) {
        if (!(i == 513 || i == 514 || i == 769)) {
            switch (i) {
                case 257:
                case 258:
                case 259:
                case 260:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    public static X509Certificate[][] a(String str) {
        RandomAccessFile randomAccessFile = new RandomAccessFile(str, "r");
        try {
            X509Certificate[][] O000000o2 = O000000o(randomAccessFile);
            randomAccessFile.close();
            return O000000o2;
        } finally {
            try {
                randomAccessFile.close();
            } catch (IOException unused) {
            }
        }
    }

    private static int b(int i) {
        if (i != 513) {
            if (i != 514) {
                if (i != 769) {
                    switch (i) {
                        case 257:
                        case 259:
                            break;
                        case 258:
                        case 260:
                            break;
                        default:
                            String str = "Unknown signature algorithm: 0x";
                            String valueOf = String.valueOf(Long.toHexString((long) i));
                            throw new IllegalArgumentException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
                    }
                }
            }
            return 2;
        }
        return 1;
    }

    private static String c(int i) {
        if (i == 1) {
            return "SHA-256";
        }
        if (i == 2) {
            return "SHA-512";
        }
        StringBuilder sb = new StringBuilder(44);
        sb.append("Unknown content digest algorthm: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }

    private static int d(int i) {
        if (i == 1) {
            return 32;
        }
        if (i == 2) {
            return 64;
        }
        StringBuilder sb = new StringBuilder(44);
        sb.append("Unknown content digest algorthm: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }

    private static String e(int i) {
        if (i == 513 || i == 514) {
            return "EC";
        }
        if (i == 769) {
            return "DSA";
        }
        switch (i) {
            case 257:
            case 258:
            case 259:
            case 260:
                return "RSA";
            default:
                String str = "Unknown signature algorithm: 0x";
                String valueOf = String.valueOf(Long.toHexString((long) i));
                throw new IllegalArgumentException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        }
    }

    public void O000000o(MessageDigest[] messageDigestArr, long j, int i) {
        ByteBuffer slice;
        synchronized (this.a) {
            int i2 = (int) j;
            this.a.position(i2);
            this.a.limit(i2 + i);
            slice = this.a.slice();
        }
        for (MessageDigest messageDigest : messageDigestArr) {
            slice.position(0);
            messageDigest.update(slice);
        }
    }

    public long a() {
        return (long) this.a.capacity();
    }
}
