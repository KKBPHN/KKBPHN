package io.reactivex.internal.operators.single;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleFlatMapCompletable extends Completable {
    final Function mapper;
    final SingleSource source;

    final class FlatMapCompletableObserver extends AtomicReference implements SingleObserver, CompletableObserver, Disposable {
        private static final long serialVersionUID = -2177128922851101253L;
        final CompletableObserver actual;
        final Function mapper;

        FlatMapCompletableObserver(CompletableObserver completableObserver, Function function) {
            this.actual = completableObserver;
            this.mapper = function;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.replace(this, disposable);
        }

        public void onSuccess(Object obj) {
            try {
                Object apply = this.mapper.apply(obj);
                ObjectHelper.requireNonNull(apply, "The mapper returned a null CompletableSource");
                CompletableSource completableSource = (CompletableSource) apply;
                if (!isDisposed()) {
                    completableSource.subscribe(this);
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                onError(th);
            }
        }
    }

    public SingleFlatMapCompletable(SingleSource singleSource, Function function) {
        this.source = singleSource;
        this.mapper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        FlatMapCompletableObserver flatMapCompletableObserver = new FlatMapCompletableObserver(completableObserver, this.mapper);
        completableObserver.onSubscribe(flatMapCompletableObserver);
        this.source.subscribe(flatMapCompletableObserver);
    }
}
