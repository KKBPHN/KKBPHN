package io.reactivex.internal.subscribers;

public interface InnerQueuedSubscriberSupport {
    void drain();

    void innerComplete(InnerQueuedSubscriber innerQueuedSubscriber);

    void innerError(InnerQueuedSubscriber innerQueuedSubscriber, Throwable th);

    void innerNext(InnerQueuedSubscriber innerQueuedSubscriber, Object obj);
}
