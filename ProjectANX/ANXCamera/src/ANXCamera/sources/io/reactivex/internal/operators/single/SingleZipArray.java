package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleZipArray extends Single {
    final SingleSource[] sources;
    final Function zipper;

    final class SingletonArrayFunc implements Function {
        SingletonArrayFunc() {
        }

        public Object apply(Object obj) {
            Object apply = SingleZipArray.this.zipper.apply(new Object[]{obj});
            ObjectHelper.requireNonNull(apply, "The zipper returned a null value");
            return apply;
        }
    }

    final class ZipCoordinator extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = -5556924161382950569L;
        final SingleObserver actual;
        final ZipSingleObserver[] observers;
        final Object[] values;
        final Function zipper;

        ZipCoordinator(SingleObserver singleObserver, int i, Function function) {
            super(i);
            this.actual = singleObserver;
            this.zipper = function;
            ZipSingleObserver[] zipSingleObserverArr = new ZipSingleObserver[i];
            for (int i2 = 0; i2 < i; i2++) {
                zipSingleObserverArr[i2] = new ZipSingleObserver(this, i2);
            }
            this.observers = zipSingleObserverArr;
            this.values = new Object[i];
        }

        public void dispose() {
            if (getAndSet(0) > 0) {
                for (ZipSingleObserver dispose : this.observers) {
                    dispose.dispose();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void disposeExcept(int i) {
            ZipSingleObserver[] zipSingleObserverArr = this.observers;
            int length = zipSingleObserverArr.length;
            for (int i2 = 0; i2 < i; i2++) {
                zipSingleObserverArr[i2].dispose();
            }
            while (true) {
                i++;
                if (i < length) {
                    zipSingleObserverArr[i].dispose();
                } else {
                    return;
                }
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

    final class ZipSingleObserver extends AtomicReference implements SingleObserver {
        private static final long serialVersionUID = 3323743579927613702L;
        final int index;
        final ZipCoordinator parent;

        ZipSingleObserver(ZipCoordinator zipCoordinator, int i) {
            this.parent = zipCoordinator;
            this.index = i;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
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

    public SingleZipArray(SingleSource[] singleSourceArr, Function function) {
        this.sources = singleSourceArr;
        this.zipper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        SingleSource[] singleSourceArr = this.sources;
        int length = singleSourceArr.length;
        int i = 0;
        if (length == 1) {
            singleSourceArr[0].subscribe(new MapSingleObserver(singleObserver, new SingletonArrayFunc()));
            return;
        }
        ZipCoordinator zipCoordinator = new ZipCoordinator(singleObserver, length, this.zipper);
        singleObserver.onSubscribe(zipCoordinator);
        while (i < length && !zipCoordinator.isDisposed()) {
            SingleSource singleSource = singleSourceArr[i];
            if (singleSource == null) {
                zipCoordinator.innerError(new NullPointerException("One of the sources is null"), i);
                return;
            } else {
                singleSource.subscribe(zipCoordinator.observers[i]);
                i++;
            }
        }
    }
}
