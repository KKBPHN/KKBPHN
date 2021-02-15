package io.reactivex.internal.operators.observable;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableSingleMaybe extends Maybe {
    final ObservableSource source;

    final class SingleElementObserver implements Observer, Disposable {
        final MaybeObserver actual;
        boolean done;
        Disposable s;
        Object value;

        SingleElementObserver(MaybeObserver maybeObserver) {
            this.actual = maybeObserver;
        }

        public void dispose() {
            this.s.dispose();
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                Object obj = this.value;
                this.value = null;
                MaybeObserver maybeObserver = this.actual;
                if (obj == null) {
                    maybeObserver.onComplete();
                } else {
                    maybeObserver.onSuccess(obj);
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                if (this.value != null) {
                    this.done = true;
                    this.s.dispose();
                    this.actual.onError(new IllegalArgumentException("Sequence contains more than one element!"));
                    return;
                }
                this.value = obj;
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableSingleMaybe(ObservableSource observableSource) {
        this.source = observableSource;
    }

    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new SingleElementObserver(maybeObserver));
    }
}
