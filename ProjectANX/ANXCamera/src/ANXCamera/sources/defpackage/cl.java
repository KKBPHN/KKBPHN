package defpackage;

/* renamed from: cl reason: default package */
public final class cl {
    public int a;
    public int b;
    public int c;
    public int d;
    public int e;

    public cl() {
    }

    public cl(int i, int i2) {
        this();
        this.e = Integer.MAX_VALUE;
        this.a = i2;
        this.c = 0;
        this.d = 0;
    }

    public static int a(int i) {
        return (-(i & 1)) ^ (i >>> 1);
    }

    public static long a(long j) {
        return (-(j & 1)) ^ (j >>> 1);
    }
}
