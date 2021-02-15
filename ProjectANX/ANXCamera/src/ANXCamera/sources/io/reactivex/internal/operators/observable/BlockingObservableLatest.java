package io.reactivex.internal.operators.observable;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

public final class BlockingObservableLatest implements Iterable {
    final ObservableSource source;

    final class BlockingObservableLatestIterator extends DisposableObserver implements Iterator {
        Notification iteratorNotification;
        final Semaphore notify = new Semaphore(0);
        final AtomicReference value = new AtomicReference();

        BlockingObservableLatestIterator() {
        }

        public boolean hasNext() {
            Notification notification = this.iteratorNotification;
            if (notification == null || !notification.isOnError()) {
                if (this.iteratorNotification == null) {
                    try {
                        BlockingHelper.verifyNonBlocking();
                        this.notify.acquire();
                        Notification notification2 = (Notification) this.value.getAndSet(null);
                        this.iteratorNotification = notification2;
                        if (notification2.isOnError()) {
                            throw ExceptionHelper.wrapOrThrow(notification2.getError());
                        }
                    } catch (InterruptedException e) {
                        dispose();
                        this.iteratorNotification = Notification.createOnError(e);
                        throw ExceptionHelper.wrapOrThrow(e);
                    }
                }
                return this.iteratorNotification.isOnNext();
            }
            throw ExceptionHelper.wrapOrThrow(this.iteratorNotification.getError());
        }

        public Object next() {
            if (hasNext()) {
                Object value2 = this.iteratorNotification.getValue();
                this.iteratorNotification = null;
                return value2;
            }
            throw new NoSuchElementException();
        }

        public void onComplete() {
        }

        public void onError(Throwable th) {
            RxJavaPlugins.onError(th);
        }

        public void onNext(Notification notification) {
            if (this.value.getAndSet(notification) == null) {
                this.notify.release();
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Read-only iterator.");
        }
    }

    public BlockingObservableLatest(ObservableSource observableSource) {
        this.source = observableSource;
    }

    public Iterator iterator() {
        BlockingObservableLatestIterator blockingObservableLatestIterator = new BlockingObservableLatestIterator();
        Observable.wrap(this.source).materialize().subscribe((Observer) blockingObservableLatestIterator);
        return blockingObservableLatestIterator;
    }
}
