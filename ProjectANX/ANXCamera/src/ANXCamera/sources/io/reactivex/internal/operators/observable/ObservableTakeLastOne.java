package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class ObservableTakeLastOne extends AbstractObservableWithUpstream {

    final class TakeLastOneObserver implements Observer, Disposable {
        final Observer actual;
        Disposable s;
        Object value;

        TakeLastOneObserver(Observer observer) {
            this.actual = observer;
        }

        public void dispose() {
            this.value = null;
            this.s.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void emit() {
            Object obj = this.value;
            if (obj != null) {
                this.value = null;
                this.actual.onNext(obj);
            }
            this.actual.onComplete();
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void onComplete() {
            emit();
        }

        public void onError(Throwable th) {
            this.value = null;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            this.value = obj;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableTakeLastOne(ObservableSource observableSource) {
        super(observableSource);
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(new TakeLastOneObserver(observer));
    }
}
