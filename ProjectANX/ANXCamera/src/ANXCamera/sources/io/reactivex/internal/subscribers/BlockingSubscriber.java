package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.NotificationLite;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public final class BlockingSubscriber extends AtomicReference implements FlowableSubscriber, Subscription {
    public static final Object TERMINATED = new Object();
    private static final long serialVersionUID = -4875965440900746268L;
    final Queue queue;

    public BlockingSubscriber(Queue queue2) {
        this.queue = queue2;
    }

    public void cancel() {
        if (SubscriptionHelper.cancel(this)) {
            this.queue.offer(TERMINATED);
        }
    }

    public boolean isCancelled() {
        return get() == SubscriptionHelper.CANCELLED;
    }

    public void onComplete() {
        this.queue.offer(NotificationLite.complete());
    }

    public void onError(Throwable th) {
        this.queue.offer(NotificationLite.error(th));
    }

    public void onNext(Object obj) {
        Queue queue2 = this.queue;
        NotificationLite.next(obj);
        queue2.offer(obj);
    }

    public void onSubscribe(Subscription subscription) {
        if (SubscriptionHelper.setOnce(this, subscription)) {
            this.queue.offer(NotificationLite.subscription(this));
        }
    }

    public void request(long j) {
        ((Subscription) get()).request(j);
    }
}
