package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleDoOnDispose extends Single {
    final Action onDispose;
    final SingleSource source;

    final class DoOnDisposeObserver extends AtomicReference implements SingleObserver, Disposable {
        private static final long serialVersionUID = -8583764624474935784L;
        final SingleObserver actual;
        Disposable d;

        DoOnDisposeObserver(SingleObserver singleObserver, Action action) {
            this.actual = singleObserver;
            lazySet(action);
        }

        public void dispose() {
            Action action = (Action) getAndSet(null);
            if (action != null) {
                try {
                    action.run();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    RxJavaPlugins.onError(th);
                }
                this.d.dispose();
            }
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
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
            this.actual.onSuccess(obj);
        }
    }

    public SingleDoOnDispose(SingleSource singleSource, Action action) {
        this.source = singleSource;
        this.onDispose = action;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new DoOnDisposeObserver(singleObserver, this.onDispose));
    }
}
