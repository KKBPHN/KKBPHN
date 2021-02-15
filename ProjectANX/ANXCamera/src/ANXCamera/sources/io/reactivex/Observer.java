package io.reactivex;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public interface Observer {
    void onComplete();

    void onError(@NonNull Throwable th);

    void onNext(@NonNull Object obj);

    void onSubscribe(@NonNull Disposable disposable);
}
