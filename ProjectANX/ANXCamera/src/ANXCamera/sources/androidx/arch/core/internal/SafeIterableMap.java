package androidx.arch.core.internal;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.util.Iterator;
import java.util.WeakHashMap;

@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
public class SafeIterableMap implements Iterable {
    private Entry mEnd;
    private WeakHashMap mIterators = new WeakHashMap();
    private int mSize = 0;
    Entry mStart;

    class AscendingIterator extends ListIterator {
        AscendingIterator(Entry entry, Entry entry2) {
            super(entry, entry2);
        }

        /* access modifiers changed from: 0000 */
        public Entry backward(Entry entry) {
            return entry.mPrevious;
        }

        /* access modifiers changed from: 0000 */
        public Entry forward(Entry entry) {
            return entry.mNext;
        }
    }

    class DescendingIterator extends ListIterator {
        DescendingIterator(Entry entry, Entry entry2) {
            super(entry, entry2);
        }

        /* access modifiers changed from: 0000 */
        public Entry backward(Entry entry) {
            return entry.mNext;
        }

        /* access modifiers changed from: 0000 */
        public Entry forward(Entry entry) {
            return entry.mPrevious;
        }
    }

    class Entry implements java.util.Map.Entry {
        @NonNull
        final Object mKey;
        Entry mNext;
        Entry mPrevious;
        @NonNull
        final Object mValue;

        Entry(@NonNull Object obj, @NonNull Object obj2) {
            this.mKey = obj;
            this.mValue = obj2;
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            if (!this.mKey.equals(entry.mKey) || !this.mValue.equals(entry.mValue)) {
                z = false;
            }
            return z;
        }

        @NonNull
        public Object getKey() {
            return this.mKey;
        }

        @NonNull
        public Object getValue() {
            return this.mValue;
        }

        public int hashCode() {
            return this.mValue.hashCode() ^ this.mKey.hashCode();
        }

