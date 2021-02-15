package io.reactivex.subjects;

import io.reactivex.Observer;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class PublishSubject extends Subject {
    static final PublishDisposable[] EMPTY = new PublishDisposable[0];
    static final PublishDisposable[] TERMINATED = new PublishDisposable[0];
    Throwable error;
    final AtomicReference subscribers = new AtomicReference(EMPTY);

    final class PublishDisposable extends AtomicBoolean implements Disposable {
        private static final long serialVersionUID = 3562861878281475070L;
        final Observer actual;
        final PublishSubject parent;

        PublishDisposable(Observer observer, PublishSubject publishSubject) {
            this.actual = observer;
            this.parent = publishSubject;
        }

        public void dispose() {
            if (compareAndSet(false, true)) {
                this.parent.remove(this);
            }
        }

        public boolean isDisposed() {
            return get();
        }

        public void onComplete() {
            if (!get()) {
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (get()) {
                RxJavaPlugins.onError(th);
            } else {
                this.actual.onError(th);
            }
        }

        public void onNext(Object obj) {
            if (!get()) {
                this.actual.onNext(obj);
            }
        }
    }

    PublishSubject() {
    }

    @CheckReturnValue
    public static PublishSubject create() {
        return new PublishSubject();
    }

    /* access modifiers changed from: 0000 */
    public boolean add(PublishDisposable publishDisposable) {
        PublishDisposable[] publishDisposableArr;
        PublishDisposable[] publishDisposableArr2;
        do {
            publishDisposableArr = (PublishDisposable[]) this.subscribers.get();
            if (publishDisposableArr == TERMINATED) {
                return false;
            }
            int length = publishDisposableArr.length;
            publishDisposableArr2 = new PublishDisposable[(length + 1)];
            System.arraycopy(publishDisposableArr, 0, publishDisposableArr2, 0, length);
            publishDisposableArr2[length] = publishDisposable;
        } while (!this.subscribers.compareAndSet(publishDisposableArr, publishDisposableArr2));
        return true;
    }

    public Throwable getThrowable() {
        if (this.subscribers.get() == TERMINATED) {
            return this.error;
        }
        return null;
    }

    public boolean hasComplete() {
        return this.subscribers.get() == TERMINATED && this.error == null;
    }

    public boolean hasObservers() {
        return ((PublishDisposable[]) this.subscribers.get()).length != 0;
    }

    public boolean hasThrowable() {
        return this.subscribers.get() == TERMINATED && this.error != null;
    }

    public void onComplete() {
        Object obj = this.subscribers.get();
        Object obj2 = TERMINATED;
        if (obj != obj2) {
            for (PublishDisposable onComplete : (PublishDisposable[]) this.subscribers.getAndSet(obj2)) {
                onComplete.onComplete();
            }
        }
    }

    public void onError(Throwable th) {
        ObjectHelper.requireNonNull(th, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        Object obj = this.subscribers.get();
        Object obj2 = TERMINATED;
        if (obj == obj2) {
            RxJavaPlugins.onError(th);
            return;
        }
        this.error = th;
        for (PublishDisposable onError : (PublishDisposable[]) this.subscribers.getAndSet(obj2)) {
            onError.onError(th);
        }
    }

    public void onNext(Object obj) {
        ObjectHelper.requireNonNull(obj, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (this.subscribers.get() != TERMINATED) {
            for (PublishDisposable onNext : (PublishDisposable[]) this.subscribers.get()) {
                onNext.onNext(obj);
            }
        }
    }

    public void onSubscribe(Disposable disposable) {
        if (this.subscribers.get() == TERMINATED) {
            disposable.dispose();
        }
    }

    /* access modifiers changed from: 0000 */
    public void remove(PublishDisposable publishDisposable) {
        PublishDisposable[] publishDisposableArr;
        PublishDisposable[] publishDisposableArr2;
        do {
            publishDisposableArr = (PublishDisposable[]) this.subscribers.get();
            if (publishDisposableArr != TERMINATED && publishDisposableArr != EMPTY) {
                int length = publishDisposableArr.length;
                int i = -1;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    } else if (publishDisposableArr[i2] == publishDisposable) {
                        i = i2;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (i >= 0) {
                    if (length == 1) {
                        publishDisposableArr2 = EMPTY;
                    } else {
                        PublishDisposable[] publishDisposableArr3 = new PublishDisposable[(length - 1)];
                        System.arraycopy(publishDisposableArr, 0, publishDisposableArr3, 0, i);
                        System.arraycopy(publishDisposableArr, i + 1, publishDisposableArr3, i, (length - i) - 1);
                        publishDisposableArr2 = publishDisposableArr3;
                    }
                } else {
                    return;
                }
            }
        } while (!this.subscribers.compareAndSet(publishDisposableArr, publishDisposableArr2));
    }

    public void subscribeActual(Observer observer) {
        PublishDisposable publishDisposable = new PublishDisposable(observer, this);
        observer.onSubscribe(publishDisposable);
        if (!add(publishDisposable)) {
            Throwable th = this.error;
            if (th != null) {
                observer.onError(th);
            } else {
                observer.onComplete();
            }
        } else if (publishDisposable.isDisposed()) {
            remove(publishDisposable);
        }
    }
}
