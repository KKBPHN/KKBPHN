package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.internal.util.ExceptionHelper;
import java.util.concurrent.CountDownLatch;
import org.reactivestreams.Subscription;

public abstract class BlockingBaseSubscriber extends CountDownLatch implements FlowableSubscriber {
    volatile boolean cancelled;
    Throwable error;
    Subscription s;
    Object value;

    public BlockingBaseSubscriber() {
        super(1);
    }

    public final Object blockingGet() {
        if (getCount() != 0) {
            try {
                BlockingHelper.verifyNonBlocking();
                await();
            } catch (InterruptedException e) {
                Subscription subscription = this.s;
                this.s = SubscriptionHelper.CANCELLED;
                if (subscription != null) {
                    subscription.cancel();
                }
                throw ExceptionHelper.wrapOrThrow(e);
            }
        }
        Throwable th = this.error;
        if (th == null) {
            return this.value;
        }
        throw ExceptionHelper.wrapOrThrow(th);
    }

    public final void onComplete() {
        countDown();
    }

    public final void onSubscribe(Subscription subscription) {
        if (SubscriptionHelper.validate(this.s, subscription)) {
            this.s = subscription;
            if (!this.cancelled) {
                subscription.request(Long.MAX_VALUE);
                if (this.cancelled) {
                    this.s = SubscriptionHelper.CANCELLED;
                    subscription.cancel();
                }
            }
        }
    }
}
