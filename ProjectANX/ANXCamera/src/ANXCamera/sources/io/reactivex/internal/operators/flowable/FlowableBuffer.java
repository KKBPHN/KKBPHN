package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.QueueDrainHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableBuffer extends AbstractFlowableWithUpstream {
    final Callable bufferSupplier;
    final int size;
    final int skip;

    final class PublisherBufferExactSubscriber implements FlowableSubscriber, Subscription {
        final Subscriber actual;
        Collection buffer;
        final Callable bufferSupplier;
        boolean done;
        int index;
        Subscription s;
        final int size;

        PublisherBufferExactSubscriber(Subscriber subscriber, int i, Callable callable) {
            this.actual = subscriber;
            this.size = i;
            this.bufferSupplier = callable;
        }

        public void cancel() {
            this.s.cancel();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                Collection collection = this.buffer;
                if (collection != null && !collection.isEmpty()) {
                    this.actual.onNext(collection);
                }
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                Collection collection = this.buffer;
                if (collection == null) {
                    try {
                        Object call = this.bufferSupplier.call();
                        ObjectHelper.requireNonNull(call, "The bufferSupplier returned a null buffer");
                        collection = (Collection) call;
                        this.buffer = collection;
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        cancel();
                        onError(th);
                        return;
                    }
                }
                collection.add(obj);
                int i = this.index + 1;
                if (i == this.size) {
                    this.index = 0;
                    this.buffer = null;
                    this.actual.onNext(collection);
                } else {
                    this.index = i;
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                this.s.request(BackpressureHelper.multiplyCap(j, (long) this.size));
            }
        }
    }

    final class PublisherBufferOverlappingSubscriber extends AtomicLong implements FlowableSubscriber, Subscription, BooleanSupplier {
        private static final long serialVersionUID = -7370244972039324525L;
        final Subscriber actual;
        final Callable bufferSupplier;
        final ArrayDeque buffers = new ArrayDeque();
        volatile boolean cancelled;
        boolean done;
        int index;
        final AtomicBoolean once = new AtomicBoolean();
        long produced;
        Subscription s;
        final int size;
        final int skip;

        PublisherBufferOverlappingSubscriber(Subscriber subscriber, int i, int i2, Callable callable) {
            this.actual = subscriber;
            this.size = i;
            this.skip = i2;
            this.bufferSupplier = callable;
        }

        public void cancel() {
            this.cancelled = true;
            this.s.cancel();
        }

        public boolean getAsBoolean() {
            return this.cancelled;
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                long j = this.produced;
                if (j != 0) {
                    BackpressureHelper.produced(this, j);
                }
                QueueDrainHelper.postComplete(this.actual, this.buffers, this, this);
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.buffers.clear();
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                ArrayDeque arrayDeque = this.buffers;
                int i = this.index;
                int i2 = i + 1;
                if (i == 0) {
                    try {
                        Object call = this.bufferSupplier.call();
                        ObjectHelper.requireNonNull(call, "The bufferSupplier returned a null buffer");
                        arrayDeque.offer((Collection) call);
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        cancel();
                        onError(th);
                        return;
                    }
                }
                Collection collection = (Collection) arrayDeque.peek();
                if (collection != null && collection.size() + 1 == this.size) {
                    arrayDeque.poll();
                    collection.add(obj);
                    this.produced++;
                    this.actual.onNext(collection);
                }
                Iterator it = arrayDeque.iterator();
                while (it.hasNext()) {
                    ((Collection) it.next()).add(obj);
                }
                if (i2 == this.skip) {
                    i2 = 0;
                }
                this.index = i2;
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void request(long j) {
            long j2;
            if (SubscriptionHelper.validate(j)) {
                if (!QueueDrainHelper.postCompleteRequest(j, this.actual, this.buffers, this, this)) {
                    if (this.once.get() || !this.once.compareAndSet(false, true)) {
                        j2 = BackpressureHelper.multiplyCap((long) this.skip, j);
                    } else {
                        j2 = BackpressureHelper.addCap((long) this.size, BackpressureHelper.multiplyCap((long) this.skip, j - 1));
                    }
                    this.s.request(j2);
                }
            }
        }
    }

    final class PublisherBufferSkipSubscriber extends AtomicInteger implements FlowableSubscriber, Subscription {
        private static final long serialVersionUID = -5616169793639412593L;
        final Subscriber actual;
        Collection buffer;
        final Callable bufferSupplier;
        boolean done;
        int index;
        Subscription s;
        final int size;
        final int skip;

        PublisherBufferSkipSubscriber(Subscriber subscriber, int i, int i2, Callable callable) {
            this.actual = subscriber;
            this.size = i;
            this.skip = i2;
            this.bufferSupplier = callable;
        }

        public void cancel() {
            this.s.cancel();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                Collection collection = this.buffer;
                this.buffer = null;
                if (collection != null) {
                    this.actual.onNext(collection);
                }
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.buffer = null;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                Collection collection = this.buffer;
                int i = this.index;
                int i2 = i + 1;
                if (i == 0) {
                    try {
                        Object call = this.bufferSupplier.call();
                        ObjectHelper.requireNonNull(call, "The bufferSupplier returned a null buffer");
                        collection = (Collection) call;
                        this.buffer = collection;
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        cancel();
                        onError(th);
                        return;
                    }
                }
                if (collection != null) {
                    collection.add(obj);
                    if (collection.size() == this.size) {
                        this.buffer = null;
                        this.actual.onNext(collection);
                    }
                }
                if (i2 == this.skip) {
                    i2 = 0;
                }
                this.index = i2;
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void request(long j) {
            if (!SubscriptionHelper.validate(j)) {
                return;
            }
            if (get() != 0 || !compareAndSet(0, 1)) {
                this.s.request(BackpressureHelper.multiplyCap((long) this.skip, j));
                return;
            }
            this.s.request(BackpressureHelper.addCap(BackpressureHelper.multiplyCap(j, (long) this.size), BackpressureHelper.multiplyCap((long) (this.skip - this.size), j - 1)));
        }
    }

    public FlowableBuffer(Flowable flowable, int i, int i2, Callable callable) {
        super(flowable);
        this.size = i;
        this.skip = i2;
        this.bufferSupplier = callable;
    }

    public void subscribeActual(Subscriber subscriber) {
        int i = this.size;
        int i2 = this.skip;
        if (i == i2) {
            this.source.subscribe((FlowableSubscriber) new PublisherBufferExactSubscriber(subscriber, i, this.bufferSupplier));
        } else {
            this.source.subscribe(i2 > i ? new PublisherBufferSkipSubscriber(subscriber, i, i2, this.bufferSupplier) : new PublisherBufferOverlappingSubscriber(subscriber, i, i2, this.bufferSupplier));
        }
    }
}
