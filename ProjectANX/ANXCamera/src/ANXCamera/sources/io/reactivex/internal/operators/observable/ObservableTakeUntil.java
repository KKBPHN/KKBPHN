package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ArrayCompositeDisposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observers.SerializedObserver;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ObservableTakeUntil extends AbstractObservableWithUpstream {
    final ObservableSource other;

    final class TakeUntil implements Observer {
        private final ArrayCompositeDisposable frc;
        private final SerializedObserver serial;

        TakeUntil(ArrayCompositeDisposable arrayCompositeDisposable, SerializedObserver serializedObserver) {
            this.frc = arrayCompositeDisposable;
            this.serial = serializedObserver;
        }

        public void onComplete() {
            this.frc.dispose();
            this.serial.onComplete();
        }

        public void onError(Throwable th) {
            this.frc.dispose();
            this.serial.onError(th);
        }

        public void onNext(Object obj) {
            this.frc.dispose();
            this.serial.onComplete();
        }

        public void onSubscribe(Disposable disposable) {
            this.frc.setResource(1, disposable);
        }
    }

    final class TakeUntilObserver extends AtomicBoolean implements Observer {
        private static final long serialVersionUID = 3451719290311127173L;
        final Observer actual;
        final ArrayCompositeDisposable frc;
        Disposable s;

        TakeUntilObserver(Observer observer, ArrayCompositeDisposable arrayCompositeDisposable) {
            this.actual = observer;
            this.frc = arrayCompositeDisposable;
        }

        public void onComplete() {
            this.frc.dispose();
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.frc.dispose();
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            this.actual.onNext(obj);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.frc.setResource(0, disposable);
            }
        }
    }

    public ObservableTakeUntil(ObservableSource observableSource, ObservableSource observableSource2) {
        super(observableSource);
        this.other = observableSource2;
    }

    public void subscribeActual(Observer observer) {
        SerializedObserver serializedObserver = new SerializedObserver(observer);
        ArrayCompositeDisposable arrayCompositeDisposable = new ArrayCompositeDisposable(2);
        TakeUntilObserver takeUntilObserver = new TakeUntilObserver(serializedObserver, arrayCompositeDisposable);
        observer.onSubscribe(arrayCompositeDisposable);
        this.other.subscribe(new TakeUntil(arrayCompositeDisposable, serializedObserver));
        this.source.subscribe(takeUntilObserver);
    }
}
