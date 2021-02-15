package io.reactivex.internal.operators.flowable;

import io.reactivex.BackpressureOverflowStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Action;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableOnBackpressureBufferStrategy extends AbstractFlowableWithUpstream {
    final long bufferSize;
    final Action onOverflow;
    final BackpressureOverflowStrategy strategy;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableOnBackpressureBufferStrategy$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$io$reactivex$BackpressureOverflowStrategy = new int[BackpressureOverflowStrategy.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$io$reactivex$BackpressureOverflowStrategy[BackpressureOverflowStrategy.DROP_LATEST.ordinal()] = 1;
            $SwitchMap$io$reactivex$BackpressureOverflowStrategy[BackpressureOverflowStrategy.DROP_OLDEST.ordinal()] = 2;
        }
    }

    final class OnBackpressureBufferStrategySubscriber extends AtomicInteger implements FlowableSubscriber, Subscription {
        private static final long serialVersionUID = 3240706908776709697L;
        final Subscriber actual;
        final long bufferSize;
        volatile boolean cancelled;
        final Deque deque = new ArrayDeque();
        volatile boolean done;
        Throwable error;
        final Action onOverflow;
        final AtomicLong requested = new AtomicLong();
        Subscription s;
        final BackpressureOverflowStrategy strategy;

        OnBackpressureBufferStrategySubscriber(Subscriber subscriber, Action action, BackpressureOverflowStrategy backpressureOverflowStrategy, long j) {
            this.actual = subscriber;
            this.onOverflow = action;
            this.strategy = backpressureOverflowStrategy;
            this.bufferSize = j;
        }

        public void cancel() {
            this.cancelled = true;
            this.s.cancel();
            if (getAndIncrement() == 0) {
                clear(this.deque);
            }
        }

        /* access modifiers changed from: 0000 */
        public void clear(Deque deque2) {
            synchronized (deque2) {
                deque2.clear();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            int i;
            boolean isEmpty;
            Object poll;
            if (getAndIncrement() == 0) {
                Deque deque2 = this.deque;
                Subscriber subscriber = this.actual;
                int i2 = 1;
                do {
                    long j = this.requested.get();
                    long j2 = 0;
                    while (true) {
                        i = (j2 > j ? 1 : (j2 == j ? 0 : -1));
                        if (i == 0) {
                            break;
                        } else if (this.cancelled) {
                            clear(deque2);
                            return;
                        } else {
                            boolean z = this.done;
                            synchronized (deque2) {
                                poll = deque2.poll();
                            }
                            boolean z2 = poll == null;
                            if (z) {
                                Throwable th = this.error;
                                if (th != null) {
                                    clear(deque2);
                                    subscriber.onError(th);
                                    return;
                                } else if (z2) {
                                    subscriber.onComplete();
                                    return;
                                }
                            }
                            if (z2) {
                                break;
                            }
                            subscriber.onNext(poll);
                            j2++;
                        }
                    }
                    if (i == 0) {
                        if (this.cancelled) {
                            clear(deque2);
                            return;
                        }
                        boolean z3 = this.done;
                        synchronized (deque2) {
                            isEmpty = deque2.isEmpty();
                        }
                        if (z3) {
                            Throwable th2 = this.error;
                            if (th2 != null) {
                                clear(deque2);
                                subscriber.onError(th2);
                                return;
                            } else if (isEmpty) {
                                subscriber.onComplete();
                                return;
                            }
                        }
                    }
                    if (j2 != 0) {
                        BackpressureHelper.produced(this.requested, j2);
                    }
                    i2 = addAndGet(-i2);
                } while (i2 != 0);
            }
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.error = th;
            this.done = true;
            drain();
        }

        public void onNext(Object obj) {
            boolean z;
            boolean z2;
            MissingBackpressureException th;
            if (!this.done) {
                Deque deque2 = this.deque;
                synchronized (deque2) {
                    z = false;
                    z2 = true;
                    if (((long) deque2.size()) == this.bufferSize) {
                        int i = AnonymousClass1.$SwitchMap$io$reactivex$BackpressureOverflowStrategy[this.strategy.ordinal()];
                        if (i == 1) {
                            deque2.pollLast();
                        } else if (i == 2) {
                            deque2.poll();
                        }
                        deque2.offer(obj);
                        z2 = false;
                        z = true;
                    } else {
                        deque2.offer(obj);
                        z2 = false;
                    }
                }
                if (z) {
                    Action action = this.onOverflow;
                    if (action != null) {
                        try {
                            action.run();
                        } catch (Throwable th2) {
                            th = th2;
                            Exceptions.throwIfFatal(th);
                            this.s.cancel();
                        }
                    }
                } else if (z2) {
                    this.s.cancel();
                    th = new MissingBackpressureException();
                    onError(th);
                } else {
                    drain();
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(Long.MAX_VALUE);
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                drain();
            }
        }
    }

    public FlowableOnBackpressureBufferStrategy(Flowable flowable, long j, Action action, BackpressureOverflowStrategy backpressureOverflowStrategy) {
        super(flowable);
        this.bufferSize = j;
        this.onOverflow = action;
        this.strategy = backpressureOverflowStrategy;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        Flowable flowable = this.source;
        OnBackpressureBufferStrategySubscriber onBackpressureBufferStrategySubscriber = new OnBackpressureBufferStrategySubscriber(subscriber, this.onOverflow, this.strategy, this.bufferSize);
        flowable.subscribe((FlowableSubscriber) onBackpressureBufferStrategySubscriber);
    }
}
