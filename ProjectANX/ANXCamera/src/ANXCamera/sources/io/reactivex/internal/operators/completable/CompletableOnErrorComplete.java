package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;

public final class CompletableOnErrorComplete extends Completable {
    final Predicate predicate;
    final CompletableSource source;

    final class OnError implements CompletableObserver {
        private final CompletableObserver s;

        OnError(CompletableObserver completableObserver) {
            this.s = completableObserver;
        }

        public void onComplete() {
            this.s.onComplete();
        }

        public void onError(Throwable th) {
            try {
                boolean test = CompletableOnErrorComplete.this.predicate.test(th);
                CompletableObserver completableObserver = this.s;
                if (test) {
                    completableObserver.onComplete();
                } else {
                    completableObserver.onError(th);
                }
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                this.s.onError(new CompositeException(th, th2));
            }
        }

        public void onSubscribe(Disposable disposable) {
            this.s.onSubscribe(disposable);
        }
    }

    public CompletableOnErrorComplete(CompletableSource completableSource, Predicate predicate2) {
        this.source = completableSource;
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        this.source.subscribe(new OnError(completableObserver));
    }
}
