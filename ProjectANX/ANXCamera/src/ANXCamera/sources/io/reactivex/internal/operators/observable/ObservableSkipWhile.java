package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.disposables.DisposableHelper;

public final class ObservableSkipWhile extends AbstractObservableWithUpstream {
    final Predicate predicate;

    final class SkipWhileObserver implements Observer, Disposable {
        final Observer actual;
        boolean notSkipping;
        final Predicate predicate;
        Disposable s;

        SkipWhileObserver(Observer observer, Predicate predicate2) {
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
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.notSkipping) {
                try {
                    if (!this.predicate.test(obj)) {
                        this.notSkipping = true;
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.s.dispose();
                    this.actual.onError(th);
                    return;
                }
            }
            this.actual.onNext(obj);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableSkipWhile(ObservableSource observableSource, Predicate predicate2) {
        super(observableSource);
        this.predicate = predicate2;
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(new SkipWhileObserver(observer, this.predicate));
    }
}
