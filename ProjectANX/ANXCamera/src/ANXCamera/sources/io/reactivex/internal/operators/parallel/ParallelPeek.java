package io.reactivex.internal.operators.parallel;

import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.LongConsumer;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelPeek extends ParallelFlowable {
    final Consumer onAfterNext;
    final Action onAfterTerminated;
    final Action onCancel;
    final Action onComplete;
    final Consumer onError;
    final Consumer onNext;
    final LongConsumer onRequest;
    final Consumer onSubscribe;
    final ParallelFlowable source;

    final class ParallelPeekSubscriber implements FlowableSubscriber, Subscription {
        final Subscriber actual;
        boolean done;
        final ParallelPeek parent;
        Subscription s;

        ParallelPeekSubscriber(Subscriber subscriber, ParallelPeek parallelPeek) {
            this.actual = subscriber;
            this.parent = parallelPeek;
        }

        public void cancel() {
            try {
                this.parent.onCancel.run();
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
            this.s.cancel();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                try {
                    this.parent.onComplete.run();
                    this.actual.onComplete();
                    try {
                        this.parent.onAfterTerminated.run();
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        RxJavaPlugins.onError(th);
                    }
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    this.actual.onError(th2);
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            try {
                this.parent.onError.accept(th);
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                th = new CompositeException(th, th2);
            }
            this.actual.onError(th);
            try {
                this.parent.onAfterTerminated.run();
            } catch (Throwable th3) {
                Exceptions.throwIfFatal(th3);
                RxJavaPlugins.onError(th3);
            }
        }

        public void onNext(Object obj) {
            if (!this.done) {
                try {
                    this.parent.onNext.accept(obj);
                    this.actual.onNext(obj);
                    try {
                        this.parent.onAfterNext.accept(obj);
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        onError(th);
                    }
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    onError(th2);
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                try {
                    this.parent.onSubscribe.accept(subscription);
                    this.actual.onSubscribe(this);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    subscription.cancel();
                    this.actual.onSubscribe(EmptySubscription.INSTANCE);
                    onError(th);
                }
            }
        }

        public void request(long j) {
            try {
                this.parent.onRequest.accept(j);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
            this.s.request(j);
        }
    }

    public ParallelPeek(ParallelFlowable parallelFlowable, Consumer consumer, Consumer consumer2, Consumer consumer3, Action action, Action action2, Consumer consumer4, LongConsumer longConsumer, Action action3) {
        this.source = parallelFlowable;
        ObjectHelper.requireNonNull(consumer, "onNext is null");
        this.onNext = consumer;
        ObjectHelper.requireNonNull(consumer2, "onAfterNext is null");
        this.onAfterNext = consumer2;
        ObjectHelper.requireNonNull(consumer3, "onError is null");
        this.onError = consumer3;
        ObjectHelper.requireNonNull(action, "onComplete is null");
        this.onComplete = action;
        ObjectHelper.requireNonNull(action2, "onAfterTerminated is null");
        this.onAfterTerminated = action2;
        ObjectHelper.requireNonNull(consumer4, "onSubscribe is null");
        this.onSubscribe = consumer4;
        ObjectHelper.requireNonNull(longConsumer, "onRequest is null");
        this.onRequest = longConsumer;
        ObjectHelper.requireNonNull(action3, "onCancel is null");
        this.onCancel = action3;
    }

    public int parallelism() {
        return this.source.parallelism();
    }

    public void subscribe(Subscriber[] subscriberArr) {
        if (validate(subscriberArr)) {
            int length = subscriberArr.length;
            Subscriber[] subscriberArr2 = new Subscriber[length];
            for (int i = 0; i < length; i++) {
                subscriberArr2[i] = new ParallelPeekSubscriber(subscriberArr[i], this);
            }
            this.source.subscribe(subscriberArr2);
        }
    }
}
