package defpackage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* renamed from: cs reason: default package */
public class cs {
    public static volatile cs a;
    static final cs b = new cs(null);
    private static volatile boolean c;
    private static volatile cs d;
    private final Map e;

    cs() {
        this.e = new HashMap();
    }

    public cs(byte[] bArr) {
        this.e = Collections.emptyMap();
    }

    public static cs a() {
        cs csVar = d;
        if (csVar == null) {
            synchronized (cs.class) {
                csVar = d;
                if (csVar == null) {
                    csVar = b;
                    d = csVar;
                }
            }
        }
        return csVar;
    }

    public cq O000000o(eh ehVar, int i) {
        return (cq) this.e.get(new cr(ehVar, i));
    }
}
