package io.reactivex.internal.operators.parallel;

import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelMap extends ParallelFlowable {
    final Function mapper;
    final ParallelFlowable source;

    final class ParallelMapConditionalSubscriber implements ConditionalSubscriber, Subscription {
        final ConditionalSubscriber actual;
        boolean done;
        final Function mapper;
        Subscription s;

        ParallelMapConditionalSubscriber(ConditionalSubscriber conditionalSubscriber, Function function) {
            this.actual = conditionalSubscriber;
            this.mapper = function;
        }

        public void cancel() {
            this.s.cancel();
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
                    Object apply = this.mapper.apply(obj);
                    ObjectHelper.requireNonNull(apply, "The mapper returned a null value");
                    this.actual.onNext(apply);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    cancel();
                    onError(th);
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

        public boolean tryOnNext(Object obj) {
            if (this.done) {
                return false;
            }
            try {
                Object apply = this.mapper.apply(obj);
                ObjectHelper.requireNonNull(apply, "The mapper returned a null value");
                return this.actual.tryOnNext(apply);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                cancel();
                onError(th);
                return false;
            }
        }
    }

    final class ParallelMapSubscriber implements FlowableSubscriber, Subscription {
        final Subscriber actual;
        boolean done;
        final Function mapper;
        Subscription s;

        ParallelMapSubscriber(Subscriber subscriber, Function function) {
            this.actual = subscriber;
            this.mapper = function;
        }

        public void cancel() {
            this.s.cancel();
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
                    Object apply = this.mapper.apply(obj);
                    ObjectHelper.requireNonNull(apply, "The mapper returned a null value");
                    this.actual.onNext(apply);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    cancel();
                    onError(th);
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

    public ParallelMap(ParallelFlowable parallelFlowable, Function function) {
        this.source = parallelFlowable;
        this.mapper = function;
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
                    subscriberArr2[i] = new ParallelMapConditionalSubscriber(conditionalSubscriber, this.mapper);
                } else {
                    subscriberArr2[i] = new ParallelMapSubscriber(conditionalSubscriber, this.mapper);
                }
            }
            this.source.subscribe(subscriberArr2);
        }
    }
}
