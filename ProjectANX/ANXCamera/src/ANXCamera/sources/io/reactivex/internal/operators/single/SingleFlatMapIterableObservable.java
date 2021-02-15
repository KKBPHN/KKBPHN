package io.reactivex.internal.operators.single;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.BasicIntQueueDisposable;
import java.util.Iterator;

public final class SingleFlatMapIterableObservable extends Observable {
    final Function mapper;
    final SingleSource source;

    final class FlatMapIterableObserver extends BasicIntQueueDisposable implements SingleObserver {
        private static final long serialVersionUID = -8938804753851907758L;
        final Observer actual;
        volatile boolean cancelled;
        Disposable d;
        volatile Iterator it;
        final Function mapper;
        boolean outputFused;

        FlatMapIterableObserver(Observer observer, Function function) {
            this.actual = observer;
            this.mapper = function;
        }

        public void clear() {
            this.it = null;
        }

        public void dispose() {
            this.cancelled = true;
            this.d.dispose();
            this.d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public boolean isEmpty() {
            return this.it == null;
        }

        public void onError(Throwable th) {
            this.d = DisposableHelper.DISPOSED;
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            Observer observer = this.actual;
            try {
                Iterator it2 = ((Iterable) this.mapper.apply(obj)).iterator();
                if (!it2.hasNext()) {
                    observer.onComplete();
                } else if (this.outputFused) {
                    this.it = it2;
                    observer.onNext(null);
                    observer.onComplete();
                } else {
                    while (!this.cancelled) {
                        try {
                            observer.onNext(it2.next());
                            if (!this.cancelled) {
                                try {
                                    if (!it2.hasNext()) {
                                        observer.onComplete();
                                        return;
                                    }
                                } catch (Throwable th) {
                                    Exceptions.throwIfFatal(th);
                                    observer.onError(th);
                                    return;
                                }
                            } else {
                                return;
                            }
                        } catch (Throwable th2) {
                            Exceptions.throwIfFatal(th2);
                            observer.onError(th2);
                            return;
                        }
                    }
                }
            } catch (Throwable th3) {
                Exceptions.throwIfFatal(th3);
                this.actual.onError(th3);
            }
        }

        @Nullable
        public Object poll() {
            Iterator it2 = this.it;
            if (it2 == null) {
                return null;
            }
            Object next = it2.next();
            ObjectHelper.requireNonNull(next, "The iterator returned a null value");
            if (!it2.hasNext()) {
                this.it = null;
            }
            return next;
        }

        public int requestFusion(int i) {
            if ((i & 2) == 0) {
                return 0;
            }
            this.outputFused = true;
            return 2;
        }
    }

    public SingleFlatMapIterableObservable(SingleSource singleSource, Function function) {
        this.source = singleSource;
        this.mapper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        this.source.subscribe(new FlatMapIterableObserver(observer, this.mapper));
    }
}
