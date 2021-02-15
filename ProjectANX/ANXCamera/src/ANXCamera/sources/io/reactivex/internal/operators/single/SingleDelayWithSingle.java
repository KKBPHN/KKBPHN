package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.observers.ResumeSingleObserver;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleDelayWithSingle extends Single {
    final SingleSource other;
    final SingleSource source;

    final class OtherObserver extends AtomicReference implements SingleObserver, Disposable {
        private static final long serialVersionUID = -8565274649390031272L;
        final SingleObserver actual;
        final SingleSource source;

        OtherObserver(SingleObserver singleObserver, SingleSource singleSource) {
            this.actual = singleObserver;
            this.source = singleSource;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.set(this, disposable)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            this.source.subscribe(new ResumeSingleObserver(this, this.actual));
        }
    }

    public SingleDelayWithSingle(SingleSource singleSource, SingleSource singleSource2) {
        this.source = singleSource;
        this.other = singleSource2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.other.subscribe(new OtherObserver(singleObserver, this.source));
    }
}
