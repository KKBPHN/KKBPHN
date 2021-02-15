package defpackage;

import java.util.Iterator;
import java.util.NoSuchElementException;

/* renamed from: ew reason: default package */
final class ew implements Iterator {
    public final boolean hasNext() {
        return false;
    }

    public final Object next() {
        throw new NoSuchElementException();
    }

    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
