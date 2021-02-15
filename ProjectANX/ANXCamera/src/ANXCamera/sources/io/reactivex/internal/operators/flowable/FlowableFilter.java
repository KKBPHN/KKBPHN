package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.subscribers.BasicFuseableConditionalSubscriber;
import io.reactivex.internal.subscribers.BasicFuseableSubscriber;
import org.reactivestreams.Subscriber;

public final class FlowableFilter extends AbstractFlowableWithUpstream {
    final Predicate predicate;

    final class FilterConditionalSubscriber extends BasicFuseableConditionalSubscriber {
        final Predicate filter;

        FilterConditionalSubscriber(ConditionalSubscriber conditionalSubscriber, Predicate predicate) {
            super(conditionalSubscriber);
            this.filter = predicate;
        }

        public void onNext(Object obj) {
            if (!tryOnNext(obj)) {
                this.s.request(1);
            }
        }

        @Nullable
        public Object poll() {
            QueueSubscription queueSubscription = this.qs;
            Predicate predicate = this.filter;
            while (true) {
                Object poll = queueSubscription.poll();
                if (poll == null) {
                    return null;
                }
                if (predicate.test(poll)) {
                    return poll;
                }
                if (this.sourceMode == 2) {
                    queueSubscription.request(1);
                }
            }
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }

        public boolean tryOnNext(Object obj) {
            if (this.done) {
                return false;
            }
            if (this.sourceMode != 0) {
                return this.actual.tryOnNext(null);
            }
            boolean z = true;
            try {
                if (!this.filter.test(obj) || !this.actual.tryOnNext(obj)) {
                    z = false;
                }
                return z;
            } catch (Throwable th) {
                fail(th);
                return true;
            }
        }
    }

    final class FilterSubscriber extends BasicFuseableSubscriber implements ConditionalSubscriber {
        final Predicate filter;

        FilterSubscriber(Subscriber subscriber, Predicate predicate) {
            super(subscriber);
            this.filter = predicate;
        }

        public void onNext(Object obj) {
            if (!tryOnNext(obj)) {
                this.s.request(1);
            }
        }

        @Nullable
        public Object poll() {
            QueueSubscription queueSubscription = this.qs;
            Predicate predicate = this.filter;
            while (true) {
                Object poll = queueSubscription.poll();
                if (poll == null) {
                    return null;
                }
                if (predicate.test(poll)) {
                    return poll;
                }
                if (this.sourceMode == 2) {
                    queueSubscription.request(1);
                }
            }
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }

        public boolean tryOnNext(Object obj) {
            if (this.done) {
                return false;
            }
            if (this.sourceMode != 0) {
                this.actual.onNext(null);
                return true;
            }
            try {
                boolean test = this.filter.test(obj);
                if (test) {
                    this.actual.onNext(obj);
                }
                return test;
            } catch (Throwable th) {
                fail(th);
                return true;
            }
        }
    }

    public FlowableFilter(Flowable flowable, Predicate predicate2) {
        super(flowable);
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        FlowableSubscriber flowableSubscriber;
        Flowable flowable;
        if (subscriber instanceof ConditionalSubscriber) {
            flowable = this.source;
            flowableSubscriber = new FilterConditionalSubscriber((ConditionalSubscriber) subscriber, this.predicate);
        } else {
            flowable = this.source;
            flowableSubscriber = new FilterSubscriber(subscriber, this.predicate);
        }
        flowable.subscribe(flowableSubscriber);
    }
}
