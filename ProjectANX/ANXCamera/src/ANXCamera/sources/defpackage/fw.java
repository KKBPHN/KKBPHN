package defpackage;

/* renamed from: fw reason: default package */
final class fw extends ft {
    private static int O000000o(byte[] bArr, int i, long j, int i2) {
        if (i2 == 0) {
            return fx.a(i);
        }
        if (i2 == 1) {
            return fx.a(i, fr.O000000o(bArr, j));
        }
        if (i2 == 2) {
            return fx.O000000o(i, (int) fr.O000000o(bArr, j), (int) fr.O000000o(bArr, j + 1));
        }
        throw new AssertionError();
    }

    public final int O000000o(CharSequence charSequence, byte[] bArr, int i, int i2) {
        char c;
        long j;
        int i3;
        long j2;
        char c2;
        long j3;
        CharSequence charSequence2 = charSequence;
        byte[] bArr2 = bArr;
        int i4 = i;
        int i5 = i2;
        long j4 = (long) i4;
        long j5 = ((long) i5) + j4;
        int length = charSequence.length();
        String str = " at index ";
        String str2 = "Failed writing ";
        if (length > i5 || bArr2.length - i5 < i4) {
            char charAt = charSequence2.charAt(length - 1);
            StringBuilder sb = new StringBuilder(37);
            sb.append(str2);
            sb.append(charAt);
            sb.append(str);
            sb.append(i4 + i5);
            throw new ArrayIndexOutOfBoundsException(sb.toString());
        }
        int i6 = 0;
        while (true) {
            c = 128;
            j = 1;
            if (i6 >= length) {
                break;
            }
            char charAt2 = charSequence2.charAt(i6);
            if (charAt2 >= 128) {
                break;
            }
            long j6 = 1 + j4;
            fr.O000000o(bArr2, j4, (byte) charAt2);
            i6++;
            j4 = j6;
        }
        if (i6 == length) {
            return (int) j4;
        }
        while (i3 < length) {
            char charAt3 = charSequence2.charAt(i3);
            if (charAt3 < c && j4 < j5) {
                long j7 = j4 + j;
                fr.O000000o(bArr2, j4, (byte) charAt3);
                j3 = j;
                j2 = j7;
                c2 = c;
            } else if (charAt3 < 2048 && j4 <= -2 + j5) {
                long j8 = j4 + j;
                fr.O000000o(bArr2, j4, (byte) ((charAt3 >>> 6) | 960));
                long j9 = j8 + j;
                fr.O000000o(bArr2, j8, (byte) ((charAt3 & '?') | 128));
                long j10 = j;
                c2 = 128;
                j2 = j9;
                j3 = j10;
            } else if ((charAt3 < 55296 || charAt3 > 57343) && j4 <= -3 + j5) {
                long j11 = j4 + j;
                fr.O000000o(bArr2, j4, (byte) ((charAt3 >>> 12) | 480));
                long j12 = j11 + j;
                fr.O000000o(bArr2, j11, (byte) (((charAt3 >>> 6) & 63) | 128));
                long j13 = j12 + 1;
                fr.O000000o(bArr2, j12, (byte) ((charAt3 & '?') | 128));
                j2 = j13;
                j3 = 1;
                c2 = 128;
            } else if (j4 <= -4 + j5) {
                int i7 = i3 + 1;
                if (i7 != length) {
                    char charAt4 = charSequence2.charAt(i7);
                    if (Character.isSurrogatePair(charAt3, charAt4)) {
                        int codePoint = Character.toCodePoint(charAt3, charAt4);
                        long j14 = j4 + 1;
                        fr.O000000o(bArr2, j4, (byte) ((codePoint >>> 18) | 240));
                        long j15 = j14 + 1;
                        c2 = 128;
                        fr.O000000o(bArr2, j14, (byte) (((codePoint >>> 12) & 63) | 128));
                        long j16 = j15 + 1;
                        fr.O000000o(bArr2, j15, (byte) (((codePoint >>> 6) & 63) | 128));
                        j3 = 1;
                        j2 = j16 + 1;
                        fr.O000000o(bArr2, j16, (byte) ((codePoint & 63) | 128));
                        i3 = i7;
                    } else {
                        i3 = i7;
                    }
                }
                throw new fv(i3 - 1, length);
            } else {
                if (charAt3 >= 55296 && charAt3 <= 57343) {
                    int i8 = i3 + 1;
                    if (i8 == length || !Character.isSurrogatePair(charAt3, charSequence2.charAt(i8))) {
                        throw new fv(i3, length);
                    }
                }
                StringBuilder sb2 = new StringBuilder(46);
                sb2.append(str2);
                sb2.append(charAt3);
                sb2.append(str);
                sb2.append(j4);
                throw new ArrayIndexOutOfBoundsException(sb2.toString());
            }
            i6 = i3 + 1;
            c = c2;
            long j17 = j3;
            j4 = j2;
            j = j17;
        }
        return (int) j4;
    }

