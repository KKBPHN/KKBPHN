package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableDelaySubscriptionOther extends Observable {
    final ObservableSource main;
    final ObservableSource other;

    final class DelayObserver implements Observer {
        final Observer child;
        boolean done;
        final SequentialDisposable serial;

        final class OnComplete implements Observer {
            OnComplete() {
            }

            public void onComplete() {
                DelayObserver.this.child.onComplete();
            }

            public void onError(Throwable th) {
                DelayObserver.this.child.onError(th);
            }

            public void onNext(Object obj) {
                DelayObserver.this.child.onNext(obj);
            }

            public void onSubscribe(Disposable disposable) {
                DelayObserver.this.serial.update(disposable);
            }
        }

        DelayObserver(SequentialDisposable sequentialDisposable, Observer observer) {
            this.serial = sequentialDisposable;
            this.child = observer;
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                ObservableDelaySubscriptionOther.this.main.subscribe(new OnComplete());
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.child.onError(th);
        }

        public void onNext(Object obj) {
            onComplete();
        }

        public void onSubscribe(Disposable disposable) {
            this.serial.update(disposable);
        }
    }

    public ObservableDelaySubscriptionOther(ObservableSource observableSource, ObservableSource observableSource2) {
        this.main = observableSource;
        this.other = observableSource2;
    }

    public void subscribeActual(Observer observer) {
        SequentialDisposable sequentialDisposable = new SequentialDisposable();
        observer.onSubscribe(sequentialDisposable);
        this.other.subscribe(new DelayObserver(sequentialDisposable, observer));
    }
}
