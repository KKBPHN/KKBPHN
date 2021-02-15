package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamObservableSource;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservablePublish extends ConnectableObservable implements HasUpstreamObservableSource {
    final AtomicReference current;
    final ObservableSource onSubscribe;
    final ObservableSource source;

    final class InnerDisposable extends AtomicReference implements Disposable {
        private static final long serialVersionUID = -1100270633763673112L;
        final Observer child;

        InnerDisposable(Observer observer) {
            this.child = observer;
        }

        public void dispose() {
            Object andSet = getAndSet(this);
            if (andSet != null && andSet != this) {
                ((PublishObserver) andSet).remove(this);
            }
        }

        public boolean isDisposed() {
            return get() == this;
        }

        /* access modifiers changed from: 0000 */
        public void setParent(PublishObserver publishObserver) {
            if (!compareAndSet(null, publishObserver)) {
                publishObserver.remove(this);
            }
        }
    }

    final class PublishObserver implements Observer, Disposable {
        static final InnerDisposable[] EMPTY = new InnerDisposable[0];
        static final InnerDisposable[] TERMINATED = new InnerDisposable[0];
        final AtomicReference current;
        final AtomicReference observers = new AtomicReference(EMPTY);
        final AtomicReference s = new AtomicReference();
        final AtomicBoolean shouldConnect;

        PublishObserver(AtomicReference atomicReference) {
            this.current = atomicReference;
            this.shouldConnect = new AtomicBoolean();
        }

        /* access modifiers changed from: 0000 */
        public boolean add(InnerDisposable innerDisposable) {
            InnerDisposable[] innerDisposableArr;
            InnerDisposable[] innerDisposableArr2;
            do {
                innerDisposableArr = (InnerDisposable[]) this.observers.get();
                if (innerDisposableArr == TERMINATED) {
                    return false;
                }
                int length = innerDisposableArr.length;
                innerDisposableArr2 = new InnerDisposable[(length + 1)];
                System.arraycopy(innerDisposableArr, 0, innerDisposableArr2, 0, length);
                innerDisposableArr2[length] = innerDisposable;
            } while (!this.observers.compareAndSet(innerDisposableArr, innerDisposableArr2));
            return true;
        }

        public void dispose() {
            Object obj = this.observers.get();
            Object obj2 = TERMINATED;
            if (obj != obj2 && ((InnerDisposable[]) this.observers.getAndSet(obj2)) != TERMINATED) {
                this.current.compareAndSet(this, null);
                DisposableHelper.dispose(this.s);
            }
        }

        public boolean isDisposed() {
            return this.observers.get() == TERMINATED;
        }

        public void onComplete() {
            this.current.compareAndSet(this, null);
            for (InnerDisposable innerDisposable : (InnerDisposable[]) this.observers.getAndSet(TERMINATED)) {
                innerDisposable.child.onComplete();
            }
        }

        public void onError(Throwable th) {
            this.current.compareAndSet(this, null);
            InnerDisposable[] innerDisposableArr = (InnerDisposable[]) this.observers.getAndSet(TERMINATED);
            if (innerDisposableArr.length != 0) {
                for (InnerDisposable innerDisposable : innerDisposableArr) {
                    innerDisposable.child.onError(th);
                }
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onNext(Object obj) {
            for (InnerDisposable innerDisposable : (InnerDisposable[]) this.observers.get()) {
                innerDisposable.child.onNext(obj);
            }
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this.s, disposable);
        }

        /* access modifiers changed from: 0000 */
        public void remove(InnerDisposable innerDisposable) {
            InnerDisposable[] innerDisposableArr;
            InnerDisposable[] innerDisposableArr2;
            do {
                innerDisposableArr = (InnerDisposable[]) this.observers.get();
                int length = innerDisposableArr.length;
                if (length != 0) {
                    int i = -1;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        } else if (innerDisposableArr[i2].equals(innerDisposable)) {
                            i = i2;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (i >= 0) {
                        if (length == 1) {
                            innerDisposableArr2 = EMPTY;
                        } else {
                            InnerDisposable[] innerDisposableArr3 = new InnerDisposable[(length - 1)];
                            System.arraycopy(innerDisposableArr, 0, innerDisposableArr3, 0, i);
                            System.arraycopy(innerDisposableArr, i + 1, innerDisposableArr3, i, (length - i) - 1);
                            innerDisposableArr2 = innerDisposableArr3;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!this.observers.compareAndSet(innerDisposableArr, innerDisposableArr2));
        }
    }

    final class PublishSource implements ObservableSource {
        private final AtomicReference curr;

        PublishSource(AtomicReference atomicReference) {
            this.curr = atomicReference;
        }

        public void subscribe(Observer observer) {
            InnerDisposable innerDisposable = new InnerDisposable(observer);
            observer.onSubscribe(innerDisposable);
            while (true) {
                PublishObserver publishObserver = (PublishObserver) this.curr.get();
                if (publishObserver == null || publishObserver.isDisposed()) {
                    PublishObserver publishObserver2 = new PublishObserver(this.curr);
                    if (this.curr.compareAndSet(publishObserver, publishObserver2)) {
                        publishObserver = publishObserver2;
                    }
                }
                if (publishObserver.add(innerDisposable)) {
                    innerDisposable.setParent(publishObserver);
                    return;
                }
            }
        }
    }

    private ObservablePublish(ObservableSource observableSource, ObservableSource observableSource2, AtomicReference atomicReference) {
        this.onSubscribe = observableSource;
        this.source = observableSource2;
        this.current = atomicReference;
    }

    public static ConnectableObservable create(ObservableSource observableSource) {
        AtomicReference atomicReference = new AtomicReference();
        return RxJavaPlugins.onAssembly((ConnectableObservable) new ObservablePublish(new PublishSource(atomicReference), observableSource, atomicReference));
    }

    public void connect(Consumer consumer) {
        PublishObserver publishObserver;
        while (true) {
            publishObserver = (PublishObserver) this.current.get();
            if (publishObserver != null && !publishObserver.isDisposed()) {
                break;
            }
            PublishObserver publishObserver2 = new PublishObserver(this.current);
            if (this.current.compareAndSet(publishObserver, publishObserver2)) {
                publishObserver = publishObserver2;
                break;
            }
        }
        boolean z = true;
        if (publishObserver.shouldConnect.get() || !publishObserver.shouldConnect.compareAndSet(false, true)) {
            z = false;
        }
        try {
            consumer.accept(publishObserver);
            if (z) {
                this.source.subscribe(publishObserver);
            }
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    public ObservableSource source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        this.onSubscribe.subscribe(observer);
    }
}
