package io.reactivex.flowables;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.operators.flowable.FlowableAutoConnect;
import io.reactivex.internal.operators.flowable.FlowableRefCount;
import io.reactivex.internal.util.ConnectConsumer;
import io.reactivex.plugins.RxJavaPlugins;

public abstract class ConnectableFlowable extends Flowable {
    @NonNull
    public Flowable autoConnect() {
        return autoConnect(1);
    }

    @NonNull
    public Flowable autoConnect(int i) {
        return autoConnect(i, Functions.emptyConsumer());
    }

    @NonNull
    public Flowable autoConnect(int i, @NonNull Consumer consumer) {
        if (i > 0) {
            return RxJavaPlugins.onAssembly((Flowable) new FlowableAutoConnect(this, i, consumer));
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
    public Flowable refCount() {
        return RxJavaPlugins.onAssembly((Flowable) new FlowableRefCount(this));
    }
}
