package io.reactivex.internal.operators.parallel;

import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelFilter extends ParallelFlowable {
    final Predicate predicate;
    final ParallelFlowable source;

    abstract class BaseFilterSubscriber implements ConditionalSubscriber, Subscription {
        boolean done;
        final Predicate predicate;
        Subscription s;

        BaseFilterSubscriber(Predicate predicate2) {
            this.predicate = predicate2;
        }

        public final void cancel() {
            this.s.cancel();
        }

        public final void onNext(Object obj) {
            if (!tryOnNext(obj) && !this.done) {
                this.s.request(1);
            }
        }

        public final void request(long j) {
            this.s.request(j);
        }
    }

    final class ParallelFilterConditionalSubscriber extends BaseFilterSubscriber {
        final ConditionalSubscriber actual;

        ParallelFilterConditionalSubscriber(ConditionalSubscriber conditionalSubscriber, Predicate predicate) {
            super(predicate);
            this.actual = conditionalSubscriber;
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

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public boolean tryOnNext(Object obj) {
            if (!this.done) {
                try {
                    if (this.predicate.test(obj)) {
                        return this.actual.tryOnNext(obj);
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    cancel();
                    onError(th);
                }
            }
            return false;
        }
    }

    final class ParallelFilterSubscriber extends BaseFilterSubscriber {
        final Subscriber actual;

        ParallelFilterSubscriber(Subscriber subscriber, Predicate predicate) {
            super(predicate);
            this.actual = subscriber;
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

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public boolean tryOnNext(Object obj) {
            if (!this.done) {
                try {
                    if (this.predicate.test(obj)) {
                        this.actual.onNext(obj);
                        return true;
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    cancel();
                    onError(th);
                }
            }
            return false;
        }
    }

    public ParallelFilter(ParallelFlowable parallelFlowable, Predicate predicate2) {
        this.source = parallelFlowable;
        this.predicate = predicate2;
    }

    public int parallelism() {
        return this.source.parallelism();
    }

    public void subscribe(Subscriber[] subscriberArr) {
        if (validate(subscriberArr)) {
            int length = subscriberArr.length;
            Subscriber[] subscriberArr2 = new Subscriber[length];
            for (int i = 0; i < length; i++) {
                ConditionalSubscriber conditionalSubscriber = subscriberArr[i];
                if (conditionalSubscriber instanceof ConditionalSubscriber) {
                    subscriberArr2[i] = new ParallelFilterConditionalSubscriber(conditionalSubscriber, this.predicate);
                } else {
                    subscriberArr2[i] = new ParallelFilterSubscriber(conditionalSubscriber, this.predicate);
                }
            }
            this.source.subscribe(subscriberArr2);
        }
    }
}
