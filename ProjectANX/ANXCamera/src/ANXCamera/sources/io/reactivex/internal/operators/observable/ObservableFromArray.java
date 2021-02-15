package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.Nullable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.BasicQueueDisposable;

public final class ObservableFromArray extends Observable {
    final Object[] array;

    final class FromArrayDisposable extends BasicQueueDisposable {
        final Observer actual;
        final Object[] array;
        volatile boolean disposed;
        boolean fusionMode;
        int index;

        FromArrayDisposable(Observer observer, Object[] objArr) {
            this.actual = observer;
            this.array = objArr;
        }

        public void clear() {
            this.index = this.array.length;
        }

        public void dispose() {
            this.disposed = true;
        }

        public boolean isDisposed() {
            return this.disposed;
        }

        public boolean isEmpty() {
            return this.index == this.array.length;
        }

        @Nullable
        public Object poll() {
            int i = this.index;
            Object[] objArr = this.array;
            if (i == objArr.length) {
                return null;
            }
            this.index = i + 1;
            Object obj = objArr[i];
            ObjectHelper.requireNonNull(obj, "The array element is null");
            return obj;
        }

        public int requestFusion(int i) {
            if ((i & 1) == 0) {
                return 0;
            }
            this.fusionMode = true;
            return 1;
        }

        /* access modifiers changed from: 0000 */
        public void run() {
            Object[] objArr = this.array;
            int length = objArr.length;
            for (int i = 0; i < length && !isDisposed(); i++) {
                Object obj = objArr[i];
                if (obj == null) {
                    Observer observer = this.actual;
                    StringBuilder sb = new StringBuilder();
                    sb.append("The ");
                    sb.append(i);
                    sb.append("th element is null");
                    observer.onError(new NullPointerException(sb.toString()));
                    return;
                }
                this.actual.onNext(obj);
            }
            if (!isDisposed()) {
                this.actual.onComplete();
            }
        }
    }

    public ObservableFromArray(Object[] objArr) {
        this.array = objArr;
    }

    public void subscribeActual(Observer observer) {
        FromArrayDisposable fromArrayDisposable = new FromArrayDisposable(observer, this.array);
        observer.onSubscribe(fromArrayDisposable);
        if (!fromArrayDisposable.fusionMode) {
            fromArrayDisposable.run();
        }
    }
}
