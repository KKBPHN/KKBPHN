package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.SerializedObserver;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableDebounce extends AbstractObservableWithUpstream {
    final Function debounceSelector;

    final class DebounceObserver implements Observer, Disposable {
        final Observer actual;
        final Function debounceSelector;
        final AtomicReference debouncer = new AtomicReference();
        boolean done;
        volatile long index;
        Disposable s;

        final class DebounceInnerObserver extends DisposableObserver {
            boolean done;
            final long index;
            final AtomicBoolean once = new AtomicBoolean();
            final DebounceObserver parent;
            final Object value;

            DebounceInnerObserver(DebounceObserver debounceObserver, long j, Object obj) {
                this.parent = debounceObserver;
                this.index = j;
                this.value = obj;
            }

            /* access modifiers changed from: 0000 */
            public void emit() {
                if (this.once.compareAndSet(false, true)) {
                    this.parent.emit(this.index, this.value);
                }
            }

            public void onComplete() {
                if (!this.done) {
                    this.done = true;
                    emit();
                }
            }

            public void onError(Throwable th) {
                if (this.done) {
                    RxJavaPlugins.onError(th);
                    return;
                }
                this.done = true;
                this.parent.onError(th);
            }

            public void onNext(Object obj) {
                if (!this.done) {
                    this.done = true;
                    dispose();
                    emit();
                }
            }
        }

        DebounceObserver(Observer observer, Function function) {
            this.actual = observer;
            this.debounceSelector = function;
        }

        public void dispose() {
            this.s.dispose();
            DisposableHelper.dispose(this.debouncer);
        }

        /* access modifiers changed from: 0000 */
        public void emit(long j, Object obj) {
            if (j == this.index) {
                this.actual.onNext(obj);
            }
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                Disposable disposable = (Disposable) this.debouncer.get();
                if (disposable != DisposableHelper.DISPOSED) {
                    ((DebounceInnerObserver) disposable).emit();
                    DisposableHelper.dispose(this.debouncer);
                    this.actual.onComplete();
                }
            }
        }

        public void onError(Throwable th) {
            DisposableHelper.dispose(this.debouncer);
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                long j = this.index + 1;
                this.index = j;
                Disposable disposable = (Disposable) this.debouncer.get();
                if (disposable != null) {
                    disposable.dispose();
                }
                try {
                    Object apply = this.debounceSelector.apply(obj);
                    ObjectHelper.requireNonNull(apply, "The ObservableSource supplied is null");
                    ObservableSource observableSource = (ObservableSource) apply;
                    DebounceInnerObserver debounceInnerObserver = new DebounceInnerObserver(this, j, obj);
                    if (this.debouncer.compareAndSet(disposable, debounceInnerObserver)) {
                        observableSource.subscribe(debounceInnerObserver);
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    dispose();
                    this.actual.onError(th);
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableDebounce(ObservableSource observableSource, Function function) {
        super(observableSource);
        this.debounceSelector = function;
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(new DebounceObserver(new SerializedObserver(observer), this.debounceSelector));
    }
}
