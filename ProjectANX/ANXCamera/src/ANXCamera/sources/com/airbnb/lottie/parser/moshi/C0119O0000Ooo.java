package com.airbnb.lottie.parser.moshi;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* renamed from: com.airbnb.lottie.parser.moshi.O0000Ooo reason: case insensitive filesystem */
abstract class C0119O0000Ooo implements Iterator {
    int expectedModCount;
    O0000o00 lastReturned = null;
    O0000o00 next;
    final /* synthetic */ LinkedHashTreeMap this$0;

    C0119O0000Ooo(LinkedHashTreeMap linkedHashTreeMap) {
        this.this$0 = linkedHashTreeMap;
        LinkedHashTreeMap linkedHashTreeMap2 = this.this$0;
        this.next = linkedHashTreeMap2.header.next;
        this.expectedModCount = linkedHashTreeMap2.modCount;
    }

    public final boolean hasNext() {
        return this.next != this.this$0.header;
    }

    /* access modifiers changed from: 0000 */
    public final O0000o00 nextNode() {
        O0000o00 o0000o00 = this.next;
        LinkedHashTreeMap linkedHashTreeMap = this.this$0;
        if (o0000o00 == linkedHashTreeMap.header) {
            throw new NoSuchElementException();
        } else if (linkedHashTreeMap.modCount == this.expectedModCount) {
            this.next = o0000o00.next;
            this.lastReturned = o0000o00;
            return o0000o00;
        } else {
            throw new ConcurrentModificationException();
        }
    }

    public final void remove() {
        O0000o00 o0000o00 = this.lastReturned;
        if (o0000o00 != null) {
            this.this$0.O000000o(o0000o00, true);
            this.lastReturned = null;
            this.expectedModCount = this.this$0.modCount;
            return;
        }
        throw new IllegalStateException();
    }
}
