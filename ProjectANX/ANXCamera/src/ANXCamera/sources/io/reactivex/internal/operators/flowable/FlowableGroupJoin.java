package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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
import io.reactivex.processors.UnicastProcessor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableGroupJoin extends AbstractFlowableWithUpstream {
    final Function leftEnd;
    final Publisher other;
    final BiFunction resultSelector;
    final Function rightEnd;

    final class GroupJoinSubscription extends AtomicInteger implements Subscription, JoinSupport {
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

        GroupJoinSubscription(Subscriber subscriber, Function function, Function function2, BiFunction biFunction) {
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
        public void drain() {
            if (getAndIncrement() == 0) {
                SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
                Subscriber subscriber = this.actual;
                int i = 1;
                while (!this.cancelled) {
                    if (((Throwable) this.error.get()) != null) {
                        spscLinkedArrayQueue.clear();
                        cancelAll();
                        errorAll(subscriber);
                        return;
                    }
                    boolean z = this.active.get() == 0;
                    Integer num = (Integer) spscLinkedArrayQueue.poll();
                    boolean z2 = num == null;
                    if (z && z2) {
                        for (UnicastProcessor onComplete : this.lefts.values()) {
                            onComplete.onComplete();
                        }
                        this.lefts.clear();
                        this.rights.clear();
                        this.disposables.dispose();
                        subscriber.onComplete();
                        return;
                    } else if (z2) {
                        i = addAndGet(-i);
                        if (i == 0) {
                            return;
                        }
                    } else {
                        Object poll = spscLinkedArrayQueue.poll();
                        if (num == LEFT_VALUE) {
                            UnicastProcessor create = UnicastProcessor.create();
                            int i2 = this.leftIndex;
                            this.leftIndex = i2 + 1;
                            this.lefts.put(Integer.valueOf(i2), create);
                            try {
                                Object apply = this.leftEnd.apply(poll);
                                ObjectHelper.requireNonNull(apply, "The leftEnd returned a null Publisher");
                                Publisher publisher = (Publisher) apply;
                                LeftRightEndSubscriber leftRightEndSubscriber = new LeftRightEndSubscriber(this, true, i2);
                                this.disposables.add(leftRightEndSubscriber);
                                publisher.subscribe(leftRightEndSubscriber);
                                if (((Throwable) this.error.get()) != null) {
                                    spscLinkedArrayQueue.clear();
                                    cancelAll();
                                    errorAll(subscriber);
                                    return;
                                }
                                try {
                                    Object apply2 = this.resultSelector.apply(poll, create);
                                    ObjectHelper.requireNonNull(apply2, "The resultSelector returned a null value");
                                    if (this.requested.get() != 0) {
                                        subscriber.onNext(apply2);
                                        BackpressureHelper.produced(this.requested, 1);
                                        for (Object onNext : this.rights.values()) {
                                            create.onNext(onNext);
                                        }
                                    } else {
                                        fail(new MissingBackpressureException("Could not emit value due to lack of requests"), subscriber, spscLinkedArrayQueue);
                                        return;
                                    }
                                } catch (Throwable th) {
                                    fail(th, subscriber, spscLinkedArrayQueue);
                                    return;
                                }
                            } catch (Throwable th2) {
                                fail(th2, subscriber, spscLinkedArrayQueue);
                                return;
                            }
                        } else if (num == RIGHT_VALUE) {
                            int i3 = this.rightIndex;
                            this.rightIndex = i3 + 1;
                            this.rights.put(Integer.valueOf(i3), poll);
                            try {
                                Object apply3 = this.rightEnd.apply(poll);
                                ObjectHelper.requireNonNull(apply3, "The rightEnd returned a null Publisher");
                                Publisher publisher2 = (Publisher) apply3;
                                LeftRightEndSubscriber leftRightEndSubscriber2 = new LeftRightEndSubscriber(this, false, i3);
                                this.disposables.add(leftRightEndSubscriber2);
                                publisher2.subscribe(leftRightEndSubscriber2);
                                if (((Throwable) this.error.get()) != null) {
                                    spscLinkedArrayQueue.clear();
                                    cancelAll();
                                    errorAll(subscriber);
                                    return;
                                }
                                for (UnicastProcessor onNext2 : this.lefts.values()) {
                                    onNext2.onNext(poll);
                                }
                            } catch (Throwable th3) {
                                fail(th3, subscriber, spscLinkedArrayQueue);
                                return;
                            }
                        } else if (num == LEFT_CLOSE) {
                            LeftRightEndSubscriber leftRightEndSubscriber3 = (LeftRightEndSubscriber) poll;
                            UnicastProcessor unicastProcessor = (UnicastProcessor) this.lefts.remove(Integer.valueOf(leftRightEndSubscriber3.index));
                            this.disposables.remove(leftRightEndSubscriber3);
                            if (unicastProcessor != null) {
                                unicastProcessor.onComplete();
                            }
                        } else if (num == RIGHT_CLOSE) {
                            LeftRightEndSubscriber leftRightEndSubscriber4 = (LeftRightEndSubscriber) poll;
                            this.rights.remove(Integer.valueOf(leftRightEndSubscriber4.index));
                            this.disposables.remove(leftRightEndSubscriber4);
                        }
                    }
                }
                spscLinkedArrayQueue.clear();
            }
        }

        /* access modifiers changed from: 0000 */
        public void errorAll(Subscriber subscriber) {
            Throwable terminate = ExceptionHelper.terminate(this.error);
            for (UnicastProcessor onError : this.lefts.values()) {
                onError.onError(terminate);
            }
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

    interface JoinSupport {
        void innerClose(boolean z, LeftRightEndSubscriber leftRightEndSubscriber);

        void innerCloseError(Throwable th);

        void innerComplete(LeftRightSubscriber leftRightSubscriber);

        void innerError(Throwable th);

        void innerValue(boolean z, Object obj);
    }

    final class LeftRightEndSubscriber extends AtomicReference implements FlowableSubscriber, Disposable {
        private static final long serialVersionUID = 1883890389173668373L;
        final int index;
        final boolean isLeft;
        final JoinSupport parent;

        LeftRightEndSubscriber(JoinSupport joinSupport, boolean z, int i) {
            this.parent = joinSupport;
            this.isLeft = z;
            this.index = i;
        }

        public void dispose() {
            SubscriptionHelper.cancel(this);
        }

        public boolean isDisposed() {
            return SubscriptionHelper.isCancelled((Subscription) get());
        }

        public void onComplete() {
            this.parent.innerClose(this.isLeft, this);
        }

        public void onError(Throwable th) {
            this.parent.innerCloseError(th);
        }

        public void onNext(Object obj) {
            if (SubscriptionHelper.cancel(this)) {
                this.parent.innerClose(this.isLeft, this);
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this, subscription)) {
                subscription.request(Long.MAX_VALUE);
            }
        }
    }

    final class LeftRightSubscriber extends AtomicReference implements FlowableSubscriber, Disposable {
        private static final long serialVersionUID = 1883890389173668373L;
        final boolean isLeft;
        final JoinSupport parent;

        LeftRightSubscriber(JoinSupport joinSupport, boolean z) {
            this.parent = joinSupport;
            this.isLeft = z;
        }

        public void dispose() {
            SubscriptionHelper.cancel(this);
        }

        public boolean isDisposed() {
            return SubscriptionHelper.isCancelled((Subscription) get());
        }

        public void onComplete() {
            this.parent.innerComplete(this);
        }

        public void onError(Throwable th) {
            this.parent.innerError(th);
        }

        public void onNext(Object obj) {
            this.parent.innerValue(this.isLeft, obj);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this, subscription)) {
                subscription.request(Long.MAX_VALUE);
            }
        }
    }

    public FlowableGroupJoin(Flowable flowable, Publisher publisher, Function function, Function function2, BiFunction biFunction) {
        super(flowable);
        this.other = publisher;
        this.leftEnd = function;
        this.rightEnd = function2;
        this.resultSelector = biFunction;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        GroupJoinSubscription groupJoinSubscription = new GroupJoinSubscription(subscriber, this.leftEnd, this.rightEnd, this.resultSelector);
        subscriber.onSubscribe(groupJoinSubscription);
        LeftRightSubscriber leftRightSubscriber = new LeftRightSubscriber(groupJoinSubscription, true);
        groupJoinSubscription.disposables.add(leftRightSubscriber);
        LeftRightSubscriber leftRightSubscriber2 = new LeftRightSubscriber(groupJoinSubscription, false);
        groupJoinSubscription.disposables.add(leftRightSubscriber2);
        this.source.subscribe((FlowableSubscriber) leftRightSubscriber);
        this.other.subscribe(leftRightSubscriber2);
    }
}