    public final String O00000o(byte[] bArr, int i, int i2) {
        int i3;
        int length = bArr.length;
        if ((i | i2 | ((length - i) - i2)) >= 0) {
            int i4 = i + i2;
            char[] cArr = new char[i2];
            int i5 = 0;
            while (r11 < i4) {
                byte O000000o2 = fr.O000000o(bArr, (long) r11);
                if (!fs.O000000o(O000000o2)) {
                    break;
                }
                i = r11 + 1;
                int i6 = i3 + 1;
                fs.O000000o(O000000o2, cArr, i3);
                i5 = i6;
            }
            while (r11 < i4) {
                int i7 = r11 + 1;
                byte O000000o3 = fr.O000000o(bArr, (long) r11);
                if (fs.O000000o(O000000o3)) {
                    int i8 = i3 + 1;
                    fs.O000000o(O000000o3, cArr, i3);
                    r11 = i7;
                    while (true) {
                        i3 = i8;
                        if (r11 >= i4) {
                            break;
                        }
                        byte O000000o4 = fr.O000000o(bArr, (long) r11);
                        if (!fs.O000000o(O000000o4)) {
                            break;
                        }
                        r11++;
                        i8 = i3 + 1;
                        fs.O000000o(O000000o4, cArr, i3);
                    }
                } else if (!fs.O00000Oo(O000000o3)) {
                    if (!fs.O00000o0(O000000o3)) {
                        if (i7 < i4 - 2) {
                            int i9 = i7 + 1;
                            int i10 = i9 + 1;
                            int i11 = i10 + 1;
                            fs.O000000o(O000000o3, fr.O000000o(bArr, (long) i7), fr.O000000o(bArr, (long) i9), fr.O000000o(bArr, (long) i10), cArr, i3);
                            i3 += 2;
                            r11 = i11;
                        } else {
                            throw dl.f();
                        }
                    } else if (i7 < i4 - 1) {
                        int i12 = i7 + 1;
                        int i13 = i12 + 1;
                        int i14 = i3 + 1;
                        fs.O000000o(O000000o3, fr.O000000o(bArr, (long) i7), fr.O000000o(bArr, (long) i12), cArr, i3);
                        r11 = i13;
                        i3 = i14;
                    } else {
                        throw dl.f();
                    }
                } else if (i7 < i4) {
                    int i15 = i7 + 1;
                    int i16 = i3 + 1;
                    fs.O000000o(O000000o3, fr.O000000o(bArr, (long) i7), cArr, i3);
                    r11 = i15;
                    i3 = i16;
                } else {
                    throw dl.f();
                }
            }
            return new String(cArr, 0, i3);
        }
        throw new ArrayIndexOutOfBoundsException(String.format("buffer length=%d, index=%d, size=%d", new Object[]{Integer.valueOf(length), Integer.valueOf(i), Integer.valueOf(i2)}));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0098, code lost:
        r3 = O000000o(r13, r14, r4, r12);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int O00000oO(byte[] bArr, int i, int i2) {
        int i3;
        long j;
        int length = bArr.length;
        int i4 = 0;
        if ((i | i2 | (length - i2)) >= 0) {
            long j2 = (long) i;
            int i5 = (int) (((long) i2) - j2);
            if (i5 >= 16) {
                i3 = 0;
                long j3 = j2;
                while (true) {
                    if (i3 >= i5) {
                        i3 = i5;
                        break;
                    }
                    long j4 = j3 + 1;
                    if (fr.O000000o(bArr, j3) < 0) {
                        break;
                    }
                    i3++;
                    j3 = j4;
                }
            } else {
                i3 = 0;
            }
            int i6 = i5 - i3;
            long j5 = j2 + ((long) i3);
            while (true) {
                int i7 = 0;
                while (true) {
                    if (i6 <= 0) {
                        break;
                    }
                    long j6 = j5 + 1;
                    byte O000000o2 = fr.O000000o(bArr, j5);
                    if (O000000o2 < 0) {
                        j5 = j6;
                        i7 = O000000o2;
                        break;
                    }
                    i6--;
                    j5 = j6;
                    i7 = O000000o2;
                }
                if (i6 == 0) {
                    break;
                }
                int i8 = i6 - 1;
                if (i7 >= -32) {
                    if (i7 < -16) {
                        if (i8 < 2) {
                            break;
                        }
                        i6 = i8 - 2;
                        long j7 = j5 + 1;
                        byte O000000o3 = fr.O000000o(bArr, j5);
                        if (O000000o3 > -65) {
                            return -1;
                        }
                        if (i7 == -32 && O000000o3 < -96) {
                            return -1;
                        }
                        if (i7 == -19 && O000000o3 >= -96) {
                            return -1;
                        }
                        j5 = j7 + 1;
                        if (fr.O000000o(bArr, j7) > -65) {
                            return -1;
                        }
                    } else if (i8 < 3) {
                        break;
                    } else {
                        i6 = i8 - 3;
                        long j8 = j5 + 1;
                        byte O000000o4 = fr.O000000o(bArr, j5);
                        if (O000000o4 > -65 || (((i7 << 28) + (O000000o4 + 112)) >> 30) != 0) {
                            return -1;
                        }
                        long j9 = j8 + 1;
                        if (fr.O000000o(bArr, j8) > -65) {
                            return -1;
                        }
                        j = j9 + 1;
                        if (fr.O000000o(bArr, j9) > -65) {
                            return -1;
                        }
                    }
                } else if (i8 == 0) {
                    return i7;
                } else {
                    i6 = i8 - 1;
                    if (i7 < -62) {
                        return -1;
                    }
                    j = j5 + 1;
                    if (fr.O000000o(bArr, j5) > -65) {
                        return -1;
                    }
                }
                j5 = j;
            }
            return i4;
        }
        throw new ArrayIndexOutOfBoundsException(String.format("Array length=%d, index=%d, limit=%d", new Object[]{Integer.valueOf(length), Integer.valueOf(i), Integer.valueOf(i2)}));
    }
}
