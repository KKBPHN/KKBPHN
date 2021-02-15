package io.reactivex.internal.operators.maybe;

import io.reactivex.Flowable;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.NotificationLite;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class MaybeConcatIterable extends Flowable {
    final Iterable sources;

    final class ConcatMaybeObserver extends AtomicInteger implements MaybeObserver, Subscription {
        private static final long serialVersionUID = 3520831347801429610L;
        final Subscriber actual;
        final AtomicReference current = new AtomicReference(NotificationLite.COMPLETE);
        final SequentialDisposable disposables = new SequentialDisposable();
        long produced;
        final AtomicLong requested = new AtomicLong();
        final Iterator sources;

        ConcatMaybeObserver(Subscriber subscriber, Iterator it) {
            this.actual = subscriber;
            this.sources = it;
        }

        public void cancel() {
            this.disposables.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                AtomicReference atomicReference = this.current;
                Subscriber subscriber = this.actual;
                SequentialDisposable sequentialDisposable = this.disposables;
                while (!sequentialDisposable.isDisposed()) {
                    Object obj = atomicReference.get();
                    if (obj != null) {
                        boolean z = true;
                        if (obj != NotificationLite.COMPLETE) {
                            long j = this.produced;
                            if (j != this.requested.get()) {
                                this.produced = j + 1;
                                atomicReference.lazySet(null);
                                subscriber.onNext(obj);
                            } else {
                                z = false;
                            }
                        } else {
                            atomicReference.lazySet(null);
                        }
                        if (z && !sequentialDisposable.isDisposed()) {
                            try {
                                if (this.sources.hasNext()) {
                                    Object next = this.sources.next();
                                    ObjectHelper.requireNonNull(next, "The source Iterator returned a null MaybeSource");
                                    ((MaybeSource) next).subscribe(this);
                                } else {
                                    subscriber.onComplete();
                                }
                            } catch (Throwable th) {
                                Exceptions.throwIfFatal(th);
                                subscriber.onError(th);
                                return;
                            }
                        }
                    }
                    if (decrementAndGet() == 0) {
                        return;
                    }
                }
                atomicReference.lazySet(null);
            }
        }

        public void onComplete() {
            this.current.lazySet(NotificationLite.COMPLETE);
            drain();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            this.disposables.replace(disposable);
        }

        public void onSuccess(Object obj) {
            this.current.lazySet(obj);
            drain();
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                drain();
            }
        }
    }

    public MaybeConcatIterable(Iterable iterable) {
        this.sources = iterable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        try {
            Iterator it = this.sources.iterator();
            ObjectHelper.requireNonNull(it, "The sources Iterable returned a null Iterator");
            ConcatMaybeObserver concatMaybeObserver = new ConcatMaybeObserver(subscriber, it);
            subscriber.onSubscribe(concatMaybeObserver);
            concatMaybeObserver.drain();
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptySubscription.error(th, subscriber);
        }
    }
}
