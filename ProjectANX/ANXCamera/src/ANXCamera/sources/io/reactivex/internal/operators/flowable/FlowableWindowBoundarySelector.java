package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Function;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableWindowBoundarySelector extends AbstractFlowableWithUpstream {
    final int bufferSize;
    final Function close;
    final Publisher open;

    final class OperatorWindowBoundaryCloseSubscriber extends DisposableSubscriber {
        boolean done;
        final WindowBoundaryMainSubscriber parent;
        final UnicastProcessor w;

        OperatorWindowBoundaryCloseSubscriber(WindowBoundaryMainSubscriber windowBoundaryMainSubscriber, UnicastProcessor unicastProcessor) {
            this.parent = windowBoundaryMainSubscriber;
            this.w = unicastProcessor;
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.close(this);
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.parent.error(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                this.done = true;
                cancel();
                this.parent.close(this);
            }
        }
    }

    final class OperatorWindowBoundaryOpenSubscriber extends DisposableSubscriber {
        boolean done;
        final WindowBoundaryMainSubscriber parent;

        OperatorWindowBoundaryOpenSubscriber(WindowBoundaryMainSubscriber windowBoundaryMainSubscriber) {
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
            this.parent.error(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                this.parent.open(obj);
            }
        }
    }

    final class WindowBoundaryMainSubscriber extends QueueDrainSubscriber implements Subscription {
        final AtomicReference boundary = new AtomicReference();
        final int bufferSize;
        final Function close;
        final Publisher open;
        final CompositeDisposable resources;
        Subscription s;
        final AtomicLong windows = new AtomicLong();
        final List ws;

        WindowBoundaryMainSubscriber(Subscriber subscriber, Publisher publisher, Function function, int i) {
            super(subscriber, new MpscLinkedQueue());
            this.open = publisher;
            this.close = function;
            this.bufferSize = i;
            this.resources = new CompositeDisposable();
            this.ws = new ArrayList();
            this.windows.lazySet(1);
        }

        public boolean accept(Subscriber subscriber, Object obj) {
            return false;
        }

        public void cancel() {
            this.cancelled = true;
        }

        /* access modifiers changed from: 0000 */
        public void close(OperatorWindowBoundaryCloseSubscriber operatorWindowBoundaryCloseSubscriber) {
            this.resources.delete(operatorWindowBoundaryCloseSubscriber);
            this.queue.offer(new WindowOperation(operatorWindowBoundaryCloseSubscriber.w, null));
            if (enter()) {
                drainLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        public void dispose() {
            this.resources.dispose();
            DisposableHelper.dispose(this.boundary);
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            MissingBackpressureException th;
            SimplePlainQueue simplePlainQueue = this.queue;
            Subscriber subscriber = this.actual;
            List<UnicastProcessor> list = this.ws;
            int i = 1;
            while (true) {
                boolean z = this.done;
                Object poll = simplePlainQueue.poll();
                boolean z2 = poll == null;
                if (z && z2) {
                    dispose();
                    Throwable th2 = this.error;
                    if (th2 != null) {
                        for (UnicastProcessor onError : list) {
                            onError.onError(th2);
                        }
                    } else {
                        for (UnicastProcessor onComplete : list) {
                            onComplete.onComplete();
                        }
                    }
                    list.clear();
                    return;
                } else if (z2) {
                    i = leave(-i);
                    if (i == 0) {
                        return;
                    }
                } else if (poll instanceof WindowOperation) {
                    WindowOperation windowOperation = (WindowOperation) poll;
                    UnicastProcessor unicastProcessor = windowOperation.w;
                    if (unicastProcessor != null) {
                        if (list.remove(unicastProcessor)) {
                            windowOperation.w.onComplete();
                            if (this.windows.decrementAndGet() == 0) {
                                dispose();
                                return;
                            }
                        } else {
                            continue;
                        }
                    } else if (!this.cancelled) {
                        UnicastProcessor create = UnicastProcessor.create(this.bufferSize);
                        long requested = requested();
                        if (requested != 0) {
                            list.add(create);
                            subscriber.onNext(create);
                            if (requested != Long.MAX_VALUE) {
                                produced(1);
                            }
                            try {
                                Object apply = this.close.apply(windowOperation.open);
                                ObjectHelper.requireNonNull(apply, "The publisher supplied is null");
                                Publisher publisher = (Publisher) apply;
                                OperatorWindowBoundaryCloseSubscriber operatorWindowBoundaryCloseSubscriber = new OperatorWindowBoundaryCloseSubscriber(this, create);
                                if (this.resources.add(operatorWindowBoundaryCloseSubscriber)) {
                                    this.windows.getAndIncrement();
                                    publisher.subscribe(operatorWindowBoundaryCloseSubscriber);
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                this.cancelled = true;
                            }
                        } else {
                            this.cancelled = true;
                            th = new MissingBackpressureException("Could not deliver new window due to lack of requests");
                            subscriber.onError(th);
                        }
                    }
                } else {
                    for (UnicastProcessor unicastProcessor2 : list) {
                        NotificationLite.getValue(poll);
                        unicastProcessor2.onNext(poll);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void error(Throwable th) {
            this.s.cancel();
            this.resources.dispose();
            DisposableHelper.dispose(this.boundary);
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                if (enter()) {
                    drainLoop();
                }
                if (this.windows.decrementAndGet() == 0) {
                    this.resources.dispose();
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
                this.resources.dispose();
            }
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                if (fastEnter()) {
                    for (UnicastProcessor onNext : this.ws) {
                        onNext.onNext(obj);
                    }
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
                this.actual.onSubscribe(this);
                if (!this.cancelled) {
                    OperatorWindowBoundaryOpenSubscriber operatorWindowBoundaryOpenSubscriber = new OperatorWindowBoundaryOpenSubscriber(this);
                    if (this.boundary.compareAndSet(null, operatorWindowBoundaryOpenSubscriber)) {
                        this.windows.getAndIncrement();
                        subscription.request(Long.MAX_VALUE);
                        this.open.subscribe(operatorWindowBoundaryOpenSubscriber);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void open(Object obj) {
            this.queue.offer(new WindowOperation(null, obj));
            if (enter()) {
                drainLoop();
            }
        }

        public void request(long j) {
            requested(j);
        }
    }

    final class WindowOperation {
        final Object open;
        final UnicastProcessor w;

        WindowOperation(UnicastProcessor unicastProcessor, Object obj) {
            this.w = unicastProcessor;
            this.open = obj;
        }
    }

    public FlowableWindowBoundarySelector(Flowable flowable, Publisher publisher, Function function, int i) {
        super(flowable);
        this.open = publisher;
        this.close = function;
        this.bufferSize = i;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new WindowBoundaryMainSubscriber(new SerializedSubscriber(subscriber), this.open, this.close, this.bufferSize));
    }
}
