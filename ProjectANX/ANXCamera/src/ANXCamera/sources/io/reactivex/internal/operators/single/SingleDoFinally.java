package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.annotations.Experimental;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;

@Experimental
public final class SingleDoFinally extends Single {
    final Action onFinally;
    final SingleSource source;

    final class DoFinallyObserver extends AtomicInteger implements SingleObserver, Disposable {
        private static final long serialVersionUID = 4109457741734051389L;
        final SingleObserver actual;
        Disposable d;
        final Action onFinally;

        DoFinallyObserver(SingleObserver singleObserver, Action action) {
            this.actual = singleObserver;
            this.onFinally = action;
        }

        public void dispose() {
            this.d.dispose();
            runFinally();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
            runFinally();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            this.actual.onSuccess(obj);
            runFinally();
        }

        /* access modifiers changed from: 0000 */
        public void runFinally() {
            if (compareAndSet(0, 1)) {
                try {
                    this.onFinally.run();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    RxJavaPlugins.onError(th);
                }
            }
        }
    }

    public SingleDoFinally(SingleSource singleSource, Action action) {
        this.source = singleSource;
        this.onFinally = action;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new DoFinallyObserver(singleObserver, this.onFinally));
    }
}
