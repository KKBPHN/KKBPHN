package io.reactivex.internal.util;

import io.reactivex.Observer;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Predicate;
import org.reactivestreams.Subscriber;

public class AppendOnlyLinkedArrayList {
    final int capacity;
    final Object[] head;
    int offset;
    Object[] tail = this.head;

    public interface NonThrowingPredicate extends Predicate {
        boolean test(Object obj);
    }

    public AppendOnlyLinkedArrayList(int i) {
        this.capacity = i;
        this.head = new Object[(i + 1)];
    }

    public boolean accept(Observer observer) {
        Object[] objArr = this.head;
        int i = this.capacity;
        while (true) {
            int i2 = 0;
            if (objArr == null) {
                return false;
            }
            while (i2 < i) {
                Object[] objArr2 = objArr[i2];
                if (objArr2 == null) {
                    continue;
                    break;
                } else if (NotificationLite.acceptFull((Object) objArr2, observer)) {
                    return true;
                } else {
                    i2++;
                }
            }
            objArr = objArr[i];
        }
    }

    public boolean accept(Subscriber subscriber) {
        Object[] objArr = this.head;
        int i = this.capacity;
        while (true) {
            int i2 = 0;
            if (objArr == null) {
                return false;
            }
            while (i2 < i) {
                Object[] objArr2 = objArr[i2];
                if (objArr2 == null) {
                    continue;
                    break;
                } else if (NotificationLite.acceptFull((Object) objArr2, subscriber)) {
                    return true;
                } else {
                    i2++;
                }
            }
            objArr = objArr[i];
        }
    }

    public void add(Object obj) {
        int i = this.capacity;
        int i2 = this.offset;
        if (i2 == i) {
            Object[] objArr = new Object[(i + 1)];
            this.tail[i] = objArr;
            this.tail = objArr;
            i2 = 0;
        }
        this.tail[i2] = obj;
        this.offset = i2 + 1;
    }

    public void forEachWhile(NonThrowingPredicate nonThrowingPredicate) {
        int i = this.capacity;
        for (Object[] objArr = this.head; objArr != null; objArr = objArr[i]) {
            for (int i2 = 0; i2 < i; i2++) {
                Object[] objArr2 = objArr[i2];
                if (objArr2 == null || nonThrowingPredicate.test(objArr2)) {
                    break;
                }
            }
        }
    }

    public void forEachWhile(Object obj, BiPredicate biPredicate) {
        Object[] objArr = this.head;
        int i = this.capacity;
        while (true) {
            int i2 = 0;
            while (i2 < i) {
                Object[] objArr2 = objArr[i2];
                if (objArr2 != null && !biPredicate.test(obj, objArr2)) {
                    i2++;
                } else {
                    return;
                }
            }
            objArr = objArr[i];
        }
    }

    public void setFirst(Object obj) {
        this.head[0] = obj;
    }
}
