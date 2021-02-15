package com.airbnb.lottie.parser.moshi;

import java.io.Closeable;
import java.util.Arrays;
import okio.BufferedSink;
import okio.BufferedSource;

public abstract class O00000Oo implements Closeable {
    private static final String[] REPLACEMENT_CHARS = new String[128];
    int[] O0O0oOo = new int[32];
    int[] O0O0oo = new int[32];
    String[] O0O0oo0 = new String[32];
    boolean O0O0ooO;
    boolean lenient;
    int stackSize;

    static {
        for (int i = 0; i <= 31; i++) {
            REPLACEMENT_CHARS[i] = String.format("\\u%04x", new Object[]{Integer.valueOf(i)});
        }
        String[] strArr = REPLACEMENT_CHARS;
        strArr[34] = "\\\"";
        strArr[92] = "\\\\";
        strArr[9] = "\\t";
        strArr[8] = "\\b";
        strArr[10] = "\\n";
        strArr[13] = "\\r";
        strArr[12] = "\\f";
    }

    O00000Oo() {
    }

    public static O00000Oo O000000o(BufferedSource bufferedSource) {
        return new O00000o(bufferedSource);
    }

    /* access modifiers changed from: private */
    public static void O00000Oo(BufferedSink bufferedSink, String str) {
        String str2;
        String[] strArr = REPLACEMENT_CHARS;
        bufferedSink.writeByte(34);
        int length = str.length();
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = str.charAt(i2);
            if (charAt < 128) {
                str2 = strArr[charAt];
                if (str2 == null) {
                }
            } else if (charAt == 8232) {
                str2 = "\\u2028";
            } else if (charAt == 8233) {
                str2 = "\\u2029";
            }
            if (i < i2) {
                bufferedSink.writeUtf8(str, i, i2);
            }
            bufferedSink.writeUtf8(str2);
            i = i2 + 1;
        }
        if (i < length) {
            bufferedSink.writeUtf8(str, i, length);
        }
        bufferedSink.writeByte(34);
    }

    public abstract int O000000o(O000000o o000000o);

    /* access modifiers changed from: 0000 */
    public final void O0000o0(int i) {
        int i2 = this.stackSize;
        int[] iArr = this.O0O0oOo;
        if (i2 == iArr.length) {
            if (i2 != 256) {
                this.O0O0oOo = Arrays.copyOf(iArr, iArr.length * 2);
                String[] strArr = this.O0O0oo0;
                this.O0O0oo0 = (String[]) Arrays.copyOf(strArr, strArr.length * 2);
                int[] iArr2 = this.O0O0oo;
                this.O0O0oo = Arrays.copyOf(iArr2, iArr2.length * 2);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Nesting too deep at ");
                sb.append(getPath());
                throw new JsonDataException(sb.toString());
            }
        }
        int[] iArr3 = this.O0O0oOo;
        int i3 = this.stackSize;
        this.stackSize = i3 + 1;
        iArr3[i3] = i;
    }

    public abstract void O00o0O0();

    public abstract void beginArray();

    public abstract void beginObject();

    public abstract void endArray();

    public abstract void endObject();

    public final String getPath() {
        return O00000o0.O000000o(this.stackSize, this.O0O0oOo, this.O0O0oo0, this.O0O0oo);
    }

    public abstract boolean hasNext();

    public abstract boolean nextBoolean();

    public abstract double nextDouble();

    public abstract int nextInt();

    public abstract String nextName();

    public abstract String nextString();

    public abstract JsonReader$Token peek();

    public abstract void skipValue();

    /* access modifiers changed from: 0000 */
    public final JsonEncodingException syntaxError(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" at path ");
        sb.append(getPath());
        throw new JsonEncodingException(sb.toString());
    }
}
