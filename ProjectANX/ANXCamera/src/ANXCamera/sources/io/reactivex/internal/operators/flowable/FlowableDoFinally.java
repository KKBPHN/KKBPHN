package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Experimental
public final class FlowableDoFinally extends AbstractFlowableWithUpstream {
    final Action onFinally;

    final class DoFinallyConditionalSubscriber extends BasicIntQueueSubscription implements ConditionalSubscriber {
        private static final long serialVersionUID = 4109457741734051389L;
        final ConditionalSubscriber actual;
        final Action onFinally;
        QueueSubscription qs;
        Subscription s;
        boolean syncFused;

        DoFinallyConditionalSubscriber(ConditionalSubscriber conditionalSubscriber, Action action) {
            this.actual = conditionalSubscriber;
            this.onFinally = action;
        }

        public void cancel() {
            this.s.cancel();
            runFinally();
        }

        public void clear() {
            this.qs.clear();
        }

        public boolean isEmpty() {
            return this.qs.isEmpty();
        }

        public void onComplete() {
            this.actual.onComplete();
            runFinally();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
            runFinally();
        }

        public void onNext(Object obj) {
            this.actual.onNext(obj);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                if (subscription instanceof QueueSubscription) {
                    this.qs = (QueueSubscription) subscription;
                }
                this.actual.onSubscribe(this);
            }
        }

        @Nullable
        public Object poll() {
            Object poll = this.qs.poll();
            if (poll == null && this.syncFused) {
                runFinally();
            }
            return poll;
        }

        public void request(long j) {
            this.s.request(j);
        }

        public int requestFusion(int i) {
            QueueSubscription queueSubscription = this.qs;
            if (queueSubscription == null || (i & 4) != 0) {
                return 0;
            }
            int requestFusion = queueSubscription.requestFusion(i);
            if (requestFusion != 0) {
                boolean z = true;
                if (requestFusion != 1) {
                    z = false;
                }
                this.syncFused = z;
            }
            return requestFusion;
        }

        /* access modifiers changed from: 0000 */
        public void runFinally() {
            if (compareAndSet(0, 1)) {
                try {
                    this.onFinally.run();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    RxJavaPlugins.onError(th);
                }
            }
        }

        public boolean tryOnNext(Object obj) {
            return this.actual.tryOnNext(obj);
        }
    }

    final class DoFinallySubscriber extends BasicIntQueueSubscription implements FlowableSubscriber {
        private static final long serialVersionUID = 4109457741734051389L;
        final Subscriber actual;
        final Action onFinally;
        QueueSubscription qs;
        Subscription s;
        boolean syncFused;

        DoFinallySubscriber(Subscriber subscriber, Action action) {
            this.actual = subscriber;
            this.onFinally = action;
        }

        public void cancel() {
            this.s.cancel();
            runFinally();
        }

        public void clear() {
            this.qs.clear();
        }

        public boolean isEmpty() {
            return this.qs.isEmpty();
        }

        public void onComplete() {
            this.actual.onComplete();
            runFinally();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
            runFinally();
        }

        public void onNext(Object obj) {
            this.actual.onNext(obj);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                if (subscription instanceof QueueSubscription) {
                    this.qs = (QueueSubscription) subscription;
                }
                this.actual.onSubscribe(this);
            }
        }

        @Nullable
        public Object poll() {
            Object poll = this.qs.poll();
            if (poll == null && this.syncFused) {
                runFinally();
            }
            return poll;
        }

        public void request(long j) {
            this.s.request(j);
        }

        public int requestFusion(int i) {
            QueueSubscription queueSubscription = this.qs;
            if (queueSubscription == null || (i & 4) != 0) {
                return 0;
            }
            int requestFusion = queueSubscription.requestFusion(i);
            if (requestFusion != 0) {
                boolean z = true;
                if (requestFusion != 1) {
                    z = false;
                }
                this.syncFused = z;
            }
            return requestFusion;
        }

        /* access modifiers changed from: 0000 */
        public void runFinally() {
            if (compareAndSet(0, 1)) {
                try {
                    this.onFinally.run();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    RxJavaPlugins.onError(th);
                }
            }
        }
    }

    public FlowableDoFinally(Flowable flowable, Action action) {
        super(flowable);
        this.onFinally = action;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        FlowableSubscriber flowableSubscriber;
        Flowable flowable;
        if (subscriber instanceof ConditionalSubscriber) {
            flowable = this.source;
            flowableSubscriber = new DoFinallyConditionalSubscriber((ConditionalSubscriber) subscriber, this.onFinally);
        } else {
            flowable = this.source;
            flowableSubscriber = new DoFinallySubscriber(subscriber, this.onFinally);
        }
        flowable.subscribe(flowableSubscriber);
    }
}
