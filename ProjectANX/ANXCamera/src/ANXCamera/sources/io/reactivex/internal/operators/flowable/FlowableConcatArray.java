package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableConcatArray extends Flowable {
    final boolean delayError;
    final Publisher[] sources;

    final class ConcatArraySubscriber extends SubscriptionArbiter implements FlowableSubscriber {
        private static final long serialVersionUID = -8158322871608889516L;
        final Subscriber actual;
        final boolean delayError;
        List errors;
        int index;
        long produced;
        final Publisher[] sources;
        final AtomicInteger wip = new AtomicInteger();

        ConcatArraySubscriber(Publisher[] publisherArr, boolean z, Subscriber subscriber) {
            this.actual = subscriber;
            this.sources = publisherArr;
            this.delayError = z;
        }

        public void onComplete() {
            if (this.wip.getAndIncrement() == 0) {
                Publisher[] publisherArr = this.sources;
                int length = publisherArr.length;
                int i = this.index;
                while (i != length) {
                    Publisher publisher = publisherArr[i];
                    if (publisher == null) {
                        NullPointerException nullPointerException = new NullPointerException("A Publisher entry is null");
                        if (this.delayError) {
                            List list = this.errors;
                            if (list == null) {
                                list = new ArrayList((length - i) + 1);
                                this.errors = list;
                            }
                            list.add(nullPointerException);
                            i++;
                        } else {
                            this.actual.onError(nullPointerException);
                            return;
                        }
                    } else {
                        long j = this.produced;
                        if (j != 0) {
                            this.produced = 0;
                            produced(j);
                        }
                        publisher.subscribe(this);
                        i++;
                        this.index = i;
                        if (this.wip.decrementAndGet() == 0) {
                        }
                    }
                }
                List list2 = this.errors;
                if (list2 != null) {
                    int size = list2.size();
                    Subscriber subscriber = this.actual;
                    if (size == 1) {
                        subscriber.onError((Throwable) list2.get(0));
                    } else {
                        subscriber.onError(new CompositeException((Iterable) list2));
                    }
                } else {
                    this.actual.onComplete();
                }
            }
        }

        public void onError(Throwable th) {
            if (this.delayError) {
                List list = this.errors;
                if (list == null) {
                    list = new ArrayList((this.sources.length - this.index) + 1);
                    this.errors = list;
                }
                list.add(th);
                onComplete();
                return;
            }
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            this.produced++;
            this.actual.onNext(obj);
        }

        public void onSubscribe(Subscription subscription) {
            setSubscription(subscription);
        }
    }

    public FlowableConcatArray(Publisher[] publisherArr, boolean z) {
        this.sources = publisherArr;
        this.delayError = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        ConcatArraySubscriber concatArraySubscriber = new ConcatArraySubscriber(this.sources, this.delayError, subscriber);
        subscriber.onSubscribe(concatArraySubscriber);
        concatArraySubscriber.onComplete();
    }
}
