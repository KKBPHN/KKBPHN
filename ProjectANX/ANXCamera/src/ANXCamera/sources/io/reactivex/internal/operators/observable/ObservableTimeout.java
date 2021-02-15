package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.ObserverFullArbiter;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.FullArbiterObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.SerializedObserver;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableTimeout extends AbstractObservableWithUpstream {
    final ObservableSource firstTimeoutIndicator;
    final Function itemTimeoutIndicator;
    final ObservableSource other;

    interface OnTimeout {
        void innerError(Throwable th);

        void timeout(long j);
    }

    final class TimeoutInnerObserver extends DisposableObserver {
        boolean done;
        final long index;
        final OnTimeout parent;

        TimeoutInnerObserver(OnTimeout onTimeout, long j) {
            this.parent = onTimeout;
            this.index = j;
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.timeout(this.index);
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.parent.innerError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                this.done = true;
                dispose();
                this.parent.timeout(this.index);
            }
        }
    }

    final class TimeoutObserver extends AtomicReference implements Observer, Disposable, OnTimeout {
        private static final long serialVersionUID = 2672739326310051084L;
        final Observer actual;
        final ObservableSource firstTimeoutIndicator;
        volatile long index;
        final Function itemTimeoutIndicator;
        Disposable s;

        TimeoutObserver(Observer observer, ObservableSource observableSource, Function function) {
            this.actual = observer;
            this.firstTimeoutIndicator = observableSource;
            this.itemTimeoutIndicator = function;
        }

        public void dispose() {
            if (DisposableHelper.dispose(this)) {
                this.s.dispose();
            }
        }

        public void innerError(Throwable th) {
            this.s.dispose();
            this.actual.onError(th);
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void onComplete() {
            DisposableHelper.dispose(this);
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            DisposableHelper.dispose(this);
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            long j = this.index + 1;
            this.index = j;
            this.actual.onNext(obj);
            Disposable disposable = (Disposable) get();
            if (disposable != null) {
                disposable.dispose();
            }
            try {
                Object apply = this.itemTimeoutIndicator.apply(obj);
                ObjectHelper.requireNonNull(apply, "The ObservableSource returned is null");
                ObservableSource observableSource = (ObservableSource) apply;
                TimeoutInnerObserver timeoutInnerObserver = new TimeoutInnerObserver(this, j);
                if (compareAndSet(disposable, timeoutInnerObserver)) {
                    observableSource.subscribe(timeoutInnerObserver);
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                dispose();
                this.actual.onError(th);
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                Observer observer = this.actual;
                ObservableSource observableSource = this.firstTimeoutIndicator;
                if (observableSource != null) {
                    TimeoutInnerObserver timeoutInnerObserver = new TimeoutInnerObserver(this, 0);
                    if (compareAndSet(null, timeoutInnerObserver)) {
                        observer.onSubscribe(this);
                        observableSource.subscribe(timeoutInnerObserver);
                        return;
                    }
                    return;
                }
                observer.onSubscribe(this);
            }
        }

        public void timeout(long j) {
            if (j == this.index) {
                dispose();
                this.actual.onError(new TimeoutException());
            }
        }
    }

    final class TimeoutOtherObserver extends AtomicReference implements Observer, Disposable, OnTimeout {
        private static final long serialVersionUID = -1957813281749686898L;
        final Observer actual;
        final ObserverFullArbiter arbiter;
        boolean done;
        final ObservableSource firstTimeoutIndicator;
        volatile long index;
        final Function itemTimeoutIndicator;
        final ObservableSource other;
        Disposable s;

        TimeoutOtherObserver(Observer observer, ObservableSource observableSource, Function function, ObservableSource observableSource2) {
            this.actual = observer;
            this.firstTimeoutIndicator = observableSource;
            this.itemTimeoutIndicator = function;
            this.other = observableSource2;
            this.arbiter = new ObserverFullArbiter(observer, this, 8);
        }

        public void dispose() {
            if (DisposableHelper.dispose(this)) {
                this.s.dispose();
            }
        }

        public void innerError(Throwable th) {
            this.s.dispose();
            this.actual.onError(th);
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                dispose();
                this.arbiter.onComplete(this.s);
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            dispose();
            this.arbiter.onError(th, this.s);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                long j = this.index + 1;
                this.index = j;
                if (this.arbiter.onNext(obj, this.s)) {
                    Disposable disposable = (Disposable) get();
                    if (disposable != null) {
                        disposable.dispose();
                    }
                    try {
                        Object apply = this.itemTimeoutIndicator.apply(obj);
                        ObjectHelper.requireNonNull(apply, "The ObservableSource returned is null");
                        ObservableSource observableSource = (ObservableSource) apply;
                        TimeoutInnerObserver timeoutInnerObserver = new TimeoutInnerObserver(this, j);
                        if (compareAndSet(disposable, timeoutInnerObserver)) {
                            observableSource.subscribe(timeoutInnerObserver);
                        }
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.actual.onError(th);
                    }
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.arbiter.setDisposable(disposable);
                Observer observer = this.actual;
                ObservableSource observableSource = this.firstTimeoutIndicator;
                if (observableSource != null) {
                    TimeoutInnerObserver timeoutInnerObserver = new TimeoutInnerObserver(this, 0);
                    if (compareAndSet(null, timeoutInnerObserver)) {
                        observer.onSubscribe(this.arbiter);
                        observableSource.subscribe(timeoutInnerObserver);
                        return;
                    }
                    return;
                }
                observer.onSubscribe(this.arbiter);
            }
        }

        public void timeout(long j) {
            if (j == this.index) {
                dispose();
                this.other.subscribe(new FullArbiterObserver(this.arbiter));
            }
        }
    }

    public ObservableTimeout(ObservableSource observableSource, ObservableSource observableSource2, Function function, ObservableSource observableSource3) {
        super(observableSource);
        this.firstTimeoutIndicator = observableSource2;
        this.itemTimeoutIndicator = function;
        this.other = observableSource3;
    }

    public void subscribeActual(Observer observer) {
        ObservableSource observableSource = this.other;
        if (observableSource == null) {
            this.source.subscribe(new TimeoutObserver(new SerializedObserver(observer), this.firstTimeoutIndicator, this.itemTimeoutIndicator));
        } else {
            this.source.subscribe(new TimeoutOtherObserver(observer, this.firstTimeoutIndicator, this.itemTimeoutIndicator, observableSource));
        }
    }
}
