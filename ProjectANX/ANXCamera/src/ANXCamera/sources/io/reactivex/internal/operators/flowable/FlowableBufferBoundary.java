package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableBufferBoundary extends AbstractFlowableWithUpstream {
    final Function bufferClose;
    final Publisher bufferOpen;
    final Callable bufferSupplier;

    final class BufferBoundarySubscriber extends AtomicInteger implements FlowableSubscriber, Subscription {
        private static final long serialVersionUID = -8466418554264089604L;
        final Subscriber actual;
        final Function bufferClose;
        final Publisher bufferOpen;
        final Callable bufferSupplier;
        Map buffers = new LinkedHashMap();
        volatile boolean cancelled;
        volatile boolean done;
        long emitted;
        final AtomicThrowable errors = new AtomicThrowable();
        long index;
        final SpscLinkedArrayQueue queue = new SpscLinkedArrayQueue(Flowable.bufferSize());
        final AtomicLong requested = new AtomicLong();
        final CompositeDisposable subscribers = new CompositeDisposable();
        final AtomicReference upstream = new AtomicReference();

        final class BufferOpenSubscriber extends AtomicReference implements FlowableSubscriber, Disposable {
            private static final long serialVersionUID = -8498650778633225126L;
            final BufferBoundarySubscriber parent;

            BufferOpenSubscriber(BufferBoundarySubscriber bufferBoundarySubscriber) {
                this.parent = bufferBoundarySubscriber;
            }

            public void dispose() {
                SubscriptionHelper.cancel(this);
            }

            public boolean isDisposed() {
                return get() == SubscriptionHelper.CANCELLED;
            }

            public void onComplete() {
                lazySet(SubscriptionHelper.CANCELLED);
                this.parent.openComplete(this);
            }

            public void onError(Throwable th) {
                lazySet(SubscriptionHelper.CANCELLED);
                this.parent.boundaryError(this, th);
            }

            public void onNext(Object obj) {
                this.parent.open(obj);
            }

            public void onSubscribe(Subscription subscription) {
                if (SubscriptionHelper.setOnce(this, subscription)) {
                    subscription.request(Long.MAX_VALUE);
                }
            }
        }

        BufferBoundarySubscriber(Subscriber subscriber, Publisher publisher, Function function, Callable callable) {
            this.actual = subscriber;
            this.bufferSupplier = callable;
            this.bufferOpen = publisher;
            this.bufferClose = function;
        }

        /* access modifiers changed from: 0000 */
        public void boundaryError(Disposable disposable, Throwable th) {
            SubscriptionHelper.cancel(this.upstream);
            this.subscribers.delete(disposable);
            onError(th);
        }

        public void cancel() {
            if (SubscriptionHelper.cancel(this.upstream)) {
                this.cancelled = true;
                this.subscribers.dispose();
                synchronized (this) {
                    this.buffers = null;
                }
                if (getAndIncrement() != 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x002d, code lost:
            if (r4 == false) goto L_0x0031;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x002f, code lost:
            r3.done = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0031, code lost:
            drain();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0034, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void close(BufferCloseSubscriber bufferCloseSubscriber, long j) {
            boolean z;
            this.subscribers.delete(bufferCloseSubscriber);
            if (this.subscribers.size() == 0) {
                SubscriptionHelper.cancel(this.upstream);
                z = true;
            } else {
                z = false;
            }
            synchronized (this) {
                if (this.buffers != null) {
                    this.queue.offer(this.buffers.remove(Long.valueOf(j)));
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            int i;
            if (getAndIncrement() == 0) {
                long j = this.emitted;
                Subscriber subscriber = this.actual;
                SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
                int i2 = 1;
                loop0:
                do {
                    long j2 = this.requested.get();
                    while (true) {
                        i = (j > j2 ? 1 : (j == j2 ? 0 : -1));
                        if (i != 0) {
                            if (!this.cancelled) {
                                boolean z = this.done;
                                if (z && this.errors.get() != null) {
                                    break loop0;
                                }
                                Collection collection = (Collection) spscLinkedArrayQueue.poll();
                                boolean z2 = collection == null;
                                if (z && z2) {
                                    subscriber.onComplete();
                                    return;
                                } else if (z2) {
                                    break;
                                } else {
                                    subscriber.onNext(collection);
                                    j++;
                                }
                            } else {
                                spscLinkedArrayQueue.clear();
                                return;
                            }
                        } else {
                            break;
                        }
                    }
                    if (i == 0) {
                        if (this.cancelled) {
                            spscLinkedArrayQueue.clear();
                            return;
                        } else if (this.done) {
                            if (this.errors.get() != null) {
                                spscLinkedArrayQueue.clear();
                                subscriber.onError(this.errors.terminate());
                                return;
                            } else if (spscLinkedArrayQueue.isEmpty()) {
                                subscriber.onComplete();
                                return;
                            }
                        }
                    }
                    this.emitted = j;
                    i2 = addAndGet(-i2);
                } while (i2 != 0);
            }
        }

        public void onComplete() {
            this.subscribers.dispose();
            synchronized (this) {
                Map map = this.buffers;
                if (map != null) {
                    for (Collection offer : map.values()) {
                        this.queue.offer(offer);
                    }
                    this.buffers = null;
                    this.done = true;
                    drain();
                }
            }
        }

        public void onError(Throwable th) {
            if (this.errors.addThrowable(th)) {
                this.subscribers.dispose();
                synchronized (this) {
                    this.buffers = null;
                }
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onNext(Object obj) {
            synchronized (this) {
                Map map = this.buffers;
                if (map != null) {
                    for (Collection add : map.values()) {
                        add.add(obj);
                    }
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this.upstream, subscription)) {
                BufferOpenSubscriber bufferOpenSubscriber = new BufferOpenSubscriber(this);
                this.subscribers.add(bufferOpenSubscriber);
                this.bufferOpen.subscribe(bufferOpenSubscriber);
                subscription.request(Long.MAX_VALUE);
            }
        }

        /* access modifiers changed from: 0000 */
        public void open(Object obj) {
            try {
                Object call = this.bufferSupplier.call();
                ObjectHelper.requireNonNull(call, "The bufferSupplier returned a null Collection");
                Collection collection = (Collection) call;
                Object apply = this.bufferClose.apply(obj);
                ObjectHelper.requireNonNull(apply, "The bufferClose returned a null Publisher");
                Publisher publisher = (Publisher) apply;
                long j = this.index;
                this.index = 1 + j;
                synchronized (this) {
                    Map map = this.buffers;
                    if (map != null) {
                        map.put(Long.valueOf(j), collection);
                        BufferCloseSubscriber bufferCloseSubscriber = new BufferCloseSubscriber(this, j);
                        this.subscribers.add(bufferCloseSubscriber);
                        publisher.subscribe(bufferCloseSubscriber);
                    }
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                SubscriptionHelper.cancel(this.upstream);
                onError(th);
            }
        }

        /* access modifiers changed from: 0000 */
        public void openComplete(BufferOpenSubscriber bufferOpenSubscriber) {
            this.subscribers.delete(bufferOpenSubscriber);
            if (this.subscribers.size() == 0) {
                SubscriptionHelper.cancel(this.upstream);
                this.done = true;
                drain();
            }
        }

        public void request(long j) {
            BackpressureHelper.add(this.requested, j);
            drain();
        }
    }

    final class BufferCloseSubscriber extends AtomicReference implements FlowableSubscriber, Disposable {
        private static final long serialVersionUID = -8498650778633225126L;
        final long index;
        final BufferBoundarySubscriber parent;

        BufferCloseSubscriber(BufferBoundarySubscriber bufferBoundarySubscriber, long j) {
            this.parent = bufferBoundarySubscriber;
            this.index = j;
        }

        public void dispose() {
            SubscriptionHelper.cancel(this);
        }

        public boolean isDisposed() {
            return get() == SubscriptionHelper.CANCELLED;
        }

        public void onComplete() {
            Object obj = get();
            SubscriptionHelper subscriptionHelper = SubscriptionHelper.CANCELLED;
            if (obj != subscriptionHelper) {
                lazySet(subscriptionHelper);
                this.parent.close(this, this.index);
            }
        }

        public void onError(Throwable th) {
            Object obj = get();
            SubscriptionHelper subscriptionHelper = SubscriptionHelper.CANCELLED;
            if (obj != subscriptionHelper) {
                lazySet(subscriptionHelper);
                this.parent.boundaryError(this, th);
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onNext(Object obj) {
            Subscription subscription = (Subscription) get();
            SubscriptionHelper subscriptionHelper = SubscriptionHelper.CANCELLED;
            if (subscription != subscriptionHelper) {
                lazySet(subscriptionHelper);
                subscription.cancel();
                this.parent.close(this, this.index);
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this, subscription)) {
                subscription.request(Long.MAX_VALUE);
            }
        }
    }

    public FlowableBufferBoundary(Flowable flowable, Publisher publisher, Function function, Callable callable) {
        super(flowable);
        this.bufferOpen = publisher;
        this.bufferClose = function;
        this.bufferSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        BufferBoundarySubscriber bufferBoundarySubscriber = new BufferBoundarySubscriber(subscriber, this.bufferOpen, this.bufferClose, this.bufferSupplier);
        subscriber.onSubscribe(bufferBoundarySubscriber);
        this.source.subscribe((FlowableSubscriber) bufferBoundarySubscriber);
    }
}
