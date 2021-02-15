package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ObservableBuffer extends AbstractObservableWithUpstream {
    final Callable bufferSupplier;
    final int count;
    final int skip;

    final class BufferExactObserver implements Observer, Disposable {
        final Observer actual;
        Collection buffer;
        final Callable bufferSupplier;
        final int count;
        Disposable s;
        int size;

        BufferExactObserver(Observer observer, int i, Callable callable) {
            this.actual = observer;
            this.count = i;
            this.bufferSupplier = callable;
        }

        /* access modifiers changed from: 0000 */
        public boolean createBuffer() {
            try {
                Object call = this.bufferSupplier.call();
                ObjectHelper.requireNonNull(call, "Empty buffer supplied");
                this.buffer = (Collection) call;
                return true;
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.buffer = null;
                Disposable disposable = this.s;
                if (disposable == null) {
                    EmptyDisposable.error(th, this.actual);
                } else {
                    disposable.dispose();
                    this.actual.onError(th);
                }
                return false;
            }
        }

        public void dispose() {
            this.s.dispose();
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void onComplete() {
            Collection collection = this.buffer;
            this.buffer = null;
            if (collection != null && !collection.isEmpty()) {
                this.actual.onNext(collection);
            }
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.buffer = null;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            Collection collection = this.buffer;
            if (collection != null) {
                collection.add(obj);
                int i = this.size + 1;
                this.size = i;
                if (i >= this.count) {
                    this.actual.onNext(collection);
                    this.size = 0;
                    createBuffer();
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    final class BufferSkipObserver extends AtomicBoolean implements Observer, Disposable {
        private static final long serialVersionUID = -8223395059921494546L;
        final Observer actual;
        final Callable bufferSupplier;
        final ArrayDeque buffers = new ArrayDeque();
        final int count;
        long index;
        Disposable s;
        final int skip;

        BufferSkipObserver(Observer observer, int i, int i2, Callable callable) {
            this.actual = observer;
            this.count = i;
            this.skip = i2;
            this.bufferSupplier = callable;
        }

        public void dispose() {
            this.s.dispose();
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void onComplete() {
            while (!this.buffers.isEmpty()) {
                this.actual.onNext(this.buffers.poll());
            }
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.buffers.clear();
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            long j = this.index;
            this.index = 1 + j;
            if (j % ((long) this.skip) == 0) {
                try {
                    Object call = this.bufferSupplier.call();
                    ObjectHelper.requireNonNull(call, "The bufferSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.");
                    this.buffers.offer((Collection) call);
                } catch (Throwable th) {
                    this.buffers.clear();
                    this.s.dispose();
                    this.actual.onError(th);
                    return;
                }
            }
            Iterator it = this.buffers.iterator();
            while (it.hasNext()) {
                Collection collection = (Collection) it.next();
                collection.add(obj);
                if (this.count <= collection.size()) {
                    it.remove();
                    this.actual.onNext(collection);
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableBuffer(ObservableSource observableSource, int i, int i2, Callable callable) {
        super(observableSource);
        this.count = i;
        this.skip = i2;
        this.bufferSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        int i = this.skip;
        int i2 = this.count;
        if (i == i2) {
            BufferExactObserver bufferExactObserver = new BufferExactObserver(observer, i2, this.bufferSupplier);
            if (bufferExactObserver.createBuffer()) {
                this.source.subscribe(bufferExactObserver);
                return;
            }
            return;
        }
        this.source.subscribe(new BufferSkipObserver(observer, i2, i, this.bufferSupplier));
    }
}
