package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.observers.LambdaConsumerIntrospection;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public final class LambdaSubscriber extends AtomicReference implements FlowableSubscriber, Subscription, Disposable, LambdaConsumerIntrospection {
    private static final long serialVersionUID = -7251123623727029452L;
    final Action onComplete;
    final Consumer onError;
    final Consumer onNext;
    final Consumer onSubscribe;

    public LambdaSubscriber(Consumer consumer, Consumer consumer2, Action action, Consumer consumer3) {
        this.onNext = consumer;
        this.onError = consumer2;
        this.onComplete = action;
        this.onSubscribe = consumer3;
    }

    public void cancel() {
        SubscriptionHelper.cancel(this);
    }

    public void dispose() {
        cancel();
    }

    public boolean hasCustomOnError() {
        return this.onError != Functions.ON_ERROR_MISSING;
    }

    public boolean isDisposed() {
        return get() == SubscriptionHelper.CANCELLED;
    }

    public void onComplete() {
        Object obj = get();
        SubscriptionHelper subscriptionHelper = SubscriptionHelper.CANCELLED;
        if (obj != subscriptionHelper) {
            lazySet(subscriptionHelper);
            try {
                this.onComplete.run();
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
        }
    }

    public void onError(Throwable th) {
        Object obj = get();
        SubscriptionHelper subscriptionHelper = SubscriptionHelper.CANCELLED;
        if (obj != subscriptionHelper) {
            lazySet(subscriptionHelper);
            try {
                this.onError.accept(th);
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                RxJavaPlugins.onError(new CompositeException(th, th2));
            }
        } else {
            RxJavaPlugins.onError(th);
        }
    }

    public void onNext(Object obj) {
        if (!isDisposed()) {
            try {
                this.onNext.accept(obj);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                ((Subscription) get()).cancel();
                onError(th);
            }
        }
    }

    public void onSubscribe(Subscription subscription) {
        if (SubscriptionHelper.setOnce(this, subscription)) {
            try {
                this.onSubscribe.accept(this);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                subscription.cancel();
                onError(th);
            }
        }
    }

    public void request(long j) {
        ((Subscription) get()).request(j);
    }
}
