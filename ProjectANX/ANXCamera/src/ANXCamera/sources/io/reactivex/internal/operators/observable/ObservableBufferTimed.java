package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Scheduler.Worker;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.QueueDrainObserver;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.util.QueueDrainHelper;
import io.reactivex.observers.SerializedObserver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableBufferTimed extends AbstractObservableWithUpstream {
    final Callable bufferSupplier;
    final int maxSize;
    final boolean restartTimerOnMaxSize;
    final Scheduler scheduler;
    final long timeskip;
    final long timespan;
    final TimeUnit unit;

    final class BufferExactBoundedObserver extends QueueDrainObserver implements Runnable, Disposable {
        Collection buffer;
        final Callable bufferSupplier;
        long consumerIndex;
        final int maxSize;
        long producerIndex;
        final boolean restartTimerOnMaxSize;
        Disposable s;
        Disposable timer;
        final long timespan;
        final TimeUnit unit;
        final Worker w;

        BufferExactBoundedObserver(Observer observer, Callable callable, long j, TimeUnit timeUnit, int i, boolean z, Worker worker) {
            super(observer, new MpscLinkedQueue());
            this.bufferSupplier = callable;
            this.timespan = j;
            this.unit = timeUnit;
            this.maxSize = i;
            this.restartTimerOnMaxSize = z;
            this.w = worker;
        }

        public void accept(Observer observer, Collection collection) {
            observer.onNext(collection);
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.s.dispose();
                this.w.dispose();
                synchronized (this) {
                    this.buffer = null;
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void onComplete() {
            Collection collection;
            this.w.dispose();
            synchronized (this) {
                collection = this.buffer;
                this.buffer = null;
            }
            this.queue.offer(collection);
            this.done = true;
            if (enter()) {
                QueueDrainHelper.drainLoop(this.queue, this.actual, false, this, this);
            }
        }

        public void onError(Throwable th) {
            synchronized (this) {
                this.buffer = null;
            }
            this.actual.onError(th);
            this.w.dispose();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0021, code lost:
            if (r7.restartTimerOnMaxSize == false) goto L_0x0028;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0023, code lost:
            r7.timer.dispose();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0028, code lost:
            fastPathOrderedEmit(r0, false, r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
            r8 = r7.bufferSupplier.call();
            io.reactivex.internal.functions.ObjectHelper.requireNonNull(r8, "The buffer supplied is null");
            r8 = (java.util.Collection) r8;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0039, code lost:
            monitor-enter(r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
            r7.buffer = r8;
            r7.consumerIndex++;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0041, code lost:
            monitor-exit(r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0044, code lost:
            if (r7.restartTimerOnMaxSize == false) goto L_0x0054;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0046, code lost:
            r0 = r7.w;
            r4 = r7.timespan;
            r7.timer = r0.schedulePeriodically(r7, r4, r4, r7.unit);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0054, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0058, code lost:
            r8 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0059, code lost:
            io.reactivex.exceptions.Exceptions.throwIfFatal(r8);
            r7.actual.onError(r8);
            dispose();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0064, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(Object obj) {
            synchronized (this) {
                Collection collection = this.buffer;
                if (collection != null) {
                    collection.add(obj);
                    if (collection.size() >= this.maxSize) {
                        this.buffer = null;
                        this.producerIndex++;
                    }
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                try {
                    Object call = this.bufferSupplier.call();
                    ObjectHelper.requireNonNull(call, "The buffer supplied is null");
                    this.buffer = (Collection) call;
                    this.actual.onSubscribe(this);
                    Worker worker = this.w;
                    long j = this.timespan;
                    this.timer = worker.schedulePeriodically(this, j, j, this.unit);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    disposable.dispose();
                    EmptyDisposable.error(th, this.actual);
                    this.w.dispose();
                }
            }
        }

        public void run() {
            try {
                Object call = this.bufferSupplier.call();
                ObjectHelper.requireNonNull(call, "The bufferSupplier returned a null buffer");
                Collection collection = (Collection) call;
                synchronized (this) {
                    Collection collection2 = this.buffer;
                    if (collection2 != null) {
                        if (this.producerIndex == this.consumerIndex) {
                            this.buffer = collection;
                            fastPathOrderedEmit(collection2, false, this);
                        }
                    }
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                dispose();
                this.actual.onError(th);
            }
        }
    }

    final class BufferExactUnboundedObserver extends QueueDrainObserver implements Runnable, Disposable {
        Collection buffer;
        final Callable bufferSupplier;
        Disposable s;
        final Scheduler scheduler;
        final AtomicReference timer = new AtomicReference();
        final long timespan;
        final TimeUnit unit;

        BufferExactUnboundedObserver(Observer observer, Callable callable, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            super(observer, new MpscLinkedQueue());
            this.bufferSupplier = callable;
            this.timespan = j;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
        }

        public void accept(Observer observer, Collection collection) {
            this.actual.onNext(collection);
        }

        public void dispose() {
            DisposableHelper.dispose(this.timer);
            this.s.dispose();
        }

        public boolean isDisposed() {
            return this.timer.get() == DisposableHelper.DISPOSED;
        }

        public void onComplete() {
            Collection collection;
            synchronized (this) {
                collection = this.buffer;
                this.buffer = null;
            }
            if (collection != null) {
                this.queue.offer(collection);
                this.done = true;
                if (enter()) {
                    QueueDrainHelper.drainLoop(this.queue, this.actual, false, null, this);
                }
            }
            DisposableHelper.dispose(this.timer);
        }

        public void onError(Throwable th) {
            synchronized (this) {
                this.buffer = null;
            }
            this.actual.onError(th);
            DisposableHelper.dispose(this.timer);
        }

        public void onNext(Object obj) {
            synchronized (this) {
                Collection collection = this.buffer;
                if (collection != null) {
                    collection.add(obj);
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                try {
                    Object call = this.bufferSupplier.call();
                    ObjectHelper.requireNonNull(call, "The buffer supplied is null");
                    this.buffer = (Collection) call;
                    this.actual.onSubscribe(this);
                    if (!this.cancelled) {
                        Scheduler scheduler2 = this.scheduler;
                        long j = this.timespan;
                        Disposable schedulePeriodicallyDirect = scheduler2.schedulePeriodicallyDirect(this, j, j, this.unit);
                        if (!this.timer.compareAndSet(null, schedulePeriodicallyDirect)) {
                            schedulePeriodicallyDirect.dispose();
                        }
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    dispose();
                    EmptyDisposable.error(th, this.actual);
                }
            }
        }

        public void run() {
            Collection collection;
            try {
                Object call = this.bufferSupplier.call();
                ObjectHelper.requireNonNull(call, "The bufferSupplier returned a null buffer");
                Collection collection2 = (Collection) call;
                synchronized (this) {
                    collection = this.buffer;
                    if (collection != null) {
                        this.buffer = collection2;
                    }
                }
                if (collection == null) {
                    DisposableHelper.dispose(this.timer);
                } else {
                    fastPathEmit(collection, false, this);
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.actual.onError(th);
                dispose();
            }
        }
    }

    final class BufferSkipBoundedObserver extends QueueDrainObserver implements Runnable, Disposable {
        final Callable bufferSupplier;
        final List buffers = new LinkedList();
        Disposable s;
        final long timeskip;
        final long timespan;
        final TimeUnit unit;
        final Worker w;

        final class RemoveFromBuffer implements Runnable {
            private final Collection b;

            RemoveFromBuffer(Collection collection) {
                this.b = collection;
            }

            public void run() {
                synchronized (BufferSkipBoundedObserver.this) {
                    BufferSkipBoundedObserver.this.buffers.remove(this.b);
                }
                BufferSkipBoundedObserver bufferSkipBoundedObserver = BufferSkipBoundedObserver.this;
                bufferSkipBoundedObserver.fastPathOrderedEmit(this.b, false, bufferSkipBoundedObserver.w);
            }
        }

        final class RemoveFromBufferEmit implements Runnable {
            private final Collection buffer;

            RemoveFromBufferEmit(Collection collection) {
                this.buffer = collection;
            }

            public void run() {
                synchronized (BufferSkipBoundedObserver.this) {
                    BufferSkipBoundedObserver.this.buffers.remove(this.buffer);
                }
                BufferSkipBoundedObserver bufferSkipBoundedObserver = BufferSkipBoundedObserver.this;
                bufferSkipBoundedObserver.fastPathOrderedEmit(this.buffer, false, bufferSkipBoundedObserver.w);
            }
        }

        BufferSkipBoundedObserver(Observer observer, Callable callable, long j, long j2, TimeUnit timeUnit, Worker worker) {
            super(observer, new MpscLinkedQueue());
            this.bufferSupplier = callable;
            this.timespan = j;
            this.timeskip = j2;
            this.unit = timeUnit;
            this.w = worker;
        }

        public void accept(Observer observer, Collection collection) {
            observer.onNext(collection);
        }

        /* access modifiers changed from: 0000 */
        public void clear() {
            synchronized (this) {
                this.buffers.clear();
            }
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                clear();
                this.s.dispose();
                this.w.dispose();
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void onComplete() {
            ArrayList<Collection> arrayList;
            synchronized (this) {
                arrayList = new ArrayList<>(this.buffers);
                this.buffers.clear();
            }
            for (Collection offer : arrayList) {
                this.queue.offer(offer);
            }
            this.done = true;
            if (enter()) {
                QueueDrainHelper.drainLoop(this.queue, this.actual, false, this.w, this);
            }
        }

        public void onError(Throwable th) {
            this.done = true;
            clear();
            this.actual.onError(th);
            this.w.dispose();
        }

        public void onNext(Object obj) {
            synchronized (this) {
                for (Collection add : this.buffers) {
                    add.add(obj);
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                try {
                    Object call = this.bufferSupplier.call();
                    ObjectHelper.requireNonNull(call, "The buffer supplied is null");
                    Collection collection = (Collection) call;
                    this.buffers.add(collection);
                    this.actual.onSubscribe(this);
                    Worker worker = this.w;
                    long j = this.timeskip;
                    worker.schedulePeriodically(this, j, j, this.unit);
                    this.w.schedule(new RemoveFromBufferEmit(collection), this.timespan, this.unit);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    disposable.dispose();
                    EmptyDisposable.error(th, this.actual);
                    this.w.dispose();
                }
            }
        }

        public void run() {
            if (!this.cancelled) {
                try {
                    Object call = this.bufferSupplier.call();
                    ObjectHelper.requireNonNull(call, "The bufferSupplier returned a null buffer");
                    Collection collection = (Collection) call;
                    synchronized (this) {
                        if (!this.cancelled) {
                            this.buffers.add(collection);
                            this.w.schedule(new RemoveFromBuffer(collection), this.timespan, this.unit);
                        }
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.actual.onError(th);
                    dispose();
                }
            }
        }
    }

    public ObservableBufferTimed(ObservableSource observableSource, long j, long j2, TimeUnit timeUnit, Scheduler scheduler2, Callable callable, int i, boolean z) {
        super(observableSource);
        this.timespan = j;
        this.timeskip = j2;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.bufferSupplier = callable;
        this.maxSize = i;
        this.restartTimerOnMaxSize = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        if (this.timespan == this.timeskip && this.maxSize == Integer.MAX_VALUE) {
            ObservableSource observableSource = this.source;
            BufferExactUnboundedObserver bufferExactUnboundedObserver = new BufferExactUnboundedObserver(new SerializedObserver(observer), this.bufferSupplier, this.timespan, this.unit, this.scheduler);
            observableSource.subscribe(bufferExactUnboundedObserver);
            return;
        }
        Worker createWorker = this.scheduler.createWorker();
        if (this.timespan == this.timeskip) {
            ObservableSource observableSource2 = this.source;
            BufferExactBoundedObserver bufferExactBoundedObserver = new BufferExactBoundedObserver(new SerializedObserver(observer), this.bufferSupplier, this.timespan, this.unit, this.maxSize, this.restartTimerOnMaxSize, createWorker);
            observableSource2.subscribe(bufferExactBoundedObserver);
            return;
        }
        ObservableSource observableSource3 = this.source;
        BufferSkipBoundedObserver bufferSkipBoundedObserver = new BufferSkipBoundedObserver(new SerializedObserver(observer), this.bufferSupplier, this.timespan, this.timeskip, this.unit, createWorker);
        observableSource3.subscribe(bufferSkipBoundedObserver);
    }
}
