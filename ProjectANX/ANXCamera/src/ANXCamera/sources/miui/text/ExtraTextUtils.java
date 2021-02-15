package miui.text;

import android.content.Context;
import com.miui.internal.R;
import java.io.IOException;
import miui.util.Pools;

public class ExtraTextUtils {
    public static final long GB = 1000000000;
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static final long KB = 1000;
    public static final long MB = 1000000;
    private static final long UNIT = 1000;

    protected ExtraTextUtils() {
        throw new InstantiationException("Cannot instantiate utility class");
    }

    public static String formatFileSize(Context context, long j) {
        return formatFileSize(context, j, false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00b0, code lost:
        if (r8.charAt(r10 - 1) == '0') goto L_0x00c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00c2, code lost:
        if (r8.charAt(r10 - 1) == '0') goto L_0x00c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00c4, code lost:
        r8 = r8.substring(0, r1);
     */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x009a  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00b5  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static String formatFileSize(Context context, long j, boolean z) {
        String str;
        int length;
        int i;
        Object[] objArr;
        Object[] objArr2;
        if (context == null) {
            return "";
        }
        float f = (float) j;
        int i2 = R.string.size_byte;
        if (f > 900.0f) {
            i2 = R.string.size_kilo_byte;
            f /= 1000.0f;
        }
        if (f > 900.0f) {
            i2 = R.string.size_mega_byte;
            f /= 1000.0f;
        }
        if (f > 900.0f) {
            i2 = R.string.size_giga_byte;
            f /= 1000.0f;
        }
        if (f > 900.0f) {
            i2 = R.string.size_tera_byte;
            f /= 1000.0f;
        }
        if (f > 900.0f) {
            i2 = R.string.size_peta_byte;
            f /= 1000.0f;
        }
        String str2 = "%.2f";
        if (f < 1.0f) {
            objArr2 = new Object[]{Float.valueOf(f)};
        } else if (f >= 10.0f) {
            String str3 = "%.0f";
            if (f >= 100.0f) {
                objArr = new Object[]{Float.valueOf(f)};
            } else if (z) {
                objArr = new Object[]{Float.valueOf(f)};
            } else {
                objArr2 = new Object[]{Float.valueOf(f)};
            }
            str = String.format(str3, objArr);
            length = str.length();
            if (length > 3) {
            }
            if (length > 2) {
            }
            return context.getResources().getString(R.string.size_suffix, new Object[]{str, context.getString(i2)});
        } else if (z) {
            str = String.format("%.1f", new Object[]{Float.valueOf(f)});
            length = str.length();
            if (length > 3) {
                i = length - 3;
                if (str.charAt(i) == '.') {
                    if (str.charAt(length - 2) == '0') {
                    }
                }
            }
            if (length > 2) {
                i = length - 2;
                if (str.charAt(i) == '.') {
                }
            }
            return context.getResources().getString(R.string.size_suffix, new Object[]{str, context.getString(i2)});
        } else {
            objArr2 = new Object[]{Float.valueOf(f)};
        }
        str = String.format(str2, objArr2);
        length = str.length();
        if (length > 3) {
        }
        if (length > 2) {
        }
        return context.getResources().getString(R.string.size_suffix, new Object[]{str, context.getString(i2)});
    }

    public static String formatShortFileSize(Context context, long j) {
        return formatFileSize(context, j, true);
    }

    public static byte[] fromHexReadable(String str) {
        int i;
        int i2;
        int i3;
        int i4;
        int length = str.length();
        String str2 = "s is not a readable string: ";
        if (length % 2 == 0) {
            byte[] bArr = new byte[(length >> 1)];
            for (int i5 = 0; i5 < length; i5 += 2) {
                char charAt = str.charAt(i5);
                if (charAt < '0' || charAt > '9') {
                    if (charAt >= 'a' && charAt <= 'f') {
                        i4 = charAt - 'a';
                    } else if (charAt < 'A' || charAt > 'F') {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str2);
                        sb.append(str);
                        throw new IllegalArgumentException(sb.toString());
                    } else {
                        i4 = charAt - 'A';
                    }
                    i = i4 + 10;
                } else {
                    i = charAt - '0';
                }
                int i6 = i << 4;
                char charAt2 = str.charAt(i5 + 1);
                if (charAt2 < '0' || charAt2 > '9') {
                    if (charAt2 >= 'a' && charAt2 <= 'f') {
                        i2 = charAt2 - 'a';
                    } else if (charAt2 < 'A' || charAt2 > 'F') {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str2);
                        sb2.append(str);
                        throw new IllegalArgumentException(sb2.toString());
                    } else {
                        i2 = charAt2 - 'A';
                    }
                    i3 = i2 + 10;
                } else {
                    i3 = charAt2 - '0';
                }
                bArr[i5 >> 1] = (byte) (i6 + i3);
            }
            return bArr;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str2);
        sb3.append(str);
        throw new IllegalArgumentException(sb3.toString());
    }

    public static String toHexReadable(byte[] bArr) {
        StringBuilder sb = (StringBuilder) Pools.getStringBuilderPool().acquire();
        toHexReadable(bArr, sb);
        String sb2 = sb.toString();
        Pools.getStringBuilderPool().release(sb);
        return sb2;
    }

    /* JADX WARNING: type inference failed for: r2v1, types: [int] */
    /* JADX WARNING: type inference failed for: r2v5 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void toHexReadable(byte[] bArr, Appendable appendable) {
        if (bArr != 0) {
            try {
                for (byte b : bArr) {
                    if (b < 0) {
                        b += 256;
                    }
                    appendable.append(HEX_DIGITS[b >> 4]).append(HEX_DIGITS[b & 15]);
                }
            } catch (IOException e) {
                throw new RuntimeException("Exception throw during when append", e);
            }
        }
    }
}
