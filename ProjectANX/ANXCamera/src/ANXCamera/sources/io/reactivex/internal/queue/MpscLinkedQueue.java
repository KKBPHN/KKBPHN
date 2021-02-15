package io.reactivex.internal.queue;

import io.reactivex.annotations.Nullable;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import java.util.concurrent.atomic.AtomicReference;

public final class MpscLinkedQueue implements SimplePlainQueue {
    private final AtomicReference consumerNode = new AtomicReference();
    private final AtomicReference producerNode = new AtomicReference();

    final class LinkedQueueNode extends AtomicReference {
        private static final long serialVersionUID = 2404266111789071508L;
        private Object value;

        LinkedQueueNode() {
        }

        LinkedQueueNode(Object obj) {
            spValue(obj);
        }

        public Object getAndNullValue() {
            Object lpValue = lpValue();
            spValue(null);
            return lpValue;
        }

        public Object lpValue() {
            return this.value;
        }

        public LinkedQueueNode lvNext() {
            return (LinkedQueueNode) get();
        }

        public void soNext(LinkedQueueNode linkedQueueNode) {
            lazySet(linkedQueueNode);
        }

        public void spValue(Object obj) {
            this.value = obj;
        }
    }

    public MpscLinkedQueue() {
        LinkedQueueNode linkedQueueNode = new LinkedQueueNode();
        spConsumerNode(linkedQueueNode);
        xchgProducerNode(linkedQueueNode);
    }

    public void clear() {
        while (poll() != null && !isEmpty()) {
        }
    }

    public boolean isEmpty() {
        return lvConsumerNode() == lvProducerNode();
    }

    /* access modifiers changed from: 0000 */
    public LinkedQueueNode lpConsumerNode() {
        return (LinkedQueueNode) this.consumerNode.get();
    }

    /* access modifiers changed from: 0000 */
    public LinkedQueueNode lvConsumerNode() {
        return (LinkedQueueNode) this.consumerNode.get();
    }

    /* access modifiers changed from: 0000 */
    public LinkedQueueNode lvProducerNode() {
        return (LinkedQueueNode) this.producerNode.get();
    }

    public boolean offer(Object obj) {
        if (obj != null) {
            LinkedQueueNode linkedQueueNode = new LinkedQueueNode(obj);
            xchgProducerNode(linkedQueueNode).soNext(linkedQueueNode);
            return true;
        }
        throw new NullPointerException("Null is not a valid element");
    }

    public boolean offer(Object obj, Object obj2) {
        offer(obj);
        offer(obj2);
        return true;
    }

    @Nullable
    public Object poll() {
        LinkedQueueNode lpConsumerNode = lpConsumerNode();
        LinkedQueueNode lvNext = lpConsumerNode.lvNext();
        if (lvNext == null) {
            if (lpConsumerNode != lvProducerNode()) {
                while (true) {
                    lvNext = lpConsumerNode.lvNext();
                    if (lvNext != null) {
                        break;
                    }
                }
            } else {
                return null;
            }
        }
        Object andNullValue = lvNext.getAndNullValue();
        spConsumerNode(lvNext);
        return andNullValue;
    }

    /* access modifiers changed from: 0000 */
    public void spConsumerNode(LinkedQueueNode linkedQueueNode) {
        this.consumerNode.lazySet(linkedQueueNode);
    }

    /* access modifiers changed from: 0000 */
    public LinkedQueueNode xchgProducerNode(LinkedQueueNode linkedQueueNode) {
        return (LinkedQueueNode) this.producerNode.getAndSet(linkedQueueNode);
    }
}
