package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableZipIterable extends AbstractFlowableWithUpstream {
    final Iterable other;
    final BiFunction zipper;

    final class ZipIterableSubscriber implements FlowableSubscriber, Subscription {
        final Subscriber actual;
        boolean done;
        final Iterator iterator;
        Subscription s;
        final BiFunction zipper;

        ZipIterableSubscriber(Subscriber subscriber, Iterator it, BiFunction biFunction) {
            this.actual = subscriber;
            this.iterator = it;
            this.zipper = biFunction;
        }

        public void cancel() {
            this.s.cancel();
        }

        /* access modifiers changed from: 0000 */
        public void error(Throwable th) {
            Exceptions.throwIfFatal(th);
            this.done = true;
            this.s.cancel();
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                try {
                    Object next = this.iterator.next();
                    ObjectHelper.requireNonNull(next, "The iterator returned a null value");
                    try {
                        Object apply = this.zipper.apply(obj, next);
                        ObjectHelper.requireNonNull(apply, "The zipper function returned a null value");
                        this.actual.onNext(apply);
                        try {
                            if (!this.iterator.hasNext()) {
                                this.done = true;
                                this.s.cancel();
                                this.actual.onComplete();
                            }
                        } catch (Throwable th) {
                            error(th);
                        }
                    } catch (Throwable th2) {
                        error(th2);
                    }
                } catch (Throwable th3) {
                    error(th3);
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void request(long j) {
            this.s.request(j);
        }
    }

    public FlowableZipIterable(Flowable flowable, Iterable iterable, BiFunction biFunction) {
        super(flowable);
        this.other = iterable;
        this.zipper = biFunction;
    }

    public void subscribeActual(Subscriber subscriber) {
        try {
            Iterator it = this.other.iterator();
            ObjectHelper.requireNonNull(it, "The iterator returned by other is null");
            Iterator it2 = it;
            try {
                if (!it2.hasNext()) {
                    EmptySubscription.complete(subscriber);
                } else {
                    this.source.subscribe((FlowableSubscriber) new ZipIterableSubscriber(subscriber, it2, this.zipper));
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                EmptySubscription.error(th, subscriber);
            }
        } catch (Throwable th2) {
            Exceptions.throwIfFatal(th2);
            EmptySubscription.error(th2, subscriber);
        }
    }
}
