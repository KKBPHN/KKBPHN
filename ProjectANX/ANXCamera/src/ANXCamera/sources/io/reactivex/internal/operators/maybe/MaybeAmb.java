package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;

public final class MaybeAmb extends Maybe {
    private final MaybeSource[] sources;
    private final Iterable sourcesIterable;

    final class AmbMaybeObserver extends AtomicBoolean implements MaybeObserver, Disposable {
        private static final long serialVersionUID = -7044685185359438206L;
        final MaybeObserver actual;
        final CompositeDisposable set = new CompositeDisposable();

        AmbMaybeObserver(MaybeObserver maybeObserver) {
            this.actual = maybeObserver;
        }

        public void dispose() {
            if (compareAndSet(false, true)) {
                this.set.dispose();
            }
        }

        public boolean isDisposed() {
            return get();
        }

        public void onComplete() {
            if (compareAndSet(false, true)) {
                this.set.dispose();
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (compareAndSet(false, true)) {
                this.set.dispose();
                this.actual.onError(th);
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            this.set.add(disposable);
        }

        public void onSuccess(Object obj) {
            if (compareAndSet(false, true)) {
                this.set.dispose();
                this.actual.onSuccess(obj);
            }
        }
    }

    public MaybeAmb(MaybeSource[] maybeSourceArr, Iterable iterable) {
        this.sources = maybeSourceArr;
        this.sourcesIterable = iterable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        int i;
        MaybeSource[] maybeSourceArr = this.sources;
        int i2 = 0;
        if (maybeSourceArr == null) {
            maybeSourceArr = new MaybeSource[8];
            try {
                i = 0;
                for (MaybeSource maybeSource : this.sourcesIterable) {
                    if (maybeSource == null) {
                        EmptyDisposable.error((Throwable) new NullPointerException("One of the sources is null"), maybeObserver);
                        return;
                    }
                    if (i == maybeSourceArr.length) {
                        MaybeSource[] maybeSourceArr2 = new MaybeSource[((i >> 2) + i)];
                        System.arraycopy(maybeSourceArr, 0, maybeSourceArr2, 0, i);
                        maybeSourceArr = maybeSourceArr2;
                    }
                    int i3 = i + 1;
                    maybeSourceArr[i] = maybeSource;
                    i = i3;
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                EmptyDisposable.error(th, maybeObserver);
                return;
            }
        } else {
            i = maybeSourceArr.length;
        }
        AmbMaybeObserver ambMaybeObserver = new AmbMaybeObserver(maybeObserver);
        maybeObserver.onSubscribe(ambMaybeObserver);
        while (i2 < i) {
            MaybeSource maybeSource2 = maybeSourceArr[i2];
            if (!ambMaybeObserver.isDisposed()) {
                if (maybeSource2 == null) {
                    ambMaybeObserver.onError(new NullPointerException("One of the MaybeSources is null"));
                    return;
                } else {
                    maybeSource2.subscribe(ambMaybeObserver);
                    i2++;
                }
            } else {
                return;
            }
        }
        if (i == 0) {
            maybeObserver.onComplete();
        }
    }
}
