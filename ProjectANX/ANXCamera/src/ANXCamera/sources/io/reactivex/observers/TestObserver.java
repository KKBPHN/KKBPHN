package io.reactivex.observers;

import com.android.camera.CameraIntentManager.ControlActions;
import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.QueueDisposable;
import io.reactivex.internal.util.ExceptionHelper;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class TestObserver extends BaseTestConsumer implements Observer, Disposable, MaybeObserver, SingleObserver, CompletableObserver {
    private final Observer actual;
    private QueueDisposable qs;
    private final AtomicReference subscription;

    enum EmptyObserver implements Observer {
        INSTANCE;

        public void onComplete() {
        }

        public void onError(Throwable th) {
        }

        public void onNext(Object obj) {
        }

        public void onSubscribe(Disposable disposable) {
        }
    }

    public TestObserver() {
        this(EmptyObserver.INSTANCE);
    }

    public TestObserver(Observer observer) {
        this.subscription = new AtomicReference();
        this.actual = observer;
    }

    public static TestObserver create() {
        return new TestObserver();
    }

    public static TestObserver create(Observer observer) {
        return new TestObserver(observer);
    }

    static String fusionModeToString(int i) {
        if (i == 0) {
            return ControlActions.CONTROL_ACTION_UNKNOWN;
        }
        if (i == 1) {
            return "SYNC";
        }
        if (i == 2) {
            return "ASYNC";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown(");
        sb.append(i);
        sb.append(")");
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    public final TestObserver assertFuseable() {
        if (this.qs != null) {
            return this;
        }
        throw new AssertionError("Upstream is not fuseable.");
    }

    /* access modifiers changed from: 0000 */
    public final TestObserver assertFusionMode(int i) {
        int i2 = this.establishedFusionMode;
        if (i2 == i) {
            return this;
        }
        if (this.qs != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fusion mode different. Expected: ");
            sb.append(fusionModeToString(i));
            sb.append(", actual: ");
            sb.append(fusionModeToString(i2));
            throw new AssertionError(sb.toString());
        }
        throw fail("Upstream is not fuseable");
    }

    /* access modifiers changed from: 0000 */
    public final TestObserver assertNotFuseable() {
        if (this.qs == null) {
            return this;
        }
        throw new AssertionError("Upstream is fuseable.");
    }

    public final TestObserver assertNotSubscribed() {
        if (this.subscription.get() != null) {
            throw fail("Subscribed!");
        } else if (this.errors.isEmpty()) {
            return this;
        } else {
            throw fail("Not subscribed but errors found");
        }
    }

    public final TestObserver assertOf(Consumer consumer) {
        try {
            consumer.accept(this);
            return this;
        } catch (Throwable th) {
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    public final TestObserver assertSubscribed() {
        if (this.subscription.get() != null) {
            return this;
        }
        throw fail("Not subscribed!");
    }

    public final void cancel() {
        dispose();
    }

    public final void dispose() {
        DisposableHelper.dispose(this.subscription);
    }

    public final boolean hasSubscription() {
        return this.subscription.get() != null;
    }

    public final boolean isCancelled() {
        return isDisposed();
    }

    public final boolean isDisposed() {
        return DisposableHelper.isDisposed((Disposable) this.subscription.get());
    }

    public void onComplete() {
        if (!this.checkSubscriptionOnce) {
            this.checkSubscriptionOnce = true;
            if (this.subscription.get() == null) {
                this.errors.add(new IllegalStateException("onSubscribe not called in proper order"));
            }
        }
        try {
            this.lastThread = Thread.currentThread();
            this.completions++;
            this.actual.onComplete();
        } finally {
            this.done.countDown();
        }
    }

    public void onError(Throwable th) {
        if (!this.checkSubscriptionOnce) {
            this.checkSubscriptionOnce = true;
            if (this.subscription.get() == null) {
                this.errors.add(new IllegalStateException("onSubscribe not called in proper order"));
            }
        }
        try {
            this.lastThread = Thread.currentThread();
            if (th == null) {
                this.errors.add(new NullPointerException("onError received a null Throwable"));
            } else {
                this.errors.add(th);
            }
            this.actual.onError(th);
        } finally {
            this.done.countDown();
        }
    }

    public void onNext(Object obj) {
        if (!this.checkSubscriptionOnce) {
            this.checkSubscriptionOnce = true;
            if (this.subscription.get() == null) {
                this.errors.add(new IllegalStateException("onSubscribe not called in proper order"));
            }
        }
        this.lastThread = Thread.currentThread();
        if (this.establishedFusionMode == 2) {
            while (true) {
                try {
                    Object poll = this.qs.poll();
                    if (poll == null) {
                        break;
                    }
                    this.values.add(poll);
                } catch (Throwable th) {
                    this.errors.add(th);
                    this.qs.dispose();
                }
            }
            return;
        }
        this.values.add(obj);
        if (obj == null) {
            this.errors.add(new NullPointerException("onNext received a null value"));
        }
        this.actual.onNext(obj);
    }

    public void onSubscribe(Disposable disposable) {
        this.lastThread = Thread.currentThread();
        if (disposable == null) {
            this.errors.add(new NullPointerException("onSubscribe received a null Subscription"));
        } else if (!this.subscription.compareAndSet(null, disposable)) {
            disposable.dispose();
            if (this.subscription.get() != DisposableHelper.DISPOSED) {
                List list = this.errors;
                StringBuilder sb = new StringBuilder();
                sb.append("onSubscribe received multiple subscriptions: ");
                sb.append(disposable);
                list.add(new IllegalStateException(sb.toString()));
            }
        } else {
            int i = this.initialFusionMode;
            if (i != 0 && (disposable instanceof QueueDisposable)) {
                this.qs = (QueueDisposable) disposable;
                int requestFusion = this.qs.requestFusion(i);
                this.establishedFusionMode = requestFusion;
                if (requestFusion == 1) {
                    this.checkSubscriptionOnce = true;
                    this.lastThread = Thread.currentThread();
                    while (true) {
                        try {
                            Object poll = this.qs.poll();
                            if (poll == null) {
                                break;
                            }
                            this.values.add(poll);
                        } catch (Throwable th) {
                            this.errors.add(th);
                        }
                    }
                    this.completions++;
                    this.subscription.lazySet(DisposableHelper.DISPOSED);
                    return;
                }
            }
            this.actual.onSubscribe(disposable);
        }
    }

    public void onSuccess(Object obj) {
        onNext(obj);
        onComplete();
    }

    /* access modifiers changed from: 0000 */
    public final TestObserver setInitialFusionMode(int i) {
        this.initialFusionMode = i;
        return this;
    }
}
