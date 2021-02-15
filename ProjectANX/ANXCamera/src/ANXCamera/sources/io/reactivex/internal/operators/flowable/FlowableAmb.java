package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableAmb extends Flowable {
    final Publisher[] sources;
    final Iterable sourcesIterable;

    final class AmbCoordinator implements Subscription {
        final Subscriber actual;
        final AmbInnerSubscriber[] subscribers;
        final AtomicInteger winner = new AtomicInteger();

        AmbCoordinator(Subscriber subscriber, int i) {
            this.actual = subscriber;
            this.subscribers = new AmbInnerSubscriber[i];
        }

        public void cancel() {
            if (this.winner.get() != -1) {
                this.winner.lazySet(-1);
                for (AmbInnerSubscriber cancel : this.subscribers) {
                    cancel.cancel();
                }
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                int i = this.winner.get();
                if (i > 0) {
                    this.subscribers[i - 1].request(j);
                } else if (i == 0) {
                    for (AmbInnerSubscriber request : this.subscribers) {
                        request.request(j);
                    }
                }
            }
        }

        public void subscribe(Publisher[] publisherArr) {
            AmbInnerSubscriber[] ambInnerSubscriberArr = this.subscribers;
            int length = ambInnerSubscriberArr.length;
            int i = 0;
            while (i < length) {
                int i2 = i + 1;
                ambInnerSubscriberArr[i] = new AmbInnerSubscriber(this, i2, this.actual);
                i = i2;
            }
            this.winner.lazySet(0);
            this.actual.onSubscribe(this);
            for (int i3 = 0; i3 < length && this.winner.get() == 0; i3++) {
                publisherArr[i3].subscribe(ambInnerSubscriberArr[i3]);
            }
        }

        public boolean win(int i) {
            int i2 = 0;
            if (this.winner.get() != 0 || !this.winner.compareAndSet(0, i)) {
                return false;
            }
            AmbInnerSubscriber[] ambInnerSubscriberArr = this.subscribers;
            int length = ambInnerSubscriberArr.length;
            while (i2 < length) {
                int i3 = i2 + 1;
                if (i3 != i) {
                    ambInnerSubscriberArr[i2].cancel();
                }
                i2 = i3;
            }
            return true;
        }
    }

    final class AmbInnerSubscriber extends AtomicReference implements FlowableSubscriber, Subscription {
        private static final long serialVersionUID = -1185974347409665484L;
        final Subscriber actual;
        final int index;
        final AtomicLong missedRequested = new AtomicLong();
        final AmbCoordinator parent;
        boolean won;

        AmbInnerSubscriber(AmbCoordinator ambCoordinator, int i, Subscriber subscriber) {
            this.parent = ambCoordinator;
            this.index = i;
            this.actual = subscriber;
        }

        public void cancel() {
            SubscriptionHelper.cancel(this);
        }

        public void onComplete() {
            if (!this.won) {
                if (this.parent.win(this.index)) {
                    this.won = true;
                } else {
                    ((Subscription) get()).cancel();
                    return;
                }
            }
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            if (!this.won) {
                if (this.parent.win(this.index)) {
                    this.won = true;
                } else {
                    ((Subscription) get()).cancel();
                    RxJavaPlugins.onError(th);
                    return;
                }
            }
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.won) {
                if (this.parent.win(this.index)) {
                    this.won = true;
                } else {
                    ((Subscription) get()).cancel();
                    return;
                }
            }
            this.actual.onNext(obj);
        }

        public void onSubscribe(Subscription subscription) {
            SubscriptionHelper.deferredSetOnce(this, this.missedRequested, subscription);
        }

        public void request(long j) {
            SubscriptionHelper.deferredRequest(this, this.missedRequested, j);
        }
    }

    public FlowableAmb(Publisher[] publisherArr, Iterable iterable) {
        this.sources = publisherArr;
        this.sourcesIterable = iterable;
    }

    public void subscribeActual(Subscriber subscriber) {
        int i;
        Publisher[] publisherArr = this.sources;
        if (publisherArr == null) {
            publisherArr = new Publisher[8];
            try {
                i = 0;
                for (Publisher publisher : this.sourcesIterable) {
                    if (publisher == null) {
                        EmptySubscription.error(new NullPointerException("One of the sources is null"), subscriber);
                        return;
                    }
                    if (i == publisherArr.length) {
                        Publisher[] publisherArr2 = new Publisher[((i >> 2) + i)];
                        System.arraycopy(publisherArr, 0, publisherArr2, 0, i);
                        publisherArr = publisherArr2;
                    }
                    int i2 = i + 1;
                    publisherArr[i] = publisher;
                    i = i2;
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                EmptySubscription.error(th, subscriber);
                return;
            }
        } else {
            i = publisherArr.length;
        }
        if (i == 0) {
            EmptySubscription.complete(subscriber);
        } else if (i == 1) {
            publisherArr[0].subscribe(subscriber);
        } else {
            new AmbCoordinator(subscriber, i).subscribe(publisherArr);
        }
    }
}
