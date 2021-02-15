package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.QueueDisposable;
import io.reactivex.internal.observers.BasicIntQueueDisposable;
import io.reactivex.plugins.RxJavaPlugins;

@Experimental
public final class ObservableDoFinally extends AbstractObservableWithUpstream {
    final Action onFinally;

    final class DoFinallyObserver extends BasicIntQueueDisposable implements Observer {
        private static final long serialVersionUID = 4109457741734051389L;
        final Observer actual;
        Disposable d;
        final Action onFinally;
        QueueDisposable qd;
        boolean syncFused;

        DoFinallyObserver(Observer observer, Action action) {
            this.actual = observer;
            this.onFinally = action;
        }

        public void clear() {
            this.qd.clear();
        }

        public void dispose() {
            this.d.dispose();
            runFinally();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public boolean isEmpty() {
            return this.qd.isEmpty();
        }

        public void onComplete() {
            this.actual.onComplete();
            runFinally();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
            runFinally();
        }

        public void onNext(Object obj) {
            this.actual.onNext(obj);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                if (disposable instanceof QueueDisposable) {
                    this.qd = (QueueDisposable) disposable;
                }
                this.actual.onSubscribe(this);
            }
        }

        @Nullable
        public Object poll() {
            Object poll = this.qd.poll();
            if (poll == null && this.syncFused) {
                runFinally();
            }
            return poll;
        }

        public int requestFusion(int i) {
            QueueDisposable queueDisposable = this.qd;
            if (queueDisposable == null || (i & 4) != 0) {
                return 0;
            }
            int requestFusion = queueDisposable.requestFusion(i);
            if (requestFusion != 0) {
                boolean z = true;
                if (requestFusion != 1) {
                    z = false;
                }
                this.syncFused = z;
            }
            return requestFusion;
        }

        /* access modifiers changed from: 0000 */
        public void runFinally() {
            if (compareAndSet(0, 1)) {
                try {
                    this.onFinally.run();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    RxJavaPlugins.onError(th);
                }
            }
        }
    }

    public ObservableDoFinally(ObservableSource observableSource, Action action) {
        super(observableSource);
        this.onFinally = action;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        this.source.subscribe(new DoFinallyObserver(observer, this.onFinally));
    }
}
