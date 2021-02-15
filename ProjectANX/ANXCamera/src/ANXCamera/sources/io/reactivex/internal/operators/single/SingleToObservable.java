package io.reactivex.internal.operators.single;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class SingleToObservable extends Observable {
    final SingleSource source;

    final class SingleToObservableObserver implements SingleObserver, Disposable {
        final Observer actual;
        Disposable d;

        SingleToObservableObserver(Observer observer) {
            this.actual = observer;
        }

        public void dispose() {
            this.d.dispose();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
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
            this.actual.onNext(obj);
            this.actual.onComplete();
        }
    }

    public SingleToObservable(SingleSource singleSource) {
        this.source = singleSource;
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(new SingleToObservableObserver(observer));
    }
}
