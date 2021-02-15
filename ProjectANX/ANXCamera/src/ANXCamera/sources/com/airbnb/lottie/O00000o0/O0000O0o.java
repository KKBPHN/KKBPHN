package com.airbnb.lottie.O00000o0;

import androidx.annotation.Nullable;
import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.O00000o.O00000o;
import com.airbnb.lottie.model.content.O00000Oo;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.android.camera.data.data.config.SupportedConfigFactory;
import com.arcsoft.supernight.SuperNightProcess;
import com.xiaomi.stat.d;

class O0000O0o {
    private static O000000o NAMES = O000000o.of("ty", SupportedConfigFactory.CLOSE_BY_BURST_SHOOT);

    private O0000O0o() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0094, code lost:
        if (r2.equals("gs") != false) goto L_0x00c0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @Nullable
    static O00000Oo O000000o(com.airbnb.lottie.parser.moshi.O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        O00000Oo o00000Oo2;
        String str;
        o00000Oo.beginObject();
        char c = 2;
        int i = 2;
        while (true) {
            o00000Oo2 = null;
            if (!o00000Oo.hasNext()) {
                str = null;
                break;
            }
            int O000000o2 = o00000Oo.O000000o(NAMES);
            if (O000000o2 == 0) {
                str = o00000Oo.nextString();
                break;
            } else if (O000000o2 != 1) {
                o00000Oo.O00o0O0();
                o00000Oo.skipValue();
            } else {
                i = o00000Oo.nextInt();
            }
        }
        if (str == null) {
            return null;
        }
        switch (str.hashCode()) {
            case 3239:
                if (str.equals("el")) {
                    c = 7;
                    break;
                }
            case 3270:
                if (str.equals("fl")) {
                    c = 3;
                    break;
                }
            case 3295:
                if (str.equals("gf")) {
                    c = 4;
                    break;
                }
            case 3307:
                if (str.equals("gr")) {
                    c = 0;
                    break;
                }
            case 3308:
                break;
            case 3488:
                if (str.equals(SupportedConfigFactory.CLOSE_BY_MANUAL_MODE)) {
                    c = 11;
                    break;
                }
            case SuperNightProcess.ASVL_PAF_RAW16_GRAY_16B /*3633*/:
                if (str.equals("rc")) {
                    c = 8;
                    break;
                }
            case 3646:
                if (str.equals("rp")) {
                    c = 12;
                    break;
                }
            case 3669:
                if (str.equals("sh")) {
                    c = 6;
                    break;
                }
            case 3679:
                if (str.equals(d.Y)) {
                    c = 10;
                    break;
                }
            case 3681:
                if (str.equals(d.n)) {
                    c = 1;
                    break;
                }
            case 3705:
                if (str.equals("tm")) {
                    c = 9;
                    break;
                }
            case 3710:
                if (str.equals("tr")) {
                    c = 5;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                o00000Oo2 = C0049O000O0oO.O000000o(o00000Oo, o0000o0O);
                break;
            case 1:
                o00000Oo2 = O000OO00.O000000o(o00000Oo, o0000o0O);
                break;
            case 2:
                o00000Oo2 = O0000o0.O000000o(o00000Oo, o0000o0O);
                break;
            case 3:
                o00000Oo2 = O000O0o.O000000o(o00000Oo, o0000o0O);
                break;
            case 4:
                o00000Oo2 = O0000o00.O000000o(o00000Oo, o0000o0O);
                break;
            case 5:
                o00000Oo2 = O00000o0.O000000o(o00000Oo, o0000o0O);
                break;
            case 6:
                o00000Oo2 = C0050O000O0oo.O000000o(o00000Oo, o0000o0O);
                break;
            case 7:
                o00000Oo2 = C0034O00000oO.O00000Oo(o00000Oo, o0000o0O, i);
                break;
            case 8:
                o00000Oo2 = O000O0OO.O000000o(o00000Oo, o0000o0O);
                break;
            case 9:
                o00000Oo2 = O000OO0o.O000000o(o00000Oo, o0000o0O);
                break;
            case 10:
                o00000Oo2 = O000O00o.O000000o(o00000Oo, o0000o0O);
                break;
            case 11:
                o00000Oo2 = C0046O0000ooO.O00000Oo(o00000Oo);
                o0000o0O.O0000oo("Animation contains merge paths. Merge paths are only supported on KitKat+ and must be manually enabled by calling enableMergePathsForKitKatAndAbove().");
                break;
            case 12:
                o00000Oo2 = C0048O000O0Oo.O000000o(o00000Oo, o0000o0O);
                break;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown shape type ");
                sb.append(str);
                O00000o.warning(sb.toString());
                break;
        }
        while (o00000Oo.hasNext()) {
            o00000Oo.skipValue();
        }
        o00000Oo.endObject();
        return o00000Oo2;
    }
}
