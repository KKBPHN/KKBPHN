package io.reactivex.internal.operators.observable;

import io.reactivex.Notification;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableDematerialize extends AbstractObservableWithUpstream {

    final class DematerializeObserver implements Observer, Disposable {
        final Observer actual;
        boolean done;
        Disposable s;

        DematerializeObserver(Observer observer) {
            this.actual = observer;
        }

        public void dispose() {
            this.s.dispose();
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        public void onNext(Notification notification) {
            if (this.done) {
                if (notification.isOnError()) {
                    RxJavaPlugins.onError(notification.getError());
                }
                return;
            }
            if (notification.isOnError()) {
                this.s.dispose();
                onError(notification.getError());
            } else if (notification.isOnComplete()) {
                this.s.dispose();
                onComplete();
            } else {
                this.actual.onNext(notification.getValue());
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableDematerialize(ObservableSource observableSource) {
        super(observableSource);
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(new DematerializeObserver(observer));
    }
}
