package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class SingleHide extends Single {
    final SingleSource source;

    final class HideSingleObserver implements SingleObserver, Disposable {
        final SingleObserver actual;
        Disposable d;

        HideSingleObserver(SingleObserver singleObserver) {
            this.actual = singleObserver;
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
            this.actual.onSuccess(obj);
        }
    }

    public SingleHide(SingleSource singleSource) {
        this.source = singleSource;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new HideSingleObserver(singleObserver));
    }
}
