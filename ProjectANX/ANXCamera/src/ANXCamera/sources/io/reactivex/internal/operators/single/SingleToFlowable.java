package io.reactivex.internal.operators.single;

import io.reactivex.Flowable;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import org.reactivestreams.Subscriber;

public final class SingleToFlowable extends Flowable {
    final SingleSource source;

    final class SingleToFlowableObserver extends DeferredScalarSubscription implements SingleObserver {
        private static final long serialVersionUID = 187782011903685568L;
        Disposable d;

        SingleToFlowableObserver(Subscriber subscriber) {
            super(subscriber);
        }

        public void cancel() {
            super.cancel();
            this.d.dispose();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            complete(obj);
        }
    }

    public SingleToFlowable(SingleSource singleSource) {
        this.source = singleSource;
    }

    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe(new SingleToFlowableObserver(subscriber));
    }
}
