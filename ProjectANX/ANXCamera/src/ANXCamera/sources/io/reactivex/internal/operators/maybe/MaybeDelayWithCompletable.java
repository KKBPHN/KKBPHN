package io.reactivex.internal.operators.maybe;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeDelayWithCompletable extends Maybe {
    final CompletableSource other;
    final MaybeSource source;

    final class DelayWithMainObserver implements MaybeObserver {
        final MaybeObserver actual;
        final AtomicReference parent;

        DelayWithMainObserver(AtomicReference atomicReference, MaybeObserver maybeObserver) {
            this.parent = atomicReference;
            this.actual = maybeObserver;
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.replace(this.parent, disposable);
        }

        public void onSuccess(Object obj) {
            this.actual.onSuccess(obj);
        }
    }

    final class OtherObserver extends AtomicReference implements CompletableObserver, Disposable {
        private static final long serialVersionUID = 703409937383992161L;
        final MaybeObserver actual;
        final MaybeSource source;

        OtherObserver(MaybeObserver maybeObserver, MaybeSource maybeSource) {
            this.actual = maybeObserver;
            this.source = maybeSource;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onComplete() {
            this.source.subscribe(new DelayWithMainObserver(this, this.actual));
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.setOnce(this, disposable)) {
                this.actual.onSubscribe(this);
            }
        }
    }

    public MaybeDelayWithCompletable(MaybeSource maybeSource, CompletableSource completableSource) {
        this.source = maybeSource;
        this.other = completableSource;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.other.subscribe(new OtherObserver(maybeObserver, this.source));
    }
}
