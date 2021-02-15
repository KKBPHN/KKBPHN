package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableFromObservable extends Flowable {
    private final Observable upstream;

    class SubscriberObserver implements Observer, Subscription {
        private Disposable d;
        private final Subscriber s;

        SubscriberObserver(Subscriber subscriber) {
            this.s = subscriber;
        }

        public void cancel() {
            this.d.dispose();
        }

        public void onComplete() {
            this.s.onComplete();
        }

        public void onError(Throwable th) {
            this.s.onError(th);
        }

        public void onNext(Object obj) {
            this.s.onNext(obj);
        }

        public void onSubscribe(Disposable disposable) {
            this.d = disposable;
            this.s.onSubscribe(this);
        }

        public void request(long j) {
        }
    }

    public FlowableFromObservable(Observable observable) {
        this.upstream = observable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.upstream.subscribe((Observer) new SubscriberObserver(subscriber));
    }
}
