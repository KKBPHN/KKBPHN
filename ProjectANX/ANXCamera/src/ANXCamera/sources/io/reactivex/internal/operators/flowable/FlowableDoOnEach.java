package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscribers.BasicFuseableConditionalSubscriber;
import io.reactivex.internal.subscribers.BasicFuseableSubscriber;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;

public final class FlowableDoOnEach extends AbstractFlowableWithUpstream {
    final Action onAfterTerminate;
    final Action onComplete;
    final Consumer onError;
    final Consumer onNext;

    final class DoOnEachConditionalSubscriber extends BasicFuseableConditionalSubscriber {
        final Action onAfterTerminate;
        final Action onComplete;
        final Consumer onError;
        final Consumer onNext;

        DoOnEachConditionalSubscriber(ConditionalSubscriber conditionalSubscriber, Consumer consumer, Consumer consumer2, Action action, Action action2) {
            super(conditionalSubscriber);
            this.onNext = consumer;
            this.onError = consumer2;
            this.onComplete = action;
            this.onAfterTerminate = action2;
        }

        public void onComplete() {
            if (!this.done) {
                try {
                    this.onComplete.run();
                    this.done = true;
                    this.actual.onComplete();
                    try {
                        this.onAfterTerminate.run();
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        RxJavaPlugins.onError(th);
                    }
                } catch (Throwable th2) {
                    fail(th2);
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            boolean z = true;
            this.done = true;
            try {
                this.onError.accept(th);
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                this.actual.onError(new CompositeException(th, th2));
                z = false;
            }
            if (z) {
                this.actual.onError(th);
            }
            try {
                this.onAfterTerminate.run();
            } catch (Throwable th3) {
                Exceptions.throwIfFatal(th3);
                RxJavaPlugins.onError(th3);
            }
        }

        public void onNext(Object obj) {
            ConditionalSubscriber conditionalSubscriber;
            if (!this.done) {
                if (this.sourceMode != 0) {
                    conditionalSubscriber = this.actual;
                    obj = null;
                } else {
                    try {
                        this.onNext.accept(obj);
                        conditionalSubscriber = this.actual;
                    } catch (Throwable th) {
                        fail(th);
                        return;
                    }
                }
                conditionalSubscriber.onNext(obj);
            }
        }

        @Nullable
        public Object poll() {
            try {
                Object poll = this.qs.poll();
                if (poll != null) {
                    try {
                        this.onNext.accept(poll);
                    } catch (Throwable th) {
                        throw new CompositeException(th, th);
                    }
                } else {
                    if (this.sourceMode == 1) {
                        this.onComplete.run();
                    }
                    return poll;
                }
                this.onAfterTerminate.run();
                return poll;
            } catch (Throwable th2) {
                throw new CompositeException(th, th2);
            }
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }

        public boolean tryOnNext(Object obj) {
            if (this.done) {
                return false;
            }
            try {
                this.onNext.accept(obj);
                return this.actual.tryOnNext(obj);
            } catch (Throwable th) {
                fail(th);
                return false;
            }
        }
    }

    final class DoOnEachSubscriber extends BasicFuseableSubscriber {
        final Action onAfterTerminate;
        final Action onComplete;
        final Consumer onError;
        final Consumer onNext;

        DoOnEachSubscriber(Subscriber subscriber, Consumer consumer, Consumer consumer2, Action action, Action action2) {
            super(subscriber);
            this.onNext = consumer;
            this.onError = consumer2;
            this.onComplete = action;
            this.onAfterTerminate = action2;
        }

        public void onComplete() {
            if (!this.done) {
                try {
                    this.onComplete.run();
                    this.done = true;
                    this.actual.onComplete();
                    try {
                        this.onAfterTerminate.run();
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        RxJavaPlugins.onError(th);
                    }
                } catch (Throwable th2) {
                    fail(th2);
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            boolean z = true;
            this.done = true;
            try {
                this.onError.accept(th);
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                this.actual.onError(new CompositeException(th, th2));
                z = false;
            }
            if (z) {
                this.actual.onError(th);
            }
            try {
                this.onAfterTerminate.run();
            } catch (Throwable th3) {
                Exceptions.throwIfFatal(th3);
                RxJavaPlugins.onError(th3);
            }
        }

        public void onNext(Object obj) {
            Subscriber subscriber;
            if (!this.done) {
                if (this.sourceMode != 0) {
                    subscriber = this.actual;
                    obj = null;
                } else {
                    try {
                        this.onNext.accept(obj);
                        subscriber = this.actual;
                    } catch (Throwable th) {
                        fail(th);
                        return;
                    }
                }
                subscriber.onNext(obj);
            }
        }

        @Nullable
        public Object poll() {
            try {
                Object poll = this.qs.poll();
                if (poll != null) {
                    try {
                        this.onNext.accept(poll);
                    } catch (Throwable th) {
                        throw new CompositeException(th, th);
                    }
                } else {
                    if (this.sourceMode == 1) {
                        this.onComplete.run();
                    }
                    return poll;
                }
                this.onAfterTerminate.run();
                return poll;
            } catch (Throwable th2) {
                throw new CompositeException(th, th2);
            }
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }
    }

    public FlowableDoOnEach(Flowable flowable, Consumer consumer, Consumer consumer2, Action action, Action action2) {
        super(flowable);
        this.onNext = consumer;
        this.onError = consumer2;
        this.onComplete = action;
        this.onAfterTerminate = action2;
    }

    /* JADX WARNING: type inference failed for: r7v0, types: [io.reactivex.FlowableSubscriber] */
    /* JADX WARNING: type inference failed for: r1v0, types: [io.reactivex.internal.operators.flowable.FlowableDoOnEach$DoOnEachSubscriber] */
    /* JADX WARNING: type inference failed for: r1v1, types: [io.reactivex.internal.operators.flowable.FlowableDoOnEach$DoOnEachConditionalSubscriber] */
    /* JADX WARNING: type inference failed for: r1v2, types: [io.reactivex.internal.operators.flowable.FlowableDoOnEach$DoOnEachSubscriber] */
    /* JADX WARNING: type inference failed for: r1v3, types: [io.reactivex.internal.operators.flowable.FlowableDoOnEach$DoOnEachConditionalSubscriber] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r1v2, types: [io.reactivex.internal.operators.flowable.FlowableDoOnEach$DoOnEachSubscriber]
  assigns: [io.reactivex.internal.operators.flowable.FlowableDoOnEach$DoOnEachSubscriber, io.reactivex.internal.operators.flowable.FlowableDoOnEach$DoOnEachConditionalSubscriber]
  uses: [io.reactivex.internal.operators.flowable.FlowableDoOnEach$DoOnEachSubscriber, io.reactivex.FlowableSubscriber, io.reactivex.internal.operators.flowable.FlowableDoOnEach$DoOnEachConditionalSubscriber]
  mth insns count: 21
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void subscribeActual(Subscriber subscriber) {
        ? r7;
        Flowable flowable;
        if (subscriber instanceof ConditionalSubscriber) {
            flowable = this.source;
            ? doOnEachConditionalSubscriber = new DoOnEachConditionalSubscriber((ConditionalSubscriber) subscriber, this.onNext, this.onError, this.onComplete, this.onAfterTerminate);
            r7 = doOnEachConditionalSubscriber;
        } else {
            flowable = this.source;
            ? doOnEachSubscriber = new DoOnEachSubscriber(subscriber, this.onNext, this.onError, this.onComplete, this.onAfterTerminate);
            r7 = doOnEachSubscriber;
        }
        flowable.subscribe((FlowableSubscriber) r7);
    }
}
