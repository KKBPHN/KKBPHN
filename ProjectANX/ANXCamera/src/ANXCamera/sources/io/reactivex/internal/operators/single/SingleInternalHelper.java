package io.reactivex.internal.operators.single;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;

public final class SingleInternalHelper {

    enum NoSuchElementCallable implements Callable {
        INSTANCE;

        public NoSuchElementException call() {
            return new NoSuchElementException();
        }
    }

    enum ToFlowable implements Function {
        INSTANCE;

        public Publisher apply(SingleSource singleSource) {
            return new SingleToFlowable(singleSource);
        }
    }

    final class ToFlowableIterable implements Iterable {
        private final Iterable sources;

        ToFlowableIterable(Iterable iterable) {
            this.sources = iterable;
        }

        public Iterator iterator() {
            return new ToFlowableIterator(this.sources.iterator());
        }
    }

    final class ToFlowableIterator implements Iterator {
        private final Iterator sit;

        ToFlowableIterator(Iterator it) {
            this.sit = it;
        }

        public boolean hasNext() {
            return this.sit.hasNext();
        }

        public Flowable next() {
            return new SingleToFlowable((SingleSource) this.sit.next());
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    enum ToObservable implements Function {
        INSTANCE;

        public Observable apply(SingleSource singleSource) {
            return new SingleToObservable(singleSource);
        }
    }

    private SingleInternalHelper() {
        throw new IllegalStateException("No instances!");
    }

    public static Callable emptyThrower() {
        return NoSuchElementCallable.INSTANCE;
    }

    public static Iterable iterableToFlowable(Iterable iterable) {
        return new ToFlowableIterable(iterable);
    }

    public static Function toFlowable() {
        return ToFlowable.INSTANCE;
    }

    public static Function toObservable() {
        return ToObservable.INSTANCE;
    }
}
