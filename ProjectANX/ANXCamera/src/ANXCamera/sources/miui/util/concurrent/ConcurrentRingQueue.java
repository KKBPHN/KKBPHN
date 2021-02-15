package miui.util.concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import miui.util.concurrent.Queue.Predicate;

public class ConcurrentRingQueue implements Queue {
    private volatile int mAdditional;
    private final boolean mAllowExtendCapacity;
    private final boolean mAutoReleaseCapacity;
    private int mCapacity;
    private volatile Node mReadCursor = new Node();
    private final AtomicInteger mReadLock = new AtomicInteger(0);
    private volatile Node mWriteCursor = this.mReadCursor;
    private final AtomicInteger mWriteLock = new AtomicInteger(0);

    class Node {
        Object element;
        Node next;

        private Node() {
        }
    }

    public ConcurrentRingQueue(int i, boolean z, boolean z2) {
        this.mCapacity = i;
        this.mAllowExtendCapacity = z;
        this.mAutoReleaseCapacity = z2;
        Node node = this.mReadCursor;
        for (int i2 = 0; i2 < i; i2++) {
            node.next = new Node();
            node = node.next;
        }
        node.next = this.mReadCursor;
    }

    public int clear() {
        while (true) {
            if (this.mReadLock.get() == 0 && this.mReadLock.compareAndSet(0, -1)) {
                break;
            }
            Thread.yield();
        }
        Node node = this.mReadCursor;
        int i = 0;
        while (node != this.mWriteCursor) {
            node.element = null;
            i++;
            node = node.next;
        }
        this.mReadCursor = node;
        this.mReadLock.set(0);
        return i;
    }

    public void decreaseCapacity(int i) {
        if (this.mAutoReleaseCapacity && i > 0) {
            while (true) {
                if (this.mWriteLock.get() != 0 || !this.mWriteLock.compareAndSet(0, -1)) {
                    Thread.yield();
                } else {
                    this.mCapacity -= i;
                    this.mAdditional = i;
                    this.mWriteLock.set(0);
                    return;
                }
            }
        }
    }

    public Object get() {
        while (true) {
            if (this.mReadLock.get() == 0 && this.mReadLock.compareAndSet(0, -1)) {
                break;
            }
            Thread.yield();
        }
        Node node = this.mReadCursor;
        Node node2 = this.mWriteCursor;
        Node node3 = node;
        Object obj = null;
        while (obj == null && node3 != node2) {
            obj = node3.element;
            node3.element = null;
            node3 = node3.next;
            node2 = this.mWriteCursor;
        }
        if (obj != null) {
            this.mReadCursor = node3;
        }
        this.mReadLock.set(0);
        return obj;
    }

    public int getCapacity() {
        int i = this.mAdditional;
        int i2 = this.mCapacity;
        return i > 0 ? i2 + i : i2;
    }

    public void increaseCapacity(int i) {
        if (!this.mAllowExtendCapacity && i > 0) {
            while (true) {
                if (this.mWriteLock.get() != 0 || !this.mWriteLock.compareAndSet(0, -1)) {
                    Thread.yield();
                } else {
                    this.mAdditional = -i;
                    this.mCapacity += i;
                    this.mWriteLock.set(0);
                    return;
                }
            }
        }
    }

    public boolean isEmpty() {
        return this.mWriteCursor == this.mReadCursor;
    }

    public boolean put(Object obj) {
        int i;
        if (obj == null) {
            return false;
        }
        while (true) {
            if (this.mWriteLock.get() == 0 && this.mWriteLock.compareAndSet(0, -1)) {
                break;
            }
            Thread.yield();
        }
        Node node = this.mReadCursor;
        Node node2 = this.mWriteCursor;
        int i2 = this.mAdditional;
        Node node3 = node2.next;
        boolean z = true;
        if (node3 != node) {
            node2.element = obj;
            Node node4 = node3.next;
            if (node4 != node && this.mAutoReleaseCapacity && i2 > 0) {
                node2.next = node4;
                i = i2 - 1;
            }
            this.mWriteCursor = node2.next;
            this.mWriteLock.set(0);
            return z;
        } else if (this.mAllowExtendCapacity || i2 < 0) {
            node2.next = new Node();
            node2.next.next = node;
            node2.element = obj;
            i = i2 + 1;
        } else {
            z = false;
            this.mWriteLock.set(0);
            return z;
        }
        this.mAdditional = i;
        this.mWriteCursor = node2.next;
        this.mWriteLock.set(0);
        return z;
    }

    public int remove(Predicate predicate) {
        if (predicate == null) {
            return 0;
        }
        while (true) {
            if (this.mReadLock.get() != 0 || !this.mReadLock.compareAndSet(0, -1)) {
                Thread.yield();
            } else {
                try {
                    break;
                } finally {
                    this.mReadLock.set(0);
                }
            }
        }
        int i = 0;
        for (Node node = this.mReadCursor; node != this.mWriteCursor; node = node.next) {
            if (predicate.apply(node.element)) {
                node.element = null;
                i++;
            }
        }
        return i;
    }

    public boolean remove(Object obj) {
        boolean z;
        if (obj == null) {
            return false;
        }
        while (true) {
            if (this.mReadLock.get() == 0 && this.mReadLock.compareAndSet(0, -1)) {
                break;
            }
            Thread.yield();
        }
        Node node = this.mReadCursor;
        while (true) {
            if (node == this.mWriteCursor) {
                z = false;
                break;
            } else if (obj.equals(node.element)) {
                node.element = null;
                z = true;
                break;
            } else {
                node = node.next;
            }
        }
        this.mReadLock.set(0);
        return z;
    }
}
