package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeFlatMapBiSelector extends AbstractMaybeWithUpstream {
    final Function mapper;
    final BiFunction resultSelector;

    final class FlatMapBiMainObserver implements MaybeObserver, Disposable {
        final InnerObserver inner;
        final Function mapper;

        final class InnerObserver extends AtomicReference implements MaybeObserver {
            private static final long serialVersionUID = -2897979525538174559L;
            final MaybeObserver actual;
            final BiFunction resultSelector;
            Object value;

            InnerObserver(MaybeObserver maybeObserver, BiFunction biFunction) {
                this.actual = maybeObserver;
                this.resultSelector = biFunction;
            }

            public void onComplete() {
                this.actual.onComplete();
            }

            public void onError(Throwable th) {
                this.actual.onError(th);
            }

            public void onSubscribe(Disposable disposable) {
                DisposableHelper.setOnce(this, disposable);
            }

            public void onSuccess(Object obj) {
                Object obj2 = this.value;
                this.value = null;
                try {
                    Object apply = this.resultSelector.apply(obj2, obj);
                    ObjectHelper.requireNonNull(apply, "The resultSelector returned a null value");
                    this.actual.onSuccess(apply);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.actual.onError(th);
                }
            }
        }

        FlatMapBiMainObserver(MaybeObserver maybeObserver, Function function, BiFunction biFunction) {
            this.inner = new InnerObserver(maybeObserver, biFunction);
            this.mapper = function;
        }

        public void dispose() {
            DisposableHelper.dispose(this.inner);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) this.inner.get());
        }

        public void onComplete() {
            this.inner.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.inner.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.setOnce(this.inner, disposable)) {
                this.inner.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            try {
                Object apply = this.mapper.apply(obj);
                ObjectHelper.requireNonNull(apply, "The mapper returned a null MaybeSource");
                MaybeSource maybeSource = (MaybeSource) apply;
                if (DisposableHelper.replace(this.inner, null)) {
                    InnerObserver innerObserver = this.inner;
                    innerObserver.value = obj;
                    maybeSource.subscribe(innerObserver);
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.inner.actual.onError(th);
            }
        }
    }

    public MaybeFlatMapBiSelector(MaybeSource maybeSource, Function function, BiFunction biFunction) {
        super(maybeSource);
        this.mapper = function;
        this.resultSelector = biFunction;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new FlatMapBiMainObserver(maybeObserver, this.mapper, this.resultSelector));
    }
}
