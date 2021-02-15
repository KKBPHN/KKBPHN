package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.Scheduler.Worker;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.subscribers.QueueDrainSubscriber;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.QueueDrainHelper;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableBufferTimed extends AbstractFlowableWithUpstream {
    final Callable bufferSupplier;
    final int maxSize;
    final boolean restartTimerOnMaxSize;
    final Scheduler scheduler;
    final long timeskip;
    final long timespan;
    final TimeUnit unit;

    final class BufferExactBoundedSubscriber extends QueueDrainSubscriber implements Subscription, Runnable, Disposable {
        Collection buffer;
        final Callable bufferSupplier;
        long consumerIndex;
        final int maxSize;
        long producerIndex;
        final boolean restartTimerOnMaxSize;
        Subscription s;
        Disposable timer;
        final long timespan;
        final TimeUnit unit;
        final Worker w;

        BufferExactBoundedSubscriber(Subscriber subscriber, Callable callable, long j, TimeUnit timeUnit, int i, boolean z, Worker worker) {
            super(subscriber, new MpscLinkedQueue());
            this.bufferSupplier = callable;
            this.timespan = j;
            this.unit = timeUnit;
            this.maxSize = i;
            this.restartTimerOnMaxSize = z;
            this.w = worker;
        }

        public boolean accept(Subscriber subscriber, Collection collection) {
            subscriber.onNext(collection);
            return true;
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                dispose();
            }
        }

        public void dispose() {
            synchronized (this) {
                this.buffer = null;
            }
            this.s.cancel();
            this.w.dispose();
        }

        public boolean isDisposed() {
            return this.w.isDisposed();
        }

        public void onComplete() {
            Collection collection;
            synchronized (this) {
                collection = this.buffer;
                this.buffer = null;
            }
            this.queue.offer(collection);
            this.done = true;
            if (enter()) {
                QueueDrainHelper.drainMaxLoop(this.queue, this.actual, false, this, this);
            }
            this.w.dispose();
        }

        public void onError(Throwable th) {
            synchronized (this) {
                this.buffer = null;
            }
            this.actual.onError(th);
            this.w.dispose();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0021, code lost:
            if (r7.restartTimerOnMaxSize == false) goto L_0x0028;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0023, code lost:
            r7.timer.dispose();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0028, code lost:
            fastPathOrderedEmitMax(r0, false, r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
            r8 = r7.bufferSupplier.call();
            io.reactivex.internal.functions.ObjectHelper.requireNonNull(r8, "The supplied buffer is null");
            r8 = (java.util.Collection) r8;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0039, code lost:
            monitor-enter(r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
            r7.buffer = r8;
            r7.consumerIndex++;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0041, code lost:
            monitor-exit(r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0044, code lost:
            if (r7.restartTimerOnMaxSize == false) goto L_0x0054;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0046, code lost:
            r0 = r7.w;
            r4 = r7.timespan;
            r7.timer = r0.schedulePeriodically(r7, r4, r4, r7.unit);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0054, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0058, code lost:
            r8 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0059, code lost:
            io.reactivex.exceptions.Exceptions.throwIfFatal(r8);
            cancel();
            r7.actual.onError(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0064, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(Object obj) {
            synchronized (this) {
                Collection collection = this.buffer;
                if (collection != null) {
                    collection.add(obj);
                    if (collection.size() >= this.maxSize) {
                        this.buffer = null;
                        this.producerIndex++;
                    }
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                try {
                    Object call = this.bufferSupplier.call();
                    ObjectHelper.requireNonNull(call, "The supplied buffer is null");
                    this.buffer = (Collection) call;
                    this.actual.onSubscribe(this);
                    Worker worker = this.w;
                    long j = this.timespan;
                    this.timer = worker.schedulePeriodically(this, j, j, this.unit);
                    subscription.request(Long.MAX_VALUE);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.w.dispose();
                    subscription.cancel();
                    EmptySubscription.error(th, this.actual);
                }
            }
        }

        public void request(long j) {
            requested(j);
        }

        public void run() {
            try {
                Object call = this.bufferSupplier.call();
                ObjectHelper.requireNonNull(call, "The supplied buffer is null");
                Collection collection = (Collection) call;
                synchronized (this) {
                    Collection collection2 = this.buffer;
                    if (collection2 != null) {
                        if (this.producerIndex == this.consumerIndex) {
                            this.buffer = collection;
                            fastPathOrderedEmitMax(collection2, false, this);
                        }
                    }
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                cancel();
                this.actual.onError(th);
            }
        }
    }

    final class BufferExactUnboundedSubscriber extends QueueDrainSubscriber implements Subscription, Runnable, Disposable {
        Collection buffer;
        final Callable bufferSupplier;
        Subscription s;
        final Scheduler scheduler;
        final AtomicReference timer = new AtomicReference();
        final long timespan;
        final TimeUnit unit;

        BufferExactUnboundedSubscriber(Subscriber subscriber, Callable callable, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            super(subscriber, new MpscLinkedQueue());
            this.bufferSupplier = callable;
            this.timespan = j;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
        }

        public boolean accept(Subscriber subscriber, Collection collection) {
            this.actual.onNext(collection);
            return true;
        }

        public void cancel() {
            this.s.cancel();
            DisposableHelper.dispose(this.timer);
        }

        public void dispose() {
            cancel();
        }

        public boolean isDisposed() {
            return this.timer.get() == DisposableHelper.DISPOSED;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x001c, code lost:
            if (enter() == false) goto L_0x0026;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x001e, code lost:
            io.reactivex.internal.util.QueueDrainHelper.drainMaxLoop(r4.queue, r4.actual, false, null, r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0026, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0010, code lost:
            r4.queue.offer(r0);
            r4.done = true;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onComplete() {
            DisposableHelper.dispose(this.timer);
            synchronized (this) {
                Collection collection = this.buffer;
                if (collection != null) {
                    this.buffer = null;
                }
            }
        }

        public void onError(Throwable th) {
            DisposableHelper.dispose(this.timer);
            synchronized (this) {
                this.buffer = null;
            }
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            synchronized (this) {
                Collection collection = this.buffer;
                if (collection != null) {
                    collection.add(obj);
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                try {
                    Object call = this.bufferSupplier.call();
                    ObjectHelper.requireNonNull(call, "The supplied buffer is null");
                    this.buffer = (Collection) call;
                    this.actual.onSubscribe(this);
                    if (!this.cancelled) {
                        subscription.request(Long.MAX_VALUE);
                        Scheduler scheduler2 = this.scheduler;
                        long j = this.timespan;
                        Disposable schedulePeriodicallyDirect = scheduler2.schedulePeriodicallyDirect(this, j, j, this.unit);
                        if (!this.timer.compareAndSet(null, schedulePeriodicallyDirect)) {
                            schedulePeriodicallyDirect.dispose();
                        }
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    cancel();
                    EmptySubscription.error(th, this.actual);
                }
            }
        }

        public void request(long j) {
            requested(j);
        }

        public void run() {
            Collection collection;
            try {
                Object call = this.bufferSupplier.call();
                ObjectHelper.requireNonNull(call, "The supplied buffer is null");
                Collection collection2 = (Collection) call;
                synchronized (this) {
                    collection = this.buffer;
                    if (collection != null) {
                        this.buffer = collection2;
                    }
                }
                if (collection == null) {
                    DisposableHelper.dispose(this.timer);
                } else {
                    fastPathEmitMax(collection, false, this);
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                cancel();
                this.actual.onError(th);
            }
        }
    }

    final class BufferSkipBoundedSubscriber extends QueueDrainSubscriber implements Subscription, Runnable {
        final Callable bufferSupplier;
        final List buffers = new LinkedList();
        Subscription s;
        final long timeskip;
        final long timespan;
        final TimeUnit unit;
        final Worker w;

        final class RemoveFromBuffer implements Runnable {
            private final Collection buffer;

            RemoveFromBuffer(Collection collection) {
                this.buffer = collection;
            }

            public void run() {
                synchronized (BufferSkipBoundedSubscriber.this) {
                    BufferSkipBoundedSubscriber.this.buffers.remove(this.buffer);
                }
                BufferSkipBoundedSubscriber bufferSkipBoundedSubscriber = BufferSkipBoundedSubscriber.this;
                bufferSkipBoundedSubscriber.fastPathOrderedEmitMax(this.buffer, false, bufferSkipBoundedSubscriber.w);
            }
        }

        BufferSkipBoundedSubscriber(Subscriber subscriber, Callable callable, long j, long j2, TimeUnit timeUnit, Worker worker) {
            super(subscriber, new MpscLinkedQueue());
            this.bufferSupplier = callable;
            this.timespan = j;
            this.timeskip = j2;
            this.unit = timeUnit;
            this.w = worker;
        }

        public boolean accept(Subscriber subscriber, Collection collection) {
            subscriber.onNext(collection);
            return true;
        }

        public void cancel() {
            clear();
            this.s.cancel();
            this.w.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void clear() {
            synchronized (this) {
                this.buffers.clear();
            }
        }

        public void onComplete() {
            ArrayList<Collection> arrayList;
            synchronized (this) {
                arrayList = new ArrayList<>(this.buffers);
                this.buffers.clear();
            }
            for (Collection offer : arrayList) {
                this.queue.offer(offer);
            }
            this.done = true;
            if (enter()) {
                QueueDrainHelper.drainMaxLoop(this.queue, this.actual, false, this.w, this);
            }
        }

        public void onError(Throwable th) {
            this.done = true;
            this.w.dispose();
            clear();
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            synchronized (this) {
                for (Collection add : this.buffers) {
                    add.add(obj);
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                try {
                    Object call = this.bufferSupplier.call();
                    ObjectHelper.requireNonNull(call, "The supplied buffer is null");
                    Collection collection = (Collection) call;
                    this.buffers.add(collection);
                    this.actual.onSubscribe(this);
                    subscription.request(Long.MAX_VALUE);
                    Worker worker = this.w;
                    long j = this.timeskip;
                    worker.schedulePeriodically(this, j, j, this.unit);
                    this.w.schedule(new RemoveFromBuffer(collection), this.timespan, this.unit);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.w.dispose();
                    subscription.cancel();
                    EmptySubscription.error(th, this.actual);
                }
            }
        }

        public void request(long j) {
            requested(j);
        }

        public void run() {
            if (!this.cancelled) {
                try {
                    Object call = this.bufferSupplier.call();
                    ObjectHelper.requireNonNull(call, "The supplied buffer is null");
                    Collection collection = (Collection) call;
                    synchronized (this) {
                        if (!this.cancelled) {
                            this.buffers.add(collection);
                            this.w.schedule(new RemoveFromBuffer(collection), this.timespan, this.unit);
                        }
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    cancel();
                    this.actual.onError(th);
                }
            }
        }
    }

    public FlowableBufferTimed(Flowable flowable, long j, long j2, TimeUnit timeUnit, Scheduler scheduler2, Callable callable, int i, boolean z) {
        super(flowable);
        this.timespan = j;
        this.timeskip = j2;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.bufferSupplier = callable;
        this.maxSize = i;
        this.restartTimerOnMaxSize = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        if (this.timespan == this.timeskip && this.maxSize == Integer.MAX_VALUE) {
            Flowable flowable = this.source;
            BufferExactUnboundedSubscriber bufferExactUnboundedSubscriber = new BufferExactUnboundedSubscriber(new SerializedSubscriber(subscriber), this.bufferSupplier, this.timespan, this.unit, this.scheduler);
            flowable.subscribe((FlowableSubscriber) bufferExactUnboundedSubscriber);
            return;
        }
        Worker createWorker = this.scheduler.createWorker();
        if (this.timespan == this.timeskip) {
            Flowable flowable2 = this.source;
            BufferExactBoundedSubscriber bufferExactBoundedSubscriber = new BufferExactBoundedSubscriber(new SerializedSubscriber(subscriber), this.bufferSupplier, this.timespan, this.unit, this.maxSize, this.restartTimerOnMaxSize, createWorker);
            flowable2.subscribe((FlowableSubscriber) bufferExactBoundedSubscriber);
            return;
        }
        Flowable flowable3 = this.source;
        BufferSkipBoundedSubscriber bufferSkipBoundedSubscriber = new BufferSkipBoundedSubscriber(new SerializedSubscriber(subscriber), this.bufferSupplier, this.timespan, this.timeskip, this.unit, createWorker);
        flowable3.subscribe((FlowableSubscriber) bufferSkipBoundedSubscriber);
    }
}
