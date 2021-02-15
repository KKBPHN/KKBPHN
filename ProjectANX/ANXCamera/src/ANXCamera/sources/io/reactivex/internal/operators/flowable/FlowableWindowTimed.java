package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.Scheduler.Worker;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.subscribers.QueueDrainSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.processors.UnicastProcessor;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableWindowTimed extends AbstractFlowableWithUpstream {
    final int bufferSize;
    final long maxSize;
    final boolean restartTimerOnMaxSize;
    final Scheduler scheduler;
    final long timeskip;
    final long timespan;
    final TimeUnit unit;

    final class WindowExactBoundedSubscriber extends QueueDrainSubscriber implements Subscription {
        final int bufferSize;
        long count;
        final long maxSize;
        long producerIndex;
        final boolean restartTimerOnMaxSize;
        Subscription s;
        final Scheduler scheduler;
        volatile boolean terminated;
        final SequentialDisposable timer = new SequentialDisposable();
        final long timespan;
        final TimeUnit unit;
        UnicastProcessor window;
        final Worker worker;

        final class ConsumerIndexHolder implements Runnable {
            final long index;
            final WindowExactBoundedSubscriber parent;

            ConsumerIndexHolder(long j, WindowExactBoundedSubscriber windowExactBoundedSubscriber) {
                this.index = j;
                this.parent = windowExactBoundedSubscriber;
            }

            public void run() {
                WindowExactBoundedSubscriber windowExactBoundedSubscriber = this.parent;
                if (!windowExactBoundedSubscriber.cancelled) {
                    windowExactBoundedSubscriber.queue.offer(this);
                } else {
                    windowExactBoundedSubscriber.terminated = true;
                    windowExactBoundedSubscriber.dispose();
                }
                if (windowExactBoundedSubscriber.enter()) {
                    windowExactBoundedSubscriber.drainLoop();
                }
            }
        }

        WindowExactBoundedSubscriber(Subscriber subscriber, long j, TimeUnit timeUnit, Scheduler scheduler2, int i, long j2, boolean z) {
            super(subscriber, new MpscLinkedQueue());
            this.timespan = j;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
            this.bufferSize = i;
            this.maxSize = j2;
            this.restartTimerOnMaxSize = z;
            this.worker = z ? scheduler2.createWorker() : null;
        }

        public void cancel() {
            this.cancelled = true;
        }

        public void dispose() {
            DisposableHelper.dispose(this.timer);
            Worker worker2 = this.worker;
            if (worker2 != null) {
                worker2.dispose();
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x002d, code lost:
            r0.window = null;
            r1.clear();
            r1 = r0.error;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0034, code lost:
            if (r1 == null) goto L_0x003a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0036, code lost:
            r3.onError(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x003a, code lost:
            r3.onComplete();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x003d, code lost:
            dispose();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0040, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drainLoop() {
            SimplePlainQueue simplePlainQueue = this.queue;
            Subscriber subscriber = this.actual;
            UnicastProcessor unicastProcessor = this.window;
            int i = 1;
            while (true) {
                if (this.terminated) {
                    this.s.cancel();
                    simplePlainQueue.clear();
                    break;
                }
                boolean z = this.done;
                Object poll = simplePlainQueue.poll();
                boolean z2 = poll == null;
                boolean z3 = poll instanceof ConsumerIndexHolder;
                if (!z || (!z2 && !z3)) {
                    if (z2) {
                        i = leave(-i);
                        if (i == 0) {
                            return;
                        }
                    } else {
                        int i2 = i;
                        if (z3) {
                            ConsumerIndexHolder consumerIndexHolder = (ConsumerIndexHolder) poll;
                            if (this.restartTimerOnMaxSize || this.producerIndex == consumerIndexHolder.index) {
                                unicastProcessor.onComplete();
                                this.count = 0;
                                unicastProcessor = UnicastProcessor.create(this.bufferSize);
                                this.window = unicastProcessor;
                                long requested = requested();
                                if (requested == 0) {
                                    this.window = null;
                                    this.queue.clear();
                                    this.s.cancel();
                                    subscriber.onError(new MissingBackpressureException("Could not deliver first window due to lack of requests."));
                                    break;
                                }
                                subscriber.onNext(unicastProcessor);
                                if (requested != Long.MAX_VALUE) {
                                    produced(1);
                                }
                            }
                        } else {
                            NotificationLite.getValue(poll);
                            unicastProcessor.onNext(poll);
                            long j = this.count + 1;
                            if (j >= this.maxSize) {
                                this.producerIndex++;
                                this.count = 0;
                                unicastProcessor.onComplete();
                                long requested2 = requested();
                                if (requested2 == 0) {
                                    this.window = null;
                                    this.s.cancel();
                                    this.actual.onError(new MissingBackpressureException("Could not deliver window due to lack of requests"));
                                    break;
                                }
                                UnicastProcessor create = UnicastProcessor.create(this.bufferSize);
                                this.window = create;
                                this.actual.onNext(create);
                                if (requested2 != Long.MAX_VALUE) {
                                    produced(1);
                                }
                                if (this.restartTimerOnMaxSize) {
                                    Disposable disposable = (Disposable) this.timer.get();
                                    disposable.dispose();
                                    Worker worker2 = this.worker;
                                    ConsumerIndexHolder consumerIndexHolder2 = new ConsumerIndexHolder(this.producerIndex, this);
                                    long j2 = this.timespan;
                                    Disposable schedulePeriodically = worker2.schedulePeriodically(consumerIndexHolder2, j2, j2, this.unit);
                                    if (!this.timer.compareAndSet(disposable, schedulePeriodically)) {
                                        schedulePeriodically.dispose();
                                    }
                                }
                                unicastProcessor = create;
                            } else {
                                this.count = j;
                            }
                        }
                        i = i2;
                    }
                }
            }
            dispose();
        }

        public void onComplete() {
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            this.actual.onComplete();
            dispose();
        }

        public void onError(Throwable th) {
            this.error = th;
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            this.actual.onError(th);
            dispose();
        }

        public void onNext(Object obj) {
            if (!this.terminated) {
                if (fastEnter()) {
                    UnicastProcessor unicastProcessor = this.window;
                    unicastProcessor.onNext(obj);
                    long j = this.count + 1;
                    if (j >= this.maxSize) {
                        this.producerIndex++;
                        this.count = 0;
                        unicastProcessor.onComplete();
                        long requested = requested();
                        if (requested != 0) {
                            UnicastProcessor create = UnicastProcessor.create(this.bufferSize);
                            this.window = create;
                            this.actual.onNext(create);
                            if (requested != Long.MAX_VALUE) {
                                produced(1);
                            }
                            if (this.restartTimerOnMaxSize) {
                                Disposable disposable = (Disposable) this.timer.get();
                                disposable.dispose();
                                Worker worker2 = this.worker;
                                ConsumerIndexHolder consumerIndexHolder = new ConsumerIndexHolder(this.producerIndex, this);
                                long j2 = this.timespan;
                                Disposable schedulePeriodically = worker2.schedulePeriodically(consumerIndexHolder, j2, j2, this.unit);
                                if (!this.timer.compareAndSet(disposable, schedulePeriodically)) {
                                    schedulePeriodically.dispose();
                                }
                            }
                        } else {
                            this.window = null;
                            this.s.cancel();
                            this.actual.onError(new MissingBackpressureException("Could not deliver window due to lack of requests"));
                            dispose();
                            return;
                        }
                    } else {
                        this.count = j;
                    }
                    if (leave(-1) == 0) {
                        return;
                    }
                } else {
                    SimplePlainQueue simplePlainQueue = this.queue;
                    NotificationLite.next(obj);
                    simplePlainQueue.offer(obj);
                    if (!enter()) {
                        return;
                    }
                }
                drainLoop();
            }
        }

        public void onSubscribe(Subscription subscription) {
            Disposable disposable;
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                Subscriber subscriber = this.actual;
                subscriber.onSubscribe(this);
                if (!this.cancelled) {
                    UnicastProcessor create = UnicastProcessor.create(this.bufferSize);
                    this.window = create;
                    long requested = requested();
                    if (requested != 0) {
                        subscriber.onNext(create);
                        if (requested != Long.MAX_VALUE) {
                            produced(1);
                        }
                        ConsumerIndexHolder consumerIndexHolder = new ConsumerIndexHolder(this.producerIndex, this);
                        if (this.restartTimerOnMaxSize) {
                            Worker worker2 = this.worker;
                            long j = this.timespan;
                            disposable = worker2.schedulePeriodically(consumerIndexHolder, j, j, this.unit);
                        } else {
                            Scheduler scheduler2 = this.scheduler;
                            long j2 = this.timespan;
                            disposable = scheduler2.schedulePeriodicallyDirect(consumerIndexHolder, j2, j2, this.unit);
                        }
                        if (this.timer.replace(disposable)) {
                            subscription.request(Long.MAX_VALUE);
                        }
                    } else {
                        this.cancelled = true;
                        subscription.cancel();
                        subscriber.onError(new MissingBackpressureException("Could not deliver initial window due to lack of requests."));
                    }
                }
            }
        }

        public void request(long j) {
            requested(j);
        }
    }

    final class WindowExactUnboundedSubscriber extends QueueDrainSubscriber implements FlowableSubscriber, Subscription, Runnable {
        static final Object NEXT = new Object();
        final int bufferSize;
        Subscription s;
        final Scheduler scheduler;
        volatile boolean terminated;
        final SequentialDisposable timer = new SequentialDisposable();
        final long timespan;
        final TimeUnit unit;
        UnicastProcessor window;

        WindowExactUnboundedSubscriber(Subscriber subscriber, long j, TimeUnit timeUnit, Scheduler scheduler2, int i) {
            super(subscriber, new MpscLinkedQueue());
            this.timespan = j;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
            this.bufferSize = i;
        }

        public void cancel() {
            this.cancelled = true;
        }

        public void dispose() {
            DisposableHelper.dispose(this.timer);
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            SimplePlainQueue simplePlainQueue = this.queue;
            Subscriber subscriber = this.actual;
            UnicastProcessor unicastProcessor = this.window;
            int i = 1;
            while (true) {
                boolean z = this.terminated;
                boolean z2 = this.done;
                Object poll = simplePlainQueue.poll();
                if (!z2 || !(poll == null || poll == NEXT)) {
                    if (poll == null) {
                        i = leave(-i);
                        if (i == 0) {
                            return;
                        }
                    } else if (poll == NEXT) {
                        unicastProcessor.onComplete();
                        if (!z) {
                            unicastProcessor = UnicastProcessor.create(this.bufferSize);
                            this.window = unicastProcessor;
                            long requested = requested();
                            if (requested != 0) {
                                subscriber.onNext(unicastProcessor);
                                if (requested != Long.MAX_VALUE) {
                                    produced(1);
                                }
                            } else {
                                this.window = null;
                                this.queue.clear();
                                this.s.cancel();
                                dispose();
                                subscriber.onError(new MissingBackpressureException("Could not deliver first window due to lack of requests."));
                                return;
                            }
                        } else {
                            this.s.cancel();
                        }
                    } else {
                        NotificationLite.getValue(poll);
                        unicastProcessor.onNext(poll);
                    }
                }
            }
            this.window = null;
            simplePlainQueue.clear();
            dispose();
            Throwable th = this.error;
            if (th != null) {
                unicastProcessor.onError(th);
            } else {
                unicastProcessor.onComplete();
            }
        }

        public void onComplete() {
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            this.actual.onComplete();
            dispose();
        }

        public void onError(Throwable th) {
            this.error = th;
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            this.actual.onError(th);
            dispose();
        }

        public void onNext(Object obj) {
            if (!this.terminated) {
                if (fastEnter()) {
                    this.window.onNext(obj);
                    if (leave(-1) == 0) {
                        return;
                    }
                } else {
                    SimplePlainQueue simplePlainQueue = this.queue;
                    NotificationLite.next(obj);
                    simplePlainQueue.offer(obj);
                    if (!enter()) {
                        return;
                    }
                }
                drainLoop();
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.window = UnicastProcessor.create(this.bufferSize);
                Subscriber subscriber = this.actual;
                subscriber.onSubscribe(this);
                long requested = requested();
                if (requested != 0) {
                    subscriber.onNext(this.window);
                    if (requested != Long.MAX_VALUE) {
                        produced(1);
                    }
                    if (!this.cancelled) {
                        SequentialDisposable sequentialDisposable = this.timer;
                        Scheduler scheduler2 = this.scheduler;
                        long j = this.timespan;
                        if (sequentialDisposable.replace(scheduler2.schedulePeriodicallyDirect(this, j, j, this.unit))) {
                            subscription.request(Long.MAX_VALUE);
                            return;
                        }
                        return;
                    }
                    return;
                }
                this.cancelled = true;
                subscription.cancel();
                subscriber.onError(new MissingBackpressureException("Could not deliver first window due to lack of requests."));
            }
        }

        public void request(long j) {
            requested(j);
        }

        public void run() {
            if (this.cancelled) {
                this.terminated = true;
                dispose();
            }
            this.queue.offer(NEXT);
            if (enter()) {
                drainLoop();
            }
        }
    }

    final class WindowSkipSubscriber extends QueueDrainSubscriber implements Subscription, Runnable {
        final int bufferSize;
        Subscription s;
        volatile boolean terminated;
        final long timeskip;
        final long timespan;
        final TimeUnit unit;
        final List windows = new LinkedList();
        final Worker worker;

        final class Completion implements Runnable {
            private final UnicastProcessor processor;

            Completion(UnicastProcessor unicastProcessor) {
                this.processor = unicastProcessor;
            }

            public void run() {
                WindowSkipSubscriber.this.complete(this.processor);
            }
        }

        final class SubjectWork {
            final boolean open;
            final UnicastProcessor w;

            SubjectWork(UnicastProcessor unicastProcessor, boolean z) {
                this.w = unicastProcessor;
                this.open = z;
            }
        }

        WindowSkipSubscriber(Subscriber subscriber, long j, long j2, TimeUnit timeUnit, Worker worker2, int i) {
            super(subscriber, new MpscLinkedQueue());
            this.timespan = j;
            this.timeskip = j2;
            this.unit = timeUnit;
            this.worker = worker2;
            this.bufferSize = i;
        }

        public void cancel() {
            this.cancelled = true;
        }

        /* access modifiers changed from: 0000 */
        public void complete(UnicastProcessor unicastProcessor) {
            this.queue.offer(new SubjectWork(unicastProcessor, false));
            if (enter()) {
                drainLoop();
            }
        }

        public void dispose() {
            this.worker.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            SimplePlainQueue simplePlainQueue = this.queue;
            Subscriber subscriber = this.actual;
            List<UnicastProcessor> list = this.windows;
            int i = 1;
            while (!this.terminated) {
                boolean z = this.done;
                Object poll = simplePlainQueue.poll();
                boolean z2 = poll == null;
                boolean z3 = poll instanceof SubjectWork;
                if (z && (z2 || z3)) {
                    simplePlainQueue.clear();
                    Throwable th = this.error;
                    if (th != null) {
                        for (UnicastProcessor onError : list) {
                            onError.onError(th);
                        }
                    } else {
                        for (UnicastProcessor onComplete : list) {
                            onComplete.onComplete();
                        }
                    }
                    list.clear();
                    dispose();
                    return;
                } else if (z2) {
                    i = leave(-i);
                    if (i == 0) {
                        return;
                    }
                } else if (z3) {
                    SubjectWork subjectWork = (SubjectWork) poll;
                    if (!subjectWork.open) {
                        list.remove(subjectWork.w);
                        subjectWork.w.onComplete();
                        if (list.isEmpty() && this.cancelled) {
                            this.terminated = true;
                        }
                    } else if (!this.cancelled) {
                        long requested = requested();
                        if (requested != 0) {
                            UnicastProcessor create = UnicastProcessor.create(this.bufferSize);
                            list.add(create);
                            subscriber.onNext(create);
                            if (requested != Long.MAX_VALUE) {
                                produced(1);
                            }
                            this.worker.schedule(new Completion(create), this.timespan, this.unit);
                        } else {
                            subscriber.onError(new MissingBackpressureException("Can't emit window due to lack of requests"));
                        }
                    }
                } else {
                    for (UnicastProcessor onNext : list) {
                        onNext.onNext(poll);
                    }
                }
            }
            this.s.cancel();
            dispose();
            simplePlainQueue.clear();
            list.clear();
        }

        public void onComplete() {
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            this.actual.onComplete();
            dispose();
        }

        public void onError(Throwable th) {
            this.error = th;
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            this.actual.onError(th);
            dispose();
        }

        public void onNext(Object obj) {
            if (fastEnter()) {
                for (UnicastProcessor onNext : this.windows) {
                    onNext.onNext(obj);
                }
                if (leave(-1) == 0) {
                    return;
                }
            } else {
                this.queue.offer(obj);
                if (!enter()) {
                    return;
                }
            }
            drainLoop();
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                if (!this.cancelled) {
                    long requested = requested();
                    if (requested != 0) {
                        UnicastProcessor create = UnicastProcessor.create(this.bufferSize);
                        this.windows.add(create);
                        this.actual.onNext(create);
                        if (requested != Long.MAX_VALUE) {
                            produced(1);
                        }
                        this.worker.schedule(new Completion(create), this.timespan, this.unit);
                        Worker worker2 = this.worker;
                        long j = this.timeskip;
                        worker2.schedulePeriodically(this, j, j, this.unit);
                        subscription.request(Long.MAX_VALUE);
                    } else {
                        subscription.cancel();
                        this.actual.onError(new MissingBackpressureException("Could not emit the first window due to lack of requests"));
                    }
                }
            }
        }

        public void request(long j) {
            requested(j);
        }

        public void run() {
            SubjectWork subjectWork = new SubjectWork(UnicastProcessor.create(this.bufferSize), true);
            if (!this.cancelled) {
                this.queue.offer(subjectWork);
            }
            if (enter()) {
                drainLoop();
            }
        }
    }

    public FlowableWindowTimed(Flowable flowable, long j, long j2, TimeUnit timeUnit, Scheduler scheduler2, long j3, int i, boolean z) {
        super(flowable);
        this.timespan = j;
        this.timeskip = j2;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.maxSize = j3;
        this.bufferSize = i;
        this.restartTimerOnMaxSize = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        SerializedSubscriber serializedSubscriber = new SerializedSubscriber(subscriber);
        long j = this.timespan;
        long j2 = this.timeskip;
        if (j == j2) {
            long j3 = this.maxSize;
            if (j3 == Long.MAX_VALUE) {
                Flowable flowable = this.source;
                WindowExactUnboundedSubscriber windowExactUnboundedSubscriber = new WindowExactUnboundedSubscriber(serializedSubscriber, j, this.unit, this.scheduler, this.bufferSize);
                flowable.subscribe((FlowableSubscriber) windowExactUnboundedSubscriber);
                return;
            }
            Flowable flowable2 = this.source;
            WindowExactBoundedSubscriber windowExactBoundedSubscriber = new WindowExactBoundedSubscriber(serializedSubscriber, j, this.unit, this.scheduler, this.bufferSize, j3, this.restartTimerOnMaxSize);
            flowable2.subscribe((FlowableSubscriber) windowExactBoundedSubscriber);
            return;
        }
        Flowable flowable3 = this.source;
        WindowSkipSubscriber windowSkipSubscriber = new WindowSkipSubscriber(serializedSubscriber, j, j2, this.unit, this.scheduler.createWorker(), this.bufferSize);
        flowable3.subscribe((FlowableSubscriber) windowSkipSubscriber);
    }
}
