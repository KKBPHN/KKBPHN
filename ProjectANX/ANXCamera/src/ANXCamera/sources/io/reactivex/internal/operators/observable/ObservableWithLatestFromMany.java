package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.HalfSerializer;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class ObservableWithLatestFromMany extends AbstractObservableWithUpstream {
    @NonNull
    final Function combiner;
    @Nullable
    final ObservableSource[] otherArray;
    @Nullable
    final Iterable otherIterable;

    final class SingletonArrayFunc implements Function {
        SingletonArrayFunc() {
        }

        public Object apply(Object obj) {
            Object apply = ObservableWithLatestFromMany.this.combiner.apply(new Object[]{obj});
            ObjectHelper.requireNonNull(apply, "The combiner returned a null value");
            return apply;
        }
    }

    final class WithLatestFromObserver extends AtomicInteger implements Observer, Disposable {
        private static final long serialVersionUID = 1577321883966341961L;
        final Observer actual;
        final Function combiner;
        final AtomicReference d;
        volatile boolean done;
        final AtomicThrowable error;
        final WithLatestInnerObserver[] observers;
        final AtomicReferenceArray values;

        WithLatestFromObserver(Observer observer, Function function, int i) {
            this.actual = observer;
            this.combiner = function;
            WithLatestInnerObserver[] withLatestInnerObserverArr = new WithLatestInnerObserver[i];
            for (int i2 = 0; i2 < i; i2++) {
                withLatestInnerObserverArr[i2] = new WithLatestInnerObserver(this, i2);
            }
            this.observers = withLatestInnerObserverArr;
            this.values = new AtomicReferenceArray(i);
            this.d = new AtomicReference();
            this.error = new AtomicThrowable();
        }

        /* access modifiers changed from: 0000 */
        public void cancelAllBut(int i) {
            WithLatestInnerObserver[] withLatestInnerObserverArr = this.observers;
            for (int i2 = 0; i2 < withLatestInnerObserverArr.length; i2++) {
                if (i2 != i) {
                    withLatestInnerObserverArr[i2].dispose();
                }
            }
        }

        public void dispose() {
            DisposableHelper.dispose(this.d);
            for (WithLatestInnerObserver dispose : this.observers) {
                dispose.dispose();
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(int i, boolean z) {
            if (!z) {
                this.done = true;
                cancelAllBut(i);
                HalfSerializer.onComplete(this.actual, (AtomicInteger) this, this.error);
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerError(int i, Throwable th) {
            this.done = true;
            DisposableHelper.dispose(this.d);
            cancelAllBut(i);
            HalfSerializer.onError(this.actual, th, (AtomicInteger) this, this.error);
        }

        /* access modifiers changed from: 0000 */
        public void innerNext(int i, Object obj) {
            this.values.set(i, obj);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) this.d.get());
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                cancelAllBut(-1);
                HalfSerializer.onComplete(this.actual, (AtomicInteger) this, this.error);
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            cancelAllBut(-1);
            HalfSerializer.onError(this.actual, th, (AtomicInteger) this, this.error);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                AtomicReferenceArray atomicReferenceArray = this.values;
                int length = atomicReferenceArray.length();
                Object[] objArr = new Object[(length + 1)];
                int i = 0;
                objArr[0] = obj;
                while (i < length) {
                    Object obj2 = atomicReferenceArray.get(i);
                    if (obj2 != null) {
                        i++;
                        objArr[i] = obj2;
                    } else {
                        return;
                    }
                }
                try {
                    Object apply = this.combiner.apply(objArr);
                    ObjectHelper.requireNonNull(apply, "combiner returned a null value");
                    HalfSerializer.onNext(this.actual, apply, (AtomicInteger) this, this.error);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    dispose();
                    onError(th);
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this.d, disposable);
        }

        /* access modifiers changed from: 0000 */
        public void subscribe(ObservableSource[] observableSourceArr, int i) {
            WithLatestInnerObserver[] withLatestInnerObserverArr = this.observers;
            AtomicReference atomicReference = this.d;
            for (int i2 = 0; i2 < i && !DisposableHelper.isDisposed((Disposable) atomicReference.get()) && !this.done; i2++) {
                observableSourceArr[i2].subscribe(withLatestInnerObserverArr[i2]);
            }
        }
    }

    final class WithLatestInnerObserver extends AtomicReference implements Observer {
        private static final long serialVersionUID = 3256684027868224024L;
        boolean hasValue;
        final int index;
        final WithLatestFromObserver parent;

        WithLatestInnerObserver(WithLatestFromObserver withLatestFromObserver, int i) {
            this.parent = withLatestFromObserver;
            this.index = i;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public void onComplete() {
            this.parent.innerComplete(this.index, this.hasValue);
        }

        public void onError(Throwable th) {
            this.parent.innerError(this.index, th);
        }

        public void onNext(Object obj) {
            if (!this.hasValue) {
                this.hasValue = true;
            }
            this.parent.innerNext(this.index, obj);
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }
    }

    public ObservableWithLatestFromMany(@NonNull ObservableSource observableSource, @NonNull Iterable iterable, @NonNull Function function) {
        super(observableSource);
        this.otherArray = null;
        this.otherIterable = iterable;
        this.combiner = function;
    }

    public ObservableWithLatestFromMany(@NonNull ObservableSource observableSource, @NonNull ObservableSource[] observableSourceArr, @NonNull Function function) {
        super(observableSource);
        this.otherArray = observableSourceArr;
        this.otherIterable = null;
        this.combiner = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        int i;
        ObservableSource[] observableSourceArr = this.otherArray;
        if (observableSourceArr == null) {
            observableSourceArr = new ObservableSource[8];
            try {
                i = 0;
                for (ObservableSource observableSource : this.otherIterable) {
                    if (i == observableSourceArr.length) {
                        observableSourceArr = (ObservableSource[]) Arrays.copyOf(observableSourceArr, (i >> 1) + i);
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
            new ObservableMap(this.source, new SingletonArrayFunc()).subscribeActual(observer);
            return;
        }
        WithLatestFromObserver withLatestFromObserver = new WithLatestFromObserver(observer, this.combiner, i);
        observer.onSubscribe(withLatestFromObserver);
        withLatestFromObserver.subscribe(observableSourceArr, i);
        this.source.subscribe(withLatestFromObserver);
    }
}
