package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableAny extends AbstractObservableWithUpstream {
    final Predicate predicate;

    final class AnyObserver implements Observer, Disposable {
        final Observer actual;
        boolean done;
        final Predicate predicate;
        Disposable s;

        AnyObserver(Observer observer, Predicate predicate2) {
            this.actual = observer;
            this.predicate = predicate2;
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
                this.actual.onNext(Boolean.valueOf(false));
                this.actual.onComplete();
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
                try {
                    if (this.predicate.test(obj)) {
                        this.done = true;
                        this.s.dispose();
                        this.actual.onNext(Boolean.valueOf(true));
                        this.actual.onComplete();
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.s.dispose();
                    onError(th);
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableAny(ObservableSource observableSource, Predicate predicate2) {
        super(observableSource);
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        this.source.subscribe(new AnyObserver(observer, this.predicate));
    }
}
