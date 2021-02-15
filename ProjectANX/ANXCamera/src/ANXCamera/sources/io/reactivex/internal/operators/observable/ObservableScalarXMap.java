package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.QueueDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableScalarXMap {

    public final class ScalarDisposable extends AtomicInteger implements QueueDisposable, Runnable {
        static final int FUSED = 1;
        static final int ON_COMPLETE = 3;
        static final int ON_NEXT = 2;
        static final int START = 0;
        private static final long serialVersionUID = 3880992722410194083L;
        final Observer observer;
        final Object value;

        public ScalarDisposable(Observer observer2, Object obj) {
            this.observer = observer2;
            this.value = obj;
        }

        public void clear() {
            lazySet(3);
        }

        public void dispose() {
            set(3);
        }

        public boolean isDisposed() {
            return get() == 3;
        }

        public boolean isEmpty() {
            return get() != 1;
        }

        public boolean offer(Object obj) {
            throw new UnsupportedOperationException("Should not be called!");
        }

        public boolean offer(Object obj, Object obj2) {
            throw new UnsupportedOperationException("Should not be called!");
        }

        @Nullable
        public Object poll() {
            if (get() != 1) {
                return null;
            }
            lazySet(3);
            return this.value;
        }

        public int requestFusion(int i) {
            if ((i & 1) == 0) {
                return 0;
            }
            lazySet(1);
            return 1;
        }

        public void run() {
            if (get() == 0 && compareAndSet(0, 2)) {
                this.observer.onNext(this.value);
                if (get() == 2) {
                    lazySet(3);
                    this.observer.onComplete();
                }
            }
        }
    }

    final class ScalarXMapObservable extends Observable {
        final Function mapper;
        final Object value;

        ScalarXMapObservable(Object obj, Function function) {
            this.value = obj;
            this.mapper = function;
        }

        public void subscribeActual(Observer observer) {
            try {
                Object apply = this.mapper.apply(this.value);
                ObjectHelper.requireNonNull(apply, "The mapper returned a null ObservableSource");
                ObservableSource observableSource = (ObservableSource) apply;
                if (observableSource instanceof Callable) {
                    try {
                        Object call = ((Callable) observableSource).call();
                        if (call == null) {
                            EmptyDisposable.complete(observer);
                            return;
                        }
                        ScalarDisposable scalarDisposable = new ScalarDisposable(observer, call);
                        observer.onSubscribe(scalarDisposable);
                        scalarDisposable.run();
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        EmptyDisposable.error(th, observer);
                    }
                } else {
                    observableSource.subscribe(observer);
                }
            } catch (Throwable th2) {
                EmptyDisposable.error(th2, observer);
            }
        }
    }

    private ObservableScalarXMap() {
        throw new IllegalStateException("No instances!");
    }

    public static Observable scalarXMap(Object obj, Function function) {
        return RxJavaPlugins.onAssembly((Observable) new ScalarXMapObservable(obj, function));
    }

    public static boolean tryScalarXMapSubscribe(ObservableSource observableSource, Observer observer, Function function) {
        if (!(observableSource instanceof Callable)) {
            return false;
        }
        try {
            Object call = ((Callable) observableSource).call();
            if (call == null) {
                EmptyDisposable.complete(observer);
                return true;
            }
            Object apply = function.apply(call);
            ObjectHelper.requireNonNull(apply, "The mapper returned a null ObservableSource");
            ObservableSource observableSource2 = (ObservableSource) apply;
            if (observableSource2 instanceof Callable) {
                Object call2 = ((Callable) observableSource2).call();
                if (call2 == null) {
                    EmptyDisposable.complete(observer);
                    return true;
                }
                ScalarDisposable scalarDisposable = new ScalarDisposable(observer, call2);
                observer.onSubscribe(scalarDisposable);
                scalarDisposable.run();
            } else {
                observableSource2.subscribe(observer);
            }
            return true;
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, observer);
            return true;
        }
    }
}
