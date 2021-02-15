package miuix.core.util;

import android.text.TextUtils;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Utf8TextUtils {
    private static final int MAX_LENGTH = 6;
    private static final int MIN_LENGTH = 1;
    private static final String TAG = "Utf8TextUtils";
    private static final String UTF8 = "UTF-8";

    class CharRange {
        int length = -1;
        int start = -1;

        CharRange() {
        }

        CharRange(int i, int i2) {
            this.start = i;
            this.length = i2;
        }

        /* access modifiers changed from: 0000 */
        public int getEndIndex() {
            return this.start + this.length;
        }

        /* access modifiers changed from: 0000 */
        public boolean isValid() {
            return this.start >= 0 && this.length > 0;
        }
    }

    private Utf8TextUtils() {
    }

    private static CharRange findRange(byte[] bArr, int i, int i2) {
        CharRange charRange = new CharRange();
        if (isValidCharacter(bArr, i, i2)) {
            charRange.start = i;
            charRange.length = i2;
        }
        return charRange;
    }

    private static int getByteCount(byte b) {
        int i = 0;
        int i2 = 7;
        while (i2 >= 1 && (1 & ((byte) (b >> i2))) != 0) {
            i++;
            i2--;
        }
        return i;
    }

    private static CharRange getCharRangeAt(byte[] bArr, int i) {
        int byteCount = getByteCount(bArr[i]);
        return byteCount == 0 ? new CharRange(i, 1) : findRange(bArr, i, byteCount);
    }

    private static List getUtf8CharList(byte[] bArr) {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            if (i >= bArr.length) {
                break;
            }
            CharRange charRangeAt = getCharRangeAt(bArr, i);
            if (!charRangeAt.isValid()) {
                arrayList.clear();
                break;
            }
            arrayList.add(charRangeAt);
            i += charRangeAt.length;
        }
        return arrayList;
    }

    private static boolean isValidCharacter(byte[] bArr, int i, int i2) {
        if (i2 <= 1 || i2 > 6) {
            return false;
        }
        for (int i3 = 1; i3 < i2; i3++) {
            if (getByteCount(bArr[i + i3]) != 1) {
                return false;
            }
        }
        return true;
    }

    public static String subString(String str, int i, int i2) {
        String str2 = "";
        if (!TextUtils.isEmpty(str) && i2 > i) {
            byte[] bytes = str.getBytes();
            List utf8CharList = getUtf8CharList(bytes);
            if (utf8CharList.isEmpty()) {
                return str.substring(i, i2);
            }
            if (i >= 0 && i < utf8CharList.size()) {
                int size = utf8CharList.size();
                int i3 = ((CharRange) utf8CharList.get(i)).start;
                int length = (i2 >= size ? bytes.length : ((CharRange) utf8CharList.get(i2)).start) - i3;
                byte[] bArr = new byte[length];
                System.arraycopy(bytes, i3, bArr, 0, length);
                try {
                    return new String(bArr, "UTF-8");
                } catch (UnsupportedEncodingException unused) {
                }
            }
        }
        return str2;
    }

    public static String truncateByte(String str, int i) {
        try {
            byte[] bytes = str.getBytes();
            List utf8CharList = getUtf8CharList(bytes);
            if (utf8CharList.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                while (sb.toString().getBytes().length < i) {
                    sb.append(str.charAt(sb.length()));
                }
                if (sb.toString().getBytes().length > i) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                return sb.toString();
            }
            int length = bytes.length;
            int size = utf8CharList.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                CharRange charRange = (CharRange) utf8CharList.get(size);
                if (charRange.start >= i) {
                    size--;
                } else {
                    length = charRange.getEndIndex();
                    if (length > i) {
                        length = charRange.start;
                    }
                }
            }
            if (length >= bytes.length) {
                return str;
            }
            byte[] bArr = new byte[length];
            System.arraycopy(bytes, 0, bArr, 0, length);
            return new String(bArr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("failed to get bytes of UTF-8 from ");
            sb2.append(str);
            sb2.append(", ");
            sb2.append(e);
            Log.w(TAG, sb2.toString());
            return null;
        }
    }
}
