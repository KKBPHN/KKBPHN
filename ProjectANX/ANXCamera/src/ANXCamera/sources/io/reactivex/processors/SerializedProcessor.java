package io.reactivex.processors;

import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

final class SerializedProcessor extends FlowableProcessor {
    final FlowableProcessor actual;
    volatile boolean done;
    boolean emitting;
    AppendOnlyLinkedArrayList queue;

    SerializedProcessor(FlowableProcessor flowableProcessor) {
        this.actual = flowableProcessor;
    }

    /* access modifiers changed from: 0000 */
    public void emitLoop() {
        AppendOnlyLinkedArrayList appendOnlyLinkedArrayList;
        while (true) {
            synchronized (this) {
                appendOnlyLinkedArrayList = this.queue;
                if (appendOnlyLinkedArrayList == null) {
                    this.emitting = false;
                    return;
                }
                this.queue = null;
            }
            appendOnlyLinkedArrayList.accept((Subscriber) this.actual);
        }
        while (true) {
        }
    }

    public Throwable getThrowable() {
        return this.actual.getThrowable();
    }

    public boolean hasComplete() {
        return this.actual.hasComplete();
    }

    public boolean hasSubscribers() {
        return this.actual.hasSubscribers();
    }

    public boolean hasThrowable() {
        return this.actual.hasThrowable();
    }

    public void onComplete() {
        if (!this.done) {
            synchronized (this) {
                if (!this.done) {
                    this.done = true;
                    if (this.emitting) {
                        AppendOnlyLinkedArrayList appendOnlyLinkedArrayList = this.queue;
                        if (appendOnlyLinkedArrayList == null) {
                            appendOnlyLinkedArrayList = new AppendOnlyLinkedArrayList(4);
                            this.queue = appendOnlyLinkedArrayList;
                        }
                        appendOnlyLinkedArrayList.add(NotificationLite.complete());
                        return;
                    }
                    this.emitting = true;
                    this.actual.onComplete();
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002f, code lost:
        if (r0 == false) goto L_0x0035;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0031, code lost:
        io.reactivex.plugins.RxJavaPlugins.onError(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0034, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0035, code lost:
        r2.actual.onError(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x003a, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onError(Throwable th) {
        boolean z;
        if (this.done) {
            RxJavaPlugins.onError(th);
            return;
        }
        synchronized (this) {
            if (this.done) {
                z = true;
            } else {
                this.done = true;
                if (this.emitting) {
                    AppendOnlyLinkedArrayList appendOnlyLinkedArrayList = this.queue;
                    if (appendOnlyLinkedArrayList == null) {
                        appendOnlyLinkedArrayList = new AppendOnlyLinkedArrayList(4);
                        this.queue = appendOnlyLinkedArrayList;
                    }
                    appendOnlyLinkedArrayList.setFirst(NotificationLite.error(th));
                    return;
                }
                z = false;
                this.emitting = true;
            }
        }
    }

    public void onNext(Object obj) {
        if (!this.done) {
            synchronized (this) {
                if (!this.done) {
                    if (this.emitting) {
                        AppendOnlyLinkedArrayList appendOnlyLinkedArrayList = this.queue;
                        if (appendOnlyLinkedArrayList == null) {
                            appendOnlyLinkedArrayList = new AppendOnlyLinkedArrayList(4);
                            this.queue = appendOnlyLinkedArrayList;
                        }
                        NotificationLite.next(obj);
                        appendOnlyLinkedArrayList.add(obj);
                        return;
                    }
                    this.emitting = true;
                    this.actual.onNext(obj);
                    emitLoop();
                }
            }
        }
    }

    public void onSubscribe(Subscription subscription) {
        boolean z = true;
        if (!this.done) {
            synchronized (this) {
                if (!this.done) {
                    if (this.emitting) {
                        AppendOnlyLinkedArrayList appendOnlyLinkedArrayList = this.queue;
                        if (appendOnlyLinkedArrayList == null) {
                            appendOnlyLinkedArrayList = new AppendOnlyLinkedArrayList(4);
                            this.queue = appendOnlyLinkedArrayList;
                        }
                        appendOnlyLinkedArrayList.add(NotificationLite.subscription(subscription));
                        return;
                    }
                    this.emitting = true;
                    z = false;
                }
            }
        }
        if (z) {
            subscription.cancel();
        } else {
            this.actual.onSubscribe(subscription);
            emitLoop();
        }
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.actual.subscribe(subscriber);
    }
}
