package defpackage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* renamed from: ep reason: default package */
final class ep {
    public static final ep a = new ep();
    private final et b = new dz();
    private final ConcurrentMap c = new ConcurrentHashMap();

    private ep() {
    }

    public final es O00000Oo(Class cls) {
        ff ffVar;
        ej ejVar;
        fh fhVar;
        dv dvVar;
        fs fsVar;
        ej ejVar2;
        ej ejVar3;
        fh fhVar2;
        String str = "messageType";
        dj.a(cls, str);
        es esVar = (es) this.c.get(cls);
        if (esVar == null) {
            et etVar = this.b;
            eu.O00000Oo(cls);
            ee O000000o2 = ((dz) etVar).a.O000000o(cls);
            if (O000000o2.a()) {
                if (de.class.isAssignableFrom(cls)) {
                    fhVar2 = eu.c;
                    ejVar3 = ct.a;
                } else {
                    fhVar2 = eu.a;
                    ejVar3 = ct.a();
                }
                esVar = el.O000000o(fhVar2, ejVar3, O000000o2.b());
            } else {
                if (de.class.isAssignableFrom(cls)) {
                    boolean O000000o3 = dz.O000000o(O000000o2);
                    fsVar = en.b;
                    dvVar = dv.b;
                    fhVar = eu.c;
                    ejVar = O000000o3 ? ct.a : null;
                    ffVar = ed.b;
                } else {
                    boolean O000000o4 = dz.O000000o(O000000o2);
                    fsVar = en.a;
                    dvVar = dv.a;
                    if (O000000o4) {
                        fhVar = eu.a;
                        ejVar2 = ct.a();
                    } else {
                        fhVar = eu.b;
                        ejVar2 = null;
                    }
                    ffVar = ed.a;
                }
                esVar = ek.O000000o(O000000o2, fsVar, dvVar, fhVar, ejVar, ffVar);
            }
            dj.a(cls, str);
            dj.a(esVar, "schema");
            es esVar2 = (es) this.c.putIfAbsent(cls, esVar);
            return esVar2 == null ? esVar : esVar2;
        }
    }

    public final es O00000oO(Object obj) {
        return O00000Oo(obj.getClass());
    }
}
