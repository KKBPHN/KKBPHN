package com.airbnb.lottie.parser.moshi;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

final class LinkedHashTreeMap extends AbstractMap implements Serializable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final Comparator NATURAL_ORDER = new C0116O00000oO();
    Comparator comparator;
    private O0000Oo0 entrySet;
    final O0000o00 header;
    private C0118O0000OoO keySet;
    int modCount;
    int size;
    O0000o00[] table;
    int threshold;

    LinkedHashTreeMap() {
        this(null);
    }

    LinkedHashTreeMap(Comparator comparator2) {
        this.size = 0;
        this.modCount = 0;
        if (comparator2 == null) {
            comparator2 = NATURAL_ORDER;
        }
        this.comparator = comparator2;
        this.header = new O0000o00();
        this.table = new O0000o00[16];
        O0000o00[] o0000o00Arr = this.table;
        this.threshold = (o0000o00Arr.length / 2) + (o0000o00Arr.length / 4);
    }

    private void O000000o(O0000o00 o0000o00, O0000o00 o0000o002) {
        O0000o00 o0000o003 = o0000o00.parent;
        o0000o00.parent = null;
        if (o0000o002 != null) {
            o0000o002.parent = o0000o003;
        }
        if (o0000o003 == null) {
            int i = o0000o00.hash;
            O0000o00[] o0000o00Arr = this.table;
            o0000o00Arr[i & (o0000o00Arr.length - 1)] = o0000o002;
        } else if (o0000o003.left == o0000o00) {
            o0000o003.left = o0000o002;
        } else {
            o0000o003.right = o0000o002;
        }
    }

    static O0000o00[] O000000o(O0000o00[] o0000o00Arr) {
        int length = o0000o00Arr.length;
        O0000o00[] o0000o00Arr2 = new O0000o00[(length * 2)];
        O0000O0o o0000O0o = new O0000O0o();
        C0117O00000oo o00000oo = new C0117O00000oo();
        C0117O00000oo o00000oo2 = new C0117O00000oo();
        for (int i = 0; i < length; i++) {
            O0000o00 o0000o00 = o0000o00Arr[i];
            if (o0000o00 != null) {
                o0000O0o.O00000Oo(o0000o00);
                int i2 = 0;
                int i3 = 0;
                while (true) {
                    O0000o00 next = o0000O0o.next();
                    if (next == null) {
                        break;
                    } else if ((next.hash & length) == 0) {
                        i2++;
                    } else {
                        i3++;
                    }
                }
                o00000oo.reset(i2);
                o00000oo2.reset(i3);
                o0000O0o.O00000Oo(o0000o00);
                while (true) {
                    O0000o00 next2 = o0000O0o.next();
                    if (next2 == null) {
                        break;
                    } else if ((next2.hash & length) == 0) {
                        o00000oo.O000000o(next2);
                    } else {
                        o00000oo2.O000000o(next2);
                    }
                }
                O0000o00 o0000o002 = null;
                o0000o00Arr2[i] = i2 > 0 ? o00000oo.O00oo000() : null;
                int i4 = i + length;
                if (i3 > 0) {
                    o0000o002 = o00000oo2.O00oo000();
                }
                o0000o00Arr2[i4] = o0000o002;
            }
        }
        return o0000o00Arr2;
    }

    private void O00000Oo(O0000o00 o0000o00, boolean z) {
        while (o0000o00 != null) {
            O0000o00 o0000o002 = o0000o00.left;
            O0000o00 o0000o003 = o0000o00.right;
            int i = 0;
            int i2 = o0000o002 != null ? o0000o002.height : 0;
            int i3 = o0000o003 != null ? o0000o003.height : 0;
            int i4 = i2 - i3;
            if (i4 == -2) {
                O0000o00 o0000o004 = o0000o003.left;
                O0000o00 o0000o005 = o0000o003.right;
                int i5 = o0000o005 != null ? o0000o005.height : 0;
                if (o0000o004 != null) {
                    i = o0000o004.height;
                }
                int i6 = i - i5;
                if (i6 != -1 && (i6 != 0 || z)) {
                    O00000o(o0000o003);
                }
                O00000o0(o0000o00);
                if (z) {
                    return;
                }
            } else if (i4 == 2) {
                O0000o00 o0000o006 = o0000o002.left;
                O0000o00 o0000o007 = o0000o002.right;
                int i7 = o0000o007 != null ? o0000o007.height : 0;
                if (o0000o006 != null) {
                    i = o0000o006.height;
                }
                int i8 = i - i7;
                if (i8 != 1 && (i8 != 0 || z)) {
                    O00000o0(o0000o002);
                }
                O00000o(o0000o00);
                if (z) {
                    return;
                }
            } else if (i4 == 0) {
                o0000o00.height = i2 + 1;
                if (z) {
                    return;
                }
            } else {
                o0000o00.height = Math.max(i2, i3) + 1;
                if (!z) {
                    return;
                }
            }
            o0000o00 = o0000o00.parent;
        }
    }

    private void O00000o(O0000o00 o0000o00) {
        O0000o00 o0000o002 = o0000o00.left;
        O0000o00 o0000o003 = o0000o00.right;
        O0000o00 o0000o004 = o0000o002.left;
        O0000o00 o0000o005 = o0000o002.right;
        o0000o00.left = o0000o005;
        if (o0000o005 != null) {
            o0000o005.parent = o0000o00;
        }
        O000000o(o0000o00, o0000o002);
        o0000o002.right = o0000o00;
        o0000o00.parent = o0000o002;
        int i = 0;
        o0000o00.height = Math.max(o0000o003 != null ? o0000o003.height : 0, o0000o005 != null ? o0000o005.height : 0) + 1;
        int i2 = o0000o00.height;
        if (o0000o004 != null) {
            i = o0000o004.height;
        }
        o0000o002.height = Math.max(i2, i) + 1;
    }

    private void O00000o0(O0000o00 o0000o00) {
        O0000o00 o0000o002 = o0000o00.left;
        O0000o00 o0000o003 = o0000o00.right;
        O0000o00 o0000o004 = o0000o003.left;
        O0000o00 o0000o005 = o0000o003.right;
        o0000o00.right = o0000o004;
        if (o0000o004 != null) {
            o0000o004.parent = o0000o00;
        }
        O000000o(o0000o00, o0000o003);
        o0000o003.left = o0000o00;
        o0000o00.parent = o0000o003;
        int i = 0;
        o0000o00.height = Math.max(o0000o002 != null ? o0000o002.height : 0, o0000o004 != null ? o0000o004.height : 0) + 1;
        int i2 = o0000o00.height;
        if (o0000o005 != null) {
            i = o0000o005.height;
        }
        o0000o003.height = Math.max(i2, i) + 1;
    }

    private static int O000Oo0(int i) {
        int i2 = i ^ ((i >>> 20) ^ (i >>> 12));
        return (i2 >>> 4) ^ ((i2 >>> 7) ^ i2);
    }

    private void doubleCapacity() {
        this.table = O000000o(this.table);
        O0000o00[] o0000o00Arr = this.table;
        this.threshold = (o0000o00Arr.length / 2) + (o0000o00Arr.length / 4);
    }

    private boolean equal(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    private Object writeReplace() {
        return new LinkedHashMap(this);
    }

    /* access modifiers changed from: 0000 */
    public void O000000o(O0000o00 o0000o00, boolean z) {
        int i;
        if (z) {
            O0000o00 o0000o002 = o0000o00.prev;
            o0000o002.next = o0000o00.next;
            o0000o00.next.prev = o0000o002;
            o0000o00.prev = null;
            o0000o00.next = null;
        }
        O0000o00 o0000o003 = o0000o00.left;
        O0000o00 o0000o004 = o0000o00.right;
        O0000o00 o0000o005 = o0000o00.parent;
        int i2 = 0;
        if (o0000o003 == null || o0000o004 == null) {
            if (o0000o003 != null) {
                O000000o(o0000o00, o0000o003);
                o0000o00.left = null;
            } else if (o0000o004 != null) {
                O000000o(o0000o00, o0000o004);
                o0000o00.right = null;
            } else {
                O000000o(o0000o00, (O0000o00) null);
            }
            O00000Oo(o0000o005, false);
            this.size--;
            this.modCount++;
            return;
        }
        O0000o00 last = o0000o003.height > o0000o004.height ? o0000o003.last() : o0000o004.first();
        O000000o(last, false);
        O0000o00 o0000o006 = o0000o00.left;
        if (o0000o006 != null) {
            i = o0000o006.height;
            last.left = o0000o006;
            o0000o006.parent = last;
            o0000o00.left = null;
        } else {
            i = 0;
        }
        O0000o00 o0000o007 = o0000o00.right;
        if (o0000o007 != null) {
            i2 = o0000o007.height;
            last.right = o0000o007;
            o0000o007.parent = last;
            o0000o00.right = null;
        }
        last.height = Math.max(i, i2) + 1;
        O000000o(o0000o00, last);
    }

    public void clear() {
        Arrays.fill(this.table, null);
        this.size = 0;
        this.modCount++;
        O0000o00 o0000o00 = this.header;
        O0000o00 o0000o002 = o0000o00.next;
        while (o0000o002 != o0000o00) {
            O0000o00 o0000o003 = o0000o002.next;
            o0000o002.prev = null;
            o0000o002.next = null;
            o0000o002 = o0000o003;
        }
        o0000o00.prev = o0000o00;
        o0000o00.next = o0000o00;
    }

    public boolean containsKey(Object obj) {
        return findByObject(obj) != null;
    }

    public Set entrySet() {
        O0000Oo0 o0000Oo0 = this.entrySet;
        if (o0000Oo0 != null) {
            return o0000Oo0;
        }
        O0000Oo0 o0000Oo02 = new O0000Oo0(this);
        this.entrySet = o0000Oo02;
        return o0000Oo02;
    }

    /* access modifiers changed from: 0000 */
    public O0000o00 find(Object obj, boolean z) {
        int i;
        O0000o00 o0000o00;
        Comparator comparator2 = this.comparator;
        O0000o00[] o0000o00Arr = this.table;
        int O000Oo0 = O000Oo0(obj.hashCode());
        int length = (o0000o00Arr.length - 1) & O000Oo0;
        O0000o00 o0000o002 = o0000o00Arr[length];
        if (o0000o002 != null) {
            Comparable comparable = comparator2 == NATURAL_ORDER ? (Comparable) obj : null;
            while (true) {
                Object obj2 = o0000o002.key;
                i = comparable != null ? comparable.compareTo(obj2) : comparator2.compare(obj, obj2);
                if (i == 0) {
                    return o0000o002;
                }
                O0000o00 o0000o003 = i < 0 ? o0000o002.left : o0000o002.right;
                if (o0000o003 == null) {
                    break;
                }
                o0000o002 = o0000o003;
            }
        } else {
            i = 0;
        }
        O0000o00 o0000o004 = o0000o002;
        int i2 = i;
        if (!z) {
            return null;
        }
        O0000o00 o0000o005 = this.header;
        if (o0000o004 != null) {
            o0000o00 = new O0000o00(o0000o004, obj, O000Oo0, o0000o005, o0000o005.prev);
            if (i2 < 0) {
                o0000o004.left = o0000o00;
            } else {
                o0000o004.right = o0000o00;
            }
            O00000Oo(o0000o004, true);
        } else if (comparator2 != NATURAL_ORDER || (obj instanceof Comparable)) {
            o0000o00 = new O0000o00(o0000o004, obj, O000Oo0, o0000o005, o0000o005.prev);
            o0000o00Arr[length] = o0000o00;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(obj.getClass().getName());
            sb.append(" is not Comparable");
            throw new ClassCastException(sb.toString());
        }
        int i3 = this.size;
        this.size = i3 + 1;
        if (i3 > this.threshold) {
            doubleCapacity();
        }
        this.modCount++;
        return o0000o00;
    }

    /* access modifiers changed from: 0000 */
    public O0000o00 findByEntry(Entry entry) {
        O0000o00 findByObject = findByObject(entry.getKey());
        boolean z = findByObject != null && equal(findByObject.value, entry.getValue());
        if (z) {
            return findByObject;
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public O0000o00 findByObject(Object obj) {
        O0000o00 o0000o00 = null;
        if (obj == null) {
            return null;
        }
        try {
            o0000o00 = find(obj, false);
            return o0000o00;
        } catch (ClassCastException unused) {
            return o0000o00;
        }
    }

    public Object get(Object obj) {
        O0000o00 findByObject = findByObject(obj);
        if (findByObject != null) {
            return findByObject.value;
        }
        return null;
    }

    public Set keySet() {
        C0118O0000OoO o0000OoO = this.keySet;
        if (o0000OoO != null) {
            return o0000OoO;
        }
        C0118O0000OoO o0000OoO2 = new C0118O0000OoO(this);
        this.keySet = o0000OoO2;
        return o0000OoO2;
    }

    public Object put(Object obj, Object obj2) {
        if (obj != null) {
            O0000o00 find = find(obj, true);
            Object obj3 = find.value;
            find.value = obj2;
            return obj3;
        }
        throw new NullPointerException("key == null");
    }

    public Object remove(Object obj) {
        O0000o00 removeInternalByKey = removeInternalByKey(obj);
        if (removeInternalByKey != null) {
            return removeInternalByKey.value;
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public O0000o00 removeInternalByKey(Object obj) {
        O0000o00 findByObject = findByObject(obj);
        if (findByObject != null) {
            O000000o(findByObject, true);
        }
        return findByObject;
    }

    public int size() {
        return this.size;
    }
}
