package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.observers.DefaultObserver;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class BlockingObservableMostRecent implements Iterable {
    final Object initialValue;
    final ObservableSource source;

    final class MostRecentObserver extends DefaultObserver {
        volatile Object value;

        final class Iterator implements java.util.Iterator {
            private Object buf;

            Iterator() {
            }

            public boolean hasNext() {
                this.buf = MostRecentObserver.this.value;
                return !NotificationLite.isComplete(this.buf);
            }

            public Object next() {
                Object obj = null;
                try {
                    if (this.buf == null) {
                        obj = MostRecentObserver.this.value;
                    }
                    if (NotificationLite.isComplete(this.buf)) {
                        throw new NoSuchElementException();
                    } else if (!NotificationLite.isError(this.buf)) {
                        Object obj2 = this.buf;
                        NotificationLite.getValue(obj2);
                        this.buf = obj;
                        return obj2;
                    } else {
                        throw ExceptionHelper.wrapOrThrow(NotificationLite.getError(this.buf));
                    }
                } finally {
                    this.buf = obj;
                }
            }

            public void remove() {
                throw new UnsupportedOperationException("Read only iterator");
            }
        }

        MostRecentObserver(Object obj) {
            NotificationLite.next(obj);
            this.value = obj;
        }

        public Iterator getIterable() {
            return new Iterator();
        }

        public void onComplete() {
            this.value = NotificationLite.complete();
        }

        public void onError(Throwable th) {
            this.value = NotificationLite.error(th);
        }

        public void onNext(Object obj) {
            NotificationLite.next(obj);
            this.value = obj;
        }
    }

    public BlockingObservableMostRecent(ObservableSource observableSource, Object obj) {
        this.source = observableSource;
        this.initialValue = obj;
    }

    public Iterator iterator() {
        MostRecentObserver mostRecentObserver = new MostRecentObserver(this.initialValue);
        this.source.subscribe(mostRecentObserver);
        return mostRecentObserver.getIterable();
    }
}
