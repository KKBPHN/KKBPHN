package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
import io.reactivex.internal.observers.DeferredScalarDisposable;

public final class MaybeToObservable extends Observable implements HasUpstreamMaybeSource {
    final MaybeSource source;

    final class MaybeToFlowableSubscriber extends DeferredScalarDisposable implements MaybeObserver {
        private static final long serialVersionUID = 7603343402964826922L;
        Disposable d;

        MaybeToFlowableSubscriber(Observer observer) {
            super(observer);
        }

        public void dispose() {
            super.dispose();
            this.d.dispose();
        }

        public void onComplete() {
            complete();
        }

        public void onError(Throwable th) {
            error(th);
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

    public MaybeToObservable(MaybeSource maybeSource) {
        this.source = maybeSource;
    }

    public MaybeSource source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        this.source.subscribe(new MaybeToFlowableSubscriber(observer));
    }
}
