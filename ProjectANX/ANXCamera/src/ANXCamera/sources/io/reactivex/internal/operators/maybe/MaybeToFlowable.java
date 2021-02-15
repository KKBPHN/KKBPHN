package io.reactivex.internal.operators.maybe;

import io.reactivex.Flowable;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import org.reactivestreams.Subscriber;

public final class MaybeToFlowable extends Flowable implements HasUpstreamMaybeSource {
    final MaybeSource source;

    final class MaybeToFlowableSubscriber extends DeferredScalarSubscription implements MaybeObserver {
        private static final long serialVersionUID = 7603343402964826922L;
        Disposable d;

        MaybeToFlowableSubscriber(Subscriber subscriber) {
            super(subscriber);
        }

        public void cancel() {
            super.cancel();
            this.d.dispose();
        }

        public void onComplete() {
            this.actual.onComplete();
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

    public MaybeToFlowable(MaybeSource maybeSource) {
        this.source = maybeSource;
    }

    public MaybeSource source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe(new MaybeToFlowableSubscriber(subscriber));
    }
}
