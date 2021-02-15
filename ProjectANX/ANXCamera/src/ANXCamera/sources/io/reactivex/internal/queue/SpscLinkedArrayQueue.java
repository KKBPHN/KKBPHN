package io.reactivex.internal.queue;

import io.reactivex.annotations.Nullable;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.util.Pow2;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class SpscLinkedArrayQueue implements SimplePlainQueue {
    private static final Object HAS_NEXT = new Object();
    static final int MAX_LOOK_AHEAD_STEP = Integer.getInteger("jctools.spsc.max.lookahead.step", 4096).intValue();
    AtomicReferenceArray consumerBuffer;
    final AtomicLong consumerIndex = new AtomicLong();
    final int consumerMask;
    AtomicReferenceArray producerBuffer;
    final AtomicLong producerIndex = new AtomicLong();
    long producerLookAhead;
    int producerLookAheadStep;
    final int producerMask;

    public SpscLinkedArrayQueue(int i) {
        int roundToPowerOfTwo = Pow2.roundToPowerOfTwo(Math.max(8, i));
        int i2 = roundToPowerOfTwo - 1;
        AtomicReferenceArray atomicReferenceArray = new AtomicReferenceArray(roundToPowerOfTwo + 1);
        this.producerBuffer = atomicReferenceArray;
        this.producerMask = i2;
        adjustLookAheadStep(roundToPowerOfTwo);
        this.consumerBuffer = atomicReferenceArray;
        this.consumerMask = i2;
        this.producerLookAhead = (long) (i2 - 1);
        soProducerIndex(0);
    }

    private void adjustLookAheadStep(int i) {
        this.producerLookAheadStep = Math.min(i / 4, MAX_LOOK_AHEAD_STEP);
    }

    private static int calcDirectOffset(int i) {
        return i;
    }

    private static int calcWrappedOffset(long j, int i) {
        return ((int) j) & i;
    }

    private long lpConsumerIndex() {
        return this.consumerIndex.get();
    }

    private long lpProducerIndex() {
        return this.producerIndex.get();
    }

    private long lvConsumerIndex() {
        return this.consumerIndex.get();
    }

    private static Object lvElement(AtomicReferenceArray atomicReferenceArray, int i) {
        return atomicReferenceArray.get(i);
    }

    private AtomicReferenceArray lvNextBufferAndUnlink(AtomicReferenceArray atomicReferenceArray, int i) {
        AtomicReferenceArray atomicReferenceArray2 = (AtomicReferenceArray) lvElement(atomicReferenceArray, i);
        soElement(atomicReferenceArray, i, null);
        return atomicReferenceArray2;
    }

    private long lvProducerIndex() {
        return this.producerIndex.get();
    }

    private Object newBufferPeek(AtomicReferenceArray atomicReferenceArray, long j, int i) {
        this.consumerBuffer = atomicReferenceArray;
        return lvElement(atomicReferenceArray, calcWrappedOffset(j, i));
    }

    private Object newBufferPoll(AtomicReferenceArray atomicReferenceArray, long j, int i) {
        this.consumerBuffer = atomicReferenceArray;
        int calcWrappedOffset = calcWrappedOffset(j, i);
        Object lvElement = lvElement(atomicReferenceArray, calcWrappedOffset);
        if (lvElement != null) {
            soElement(atomicReferenceArray, calcWrappedOffset, null);
            soConsumerIndex(j + 1);
        }
        return lvElement;
    }

    private void resize(AtomicReferenceArray atomicReferenceArray, long j, int i, Object obj, long j2) {
        AtomicReferenceArray atomicReferenceArray2 = new AtomicReferenceArray(atomicReferenceArray.length());
        this.producerBuffer = atomicReferenceArray2;
        this.producerLookAhead = (j2 + j) - 1;
        soElement(atomicReferenceArray2, i, obj);
        soNext(atomicReferenceArray, atomicReferenceArray2);
        soElement(atomicReferenceArray, i, HAS_NEXT);
        soProducerIndex(j + 1);
    }

    private void soConsumerIndex(long j) {
        this.consumerIndex.lazySet(j);
    }

    private static void soElement(AtomicReferenceArray atomicReferenceArray, int i, Object obj) {
        atomicReferenceArray.lazySet(i, obj);
    }

    private void soNext(AtomicReferenceArray atomicReferenceArray, AtomicReferenceArray atomicReferenceArray2) {
        soElement(atomicReferenceArray, atomicReferenceArray.length() - 1, atomicReferenceArray2);
    }

    private void soProducerIndex(long j) {
        this.producerIndex.lazySet(j);
    }

    private boolean writeToQueue(AtomicReferenceArray atomicReferenceArray, Object obj, long j, int i) {
        soElement(atomicReferenceArray, i, obj);
        soProducerIndex(j + 1);
        return true;
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
        return lvProducerIndex() == lvConsumerIndex();
    }

    public boolean offer(Object obj) {
        if (obj != null) {
            AtomicReferenceArray atomicReferenceArray = this.producerBuffer;
            long lpProducerIndex = lpProducerIndex();
            int i = this.producerMask;
            int calcWrappedOffset = calcWrappedOffset(lpProducerIndex, i);
            if (lpProducerIndex < this.producerLookAhead) {
                return writeToQueue(atomicReferenceArray, obj, lpProducerIndex, calcWrappedOffset);
            }
            long j = ((long) this.producerLookAheadStep) + lpProducerIndex;
            if (lvElement(atomicReferenceArray, calcWrappedOffset(j, i)) == null) {
                this.producerLookAhead = j - 1;
                return writeToQueue(atomicReferenceArray, obj, lpProducerIndex, calcWrappedOffset);
            } else if (lvElement(atomicReferenceArray, calcWrappedOffset(1 + lpProducerIndex, i)) == null) {
                return writeToQueue(atomicReferenceArray, obj, lpProducerIndex, calcWrappedOffset);
            } else {
                resize(atomicReferenceArray, lpProducerIndex, calcWrappedOffset, obj, (long) i);
                return true;
            }
        } else {
            throw new NullPointerException("Null is not a valid element");
        }
    }

    public boolean offer(Object obj, Object obj2) {
        int i;
        AtomicReferenceArray atomicReferenceArray = this.producerBuffer;
        long lvProducerIndex = lvProducerIndex();
        int i2 = this.producerMask;
        long j = 2 + lvProducerIndex;
        if (lvElement(atomicReferenceArray, calcWrappedOffset(j, i2)) == null) {
            i = calcWrappedOffset(lvProducerIndex, i2);
            soElement(atomicReferenceArray, i + 1, obj2);
        } else {
            AtomicReferenceArray atomicReferenceArray2 = new AtomicReferenceArray(atomicReferenceArray.length());
            this.producerBuffer = atomicReferenceArray2;
            i = calcWrappedOffset(lvProducerIndex, i2);
            soElement(atomicReferenceArray2, i + 1, obj2);
            soElement(atomicReferenceArray2, i, obj);
            soNext(atomicReferenceArray, atomicReferenceArray2);
            obj = HAS_NEXT;
        }
        soElement(atomicReferenceArray, i, obj);
        soProducerIndex(j);
        return true;
    }

    public Object peek() {
        AtomicReferenceArray atomicReferenceArray = this.consumerBuffer;
        long lpConsumerIndex = lpConsumerIndex();
        int i = this.consumerMask;
        Object lvElement = lvElement(atomicReferenceArray, calcWrappedOffset(lpConsumerIndex, i));
        return lvElement == HAS_NEXT ? newBufferPeek(lvNextBufferAndUnlink(atomicReferenceArray, i + 1), lpConsumerIndex, i) : lvElement;
    }

    @Nullable
    public Object poll() {
        AtomicReferenceArray atomicReferenceArray = this.consumerBuffer;
        long lpConsumerIndex = lpConsumerIndex();
        int i = this.consumerMask;
        int calcWrappedOffset = calcWrappedOffset(lpConsumerIndex, i);
        Object lvElement = lvElement(atomicReferenceArray, calcWrappedOffset);
        boolean z = lvElement == HAS_NEXT;
        if (lvElement != null && !z) {
            soElement(atomicReferenceArray, calcWrappedOffset, null);
            soConsumerIndex(lpConsumerIndex + 1);
            return lvElement;
        } else if (z) {
            return newBufferPoll(lvNextBufferAndUnlink(atomicReferenceArray, i + 1), lpConsumerIndex, i);
        } else {
            return null;
        }
    }

    public int size() {
        long lvConsumerIndex = lvConsumerIndex();
        while (true) {
            long lvProducerIndex = lvProducerIndex();
            long lvConsumerIndex2 = lvConsumerIndex();
            if (lvConsumerIndex == lvConsumerIndex2) {
                return (int) (lvProducerIndex - lvConsumerIndex2);
            }
            lvConsumerIndex = lvConsumerIndex2;
        }
    }
}
