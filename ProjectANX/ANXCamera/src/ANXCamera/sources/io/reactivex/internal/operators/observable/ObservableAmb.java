package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableAmb extends Observable {
    final ObservableSource[] sources;
    final Iterable sourcesIterable;

    final class AmbCoordinator implements Disposable {
        final Observer actual;
        final AmbInnerObserver[] observers;
        final AtomicInteger winner = new AtomicInteger();

        AmbCoordinator(Observer observer, int i) {
            this.actual = observer;
            this.observers = new AmbInnerObserver[i];
        }

        public void dispose() {
            if (this.winner.get() != -1) {
                this.winner.lazySet(-1);
                for (AmbInnerObserver dispose : this.observers) {
                    dispose.dispose();
                }
            }
        }

        public boolean isDisposed() {
            return this.winner.get() == -1;
        }

        public void subscribe(ObservableSource[] observableSourceArr) {
            AmbInnerObserver[] ambInnerObserverArr = this.observers;
            int length = ambInnerObserverArr.length;
            int i = 0;
            while (i < length) {
                int i2 = i + 1;
                ambInnerObserverArr[i] = new AmbInnerObserver(this, i2, this.actual);
                i = i2;
            }
            this.winner.lazySet(0);
            this.actual.onSubscribe(this);
            for (int i3 = 0; i3 < length && this.winner.get() == 0; i3++) {
                observableSourceArr[i3].subscribe(ambInnerObserverArr[i3]);
            }
        }

        public boolean win(int i) {
            int i2 = this.winner.get();
            boolean z = true;
            int i3 = 0;
            if (i2 != 0) {
                if (i2 != i) {
                    z = false;
                }
                return z;
            } else if (!this.winner.compareAndSet(0, i)) {
                return false;
            } else {
                AmbInnerObserver[] ambInnerObserverArr = this.observers;
                int length = ambInnerObserverArr.length;
                while (i3 < length) {
                    int i4 = i3 + 1;
                    if (i4 != i) {
                        ambInnerObserverArr[i3].dispose();
                    }
                    i3 = i4;
                }
                return true;
            }
        }
    }

    final class AmbInnerObserver extends AtomicReference implements Observer {
        private static final long serialVersionUID = -1185974347409665484L;
        final Observer actual;
        final int index;
        final AmbCoordinator parent;
        boolean won;

        AmbInnerObserver(AmbCoordinator ambCoordinator, int i, Observer observer) {
            this.parent = ambCoordinator;
            this.index = i;
            this.actual = observer;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public void onComplete() {
            if (!this.won) {
                if (this.parent.win(this.index)) {
                    this.won = true;
                } else {
                    return;
                }
            }
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            if (!this.won) {
                if (this.parent.win(this.index)) {
                    this.won = true;
                } else {
                    RxJavaPlugins.onError(th);
                    return;
                }
            }
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.won) {
                if (this.parent.win(this.index)) {
                    this.won = true;
                } else {
                    ((Disposable) get()).dispose();
                    return;
                }
            }
            this.actual.onNext(obj);
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }
    }

    public ObservableAmb(ObservableSource[] observableSourceArr, Iterable iterable) {
        this.sources = observableSourceArr;
        this.sourcesIterable = iterable;
    }

    public void subscribeActual(Observer observer) {
        int i;
        ObservableSource[] observableSourceArr = this.sources;
        if (observableSourceArr == null) {
            observableSourceArr = new Observable[8];
            try {
                i = 0;
                for (ObservableSource observableSource : this.sourcesIterable) {
                    if (observableSource == null) {
                        EmptyDisposable.error((Throwable) new NullPointerException("One of the sources is null"), observer);
                        return;
                    }
                    if (i == observableSourceArr.length) {
                        ObservableSource[] observableSourceArr2 = new ObservableSource[((i >> 2) + i)];
                        System.arraycopy(observableSourceArr, 0, observableSourceArr2, 0, i);
                        observableSourceArr = observableSourceArr2;
                    }
                    int i2 = i + 1;
                    observableSourceArr[i] = observableSource;
                    i = i2;
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                EmptyDisposable.error(th, observer);
                return;
            }
        } else {
            i = observableSourceArr.length;
        }
        if (i == 0) {
            EmptyDisposable.complete(observer);
        } else if (i == 1) {
            observableSourceArr[0].subscribe(observer);
        } else {
            new AmbCoordinator(observer, i).subscribe(observableSourceArr);
        }
    }
}
