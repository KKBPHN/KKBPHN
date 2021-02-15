package O00000Oo.O00000oO.O000000o;

import android.os.Build.VERSION;
import android.os.SystemProperties;
import android.text.TextUtils;
import com.android.camera.Display;
import com.android.camera.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import miui.os.Build;

/* renamed from: O00000Oo.O00000oO.O000000o.O00000oO reason: case insensitive filesystem */
public class C0124O00000oO {
    public static final boolean IS_HONGMI = OOoooo0();
    private static final AtomicReference IS_MTK_PLATFORM = new AtomicReference(Optional.empty());
    public static final boolean IS_XIAOMI = Oo0O0OO();
    public static final String MODULE = Build.MODEL;
    public static final String O0Ooo = "qcom";
    public static final String O0Ooo0o = Build.DEVICE;
    private static final int O0OooO = 100;
    public static final String O0OooO0 = "mediatek";
    public static final boolean O0OooOO = "beryllium".equals(O0Ooo0o);
    public static final boolean O0OooOo = "lavender".equals(O0Ooo0o);
    public static final boolean O0Oooo0 = "violet".equals(O0Ooo0o);
    public static final boolean O0OoooO = "polaris".equals(O0Ooo0o);
    public static final boolean O0Ooooo = "sirius".equals(O0Ooo0o);
    public static final boolean O0o = Build.IS_CM_CUSTOMIZATION;
    public static final boolean O0o0 = "laurel_sprout".equals(O0Ooo0o);
    public static final boolean O0o00 = "raphael".equals(O0Ooo0o);
    public static final boolean O0o000 = "andromeda".equals(O0Ooo0o);
    public static final boolean O0o0000 = "dipper".equals(O0Ooo0o);
    public static final boolean O0o000O = "perseus".equals(O0Ooo0o);
    public static final boolean O0o000o = "cepheus".equals(O0Ooo0o);
    public static final boolean O0o00O = "begonia".equals(O0Ooo0o);
    public static final boolean O0o00O0 = "grus".equals(O0Ooo0o);
    public static final boolean O0o00OO;
    public static final boolean O0o00Oo = "begoniain".equals(O0Ooo0o);
    public static final boolean O0o00o = "pyxis".equals(O0Ooo0o);
    public static final boolean O0o00o0 = "ginkgo".equals(O0Ooo0o);
    public static final boolean O0o00oO = "vela".equals(O0Ooo0o);
    public static final boolean O0o00oo = "laurus".equals(O0Ooo0o);
    public static final boolean O0o0O;
    public static final boolean O0o0O0 = "umi".equals(O0Ooo0o);
    public static final boolean O0o0O00 = "tucana".equals(O0Ooo0o);
    public static final boolean O0o0O0O = "cmi".equals(O0Ooo0o);
    public static final boolean O0o0O0o = "cas".equals(O0Ooo0o);
    public static final boolean O0o0OO;
    public static final boolean O0o0OO0;
    public static final boolean O0o0OOO;
    public static final boolean O0o0OOo = "draco".equals(O0Ooo0o);
    public static final boolean O0o0Oo;
    public static final boolean O0o0Oo0;
    public static final boolean O0o0OoO = "vangogh".equals(O0Ooo0o);
    public static final boolean O0o0Ooo;
    public static final boolean O0o0o0 = "cezanne".equals(O0Ooo0o);
    public static final boolean O0o0o00;
    public static final boolean O0o0o0O = "crux".equals(O0Ooo0o);
    public static final boolean O0o0o0o;
    public static final boolean O0o0oO;
    public static final boolean O0o0oO0 = "dandelion".equals(O0Ooo0o);
    public static final boolean O0o0oOO;
    public static final boolean O0o0oOo;
    public static final boolean O0o0oo;
    public static final boolean O0o0ooO = Build.IS_STABLE_VERSION;
    private static ArrayList O0oO0 = null;
    public static final boolean O0oO00 = "haydn".equals(O0Ooo0o);
    public static final boolean O0oO000 = "venus".equals(O0Ooo0o);
    private static final int O0oO00O = 4;
    private static final int O0oO00o = 8;
    private static final String O0oO0O = "ro.boot.hwversion";
    private static final String[] O0oO0O0 = {"KR", "JP"};
    private static final int O0oO0oO = 1;
    public static final boolean O0oo0o;
    public static final boolean oOOoOO = Build.IS_CM_CUSTOMIZATION_TEST;

