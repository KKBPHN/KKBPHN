package io.reactivex.internal.operators.single;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.NoSuchElementException;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public final class SingleFromPublisher extends Single {
    final Publisher publisher;

    final class ToSingleObserver implements FlowableSubscriber, Disposable {
        final SingleObserver actual;
        volatile boolean disposed;
        boolean done;
        Subscription s;
        Object value;

        ToSingleObserver(SingleObserver singleObserver) {
            this.actual = singleObserver;
        }

        public void dispose() {
            this.disposed = true;
            this.s.cancel();
        }

        public boolean isDisposed() {
            return this.disposed;
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                Object obj = this.value;
                this.value = null;
                SingleObserver singleObserver = this.actual;
                if (obj == null) {
                    singleObserver.onError(new NoSuchElementException("The source Publisher is empty"));
                } else {
                    singleObserver.onSuccess(obj);
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.value = null;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                if (this.value != null) {
                    this.s.cancel();
                    this.done = true;
                    this.value = null;
                    this.actual.onError(new IndexOutOfBoundsException("Too many elements in the Publisher"));
                } else {
                    this.value = obj;
                }
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

    public SingleFromPublisher(Publisher publisher2) {
        this.publisher = publisher2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.publisher.subscribe(new ToSingleObserver(singleObserver));
    }
}
