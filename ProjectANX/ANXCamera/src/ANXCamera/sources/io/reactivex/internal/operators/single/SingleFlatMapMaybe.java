package io.reactivex.internal.operators.single;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleFlatMapMaybe extends Maybe {
    final Function mapper;
    final SingleSource source;

    final class FlatMapMaybeObserver implements MaybeObserver {
        final MaybeObserver actual;
        final AtomicReference parent;

        FlatMapMaybeObserver(AtomicReference atomicReference, MaybeObserver maybeObserver) {
            this.parent = atomicReference;
            this.actual = maybeObserver;
        }

        public void onComplete() {
            this.actual.onComplete();
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

    final class FlatMapSingleObserver extends AtomicReference implements SingleObserver, Disposable {
        private static final long serialVersionUID = -5843758257109742742L;
        final MaybeObserver actual;
        final Function mapper;

        FlatMapSingleObserver(MaybeObserver maybeObserver, Function function) {
            this.actual = maybeObserver;
            this.mapper = function;
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
            if (DisposableHelper.setOnce(this, disposable)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            try {
                Object apply = this.mapper.apply(obj);
                ObjectHelper.requireNonNull(apply, "The mapper returned a null MaybeSource");
                MaybeSource maybeSource = (MaybeSource) apply;
                if (!isDisposed()) {
                    maybeSource.subscribe(new FlatMapMaybeObserver(this, this.actual));
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                onError(th);
            }
        }
    }

    public SingleFlatMapMaybe(SingleSource singleSource, Function function) {
        this.mapper = function;
        this.source = singleSource;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new FlatMapSingleObserver(maybeObserver, this.mapper));
    }
}
