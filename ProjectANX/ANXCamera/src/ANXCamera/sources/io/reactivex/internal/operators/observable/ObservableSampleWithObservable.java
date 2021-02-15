package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observers.SerializedObserver;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableSampleWithObservable extends AbstractObservableWithUpstream {
    final boolean emitLast;
    final ObservableSource other;

    final class SampleMainEmitLast extends SampleMainObserver {
        private static final long serialVersionUID = -3029755663834015785L;
        volatile boolean done;
        final AtomicInteger wip = new AtomicInteger();

        SampleMainEmitLast(Observer observer, ObservableSource observableSource) {
            super(observer, observableSource);
        }

        /* access modifiers changed from: 0000 */
        public void completeMain() {
            this.done = true;
            if (this.wip.getAndIncrement() == 0) {
                emit();
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void completeOther() {
            this.done = true;
            if (this.wip.getAndIncrement() == 0) {
                emit();
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void run() {
            if (this.wip.getAndIncrement() == 0) {
                do {
                    boolean z = this.done;
                    emit();
                    if (z) {
                        this.actual.onComplete();
                        return;
                    }
                } while (this.wip.decrementAndGet() != 0);
            }
        }
    }

    final class SampleMainNoLast extends SampleMainObserver {
        private static final long serialVersionUID = -3029755663834015785L;

        SampleMainNoLast(Observer observer, ObservableSource observableSource) {
            super(observer, observableSource);
        }

        /* access modifiers changed from: 0000 */
        public void completeMain() {
            this.actual.onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void completeOther() {
            this.actual.onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void run() {
            emit();
        }
    }

    abstract class SampleMainObserver extends AtomicReference implements Observer, Disposable {
        private static final long serialVersionUID = -3517602651313910099L;
        final Observer actual;
        final AtomicReference other = new AtomicReference();
        Disposable s;
        final ObservableSource sampler;

        SampleMainObserver(Observer observer, ObservableSource observableSource) {
            this.actual = observer;
            this.sampler = observableSource;
        }

        public void complete() {
            this.s.dispose();
            completeOther();
        }

        public abstract void completeMain();

        public abstract void completeOther();

        public void dispose() {
            DisposableHelper.dispose(this.other);
            this.s.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void emit() {
            Object andSet = getAndSet(null);
            if (andSet != null) {
                this.actual.onNext(andSet);
            }
        }

        public void error(Throwable th) {
            this.s.dispose();
            this.actual.onError(th);
        }

        public boolean isDisposed() {
            return this.other.get() == DisposableHelper.DISPOSED;
        }

        public void onComplete() {
            DisposableHelper.dispose(this.other);
            completeMain();
        }

        public void onError(Throwable th) {
            DisposableHelper.dispose(this.other);
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            lazySet(obj);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
                if (this.other.get() == null) {
                    this.sampler.subscribe(new SamplerObserver(this));
                }
            }
        }

        public abstract void run();

        /* access modifiers changed from: 0000 */
        public boolean setOther(Disposable disposable) {
            return DisposableHelper.setOnce(this.other, disposable);
        }
    }

    final class SamplerObserver implements Observer {
        final SampleMainObserver parent;

        SamplerObserver(SampleMainObserver sampleMainObserver) {
            this.parent = sampleMainObserver;
        }

        public void onComplete() {
            this.parent.complete();
        }

        public void onError(Throwable th) {
            this.parent.error(th);
        }

        public void onNext(Object obj) {
            this.parent.run();
        }

        public void onSubscribe(Disposable disposable) {
            this.parent.setOther(disposable);
        }
    }

    public ObservableSampleWithObservable(ObservableSource observableSource, ObservableSource observableSource2, boolean z) {
        super(observableSource);
        this.other = observableSource2;
        this.emitLast = z;
    }

    public void subscribeActual(Observer observer) {
        ObservableSource observableSource;
        Observer observer2;
        SerializedObserver serializedObserver = new SerializedObserver(observer);
        if (this.emitLast) {
            observableSource = this.source;
            observer2 = new SampleMainEmitLast(serializedObserver, this.other);
        } else {
            observableSource = this.source;
            observer2 = new SampleMainNoLast(serializedObserver, this.other);
        }
        observableSource.subscribe(observer2);
    }
}
