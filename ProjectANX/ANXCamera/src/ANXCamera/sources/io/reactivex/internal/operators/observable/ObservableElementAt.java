package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.NoSuchElementException;

public final class ObservableElementAt extends AbstractObservableWithUpstream {
    final Object defaultValue;
    final boolean errorOnFewer;
    final long index;

    final class ElementAtObserver implements Observer, Disposable {
        final Observer actual;
        long count;
        final Object defaultValue;
        boolean done;
        final boolean errorOnFewer;
        final long index;
        Disposable s;

        ElementAtObserver(Observer observer, long j, Object obj, boolean z) {
            this.actual = observer;
            this.index = j;
            this.defaultValue = obj;
            this.errorOnFewer = z;
        }

        public void dispose() {
            this.s.dispose();
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                Object obj = this.defaultValue;
                if (obj != null || !this.errorOnFewer) {
                    if (obj != null) {
                        this.actual.onNext(obj);
                    }
                    this.actual.onComplete();
                    return;
                }
                this.actual.onError(new NoSuchElementException());
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                long j = this.count;
                if (j == this.index) {
                    this.done = true;
                    this.s.dispose();
                    this.actual.onNext(obj);
                    this.actual.onComplete();
                    return;
                }
                this.count = j + 1;
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableElementAt(ObservableSource observableSource, long j, Object obj, boolean z) {
        super(observableSource);
        this.index = j;
        this.defaultValue = obj;
        this.errorOnFewer = z;
    }

    public void subscribeActual(Observer observer) {
        ObservableSource observableSource = this.source;
        ElementAtObserver elementAtObserver = new ElementAtObserver(observer, this.index, this.defaultValue, this.errorOnFewer);
        observableSource.subscribe(elementAtObserver);
    }
}
