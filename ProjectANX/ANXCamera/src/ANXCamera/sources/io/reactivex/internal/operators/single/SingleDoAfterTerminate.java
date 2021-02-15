package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class SingleDoAfterTerminate extends Single {
    final Action onAfterTerminate;
    final SingleSource source;

    final class DoAfterTerminateObserver implements SingleObserver, Disposable {
        final SingleObserver actual;
        Disposable d;
        final Action onAfterTerminate;

        DoAfterTerminateObserver(SingleObserver singleObserver, Action action) {
            this.actual = singleObserver;
            this.onAfterTerminate = action;
        }

        private void onAfterTerminate() {
            try {
                this.onAfterTerminate.run();
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
        }

        public void dispose() {
            this.d.dispose();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
            onAfterTerminate();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            this.actual.onSuccess(obj);
            onAfterTerminate();
        }
    }

    public SingleDoAfterTerminate(SingleSource singleSource, Action action) {
        this.source = singleSource;
        this.onAfterTerminate = action;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new DoAfterTerminateObserver(singleObserver, this.onAfterTerminate));
    }
}
