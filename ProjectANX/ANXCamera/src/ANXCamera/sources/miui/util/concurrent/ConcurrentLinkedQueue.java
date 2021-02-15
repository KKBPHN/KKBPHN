package miui.util.concurrent;

import java.util.Iterator;
import miui.util.concurrent.Queue.Predicate;

public class ConcurrentLinkedQueue implements Queue {
    private final java.util.concurrent.ConcurrentLinkedQueue mQueue = new java.util.concurrent.ConcurrentLinkedQueue();

    public int clear() {
        int size = this.mQueue.size();
        this.mQueue.clear();
        return size;
    }

    public Object get() {
        return this.mQueue.poll();
    }

    public int getCapacity() {
        return -1;
    }

    public boolean isEmpty() {
        return this.mQueue.isEmpty();
    }

    public boolean put(Object obj) {
        return this.mQueue.offer(obj);
    }

    public int remove(Predicate predicate) {
        Iterator it = this.mQueue.iterator();
        int i = 0;
        while (it.hasNext()) {
            if (predicate.apply(it.next())) {
                it.remove();
                i++;
            }
        }
        return i;
    }

    public boolean remove(Object obj) {
        return this.mQueue.remove(obj);
    }
}
