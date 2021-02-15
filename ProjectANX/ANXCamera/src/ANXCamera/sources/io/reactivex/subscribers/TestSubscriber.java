package io.reactivex.subscribers;

import com.android.camera.CameraIntentManager.ControlActions;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.observers.BaseTestConsumer;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class TestSubscriber extends BaseTestConsumer implements FlowableSubscriber, Subscription, Disposable {
    private final Subscriber actual;
    private volatile boolean cancelled;
    private final AtomicLong missedRequested;
    private QueueSubscription qs;
    private final AtomicReference subscription;

    enum EmptySubscriber implements FlowableSubscriber {
        INSTANCE;

        public void onComplete() {
        }

        public void onError(Throwable th) {
        }

        public void onNext(Object obj) {
        }

        public void onSubscribe(Subscription subscription) {
        }
    }

    public TestSubscriber() {
        this(EmptySubscriber.INSTANCE, Long.MAX_VALUE);
    }

    public TestSubscriber(long j) {
        this(EmptySubscriber.INSTANCE, j);
    }

    public TestSubscriber(Subscriber subscriber) {
        this(subscriber, Long.MAX_VALUE);
    }

    public TestSubscriber(Subscriber subscriber, long j) {
        if (j >= 0) {
            this.actual = subscriber;
            this.subscription = new AtomicReference();
            this.missedRequested = new AtomicLong(j);
            return;
        }
        throw new IllegalArgumentException("Negative initial request not allowed");
    }

    public static TestSubscriber create() {
        return new TestSubscriber();
    }

    public static TestSubscriber create(long j) {
        return new TestSubscriber(j);
    }

    public static TestSubscriber create(Subscriber subscriber) {
        return new TestSubscriber(subscriber);
    }

    static String fusionModeToString(int i) {
        if (i == 0) {
            return ControlActions.CONTROL_ACTION_UNKNOWN;
        }
        if (i == 1) {
            return "SYNC";
        }
        if (i == 2) {
            return "ASYNC";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown(");
        sb.append(i);
        sb.append(")");
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    public final TestSubscriber assertFuseable() {
        if (this.qs != null) {
            return this;
        }
        throw new AssertionError("Upstream is not fuseable.");
    }

    /* access modifiers changed from: 0000 */
    public final TestSubscriber assertFusionMode(int i) {
        int i2 = this.establishedFusionMode;
        if (i2 == i) {
            return this;
        }
        if (this.qs != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fusion mode different. Expected: ");
            sb.append(fusionModeToString(i));
            sb.append(", actual: ");
            sb.append(fusionModeToString(i2));
            throw new AssertionError(sb.toString());
        }
        throw fail("Upstream is not fuseable");
    }

    /* access modifiers changed from: 0000 */
    public final TestSubscriber assertNotFuseable() {
        if (this.qs == null) {
            return this;
        }
        throw new AssertionError("Upstream is fuseable.");
    }

    public final TestSubscriber assertNotSubscribed() {
        if (this.subscription.get() != null) {
            throw fail("Subscribed!");
        } else if (this.errors.isEmpty()) {
            return this;
        } else {
            throw fail("Not subscribed but errors found");
        }
    }

    public final TestSubscriber assertOf(Consumer consumer) {
        try {
            consumer.accept(this);
            return this;
        } catch (Throwable th) {
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    public final TestSubscriber assertSubscribed() {
        if (this.subscription.get() != null) {
            return this;
        }
        throw fail("Not subscribed!");
    }

    public final void cancel() {
        if (!this.cancelled) {
            this.cancelled = true;
            SubscriptionHelper.cancel(this.subscription);
        }
    }

    public final void dispose() {
        cancel();
    }

    public final boolean hasSubscription() {
        return this.subscription.get() != null;
    }

    public final boolean isCancelled() {
        return this.cancelled;
    }

    public final boolean isDisposed() {
        return this.cancelled;
    }

    public void onComplete() {
        if (!this.checkSubscriptionOnce) {
            this.checkSubscriptionOnce = true;
            if (this.subscription.get() == null) {
                this.errors.add(new IllegalStateException("onSubscribe not called in proper order"));
            }
        }
        try {
            this.lastThread = Thread.currentThread();
            this.completions++;
            this.actual.onComplete();
        } finally {
            this.done.countDown();
        }
    }

    public void onError(Throwable th) {
        if (!this.checkSubscriptionOnce) {
            this.checkSubscriptionOnce = true;
            if (this.subscription.get() == null) {
                this.errors.add(new NullPointerException("onSubscribe not called in proper order"));
            }
        }
        try {
            this.lastThread = Thread.currentThread();
            this.errors.add(th);
            if (th == null) {
                this.errors.add(new IllegalStateException("onError received a null Throwable"));
            }
            this.actual.onError(th);
        } finally {
            this.done.countDown();
        }
    }

    public void onNext(Object obj) {
        if (!this.checkSubscriptionOnce) {
            this.checkSubscriptionOnce = true;
            if (this.subscription.get() == null) {
                this.errors.add(new IllegalStateException("onSubscribe not called in proper order"));
            }
        }
        this.lastThread = Thread.currentThread();
        if (this.establishedFusionMode == 2) {
            while (true) {
                try {
                    Object poll = this.qs.poll();
                    if (poll == null) {
                        break;
                    }
                    this.values.add(poll);
                } catch (Throwable th) {
                    this.errors.add(th);
                    this.qs.cancel();
                }
            }
            return;
        }
        this.values.add(obj);
        if (obj == null) {
            this.errors.add(new NullPointerException("onNext received a null value"));
        }
        this.actual.onNext(obj);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    public void onSubscribe(Subscription subscription2) {
        this.lastThread = Thread.currentThread();
        if (subscription2 == null) {
            this.errors.add(new NullPointerException("onSubscribe received a null Subscription"));
        } else if (!this.subscription.compareAndSet(null, subscription2)) {
            subscription2.cancel();
            if (this.subscription.get() != SubscriptionHelper.CANCELLED) {
                List list = this.errors;
                StringBuilder sb = new StringBuilder();
                sb.append("onSubscribe received multiple subscriptions: ");
                sb.append(subscription2);
                list.add(new IllegalStateException(sb.toString()));
            }
        } else {
            int i = this.initialFusionMode;
            if (i != 0 && (subscription2 instanceof QueueSubscription)) {
                this.qs = (QueueSubscription) subscription2;
                int requestFusion = this.qs.requestFusion(i);
                this.establishedFusionMode = requestFusion;
                if (requestFusion == 1) {
                    this.checkSubscriptionOnce = true;
                    this.lastThread = Thread.currentThread();
                    while (true) {
                        try {
                            Object poll = this.qs.poll();
                            if (poll == null) {
                                break;
                            }
                            this.values.add(poll);
                        } catch (Throwable th) {
                            this.errors.add(th);
                        }
                    }
                    this.completions++;
                    return;
                }
            }
            this.actual.onSubscribe(subscription2);
            long andSet = this.missedRequested.getAndSet(0);
            if (andSet != 0) {
                subscription2.request(andSet);
            }
            onStart();
        }
    }

    public final void request(long j) {
        SubscriptionHelper.deferredRequest(this.subscription, this.missedRequested, j);
    }

    public final TestSubscriber requestMore(long j) {
        request(j);
        return this;
    }

    /* access modifiers changed from: 0000 */
    public final TestSubscriber setInitialFusionMode(int i) {
        this.initialFusionMode = i;
        return this;
    }
}
