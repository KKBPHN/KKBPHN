package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.QueueDrain;
import io.reactivex.internal.util.QueueDrainHelper;
import org.reactivestreams.Subscriber;

public abstract class QueueDrainSubscriber extends QueueDrainSubscriberPad4 implements FlowableSubscriber, QueueDrain {
    protected final Subscriber actual;
    /* access modifiers changed from: protected */
    public volatile boolean cancelled;
    protected volatile boolean done;
    protected Throwable error;
    /* access modifiers changed from: protected */
    public final SimplePlainQueue queue;

    public QueueDrainSubscriber(Subscriber subscriber, SimplePlainQueue simplePlainQueue) {
        this.actual = subscriber;
        this.queue = simplePlainQueue;
    }

    public boolean accept(Subscriber subscriber, Object obj) {
        return false;
    }

    public final boolean cancelled() {
        return this.cancelled;
    }

    public final boolean done() {
        return this.done;
    }

    public final boolean enter() {
        return this.wip.getAndIncrement() == 0;
    }

    public final Throwable error() {
        return this.error;
    }

    public final boolean fastEnter() {
        return this.wip.get() == 0 && this.wip.compareAndSet(0, 1);
    }

    /* access modifiers changed from: protected */
    public final void fastPathEmitMax(Object obj, boolean z, Disposable disposable) {
        Subscriber subscriber = this.actual;
        SimplePlainQueue simplePlainQueue = this.queue;
        if (this.wip.get() != 0 || !this.wip.compareAndSet(0, 1)) {
            simplePlainQueue.offer(obj);
            if (!enter()) {
                return;
            }
        } else {
            long j = this.requested.get();
            if (j != 0) {
                if (accept(subscriber, obj) && j != Long.MAX_VALUE) {
                    produced(1);
                }
                if (leave(-1) == 0) {
                    return;
                }
            } else {
                disposable.dispose();
                subscriber.onError(new MissingBackpressureException("Could not emit buffer due to lack of requests"));
                return;
            }
        }
        QueueDrainHelper.drainMaxLoop(simplePlainQueue, subscriber, z, disposable, this);
    }

    /* access modifiers changed from: protected */
    public final void fastPathOrderedEmitMax(Object obj, boolean z, Disposable disposable) {
        Subscriber subscriber = this.actual;
        SimplePlainQueue simplePlainQueue = this.queue;
        if (this.wip.get() != 0 || !this.wip.compareAndSet(0, 1)) {
            simplePlainQueue.offer(obj);
            if (!enter()) {
                return;
            }
        } else {
            long j = this.requested.get();
            if (j == 0) {
                this.cancelled = true;
                disposable.dispose();
                subscriber.onError(new MissingBackpressureException("Could not emit buffer due to lack of requests"));
                return;
            } else if (simplePlainQueue.isEmpty()) {
                if (accept(subscriber, obj) && j != Long.MAX_VALUE) {
                    produced(1);
                }
                if (leave(-1) == 0) {
                    return;
                }
            } else {
                simplePlainQueue.offer(obj);
            }
        }
        QueueDrainHelper.drainMaxLoop(simplePlainQueue, subscriber, z, disposable, this);
    }

    public final int leave(int i) {
        return this.wip.addAndGet(i);
    }

    public final long produced(long j) {
        return this.requested.addAndGet(-j);
    }

    public final long requested() {
        return this.requested.get();
    }

    public final void requested(long j) {
        if (SubscriptionHelper.validate(j)) {
            BackpressureHelper.add(this.requested, j);
        }
    }
}
