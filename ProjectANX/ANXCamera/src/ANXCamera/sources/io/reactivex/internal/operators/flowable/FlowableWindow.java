package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.processors.UnicastProcessor;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Processor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableWindow extends AbstractFlowableWithUpstream {
    final int bufferSize;
    final long size;
    final long skip;

    final class WindowExactSubscriber extends AtomicInteger implements FlowableSubscriber, Subscription, Runnable {
        private static final long serialVersionUID = -2365647875069161133L;
        final Subscriber actual;
        final int bufferSize;
        long index;
        final AtomicBoolean once = new AtomicBoolean();
        Subscription s;
        final long size;
        UnicastProcessor window;

        WindowExactSubscriber(Subscriber subscriber, long j, int i) {
            super(1);
            this.actual = subscriber;
            this.size = j;
            this.bufferSize = i;
        }

        public void cancel() {
            if (this.once.compareAndSet(false, true)) {
                run();
            }
        }

        public void onComplete() {
            UnicastProcessor unicastProcessor = this.window;
            if (unicastProcessor != null) {
                this.window = null;
                unicastProcessor.onComplete();
            }
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            UnicastProcessor unicastProcessor = this.window;
            if (unicastProcessor != null) {
                this.window = null;
                unicastProcessor.onError(th);
            }
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            long j = this.index;
            UnicastProcessor unicastProcessor = this.window;
            if (j == 0) {
                getAndIncrement();
                unicastProcessor = UnicastProcessor.create(this.bufferSize, this);
                this.window = unicastProcessor;
                this.actual.onNext(unicastProcessor);
            }
            long j2 = j + 1;
            unicastProcessor.onNext(obj);
            if (j2 == this.size) {
                this.index = 0;
                this.window = null;
                unicastProcessor.onComplete();
                return;
            }
            this.index = j2;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                this.s.request(BackpressureHelper.multiplyCap(this.size, j));
            }
        }

        public void run() {
            if (decrementAndGet() == 0) {
                this.s.cancel();
            }
        }
    }

    final class WindowOverlapSubscriber extends AtomicInteger implements FlowableSubscriber, Subscription, Runnable {
        private static final long serialVersionUID = 2428527070996323976L;
        final Subscriber actual;
        final int bufferSize;
        volatile boolean cancelled;
        volatile boolean done;
        Throwable error;
        final AtomicBoolean firstRequest = new AtomicBoolean();
        long index;
        final AtomicBoolean once = new AtomicBoolean();
        long produced;
        final SpscLinkedArrayQueue queue;
        final AtomicLong requested = new AtomicLong();
        Subscription s;
        final long size;
        final long skip;
        final ArrayDeque windows = new ArrayDeque();
        final AtomicInteger wip = new AtomicInteger();

        WindowOverlapSubscriber(Subscriber subscriber, long j, long j2, int i) {
            super(1);
            this.actual = subscriber;
            this.size = j;
            this.skip = j2;
            this.queue = new SpscLinkedArrayQueue(i);
            this.bufferSize = i;
        }

        public void cancel() {
            this.cancelled = true;
            if (this.once.compareAndSet(false, true)) {
                run();
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean z, boolean z2, Subscriber subscriber, SpscLinkedArrayQueue spscLinkedArrayQueue) {
            if (this.cancelled) {
                spscLinkedArrayQueue.clear();
                return true;
            }
            if (z) {
                Throwable th = this.error;
                if (th != null) {
                    spscLinkedArrayQueue.clear();
                    subscriber.onError(th);
                    return true;
                } else if (z2) {
                    subscriber.onComplete();
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            int i;
            if (this.wip.getAndIncrement() == 0) {
                Subscriber subscriber = this.actual;
                SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
                int i2 = 1;
                do {
                    long j = this.requested.get();
                    long j2 = 0;
                    while (true) {
                        i = (j2 > j ? 1 : (j2 == j ? 0 : -1));
                        if (i == 0) {
                            break;
                        }
                        boolean z = this.done;
                        UnicastProcessor unicastProcessor = (UnicastProcessor) spscLinkedArrayQueue.poll();
                        boolean z2 = unicastProcessor == null;
                        if (!checkTerminated(z, z2, subscriber, spscLinkedArrayQueue)) {
                            if (z2) {
                                break;
                            }
                            subscriber.onNext(unicastProcessor);
                            j2++;
                        } else {
                            return;
                        }
                    }
                    if (i != 0 || !checkTerminated(this.done, spscLinkedArrayQueue.isEmpty(), subscriber, spscLinkedArrayQueue)) {
                        if (!(j2 == 0 || j == Long.MAX_VALUE)) {
                            this.requested.addAndGet(-j2);
                        }
                        i2 = this.wip.addAndGet(-i2);
                    } else {
                        return;
                    }
                } while (i2 != 0);
            }
        }

        public void onComplete() {
            if (!this.done) {
                Iterator it = this.windows.iterator();
                while (it.hasNext()) {
                    ((Processor) it.next()).onComplete();
                }
                this.windows.clear();
                this.done = true;
                drain();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            Iterator it = this.windows.iterator();
            while (it.hasNext()) {
                ((Processor) it.next()).onError(th);
            }
            this.windows.clear();
            this.error = th;
            this.done = true;
            drain();
        }

        public void onNext(Object obj) {
            if (!this.done) {
                long j = this.index;
                if (j == 0 && !this.cancelled) {
                    getAndIncrement();
                    UnicastProcessor create = UnicastProcessor.create(this.bufferSize, this);
                    this.windows.offer(create);
                    this.queue.offer(create);
                    drain();
                }
                long j2 = j + 1;
                Iterator it = this.windows.iterator();
                while (it.hasNext()) {
                    ((Processor) it.next()).onNext(obj);
                }
                long j3 = this.produced + 1;
                if (j3 == this.size) {
                    this.produced = j3 - this.skip;
                    Processor processor = (Processor) this.windows.poll();
                    if (processor != null) {
                        processor.onComplete();
                    }
                } else {
                    this.produced = j3;
                }
                if (j2 == this.skip) {
                    this.index = 0;
                } else {
                    this.index = j2;
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void request(long j) {
            long j2;
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                if (this.firstRequest.get() || !this.firstRequest.compareAndSet(false, true)) {
                    j2 = BackpressureHelper.multiplyCap(this.skip, j);
                } else {
                    j2 = BackpressureHelper.addCap(this.size, BackpressureHelper.multiplyCap(this.skip, j - 1));
                }
                this.s.request(j2);
                drain();
            }
        }

        public void run() {
            if (decrementAndGet() == 0) {
                this.s.cancel();
            }
        }
    }

    final class WindowSkipSubscriber extends AtomicInteger implements FlowableSubscriber, Subscription, Runnable {
        private static final long serialVersionUID = -8792836352386833856L;
        final Subscriber actual;
        final int bufferSize;
        final AtomicBoolean firstRequest = new AtomicBoolean();
        long index;
        final AtomicBoolean once = new AtomicBoolean();
        Subscription s;
        final long size;
        final long skip;
        UnicastProcessor window;

        WindowSkipSubscriber(Subscriber subscriber, long j, long j2, int i) {
            super(1);
            this.actual = subscriber;
            this.size = j;
            this.skip = j2;
            this.bufferSize = i;
        }

        public void cancel() {
            if (this.once.compareAndSet(false, true)) {
                run();
            }
        }

        public void onComplete() {
            UnicastProcessor unicastProcessor = this.window;
            if (unicastProcessor != null) {
                this.window = null;
                unicastProcessor.onComplete();
            }
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            UnicastProcessor unicastProcessor = this.window;
            if (unicastProcessor != null) {
                this.window = null;
                unicastProcessor.onError(th);
            }
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            long j = this.index;
            UnicastProcessor unicastProcessor = this.window;
            if (j == 0) {
                getAndIncrement();
                unicastProcessor = UnicastProcessor.create(this.bufferSize, this);
                this.window = unicastProcessor;
                this.actual.onNext(unicastProcessor);
            }
            long j2 = j + 1;
            if (unicastProcessor != null) {
                unicastProcessor.onNext(obj);
            }
            if (j2 == this.size) {
                this.window = null;
                unicastProcessor.onComplete();
            }
            if (j2 == this.skip) {
                this.index = 0;
            } else {
                this.index = j2;
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                long multiplyCap = (this.firstRequest.get() || !this.firstRequest.compareAndSet(false, true)) ? BackpressureHelper.multiplyCap(this.skip, j) : BackpressureHelper.addCap(BackpressureHelper.multiplyCap(this.size, j), BackpressureHelper.multiplyCap(this.skip - this.size, j - 1));
                this.s.request(multiplyCap);
            }
        }

        public void run() {
            if (decrementAndGet() == 0) {
                this.s.cancel();
            }
        }
    }

    public FlowableWindow(Flowable flowable, long j, long j2, int i) {
        super(flowable);
        this.size = j;
        this.skip = j2;
        this.bufferSize = i;
    }

    /* JADX WARNING: type inference failed for: r8v0, types: [io.reactivex.FlowableSubscriber] */
    /* JADX WARNING: type inference failed for: r0v2, types: [io.reactivex.internal.operators.flowable.FlowableWindow$WindowOverlapSubscriber] */
    /* JADX WARNING: type inference failed for: r0v3, types: [io.reactivex.internal.operators.flowable.FlowableWindow$WindowSkipSubscriber] */
    /* JADX WARNING: type inference failed for: r0v5, types: [io.reactivex.internal.operators.flowable.FlowableWindow$WindowOverlapSubscriber] */
    /* JADX WARNING: type inference failed for: r0v6, types: [io.reactivex.internal.operators.flowable.FlowableWindow$WindowSkipSubscriber] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v5, types: [io.reactivex.internal.operators.flowable.FlowableWindow$WindowOverlapSubscriber]
  assigns: [io.reactivex.internal.operators.flowable.FlowableWindow$WindowOverlapSubscriber, io.reactivex.internal.operators.flowable.FlowableWindow$WindowSkipSubscriber]
  uses: [io.reactivex.internal.operators.flowable.FlowableWindow$WindowOverlapSubscriber, io.reactivex.FlowableSubscriber, io.reactivex.internal.operators.flowable.FlowableWindow$WindowSkipSubscriber]
  mth insns count: 22
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
        ? r8;
        long j = this.skip;
        long j2 = this.size;
        if (j == j2) {
            this.source.subscribe((FlowableSubscriber) new WindowExactSubscriber(subscriber, j2, this.bufferSize));
            return;
        }
        int i = (j > j2 ? 1 : (j == j2 ? 0 : -1));
        Flowable flowable = this.source;
        if (i > 0) {
            ? windowSkipSubscriber = new WindowSkipSubscriber(subscriber, j2, j, this.bufferSize);
            r8 = windowSkipSubscriber;
        } else {
            ? windowOverlapSubscriber = new WindowOverlapSubscriber(subscriber, j2, j, this.bufferSize);
            r8 = windowOverlapSubscriber;
        }
        flowable.subscribe((FlowableSubscriber) r8);
    }
}
