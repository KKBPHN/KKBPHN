package defpackage;

import com.android.camera.data.data.config.SupportedConfigFactory;

/* renamed from: bi reason: default package */
public final class bi extends de implements ei {
    public static final bi f;
    public int a;
    public String b;
    public String c;
    public int d = -1;
    public int e = -1;

    static {
        bi biVar = new bi();
        f = biVar;
        de.O000000o(bi.class, (de) biVar);
    }

    private bi() {
        String str = "";
        this.b = str;
        this.c = str;
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
            return i2 != 3 ? i2 != 4 ? f : new bf() : new bi();
        }
        return de.O000000o((eh) f, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0000\u0000\u0001ဈ\u0000\u0002ဈ\u0001\u0003ဌ\u0002\u0004ဌ\u0003", new Object[]{SupportedConfigFactory.CLOSE_BY_HHT, SupportedConfigFactory.CLOSE_BY_GROUP, SupportedConfigFactory.CLOSE_BY_SUPER_RESOLUTION, SupportedConfigFactory.CLOSE_BY_BURST_SHOOT, bh.a(), "e", bh.a()});
    }
}
