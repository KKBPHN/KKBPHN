package defpackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* renamed from: dt reason: default package */
final class dt extends dv {
    private static final Class c = Collections.unmodifiableList(Collections.emptyList()).getClass();

    static List O00000Oo(Object obj, long j) {
        return (List) fr.O00000oo(obj, j);
    }

    public final void O000000o(Object obj, long j) {
        Object obj2;
        List list = (List) fr.O00000oo(obj, j);
        if (list instanceof ds) {
            obj2 = ((ds) list).e();
        } else {
            if (!c.isAssignableFrom(list.getClass())) {
                if (!(list instanceof eo) || !(list instanceof di)) {
                    obj2 = Collections.unmodifiableList(list);
                } else {
                    di diVar = (di) list;
                    if (diVar.a()) {
                        diVar.b();
                    }
                    return;
                }
            }
        }
        fr.O000000o(obj, j, obj2);
    }

    /* JADX WARNING: type inference failed for: r1v10, types: [java.lang.Object] */
    /* JADX WARNING: type inference failed for: r1v15 */
    /* JADX WARNING: type inference failed for: r1v16 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void O000000o(Object obj, Object obj2, long j) {
        ? r1;
        List O00000Oo2 = O00000Oo(obj2, j);
        int size = O00000Oo2.size();
        List O00000Oo3 = O00000Oo(obj, j);
        if (O00000Oo3.isEmpty()) {
            Object obj3 = O00000Oo3 instanceof ds ? new dr(size) : (!(O00000Oo3 instanceof eo) || !(O00000Oo3 instanceof di)) ? new ArrayList(size) : ((di) O00000Oo3).a(size);
            fr.O000000o(obj, j, obj3);
            O00000Oo3 = obj3;
        } else {
            if (c.isAssignableFrom(O00000Oo3.getClass())) {
                ArrayList arrayList = new ArrayList(O00000Oo3.size() + size);
                arrayList.addAll(O00000Oo3);
                r1 = arrayList;
            } else if (O00000Oo3 instanceof fl) {
                dr drVar = new dr(O00000Oo3.size() + size);
                drVar.addAll((fl) O00000Oo3);
                r1 = drVar;
            } else if ((O00000Oo3 instanceof eo) && (O00000Oo3 instanceof di)) {
                di diVar = (di) O00000Oo3;
                if (!diVar.a()) {
                    List a = diVar.a(O00000Oo3.size() + size);
                    fr.O000000o(obj, j, (Object) a);
                    O00000Oo3 = a;
                }
            }
            fr.O000000o(obj, j, (Object) r1);
            O00000Oo3 = r1;
        }
        int size2 = O00000Oo3.size();
        int size3 = O00000Oo2.size();
        if (size2 > 0 && size3 > 0) {
            O00000Oo3.addAll(O00000Oo2);
        }
        if (size2 > 0) {
            O00000Oo2 = O00000Oo3;
        }
        fr.O000000o(obj, j, O00000Oo2);
    }
}
