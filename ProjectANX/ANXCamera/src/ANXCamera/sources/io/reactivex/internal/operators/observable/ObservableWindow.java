package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.subjects.UnicastSubject;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableWindow extends AbstractObservableWithUpstream {
    final int capacityHint;
    final long count;
    final long skip;

    final class WindowExactObserver extends AtomicInteger implements Observer, Disposable, Runnable {
        private static final long serialVersionUID = -7481782523886138128L;
        final Observer actual;
        volatile boolean cancelled;
        final int capacityHint;
        final long count;
        Disposable s;
        long size;
        UnicastSubject window;

        WindowExactObserver(Observer observer, long j, int i) {
            this.actual = observer;
            this.count = j;
            this.capacityHint = i;
        }

        public void dispose() {
            this.cancelled = true;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void onComplete() {
            UnicastSubject unicastSubject = this.window;
            if (unicastSubject != null) {
                this.window = null;
                unicastSubject.onComplete();
            }
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            UnicastSubject unicastSubject = this.window;
            if (unicastSubject != null) {
                this.window = null;
                unicastSubject.onError(th);
            }
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            UnicastSubject unicastSubject = this.window;
            if (unicastSubject == null && !this.cancelled) {
                unicastSubject = UnicastSubject.create(this.capacityHint, this);
                this.window = unicastSubject;
                this.actual.onNext(unicastSubject);
            }
            if (unicastSubject != null) {
                unicastSubject.onNext(obj);
                long j = this.size + 1;
                this.size = j;
                if (j >= this.count) {
                    this.size = 0;
                    this.window = null;
                    unicastSubject.onComplete();
                    if (this.cancelled) {
                        this.s.dispose();
                    }
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void run() {
            if (this.cancelled) {
                this.s.dispose();
            }
        }
    }

    final class WindowSkipObserver extends AtomicBoolean implements Observer, Disposable, Runnable {
        private static final long serialVersionUID = 3366976432059579510L;
        final Observer actual;
        volatile boolean cancelled;
        final int capacityHint;
        final long count;
        long firstEmission;
        long index;
        Disposable s;
        final long skip;
        final ArrayDeque windows;
        final AtomicInteger wip = new AtomicInteger();

        WindowSkipObserver(Observer observer, long j, long j2, int i) {
            this.actual = observer;
            this.count = j;
            this.skip = j2;
            this.capacityHint = i;
            this.windows = new ArrayDeque();
        }

        public void dispose() {
            this.cancelled = true;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void onComplete() {
            ArrayDeque arrayDeque = this.windows;
            while (!arrayDeque.isEmpty()) {
                ((UnicastSubject) arrayDeque.poll()).onComplete();
            }
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            ArrayDeque arrayDeque = this.windows;
            while (!arrayDeque.isEmpty()) {
                ((UnicastSubject) arrayDeque.poll()).onError(th);
            }
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            ArrayDeque arrayDeque = this.windows;
            long j = this.index;
            long j2 = this.skip;
            if (j % j2 == 0 && !this.cancelled) {
                this.wip.getAndIncrement();
                UnicastSubject create = UnicastSubject.create(this.capacityHint, this);
                arrayDeque.offer(create);
                this.actual.onNext(create);
            }
            long j3 = this.firstEmission + 1;
            Iterator it = arrayDeque.iterator();
            while (it.hasNext()) {
                ((UnicastSubject) it.next()).onNext(obj);
            }
            if (j3 >= this.count) {
                ((UnicastSubject) arrayDeque.poll()).onComplete();
                if (!arrayDeque.isEmpty() || !this.cancelled) {
                    j3 -= j2;
                } else {
                    this.s.dispose();
                    return;
                }
            }
            this.firstEmission = j3;
            this.index = j + 1;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void run() {
            if (this.wip.decrementAndGet() == 0 && this.cancelled) {
                this.s.dispose();
            }
        }
    }

    public ObservableWindow(ObservableSource observableSource, long j, long j2, int i) {
        super(observableSource);
        this.count = j;
        this.skip = j2;
        this.capacityHint = i;
    }

    public void subscribeActual(Observer observer) {
        long j = this.count;
        long j2 = this.skip;
        if (j == j2) {
            this.source.subscribe(new WindowExactObserver(observer, j, this.capacityHint));
            return;
        }
        ObservableSource observableSource = this.source;
        WindowSkipObserver windowSkipObserver = new WindowSkipObserver(observer, j, j2, this.capacityHint);
        observableSource.subscribe(windowSkipObserver);
    }
}
