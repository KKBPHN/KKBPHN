package io.reactivex.internal.operators.flowable;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.NoSuchElementException;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public final class FlowableLastSingle extends Single {
    final Object defaultItem;
    final Publisher source;

    final class LastSubscriber implements FlowableSubscriber, Disposable {
        final SingleObserver actual;
        final Object defaultItem;
        Object item;
        Subscription s;

        LastSubscriber(SingleObserver singleObserver, Object obj) {
            this.actual = singleObserver;
            this.defaultItem = obj;
        }

        public void dispose() {
            this.s.cancel();
            this.s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.s == SubscriptionHelper.CANCELLED;
        }

        public void onComplete() {
            SingleObserver singleObserver;
            this.s = SubscriptionHelper.CANCELLED;
            Object obj = this.item;
            if (obj != null) {
                this.item = null;
                singleObserver = this.actual;
            } else {
                obj = this.defaultItem;
                singleObserver = this.actual;
                if (obj == null) {
                    singleObserver.onError(new NoSuchElementException());
                    return;
                }
            }
            singleObserver.onSuccess(obj);
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

    public FlowableLastSingle(Publisher publisher, Object obj) {
        this.source = publisher;
        this.defaultItem = obj;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new LastSubscriber(singleObserver, this.defaultItem));
    }
}
