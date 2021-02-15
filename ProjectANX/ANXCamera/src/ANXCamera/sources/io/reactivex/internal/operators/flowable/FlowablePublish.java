package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.fuseable.HasUpstreamPublisher;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowablePublish extends ConnectableFlowable implements HasUpstreamPublisher {
    static final long CANCELLED = Long.MIN_VALUE;
    final int bufferSize;
    final AtomicReference current;
    final Publisher onSubscribe;
    final Flowable source;

    final class FlowablePublisher implements Publisher {
        private final int bufferSize;
        private final AtomicReference curr;

        FlowablePublisher(AtomicReference atomicReference, int i) {
            this.curr = atomicReference;
            this.bufferSize = i;
        }

        public void subscribe(Subscriber subscriber) {
            PublishSubscriber publishSubscriber;
            InnerSubscriber innerSubscriber = new InnerSubscriber(subscriber);
            subscriber.onSubscribe(innerSubscriber);
            while (true) {
                publishSubscriber = (PublishSubscriber) this.curr.get();
                if (publishSubscriber == null || publishSubscriber.isDisposed()) {
                    PublishSubscriber publishSubscriber2 = new PublishSubscriber(this.curr, this.bufferSize);
                    if (this.curr.compareAndSet(publishSubscriber, publishSubscriber2)) {
                        publishSubscriber = publishSubscriber2;
                    }
                }
                if (publishSubscriber.add(innerSubscriber)) {
                    break;
                }
            }
            if (innerSubscriber.get() == FlowablePublish.CANCELLED) {
                publishSubscriber.remove(innerSubscriber);
            } else {
                innerSubscriber.parent = publishSubscriber;
            }
            publishSubscriber.dispatch();
        }
    }

    final class InnerSubscriber extends AtomicLong implements Subscription {
        private static final long serialVersionUID = -4453897557930727610L;
        final Subscriber child;
        volatile PublishSubscriber parent;

        InnerSubscriber(Subscriber subscriber) {
            this.child = subscriber;
        }

        public void cancel() {
            if (get() != FlowablePublish.CANCELLED && getAndSet(FlowablePublish.CANCELLED) != FlowablePublish.CANCELLED) {
                PublishSubscriber publishSubscriber = this.parent;
                if (publishSubscriber != null) {
                    publishSubscriber.remove(this);
                    publishSubscriber.dispatch();
                }
            }
        }

        public long produced(long j) {
            return BackpressureHelper.producedCancel(this, j);
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.addCancel(this, j);
                PublishSubscriber publishSubscriber = this.parent;
                if (publishSubscriber != null) {
                    publishSubscriber.dispatch();
                }
            }
        }
    }

    final class PublishSubscriber extends AtomicInteger implements FlowableSubscriber, Disposable {
        static final InnerSubscriber[] EMPTY = new InnerSubscriber[0];
        static final InnerSubscriber[] TERMINATED = new InnerSubscriber[0];
        private static final long serialVersionUID = -202316842419149694L;
        final int bufferSize;
        final AtomicReference current;
        volatile SimpleQueue queue;
        final AtomicReference s = new AtomicReference();
        final AtomicBoolean shouldConnect;
        int sourceMode;
        final AtomicReference subscribers = new AtomicReference(EMPTY);
        volatile Object terminalEvent;

        PublishSubscriber(AtomicReference atomicReference, int i) {
            this.current = atomicReference;
            this.shouldConnect = new AtomicBoolean();
            this.bufferSize = i;
        }

        /* access modifiers changed from: 0000 */
        public boolean add(InnerSubscriber innerSubscriber) {
            InnerSubscriber[] innerSubscriberArr;
            InnerSubscriber[] innerSubscriberArr2;
            do {
                innerSubscriberArr = (InnerSubscriber[]) this.subscribers.get();
                if (innerSubscriberArr == TERMINATED) {
                    return false;
                }
                int length = innerSubscriberArr.length;
                innerSubscriberArr2 = new InnerSubscriber[(length + 1)];
                System.arraycopy(innerSubscriberArr, 0, innerSubscriberArr2, 0, length);
                innerSubscriberArr2[length] = innerSubscriber;
            } while (!this.subscribers.compareAndSet(innerSubscriberArr, innerSubscriberArr2));
            return true;
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(Object obj, boolean z) {
            int i = 0;
            if (obj != null) {
                if (!NotificationLite.isComplete(obj)) {
                    Throwable error = NotificationLite.getError(obj);
                    this.current.compareAndSet(this, null);
                    InnerSubscriber[] innerSubscriberArr = (InnerSubscriber[]) this.subscribers.getAndSet(TERMINATED);
                    if (innerSubscriberArr.length != 0) {
                        int length = innerSubscriberArr.length;
                        while (i < length) {
                            innerSubscriberArr[i].child.onError(error);
                            i++;
                        }
                    } else {
                        RxJavaPlugins.onError(error);
                    }
                    return true;
                } else if (z) {
                    this.current.compareAndSet(this, null);
                    InnerSubscriber[] innerSubscriberArr2 = (InnerSubscriber[]) this.subscribers.getAndSet(TERMINATED);
                    int length2 = innerSubscriberArr2.length;
                    while (i < length2) {
                        innerSubscriberArr2[i].child.onComplete();
                        i++;
                    }
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:72:0x0117, code lost:
            if (r16 == false) goto L_0x0119;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void dispatch() {
            int i;
            boolean z;
            long j;
            long j2;
            boolean z2;
            Object obj;
            Object obj2;
            Object obj3;
            if (getAndIncrement() == 0) {
                boolean z3 = true;
                int i2 = 1;
                while (true) {
                    Object obj4 = this.terminalEvent;
                    SimpleQueue simpleQueue = this.queue;
                    boolean z4 = (simpleQueue == null || simpleQueue.isEmpty()) ? z3 : false;
                    if (!checkTerminated(obj4, z4)) {
                        if (!z4) {
                            InnerSubscriber[] innerSubscriberArr = (InnerSubscriber[]) this.subscribers.get();
                            int length = innerSubscriberArr.length;
                            int length2 = innerSubscriberArr.length;
                            long j3 = Long.MAX_VALUE;
                            int i3 = 0;
                            int i4 = 0;
                            while (true) {
                                j = 0;
                                if (i3 >= length2) {
                                    break;
                                }
                                boolean z5 = z4;
                                long j4 = innerSubscriberArr[i3].get();
                                if (j4 >= 0) {
                                    j3 = Math.min(j3, j4);
                                } else if (j4 == FlowablePublish.CANCELLED) {
                                    i4++;
                                }
                                i3++;
                                z4 = z5;
                            }
                            boolean z6 = z4;
                            if (length == i4) {
                                Object obj5 = this.terminalEvent;
                                try {
                                    obj3 = simpleQueue.poll();
                                } catch (Throwable th) {
                                    Throwable th2 = th;
                                    Exceptions.throwIfFatal(th2);
                                    ((Subscription) this.s.get()).cancel();
                                    obj5 = NotificationLite.error(th2);
                                    this.terminalEvent = obj5;
                                    obj3 = null;
                                }
                                if (!checkTerminated(obj5, obj3 == null ? z3 : false)) {
                                    if (this.sourceMode != z3) {
                                        ((Subscription) this.s.get()).request(1);
                                    }
                                    z = z3;
                                    i = i2;
                                } else {
                                    return;
                                }
                            } else {
                                i = i2;
                                int i5 = 0;
                                while (true) {
                                    j2 = (long) i5;
                                    if (j2 >= j3) {
                                        break;
                                    }
                                    try {
                                        obj = this.terminalEvent;
                                        obj2 = simpleQueue.poll();
                                    } catch (Throwable th3) {
                                        Throwable th4 = th3;
                                        Exceptions.throwIfFatal(th4);
                                        ((Subscription) this.s.get()).cancel();
                                        Object error = NotificationLite.error(th4);
                                        this.terminalEvent = error;
                                        obj = error;
                                        obj2 = null;
                                    }
                                    boolean z7 = obj2 == null;
                                    if (!checkTerminated(obj, z7)) {
                                        if (z7) {
                                            z6 = z7;
                                            break;
                                        }
                                        NotificationLite.getValue(obj2);
                                        int length3 = innerSubscriberArr.length;
                                        int i6 = 0;
                                        while (i6 < length3) {
                                            InnerSubscriber innerSubscriber = innerSubscriberArr[i6];
                                            if (innerSubscriber.get() > j) {
                                                innerSubscriber.child.onNext(obj2);
                                                innerSubscriber.produced(1);
                                            }
                                            i6++;
                                            j = 0;
                                        }
                                        i5++;
                                        z6 = z7;
                                        j = 0;
                                    } else {
                                        return;
                                    }
                                }
                                if (i5 > 0) {
                                    z2 = true;
                                    if (this.sourceMode != 1) {
                                        ((Subscription) this.s.get()).request(j2);
                                    }
                                } else {
                                    z2 = true;
                                }
                                if (j3 != 0) {
                                }
                            }
                            z3 = z;
                            i2 = i;
                        } else {
                            z = z3;
                            i = i2;
                        }
                        i2 = addAndGet(-i);
                        if (i2 != 0) {
                            z3 = z;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }
        }

        public void dispose() {
            Object obj = this.subscribers.get();
            Object obj2 = TERMINATED;
            if (obj != obj2 && ((InnerSubscriber[]) this.subscribers.getAndSet(obj2)) != TERMINATED) {
                this.current.compareAndSet(this, null);
                SubscriptionHelper.cancel(this.s);
            }
        }

        public boolean isDisposed() {
            return this.subscribers.get() == TERMINATED;
        }

        public void onComplete() {
            if (this.terminalEvent == null) {
                this.terminalEvent = NotificationLite.complete();
                dispatch();
            }
        }

        public void onError(Throwable th) {
            if (this.terminalEvent == null) {
                this.terminalEvent = NotificationLite.error(th);
                dispatch();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onNext(Object obj) {
            if (this.sourceMode != 0 || this.queue.offer(obj)) {
                dispatch();
            } else {
                onError(new MissingBackpressureException("Prefetch queue is full?!"));
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this.s, subscription)) {
                if (subscription instanceof QueueSubscription) {
                    QueueSubscription queueSubscription = (QueueSubscription) subscription;
                    int requestFusion = queueSubscription.requestFusion(3);
                    if (requestFusion == 1) {
                        this.sourceMode = requestFusion;
                        this.queue = queueSubscription;
                        this.terminalEvent = NotificationLite.complete();
                        dispatch();
                        return;
                    } else if (requestFusion == 2) {
                        this.sourceMode = requestFusion;
                        this.queue = queueSubscription;
                        subscription.request((long) this.bufferSize);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.bufferSize);
                subscription.request((long) this.bufferSize);
            }
        }

        /* access modifiers changed from: 0000 */
        public void remove(InnerSubscriber innerSubscriber) {
            InnerSubscriber[] innerSubscriberArr;
            InnerSubscriber[] innerSubscriberArr2;
            do {
                innerSubscriberArr = (InnerSubscriber[]) this.subscribers.get();
                int length = innerSubscriberArr.length;
                if (length == 0) {
                    break;
                }
                int i = -1;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    } else if (innerSubscriberArr[i2].equals(innerSubscriber)) {
                        i = i2;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (i >= 0) {
                    if (length == 1) {
                        innerSubscriberArr2 = EMPTY;
                    } else {
                        InnerSubscriber[] innerSubscriberArr3 = new InnerSubscriber[(length - 1)];
                        System.arraycopy(innerSubscriberArr, 0, innerSubscriberArr3, 0, i);
                        System.arraycopy(innerSubscriberArr, i + 1, innerSubscriberArr3, i, (length - i) - 1);
                        innerSubscriberArr2 = innerSubscriberArr3;
                    }
                } else {
                    return;
                }
            } while (!this.subscribers.compareAndSet(innerSubscriberArr, innerSubscriberArr2));
        }
    }

    private FlowablePublish(Publisher publisher, Flowable flowable, AtomicReference atomicReference, int i) {
        this.onSubscribe = publisher;
        this.source = flowable;
        this.current = atomicReference;
        this.bufferSize = i;
    }

    public static ConnectableFlowable create(Flowable flowable, int i) {
        AtomicReference atomicReference = new AtomicReference();
        return RxJavaPlugins.onAssembly((ConnectableFlowable) new FlowablePublish(new FlowablePublisher(atomicReference, i), flowable, atomicReference, i));
    }

    public void connect(Consumer consumer) {
        PublishSubscriber publishSubscriber;
        while (true) {
            publishSubscriber = (PublishSubscriber) this.current.get();
            if (publishSubscriber != null && !publishSubscriber.isDisposed()) {
                break;
            }
            PublishSubscriber publishSubscriber2 = new PublishSubscriber(this.current, this.bufferSize);
            if (this.current.compareAndSet(publishSubscriber, publishSubscriber2)) {
                publishSubscriber = publishSubscriber2;
                break;
            }
        }
        boolean z = true;
        if (publishSubscriber.shouldConnect.get() || !publishSubscriber.shouldConnect.compareAndSet(false, true)) {
            z = false;
        }
        try {
            consumer.accept(publishSubscriber);
            if (z) {
                this.source.subscribe((FlowableSubscriber) publishSubscriber);
            }
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    public Publisher source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.onSubscribe.subscribe(subscriber);
    }
}
