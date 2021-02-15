package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.annotations.Nullable;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.BasicQueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import org.reactivestreams.Subscriber;

public final class FlowableRangeLong extends Flowable {
    final long end;
    final long start;

    abstract class BaseRangeSubscription extends BasicQueueSubscription {
        private static final long serialVersionUID = -2252972430506210021L;
        volatile boolean cancelled;
        final long end;
        long index;

        BaseRangeSubscription(long j, long j2) {
            this.index = j;
            this.end = j2;
        }

        public final void cancel() {
            this.cancelled = true;
        }

        public final void clear() {
            this.index = this.end;
        }

        public abstract void fastPath();

        public final boolean isEmpty() {
            return this.index == this.end;
        }

        @Nullable
        public final Long poll() {
            long j = this.index;
            if (j == this.end) {
                return null;
            }
            this.index = 1 + j;
            return Long.valueOf(j);
        }

        public final void request(long j) {
            if (SubscriptionHelper.validate(j) && BackpressureHelper.add(this, j) == 0) {
                if (j == Long.MAX_VALUE) {
                    fastPath();
                } else {
                    slowPath(j);
                }
            }
        }

        public final int requestFusion(int i) {
            return i & 1;
        }

        public abstract void slowPath(long j);
    }

    final class RangeConditionalSubscription extends BaseRangeSubscription {
        private static final long serialVersionUID = 2587302975077663557L;
        final ConditionalSubscriber actual;

        RangeConditionalSubscription(ConditionalSubscriber conditionalSubscriber, long j, long j2) {
            super(j, j2);
            this.actual = conditionalSubscriber;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            long j = this.end;
            ConditionalSubscriber conditionalSubscriber = this.actual;
            long j2 = this.index;
            while (j2 != j) {
                if (!this.cancelled) {
                    conditionalSubscriber.tryOnNext(Long.valueOf(j2));
                    j2++;
                } else {
                    return;
                }
            }
            if (!this.cancelled) {
                conditionalSubscriber.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long j) {
            long j2 = this.end;
            long j3 = this.index;
            ConditionalSubscriber conditionalSubscriber = this.actual;
            long j4 = j3;
            long j5 = j;
            do {
                long j6 = 0;
                while (true) {
                    if (j6 == j5 || j4 == j2) {
                        if (j4 == j2) {
                            if (!this.cancelled) {
                                conditionalSubscriber.onComplete();
                            }
                            return;
                        }
                        j5 = get();
                        if (j6 == j5) {
                            this.index = j4;
                            j5 = addAndGet(-j6);
                        }
                    } else if (!this.cancelled) {
                        if (conditionalSubscriber.tryOnNext(Long.valueOf(j4))) {
                            j6++;
                        }
                        j4++;
                    } else {
                        return;
                    }
                }
            } while (j5 != 0);
        }
    }

    final class RangeSubscription extends BaseRangeSubscription {
        private static final long serialVersionUID = 2587302975077663557L;
        final Subscriber actual;

        RangeSubscription(Subscriber subscriber, long j, long j2) {
            super(j, j2);
            this.actual = subscriber;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            long j = this.end;
            Subscriber subscriber = this.actual;
            long j2 = this.index;
            while (j2 != j) {
                if (!this.cancelled) {
                    subscriber.onNext(Long.valueOf(j2));
                    j2++;
                } else {
                    return;
                }
            }
            if (!this.cancelled) {
                subscriber.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long j) {
            long j2 = this.end;
            long j3 = this.index;
            Subscriber subscriber = this.actual;
            long j4 = j3;
            long j5 = j;
            do {
                long j6 = 0;
                while (true) {
                    if (j6 == j5 || j4 == j2) {
                        if (j4 == j2) {
                            if (!this.cancelled) {
                                subscriber.onComplete();
                            }
                            return;
                        }
                        j5 = get();
                        if (j6 == j5) {
                            this.index = j4;
                            j5 = addAndGet(-j6);
                        }
                    } else if (!this.cancelled) {
                        subscriber.onNext(Long.valueOf(j4));
                        j6++;
                        j4++;
                    } else {
                        return;
                    }
                }
            } while (j5 != 0);
        }
    }

    public FlowableRangeLong(long j, long j2) {
        this.start = j;
        this.end = j + j2;
    }

    /* JADX WARNING: type inference failed for: r0v1, types: [org.reactivestreams.Subscription] */
    /* JADX WARNING: type inference failed for: r7v0, types: [io.reactivex.internal.operators.flowable.FlowableRangeLong$RangeSubscription] */
    /* JADX WARNING: type inference failed for: r1v0, types: [io.reactivex.internal.operators.flowable.FlowableRangeLong$RangeConditionalSubscription] */
    /* JADX WARNING: type inference failed for: r7v1, types: [io.reactivex.internal.operators.flowable.FlowableRangeLong$RangeSubscription] */
    /* JADX WARNING: type inference failed for: r1v1, types: [io.reactivex.internal.operators.flowable.FlowableRangeLong$RangeConditionalSubscription] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r7v1, types: [io.reactivex.internal.operators.flowable.FlowableRangeLong$RangeSubscription]
  assigns: [io.reactivex.internal.operators.flowable.FlowableRangeLong$RangeSubscription, io.reactivex.internal.operators.flowable.FlowableRangeLong$RangeConditionalSubscription]
  uses: [io.reactivex.internal.operators.flowable.FlowableRangeLong$RangeSubscription, org.reactivestreams.Subscription, io.reactivex.internal.operators.flowable.FlowableRangeLong$RangeConditionalSubscription]
  mth insns count: 15
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void subscribeActual(Subscriber subscriber) {
        ? r0;
        if (subscriber instanceof ConditionalSubscriber) {
            ? rangeConditionalSubscription = new RangeConditionalSubscription((ConditionalSubscriber) subscriber, this.start, this.end);
            r0 = rangeConditionalSubscription;
        } else {
            ? rangeSubscription = new RangeSubscription(subscriber, this.start, this.end);
            r0 = rangeSubscription;
        }
        subscriber.onSubscribe(r0);
    }
}
