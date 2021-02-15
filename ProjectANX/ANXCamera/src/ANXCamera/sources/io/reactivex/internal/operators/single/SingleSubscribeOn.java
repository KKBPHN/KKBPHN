package io.reactivex.internal.operators.single;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.SequentialDisposable;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleSubscribeOn extends Single {
    final Scheduler scheduler;
    final SingleSource source;

    final class SubscribeOnObserver extends AtomicReference implements SingleObserver, Disposable, Runnable {
        private static final long serialVersionUID = 7000911171163930287L;
        final SingleObserver actual;
        final SingleSource source;
        final SequentialDisposable task = new SequentialDisposable();

        SubscribeOnObserver(SingleObserver singleObserver, SingleSource singleSource) {
            this.actual = singleObserver;
            this.source = singleSource;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
            this.task.dispose();
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }

        public void onSuccess(Object obj) {
            this.actual.onSuccess(obj);
        }

        public void run() {
            this.source.subscribe(this);
        }
    }

    public SingleSubscribeOn(SingleSource singleSource, Scheduler scheduler2) {
        this.source = singleSource;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        SubscribeOnObserver subscribeOnObserver = new SubscribeOnObserver(singleObserver, this.source);
        singleObserver.onSubscribe(subscribeOnObserver);
        subscribeOnObserver.task.replace(this.scheduler.scheduleDirect(subscribeOnObserver));
    }
}
