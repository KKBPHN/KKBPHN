package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;

public final class MaybeMap extends AbstractMaybeWithUpstream {
    final Function mapper;

    final class MapMaybeObserver implements MaybeObserver, Disposable {
        final MaybeObserver actual;
        Disposable d;
        final Function mapper;

        MapMaybeObserver(MaybeObserver maybeObserver, Function function) {
            this.actual = maybeObserver;
            this.mapper = function;
        }

        public void dispose() {
            Disposable disposable = this.d;
            this.d = DisposableHelper.DISPOSED;
            disposable.dispose();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onComplete() {
            this.actual.onComplete();
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
            try {
                Object apply = this.mapper.apply(obj);
                ObjectHelper.requireNonNull(apply, "The mapper returned a null item");
                this.actual.onSuccess(apply);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.actual.onError(th);
            }
        }
    }

    public MaybeMap(MaybeSource maybeSource, Function function) {
        super(maybeSource);
        this.mapper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new MapMaybeObserver(maybeObserver, this.mapper));
    }
}
