package defpackage;

/* renamed from: er reason: default package */
final class er implements ee {
    public final eh a;
    public final String b;
    public final Object[] c;
    private final int d;

    public er(eh ehVar, String str, Object[] objArr) {
        this.a = ehVar;
        this.b = str;
        this.c = objArr;
        char charAt = str.charAt(0);
        if (charAt >= 55296) {
            char c2 = charAt & 8191;
            int i = 13;
            int i2 = 1;
            while (true) {
                int i3 = i2 + 1;
                char charAt2 = str.charAt(i2);
                if (charAt2 >= 55296) {
                    c2 |= (charAt2 & 8191) << i;
                    i += 13;
                    i2 = i3;
                } else {
                    this.d = c2 | (charAt2 << i);
                    return;
                }
            }
        } else {
            this.d = charAt;
        }
    }

    public final boolean a() {
        return (this.d & 2) == 2;
    }

    public final eh b() {
        return this.a;
    }

    public final int c() {
        return (this.d & 1) == 1 ? 1 : 2;
    }
}
