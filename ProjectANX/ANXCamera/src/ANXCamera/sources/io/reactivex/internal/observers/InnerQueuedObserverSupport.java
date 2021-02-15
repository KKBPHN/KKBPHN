package io.reactivex.internal.observers;

public interface InnerQueuedObserverSupport {
    void drain();

    void innerComplete(InnerQueuedObserver innerQueuedObserver);

    void innerError(InnerQueuedObserver innerQueuedObserver, Throwable th);

    void innerNext(InnerQueuedObserver innerQueuedObserver, Object obj);
}
