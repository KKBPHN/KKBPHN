package com.android.camera.data.observeable;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.Lifecycle.State;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.camera.log.Log;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.EndConsumerHelper;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import java.util.concurrent.atomic.AtomicReference;

public class RxData {
    private static final String TAG = "RxLiveData";
    private Object data;
    private final Object dataLock;
    private Subject triggers;

    final class DataCheck {
        /* access modifiers changed from: private */
        public LifecycleOwner owner;
        private final Predicate predicateCheck = new Predicate() {
            public boolean test(Object obj) {
                return !RxData.isLifecycleState(DataCheck.this.owner, State.DESTROYED);
            }
        };

        DataCheck(LifecycleOwner lifecycleOwner) {
            this.owner = lifecycleOwner;
        }

        /* access modifiers changed from: 0000 */
        public Predicate getPredicateCheck() {
            return this.predicateCheck;
        }
    }

    public class DataObservable extends Observable implements LifecycleObserver {
        private final DataCheck dataCheck;
        private DataObserver dataObserver;
        private final Observable observable;

        DataObservable(Observable observable2, DataCheck dataCheck2) {
            this.observable = observable2;
            this.dataCheck = dataCheck2;
            if (dataCheck2.owner != null) {
                boolean access$100 = RxData.isLifecycleState(dataCheck2.owner, State.DESTROYED);
                String str = RxData.TAG;
                if (!access$100) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("DataObservable add:");
                    sb.append(dataCheck2.owner.getClass().getSimpleName());
                    Log.d(str, sb.toString());
                    dataCheck2.owner.getLifecycle().addObserver(this);
                    return;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("DataObservable skip:");
                sb2.append(dataCheck2.owner.getClass().getSimpleName());
                Log.d(str, sb2.toString());
            }
        }

        static Function toFunction(final DataCheck dataCheck2) {
            return new Function() {
                public DataObservable apply(Observable observable) {
                    return new DataObservable(observable, DataCheck.this);
                }
            };
        }

        @OnLifecycleEvent(Event.ON_DESTROY)
        public void onLifecycleDestroy() {
            DataObserver dataObserver2 = this.dataObserver;
            if (dataObserver2 != null && !dataObserver2.isDisposed()) {
                this.dataObserver.dispose();
            }
            if (this.dataCheck.owner != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("removeObserver: ");
                sb.append(this.dataCheck.owner.getClass().getSimpleName());
                Log.d(RxData.TAG, sb.toString());
                this.dataCheck.owner.getLifecycle().removeObserver(this);
            }
        }

        /* access modifiers changed from: protected */
        public void subscribeActual(Observer observer) {
            this.dataObserver = new DataObserver(observer);
            this.observable.subscribe((Observer) this.dataObserver);
            if (RxData.isLifecycleState(this.dataCheck.owner, State.DESTROYED)) {
                DataObserver dataObserver2 = this.dataObserver;
                if (dataObserver2 != null && !dataObserver2.isDisposed()) {
                    this.dataObserver.dispose();
                }
            }
        }
    }

    public class DataObserver implements Observer, Disposable {
        final Observer observer;
        final AtomicReference s = new AtomicReference();

        DataObserver(Observer observer2) {
            this.observer = observer2;
        }

        public final void dispose() {
            DisposableHelper.dispose(this.s);
        }

        public final boolean isDisposed() {
            return this.s.get() == DisposableHelper.DISPOSED;
        }

        public void onComplete() {
            this.observer.onComplete();
        }

        public void onError(Throwable th) {
            this.observer.onError(th);
        }

        public void onNext(Object obj) {
            this.observer.onNext(obj);
        }

        public final void onSubscribe(@NonNull Disposable disposable) {
            EndConsumerHelper.setOnce(this.s, disposable, DataObserver.class);
            this.observer.onSubscribe(disposable);
        }
    }

    public final class DataWrap {
        final Object data;

        public DataWrap(Object obj) {
            this.data = obj;
        }

        public Object get() {
            return this.data;
        }

        public boolean isNull() {
            return this.data == null;
        }
    }

    public RxData() {
        this.dataLock = new Object();
        this.triggers = PublishSubject.create();
    }

    public RxData(Object obj) {
        this();
        this.data = obj;
    }

    /* access modifiers changed from: private */
    public static boolean isLifecycleState(LifecycleOwner lifecycleOwner, @NonNull State state) {
        return lifecycleOwner != null && lifecycleOwner.getLifecycle().getCurrentState() == state;
    }

    private void notifyChangedInternal(Object obj) {
        this.triggers.onNext(new DataWrap(obj));
    }

    public Object get() {
        return this.data;
    }

    public void notifyChanged() {
        synchronized (this.dataLock) {
            notifyChangedInternal(this.data);
        }
    }

    public DataObservable observable(LifecycleOwner lifecycleOwner) {
        DataCheck dataCheck = new DataCheck(lifecycleOwner);
        return (DataObservable) this.triggers.startWith((Object) new DataWrap(this.data)).filter(dataCheck.getPredicateCheck()).to(DataObservable.toFunction(dataCheck));
    }

    public DataObservable observableNullLife() {
        return observable(null);
    }

    public void set(Object obj) {
        synchronized (this.dataLock) {
            this.data = obj;
            notifyChangedInternal(this.data);
        }
    }

    public void setSilently(Object obj) {
        synchronized (this.dataLock) {
            this.data = obj;
        }
    }
}
