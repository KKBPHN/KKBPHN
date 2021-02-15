package defpackage;

/* renamed from: fu reason: default package */
final class fu extends ft {
    public final int O000000o(CharSequence charSequence, byte[] bArr, int i, int i2) {
        int i3;
        int i4;
        int length = charSequence.length();
        int i5 = i2 + i;
        int i6 = 0;
        while (i6 < length) {
            int i7 = i6 + i;
            if (i7 >= i5) {
                break;
            }
            char charAt = charSequence.charAt(i6);
            if (charAt >= 128) {
                break;
            }
            bArr[i7] = (byte) charAt;
            i6++;
        }
        if (i6 == length) {
            return i + length;
        }
        int i8 = i + i6;
        while (i3 < length) {
            char charAt2 = charSequence.charAt(i3);
            if (charAt2 < 128 && i8 < i5) {
                i4 = i8 + 1;
                bArr[i8] = (byte) charAt2;
            } else if (charAt2 < 2048 && i8 <= i5 - 2) {
                int i9 = i8 + 1;
                bArr[i8] = (byte) ((charAt2 >>> 6) | 960);
                i8 = i9 + 1;
                bArr[i9] = (byte) ((charAt2 & '?') | 128);
                i6 = i3 + 1;
            } else if ((charAt2 < 55296 || charAt2 > 57343) && i8 <= i5 - 3) {
                int i10 = i8 + 1;
                bArr[i8] = (byte) ((charAt2 >>> 12) | 480);
                int i11 = i10 + 1;
                bArr[i10] = (byte) (((charAt2 >>> 6) & 63) | 128);
                i4 = i11 + 1;
                bArr[i11] = (byte) ((charAt2 & '?') | 128);
            } else if (i8 <= i5 - 4) {
                int i12 = i3 + 1;
                if (i12 != charSequence.length()) {
                    char charAt3 = charSequence.charAt(i12);
                    if (Character.isSurrogatePair(charAt2, charAt3)) {
                        int codePoint = Character.toCodePoint(charAt2, charAt3);
                        int i13 = i8 + 1;
                        bArr[i8] = (byte) ((codePoint >>> 18) | 240);
                        int i14 = i13 + 1;
                        bArr[i13] = (byte) (((codePoint >>> 12) & 63) | 128);
                        int i15 = i14 + 1;
                        bArr[i14] = (byte) (((codePoint >>> 6) & 63) | 128);
                        i8 = i15 + 1;
                        bArr[i15] = (byte) ((codePoint & 63) | 128);
                        i3 = i12;
                        i6 = i3 + 1;
                    } else {
                        i3 = i12;
                    }
                }
                throw new fv(i3 - 1, length);
            } else {
                if (charAt2 >= 55296 && charAt2 <= 57343) {
                    int i16 = i3 + 1;
                    if (i16 == charSequence.length() || !Character.isSurrogatePair(charAt2, charSequence.charAt(i16))) {
                        throw new fv(i3, length);
                    }
                }
                StringBuilder sb = new StringBuilder(37);
                sb.append("Failed writing ");
                sb.append(charAt2);
                sb.append(" at index ");
                sb.append(i8);
                throw new ArrayIndexOutOfBoundsException(sb.toString());
            }
            i8 = i4;
            i6 = i3 + 1;
        }
        return i8;
    }

    public final String O00000o(byte[] bArr, int i, int i2) {
        int i3;
        int length = bArr.length;
        if ((i | i2 | ((length - i) - i2)) >= 0) {
            int i4 = i + i2;
            char[] cArr = new char[i2];
            int i5 = 0;
            while (r11 < i4) {
                byte b = bArr[r11];
                if (!fs.O000000o(b)) {
                    break;
                }
                i = r11 + 1;
                int i6 = i3 + 1;
                fs.O000000o(b, cArr, i3);
                i5 = i6;
            }
            while (r11 < i4) {
                int i7 = r11 + 1;
                byte b2 = bArr[r11];
                if (fs.O000000o(b2)) {
                    int i8 = i3 + 1;
                    fs.O000000o(b2, cArr, i3);
                    r11 = i7;
                    while (true) {
                        i3 = i8;
                        if (r11 >= i4) {
                            break;
                        }
                        byte b3 = bArr[r11];
                        if (!fs.O000000o(b3)) {
                            break;
                        }
                        r11++;
                        i8 = i3 + 1;
                        fs.O000000o(b3, cArr, i3);
                    }
                } else if (!fs.O00000Oo(b2)) {
                    if (!fs.O00000o0(b2)) {
                        if (i7 < i4 - 2) {
                            int i9 = i7 + 1;
                            int i10 = i9 + 1;
                            int i11 = i10 + 1;
                            fs.O000000o(b2, bArr[i7], bArr[i9], bArr[i10], cArr, i3);
                            i3 += 2;
                            r11 = i11;
                        } else {
                            throw dl.f();
                        }
                    } else if (i7 < i4 - 1) {
                        int i12 = i7 + 1;
                        int i13 = i12 + 1;
                        int i14 = i3 + 1;
                        fs.O000000o(b2, bArr[i7], bArr[i12], cArr, i3);
                        r11 = i13;
                        i3 = i14;
                    } else {
                        throw dl.f();
                    }
                } else if (i7 < i4) {
                    int i15 = i7 + 1;
                    int i16 = i3 + 1;
                    fs.O000000o(b2, bArr[i7], cArr, i3);
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

    public final int O00000oO(byte[] bArr, int i, int i2) {
        int i3;
        while (r8 < i2 && bArr[r8] >= 0) {
            i = r8 + 1;
        }
        int i4 = 0;
        if (r8 < i2) {
            while (true) {
                if (r8 >= i2) {
                    break;
                }
                i3 = r8 + 1;
                byte b = bArr[r8];
                if (b < 0) {
                    if (b >= -32) {
                        if (b < -16) {
                            if (i3 >= i2 - 1) {
                                break;
                            }
                            int i5 = i3 + 1;
                            byte b2 = bArr[i3];
                            if (b2 > -65) {
                                return -1;
                            }
                            if (b == -32 && b2 < -96) {
                                return -1;
                            }
                            if (b == -19 && b2 >= -96) {
                                return -1;
                            }
                            r8 = i5 + 1;
                            if (bArr[i5] > -65) {
                                return -1;
                            }
                        } else if (i3 >= i2 - 2) {
                            break;
                        } else {
                            int i6 = i3 + 1;
                            byte b3 = bArr[i3];
                            if (b3 > -65 || (((b << 28) + (b3 + 112)) >> 30) != 0) {
                                return -1;
                            }
                            int i7 = i6 + 1;
                            if (bArr[i6] > -65) {
                                return -1;
                            }
                            i3 = i7 + 1;
                            if (bArr[i7] > -65) {
                                return -1;
                            }
                        }
                    } else if (i3 >= i2) {
                        return b;
                    } else {
                        if (b < -62) {
                            return -1;
                        }
                        r8 = i3 + 1;
                        if (bArr[i3] > -65) {
                            return -1;
                        }
                    }
                }
                r8 = i3;
            }
            i4 = fx.O00000o(bArr, i3, i2);
        }
        return i4;
    }
}
