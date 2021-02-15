package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableJoin extends AbstractFlowableWithUpstream {
    final Function leftEnd;
    final Publisher other;
    final BiFunction resultSelector;
    final Function rightEnd;

    final class JoinSubscription extends AtomicInteger implements Subscription, JoinSupport {
        static final Integer LEFT_CLOSE = Integer.valueOf(3);
        static final Integer LEFT_VALUE = Integer.valueOf(1);
        static final Integer RIGHT_CLOSE = Integer.valueOf(4);
        static final Integer RIGHT_VALUE = Integer.valueOf(2);
        private static final long serialVersionUID = -6071216598687999801L;
        final AtomicInteger active;
        final Subscriber actual;
        volatile boolean cancelled;
        final CompositeDisposable disposables = new CompositeDisposable();
        final AtomicReference error = new AtomicReference();
        final Function leftEnd;
        int leftIndex;
        final Map lefts = new LinkedHashMap();
        final SpscLinkedArrayQueue queue = new SpscLinkedArrayQueue(Flowable.bufferSize());
        final AtomicLong requested = new AtomicLong();
        final BiFunction resultSelector;
        final Function rightEnd;
        int rightIndex;
        final Map rights = new LinkedHashMap();

        JoinSubscription(Subscriber subscriber, Function function, Function function2, BiFunction biFunction) {
            this.actual = subscriber;
            this.leftEnd = function;
            this.rightEnd = function2;
            this.resultSelector = biFunction;
            this.active = new AtomicInteger(2);
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                cancelAll();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void cancelAll() {
            this.disposables.dispose();
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:52:0x00fa, code lost:
            if (r13 != 0) goto L_0x00fc;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:81:0x0194, code lost:
            if (r13 != 0) goto L_0x00fc;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            LeftRightEndSubscriber leftRightEndSubscriber;
            Map map;
            long j;
            if (getAndIncrement() == 0) {
                SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
                Subscriber subscriber = this.actual;
                boolean z = true;
                int i = 1;
                while (!this.cancelled) {
                    if (((Throwable) this.error.get()) != null) {
                        spscLinkedArrayQueue.clear();
                        cancelAll();
                        errorAll(subscriber);
                        return;
                    }
                    boolean z2 = this.active.get() == 0 ? z : false;
                    Integer num = (Integer) spscLinkedArrayQueue.poll();
                    boolean z3 = num == null ? z : false;
                    if (z2 && z3) {
                        this.lefts.clear();
                        this.rights.clear();
                        this.disposables.dispose();
                        subscriber.onComplete();
                        return;
                    } else if (z3) {
                        i = addAndGet(-i);
                        if (i == 0) {
                            return;
                        }
                    } else {
                        Object poll = spscLinkedArrayQueue.poll();
                        String str = "Could not emit value due to lack of requests";
                        String str2 = "The resultSelector returned a null value";
                        if (num == LEFT_VALUE) {
                            int i2 = this.leftIndex;
                            this.leftIndex = i2 + 1;
                            this.lefts.put(Integer.valueOf(i2), poll);
                            try {
                                Object apply = this.leftEnd.apply(poll);
                                ObjectHelper.requireNonNull(apply, "The leftEnd returned a null Publisher");
                                Publisher publisher = (Publisher) apply;
                                LeftRightEndSubscriber leftRightEndSubscriber2 = new LeftRightEndSubscriber(this, z, i2);
                                this.disposables.add(leftRightEndSubscriber2);
                                publisher.subscribe(leftRightEndSubscriber2);
                                if (((Throwable) this.error.get()) != null) {
                                    spscLinkedArrayQueue.clear();
                                    cancelAll();
                                    errorAll(subscriber);
                                    return;
                                }
                                long j2 = this.requested.get();
                                j = 0;
                                for (Object apply2 : this.rights.values()) {
                                    try {
                                        Object apply3 = this.resultSelector.apply(poll, apply2);
                                        ObjectHelper.requireNonNull(apply3, str2);
                                        if (j != j2) {
                                            subscriber.onNext(apply3);
                                            j++;
                                        } else {
                                            ExceptionHelper.addThrowable(this.error, new MissingBackpressureException(str));
                                            spscLinkedArrayQueue.clear();
                                            cancelAll();
                                            errorAll(subscriber);
                                            return;
                                        }
                                    } catch (Throwable th) {
                                        fail(th, subscriber, spscLinkedArrayQueue);
                                        return;
                                    }
                                }
                            } catch (Throwable th2) {
                                fail(th2, subscriber, spscLinkedArrayQueue);
                                return;
                            }
                        } else {
                            if (num == RIGHT_VALUE) {
                                int i3 = this.rightIndex;
                                this.rightIndex = i3 + 1;
                                this.rights.put(Integer.valueOf(i3), poll);
                                try {
                                    Object apply4 = this.rightEnd.apply(poll);
                                    ObjectHelper.requireNonNull(apply4, "The rightEnd returned a null Publisher");
                                    Publisher publisher2 = (Publisher) apply4;
                                    LeftRightEndSubscriber leftRightEndSubscriber3 = new LeftRightEndSubscriber(this, false, i3);
                                    this.disposables.add(leftRightEndSubscriber3);
                                    publisher2.subscribe(leftRightEndSubscriber3);
                                    if (((Throwable) this.error.get()) != null) {
                                        spscLinkedArrayQueue.clear();
                                        cancelAll();
                                        errorAll(subscriber);
                                        return;
                                    }
                                    long j3 = this.requested.get();
                                    long j4 = 0;
                                    for (Object apply5 : this.lefts.values()) {
                                        try {
                                            Object apply6 = this.resultSelector.apply(apply5, poll);
                                            ObjectHelper.requireNonNull(apply6, str2);
                                            if (j != j3) {
                                                subscriber.onNext(apply6);
                                                j4 = j + 1;
                                            } else {
                                                ExceptionHelper.addThrowable(this.error, new MissingBackpressureException(str));
                                                spscLinkedArrayQueue.clear();
                                                cancelAll();
                                                errorAll(subscriber);
                                                return;
                                            }
                                        } catch (Throwable th3) {
                                            fail(th3, subscriber, spscLinkedArrayQueue);
                                            return;
                                        }
                                    }
                                } catch (Throwable th4) {
                                    fail(th4, subscriber, spscLinkedArrayQueue);
                                    return;
                                }
                            } else {
                                if (num == LEFT_CLOSE) {
                                    leftRightEndSubscriber = (LeftRightEndSubscriber) poll;
                                    map = this.lefts;
                                } else if (num == RIGHT_CLOSE) {
                                    leftRightEndSubscriber = (LeftRightEndSubscriber) poll;
                                    map = this.rights;
                                }
                                map.remove(Integer.valueOf(leftRightEndSubscriber.index));
                                this.disposables.remove(leftRightEndSubscriber);
                            }
                            z = true;
                        }
                        BackpressureHelper.produced(this.requested, j);
                        z = true;
                    }
                }
                spscLinkedArrayQueue.clear();
            }
        }

        /* access modifiers changed from: 0000 */
        public void errorAll(Subscriber subscriber) {
            Throwable terminate = ExceptionHelper.terminate(this.error);
            this.lefts.clear();
            this.rights.clear();
            subscriber.onError(terminate);
        }

        /* access modifiers changed from: 0000 */
        public void fail(Throwable th, Subscriber subscriber, SimpleQueue simpleQueue) {
            Exceptions.throwIfFatal(th);
            ExceptionHelper.addThrowable(this.error, th);
            simpleQueue.clear();
            cancelAll();
            errorAll(subscriber);
        }

        public void innerClose(boolean z, LeftRightEndSubscriber leftRightEndSubscriber) {
            synchronized (this) {
                this.queue.offer(z ? LEFT_CLOSE : RIGHT_CLOSE, leftRightEndSubscriber);
            }
            drain();
        }

        public void innerCloseError(Throwable th) {
            if (ExceptionHelper.addThrowable(this.error, th)) {
                drain();
            } else {
                RxJavaPlugins.onError(th);
            }
        }

        public void innerComplete(LeftRightSubscriber leftRightSubscriber) {
            this.disposables.delete(leftRightSubscriber);
            this.active.decrementAndGet();
            drain();
        }

        public void innerError(Throwable th) {
            if (ExceptionHelper.addThrowable(this.error, th)) {
                this.active.decrementAndGet();
                drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void innerValue(boolean z, Object obj) {
            synchronized (this) {
                this.queue.offer(z ? LEFT_VALUE : RIGHT_VALUE, obj);
            }
            drain();
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
            }
        }
    }

    public FlowableJoin(Flowable flowable, Publisher publisher, Function function, Function function2, BiFunction biFunction) {
        super(flowable);
        this.other = publisher;
        this.leftEnd = function;
        this.rightEnd = function2;
        this.resultSelector = biFunction;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        JoinSubscription joinSubscription = new JoinSubscription(subscriber, this.leftEnd, this.rightEnd, this.resultSelector);
        subscriber.onSubscribe(joinSubscription);
        LeftRightSubscriber leftRightSubscriber = new LeftRightSubscriber(joinSubscription, true);
        joinSubscription.disposables.add(leftRightSubscriber);
        LeftRightSubscriber leftRightSubscriber2 = new LeftRightSubscriber(joinSubscription, false);
        joinSubscription.disposables.add(leftRightSubscriber2);
        this.source.subscribe((FlowableSubscriber) leftRightSubscriber);
        this.other.subscribe(leftRightSubscriber2);
    }
}
