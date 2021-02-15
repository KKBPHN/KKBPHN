package io.reactivex.internal.operators.observable;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public final class ObservableFromPublisher extends Observable {
    final Publisher source;

    final class PublisherSubscriber implements FlowableSubscriber, Disposable {
        final Observer actual;
        Subscription s;

        PublisherSubscriber(Observer observer) {
            this.actual = observer;
        }

        public void dispose() {
            this.s.cancel();
            this.s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.s == SubscriptionHelper.CANCELLED;
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            this.actual.onNext(obj);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(Long.MAX_VALUE);
            }
        }
    }

    public ObservableFromPublisher(Publisher publisher) {
        this.source = publisher;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        this.source.subscribe(new PublisherSubscriber(observer));
    }
}