    /* JADX WARNING: Code restructure failed: missing block: B:97:0x02e3, code lost:
        if ("citrus".equals(O0Ooo0o) != false) goto L_0x02e5;
     */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0132  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x014c  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0166  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0180  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x01a4  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x01be  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x01eb  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0223  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0265  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0293  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x02ad  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0118  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x02c7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        String str;
        boolean z7;
        boolean z8;
        boolean z9;
        boolean z10;
        boolean z11;
        boolean z12 = false;
        if (!"phoenix".equals(O0Ooo0o)) {
            if (!"phoenixin".equals(O0Ooo0o)) {
                z = false;
                O0o00OO = z;
                if (!"apollo".equals(O0Ooo0o)) {
                    if (!"apolloin".equals(O0Ooo0o)) {
                        z2 = false;
                        O0o0O = z2;
                        if (!"atom".equals(O0Ooo0o)) {
                            if (!"apricot".equals(O0Ooo0o)) {
                                z3 = false;
                                O0o0OO0 = z3;
                                if (!"bomb".equals(O0Ooo0o)) {
                                    if (!"banana".equals(O0Ooo0o)) {
                                        z4 = false;
                                        O0oo0o = z4;
                                        if (!"lmi".equals(O0Ooo0o)) {
                                            if (!"lmiin".equals(O0Ooo0o)) {
                                                z5 = false;
                                                O0o0OO = z5;
                                                if (!"lmipro".equals(O0Ooo0o)) {
                                                    if (!"lmiinpro".equals(O0Ooo0o)) {
                                                        z6 = false;
                                                        O0o0OOO = z6;
                                                        str = "picasso";
                                                        if (!str.equals(O0Ooo0o)) {
                                                            if (!"picassoin".equals(O0Ooo0o)) {
                                                                z7 = false;
                                                                O0o0Oo0 = z7;
                                                                if (!"monet".equals(O0Ooo0o)) {
                                                                    if (!"monetin".equals(O0Ooo0o)) {
                                                                        z8 = false;
                                                                        O0o0Oo = z8;
                                                                        O0o0Ooo = str.equals(O0Ooo0o);
                                                                        if (!"gauguin".equals(O0Ooo0o)) {
                                                                            if (!"gauguinpro".equals(O0Ooo0o)) {
                                                                                if (!"gauguininpro".equals(O0Ooo0o)) {
                                                                                    z9 = false;
                                                                                    O0o0o00 = z9;
                                                                                    if (!"curtana".equals(O0Ooo0o)) {
                                                                                        if (!"durandal".equals(O0Ooo0o)) {
                                                                                            if (!"excalibur".equals(O0Ooo0o)) {
                                                                                                if (!"joyeuse".equals(O0Ooo0o)) {
                                                                                                    if (!"gram".equals(O0Ooo0o)) {
                                                                                                        z10 = false;
                                                                                                        O0o0o0o = z10;
                                                                                                        if (!"angelica".equals(O0Ooo0o)) {
                                                                                                            if (!"angelican".equals(O0Ooo0o)) {
                                                                                                                if (!"angelicain".equals(O0Ooo0o)) {
                                                                                                                    if (!"cattail".equals(O0Ooo0o)) {
                                                                                                                        z11 = false;
                                                                                                                        O0o0oO = z11;
                                                                                                                        boolean z13 = !TextUtils.equals(O0Ooo0o, "merlin") || TextUtils.equals(O0Ooo0o, "merlinnfc");
                                                                                                                        O0o0oOO = z13;
                                                                                                                        boolean z14 = !TextUtils.equals(O0Ooo0o, "cannon") || TextUtils.equals(O0Ooo0o, "cannong");
                                                                                                                        O0o0oOo = z14;
                                                                                                                        if (!"lime".equals(O0Ooo0o)) {
                                                                                                                            if (!"lemon".equals(O0Ooo0o)) {
                                                                                                                                if (!"pomelo".equals(O0Ooo0o)) {
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                        z12 = true;
                                                                                                                        O0o0oo = z12;
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                        z11 = true;
                                                                                                        O0o0oO = z11;
                                                                                                        if (!TextUtils.equals(O0Ooo0o, "merlin")) {
                                                                                                        }
                                                                                                        O0o0oOO = z13;
                                                                                                        if (!TextUtils.equals(O0Ooo0o, "cannon")) {
                                                                                                        }
                                                                                                        O0o0oOo = z14;
                                                                                                        if (!"lime".equals(O0Ooo0o)) {
                                                                                                        }
                                                                                                        z12 = true;
                                                                                                        O0o0oo = z12;
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    z10 = true;
                                                                                    O0o0o0o = z10;
                                                                                    if (!"angelica".equals(O0Ooo0o)) {
                                                                                    }
                                                                                    z11 = true;
                                                                                    O0o0oO = z11;
                                                                                    if (!TextUtils.equals(O0Ooo0o, "merlin")) {
                                                                                    }
                                                                                    O0o0oOO = z13;
                                                                                    if (!TextUtils.equals(O0Ooo0o, "cannon")) {
                                                                                    }
                                                                                    O0o0oOo = z14;
                                                                                    if (!"lime".equals(O0Ooo0o)) {
                                                                                    }
                                                                                    z12 = true;
                                                                                    O0o0oo = z12;
                                                                                }
                                                                            }
                                                                        }
                                                                        z9 = true;
                                                                        O0o0o00 = z9;
                                                                        if (!"curtana".equals(O0Ooo0o)) {
                                                                        }
                                                                        z10 = true;
                                                                        O0o0o0o = z10;
                                                                        if (!"angelica".equals(O0Ooo0o)) {
                                                                        }
                                                                        z11 = true;
                                                                        O0o0oO = z11;
                                                                        if (!TextUtils.equals(O0Ooo0o, "merlin")) {
                                                                        }
                                                                        O0o0oOO = z13;
                                                                        if (!TextUtils.equals(O0Ooo0o, "cannon")) {
                                                                        }
                                                                        O0o0oOo = z14;
                                                                        if (!"lime".equals(O0Ooo0o)) {
                                                                        }
                                                                        z12 = true;
                                                                        O0o0oo = z12;
                                                                    }
                                                                }
                                                                z8 = true;
                                                                O0o0Oo = z8;
                                                                O0o0Ooo = str.equals(O0Ooo0o);
                                                                if (!"gauguin".equals(O0Ooo0o)) {
                                                                }
                                                                z9 = true;
                                                                O0o0o00 = z9;
                                                                if (!"curtana".equals(O0Ooo0o)) {
                                                                }
                                                                z10 = true;
                                                                O0o0o0o = z10;
                                                                if (!"angelica".equals(O0Ooo0o)) {
                                                                }
                                                                z11 = true;
                                                                O0o0oO = z11;
                                                                if (!TextUtils.equals(O0Ooo0o, "merlin")) {
                                                                }
                                                                O0o0oOO = z13;
                                                                if (!TextUtils.equals(O0Ooo0o, "cannon")) {
                                                                }
                                                                O0o0oOo = z14;
                                                                if (!"lime".equals(O0Ooo0o)) {
                                                                }
                                                                z12 = true;
                                                                O0o0oo = z12;
                                                            }
                                                        }
                                                        z7 = true;
                                                        O0o0Oo0 = z7;
                                                        if (!"monet".equals(O0Ooo0o)) {
                                                        }
                                                        z8 = true;
                                                        O0o0Oo = z8;
                                                        O0o0Ooo = str.equals(O0Ooo0o);
                                                        if (!"gauguin".equals(O0Ooo0o)) {
                                                        }
                                                        z9 = true;
                                                        O0o0o00 = z9;
                                                        if (!"curtana".equals(O0Ooo0o)) {
                                                        }
                                                        z10 = true;
                                                        O0o0o0o = z10;
                                                        if (!"angelica".equals(O0Ooo0o)) {
                                                        }
                                                        z11 = true;
                                                        O0o0oO = z11;
                                                        if (!TextUtils.equals(O0Ooo0o, "merlin")) {
                                                        }
                                                        O0o0oOO = z13;
                                                        if (!TextUtils.equals(O0Ooo0o, "cannon")) {
                                                        }
                                                        O0o0oOo = z14;
                                                        if (!"lime".equals(O0Ooo0o)) {
                                                        }
                                                        z12 = true;
                                                        O0o0oo = z12;
                                                    }
                                                }
                                                z6 = true;
                                                O0o0OOO = z6;
                                                str = "picasso";
                                                if (!str.equals(O0Ooo0o)) {
                                                }
                                                z7 = true;
                                                O0o0Oo0 = z7;
                                                if (!"monet".equals(O0Ooo0o)) {
                                                }
                                                z8 = true;
                                                O0o0Oo = z8;
                                                O0o0Ooo = str.equals(O0Ooo0o);
                                                if (!"gauguin".equals(O0Ooo0o)) {
                                                }
                                                z9 = true;
                                                O0o0o00 = z9;
                                                if (!"curtana".equals(O0Ooo0o)) {
                                                }
                                                z10 = true;
                                                O0o0o0o = z10;
                                                if (!"angelica".equals(O0Ooo0o)) {
                                                }
                                                z11 = true;
                                                O0o0oO = z11;
                                                if (!TextUtils.equals(O0Ooo0o, "merlin")) {
                                                }
                                                O0o0oOO = z13;
                                                if (!TextUtils.equals(O0Ooo0o, "cannon")) {
                                                }
                                                O0o0oOo = z14;
                                                if (!"lime".equals(O0Ooo0o)) {
                                                }
                                                z12 = true;
                                                O0o0oo = z12;
                                            }
                                        }
                                        z5 = true;
                                        O0o0OO = z5;
                                        if (!"lmipro".equals(O0Ooo0o)) {
                                        }
                                        z6 = true;
                                        O0o0OOO = z6;
                                        str = "picasso";
                                        if (!str.equals(O0Ooo0o)) {
                                        }
                                        z7 = true;
                                        O0o0Oo0 = z7;
                                        if (!"monet".equals(O0Ooo0o)) {
                                        }
                                        z8 = true;
                                        O0o0Oo = z8;
                                        O0o0Ooo = str.equals(O0Ooo0o);
                                        if (!"gauguin".equals(O0Ooo0o)) {
                                        }
                                        z9 = true;
                                        O0o0o00 = z9;
                                        if (!"curtana".equals(O0Ooo0o)) {
                                        }
                                        z10 = true;
                                        O0o0o0o = z10;
                                        if (!"angelica".equals(O0Ooo0o)) {
                                        }
                                        z11 = true;
                                        O0o0oO = z11;
                                        if (!TextUtils.equals(O0Ooo0o, "merlin")) {
                                        }
                                        O0o0oOO = z13;
                                        if (!TextUtils.equals(O0Ooo0o, "cannon")) {
                                        }
                                        O0o0oOo = z14;
                                        if (!"lime".equals(O0Ooo0o)) {
                                        }
                                        z12 = true;
                                        O0o0oo = z12;
                                    }
                                }
                                z4 = true;
                                O0oo0o = z4;
                                if (!"lmi".equals(O0Ooo0o)) {
                                }
                                z5 = true;
                                O0o0OO = z5;
                                if (!"lmipro".equals(O0Ooo0o)) {
                                }
                                z6 = true;
                                O0o0OOO = z6;
                                str = "picasso";
                                if (!str.equals(O0Ooo0o)) {
                                }
                                z7 = true;
                                O0o0Oo0 = z7;
                                if (!"monet".equals(O0Ooo0o)) {
                                }
                                z8 = true;
                                O0o0Oo = z8;
                                O0o0Ooo = str.equals(O0Ooo0o);
                                if (!"gauguin".equals(O0Ooo0o)) {
                                }
                                z9 = true;
                                O0o0o00 = z9;
                                if (!"curtana".equals(O0Ooo0o)) {
                                }
                                z10 = true;
                                O0o0o0o = z10;
                                if (!"angelica".equals(O0Ooo0o)) {
                                }
                                z11 = true;
                                O0o0oO = z11;
                                if (!TextUtils.equals(O0Ooo0o, "merlin")) {
                                }
                                O0o0oOO = z13;
                                if (!TextUtils.equals(O0Ooo0o, "cannon")) {
                                }
                                O0o0oOo = z14;
                                if (!"lime".equals(O0Ooo0o)) {
                                }
                                z12 = true;
                                O0o0oo = z12;
                            }
                        }
                        z3 = true;
                        O0o0OO0 = z3;
                        if (!"bomb".equals(O0Ooo0o)) {
                        }
                        z4 = true;
                        O0oo0o = z4;
                        if (!"lmi".equals(O0Ooo0o)) {
                        }
                        z5 = true;
                        O0o0OO = z5;
                        if (!"lmipro".equals(O0Ooo0o)) {
                        }
                        z6 = true;
                        O0o0OOO = z6;
                        str = "picasso";
                        if (!str.equals(O0Ooo0o)) {
                        }
                        z7 = true;
                        O0o0Oo0 = z7;
                        if (!"monet".equals(O0Ooo0o)) {
                        }
                        z8 = true;
                        O0o0Oo = z8;
                        O0o0Ooo = str.equals(O0Ooo0o);
                        if (!"gauguin".equals(O0Ooo0o)) {
                        }
                        z9 = true;
                        O0o0o00 = z9;
                        if (!"curtana".equals(O0Ooo0o)) {
                        }
                        z10 = true;
                        O0o0o0o = z10;
                        if (!"angelica".equals(O0Ooo0o)) {
                        }
                        z11 = true;
                        O0o0oO = z11;
                        if (!TextUtils.equals(O0Ooo0o, "merlin")) {
                        }
                        O0o0oOO = z13;
                        if (!TextUtils.equals(O0Ooo0o, "cannon")) {
                        }
                        O0o0oOo = z14;
                        if (!"lime".equals(O0Ooo0o)) {
                        }
                        z12 = true;
                        O0o0oo = z12;
                    }
                }
                z2 = true;
                O0o0O = z2;
                if (!"atom".equals(O0Ooo0o)) {
                }
                z3 = true;
                O0o0OO0 = z3;
                if (!"bomb".equals(O0Ooo0o)) {
                }
                z4 = true;
                O0oo0o = z4;
                if (!"lmi".equals(O0Ooo0o)) {
                }
                z5 = true;
                O0o0OO = z5;
                if (!"lmipro".equals(O0Ooo0o)) {
                }
                z6 = true;
                O0o0OOO = z6;
                str = "picasso";
                if (!str.equals(O0Ooo0o)) {
                }
                z7 = true;
                O0o0Oo0 = z7;
                if (!"monet".equals(O0Ooo0o)) {
                }
                z8 = true;
                O0o0Oo = z8;
                O0o0Ooo = str.equals(O0Ooo0o);
                if (!"gauguin".equals(O0Ooo0o)) {
                }
                z9 = true;
                O0o0o00 = z9;
                if (!"curtana".equals(O0Ooo0o)) {
                }
                z10 = true;
                O0o0o0o = z10;
                if (!"angelica".equals(O0Ooo0o)) {
                }
                z11 = true;
                O0o0oO = z11;
                if (!TextUtils.equals(O0Ooo0o, "merlin")) {
                }
                O0o0oOO = z13;
                if (!TextUtils.equals(O0Ooo0o, "cannon")) {
                }
                O0o0oOo = z14;
                if (!"lime".equals(O0Ooo0o)) {
                }
                z12 = true;
                O0o0oo = z12;
            }
        }
        z = true;
        O0o00OO = z;
        if (!"apollo".equals(O0Ooo0o)) {
        }
        z2 = true;
        O0o0O = z2;
        if (!"atom".equals(O0Ooo0o)) {
        }
        z3 = true;
        O0o0OO0 = z3;
        if (!"bomb".equals(O0Ooo0o)) {
        }
        z4 = true;
        O0oo0o = z4;
        if (!"lmi".equals(O0Ooo0o)) {
        }
        z5 = true;
        O0o0OO = z5;
        if (!"lmipro".equals(O0Ooo0o)) {
        }
        z6 = true;
        O0o0OOO = z6;
        str = "picasso";
        if (!str.equals(O0Ooo0o)) {
        }
        z7 = true;
        O0o0Oo0 = z7;
        if (!"monet".equals(O0Ooo0o)) {
        }
        z8 = true;
        O0o0Oo = z8;
        O0o0Ooo = str.equals(O0Ooo0o);
        if (!"gauguin".equals(O0Ooo0o)) {
        }
        z9 = true;
        O0o0o00 = z9;
        if (!"curtana".equals(O0Ooo0o)) {
        }
        z10 = true;
        O0o0o0o = z10;
        if (!"angelica".equals(O0Ooo0o)) {
        }
        z11 = true;
        O0o0oO = z11;
        if (!TextUtils.equals(O0Ooo0o, "merlin")) {
        }
        O0o0oOO = z13;
        if (!TextUtils.equals(O0Ooo0o, "cannon")) {
        }
        O0o0oOo = z14;
        if (!"lime".equals(O0Ooo0o)) {
        }
        z12 = true;
        O0o0oo = z12;
    }

    public static boolean O000O0o(boolean z) {
        String str = SystemProperties.get("ro.miui.customized.region");
        if ("fr_sfr".equals(str) || "fr_orange".equals(str) || "jp_kd".equals(str)) {
            return false;
        }
        if ("es_vodafone".equals(str)) {
            if ("NL".equals(getCountry())) {
                return false;
            }
        }
        return z;
    }

    private static boolean O000Ooo0(String str) {
        for (String equals : O0oO0O0) {
            if (TextUtils.equals(str, equals)) {
                return true;
            }
        }
        return false;
    }

    public static boolean OOO0Oo() {
        return C0122O00000o.instance().OOO0Oo();
    }

    public static boolean OOOOOo() {
        return C0122O00000o.instance().OOOOOo();
    }

    public static boolean OOo0oOo() {
        return C0122O00000o.instance().OOo0oOo();
    }

    public static boolean OOoOooO() {
        return !oOOoOO && C0122O00000o.instance().getConfig().OOoOooO();
    }

    public static boolean OOoOooo() {
        return C0122O00000o.instance().getConfig().OOoOooo();
    }

    public static boolean OOoo() {
        String str = SystemProperties.get(O0oO0O);
        return O0o0o0O && (TextUtils.equals(str, "7.1.7") || TextUtils.equals(str, "7.2.0"));
    }

    public static String[] OOoo0() {
        return C0122O00000o.instance().getConfig().OOoo0().split(",");
    }

    public static float OOoo000() {
        return C0122O00000o.instance().getConfig().OOoo000();
    }

    public static int OOoo00O() {
        return C0122O00000o.instance().getConfig().OOoo00O();
    }

    public static ArrayList OOoo00o() {
        if (O0oO0 == null) {
            O0oO0 = new ArrayList();
            String[] OOoo0 = OOoo0();
            if (OOoo0 != null) {
                Collections.addAll(O0oO0, OOoo0);
            }
        }
        return O0oO0;
    }

    public static int[] OOoo0O() {
        String OOoo0O = C0122O00000o.instance().getConfig().OOoo0O();
        if (TextUtils.isEmpty(OOoo0O)) {
            return new int[0];
        }
        String[] split = OOoo0O.split(",");
        int[] iArr = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            iArr[i] = Integer.parseInt(split[i]);
        }
        return iArr;
    }

    public static int OOoo0O0() {
        return C0122O00000o.instance().getConfig().OOoo0O0();
    }

    public static boolean OOoo0OO() {
        return C0122O00000o.instance().getConfig().OOoo0OO();
    }

    public static boolean OOoo0Oo() {
        return ((float) Display.getWindowHeight()) / ((float) Display.getWindowWidth()) >= 2.0f && C0122O00000o.instance().getConfig().OOoo0Oo();
    }

    public static boolean OOoo0o() {
        return C0122O00000o.instance().OOo000o() || O0o00OO;
    }

    public static boolean OOoo0o0() {
        return !Oo0Oo0o() && C0122O00000o.instance().OOO00oO() && OOoo00o() != null && !OOoo00o().isEmpty();
    }

    public static boolean OOoo0oO() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return O000Ooo0(getCountry());
        }
        return false;
    }

    public static boolean OOoo0oo() {
        return C0122O00000o.instance().getConfig().OOoo0oo();
    }

    public static boolean OOooO() {
        return OOooOoO() || O0o00o || O0o00O0 || C0122O00000o.instance().OOo000o();
    }

    public static boolean OOooO0() {
        if (O0OooOo) {
            if ("India".equalsIgnoreCase(SystemProperties.get("ro.boot.hwc"))) {
                return true;
            }
        }
        return false;
    }

    public static boolean OOooO00() {
        if (!"onc".equals(O0Ooo0o)) {
            return false;
        }
        String str = SystemProperties.get(O0oO0O);
        return !TextUtils.isEmpty(str) && '2' == str.charAt(0);
    }

    public static boolean OOooO0O() {
        if (O0OooOo) {
            if ("India_48_5".equalsIgnoreCase(SystemProperties.get("ro.boot.hwc"))) {
                return true;
            }
        }
        return false;
    }

    public static boolean OOooO0o() {
        return O0OoooO;
    }

    public static boolean OOooOO() {
        return O0o00O || O0o00Oo;
    }

    public static boolean OOooOO0() {
        return C0122O00000o.instance().getConfig().OOooOO0();
    }

    public static boolean OOooOOO() {
        if (!O0o0Ooo) {
            return false;
        }
        return "picasso_48m".equalsIgnoreCase(SystemProperties.get("ro.product.name"));
    }

    public static boolean OOooOOo() {
        if (!O0o0Ooo) {
            return false;
        }
        String[] strArr = {"3.9.3", "3.9.5"};
        String str = SystemProperties.get(O0oO0O);
        for (String equalsIgnoreCase : strArr) {
            if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean OOooOo0() {
        return O0Ooo0o.equalsIgnoreCase("equuleus") && Build.IS_INTERNATIONAL_BUILD;
    }

    public static boolean OOooOoO() {
        return O0o000O && Build.IS_INTERNATIONAL_BUILD;
    }

    public static boolean OOooOoo() {
        return O0o000o && Build.IS_INTERNATIONAL_BUILD;
    }

    public static boolean OOooo() {
        return "toco".equals(O0Ooo0o);
    }

    public static boolean OOooo0() {
        return O0Ooo0o.equalsIgnoreCase("raphael") && Build.IS_INTERNATIONAL_BUILD;
    }

    public static boolean OOooo00() {
        return O0Ooo0o.equalsIgnoreCase("davinci") && Build.IS_INTERNATIONAL_BUILD;
    }

    public static boolean OOooo0O() {
        return O0o00o && Build.IS_INTERNATIONAL_BUILD;
    }

    public static boolean OOooo0o() {
        return O0o0O00 && Build.IS_INTERNATIONAL_BUILD;
    }

    public static boolean OOoooO() {
        return O0o0oOO && Build.IS_INTERNATIONAL_BUILD;
    }

    public static boolean OOoooO0() {
        return "lmi".equals(O0Ooo0o) && Build.IS_INTERNATIONAL_BUILD;
    }

    public static boolean OOoooOO() {
        return C0122O00000o.instance().getConfig().OOoooOO();
    }

    public static boolean OOoooOo() {
        return C0122O00000o.instance().getConfig().OOoooOo();
    }

    public static boolean OOoooo0() {
        return C0122O00000o.instance().getConfig().OOoooo0();
    }

    public static boolean OOooooO() {
        if (O0OooOO) {
            if ("India".equalsIgnoreCase(SystemProperties.get("ro.boot.hwc"))) {
                return true;
            }
        }
        return false;
    }

    public static boolean OOooooo() {
        return C0122O00000o.instance().getConfig().OOooooo();
    }

    private static boolean Oo() {
        return SystemProperties.getBoolean("ro.hardware.fp.fod", false);
    }

    public static boolean Oo0() {
        return C0122O00000o.instance().getConfig().Oo0();
    }

    public static boolean Oo00() {
        return C0122O00000o.instance().getConfig().Oo00();
    }

    public static boolean Oo000() {
        return C0122O00000o.instance().getConfig().Oo000();
    }

    public static boolean Oo00000() {
        if (O0o0o0o) {
            if ("India".equalsIgnoreCase(SystemProperties.get("ro.boot.hwc"))) {
                return true;
            }
        }
        return false;
    }

    public static boolean Oo0000O() {
        return O0o0OO || O0o0OOO;
    }

    public static boolean Oo0000o() {
        if (!O0o0O00) {
            return false;
        }
        String str = "03";
        return SystemProperties.get(VERSION.SDK_INT >= 29 ? "persist.vendor.camera.rearMain.vendorID" : "persist.camera.rearMain.vendorID", str).equals(str);
    }

    public static boolean Oo000O() {
        return O0o0O00 && !Oo0000o();
    }

    public static boolean Oo000O0() {
        return O0Ooo0o.equalsIgnoreCase("raphael") && Build.MODEL.endsWith("Premium Edition");
    }

    public static boolean Oo000OO() {
        return O0Ooo.equals(Oo0O());
    }

    public static boolean Oo000Oo() {
        return C0122O00000o.instance().getConfig().Oo000Oo();
    }

    public static boolean Oo000o() {
        return C0122O00000o.instance().getConfig().Oo000o();
    }

    public static boolean Oo000o0() {
        return O0o0O0O || O0o0O0 || Oo0000O() || O0o0o0o || O0o0o00 || O0o0Oo || O0o0OoO || O0o0Oo0;
    }

    public static boolean Oo000oO() {
        return C0122O00000o.instance().getConfig().Oo000oO();
    }

    public static boolean Oo000oo() {
        return C0122O00000o.instance().getConfig().Oo000oo();
    }

    public static boolean Oo00O() {
        return Oo000oo() || Oo00O0o();
    }

    public static boolean Oo00O0() {
        return C0122O00000o.instance().getConfig().Oo00O0();
    }

    public static boolean Oo00O00() {
        return C0122O00000o.instance().getConfig().Oo00O00();
    }

    public static boolean Oo00O0O() {
        return C0122O00000o.instance().getConfig().Oo00O0O();
    }

    public static boolean Oo00O0o() {
        return C0122O00000o.instance().getConfig().Oo00O0o();
    }

    public static boolean Oo00OO() {
        return (Oo0O0o() & 13) != 0;
    }

    public static boolean Oo00OO0() {
        return C0122O00000o.instance().getConfig().Oo00OO0();
    }

    public static boolean Oo00OOO() {
        return C0122O00000o.instance().getConfig().Oo00OOO();
    }

    public static boolean Oo00OOo() {
        return (Oo0O0o() & 1) != 0;
    }

    public static boolean Oo00Oo() {
        return C0122O00000o.instance().getConfig().Oo00Oo();
    }

    public static boolean Oo00Oo0() {
        return C0122O00000o.instance().getConfig().Oo00Oo0();
    }

    public static boolean Oo00OoO() {
        return C0122O00000o.instance().getConfig().Oo00OoO();
    }

    public static boolean Oo00Ooo() {
        return C0122O00000o.instance().getConfig().Oo00Ooo();
    }

    public static boolean Oo00o() {
        return C0122O00000o.instance().getConfig().Oo00o();
    }

    public static boolean Oo00o0() {
        if (OOooO00()) {
            return false;
        }
        return C0122O00000o.instance().getConfig().Oo00o0();
    }

    public static boolean Oo00o00() {
        return C0122O00000o.instance().getConfig().Oo00o00();
    }

    public static boolean Oo00o0O() {
        return !OOoo0oO();
    }

    public static boolean Oo00o0o() {
        return C0122O00000o.instance().getConfig().Oo00o0o();
    }

    public static boolean Oo00oO0() {
        return C0122O00000o.instance().getConfig().Oo00oO0();
    }

    public static boolean Oo00oOO() {
        return C0122O00000o.instance().getConfig().Oo00oOO();
    }

    public static boolean Oo00oo() {
        return C0122O00000o.instance().getConfig().Oo00oo();
    }

    public static boolean Oo00oo0() {
        return C0122O00000o.instance().getConfig().Oo00oo0();
    }

    public static boolean Oo00ooO() {
        return C0122O00000o.instance().getConfig().Oo00ooO();
    }

    public static boolean Oo00ooo() {
        return C0122O00000o.instance().getConfig().Oo00ooo();
    }

    public static String Oo0O() {
        return C0122O00000o.instance().getConfig().Oo0O();
    }

    public static boolean Oo0O0() {
        return C0122O00000o.instance().getConfig().Oo0O0();
    }

    public static boolean Oo0O00() {
        return C0122O00000o.instance().getConfig().Oo0O00();
    }

    public static boolean Oo0O000() {
        return C0122O00000o.instance().getConfig().Oo0O000();
    }

    public static boolean Oo0O00O() {
        return C0122O00000o.instance().getConfig().Oo0O00O();
    }

    public static boolean Oo0O00o() {
        return C0122O00000o.instance().getConfig().Oo0O00o();
    }

    public static boolean Oo0O0O() {
        return C0122O00000o.instance().getConfig().Oo0O0O();
    }

    public static boolean Oo0O0O0() {
        return !IS_XIAOMI && !IS_HONGMI;
    }

    public static boolean Oo0O0OO() {
        return C0122O00000o.instance().getConfig().Oo0O0OO();
    }

    public static boolean Oo0O0Oo() {
        return C0122O00000o.instance().getConfig().Oo0OooO() && Util.TOTAL_MEMORY_GB < 6;
    }

    public static int Oo0O0o() {
        return C0122O00000o.instance().getConfig().Oo0O0o();
    }

    public static int Oo0O0o0() {
        return C0122O00000o.instance().getConfig().Oo0O0o0();
    }

    public static boolean Oo0O0oO() {
        return SystemProperties.getInt("ro.boot.camera.config", -1) != -1;
    }

    public static boolean Oo0O0oo() {
        int[] OOoo0O = OOoo0O();
        return OOoo0O != null && OOoo0O.length > 0;
    }

    private static boolean Oo0Oo0o() {
        return C0122O00000o.instance().getConfig().Oo0Oo0o() || Oo();
    }

    public static boolean Oo0o0O0() {
        return C0122O00000o.instance().getConfig().Oo0o0O0();
    }

    public static boolean Oo0o0Oo() {
        return C0122O00000o.instance().getConfig().Oo0o0Oo();
    }

    public static boolean Oo0o0o() {
        return !C0122O00000o.instance().OO0oO0o() && IS_HONGMI;
    }

    public static String getCountry() {
        String str = Util.sRegion;
        return !TextUtils.isEmpty(str) ? str : Locale.getDefault().getCountry();
    }

    public static boolean isGlobal() {
        return Build.IS_INTERNATIONAL_BUILD;
    }

    public static boolean isMTKPlatform() {
        if (!((Optional) IS_MTK_PLATFORM.get()).isPresent()) {
            synchronized (IS_MTK_PLATFORM) {
                if (!((Optional) IS_MTK_PLATFORM.get()).isPresent()) {
                    IS_MTK_PLATFORM.set(Optional.of(Boolean.valueOf(O0OooO0.equals(Oo0O()))));
                }
            }
        }
        return ((Boolean) ((Optional) IS_MTK_PLATFORM.get()).get()).booleanValue();
    }

    public static boolean isPad() {
        return C0122O00000o.instance().getConfig().isPad();
    }

    public static boolean isSupportSuperResolution() {
        return C0122O00000o.instance().getConfig().isSupportSuperResolution();
    }

    public static boolean isSupportedOpticalZoom() {
        return C0122O00000o.instance().getConfig().isSupportedOpticalZoom();
    }

    public static boolean o00OO00() {
        return C0122O00000o.instance().getConfig().o00OO00();
    }

    public static boolean o00oO00() {
        return C0122O00000o.instance().getConfig().o00oO00();
    }
}
