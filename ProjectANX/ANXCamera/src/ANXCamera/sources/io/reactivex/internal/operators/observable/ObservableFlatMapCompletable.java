package io.reactivex.internal.operators.observable;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.BasicIntQueueDisposable;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableFlatMapCompletable extends AbstractObservableWithUpstream {
    final boolean delayErrors;
    final Function mapper;

    final class FlatMapCompletableMainObserver extends BasicIntQueueDisposable implements Observer {
        private static final long serialVersionUID = 8443155186132538303L;
        final Observer actual;
        Disposable d;
        final boolean delayErrors;
        volatile boolean disposed;
        final AtomicThrowable errors = new AtomicThrowable();
        final Function mapper;
        final CompositeDisposable set = new CompositeDisposable();

        final class InnerObserver extends AtomicReference implements CompletableObserver, Disposable {
            private static final long serialVersionUID = 8606673141535671828L;

            InnerObserver() {
            }

            public void dispose() {
                DisposableHelper.dispose(this);
            }

            public boolean isDisposed() {
                return DisposableHelper.isDisposed((Disposable) get());
            }

            public void onComplete() {
                FlatMapCompletableMainObserver.this.innerComplete(this);
            }

            public void onError(Throwable th) {
                FlatMapCompletableMainObserver.this.innerError(this, th);
            }

            public void onSubscribe(Disposable disposable) {
                DisposableHelper.setOnce(this, disposable);
            }
        }

        FlatMapCompletableMainObserver(Observer observer, Function function, boolean z) {
            this.actual = observer;
            this.mapper = function;
            this.delayErrors = z;
            lazySet(1);
        }

        public void clear() {
        }

        public void dispose() {
            this.disposed = true;
            this.d.dispose();
            this.set.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(InnerObserver innerObserver) {
            this.set.delete(innerObserver);
            onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void innerError(InnerObserver innerObserver, Throwable th) {
            this.set.delete(innerObserver);
            onError(th);
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public boolean isEmpty() {
            return true;
        }

        public void onComplete() {
            if (decrementAndGet() == 0) {
                Throwable terminate = this.errors.terminate();
                Observer observer = this.actual;
                if (terminate != null) {
                    observer.onError(terminate);
                } else {
                    observer.onComplete();
                }
            }
        }

        public void onError(Throwable th) {
            if (this.errors.addThrowable(th)) {
                if (!this.delayErrors) {
                    dispose();
                    if (getAndSet(0) <= 0) {
                        return;
                    }
                } else if (decrementAndGet() != 0) {
                    return;
                }
                this.actual.onError(this.errors.terminate());
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onNext(Object obj) {
            try {
                Object apply = this.mapper.apply(obj);
                ObjectHelper.requireNonNull(apply, "The mapper returned a null CompletableSource");
                CompletableSource completableSource = (CompletableSource) apply;
                getAndIncrement();
                InnerObserver innerObserver = new InnerObserver();
                if (!this.disposed && this.set.add(innerObserver)) {
                    completableSource.subscribe(innerObserver);
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.d.dispose();
                onError(th);
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        @Nullable
        public Object poll() {
            return null;
        }

        public int requestFusion(int i) {
            return i & 2;
        }
    }

    public ObservableFlatMapCompletable(ObservableSource observableSource, Function function, boolean z) {
        super(observableSource);
        this.mapper = function;
        this.delayErrors = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        this.source.subscribe(new FlatMapCompletableMainObserver(observer, this.mapper, this.delayErrors));
    }
}
