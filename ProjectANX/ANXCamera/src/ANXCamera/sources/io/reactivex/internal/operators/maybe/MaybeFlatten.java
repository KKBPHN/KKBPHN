package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeFlatten extends AbstractMaybeWithUpstream {
    final Function mapper;

    final class FlatMapMaybeObserver extends AtomicReference implements MaybeObserver, Disposable {
        private static final long serialVersionUID = 4375739915521278546L;
        final MaybeObserver actual;
        Disposable d;
        final Function mapper;

        final class InnerObserver implements MaybeObserver {
            InnerObserver() {
            }

            public void onComplete() {
                FlatMapMaybeObserver.this.actual.onComplete();
            }

            public void onError(Throwable th) {
                FlatMapMaybeObserver.this.actual.onError(th);
            }

            public void onSubscribe(Disposable disposable) {
                DisposableHelper.setOnce(FlatMapMaybeObserver.this, disposable);
            }

            public void onSuccess(Object obj) {
                FlatMapMaybeObserver.this.actual.onSuccess(obj);
            }
        }

        FlatMapMaybeObserver(MaybeObserver maybeObserver, Function function) {
            this.actual = maybeObserver;
            this.mapper = function;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
            this.d.dispose();
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
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            try {
                Object apply = this.mapper.apply(obj);
                ObjectHelper.requireNonNull(apply, "The mapper returned a null MaybeSource");
                MaybeSource maybeSource = (MaybeSource) apply;
                if (!isDisposed()) {
                    maybeSource.subscribe(new InnerObserver());
                }
            } catch (Exception e) {
                Exceptions.throwIfFatal(e);
                this.actual.onError(e);
            }
        }
    }

    public MaybeFlatten(MaybeSource maybeSource, Function function) {
        super(maybeSource);
        this.mapper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new FlatMapMaybeObserver(maybeObserver, this.mapper));
    }
}
