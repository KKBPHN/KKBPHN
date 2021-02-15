package io.reactivex.processors;

import io.reactivex.Scheduler;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ReplayProcessor extends FlowableProcessor {
    static final ReplaySubscription[] EMPTY = new ReplaySubscription[0];
    private static final Object[] EMPTY_ARRAY = new Object[0];
    static final ReplaySubscription[] TERMINATED = new ReplaySubscription[0];
    final ReplayBuffer buffer;
    boolean done;
    final AtomicReference subscribers = new AtomicReference(EMPTY);

    final class Node extends AtomicReference {
        private static final long serialVersionUID = 6404226426336033100L;
        final Object value;

        Node(Object obj) {
            this.value = obj;
        }
    }

    interface ReplayBuffer {
        void complete();

        void error(Throwable th);

        Throwable getError();

        Object getValue();

        Object[] getValues(Object[] objArr);

        boolean isDone();

        void next(Object obj);

        void replay(ReplaySubscription replaySubscription);

        int size();
    }

    final class ReplaySubscription extends AtomicInteger implements Subscription {
        private static final long serialVersionUID = 466549804534799122L;
        final Subscriber actual;
        volatile boolean cancelled;
        long emitted;
        Object index;
        final AtomicLong requested = new AtomicLong();
        final ReplayProcessor state;

        ReplaySubscription(Subscriber subscriber, ReplayProcessor replayProcessor) {
            this.actual = subscriber;
            this.state = replayProcessor;
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.state.remove(this);
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                this.state.buffer.replay(this);
            }
        }
    }

    final class SizeAndTimeBoundReplayBuffer implements ReplayBuffer {
        volatile boolean done;
        Throwable error;
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

        public void complete() {
            trimFinal();
            this.done = true;
        }

        public void error(Throwable th) {
            trimFinal();
            this.error = th;
            this.done = true;
        }

        public Throwable getError() {
            return this.error;
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
            while (true) {
                TimedNode timedNode2 = (TimedNode) timedNode.get();
                if (timedNode2 == null) {
                    break;
                }
                timedNode = timedNode2;
            }
            if (timedNode.time < this.scheduler.now(this.unit) - this.maxAge) {
                return null;
            }
            return timedNode.value;
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

        public boolean isDone() {
            return this.done;
        }

        public void next(Object obj) {
            TimedNode timedNode = new TimedNode(obj, this.scheduler.now(this.unit));
            TimedNode timedNode2 = this.tail;
            this.tail = timedNode;
            this.size++;
            timedNode2.set(timedNode);
            trim();
        }

        public void replay(ReplaySubscription replaySubscription) {
            int i;
            if (replaySubscription.getAndIncrement() == 0) {
                Subscriber subscriber = replaySubscription.actual;
                TimedNode timedNode = (TimedNode) replaySubscription.index;
                if (timedNode == null) {
                    timedNode = getHead();
                }
                long j = replaySubscription.emitted;
                int i2 = 1;
                do {
                    long j2 = replaySubscription.requested.get();
                    while (true) {
                        i = (j > j2 ? 1 : (j == j2 ? 0 : -1));
                        if (i == 0) {
                            break;
                        } else if (replaySubscription.cancelled) {
                            replaySubscription.index = null;
                            return;
                        } else {
                            boolean z = this.done;
                            TimedNode timedNode2 = (TimedNode) timedNode.get();
                            boolean z2 = timedNode2 == null;
                            if (z && z2) {
                                replaySubscription.index = null;
                                replaySubscription.cancelled = true;
                                Throwable th = this.error;
                                if (th == null) {
                                    subscriber.onComplete();
                                } else {
                                    subscriber.onError(th);
                                }
                                return;
                            } else if (z2) {
                                break;
                            } else {
                                subscriber.onNext(timedNode2.value);
                                j++;
                                timedNode = timedNode2;
                            }
                        }
                    }
                    if (i == 0) {
                        if (replaySubscription.cancelled) {
                            replaySubscription.index = null;
                            return;
                        } else if (this.done && timedNode.get() == null) {
                            replaySubscription.index = null;
                            replaySubscription.cancelled = true;
                            Throwable th2 = this.error;
                            if (th2 == null) {
                                subscriber.onComplete();
                            } else {
                                subscriber.onError(th2);
                            }
                            return;
                        }
                    }
                    replaySubscription.index = timedNode;
                    replaySubscription.emitted = j;
                    i2 = replaySubscription.addAndGet(-i2);
                } while (i2 != 0);
            }
        }

        public int size() {
            return size(getHead());
        }

        /* access modifiers changed from: 0000 */
        public int size(TimedNode timedNode) {
            int i = 0;
            while (i != Integer.MAX_VALUE) {
                timedNode = (TimedNode) timedNode.get();
                if (timedNode == null) {
                    break;
                }
                i++;
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
                if (timedNode2 != null && timedNode2.time <= now) {
                    timedNode = timedNode2;
                }
            }
            this.head = timedNode;
        }
    }

    final class SizeBoundReplayBuffer implements ReplayBuffer {
        volatile boolean done;
        Throwable error;
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

        public void complete() {
            this.done = true;
        }

        public void error(Throwable th) {
            this.error = th;
            this.done = true;
        }

        public Throwable getError() {
            return this.error;
        }

        public Object getValue() {
            Node node = this.head;
            while (true) {
                Node node2 = (Node) node.get();
                if (node2 == null) {
                    return node.value;
                }
                node = node2;
            }
        }

        public Object[] getValues(Object[] objArr) {
            Node node = this.head;
            Node node2 = node;
            int i = 0;
            while (true) {
                node2 = (Node) node2.get();
                if (node2 == null) {
                    break;
                }
                i++;
            }
            if (objArr.length < i) {
                objArr = (Object[]) Array.newInstance(objArr.getClass().getComponentType(), i);
            }
            for (int i2 = 0; i2 < i; i2++) {
                node = (Node) node.get();
                objArr[i2] = node.value;
            }
            if (objArr.length > i) {
                objArr[i] = null;
            }
            return objArr;
        }

        public boolean isDone() {
            return this.done;
        }

        public void next(Object obj) {
            Node node = new Node(obj);
            Node node2 = this.tail;
            this.tail = node;
            this.size++;
            node2.set(node);
            trim();
        }

        public void replay(ReplaySubscription replaySubscription) {
            int i;
            if (replaySubscription.getAndIncrement() == 0) {
                Subscriber subscriber = replaySubscription.actual;
                Node node = (Node) replaySubscription.index;
                if (node == null) {
                    node = this.head;
                }
                long j = replaySubscription.emitted;
                int i2 = 1;
                do {
                    long j2 = replaySubscription.requested.get();
                    while (true) {
                        i = (j > j2 ? 1 : (j == j2 ? 0 : -1));
                        if (i == 0) {
                            break;
                        } else if (replaySubscription.cancelled) {
                            replaySubscription.index = null;
                            return;
                        } else {
                            boolean z = this.done;
                            Node node2 = (Node) node.get();
                            boolean z2 = node2 == null;
                            if (z && z2) {
                                replaySubscription.index = null;
                                replaySubscription.cancelled = true;
                                Throwable th = this.error;
                                if (th == null) {
                                    subscriber.onComplete();
                                } else {
                                    subscriber.onError(th);
                                }
                                return;
                            } else if (z2) {
                                break;
                            } else {
                                subscriber.onNext(node2.value);
                                j++;
                                node = node2;
                            }
                        }
                    }
                    if (i == 0) {
                        if (replaySubscription.cancelled) {
                            replaySubscription.index = null;
                            return;
                        } else if (this.done && node.get() == null) {
                            replaySubscription.index = null;
                            replaySubscription.cancelled = true;
                            Throwable th2 = this.error;
                            if (th2 == null) {
                                subscriber.onComplete();
                            } else {
                                subscriber.onError(th2);
                            }
                            return;
                        }
                    }
                    replaySubscription.index = node;
                    replaySubscription.emitted = j;
                    i2 = replaySubscription.addAndGet(-i2);
                } while (i2 != 0);
            }
        }

        public int size() {
            Node node = this.head;
            int i = 0;
            while (i != Integer.MAX_VALUE) {
                node = (Node) node.get();
                if (node == null) {
                    break;
                }
                i++;
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

    final class UnboundedReplayBuffer implements ReplayBuffer {
        final List buffer;
        volatile boolean done;
        Throwable error;
        volatile int size;

        UnboundedReplayBuffer(int i) {
            ObjectHelper.verifyPositive(i, "capacityHint");
            this.buffer = new ArrayList(i);
        }

        public void complete() {
            this.done = true;
        }

        public void error(Throwable th) {
            this.error = th;
            this.done = true;
        }

        public Throwable getError() {
            return this.error;
        }

        public Object getValue() {
            int i = this.size;
            if (i == 0) {
                return null;
            }
            return this.buffer.get(i - 1);
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

        public boolean isDone() {
            return this.done;
        }

        public void next(Object obj) {
            this.buffer.add(obj);
            this.size++;
        }

        public void replay(ReplaySubscription replaySubscription) {
            int i;
            if (replaySubscription.getAndIncrement() == 0) {
                List list = this.buffer;
                Subscriber subscriber = replaySubscription.actual;
                Integer num = (Integer) replaySubscription.index;
                int i2 = 0;
                if (num != null) {
                    i2 = num.intValue();
                } else {
                    replaySubscription.index = Integer.valueOf(0);
                }
                long j = replaySubscription.emitted;
                int i3 = 1;
                do {
                    long j2 = replaySubscription.requested.get();
                    while (true) {
                        i = (j > j2 ? 1 : (j == j2 ? 0 : -1));
                        if (i == 0) {
                            break;
                        } else if (replaySubscription.cancelled) {
                            replaySubscription.index = null;
                            return;
                        } else {
                            boolean z = this.done;
                            int i4 = this.size;
                            if (z && i2 == i4) {
                                replaySubscription.index = null;
                                replaySubscription.cancelled = true;
                                Throwable th = this.error;
                                if (th == null) {
                                    subscriber.onComplete();
                                } else {
                                    subscriber.onError(th);
                                }
                                return;
                            } else if (i2 == i4) {
                                break;
                            } else {
                                subscriber.onNext(list.get(i2));
                                i2++;
                                j++;
                            }
                        }
                    }
                    if (i == 0) {
                        if (replaySubscription.cancelled) {
                            replaySubscription.index = null;
                            return;
                        }
                        boolean z2 = this.done;
                        int i5 = this.size;
                        if (z2 && i2 == i5) {
                            replaySubscription.index = null;
                            replaySubscription.cancelled = true;
                            Throwable th2 = this.error;
                            if (th2 == null) {
                                subscriber.onComplete();
                            } else {
                                subscriber.onError(th2);
                            }
                            return;
                        }
                    }
                    replaySubscription.index = Integer.valueOf(i2);
                    replaySubscription.emitted = j;
                    i3 = replaySubscription.addAndGet(-i3);
                } while (i3 != 0);
            }
        }

        public int size() {
            return this.size;
        }
    }

    ReplayProcessor(ReplayBuffer replayBuffer) {
        this.buffer = replayBuffer;
    }

    @CheckReturnValue
    public static ReplayProcessor create() {
        return new ReplayProcessor(new UnboundedReplayBuffer(16));
    }

    @CheckReturnValue
    public static ReplayProcessor create(int i) {
        return new ReplayProcessor(new UnboundedReplayBuffer(i));
    }

    static ReplayProcessor createUnbounded() {
        return new ReplayProcessor(new SizeBoundReplayBuffer(Integer.MAX_VALUE));
    }

    @CheckReturnValue
    public static ReplayProcessor createWithSize(int i) {
        return new ReplayProcessor(new SizeBoundReplayBuffer(i));
    }

    @CheckReturnValue
    public static ReplayProcessor createWithTime(long j, TimeUnit timeUnit, Scheduler scheduler) {
        SizeAndTimeBoundReplayBuffer sizeAndTimeBoundReplayBuffer = new SizeAndTimeBoundReplayBuffer(Integer.MAX_VALUE, j, timeUnit, scheduler);
        return new ReplayProcessor(sizeAndTimeBoundReplayBuffer);
    }

    @CheckReturnValue
    public static ReplayProcessor createWithTimeAndSize(long j, TimeUnit timeUnit, Scheduler scheduler, int i) {
        SizeAndTimeBoundReplayBuffer sizeAndTimeBoundReplayBuffer = new SizeAndTimeBoundReplayBuffer(i, j, timeUnit, scheduler);
        return new ReplayProcessor(sizeAndTimeBoundReplayBuffer);
    }

    /* access modifiers changed from: 0000 */
    public boolean add(ReplaySubscription replaySubscription) {
        ReplaySubscription[] replaySubscriptionArr;
        ReplaySubscription[] replaySubscriptionArr2;
        do {
            replaySubscriptionArr = (ReplaySubscription[]) this.subscribers.get();
            if (replaySubscriptionArr == TERMINATED) {
                return false;
            }
            int length = replaySubscriptionArr.length;
            replaySubscriptionArr2 = new ReplaySubscription[(length + 1)];
            System.arraycopy(replaySubscriptionArr, 0, replaySubscriptionArr2, 0, length);
            replaySubscriptionArr2[length] = replaySubscription;
        } while (!this.subscribers.compareAndSet(replaySubscriptionArr, replaySubscriptionArr2));
        return true;
    }

    public Throwable getThrowable() {
        ReplayBuffer replayBuffer = this.buffer;
        if (replayBuffer.isDone()) {
            return replayBuffer.getError();
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
        ReplayBuffer replayBuffer = this.buffer;
        return replayBuffer.isDone() && replayBuffer.getError() == null;
    }

    public boolean hasSubscribers() {
        return ((ReplaySubscription[]) this.subscribers.get()).length != 0;
    }

    public boolean hasThrowable() {
        ReplayBuffer replayBuffer = this.buffer;
        return replayBuffer.isDone() && replayBuffer.getError() != null;
    }

    public boolean hasValue() {
        return this.buffer.size() != 0;
    }

    public void onComplete() {
        if (!this.done) {
            this.done = true;
            ReplayBuffer replayBuffer = this.buffer;
            replayBuffer.complete();
            for (ReplaySubscription replay : (ReplaySubscription[]) this.subscribers.getAndSet(TERMINATED)) {
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
        ReplayBuffer replayBuffer = this.buffer;
        replayBuffer.error(th);
        for (ReplaySubscription replay : (ReplaySubscription[]) this.subscribers.getAndSet(TERMINATED)) {
            replayBuffer.replay(replay);
        }
    }

    public void onNext(Object obj) {
        ObjectHelper.requireNonNull(obj, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (!this.done) {
            ReplayBuffer replayBuffer = this.buffer;
            replayBuffer.next(obj);
            for (ReplaySubscription replay : (ReplaySubscription[]) this.subscribers.get()) {
                replayBuffer.replay(replay);
            }
        }
    }

    public void onSubscribe(Subscription subscription) {
        if (this.done) {
            subscription.cancel();
        } else {
            subscription.request(Long.MAX_VALUE);
        }
    }

    /* access modifiers changed from: 0000 */
    public void remove(ReplaySubscription replaySubscription) {
        ReplaySubscription[] replaySubscriptionArr;
        ReplaySubscription[] replaySubscriptionArr2;
        do {
            replaySubscriptionArr = (ReplaySubscription[]) this.subscribers.get();
            if (replaySubscriptionArr != TERMINATED && replaySubscriptionArr != EMPTY) {
                int length = replaySubscriptionArr.length;
                int i = -1;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    } else if (replaySubscriptionArr[i2] == replaySubscription) {
                        i = i2;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (i >= 0) {
                    if (length == 1) {
                        replaySubscriptionArr2 = EMPTY;
                    } else {
                        ReplaySubscription[] replaySubscriptionArr3 = new ReplaySubscription[(length - 1)];
                        System.arraycopy(replaySubscriptionArr, 0, replaySubscriptionArr3, 0, i);
                        System.arraycopy(replaySubscriptionArr, i + 1, replaySubscriptionArr3, i, (length - i) - 1);
                        replaySubscriptionArr2 = replaySubscriptionArr3;
                    }
                } else {
                    return;
                }
            }
        } while (!this.subscribers.compareAndSet(replaySubscriptionArr, replaySubscriptionArr2));
    }

    /* access modifiers changed from: 0000 */
    public int size() {
        return this.buffer.size();
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        ReplaySubscription replaySubscription = new ReplaySubscription(subscriber, this);
        subscriber.onSubscribe(replaySubscription);
        if (!add(replaySubscription) || !replaySubscription.cancelled) {
            this.buffer.replay(replaySubscription);
        } else {
            remove(replaySubscription);
        }
    }

    /* access modifiers changed from: 0000 */
    public int subscriberCount() {
        return ((ReplaySubscription[]) this.subscribers.get()).length;
    }
}
