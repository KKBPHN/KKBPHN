package io.reactivex.internal.operators.parallel;

import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFailureHandling;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelDoOnNextTry extends ParallelFlowable {
    final BiFunction errorHandler;
    final Consumer onNext;
    final ParallelFlowable source;

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelDoOnNextTry$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$io$reactivex$parallel$ParallelFailureHandling = new int[ParallelFailureHandling.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$io$reactivex$parallel$ParallelFailureHandling[ParallelFailureHandling.RETRY.ordinal()] = 1;
            $SwitchMap$io$reactivex$parallel$ParallelFailureHandling[ParallelFailureHandling.SKIP.ordinal()] = 2;
            try {
                $SwitchMap$io$reactivex$parallel$ParallelFailureHandling[ParallelFailureHandling.STOP.ordinal()] = 3;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    final class ParallelDoOnNextConditionalSubscriber implements ConditionalSubscriber, Subscription {
        final ConditionalSubscriber actual;
        boolean done;
        final BiFunction errorHandler;
        final Consumer onNext;
        Subscription s;

        ParallelDoOnNextConditionalSubscriber(ConditionalSubscriber conditionalSubscriber, Consumer consumer, BiFunction biFunction) {
            this.actual = conditionalSubscriber;
            this.onNext = consumer;
            this.errorHandler = biFunction;
        }

        public void cancel() {
            this.s.cancel();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!tryOnNext(obj) && !this.done) {
                this.s.request(1);
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void request(long j) {
            this.s.request(j);
        }

        /* JADX WARNING: Removed duplicated region for block: B:15:0x003a  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean tryOnNext(Object obj) {
            int i;
            if (this.done) {
                return false;
            }
            long j = 0;
            do {
                try {
                    this.onNext.accept(obj);
                    return this.actual.tryOnNext(obj);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    cancel();
                    onError(new CompositeException(th, th));
                    return false;
                }
            } while (i != 1);
            if (i != 2) {
                cancel();
                if (i != 3) {
                    onError(th);
                    return false;
                }
                onComplete();
            }
            return false;
        }
    }

    final class ParallelDoOnNextSubscriber implements ConditionalSubscriber, Subscription {
        final Subscriber actual;
        boolean done;
        final BiFunction errorHandler;
        final Consumer onNext;
        Subscription s;

        ParallelDoOnNextSubscriber(Subscriber subscriber, Consumer consumer, BiFunction biFunction) {
            this.actual = subscriber;
            this.onNext = consumer;
            this.errorHandler = biFunction;
        }

        public void cancel() {
            this.s.cancel();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!tryOnNext(obj)) {
                this.s.request(1);
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void request(long j) {
            this.s.request(j);
        }

        /* JADX WARNING: Removed duplicated region for block: B:16:0x0039  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean tryOnNext(Object obj) {
            int i;
            if (this.done) {
                return false;
            }
            long j = 0;
            do {
                try {
                    this.onNext.accept(obj);
                    this.actual.onNext(obj);
                    return true;
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    cancel();
                    onError(new CompositeException(th, th));
                    return false;
                }
            } while (i != 1);
            if (i != 2) {
                cancel();
                if (i != 3) {
                    onError(th);
                    return false;
                }
                onComplete();
            }
            return false;
        }
    }

    public ParallelDoOnNextTry(ParallelFlowable parallelFlowable, Consumer consumer, BiFunction biFunction) {
        this.source = parallelFlowable;
        this.onNext = consumer;
        this.errorHandler = biFunction;
    }

    public int parallelism() {
        return this.source.parallelism();
    }

    public void subscribe(Subscriber[] subscriberArr) {
        if (validate(subscriberArr)) {
            int length = subscriberArr.length;
            Subscriber[] subscriberArr2 = new Subscriber[length];
            for (int i = 0; i < length; i++) {
                ConditionalSubscriber conditionalSubscriber = subscriberArr[i];
                if (conditionalSubscriber instanceof ConditionalSubscriber) {
                    subscriberArr2[i] = new ParallelDoOnNextConditionalSubscriber(conditionalSubscriber, this.onNext, this.errorHandler);
                } else {
                    subscriberArr2[i] = new ParallelDoOnNextSubscriber(conditionalSubscriber, this.onNext, this.errorHandler);
                }
            }
            this.source.subscribe(subscriberArr2);
        }
    }
}
