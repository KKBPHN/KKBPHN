package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;

public final class SingleEquals extends Single {
    final SingleSource first;
    final SingleSource second;

    class InnerObserver implements SingleObserver {
        final AtomicInteger count;
        final int index;
        final SingleObserver s;
        final CompositeDisposable set;
        final Object[] values;

        InnerObserver(int i, CompositeDisposable compositeDisposable, Object[] objArr, SingleObserver singleObserver, AtomicInteger atomicInteger) {
            this.index = i;
            this.set = compositeDisposable;
            this.values = objArr;
            this.s = singleObserver;
            this.count = atomicInteger;
        }

        public void onError(Throwable th) {
            int i;
            do {
                i = this.count.get();
                if (i >= 2) {
                    RxJavaPlugins.onError(th);
                    return;
                }
            } while (!this.count.compareAndSet(i, 2));
            this.set.dispose();
            this.s.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            this.set.add(disposable);
        }

        public void onSuccess(Object obj) {
            this.values[this.index] = obj;
            if (this.count.incrementAndGet() == 2) {
                SingleObserver singleObserver = this.s;
                Object[] objArr = this.values;
                singleObserver.onSuccess(Boolean.valueOf(ObjectHelper.equals(objArr[0], objArr[1])));
            }
        }
    }

    public SingleEquals(SingleSource singleSource, SingleSource singleSource2) {
        this.first = singleSource;
        this.second = singleSource2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        AtomicInteger atomicInteger = new AtomicInteger();
        Object[] objArr = {null, null};
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        singleObserver.onSubscribe(compositeDisposable);
        SingleSource singleSource = this.first;
        CompositeDisposable compositeDisposable2 = compositeDisposable;
        Object[] objArr2 = objArr;
        SingleObserver singleObserver2 = singleObserver;
        AtomicInteger atomicInteger2 = atomicInteger;
        InnerObserver innerObserver = new InnerObserver(0, compositeDisposable2, objArr2, singleObserver2, atomicInteger2);
        singleSource.subscribe(innerObserver);
        SingleSource singleSource2 = this.second;
        InnerObserver innerObserver2 = new InnerObserver(1, compositeDisposable2, objArr2, singleObserver2, atomicInteger2);
        singleSource2.subscribe(innerObserver2);
    }
}
