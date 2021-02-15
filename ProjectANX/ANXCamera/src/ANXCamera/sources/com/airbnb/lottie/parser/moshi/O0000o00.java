package com.airbnb.lottie.parser.moshi;

import java.util.Map.Entry;

final class O0000o00 implements Entry {
    final int hash;
    int height;
    final Object key;
    O0000o00 left;
    O0000o00 next;
    O0000o00 parent;
    O0000o00 prev;
    O0000o00 right;
    Object value;

    O0000o00() {
        this.key = null;
        this.hash = -1;
        this.prev = this;
        this.next = this;
    }

    O0000o00(O0000o00 o0000o00, Object obj, int i, O0000o00 o0000o002, O0000o00 o0000o003) {
        this.parent = o0000o00;
        this.key = obj;
        this.hash = i;
        this.height = 1;
        this.next = o0000o002;
        this.prev = o0000o003;
        o0000o003.next = this;
        o0000o002.prev = this;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Entry)) {
            return false;
        }
        Entry entry = (Entry) obj;
        Object obj2 = this.key;
        if (obj2 == null) {
            if (entry.getKey() != null) {
                return false;
            }
        } else if (!obj2.equals(entry.getKey())) {
            return false;
        }
        Object obj3 = this.value;
        if (obj3 == null) {
            if (entry.getValue() != null) {
                return false;
            }
        } else if (!obj3.equals(entry.getValue())) {
            return false;
        }
        return true;
    }

    public O0000o00 first() {
        while (true) {
            O0000o00 o0000o00 = this;
            this = this.left;
            if (this == null) {
                return o0000o00;
            }
        }
    }

    public Object getKey() {
        return this.key;
    }

    public Object getValue() {
        return this.value;
    }

    public int hashCode() {
        Object obj = this.key;
        int i = 0;
        int hashCode = obj == null ? 0 : obj.hashCode();
        Object obj2 = this.value;
        if (obj2 != null) {
            i = obj2.hashCode();
        }
        return hashCode ^ i;
    }

    public O0000o00 last() {
        while (true) {
            O0000o00 o0000o00 = this;
            this = this.right;
            if (this == null) {
                return o0000o00;
            }
        }
    }

    public Object setValue(Object obj) {
        Object obj2 = this.value;
        this.value = obj;
        return obj2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.key);
        sb.append("=");
        sb.append(this.value);
        return sb.toString();
    }
}
