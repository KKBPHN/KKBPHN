package defpackage;

import java.util.NoSuchElementException;

/* renamed from: cb reason: default package */
final class cb implements cf {
    final /* synthetic */ ck a;
    private int b;
    private final int c;

    public cb() {
    }

    public cb(ck ckVar) {
        this.a = ckVar;
        this();
        this.b = 0;
        this.c = this.a.a();
    }

    public byte a() {
        int i = this.b;
        if (i < this.c) {
            this.b = i + 1;
            return this.a.b(i);
        }
        throw new NoSuchElementException();
    }

    public boolean hasNext() {
        return this.b < this.c;
    }

    public final /* bridge */ /* synthetic */ Object next() {
        return Byte.valueOf(a());
    }

    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
