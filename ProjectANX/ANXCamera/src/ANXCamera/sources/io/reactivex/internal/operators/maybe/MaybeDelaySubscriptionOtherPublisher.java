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

public final class MaybeDelaySubscriptionOtherPublisher extends AbstractMaybeWithUpstream {
    final Publisher other;

    final class DelayMaybeObserver extends AtomicReference implements MaybeObserver {
        private static final long serialVersionUID = 706635022205076709L;
        final MaybeObserver actual;

        DelayMaybeObserver(MaybeObserver maybeObserver) {
            this.actual = maybeObserver;
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }

        public void onSuccess(Object obj) {
            this.actual.onSuccess(obj);
        }
    }

    final class OtherSubscriber implements FlowableSubscriber, Disposable {
        final DelayMaybeObserver main;
        Subscription s;
        MaybeSource source;

        OtherSubscriber(MaybeObserver maybeObserver, MaybeSource maybeSource) {
            this.main = new DelayMaybeObserver(maybeObserver);
            this.source = maybeSource;
        }

        public void dispose() {
            this.s.cancel();
            this.s = SubscriptionHelper.CANCELLED;
            DisposableHelper.dispose(this.main);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) this.main.get());
        }

        public void onComplete() {
            Subscription subscription = this.s;
            SubscriptionHelper subscriptionHelper = SubscriptionHelper.CANCELLED;
            if (subscription != subscriptionHelper) {
                this.s = subscriptionHelper;
                subscribeNext();
            }
        }

        public void onError(Throwable th) {
            Subscription subscription = this.s;
            SubscriptionHelper subscriptionHelper = SubscriptionHelper.CANCELLED;
            if (subscription != subscriptionHelper) {
                this.s = subscriptionHelper;
                this.main.actual.onError(th);
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onNext(Object obj) {
            Subscription subscription = this.s;
            if (subscription != SubscriptionHelper.CANCELLED) {
                subscription.cancel();
                this.s = SubscriptionHelper.CANCELLED;
                subscribeNext();
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.main.actual.onSubscribe(this);
                subscription.request(Long.MAX_VALUE);
            }
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            MaybeSource maybeSource = this.source;
            this.source = null;
            maybeSource.subscribe(this.main);
        }
    }

    public MaybeDelaySubscriptionOtherPublisher(MaybeSource maybeSource, Publisher publisher) {
        super(maybeSource);
        this.other = publisher;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.other.subscribe(new OtherSubscriber(maybeObserver, this.source));
    }
}
