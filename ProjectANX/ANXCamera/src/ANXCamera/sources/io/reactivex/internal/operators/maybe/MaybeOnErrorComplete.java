package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.disposables.DisposableHelper;

public final class MaybeOnErrorComplete extends AbstractMaybeWithUpstream {
    final Predicate predicate;

    final class OnErrorCompleteMaybeObserver implements MaybeObserver, Disposable {
        final MaybeObserver actual;
        Disposable d;
        final Predicate predicate;

        OnErrorCompleteMaybeObserver(MaybeObserver maybeObserver, Predicate predicate2) {
            this.actual = maybeObserver;
            this.predicate = predicate2;
        }

        public void dispose() {
            this.d.dispose();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            try {
                boolean test = this.predicate.test(th);
                MaybeObserver maybeObserver = this.actual;
                if (test) {
                    maybeObserver.onComplete();
                } else {
                    maybeObserver.onError(th);
                }
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                this.actual.onError(new CompositeException(th, th2));
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            this.actual.onSuccess(obj);
        }
    }

    public MaybeOnErrorComplete(MaybeSource maybeSource, Predicate predicate2) {
        super(maybeSource);
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new OnErrorCompleteMaybeObserver(maybeObserver, this.predicate));
    }
}
