package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public final class CompletableFromObservable extends Completable {
    final ObservableSource observable;

    final class CompletableFromObservableObserver implements Observer {
        final CompletableObserver co;

        CompletableFromObservableObserver(CompletableObserver completableObserver) {
            this.co = completableObserver;
        }

        public void onComplete() {
            this.co.onComplete();
        }

        public void onError(Throwable th) {
            this.co.onError(th);
        }

        public void onNext(Object obj) {
        }

        public void onSubscribe(Disposable disposable) {
            this.co.onSubscribe(disposable);
        }
    }

    public CompletableFromObservable(ObservableSource observableSource) {
        this.observable = observableSource;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        this.observable.subscribe(new CompletableFromObservableObserver(completableObserver));
    }
}
