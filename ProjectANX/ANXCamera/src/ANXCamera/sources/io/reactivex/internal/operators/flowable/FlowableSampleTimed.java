package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSampleTimed extends AbstractFlowableWithUpstream {
    final boolean emitLast;
    final long period;
    final Scheduler scheduler;
    final TimeUnit unit;

    final class SampleTimedEmitLast extends SampleTimedSubscriber {
        private static final long serialVersionUID = -7139995637533111443L;
        final AtomicInteger wip = new AtomicInteger(1);

        SampleTimedEmitLast(Subscriber subscriber, long j, TimeUnit timeUnit, Scheduler scheduler) {
            super(subscriber, j, timeUnit, scheduler);
        }

        /* access modifiers changed from: 0000 */
        public void complete() {
            emit();
            if (this.wip.decrementAndGet() == 0) {
                this.actual.onComplete();
            }
        }

        public void run() {
            if (this.wip.incrementAndGet() == 2) {
                emit();
                if (this.wip.decrementAndGet() == 0) {
                    this.actual.onComplete();
                }
            }
        }
    }

    final class SampleTimedNoLast extends SampleTimedSubscriber {
        private static final long serialVersionUID = -7139995637533111443L;

        SampleTimedNoLast(Subscriber subscriber, long j, TimeUnit timeUnit, Scheduler scheduler) {
            super(subscriber, j, timeUnit, scheduler);
        }

        /* access modifiers changed from: 0000 */
        public void complete() {
            this.actual.onComplete();
        }

        public void run() {
            emit();
        }
    }

    abstract class SampleTimedSubscriber extends AtomicReference implements FlowableSubscriber, Subscription, Runnable {
        private static final long serialVersionUID = -3517602651313910099L;
        final Subscriber actual;
        final long period;
        final AtomicLong requested = new AtomicLong();
        Subscription s;
        final Scheduler scheduler;
        final SequentialDisposable timer = new SequentialDisposable();
        final TimeUnit unit;

        SampleTimedSubscriber(Subscriber subscriber, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            this.actual = subscriber;
            this.period = j;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
        }

        public void cancel() {
            cancelTimer();
            this.s.cancel();
        }

        /* access modifiers changed from: 0000 */
        public void cancelTimer() {
            DisposableHelper.dispose(this.timer);
        }

        public abstract void complete();

        /* access modifiers changed from: 0000 */
        public void emit() {
            Object andSet = getAndSet(null);
            if (andSet == null) {
                return;
            }
            if (this.requested.get() != 0) {
                this.actual.onNext(andSet);
                BackpressureHelper.produced(this.requested, 1);
                return;
            }
            cancel();
            this.actual.onError(new MissingBackpressureException("Couldn't emit value due to lack of requests!"));
        }

        public void onComplete() {
            cancelTimer();
            complete();
        }

        public void onError(Throwable th) {
            cancelTimer();
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            lazySet(obj);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                SequentialDisposable sequentialDisposable = this.timer;
                Scheduler scheduler2 = this.scheduler;
                long j = this.period;
                sequentialDisposable.replace(scheduler2.schedulePeriodicallyDirect(this, j, j, this.unit));
                subscription.request(Long.MAX_VALUE);
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
            }
        }
    }

    public FlowableSampleTimed(Flowable flowable, long j, TimeUnit timeUnit, Scheduler scheduler2, boolean z) {
        super(flowable);
        this.period = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.emitLast = z;
    }

    /* JADX WARNING: type inference failed for: r6v0, types: [io.reactivex.FlowableSubscriber] */
    /* JADX WARNING: type inference failed for: r0v0, types: [io.reactivex.internal.operators.flowable.FlowableSampleTimed$SampleTimedNoLast] */
    /* JADX WARNING: type inference failed for: r0v1, types: [io.reactivex.internal.operators.flowable.FlowableSampleTimed$SampleTimedEmitLast] */
    /* JADX WARNING: type inference failed for: r0v2, types: [io.reactivex.internal.operators.flowable.FlowableSampleTimed$SampleTimedNoLast] */
    /* JADX WARNING: type inference failed for: r0v3, types: [io.reactivex.internal.operators.flowable.FlowableSampleTimed$SampleTimedEmitLast] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v2, types: [io.reactivex.internal.operators.flowable.FlowableSampleTimed$SampleTimedNoLast]
  assigns: [io.reactivex.internal.operators.flowable.FlowableSampleTimed$SampleTimedNoLast, io.reactivex.internal.operators.flowable.FlowableSampleTimed$SampleTimedEmitLast]
  uses: [io.reactivex.internal.operators.flowable.FlowableSampleTimed$SampleTimedNoLast, io.reactivex.FlowableSubscriber, io.reactivex.internal.operators.flowable.FlowableSampleTimed$SampleTimedEmitLast]
  mth insns count: 17
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
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void subscribeActual(Subscriber subscriber) {
        Flowable flowable;
        ? r6;
        SerializedSubscriber serializedSubscriber = new SerializedSubscriber(subscriber);
        if (this.emitLast) {
            flowable = this.source;
            ? sampleTimedEmitLast = new SampleTimedEmitLast(serializedSubscriber, this.period, this.unit, this.scheduler);
            r6 = sampleTimedEmitLast;
        } else {
            flowable = this.source;
            ? sampleTimedNoLast = new SampleTimedNoLast(serializedSubscriber, this.period, this.unit, this.scheduler);
            r6 = sampleTimedNoLast;
        }
        flowable.subscribe((FlowableSubscriber) r6);
    }
}
