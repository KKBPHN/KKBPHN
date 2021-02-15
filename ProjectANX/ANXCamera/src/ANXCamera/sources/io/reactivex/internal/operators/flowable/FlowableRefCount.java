package io.reactivex.internal.operators.flowable;

import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableRefCount extends AbstractFlowableWithUpstream {
    volatile CompositeDisposable baseDisposable = new CompositeDisposable();
    final ReentrantLock lock = new ReentrantLock();
    final ConnectableFlowable source;
    final AtomicInteger subscriptionCount = new AtomicInteger();

    final class ConnectionSubscriber extends AtomicReference implements FlowableSubscriber, Subscription {
        private static final long serialVersionUID = 152064694420235350L;
        final CompositeDisposable currentBase;
        final AtomicLong requested = new AtomicLong();
        final Disposable resource;
        final Subscriber subscriber;

        ConnectionSubscriber(Subscriber subscriber2, CompositeDisposable compositeDisposable, Disposable disposable) {
            this.subscriber = subscriber2;
            this.currentBase = compositeDisposable;
            this.resource = disposable;
        }

        public void cancel() {
            SubscriptionHelper.cancel(this);
            this.resource.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void cleanup() {
            FlowableRefCount.this.lock.lock();
            try {
                if (FlowableRefCount.this.baseDisposable == this.currentBase) {
                    if (FlowableRefCount.this.source instanceof Disposable) {
                        ((Disposable) FlowableRefCount.this.source).dispose();
                    }
                    FlowableRefCount.this.baseDisposable.dispose();
                    FlowableRefCount.this.baseDisposable = new CompositeDisposable();
                    FlowableRefCount.this.subscriptionCount.set(0);
                }
            } finally {
                FlowableRefCount.this.lock.unlock();
            }
        }

        public void onComplete() {
            cleanup();
            this.subscriber.onComplete();
        }

        public void onError(Throwable th) {
            cleanup();
            this.subscriber.onError(th);
        }

        public void onNext(Object obj) {
            this.subscriber.onNext(obj);
        }

        public void onSubscribe(Subscription subscription) {
            SubscriptionHelper.deferredSetOnce(this, this.requested, subscription);
        }

        public void request(long j) {
            SubscriptionHelper.deferredRequest(this, this.requested, j);
        }
    }

    final class DisposeConsumer implements Consumer {
        private final Subscriber subscriber;
        private final AtomicBoolean writeLocked;

        DisposeConsumer(Subscriber subscriber2, AtomicBoolean atomicBoolean) {
            this.subscriber = subscriber2;
            this.writeLocked = atomicBoolean;
        }

        public void accept(Disposable disposable) {
            try {
                FlowableRefCount.this.baseDisposable.add(disposable);
                FlowableRefCount.this.doSubscribe(this.subscriber, FlowableRefCount.this.baseDisposable);
            } finally {
                FlowableRefCount.this.lock.unlock();
                this.writeLocked.set(false);
            }
        }
    }

    final class DisposeTask implements Runnable {
        private final CompositeDisposable current;

        DisposeTask(CompositeDisposable compositeDisposable) {
            this.current = compositeDisposable;
        }

        public void run() {
            FlowableRefCount.this.lock.lock();
            try {
                if (FlowableRefCount.this.baseDisposable == this.current && FlowableRefCount.this.subscriptionCount.decrementAndGet() == 0) {
                    if (FlowableRefCount.this.source instanceof Disposable) {
                        ((Disposable) FlowableRefCount.this.source).dispose();
                    }
                    FlowableRefCount.this.baseDisposable.dispose();
                    FlowableRefCount.this.baseDisposable = new CompositeDisposable();
                }
            } finally {
                FlowableRefCount.this.lock.unlock();
            }
        }
    }

    public FlowableRefCount(ConnectableFlowable connectableFlowable) {
        super(connectableFlowable);
        this.source = connectableFlowable;
    }

    private Disposable disconnect(CompositeDisposable compositeDisposable) {
        return Disposables.fromRunnable(new DisposeTask(compositeDisposable));
    }

    private Consumer onSubscribe(Subscriber subscriber, AtomicBoolean atomicBoolean) {
        return new DisposeConsumer(subscriber, atomicBoolean);
    }

    /* access modifiers changed from: 0000 */
    public void doSubscribe(Subscriber subscriber, CompositeDisposable compositeDisposable) {
        ConnectionSubscriber connectionSubscriber = new ConnectionSubscriber(subscriber, compositeDisposable, disconnect(compositeDisposable));
        subscriber.onSubscribe(connectionSubscriber);
        this.source.subscribe((FlowableSubscriber) connectionSubscriber);
    }

    public void subscribeActual(Subscriber subscriber) {
        this.lock.lock();
        if (this.subscriptionCount.incrementAndGet() == 1) {
            AtomicBoolean atomicBoolean = new AtomicBoolean(true);
            try {
                this.source.connect(onSubscribe(subscriber, atomicBoolean));
            } finally {
                if (atomicBoolean.get()) {
                    this.lock.unlock();
                }
            }
        } else {
            try {
                doSubscribe(subscriber, this.baseDisposable);
            } catch (Throwable th) {
                this.lock.unlock();
                throw th;
            }
        }
    }
}
