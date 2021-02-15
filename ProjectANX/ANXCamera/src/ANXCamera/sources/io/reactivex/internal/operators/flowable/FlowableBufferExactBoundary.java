package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.subscribers.QueueDrainSubscriber;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.subscribers.DisposableSubscriber;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.Collection;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableBufferExactBoundary extends AbstractFlowableWithUpstream {
    final Publisher boundary;
    final Callable bufferSupplier;

    final class BufferBoundarySubscriber extends DisposableSubscriber {
        final BufferExactBoundarySubscriber parent;

        BufferBoundarySubscriber(BufferExactBoundarySubscriber bufferExactBoundarySubscriber) {
            this.parent = bufferExactBoundarySubscriber;
        }

        public void onComplete() {
            this.parent.onComplete();
        }

        public void onError(Throwable th) {
            this.parent.onError(th);
        }

        public void onNext(Object obj) {
            this.parent.next();
        }
    }

    final class BufferExactBoundarySubscriber extends QueueDrainSubscriber implements FlowableSubscriber, Subscription, Disposable {
        final Publisher boundary;
        Collection buffer;
        final Callable bufferSupplier;
        Disposable other;
        Subscription s;

        BufferExactBoundarySubscriber(Subscriber subscriber, Callable callable, Publisher publisher) {
            super(subscriber, new MpscLinkedQueue());
            this.bufferSupplier = callable;
            this.boundary = publisher;
        }

        public boolean accept(Subscriber subscriber, Collection collection) {
            this.actual.onNext(collection);
            return true;
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.other.dispose();
                this.s.cancel();
                if (enter()) {
                    this.queue.clear();
                }
            }
        }

        public void dispose() {
            cancel();
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void next() {
            try {
                Object call = this.bufferSupplier.call();
                ObjectHelper.requireNonNull(call, "The buffer supplied is null");
                Collection collection = (Collection) call;
                synchronized (this) {
                    Collection collection2 = this.buffer;
                    if (collection2 != null) {
                        this.buffer = collection;
                        fastPathEmitMax(collection2, false, this);
                    }
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                cancel();
                this.actual.onError(th);
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
                try {
                    Object call = this.bufferSupplier.call();
                    ObjectHelper.requireNonNull(call, "The buffer supplied is null");
                    this.buffer = (Collection) call;
                    BufferBoundarySubscriber bufferBoundarySubscriber = new BufferBoundarySubscriber(this);
                    this.other = bufferBoundarySubscriber;
                    this.actual.onSubscribe(this);
                    if (!this.cancelled) {
                        subscription.request(Long.MAX_VALUE);
                        this.boundary.subscribe(bufferBoundarySubscriber);
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.cancelled = true;
                    subscription.cancel();
                    EmptySubscription.error(th, this.actual);
                }
            }
        }

        public void request(long j) {
            requested(j);
        }
    }

    public FlowableBufferExactBoundary(Flowable flowable, Publisher publisher, Callable callable) {
        super(flowable);
        this.boundary = publisher;
        this.bufferSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new BufferExactBoundarySubscriber(new SerializedSubscriber(subscriber), this.bufferSupplier, this.boundary));
    }
}
