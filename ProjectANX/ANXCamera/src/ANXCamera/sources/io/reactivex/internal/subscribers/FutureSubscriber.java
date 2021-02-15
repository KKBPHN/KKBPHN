package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.NoSuchElementException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public final class FutureSubscriber extends CountDownLatch implements FlowableSubscriber, Future, Subscription {
    Throwable error;
    final AtomicReference s = new AtomicReference();
    Object value;

    public FutureSubscriber() {
        super(1);
    }

    public void cancel() {
    }

    public boolean cancel(boolean z) {
        Subscription subscription;
        SubscriptionHelper subscriptionHelper;
        do {
            subscription = (Subscription) this.s.get();
            if (subscription != this) {
                subscriptionHelper = SubscriptionHelper.CANCELLED;
                if (subscription == subscriptionHelper) {
                }
            }
            return false;
        } while (!this.s.compareAndSet(subscription, subscriptionHelper));
        if (subscription != null) {
            subscription.cancel();
        }
        countDown();
        return true;
    }

    public Object get() {
        if (getCount() != 0) {
            BlockingHelper.verifyNonBlocking();
            await();
        }
        if (!isCancelled()) {
            Throwable th = this.error;
            if (th == null) {
                return this.value;
            }
            throw new ExecutionException(th);
        }
        throw new CancellationException();
    }

    public Object get(long j, TimeUnit timeUnit) {
        if (getCount() != 0) {
            BlockingHelper.verifyNonBlocking();
            if (!await(j, timeUnit)) {
                throw new TimeoutException();
            }
        }
        if (!isCancelled()) {
            Throwable th = this.error;
            if (th == null) {
                return this.value;
            }
            throw new ExecutionException(th);
        }
        throw new CancellationException();
    }

    public boolean isCancelled() {
        return SubscriptionHelper.isCancelled((Subscription) this.s.get());
    }

    public boolean isDone() {
        return getCount() == 0;
    }

    public void onComplete() {
        if (this.value == null) {
            onError(new NoSuchElementException("The source is empty"));
            return;
        }
        while (true) {
            Subscription subscription = (Subscription) this.s.get();
            if (subscription != this && subscription != SubscriptionHelper.CANCELLED) {
                if (this.s.compareAndSet(subscription, this)) {
                    countDown();
                    break;
                }
            } else {
                break;
            }
        }
    }

    public void onError(Throwable th) {
        Subscription subscription;
        do {
            subscription = (Subscription) this.s.get();
            if (subscription == this || subscription == SubscriptionHelper.CANCELLED) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.error = th;
        } while (!this.s.compareAndSet(subscription, this));
        countDown();
    }

    public void onNext(Object obj) {
        if (this.value != null) {
            ((Subscription) this.s.get()).cancel();
            onError(new IndexOutOfBoundsException("More than one element received"));
            return;
        }
        this.value = obj;
    }

    public void onSubscribe(Subscription subscription) {
        if (SubscriptionHelper.setOnce(this.s, subscription)) {
            subscription.request(Long.MAX_VALUE);
        }
    }

    public void request(long j) {
    }
}
