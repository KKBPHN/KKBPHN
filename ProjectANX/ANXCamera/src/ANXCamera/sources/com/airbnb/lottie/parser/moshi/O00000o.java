package com.airbnb.lottie.parser.moshi;

import androidx.annotation.Nullable;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import java.io.EOFException;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;

final class O00000o extends O00000Oo {
    private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
    private static final int NUMBER_CHAR_DECIMAL = 3;
    private static final int NUMBER_CHAR_DIGIT = 2;
    private static final int NUMBER_CHAR_EXP_DIGIT = 7;
    private static final int NUMBER_CHAR_EXP_E = 5;
    private static final int NUMBER_CHAR_EXP_SIGN = 6;
    private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
    private static final int NUMBER_CHAR_NONE = 0;
    private static final int NUMBER_CHAR_SIGN = 1;
    private static final ByteString O0O0ooo = ByteString.encodeUtf8("'\\");
    private static final ByteString O0OO00O = ByteString.encodeUtf8("\n\r");
    private static final ByteString O0OO00o = ByteString.encodeUtf8("*/");
    private static final int O0OO0O = 15;
    private static final ByteString O0OoOO = ByteString.encodeUtf8("{}[]:, \n\t\r\f/\\;#=");
    private static final ByteString O0OoOo = ByteString.encodeUtf8("\"\\");
    private static final int PEEKED_BEGIN_ARRAY = 3;
    private static final int PEEKED_BEGIN_OBJECT = 1;
    private static final int PEEKED_BUFFERED = 11;
    private static final int PEEKED_DOUBLE_QUOTED = 9;
    private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
    private static final int PEEKED_END_ARRAY = 4;
    private static final int PEEKED_END_OBJECT = 2;
    private static final int PEEKED_EOF = 18;
    private static final int PEEKED_FALSE = 6;
    private static final int PEEKED_LONG = 16;
    private static final int PEEKED_NONE = 0;
    private static final int PEEKED_NULL = 7;
    private static final int PEEKED_NUMBER = 17;
    private static final int PEEKED_SINGLE_QUOTED = 8;
    private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
    private static final int PEEKED_TRUE = 5;
    private static final int PEEKED_UNQUOTED = 10;
    private static final int PEEKED_UNQUOTED_NAME = 14;
    private final Buffer buffer;
    private int peeked = 0;
    private long peekedLong;
    private int peekedNumberLength;
    @Nullable
    private String peekedString;
    private final BufferedSource source;

    O00000o(BufferedSource bufferedSource) {
        if (bufferedSource != null) {
            this.source = bufferedSource;
            this.buffer = bufferedSource.getBuffer();
            O0000o0(6);
            return;
        }
        throw new NullPointerException("source == null");
    }

    private int O000000o(String str, O000000o o000000o) {
        int length = o000000o.strings.length;
        for (int i = 0; i < length; i++) {
            if (str.equals(o000000o.strings[i])) {
                this.peeked = 0;
                this.O0O0oo0[this.stackSize - 1] = str;
                return i;
            }
        }
        return -1;
    }

    private String O000000o(ByteString byteString) {
        StringBuilder sb = null;
        while (true) {
            long indexOfElement = this.source.indexOfElement(byteString);
            if (indexOfElement == -1) {
                throw syntaxError("Unterminated string");
            } else if (this.buffer.getByte(indexOfElement) == 92) {
                if (sb == null) {
                    sb = new StringBuilder();
                }
                sb.append(this.buffer.readUtf8(indexOfElement));
                this.buffer.readByte();
                sb.append(readEscapeCharacter());
            } else {
                String readUtf8 = this.buffer.readUtf8(indexOfElement);
                if (sb == null) {
                    this.buffer.readByte();
                    return readUtf8;
                }
                sb.append(readUtf8);
                this.buffer.readByte();
                return sb.toString();
            }
        }
    }

