package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeFlatMapNotification extends AbstractMaybeWithUpstream {
    final Callable onCompleteSupplier;
    final Function onErrorMapper;
    final Function onSuccessMapper;

    final class FlatMapMaybeObserver extends AtomicReference implements MaybeObserver, Disposable {
        private static final long serialVersionUID = 4375739915521278546L;
        final MaybeObserver actual;
        Disposable d;
        final Callable onCompleteSupplier;
        final Function onErrorMapper;
        final Function onSuccessMapper;

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

        FlatMapMaybeObserver(MaybeObserver maybeObserver, Function function, Function function2, Callable callable) {
            this.actual = maybeObserver;
            this.onSuccessMapper = function;
            this.onErrorMapper = function2;
            this.onCompleteSupplier = callable;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
            this.d.dispose();
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onComplete() {
            try {
                Object call = this.onCompleteSupplier.call();
                ObjectHelper.requireNonNull(call, "The onCompleteSupplier returned a null MaybeSource");
                ((MaybeSource) call).subscribe(new InnerObserver());
            } catch (Exception e) {
                Exceptions.throwIfFatal(e);
                this.actual.onError(e);
            }
        }

        public void onError(Throwable th) {
            try {
                Object apply = this.onErrorMapper.apply(th);
                ObjectHelper.requireNonNull(apply, "The onErrorMapper returned a null MaybeSource");
                ((MaybeSource) apply).subscribe(new InnerObserver());
            } catch (Exception e) {
                Exceptions.throwIfFatal(e);
                this.actual.onError(new CompositeException(th, e));
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            try {
                Object apply = this.onSuccessMapper.apply(obj);
                ObjectHelper.requireNonNull(apply, "The onSuccessMapper returned a null MaybeSource");
                ((MaybeSource) apply).subscribe(new InnerObserver());
            } catch (Exception e) {
                Exceptions.throwIfFatal(e);
                this.actual.onError(e);
            }
        }
    }

    public MaybeFlatMapNotification(MaybeSource maybeSource, Function function, Function function2, Callable callable) {
        super(maybeSource);
        this.onSuccessMapper = function;
        this.onErrorMapper = function2;
        this.onCompleteSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new FlatMapMaybeObserver(maybeObserver, this.onSuccessMapper, this.onErrorMapper, this.onCompleteSupplier));
    }
}
