package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.HalfSerializer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableRepeatWhen extends AbstractObservableWithUpstream {
    final Function handler;

    final class RepeatWhenObserver extends AtomicInteger implements Observer, Disposable {
        private static final long serialVersionUID = 802743776666017014L;
        volatile boolean active;
        final Observer actual;
        final AtomicReference d = new AtomicReference();
        final AtomicThrowable error = new AtomicThrowable();
        final InnerRepeatObserver inner = new InnerRepeatObserver();
        final Subject signaller;
        final ObservableSource source;
        final AtomicInteger wip = new AtomicInteger();

        final class InnerRepeatObserver extends AtomicReference implements Observer {
            private static final long serialVersionUID = 3254781284376480842L;

            InnerRepeatObserver() {
            }

            public void onComplete() {
                RepeatWhenObserver.this.innerComplete();
            }

            public void onError(Throwable th) {
                RepeatWhenObserver.this.innerError(th);
            }

            public void onNext(Object obj) {
                RepeatWhenObserver.this.innerNext();
            }

            public void onSubscribe(Disposable disposable) {
                DisposableHelper.setOnce(this, disposable);
            }
        }

        RepeatWhenObserver(Observer observer, Subject subject, ObservableSource observableSource) {
            this.actual = observer;
            this.signaller = subject;
            this.source = observableSource;
        }

        public void dispose() {
            DisposableHelper.dispose(this.d);
            DisposableHelper.dispose(this.inner);
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete() {
            DisposableHelper.dispose(this.d);
            HalfSerializer.onComplete(this.actual, (AtomicInteger) this, this.error);
        }

        /* access modifiers changed from: 0000 */
        public void innerError(Throwable th) {
            DisposableHelper.dispose(this.d);
            HalfSerializer.onError(this.actual, th, (AtomicInteger) this, this.error);
        }

        /* access modifiers changed from: 0000 */
        public void innerNext() {
            subscribeNext();
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) this.d.get());
        }

        public void onComplete() {
            this.active = false;
            this.signaller.onNext(Integer.valueOf(0));
        }

        public void onError(Throwable th) {
            DisposableHelper.dispose(this.inner);
            HalfSerializer.onError(this.actual, th, (AtomicInteger) this, this.error);
        }

        public void onNext(Object obj) {
            HalfSerializer.onNext(this.actual, obj, (AtomicInteger) this, this.error);
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.replace(this.d, disposable);
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (this.wip.getAndIncrement() == 0) {
                while (!isDisposed()) {
                    if (!this.active) {
                        this.active = true;
                        this.source.subscribe(this);
                    }
                    if (this.wip.decrementAndGet() == 0) {
                    }
                }
            }
        }
    }

    public ObservableRepeatWhen(ObservableSource observableSource, Function function) {
        super(observableSource);
        this.handler = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        Subject serialized = PublishSubject.create().toSerialized();
        try {
            Object apply = this.handler.apply(serialized);
            ObjectHelper.requireNonNull(apply, "The handler returned a null ObservableSource");
            ObservableSource observableSource = (ObservableSource) apply;
            RepeatWhenObserver repeatWhenObserver = new RepeatWhenObserver(observer, serialized, this.source);
            observer.onSubscribe(repeatWhenObserver);
            observableSource.subscribe(repeatWhenObserver.inner);
            repeatWhenObserver.subscribeNext();
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, observer);
        }
    }
}
