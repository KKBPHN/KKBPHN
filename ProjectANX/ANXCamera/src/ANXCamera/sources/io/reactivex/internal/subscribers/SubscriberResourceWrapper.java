package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class SubscriberResourceWrapper extends AtomicReference implements FlowableSubscriber, Disposable, Subscription {
    private static final long serialVersionUID = -8612022020200669122L;
    final Subscriber actual;
    final AtomicReference subscription = new AtomicReference();

    public SubscriberResourceWrapper(Subscriber subscriber) {
        this.actual = subscriber;
    }

    public void cancel() {
        dispose();
    }

    public void dispose() {
        SubscriptionHelper.cancel(this.subscription);
        DisposableHelper.dispose(this);
    }

    public boolean isDisposed() {
        return this.subscription.get() == SubscriptionHelper.CANCELLED;
    }

    public void onComplete() {
        DisposableHelper.dispose(this);
        this.actual.onComplete();
    }

    public void onError(Throwable th) {
        DisposableHelper.dispose(this);
        this.actual.onError(th);
    }

    public void onNext(Object obj) {
        this.actual.onNext(obj);
    }

    public void onSubscribe(Subscription subscription2) {
        if (SubscriptionHelper.setOnce(this.subscription, subscription2)) {
            this.actual.onSubscribe(this);
        }
    }

    public void request(long j) {
        if (SubscriptionHelper.validate(j)) {
            ((Subscription) this.subscription.get()).request(j);
        }
    }

    public void setResource(Disposable disposable) {
        DisposableHelper.set(this, disposable);
    }
}
