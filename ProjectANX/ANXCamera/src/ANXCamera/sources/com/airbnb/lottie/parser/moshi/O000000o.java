package com.airbnb.lottie.parser.moshi;

import java.io.IOException;
import okio.Buffer;
import okio.ByteString;
import okio.Options;

public final class O000000o {
    final Options O0O0oOO;
    final String[] strings;

    private O000000o(String[] strArr, Options options) {
        this.strings = strArr;
        this.O0O0oOO = options;
    }

    public static O000000o of(String... strArr) {
        try {
            ByteString[] byteStringArr = new ByteString[strArr.length];
            Buffer buffer = new Buffer();
            for (int i = 0; i < strArr.length; i++) {
                O00000Oo.O00000Oo(buffer, strArr[i]);
                buffer.readByte();
                byteStringArr[i] = buffer.readByteString();
            }
            return new O000000o((String[]) strArr.clone(), Options.of(byteStringArr));
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
