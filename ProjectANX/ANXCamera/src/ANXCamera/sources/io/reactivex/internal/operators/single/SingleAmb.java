package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;

public final class SingleAmb extends Single {
    private final SingleSource[] sources;
    private final Iterable sourcesIterable;

    final class AmbSingleObserver extends AtomicBoolean implements SingleObserver {
        private static final long serialVersionUID = -1944085461036028108L;
        final SingleObserver s;
        final CompositeDisposable set;

        AmbSingleObserver(SingleObserver singleObserver, CompositeDisposable compositeDisposable) {
            this.s = singleObserver;
            this.set = compositeDisposable;
        }

        public void onError(Throwable th) {
            if (compareAndSet(false, true)) {
                this.set.dispose();
                this.s.onError(th);
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
                this.s.onSuccess(obj);
            }
        }
    }

    public SingleAmb(SingleSource[] singleSourceArr, Iterable iterable) {
        this.sources = singleSourceArr;
        this.sourcesIterable = iterable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        int i;
        SingleSource[] singleSourceArr = this.sources;
        String str = "One of the sources is null";
        if (singleSourceArr == null) {
            singleSourceArr = new SingleSource[8];
            try {
                i = 0;
                for (SingleSource singleSource : this.sourcesIterable) {
                    if (singleSource == null) {
                        EmptyDisposable.error((Throwable) new NullPointerException(str), singleObserver);
                        return;
                    }
                    if (i == singleSourceArr.length) {
                        SingleSource[] singleSourceArr2 = new SingleSource[((i >> 2) + i)];
                        System.arraycopy(singleSourceArr, 0, singleSourceArr2, 0, i);
                        singleSourceArr = singleSourceArr2;
                    }
                    int i2 = i + 1;
                    singleSourceArr[i] = singleSource;
                    i = i2;
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                EmptyDisposable.error(th, singleObserver);
                return;
            }
        } else {
            i = singleSourceArr.length;
        }
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        AmbSingleObserver ambSingleObserver = new AmbSingleObserver(singleObserver, compositeDisposable);
        singleObserver.onSubscribe(compositeDisposable);
        int i3 = 0;
        while (i3 < i) {
            SingleSource singleSource2 = singleSourceArr[i3];
            if (!ambSingleObserver.get()) {
                if (singleSource2 == null) {
                    compositeDisposable.dispose();
                    NullPointerException nullPointerException = new NullPointerException(str);
                    if (ambSingleObserver.compareAndSet(false, true)) {
                        singleObserver.onError(nullPointerException);
                    } else {
                        RxJavaPlugins.onError(nullPointerException);
                    }
                    return;
                }
                singleSource2.subscribe(ambSingleObserver);
                i3++;
            } else {
                return;
            }
        }
    }
}
