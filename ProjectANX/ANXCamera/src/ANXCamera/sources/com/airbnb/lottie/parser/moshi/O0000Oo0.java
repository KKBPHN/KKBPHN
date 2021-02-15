package com.airbnb.lottie.parser.moshi;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map.Entry;

final class O0000Oo0 extends AbstractSet {
    final /* synthetic */ LinkedHashTreeMap this$0;

    O0000Oo0(LinkedHashTreeMap linkedHashTreeMap) {
        this.this$0 = linkedHashTreeMap;
    }

    public void clear() {
        this.this$0.clear();
    }

    public boolean contains(Object obj) {
        return (obj instanceof Entry) && this.this$0.findByEntry((Entry) obj) != null;
    }

    public Iterator iterator() {
        return new O0000OOo(this);
    }

    public boolean remove(Object obj) {
        if (!(obj instanceof Entry)) {
            return false;
        }
        O0000o00 findByEntry = this.this$0.findByEntry((Entry) obj);
        if (findByEntry == null) {
            return false;
        }
        this.this$0.O000000o(findByEntry, true);
        return true;
    }

    public int size() {
        return this.this$0.size;
    }
}
