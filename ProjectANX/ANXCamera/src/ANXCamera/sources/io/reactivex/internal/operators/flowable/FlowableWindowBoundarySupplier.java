package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.subscribers.QueueDrainSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.processors.UnicastProcessor;
import io.reactivex.subscribers.DisposableSubscriber;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableWindowBoundarySupplier extends AbstractFlowableWithUpstream {
    final int bufferSize;
    final Callable other;

    final class WindowBoundaryInnerSubscriber extends DisposableSubscriber {
        boolean done;
        final WindowBoundaryMainSubscriber parent;

        WindowBoundaryInnerSubscriber(WindowBoundaryMainSubscriber windowBoundaryMainSubscriber) {
            this.parent = windowBoundaryMainSubscriber;
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.parent.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                this.done = true;
                cancel();
                this.parent.next();
            }
        }
    }

    final class WindowBoundaryMainSubscriber extends QueueDrainSubscriber implements Subscription {
        static final Object NEXT = new Object();
        final AtomicReference boundary = new AtomicReference();
        final int bufferSize;
        final Callable other;
        Subscription s;
        UnicastProcessor window;
        final AtomicLong windows = new AtomicLong();

        WindowBoundaryMainSubscriber(Subscriber subscriber, Callable callable, int i) {
            super(subscriber, new MpscLinkedQueue());
            this.other = callable;
            this.bufferSize = i;
            this.windows.lazySet(1);
        }

        public void cancel() {
            this.cancelled = true;
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            SimplePlainQueue simplePlainQueue = this.queue;
            Subscriber subscriber = this.actual;
            UnicastProcessor unicastProcessor = this.window;
            int i = 1;
            while (true) {
                boolean z = this.done;
                Object poll = simplePlainQueue.poll();
                boolean z2 = poll == null;
                if (z && z2) {
                    DisposableHelper.dispose(this.boundary);
                    Throwable th = this.error;
                    if (th != null) {
                        unicastProcessor.onError(th);
                    } else {
                        unicastProcessor.onComplete();
                    }
                    return;
                } else if (z2) {
                    i = leave(-i);
                    if (i == 0) {
                        return;
                    }
                } else if (poll == NEXT) {
                    unicastProcessor.onComplete();
                    if (this.windows.decrementAndGet() == 0) {
                        DisposableHelper.dispose(this.boundary);
                        return;
                    } else if (!this.cancelled) {
                        try {
                            Object call = this.other.call();
                            ObjectHelper.requireNonNull(call, "The publisher supplied is null");
                            Publisher publisher = (Publisher) call;
                            UnicastProcessor create = UnicastProcessor.create(this.bufferSize);
                            long requested = requested();
                            if (requested != 0) {
                                this.windows.getAndIncrement();
                                subscriber.onNext(create);
                                if (requested != Long.MAX_VALUE) {
                                    produced(1);
                                }
                                this.window = create;
                                WindowBoundaryInnerSubscriber windowBoundaryInnerSubscriber = new WindowBoundaryInnerSubscriber(this);
                                AtomicReference atomicReference = this.boundary;
                                if (atomicReference.compareAndSet(atomicReference.get(), windowBoundaryInnerSubscriber)) {
                                    publisher.subscribe(windowBoundaryInnerSubscriber);
                                }
                            } else {
                                this.cancelled = true;
                                subscriber.onError(new MissingBackpressureException("Could not deliver new window due to lack of requests"));
                            }
                            unicastProcessor = create;
                        } catch (Throwable th2) {
                            Exceptions.throwIfFatal(th2);
                            DisposableHelper.dispose(this.boundary);
                            subscriber.onError(th2);
                            return;
                        }
                    }
                } else {
                    NotificationLite.getValue(poll);
                    unicastProcessor.onNext(poll);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void next() {
            this.queue.offer(NEXT);
            if (enter()) {
                drainLoop();
            }
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                if (enter()) {
                    drainLoop();
                }
                if (this.windows.decrementAndGet() == 0) {
                    DisposableHelper.dispose(this.boundary);
                }
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.error = th;
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            if (this.windows.decrementAndGet() == 0) {
                DisposableHelper.dispose(this.boundary);
            }
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                if (fastEnter()) {
                    this.window.onNext(obj);
                    if (leave(-1) == 0) {
                        return;
                    }
                } else {
                    SimplePlainQueue simplePlainQueue = this.queue;
                    NotificationLite.next(obj);
                    simplePlainQueue.offer(obj);
                    if (!enter()) {
                        return;
                    }
                }
                drainLoop();
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                Subscriber subscriber = this.actual;
                subscriber.onSubscribe(this);
                if (!this.cancelled) {
                    try {
                        Object call = this.other.call();
                        ObjectHelper.requireNonNull(call, "The first window publisher supplied is null");
                        Publisher publisher = (Publisher) call;
                        UnicastProcessor create = UnicastProcessor.create(this.bufferSize);
                        long requested = requested();
                        if (requested != 0) {
                            subscriber.onNext(create);
                            if (requested != Long.MAX_VALUE) {
                                produced(1);
                            }
                            this.window = create;
                            WindowBoundaryInnerSubscriber windowBoundaryInnerSubscriber = new WindowBoundaryInnerSubscriber(this);
                            if (this.boundary.compareAndSet(null, windowBoundaryInnerSubscriber)) {
                                this.windows.getAndIncrement();
                                subscription.request(Long.MAX_VALUE);
                                publisher.subscribe(windowBoundaryInnerSubscriber);
                            }
                        } else {
                            subscription.cancel();
                            th = new MissingBackpressureException("Could not deliver first window due to lack of requests");
                            subscriber.onError(th);
                        }
                    } catch (Throwable th) {
                        th = th;
                        Exceptions.throwIfFatal(th);
                        subscription.cancel();
                    }
                }
            }
        }

        public void request(long j) {
            requested(j);
        }
    }

    public FlowableWindowBoundarySupplier(Flowable flowable, Callable callable, int i) {
        super(flowable);
        this.other = callable;
        this.bufferSize = i;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new WindowBoundaryMainSubscriber(new SerializedSubscriber(subscriber), this.other, this.bufferSize));
    }
}
