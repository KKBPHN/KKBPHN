package io.reactivex.internal.operators.single;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleUnsubscribeOn extends Single {
    final Scheduler scheduler;
    final SingleSource source;

    final class UnsubscribeOnSingleObserver extends AtomicReference implements SingleObserver, Disposable, Runnable {
        private static final long serialVersionUID = 3256698449646456986L;
        final SingleObserver actual;
        Disposable ds;
        final Scheduler scheduler;

        UnsubscribeOnSingleObserver(SingleObserver singleObserver, Scheduler scheduler2) {
            this.actual = singleObserver;
            this.scheduler = scheduler2;
        }

        public void dispose() {
            Disposable disposable = (Disposable) getAndSet(DisposableHelper.DISPOSED);
            if (disposable != DisposableHelper.DISPOSED) {
                this.ds = disposable;
                this.scheduler.scheduleDirect(this);
            }
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
            this.actual.onSuccess(obj);
        }

        public void run() {
            this.ds.dispose();
        }
    }

    public SingleUnsubscribeOn(SingleSource singleSource, Scheduler scheduler2) {
        this.source = singleSource;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new UnsubscribeOnSingleObserver(singleObserver, this.scheduler));
    }
}
