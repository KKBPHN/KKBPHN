package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.Scheduler.Worker;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableObserveOn extends AbstractFlowableWithUpstream {
    final boolean delayError;
    final int prefetch;
    final Scheduler scheduler;

    abstract class BaseObserveOnSubscriber extends BasicIntQueueSubscription implements FlowableSubscriber, Runnable {
        private static final long serialVersionUID = -8241002408341274697L;
        volatile boolean cancelled;
        final boolean delayError;
        volatile boolean done;
        Throwable error;
        final int limit;
        boolean outputFused;
        final int prefetch;
        long produced;
        SimpleQueue queue;
        final AtomicLong requested = new AtomicLong();
        Subscription s;
        int sourceMode;
        final Worker worker;

        BaseObserveOnSubscriber(Worker worker2, boolean z, int i) {
            this.worker = worker2;
            this.delayError = z;
            this.prefetch = i;
            this.limit = i - (i >> 2);
        }

        public final void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.s.cancel();
                this.worker.dispose();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0027, code lost:
            if (r4 != false) goto L_0x0029;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0029, code lost:
            r5.onComplete();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0013, code lost:
            if (r3 != null) goto L_0x0023;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final boolean checkTerminated(boolean z, boolean z2, Subscriber subscriber) {
            Throwable th;
            if (this.cancelled) {
                clear();
                return true;
            }
            if (z) {
                if (!this.delayError) {
                    th = this.error;
                    if (th != null) {
                        clear();
                        subscriber.onError(th);
                    }
                } else if (z2) {
                    th = this.error;
                }
                this.worker.dispose();
                return true;
            }
            return false;
        }

        public final void clear() {
            this.queue.clear();
        }

        public final boolean isEmpty() {
            return this.queue.isEmpty();
        }

        public final void onComplete() {
            if (!this.done) {
                this.done = true;
                trySchedule();
            }
        }

        public final void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.error = th;
            this.done = true;
            trySchedule();
        }

        public final void onNext(Object obj) {
            if (!this.done) {
                if (this.sourceMode == 2) {
                    trySchedule();
                    return;
                }
                if (!this.queue.offer(obj)) {
                    this.s.cancel();
                    this.error = new MissingBackpressureException("Queue is full?!");
                    this.done = true;
                }
                trySchedule();
            }
        }

        public final void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                trySchedule();
            }
        }

        public final int requestFusion(int i) {
            if ((i & 2) == 0) {
                return 0;
            }
            this.outputFused = true;
            return 2;
        }

        public final void run() {
            if (this.outputFused) {
                runBackfused();
            } else if (this.sourceMode == 1) {
                runSync();
            } else {
                runAsync();
            }
        }

        public abstract void runAsync();

        public abstract void runBackfused();

        public abstract void runSync();

        /* access modifiers changed from: 0000 */
        public final void trySchedule() {
            if (getAndIncrement() == 0) {
                this.worker.schedule(this);
            }
        }
    }

    final class ObserveOnConditionalSubscriber extends BaseObserveOnSubscriber {
        private static final long serialVersionUID = 644624475404284533L;
        final ConditionalSubscriber actual;
        long consumed;

        ObserveOnConditionalSubscriber(ConditionalSubscriber conditionalSubscriber, Worker worker, boolean z, int i) {
            super(worker, z, i);
            this.actual = conditionalSubscriber;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                if (subscription instanceof QueueSubscription) {
                    QueueSubscription queueSubscription = (QueueSubscription) subscription;
                    int requestFusion = queueSubscription.requestFusion(7);
                    if (requestFusion == 1) {
                        this.sourceMode = 1;
                        this.queue = queueSubscription;
                        this.done = true;
                        this.actual.onSubscribe(this);
                        return;
                    } else if (requestFusion == 2) {
                        this.sourceMode = 2;
                        this.queue = queueSubscription;
                        this.actual.onSubscribe(this);
                        subscription.request((long) this.prefetch);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.prefetch);
                this.actual.onSubscribe(this);
                subscription.request((long) this.prefetch);
            }
        }

        @Nullable
        public Object poll() {
            Object poll = this.queue.poll();
            if (!(poll == null || this.sourceMode == 1)) {
                long j = this.consumed + 1;
                if (j == ((long) this.limit)) {
                    this.consumed = 0;
                    this.s.request(j);
                } else {
                    this.consumed = j;
                }
            }
            return poll;
        }

        /* access modifiers changed from: 0000 */
        public void runAsync() {
            int i;
            ConditionalSubscriber conditionalSubscriber = this.actual;
            SimpleQueue simpleQueue = this.queue;
            long j = this.produced;
            long j2 = this.consumed;
            int i2 = 1;
            while (true) {
                long j3 = this.requested.get();
                while (true) {
                    i = (j > j3 ? 1 : (j == j3 ? 0 : -1));
                    if (i == 0) {
                        break;
                    }
                    boolean z = this.done;
                    try {
                        Object poll = simpleQueue.poll();
                        boolean z2 = poll == null;
                        if (!checkTerminated(z, z2, conditionalSubscriber)) {
                            if (z2) {
                                break;
                            }
                            if (conditionalSubscriber.tryOnNext(poll)) {
                                j++;
                            }
                            j2++;
                            if (j2 == ((long) this.limit)) {
                                this.s.request(j2);
                                j2 = 0;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.s.cancel();
                        simpleQueue.clear();
                        conditionalSubscriber.onError(th);
                        this.worker.dispose();
                        return;
                    }
                }
                if (i != 0 || !checkTerminated(this.done, simpleQueue.isEmpty(), conditionalSubscriber)) {
                    int i3 = get();
                    if (i2 == i3) {
                        this.produced = j;
                        this.consumed = j2;
                        i2 = addAndGet(-i2);
                        if (i2 == 0) {
                            return;
                        }
                    } else {
                        i2 = i3;
                    }
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void runBackfused() {
            int i = 1;
            while (!this.cancelled) {
                boolean z = this.done;
                this.actual.onNext(null);
                if (z) {
                    Throwable th = this.error;
                    if (th != null) {
                        this.actual.onError(th);
                    } else {
                        this.actual.onComplete();
                    }
                    this.worker.dispose();
                    return;
                }
                i = addAndGet(-i);
                if (i == 0) {
                    return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x003e, code lost:
            if (r9.cancelled == false) goto L_0x0041;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0040, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0045, code lost:
            if (r1.isEmpty() == false) goto L_0x0048;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0048, code lost:
            r5 = get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x004c, code lost:
            if (r4 != r5) goto L_0x0058;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x004e, code lost:
            r9.produced = r2;
            r4 = addAndGet(-r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0055, code lost:
            if (r4 != 0) goto L_0x0007;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0057, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0058, code lost:
            r4 = r5;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void runSync() {
            ConditionalSubscriber conditionalSubscriber = this.actual;
            SimpleQueue simpleQueue = this.queue;
            long j = this.produced;
            int i = 1;
            loop0:
            while (true) {
                long j2 = this.requested.get();
                while (true) {
                    if (j == j2) {
                        break;
                    }
                    try {
                        Object poll = simpleQueue.poll();
                        if (!this.cancelled) {
                            if (poll == null) {
                                break loop0;
                            } else if (conditionalSubscriber.tryOnNext(poll)) {
                                j++;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.s.cancel();
                        conditionalSubscriber.onError(th);
                    }
                }
            }
            conditionalSubscriber.onComplete();
            this.worker.dispose();
        }
    }

    final class ObserveOnSubscriber extends BaseObserveOnSubscriber implements FlowableSubscriber {
        private static final long serialVersionUID = -4547113800637756442L;
        final Subscriber actual;

        ObserveOnSubscriber(Subscriber subscriber, Worker worker, boolean z, int i) {
            super(worker, z, i);
            this.actual = subscriber;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                if (subscription instanceof QueueSubscription) {
                    QueueSubscription queueSubscription = (QueueSubscription) subscription;
                    int requestFusion = queueSubscription.requestFusion(7);
                    if (requestFusion == 1) {
                        this.sourceMode = 1;
                        this.queue = queueSubscription;
                        this.done = true;
                        this.actual.onSubscribe(this);
                        return;
                    } else if (requestFusion == 2) {
                        this.sourceMode = 2;
                        this.queue = queueSubscription;
                        this.actual.onSubscribe(this);
                        subscription.request((long) this.prefetch);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.prefetch);
                this.actual.onSubscribe(this);
                subscription.request((long) this.prefetch);
            }
        }

        @Nullable
        public Object poll() {
            Object poll = this.queue.poll();
            if (!(poll == null || this.sourceMode == 1)) {
                long j = this.produced + 1;
                if (j == ((long) this.limit)) {
                    this.produced = 0;
                    this.s.request(j);
                } else {
                    this.produced = j;
                }
            }
            return poll;
        }

        /* access modifiers changed from: 0000 */
        public void runAsync() {
            int i;
            Subscriber subscriber = this.actual;
            SimpleQueue simpleQueue = this.queue;
            long j = this.produced;
            int i2 = 1;
            while (true) {
                long j2 = this.requested.get();
                while (true) {
                    i = (j > j2 ? 1 : (j == j2 ? 0 : -1));
                    if (i == 0) {
                        break;
                    }
                    boolean z = this.done;
                    try {
                        Object poll = simpleQueue.poll();
                        boolean z2 = poll == null;
                        if (!checkTerminated(z, z2, subscriber)) {
                            if (z2) {
                                break;
                            }
                            subscriber.onNext(poll);
                            j++;
                            if (j == ((long) this.limit)) {
                                if (j2 != Long.MAX_VALUE) {
                                    j2 = this.requested.addAndGet(-j);
                                }
                                this.s.request(j);
                                j = 0;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.s.cancel();
                        simpleQueue.clear();
                        subscriber.onError(th);
                        this.worker.dispose();
                        return;
                    }
                }
                if (i != 0 || !checkTerminated(this.done, simpleQueue.isEmpty(), subscriber)) {
                    int i3 = get();
                    if (i2 == i3) {
                        this.produced = j;
                        i2 = addAndGet(-i2);
                        if (i2 == 0) {
                            return;
                        }
                    } else {
                        i2 = i3;
                    }
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void runBackfused() {
            int i = 1;
            while (!this.cancelled) {
                boolean z = this.done;
                this.actual.onNext(null);
                if (z) {
                    Throwable th = this.error;
                    if (th != null) {
                        this.actual.onError(th);
                    } else {
                        this.actual.onComplete();
                    }
                    this.worker.dispose();
                    return;
                }
                i = addAndGet(-i);
                if (i == 0) {
                    return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x003b, code lost:
            if (r9.cancelled == false) goto L_0x003e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x003d, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0042, code lost:
            if (r1.isEmpty() == false) goto L_0x0045;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0045, code lost:
            r5 = get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0049, code lost:
            if (r4 != r5) goto L_0x0055;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x004b, code lost:
            r9.produced = r2;
            r4 = addAndGet(-r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0052, code lost:
            if (r4 != 0) goto L_0x0007;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0054, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0055, code lost:
            r4 = r5;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void runSync() {
            Subscriber subscriber = this.actual;
            SimpleQueue simpleQueue = this.queue;
            long j = this.produced;
            int i = 1;
            loop0:
            while (true) {
                long j2 = this.requested.get();
                while (true) {
                    if (j == j2) {
                        break;
                    }
                    try {
                        Object poll = simpleQueue.poll();
                        if (!this.cancelled) {
                            if (poll == null) {
                                break loop0;
                            }
                            subscriber.onNext(poll);
                            j++;
                        } else {
                            return;
                        }
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.s.cancel();
                        subscriber.onError(th);
                    }
                }
            }
            subscriber.onComplete();
            this.worker.dispose();
        }
    }

    public FlowableObserveOn(Flowable flowable, Scheduler scheduler2, boolean z, int i) {
        super(flowable);
        this.scheduler = scheduler2;
        this.delayError = z;
        this.prefetch = i;
    }

    public void subscribeActual(Subscriber subscriber) {
        FlowableSubscriber flowableSubscriber;
        Flowable flowable;
        Worker createWorker = this.scheduler.createWorker();
        if (subscriber instanceof ConditionalSubscriber) {
            flowable = this.source;
            flowableSubscriber = new ObserveOnConditionalSubscriber((ConditionalSubscriber) subscriber, createWorker, this.delayError, this.prefetch);
        } else {
            flowable = this.source;
            flowableSubscriber = new ObserveOnSubscriber(subscriber, createWorker, this.delayError, this.prefetch);
        }
        flowable.subscribe(flowableSubscriber);
    }
}
