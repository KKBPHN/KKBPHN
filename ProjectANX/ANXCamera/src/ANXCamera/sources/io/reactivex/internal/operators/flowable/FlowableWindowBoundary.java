package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.subscribers.QueueDrainSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.processors.UnicastProcessor;
import io.reactivex.subscribers.DisposableSubscriber;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableWindowBoundary extends AbstractFlowableWithUpstream {
    final int bufferSize;
    final Publisher other;

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
                this.parent.next();
            }
        }
    }

    final class WindowBoundaryMainSubscriber extends QueueDrainSubscriber implements Subscription {
        static final Object NEXT = new Object();
        final AtomicReference boundary = new AtomicReference();
        final int bufferSize;
        final Publisher other;
        Subscription s;
        UnicastProcessor window;
        final AtomicLong windows = new AtomicLong();

        WindowBoundaryMainSubscriber(Subscriber subscriber, Publisher publisher, int i) {
            super(subscriber, new MpscLinkedQueue());
            this.other = publisher;
            this.bufferSize = i;
            this.windows.lazySet(1);
        }

        public boolean accept(Subscriber subscriber, Object obj) {
            return false;
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
                        unicastProcessor = UnicastProcessor.create(this.bufferSize);
                        long requested = requested();
                        if (requested != 0) {
                            this.windows.getAndIncrement();
                            subscriber.onNext(unicastProcessor);
                            if (requested != Long.MAX_VALUE) {
                                produced(1);
                            }
                            this.window = unicastProcessor;
                        } else {
                            this.cancelled = true;
                            subscriber.onError(new MissingBackpressureException("Could not deliver new window due to lack of requests"));
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

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                Subscriber subscriber = this.actual;
                subscriber.onSubscribe(this);
                if (!this.cancelled) {
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
                            this.other.subscribe(windowBoundaryInnerSubscriber);
                        }
                    } else {
                        subscriber.onError(new MissingBackpressureException("Could not deliver first window due to lack of requests"));
                    }
                }
            }
        }

        public void request(long j) {
            requested(j);
        }
    }

    public FlowableWindowBoundary(Flowable flowable, Publisher publisher, int i) {
        super(flowable);
        this.other = publisher;
        this.bufferSize = i;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new WindowBoundaryMainSubscriber(new SerializedSubscriber(subscriber), this.other, this.bufferSize));
    }
}
