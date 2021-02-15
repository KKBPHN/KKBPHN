package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ArrayCompositeDisposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observers.SerializedObserver;

public final class ObservableSkipUntil extends AbstractObservableWithUpstream {
    final ObservableSource other;

    final class SkipUntil implements Observer {
        private final ArrayCompositeDisposable frc;
        Disposable s;
        private final SerializedObserver serial;
        private final SkipUntilObserver sus;

        SkipUntil(ArrayCompositeDisposable arrayCompositeDisposable, SkipUntilObserver skipUntilObserver, SerializedObserver serializedObserver) {
            this.frc = arrayCompositeDisposable;
            this.sus = skipUntilObserver;
            this.serial = serializedObserver;
        }

        public void onComplete() {
            this.sus.notSkipping = true;
        }

        public void onError(Throwable th) {
            this.frc.dispose();
            this.serial.onError(th);
        }

        public void onNext(Object obj) {
            this.s.dispose();
            this.sus.notSkipping = true;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.frc.setResource(1, disposable);
            }
        }
    }

    final class SkipUntilObserver implements Observer {
        final Observer actual;
        final ArrayCompositeDisposable frc;
        volatile boolean notSkipping;
        boolean notSkippingLocal;
        Disposable s;

        SkipUntilObserver(Observer observer, ArrayCompositeDisposable arrayCompositeDisposable) {
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
            if (!this.notSkippingLocal) {
                if (this.notSkipping) {
                    this.notSkippingLocal = true;
                } else {
                    return;
                }
            }
            this.actual.onNext(obj);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.frc.setResource(0, disposable);
            }
        }
    }

    public ObservableSkipUntil(ObservableSource observableSource, ObservableSource observableSource2) {
        super(observableSource);
        this.other = observableSource2;
    }

    public void subscribeActual(Observer observer) {
        SerializedObserver serializedObserver = new SerializedObserver(observer);
        ArrayCompositeDisposable arrayCompositeDisposable = new ArrayCompositeDisposable(2);
        serializedObserver.onSubscribe(arrayCompositeDisposable);
        SkipUntilObserver skipUntilObserver = new SkipUntilObserver(serializedObserver, arrayCompositeDisposable);
        this.other.subscribe(new SkipUntil(arrayCompositeDisposable, skipUntilObserver, serializedObserver));
        this.source.subscribe(skipUntilObserver);
    }
}
