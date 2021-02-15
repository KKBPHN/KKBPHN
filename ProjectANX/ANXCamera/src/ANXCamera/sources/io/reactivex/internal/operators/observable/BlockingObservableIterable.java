package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.internal.util.ExceptionHelper;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class BlockingObservableIterable implements Iterable {
    final int bufferSize;
    final ObservableSource source;

    final class BlockingObservableIterator extends AtomicReference implements Observer, Iterator, Disposable {
        private static final long serialVersionUID = 6695226475494099826L;
        final Condition condition = this.lock.newCondition();
        volatile boolean done;
        Throwable error;
        final Lock lock = new ReentrantLock();
        final SpscLinkedArrayQueue queue;

        BlockingObservableIterator(int i) {
            this.queue = new SpscLinkedArrayQueue(i);
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean hasNext() {
            while (true) {
                boolean z = this.done;
                boolean isEmpty = this.queue.isEmpty();
                if (z) {
                    Throwable th = this.error;
                    if (th != null) {
                        throw ExceptionHelper.wrapOrThrow(th);
                    } else if (isEmpty) {
                        return false;
                    }
                }
                if (!isEmpty) {
                    return true;
                }
                BlockingHelper.verifyNonBlocking();
                this.lock.lock();
                while (!this.done && this.queue.isEmpty()) {
                    try {
                        this.condition.await();
                    } catch (InterruptedException e) {
                        DisposableHelper.dispose(this);
                        signalConsumer();
                        throw ExceptionHelper.wrapOrThrow(e);
                    } catch (Throwable th2) {
                        this.lock.unlock();
                        throw th2;
                    }
                }
                this.lock.unlock();
            }
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public Object next() {
            if (hasNext()) {
                return this.queue.poll();
            }
            throw new NoSuchElementException();
        }

        public void onComplete() {
            this.done = true;
            signalConsumer();
        }

        public void onError(Throwable th) {
            this.error = th;
            this.done = true;
            signalConsumer();
        }

        public void onNext(Object obj) {
            this.queue.offer(obj);
            signalConsumer();
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }

        public void remove() {
            throw new UnsupportedOperationException("remove");
        }

        /* access modifiers changed from: 0000 */
        public void signalConsumer() {
            this.lock.lock();
            try {
                this.condition.signalAll();
            } finally {
                this.lock.unlock();
            }
        }
    }

    public BlockingObservableIterable(ObservableSource observableSource, int i) {
        this.source = observableSource;
        this.bufferSize = i;
    }

    public Iterator iterator() {
        BlockingObservableIterator blockingObservableIterator = new BlockingObservableIterator(this.bufferSize);
        this.source.subscribe(blockingObservableIterator);
        return blockingObservableIterator;
    }
}
