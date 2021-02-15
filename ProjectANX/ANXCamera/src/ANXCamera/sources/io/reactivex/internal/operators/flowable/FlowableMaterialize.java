package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Notification;
import io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;

public final class FlowableMaterialize extends AbstractFlowableWithUpstream {

    final class MaterializeSubscriber extends SinglePostCompleteSubscriber {
        private static final long serialVersionUID = -3740826063558713822L;

        MaterializeSubscriber(Subscriber subscriber) {
            super(subscriber);
        }

        public void onComplete() {
            complete(Notification.createOnComplete());
        }

        /* access modifiers changed from: protected */
        public void onDrop(Notification notification) {
            if (notification.isOnError()) {
                RxJavaPlugins.onError(notification.getError());
            }
        }

        public void onError(Throwable th) {
            complete(Notification.createOnError(th));
        }

        public void onNext(Object obj) {
            this.produced++;
            this.actual.onNext(Notification.createOnNext(obj));
        }
    }

    public FlowableMaterialize(Flowable flowable) {
        super(flowable);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new MaterializeSubscriber(subscriber));
    }
}
