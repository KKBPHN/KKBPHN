package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableZip extends Flowable {
    final int bufferSize;
    final boolean delayError;
    final Publisher[] sources;
    final Iterable sourcesIterable;
    final Function zipper;

    final class ZipCoordinator extends AtomicInteger implements Subscription {
        private static final long serialVersionUID = -2434867452883857743L;
        final Subscriber actual;
        volatile boolean cancelled;
        final Object[] current;
        final boolean delayErrors;
        final AtomicThrowable errors;
        final AtomicLong requested;
        final ZipSubscriber[] subscribers;
        final Function zipper;

        ZipCoordinator(Subscriber subscriber, Function function, int i, int i2, boolean z) {
            this.actual = subscriber;
            this.zipper = function;
            this.delayErrors = z;
            ZipSubscriber[] zipSubscriberArr = new ZipSubscriber[i];
            for (int i3 = 0; i3 < i; i3++) {
                zipSubscriberArr[i3] = new ZipSubscriber(this, i2);
            }
            this.current = new Object[i];
            this.subscribers = zipSubscriberArr;
            this.requested = new AtomicLong();
            this.errors = new AtomicThrowable();
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                cancelAll();
            }
        }

        /* access modifiers changed from: 0000 */
        public void cancelAll() {
            for (ZipSubscriber cancel : this.subscribers) {
                cancel.cancel();
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x0092, code lost:
            if (r0 == false) goto L_0x0095;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
            r0 = r1.zipper.apply(r5.clone());
            io.reactivex.internal.functions.ObjectHelper.requireNonNull(r0, "The zipper returned a null value");
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x00af, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x00b0, code lost:
            io.reactivex.exceptions.Exceptions.throwIfFatal(r0);
            cancelAll();
            r1.errors.addThrowable(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x00bd, code lost:
            if (r14 != 0) goto L_0x0124;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x00c1, code lost:
            if (r1.cancelled == false) goto L_0x00c4;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:0x00c3, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x00c6, code lost:
            if (r1.delayErrors != false) goto L_0x00d2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x00ce, code lost:
            if (r1.errors.get() == null) goto L_0x00d2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:57:0x00d2, code lost:
            r6 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:0x00d4, code lost:
            if (r6 >= r4) goto L_0x0124;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x00d6, code lost:
            r0 = r3[r6];
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:0x00da, code lost:
            if (r5[r6] != null) goto L_0x0121;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:62:?, code lost:
            r10 = r0.done;
            r0 = r0.queue;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x00e0, code lost:
            if (r0 == null) goto L_0x00e7;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:64:0x00e2, code lost:
            r0 = r0.poll();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:65:0x00e7, code lost:
            r0 = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:66:0x00e8, code lost:
            if (r0 != null) goto L_0x00ec;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:67:0x00ea, code lost:
            r11 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:0x00ec, code lost:
            r11 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:69:0x00ee, code lost:
            if (r10 == false) goto L_0x010d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:70:0x00f0, code lost:
            if (r11 == false) goto L_0x010d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:71:0x00f2, code lost:
            cancelAll();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:72:0x00fd, code lost:
            if (((java.lang.Throwable) r1.errors.get()) == null) goto L_0x0109;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:73:0x00ff, code lost:
            r2.onError(r1.errors.terminate());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:74:0x0109, code lost:
            r2.onComplete();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:75:0x010c, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:76:0x010d, code lost:
            if (r11 != false) goto L_0x0121;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:77:0x010f, code lost:
            r5[r6] = r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:78:0x0112, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:79:0x0113, code lost:
            io.reactivex.exceptions.Exceptions.throwIfFatal(r0);
            r1.errors.addThrowable(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:80:0x011d, code lost:
            if (r1.delayErrors == false) goto L_0x0031;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:81:0x0121, code lost:
            r6 = r6 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:83:0x0128, code lost:
            if (r12 == 0) goto L_0x0146;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:84:0x012a, code lost:
            r0 = r3.length;
            r6 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:85:0x012d, code lost:
            if (r6 >= r0) goto L_0x0137;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:86:0x012f, code lost:
            r3[r6].request(r12);
            r6 = r6 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:88:0x013e, code lost:
            if (r8 == Long.MAX_VALUE) goto L_0x0146;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:89:0x0140, code lost:
            r1.requested.addAndGet(-r12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:90:0x0146, code lost:
            r7 = addAndGet(-r7);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            Object apply;
            if (getAndIncrement() == 0) {
                Subscriber subscriber = this.actual;
                ZipSubscriber[] zipSubscriberArr = this.subscribers;
                int length = zipSubscriberArr.length;
                Object[] objArr = this.current;
                int i = 1;
                loop0:
                do {
                    long j = this.requested.get();
                    long j2 = 0;
                    while (true) {
                        int i2 = (j > j2 ? 1 : (j == j2 ? 0 : -1));
                        if (i2 != 0) {
                            if (!this.cancelled) {
                                if (!this.delayErrors && this.errors.get() != null) {
                                    break loop0;
                                }
                                boolean z = false;
                                int i3 = 0;
                                while (true) {
                                    if (i3 >= length) {
                                        break;
                                    }
                                    ZipSubscriber zipSubscriber = zipSubscriberArr[i3];
                                    if (objArr[i3] == null) {
                                        try {
                                            boolean z2 = zipSubscriber.done;
                                            SimpleQueue simpleQueue = zipSubscriber.queue;
                                            Object poll = simpleQueue != null ? simpleQueue.poll() : null;
                                            boolean z3 = poll == null;
                                            if (!z2 || !z3) {
                                                if (!z3) {
                                                    objArr[i3] = poll;
                                                }
                                                z = true;
                                            } else {
                                                cancelAll();
                                                if (((Throwable) this.errors.get()) != null) {
                                                    subscriber.onError(this.errors.terminate());
                                                } else {
                                                    subscriber.onComplete();
                                                }
                                                return;
                                            }
                                        } catch (Throwable th) {
                                            Exceptions.throwIfFatal(th);
                                            this.errors.addThrowable(th);
                                            if (!this.delayErrors) {
                                                break loop0;
                                            }
                                        }
                                    }
                                    i3++;
                                }
                            } else {
                                return;
                            }
                        } else {
                            break;
                        }
                        subscriber.onNext(apply);
                        j2++;
                        Arrays.fill(objArr, null);
                    }
                    cancelAll();
                    subscriber.onError(this.errors.terminate());
                    return;
                } while (i != 0);
            }
        }

        /* access modifiers changed from: 0000 */
        public void error(ZipSubscriber zipSubscriber, Throwable th) {
            if (this.errors.addThrowable(th)) {
                zipSubscriber.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                drain();
            }
        }

        /* access modifiers changed from: 0000 */
        public void subscribe(Publisher[] publisherArr, int i) {
            ZipSubscriber[] zipSubscriberArr = this.subscribers;
            int i2 = 0;
            while (i2 < i && !this.cancelled) {
                if (this.delayErrors || this.errors.get() == null) {
                    publisherArr[i2].subscribe(zipSubscriberArr[i2]);
                    i2++;
                } else {
                    return;
                }
            }
        }
    }

    final class ZipSubscriber extends AtomicReference implements FlowableSubscriber, Subscription {
        private static final long serialVersionUID = -4627193790118206028L;
        volatile boolean done;
        final int limit;
        final ZipCoordinator parent;
        final int prefetch;
        long produced;
        SimpleQueue queue;
        int sourceMode;

        ZipSubscriber(ZipCoordinator zipCoordinator, int i) {
            this.parent = zipCoordinator;
            this.prefetch = i;
            this.limit = i - (i >> 2);
        }

        public void cancel() {
            SubscriptionHelper.cancel(this);
        }

        public void onComplete() {
            this.done = true;
            this.parent.drain();
        }

        public void onError(Throwable th) {
            this.parent.error(this, th);
        }

        public void onNext(Object obj) {
            if (this.sourceMode != 2) {
                this.queue.offer(obj);
            }
            this.parent.drain();
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this, subscription)) {
                if (subscription instanceof QueueSubscription) {
                    QueueSubscription queueSubscription = (QueueSubscription) subscription;
                    int requestFusion = queueSubscription.requestFusion(7);
                    if (requestFusion == 1) {
                        this.sourceMode = requestFusion;
                        this.queue = queueSubscription;
                        this.done = true;
                        this.parent.drain();
                        return;
                    } else if (requestFusion == 2) {
                        this.sourceMode = requestFusion;
                        this.queue = queueSubscription;
                        subscription.request((long) this.prefetch);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.prefetch);
                subscription.request((long) this.prefetch);
            }
        }

        public void request(long j) {
            if (this.sourceMode != 1) {
                long j2 = this.produced + j;
                if (j2 >= ((long) this.limit)) {
                    this.produced = 0;
                    ((Subscription) get()).request(j2);
                    return;
                }
                this.produced = j2;
            }
        }
    }

    public FlowableZip(Publisher[] publisherArr, Iterable iterable, Function function, int i, boolean z) {
        this.sources = publisherArr;
        this.sourcesIterable = iterable;
        this.zipper = function;
        this.bufferSize = i;
        this.delayError = z;
    }

    public void subscribeActual(Subscriber subscriber) {
        int i;
        Publisher[] publisherArr = this.sources;
        if (publisherArr == null) {
            publisherArr = new Publisher[8];
            i = 0;
            for (Publisher publisher : this.sourcesIterable) {
                if (i == publisherArr.length) {
                    Publisher[] publisherArr2 = new Publisher[((i >> 2) + i)];
                    System.arraycopy(publisherArr, 0, publisherArr2, 0, i);
                    publisherArr = publisherArr2;
                }
                int i2 = i + 1;
                publisherArr[i] = publisher;
                i = i2;
            }
        } else {
            i = publisherArr.length;
        }
        if (i == 0) {
            EmptySubscription.complete(subscriber);
            return;
        }
        ZipCoordinator zipCoordinator = new ZipCoordinator(subscriber, this.zipper, i, this.bufferSize, this.delayError);
        subscriber.onSubscribe(zipCoordinator);
        zipCoordinator.subscribe(publisherArr, i);
    }
}
