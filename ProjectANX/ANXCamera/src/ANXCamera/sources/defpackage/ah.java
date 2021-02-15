package defpackage;

import com.android.camera.data.data.config.SupportedConfigFactory;

/* renamed from: ah reason: default package */
public final class ah extends dc implements ei {
    public static final ah b;
    public int a = 1;
    private int c;
    private byte e = 2;

    static {
        ah ahVar = new ah();
        b = ahVar;
        de.O000000o(ah.class, (de) ahVar);
    }

    private ah() {
    }

    /* access modifiers changed from: protected */
    public final Object O000000o(int i, Object obj) {
        int i2 = i - 1;
        if (i2 == 0) {
            return Byte.valueOf(this.e);
        }
        byte b2 = 0;
        if (i2 == 1) {
            if (obj != null) {
                b2 = 1;
            }
            this.e = b2;
            return null;
        } else if (i2 != 2) {
            return i2 != 3 ? i2 != 4 ? b : new ag() : new ah();
        } else {
            return de.O000000o((eh) b, "\u0001\u0001\u0000\u0001\u0001\u0001\u0001\u0000\u0000\u0000\u0001ဌ\u0000", new Object[]{SupportedConfigFactory.CLOSE_BY_SUPER_RESOLUTION, SupportedConfigFactory.CLOSE_BY_HHT, af.a()});
        }
    }
}
