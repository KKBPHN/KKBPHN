package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observers.SerializedObserver;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableSampleTimed extends AbstractObservableWithUpstream {
    final boolean emitLast;
    final long period;
    final Scheduler scheduler;
    final TimeUnit unit;

    final class SampleTimedEmitLast extends SampleTimedObserver {
        private static final long serialVersionUID = -7139995637533111443L;
        final AtomicInteger wip = new AtomicInteger(1);

        SampleTimedEmitLast(Observer observer, long j, TimeUnit timeUnit, Scheduler scheduler) {
            super(observer, j, timeUnit, scheduler);
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

    final class SampleTimedNoLast extends SampleTimedObserver {
        private static final long serialVersionUID = -7139995637533111443L;

        SampleTimedNoLast(Observer observer, long j, TimeUnit timeUnit, Scheduler scheduler) {
            super(observer, j, timeUnit, scheduler);
        }

        /* access modifiers changed from: 0000 */
        public void complete() {
            this.actual.onComplete();
        }

        public void run() {
            emit();
        }
    }

    abstract class SampleTimedObserver extends AtomicReference implements Observer, Disposable, Runnable {
        private static final long serialVersionUID = -3517602651313910099L;
        final Observer actual;
        final long period;
        Disposable s;
        final Scheduler scheduler;
        final AtomicReference timer = new AtomicReference();
        final TimeUnit unit;

        SampleTimedObserver(Observer observer, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            this.actual = observer;
            this.period = j;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
        }

        /* access modifiers changed from: 0000 */
        public void cancelTimer() {
            DisposableHelper.dispose(this.timer);
        }

        public abstract void complete();

        public void dispose() {
            cancelTimer();
            this.s.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void emit() {
            Object andSet = getAndSet(null);
            if (andSet != null) {
                this.actual.onNext(andSet);
            }
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
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

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
                Scheduler scheduler2 = this.scheduler;
                long j = this.period;
                DisposableHelper.replace(this.timer, scheduler2.schedulePeriodicallyDirect(this, j, j, this.unit));
            }
        }
    }

    public ObservableSampleTimed(ObservableSource observableSource, long j, TimeUnit timeUnit, Scheduler scheduler2, boolean z) {
        super(observableSource);
        this.period = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.emitLast = z;
    }

    /* JADX WARNING: type inference failed for: r6v0, types: [io.reactivex.Observer] */
    /* JADX WARNING: type inference failed for: r0v0, types: [io.reactivex.internal.operators.observable.ObservableSampleTimed$SampleTimedNoLast] */
    /* JADX WARNING: type inference failed for: r0v1, types: [io.reactivex.internal.operators.observable.ObservableSampleTimed$SampleTimedEmitLast] */
    /* JADX WARNING: type inference failed for: r0v2, types: [io.reactivex.internal.operators.observable.ObservableSampleTimed$SampleTimedNoLast] */
    /* JADX WARNING: type inference failed for: r0v3, types: [io.reactivex.internal.operators.observable.ObservableSampleTimed$SampleTimedEmitLast] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v2, types: [io.reactivex.internal.operators.observable.ObservableSampleTimed$SampleTimedNoLast]
  assigns: [io.reactivex.internal.operators.observable.ObservableSampleTimed$SampleTimedNoLast, io.reactivex.internal.operators.observable.ObservableSampleTimed$SampleTimedEmitLast]
  uses: [io.reactivex.internal.operators.observable.ObservableSampleTimed$SampleTimedNoLast, io.reactivex.Observer, io.reactivex.internal.operators.observable.ObservableSampleTimed$SampleTimedEmitLast]
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
    public void subscribeActual(Observer observer) {
        ObservableSource observableSource;
        ? r6;
        SerializedObserver serializedObserver = new SerializedObserver(observer);
        if (this.emitLast) {
            observableSource = this.source;
            ? sampleTimedEmitLast = new SampleTimedEmitLast(serializedObserver, this.period, this.unit, this.scheduler);
            r6 = sampleTimedEmitLast;
        } else {
            observableSource = this.source;
            ? sampleTimedNoLast = new SampleTimedNoLast(serializedObserver, this.period, this.unit, this.scheduler);
            r6 = sampleTimedNoLast;
        }
        observableSource.subscribe(r6);
    }
}
