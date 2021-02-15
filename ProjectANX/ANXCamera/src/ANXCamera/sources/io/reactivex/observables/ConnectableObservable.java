package io.reactivex.observables;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.operators.observable.ObservableAutoConnect;
import io.reactivex.internal.operators.observable.ObservableRefCount;
import io.reactivex.internal.util.ConnectConsumer;
import io.reactivex.plugins.RxJavaPlugins;

public abstract class ConnectableObservable extends Observable {
    @NonNull
    public Observable autoConnect() {
        return autoConnect(1);
    }

    @NonNull
    public Observable autoConnect(int i) {
        return autoConnect(i, Functions.emptyConsumer());
    }

    @NonNull
    public Observable autoConnect(int i, @NonNull Consumer consumer) {
        if (i > 0) {
            return RxJavaPlugins.onAssembly((Observable) new ObservableAutoConnect(this, i, consumer));
        }
        connect(consumer);
        return RxJavaPlugins.onAssembly(this);
    }

    public final Disposable connect() {
        ConnectConsumer connectConsumer = new ConnectConsumer();
        connect(connectConsumer);
        return connectConsumer.disposable;
    }

    public abstract void connect(@NonNull Consumer consumer);

    @NonNull
    public Observable refCount() {
        return RxJavaPlugins.onAssembly((Observable) new ObservableRefCount(this));
    }
}
