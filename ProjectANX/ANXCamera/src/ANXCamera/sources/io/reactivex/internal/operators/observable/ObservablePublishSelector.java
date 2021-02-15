package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.subjects.PublishSubject;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservablePublishSelector extends AbstractObservableWithUpstream {
    final Function selector;

    final class SourceObserver implements Observer {
        final PublishSubject subject;
        final AtomicReference target;

        SourceObserver(PublishSubject publishSubject, AtomicReference atomicReference) {
            this.subject = publishSubject;
            this.target = atomicReference;
        }

        public void onComplete() {
            this.subject.onComplete();
        }

        public void onError(Throwable th) {
            this.subject.onError(th);
        }

        public void onNext(Object obj) {
            this.subject.onNext(obj);
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this.target, disposable);
        }
    }

    final class TargetObserver extends AtomicReference implements Observer, Disposable {
        private static final long serialVersionUID = 854110278590336484L;
        final Observer actual;
        Disposable d;

        TargetObserver(Observer observer) {
            this.actual = observer;
        }

        public void dispose() {
            this.d.dispose();
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
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
            this.actual.onNext(obj);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservablePublishSelector(ObservableSource observableSource, Function function) {
        super(observableSource);
        this.selector = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        PublishSubject create = PublishSubject.create();
        try {
            Object apply = this.selector.apply(create);
            ObjectHelper.requireNonNull(apply, "The selector returned a null ObservableSource");
            ObservableSource observableSource = (ObservableSource) apply;
            TargetObserver targetObserver = new TargetObserver(observer);
            observableSource.subscribe(targetObserver);
            this.source.subscribe(new SourceObserver(create, targetObserver));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, observer);
        }
    }
}
