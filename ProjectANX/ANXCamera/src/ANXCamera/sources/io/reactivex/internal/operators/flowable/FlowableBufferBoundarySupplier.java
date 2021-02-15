package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.subscribers.QueueDrainSubscriber;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subscribers.DisposableSubscriber;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableBufferBoundarySupplier extends AbstractFlowableWithUpstream {
    final Callable boundarySupplier;
    final Callable bufferSupplier;

    final class BufferBoundarySubscriber extends DisposableSubscriber {
        boolean once;
        final BufferBoundarySupplierSubscriber parent;

        BufferBoundarySubscriber(BufferBoundarySupplierSubscriber bufferBoundarySupplierSubscriber) {
            this.parent = bufferBoundarySupplierSubscriber;
        }

        public void onComplete() {
            if (!this.once) {
                this.once = true;
                this.parent.next();
            }
        }

        public void onError(Throwable th) {
            if (this.once) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.once = true;
            this.parent.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.once) {
                this.once = true;
                cancel();
                this.parent.next();
            }
        }
    }

    final class BufferBoundarySupplierSubscriber extends QueueDrainSubscriber implements FlowableSubscriber, Subscription, Disposable {
        final Callable boundarySupplier;
        Collection buffer;
        final Callable bufferSupplier;
        final AtomicReference other = new AtomicReference();
        Subscription s;

        BufferBoundarySupplierSubscriber(Subscriber subscriber, Callable callable, Callable callable2) {
            super(subscriber, new MpscLinkedQueue());
            this.bufferSupplier = callable;
            this.boundarySupplier = callable2;
        }

        public boolean accept(Subscriber subscriber, Collection collection) {
            this.actual.onNext(collection);
            return true;
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.s.cancel();
                disposeOther();
                if (enter()) {
                    this.queue.clear();
                }
            }
        }

        public void dispose() {
            this.s.cancel();
            disposeOther();
        }

        /* access modifiers changed from: 0000 */
        public void disposeOther() {
            DisposableHelper.dispose(this.other);
        }

        public boolean isDisposed() {
            return this.other.get() == DisposableHelper.DISPOSED;
        }

        /* access modifiers changed from: 0000 */
        public void next() {
            try {
                Object call = this.bufferSupplier.call();
                ObjectHelper.requireNonNull(call, "The buffer supplied is null");
                Collection collection = (Collection) call;
                try {
                    Object call2 = this.boundarySupplier.call();
                    ObjectHelper.requireNonNull(call2, "The boundary publisher supplied is null");
                    Publisher publisher = (Publisher) call2;
                    BufferBoundarySubscriber bufferBoundarySubscriber = new BufferBoundarySubscriber(this);
                    if (this.other.compareAndSet((Disposable) this.other.get(), bufferBoundarySubscriber)) {
                        synchronized (this) {
                            Collection collection2 = this.buffer;
                            if (collection2 != null) {
                                this.buffer = collection;
                                publisher.subscribe(bufferBoundarySubscriber);
                                fastPathEmitMax(collection2, false, this);
                            }
                        }
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.cancelled = true;
                    this.s.cancel();
                    this.actual.onError(th);
                }
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                cancel();
                this.actual.onError(th2);
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0019, code lost:
            io.reactivex.internal.util.QueueDrainHelper.drainMaxLoop(r3.queue, r3.actual, false, r3, r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0021, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x000b, code lost:
            r3.queue.offer(r0);
            r3.done = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0017, code lost:
            if (enter() == false) goto L_0x0021;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onComplete() {
            synchronized (this) {
                Collection collection = this.buffer;
                if (collection != null) {
                    this.buffer = null;
                }
            }
        }

        public void onError(Throwable th) {
            cancel();
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
                Subscriber subscriber = this.actual;
                try {
                    Object call = this.bufferSupplier.call();
                    ObjectHelper.requireNonNull(call, "The buffer supplied is null");
                    this.buffer = (Collection) call;
                    Object call2 = this.boundarySupplier.call();
                    ObjectHelper.requireNonNull(call2, "The boundary publisher supplied is null");
                    Publisher publisher = (Publisher) call2;
                    BufferBoundarySubscriber bufferBoundarySubscriber = new BufferBoundarySubscriber(this);
                    this.other.set(bufferBoundarySubscriber);
                    subscriber.onSubscribe(this);
                    if (!this.cancelled) {
                        subscription.request(Long.MAX_VALUE);
                        publisher.subscribe(bufferBoundarySubscriber);
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.cancelled = true;
                    subscription.cancel();
                    EmptySubscription.error(th, subscriber);
                }
            }
        }

        public void request(long j) {
            requested(j);
        }
    }

    public FlowableBufferBoundarySupplier(Flowable flowable, Callable callable, Callable callable2) {
        super(flowable);
        this.boundarySupplier = callable;
        this.bufferSupplier = callable2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new BufferBoundarySupplierSubscriber(new SerializedSubscriber(subscriber), this.bufferSupplier, this.boundarySupplier));
    }
}
