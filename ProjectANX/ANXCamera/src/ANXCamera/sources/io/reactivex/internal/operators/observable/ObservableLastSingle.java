package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.NoSuchElementException;

public final class ObservableLastSingle extends Single {
    final Object defaultItem;
    final ObservableSource source;

    final class LastObserver implements Observer, Disposable {
        final SingleObserver actual;
        final Object defaultItem;
        Object item;
        Disposable s;

        LastObserver(SingleObserver singleObserver, Object obj) {
            this.actual = singleObserver;
            this.defaultItem = obj;
        }

        public void dispose() {
            this.s.dispose();
            this.s = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.s == DisposableHelper.DISPOSED;
        }

        public void onComplete() {
            SingleObserver singleObserver;
            this.s = DisposableHelper.DISPOSED;
            Object obj = this.item;
            if (obj != null) {
                this.item = null;
                singleObserver = this.actual;
            } else {
                obj = this.defaultItem;
                singleObserver = this.actual;
                if (obj == null) {
                    singleObserver.onError(new NoSuchElementException());
                    return;
                }
            }
            singleObserver.onSuccess(obj);
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

    public ObservableLastSingle(ObservableSource observableSource, Object obj) {
        this.source = observableSource;
        this.defaultItem = obj;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new LastObserver(singleObserver, this.defaultItem));
    }
}
