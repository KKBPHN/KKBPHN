package io.reactivex.internal.operators.single;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.observers.ResumeSingleObserver;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleDelayWithCompletable extends Single {
    final CompletableSource other;
    final SingleSource source;

    final class OtherObserver extends AtomicReference implements CompletableObserver, Disposable {
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

        public void onComplete() {
            this.source.subscribe(new ResumeSingleObserver(this, this.actual));
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.setOnce(this, disposable)) {
                this.actual.onSubscribe(this);
            }
        }
    }

    public SingleDelayWithCompletable(SingleSource singleSource, CompletableSource completableSource) {
        this.source = singleSource;
        this.other = completableSource;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.other.subscribe(new OtherObserver(singleObserver, this.source));
    }
}
