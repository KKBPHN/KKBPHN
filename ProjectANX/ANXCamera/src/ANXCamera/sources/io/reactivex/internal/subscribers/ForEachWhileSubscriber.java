package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public final class ForEachWhileSubscriber extends AtomicReference implements FlowableSubscriber, Disposable {
    private static final long serialVersionUID = -4403180040475402120L;
    boolean done;
    final Action onComplete;
    final Consumer onError;
    final Predicate onNext;

    public ForEachWhileSubscriber(Predicate predicate, Consumer consumer, Action action) {
        this.onNext = predicate;
        this.onError = consumer;
        this.onComplete = action;
    }

    public void dispose() {
        SubscriptionHelper.cancel(this);
    }

    public boolean isDisposed() {
        return SubscriptionHelper.isCancelled((Subscription) get());
    }

    public void onComplete() {
        if (!this.done) {
            this.done = true;
            try {
                this.onComplete.run();
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
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
            this.onError.accept(th);
        } catch (Throwable th2) {
            Exceptions.throwIfFatal(th2);
            RxJavaPlugins.onError(new CompositeException(th, th2));
        }
    }

    public void onNext(Object obj) {
        if (!this.done) {
            try {
                if (!this.onNext.test(obj)) {
                    dispose();
                    onComplete();
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                dispose();
                onError(th);
            }
        }
    }

    public void onSubscribe(Subscription subscription) {
        if (SubscriptionHelper.setOnce(this, subscription)) {
            subscription.request(Long.MAX_VALUE);
        }
    }
}
