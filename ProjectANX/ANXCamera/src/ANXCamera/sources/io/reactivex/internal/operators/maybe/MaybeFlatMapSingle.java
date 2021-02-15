package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeFlatMapSingle extends Single {
    final Function mapper;
    final MaybeSource source;

    final class FlatMapMaybeObserver extends AtomicReference implements MaybeObserver, Disposable {
        private static final long serialVersionUID = 4827726964688405508L;
        final SingleObserver actual;
        final Function mapper;

        FlatMapMaybeObserver(SingleObserver singleObserver, Function function) {
            this.actual = singleObserver;
            this.mapper = function;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onComplete() {
            this.actual.onError(new NoSuchElementException());
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.setOnce(this, disposable)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            try {
                Object apply = this.mapper.apply(obj);
                ObjectHelper.requireNonNull(apply, "The mapper returned a null SingleSource");
                SingleSource singleSource = (SingleSource) apply;
                if (!isDisposed()) {
                    singleSource.subscribe(new FlatMapSingleObserver(this, this.actual));
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                onError(th);
            }
        }
    }

    final class FlatMapSingleObserver implements SingleObserver {
        final SingleObserver actual;
        final AtomicReference parent;

        FlatMapSingleObserver(AtomicReference atomicReference, SingleObserver singleObserver) {
            this.parent = atomicReference;
            this.actual = singleObserver;
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.replace(this.parent, disposable);
        }

        public void onSuccess(Object obj) {
            this.actual.onSuccess(obj);
        }
    }

    public MaybeFlatMapSingle(MaybeSource maybeSource, Function function) {
        this.source = maybeSource;
        this.mapper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new FlatMapMaybeObserver(singleObserver, this.mapper));
    }
}
