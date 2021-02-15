package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.Scheduler.Worker;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTimeoutTimed extends AbstractFlowableWithUpstream {
    final Publisher other;
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    final class FallbackSubscriber implements FlowableSubscriber {
        final Subscriber actual;
        final SubscriptionArbiter arbiter;

        FallbackSubscriber(Subscriber subscriber, SubscriptionArbiter subscriptionArbiter) {
            this.actual = subscriber;
            this.arbiter = subscriptionArbiter;
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            this.actual.onNext(obj);
        }

        public void onSubscribe(Subscription subscription) {
            this.arbiter.setSubscription(subscription);
        }
    }

    final class TimeoutFallbackSubscriber extends SubscriptionArbiter implements FlowableSubscriber, TimeoutSupport {
        private static final long serialVersionUID = 3764492702657003550L;
        final Subscriber actual;
        long consumed;
        Publisher fallback;
        final AtomicLong index = new AtomicLong();
        final SequentialDisposable task = new SequentialDisposable();
        final long timeout;
        final TimeUnit unit;
        final AtomicReference upstream = new AtomicReference();
        final Worker worker;

        TimeoutFallbackSubscriber(Subscriber subscriber, long j, TimeUnit timeUnit, Worker worker2, Publisher publisher) {
            this.actual = subscriber;
            this.timeout = j;
            this.unit = timeUnit;
            this.worker = worker2;
            this.fallback = publisher;
        }

        public void cancel() {
            super.cancel();
            this.worker.dispose();
        }

        public void onComplete() {
            if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onComplete();
                this.worker.dispose();
            }
        }

        public void onError(Throwable th) {
            if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onError(th);
                this.worker.dispose();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onNext(Object obj) {
            long j = this.index.get();
            if (j != Long.MAX_VALUE) {
                long j2 = j + 1;
                if (this.index.compareAndSet(j, j2)) {
                    ((Disposable) this.task.get()).dispose();
                    this.consumed++;
                    this.actual.onNext(obj);
                    startTimeout(j2);
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this.upstream, subscription)) {
                setSubscription(subscription);
            }
        }

        public void onTimeout(long j) {
            if (this.index.compareAndSet(j, Long.MAX_VALUE)) {
                SubscriptionHelper.cancel(this.upstream);
                long j2 = this.consumed;
                if (j2 != 0) {
                    produced(j2);
                }
                Publisher publisher = this.fallback;
                this.fallback = null;
                publisher.subscribe(new FallbackSubscriber(this.actual, this));
                this.worker.dispose();
            }
        }

        /* access modifiers changed from: 0000 */
        public void startTimeout(long j) {
            this.task.replace(this.worker.schedule(new TimeoutTask(j, this), this.timeout, this.unit));
        }
    }

    final class TimeoutSubscriber extends AtomicLong implements FlowableSubscriber, Subscription, TimeoutSupport {
        private static final long serialVersionUID = 3764492702657003550L;
        final Subscriber actual;
        final AtomicLong requested = new AtomicLong();
        final SequentialDisposable task = new SequentialDisposable();
        final long timeout;
        final TimeUnit unit;
        final AtomicReference upstream = new AtomicReference();
        final Worker worker;

        TimeoutSubscriber(Subscriber subscriber, long j, TimeUnit timeUnit, Worker worker2) {
            this.actual = subscriber;
            this.timeout = j;
            this.unit = timeUnit;
            this.worker = worker2;
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.upstream);
            this.worker.dispose();
        }

        public void onComplete() {
            if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onComplete();
                this.worker.dispose();
            }
        }

        public void onError(Throwable th) {
            if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onError(th);
                this.worker.dispose();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onNext(Object obj) {
            long j = get();
            if (j != Long.MAX_VALUE) {
                long j2 = 1 + j;
                if (compareAndSet(j, j2)) {
                    ((Disposable) this.task.get()).dispose();
                    this.actual.onNext(obj);
                    startTimeout(j2);
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, subscription);
        }

        public void onTimeout(long j) {
            if (compareAndSet(j, Long.MAX_VALUE)) {
                SubscriptionHelper.cancel(this.upstream);
                this.actual.onError(new TimeoutException());
                this.worker.dispose();
            }
        }

        public void request(long j) {
            SubscriptionHelper.deferredRequest(this.upstream, this.requested, j);
        }

        /* access modifiers changed from: 0000 */
        public void startTimeout(long j) {
            this.task.replace(this.worker.schedule(new TimeoutTask(j, this), this.timeout, this.unit));
        }
    }

    interface TimeoutSupport {
        void onTimeout(long j);
    }

    final class TimeoutTask implements Runnable {
        final long idx;
        final TimeoutSupport parent;

        TimeoutTask(long j, TimeoutSupport timeoutSupport) {
            this.idx = j;
            this.parent = timeoutSupport;
        }

        public void run() {
            this.parent.onTimeout(this.idx);
        }
    }

    public FlowableTimeoutTimed(Flowable flowable, long j, TimeUnit timeUnit, Scheduler scheduler2, Publisher publisher) {
        super(flowable);
        this.timeout = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.other = publisher;
    }

    /* JADX WARNING: type inference failed for: r0v1, types: [io.reactivex.FlowableSubscriber] */
    /* JADX WARNING: type inference failed for: r3v1, types: [io.reactivex.internal.operators.flowable.FlowableTimeoutTimed$TimeoutFallbackSubscriber] */
    /* JADX WARNING: type inference failed for: r3v3, types: [io.reactivex.internal.operators.flowable.FlowableTimeoutTimed$TimeoutSubscriber] */
    /* JADX WARNING: type inference failed for: r3v4, types: [io.reactivex.internal.operators.flowable.FlowableTimeoutTimed$TimeoutFallbackSubscriber] */
    /* JADX WARNING: type inference failed for: r3v5, types: [io.reactivex.internal.operators.flowable.FlowableTimeoutTimed$TimeoutSubscriber] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r3v4, types: [io.reactivex.internal.operators.flowable.FlowableTimeoutTimed$TimeoutFallbackSubscriber]
  assigns: [io.reactivex.internal.operators.flowable.FlowableTimeoutTimed$TimeoutFallbackSubscriber, io.reactivex.internal.operators.flowable.FlowableTimeoutTimed$TimeoutSubscriber]
  uses: [io.reactivex.internal.operators.flowable.FlowableTimeoutTimed$TimeoutFallbackSubscriber, io.reactivex.FlowableSubscriber, io.reactivex.internal.operators.flowable.FlowableTimeoutTimed$TimeoutSubscriber]
  mth insns count: 24
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
        ? r0;
        if (this.other == null) {
            ? timeoutSubscriber = new TimeoutSubscriber(subscriber, this.timeout, this.unit, this.scheduler.createWorker());
            subscriber.onSubscribe(timeoutSubscriber);
            timeoutSubscriber.startTimeout(0);
            r0 = timeoutSubscriber;
        } else {
            ? timeoutFallbackSubscriber = new TimeoutFallbackSubscriber(subscriber, this.timeout, this.unit, this.scheduler.createWorker(), this.other);
            subscriber.onSubscribe(timeoutFallbackSubscriber);
            timeoutFallbackSubscriber.startTimeout(0);
            r0 = timeoutFallbackSubscriber;
        }
        this.source.subscribe((FlowableSubscriber) r0);
    }
}