    private int O00000Oo(String str, O000000o o000000o) {
        int length = o000000o.strings.length;
        for (int i = 0; i < length; i++) {
            if (str.equals(o000000o.strings[i])) {
                this.peeked = 0;
                int[] iArr = this.O0O0oo;
                int i2 = this.stackSize - 1;
                iArr[i2] = iArr[i2] + 1;
                return i;
            }
        }
        return -1;
    }

    private void O00000Oo(ByteString byteString) {
        while (true) {
            long indexOfElement = this.source.indexOfElement(byteString);
            if (indexOfElement == -1) {
                throw syntaxError("Unterminated string");
            } else if (this.buffer.getByte(indexOfElement) == 92) {
                this.buffer.skip(indexOfElement + 1);
                readEscapeCharacter();
            } else {
                this.buffer.skip(indexOfElement + 1);
                return;
            }
        }
    }

    private boolean O000Oo00(int i) {
        if (!(i == 9 || i == 10 || i == 12 || i == 13 || i == 32)) {
            if (i != 35) {
                if (i != 44) {
                    if (!(i == 47 || i == 61)) {
                        if (!(i == 123 || i == 125 || i == 58)) {
                            if (i != 59) {
                                switch (i) {
                                    case 91:
                                    case 93:
                                        break;
                                    case 92:
                                        break;
                                    default:
                                        return true;
                                }
                            }
                        }
                    }
                }
            }
            checkLenient();
        }
        return false;
    }

    private boolean Oo0oooo() {
        long indexOf = this.source.indexOf(O0OO00o);
        boolean z = indexOf != -1;
        Buffer buffer2 = this.buffer;
        buffer2.skip(z ? indexOf + ((long) O0OO00o.size()) : buffer2.size());
        return z;
    }

