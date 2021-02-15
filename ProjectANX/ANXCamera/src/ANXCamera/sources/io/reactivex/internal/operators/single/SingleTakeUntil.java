package io.reactivex.internal.operators.single;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public final class SingleTakeUntil extends Single {
    final Publisher other;
    final SingleSource source;

    final class TakeUntilMainObserver extends AtomicReference implements SingleObserver, Disposable {
        private static final long serialVersionUID = -622603812305745221L;
        final SingleObserver actual;
        final TakeUntilOtherSubscriber other = new TakeUntilOtherSubscriber(this);

        TakeUntilMainObserver(SingleObserver singleObserver) {
            this.actual = singleObserver;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onError(Throwable th) {
            this.other.dispose();
            Disposable disposable = (Disposable) get();
            DisposableHelper disposableHelper = DisposableHelper.DISPOSED;
            if (disposable == disposableHelper || ((Disposable) getAndSet(disposableHelper)) == DisposableHelper.DISPOSED) {
                RxJavaPlugins.onError(th);
            } else {
                this.actual.onError(th);
            }
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }

        public void onSuccess(Object obj) {
            this.other.dispose();
            Disposable disposable = (Disposable) get();
            DisposableHelper disposableHelper = DisposableHelper.DISPOSED;
            if (disposable != disposableHelper && ((Disposable) getAndSet(disposableHelper)) != DisposableHelper.DISPOSED) {
                this.actual.onSuccess(obj);
            }
        }

        /* access modifiers changed from: 0000 */
        public void otherError(Throwable th) {
            Disposable disposable = (Disposable) get();
            DisposableHelper disposableHelper = DisposableHelper.DISPOSED;
            if (disposable != disposableHelper) {
                Disposable disposable2 = (Disposable) getAndSet(disposableHelper);
                if (disposable2 != DisposableHelper.DISPOSED) {
                    if (disposable2 != null) {
                        disposable2.dispose();
                    }
                    this.actual.onError(th);
                    return;
                }
            }
            RxJavaPlugins.onError(th);
        }
    }

    final class TakeUntilOtherSubscriber extends AtomicReference implements FlowableSubscriber {
        private static final long serialVersionUID = 5170026210238877381L;
        final TakeUntilMainObserver parent;

        TakeUntilOtherSubscriber(TakeUntilMainObserver takeUntilMainObserver) {
            this.parent = takeUntilMainObserver;
        }

        public void dispose() {
            SubscriptionHelper.cancel(this);
        }

        public void onComplete() {
            Object obj = get();
            SubscriptionHelper subscriptionHelper = SubscriptionHelper.CANCELLED;
            if (obj != subscriptionHelper) {
                lazySet(subscriptionHelper);
                this.parent.otherError(new CancellationException());
            }
        }

        public void onError(Throwable th) {
            this.parent.otherError(th);
        }

        public void onNext(Object obj) {
            if (SubscriptionHelper.cancel(this)) {
                this.parent.otherError(new CancellationException());
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this, subscription)) {
                subscription.request(Long.MAX_VALUE);
            }
        }
    }

    public SingleTakeUntil(SingleSource singleSource, Publisher publisher) {
        this.source = singleSource;
        this.other = publisher;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        TakeUntilMainObserver takeUntilMainObserver = new TakeUntilMainObserver(singleObserver);
        singleObserver.onSubscribe(takeUntilMainObserver);
        this.other.subscribe(takeUntilMainObserver.other);
        this.source.subscribe(takeUntilMainObserver);
    }
}
