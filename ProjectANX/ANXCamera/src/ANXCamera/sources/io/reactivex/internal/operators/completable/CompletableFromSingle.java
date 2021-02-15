package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;

public final class CompletableFromSingle extends Completable {
    final SingleSource single;

    final class CompletableFromSingleObserver implements SingleObserver {
        final CompletableObserver co;

        CompletableFromSingleObserver(CompletableObserver completableObserver) {
            this.co = completableObserver;
        }

        public void onError(Throwable th) {
            this.co.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            this.co.onSubscribe(disposable);
        }

        public void onSuccess(Object obj) {
            this.co.onComplete();
        }
    }

    public CompletableFromSingle(SingleSource singleSource) {
        this.single = singleSource;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        this.single.subscribe(new CompletableFromSingleObserver(completableObserver));
    }
}
