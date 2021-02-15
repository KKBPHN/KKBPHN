package io.reactivex.internal.queue;

import io.reactivex.annotations.Nullable;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.util.Pow2;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class SpscArrayQueue extends AtomicReferenceArray implements SimplePlainQueue {
    private static final Integer MAX_LOOK_AHEAD_STEP = Integer.getInteger("jctools.spsc.max.lookahead.step", 4096);
    private static final long serialVersionUID = -1296597691183856449L;
    final AtomicLong consumerIndex = new AtomicLong();
    final int lookAheadStep;
    final int mask = (length() - 1);
    final AtomicLong producerIndex = new AtomicLong();
    long producerLookAhead;

    public SpscArrayQueue(int i) {
        super(Pow2.roundToPowerOfTwo(i));
        this.lookAheadStep = Math.min(i / 4, MAX_LOOK_AHEAD_STEP.intValue());
    }

    /* access modifiers changed from: 0000 */
    public int calcElementOffset(long j) {
        return this.mask & ((int) j);
    }

    /* access modifiers changed from: 0000 */
    public int calcElementOffset(long j, int i) {
        return ((int) j) & i;
    }

    public void clear() {
        while (true) {
            if (poll() == null) {
                if (isEmpty()) {
                    return;
                }
            }
        }
    }

    public boolean isEmpty() {
        return this.producerIndex.get() == this.consumerIndex.get();
    }

    /* access modifiers changed from: 0000 */
    public Object lvElement(int i) {
        return get(i);
    }

    public boolean offer(Object obj) {
        if (obj != null) {
            int i = this.mask;
            long j = this.producerIndex.get();
            int calcElementOffset = calcElementOffset(j, i);
            if (j >= this.producerLookAhead) {
                long j2 = ((long) this.lookAheadStep) + j;
                if (lvElement(calcElementOffset(j2, i)) == null) {
                    this.producerLookAhead = j2;
                } else if (lvElement(calcElementOffset) != null) {
                    return false;
                }
            }
            soElement(calcElementOffset, obj);
            soProducerIndex(j + 1);
            return true;
        }
        throw new NullPointerException("Null is not a valid element");
    }

    public boolean offer(Object obj, Object obj2) {
        return offer(obj) && offer(obj2);
    }

    @Nullable
    public Object poll() {
        long j = this.consumerIndex.get();
        int calcElementOffset = calcElementOffset(j);
        Object lvElement = lvElement(calcElementOffset);
        if (lvElement == null) {
            return null;
        }
        soConsumerIndex(j + 1);
        soElement(calcElementOffset, null);
        return lvElement;
    }

    /* access modifiers changed from: 0000 */
    public void soConsumerIndex(long j) {
        this.consumerIndex.lazySet(j);
    }

    /* access modifiers changed from: 0000 */
    public void soElement(int i, Object obj) {
        lazySet(i, obj);
    }

    /* access modifiers changed from: 0000 */
    public void soProducerIndex(long j) {
        this.producerIndex.lazySet(j);
    }
}
