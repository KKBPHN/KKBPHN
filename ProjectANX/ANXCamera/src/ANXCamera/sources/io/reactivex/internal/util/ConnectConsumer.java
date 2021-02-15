package io.reactivex.internal.util;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public final class ConnectConsumer implements Consumer {
    public Disposable disposable;

    public void accept(Disposable disposable2) {
        this.disposable = disposable2;
    }
}
