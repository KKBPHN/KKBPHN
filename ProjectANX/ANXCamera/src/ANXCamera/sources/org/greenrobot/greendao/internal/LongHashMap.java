package org.greenrobot.greendao.internal;

import java.util.Arrays;
import org.greenrobot.greendao.DaoLog;

public final class LongHashMap {
    private int capacity;
    private int size;
    private Entry[] table;
    private int threshold;

    final class Entry {
        final long key;
        Entry next;
        Object value;

        Entry(long j, Object obj, Entry entry) {
            this.key = j;
            this.value = obj;
            this.next = entry;
        }
    }

    public LongHashMap() {
        this(16);
    }

    public LongHashMap(int i) {
        this.capacity = i;
        this.threshold = (i * 4) / 3;
        this.table = new Entry[i];
    }

    public void clear() {
        this.size = 0;
        Arrays.fill(this.table, null);
    }

    public boolean containsKey(long j) {
        for (Entry entry = this.table[((((int) (j >>> 32)) ^ ((int) j)) & Integer.MAX_VALUE) % this.capacity]; entry != null; entry = entry.next) {
            if (entry.key == j) {
                return true;
            }
        }
        return false;
    }

    public Object get(long j) {
        for (Entry entry = this.table[((((int) (j >>> 32)) ^ ((int) j)) & Integer.MAX_VALUE) % this.capacity]; entry != null; entry = entry.next) {
            if (entry.key == j) {
                return entry.value;
            }
        }
        return null;
    }

    public void logStats() {
        Entry[] entryArr;
        int i = 0;
        for (Entry entry : this.table) {
            while (entry != null) {
                entry = entry.next;
                if (entry == null) {
                    break;
                }
                i++;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("load: ");
        sb.append(((float) this.size) / ((float) this.capacity));
        sb.append(", size: ");
        sb.append(this.size);
        sb.append(", capa: ");
        sb.append(this.capacity);
        sb.append(", collisions: ");
        sb.append(i);
        sb.append(", collision ratio: ");
        sb.append(((float) i) / ((float) this.size));
        DaoLog.d(sb.toString());
    }

    public Object put(long j, Object obj) {
        int i = ((((int) (j >>> 32)) ^ ((int) j)) & Integer.MAX_VALUE) % this.capacity;
        Entry entry = this.table[i];
        for (Entry entry2 = entry; entry2 != null; entry2 = entry2.next) {
            if (entry2.key == j) {
                Object obj2 = entry2.value;
                entry2.value = obj;
                return obj2;
            }
        }
        this.table[i] = new Entry(j, obj, entry);
        this.size++;
        if (this.size > this.threshold) {
            setCapacity(this.capacity * 2);
        }
        return null;
    }

    public Object remove(long j) {
        int i = ((((int) (j >>> 32)) ^ ((int) j)) & Integer.MAX_VALUE) % this.capacity;
        Entry entry = this.table[i];
        Entry entry2 = null;
        while (entry != null) {
            Entry entry3 = entry.next;
            if (entry.key == j) {
                if (entry2 == null) {
                    this.table[i] = entry3;
                } else {
                    entry2.next = entry3;
                }
                this.size--;
                return entry.value;
            }
            entry2 = entry;
            entry = entry3;
        }
        return null;
    }

    public void reserveRoom(int i) {
        setCapacity((i * 5) / 3);
    }

    public void setCapacity(int i) {
        Entry[] entryArr = new Entry[i];
        for (Entry entry : this.table) {
            while (entry != null) {
                long j = entry.key;
                int i2 = ((((int) j) ^ ((int) (j >>> 32))) & Integer.MAX_VALUE) % i;
                Entry entry2 = entry.next;
                entry.next = entryArr[i2];
                entryArr[i2] = entry;
                entry = entry2;
            }
        }
        this.table = entryArr;
        this.capacity = i;
        this.threshold = (i * 4) / 3;
    }

    public int size() {
        return this.size;
    }
}
