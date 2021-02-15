package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.HasUpstreamObservableSource;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Timed;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableReplay extends ConnectableObservable implements HasUpstreamObservableSource, Disposable {
    static final BufferSupplier DEFAULT_UNBOUNDED_FACTORY = new UnBoundedFactory();
    final BufferSupplier bufferFactory;
    final AtomicReference current;
    final ObservableSource onSubscribe;
    final ObservableSource source;

    abstract class BoundedReplayBuffer extends AtomicReference implements ReplayBuffer {
        private static final long serialVersionUID = 2346567790059478686L;
        int size;
        Node tail;

        BoundedReplayBuffer() {
            Node node = new Node(null);
            this.tail = node;
            set(node);
        }

        /* access modifiers changed from: 0000 */
        public final void addLast(Node node) {
            this.tail.set(node);
            this.tail = node;
            this.size++;
        }

        /* access modifiers changed from: 0000 */
        public final void collect(Collection collection) {
            Node head = getHead();
            while (true) {
                head = (Node) head.get();
                if (head != null) {
                    Object leaveTransform = leaveTransform(head.value);
                    if (!NotificationLite.isComplete(leaveTransform) && !NotificationLite.isError(leaveTransform)) {
                        NotificationLite.getValue(leaveTransform);
                        collection.add(leaveTransform);
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        public final void complete() {
            addLast(new Node(enterTransform(NotificationLite.complete())));
            truncateFinal();
        }

        /* access modifiers changed from: 0000 */
        public Object enterTransform(Object obj) {
            return obj;
        }

        public final void error(Throwable th) {
            addLast(new Node(enterTransform(NotificationLite.error(th))));
            truncateFinal();
        }

        /* access modifiers changed from: 0000 */
        public Node getHead() {
            return (Node) get();
        }

        /* access modifiers changed from: 0000 */
        public boolean hasCompleted() {
            Object obj = this.tail.value;
            return obj != null && NotificationLite.isComplete(leaveTransform(obj));
        }

        /* access modifiers changed from: 0000 */
        public boolean hasError() {
            Object obj = this.tail.value;
            return obj != null && NotificationLite.isError(leaveTransform(obj));
        }

        /* access modifiers changed from: 0000 */
        public Object leaveTransform(Object obj) {
            return obj;
        }

        public final void next(Object obj) {
            NotificationLite.next(obj);
            addLast(new Node(enterTransform(obj)));
            truncate();
        }

        /* access modifiers changed from: 0000 */
        public final void removeFirst() {
            Node node = (Node) ((Node) get()).get();
            this.size--;
            setFirst(node);
        }

        /* access modifiers changed from: 0000 */
        public final void removeSome(int i) {
            Node node = (Node) get();
            while (i > 0) {
                node = (Node) node.get();
                i--;
                this.size--;
            }
            setFirst(node);
        }

        public final void replay(InnerDisposable innerDisposable) {
            if (innerDisposable.getAndIncrement() == 0) {
                int i = 1;
                do {
                    Node node = (Node) innerDisposable.index();
                    if (node == null) {
                        node = getHead();
                        innerDisposable.index = node;
                    }
                    while (!innerDisposable.isDisposed()) {
                        Node node2 = (Node) node.get();
                        if (node2 == null) {
                            innerDisposable.index = node;
                            i = innerDisposable.addAndGet(-i);
                        } else if (NotificationLite.accept(leaveTransform(node2.value), innerDisposable.child)) {
                            innerDisposable.index = null;
                            return;
                        } else {
                            node = node2;
                        }
                    }
                    return;
                } while (i != 0);
            }
        }

        /* access modifiers changed from: 0000 */
        public final void setFirst(Node node) {
            set(node);
        }

        public abstract void truncate();

        /* access modifiers changed from: 0000 */
        public void truncateFinal() {
        }
    }

    interface BufferSupplier {
        ReplayBuffer call();
    }

    final class DisposeConsumer implements Consumer {
        private final ObserverResourceWrapper srw;

        DisposeConsumer(ObserverResourceWrapper observerResourceWrapper) {
            this.srw = observerResourceWrapper;
        }

        public void accept(Disposable disposable) {
            this.srw.setResource(disposable);
        }
    }

    final class InnerDisposable extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = 2728361546769921047L;
        volatile boolean cancelled;
        final Observer child;
        Object index;
        final ReplayObserver parent;

        InnerDisposable(ReplayObserver replayObserver, Observer observer) {
            this.parent = replayObserver;
            this.child = observer;
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.parent.remove(this);
            }
        }

        /* access modifiers changed from: 0000 */
        public Object index() {
            return this.index;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }
    }

    final class MulticastReplay extends Observable {
        private final Callable connectableFactory;
        private final Function selector;

        MulticastReplay(Callable callable, Function function) {
            this.connectableFactory = callable;
            this.selector = function;
        }

        /* access modifiers changed from: protected */
        public void subscribeActual(Observer observer) {
            try {
                Object call = this.connectableFactory.call();
                ObjectHelper.requireNonNull(call, "The connectableFactory returned a null ConnectableObservable");
                ConnectableObservable connectableObservable = (ConnectableObservable) call;
                Object apply = this.selector.apply(connectableObservable);
                ObjectHelper.requireNonNull(apply, "The selector returned a null ObservableSource");
                ObservableSource observableSource = (ObservableSource) apply;
                ObserverResourceWrapper observerResourceWrapper = new ObserverResourceWrapper(observer);
                observableSource.subscribe(observerResourceWrapper);
                connectableObservable.connect(new DisposeConsumer(observerResourceWrapper));
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                EmptyDisposable.error(th, observer);
            }
        }
    }

    final class Node extends AtomicReference {
        private static final long serialVersionUID = 245354315435971818L;
        final Object value;

        Node(Object obj) {
            this.value = obj;
        }
    }

    final class Replay extends ConnectableObservable {
        private final ConnectableObservable co;
        private final Observable observable;

        Replay(ConnectableObservable connectableObservable, Observable observable2) {
            this.co = connectableObservable;
            this.observable = observable2;
        }

        public void connect(Consumer consumer) {
            this.co.connect(consumer);
        }

        /* access modifiers changed from: protected */
        public void subscribeActual(Observer observer) {
            this.observable.subscribe(observer);
        }
    }

    interface ReplayBuffer {
        void complete();

        void error(Throwable th);

        void next(Object obj);

        void replay(InnerDisposable innerDisposable);
    }

    final class ReplayBufferSupplier implements BufferSupplier {
        private final int bufferSize;

        ReplayBufferSupplier(int i) {
            this.bufferSize = i;
        }

        public ReplayBuffer call() {
            return new SizeBoundReplayBuffer(this.bufferSize);
        }
    }

    final class ReplayObserver extends AtomicReference implements Observer, Disposable {
        static final InnerDisposable[] EMPTY = new InnerDisposable[0];
        static final InnerDisposable[] TERMINATED = new InnerDisposable[0];
        private static final long serialVersionUID = -533785617179540163L;
        final ReplayBuffer buffer;
        boolean done;
        final AtomicReference observers = new AtomicReference(EMPTY);
        final AtomicBoolean shouldConnect = new AtomicBoolean();

        ReplayObserver(ReplayBuffer replayBuffer) {
            this.buffer = replayBuffer;
        }

        /* access modifiers changed from: 0000 */
        public boolean add(InnerDisposable innerDisposable) {
            InnerDisposable[] innerDisposableArr;
            InnerDisposable[] innerDisposableArr2;
            do {
                innerDisposableArr = (InnerDisposable[]) this.observers.get();
                if (innerDisposableArr == TERMINATED) {
                    return false;
                }
                int length = innerDisposableArr.length;
                innerDisposableArr2 = new InnerDisposable[(length + 1)];
                System.arraycopy(innerDisposableArr, 0, innerDisposableArr2, 0, length);
                innerDisposableArr2[length] = innerDisposable;
            } while (!this.observers.compareAndSet(innerDisposableArr, innerDisposableArr2));
            return true;
        }

        public void dispose() {
            this.observers.set(TERMINATED);
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return this.observers.get() == TERMINATED;
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.buffer.complete();
                replayFinal();
            }
        }

        public void onError(Throwable th) {
            if (!this.done) {
                this.done = true;
                this.buffer.error(th);
                replayFinal();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                this.buffer.next(obj);
                replay();
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.setOnce(this, disposable)) {
                replay();
            }
        }

        /* access modifiers changed from: 0000 */
        public void remove(InnerDisposable innerDisposable) {
            InnerDisposable[] innerDisposableArr;
            InnerDisposable[] innerDisposableArr2;
            do {
                innerDisposableArr = (InnerDisposable[]) this.observers.get();
                int length = innerDisposableArr.length;
                if (length != 0) {
                    int i = -1;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        } else if (innerDisposableArr[i2].equals(innerDisposable)) {
                            i = i2;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (i >= 0) {
                        if (length == 1) {
                            innerDisposableArr2 = EMPTY;
                        } else {
                            InnerDisposable[] innerDisposableArr3 = new InnerDisposable[(length - 1)];
                            System.arraycopy(innerDisposableArr, 0, innerDisposableArr3, 0, i);
                            System.arraycopy(innerDisposableArr, i + 1, innerDisposableArr3, i, (length - i) - 1);
                            innerDisposableArr2 = innerDisposableArr3;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!this.observers.compareAndSet(innerDisposableArr, innerDisposableArr2));
        }

        /* access modifiers changed from: 0000 */
        public void replay() {
            for (InnerDisposable replay : (InnerDisposable[]) this.observers.get()) {
                this.buffer.replay(replay);
            }
        }

        /* access modifiers changed from: 0000 */
        public void replayFinal() {
            for (InnerDisposable replay : (InnerDisposable[]) this.observers.getAndSet(TERMINATED)) {
                this.buffer.replay(replay);
            }
        }
    }

    final class ReplaySource implements ObservableSource {
        private final BufferSupplier bufferFactory;
        private final AtomicReference curr;

        ReplaySource(AtomicReference atomicReference, BufferSupplier bufferSupplier) {
            this.curr = atomicReference;
            this.bufferFactory = bufferSupplier;
        }

        public void subscribe(Observer observer) {
            ReplayObserver replayObserver;
            while (true) {
                replayObserver = (ReplayObserver) this.curr.get();
                if (replayObserver != null) {
                    break;
                }
                ReplayObserver replayObserver2 = new ReplayObserver(this.bufferFactory.call());
                if (this.curr.compareAndSet(null, replayObserver2)) {
                    replayObserver = replayObserver2;
                    break;
                }
            }
            InnerDisposable innerDisposable = new InnerDisposable(replayObserver, observer);
            observer.onSubscribe(innerDisposable);
            replayObserver.add(innerDisposable);
            if (innerDisposable.isDisposed()) {
                replayObserver.remove(innerDisposable);
            } else {
                replayObserver.buffer.replay(innerDisposable);
            }
        }
    }

    final class ScheduledReplaySupplier implements BufferSupplier {
        private final int bufferSize;
        private final long maxAge;
        private final Scheduler scheduler;
        private final TimeUnit unit;

        ScheduledReplaySupplier(int i, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            this.bufferSize = i;
            this.maxAge = j;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
        }

        public ReplayBuffer call() {
            SizeAndTimeBoundReplayBuffer sizeAndTimeBoundReplayBuffer = new SizeAndTimeBoundReplayBuffer(this.bufferSize, this.maxAge, this.unit, this.scheduler);
            return sizeAndTimeBoundReplayBuffer;
        }
    }

    final class SizeAndTimeBoundReplayBuffer extends BoundedReplayBuffer {
        private static final long serialVersionUID = 3457957419649567404L;
        final int limit;
        final long maxAge;
        final Scheduler scheduler;
        final TimeUnit unit;

        SizeAndTimeBoundReplayBuffer(int i, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            this.scheduler = scheduler2;
            this.limit = i;
            this.maxAge = j;
            this.unit = timeUnit;
        }

        /* access modifiers changed from: 0000 */
        public Object enterTransform(Object obj) {
            return new Timed(obj, this.scheduler.now(this.unit), this.unit);
        }

        /* access modifiers changed from: 0000 */
        public Node getHead() {
            Node node;
            long now = this.scheduler.now(this.unit) - this.maxAge;
            Node node2 = (Node) get();
            while (true) {
                node = node2;
                node2 = (Node) node2.get();
                if (node2 != null) {
                    Timed timed = (Timed) node2.value;
                    if (NotificationLite.isComplete(timed.value()) || NotificationLite.isError(timed.value()) || timed.time() > now) {
                        break;
                    }
                } else {
                    break;
                }
            }
            return node;
        }

        /* access modifiers changed from: 0000 */
        public Object leaveTransform(Object obj) {
            return ((Timed) obj).value();
        }

        /* access modifiers changed from: 0000 */
        public void truncate() {
            Node node;
            long now = this.scheduler.now(this.unit) - this.maxAge;
            Node node2 = (Node) get();
            Node node3 = (Node) node2.get();
            int i = 0;
            while (true) {
                Node node4 = node3;
                node = node2;
                node2 = node4;
                if (node2 == null) {
                    break;
                }
                int i2 = this.size;
                if (i2 <= this.limit) {
                    if (((Timed) node2.value).time() > now) {
                        break;
                    }
                    i++;
                    this.size--;
                } else {
                    i++;
                    this.size = i2 - 1;
                }
                node3 = (Node) node2.get();
            }
            if (i != 0) {
                setFirst(node);
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:9:0x003e  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void truncateFinal() {
            long now = this.scheduler.now(this.unit) - this.maxAge;
            Node node = (Node) get();
            Node node2 = (Node) node.get();
            int i = 0;
            while (true) {
                Node node3 = node2;
                Node node4 = node;
                node = node3;
                if (node != null && this.size > 1 && ((Timed) node.value).time() <= now) {
                    i++;
                    this.size--;
                    node2 = (Node) node.get();
                } else if (i == 0) {
                    setFirst(node4);
                    return;
                } else {
                    return;
                }
            }
            if (i == 0) {
            }
        }
    }

    final class SizeBoundReplayBuffer extends BoundedReplayBuffer {
        private static final long serialVersionUID = -5898283885385201806L;
        final int limit;

        SizeBoundReplayBuffer(int i) {
            this.limit = i;
        }

        /* access modifiers changed from: 0000 */
        public void truncate() {
            if (this.size > this.limit) {
                removeFirst();
            }
        }
    }

    final class UnBoundedFactory implements BufferSupplier {
        UnBoundedFactory() {
        }

        public ReplayBuffer call() {
            return new UnboundedReplayBuffer(16);
        }
    }

    final class UnboundedReplayBuffer extends ArrayList implements ReplayBuffer {
        private static final long serialVersionUID = 7063189396499112664L;
        volatile int size;

        UnboundedReplayBuffer(int i) {
            super(i);
        }

        public void complete() {
            add(NotificationLite.complete());
            this.size++;
        }

        public void error(Throwable th) {
            add(NotificationLite.error(th));
            this.size++;
        }

        public void next(Object obj) {
            NotificationLite.next(obj);
            add(obj);
            this.size++;
        }

        public void replay(InnerDisposable innerDisposable) {
            if (innerDisposable.getAndIncrement() == 0) {
                Observer observer = innerDisposable.child;
                int i = 1;
                while (!innerDisposable.isDisposed()) {
                    int i2 = this.size;
                    Integer num = (Integer) innerDisposable.index();
                    int intValue = num != null ? num.intValue() : 0;
                    while (intValue < i2) {
                        if (!NotificationLite.accept(get(intValue), observer) && !innerDisposable.isDisposed()) {
                            intValue++;
                        } else {
                            return;
                        }
                    }
                    innerDisposable.index = Integer.valueOf(intValue);
                    i = innerDisposable.addAndGet(-i);
                    if (i == 0) {
                        return;
                    }
                }
            }
        }
    }

    private ObservableReplay(ObservableSource observableSource, ObservableSource observableSource2, AtomicReference atomicReference, BufferSupplier bufferSupplier) {
        this.onSubscribe = observableSource;
        this.source = observableSource2;
        this.current = atomicReference;
        this.bufferFactory = bufferSupplier;
    }

    public static ConnectableObservable create(ObservableSource observableSource, int i) {
        return i == Integer.MAX_VALUE ? createFrom(observableSource) : create(observableSource, (BufferSupplier) new ReplayBufferSupplier(i));
    }

    public static ConnectableObservable create(ObservableSource observableSource, long j, TimeUnit timeUnit, Scheduler scheduler) {
        return create(observableSource, j, timeUnit, scheduler, Integer.MAX_VALUE);
    }

    public static ConnectableObservable create(ObservableSource observableSource, long j, TimeUnit timeUnit, Scheduler scheduler, int i) {
        ScheduledReplaySupplier scheduledReplaySupplier = new ScheduledReplaySupplier(i, j, timeUnit, scheduler);
        return create(observableSource, (BufferSupplier) scheduledReplaySupplier);
    }

    static ConnectableObservable create(ObservableSource observableSource, BufferSupplier bufferSupplier) {
        AtomicReference atomicReference = new AtomicReference();
        return RxJavaPlugins.onAssembly((ConnectableObservable) new ObservableReplay(new ReplaySource(atomicReference, bufferSupplier), observableSource, atomicReference, bufferSupplier));
    }

    public static ConnectableObservable createFrom(ObservableSource observableSource) {
        return create(observableSource, DEFAULT_UNBOUNDED_FACTORY);
    }

    public static Observable multicastSelector(Callable callable, Function function) {
        return RxJavaPlugins.onAssembly((Observable) new MulticastReplay(callable, function));
    }

    public static ConnectableObservable observeOn(ConnectableObservable connectableObservable, Scheduler scheduler) {
        return RxJavaPlugins.onAssembly((ConnectableObservable) new Replay(connectableObservable, connectableObservable.observeOn(scheduler)));
    }

    public void connect(Consumer consumer) {
        ReplayObserver replayObserver;
        while (true) {
            replayObserver = (ReplayObserver) this.current.get();
            if (replayObserver != null && !replayObserver.isDisposed()) {
                break;
            }
            ReplayObserver replayObserver2 = new ReplayObserver(this.bufferFactory.call());
            if (this.current.compareAndSet(replayObserver, replayObserver2)) {
                replayObserver = replayObserver2;
                break;
            }
        }
        boolean z = !replayObserver.shouldConnect.get() && replayObserver.shouldConnect.compareAndSet(false, true);
        try {
            consumer.accept(replayObserver);
            if (z) {
                this.source.subscribe(replayObserver);
            }
        } catch (Throwable th) {
            if (z) {
                replayObserver.shouldConnect.compareAndSet(true, false);
            }
            Exceptions.throwIfFatal(th);
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    public void dispose() {
        this.current.lazySet(null);
    }

    public boolean isDisposed() {
        Disposable disposable = (Disposable) this.current.get();
        return disposable == null || disposable.isDisposed();
    }

    public ObservableSource source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        this.onSubscribe.subscribe(observer);
    }
}
