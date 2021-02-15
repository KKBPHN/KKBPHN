package io.reactivex.internal.operators.flowable;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public final class FlowableLastMaybe extends Maybe {
    final Publisher source;

    final class LastSubscriber implements FlowableSubscriber, Disposable {
        final MaybeObserver actual;
        Object item;
        Subscription s;

        LastSubscriber(MaybeObserver maybeObserver) {
            this.actual = maybeObserver;
        }

        public void dispose() {
            this.s.cancel();
            this.s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.s == SubscriptionHelper.CANCELLED;
        }

        public void onComplete() {
            this.s = SubscriptionHelper.CANCELLED;
            Object obj = this.item;
            if (obj != null) {
                this.item = null;
                this.actual.onSuccess(obj);
                return;
            }
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.s = SubscriptionHelper.CANCELLED;
            this.item = null;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            this.item = obj;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(Long.MAX_VALUE);
            }
        }
    }

    public FlowableLastMaybe(Publisher publisher) {
        this.source = publisher;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new LastSubscriber(maybeObserver));
    }
}
