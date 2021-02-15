package com.xiaomi.camera.rx;

import android.os.Handler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SimpleDisposable implements Disposable {
    private final Handler mDisposeCallbackHandler;
    private final AtomicBoolean unsubscribed = new AtomicBoolean();

    public SimpleDisposable(Handler handler) {
        this.mDisposeCallbackHandler = handler;
    }

    public final void dispose() {
        if (this.unsubscribed.compareAndSet(false, true)) {
            Handler handler = this.mDisposeCallbackHandler;
            if (handler != null) {
                handler.post(new O00000o0(this));
            } else {
                AndroidSchedulers.mainThread().scheduleDirect(new O00000Oo(this));
            }
        }
    }

    public final boolean isDisposed() {
        return this.unsubscribed.get();
    }

    /* renamed from: onDispose */
    public abstract void Oo0o0o0();
}
