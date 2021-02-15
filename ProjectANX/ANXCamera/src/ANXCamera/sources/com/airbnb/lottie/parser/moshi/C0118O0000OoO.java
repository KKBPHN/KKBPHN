package com.airbnb.lottie.parser.moshi;

import java.util.AbstractSet;
import java.util.Iterator;

/* renamed from: com.airbnb.lottie.parser.moshi.O0000OoO reason: case insensitive filesystem */
final class C0118O0000OoO extends AbstractSet {
    final /* synthetic */ LinkedHashTreeMap this$0;

    C0118O0000OoO(LinkedHashTreeMap linkedHashTreeMap) {
        this.this$0 = linkedHashTreeMap;
    }

    public void clear() {
        this.this$0.clear();
    }

    public boolean contains(Object obj) {
        return this.this$0.containsKey(obj);
    }

    public Iterator iterator() {
        return new O0000Oo(this);
    }

    public boolean remove(Object obj) {
        return this.this$0.removeInternalByKey(obj) != null;
    }

    public int size() {
        return this.this$0.size;
    }
}
