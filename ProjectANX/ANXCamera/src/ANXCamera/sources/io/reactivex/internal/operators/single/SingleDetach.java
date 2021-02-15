package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.annotations.Experimental;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

@Experimental
public final class SingleDetach extends Single {
    final SingleSource source;

    final class DetachSingleObserver implements SingleObserver, Disposable {
        SingleObserver actual;
        Disposable d;

        DetachSingleObserver(SingleObserver singleObserver) {
            this.actual = singleObserver;
        }

        public void dispose() {
            this.actual = null;
            this.d.dispose();
            this.d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onError(Throwable th) {
            this.d = DisposableHelper.DISPOSED;
            SingleObserver singleObserver = this.actual;
            if (singleObserver != null) {
                this.actual = null;
                singleObserver.onError(th);
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            this.d = DisposableHelper.DISPOSED;
            SingleObserver singleObserver = this.actual;
            if (singleObserver != null) {
                this.actual = null;
                singleObserver.onSuccess(obj);
            }
        }
    }

    public SingleDetach(SingleSource singleSource) {
        this.source = singleSource;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new DetachSingleObserver(singleObserver));
    }
}
