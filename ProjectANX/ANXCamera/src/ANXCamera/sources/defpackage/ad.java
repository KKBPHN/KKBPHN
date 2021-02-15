package defpackage;

import com.android.camera.data.data.config.SupportedConfigFactory;

/* renamed from: ad reason: default package */
public final class ad extends de implements ei {
    public static final ad a;
    private int b;
    private boolean c;

    static {
        ad adVar = new ad();
        a = adVar;
        de.O000000o(ad.class, (de) adVar);
    }

    private ad() {
    }

    public static /* synthetic */ void O000000o(ad adVar) {
        adVar.b |= 1;
        adVar.c = true;
    }

    /* access modifiers changed from: protected */
    public final Object O000000o(int i, Object obj) {
        int i2 = i - 1;
        if (i2 == 0) {
            return Byte.valueOf(1);
        }
        if (i2 == 1) {
            return null;
        }
        if (i2 != 2) {
            return i2 != 3 ? i2 != 4 ? a : new ac() : new ad();
        }
        return de.O000000o((eh) a, "\u0001\u0001\u0000\u0001\u0001\u0001\u0001\u0000\u0000\u0000\u0001ဇ\u0000", new Object[]{SupportedConfigFactory.CLOSE_BY_GROUP, SupportedConfigFactory.CLOSE_BY_SUPER_RESOLUTION});
    }
}
