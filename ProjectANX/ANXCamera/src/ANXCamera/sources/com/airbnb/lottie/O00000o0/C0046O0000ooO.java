package com.airbnb.lottie.O00000o0;

import com.airbnb.lottie.model.content.MergePaths$MergePathsMode;
import com.airbnb.lottie.model.content.O0000O0o;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.android.camera.data.data.config.SupportedConfigFactory;

/* renamed from: com.airbnb.lottie.O00000o0.O0000ooO reason: case insensitive filesystem */
class C0046O0000ooO {
    private static final O000000o NAMES = O000000o.of("nm", SupportedConfigFactory.CLOSE_BY_MANUAL_MODE, "hd");

    private C0046O0000ooO() {
    }

    static O0000O0o O00000Oo(O00000Oo o00000Oo) {
        String str = null;
        boolean z = false;
        MergePaths$MergePathsMode mergePaths$MergePathsMode = null;
        while (o00000Oo.hasNext()) {
            int O000000o2 = o00000Oo.O000000o(NAMES);
            if (O000000o2 == 0) {
                str = o00000Oo.nextString();
            } else if (O000000o2 == 1) {
                mergePaths$MergePathsMode = MergePaths$MergePathsMode.O000OOo0(o00000Oo.nextInt());
            } else if (O000000o2 != 2) {
                o00000Oo.O00o0O0();
                o00000Oo.skipValue();
            } else {
                z = o00000Oo.nextBoolean();
            }
        }
        return new O0000O0o(str, mergePaths$MergePathsMode, z);
    }
}
