package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeZipArray extends Maybe {
    final MaybeSource[] sources;
    final Function zipper;

    final class SingletonArrayFunc implements Function {
        SingletonArrayFunc() {
        }

        public Object apply(Object obj) {
            Object apply = MaybeZipArray.this.zipper.apply(new Object[]{obj});
            ObjectHelper.requireNonNull(apply, "The zipper returned a null value");
            return apply;
        }
    }

    final class ZipCoordinator extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = -5556924161382950569L;
        final MaybeObserver actual;
        final ZipMaybeObserver[] observers;
        final Object[] values;
        final Function zipper;

        ZipCoordinator(MaybeObserver maybeObserver, int i, Function function) {
            super(i);
            this.actual = maybeObserver;
            this.zipper = function;
            ZipMaybeObserver[] zipMaybeObserverArr = new ZipMaybeObserver[i];
            for (int i2 = 0; i2 < i; i2++) {
                zipMaybeObserverArr[i2] = new ZipMaybeObserver(this, i2);
            }
            this.observers = zipMaybeObserverArr;
            this.values = new Object[i];
        }

        public void dispose() {
            if (getAndSet(0) > 0) {
                for (ZipMaybeObserver dispose : this.observers) {
                    dispose.dispose();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void disposeExcept(int i) {
            ZipMaybeObserver[] zipMaybeObserverArr = this.observers;
            int length = zipMaybeObserverArr.length;
            for (int i2 = 0; i2 < i; i2++) {
                zipMaybeObserverArr[i2].dispose();
            }
            while (true) {
                i++;
                if (i < length) {
                    zipMaybeObserverArr[i].dispose();
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(int i) {
            if (getAndSet(0) > 0) {
                disposeExcept(i);
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerError(Throwable th, int i) {
            if (getAndSet(0) > 0) {
                disposeExcept(i);
                this.actual.onError(th);
                return;
            }
            RxJavaPlugins.onError(th);
        }

        /* access modifiers changed from: 0000 */
        public void innerSuccess(Object obj, int i) {
            this.values[i] = obj;
            if (decrementAndGet() == 0) {
                try {
                    Object apply = this.zipper.apply(this.values);
                    ObjectHelper.requireNonNull(apply, "The zipper returned a null value");
                    this.actual.onSuccess(apply);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.actual.onError(th);
                }
            }
        }

        public boolean isDisposed() {
            return get() <= 0;
        }
    }

    final class ZipMaybeObserver extends AtomicReference implements MaybeObserver {
        private static final long serialVersionUID = 3323743579927613702L;
        final int index;
        final ZipCoordinator parent;

        ZipMaybeObserver(ZipCoordinator zipCoordinator, int i) {
            this.parent = zipCoordinator;
            this.index = i;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public void onComplete() {
            this.parent.innerComplete(this.index);
        }

        public void onError(Throwable th) {
            this.parent.innerError(th, this.index);
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }

        public void onSuccess(Object obj) {
            this.parent.innerSuccess(obj, this.index);
        }
    }

    public MaybeZipArray(MaybeSource[] maybeSourceArr, Function function) {
        this.sources = maybeSourceArr;
        this.zipper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        MaybeSource[] maybeSourceArr = this.sources;
        int length = maybeSourceArr.length;
        int i = 0;
        if (length == 1) {
            maybeSourceArr[0].subscribe(new MapMaybeObserver(maybeObserver, new SingletonArrayFunc()));
            return;
        }
        ZipCoordinator zipCoordinator = new ZipCoordinator(maybeObserver, length, this.zipper);
        maybeObserver.onSubscribe(zipCoordinator);
        while (i < length && !zipCoordinator.isDisposed()) {
            MaybeSource maybeSource = maybeSourceArr[i];
            if (maybeSource == null) {
                zipCoordinator.innerError(new NullPointerException("One of the sources is null"), i);
                return;
            } else {
                maybeSource.subscribe(zipCoordinator.observers[i]);
                i++;
            }
        }
    }
}
