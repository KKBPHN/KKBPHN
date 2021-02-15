package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.AtomicThrowable;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public final class CompletableMergeDelayErrorIterable extends Completable {
    final Iterable sources;

    public CompletableMergeDelayErrorIterable(Iterable iterable) {
        this.sources = iterable;
    }

    public void subscribeActual(CompletableObserver completableObserver) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        completableObserver.onSubscribe(compositeDisposable);
        try {
            Iterator it = this.sources.iterator();
            ObjectHelper.requireNonNull(it, "The source iterator returned is null");
            Iterator it2 = it;
            AtomicInteger atomicInteger = new AtomicInteger(1);
            AtomicThrowable atomicThrowable = new AtomicThrowable();
            while (!compositeDisposable.isDisposed()) {
                try {
                    if (!it2.hasNext()) {
                        if (atomicInteger.decrementAndGet() == 0) {
                            Throwable terminate = atomicThrowable.terminate();
                            if (terminate == null) {
                                completableObserver.onComplete();
                            } else {
                                completableObserver.onError(terminate);
                            }
                        }
                        return;
                    } else if (!compositeDisposable.isDisposed()) {
                        Object next = it2.next();
                        ObjectHelper.requireNonNull(next, "The iterator returned a null CompletableSource");
                        CompletableSource completableSource = (CompletableSource) next;
                        if (!compositeDisposable.isDisposed()) {
                            atomicInteger.getAndIncrement();
                            completableSource.subscribe(new MergeInnerCompletableObserver(completableObserver, compositeDisposable, atomicThrowable, atomicInteger));
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    atomicThrowable.addThrowable(th);
                }
            }
        } catch (Throwable th2) {
            Exceptions.throwIfFatal(th2);
            completableObserver.onError(th2);
        }
    }
}
