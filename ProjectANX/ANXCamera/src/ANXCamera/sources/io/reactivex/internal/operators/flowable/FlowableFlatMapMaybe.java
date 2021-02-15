package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableFlatMapMaybe extends AbstractFlowableWithUpstream {
    final boolean delayErrors;
    final Function mapper;
    final int maxConcurrency;

    final class FlatMapMaybeSubscriber extends AtomicInteger implements FlowableSubscriber, Subscription {
        private static final long serialVersionUID = 8600231336733376951L;
        final AtomicInteger active = new AtomicInteger(1);
        final Subscriber actual;
        volatile boolean cancelled;
        final boolean delayErrors;
        final AtomicThrowable errors = new AtomicThrowable();
        final Function mapper;
        final int maxConcurrency;
        final AtomicReference queue = new AtomicReference();
        final AtomicLong requested = new AtomicLong();
        Subscription s;
        final CompositeDisposable set = new CompositeDisposable();

        final class InnerObserver extends AtomicReference implements MaybeObserver, Disposable {
            private static final long serialVersionUID = -502562646270949838L;

            InnerObserver() {
            }

            public void dispose() {
                DisposableHelper.dispose(this);
            }

            public boolean isDisposed() {
                return DisposableHelper.isDisposed((Disposable) get());
            }

            public void onComplete() {
                FlatMapMaybeSubscriber.this.innerComplete(this);
            }

            public void onError(Throwable th) {
                FlatMapMaybeSubscriber.this.innerError(this, th);
            }

            public void onSubscribe(Disposable disposable) {
                DisposableHelper.setOnce(this, disposable);
            }

            public void onSuccess(Object obj) {
                FlatMapMaybeSubscriber.this.innerSuccess(this, obj);
            }
        }

        FlatMapMaybeSubscriber(Subscriber subscriber, Function function, boolean z, int i) {
            this.actual = subscriber;
            this.mapper = function;
            this.delayErrors = z;
            this.maxConcurrency = i;
        }

        public void cancel() {
            this.cancelled = true;
            this.s.cancel();
            this.set.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void clear() {
            SpscLinkedArrayQueue spscLinkedArrayQueue = (SpscLinkedArrayQueue) this.queue.get();
            if (spscLinkedArrayQueue != null) {
                spscLinkedArrayQueue.clear();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                drainLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            int i;
            boolean z;
            Subscriber subscriber = this.actual;
            AtomicInteger atomicInteger = this.active;
            AtomicReference atomicReference = this.queue;
            int i2 = 1;
            loop0:
            do {
                long j = this.requested.get();
                long j2 = 0;
                while (true) {
                    i = (j2 > j ? 1 : (j2 == j ? 0 : -1));
                    z = false;
                    if (i != 0) {
                        if (!this.cancelled) {
                            if (!this.delayErrors && ((Throwable) this.errors.get()) != null) {
                                break loop0;
                            }
                            boolean z2 = atomicInteger.get() == 0;
                            SpscLinkedArrayQueue spscLinkedArrayQueue = (SpscLinkedArrayQueue) atomicReference.get();
                            Object poll = spscLinkedArrayQueue != null ? spscLinkedArrayQueue.poll() : null;
                            boolean z3 = poll == null;
                            if (z2 && z3) {
                                Throwable terminate = this.errors.terminate();
                                if (terminate != null) {
                                    subscriber.onError(terminate);
                                } else {
                                    subscriber.onComplete();
                                }
                                return;
                            } else if (z3) {
                                break;
                            } else {
                                subscriber.onNext(poll);
                                j2++;
                            }
                        } else {
                            clear();
                            return;
                        }
                    } else {
                        break;
                    }
                }
                if (i == 0) {
                    if (this.cancelled) {
                        clear();
                        return;
                    } else if (this.delayErrors || ((Throwable) this.errors.get()) == null) {
                        boolean z4 = atomicInteger.get() == 0;
                        SpscLinkedArrayQueue spscLinkedArrayQueue2 = (SpscLinkedArrayQueue) atomicReference.get();
                        if (spscLinkedArrayQueue2 == null || spscLinkedArrayQueue2.isEmpty()) {
                            z = true;
                        }
                        if (z4 && z) {
                            Throwable terminate2 = this.errors.terminate();
                            if (terminate2 != null) {
                                subscriber.onError(terminate2);
                            } else {
                                subscriber.onComplete();
                            }
                            return;
                        }
                    } else {
                        Throwable terminate3 = this.errors.terminate();
                        clear();
                        subscriber.onError(terminate3);
                        return;
                    }
                }
                if (j2 != 0) {
                    BackpressureHelper.produced(this.requested, j2);
                    if (this.maxConcurrency != Integer.MAX_VALUE) {
                        this.s.request(j2);
                    }
                }
                i2 = addAndGet(-i2);
            } while (i2 != 0);
        }

        /* access modifiers changed from: 0000 */
        public SpscLinkedArrayQueue getOrCreateQueue() {
            SpscLinkedArrayQueue spscLinkedArrayQueue;
            do {
                SpscLinkedArrayQueue spscLinkedArrayQueue2 = (SpscLinkedArrayQueue) this.queue.get();
                if (spscLinkedArrayQueue2 != null) {
                    return spscLinkedArrayQueue2;
                }
                spscLinkedArrayQueue = new SpscLinkedArrayQueue(Flowable.bufferSize());
            } while (!this.queue.compareAndSet(null, spscLinkedArrayQueue));
            return spscLinkedArrayQueue;
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(InnerObserver innerObserver) {
            this.set.delete(innerObserver);
            if (get() == 0) {
                boolean z = true;
                if (compareAndSet(0, 1)) {
                    if (this.active.decrementAndGet() != 0) {
                        z = false;
                    }
                    SpscLinkedArrayQueue spscLinkedArrayQueue = (SpscLinkedArrayQueue) this.queue.get();
                    if (!z || (spscLinkedArrayQueue != null && !spscLinkedArrayQueue.isEmpty())) {
                        if (this.maxConcurrency != Integer.MAX_VALUE) {
                            this.s.request(1);
                        }
                        if (decrementAndGet() != 0) {
                            drainLoop();
                        }
                        return;
                    }
                    Throwable terminate = this.errors.terminate();
                    Subscriber subscriber = this.actual;
                    if (terminate != null) {
                        subscriber.onError(terminate);
                    } else {
                        subscriber.onComplete();
                    }
                    return;
                }
            }
            this.active.decrementAndGet();
            if (this.maxConcurrency != Integer.MAX_VALUE) {
                this.s.request(1);
            }
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void innerError(InnerObserver innerObserver, Throwable th) {
            this.set.delete(innerObserver);
            if (this.errors.addThrowable(th)) {
                if (!this.delayErrors) {
                    this.s.cancel();
                    this.set.dispose();
                } else if (this.maxConcurrency != Integer.MAX_VALUE) {
                    this.s.request(1);
                }
                this.active.decrementAndGet();
                drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        /* access modifiers changed from: 0000 */
        public void innerSuccess(InnerObserver innerObserver, Object obj) {
            this.set.delete(innerObserver);
            if (get() == 0) {
                boolean z = true;
                if (compareAndSet(0, 1)) {
                    if (this.active.decrementAndGet() != 0) {
                        z = false;
                    }
                    if (this.requested.get() != 0) {
                        this.actual.onNext(obj);
                        SpscLinkedArrayQueue spscLinkedArrayQueue = (SpscLinkedArrayQueue) this.queue.get();
                        if (!z || (spscLinkedArrayQueue != null && !spscLinkedArrayQueue.isEmpty())) {
                            BackpressureHelper.produced(this.requested, 1);
                            if (this.maxConcurrency != Integer.MAX_VALUE) {
                                this.s.request(1);
                            }
                        } else {
                            Throwable terminate = this.errors.terminate();
                            Subscriber subscriber = this.actual;
                            if (terminate != null) {
                                subscriber.onError(terminate);
                            } else {
                                subscriber.onComplete();
                            }
                            return;
                        }
                    } else {
                        SpscLinkedArrayQueue orCreateQueue = getOrCreateQueue();
                        synchronized (orCreateQueue) {
                            orCreateQueue.offer(obj);
                        }
                    }
                    if (decrementAndGet() == 0) {
                        return;
                    }
                    drainLoop();
                }
            }
            SpscLinkedArrayQueue orCreateQueue2 = getOrCreateQueue();
            synchronized (orCreateQueue2) {
                orCreateQueue2.offer(obj);
            }
            this.active.decrementAndGet();
            if (getAndIncrement() != 0) {
                return;
            }
            drainLoop();
        }

        public void onComplete() {
            this.active.decrementAndGet();
            drain();
        }

        public void onError(Throwable th) {
            this.active.decrementAndGet();
            if (this.errors.addThrowable(th)) {
                if (!this.delayErrors) {
                    this.set.dispose();
                }
                drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onNext(Object obj) {
            try {
                Object apply = this.mapper.apply(obj);
                ObjectHelper.requireNonNull(apply, "The mapper returned a null MaybeSource");
                MaybeSource maybeSource = (MaybeSource) apply;
                this.active.getAndIncrement();
                InnerObserver innerObserver = new InnerObserver();
                if (!this.cancelled && this.set.add(innerObserver)) {
                    maybeSource.subscribe(innerObserver);
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.s.cancel();
                onError(th);
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                int i = this.maxConcurrency;
                subscription.request(i == Integer.MAX_VALUE ? Long.MAX_VALUE : (long) i);
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                drain();
            }
        }
    }

    public FlowableFlatMapMaybe(Flowable flowable, Function function, boolean z, int i) {
        super(flowable);
        this.mapper = function;
        this.delayErrors = z;
        this.maxConcurrency = i;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new FlatMapMaybeSubscriber(subscriber, this.mapper, this.delayErrors, this.maxConcurrency));
    }
}
