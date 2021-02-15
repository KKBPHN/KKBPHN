package com.airbnb.lottie.O00000o0;

import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.model.O000000o.O00000o;
import com.airbnb.lottie.model.O000000o.O0000OOo;
import com.airbnb.lottie.model.content.C0105O00000oo;
import com.airbnb.lottie.model.content.Mask$MaskMode;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.android.camera.data.data.config.SupportedConfigFactory;

/* renamed from: com.airbnb.lottie.O00000o0.O0000oo reason: case insensitive filesystem */
class C0044O0000oo {
    private C0044O0000oo() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0058  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0071  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static C0105O00000oo O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        boolean z;
        o00000Oo.beginObject();
        Mask$MaskMode mask$MaskMode = null;
        boolean z2 = false;
        O0000OOo o0000OOo = null;
        O00000o o00000o = null;
        while (o00000Oo.hasNext()) {
            String nextName = o00000Oo.nextName();
            int hashCode = nextName.hashCode();
            char c = 65535;
            if (hashCode != 111) {
                if (hashCode != 3588) {
                    if (hashCode != 104433) {
                        if (hashCode == 3357091 && nextName.equals("mode")) {
                            z = false;
                            if (!z) {
                                String nextString = o00000Oo.nextString();
                                int hashCode2 = nextString.hashCode();
                                if (hashCode2 != 97) {
                                    if (hashCode2 != 105) {
                                        if (hashCode2 != 110) {
                                            if (hashCode2 == 115 && nextString.equals("s")) {
                                                c = 1;
                                            }
                                        } else if (nextString.equals("n")) {
                                            c = 2;
                                        }
                                    } else if (nextString.equals(SupportedConfigFactory.CLOSE_BY_ULTRA_WIDE)) {
                                        c = 3;
                                    }
                                } else if (nextString.equals(SupportedConfigFactory.CLOSE_BY_HHT)) {
                                    c = 0;
                                }
                                if (c != 0) {
                                    if (c == 1) {
                                        mask$MaskMode = Mask$MaskMode.MASK_MODE_SUBTRACT;
                                    } else if (c == 2) {
                                        mask$MaskMode = Mask$MaskMode.MASK_MODE_NONE;
                                    } else if (c != 3) {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("Unknown mask mode ");
                                        sb.append(nextName);
                                        sb.append(". Defaulting to Add.");
                                        com.airbnb.lottie.O00000o.O00000o.warning(sb.toString());
                                    } else {
                                        o0000o0O.O0000oo("Animation contains intersect masks. They are not supported but will be treated like add masks.");
                                        mask$MaskMode = Mask$MaskMode.MASK_MODE_INTERSECT;
                                    }
                                }
                                mask$MaskMode = Mask$MaskMode.MASK_MODE_ADD;
                            } else if (z) {
                                o0000OOo = O00000o.O0000Oo0(o00000Oo, o0000o0O);
                            } else if (z) {
                                o00000o = O00000o.O00000oo(o00000Oo, o0000o0O);
                            } else if (!z) {
                                o00000Oo.skipValue();
                            } else {
                                z2 = o00000Oo.nextBoolean();
                            }
                        }
                    } else if (nextName.equals("inv")) {
                        z = true;
                        if (!z) {
                        }
                    }
                } else if (nextName.equals("pt")) {
                    z = true;
                    if (!z) {
                    }
                }
            } else if (nextName.equals(SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT)) {
                z = true;
                if (!z) {
                }
            }
            z = true;
            if (!z) {
            }
        }
        o00000Oo.endObject();
        return new C0105O00000oo(mask$MaskMode, o0000OOo, o00000o, z2);
    }
}
