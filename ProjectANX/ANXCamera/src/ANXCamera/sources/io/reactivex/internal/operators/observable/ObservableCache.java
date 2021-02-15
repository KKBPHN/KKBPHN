package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.LinkedArrayList;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableCache extends AbstractObservableWithUpstream {
    final AtomicBoolean once = new AtomicBoolean();
    final CacheState state;

    final class CacheState extends LinkedArrayList implements Observer {
        static final ReplayDisposable[] EMPTY = new ReplayDisposable[0];
        static final ReplayDisposable[] TERMINATED = new ReplayDisposable[0];
        final SequentialDisposable connection = new SequentialDisposable();
        volatile boolean isConnected;
        final AtomicReference observers = new AtomicReference(EMPTY);
        final Observable source;
        boolean sourceDone;

        CacheState(Observable observable, int i) {
            super(i);
            this.source = observable;
        }

        public boolean addChild(ReplayDisposable replayDisposable) {
            ReplayDisposable[] replayDisposableArr;
            ReplayDisposable[] replayDisposableArr2;
            do {
                replayDisposableArr = (ReplayDisposable[]) this.observers.get();
                if (replayDisposableArr == TERMINATED) {
                    return false;
                }
                int length = replayDisposableArr.length;
                replayDisposableArr2 = new ReplayDisposable[(length + 1)];
                System.arraycopy(replayDisposableArr, 0, replayDisposableArr2, 0, length);
                replayDisposableArr2[length] = replayDisposable;
            } while (!this.observers.compareAndSet(replayDisposableArr, replayDisposableArr2));
            return true;
        }

        public void connect() {
            this.source.subscribe((Observer) this);
            this.isConnected = true;
        }

        public void onComplete() {
            if (!this.sourceDone) {
                this.sourceDone = true;
                add(NotificationLite.complete());
                this.connection.dispose();
                for (ReplayDisposable replay : (ReplayDisposable[]) this.observers.getAndSet(TERMINATED)) {
                    replay.replay();
                }
            }
        }

        public void onError(Throwable th) {
            if (!this.sourceDone) {
                this.sourceDone = true;
                add(NotificationLite.error(th));
                this.connection.dispose();
                for (ReplayDisposable replay : (ReplayDisposable[]) this.observers.getAndSet(TERMINATED)) {
                    replay.replay();
                }
            }
        }

        public void onNext(Object obj) {
            if (!this.sourceDone) {
                NotificationLite.next(obj);
                add(obj);
                for (ReplayDisposable replay : (ReplayDisposable[]) this.observers.get()) {
                    replay.replay();
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            this.connection.update(disposable);
        }

        public void removeChild(ReplayDisposable replayDisposable) {
            ReplayDisposable[] replayDisposableArr;
            ReplayDisposable[] replayDisposableArr2;
            do {
                replayDisposableArr = (ReplayDisposable[]) this.observers.get();
                int length = replayDisposableArr.length;
                if (length != 0) {
                    int i = -1;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        } else if (replayDisposableArr[i2].equals(replayDisposable)) {
                            i = i2;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (i >= 0) {
                        if (length == 1) {
                            replayDisposableArr2 = EMPTY;
                        } else {
                            ReplayDisposable[] replayDisposableArr3 = new ReplayDisposable[(length - 1)];
                            System.arraycopy(replayDisposableArr, 0, replayDisposableArr3, 0, i);
                            System.arraycopy(replayDisposableArr, i + 1, replayDisposableArr3, i, (length - i) - 1);
                            replayDisposableArr2 = replayDisposableArr3;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!this.observers.compareAndSet(replayDisposableArr, replayDisposableArr2));
        }
    }

    final class ReplayDisposable extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = 7058506693698832024L;
        volatile boolean cancelled;
        final Observer child;
        Object[] currentBuffer;
        int currentIndexInBuffer;
        int index;
        final CacheState state;

        ReplayDisposable(Observer observer, CacheState cacheState) {
            this.child = observer;
            this.state = cacheState;
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.state.removeChild(this);
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void replay() {
            if (getAndIncrement() == 0) {
                Observer observer = this.child;
                int i = 1;
                while (!this.cancelled) {
                    int size = this.state.size();
                    if (size != 0) {
                        Object[] objArr = this.currentBuffer;
                        if (objArr == null) {
                            objArr = this.state.head();
                            this.currentBuffer = objArr;
                        }
                        int length = objArr.length - 1;
                        int i2 = this.index;
                        int i3 = this.currentIndexInBuffer;
                        while (i2 < size) {
                            if (!this.cancelled) {
                                if (i3 == length) {
                                    objArr = (Object[]) objArr[length];
                                    i3 = 0;
                                }
                                if (!NotificationLite.accept(objArr[i3], observer)) {
                                    i3++;
                                    i2++;
                                } else {
                                    return;
                                }
                            } else {
                                return;
                            }
                        }
                        if (!this.cancelled) {
                            this.index = i2;
                            this.currentIndexInBuffer = i3;
                            this.currentBuffer = objArr;
                        } else {
                            return;
                        }
                    }
                    i = addAndGet(-i);
                    if (i == 0) {
                        return;
                    }
                }
            }
        }
    }

    private ObservableCache(Observable observable, CacheState cacheState) {
        super(observable);
        this.state = cacheState;
    }

    public static Observable from(Observable observable) {
        return from(observable, 16);
    }

    public static Observable from(Observable observable, int i) {
        ObjectHelper.verifyPositive(i, "capacityHint");
        return RxJavaPlugins.onAssembly((Observable) new ObservableCache(observable, new CacheState(observable, i)));
    }

    /* access modifiers changed from: 0000 */
    public int cachedEventCount() {
        return this.state.size();
    }

    /* access modifiers changed from: 0000 */
    public boolean hasObservers() {
        return ((ReplayDisposable[]) this.state.observers.get()).length != 0;
    }

    /* access modifiers changed from: 0000 */
    public boolean isConnected() {
        return this.state.isConnected;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        ReplayDisposable replayDisposable = new ReplayDisposable(observer, this.state);
        observer.onSubscribe(replayDisposable);
        this.state.addChild(replayDisposable);
        if (!this.once.get() && this.once.compareAndSet(false, true)) {
            this.state.connect();
        }
        replayDisposable.replay();
    }
}
