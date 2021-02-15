package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.subscribers.DefaultSubscriber;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class BlockingFlowableMostRecent implements Iterable {
    final Object initialValue;
    final Flowable source;

    final class MostRecentSubscriber extends DefaultSubscriber {
        volatile Object value;

        final class Iterator implements java.util.Iterator {
            private Object buf;

            Iterator() {
            }

            public boolean hasNext() {
                this.buf = MostRecentSubscriber.this.value;
                return !NotificationLite.isComplete(this.buf);
            }

            public Object next() {
                Object obj = null;
                try {
                    if (this.buf == null) {
                        obj = MostRecentSubscriber.this.value;
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

        MostRecentSubscriber(Object obj) {
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

    public BlockingFlowableMostRecent(Flowable flowable, Object obj) {
        this.source = flowable;
        this.initialValue = obj;
    }

    public Iterator iterator() {
        MostRecentSubscriber mostRecentSubscriber = new MostRecentSubscriber(this.initialValue);
        this.source.subscribe((FlowableSubscriber) mostRecentSubscriber);
        return mostRecentSubscriber.getIterable();
    }
}
