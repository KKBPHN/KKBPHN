package io.reactivex.internal.operators.observable;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class ObservableLastMaybe extends Maybe {
    final ObservableSource source;

    final class LastObserver implements Observer, Disposable {
        final MaybeObserver actual;
        Object item;
        Disposable s;

        LastObserver(MaybeObserver maybeObserver) {
            this.actual = maybeObserver;
        }

        public void dispose() {
            this.s.dispose();
            this.s = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.s == DisposableHelper.DISPOSED;
        }

        public void onComplete() {
            this.s = DisposableHelper.DISPOSED;
            Object obj = this.item;
            if (obj != null) {
                this.item = null;
                this.actual.onSuccess(obj);
                return;
            }
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.s = DisposableHelper.DISPOSED;
            this.item = null;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            this.item = obj;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableLastMaybe(ObservableSource observableSource) {
        this.source = observableSource;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new LastObserver(maybeObserver));
    }
}
