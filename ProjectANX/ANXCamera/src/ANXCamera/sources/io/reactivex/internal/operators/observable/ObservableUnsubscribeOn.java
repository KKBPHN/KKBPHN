package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ObservableUnsubscribeOn extends AbstractObservableWithUpstream {
    final Scheduler scheduler;

    final class UnsubscribeObserver extends AtomicBoolean implements Observer, Disposable {
        private static final long serialVersionUID = 1015244841293359600L;
        final Observer actual;
        Disposable s;
        final Scheduler scheduler;

        final class DisposeTask implements Runnable {
            DisposeTask() {
            }

            public void run() {
                UnsubscribeObserver.this.s.dispose();
            }
        }

        UnsubscribeObserver(Observer observer, Scheduler scheduler2) {
            this.actual = observer;
            this.scheduler = scheduler2;
        }

        public void dispose() {
            if (compareAndSet(false, true)) {
                this.scheduler.scheduleDirect(new DisposeTask());
            }
        }

        public boolean isDisposed() {
            return get();
        }

        public void onComplete() {
            if (!get()) {
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (get()) {
                RxJavaPlugins.onError(th);
            } else {
                this.actual.onError(th);
            }
        }

        public void onNext(Object obj) {
            if (!get()) {
                this.actual.onNext(obj);
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableUnsubscribeOn(ObservableSource observableSource, Scheduler scheduler2) {
        super(observableSource);
        this.scheduler = scheduler2;
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(new UnsubscribeObserver(observer, this.scheduler));
    }
}
