package io.reactivex.subjects;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ReplaySubject extends Subject {
    static final ReplayDisposable[] EMPTY = new ReplayDisposable[0];
    private static final Object[] EMPTY_ARRAY = new Object[0];
    static final ReplayDisposable[] TERMINATED = new ReplayDisposable[0];
    final ReplayBuffer buffer;
    boolean done;
    final AtomicReference observers = new AtomicReference(EMPTY);

    final class Node extends AtomicReference {
        private static final long serialVersionUID = 6404226426336033100L;
        final Object value;

        Node(Object obj) {
            this.value = obj;
        }
    }

    interface ReplayBuffer {
        void add(Object obj);

        void addFinal(Object obj);

        boolean compareAndSet(Object obj, Object obj2);

        Object get();

        Object getValue();

        Object[] getValues(Object[] objArr);

        void replay(ReplayDisposable replayDisposable);

        int size();
    }

    final class ReplayDisposable extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = 466549804534799122L;
        final Observer actual;
        volatile boolean cancelled;
        Object index;
        final ReplaySubject state;

        ReplayDisposable(Observer observer, ReplaySubject replaySubject) {
            this.actual = observer;
            this.state = replaySubject;
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.state.remove(this);
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }
    }

    final class SizeAndTimeBoundReplayBuffer extends AtomicReference implements ReplayBuffer {
        private static final long serialVersionUID = -8056260896137901749L;
        volatile boolean done;
        volatile TimedNode head;
        final long maxAge;
        final int maxSize;
        final Scheduler scheduler;
        int size;
        TimedNode tail;
        final TimeUnit unit;

        SizeAndTimeBoundReplayBuffer(int i, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            ObjectHelper.verifyPositive(i, "maxSize");
            this.maxSize = i;
            ObjectHelper.verifyPositive(j, "maxAge");
            this.maxAge = j;
            ObjectHelper.requireNonNull(timeUnit, "unit is null");
            this.unit = timeUnit;
            ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
            this.scheduler = scheduler2;
            TimedNode timedNode = new TimedNode(null, 0);
            this.tail = timedNode;
            this.head = timedNode;
        }

        public void add(Object obj) {
            TimedNode timedNode = new TimedNode(obj, this.scheduler.now(this.unit));
            TimedNode timedNode2 = this.tail;
            this.tail = timedNode;
            this.size++;
            timedNode2.set(timedNode);
            trim();
        }

        public void addFinal(Object obj) {
            TimedNode timedNode = new TimedNode(obj, Long.MAX_VALUE);
            TimedNode timedNode2 = this.tail;
            this.tail = timedNode;
            this.size++;
            timedNode2.lazySet(timedNode);
            trimFinal();
            this.done = true;
        }

        /* access modifiers changed from: 0000 */
        public TimedNode getHead() {
            TimedNode timedNode;
            TimedNode timedNode2 = this.head;
            long now = this.scheduler.now(this.unit) - this.maxAge;
            do {
                TimedNode timedNode3 = timedNode2;
                timedNode2 = (TimedNode) timedNode2.get();
                timedNode = timedNode3;
                if (timedNode2 == null) {
                    break;
                }
            } while (timedNode2.time <= now);
            return timedNode;
        }

        public Object getValue() {
            TimedNode timedNode = this.head;
            TimedNode timedNode2 = null;
            while (true) {
                TimedNode timedNode3 = (TimedNode) timedNode.get();
                if (timedNode3 == null) {
                    break;
                }
                timedNode2 = timedNode;
                timedNode = timedNode3;
            }
            if (timedNode.time < this.scheduler.now(this.unit) - this.maxAge) {
                return null;
            }
            Object obj = timedNode.value;
            if (obj == null) {
                return null;
            }
            return (NotificationLite.isComplete(obj) || NotificationLite.isError(obj)) ? timedNode2.value : obj;
        }

        public Object[] getValues(Object[] objArr) {
            TimedNode head2 = getHead();
            int size2 = size(head2);
            if (size2 != 0) {
                if (objArr.length < size2) {
                    objArr = (Object[]) Array.newInstance(objArr.getClass().getComponentType(), size2);
                }
                for (int i = 0; i != size2; i++) {
                    head2 = (TimedNode) head2.get();
                    objArr[i] = head2.value;
                }
                if (objArr.length > size2) {
                    objArr[size2] = null;
                }
            } else if (objArr.length != 0) {
                objArr[0] = null;
            }
            return objArr;
        }

        public void replay(ReplayDisposable replayDisposable) {
            if (replayDisposable.getAndIncrement() == 0) {
                Observer observer = replayDisposable.actual;
                TimedNode timedNode = (TimedNode) replayDisposable.index;
                if (timedNode == null) {
                    timedNode = getHead();
                }
                int i = 1;
                while (!replayDisposable.cancelled) {
                    while (!replayDisposable.cancelled) {
                        TimedNode timedNode2 = (TimedNode) timedNode.get();
                        if (timedNode2 != null) {
                            Object obj = timedNode2.value;
                            if (!this.done || timedNode2.get() != null) {
                                observer.onNext(obj);
                                timedNode = timedNode2;
                            } else {
                                if (NotificationLite.isComplete(obj)) {
                                    observer.onComplete();
                                } else {
                                    observer.onError(NotificationLite.getError(obj));
                                }
                                replayDisposable.index = null;
                                replayDisposable.cancelled = true;
                                return;
                            }
                        } else if (timedNode.get() == null) {
                            replayDisposable.index = timedNode;
                            i = replayDisposable.addAndGet(-i);
                            if (i == 0) {
                                return;
                            }
                        }
                    }
                    replayDisposable.index = null;
                    return;
                }
                replayDisposable.index = null;
            }
        }

        public int size() {
            return size(getHead());
        }

        /* access modifiers changed from: 0000 */
        public int size(TimedNode timedNode) {
            int i = 0;
            while (i != Integer.MAX_VALUE) {
                TimedNode timedNode2 = (TimedNode) timedNode.get();
                if (timedNode2 == null) {
                    Object obj = timedNode.value;
                    return (NotificationLite.isComplete(obj) || NotificationLite.isError(obj)) ? i - 1 : i;
                }
                i++;
                timedNode = timedNode2;
            }
            return i;
        }

        /* access modifiers changed from: 0000 */
        public void trim() {
            int i = this.size;
            if (i > this.maxSize) {
                this.size = i - 1;
                this.head = (TimedNode) this.head.get();
            }
            long now = this.scheduler.now(this.unit) - this.maxAge;
            TimedNode timedNode = this.head;
            while (true) {
                TimedNode timedNode2 = (TimedNode) timedNode.get();
                if (timedNode2 != null && timedNode2.time <= now) {
                    timedNode = timedNode2;
                }
            }
            this.head = timedNode;
        }

        /* access modifiers changed from: 0000 */
        public void trimFinal() {
            long now = this.scheduler.now(this.unit) - this.maxAge;
            TimedNode timedNode = this.head;
            while (true) {
                TimedNode timedNode2 = (TimedNode) timedNode.get();
                if (timedNode2.get() != null && timedNode2.time <= now) {
                    timedNode = timedNode2;
                }
            }
            this.head = timedNode;
        }
    }

    final class SizeBoundReplayBuffer extends AtomicReference implements ReplayBuffer {
        private static final long serialVersionUID = 1107649250281456395L;
        volatile boolean done;
        volatile Node head;
        final int maxSize;
        int size;
        Node tail;

        SizeBoundReplayBuffer(int i) {
            ObjectHelper.verifyPositive(i, "maxSize");
            this.maxSize = i;
            Node node = new Node(null);
            this.tail = node;
            this.head = node;
        }

        public void add(Object obj) {
            Node node = new Node(obj);
            Node node2 = this.tail;
            this.tail = node;
            this.size++;
            node2.set(node);
            trim();
        }

        public void addFinal(Object obj) {
            Node node = new Node(obj);
            Node node2 = this.tail;
            this.tail = node;
            this.size++;
            node2.lazySet(node);
            this.done = true;
        }

        public Object getValue() {
            Node node = this.head;
            Node node2 = null;
            while (true) {
                Node node3 = (Node) node.get();
                if (node3 == null) {
                    break;
                }
                node2 = node;
                node = node3;
            }
            Object obj = node.value;
            if (obj == null) {
                return null;
            }
            return (NotificationLite.isComplete(obj) || NotificationLite.isError(obj)) ? node2.value : obj;
        }

        public Object[] getValues(Object[] objArr) {
            Node node = this.head;
            int size2 = size();
            if (size2 != 0) {
                if (objArr.length < size2) {
                    objArr = (Object[]) Array.newInstance(objArr.getClass().getComponentType(), size2);
                }
                for (int i = 0; i != size2; i++) {
                    node = (Node) node.get();
                    objArr[i] = node.value;
                }
                if (objArr.length > size2) {
                    objArr[size2] = null;
                }
            } else if (objArr.length != 0) {
                objArr[0] = null;
            }
            return objArr;
        }

        public void replay(ReplayDisposable replayDisposable) {
            if (replayDisposable.getAndIncrement() == 0) {
                Observer observer = replayDisposable.actual;
                Node node = (Node) replayDisposable.index;
                if (node == null) {
                    node = this.head;
                }
                int i = 1;
                while (!replayDisposable.cancelled) {
                    Node node2 = (Node) node.get();
                    if (node2 != null) {
                        Object obj = node2.value;
                        if (!this.done || node2.get() != null) {
                            observer.onNext(obj);
                            node = node2;
                        } else {
                            if (NotificationLite.isComplete(obj)) {
                                observer.onComplete();
                            } else {
                                observer.onError(NotificationLite.getError(obj));
                            }
                            replayDisposable.index = null;
                            replayDisposable.cancelled = true;
                            return;
                        }
                    } else if (node.get() == null) {
                        replayDisposable.index = node;
                        i = replayDisposable.addAndGet(-i);
                        if (i == 0) {
                            return;
                        }
                    }
                }
                replayDisposable.index = null;
            }
        }

        public int size() {
            Node node = this.head;
            int i = 0;
            while (i != Integer.MAX_VALUE) {
                Node node2 = (Node) node.get();
                if (node2 == null) {
                    Object obj = node.value;
                    return (NotificationLite.isComplete(obj) || NotificationLite.isError(obj)) ? i - 1 : i;
                }
                i++;
                node = node2;
            }
            return i;
        }

        /* access modifiers changed from: 0000 */
        public void trim() {
            int i = this.size;
            if (i > this.maxSize) {
                this.size = i - 1;
                this.head = (Node) this.head.get();
            }
        }
    }

    final class TimedNode extends AtomicReference {
        private static final long serialVersionUID = 6404226426336033100L;
        final long time;
        final Object value;

        TimedNode(Object obj, long j) {
            this.value = obj;
            this.time = j;
        }
    }

    final class UnboundedReplayBuffer extends AtomicReference implements ReplayBuffer {
        private static final long serialVersionUID = -733876083048047795L;
        final List buffer;
        volatile boolean done;
        volatile int size;

        UnboundedReplayBuffer(int i) {
            ObjectHelper.verifyPositive(i, "capacityHint");
            this.buffer = new ArrayList(i);
        }

        public void add(Object obj) {
            this.buffer.add(obj);
            this.size++;
        }

        public void addFinal(Object obj) {
            this.buffer.add(obj);
            this.size++;
            this.done = true;
        }

        public Object getValue() {
            int i = this.size;
            if (i == 0) {
                return null;
            }
            List list = this.buffer;
            Object obj = list.get(i - 1);
            if (!NotificationLite.isComplete(obj) && !NotificationLite.isError(obj)) {
                return obj;
            }
            if (i == 1) {
                return null;
            }
            return list.get(i - 2);
        }

        public Object[] getValues(Object[] objArr) {
            int i = this.size;
            if (i == 0) {
                if (objArr.length != 0) {
                    objArr[0] = null;
                }
                return objArr;
            }
            List list = this.buffer;
            Object obj = list.get(i - 1);
            if (NotificationLite.isComplete(obj) || NotificationLite.isError(obj)) {
                i--;
                if (i == 0) {
                    if (objArr.length != 0) {
                        objArr[0] = null;
                    }
                    return objArr;
                }
            }
            if (objArr.length < i) {
                objArr = (Object[]) Array.newInstance(objArr.getClass().getComponentType(), i);
            }
            for (int i2 = 0; i2 < i; i2++) {
                objArr[i2] = list.get(i2);
            }
            if (objArr.length > i) {
                objArr[i] = null;
            }
            return objArr;
        }

        public void replay(ReplayDisposable replayDisposable) {
            if (replayDisposable.getAndIncrement() == 0) {
                List list = this.buffer;
                Observer observer = replayDisposable.actual;
                Integer num = (Integer) replayDisposable.index;
                int i = 0;
                if (num != null) {
                    i = num.intValue();
                } else {
                    replayDisposable.index = Integer.valueOf(0);
                }
                int i2 = 1;
                while (!replayDisposable.cancelled) {
                    int i3 = this.size;
                    while (i3 != i) {
                        if (replayDisposable.cancelled) {
                            replayDisposable.index = null;
                            return;
                        }
                        Object obj = list.get(i);
                        if (this.done) {
                            int i4 = i + 1;
                            if (i4 == i3) {
                                i3 = this.size;
                                if (i4 == i3) {
                                    if (NotificationLite.isComplete(obj)) {
                                        observer.onComplete();
                                    } else {
                                        observer.onError(NotificationLite.getError(obj));
                                    }
                                    replayDisposable.index = null;
                                    replayDisposable.cancelled = true;
                                    return;
                                }
                            } else {
                                continue;
                            }
                        }
                        observer.onNext(obj);
                        i++;
                    }
                    if (i == this.size) {
                        replayDisposable.index = Integer.valueOf(i);
                        i2 = replayDisposable.addAndGet(-i2);
                        if (i2 == 0) {
                            return;
                        }
                    }
                }
                replayDisposable.index = null;
            }
        }

        public int size() {
            int i = this.size;
            if (i == 0) {
                return 0;
            }
            int i2 = i - 1;
            Object obj = this.buffer.get(i2);
            return (NotificationLite.isComplete(obj) || NotificationLite.isError(obj)) ? i2 : i;
        }
    }

    ReplaySubject(ReplayBuffer replayBuffer) {
        this.buffer = replayBuffer;
    }

    @CheckReturnValue
    public static ReplaySubject create() {
        return new ReplaySubject(new UnboundedReplayBuffer(16));
    }

    @CheckReturnValue
    public static ReplaySubject create(int i) {
        return new ReplaySubject(new UnboundedReplayBuffer(i));
    }

    static ReplaySubject createUnbounded() {
        return new ReplaySubject(new SizeBoundReplayBuffer(Integer.MAX_VALUE));
    }

    @CheckReturnValue
    public static ReplaySubject createWithSize(int i) {
        return new ReplaySubject(new SizeBoundReplayBuffer(i));
    }

    @CheckReturnValue
    public static ReplaySubject createWithTime(long j, TimeUnit timeUnit, Scheduler scheduler) {
        SizeAndTimeBoundReplayBuffer sizeAndTimeBoundReplayBuffer = new SizeAndTimeBoundReplayBuffer(Integer.MAX_VALUE, j, timeUnit, scheduler);
        return new ReplaySubject(sizeAndTimeBoundReplayBuffer);
    }

    @CheckReturnValue
    public static ReplaySubject createWithTimeAndSize(long j, TimeUnit timeUnit, Scheduler scheduler, int i) {
        SizeAndTimeBoundReplayBuffer sizeAndTimeBoundReplayBuffer = new SizeAndTimeBoundReplayBuffer(i, j, timeUnit, scheduler);
        return new ReplaySubject(sizeAndTimeBoundReplayBuffer);
    }

    /* access modifiers changed from: 0000 */
    public boolean add(ReplayDisposable replayDisposable) {
        ReplayDisposable[] replayDisposableArr;
        ReplayDisposable[] replayDisposableArr2;
        do {
            replayDisposableArr = (ReplayDisposable[]) this.observers.get();
            if (replayDisposableArr == TERMINATED) {
                return false;
            }
            int length = replayDisposableArr.length;
            replayDisposableArr2 = new ReplayDisposable[(length + 1)];
            System.arraycopy(replayDisposableArr, 0, replayDisposableArr2, 0, length);
            replayDisposableArr2[length] = replayDisposable;
        } while (!this.observers.compareAndSet(replayDisposableArr, replayDisposableArr2));
        return true;
    }

    public Throwable getThrowable() {
        Object obj = this.buffer.get();
        if (NotificationLite.isError(obj)) {
            return NotificationLite.getError(obj);
        }
        return null;
    }

    public Object getValue() {
        return this.buffer.getValue();
    }

    public Object[] getValues() {
        Object[] values = getValues(EMPTY_ARRAY);
        return values == EMPTY_ARRAY ? new Object[0] : values;
    }

    public Object[] getValues(Object[] objArr) {
        return this.buffer.getValues(objArr);
    }

    public boolean hasComplete() {
        return NotificationLite.isComplete(this.buffer.get());
    }

    public boolean hasObservers() {
        return ((ReplayDisposable[]) this.observers.get()).length != 0;
    }

    public boolean hasThrowable() {
        return NotificationLite.isError(this.buffer.get());
    }

    public boolean hasValue() {
        return this.buffer.size() != 0;
    }

    /* access modifiers changed from: 0000 */
    public int observerCount() {
        return ((ReplayDisposable[]) this.observers.get()).length;
    }

    public void onComplete() {
        if (!this.done) {
            this.done = true;
            Object complete = NotificationLite.complete();
            ReplayBuffer replayBuffer = this.buffer;
            replayBuffer.addFinal(complete);
            for (ReplayDisposable replay : terminate(complete)) {
                replayBuffer.replay(replay);
            }
        }
    }

    public void onError(Throwable th) {
        ObjectHelper.requireNonNull(th, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (this.done) {
            RxJavaPlugins.onError(th);
            return;
        }
        this.done = true;
        Object error = NotificationLite.error(th);
        ReplayBuffer replayBuffer = this.buffer;
        replayBuffer.addFinal(error);
        for (ReplayDisposable replay : terminate(error)) {
            replayBuffer.replay(replay);
        }
    }

    public void onNext(Object obj) {
        ObjectHelper.requireNonNull(obj, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (!this.done) {
            ReplayBuffer replayBuffer = this.buffer;
            replayBuffer.add(obj);
            for (ReplayDisposable replay : (ReplayDisposable[]) this.observers.get()) {
                replayBuffer.replay(replay);
            }
        }
    }

    public void onSubscribe(Disposable disposable) {
        if (this.done) {
            disposable.dispose();
        }
    }

    /* access modifiers changed from: 0000 */
    public void remove(ReplayDisposable replayDisposable) {
        ReplayDisposable[] replayDisposableArr;
        ReplayDisposable[] replayDisposableArr2;
        do {
            replayDisposableArr = (ReplayDisposable[]) this.observers.get();
            if (replayDisposableArr != TERMINATED && replayDisposableArr != EMPTY) {
                int length = replayDisposableArr.length;
                int i = -1;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    } else if (replayDisposableArr[i2] == replayDisposable) {
                        i = i2;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (i >= 0) {
                    if (length == 1) {
                        replayDisposableArr2 = EMPTY;
                    } else {
                        ReplayDisposable[] replayDisposableArr3 = new ReplayDisposable[(length - 1)];
                        System.arraycopy(replayDisposableArr, 0, replayDisposableArr3, 0, i);
                        System.arraycopy(replayDisposableArr, i + 1, replayDisposableArr3, i, (length - i) - 1);
                        replayDisposableArr2 = replayDisposableArr3;
                    }
                } else {
                    return;
                }
            }
        } while (!this.observers.compareAndSet(replayDisposableArr, replayDisposableArr2));
    }

    /* access modifiers changed from: 0000 */
    public int size() {
        return this.buffer.size();
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        ReplayDisposable replayDisposable = new ReplayDisposable(observer, this);
        observer.onSubscribe(replayDisposable);
        if (!replayDisposable.cancelled) {
            if (!add(replayDisposable) || !replayDisposable.cancelled) {
                this.buffer.replay(replayDisposable);
            } else {
                remove(replayDisposable);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public ReplayDisposable[] terminate(Object obj) {
        return this.buffer.compareAndSet(null, obj) ? (ReplayDisposable[]) this.observers.getAndSet(TERMINATED) : TERMINATED;
    }
}