        public Object setValue(Object obj) {
            throw new UnsupportedOperationException("An entry modification is not supported");
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mKey);
            sb.append("=");
            sb.append(this.mValue);
            return sb.toString();
        }
    }

    class IteratorWithAdditions implements Iterator, SupportRemove {
        private boolean mBeforeStart = true;
        private Entry mCurrent;

        IteratorWithAdditions() {
        }

        public boolean hasNext() {
            boolean z = true;
            if (this.mBeforeStart) {
                if (SafeIterableMap.this.mStart == null) {
                    z = false;
                }
                return z;
            }
            Entry entry = this.mCurrent;
            if (entry == null || entry.mNext == null) {
                z = false;
            }
            return z;
        }

        public java.util.Map.Entry next() {
            Entry entry;
            if (this.mBeforeStart) {
                this.mBeforeStart = false;
                entry = SafeIterableMap.this.mStart;
            } else {
                Entry entry2 = this.mCurrent;
                entry = entry2 != null ? entry2.mNext : null;
            }
            this.mCurrent = entry;
            return this.mCurrent;
        }

        public void supportRemove(@NonNull Entry entry) {
            Entry entry2 = this.mCurrent;
            if (entry == entry2) {
                this.mCurrent = entry2.mPrevious;
                this.mBeforeStart = this.mCurrent == null;
            }
        }
    }

    abstract class ListIterator implements Iterator, SupportRemove {
        Entry mExpectedEnd;
        Entry mNext;

        ListIterator(Entry entry, Entry entry2) {
            this.mExpectedEnd = entry2;
            this.mNext = entry;
        }

        private Entry nextNode() {
            Entry entry = this.mNext;
            Entry entry2 = this.mExpectedEnd;
            if (entry == entry2 || entry2 == null) {
                return null;
            }
            return forward(entry);
        }

        public abstract Entry backward(Entry entry);

        public abstract Entry forward(Entry entry);

        public boolean hasNext() {
            return this.mNext != null;
        }

        public java.util.Map.Entry next() {
            Entry entry = this.mNext;
            this.mNext = nextNode();
            return entry;
        }

        public void supportRemove(@NonNull Entry entry) {
            if (this.mExpectedEnd == entry && entry == this.mNext) {
                this.mNext = null;
                this.mExpectedEnd = null;
            }
            Entry entry2 = this.mExpectedEnd;
            if (entry2 == entry) {
                this.mExpectedEnd = backward(entry2);
            }
            if (this.mNext == entry) {
                this.mNext = nextNode();
            }
        }
    }

    interface SupportRemove {
        void supportRemove(@NonNull Entry entry);
    }

    public Iterator descendingIterator() {
        DescendingIterator descendingIterator = new DescendingIterator(this.mEnd, this.mStart);
        this.mIterators.put(descendingIterator, Boolean.valueOf(false));
        return descendingIterator;
    }

    public java.util.Map.Entry eldest() {
        return this.mStart;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SafeIterableMap)) {
            return false;
        }
        SafeIterableMap safeIterableMap = (SafeIterableMap) obj;
        if (size() != safeIterableMap.size()) {
            return false;
        }
        Iterator it = iterator();
        Iterator it2 = safeIterableMap.iterator();
        while (it.hasNext() && it2.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
            Object next = it2.next();
            if ((entry == null && next != null) || (entry != null && !entry.equals(next))) {
                return false;
            }
        }
        if (it.hasNext() || it2.hasNext()) {
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public Entry get(Object obj) {
        Entry entry = this.mStart;
        while (entry != null && !entry.mKey.equals(obj)) {
            entry = entry.mNext;
        }
        return entry;
    }

    public int hashCode() {
        Iterator it = iterator();
        int i = 0;
        while (it.hasNext()) {
            i += ((java.util.Map.Entry) it.next()).hashCode();
        }
        return i;
    }

    @NonNull
    public Iterator iterator() {
        AscendingIterator ascendingIterator = new AscendingIterator(this.mStart, this.mEnd);
        this.mIterators.put(ascendingIterator, Boolean.valueOf(false));
        return ascendingIterator;
    }

    public IteratorWithAdditions iteratorWithAdditions() {
        IteratorWithAdditions iteratorWithAdditions = new IteratorWithAdditions();
        this.mIterators.put(iteratorWithAdditions, Boolean.valueOf(false));
        return iteratorWithAdditions;
    }

    public java.util.Map.Entry newest() {
        return this.mEnd;
    }

    /* access modifiers changed from: protected */
    public Entry put(@NonNull Object obj, @NonNull Object obj2) {
        Entry entry = new Entry(obj, obj2);
        this.mSize++;
        Entry entry2 = this.mEnd;
        if (entry2 == null) {
            this.mStart = entry;
            this.mEnd = this.mStart;
            return entry;
        }
        entry2.mNext = entry;
        entry.mPrevious = entry2;
        this.mEnd = entry;
        return entry;
    }

    public Object putIfAbsent(@NonNull Object obj, @NonNull Object obj2) {
        Entry entry = get(obj);
        if (entry != null) {
            return entry.mValue;
        }
        put(obj, obj2);
        return null;
    }

    public Object remove(@NonNull Object obj) {
        Entry entry = get(obj);
        if (entry == null) {
            return null;
        }
        this.mSize--;
        if (!this.mIterators.isEmpty()) {
            for (SupportRemove supportRemove : this.mIterators.keySet()) {
                supportRemove.supportRemove(entry);
            }
        }
        Entry entry2 = entry.mPrevious;
        if (entry2 != null) {
            entry2.mNext = entry.mNext;
        } else {
            this.mStart = entry.mNext;
        }
        Entry entry3 = entry.mNext;
        if (entry3 != null) {
            entry3.mPrevious = entry.mPrevious;
        } else {
            this.mEnd = entry.mPrevious;
        }
        entry.mNext = null;
        entry.mPrevious = null;
        return entry.mValue;
    }

    public int size() {
        return this.mSize;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Iterator it = iterator();
        while (it.hasNext()) {
            sb.append(((java.util.Map.Entry) it.next()).toString());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
