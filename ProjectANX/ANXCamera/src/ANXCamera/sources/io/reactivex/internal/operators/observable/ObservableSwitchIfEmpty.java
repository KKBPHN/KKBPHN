package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;

public final class ObservableSwitchIfEmpty extends AbstractObservableWithUpstream {
    final ObservableSource other;

    final class SwitchIfEmptyObserver implements Observer {
        final Observer actual;
        final SequentialDisposable arbiter = new SequentialDisposable();
        boolean empty = true;
        final ObservableSource other;

        SwitchIfEmptyObserver(Observer observer, ObservableSource observableSource) {
            this.actual = observer;
            this.other = observableSource;
        }

        public void onComplete() {
            if (this.empty) {
                this.empty = false;
                this.other.subscribe(this);
                return;
            }
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (this.empty) {
                this.empty = false;
            }
            this.actual.onNext(obj);
        }

        public void onSubscribe(Disposable disposable) {
            this.arbiter.update(disposable);
        }
    }

    public ObservableSwitchIfEmpty(ObservableSource observableSource, ObservableSource observableSource2) {
        super(observableSource);
        this.other = observableSource2;
    }

    public void subscribeActual(Observer observer) {
        SwitchIfEmptyObserver switchIfEmptyObserver = new SwitchIfEmptyObserver(observer, this.other);
        observer.onSubscribe(switchIfEmptyObserver.arbiter);
        this.source.subscribe(switchIfEmptyObserver);
    }
}
