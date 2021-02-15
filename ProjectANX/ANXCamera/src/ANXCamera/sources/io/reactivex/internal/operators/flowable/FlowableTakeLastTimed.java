package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTakeLastTimed extends AbstractFlowableWithUpstream {
    final int bufferSize;
    final long count;
    final boolean delayError;
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    final class TakeLastTimedSubscriber extends AtomicInteger implements FlowableSubscriber, Subscription {
        private static final long serialVersionUID = -5677354903406201275L;
        final Subscriber actual;
        volatile boolean cancelled;
        final long count;
        final boolean delayError;
        volatile boolean done;
        Throwable error;
        final SpscLinkedArrayQueue queue;
        final AtomicLong requested = new AtomicLong();
        Subscription s;
        final Scheduler scheduler;
        final long time;
        final TimeUnit unit;

        TakeLastTimedSubscriber(Subscriber subscriber, long j, long j2, TimeUnit timeUnit, Scheduler scheduler2, int i, boolean z) {
            this.actual = subscriber;
            this.count = j;
            this.time = j2;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
            this.queue = new SpscLinkedArrayQueue(i);
            this.delayError = z;
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.s.cancel();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean z, Subscriber subscriber, boolean z2) {
            if (this.cancelled) {
                this.queue.clear();
                return true;
            }
            if (!z2) {
                Throwable th = this.error;
                if (th != null) {
                    this.queue.clear();
                    subscriber.onError(th);
                    return true;
                } else if (z) {
                    subscriber.onComplete();
                    return true;
                }
            } else if (z) {
                Throwable th2 = this.error;
                if (th2 != null) {
                    subscriber.onError(th2);
                } else {
                    subscriber.onComplete();
                }
                return true;
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                Subscriber subscriber = this.actual;
                SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
                boolean z = this.delayError;
                int i = 1;
                do {
                    if (this.done) {
                        if (!checkTerminated(spscLinkedArrayQueue.isEmpty(), subscriber, z)) {
                            long j = this.requested.get();
                            long j2 = 0;
                            while (true) {
                                if (!checkTerminated(spscLinkedArrayQueue.peek() == null, subscriber, z)) {
                                    if (j != j2) {
                                        spscLinkedArrayQueue.poll();
                                        subscriber.onNext(spscLinkedArrayQueue.poll());
                                        j2++;
                                    } else if (j2 != 0) {
                                        BackpressureHelper.produced(this.requested, j2);
                                    }
                                } else {
                                    return;
                                }
                            }
                        } else {
                            return;
                        }
                    }
                    i = addAndGet(-i);
                } while (i != 0);
            }
        }

        public void onComplete() {
            trim(this.scheduler.now(this.unit), this.queue);
            this.done = true;
            drain();
        }

        public void onError(Throwable th) {
            if (this.delayError) {
                trim(this.scheduler.now(this.unit), this.queue);
            }
            this.error = th;
            this.done = true;
            drain();
        }

        public void onNext(Object obj) {
            SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
            long now = this.scheduler.now(this.unit);
            spscLinkedArrayQueue.offer(Long.valueOf(now), obj);
            trim(now, spscLinkedArrayQueue);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(Long.MAX_VALUE);
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                drain();
            }
        }

        /* access modifiers changed from: 0000 */
        public void trim(long j, SpscLinkedArrayQueue spscLinkedArrayQueue) {
            long j2 = this.time;
            long j3 = this.count;
            boolean z = j3 == Long.MAX_VALUE;
            while (!spscLinkedArrayQueue.isEmpty()) {
                if (((Long) spscLinkedArrayQueue.peek()).longValue() < j - j2 || (!z && ((long) (spscLinkedArrayQueue.size() >> 1)) > j3)) {
                    spscLinkedArrayQueue.poll();
                    spscLinkedArrayQueue.poll();
                } else {
                    return;
                }
            }
        }
    }

    public FlowableTakeLastTimed(Flowable flowable, long j, long j2, TimeUnit timeUnit, Scheduler scheduler2, int i, boolean z) {
        super(flowable);
        this.count = j;
        this.time = j2;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.bufferSize = i;
        this.delayError = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        Flowable flowable = this.source;
        TakeLastTimedSubscriber takeLastTimedSubscriber = new TakeLastTimedSubscriber(subscriber, this.count, this.time, this.unit, this.scheduler, this.bufferSize, this.delayError);
        flowable.subscribe((FlowableSubscriber) takeLastTimedSubscriber);
    }
}
