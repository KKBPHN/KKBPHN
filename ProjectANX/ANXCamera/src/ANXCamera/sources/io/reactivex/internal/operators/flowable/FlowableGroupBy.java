package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.flowables.GroupedFlowable;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableGroupBy extends AbstractFlowableWithUpstream {
    final int bufferSize;
    final boolean delayError;
    final Function keySelector;
    final Function valueSelector;

    public final class GroupBySubscriber extends BasicIntQueueSubscription implements FlowableSubscriber {
        static final Object NULL_KEY = new Object();
        private static final long serialVersionUID = -3688291656102519502L;
        final Subscriber actual;
        final int bufferSize;
        final AtomicBoolean cancelled = new AtomicBoolean();
        final boolean delayError;
        volatile boolean done;
        Throwable error;
        final AtomicInteger groupCount = new AtomicInteger(1);
        final Map groups;
        final Function keySelector;
        boolean outputFused;
        final SpscLinkedArrayQueue queue;
        final AtomicLong requested = new AtomicLong();
        Subscription s;
        final Function valueSelector;

        public GroupBySubscriber(Subscriber subscriber, Function function, Function function2, int i, boolean z) {
            this.actual = subscriber;
            this.keySelector = function;
            this.valueSelector = function2;
            this.bufferSize = i;
            this.delayError = z;
            this.groups = new ConcurrentHashMap();
            this.queue = new SpscLinkedArrayQueue(i);
        }

        public void cancel() {
            if (this.cancelled.compareAndSet(false, true) && this.groupCount.decrementAndGet() == 0) {
                this.s.cancel();
            }
        }

        public void cancel(Object obj) {
            if (obj == null) {
                obj = NULL_KEY;
            }
            this.groups.remove(obj);
            if (this.groupCount.decrementAndGet() == 0) {
                this.s.cancel();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean z, boolean z2, Subscriber subscriber, SpscLinkedArrayQueue spscLinkedArrayQueue) {
            if (this.cancelled.get()) {
                spscLinkedArrayQueue.clear();
                return true;
            }
            if (this.delayError) {
                if (z && z2) {
                    Throwable th = this.error;
                    if (th != null) {
                        subscriber.onError(th);
                    } else {
                        subscriber.onComplete();
                    }
                    return true;
                }
            } else if (z) {
                Throwable th2 = this.error;
                if (th2 != null) {
                    spscLinkedArrayQueue.clear();
                    subscriber.onError(th2);
                    return true;
                } else if (z2) {
                    subscriber.onComplete();
                    return true;
                }
            }
            return false;
        }

        public void clear() {
            this.queue.clear();
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                if (this.outputFused) {
                    drainFused();
                } else {
                    drainNormal();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void drainFused() {
            SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
            Subscriber subscriber = this.actual;
            int i = 1;
            while (!this.cancelled.get()) {
                boolean z = this.done;
                if (z && !this.delayError) {
                    Throwable th = this.error;
                    if (th != null) {
                        spscLinkedArrayQueue.clear();
                        subscriber.onError(th);
                        return;
                    }
                }
                subscriber.onNext(null);
                if (z) {
                    Throwable th2 = this.error;
                    if (th2 != null) {
                        subscriber.onError(th2);
                    } else {
                        subscriber.onComplete();
                    }
                    return;
                }
                i = addAndGet(-i);
                if (i == 0) {
                    return;
                }
            }
            spscLinkedArrayQueue.clear();
        }

        /* access modifiers changed from: 0000 */
        public void drainNormal() {
            int i;
            SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
            Subscriber subscriber = this.actual;
            int i2 = 1;
            do {
                long j = this.requested.get();
                long j2 = 0;
                while (true) {
                    i = (j2 > j ? 1 : (j2 == j ? 0 : -1));
                    if (i == 0) {
                        break;
                    }
                    boolean z = this.done;
                    GroupedFlowable groupedFlowable = (GroupedFlowable) spscLinkedArrayQueue.poll();
                    boolean z2 = groupedFlowable == null;
                    if (!checkTerminated(z, z2, subscriber, spscLinkedArrayQueue)) {
                        if (z2) {
                            break;
                        }
                        subscriber.onNext(groupedFlowable);
                        j2++;
                    } else {
                        return;
                    }
                }
                if (i != 0 || !checkTerminated(this.done, spscLinkedArrayQueue.isEmpty(), subscriber, spscLinkedArrayQueue)) {
                    if (j2 != 0) {
                        if (j != Long.MAX_VALUE) {
                            this.requested.addAndGet(-j2);
                        }
                        this.s.request(j2);
                    }
                    i2 = addAndGet(-i2);
                } else {
                    return;
                }
            } while (i2 != 0);
        }

        public boolean isEmpty() {
            return this.queue.isEmpty();
        }

        public void onComplete() {
            if (!this.done) {
                for (GroupedUnicast onComplete : this.groups.values()) {
                    onComplete.onComplete();
                }
                this.groups.clear();
                this.done = true;
                drain();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            for (GroupedUnicast onError : this.groups.values()) {
                onError.onError(th);
            }
            this.groups.clear();
            this.error = th;
            this.done = true;
            drain();
        }

        public void onNext(Object obj) {
            if (!this.done) {
                SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
                try {
                    Object apply = this.keySelector.apply(obj);
                    boolean z = false;
                    Object obj2 = apply != null ? apply : NULL_KEY;
                    GroupedUnicast groupedUnicast = (GroupedUnicast) this.groups.get(obj2);
                    if (groupedUnicast == null) {
                        if (!this.cancelled.get()) {
                            groupedUnicast = GroupedUnicast.createWith(apply, this.bufferSize, this, this.delayError);
                            this.groups.put(obj2, groupedUnicast);
                            this.groupCount.getAndIncrement();
                            z = true;
                        } else {
                            return;
                        }
                    }
                    Object apply2 = this.valueSelector.apply(obj);
                    ObjectHelper.requireNonNull(apply2, "The valueSelector returned null");
                    groupedUnicast.onNext(apply2);
                    if (z) {
                        spscLinkedArrayQueue.offer(groupedUnicast);
                        drain();
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.s.cancel();
                    onError(th);
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                subscription.request((long) this.bufferSize);
            }
        }

        @Nullable
        public GroupedFlowable poll() {
            return (GroupedFlowable) this.queue.poll();
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                drain();
            }
        }

        public int requestFusion(int i) {
            if ((i & 2) == 0) {
                return 0;
            }
            this.outputFused = true;
            return 2;
        }
    }

    final class GroupedUnicast extends GroupedFlowable {
        final State state;

        protected GroupedUnicast(Object obj, State state2) {
            super(obj);
            this.state = state2;
        }

        public static GroupedUnicast createWith(Object obj, int i, GroupBySubscriber groupBySubscriber, boolean z) {
            return new GroupedUnicast(obj, new State(i, groupBySubscriber, obj, z));
        }

        public void onComplete() {
            this.state.onComplete();
        }

        public void onError(Throwable th) {
            this.state.onError(th);
        }

        public void onNext(Object obj) {
            this.state.onNext(obj);
        }

        /* access modifiers changed from: protected */
        public void subscribeActual(Subscriber subscriber) {
            this.state.subscribe(subscriber);
        }
    }

    final class State extends BasicIntQueueSubscription implements Publisher {
        private static final long serialVersionUID = -3852313036005250360L;
        final AtomicReference actual = new AtomicReference();
        final AtomicBoolean cancelled = new AtomicBoolean();
        final boolean delayError;
        volatile boolean done;
        Throwable error;
        final Object key;
        final AtomicBoolean once = new AtomicBoolean();
        boolean outputFused;
        final GroupBySubscriber parent;
        int produced;
        final SpscLinkedArrayQueue queue;
        final AtomicLong requested = new AtomicLong();

        State(int i, GroupBySubscriber groupBySubscriber, Object obj, boolean z) {
            this.queue = new SpscLinkedArrayQueue(i);
            this.parent = groupBySubscriber;
            this.key = obj;
            this.delayError = z;
        }

        public void cancel() {
            if (this.cancelled.compareAndSet(false, true)) {
                this.parent.cancel(this.key);
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean z, boolean z2, Subscriber subscriber, boolean z3) {
            if (this.cancelled.get()) {
                this.queue.clear();
                return true;
            }
            if (z) {
                if (!z3) {
                    Throwable th = this.error;
                    if (th != null) {
                        this.queue.clear();
                        subscriber.onError(th);
                        return true;
                    } else if (z2) {
                        subscriber.onComplete();
                        return true;
                    }
                } else if (z2) {
                    Throwable th2 = this.error;
                    if (th2 != null) {
                        subscriber.onError(th2);
                    } else {
                        subscriber.onComplete();
                    }
                    return true;
                }
            }
            return false;
        }

        public void clear() {
            this.queue.clear();
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                if (this.outputFused) {
                    drainFused();
                } else {
                    drainNormal();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void drainFused() {
            SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
            Subscriber subscriber = (Subscriber) this.actual.get();
            int i = 1;
            while (true) {
                if (subscriber != null) {
                    if (this.cancelled.get()) {
                        spscLinkedArrayQueue.clear();
                        return;
                    }
                    boolean z = this.done;
                    if (z && !this.delayError) {
                        Throwable th = this.error;
                        if (th != null) {
                            spscLinkedArrayQueue.clear();
                            subscriber.onError(th);
                            return;
                        }
                    }
                    subscriber.onNext(null);
                    if (z) {
                        Throwable th2 = this.error;
                        if (th2 != null) {
                            subscriber.onError(th2);
                        } else {
                            subscriber.onComplete();
                        }
                        return;
                    }
                }
                i = addAndGet(-i);
                if (i != 0) {
                    if (subscriber == null) {
                        subscriber = (Subscriber) this.actual.get();
                    }
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void drainNormal() {
            int i;
            SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
            boolean z = this.delayError;
            Subscriber subscriber = (Subscriber) this.actual.get();
            int i2 = 1;
            while (true) {
                if (subscriber != null) {
                    long j = this.requested.get();
                    long j2 = 0;
                    while (true) {
                        i = (j2 > j ? 1 : (j2 == j ? 0 : -1));
                        if (i == 0) {
                            break;
                        }
                        boolean z2 = this.done;
                        Object poll = spscLinkedArrayQueue.poll();
                        boolean z3 = poll == null;
                        if (!checkTerminated(z2, z3, subscriber, z)) {
                            if (z3) {
                                break;
                            }
                            subscriber.onNext(poll);
                            j2++;
                        } else {
                            return;
                        }
                    }
                    if (i == 0 && checkTerminated(this.done, spscLinkedArrayQueue.isEmpty(), subscriber, z)) {
                        return;
                    }
                    if (j2 != 0) {
                        if (j != Long.MAX_VALUE) {
                            this.requested.addAndGet(-j2);
                        }
                        this.parent.s.request(j2);
                    }
                }
                i2 = addAndGet(-i2);
                if (i2 != 0) {
                    if (subscriber == null) {
                        subscriber = (Subscriber) this.actual.get();
                    }
                } else {
                    return;
                }
            }
        }

        public boolean isEmpty() {
            return this.queue.isEmpty();
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public void onError(Throwable th) {
            this.error = th;
            this.done = true;
            drain();
        }

        public void onNext(Object obj) {
            this.queue.offer(obj);
            drain();
        }

        @Nullable
        public Object poll() {
            Object poll = this.queue.poll();
            if (poll != null) {
                this.produced++;
                return poll;
            }
            int i = this.produced;
            if (i != 0) {
                this.produced = 0;
                this.parent.s.request((long) i);
            }
            return null;
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                drain();
            }
        }

        public int requestFusion(int i) {
            if ((i & 2) == 0) {
                return 0;
            }
            this.outputFused = true;
            return 2;
        }

        public void subscribe(Subscriber subscriber) {
            if (this.once.compareAndSet(false, true)) {
                subscriber.onSubscribe(this);
                this.actual.lazySet(subscriber);
                drain();
                return;
            }
            EmptySubscription.error(new IllegalStateException("Only one Subscriber allowed!"), subscriber);
        }
    }

    public FlowableGroupBy(Flowable flowable, Function function, Function function2, int i, boolean z) {
        super(flowable);
        this.keySelector = function;
        this.valueSelector = function2;
        this.bufferSize = i;
        this.delayError = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        Flowable flowable = this.source;
        GroupBySubscriber groupBySubscriber = new GroupBySubscriber(subscriber, this.keySelector, this.valueSelector, this.bufferSize, this.delayError);
        flowable.subscribe((FlowableSubscriber) groupBySubscriber);
    }
}
