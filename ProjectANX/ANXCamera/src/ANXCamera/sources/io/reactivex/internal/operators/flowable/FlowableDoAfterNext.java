package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscribers.BasicFuseableConditionalSubscriber;
import io.reactivex.internal.subscribers.BasicFuseableSubscriber;
import org.reactivestreams.Subscriber;

@Experimental
public final class FlowableDoAfterNext extends AbstractFlowableWithUpstream {
    final Consumer onAfterNext;

    final class DoAfterConditionalSubscriber extends BasicFuseableConditionalSubscriber {
        final Consumer onAfterNext;

        DoAfterConditionalSubscriber(ConditionalSubscriber conditionalSubscriber, Consumer consumer) {
            super(conditionalSubscriber);
            this.onAfterNext = consumer;
        }

        public void onNext(Object obj) {
            this.actual.onNext(obj);
            if (this.sourceMode == 0) {
                try {
                    this.onAfterNext.accept(obj);
                } catch (Throwable th) {
                    fail(th);
                }
            }
        }

        @Nullable
        public Object poll() {
            Object poll = this.qs.poll();
            if (poll != null) {
                this.onAfterNext.accept(poll);
            }
            return poll;
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }

        public boolean tryOnNext(Object obj) {
            boolean tryOnNext = this.actual.tryOnNext(obj);
            try {
                this.onAfterNext.accept(obj);
            } catch (Throwable th) {
                fail(th);
            }
            return tryOnNext;
        }
    }

    final class DoAfterSubscriber extends BasicFuseableSubscriber {
        final Consumer onAfterNext;

        DoAfterSubscriber(Subscriber subscriber, Consumer consumer) {
            super(subscriber);
            this.onAfterNext = consumer;
        }

        public void onNext(Object obj) {
            if (!this.done) {
                this.actual.onNext(obj);
                if (this.sourceMode == 0) {
                    try {
                        this.onAfterNext.accept(obj);
                    } catch (Throwable th) {
                        fail(th);
                    }
                }
            }
        }

        @Nullable
        public Object poll() {
            Object poll = this.qs.poll();
            if (poll != null) {
                this.onAfterNext.accept(poll);
            }
            return poll;
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }
    }

    public FlowableDoAfterNext(Flowable flowable, Consumer consumer) {
        super(flowable);
        this.onAfterNext = consumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        FlowableSubscriber flowableSubscriber;
        Flowable flowable;
        if (subscriber instanceof ConditionalSubscriber) {
            flowable = this.source;
            flowableSubscriber = new DoAfterConditionalSubscriber((ConditionalSubscriber) subscriber, this.onAfterNext);
        } else {
            flowable = this.source;
            flowableSubscriber = new DoAfterSubscriber(subscriber, this.onAfterNext);
        }
        flowable.subscribe(flowableSubscriber);
    }
}
