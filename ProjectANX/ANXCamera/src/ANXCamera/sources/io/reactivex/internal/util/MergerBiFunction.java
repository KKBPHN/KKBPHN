package io.reactivex.internal.util;

import io.reactivex.functions.BiFunction;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public final class MergerBiFunction implements BiFunction {
    final Comparator comparator;

    public MergerBiFunction(Comparator comparator2) {
        this.comparator = comparator2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0031, code lost:
        r3 = r7.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0036, code lost:
        r3 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0037, code lost:
        if (r0 == null) goto L_0x005d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0039, code lost:
        if (r3 == null) goto L_0x005d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0041, code lost:
        if (r5.comparator.compare(r0, r3) >= 0) goto L_0x0053;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0043, code lost:
        r1.add(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004a, code lost:
        if (r6.hasNext() == false) goto L_0x0051;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004c, code lost:
        r0 = r6.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0051, code lost:
        r0 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0053, code lost:
        r1.add(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005a, code lost:
        if (r7.hasNext() == false) goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005d, code lost:
        if (r0 == null) goto L_0x0070;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x005f, code lost:
        r1.add(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0066, code lost:
        if (r6.hasNext() == false) goto L_0x0083;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0068, code lost:
        r1.add(r6.next());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0070, code lost:
        if (r3 == null) goto L_0x0083;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0072, code lost:
        r1.add(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0079, code lost:
        if (r7.hasNext() == false) goto L_0x0083;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x007b, code lost:
        r1.add(r7.next());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0083, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002f, code lost:
        if (r7.hasNext() != false) goto L_0x0031;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public List apply(List list, List list2) {
        int size = list.size() + list2.size();
        if (size == 0) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList(size);
        Iterator it = list.iterator();
        Iterator it2 = list2.iterator();
        Object next = it.hasNext() ? it.next() : null;
    }
}
