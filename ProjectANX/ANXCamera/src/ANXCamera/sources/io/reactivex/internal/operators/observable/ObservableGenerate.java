package io.reactivex.internal.operators.observable;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;

public final class ObservableGenerate extends Observable {
    final Consumer disposeState;
    final BiFunction generator;
    final Callable stateSupplier;

    final class GeneratorDisposable implements Emitter, Disposable {
        final Observer actual;
        volatile boolean cancelled;
        final Consumer disposeState;
        final BiFunction generator;
        boolean hasNext;
        Object state;
        boolean terminate;

        GeneratorDisposable(Observer observer, BiFunction biFunction, Consumer consumer, Object obj) {
            this.actual = observer;
            this.generator = biFunction;
            this.disposeState = consumer;
            this.state = obj;
        }

        private void dispose(Object obj) {
            try {
                this.disposeState.accept(obj);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
        }

        public void dispose() {
            this.cancelled = true;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void onComplete() {
            if (!this.terminate) {
                this.terminate = true;
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.terminate) {
                RxJavaPlugins.onError(th);
                return;
            }
            if (th == null) {
                th = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
            }
            this.terminate = true;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            Throwable nullPointerException;
            if (!this.terminate) {
                if (this.hasNext) {
                    nullPointerException = new IllegalStateException("onNext already called in this generate turn");
                } else if (obj == null) {
                    nullPointerException = new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
                } else {
                    this.hasNext = true;
                    this.actual.onNext(obj);
                    return;
                }
                onError(nullPointerException);
            }
        }

        public void run() {
            Object obj = this.state;
            if (!this.cancelled) {
                BiFunction biFunction = this.generator;
                while (true) {
                    if (this.cancelled) {
                        break;
                    }
                    this.hasNext = false;
                    try {
                        obj = biFunction.apply(obj, this);
                        if (this.terminate) {
                            this.cancelled = true;
                            break;
                        }
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.state = null;
                        this.cancelled = true;
                        onError(th);
                    }
                }
            }
            this.state = null;
            dispose(obj);
        }
    }

    public ObservableGenerate(Callable callable, BiFunction biFunction, Consumer consumer) {
        this.stateSupplier = callable;
        this.generator = biFunction;
        this.disposeState = consumer;
    }

    public void subscribeActual(Observer observer) {
        try {
            GeneratorDisposable generatorDisposable = new GeneratorDisposable(observer, this.generator, this.disposeState, this.stateSupplier.call());
            observer.onSubscribe(generatorDisposable);
            generatorDisposable.run();
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, observer);
        }
    }
}
