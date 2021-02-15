package io.reactivex.internal.operators.maybe;

import io.reactivex.FlowableSubscriber;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public final class MaybeTakeUntilPublisher extends AbstractMaybeWithUpstream {
    final Publisher other;

    final class TakeUntilMainMaybeObserver extends AtomicReference implements MaybeObserver, Disposable {
        private static final long serialVersionUID = -2187421758664251153L;
        final MaybeObserver actual;
        final TakeUntilOtherMaybeObserver other = new TakeUntilOtherMaybeObserver(this);

        final class TakeUntilOtherMaybeObserver extends AtomicReference implements FlowableSubscriber {
            private static final long serialVersionUID = -1266041316834525931L;
            final TakeUntilMainMaybeObserver parent;

            TakeUntilOtherMaybeObserver(TakeUntilMainMaybeObserver takeUntilMainMaybeObserver) {
                this.parent = takeUntilMainMaybeObserver;
            }

            public void onComplete() {
                this.parent.otherComplete();
            }

            public void onError(Throwable th) {
                this.parent.otherError(th);
            }

            public void onNext(Object obj) {
                this.parent.otherComplete();
            }

            public void onSubscribe(Subscription subscription) {
                if (SubscriptionHelper.setOnce(this, subscription)) {
                    subscription.request(Long.MAX_VALUE);
                }
            }
        }

        TakeUntilMainMaybeObserver(MaybeObserver maybeObserver) {
            this.actual = maybeObserver;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
            SubscriptionHelper.cancel(this.other);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onComplete() {
            SubscriptionHelper.cancel(this.other);
            if (getAndSet(DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            SubscriptionHelper.cancel(this.other);
            if (getAndSet(DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
                this.actual.onError(th);
            } else {
                RxJavaPlugins.onError(th);
            }
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }

        public void onSuccess(Object obj) {
            SubscriptionHelper.cancel(this.other);
            if (getAndSet(DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
                this.actual.onSuccess(obj);
            }
        }

        /* access modifiers changed from: 0000 */
        public void otherComplete() {
            if (DisposableHelper.dispose(this)) {
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void otherError(Throwable th) {
            if (DisposableHelper.dispose(this)) {
                this.actual.onError(th);
            } else {
                RxJavaPlugins.onError(th);
            }
        }
    }

    public MaybeTakeUntilPublisher(MaybeSource maybeSource, Publisher publisher) {
        super(maybeSource);
        this.other = publisher;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        TakeUntilMainMaybeObserver takeUntilMainMaybeObserver = new TakeUntilMainMaybeObserver(maybeObserver);
        maybeObserver.onSubscribe(takeUntilMainMaybeObserver);
        this.other.subscribe(takeUntilMainMaybeObserver.other);
        this.source.subscribe(takeUntilMainMaybeObserver);
    }
}
