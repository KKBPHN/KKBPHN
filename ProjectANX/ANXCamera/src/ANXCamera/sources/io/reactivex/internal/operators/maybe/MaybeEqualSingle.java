package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiPredicate;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeEqualSingle extends Single {
    final BiPredicate isEqual;
    final MaybeSource source1;
    final MaybeSource source2;

    final class EqualCoordinator extends AtomicInteger implements Disposable {
        final SingleObserver actual;
        final BiPredicate isEqual;
        final EqualObserver observer1 = new EqualObserver(this);
        final EqualObserver observer2 = new EqualObserver(this);

        EqualCoordinator(SingleObserver singleObserver, BiPredicate biPredicate) {
            super(2);
            this.actual = singleObserver;
            this.isEqual = biPredicate;
        }

        public void dispose() {
            this.observer1.dispose();
            this.observer2.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void done() {
            SingleObserver singleObserver;
            boolean z;
            if (decrementAndGet() == 0) {
                Object obj = this.observer1.value;
                Object obj2 = this.observer2.value;
                if (obj == null || obj2 == null) {
                    singleObserver = this.actual;
                    z = obj == null && obj2 == null;
                } else {
                    try {
                        z = this.isEqual.test(obj, obj2);
                        singleObserver = this.actual;
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.actual.onError(th);
                        return;
                    }
                }
                singleObserver.onSuccess(Boolean.valueOf(z));
            }
        }

        /* access modifiers changed from: 0000 */
        public void error(EqualObserver equalObserver, Throwable th) {
            if (getAndSet(0) > 0) {
                EqualObserver equalObserver2 = this.observer1;
                if (equalObserver == equalObserver2) {
                    this.observer2.dispose();
                } else {
                    equalObserver2.dispose();
                }
                this.actual.onError(th);
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) this.observer1.get());
        }

        /* access modifiers changed from: 0000 */
        public void subscribe(MaybeSource maybeSource, MaybeSource maybeSource2) {
            maybeSource.subscribe(this.observer1);
            maybeSource2.subscribe(this.observer2);
        }
    }

    final class EqualObserver extends AtomicReference implements MaybeObserver {
        private static final long serialVersionUID = -3031974433025990931L;
        final EqualCoordinator parent;
        Object value;

        EqualObserver(EqualCoordinator equalCoordinator) {
            this.parent = equalCoordinator;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public void onComplete() {
            this.parent.done();
        }

        public void onError(Throwable th) {
            this.parent.error(this, th);
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }

        public void onSuccess(Object obj) {
            this.value = obj;
            this.parent.done();
        }
    }

    public MaybeEqualSingle(MaybeSource maybeSource, MaybeSource maybeSource2, BiPredicate biPredicate) {
        this.source1 = maybeSource;
        this.source2 = maybeSource2;
        this.isEqual = biPredicate;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        EqualCoordinator equalCoordinator = new EqualCoordinator(singleObserver, this.isEqual);
        singleObserver.onSubscribe(equalCoordinator);
        equalCoordinator.subscribe(this.source1, this.source2);
    }
}
