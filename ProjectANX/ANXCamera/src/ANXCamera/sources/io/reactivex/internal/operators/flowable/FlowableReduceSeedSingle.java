package io.reactivex.internal.operators.flowable;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public final class FlowableReduceSeedSingle extends Single {
    final BiFunction reducer;
    final Object seed;
    final Publisher source;

    final class ReduceSeedObserver implements FlowableSubscriber, Disposable {
        final SingleObserver actual;
        final BiFunction reducer;
        Subscription s;
        Object value;

        ReduceSeedObserver(SingleObserver singleObserver, BiFunction biFunction, Object obj) {
            this.actual = singleObserver;
            this.value = obj;
            this.reducer = biFunction;
        }

        public void dispose() {
            this.s.cancel();
            this.s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.s == SubscriptionHelper.CANCELLED;
        }

        public void onComplete() {
            Object obj = this.value;
            this.value = null;
            this.s = SubscriptionHelper.CANCELLED;
            this.actual.onSuccess(obj);
        }

        public void onError(Throwable th) {
            this.value = null;
            this.s = SubscriptionHelper.CANCELLED;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            try {
                Object apply = this.reducer.apply(this.value, obj);
                ObjectHelper.requireNonNull(apply, "The reducer returned a null value");
                this.value = apply;
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.s.cancel();
                onError(th);
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(Long.MAX_VALUE);
            }
        }
    }

    public FlowableReduceSeedSingle(Publisher publisher, Object obj, BiFunction biFunction) {
        this.source = publisher;
        this.seed = obj;
        this.reducer = biFunction;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new ReduceSeedObserver(singleObserver, this.reducer, this.seed));
    }
}
