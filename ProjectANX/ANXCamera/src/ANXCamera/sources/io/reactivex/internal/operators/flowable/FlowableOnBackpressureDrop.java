package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableOnBackpressureDrop extends AbstractFlowableWithUpstream implements Consumer {
    final Consumer onDrop;

    final class BackpressureDropSubscriber extends AtomicLong implements FlowableSubscriber, Subscription {
        private static final long serialVersionUID = -6246093802440953054L;
        final Subscriber actual;
        boolean done;
        final Consumer onDrop;
        Subscription s;

        BackpressureDropSubscriber(Subscriber subscriber, Consumer consumer) {
            this.actual = subscriber;
            this.onDrop = consumer;
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
                if (get() != 0) {
                    this.actual.onNext(obj);
                    BackpressureHelper.produced(this, 1);
                } else {
                    try {
                        this.onDrop.accept(obj);
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        cancel();
                        onError(th);
                    }
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(Long.MAX_VALUE);
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this, j);
            }
        }
    }

    public FlowableOnBackpressureDrop(Flowable flowable) {
        super(flowable);
        this.onDrop = this;
    }

    public FlowableOnBackpressureDrop(Flowable flowable, Consumer consumer) {
        super(flowable);
        this.onDrop = consumer;
    }

    public void accept(Object obj) {
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new BackpressureDropSubscriber(subscriber, this.onDrop));
    }
}