    private void checkLenient() {
        if (!this.lenient) {
            throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a0  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0110  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int doPeek() {
        int nextNonWhitespace;
        int i;
        int[] iArr = this.O0O0oOo;
        int i2 = this.stackSize;
        int i3 = iArr[i2 - 1];
        if (i3 == 1) {
            iArr[i2 - 1] = 2;
            nextNonWhitespace = nextNonWhitespace(true);
            if (nextNonWhitespace != 34) {
                this.buffer.readByte();
                i = 9;
            } else if (nextNonWhitespace != 39) {
                if (!(nextNonWhitespace == 44 || nextNonWhitespace == 59)) {
                    if (nextNonWhitespace == 91) {
                        this.buffer.readByte();
                        this.peeked = 3;
                        return 3;
                    } else if (nextNonWhitespace != 93) {
                        if (nextNonWhitespace != 123) {
                            int peekKeyword = peekKeyword();
                            if (peekKeyword != 0) {
                                return peekKeyword;
                            }
                            int peekNumber = peekNumber();
                            if (peekNumber != 0) {
                                return peekNumber;
                            }
                            if (O000Oo00(this.buffer.getByte(0))) {
                                checkLenient();
                                i = 10;
                            } else {
                                throw syntaxError("Expected value");
                            }
                        } else {
                            this.buffer.readByte();
                            this.peeked = 1;
                            return 1;
                        }
                    } else if (i3 == 1) {
                        this.buffer.readByte();
                        i = 4;
                    }
                }
                if (i3 == 1 || i3 == 2) {
                    checkLenient();
                    this.peeked = 7;
                    return 7;
                }
                throw syntaxError("Unexpected value");
            } else {
                checkLenient();
                this.buffer.readByte();
                this.peeked = 8;
                return 8;
            }
        } else {
            if (i3 == 2) {
                int nextNonWhitespace2 = nextNonWhitespace(true);
                this.buffer.readByte();
                if (nextNonWhitespace2 != 44) {
                    if (nextNonWhitespace2 != 59) {
                        if (nextNonWhitespace2 == 93) {
                            this.peeked = 4;
                            return 4;
                        }
                        throw syntaxError("Unterminated array");
                    }
                }
                nextNonWhitespace = nextNonWhitespace(true);
                if (nextNonWhitespace != 34) {
                }
            } else if (i3 == 3 || i3 == 5) {
                this.O0O0oOo[this.stackSize - 1] = 4;
                if (i3 == 5) {
                    int nextNonWhitespace3 = nextNonWhitespace(true);
                    this.buffer.readByte();
                    if (nextNonWhitespace3 != 44) {
                        if (nextNonWhitespace3 != 59) {
                            if (nextNonWhitespace3 != 125) {
                                throw syntaxError("Unterminated object");
                            }
                            i = 2;
                        } else {
                            checkLenient();
                        }
                    }
                }
                int nextNonWhitespace4 = nextNonWhitespace(true);
                if (nextNonWhitespace4 == 34) {
                    this.buffer.readByte();
                    i = 13;
                } else if (nextNonWhitespace4 != 39) {
                    String str = "Expected name";
                    if (nextNonWhitespace4 != 125) {
                        checkLenient();
                        if (O000Oo00((char) nextNonWhitespace4)) {
                            i = 14;
                        } else {
                            throw syntaxError(str);
                        }
                    } else if (i3 != 5) {
                        this.buffer.readByte();
                        i = 2;
                    } else {
                        throw syntaxError(str);
                    }
                } else {
                    this.buffer.readByte();
                    checkLenient();
                    i = 12;
                }
            } else {
                if (i3 == 4) {
                    iArr[i2 - 1] = 5;
                    int nextNonWhitespace5 = nextNonWhitespace(true);
                    this.buffer.readByte();
                    if (nextNonWhitespace5 != 58) {
                        if (nextNonWhitespace5 == 61) {
                            checkLenient();
                            if (this.source.request(1) && this.buffer.getByte(0) == 62) {
                                this.buffer.readByte();
                            }
                        } else {
                            throw syntaxError("Expected ':'");
                        }
                    }
                } else if (i3 == 6) {
                    iArr[i2 - 1] = 7;
                } else if (i3 == 7) {
                    if (nextNonWhitespace(false) == -1) {
                        i = 18;
                    }
                } else if (i3 == 8) {
                    throw new IllegalStateException("JsonReader is closed");
                }
                nextNonWhitespace = nextNonWhitespace(true);
                if (nextNonWhitespace != 34) {
                }
            }
            checkLenient();
            nextNonWhitespace = nextNonWhitespace(true);
            if (nextNonWhitespace != 34) {
            }
        }
        this.peeked = i;
        return i;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0025, code lost:
        r6.buffer.skip((long) (r3 - 1));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002f, code lost:
        if (r1 != 47) goto L_0x0074;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0039, code lost:
        if (r6.source.request(2) != false) goto L_0x003c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003b, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003c, code lost:
        checkLenient();
        r3 = r6.buffer.getByte(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0049, code lost:
        if (r3 == 42) goto L_0x005c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004b, code lost:
        if (r3 == 47) goto L_0x004e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x004d, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x004e, code lost:
        r6.buffer.readByte();
        r6.buffer.readByte();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0058, code lost:
        skipToEndOfLine();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x005c, code lost:
        r6.buffer.readByte();
        r6.buffer.readByte();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x006a, code lost:
        if (Oo0oooo() == false) goto L_0x006d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0073, code lost:
        throw syntaxError("Unterminated comment");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0076, code lost:
        if (r1 != 35) goto L_0x007c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0078, code lost:
        checkLenient();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x007c, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int nextNonWhitespace(boolean z) {
        while (true) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                if (this.source.request((long) i2)) {
                    byte b = this.buffer.getByte((long) i);
                    if (b != 10 && b != 32 && b != 13 && b != 9) {
                        break;
                    }
                    i = i2;
                } else if (!z) {
                    return -1;
                } else {
                    throw new EOFException("End of input");
                }
            }
        }
    }

    private String nextUnquotedValue() {
        long indexOfElement = this.source.indexOfElement(O0OoOO);
        int i = (indexOfElement > -1 ? 1 : (indexOfElement == -1 ? 0 : -1));
        Buffer buffer2 = this.buffer;
        return i != 0 ? buffer2.readUtf8(indexOfElement) : buffer2.readUtf8();
    }

    private int peekKeyword() {
        String str;
        String str2;
        int i;
        byte b = this.buffer.getByte(0);
        if (b == 116 || b == 84) {
            i = 5;
            str2 = BaseEvent.VALUE_TRUE;
            str = "TRUE";
        } else if (b == 102 || b == 70) {
            i = 6;
            str2 = BaseEvent.VALUE_FALSE;
            str = "FALSE";
        } else if (b != 110 && b != 78) {
            return 0;
        } else {
            i = 7;
            str2 = "null";
            str = "NULL";
        }
        int length = str2.length();
        int i2 = 1;
        while (i2 < length) {
            int i3 = i2 + 1;
            if (!this.source.request((long) i3)) {
                return 0;
            }
            byte b2 = this.buffer.getByte((long) i2);
            if (b2 != str2.charAt(i2) && b2 != str.charAt(i2)) {
                return 0;
            }
            i2 = i3;
        }
        if (this.source.request((long) (length + 1)) && O000Oo00(this.buffer.getByte((long) length))) {
            return 0;
        }
        this.buffer.skip((long) length);
        this.peeked = i;
        return i;
    }

    /* JADX WARNING: type inference failed for: r6v2 */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r6v3 */
    /* JADX WARNING: type inference failed for: r3v6 */
    /* JADX WARNING: type inference failed for: r6v4 */
    /* JADX WARNING: type inference failed for: r3v8 */
    /* JADX WARNING: type inference failed for: r6v5 */
    /* JADX WARNING: type inference failed for: r3v9 */
    /* JADX WARNING: type inference failed for: r6v6 */
    /* JADX WARNING: type inference failed for: r6v7 */
    /* JADX WARNING: type inference failed for: r6v8 */
    /* JADX WARNING: type inference failed for: r6v9 */
    /* JADX WARNING: type inference failed for: r6v10 */
    /* JADX WARNING: type inference failed for: r6v11 */
    /* JADX WARNING: type inference failed for: r6v12 */
    /* JADX WARNING: type inference failed for: r6v13 */
    /* JADX WARNING: type inference failed for: r6v14 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r6v2
  assigns: []
  uses: []
  mth insns count: 106
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 6 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int peekNumber() {
        int i;
        long j;
        byte b;
        ? r6;
        ? r3;
        ? r62;
        boolean z = true;
        int i2 = 0;
        long j2 = 0;
        boolean z2 = true;
        int i3 = 0;
        ? r63 = false;
        boolean z3 = false;
        while (true) {
            int i4 = i3 + 1;
            if (!this.source.request((long) i4)) {
                break;
            }
            b = this.buffer.getByte((long) i3);
            if (b == 43) {
                r3 = 6;
                if (!r63) {
                    return i2;
                }
            } else if (b == 69 || b == 101) {
                if (!r63 && !r63) {
                    return i2;
                }
                r6 = 5;
                i3 = i4;
                z = true;
                r63 = r6;
            } else if (b == 45) {
                r3 = 6;
                if (!r63) {
                    z3 = true;
                    r6 = 1;
                    i3 = i4;
                    z = true;
                    r63 = r6;
                } else if (!r63) {
                    return i2;
                }
            } else if (b == 46) {
                r3 = 3;
                if (!r63) {
                    return i2;
                }
            } else if (b >= 48 && b <= 57) {
                if (r63 == z || !r63) {
                    j2 = (long) (-(b - 48));
                    r63 = 2;
                } else if (!r63) {
                    if (r63) {
                        i2 = 0;
                        r6 = 4;
                    } else if (r63 || r63) {
                        i2 = 0;
                        r6 = 7;
                    }
                    i3 = i4;
                    z = true;
                    r63 = r6;
                } else if (j2 == 0) {
                    return i2;
                } else {
                    long j3 = (10 * j2) - ((long) (b - 48));
                    int i5 = (j2 > MIN_INCOMPLETE_INTEGER ? 1 : (j2 == MIN_INCOMPLETE_INTEGER ? 0 : -1));
                    boolean z4 = i5 > 0 || (i5 == 0 && j3 < j2);
                    z2 = z4 & z2;
                    j2 = j3;
                }
                i2 = 0;
                r6 = r62;
                i3 = i4;
                z = true;
                r63 = r6;
            }
            r6 = r3;
            i3 = i4;
            z = true;
            r63 = r6;
        }
        if (O000Oo00(b)) {
            return 0;
        }
        if (r63 && z2 && ((j2 != Long.MIN_VALUE || z3) && (j2 != 0 || !z3))) {
            if (!z3) {
                j = -j2;
            }
            this.peekedLong = j;
            this.buffer.skip((long) i3);
            i = 16;
        } else if (!r63 && !r63 && !r63) {
            return 0;
        } else {
            this.peekedNumberLength = i3;
            i = 17;
        }
        this.peeked = i;
        return i;
    }

    private char readEscapeCharacter() {
        int i;
        int i2;
        if (this.source.request(1)) {
            byte readByte = this.buffer.readByte();
            if (readByte == 10 || readByte == 34 || readByte == 39 || readByte == 47 || readByte == 92) {
                return (char) readByte;
            }
            if (readByte == 98) {
                return 8;
            }
            if (readByte == 102) {
                return 12;
            }
            if (readByte == 110) {
                return 10;
            }
            if (readByte == 114) {
                return 13;
            }
            if (readByte == 116) {
                return 9;
            }
            if (readByte != 117) {
                if (this.lenient) {
                    return (char) readByte;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid escape sequence: \\");
                sb.append((char) readByte);
                throw syntaxError(sb.toString());
            } else if (this.source.request(4)) {
                char c = 0;
                for (int i3 = 0; i3 < 4; i3++) {
                    byte b = this.buffer.getByte((long) i3);
                    char c2 = (char) (c << 4);
                    if (b < 48 || b > 57) {
                        if (b >= 97 && b <= 102) {
                            i = b - 97;
                        } else if (b < 65 || b > 70) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("\\u");
                            sb2.append(this.buffer.readUtf8(4));
                            throw syntaxError(sb2.toString());
                        } else {
                            i = b - 65;
                        }
                        i2 = i + 10;
                    } else {
                        i2 = b - 48;
                    }
                    c = (char) (c2 + i2);
                }
                this.buffer.skip(4);
                return c;
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Unterminated escape sequence at path ");
                sb3.append(getPath());
                throw new EOFException(sb3.toString());
            }
        } else {
            throw syntaxError("Unterminated escape sequence");
        }
    }

    private void skipToEndOfLine() {
        long indexOfElement = this.source.indexOfElement(O0OO00O);
        Buffer buffer2 = this.buffer;
        buffer2.skip(indexOfElement != -1 ? indexOfElement + 1 : buffer2.size());
    }

    private void skipUnquotedValue() {
        long indexOfElement = this.source.indexOfElement(O0OoOO);
        Buffer buffer2 = this.buffer;
        if (indexOfElement == -1) {
            indexOfElement = buffer2.size();
        }
        buffer2.skip(indexOfElement);
    }

    public int O000000o(O000000o o000000o) {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i < 12 || i > 15) {
            return -1;
        }
        if (i == 15) {
            return O000000o(this.peekedString, o000000o);
        }
        int select = this.source.select(o000000o.O0O0oOO);
        if (select != -1) {
            this.peeked = 0;
            this.O0O0oo0[this.stackSize - 1] = o000000o.strings[select];
            return select;
        }
        String str = this.O0O0oo0[this.stackSize - 1];
        String nextName = nextName();
        int O000000o2 = O000000o(nextName, o000000o);
        if (O000000o2 == -1) {
            this.peeked = 15;
            this.peekedString = nextName;
            this.O0O0oo0[this.stackSize - 1] = str;
        }
        return O000000o2;
    }

    public void O00o0O0() {
        ByteString byteString;
        if (!this.O0O0ooO) {
            int i = this.peeked;
            if (i == 0) {
                i = doPeek();
            }
            if (i == 14) {
                skipUnquotedValue();
            } else {
                if (i == 13) {
                    byteString = O0OoOo;
                } else if (i == 12) {
                    byteString = O0O0ooo;
                } else if (i != 15) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Expected a name but was ");
                    sb.append(peek());
                    sb.append(" at path ");
                    sb.append(getPath());
                    throw new JsonDataException(sb.toString());
                }
                O00000Oo(byteString);
            }
            this.peeked = 0;
            this.O0O0oo0[this.stackSize - 1] = "null";
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Cannot skip unexpected ");
        sb2.append(peek());
        sb2.append(" at ");
        sb2.append(getPath());
        throw new JsonDataException(sb2.toString());
    }

    public void beginArray() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 3) {
            O0000o0(1);
            this.O0O0oo[this.stackSize - 1] = 0;
            this.peeked = 0;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Expected BEGIN_ARRAY but was ");
        sb.append(peek());
        sb.append(" at path ");
        sb.append(getPath());
        throw new JsonDataException(sb.toString());
    }

    public void beginObject() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 1) {
            O0000o0(3);
            this.peeked = 0;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Expected BEGIN_OBJECT but was ");
        sb.append(peek());
        sb.append(" at path ");
        sb.append(getPath());
        throw new JsonDataException(sb.toString());
    }

    public void close() {
        this.peeked = 0;
        this.O0O0oOo[0] = 8;
        this.stackSize = 1;
        this.buffer.clear();
        this.source.close();
    }

    public void endArray() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 4) {
            this.stackSize--;
            int[] iArr = this.O0O0oo;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
            this.peeked = 0;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Expected END_ARRAY but was ");
        sb.append(peek());
        sb.append(" at path ");
        sb.append(getPath());
        throw new JsonDataException(sb.toString());
    }

    public void endObject() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 2) {
            this.stackSize--;
            String[] strArr = this.O0O0oo0;
            int i2 = this.stackSize;
            strArr[i2] = null;
            int[] iArr = this.O0O0oo;
            int i3 = i2 - 1;
            iArr[i3] = iArr[i3] + 1;
            this.peeked = 0;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Expected END_OBJECT but was ");
        sb.append(peek());
        sb.append(" at path ");
        sb.append(getPath());
        throw new JsonDataException(sb.toString());
    }

    public boolean hasNext() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        return (i == 2 || i == 4 || i == 18) ? false : true;
    }

    public boolean nextBoolean() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 5) {
            this.peeked = 0;
            int[] iArr = this.O0O0oo;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
            return true;
        } else if (i == 6) {
            this.peeked = 0;
            int[] iArr2 = this.O0O0oo;
            int i3 = this.stackSize - 1;
            iArr2[i3] = iArr2[i3] + 1;
            return false;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Expected a boolean but was ");
            sb.append(peek());
            sb.append(" at path ");
            sb.append(getPath());
            throw new JsonDataException(sb.toString());
        }
    }

    public double nextDouble() {
        double parseDouble;
        String nextUnquotedValue;
        ByteString byteString;
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 16) {
            this.peeked = 0;
            int[] iArr = this.O0O0oo;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
            return (double) this.peekedLong;
        }
        String str = "Expected a double but was ";
        String str2 = " at path ";
        if (i == 17) {
            nextUnquotedValue = this.buffer.readUtf8((long) this.peekedNumberLength);
        } else {
            if (i == 9) {
                byteString = O0OoOo;
            } else if (i == 8) {
                byteString = O0O0ooo;
            } else if (i == 10) {
                nextUnquotedValue = nextUnquotedValue();
            } else {
                if (i != 11) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(peek());
                    sb.append(str2);
                    sb.append(getPath());
                    throw new JsonDataException(sb.toString());
                }
                this.peeked = 11;
                parseDouble = Double.parseDouble(this.peekedString);
                if (!this.lenient || (!Double.isNaN(parseDouble) && !Double.isInfinite(parseDouble))) {
                    this.peekedString = null;
                    this.peeked = 0;
                    int[] iArr2 = this.O0O0oo;
                    int i3 = this.stackSize - 1;
                    iArr2[i3] = iArr2[i3] + 1;
                    return parseDouble;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("JSON forbids NaN and infinities: ");
                sb2.append(parseDouble);
                sb2.append(str2);
                sb2.append(getPath());
                throw new JsonEncodingException(sb2.toString());
            }
            nextUnquotedValue = O000000o(byteString);
        }
        this.peekedString = nextUnquotedValue;
        this.peeked = 11;
        try {
            parseDouble = Double.parseDouble(this.peekedString);
            if (!this.lenient) {
            }
            this.peekedString = null;
            this.peeked = 0;
            int[] iArr22 = this.O0O0oo;
            int i32 = this.stackSize - 1;
            iArr22[i32] = iArr22[i32] + 1;
            return parseDouble;
        } catch (NumberFormatException unused) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str);
            sb3.append(this.peekedString);
            sb3.append(str2);
            sb3.append(getPath());
            throw new JsonDataException(sb3.toString());
        }
    }

    public int nextInt() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        String str = " at path ";
        String str2 = "Expected an int but was ";
        if (i == 16) {
            long j = this.peekedLong;
            int i2 = (int) j;
            if (j == ((long) i2)) {
                this.peeked = 0;
                int[] iArr = this.O0O0oo;
                int i3 = this.stackSize - 1;
                iArr[i3] = iArr[i3] + 1;
                return i2;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(this.peekedLong);
            sb.append(str);
            sb.append(getPath());
            throw new JsonDataException(sb.toString());
        }
        if (i == 17) {
            this.peekedString = this.buffer.readUtf8((long) this.peekedNumberLength);
        } else if (i == 9 || i == 8) {
            this.peekedString = O000000o(i == 9 ? O0OoOo : O0O0ooo);
            try {
                int parseInt = Integer.parseInt(this.peekedString);
                this.peeked = 0;
                int[] iArr2 = this.O0O0oo;
                int i4 = this.stackSize - 1;
                iArr2[i4] = iArr2[i4] + 1;
                return parseInt;
            } catch (NumberFormatException unused) {
            }
        } else if (i != 11) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(peek());
            sb2.append(str);
            sb2.append(getPath());
            throw new JsonDataException(sb2.toString());
        }
        this.peeked = 11;
        try {
            double parseDouble = Double.parseDouble(this.peekedString);
            int i5 = (int) parseDouble;
            if (((double) i5) == parseDouble) {
                this.peekedString = null;
                this.peeked = 0;
                int[] iArr3 = this.O0O0oo;
                int i6 = this.stackSize - 1;
                iArr3[i6] = iArr3[i6] + 1;
                return i5;
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str2);
            sb3.append(this.peekedString);
            sb3.append(str);
            sb3.append(getPath());
            throw new JsonDataException(sb3.toString());
        } catch (NumberFormatException unused2) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str2);
            sb4.append(this.peekedString);
            sb4.append(str);
            sb4.append(getPath());
            throw new JsonDataException(sb4.toString());
        }
    }

    public String nextName() {
        String str;
        ByteString byteString;
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 14) {
            str = nextUnquotedValue();
        } else {
            if (i == 13) {
                byteString = O0OoOo;
            } else if (i == 12) {
                byteString = O0O0ooo;
            } else if (i == 15) {
                str = this.peekedString;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Expected a name but was ");
                sb.append(peek());
                sb.append(" at path ");
                sb.append(getPath());
                throw new JsonDataException(sb.toString());
            }
            str = O000000o(byteString);
        }
        this.peeked = 0;
        this.O0O0oo0[this.stackSize - 1] = str;
        return str;
    }

    public String nextString() {
        String str;
        ByteString byteString;
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 10) {
            str = nextUnquotedValue();
        } else {
            if (i == 9) {
                byteString = O0OoOo;
            } else if (i == 8) {
                byteString = O0O0ooo;
            } else if (i == 11) {
                str = this.peekedString;
                this.peekedString = null;
            } else if (i == 16) {
                str = Long.toString(this.peekedLong);
            } else if (i == 17) {
                str = this.buffer.readUtf8((long) this.peekedNumberLength);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Expected a string but was ");
                sb.append(peek());
                sb.append(" at path ");
                sb.append(getPath());
                throw new JsonDataException(sb.toString());
            }
            str = O000000o(byteString);
        }
        this.peeked = 0;
        int[] iArr = this.O0O0oo;
        int i2 = this.stackSize - 1;
        iArr[i2] = iArr[i2] + 1;
        return str;
    }

    public JsonReader$Token peek() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        switch (i) {
            case 1:
                return JsonReader$Token.BEGIN_OBJECT;
            case 2:
                return JsonReader$Token.END_OBJECT;
            case 3:
                return JsonReader$Token.BEGIN_ARRAY;
            case 4:
                return JsonReader$Token.END_ARRAY;
            case 5:
            case 6:
                return JsonReader$Token.BOOLEAN;
            case 7:
                return JsonReader$Token.NULL;
            case 8:
            case 9:
            case 10:
            case 11:
                return JsonReader$Token.STRING;
            case 12:
            case 13:
            case 14:
            case 15:
                return JsonReader$Token.NAME;
            case 16:
            case 17:
                return JsonReader$Token.NUMBER;
            case 18:
                return JsonReader$Token.END_DOCUMENT;
            default:
                throw new AssertionError();
        }
    }

    public void skipValue() {
        ByteString byteString;
        if (!this.O0O0ooO) {
            int i = 0;
            do {
                int i2 = this.peeked;
                if (i2 == 0) {
                    i2 = doPeek();
                }
                if (i2 == 3) {
                    O0000o0(1);
                } else if (i2 == 1) {
                    O0000o0(3);
                } else {
                    String str = " at path ";
                    String str2 = "Expected a value but was ";
                    if (i2 == 4) {
                        i--;
                        if (i < 0) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(str2);
                            sb.append(peek());
                            sb.append(str);
                            sb.append(getPath());
                            throw new JsonDataException(sb.toString());
                        }
                    } else if (i2 == 2) {
                        i--;
                        if (i < 0) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(str2);
                            sb2.append(peek());
                            sb2.append(str);
                            sb2.append(getPath());
                            throw new JsonDataException(sb2.toString());
                        }
                    } else if (i2 == 14 || i2 == 10) {
                        skipUnquotedValue();
                        this.peeked = 0;
                    } else {
                        if (i2 == 9 || i2 == 13) {
                            byteString = O0OoOo;
                        } else if (i2 == 8 || i2 == 12) {
                            byteString = O0O0ooo;
                        } else {
                            if (i2 == 17) {
                                this.buffer.skip((long) this.peekedNumberLength);
                            } else if (i2 == 18) {
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append(str2);
                                sb3.append(peek());
                                sb3.append(str);
                                sb3.append(getPath());
                                throw new JsonDataException(sb3.toString());
                            }
                            this.peeked = 0;
                        }
                        O00000Oo(byteString);
                        this.peeked = 0;
                    }
                    this.stackSize--;
                    this.peeked = 0;
                }
                i++;
                this.peeked = 0;
            } while (i != 0);
            int[] iArr = this.O0O0oo;
            int i3 = this.stackSize;
            int i4 = i3 - 1;
            iArr[i4] = iArr[i4] + 1;
            this.O0O0oo0[i3 - 1] = "null";
            return;
        }
        StringBuilder sb4 = new StringBuilder();
        sb4.append("Cannot skip unexpected ");
        sb4.append(peek());
        sb4.append(" at ");
        sb4.append(getPath());
        throw new JsonDataException(sb4.toString());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("JsonReader(");
        sb.append(this.source);
        sb.append(")");
        return sb.toString();
    }
}
