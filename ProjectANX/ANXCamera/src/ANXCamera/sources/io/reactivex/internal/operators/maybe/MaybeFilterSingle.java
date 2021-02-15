package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.disposables.DisposableHelper;

public final class MaybeFilterSingle extends Maybe {
    final Predicate predicate;
    final SingleSource source;

    final class FilterMaybeObserver implements SingleObserver, Disposable {
        final MaybeObserver actual;
        Disposable d;
        final Predicate predicate;

        FilterMaybeObserver(MaybeObserver maybeObserver, Predicate predicate2) {
            this.actual = maybeObserver;
            this.predicate = predicate2;
        }

        public void dispose() {
            Disposable disposable = this.d;
            this.d = DisposableHelper.DISPOSED;
            disposable.dispose();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            try {
                boolean test = this.predicate.test(obj);
                MaybeObserver maybeObserver = this.actual;
                if (test) {
                    maybeObserver.onSuccess(obj);
                } else {
                    maybeObserver.onComplete();
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.actual.onError(th);
            }
        }
    }

    public MaybeFilterSingle(SingleSource singleSource, Predicate predicate2) {
        this.source = singleSource;
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new FilterMaybeObserver(maybeObserver, this.predicate));
    }
}
